package wowsync;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.ImageIcon;

/**
 * The synchronization process.
 * @author reynaud
 */
public class WowSyncThread extends Thread {
	
	private WowSyncModel wsm;
	private WowSyncSftp wss;
	private WowSyncZip wsz;

	public WowSyncThread(WowSyncModel wsm) {
		super(WowSync.WOWSYNC);
		this.wsm = wsm;
		wss = new WowSyncSftp(wsm);
		wsz = new WowSyncZip(wsm);
	}
	
	/**
	 * Return an ImageIcon from a resource in a jar file or a local file.
	 * First check the jar file, then the local one.
	 * @param src image file path
	 * @return local resource image or local file or null
	 */
	public ImageIcon getImageIcon(String src) {
		ImageIcon ret = null;
		URL url = getClass().getResource(src);
		if (url == null) {
			if (src.startsWith("/")) src = src.substring(1);
			Image img = Toolkit.getDefaultToolkit().getImage(src);
			try {
				ret = new ImageIcon(img);
			} catch(Exception e) {}
		} else {
			try {
				ret = new ImageIcon(url);
			} catch(Exception e) {}
		}
		return ret;
	}
	
	/**
	 * The local to remote synchronization process.
	 */
	protected void syncLocal2Remote() {
		System.out.println("Sync from local to remote");

		System.out.print(" Zipping ... ");
		if (!wsz.launchZipAccount()) return;
		System.out.println("zipped.");

		System.out.print(" Transmitting to server ... ");
		if (!wss.put(wsm.getArchiveDateTimeName())) return;
		if (!wss.put(wsm.getLocaleArchivePath())) return;
		System.out.println("archive transfered.");
	}

	/**
	 * The remote to local synchronization process.
	 */
	protected void syncRemote2Local() {
		System.out.println("Sync from remote to local");
		
		// backup the old file if needed
		File wowrep = new File(wsm.getLocaleWTFAccountPath());
		File zipfile = new File(wowrep, wsm.getArchiveName());
		if (wsm.isArchiveBackup()) {
			File backup = WowSyncZip.addBackupExtension(zipfile);
			File bbackup = WowSyncZip.addBackupExtension(backup);
			if (bbackup.exists()) bbackup.delete();
			if (backup.exists()) backup.renameTo(bbackup);
			if (new File(zipfile.toURI()).renameTo(backup))	bbackup.delete();
		}
		
		System.out.print(" Transmitting from server ... ");
		if (!wss.get(wsm.getArchiveName())) return;
		System.out.println("archive transfered.");
		
		System.out.print(" Unzipping ...");
		if (!wsz.launchUnzipAccount()) return;
		System.out.println(" unzipped.");
	}
	
	/**
	 * Return the string representation of a long Epoch date.
	 * @param l the Epoch date
	 * @return String representation
	 */
	protected String strFormatDate(long l) {
		if (l == 0) return "no date";
		DateFormat df = DateFormat.getInstance();
		return df.format(new Date(l));
	}
	
	/**
	 * Get the remote datetime file and read the datetime.
	 * @return datetime read in the file 0 if error.
	 */
	private long getRemoteMTime() {
		File f = new File(wsm.getLocaleWTFAccountPath(), wsm.getArchiveDateTimeName());
		f.delete();
		if (wss.get(wsm.getArchiveDateTimeName()) == false) return 0;
		//System.out.println("getRemoteMTime get : "+f.getAbsolutePath()+", result="+ret);
		return wsz.readDatetime(f);
	}

