/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tufer.com.menutest.UIActivity.network.networkfove;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.EthernetManager;
import android.net.IpConfiguration;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.NetworkInfo;
import android.net.StaticIpConfiguration;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.os.INetworkManagementService;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;


/**
 * Listens for changes to the current connectivity status.
 */
public class ConnectivityListener {

    public interface Listener {
        void onConnectivityChange(Intent intent);
    }
   
    public interface WifiNetworkListener {
        void onWifiListChanged();
    }
    private INetworkManagementService mNMService;
    private static final String TAG = "ConnectivityListener";
    private static final boolean DEBUG = false;

    class WifiReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            for (WifiNetworkListener listener : mWifiNetworkListeners) {
                listener.onWifiListChanged();
            }
        }
    }

    private final Context mContext;
    private final Listener mListener;
    private final IntentFilter mFilter;
    private final BroadcastReceiver mReceiver;
    private boolean mIsRegistered;
   
    private final ConnectivityManager mConnectivityManager;
    private final WifiManager mWifiManager;
    private WifiReceiver mWifiReceiver;
    private final EthernetManager mEthernetManager;
    private final ArrayList<WifiNetworkListener> mWifiNetworkListeners =
            new ArrayList<WifiNetworkListener>();
    private int mState =Constants.ETHERNET_STATE_INITIAL;
    private final AtomicBoolean mStarted = new AtomicBoolean(false);
    public static String mIface = "";
    public static class ConnectivityStatus {
        public static final int NETWORK_NONE = 1;
        public static final int NETWORK_WIFI_OPEN = 3;
        public static final int NETWORK_WIFI_SECURE = 5;
        public static final int NETWORK_ETHERNET = 7;
      
        public int mNetworkType;
        public String mWifiSsid;
        public int mWifiSignalStrength;

        boolean isEthernetConnected() { return mNetworkType == NETWORK_ETHERNET; }
        boolean isWifiConnected() {
            return mNetworkType == NETWORK_WIFI_OPEN ||  mNetworkType == NETWORK_WIFI_SECURE;
        }
        
        @Override
        public String toString() {
            return new StringBuilder()
                .append("mNetworkType ").append(mNetworkType)
                .append("  miWifiSsid ").append(mWifiSsid)
                .append("  mWifiSignalStrength ").append(mWifiSignalStrength)
                .toString();
            
        }
    };

    private ConnectivityStatus mConnectivityStatus = new ConnectivityStatus();
    
    public ConnectivityListener(Context context, Listener listener) {
        mContext = context;
        mConnectivityManager = (ConnectivityManager) mContext.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        mEthernetManager = (EthernetManager) mContext.getSystemService(Context.ETHERNET_SERVICE);
        IBinder b = ServiceManager.getService(Context.NETWORKMANAGEMENT_SERVICE);
        mNMService = INetworkManagementService.Stub.asInterface(b);
        mListener = listener;
        mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        //mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION_IMMEDIATE);
        mFilter.addAction(ConnectivityManager.INET_CONDITION_ACTION);
        mFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
     
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (DEBUG) {
                    Log.d(TAG, "Connectivity change!");
                }
                if (updateConnectivityStatus()) {
                    mListener.onConnectivityChange(intent);
                }
            }
        };
    }
 
    public void start() {
        if (!mIsRegistered) {
            boolean hasChanged = updateConnectivityStatus();
            mContext.registerReceiver(mReceiver, mFilter);
            mIsRegistered = true;
        }
    }
 
    public void stop() {
        if (mIsRegistered) {
            mContext.unregisterReceiver(mReceiver);
            mIsRegistered = false;
        }
        if (mWifiReceiver != null) {
            mContext.unregisterReceiver(mWifiReceiver);
            mWifiReceiver = null;
        }
    }
   
    public void startScanningWifi(WifiNetworkListener wifiNetworkListener) {
        if (!mWifiNetworkListeners.contains(wifiNetworkListener)) {
            mWifiNetworkListeners.add(wifiNetworkListener);
            if (mWifiNetworkListeners.size () == 1) {
                mWifiReceiver = new WifiReceiver();
              
                mContext.registerReceiver(mWifiReceiver, new IntentFilter(
                        WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                mWifiManager.startScan();
            }
        }
    }

    public void stopScanningWifi(WifiNetworkListener wifiNetworkListener) {
        mWifiNetworkListeners.remove(wifiNetworkListener);
        if (mWifiNetworkListeners.isEmpty() && mWifiReceiver != null) {
            mContext.unregisterReceiver(mWifiReceiver);
            mWifiReceiver = null;
        }
    }

    public ConnectivityStatus getConnectivityStatus() {
        return mConnectivityStatus;
    }

    
    public String getWifiIpAddress() {
        if (isWifiConnected()) {
            WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
            int ip = wifiInfo.getIpAddress();
            return String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff),
                    (ip >> 16 & 0xff), (ip >> 24 & 0xff));
        } else {
            return "";
        }
    }
    
    public boolean isWifiConnected() {
        WifiManager wifiManager = mWifiManager;
        // wifi is disabled
        if (!wifiManager.isWifiEnabled()) {
            return false;
        }

        // wifi have not connected
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info == null || info.getSSID() == null
                || info.getNetworkId() == WifiConfiguration.INVALID_NETWORK_ID) {
            return false;
        }

        return true;
    }
    /**
     * Return the MAC address of the currently connected Wifi AP.
     */
    public String getWifiMacAddress() {
        if (mConnectivityStatus.isWifiConnected()) {
            WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
            return wifiInfo.getMacAddress();
        } else {
            return "";
        }
    }

    public String getEthernetMacAddress() {
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (networkInfo == null ||networkInfo.getType() != ConnectivityManager.TYPE_ETHERNET) {
            return "";
        } else {
            return networkInfo.getExtraInfo();
        }
    }

    public String getEthernetIpAddress() {
        LinkProperties linkProperties =
                mConnectivityManager.getLinkProperties(ConnectivityManager.TYPE_ETHERNET);
        StaticIpConfiguration staticConfig = new StaticIpConfiguration();
			
		if(mConnectivityManager.getActiveNetworkInfo() != null&&
                linkProperties!=null){
			for (LinkAddress linkAddress: linkProperties.getAllLinkAddresses()) {
				InetAddress address = linkAddress.getAddress();
            
				if (address instanceof Inet4Address) {
            	
					return address.getHostAddress();
				}
			}
		}
        // IPv6 address will not be shown like WifiInfo internally does.
        return null;
    }
    public String getDnsadd(){
    	  NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
          if (networkInfo == null ||networkInfo.getType() != ConnectivityManager.TYPE_ETHERNET) {
              return "";
          } else {
              return networkInfo.getReason();
          }
    }
    
    public String getGetway(){
        if (mConnectivityStatus.isWifiConnected()) {
            WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
            return wifiInfo.getWifiSsid().getHexString();
        } else {
            return "";
        }
    }
    
    
    
    public int getWifiSignalStrength(int maxLevel) {
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        return WifiManager.calculateSignalLevel(wifiInfo.getRssi(), maxLevel);
    }

    public void forgetWifiNetwork() {
        int networkId = getWifiNetworkId();
        if (networkId != -1) {
            mWifiManager.forget(networkId, null);
        }
    }

    public int getWifiNetworkId() {
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        if (wifiInfo != null) {
            return wifiInfo.getNetworkId();
        } else {
            return -1;
        }
    }

    public WifiConfiguration getWifiConfiguration() {
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        if (wifiInfo != null) {
            int networkId = wifiInfo.getNetworkId();
            List<WifiConfiguration> configuredNetworks = mWifiManager.getConfiguredNetworks();
            if (configuredNetworks != null) {
                for (WifiConfiguration configuredNetwork : configuredNetworks) {
                    if (configuredNetwork.networkId == networkId) {
                        return configuredNetwork;
                      
                    }
                }
            }
        }
        return null;
    }

    /**
     * Return a list of wifi networks. Ensure that if a wifi network is connected that it appears
     * as the first item on the list.
     */
    public List<ScanResult> getAvailableNetworks() {
        WifiInfo connectedWifiInfo = mWifiManager.getConnectionInfo();
        String currentConnectedSSID = connectedWifiInfo == null ? "" : connectedWifiInfo.getSSID();
        currentConnectedSSID = WifiInfo.removeDoubleQuotes(currentConnectedSSID);
        WifiSecurity currentConnectedSecurity = WifiConfigHelper.getCurrentConnectionSecurity(
                mWifiManager, connectedWifiInfo);

        // TODO : Refactor with similar code in SelectFromListWizard
        final List<ScanResult> results = mWifiManager.getScanResults();

        if (results.size() == 0) {
            Log.w(TAG, "No results found! Initiate scan...");
            mWifiManager.startScan();
        }

        final HashMap<Pair<String, WifiSecurity>, ScanResult> consolidatedScanResults =
                new HashMap<Pair<String, WifiSecurity>, ScanResult>();
        HashMap<Pair<String, WifiSecurity>, Boolean> specialNetworks = new HashMap<
                Pair<String, WifiSecurity>, Boolean>();
        for (ScanResult result : results) {
            if (TextUtils.isEmpty(result.SSID)) {
                continue;
            }

            Pair<String, WifiSecurity> key = Pair.create(
                    result.SSID, WifiSecurity.getSecurity(result));
            ScanResult existing = consolidatedScanResults.get(key);

            if (WifiConfigHelper.areSameNetwork(mWifiManager, result, connectedWifiInfo)) {
                // The currently connected network should always be included.
                consolidatedScanResults.put(key, result);
                specialNetworks.put(key, true);
            } else {
                if (existing == null ||
                        (!specialNetworks.containsKey(key) && existing.level < result.level)) {
                    consolidatedScanResults.put(key, result);
                }
            }
        }

        ArrayList<ScanResult> networkList = new ArrayList<ScanResult>(
                consolidatedScanResults.size());
        networkList.addAll(consolidatedScanResults.values());
        ScanResultComparator comparator = connectedWifiInfo == null ? new ScanResultComparator() :
                new ScanResultComparator(currentConnectedSSID, currentConnectedSecurity);
        Collections.sort(networkList, comparator);
        return networkList;
    }

    public IpConfiguration getIpConfiguration() {
        return mEthernetManager.getConfiguration();
    }

    private boolean isSecureWifi(WifiInfo wifiInfo) {
        if (wifiInfo == null)
            return false;
        int networkId = wifiInfo.getNetworkId();
        List<WifiConfiguration> configuredNetworks = mWifiManager.getConfiguredNetworks();
        if (configuredNetworks != null) {
            for (WifiConfiguration configuredNetwork : configuredNetworks) {
                if (configuredNetwork.networkId == networkId) {
                    return configuredNetwork.allowedKeyManagement.get(KeyMgmt.WPA_PSK) ||
                        configuredNetwork.allowedKeyManagement.get(KeyMgmt.WPA_EAP) ||
                        configuredNetwork.allowedKeyManagement.get(KeyMgmt.IEEE8021X);
                }
            }
        }
        return false;
    }

    private boolean setNetworkType(int networkType) {
        boolean hasChanged = mConnectivityStatus.mNetworkType != networkType;
        mConnectivityStatus.mNetworkType = networkType;
        return hasChanged;
    }

    public  boolean updateConnectivityStatus() {
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return setNetworkType(ConnectivityStatus.NETWORK_NONE);
        } else {
            switch (networkInfo.getType()) {
                case ConnectivityManager.TYPE_WIFI: {
                    boolean hasChanged;

                    // Determine if this is an open or secure wifi connection.
                    WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
                    if (isSecureWifi(wifiInfo)) {
                        hasChanged = setNetworkType(ConnectivityStatus.NETWORK_WIFI_SECURE);
                    } else {
                        hasChanged = setNetworkType(ConnectivityStatus.NETWORK_WIFI_OPEN);
                    }

                    // Find the SSID of network.
                    String ssid = null;
                    if (wifiInfo != null) {
                        ssid = wifiInfo.getSSID();
                        if (ssid != null) {
                            ssid = WifiInfo.removeDoubleQuotes(ssid);
                        }
                    }
                    if (!TextUtils.equals(mConnectivityStatus.mWifiSsid, ssid)) {
                        hasChanged = true;
                        mConnectivityStatus.mWifiSsid = ssid;
                    }
                    // Calculate the signal strength.
                    int signalStrength;
                    if (wifiInfo != null) {
                        // Calculate the signal strength between 0 and 3.
                        signalStrength = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 4);
                    } else {
                        signalStrength = 0;
                    }
                    if (mConnectivityStatus.mWifiSignalStrength != signalStrength) {
                        hasChanged = true;
                        mConnectivityStatus.mWifiSignalStrength = signalStrength;
                    }
                    return hasChanged;
                }

                case ConnectivityManager.TYPE_ETHERNET:
                    return setNetworkType(ConnectivityStatus.NETWORK_ETHERNET);

                default:
                    return setNetworkType(ConnectivityStatus.NETWORK_NONE);
            }
        }
    }
    public synchronized void setState(int state) {
        enforceChangePermission();

        Log.i(TAG, "setState from mState=" + mState + " to state=" + state);
        if (mState != state) {
            mState = state;
            if (state ==Constants.ETHERNET_STATE_DISABLED) {
                setPersistedState(Constants.ETHERNET_STATE_DISABLED);
               stopInterface();
                mStarted.set(false);
            } else {
                setPersistedState(Constants.ETHERNET_STATE_ENABLED);
//                mTracker.stop();
//                mTracker.start(mContext, mHandler);
                mStarted.set(true);
            }
        }
        
    }
    
    private void setPersistedState(int state) {
        final ContentResolver cr = mContext.getContentResolver();
    //    Settings.Global.putInt(cr, Settings.Global.ETHERNET_ON, state);
    }
    private void enforceChangePermission() {
        mContext.enforceCallingOrSelfPermission(
                android.Manifest.permission.CHANGE_NETWORK_STATE,
                "EthernetService");
    }
    public synchronized void stopInterface() {
        
		if (!mIface.isEmpty()) {
            try {
                mNMService.setInterfaceDown(mIface);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        this.stop();
    }
}
