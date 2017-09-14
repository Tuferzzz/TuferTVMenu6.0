//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2012 MStar Semiconductor, Inc. All rights reserved.
// All software, firmware and related documentation herein ("MStar Software") are
// intellectual property of MStar Semiconductor, Inc. ("MStar") and protected by
// law, including, but not limited to, copyright law and international treaties.
// Any use, modification, reproduction, retransmission, or republication of all
// or part of MStar Software is expressly prohibited, unless prior written
// permission has been granted by MStar.
//
// By accessing, browsing and/or using MStar Software, you acknowledge that you
// have read, understood, and agree, to be bound by below terms ("Terms") and to
// comply with all applicable laws and regulations:
//
// 1. MStar shall retain any and all right, ownership and interest to MStar
//    Software and any modification/derivatives thereof.
//    No right, ownership, or interest to MStar Software and any
//    modification/derivatives thereof is transferred to you under Terms.
//
// 2. You understand that MStar Software might include, incorporate or be
//    supplied together with third party's software and the use of MStar
//    Software may require additional licenses from third parties.
//    Therefore, you hereby agree it is your sole responsibility to separately
//    obtain any and all third party right and license necessary for your use of
//    such third party's software.
//
// 3. MStar Software and any modification/derivatives thereof shall be deemed as
//    MStar's confidential information and you agree to keep MStar's
//    confidential information in strictest confidence and not disclose to any
//    third party.
//
// 4. MStar Software is provided on an "AS IS" basis without warranties of any
//    kind. Any warranties are hereby expressly disclaimed by MStar, including
//    without limitation, any warranties of merchantability, non-infringement of
//    intellectual property rights, fitness for a particular purpose, error free
//    and in conformity with any international standard.  You agree to waive any
//    claim against MStar for any loss, damage, cost or expense that you may
//    incur related to your use of MStar Software.
//    In no event shall MStar be liable for any direct, indirect, incidental or
//    consequential damages, including without limitation, lost of profit or
//    revenues, lost or damage of data, and unauthorized system use.
//    You agree that this Section 4 shall still apply without being affected
//    even if MStar Software has been modified by MStar in accordance with your
//    request or instruction for your use, except otherwise agreed by both
//    parties in writing.
//
// 5. If requested, MStar may from time to time provide technical supports or
//    services in relation with MStar Software to you for your use of
//    MStar Software in conjunction with your or your customer's product
//    ("Services").
//    You understand and agree that, except otherwise agreed by both parties in
//    writing, Services are provided on an "AS IS" basis and the warranty
//    disclaimer set forth in Section 4 above shall apply.
//
// 6. Nothing contained herein shall be construed as by implication, estoppels
//    or otherwise:
//    (a) conferring any license or right to use MStar name, trademark, service
//        mark, symbol or any other identification;
//    (b) obligating MStar or any of its affiliates to furnish any person,
//        including without limitation, you and your customers, any assistance
//        of any kind whatsoever, or any information; or
//    (c) conferring any license or right under any intellectual property right.
//
// 7. These terms shall be governed by and construed in accordance with the laws
//    of Taiwan, R.O.C., excluding its conflict of law rules.
//    Any and all dispute arising out hereof or related hereto shall be finally
//    settled by arbitration referred to the Chinese Arbitration Association,
//    Taipei in accordance with the ROC Arbitration Law and the Arbitration
//    Rules of the Association by three (3) arbitrators appointed in accordance
//    with the said Rules.
//    The place of arbitration shall be in Taipei, Taiwan and the language shall
//    be English.
//    The arbitration award shall be final and binding to both parties.
//
//******************************************************************************
//<MStar Software>

package tufer.com.menutest.UIActivity.network.networkfove;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.IpConfiguration;
import android.net.IpConfiguration.IpAssignment;
import android.net.LinkAddress;
import android.net.StaticIpConfiguration;
import com.mstar.android.pppoe.PppoeManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemProperties;
import android.util.Log;
import android.view.KeyEvent;

import com.mstar.android.wifi.MWifiManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetAddress;
import java.util.Iterator;

import tufer.com.menutest.R;
import tufer.com.menutest.UIActivity.network.networkfove.ProxySettings.ProxyInfo;
import tufer.com.menutest.Util.Tools;

/**
 * network status setting.
 */
public class NetworkStatusSettings extends NetworkSettings implements INetworkSettingsListener ,ConnectivityListener.Listener {

	private static final String TAG = "MSettings.NetworkStatus";

	private static final int TYPE_ETHERNET = 0;
	private static final int TYPE_WIFI = 1;
	private static final int TYPE_PPPoE = 2;

