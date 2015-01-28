


public class WinRegistryTest {
	  public static void main(String args[]) throws Exception {
	      String value = "";

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

	      // Loop through installed JRE and print the JAVA_HOME value
	      // HKEY_LOCAL_MACHINE\SOFTWARE\JavaSoft\Java Runtime Environment
	      java.util.Map<String, String> res1 = WinRegistry.readStringValues(
	          WinRegistry.HKEY_LOCAL_MACHINE, 
	          "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion");
	      System.out.println(res1.toString());
	      
	      java.util.List<String> res2 = WinRegistry.readStringSubKeys(
	          WinRegistry.HKEY_LOCAL_MACHINE,
	          "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion");
	      System.out.println(res2.toString());

//	      WinRegistry.createKey(
//	          WinRegistry.HKEY_CURRENT_USER, "SOFTWARE\\rgagnon.com");
//	      WinRegistry.writeStringValue(
//	          WinRegistry.HKEY_CURRENT_USER, 
//	          "SOFTWARE\\rgagnon.com", 
//	          "HowTo", 
//	          "java");
	      
//	      WinRegistry.deleteValue(
//	          WinRegistry.HKEY_CURRENT_USER, 
//	          "SOFTWARE\\rgagnon.com", "HowTo");
//	      WinRegistry.deleteKey(
//	          WinRegistry.HKEY_CURRENT_USER, 
//	          "SOFTWARE\\rgagnon.com\\");
	      
	      System.out.println("Done." );
	  }
	}