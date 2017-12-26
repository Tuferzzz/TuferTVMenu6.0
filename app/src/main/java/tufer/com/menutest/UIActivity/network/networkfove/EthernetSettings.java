package tufer.com.menutest.UIActivity.network.networkfove;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.EthernetManager;
import android.net.IpConfiguration;
import android.net.IpConfiguration.IpAssignment;
import android.net.LinkAddress;
import android.net.NetworkUtils;
import android.net.StaticIpConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Iterator;

import tufer.com.menutest.UIActivity.network.networkfove.ProxySettings.ProxyInfo;
import tufer.com.menutest.R;
import tufer.com.menutest.Util.Tools;


public class EthernetSettings extends NetworkSettings implements INetworkSettingsListener, ConnectivityListener.Listener, WifiManager.ActionListener {

    private static final String TAG = "MSettings.EthernetSettings";

    private NetworkSettingsActivity mNetworkSettingsActivity;

    private EthernetSettingsHolder mEthernetHolder;
    private CheckBox mEthernetToggle;
    private CheckBox mAutoIpToggle;
    private CheckBox mIPv6Toggle;

    private CheckBox mDevToggle;
    private boolean DeviceFlag = false;
    private ConnectivityManager mConnectivityManager;
    private NetworkConfiguration mConfiguration;

    private ConnectivityListener mConnectivityListener;
    private IpConfiguration mIpConfiguration;
    private final NetworkConfiguration mInitialConfiguration;
    // foucs item on the right
    private Ethernetnet methnet;
    private int mSettingItem = Constants.SETTING_ITEM_0;

    public interface Listener {
        void onSaveWifiConfigurationCompleted(int result);
    }

    private EthernetManager mEthernetManager;

    private LinearLayout mEthernetset;

    public EthernetManager getmEthernetManager() {
        return mEthernetManager;
    }

    public EthernetSettings(NetworkSettingsActivity networkSettingsActivity, NetworkConfiguration initialConfiguration) {
        super(networkSettingsActivity);
        mInitialConfiguration = initialConfiguration;
        mEthernetHolder = new EthernetSettingsHolder(networkSettingsActivity, DeviceFlag);
        mEthernetToggle = mEthernetHolder.getEthernetToggleCheckBox();
        mAutoIpToggle = mEthernetHolder.getAutoIpCheckBox();
        mIPv6Toggle = mEthernetHolder.getIPv6CheckBox();
        mNetworkSettingsActivity = networkSettingsActivity;
        mEthernetset = mEthernetHolder.getRlethetsting();
        mConnectivityListener = new ConnectivityListener(networkSettingsActivity, this);
        methnet = new Ethernetnet();

        mIpConfiguration = (initialConfiguration != null) ?
                mInitialConfiguration.getIpConfiguration() :
                new IpConfiguration();
        mConfiguration = NetworkConfigurationFactory.createNetworkConfiguration(
                mNetworkSettingsActivity, NetworkConfigurationFactory.TYPE_ETHERNET);
        Log.d("yesuo", "mConfiguration1" + mConfiguration);
        Log.d("yesuo", "mConfiguration1" + mConfiguration);
        ((EthernetConfig) mConfiguration).load();

        ischen();
        setOnli();

    }