	private static final String ETHERNET_STATE_CHANGED_ACTION = "ethernetchanged";

	private NetworkSettingsActivity mActivity;

	private NetworkStatusHolder mNetworkStatusHolder;

	private ConnectivityListener mConnectivityListener;
	private WifiManager mWifiManager;
	private Ethernetnet methnet;
	private  ConnectivityManager mConnectivityManager;
	//   private PppoeManager mPPPoEManager;
	private Resources mResources;

	private int[] state = {
			R.string.wire_connect, R.string.wireless_connect, R.string.pppoe_connect
	};

	public NetworkStatusSettings(NetworkSettingsActivity networkSettingsActivity) {
		super(networkSettingsActivity);
		this.mActivity = networkSettingsActivity;
		this.mWifiManager = getWifiManager();
		//       this.mPPPoEManager = getPPPoEManager();
		mConnectivityListener=new ConnectivityListener(networkSettingsActivity,this);
		mNetworkStatusHolder = new NetworkStatusHolder(networkSettingsActivity);
		mResources = mActivity.getResources();
		methnet=new Ethernetnet();
		
		registerReceiver();
		mNetworkStatusHolder.getwifiMactext().setText(mActivity.getString(R.string.mac_wireless_address)+" "+getWifiMac(mActivity));

	}

	@Override
	public void onExit() {
		mActivity.unregisterReceiver(mNetworkChangedReceiver);
		mActivity.unregisterReceiver(mWifiHWReceiver);
	}


	@Override
	public void onWifiHWChanged(boolean isOn) {
		Log.d(TAG, "isOn, " + isOn);
		if (!isOn) {
			refreshNetworkStatus();
		}
	}

	@Override
	public boolean onKeyEvent(int keyCode, KeyEvent keyEvent) {
		return false;
	}

	@Override
	public void onProxyChanged(boolean enabled, ProxyInfo proxyInfo) {

	}

	@Override
	public void onFocusChange(boolean hasFocus) {

	}

	/**
	 * hide/show the layout.
	 *
	 * @param visible if visible.
	 */

	public void setVisible(boolean visible) {
		mNetworkStatusHolder.setVisible(visible);
		if (visible) {
			refreshNetworkStatus();
		}
	}

