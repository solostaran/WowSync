package wowsync;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

/**
 * A container class for Ssh servers and the corresponding XML configuration.
 * @author reynaud
 *
 */
public class SshServerInfo {
	
	// ********** DEFINITIONS **********

	protected static final String XML_SSHACCOUNT = "SshAccount";
	protected static final String XML_SSHACCOUNT_ALIAS = "SshAccountAlias";
	protected static final String XML_SSHACCOUNT_SERVERADDRESS = "ServerAddress";
	protected static final String XML_SSHACCOUNT_SERVERPORT = "ServerPort";
	protected static final String XML_SSHACCOUNT_LOGIN = "Login";
	protected static final String XML_SSHACCOUNT_STOREPASSWORD = "StorePassword"; // boolean
	protected static final String XML_SSHACCOUNT_PASSWORD = "Password";
	protected static final String XML_SSHACCOUNT_SERVERPATH = "ServerPath";
	protected static final String XML_SSHACCOUNT_STOREKEY = "StoreKey"; //boolean
	protected static final String XML_SSHACCOUNT_PRIVATEKEYAUTHENTICATION = "PrivateKeyAuthentication"; // boolean
	protected static final String XML_SSHACCOUNT_PRIVATEKEYFILE = "PrivateKeyFile";

	/**
	 * web address of the server
	 */
	private String serverAddress = "localhost";
	/**
	 * ssh account alias
	 */
	private String accountAlias = serverAddress;
	/**
	 * server port number
	 */
	private int serverPort = 22;
	/**
	 * login to connect to this server
	 */
	private String login = null;
	/**
	 * define if the password is stored in the XML file and thus is not prompted at login time.
	 */
	private boolean storePassword = false;
	/**
	 * password to connect to this server (if stored)
	 */
	private String password = null;
	/**
	 * Server repository path (where the archive will be put onto the server)
	 */
	private String serverPath = ".";
	/**
	 * Define if the key is stored or not (in the KNOWN_HOST file)
	 * Thus it will only ask you if the key is ok either at the first connection or when key fingerprint has changed.
	 */
	private boolean storeKey = true;
	/**
	 * define if the authentication is through a login/pass or with an authentication key
	 */
	private boolean isAuthenticationKey = false;
	/**
	 * path to the authentication key file (in OpenSsh format)
	 */
	private String authenticationKeyFile = null;
	
