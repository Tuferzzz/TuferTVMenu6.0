package tufer.com.menutest.UIActivity.network.networkfove;

import android.view.KeyEvent;

public class MacAddressSettings extends NetworkSettings{
	private NetworkSettingsActivity mActivity;
	private MacAddressSettingsHolder mWiredMacAddressSettingsHolder;
	private ConnectivityListener mConnectivityListener;
	public MacAddressSettings(NetworkSettingsActivity activity) {
		super(activity);
		this.mActivity = activity;
		mWiredMacAddressSettingsHolder = new MacAddressSettingsHolder(activity);

	}
	
	public void onExit() {
		
	}

	public boolean onKeyEvent(int keyCode, KeyEvent event) {
		return true;
	}

	public void onFocusChange(boolean flag) {
		// TODO Auto-generated method stub
		
	}

	public void setVisible(boolean b) {
		mWiredMacAddressSettingsHolder.setVisible(b);
		
	}

	

}