	/**
	 * refresh network status.
	 */
	public void refreshNetworkStatus() {
		if (isWifiConnected()) {
			refreshWifiStatus();
			//        } else if (mPPPoEManager.PppoeGetStatus() == PPPOE_STA.CONNECTED) {
			//            refreshPPPoEStatus();
		} else {
			refreshEthernetStatus();

		}
	}

	
	private void   refreshEthernetStatus(){
		Log.d(TAG, "refreshEthernetStatus");
		// show connect type.
		mNetworkStatusHolder.refreshConnectType(mResources.getString(state[TYPE_ETHERNET]));
		// show network information.
		StringBuffer networkInformation = new StringBuffer();
		ConnectivityListener.ConnectivityStatus status =
				mConnectivityListener.getConnectivityStatus();	
		
		if (!mConnectivityListener.getEthernetIpAddress().isEmpty()) {

			networkInformation.append(mResources.getString(R.string.eth_able));
			IpConfiguration ipConfiguration = mConnectivityListener.getIpConfiguration();
			if (ipConfiguration != null) {
				
				String netmask=null;
				String defaultWay=null;
				String firstdns=null;
				String secDns=null;
				if (ipConfiguration.getIpAssignment() == IpAssignment.STATIC) {

				



					String macAddr = mConnectivityListener.getEthernetMacAddress();
					String ip = mConnectivityListener.getEthernetIpAddress();
					StaticIpConfiguration cifi = ipConfiguration.getStaticIpConfiguration();
					
					if(cifi!=null) {
						LinkAddress net = cifi.ipAddress;

						if(net!=null){
							int nemask = net.describeContents();
							int neamask = net.getNetworkPrefixLength();
							int nesmask = net.getPrefixLength();
						}
						InetAddress way = cifi.gateway;
						if(way!=null){
							defaultWay=way.getHostAddress();
						}
						String waknet = methnet.getWosnet();
						if(waknet!=null){
							netmask=waknet;
						}else{
						if(ip!=null&&defaultWay!=null){
							netmask= Tools.buildUpNetmask(ip,defaultWay);
							Log.d("yesuo",netmask+"net");
						}
						}
						Iterator<InetAddress> dns = cifi.dnsServers.iterator();
						if(dns.hasNext()){
							firstdns = dns.next().getHostAddress();
							if (!Tools.matchIP(firstdns)) {
								firstdns = null;
							}
						}
						if(dns.hasNext()){
							secDns = dns.next().getHostAddress();
							if (!Tools.matchIP(secDns)) {
								secDns = null;
							}
						
					}
					Log.d("yesuo",ipConfiguration.getStaticIpConfiguration()+"netmask"+cifi.dnsServers+"dnsserver");

					String wifiInfo = assemblingInfo(macAddr, ip, netmask, defaultWay, firstdns, secDns);

				
					networkInformation.append(wifiInfo);
				
					}
				}else{
					//   misethernetChboxs.setChecked(false);
		
					String macAddr = mConnectivityListener.getEthernetMacAddress();
					if(!Tools.matchIP(macAddr)){
						macAddr=null;
					}


					 String ip = mConnectivityListener.getEthernetIpAddress();
					if(!Tools.matchIP(ip)){
						ip =null;
					}
				 netmask = SystemProperties.get("dhcp.eth0.mask");
					if(!Tools.matchIP(netmask  )){
						netmask  =null;
					}
					 defaultWay = SystemProperties.get("dhcp.eth0.gateway");
					if(!Tools.matchIP(defaultWay )){
						defaultWay =null;
					}
					 firstdns = SystemProperties.get("dhcp.eth0.dns1");
					if(!Tools.matchIP(firstdns)){
						firstdns=null;
					}
					 secDns = SystemProperties.get("dhcp.eth0.dns2");
					if(!Tools.matchIP(secDns)){
						secDns=null;
					}
					String wifiInfo = assemblingInfo(macAddr, ip, netmask, defaultWay, firstdns, secDns);

	
					networkInformation.append(wifiInfo);
			
				
				}
			} else {
		
				// ethernet can not be used.
				//                }
			}
			//                }
			// refresh network status
		

			}
			mNetworkStatusHolder.refreshNetworkStatus(networkInformation.toString());
			networkInformation.append(mResources.getString(R.string.eth_enable));
		
			if (mConnectivityListener.getEthernetIpAddress()!=null) {
				String mac = mConnectivityListener.getEthernetMacAddress();
				mNetworkStatusHolder.getethernetmactext().setText( mActivity.getString(R.string.mac_wired_address)+mac);
			}
		
		}
		//        if (isEthernetEnabled() && isNetInterfaceAvailable("eth0")) {
		//        networkInformation.append(mResources.getString(R.string.eth_able));
		//            EthernetManager ethernetManager = getEthernetManager();
		//            if (ethernetManager.isConfigured()) {
		//                EthernetDevInfo devInfo = ethernetManager.getSavedConfig();
		//                String ifName = devInfo.getIfName();
		//                String macAddr = devInfo.getMacAddress(ifName);
		//
		//                Log.d(TAG, "ifName, " + ifName);
		//                Log.d(TAG, "mac, " + devInfo.getMacAddress(ifName));
		//
		//                String ip = devInfo.getIpAddress();
		//                String netmask = devInfo.getNetMask();
		//                String defaultWay = devInfo.getRouteAddr();
		//                String firstdns = devInfo.getDnsAddr();
		//                String secDns = devInfo.getDns2Addr();
		//
		//                String wifiInfo = assemblingInfo(macAddr, ip, netmask, defaultWay, firstdns, secDns);
		//                networkInformation.append(wifiInfo);
		//            } else {
		//                // ethernet not right, do not show ip etc. .
		//            }
		//      } else {
		// ethernet can not be used.

		//          networkInformation.append(mResources.getString(R.string.eth_enable));

	


	private String assemblingInfo(String netmask, String ip, String macAddr) {
		StringBuffer netStatus = new StringBuffer();
		netStatus.append("\n\n");
		if (null != macAddr) {
			netStatus.append(mResources.getString(R.string.mac_address));
			netStatus.append(":  ");
			netStatus.append((macAddr + "\n"));
		
		}



		if (null != ip) {
			netStatus.append(mResources.getString(R.string.ip_address));
			netStatus.append(":  ");
			netStatus.append((ip + "\n"));
		}
		if (null != netmask) {
		
			netStatus.append(mResources.getString(R.string.mac_address));
			netStatus.append(":  ");
			netStatus.append((netmask + "\n"));
		}
		return netStatus.toString();
	}

