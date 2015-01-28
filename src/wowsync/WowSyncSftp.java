package wowsync;

import java.io.File;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;
import com.jcraft.jsch.UserInfo;

/**
 * Wrapping for the JSCH API (SFTP).
 * @author reynaud
 *
 */
public class WowSyncSftp {

	public final static String KNOWN_HOSTS = "known_hosts";

	private WowSyncModel wsm;
	private boolean connected;
	private Session session;
	private ChannelSftp cs;

	/**
	 * @param wsm WowSync configuration
	 */
	public WowSyncSftp(WowSyncModel wsm) {
		this.wsm = wsm;
	}

	/**
	 * @return true = connected to the SSH server
	 */
	public boolean isConnected() {
		return session.isConnected();
	}

	/**
	 * Sftp session connection.<br/>
	 * Use the password authentication as well as the private key authentication.<br/>
	 * The User Info depends on the type of authentication.<br/>
	 * Create a 'known_hosts' file locally (launch directory).
	 */
	public void connect() {
		try {
			if (WowSync.DEBUG) System.out.println("DEBUG: Connexion ...");
			JSch jsch=new JSch();
			if (wsm.isSshStoreKey())
				jsch.setKnownHosts("./"+KNOWN_HOSTS);
			if (wsm.isSshPrivateKeyAuthentication()) {
				// Private Key authentication
				jsch.addIdentity(wsm.getSshPrivateKeyFile());
			}
			session=jsch.getSession(wsm.getSshLogin(), wsm.getSshServerAddress(), wsm.getSshServerPort());
			UserInfo ui = wsm.isSshPrivateKeyAuthentication() ? new WowSyncUserInfo2() : new WowSyncUserInfo(wsm);
			session.setUserInfo(ui);
			session.connect();
			Channel channel=session.openChannel("sftp");
			channel.connect();
			cs=(ChannelSftp)channel;
			if (WowSync.DEBUG) System.out.println("DEBUG: ... Connected.");
			connected = true;
		} catch (JSchException e) {
			System.out.println("No connexion established : "+e.getMessage());
		}
	}

	/**
	 * sftp session connexion with private key authentication.
	 * WARNING *** actually just for test purpose ***
	 * private key authentication and password authentication are in the "connect" method.
	 */
	public void connectPrivate() {
		try{
			if (WowSync.DEBUG) System.out.println("DEBUG: Connexion ...");
			JSch jsch=new JSch();
			if (wsm.isSshStoreKey())
				jsch.setKnownHosts("./"+KNOWN_HOSTS);
			// -- specific private key authentication
			File f = new File("c:/Users/reynaud/gulltopp.ssh");
			jsch.addIdentity(f.getAbsolutePath() //,"passphrase"
					);
			// -- end of specific
			session=jsch.getSession("cpatrigeon", "gulltopp.vernois.net", 22);

			// username and passphrase will be given via UserInfo interface.
			UserInfo ui=new WowSyncUserInfo2();
			session.setUserInfo(ui);
			session.connect();
			
			if (WowSync.DEBUG) {
				System.out.println("DEBUG: Client version = "+session.getClientVersion());
				System.out.println("DEBUG: Server version = "+session.getServerVersion());
			}

//			Channel channel=session.openChannel("shell");
//			channel.setInputStream(System.in);
//			channel.setOutputStream(System.out);
			
			Channel channel=session.openChannel("sftp");
			channel.connect();
			cs=(ChannelSftp)channel;
			if (WowSync.DEBUG) {
				System.out.println("DEBUG: Server home = "+cs.getHome());
				System.out.println("DEBUG: ... Connected.");
			}
			connected = true;
		}
		catch(Exception e){
			System.out.println("No connexion established : "+e.getMessage());
		}
	}

	/**
	 * Sftp session disconnection.
	 */
	public void disconnect() {
		cs.exit();
		session.disconnect();
		if (WowSync.DEBUG) System.out.println("DEBUG: Disconnected.");
	}

	/**
	 * Return the filesystem attributes of a file.
	 * @param filename
	 * @return file attributes (null on error)
	 */
	public SftpATTRS getStat(String filename) {
		if (!connected) return null;
		try {
			return cs.stat(filename);
		} catch (SftpException e) {
			System.out.println(filename+" (sftp-stat) "+e.getMessage());
			return null;
		}
	}

	/**
	 * Find the last modification date/time of a file.
	 * The result is a millisecond offset from the Epoch, January 1, 1970 00:00:00.000 GMT (Gregorian)
	 * @param filename
	 * @return last modification date/time (-1 on error)
	 */
	public long getMTime(String filename) {
		SftpATTRS attrs = getStat(filename);
		if (attrs == null) return 0L;
		return ((long)attrs.getMTime())*1000;
	}

	/**
	 * Sftp put command
	 * from the local 'lpwd' path to the remote 'pwd' path
	 * @param filename local filename to put
	 * @return true if ok, false else.
	 */
	public boolean put(String filename) {
		if (!connected) return false;

		try {
			SftpProgressMonitor monitor=new WowSyncProgressMonitor();
			int mode=ChannelSftp.OVERWRITE;

			cs.put(filename, ".", monitor, mode);

		} catch (SftpException e) {
			System.out.println(filename+" (sftp-put) "+e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Sftp get command
	 * from the remote 'pwd' path to the local 'lpwd' path
	 * @param filename remote filename to get
	 * @return true if ok, false else.
	 */
	public boolean get(String filename)  {
		if (!connected) return false;

		try {
			SftpProgressMonitor monitor=new WowSyncProgressMonitor();
			int mode=ChannelSftp.OVERWRITE;

			cs.get(filename, filename, monitor, mode);

		} catch (SftpException e) {
			System.out.println(filename+" (sftp-get) "+e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Sftp cd command
	 * change the remote 'pwd' path
	 * @param path
	 * @return true if ok, false else.
	 */
	public boolean cd(String path) {
		if (!connected) return false;

		try {
			cs.cd(path);
		} catch (SftpException e) {
			System.out.println(path+" (sftp-cd) "+e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Sftp lcd command
	 * change the local 'lpwd' path
	 * @param path
	 * @return true if ok, false else.
	 */
	public boolean lcd(String path) {
		if (!connected) return false;

		try {
			cs.lcd(path);
		} catch (SftpException e) {
			System.out.println(path+" (sftp-lcd) "+e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Entry point for testing purpose.
	 */
	public static void main(String[] args) {
		WowSync.DEBUG = true;
		
		WowSyncModel wsm = new WowSyncModel();
		wsm.ReadConfigurationFile();
		WowSyncSftp wss = new WowSyncSftp(wsm);
		wss.connectPrivate();
		
		if (wss.isConnected()) {
			System.out.println("Connexion established.");
			wss.disconnect();
		}
	}

}


