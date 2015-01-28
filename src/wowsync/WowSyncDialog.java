package wowsync;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

/**
 * The specific dialog that appears pre-synchronization treatment.
 */
public class WowSyncDialog extends JDialog implements ActionListener {
	
	// 3 possible choices
	public static final int WOWSYNCDIALOG_NOACTION = 0;
	public static final int WOWSYNCDIALOG_CHOICE1 = 1;
	public static final int WOWSYNCDIALOG_CHOICE2 = 2;

	// 2 possible dialog appearances
	public static final int DIALOG_SYNC = 0;
	public static final int DIALOG_EQUAL = 1;

	private static final long serialVersionUID = -7030942016743676894L;
	private JButton jbSync1;
	private JButton jbCancel;
	private JButton jbSync2;
	private JLabel jlblMsg2;
	private JLabel jlblMsg1;

	/**
	 * The read/write parameter which represents the dialog box's exit status.
	 * TODO: read only accessor ?
	 */
	public int exit_status = WOWSYNCDIALOG_NOACTION;

	/**
	 * Only one constructor with all parameters.
	 * @param title dialog title
	 * @param msg1 first line of dialog message
	 * @param msg2 second line of dialog message
	 * @param icon1 The first button image
	 * @param icon2 The second button image
	 * @param type choice between DIALOG_SYNC or DIALOG_EQUAL
	 */
	public WowSyncDialog(String title, String msg1, String msg2, ImageIcon icon1, ImageIcon icon2, int type) {

		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setTitle(title);
		getContentPane().setLayout(null);
		this.setResizable(false);
		
		if (type == DIALOG_SYNC) BuildSyncQuestionUI(title, msg1, msg2, icon1, icon2);
		if (type == DIALOG_EQUAL) BuildEqualQuestionUI(title, msg1, msg2, icon1, icon2);

		this.setModal(true);
		setLocationRelativeTo(null);
		
//		if (com.sun.awt.AWTUtilities.isTranslucencySupported(Translucency.PERPIXEL_TRANSLUCENT))
//			com.sun.awt.AWTUtilities.setWindowOpacity(this, 0.9f);
	}

	/**
	 * GUI builder for DIALOG_SYNC type dialog.<br/>
	 * 3 buttons :<br/>
	 * - a big main button to synchronize from the most recent item over the older one,<br/>
	 * - a smaller cancel button,<br/>
	 * - a smaller button to synchronize opposite to the main choice.
	 * @param title dialog title
	 * @param msg1 first line of the message
	 * @param msg2 second line of the message
	 * @param icon1 The bigger button and main choice 
	 * @param icon2 The smaller button and alternative choice
	 */
	protected void BuildSyncQuestionUI(String title, String msg1, String msg2, ImageIcon icon1, ImageIcon icon2) {
		int iTxtSizeMax = 0;
		FontMetrics fm = null;
		{
			jbSync1 = new JButton();
			getContentPane().add(jbSync1);
			jbSync1.setIcon(icon1);
			jbSync1.setBounds(10, 20, icon1.getIconWidth()+10, icon1.getIconHeight()+10);
			jbSync1.addActionListener(this);
			jbSync1.requestFocus();
		}
		{
			jlblMsg1 = new JLabel();
			getContentPane().add(jlblMsg1);
			fm = jlblMsg1.getFontMetrics(jlblMsg1.getFont());
			iTxtSizeMax = iTxtSizeMax < fm.stringWidth(msg1) ? fm.stringWidth(msg1) : iTxtSizeMax;
			jlblMsg1.setBounds(jbSync1.getWidth()+20, 20, fm.stringWidth(msg1), 20);
			jlblMsg1.setText(msg1);
		}
		{
			jlblMsg2 = new JLabel();
			getContentPane().add(jlblMsg2);
			fm = jlblMsg2.getFontMetrics(jlblMsg2.getFont());
			iTxtSizeMax = iTxtSizeMax < fm.stringWidth(msg2) ? fm.stringWidth(msg2) : iTxtSizeMax;
			jlblMsg2.setBounds(jbSync1.getWidth()+20, 50, fm.stringWidth(msg2), 20);
			jlblMsg2.setText(msg2);
		}
		int width = Math.max(jbSync1.getWidth()+30+iTxtSizeMax, jbSync1.getWidth()+150);
		{
			jbCancel = new JButton();
			getContentPane().add(jbCancel);
			jbCancel.setText("Cancel");
			jbCancel.setBounds(10, jbSync1.getHeight()+35, 100, 30);
			jbCancel.addActionListener(this);
		}
		{
			jbSync2 = new JButton();
			getContentPane().add(jbSync2);
			// resize the icon
			Image img = icon2.getImage();
			Image img2 = img.getScaledInstance(110, 25, Image.SCALE_SMOOTH);
			jbSync2.setIcon(new ImageIcon(img2));
			jbSync2.setBounds(width - 140, jbSync1.getHeight()+35, 120, 30);
			jbSync2.addActionListener(this);
		}
		this.setSize(width, icon1.getIconHeight()+120);	
	}
	