	private void refreshWifiStatus() {
		Log.d("yesuo", "refreshWifiStatus");
		//        if (isEthernetEnabled()) {
		//            Log.d(TAG, "ethernet is enabled");
		//            return;
		//        }

		// show connect type:
		mNetworkStatusHolder.refreshConnectType(mResources.getString(state[TYPE_WIFI]));

		// show network information:
		StringBuffer networkInformation = new StringBuffer();
		if (mWifiManager.isWifiEnabled()) {
			WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
			if (null != wifiInfo && null != wifiInfo.getSSID()
					&& wifiInfo.getNetworkId() != WifiConfiguration.INVALID_NETWORK_ID) {
				String localSsid = wifiInfo.getSSID();
				// get wifi name.
				if (localSsid.contains("\"")) {
					networkInformation.append(mResources.getString(R.string.connect_state));
					networkInformation.append(localSsid.substring(1, localSsid.lastIndexOf("\"")));
				} else {
					networkInformation.append(mResources.getString(R.string.connect_state));
					networkInformation.append(wifiInfo.getSSID());
				}
				// get network information(ip etc.).
				String networkInfomation = getWifiNetworkInformation();
				networkInformation.append(networkInfomation);
			} else {
				// scanning wifi devices.
				networkInformation.append(mResources.getString(R.string.wifi_state_enable));
			
			}
		} else {
			// wifi can not be used.
			networkInformation.append(mResources.getString(R.string.wifi_state_enable));
		
		}
		
		// show wifi information.
		mNetworkStatusHolder.refreshNetworkStatus(networkInformation.toString());
		//		mMacAddressSettingsHolder.refreshWiredNetworkStatus(networkInformation.toString());

	}

	//    private void refreshPPPoEStatus() {
	//        Log.d(TAG, "refreshPPPoEStatus");
	//        // show connect type.
	//        mNetworkStatusHolder.refreshConnectType(mResources.getString(state[TYPE_PPPoE]));
	//
	//        // network information.
	//        StringBuffer networkInformation = new StringBuffer();
	//        PPPOE_STA currentStatus = mPPPoEManager.PppoeGetStatus();
	//        if (currentStatus == PPPOE_STA.CONNECTED) {
	//           String ip = mPPPoEManager.getIpaddr();
	//            String netmask = mPPPoEManager.getMask();
	//            String defaultWay = mPPPoEManager.getRoute();
	//            String dns1 = mPPPoEManager.getDns1();
	//            String dns2 = mPPPoEManager.getDns2();
	//            Log.d(TAG, "ip, " + ip);
	//            Log.d(TAG, " netmask, " + netmask);
	//            Log.d(TAG, " defaultWay, " + defaultWay);
	//            Log.d(TAG, " dns1, " + dns1);
	//
	//            networkInformation.append(mResources.getString(R.string.pppoe_connect_success));
	//            String pppoeInfo = assemblingInfo(null, ip, netmask, defaultWay, dns1, dns2);
	//            networkInformation.append(pppoeInfo);
	//        } else {
	//            // do not connected.
	//            networkInformation.append(mResources.getString(R.string.pppoe_disconnect));
	//        }
	//        // refresh network status
	//        mNetworkStatusHolder.refreshNetworkStatus(networkInformation.toString());
	//    }
	// 
	private String getWifiNetworkInformation() {
		WifiConfiguration config =mWifiManager.getWifiApConfiguration();
		String netmask=null;
		String defaultWay=null;
		String firstdns=null;
		String secDns=null;
		if (config != null) {
			WifiConfiguration ipConfiguration = mConnectivityListener.getWifiConfiguration();
			if(ipConfiguration.getIpAssignment() == IpAssignment.STATIC){
		
				String macAddr = mConnectivityListener.getWifiMacAddress();
				String ip = mConnectivityListener.getWifiIpAddress();
				StaticIpConfiguration cifi = ipConfiguration.getStaticIpConfiguration();

				if(cifi!=null) {
					LinkAddress net = cifi.ipAddress;
					Log.d("yesuo",net+"net");
					if(net!=null){
						int nemask = net.describeContents();
						int neamask = net.getNetworkPrefixLength();
						int nesmask = net.getPrefixLength();

					}
					InetAddress way = cifi.gateway;
					if(way!=null){
						defaultWay=way.getHostAddress();
					}
					if(ip!=null&&defaultWay!=null){
						netmask=Tools.buildUpNetmask(ip,defaultWay);
					}
					Iterator<InetAddress> dns = cifi.dnsServers.iterator();
					if(dns.hasNext()){
						firstdns = dns.next().getHostAddress();
						if (!Tools.matchIP(firstdns)) {
							firstdns = null;
						}
					}
					if(dns.hasNext()){
						secDns = dns.next().getHostAddress();
						if (!Tools.matchIP(secDns)) {
							secDns = null;
						}
					}
					return assemblingInfo(macAddr, ip, netmask, defaultWay, firstdns, secDns);

				}

			}else{
				WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
				String macAddr = null;
				String ip = null;
				if (null != wifiInfo) {
					macAddr = wifiInfo.getMacAddress();
					ip=mConnectivityListener.getWifiIpAddress();
					netmask = SystemProperties.get("dhcp.wlan0.mask");
					defaultWay = SystemProperties.get("dhcp.wlan0.gateway");
					firstdns = SystemProperties.get("dhcp.wlan0.dns1");
					String secDnsw = SystemProperties.get("dhcp.wlan0.dns2");
					if (!Tools.matchIP(secDnsw)) {
						secDns = null;
					}
				}

				return assemblingInfo(macAddr, ip, netmask, defaultWay, firstdns, secDns);

			}

		}
		return "";
	}