	/**
	 * The thread execution.<br/>
	 * This a simple state machine.<br/>
	 * TODO: either a polling method or a real state machine ?
	 */
	public void run() {
		wss.connect();
		if (!wss.isConnected()) return;
		
		// place the Sftp session in the correct directories (local and remote)
		if (WowSync.DEBUG) System.out.println("DEBUG: Local Path = "+wsm.getLocaleWTFAccountPath());
		wss.lcd(wsm.getLocaleWTFAccountPath());
		if (WowSync.DEBUG) System.out.println("DEBUG: Remote Path = "+wsm.getSshServerPath());
		wss.cd(wsm.getSshServerPath());
		
		// Get the local and remote times
		long localTime = wsm.getLocaleAccountMTime();
		String strLocalTime = strFormatDate(localTime);
		long remoteTime = getRemoteMTime();
		String strRemoteTime = strFormatDate(remoteTime);
		String msg1 = "Local account: "+strLocalTime+" , remote file: "+strRemoteTime+" .";
		System.out.println(msg1);
		
		if ((localTime == 0) && (remoteTime == 0)) {
			msg1 = "No local account and no remote file !";
			System.out.println(msg1);
			wss.disconnect();
		}
		
		if (localTime == 0) {
			syncRemote2Local();
			wss.disconnect();
			return;
		}
		
		if (remoteTime == 0) {
			syncLocal2Remote();
			wss.disconnect();
			return;
		}
		
		// Original test (localTime == remoteTime)
		// This is made for @&$£% file system optimizations and differences in Epoch relative date & times
		// Thanks to Tok and his marvelous server
		if (Math.abs(localTime - remoteTime) <= 2000) {
			String msg2 = "Local and remote are the same !";
			System.out.println(msg2);
			ImageIcon img1 = getImageIcon("/images/wow2serv.png");
			ImageIcon img2 = getImageIcon("/images/serv2wow.png");
			int ret = WowSyncDialog.showWowEqualQuestion(WowSync.WOWSYNC, msg1, msg2, img1, img2);
			if (ret == WowSyncDialog.WOWSYNCDIALOG_CHOICE1) {
				syncLocal2Remote();
			}
			if (ret == WowSyncDialog.WOWSYNCDIALOG_CHOICE2) {
				syncRemote2Local();
			}
			wss.disconnect();
			//JOptionPane.showMessageDialog(null, msg2, WowSync.WOWSYNC, JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		// Determine the sync behavior
		// if the case is 0 then it will do 'case 0' and 'case 1'
		long i = localTime - remoteTime;
		int yesno;
		switch (wsm.getSyncBehavior()) {
		case 0:  // Ask every sync
			
			String msg2;
			ImageIcon img1 = null;
			ImageIcon img2 = null;
			if (i > 0) {
				msg2 = "Remote file out of date, save your account to the server ?";
				img1 = getImageIcon("/images/wow2serv.png");
				img2 = getImageIcon("/images/serv2wow.png");
			} else {
				msg2 = "Local account out of date, update your account from the server ?";
				img1 = getImageIcon("/images/serv2wow.png");
				img2 = getImageIcon("/images/wow2serv.png");
			}
			yesno = WowSyncDialog.showWowSyncQuestion(WowSync.WOWSYNC, msg1, msg2, img1, img2);
			if (yesno == WowSyncDialog.WOWSYNCDIALOG_NOACTION) break;
			if (yesno == WowSyncDialog.WOWSYNCDIALOG_CHOICE2) i = -i;
			if (!wss.isConnected()) {
				System.out.println("Connexion lost !");
				return;
			}
			// no break here --> case 1
			
		case 1:  // Auto sync
			if (i > 0)
				syncLocal2Remote();
			else
				syncRemote2Local();
			break;
			
		case 2:  // local to server (if needed)
			if (i > 0) syncLocal2Remote();
			break;
			
		case 3:  // server to local (if needed)
			if (i < 0) syncRemote2Local();
			break;
			
		case 4:  // force local to server
			syncLocal2Remote();
			break;
			
		case 5:  // force server to local
			syncRemote2Local();
			break;
		}
		
		wss.disconnect();
	}
	
	/**
	 * Entry point for testing purpose.
	 */
	public static void main(String [] args) {
		WowSyncModel wsm = new WowSyncModel();
		wsm.ReadConfigurationFile();
		WowSyncThread wst = new WowSyncThread(wsm);
		//wst.start();
		ImageIcon img1 = wst.getImageIcon("/images/serv2wow.png");
		ImageIcon img2 = wst.getImageIcon("/images/wow2serv.png");
		int ret = WowSyncDialog.showWowEqualQuestion(WowSync.WOWSYNC, "ms1", "msg2", img1, img2);
		System.out.println("Ret="+ret);
	}
}