	/**
	 * GUI builder for DIALOG_EQUAL type dialog.<br/>
	 * 3 buttons :<br/>
	 * - 'OK' button to do nothing,<br/>
	 * - server to client forced synchro,<br/>
	 * - client to server forced synchro.
	 * @param title dialog title
	 * @param msg1 first line of the message
	 * @param msg2 second line of the message
	 * @param icon1 image button choice 1
	 * @param icon2 image button choice 2
	 */
	protected void BuildEqualQuestionUI(String title, String msg1, String msg2, ImageIcon icon1, ImageIcon icon2) {
		int iTxtSizeMax = 0;
		FontMetrics fm = null;
		{
			jlblMsg1 = new JLabel();
			getContentPane().add(jlblMsg1);
			fm = jlblMsg1.getFontMetrics(jlblMsg1.getFont());
			iTxtSizeMax = iTxtSizeMax < fm.stringWidth(msg1) ? fm.stringWidth(msg1) : iTxtSizeMax;
			jlblMsg1.setBounds(20, 20, fm.stringWidth(msg1), 20);
			jlblMsg1.setText(msg1);
		}
		{
			jlblMsg2 = new JLabel();
			getContentPane().add(jlblMsg2);
			fm = jlblMsg2.getFontMetrics(jlblMsg2.getFont());
			iTxtSizeMax = iTxtSizeMax < fm.stringWidth(msg2) ? fm.stringWidth(msg2) : iTxtSizeMax;
			jlblMsg2.setBounds(20, 50, fm.stringWidth(msg2), 20);
			jlblMsg2.setText(msg2);
		}
		int width = Math.max(iTxtSizeMax+40, 600);
		{
			jbCancel = new JButton();
			getContentPane().add(jbCancel);
			jbCancel.setText("Ok, No Sync");
			jbCancel.setBounds(20, 80, 120, 40);
			jbCancel.addActionListener(this);
			jbCancel.requestFocus();
		}
		{
			jbSync1 = new JButton();
			getContentPane().add(jbSync1);
			// resize the icon
			Image img = icon1.getImage();
			Image img2 = img.getScaledInstance(110, 25, Image.SCALE_SMOOTH);
			jbSync1.setIcon(new ImageIcon(img2));
			jbSync1.setBounds(width - 280, 85, 120, 30);
			jbSync1.addActionListener(this);
		}
		{
			jbSync2 = new JButton();
			getContentPane().add(jbSync2);
			// resize the icon
			Image img = icon2.getImage();
			Image img2 = img.getScaledInstance(110, 25, Image.SCALE_SMOOTH);
			jbSync2.setIcon(new ImageIcon(img2));
			jbSync2.setBounds(width - 140, 85, 120, 30);
			jbSync2.addActionListener(this);
		}
		this.setSize(width, 180);
	}

	/**
	 * Build and show a synchronization dialog box.<br/>
	 * @see #BuildSyncQuestionUI(String, String, String, ImageIcon, ImageIcon)
	 * @param title dialog title
	 * @param msg1 first line of dialog message
	 * @param msg2 second line of dialog message
	 * @param icon1 image button main choice
	 * @param icon2 image button alternative choice
	 * @return the selected action
	 */
	public static int showWowSyncQuestion(String title, String msg1, String msg2, ImageIcon icon1, ImageIcon icon2) {
		WowSyncDialog f = new WowSyncDialog(title, msg1, msg2, icon1, icon2, WowSyncDialog.DIALOG_SYNC);
		f.setVisible(true);
		if (f.exit_status == WOWSYNCDIALOG_CHOICE2) {
			int ret = JOptionPane.showConfirmDialog(null, "Confirm your choice ...", "Force synchronization", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, icon2);
			if (ret == JOptionPane.YES_OPTION) return WOWSYNCDIALOG_CHOICE2;
			return WOWSYNCDIALOG_NOACTION;
		}
		return f.exit_status;
	}

	/**
	 * Build and show a synchronization dialog box (with already synchronized items).<br/>
	 * @see #BuildEqualQuestionUI(String, String, String, ImageIcon, ImageIcon)
	 * @param title dialog title
	 * @param msg1 first line of dialog message
	 * @param msg2 second line of dialog message
	 * @param icon1 image button choice 1
	 * @param icon2 image button choice 2
	 * @return the selected action
	 */
	public static int showWowEqualQuestion(String title, String msg1, String msg2, ImageIcon icon1, ImageIcon icon2) {
		WowSyncDialog f = new WowSyncDialog(title, msg1, msg2, icon1, icon2, WowSyncDialog.DIALOG_EQUAL);
		f.setVisible(true);
		if (f.exit_status == WOWSYNCDIALOG_CHOICE1) {
			int ret = JOptionPane.showConfirmDialog(null, "Confirm your choice ...", "Force synchronization", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, icon1);
			if (ret == JOptionPane.YES_OPTION) return WOWSYNCDIALOG_CHOICE1;
		}
		if (f.exit_status == WOWSYNCDIALOG_CHOICE2) {
			int ret = JOptionPane.showConfirmDialog(null, "Confirm your choice ...", "Force synchronization", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, icon2);
			if (ret == JOptionPane.YES_OPTION) return WOWSYNCDIALOG_CHOICE2;
		}
		return WOWSYNCDIALOG_NOACTION;
	}

	/**
	 * Entry point for testing purpose.
	 */
	public static void main(String[] args) {
		ImageIcon img1 = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/serv2wow.png"));
		ImageIcon img2 = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/wow2serv.png"));
		int ret = showWowSyncQuestion("titre", "message1", "message2 plus long plus dur que le message 1", img1, img2);
		System.out.println("Ret = "+ret);
		ret = showWowEqualQuestion("titre", "message1", "message2 plus long plus dur que le message 1", img1, img2);
		System.out.println("Ret = "+ret);
	}

	/**
	 * Event management with return choices.
	 * @param evt simple action event
	 */
	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == jbCancel) {
			exit_status = WOWSYNCDIALOG_NOACTION;
			this.dispose();
		}
		if (evt.getSource() == jbSync1) {
			exit_status = WOWSYNCDIALOG_CHOICE1;
			this.dispose();
		}
		if (evt.getSource() == jbSync2) {
			exit_status = WOWSYNCDIALOG_CHOICE2;
			this.dispose();
		}
	}

}
