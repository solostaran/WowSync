package wowsync;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

/**
 * JSCH User Info, login/password case.
 */
public class WowSyncUserInfo implements UserInfo, UIKeyboardInteractive {
	private WowSyncModel wsm;
	private String passwd;
	
	private JTextField passwordField = (JTextField)new JPasswordField(20);
	
	public WowSyncUserInfo(WowSyncModel wsm) {
		this.wsm = wsm;
	}

	public String getPassword(){ return passwd; }
	public String getPassphrase(){ return null; }
	public boolean promptPassphrase(String message){ return true; }

	/**
	 * yes/no prompt + message.
	 * Typically used for prompting if the user trust the host server.
	 * @param str prompt message
	 * @return true = yes, false = no
	 */
	public boolean promptYesNo(String str){
		System.out.println(str);
		Object[] options={ "yes", "no" };
		int foo=JOptionPane.showOptionDialog(null, 
				str,
				"Warning", 
				JOptionPane.DEFAULT_OPTION, 
				JOptionPane.WARNING_MESSAGE,
				null, options, options[0]);
		return foo==0;
	}

	/**
	 * Called at password time ... modify it if the password is stored.
	 * @param message password prompt message
	 * @return true = a password has been prompted, false = cancellation
	 */
	public boolean promptPassword(String message){
		if (wsm.isSshStorePassword()) {
			// WARNING - unsecured procedure
			passwd = wsm.getSshPassword();
			return true;
		}
		Object[] ob={passwordField}; 
		int result=
			JOptionPane.showConfirmDialog(null, ob, message,
					JOptionPane.OK_CANCEL_OPTION);
		if(result==JOptionPane.OK_OPTION){
			passwd=passwordField.getText();
			return true;
		}
		else{ 
			return false; 
		}
	}

	/**
	 * The host server sends a message that can be displayed or not here.
	 */
	public void showMessage(String message){
		//JOptionPane.showMessageDialog(null, message);
		System.out.println(message);
	}

	final GridBagConstraints gbc = 
		new GridBagConstraints(0,0,1,1,1,1,
				GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE,
				new Insets(0,0,0,0),0,0);
	private Container panel;
	
	/**
	 * Multi-fields prompt.
	 */
	public String[] promptKeyboardInteractive(String destination,
			String name,
			String instruction,
			String[] prompt,
			boolean[] echo){
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridx = 0;
		panel.add(new JLabel(instruction), gbc);
		gbc.gridy++;

		gbc.gridwidth = GridBagConstraints.RELATIVE;

		JTextField[] texts=new JTextField[prompt.length];
		for(int i=0; i<prompt.length; i++){
			gbc.fill = GridBagConstraints.NONE;
			gbc.gridx = 0;
			gbc.weightx = 1;
			panel.add(new JLabel(prompt[i]),gbc);

			gbc.gridx = 1;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weighty = 1;
			if(echo[i]){
				texts[i]=new JTextField(20);
			}
			else{
				texts[i]=new JPasswordField(20);
			}
			panel.add(texts[i], gbc);
			gbc.gridy++;
		}

		if(JOptionPane.showConfirmDialog(null, panel, 
				destination+": "+name,
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE)
				==JOptionPane.OK_OPTION){
			String[] response=new String[prompt.length];
			for(int i=0; i<prompt.length; i++){
				response[i]=texts[i].getText();
			}
			return response;
		}
		else{
			return null;  // cancel
		}
	}
}
