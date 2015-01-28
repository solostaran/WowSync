package wowsync;

import javax.swing.SwingUtilities;

public class WowSync {

	public final static String WOWSYNC = "WowSync";
	public static boolean DEBUG = false;
	public static String VERSION = "1.1";

	static private WowSyncModel wsm = null;

	/**
	 * Launch the application, config or synchronization
	 * @param args
	 */
	public static void main(String[] args) {
		wsm = new WowSyncModel();
		wsm.ReadConfigurationFile();
		if (args.length == 1 && args[0].equals("DEBUG")) DEBUG = true;
		if (args.length == 2 && args[1].equals("DEBUG")) DEBUG = true;
		if ((args.length > 0 && args[0].equals("config")) || !wsm.isConfigLoaded()) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					WowSyncUI inst = new WowSyncUI(wsm);
					inst.setLocationRelativeTo(null);
					inst.setVisible(true);
				}
			});
		} else {
			if (!wsm.isConfigLoaded()) return;

			Thread wst = new WowSyncThread(wsm);
			wst.start();
		}
	}
}
