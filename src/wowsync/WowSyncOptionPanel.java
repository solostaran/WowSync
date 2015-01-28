package wowsync;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WowSyncOptionPanel extends JPanel {
	private static final long serialVersionUID = 605323723935463967L;
	private Image img;
	
	public WowSyncOptionPanel(String msg1, String msg2, Image img) {
		BoxLayout bl = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(bl);
		FontMetrics fm = this.getFontMetrics(this.getFont());
		this.setSize(200, fm.getHeight()*2 + 68);
		JLabel lbl1 = new JLabel(msg1);
		this.add(lbl1);
		JLabel lbl2 = new JLabel(msg2);
		this.add(lbl2);
		this.img = img;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (img != null) {
			FontMetrics fm = this.getFontMetrics(this.getFont());
			g.drawImage(img, 0, fm.getHeight()*2+20, this);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Image img = Toolkit.getDefaultToolkit().getImage("images/serv2wow.png");
		WowSyncOptionPanel wsop = new WowSyncOptionPanel("message1", "message2", img);
		f.setSize(220,150);
		f.setContentPane(wsop);
		f.setVisible(true);
	}

}