    private void setOnli() {
        mEthernetToggle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                mConnectivityListener.setState(Constants.ETHERNET_STATE_DISABLED);

            }


        });
        mEthernetHolder.getSaveButton().setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View arg0) {
                IpConfiguration ipConfiguration = mConnectivityListener.getIpConfiguration();
                //if(!mConnectivityListener.getEthernetIpAddress().isEmpty()){
                if (mAutoIpToggle.isChecked()) {

                    if (ipConfiguration.getIpAssignment() == IpAssignment.DHCP) {
                        refreshEthernetStatus();
                        //

                    } else {
                        mIpConfiguration.setIpAssignment(IpAssignment.DHCP);
                        mIpConfiguration.setProxySettings(ipConfiguration.getProxySettings());
                        mIpConfiguration.setStaticIpConfiguration(null);
                        save();
                    }

                } else {

                    if (mEthernetHolder.isednull()) {
                        processIpSettings();
                        save();
                    } else {
                        return;
                    }


                }
                //}
            }
        });
        mEthernetHolder.getCancelButton().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //if(!mConnectivityListener.getEthernetIpAddress().isEmpty()){
                IpConfiguration ipConfiguration = mConnectivityListener.getIpConfiguration();
                if (ipConfiguration.getIpAssignment() == IpAssignment.STATIC) {
                    mAutoIpToggle.setChecked(false);
                } else {
                    mAutoIpToggle.setChecked(true);
                }
                refreshEthernetStatus();
                //}
            }
        });

    }

    public void getnull() {

    }

    private int processIpSettings() {

        IpConfiguration ipConfiguration = mConnectivityListener.getIpConfiguration();

        mIpConfiguration.setIpAssignment(IpAssignment.STATIC);
        mIpConfiguration.setProxySettings(ipConfiguration.getProxySettings());

        StaticIpConfiguration staticConfig = new StaticIpConfiguration();

        //      mIpConfiguration.setStaticIpConfiguration(staticConfig);

        //   String ipAddr = mIpAddressPage.getDataSummary();


        String ipAddr = mEthernetHolder.getEthernetV4Address();
        if (ipAddr == null) {
            ipAddr = "192.168.1.110";
        }
        Log.d("yesuo", "AdcabcdewifioptionFlowipAddr" + ipAddr);
        if (TextUtils.isEmpty(ipAddr))
            return R.string.wifi_ip_settings_invalid_ip_address;

        Inet4Address inetAddr = null;
        Log.d("yesuo", "AdcabcdewifioptionFlowinetAdd" + inetAddr);
        try {
            inetAddr = (Inet4Address) NetworkUtils.numericToInetAddress(ipAddr);
            Log.d("yesuo", "AdcabcdewifioptionFlowinetAdd1" + inetAddr);
        } catch (IllegalArgumentException | ClassCastException e) {
            return R.string.wifi_ip_settings_invalid_ip_address;
        }

        int networkPrefixLength = -1;
        String netmasks = mEthernetHolder.getEthernetV4Netmask();
        Log.d("yesuo", "netmasks:" + netmasks);
        if (netmasks == null) {
            netmasks = "255.255.255.0";
        }
//        methnet.setWosnet(netmasks);
        SharedPreferences.Editor localEditor =mNetworkSettingsActivity.getSharedPreferences("MyNetmask", 0).edit();
        localEditor.putString("MyNetmask", netmasks);
        localEditor.apply();
        int wasks = Tools.twoget(netmasks);
        Log.d("yesuo", "wasks:" + wasks);
        try {
            //   networkPrefixLength = Integer.parseInt(mNetworkPrefixLengthPage.getDataSummary());


            networkPrefixLength = wasks;
            if (networkPrefixLength < 0 || networkPrefixLength > 32) {
                return R.string.wifi_ip_settings_invalid_network_prefix_length;
            }

            staticConfig.ipAddress = new LinkAddress(inetAddr, networkPrefixLength);
            Log.d("yesuo", "staticConfig.ipAddress:" + staticConfig.ipAddress);
        } catch (NumberFormatException e) {
            return R.string.wifi_ip_settings_invalid_ip_address;
        }

        //String gateway = mGatewayPage.getDataSummary();
        String gateway = mEthernetHolder.getEthernetV4Gateway();

        if (gateway == null) {
            gateway = "192.168.1.1";
        }
        Log.d("yesuo", "AdcabcdewifioptionFlowgateway" + gateway);
        if (!TextUtils.isEmpty(gateway)) {
            try {
                staticConfig.gateway = (Inet4Address) NetworkUtils.numericToInetAddress(gateway);
            } catch (IllegalArgumentException | ClassCastException e) {
                return R.string.wifi_ip_settings_invalid_gateway;
            }
        }

        //   String dns1 = mDns1Page.getDataSummary();
        String dns1 = mEthernetHolder.getEthernetV4Dns1();
        if (dns1 == null) {
            dns1 = "192.168.1.1";
        }
        Log.d("yesuo", "AdcabcdewifioptionFlowDNS1DNS1" + dns1);
        if (!TextUtils.isEmpty(dns1)) {

            try {
                staticConfig.dnsServers.add(
                        (Inet4Address) NetworkUtils.numericToInetAddress(dns1));
            } catch (IllegalArgumentException | ClassCastException e) {
                return R.string.wifi_ip_settings_invalid_dns;
            }
        }

        //String dns2 = mDns2Page.getDataSummary();
        String dns2 = mEthernetHolder.getEthernetV4Dns2();
        Log.d("yesuo", "AdcabcdewifioptionFlowDNS2DNS2" + dns2);
        if (!TextUtils.isEmpty(dns2)) {
            try {
                staticConfig.dnsServers.add(
                        (Inet4Address) NetworkUtils.numericToInetAddress(dns1));
            } catch (IllegalArgumentException | ClassCastException e) {
                return R.string.wifi_ip_settings_invalid_dns;
            }
        }
        mIpConfiguration.setStaticIpConfiguration(staticConfig);

        return 0;
    }


    private void ischen() {

        IpConfiguration ipConfiguration = mConnectivityListener.getIpConfiguration();
        if (ipConfiguration.getIpAssignment() == IpAssignment.STATIC) {
            mAutoIpToggle.setChecked(false);
        } else {
            mAutoIpToggle.setChecked(true);
        }
        if (mAutoIpToggle.isChecked()) {

            mEthernetHolder.setV4EditTextWritable(false);
        } else {
            mEthernetHolder.setV4EditTextWritable(true);
        }
        if (mEthernetset.getVisibility() == View.VISIBLE) {
            refreshEthernetStatus();
        } else {

        }
    }

    /**
     * ethernet setting layout visible.
     *
     * @param visible if visible.
     */

    public void setVisible(boolean visible) {
        Log.d(TAG, "visible, " + visible);
        mEthernetHolder.setEthernetVisible(visible);
        if (visible) {
            //  showEthernetInfo();
            refreshEthernetStatus();
        }
    }

    @Override
    public void onExit() {
    }

    @Override
    public boolean onKeyEvent(int keyCode, KeyEvent keyEvent) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                if (mSettingItem > Constants.SETTING_ITEM_0
                        && mSettingItem <= Constants.SETTING_ITEM_8) {
                    mSettingItem--;
                    mEthernetHolder.requestFocus(mSettingItem);
                } else if (mSettingItem == Constants.SETTING_ITEM_9) {
                    mSettingItem -= 2;
                    mEthernetHolder.requestFocus(mSettingItem);
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (mSettingItem == Constants.SETTING_ITEM_0) {
                    if (mEthernetHolder.isEthernetOpened()) {
                        mSettingItem++;
                        mEthernetHolder.requestFocus(mSettingItem);
                    } else {
                        return true;
                    }
                } else if (mSettingItem > Constants.SETTING_ITEM_0
                        && mSettingItem <= Constants.SETTING_ITEM_7) {
                    mSettingItem++;
                    mEthernetHolder.requestFocus(mSettingItem);
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (mSettingItem >= Constants.SETTING_ITEM_3
                        && mSettingItem <= Constants.SETTING_ITEM_7) {
                    return false;
                } else if (mSettingItem == Constants.SETTING_ITEM_9) {
                    mSettingItem--;
                    mEthernetHolder.requestFocus(mSettingItem);
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (mSettingItem >= Constants.SETTING_ITEM_0
                        && mSettingItem <= Constants.SETTING_ITEM_2) {
                    return true;
                } else if (mSettingItem >= Constants.SETTING_ITEM_3
                        && mSettingItem <= Constants.SETTING_ITEM_7) {
                    if (isLastFocused()) {
                        return true;
                    }
                } else if (mSettingItem == Constants.SETTING_ITEM_8) {
                    mSettingItem++;
                    mEthernetHolder.requestFocus(mSettingItem);
                    return true;
                } else if (mSettingItem == Constants.SETTING_ITEM_9) {
                    return true;
                }
                break;
            default:
                break;
        }

        return false;
    }


    @Override
    public void onFocusChange(boolean hasFocus) {
        if (hasFocus) {
            mEthernetHolder.requestFocus(Constants.SETTING_ITEM_0);
        } else {
            mEthernetHolder.clearFocus(mSettingItem);
            mSettingItem = Constants.SETTING_ITEM_0;
        }
    }


    public boolean isV4FirstFocused() {
        if (mEthernetHolder.isV4FirstFocused() || mEthernetHolder.isV6Focus()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isConfigEditTextFocused() {
        if (mSettingItem >= Constants.SETTING_ITEM_3 && mSettingItem <= Constants.SETTING_ITEM_7) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isLastFocused() {
        if (mEthernetHolder.isV4LastFocused() || mEthernetHolder.isV6Focus()) {
            return true;
        } else {
            return false;
        }
    }


    private void refreshEthernetStatus() {
        Log.d(TAG, "refreshEthernetStatus");
        // show connect type.
        // show network information.


        String ip = mConnectivityListener.getEthernetIpAddress();

        String netmask = null;
        String defaultWay = null;
        String firstdns = null;
        String secDns = null;
        //if (!mConnectivityListener.getEthernetIpAddress().isEmpty()) {

        IpConfiguration ipConfiguration = mConnectivityListener.getIpConfiguration();
        if (ipConfiguration != null) {
            if (ipConfiguration.getIpAssignment() == IpAssignment.STATIC) {


                StaticIpConfiguration cifi = ipConfiguration.getStaticIpConfiguration();


                if (cifi != null) {
                    LinkAddress net = cifi.ipAddress;
                    Log.d("yesuo", net + "net");
                    if (net != null) {
                        if (ip == null) {
                            InetAddress address = net.getAddress();
                            if (address instanceof Inet4Address) {
                                ip = address.getHostAddress();
                            }
                        }
                        int nemask = net.describeContents();
                        int neamask = net.getNetworkPrefixLength();
                        int nesmask = net.getScope();
                        Log.d("yesuo", nemask + "net0");
                        Log.d("yesuo", neamask + "net1");
                        Log.d("yesuo", nesmask + "net2");
                        String s = cifi.domains;
                        Log.d("yesuo", s + "net2");
                    }
                    InetAddress way = cifi.gateway;
                    if (way != null) {
                        defaultWay = way.getHostAddress();
                    }
//                    String nets = methnet.getWosnet();
                    String nets = mNetworkSettingsActivity.getSharedPreferences("MyNetmask", 0).getString("MyNetmask", "255.255.255.0");
                    if (nets != null&&!nets.equals("")) {
                        netmask = nets;
                    } else {
                        if (ip != null && defaultWay != null) {
                            netmask = Tools.buildUpNetmask(ip, defaultWay);
                            Log.d("yesuo", ip + ":ip   "+defaultWay+":defaultWay");
                        }
                    }

                    Iterator<InetAddress> dns = cifi.dnsServers.iterator();
                    if (dns.hasNext()) {
                        firstdns = dns.next().getHostAddress();
                        if (firstdns!=null&&firstdns.equals("")&&!Tools.matchIP(firstdns)) {
                            firstdns = null;
                        }
                    }
                    if (dns.hasNext()) {
                        secDns = dns.next().getHostAddress();
                        if (secDns!=null&&secDns.equals("")&&!Tools.matchIP(secDns)) {
                            secDns = null;
                        }
                    }

                    Log.d("yesuo", ipConfiguration.getStaticIpConfiguration() + "netmask" + cifi.dnsServers + "dnsserver");

                    mEthernetHolder.refreshNetworkInfo(ip, netmask, defaultWay, firstdns, secDns);
                    InputIPAddress.isForwardRightWithTextChange = true;

                }
            } else {
                //   misethernetChboxs.setChecked(false);


                ip = mConnectivityListener.getEthernetIpAddress();
//                SystemProperties.set("dhcp.eth0.mask","255.255.255.0");
//                SystemProperties.set("dhcp.eth0.gateway","192.168.1.1");
//                SystemProperties.set("dhcp.eth0.dns1","192.168.1.1");
//                SystemProperties.set("dhcp.eth0.dns2","192.168.1.1");
                netmask = SystemProperties.get("dhcp.eth0.mask");
                defaultWay = SystemProperties.get("dhcp.eth0.gateway");
                firstdns = SystemProperties.get("dhcp.eth0.dns1");
                secDns = SystemProperties.get("dhcp.eth0.dns2");
                if (secDns!=null&&secDns.equals("")&&!Tools.matchIP(secDns)) {
                    secDns = null;
                }
                mEthernetHolder.refreshNetworkInfo(ip, netmask, defaultWay, firstdns, secDns);
                InputIPAddress.isForwardRightWithTextChange = true;

            }

        } else {

            //                }
        }
        //                }


        //}
    }

    @Override
    public void onConnectivityChange(Intent intent) {

    }

    @Override
    public void onProxyChanged(boolean enabled, ProxyInfo proxyInfo) {

    }

    @Override
    public void onWifiHWChanged(boolean isOn) {
    }

    private void save() {

        updateConfiguration(mConfiguration);
        mConfiguration.save(this);
    }
    //    public void updateConfiguration(WifiConfiguration configuration) {
    //			Log.d("yesuo","updateConfiguration"+configuration+mIpConfiguration);
    //        configuration.setIpConfiguration(mIpConfiguration);
    //    }

    public void updateConfiguration(NetworkConfiguration configuration) {
        Log.d("yesuo", "updateConfiguration(" + configuration + mIpConfiguration);
        configuration.setIpConfiguration(mIpConfiguration);

    }

    @Override
    public void onFailure(int arg0) {
        Toast.makeText(mNetworkSettingsActivity, arg0, Toast.LENGTH_SHORT).show();
        mIpConfiguration.setIpAssignment(IpAssignment.DHCP);
        mIpConfiguration.setStaticIpConfiguration(null);
        save();
        refreshEthernetStatus();
    }

    @Override
    public void onSuccess() {
        Toast.makeText(mNetworkSettingsActivity, "save success", Toast.LENGTH_SHORT).show();

        refreshEthernetStatus();
    }

}