	private String assemblingInfo(String macAddr, String ip
			, String netmask, String gateway,
			String firstDns, String secDns
			) {
		StringBuffer netStatus = new StringBuffer();
		netStatus.append("\n\n");

		if (null != macAddr) {
			netStatus.append(mResources.getString(R.string.mac_address));
			netStatus.append(":  ");
			netStatus.append((macAddr + "\n"));
		}

		if (null != ip) {
			netStatus.append(mResources.getString(R.string.ip_address));
			netStatus.append(":  ");
			netStatus.append((ip + "\n"));
		}

		if (null != netmask) {
			netStatus.append(mResources.getString(R.string.subnet_mask));
			netStatus.append(":  ");
			netStatus.append((netmask + "\n"));
		}

		if (null != gateway) {
			netStatus.append(mResources.getString(R.string.default_geteway));
			netStatus.append(":  ");
			netStatus.append((gateway + "\n"));
		}

		if (null != firstDns) {
			netStatus.append(mResources.getString(R.string.first_dns));
			netStatus.append(":  ");
			netStatus.append((firstDns + "\n"));
		}

		if (secDns!=null) {
			netStatus.append(mResources.getString(R.string.second_dns));
			netStatus.append(":  ");
			netStatus.append((secDns + "\n"));
		}

		return netStatus.toString();
	}

	private boolean isNetInterfaceAvailable(String ifName) {
		String netInterfaceStatusFile = "/sys/class/net/" + ifName + "/carrier";
		return isStatusAvailable(netInterfaceStatusFile);
	}

	private boolean isStatusAvailable(String statusFile) {
		char st = readStatus(statusFile);
		if (st == '1') {
			return true;
		}

		return false;
	}

	private synchronized char readStatus(String filePath) {
		int tempChar = 0;
		File file = new File(filePath);
		if (file.exists()) {
			Reader reader = null;
			try {
				reader = new InputStreamReader(new FileInputStream(file));
				tempChar = reader.read();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return (char) tempChar;
	}

	private void registerReceiver() {
		IntentFilter filter = new IntentFilter();
		// ethernet state change
		filter.addAction(ETHERNET_STATE_CHANGED_ACTION);
		// wifi state change
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		// pppoe state change
		filter.addAction(PppoeManager.PPPOE_STATE_ACTION);
		mActivity.registerReceiver(mNetworkChangedReceiver, filter);

		// wifi hw changed
		IntentFilter wifiFilter = new IntentFilter();
		wifiFilter.addAction(MWifiManager.WIFI_DEVICE_ADDED_ACTION);
		wifiFilter.addAction(MWifiManager.WIFI_DEVICE_REMOVED_ACTION);
		mActivity.registerReceiver(mWifiHWReceiver, wifiFilter);
	}

	private BroadcastReceiver mNetworkChangedReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ETHERNET_STATE_CHANGED_ACTION.equals(action)) {
				refreshEthernetStatus();

			} else 
				if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {

					refreshNetworkStatus();

				} else if (PppoeManager.PPPOE_STATE_ACTION.equals(action)) {
					//                String status = intent.getStringExtra(PppoeManager.PPPOE_STATE_STATUE);
					//                Log.d(TAG, "pppoe status, " + status);
					//                if (PppoeManager.PPPOE_STATE_CONNECT.equals(status)) {
					//                    Log.d(TAG, "pppoe_connect");
					//                    refreshPPPoEStatus();
					//                }
				}
		}
	};

	private BroadcastReceiver mWifiHWReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (MWifiManager.WIFI_DEVICE_REMOVED_ACTION.equals(action)) {
				onWifiHWChanged(false);
			} else if (MWifiManager.WIFI_DEVICE_ADDED_ACTION.equals(action)) {
				onWifiHWChanged(true);
			}
		}
	};

	@Override
	public void onConnectivityChange(Intent intent) {


	}
	public static String getWifiMac(Context context){
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

		WifiInfo info = wifi.getConnectionInfo();
		String mac = info.getMacAddress();
		return mac ;
	}

}