	// ********** GETTERs & SETTERs **********
	
	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}
	public String getServerAddress() {
		return serverAddress;
	}
	public void setAccountAlias(String accountAlias) {
		this.accountAlias = accountAlias;
	}
	public String getAccountAlias() {
		return accountAlias;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	public int getServerPort() {
		return serverPort;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getLogin() {
		return login;
	}
	public void setStorePassword(boolean storePassword) {
		this.storePassword = storePassword;
	}
	public boolean isStorePassword() {
		return storePassword;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	public void setServerPath(String serverPath) {
		this.serverPath = serverPath;
	}
	public String getServerPath() {
		return serverPath;
	}
	public void setStoreKey(boolean storeKey) {
		this.storeKey = storeKey;
	}
	public boolean isStoreKey() {
		return storeKey;
	}
	public void setAuthenticationKey(boolean isAuthenticationKey) {
		this.isAuthenticationKey = isAuthenticationKey;
	}
	public boolean isAuthenticationKey() {
		return isAuthenticationKey;
	}
	public void setAuthenticationKeyFile(String authenticationKeyFile) {
		this.authenticationKeyFile = authenticationKeyFile;
	}
	public String getAuthenticationKeyFile() {
		return authenticationKeyFile;
	}
	
	// ********** Constructors **********
	
	public SshServerInfo() {
	}
	
	/**
	 * Should use this constructor.
	 * @param e XML element to parse
	 * @see #getSshServers(Element)
	 */
	public SshServerInfo(Element e) {
		readXML(e);
	}
	
	// ********** XML read and write **********
	
	/**
	 * rot13 algorithm
	 * @param s original string
	 * @return encoded string
	 */
	private static String rot13(String s) {
		if (s == null) return null;
		String tmp = "";
		for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if       (c >= 'a' && c <= 'm') c += 13;
            else if  (c >= 'n' && c <= 'z') c -= 13;
            else if  (c >= 'A' && c <= 'M') c += 13;
            else if  (c >= 'A' && c <= 'Z') c -= 13;
            tmp += c;
        }
		return tmp;
	}
	
	/**
	 * Create and add the JDOM XML Element from this instance to the specified parameter
	 * @param parent the parent element under which we add this content
	 */
	public void writeXML(Element parent) {
		parent.addContent(writeXML());
	}
	
	/**
	 * Create the JDOM XML Element from this instance
	 * @return a JDOM XML Element
	 */
	public Element writeXML() {
		Element elem = new Element(XML_SSHACCOUNT);
		WowSyncModel.addXMLStringValue(elem, XML_SSHACCOUNT_SERVERADDRESS, getServerAddress());
		WowSyncModel.addXMLStringValue(elem, XML_SSHACCOUNT_ALIAS, getAccountAlias());
		if (getServerPort() != 22) {
			WowSyncModel.addXMLStringValue(elem, XML_SSHACCOUNT_SERVERPORT, ""+getServerPort());
		}
		WowSyncModel.addXMLStringValue(elem, XML_SSHACCOUNT_LOGIN, getLogin());
		WowSyncModel.addXMLStringValue(elem, XML_SSHACCOUNT_SERVERPATH, getServerPath());
		WowSyncModel.addXMLStringValue(elem, XML_SSHACCOUNT_STOREKEY, isStoreKey() ? "true" : "false");
		
		WowSyncModel.addXMLStringValue(elem, XML_SSHACCOUNT_PRIVATEKEYAUTHENTICATION, isAuthenticationKey() ? "true" : "false");
		if (isAuthenticationKey()) {
			WowSyncModel.addXMLStringValue(elem, XML_SSHACCOUNT_PRIVATEKEYFILE, getAuthenticationKeyFile());
		} else {
			WowSyncModel.addXMLStringValue(elem, XML_SSHACCOUNT_STOREPASSWORD, isStorePassword() ? "true" : "false");
			if (isStorePassword()) {
				WowSyncModel.addXMLStringValue(elem, XML_SSHACCOUNT_PASSWORD, rot13(getPassword()));
			}
		}
		return elem;
	}
	
	/**
	 * Extract from a JDOM XML Element information to fill this instance's attributes
	 * @param e the Element holding XML informations
	 */
	public void readXML(Element e) {
		setServerAddress(WowSyncModel.getXMLStringValue(e, XML_SSHACCOUNT_SERVERADDRESS, null));
		setAccountAlias(WowSyncModel.getXMLStringValue(e, XML_SSHACCOUNT_ALIAS, getServerAddress()));
		if (getServerAddress() == null) return;
		setServerPort(WowSyncModel.getXMLIntValue(e, XML_SSHACCOUNT_SERVERPORT, 22));
		setLogin(WowSyncModel.getXMLStringValue(e, XML_SSHACCOUNT_LOGIN, null));
		setStorePassword(WowSyncModel.getXMLBooleanValue(e, XML_SSHACCOUNT_STOREPASSWORD, false));
		if (isStorePassword())
			setPassword(rot13(WowSyncModel.getXMLStringValue(e, XML_SSHACCOUNT_PASSWORD, null)));
		setAuthenticationKey(WowSyncModel.getXMLBooleanValue(e, XML_SSHACCOUNT_PRIVATEKEYAUTHENTICATION, false));
		setStoreKey(WowSyncModel.getXMLBooleanValue(e, XML_SSHACCOUNT_STOREKEY, true));
		setServerPath(WowSyncModel.getXMLStringValue(e, XML_SSHACCOUNT_SERVERPATH, "."));
		if (isAuthenticationKey())
			setAuthenticationKeyFile(WowSyncModel.getXMLStringValue(e, XML_SSHACCOUNT_PRIVATEKEYFILE, null));
	}
	
	/**
	 * Extract from a JDOM XML root element a list of SshAccount informations
	 * @param racine JDOM document root
	 * @return list of SshServerInfo
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<SshServerInfo> getSshServers(Element racine) {
		ArrayList<SshServerInfo> list = new ArrayList<SshServerInfo>();
		List<Element> l = racine.getChildren(XML_SSHACCOUNT);
		for (Element e : l) {
			list.add(new SshServerInfo(e));		
		}
		return list;
	}
	
	public String toString() {
		return accountAlias;
	}
}
