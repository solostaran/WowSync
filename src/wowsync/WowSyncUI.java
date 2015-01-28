package wowsync;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * The configuration GUI.
 */
public class WowSyncUI extends javax.swing.JFrame implements ActionListener, FocusListener {

	private static final long serialVersionUID = -6404229472103436892L;
	private JTabbedPane jTabbedPane1;
	private JPanel jpArchiver;
	private JPanel jpServer;
	private JTextField jtfArchiverPath;
	private JTextField jtfArchiverAdd;
	private JLabel jlblArchiveExt;
	private JPanel jpSyncBehavior;
	private JButton jbRemoveSshServer;
	private JButton jbNewSshServer;
	private JButton jbRenameSshServerAlias;
	private JPanel jPanel1;
	private JComboBox jcbSshServers;
	private JTextField jtfServerPort;
	private JLabel jlblServerPort;
	private JButton jbPrivateKeyFileChooser;
	private JTextField jtfPrivateKeyFile;
	private JCheckBox jcbPrivateKeyAuthentication;
	private JTextField jtfServerPath;
	private JLabel jlblServerPath;
	private JLabel jlblPasswordUnsecured;
	private JCheckBox jcbArchiveBackup;
	private JButton jbShowConfig;
	private JPanel jpSync;
	private JCheckBox jcbStoreKey;
	private JPasswordField jpfPassword;
	private JCheckBox jcbStorePass;
	private JTextField jtfArchiverExtract;
	private JLabel jlblArchiveExtract;
	private JTextField jtfLogin;
	private JLabel jlblLogin;
	private JTextField jtfServerAddress;
	private JLabel jlblServerAddress;
	private JLabel jlblWowAccount;
	private JComboBox jcbWowAccounts;
	private JButton jbWowPath;
	private JTextField jtfWowPath;
	private JLabel jlblWowPath;
	private JTextField jtfArchiveExt;
	private JLabel jlblArchiveAdd;
	private JButton jbArchiverBrowse;
	private JLabel jlblArchivePath;
	private JPanel jpWoW;
	private ButtonGroup gbSyncBehavior;
	
	private WowSyncModel wsm;
	private Vector<ButtonModel> tabSyncBehaviorButtons;
	
	/**
	 * @see wowsync.WowSyncModel
	 * @param wsm the loaded configuration model
	 */
	public WowSyncUI(WowSyncModel wsm) {
		super();
		this.wsm = wsm;
		initGUI();
		if (wsm.isConfigLoaded()) Model2UI();
		
//		if (com.sun.awt.AWTUtilities.isTranslucencySupported(Translucency.PERPIXEL_TRANSLUCENT))
//			com.sun.awt.AWTUtilities.setWindowOpacity(this, 0.9f);
	}
	
	/**
	 * When closing the frame, ask for confirmation (includes yes/no configuration save).
	 * @see #initGUI()
	 */
	private void closing() {
		int ret = JOptionPane.showConfirmDialog(this, "Save the configuration ?", "WowSync : Exit", JOptionPane.YES_NO_CANCEL_OPTION);
		if (ret == JOptionPane.CANCEL_OPTION)
			this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		else
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		if (ret == JOptionPane.YES_OPTION) {
			wsm.SaveConfigurationFile();
		}
		
	}
	
