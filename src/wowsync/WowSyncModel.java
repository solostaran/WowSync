package wowsync;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.IllegalDataException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class WowSyncModel {
	protected static final String CONFIG_FILENAME = "wowsync.xml";
	protected static final String KNOWN_HOSTS_FILENAME = "known_hosts";
	protected static final String WOW_WTF_ACCOUNT = "WTF/Account";
	protected static final String WOW_ACCOUNT_MOST_RECENT_FILE = "cache.md5";
	protected static final String ARCHIVE_DATETIMEEXT = ".datetime";

	protected static final String XML_ROOT = "WowSync";
	
	protected static final String XML_VERSION = "Version";

	protected static final String XML_ARCHIVER = "Archiver";
	protected static final String XML_ARCHIVER_EXECPATH = "ExecPath";
	protected static final String XML_ARCHIVER_ARCHIVEADD = "ArchiveAdd";
	protected static final String XML_ARCHIVER_ARCHIVEEXTRACT = "ArchiveExtract";
	protected static final String XML_ARCHIVER_ARCHIVEEXT = "ArchiveExtension";
	protected static final String XML_ARCHIVER_ARCHIVEBACKUP = "ArchiveBackup"; // boolean

	protected static final String XML_WOW = "WoW";
	protected static final String XML_WOW_INSTALLPATH = "InstallPath";
	protected static final String XML_WOW_SYNCACCOUNT = "SyncAccount";
	
	protected static final String XML_SSHACCOUNT_ACTIVE = "ActiveSshAccount";
//	protected static final String XML_SSHACCOUNT = "SshAccount";
//	protected static final String XML_SSHACCOUNT_SERVERADDRESS = "ServerAddress";
//	protected static final String XML_SSHACCOUNT_SERVERPORT = "ServerPort";
//	protected static final String XML_SSHACCOUNT_LOGIN = "Login";
//	protected static final String XML_SSHACCOUNT_STOREPASSWORD = "StorePassword"; // boolean
//	protected static final String XML_SSHACCOUNT_PASSWORD = "Password";
//	protected static final String XML_SSHACCOUNT_STOREKEY = "StoreKey"; //boolean
//	protected static final String XML_SSHACCOUNT_KEYSTORED = "KeyIsStored"; // boolean
//	protected static final String XML_SSHACCOUNT_SERVERKEY = "Key";
//	protected static final String XML_SSHACCOUNT_SERVERKEYTYPE = "KeyType";
//	protected static final String XML_SSHACCOUNT_SERVERPATH = "ServerPath";
//	protected static final String XML_SSHACCOUNT_PRIVATEKEYAUTHENTICATION = "PrivateKeyAuthentication";
//	protected static final String XML_SSHACCOUNT_PRIVATEKEYFILE = "PrivateKeyFile";

	protected static final String XML_SYNC = "Sync";
	protected static final String XML_SYNC_BEHAVIOR = "SyncBehavior"; // int
	protected static final String XML_SYNC_BEHAVIOR_TEXT = "SyncBehaviorComment";
	protected static final String [] XML_SYNC_BEHAVIOR_TAB = {
		"Ask every sync",
		"Sync automatically (most recent replace older)",
		"Sync local to server (if needed)",
		"Sync server to local (if needed)",
		"Force local to server",
		"Force server to local"
	};

	protected static final String MSG_CORRUPTED_CONFIG= "The configuration file is corrupted !";
	
	private transient boolean configLoaded = false;
	
	private String archiverExecPath = "c:\\Program Files\\7-Zip\\7z.exe";
	private String archiverAddCommand = "a";
	private String archiverExtractCommand = "x -y";
	private String archiveExtension = "7z";
	private boolean archiveBackup = true;
	
	private String wowInstallPath = "c:\\World of Warcraft";
	private String wowSyncAccount;
	private transient boolean wowAccounts = false;
	private transient Vector<String> tabAccounts = null;
	
	private ArrayList<SshServerInfo> sshServers;
	private int activeSshServerIndex;
//	private String sshServerAddress = "myserver";
//	private int sshServerPort = 22;
//	private String sshLogin = "mylogin";
//	private boolean sshStorePassword = false;
//	private String sshPassword;
//	private boolean sshStoreKey = true;
//	private boolean sshKeyStored = false;
//	private transient String sshServerKey; // non utilisé actuellement
//	private transient String sshServerKeyType; // non utilisé actuellement
//	private String sshServerPath = ".";
//	private boolean sshPrivateKeyAuthentication = false;
//	private String sshPrivateKeyFile = null;
	
	private int syncBehavior = 0;

	// ********** ACCESSORS ***********
	
	public boolean isConfigLoaded() {
		return configLoaded;
	}

	public void setArchiverExecPath(String archiverExecPath) {
		this.archiverExecPath = archiverExecPath;
	}

	public String getArchiverExecPath() {
		return archiverExecPath;
	}

	public void setArchiverAddCommand(String archiverAddCommand) {
		this.archiverAddCommand = archiverAddCommand;
	}

	public String getArchiverAddCommand() {
		return archiverAddCommand;
	}

	public void setArchiverExtractCommand(String archiverExtractCommand) {
		this.archiverExtractCommand = archiverExtractCommand;
	}

	public String getArchiverExtractCommand() {
		return archiverExtractCommand;
	}

	public void setArchiveExtension(String archiveExtension) {
		this.archiveExtension = archiveExtension;
	}

	public String getArchiveExtension() {
		return archiveExtension;
	}

	public void setArchiveBackup(boolean archiveBackup) {
		this.archiveBackup = archiveBackup;
	}

	public boolean isArchiveBackup() {
		return archiveBackup;
	}

	public void setWowInstallPath(String wowInstallPath) {
		this.wowInstallPath = wowInstallPath;
		ListWowAccounts();
	}

	public String getWowInstallPath() {
		return wowInstallPath;
	}

	public void setWowSyncAccount(String wowSyncAccount) {
		this.wowSyncAccount = wowSyncAccount.toUpperCase();
	}

	public String getWowSyncAccount() {
		return wowSyncAccount;
	}

	public boolean isWowAccounts() {
		return wowAccounts;
	}
	
	public Vector<String> getWowAccounts() {
		return tabAccounts;
	}

	public void setSshServers(ArrayList<SshServerInfo> sshServers) {
		this.sshServers = sshServers;
	}
	
	public ArrayList<SshServerInfo> getSshServers() {
		return sshServers;
	}

	public void setSshServerAddress(String sshServerAddress) {
		getActiveSshServer().setServerAddress(sshServerAddress);
	}

	public String getSshServerAddress() {
		return getActiveSshServer().getServerAddress();
	}

	public void setSshServerPort(int sshServerPort) {
		getActiveSshServer().setServerPort(sshServerPort);
	}

	public int getSshServerPort() {
		return getActiveSshServer().getServerPort();
	}

	public void setSshLogin(String sshLogin) {
		getActiveSshServer().setLogin(sshLogin);
	}

	public String getSshLogin() {
		return getActiveSshServer().getLogin();
	}

	public void setSshStorePass(boolean sshStorePass) {
		getActiveSshServer().setStorePassword(sshStorePass);
	}

	public boolean isSshStorePassword() {
		return getActiveSshServer().isStorePassword();
	}

	public void setSshPassword(String sshPassword) {
		getActiveSshServer().setPassword(sshPassword);
	}

	public String getSshPassword() {
		return getActiveSshServer().getPassword();
	}

	public void setSshStoreKey(boolean sshStoreKey) {
		getActiveSshServer().setStoreKey(sshStoreKey);
	}

	public boolean isSshStoreKey() {
		return getActiveSshServer().isStoreKey();
	}

//	private void setSshKeyStored(boolean sshKeyStored) {
//		this.sshKeyStored = sshKeyStored;
//	}

//	public boolean isSshKeyStored() {
//		return sshKeyStored;
//	}

//	private void setSshServerKey(String sshServerKey) {
//		this.sshServerKey = sshServerKey;
//	}

//	public String getSshServerKey() {
//		return sshServerKey;
//	}

//	public void setSshServerKeyType(String sshServerKeyType) {
//		this.sshServerKeyType = sshServerKeyType;
//	}

//	public String getSshServerKeyType() {
//		return sshServerKeyType;
//	}

	public void setSshServerPath(String sshServerPath) {
		getActiveSshServer().setServerPath(sshServerPath);
	}

	public String getSshServerPath() {
		return getActiveSshServer().getServerPath();
	}

	public void setSshPrivateKeyAuthentication(boolean sshPrivateKeyAuthentication) {
		getActiveSshServer().setAuthenticationKey(sshPrivateKeyAuthentication);
	}

	public boolean isSshPrivateKeyAuthentication() {
		return getActiveSshServer().isAuthenticationKey();
	}

	public void setSshPrivateKeyFile(String sshPrivateKeyFile) {
		getActiveSshServer().setAuthenticationKeyFile(sshPrivateKeyFile);
	}

	public String getSshPrivateKeyFile() {
		return getActiveSshServer().getAuthenticationKeyFile();
	}

	public void setSyncBehavior(int syncBehavior) {
		this.syncBehavior = syncBehavior;
	}

	public int getSyncBehavior() {
		return syncBehavior;
	}
	
	public int getSyncBehaviorCount() {
		return XML_SYNC_BEHAVIOR_TAB.length;
	}
	
	public String getSyncBehaviorText() {
		return XML_SYNC_BEHAVIOR_TAB[syncBehavior];
	}
	
	public String getSyncBehaviorText(int i) {
		return XML_SYNC_BEHAVIOR_TAB[i];
	}
	
	/**
	 * Return the name of the archive file.
	 * (Account Name + "." + Archive Extension)
	 * @return archive filename
	 */
	public String getArchiveName() {
		if (!isWowAccounts()) return null;
		return getWowSyncAccount()+"."+getArchiveExtension();
	}
	
	public String getArchiveDateTimeName() {
		if (!isWowAccounts()) return null;
		return getWowSyncAccount()+ARCHIVE_DATETIMEEXT;
	}
	
	/**
	 * Return the WoW/WTF/Account locale path
	 * @return path
	 */
	public String getLocaleWTFAccountPath() {
		return new File(wowInstallPath, WOW_WTF_ACCOUNT).getAbsolutePath(); 
	}

	/**
	 * Return the complete pathname to the locale archive file.
	 * @return locale archive file pathname
	 */
	public String getLocaleArchivePath() {
		String str = getArchiveName();
		if (str == null) return null;
		File f = new File(new File(wowInstallPath, WOW_WTF_ACCOUNT), str);
		return f.getAbsolutePath();
	}
	
	/**
	 * Convenient method to get the lastModified time of a file.
	 * @param pathname
	 * @return lastmodified time
	 */
	public long getMTime(String pathname) {
		File f = new File(pathname);
		return f.lastModified();
	}
	
	/**
	 * Return the lastmodified time of the locale archive file.
	 * @return lastmodified time
	 */
	//public long getLocaleArchiveMTime() {
	//	return getMTime(getLocaleArchivePath());
	//}
	
	/**
	 * Return the lastmodified time of the account directory.
	 * Based on a file in the account (I use 'cache.md5')
	 * @return localtime (time since EPOCH)
	 */
	public long getLocaleAccountMTime() {
		File farchive = new File(wowInstallPath, WOW_WTF_ACCOUNT);
		farchive = new File(farchive, getWowSyncAccount());
		if (!farchive.exists()) return 0;
		farchive = new File(farchive, WOW_ACCOUNT_MOST_RECENT_FILE);
		if (WowSync.DEBUG) System.out.println("DEBUG: LocalMTime="+((farchive.lastModified()/1000L)*1000L));
		return (farchive.lastModified()/1000L)*1000L; // no millisecond info
	}
	
	/**
	 * Check the WowInstallPath/WTF/Accound to build a list of accounts and modify the Accounts ComboBox.
	 * Files or Directory with "." are ignored ; this is made for my habits to rename old accounts with ".old" extensions.
	 */
	protected void ListWowAccounts() {
		File f = new File(wowInstallPath, WOW_WTF_ACCOUNT);
		tabAccounts = new Vector<String>();
		if (f.exists() && f.isDirectory()) {
			File [] list = f.listFiles();
			if ((list != null) && (list.length > 0)) {
				for (int i=0; i < list.length; i++) {
					if (list[i].isDirectory() && !list[i].getName().contains(".")) tabAccounts.add(list[i].getName());
				}
				setWowSyncAccount(tabAccounts.firstElement());
				wowAccounts = true;
				/*ComboBoxModel jcbWowAccountsModel = 
					new DefaultComboBoxModel(tabAccounts);
				jcbWowAccounts.setModel(jcbWowAccountsModel);
				jcbWowAccounts.setEnabled(true);*/
			} else {
				wowAccounts = false;
				tabAccounts.add("No account !");
				/*ComboBoxModel jcbWowAccountsModel = 
					new DefaultComboBoxModel(
							new String[] { "No account !"});
				jcbWowAccounts.setModel(jcbWowAccountsModel);
				jcbWowAccounts.setEnabled(false);
				*/
			}
		} else {
			wowAccounts = false;
			tabAccounts.add("Can't find WTF/Account !");
			/*ComboBoxModel jcbWowAccountsModel = 
				new DefaultComboBoxModel(
						new String[] { "Can't find WTF/Account !"});
			jcbWowAccounts.setModel(jcbWowAccountsModel);
			jcbWowAccounts.setEnabled(false);
			*/
		}
	}
	
	/**
	 * Get the current ssh server.
	 * @return the current ssh server specified by the 'currentSshServer' local variable.
	 * @see #activeSshServerIndex
	 * @see wowsync.SshServerInfo
	 */
	protected SshServerInfo getActiveSshServer() {
		if (sshServers == null || sshServers.isEmpty()) return null;
		return sshServers.get(activeSshServerIndex);
	}
	
	public int getActiveServerIndex() {
		return activeSshServerIndex;
	}
	
	public void setActiveServerIndex(int i) {
		activeSshServerIndex = i;
	}
	
	/**
	 * Read the configuration file from an XML file
	 * and modify the UI accordingly.
	 */
	protected void ReadConfigurationFile() {
		File f = new File(CONFIG_FILENAME);
		if (!f.exists()) {
			configLoaded = false;
			return;
		}

		Document document = null;
		SAXBuilder sxb = new SAXBuilder();
		try
		{
			document = sxb.build(f);

			Element racine = document.getRootElement();
			if (!racine.getName().equals(XML_ROOT)) {
				throw new Exception("Wrong XML file !");
			}

			// Archiver element
			String childname = XML_ARCHIVER;
			setArchiverExecPath(getXMLStringValue(racine, childname, XML_ARCHIVER_EXECPATH, getArchiverExecPath()));
			setArchiverAddCommand(getXMLStringValue(racine, childname, XML_ARCHIVER_ARCHIVEADD, getArchiverAddCommand()));
			setArchiverExtractCommand(getXMLStringValue(racine, childname, XML_ARCHIVER_ARCHIVEEXTRACT, getArchiverExtractCommand()));
			setArchiveExtension(getXMLStringValue(racine, childname, XML_ARCHIVER_ARCHIVEEXT, getArchiveExtension()));
			setArchiveBackup(getXMLBooleanValue(racine, childname, XML_ARCHIVER_ARCHIVEBACKUP, isArchiveBackup()));

			// WoW element
			childname = XML_WOW;
			setWowInstallPath(getXMLStringValue(racine, childname, XML_WOW_INSTALLPATH, getWowInstallPath()));
			setWowSyncAccount(getXMLStringValue(racine, childname, XML_WOW_SYNCACCOUNT, getWowSyncAccount()));
			
			// Ssh element
			sshServers = SshServerInfo.getSshServers(racine);
			activeSshServerIndex = -1; 
			if (sshServers != null && !sshServers.isEmpty()) {
				activeSshServerIndex = 0;
				// find the active server configuration
				String activeServerAlias = getXMLStringValue(racine, XML_SSHACCOUNT_ACTIVE, null);
				if (activeServerAlias != null) {
					int i = 0;
					for (SshServerInfo ssi : sshServers) {
						if (activeServerAlias.equals(ssi.getAccountAlias())) {
							activeSshServerIndex = i;
							break;
						}
						i++;
					}
				}
			}
//			childname = XML_SSHACCOUNT;
//			setSshServerAddress(getXMLStringValue(racine, childname, XML_SSHACCOUNT_SERVERADDRESS, getSshServerAddress()));
//			setSshLogin(getXMLStringValue(racine, childname, XML_SSHACCOUNT_LOGIN, getSshLogin()));
//			setSshStorePass(getXMLBooleanValue(racine, childname, XML_SSHACCOUNT_STOREPASSWORD, isSshStorePassword()));
//			if (isSshStorePassword())
//				setSshPassword(rot13(getXMLStringValue(racine, childname, XML_SSHACCOUNT_PASSWORD, getSshPassword())));
//			setSshStoreKey(getXMLBooleanValue(racine, childname, XML_SSHACCOUNT_STOREKEY, isSshStoreKey()));
//			if (isSshStoreKey()) {
//				setSshKeyStored(getXMLBooleanValue(racine, childname, XML_SSHACCOUNT_KEYSTORED, isSshKeyStored()));
//				if (isSshKeyStored()) {
//					setSshServerKey(getXMLStringValue(racine, childname, XML_SSHACCOUNT_SERVERKEY, getSshServerKey()));
//					setSshServerKeyType(getXMLStringValue(racine, childname, XML_SSHACCOUNT_SERVERKEYTYPE, getSshServerKeyType()));
//				}
//			}
//			setSshServerPath(getXMLStringValue(racine, childname, XML_SSHACCOUNT_SERVERPATH, getSshServerPath()));
//			setSshPrivateKeyAuthentication(getXMLBooleanValue(racine, childname, XML_SSHACCOUNT_PRIVATEKEYAUTHENTICATION, isSshPrivateKeyAuthentication()));
//			setSshPrivateKeyFile(getXMLStringValue(racine, childname, XML_SSHACCOUNT_PRIVATEKEYFILE, null));
			
			// Sync element
			childname = XML_SYNC;
			setSyncBehavior(getXMLIntValue(racine, childname, XML_SYNC_BEHAVIOR, getSyncBehavior()));
			
			configLoaded = true;
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null, MSG_CORRUPTED_CONFIG, "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			configLoaded = false;
		}
	}
	
	/**
	 * Generate the configuration file format.
	 * @return the XML document
	 */
	private Document GenerateXML() {
		Element racine = new Element(XML_ROOT);
		Document document = new Document(racine);
		
		// Archiver element
		Element elem = new Element(XML_ARCHIVER);
		racine.addContent(elem);
		
		addXMLStringValue(elem, XML_ARCHIVER_EXECPATH, getArchiverExecPath());
		addXMLStringValue(elem, XML_ARCHIVER_ARCHIVEADD, getArchiverAddCommand());
		addXMLStringValue(elem, XML_ARCHIVER_ARCHIVEEXTRACT, getArchiverExtractCommand());
		addXMLStringValue(elem, XML_ARCHIVER_ARCHIVEEXT, getArchiveExtension());
		addXMLStringValue(elem, XML_ARCHIVER_ARCHIVEBACKUP, isArchiveBackup() ? "true" : "false");
		
		// WoW element
		elem = new Element(XML_WOW);
		racine.addContent(elem);
		
		addXMLStringValue(elem, XML_WOW_INSTALLPATH, getWowInstallPath());
		addXMLStringValue(elem, XML_WOW_SYNCACCOUNT, getWowSyncAccount());
		
		// SSH element
		for (SshServerInfo ssi : sshServers) {
			ssi.writeXML(racine);
		}
		if (sshServers != null && !sshServers.isEmpty())
			addXMLStringValue(racine, XML_SSHACCOUNT_ACTIVE, getActiveSshServer().getAccountAlias());
//		elem = new Element(XML_SSHACCOUNT);
//		racine.addContent(elem);
//		
//		addXMLStringValue(elem, XML_SSHACCOUNT_SERVERADDRESS,getSshServerAddress());
//		addXMLStringValue(elem, XML_SSHACCOUNT_LOGIN, getSshLogin());
//		addXMLStringValue(elem, XML_SSHACCOUNT_STOREPASSWORD, isSshStorePassword() ? "true" : "false");
//		if (isSshStorePassword()) {
//			addXMLStringValue(elem, XML_SSHACCOUNT_PASSWORD, rot13(getSshPassword()));
//		}
//		addXMLStringValue(elem, XML_SSHACCOUNT_STOREKEY, isSshStoreKey() ? "true" : "false");
//		if (isSshStoreKey()) 
//		{
//			addXMLStringValue(elem, XML_SSHACCOUNT_KEYSTORED, isSshKeyStored() ? "true" : "false");
//			if (isSshKeyStored()) {
//				addXMLStringValue(elem, XML_SSHACCOUNT_SERVERKEY, getSshServerKey());
//				addXMLStringValue(elem, XML_SSHACCOUNT_SERVERKEYTYPE, getSshServerKeyType());
//			}
//		}
//		addXMLStringValue(elem, XML_SSHACCOUNT_SERVERPATH, getSshServerPath());
//		addXMLStringValue(elem, XML_SSHACCOUNT_PRIVATEKEYAUTHENTICATION, isSshPrivateKeyAuthentication() ? "true" : "false");
//		addXMLStringValue(elem, XML_SSHACCOUNT_PRIVATEKEYFILE, getSshPrivateKeyFile());
		
		// Sync element
		elem = new Element(XML_SYNC);
		racine.addContent(elem);
		
		addXMLStringValue(elem, XML_SYNC_BEHAVIOR, ""+getSyncBehavior());
		addXMLStringValue(elem, XML_SYNC_BEHAVIOR_TEXT, getSyncBehaviorText());
		
		return document;
	}
	
	/**
	 * Save the model into the XML configuration file.
	 */
	protected void SaveConfigurationFile() {
		Document document = GenerateXML();
		
		// Writing
		try
		{			
			XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
			sortie.output(document, new FileOutputStream(CONFIG_FILENAME));
		}
		catch (java.io.IOException e){}
	}
	
	/**
	 * Display the model into an XML form (configuration file).
	 */
	protected String getConfigurationFile() {
		Document document = GenerateXML();
		
		StringWriter sw = new StringWriter();
		// Writing
		try
		{			
			XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
			//sortie.output(document, System.out);
			sortie.output(document, sw);
		}
		catch (java.io.IOException e){}
		return new String(sw.getBuffer()); 
	}
	
	/**
	 * Get a boolean from parent->child1->child2 'value' attribute.
	 * @param parent
	 * @param childname
	 * @return attribute 'value' boolean value
	 */
	public static boolean getXMLBooleanValue(Element parent, String childname, boolean defaultValue) {
		try {
			Element elem = parent.getChild(childname);
			return elem.getAttribute("value").getValue().equals("true") ? true : false;
		} catch (IllegalDataException e) {
			return defaultValue;
		} catch (NullPointerException e2) {
			return defaultValue;
		}
	}
	
	/**
	 * Get a boolean from parent->child1->child2 'value' attribute.
	 * @param parent
	 * @param child1name
	 * @param child2name
	 * @return attribute 'value' boolean value
	 */
	public static boolean getXMLBooleanValue(Element parent, String child1name, String child2name, boolean defaultValue) {
		try {
			Element elem = parent.getChild(child1name);
			elem = elem.getChild(child2name);
			return elem.getAttribute("value").getValue().equals("true") ? true : false;
		} catch (IllegalDataException e) {
			return defaultValue;
		} catch (NullPointerException e2) {
			return defaultValue;
		}
	}
	
	/**
	 * Get an integer from parent->child1->child2 'value' attribute.
	 * @param parent
	 * @param childname
	 * @return attribute 'value' int value
	 */
	public static int getXMLIntValue(Element parent, String childname, int defaultValue) {
		try {
			Element elem = parent.getChild(childname);
			return Integer.parseInt(elem.getAttribute("value").getValue());
		} catch (IllegalDataException e) {
			return defaultValue;
		} catch (NullPointerException e2) {
			return defaultValue;
		}
	}
	
	/**
	 * Get an integer from parent->child1->child2 'value' attribute.
	 * @param parent
	 * @param child1name
	 * @param child2name
	 * @return attribute 'value' int value
	 */
	public static int getXMLIntValue(Element parent, String child1name, String child2name, int defaultValue) {
		try {
			Element elem = parent.getChild(child1name);
			elem = elem.getChild(child2name);
			return Integer.parseInt(elem.getAttribute("value").getValue());
		} catch (IllegalDataException e) {
			return defaultValue;
		} catch (NullPointerException e2) {
			return defaultValue;
		}
	}
	
	/**
	 * Get a string from parent->child1 'value' attribute.
	 * @param parent
	 * @param childname
	 * @return text of the attribute 'value'
	 */
	public static String getXMLStringValue(Element parent, String childname, String defaultValue) {
		try {
			Element elem = parent.getChild(childname);
			return elem.getAttribute("value").getValue();
		} catch(IllegalDataException e) {
			return defaultValue;
		} catch (NullPointerException e2) {
			return defaultValue;
		}
	}
	
	/**
	 * Get a string from parent->child1->child2 'value' attribute.
	 * @param parent
	 * @param child1name
	 * @param child2name
	 * @return text of the attribute 'value'
	 */
	public static String getXMLStringValue(Element parent, String child1name, String child2name, String defaultValue) {
		try {
			Element elem = parent.getChild(child1name);
			elem = elem.getChild(child2name);
			return elem.getAttribute("value").getValue();
		} catch(IllegalDataException e) {
			return defaultValue;
		} catch (NullPointerException e2) {
			return defaultValue;
		}
	}
	
	/**
	 * Create a new element with a 'value' attribute.
	 * @param parent parent of the new element
	 * @param name name of the element
	 * @param value value ot the attribute (attribute name = 'value')
	 */
	public static void addXMLStringValue(Element parent, String name, String value) {
		if (value == null) return;
		Element elem = new Element(name);
		parent.addContent(elem);
		Attribute attrib = new Attribute("value", value);
		elem.setAttribute(attrib);
	}
	
	/**
	 * entry point for testing purpose
	 */
	public static void main(String[] args) {
		WowSyncModel wsm = new WowSyncModel();
		wsm.ReadConfigurationFile();
		System.out.println(wsm.getConfigurationFile());
		//wsm.SaveConfigurationFile();
	}
}
