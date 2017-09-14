package tufer.com.menutest.UIActivity.network.networkfove;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.IpConfiguration;
import android.net.IpConfiguration.IpAssignment;
import android.os.Build;
import android.os.Handler;
import android.os.SystemProperties;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.CheckBox;
import tufer.com.menutest.UIActivity.network.networkfove.ProxySettings.ProxyInfo;

public class EthernetSetting extends NetworkSettings implements INetworkSettingsListener ,ConnectivityListener.Listener {
	 private NetworkSettingsActivity mNetworkSettingsActivity;
	 private CheckBox mDHCPchboxs;

	 private CheckBox mstaticipchboxs;
	 private CheckBox misethernetChboxs;

	 private CheckBox mIpsetingchboxs;
	 private ConnectivityListener mConnectivityListener;
	 private  ConnectivityManager mConnectivityManager;
	    private EthernetSettingsholders mEthernetHolder;
	    private Handler mhandler=new Handler();

	public EthernetSetting(NetworkSettingsActivity networkSettingsActivity) {
		super(networkSettingsActivity);
		mNetworkSettingsActivity = networkSettingsActivity;
		mEthernetHolder=new EthernetSettingsholders(mNetworkSettingsActivity);
		mDHCPchboxs=mEthernetHolder.getmDHCPchboxs();
		mstaticipchboxs=mEthernetHolder.getMstaticipchboxs();
		mIpsetingchboxs=mEthernetHolder.getmIpsetingchboxs();
		misethernetChboxs=mEthernetHolder.getMisethernetChboxs();
		mConnectivityListener=new ConnectivityListener(networkSettingsActivity, this);
		setlisert();
		xianshi();
		String RemoveFocusMode = SystemProperties.get("dhcp.eth0.dns1");
		String RemoveFocusModeget = SystemProperties.get("dhcp.eth0.gateway");
		String RemoveFocusModes = SystemProperties.get("dhcp.eth0.dns2");
		Log.d("yesuo",RemoveFocusMode+"dns1"+RemoveFocusModeget+"gateaway"+RemoveFocusModes+"dns2");
	}

	
	private void xianshi() {
		if(mConnectivityListener.getEthernetIpAddress()!=null){
			
		if(mDHCPchboxs.isChecked()){
		
			String ip = mConnectivityListener.getEthernetIpAddress();
			String netmask = SystemProperties.get("dhcp.eth0.mask");
			String defaultWay = SystemProperties.get("dhcp.eth0.gateway");
			String firstdns = SystemProperties.get("dhcp.eth0.dns1");
			String secDns = SystemProperties.get("dhcp.eth0.dns2");
			mEthernetHolder.Dhsettext(ip, netmask,defaultWay, firstdns, secDns );
			mEthernetHolder.setV4EditTextWritable(false);
		
		}
		}else{
			String ip = null;
			String netmask = null;
			String defaultWay = null;
			String firstdns = null;
			String secDns =null;
			mEthernetHolder.Dhsettext(ip, netmask,defaultWay, firstdns, secDns );
		}
	}

	private void setlisert() {

		if(mConnectivityListener.getEthernetIpAddress()!=null){
			misethernetChboxs.setChecked(true);
		}else{
			misethernetChboxs.setChecked(false);
		}

		if(mstaticipchboxs.isChecked()){
			mDHCPchboxs.setChecked(false);
			mEthernetHolder.setV4EditTextWritable(true);
			
		}else{
			mDHCPchboxs.setChecked(true);
			mEthernetHolder.setV4EditTextWritable(false);
		}

		if(mDHCPchboxs.isChecked()){
			mstaticipchboxs.setChecked(false);
			mEthernetHolder.setV4EditTextWritable(false);
		}else{
			mstaticipchboxs.setChecked(true);
			mEthernetHolder.setV4EditTextWritable(true);
		}
	
		 IpConfiguration IPConfiguration = mConnectivityListener.getIpConfiguration();
		if(IPConfiguration!=null){
			if(IPConfiguration.getIpAssignment() == IpAssignment.STATIC){
				mstaticipchboxs.setChecked(true);
				mDHCPchboxs.setChecked(false);
				
			}else{
				mstaticipchboxs.setChecked(false);
				mDHCPchboxs.setChecked(true);
			}
		}else{
			misethernetChboxs.setChecked(false);
		}
		
		
		
	}

	@Override
	public void onExit() {
		
	}

	@Override
	public boolean onKeyEvent(int keyCode, KeyEvent keyEvent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onProxyChanged(boolean enabled, ProxyInfo proxyInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFocusChange(boolean hasFocus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWifiHWChanged(boolean isOn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectivityChange(Intent intent) {
		// TODO Auto-generated method stub
		
	}
	 public void setVisible(boolean visible) {
	        Log.d("yesuo", "visible, " + visible);
	        mEthernetHolder.setEthernetVisible(visible);
	    
	    }

}