	/**
	 * Build the UI
	 */
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			this.setTitle("WowSync");
			this.setResizable(false);
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					closing(); 
				}
			});
			{
				jTabbedPane1 = new JTabbedPane();
				getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
				// ********** ARCHIVER TAB **********
				{
					jpArchiver = new JPanel();
					jTabbedPane1.addTab("Archiver", null, jpArchiver, null);
					jpArchiver.setLayout(null);
					{
						jlblArchivePath = new JLabel();
						jpArchiver.add(jlblArchivePath);
						jlblArchivePath.setText("Archiver executable :");
						jlblArchivePath.setBounds(12, 12, 137, 21);
					}
					{
						jtfArchiverPath = new JTextField();
						jpArchiver.add(jtfArchiverPath);
						jtfArchiverPath.setText("c:/Program Files/7-Zip/7z.exe");
						jtfArchiverPath.setBounds(156, 9, 354, 28);
						jtfArchiverPath.addFocusListener(this);
					}
					{
						jbArchiverBrowse = new JButton();
						jpArchiver.add(jbArchiverBrowse);
						jbArchiverBrowse.setText("Browse ...");
						jbArchiverBrowse.setBounds(516, 9, 96, 28);
						jbArchiverBrowse.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								jbArchiverBrowseActionPerformed(evt);
							}
						});
					}
					{
						jlblArchiveAdd = new JLabel();
						jpArchiver.add(jlblArchiveAdd);
						jlblArchiveAdd.setText("Archive add param");
						jlblArchiveAdd.setBounds(12, 58, 153, 21);
					}
					{
						jtfArchiverAdd = new JTextField();
						jpArchiver.add(jtfArchiverAdd);
						jtfArchiverAdd.setText("a");
						jtfArchiverAdd.setBounds(156, 55, 156, 28);
						jtfArchiverAdd.addFocusListener(this);
						jtfArchiverAdd.addActionListener(this);
					}
					{
						jlblArchiveExt = new JLabel();
						jpArchiver.add(jlblArchiveExt);
						jlblArchiveExt.setText("Archive extension : (without the dot)");
						jlblArchiveExt.setBounds(12, 104, 275, 21);
					}
					{
						jtfArchiveExt = new JTextField();
						jpArchiver.add(jtfArchiveExt);
						jtfArchiveExt.setText("7z");
						jtfArchiveExt.setBounds(312, 101, 75, 28);
						jtfArchiveExt.addFocusListener(this);
						jtfArchiveExt.addActionListener(this);
					}
					{
						jlblArchiveExtract = new JLabel();
						jpArchiver.add(jlblArchiveExtract);
						jlblArchiveExtract.setText("Archive extract param");
						jlblArchiveExtract.setBounds(333, 58, 171, 21);
					}
					{
						jtfArchiverExtract = new JTextField();
						jpArchiver.add(jtfArchiverExtract);
						jtfArchiverExtract.setText("x -y");
						jtfArchiverExtract.setBounds(516, 55, 64, 28);
						jtfArchiverExtract.addFocusListener(this);
						jtfArchiverExtract.addActionListener(this);
					}
					{
						jcbArchiveBackup = new JCheckBox();
						jpArchiver.add(jcbArchiveBackup);
						jcbArchiveBackup.setText("Archive backup");
						jcbArchiveBackup.setBounds(12, 148, 219, 25);
						jcbArchiveBackup.setSelected(true);
						jcbArchiveBackup.addActionListener(this);
					}
				}
				// ********** WOW TAB **********
				{
					jpWoW = new JPanel();
					jTabbedPane1.addTab("WoW", null, jpWoW, null);
					jpWoW.setLayout(null);
					{
						jlblWowPath = new JLabel();
						jpWoW.add(jlblWowPath);
						jlblWowPath.setText("WoW Install Path :");
						jlblWowPath.setBounds(12, 12, 137, 21);
					}
					{
						jtfWowPath = new JTextField();
						jpWoW.add(jtfWowPath);
						jtfWowPath.setText("c:/World of Warcraft");
						jtfWowPath.setBounds(156, 9, 354, 28);
						jtfWowPath.addFocusListener(this);
						jtfWowPath.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								jtfWowPathActionPerformed(evt);
							}
						});
					}
					{
						jbWowPath = new JButton();
						jpWoW.add(jbWowPath);
						jbWowPath.setText("SetPath ...");
						jbWowPath.setBounds(516, 9, 96, 28);
						jbWowPath.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								jbWowPathActionPerformed(evt);
							}
						});
					}
					{
						//ComboBoxModel jcbWowAccountsModel = 
						//	new DefaultComboBoxModel(new String[] { "Account One", "Account Two" });
						jcbWowAccounts = new JComboBox();
						jpWoW.add(jcbWowAccounts);
						//jcbWowAccounts.setModel(jcbWowAccountsModel);
						jcbWowAccounts.setBounds(247, 58, 263, 28);
						jcbWowAccounts.addActionListener(this);
						jcbWowAccounts.setEnabled(false);
					}
					{
						jlblWowAccount = new JLabel();
						jpWoW.add(jlblWowAccount);
						jlblWowAccount.setText("Select WoW Account to Sync :");
						jlblWowAccount.setBounds(12, 62, 198, 21);
					}
				}
				// ********** SSH TAB **********
				{
					jpServer = new JPanel();
					jTabbedPane1.addTab("Server", null, jpServer, null);
					jpServer.setLayout(null);
					{
						//						ComboBoxModel jcbSshServersModel = 
							//							new DefaultComboBoxModel(
									//									new String[] { "Item One", "Item Two" });
						jcbSshServers = new JComboBox();
						jpServer.add(jcbSshServers);
						//						jcbSshServers.setModel(jcbSshServersModel);
						jcbSshServers.setBounds(13, 10, 259, 28);
						jcbSshServers.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								jcbSshServersActionPerformed(evt);
							}
						});
					}
					{
						jPanel1 = new JPanel();
						jpServer.add(jPanel1);
						jPanel1.setBounds(7, 44, 616, 257);
						jPanel1.setLayout(null);
						jPanel1.setBorder(BorderFactory.createTitledBorder("Ssh Server Config"));
						{
							jlblServerAddress = new JLabel();
							jPanel1.add(jlblServerAddress);
							jlblServerAddress.setText("Server address :");
							jlblServerAddress.setBounds(8, 22, 137, 21);
						}
						{
							jtfServerAddress = new JTextField();
							jPanel1.add(jtfServerAddress);
							jtfServerAddress.setText("myserver");
							jtfServerAddress.setBounds(152, 19, 262, 28);
							jtfServerAddress.addFocusListener(this);
							jtfServerAddress.addActionListener(this);
						}
						{
							jlblServerPort = new JLabel();
							jPanel1.add(jlblServerPort);
							jlblServerPort.setText("port :");
							jlblServerPort.setBounds(454, 22, 43, 21);
						}
						{
							jtfServerPort = new JTextField();
							jPanel1.add(jtfServerPort);
							jtfServerPort.setText("22");
							jtfServerPort.setBounds(518, 19, 90, 28);
						}
						{
							jlblLogin = new JLabel();
							jPanel1.add(jlblLogin);
							jlblLogin.setText("Login :");
							jlblLogin.setBounds(8, 57, 140, 21);
						}
						{
							jtfLogin = new JTextField();
							jPanel1.add(jtfLogin);
							jtfLogin.setText("mylogin");
							jtfLogin.setBounds(152, 54, 354, 28);
							jtfLogin.addFocusListener(this);
							jtfLogin.addActionListener(this);
						}
						{
							jcbStorePass = new JCheckBox();
							jPanel1.add(jcbStorePass);
							jcbStorePass.setText("Store Password ?");
							jcbStorePass.setBounds(8, 150, 130, 25);
							jcbStorePass.setToolTipText("if checked set the password here else ask for password at log time.");
							jcbStorePass.addActionListener(this);
						}
						{
							jpfPassword = new JPasswordField();
							jPanel1.add(jpfPassword);
							jpfPassword.setText("");
							jpfPassword.setBounds(152, 149, 354, 28);
							jpfPassword.setEnabled(false);
							jpfPassword.addFocusListener(this);
							jpfPassword.addActionListener(this);
						}
						{
							jcbStoreKey = new JCheckBox();
							jPanel1.add(jcbStoreKey);
							jcbStoreKey.setText("Store Server Key ?");
							jcbStoreKey.setBounds(8, 187, 498, 25);
							jcbStoreKey.setSelected(true);
							jcbStoreKey.addActionListener(this);
						}
						{
							jlblPasswordUnsecured = new JLabel();
							jPanel1.add(jlblPasswordUnsecured);
							jlblPasswordUnsecured.setText("WARNING - Password or Passphrase storage is unsecured");
							jlblPasswordUnsecured.setBounds(8, 123, 425, 21);
						}
						{
							jlblServerPath = new JLabel();
							jPanel1.add(jlblServerPath);
							jlblServerPath.setText("Server repository (path)");
							jlblServerPath.setBounds(8, 222, 167, 21);
						}
						{
							jtfServerPath = new JTextField();
							jPanel1.add(jtfServerPath);
							jtfServerPath.setText(".");
							jtfServerPath.setBounds(181, 222, 325, 28);
							jtfServerPath.addFocusListener(this);
							jtfServerPath.addActionListener(this);
						}
						{
							jcbPrivateKeyAuthentication = new JCheckBox();
							jPanel1.add(jcbPrivateKeyAuthentication);
							jcbPrivateKeyAuthentication.setText("Private key authentication ?");
							jcbPrivateKeyAuthentication.setToolTipText("if checked your authentication will be with a local private key (with a passphrase) instead of a password.");
							jcbPrivateKeyAuthentication.setBounds(8, 94, 202, 20);
							jcbPrivateKeyAuthentication.addActionListener(this);
						}
						{
							jtfPrivateKeyFile = new JTextField();
							jPanel1.add(jtfPrivateKeyFile);
							jtfPrivateKeyFile.setBounds(221, 90, 285, 28);
							jtfPrivateKeyFile.setEnabled(false);
							jtfPrivateKeyFile.addActionListener(this);
						}
						{
							jbPrivateKeyFileChooser = new JButton();
							jPanel1.add(jbPrivateKeyFileChooser);
							jbPrivateKeyFileChooser.setText("Set file ...");
							jbPrivateKeyFileChooser.setBounds(518, 90, 90, 28);
							jbPrivateKeyFileChooser.setEnabled(false);
							jbPrivateKeyFileChooser.addActionListener(this);
						}
					}
					{
						jbRenameSshServerAlias = new JButton();
						jpServer.add(jbRenameSshServerAlias);
						jbRenameSshServerAlias.setText("Rename");
						jbRenameSshServerAlias.setBounds(284, 11, 82, 28);
						jbRenameSshServerAlias.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								jbRenameSshServerAliasActionPerformed(evt);
							}
						});
					}
					{
						jbNewSshServer = new JButton();
						jpServer.add(jbNewSshServer);
						jbNewSshServer.setText("New");
						jbNewSshServer.setBounds(377, 11, 83, 28);
						jbNewSshServer.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								jbNewSshServerActionPerformed(evt);
							}
						});
					}
					{
						jbRemoveSshServer = new JButton();
						jpServer.add(jbRemoveSshServer);
						jbRemoveSshServer.setText("Remove");
						jbRemoveSshServer.setBounds(471, 11, 83, 28);
						jbRemoveSshServer.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								jbRemoveSshServerActionPerformed(evt);
							}
						});
					}
				}
				// ********** SYNC TAB **********
				{
					jpSync = new JPanel();
					jTabbedPane1.addTab("Sync", null, jpSync, null);
					jpSync.setLayout(null);
					{
						jpSyncBehavior = new JPanel();
						jpSync.add(jpSyncBehavior);
						BoxLayout jpSyncBehaviorLayout = new BoxLayout(jpSyncBehavior, javax.swing.BoxLayout.Y_AXIS);
						jpSyncBehavior.setLayout(jpSyncBehaviorLayout);
						jpSyncBehavior.setBounds(12, 22, 464, 182);
						jpSyncBehavior.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
						{
							gbSyncBehavior = new ButtonGroup();
							tabSyncBehaviorButtons = new Vector<ButtonModel>();
							for (int i = 0; i < wsm.getSyncBehaviorCount(); i++) {
								JRadioButton rb = new JRadioButton();
								tabSyncBehaviorButtons.add(rb.getModel());
								rb.setText(wsm.getSyncBehaviorText(i));
								rb.setActionCommand("Radio"+i);
								rb.addActionListener(this);
								jpSyncBehavior.add(rb);
								gbSyncBehavior.add(rb);
							}
							gbSyncBehavior.setSelected(tabSyncBehaviorButtons.firstElement(), true);
						}
					}
				}
			}
			{
				jbShowConfig = new JButton();
				getContentPane().add(jbShowConfig, BorderLayout.SOUTH);
				jbShowConfig.setText("Show configuration file");
				jbShowConfig.addActionListener(this);
			}
			pack();
			setSize(640, 400);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}
	
	/**
	 * Refresh the UI with the model.
	 */
	private void Model2UI() {
		// ********** ARCHIVER TAB **********
		jtfArchiverPath.setText(wsm.getArchiverExecPath());
		jtfArchiverAdd.setText(wsm.getArchiverAddCommand());
		jtfArchiverExtract.setText(wsm.getArchiverExtractCommand());
		jtfArchiveExt.setText(wsm.getArchiveExtension());
		jcbArchiveBackup.setSelected(wsm.isArchiveBackup());
		
		// ********** WOW TAB **********
		jtfWowPath.setText(wsm.getWowInstallPath());
		ComboBoxModel jcbWowAccountsModel = new DefaultComboBoxModel(wsm.getWowAccounts());
		jcbWowAccounts.setModel(jcbWowAccountsModel);
		jcbWowAccounts.setEnabled(wsm.isWowAccounts());
		Enumeration<String> ewa = wsm.getWowAccounts().elements();
		String str = null;
		int i;
		for(i = 0; ewa.hasMoreElements(); i++) {
			str = ewa.nextElement();
			if (str.equals(wsm.getWowSyncAccount())) break;
		}
		if (wsm.isWowAccounts()) jcbWowAccounts.setSelectedIndex(i);


		// ********** SSH TAB **********
		ArrayList<SshServerInfo> sshServers = wsm.getSshServers();
		if (sshServers == null || sshServers.isEmpty()) {
			setSshServerConfigEnabled(false);
		} else {
			for (SshServerInfo ssi : sshServers) {
				jcbSshServers.addItem(ssi);
			}
			jcbSshServers.setSelectedIndex(wsm.getActiveServerIndex());
			SshServer2UI(sshServers.get(wsm.getActiveServerIndex()));
		}
		
		// ********** SYNC TAB **********
		gbSyncBehavior.setSelected(tabSyncBehaviorButtons.get(wsm.getSyncBehavior()), true);
	}
	
	/**
	 * Enable or disable the Ssh Server configuration panel
	 * @param b true = enabled, false = disabled
	 */
	private void setSshServerConfigEnabled(boolean b) {
		jcbSshServers.setEnabled(b);
		jPanel1.setEnabled(b);
		for (Component c : jPanel1.getComponents()) {
			c.setEnabled(b);
		}
		jbRenameSshServerAlias.setEnabled(b);
		jbRemoveSshServer.setEnabled(b);
	}
	
	/**
	 * Fill the Ssh Server configuration panel with the provided informations
	 * @param ssi server informations to fill the panel
	 */
	private void SshServer2UI(SshServerInfo ssi) {
		if (ssi == null) return;
		jtfServerAddress.setText(ssi.getServerAddress());
		jtfLogin.setText(ssi.getLogin());
		jcbStorePass.setSelected(ssi.isStorePassword());
		if (ssi.isStorePassword())
			jpfPassword.setText(ssi.getPassword());
		jpfPassword.setEnabled(ssi.isStorePassword());
		jcbStoreKey.setSelected(ssi.isStoreKey());
		jcbPrivateKeyAuthentication.setSelected(ssi.isAuthenticationKey());
		jbPrivateKeyFileChooser.setEnabled(ssi.isAuthenticationKey());
		jtfPrivateKeyFile.setText(ssi.getAuthenticationKeyFile());
		jtfServerPath.setText(ssi.getServerPath());
	}

	/**
	 * Manage events for most of the components.
	 */
	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == jbShowConfig) {
			System.out.println(wsm.getConfigurationFile());
			JOptionPane.showMessageDialog(this, wsm.getConfigurationFile(), "Configuration file", JOptionPane.INFORMATION_MESSAGE);
		}
		if (evt.getSource() == jtfArchiverAdd)
			wsm.setArchiverAddCommand(jtfArchiverAdd.getText());
		if (evt.getSource() == jtfArchiverExtract)
			wsm.setArchiverExtractCommand(jtfArchiverExtract.getText());
		if (evt.getSource() == jtfArchiveExt)
			wsm.setArchiveExtension(jtfArchiveExt.getText());
		if (evt.getSource() == jcbStorePass) {
			wsm.setSshStorePass(jcbStorePass.isSelected());
			jpfPassword.setEnabled(jcbStorePass.isSelected());
		}
		if (evt.getSource() == jcbWowAccounts)
			wsm.setWowSyncAccount((String)(jcbWowAccounts.getSelectedItem()));
		if (evt.getSource() == jtfServerAddress)
			wsm.setSshServerAddress(jtfServerAddress.getText());
		if (evt.getSource() == jtfLogin)
			wsm.setSshLogin(jtfLogin.getText());
		if (evt.getSource() == jcbStorePass) {
			wsm.setSshStorePass(jcbStorePass.isSelected());
			if (jcbStorePass.isSelected())
				wsm.setSshPassword(new String(jpfPassword.getPassword()));
		}
		if (evt.getSource() == jpfPassword)
			wsm.setSshPassword(new String(jpfPassword.getPassword()));
		if (evt.getSource() == jcbStoreKey)
			wsm.setSshStoreKey(jcbStoreKey.isSelected());
		if (evt.getSource() == jtfServerPath)
			wsm.setSshServerPath(jtfServerPath.getText());
		if (evt.getSource() == jcbPrivateKeyAuthentication) {
			boolean b = jcbPrivateKeyAuthentication.isSelected();
			wsm.setSshPrivateKeyAuthentication(b);
			jtfPrivateKeyFile.setEnabled(b);
			jbPrivateKeyFileChooser.setEnabled(b);
			if (b)
				JOptionPane.showMessageDialog(this, "The key file must be in a standard format like OpenSSH format.\n" +
					"A putty \".ppk\" file doesn't work. This means you must convert your .ppk into a .ssh\n" +
					"Look at your putty installation dir and launch \"puttygen.exe\",\n" +
					"load your \"ppk\" file and use \"Conversion\" > \"Export OpenSSH key\".", "Private key file format", JOptionPane.INFORMATION_MESSAGE);
		}
		if (evt.getSource() == jtfPrivateKeyFile) {
			File f = new File(jtfPrivateKeyFile.getText());
			if (!f.exists() || f.isDirectory()) {
				if (JOptionPane.showConfirmDialog(this, "The file \""+f.getAbsolutePath()+"\" does not exist !\nUse it anyway ?", "Private key file", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
					wsm.setSshPrivateKeyFile(jtfPrivateKeyFile.getText());
				else
					jtfPrivateKeyFile.setText(wsm.getSshPrivateKeyFile());
			} else {
				wsm.setSshPrivateKeyFile(jtfPrivateKeyFile.getText());
			}
		}
		if (evt.getSource() == jbPrivateKeyFileChooser) {
			JFileChooser jfc = new JFileChooser(".");
			jfc.setSelectedFile(new File(jtfPrivateKeyFile.getText()));
			int ret = jfc.showOpenDialog(this);
			if (ret == JFileChooser.APPROVE_OPTION) {
				wsm.setSshPrivateKeyFile(jfc.getSelectedFile().getAbsolutePath());
				jtfPrivateKeyFile.setText(wsm.getSshPrivateKeyFile());
			}
		}
		if (evt.getSource() instanceof JRadioButton) {
			//System.out.println(evt.getActionCommand());
			int i = Integer.parseInt(evt.getActionCommand().substring(5,6));
			wsm.setSyncBehavior(i);
		}
		if (evt.getSource() == jcbArchiveBackup) {
			wsm.setArchiveBackup(jcbArchiveBackup.isSelected());
		}
	}
	
	@Override
	public void focusGained(FocusEvent arg0) {
		// nothing to do
	}

	/**
	 * Some components needs refreshing the UI and model on focus lost. 
	 */
	@Override
	public void focusLost(FocusEvent fe) {
		ActionEvent ae = new ActionEvent(fe.getSource(), fe.getID(), "LostFocus");
		if (fe.getSource() == jtfWowPath) {
			jtfWowPathActionPerformed(ae);
			return;
		}
		actionPerformed(ae);
	}
	
	/**
	 * Display a file selection dialog for the archiver executable.
	 * @param evt event not used
	 */
	private void jbArchiverBrowseActionPerformed(ActionEvent evt) {
		JFileChooser jfc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Executable files", "exe", "bat", "cmd", "sh", "pl", "py");
		jfc.addChoosableFileFilter(filter);
		jfc.setSelectedFile(new File(jtfArchiverPath.getText()));
		int ret = jfc.showOpenDialog(this);
		if (ret == JFileChooser.APPROVE_OPTION) {
			if (jfc.getSelectedFile().exists()) {
				jtfArchiverPath.setText(jfc.getSelectedFile().toString());
				wsm.setArchiverExecPath(jtfArchiverPath.getText());
				String arcname = jtfArchiverPath.getText().toLowerCase();
				// Default archiver's extensions
				if (arcname.contains("zip")) {
					jtfArchiveExt.setText("zip");
				}
				if (arcname.contains("tar")) {
					jtfArchiveExt.setText("tar");
				}
				if (arcname.contains("7")) {
					jtfArchiveExt.setText("7z");
				}
				if (arcname.contains("rar")) {
					jtfArchiveExt.setText("rar");
				}
				if (arcname.contains("stuf")) {
					jtfArchiveExt.setText("sit");
				}
			}
		}
	}
	
	/**
	 * If you push the WoW SetPath button, display a directory selection dialog.
	 * @param evt event not used
	 */
	private void jbWowPathActionPerformed(ActionEvent evt) {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setCurrentDirectory(new File(jbWowPath.getText()));
		int ret = jfc.showOpenDialog(this);
		if (ret == JFileChooser.APPROVE_OPTION) {
			File f = jfc.getSelectedFile();
			jtfWowPath.setText(f.toString());
			jtfWowPathActionPerformed(evt);
		}
	}
	
	/**
	 *  In case you manually modify the WoW Path, force to rebuild the account list.
	 * @param evt event not used
	 */
	private void jtfWowPathActionPerformed(ActionEvent evt) {
		wsm.setWowInstallPath(jtfWowPath.getText());
		ComboBoxModel jcbWowAccountsModel = new DefaultComboBoxModel(wsm.getWowAccounts());
		jcbWowAccounts.setModel(jcbWowAccountsModel);
		jcbWowAccounts.repaint();
		jcbWowAccounts.setEnabled(wsm.isWowAccounts());		
	}
	
	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				WowSyncModel wsm = new WowSyncModel();
				wsm.ReadConfigurationFile();
				WowSyncUI inst = new WowSyncUI(wsm);
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	/**
	 * To modify the alias by which a Ssh Server Account is referenced
	 * @param evt
	 */
	private void jbRenameSshServerAliasActionPerformed(ActionEvent evt) {
		String ret = JOptionPane.showInputDialog(this, "Enter a new alias for this server", wsm.getActiveSshServer().getAccountAlias());
		if (ret != null) wsm.getActiveSshServer().setAccountAlias(ret);
		this.repaint();
	}
	
	/**
	 * Selection of a Ssh Server configuration
	 * @param evt
	 */
	private void jcbSshServersActionPerformed(ActionEvent evt) {
		if (jcbSshServers.getItemCount() == 0) return;
		wsm.setActiveServerIndex(jcbSshServers.getSelectedIndex());
		SshServer2UI(wsm.getActiveSshServer());
	}
	
	/**
	 * Creation of a new Ssh Server configuration
	 * @param evt
	 */
	private void jbNewSshServerActionPerformed(ActionEvent evt) {
		int ret = -1;
		if (jcbSshServers.getItemCount() == 0) {
			String [] options = {"New", "Cancel"};
			ret = JOptionPane.showOptionDialog(this,
					"Create a new Ssh Server configuration ...",
					"New Ssh Server configuration",
					0,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					null);
			ret += 1;
		} else {
			String [] options = {"Copy", "New", "Cancel"}; 
			ret = JOptionPane.showOptionDialog(this,
					"What kind of new configuration ?\n- a copy of the current configuration\n- a totally new one\n- cancel this creation",
					"New Ssh Server configuration",
					0,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					null);
		}
		SshServerInfo ssi = null;
		switch(ret) {
		case 0: // copy
			ssi = new SshServerInfo();
			ssi.readXML(wsm.getActiveSshServer().writeXML());
			ssi.setAccountAlias("Copy of "+ssi.getAccountAlias());
			break;
		case 1: // new
			ssi = new SshServerInfo();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			ssi.setAccountAlias("Config"+sdf.format(new Date()));
			break;
		default: // cancel or exit
			break;
		}
		if (ssi != null) {
			ArrayList<SshServerInfo> sshServers = wsm.getSshServers();
			sshServers.add(ssi);
			wsm.setSshServers(sshServers);
			wsm.setActiveServerIndex(sshServers.size() - 1);
			jcbSshServers.addItem(ssi);
			jcbSshServers.setSelectedIndex(wsm.getActiveServerIndex());
			if (sshServers.size() == 1) setSshServerConfigEnabled(true);
		}
		this.repaint();
	}
	
	/**
	 * Remove a Ssh Server configuration
	 * @param evt
	 */
	private void jbRemoveSshServerActionPerformed(ActionEvent evt) {
		int ret = JOptionPane.showConfirmDialog(this,
				"Remove the Ssh Server configuration :\n"+jcbSshServers.getSelectedItem(),
				"Remove Ssh Server",
				JOptionPane.OK_CANCEL_OPTION);
		if (ret != JOptionPane.OK_OPTION) return;
		int i = jcbSshServers.getSelectedIndex();
		ArrayList<SshServerInfo> sshServers = wsm.getSshServers();
		sshServers.remove(i);
		jcbSshServers.removeItemAt(i);
		if (sshServers.size() == 0) setSshServerConfigEnabled(false);
		this.repaint();
	}
}
