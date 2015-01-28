

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestZip {

	private static final String ZIPNAME = "HAWKEYE74.7z";
	private static final String ZIPAPPLI = "c:/Program Files/7-Zip/7z.exe";
	private static final String [] EXEC_CMDARRAY = {ZIPAPPLI, "a", ZIPNAME, "HAWKEYE74"};
	private static final String [] EXEC_ENVP = null;
	private static final String EXEC_DIR = "C:/wow/WTF/Account";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File wowrep = new File(EXEC_DIR);
		if (!wowrep.isDirectory()) {
			System.out.println(EXEC_DIR + " n'est pas un repertoire.");
			return;
		}
		File zipfile = new File(wowrep, ZIPNAME);
		if (zipfile.exists()) {
			System.out.println(ZIPNAME + " existe. Effacement ...");
			if (!zipfile.delete()) {
				System.out.println(ZIPNAME + " n'a pas pu etre efface !");
				return;
			}
			System.out.println(" efface.");
		}
		Runtime rt = Runtime.getRuntime();
		try {
			String line;

			System.out.println("Creation de l'archive ...\n");
			Process process = rt.exec(EXEC_CMDARRAY, EXEC_ENVP, wowrep);
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((line = input.readLine()) != null) {
				System.out.println(line);
			}
			input.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
