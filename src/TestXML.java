

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import wowsync.SshServerInfo;
import wowsync.WowSyncModel;

@SuppressWarnings("unused")
public class TestXML {
	
	private static void test1() {
		Element racine = new Element("WowSyncTest");
		org.jdom.Document document = new Document(racine);

		// configuration par défaut pour 7-Zip
		Element arc = new Element("Archiver");
		racine.addContent(arc);

		Element elem = new Element("ExecPath");
		arc.addContent(elem);
		Attribute attrib = new Attribute("value","c:/Program Files/7-Zip/7z.exe");
		elem.setAttribute(attrib);

		elem = new Element("ExecParams");
		arc.addContent(elem);
		attrib = new Attribute("value","a");
		elem.setAttribute(attrib);
		
		elem = new Element("ArchiveExtension");
		arc.addContent(elem);
		attrib = new Attribute("value","7z");
		elem.setAttribute(attrib);

		// configuration par défaut pour WoW
		Element wow = new Element("WoW");
		racine.addContent(wow);

		elem = new Element("InstallPath");
		wow.addContent(elem);
		attrib = new Attribute("value", "c:/World of Warcraft/");
		elem.setAttribute(attrib);

		elem = new Element("SyncAccount");
		wow.addContent(elem);
		attrib = new Attribute("value", "myAccount");
		elem.setAttribute(attrib);

		// configuration par défaut pour SSH
		Element ssh = new Element("SshAccount");
		racine.addContent(ssh);

		elem = new Element("ServerAddress");
		ssh.addContent(elem);
		attrib = new Attribute("value", "myserver");
		elem.setAttribute(attrib);

		elem = new Element("Login");
		ssh.addContent(elem);
		attrib = new Attribute("value", "login");
		elem.setAttribute(attrib);

		elem = new Element("StorePassword");
		ssh.addContent(elem);
		attrib = new Attribute("value", "false");
		elem.setAttribute(attrib);

		elem = new Element("Password");
		ssh.addContent(elem);

		elem = new Element("StoreKey");
		ssh.addContent(elem);
		attrib = new Attribute("value", "false");
		elem.setAttribute(attrib);

		elem = new Element("Key");
		ssh.addContent(elem);

		// Enregistrement
		try
		{			
			XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
			sortie.output(document, new FileOutputStream("wowsynctest.xml"));
		}
		catch (java.io.IOException e){}

		// Lecture
		SAXBuilder sxb = new SAXBuilder();
		try
		{
			document = sxb.build(new File("wowsynctest.xml"));
		}
		catch(Exception e){}
		
		// Affichage
		try
		{			
			XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
			sortie.output(document, System.out);
		}
		catch (java.io.IOException e){}
	}
	
	private static void test2() {
		Element racine = new Element("WowSync");
		Document document = new Document(racine);
		
		ArrayList<SshServerInfo> c = new ArrayList<SshServerInfo>();
		
		SshServerInfo ssi = new SshServerInfo();
		ssi.setServerAddress("A");
		ssi.setLogin("login1");
		c.add(ssi);
		
		ssi = new SshServerInfo();
		ssi.setServerAddress("B");
		ssi.setServerPort(443);
		ssi.setLogin("login2");
		ssi.setStorePassword(true);
		ssi.setPassword("password1");
		c.add(ssi);
		
		ssi = new SshServerInfo();
		ssi.setServerAddress("C");
		ssi.setLogin("login3");
		ssi.setAuthenticationKey(true);
		ssi.setAuthenticationKeyFile("key.ssh");
		c.add(ssi);
		
		for (SshServerInfo i : c) {
			i.writeXML(racine);
		}
		
		// Enregistrement
		try
		{			
			XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
			sortie.output(document, new FileOutputStream("wowsynctest.xml"));
		}
		catch (java.io.IOException e){}

		// Lecture
		SAXBuilder sxb = new SAXBuilder();
		try
		{
			document = sxb.build(new File("wowsynctest.xml"));
		}
		catch(Exception e){}
		
		// Affichage
		try
		{			
			XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
			sortie.output(document, System.out);
		}
		catch (java.io.IOException e){}
		
		ArrayList<SshServerInfo> l = SshServerInfo.getSshServers(document.getRootElement());
		for (SshServerInfo i : l) {
			System.out.println("Adresse serveur : "+i.getServerAddress());
			if (i.isAuthenticationKey()) {
				System.out.println("\tConnexion par clé");
			} else {
				System.out.print("\tConnexion par mot de passe : ");
				System.out.println(i.isStorePassword() ? "stocké." : "non stocké.");
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		test1();
		test2();
	}

}
