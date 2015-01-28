package wowsync;

import javax.swing.ProgressMonitor;

import com.jcraft.jsch.SftpProgressMonitor;

public class WowSyncProgressMonitor implements SftpProgressMonitor {
	private ProgressMonitor monitor;
	private long count=0;
	private long max=0;
	private long percent=-1;
	
	public void init(int op, String src, String dest, long max){
		this.max=max;
		monitor=new ProgressMonitor(null, 
				((op==SftpProgressMonitor.PUT)? 
						"put" : "get")+": "+src, 
						"",  0, (int)max);
		count=0;
		percent=-1;
		monitor.setProgress((int)this.count);
		monitor.setMillisToDecideToPopup(1000);
	}
	
	public boolean count(long count){
		this.count+=count;

		if(percent>=this.count*100/max){ return true; }
		percent=this.count*100/max;

		monitor.setNote("Completed "+this.count+"("+percent+"%) out of "+max+".");     
		monitor.setProgress((int)this.count);

		return !(monitor.isCanceled());
	}
	
	public void end(){
		monitor.close();
	}
}
