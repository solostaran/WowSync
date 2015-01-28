

import java.lang.reflect.InvocationTargetException;


public class TestRegWow {

	public static final String ZIP_NODE = "SOFTWARE\\7-Zip";
	public static final String ZIP_KEY = "Path";
	public static final String WOW_KEY = "SOFTWARE\\Blizzard Entertainment\\World of Warcraft\\InstallPath";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String value;
		try {
			// IE Download directory (HKEY_CURRENT_USER)
			value = WinRegistry.readString(
					WinRegistry.HKEY_CURRENT_USER,
					"Software\\Microsoft\\Internet Explorer",
			"Download Directory");
			System.out.println("IE Download directory = " + value);

			// Query for Acrobat Reader installation path (HKEY_LOCAL_MACHINE)
			value = WinRegistry.readString(
					WinRegistry.HKEY_LOCAL_MACHINE,
					"SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\App Paths\\AcroRd32.exe",
			"");
			System.out.println("Acrobat Reader Path = " + value);

			// 7-Zip path
			value = WinRegistry.readString(
					WinRegistry.HKEY_LOCAL_MACHINE,
					"SOFTWARE\\7-Zip",
					"Path");
			System.out.println("7-Zip installation path = " + (value == null ? "cle inaccessible" : value));

			// Wow Path
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
