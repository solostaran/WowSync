package wowsync;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class WowSyncZip {
	
	private WowSyncModel wsm;
	
	public WowSyncZip(WowSyncModel wsm) {
		this.wsm = wsm;
	}
	
	public static File addBackupExtension(File f) {
		return new File(f.getParentFile(), f.getName()+".bak");
	}
	
	/**
	 * Write a datetime to a file
	 * @param f file reference
	 * @param ltime time in the form milliseconds to Epoch
	 * @return true if the operation is complete, else false
	 */
	public boolean writeDatetime(File f, long ltime) {
		try {
			FileWriter fw = new FileWriter(f);
			fw.write(""+ltime);
			fw.close();
		} catch(IOException e) {
			System.out.println("Error writing the datetime file : "+e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * Get a datetime from a file
	 * @param f file reference
	 * @return time in the form of milliseconds to Epoch, 0 if a problem occurred
	 */
	public long readDatetime(File f) {
		long ltime = 0;
		try {
			FileReader fw = new FileReader(f);
			BufferedReader br = new BufferedReader(fw);
			String str = br.readLine();
			ltime = Long.parseLong(str);
			if (WowSync.DEBUG) System.out.println("DEBUG: ReadDateTime="+ltime);
			br.close();
		} catch(IOException e) {
			System.out.println("Error reading the datetime file : "+e.getMessage());
			return 0;
		}
		return ltime;
	}
	
	/**
	 * Zip the account with a system command to the referenced archiver.
	 */
	public boolean launchZipAccount() {
		// Get where the zip should be created
		File wowrep = new File(wsm.getLocaleWTFAccountPath());
		if (!wowrep.isDirectory()) {
			System.out.println(wowrep + " is not a directory.");
			return false;
		}
		
		// Get the absolute zip pathname
		File zipfile = new File(wowrep, wsm.getArchiveName());
		if (zipfile.exists()) {
			// backup the old file if needed or delete it if not backed up
			if (wsm.isArchiveBackup()) {
				File backup = addBackupExtension(zipfile);
				File bbackup = addBackupExtension(backup);
				if (bbackup.exists()) bbackup.delete();
				if (backup.exists()) backup.renameTo(bbackup);
				if (new File(zipfile.toURI()).renameTo(backup))	bbackup.delete();
			} else if (!zipfile.delete()) {
				System.out.println(wsm.getArchiveName() + " could not be deleted !");
				return false;
			}
		}
		
		// Run the zip command
		Runtime rt = Runtime.getRuntime();
		try {
			//String line;
			String [] aec = wsm.getArchiverAddCommand().split("\\s");
			String [] exec_cmdarray = new String[3+aec.length];
			exec_cmdarray[0] = wsm.getArchiverExecPath();
			int i = 1;
			for (; i <= aec.length; i++)
				exec_cmdarray[i] = aec[i-1];
			exec_cmdarray[i++] = wsm.getArchiveName();
			exec_cmdarray[i] = wsm.getWowSyncAccount();
			Process process = rt.exec(exec_cmdarray, null, wowrep);
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while (input.readLine() != null) {
			//while ((line = input.readLine()) != null) {
				//System.out.println(line);
				//System.out.print(".");
			}
			input.close();
			
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		
		if (!zipfile.exists()) {
			System.out.println("Zip error !");
			return false;
		}
		
		
		long localtime = wsm.getLocaleAccountMTime();
		// Modify the zip time & date (WARNING --- to be replaced by a file with the time & date)
		//zipfile.setLastModified(localtime);
		
		// Create datetime file
		File f = new File(wowrep, wsm.getArchiveDateTimeName());
		writeDatetime(f, localtime);
		
		return true;
	}
	
	/**
	 * Unzip the account with a system command to the archiver.
	 * @return true if ok, false else
	 */
	public boolean launchUnzipAccount() {
		File wowrep = new File(wsm.getLocaleWTFAccountPath());
		File zipfile = new File(wowrep, wsm.getArchiveName());
		if (!zipfile.exists()) {
			System.out.println("Error no archive to unzip !");
			return false;
		}
		
		Runtime rt = Runtime.getRuntime();
		try {
			//String line;
			String [] aec = wsm.getArchiverExtractCommand().split("\\s");
			String [] exec_cmdarray = new String[2+aec.length];
			exec_cmdarray[0] = wsm.getArchiverExecPath();
			int i = 1;
			for (; i <= aec.length; i++)
				exec_cmdarray[i] = aec[i-1];
			exec_cmdarray[i] = wsm.getArchiveName();
			Process process = rt.exec(exec_cmdarray, null, wowrep);
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while (input.readLine() != null) {
			//while ((line = input.readLine()) != null) {
				//System.out.println(line);
				//System.out.print(".");
			}
			input.close();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WowSyncModel wsm = new WowSyncModel();
		wsm.ReadConfigurationFile();
		//WowSyncZip wsz = new WowSyncZip(wsm);
		//System.out.println("Creation de l'archive ...");
		//wsz.launchUnzipAccount();
		//System.out.println(" ... archive cree.");
	}

}
