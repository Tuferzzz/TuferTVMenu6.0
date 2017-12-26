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

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import tufer.com.menutest.R;
import tufer.com.menutest.Util.Tools;


/**
 * Ethernet Controller holder.
 */
public class EthernetSettingsHolder {

    private static final String TAG = "MSettings.EthernetSettingsHolder";

    private NetworkSettingsActivity mNetworkSettingActivity;
    // root layout
    private LinearLayout mEthernetSettingsRootLayout;

    // ethernet switch layout
    private RelativeLayout mEthernetToggleLayout;
    // ethernet switch check-box.
    private CheckBox mEthernetSwitchChbox;

    //device switch layout, add by ken.bi [2013/5/6]
    private RelativeLayout mDeviceToggleLayout;
    private CheckBox mDeviceSwitchChbox;


    //device name ,add by ken.bi [2013/5/7]
    private String deviceName;

    public CheckBox getmDeviceSwitchChbox() {
		return mDeviceSwitchChbox;
	}

	private boolean DeviceFlag; //muti ether device flag is true

    public boolean isDeviceFlag() {
		return DeviceFlag;
	}

	public void setDeviceFlag(boolean deviceFlag) {
		DeviceFlag = deviceFlag;
	}

	// ethernet config toggle
    private LinearLayout mEthernetConfigLayout;
    // get ip automatic check-box.
    private RelativeLayout mAutoToggleLayout;
    private CheckBox mAutoIpChbox;
    // ipv4|ipv6 chosen.
    private RelativeLayout mIpv6ToggleLayout;
    private CheckBox mIpv6Chbox;
    // save configurations btn.
    private Button mSaveConfigBtn;
    private Button mCancelConfigBtn;

    /* ipv4 layout and components begin: */
    private LinearLayout mEthernetSettingsLayoutV4;
    // ip:
    private EditText mEthernetV4Ip4;
    private EditText mEthernetV4Ip3;
    private EditText mEthernetV4Ip2;
    private EditText mEthernetV4Ip1;
    // subnet
    private EditText mEthernetV4Subnet4;
    private EditText mEthernetV4Subnet3;
    private EditText mEthernetV4Subnet2;
    private EditText mEthernetV4Subnet1;
    // default gateway.
    private EditText mEthernetV4Gateway4;
    private EditText mEthernetV4Gateway3;
    private EditText mEthernetV4Gateway2;
    private EditText mEthernetV4Gateway1;
    // dns1
    private EditText mEthernetV4FirstDNS4;
    private EditText mEthernetV4FirstDNS3;
    private EditText mEthernetV4FirstDNS2;
    private EditText mEthernetV4FirstDNS1;
    // dns2
    private EditText mEthernetV4SecondDNS4;
    private EditText mEthernetV4SecondDNS3;
    private EditText mEthernetV4SecondDNS2;
    private EditText mEthernetV4SecondDNS1;

    /* ipv6 layout and components begin: */
    private LinearLayout mEthernetSettingsLayoutV6;
    private EditText mEthernetV6Ip;
    private EditText mEthernetV6Subnet;
    private EditText mEthernetV6Gateway;
    private EditText mEthernetV6Dns1;
    private EditText mEthernetV6Dns2;

    public EthernetSettingsHolder(NetworkSettingsActivity networkSettingActivity) {
        this.mNetworkSettingActivity = networkSettingActivity;

        findViews();
        setListeners();
    }

    public EthernetSettingsHolder(NetworkSettingsActivity networkSettingActivity,boolean flag){
    	this.mNetworkSettingActivity = networkSettingActivity;
    	this.DeviceFlag = flag;
    	Log.d(TAG, "Device flag is"+DeviceFlag);
    	findViews();
    	setListeners();
    }

    private void findViews() {
        mEthernetSettingsRootLayout = (LinearLayout) getViewById(R.id.ethernet_setting_ll);
        // toggle layout
        mEthernetToggleLayout = (RelativeLayout) getViewById(R.id.ethernet_switch_rl);
        mEthernetSwitchChbox = (CheckBox) getViewById(R.id.ethernet_switch_checkbox);

        //toggle device, add by ken.bi [2013/5/6]
        mDeviceToggleLayout = (RelativeLayout) getViewById(R.id.ethernet_dev_switch_rl);
        mDeviceSwitchChbox = (CheckBox) getViewById(R.id.ethernet_dev_switch_checkbox);

        if(isDeviceFlag()){
        	mDeviceToggleLayout.setVisibility(View.VISIBLE);
        }else {
        	mDeviceToggleLayout.setVisibility(View.GONE);
        }

        // ethernet config fields
        mEthernetConfigLayout = (LinearLayout) getViewById(R.id.ethernet_toggle_layout);
        // auto ip toggle
        mAutoToggleLayout = (RelativeLayout) getViewById(R.id.ethernet_auto_ip_layout);
        mAutoIpChbox = (CheckBox) getViewById(R.id.ethernet_auto_ip);
        // ipv6 toggle
        mIpv6ToggleLayout = (RelativeLayout) getViewById(R.id.ethernet_ipv6_layout);
        mIpv6Chbox = (CheckBox) getViewById(R.id.ethernet_ipv6);

        mEthernetSettingsLayoutV4 = (LinearLayout) getViewById(R.id.ethernet_ipv4_address_rl);
        mEthernetSettingsLayoutV6 = (LinearLayout) getViewById(R.id.ethernet_ipv6_address_rl);

        if (mIpv6Chbox.isChecked()) {
            mEthernetSettingsLayoutV4.setVisibility(View.GONE);
            mEthernetSettingsLayoutV6.setVisibility(View.VISIBLE);
        } else {
            mEthernetSettingsLayoutV6.setVisibility(View.GONE);
            mEthernetSettingsLayoutV4.setVisibility(View.VISIBLE);
        }

        // ipv4_ip:
        mEthernetV4Ip4 = (EditText) getViewById(R.id.ethernet_v4_ip_four);
        mEthernetV4Ip3 = (EditText) getViewById(R.id.ethernet_v4_ip_three);
        mEthernetV4Ip2 = (EditText) getViewById(R.id.ethernet_v4_ip_two);
        mEthernetV4Ip1 = (EditText) getViewById(R.id.ethernet_v4_ip_one);

        // ipv4_subnet
        mEthernetV4Subnet4 = (EditText) getViewById(R.id.ethernet_v4_subnet_four);
        mEthernetV4Subnet3 = (EditText) getViewById(R.id.ethernet_v4_subnet_three);
        mEthernetV4Subnet2 = (EditText) getViewById(R.id.ethernet_v4_subnet_two);
        mEthernetV4Subnet1 = (EditText) getViewById(R.id.ethernet_v4_subnet_one);

        // ipv4_default gateway.
        mEthernetV4Gateway4 = (EditText) getViewById(R.id.ethernet_v4_gateway_four);
        mEthernetV4Gateway3 = (EditText) getViewById(R.id.ethernet_v4_gateway_three);
        mEthernetV4Gateway2 = (EditText) getViewById(R.id.ethernet_v4_gateway_two);
        mEthernetV4Gateway1 = (EditText) getViewById(R.id.ethernet_v4_gateway_one);

        // ipv4_dns1
        mEthernetV4FirstDNS4 = (EditText) getViewById(R.id.ethernet_v4_first_four);
        mEthernetV4FirstDNS3 = (EditText) getViewById(R.id.ethernet_v4_first_three);
        mEthernetV4FirstDNS2 = (EditText) getViewById(R.id.ethernet_v4_first_two);
        mEthernetV4FirstDNS1 = (EditText) getViewById(R.id.ethernet_v4_first_one);

        // ipv4_dns2
        mEthernetV4SecondDNS4 = (EditText) getViewById(R.id.ethernet_v4_second_four);
        mEthernetV4SecondDNS3 = (EditText) getViewById(R.id.ethernet_v4_second_three);
        mEthernetV4SecondDNS2 = (EditText) getViewById(R.id.ethernet_v4_second_two);
        mEthernetV4SecondDNS1 = (EditText) getViewById(R.id.ethernet_v4_second_one);

        // ipv6_configuration editor:
        mEthernetV6Ip = (EditText) getViewById(R.id.ethernet_v6_ip);
        mEthernetV6Subnet = (EditText) getViewById(R.id.ethernet_v6_subnet);
        mEthernetV6Gateway = (EditText) getViewById(R.id.ethernet_v6_gateway);
        mEthernetV6Dns1 = (EditText) getViewById(R.id.ethernet_v6_first);
        mEthernetV6Dns2 = (EditText) getViewById(R.id.ethernet_v6_second);

        // save config
        mSaveConfigBtn = (Button) getViewById(R.id.ethernet_ip_save_btn);
        mCancelConfigBtn = (Button) getViewById(R.id.ethernet_ip_cancel_btn);
    }

    private View getViewById(int id) {
        return mNetworkSettingActivity.findViewById(id);
    }

    private void setListeners() {

    	//device switch, add by ken.bi [2013/5/7]
    	mDeviceSwitchChbox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked){
					setDeviceName("eth1");
				}else{
					setDeviceName("eth0");
				}
			}

    	});

    	mDeviceSwitchChbox.setOnFocusChangeListener(new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(hasFocus){
					mEthernetToggleLayout.setBackgroundResource(R.drawable.set_button);
				}else{
					mEthernetToggleLayout.setBackgroundResource(R.drawable.one_px);
				}

			}

    	});

        // ethernet toggle
        mEthernetSwitchChbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mEthernetConfigLayout.setVisibility(View.VISIBLE);
                } else {
                    mEthernetConfigLayout.setVisibility(View.GONE);
                }
            }
        });
        mEthernetSwitchChbox.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    mEthernetToggleLayout.setBackgroundResource(R.drawable.set_button);
                } else {
                    mEthernetToggleLayout.setBackgroundResource(R.drawable.one_px);
                }
            }
        });

        // auto ip toggle
        mAutoIpChbox.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    mAutoToggleLayout.setBackgroundResource(R.drawable.set_button);
                } else {
                    mAutoToggleLayout.setBackgroundResource(R.drawable.one_px);
                }
            }
        });

        // ipv6 toggle listener
        mIpv6Chbox.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mIpv6Chbox.isChecked()) {
                    mEthernetSettingsLayoutV4.setVisibility(View.GONE);
                    mEthernetSettingsLayoutV6.setVisibility(View.VISIBLE);
                } else {
                    mEthernetSettingsLayoutV4.setVisibility(View.VISIBLE);
                    mEthernetSettingsLayoutV6.setVisibility(View.GONE);
                }
            }
        });
        mIpv6Chbox.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    mIpv6ToggleLayout.setBackgroundResource(R.drawable.set_button);
                } else {
                    mIpv6ToggleLayout.setBackgroundResource(R.drawable.one_px);
                }
            }
        });

        // save config
        mSaveConfigBtn.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    mSaveConfigBtn.setBackgroundResource(R.drawable.edit_focus);
                } else {
                    mSaveConfigBtn.setBackgroundResource(R.drawable.edit_normal);
                }
            }
        });

//        // cancel config
        mCancelConfigBtn.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    mCancelConfigBtn.setBackgroundResource(R.drawable.edit_focus);
                } else {
                    mCancelConfigBtn.setBackgroundResource(R.drawable.edit_normal);
                }
            }
        });

        mAutoIpChbox.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (mAutoIpChbox.isChecked()) {
                    // TODO auto ip.
                    mAutoIpChbox.setButtonDrawable(R.drawable.open);
                    setV4EditTextWritable(false);
                    setV6EditTextWritable(false);
                } else {
                    // TODO set ip by user.
                    mAutoIpChbox.setButtonDrawable(R.drawable.close);
                    setV4EditTextWritable(true);
                    setV6EditTextWritable(true);
                }
            }
        });

        mIpv6Chbox.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mIpv6Chbox.isChecked()) {
                    mIpv6Chbox.setButtonDrawable(R.drawable.open);
                    mEthernetSettingsLayoutV4.setVisibility(View.GONE);
                    mEthernetSettingsLayoutV6.setVisibility(View.VISIBLE);
                } else {
                    mIpv6Chbox.setButtonDrawable(R.drawable.close);
                    mEthernetSettingsLayoutV4.setVisibility(View.VISIBLE);
                    mEthernetSettingsLayoutV6.setVisibility(View.GONE);
                }
                //
                if (mAutoIpChbox.isChecked()) {
                    setV6EditTextWritable(false);
                } else {
                    setV6EditTextWritable(true);
                }
            }
        });

        mEthernetV4Ip1.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    if (hasFocus && editText.isEnabled()) {
                        editText.selectAll();
                    }
                }
            }
        });
        mEthernetV4Ip2.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    if (hasFocus && editText.isEnabled()) {
                        editText.selectAll();
                    }
                }
            }
        });
        mEthernetV4Ip3.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    if (hasFocus && editText.isEnabled()) {
                        editText.selectAll();
                    }
                }
            }
        });
        mEthernetV4Ip4.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    if (hasFocus && editText.isEnabled()) {
                        editText.selectAll();
                    }
                }
            }
        });

        mEthernetV4Subnet1.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    if (hasFocus && editText.isEnabled()) {
                        editText.selectAll();
                    }
                }
            }
        });
        mEthernetV4Subnet2.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    if (hasFocus && editText.isEnabled()) {
                        editText.selectAll();
                    }
                }
            }
        });
        mEthernetV4Subnet3.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    if (hasFocus && editText.isEnabled()) {
                        editText.selectAll();
                    }
                }
            }
        });
        mEthernetV4Subnet4.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    if (hasFocus && editText.isEnabled()) {
                        editText.selectAll();
                    }
                }
            }
        });

        mEthernetV4Gateway1.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    if (hasFocus && editText.isEnabled()) {
                        editText.selectAll();
                    }
                }
            }
        });
        mEthernetV4Gateway2.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    if (hasFocus && editText.isEnabled()) {
                        editText.selectAll();
                    }
                }
            }
        });
        mEthernetV4Gateway3.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    if (hasFocus && editText.isEnabled()) {
                        editText.selectAll();
                    }
                }
            }
        });
        mEthernetV4Gateway4.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    if (hasFocus && editText.isEnabled()) {
                        editText.selectAll();
                    }
                }
            }
        });

        mEthernetV4FirstDNS1.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    if (hasFocus && editText.isEnabled()) {
                        editText.selectAll();
                    }
                }
            }
        });
        mEthernetV4FirstDNS2.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    if (hasFocus && editText.isEnabled()) {
                        editText.selectAll();
                    }
                }
            }
        });
        mEthernetV4FirstDNS3.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    if (hasFocus && editText.isEnabled()) {
                        editText.selectAll();
                    }
                }
            }
        });
        mEthernetV4FirstDNS4.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    if (hasFocus && editText.isEnabled()) {
                        editText.selectAll();
                    }
                }
            }
        });

        mEthernetV4SecondDNS1.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    if (hasFocus && editText.isEnabled()) {
                        editText.selectAll();
                    }
                }
            }
        });
        mEthernetV4SecondDNS2.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    if (hasFocus && editText.isEnabled()) {
                        editText.selectAll();
                    }
                }
            }
        });
        mEthernetV4SecondDNS3.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    if (hasFocus && editText.isEnabled()) {
                        editText.selectAll();
                    }
                }
            }
        });
        mEthernetV4SecondDNS4.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    if (hasFocus && editText.isEnabled()) {
                        editText.selectAll();
                    }
                }
            }
        });

        mEthernetV6Ip.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    if (hasFocus && editText.isEnabled()) {
                        editText.selectAll();
                    }
                }
            }
        });
        mEthernetV6Subnet.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    if (hasFocus && editText.isEnabled()) {
                        editText.selectAll();
                    }
                }
            }
        });
        mEthernetV6Gateway.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    if (hasFocus && editText.isEnabled()) {
                        editText.selectAll();
                    }
                }
            }
        });
        mEthernetV6Dns1.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    if (hasFocus && editText.isEnabled()) {
                        editText.selectAll();
                    }
                }
            }
        });
        mEthernetV6Dns2.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    if (hasFocus && editText.isEnabled()) {
                        editText.selectAll();
                    }
                }
            }
        });
    }

    /**
     * show ethernet information.
     *
     * @param ip ip.
     * @param netmask subnet mask.
     * @param gateway default gateway.
     * @param dns1 first DNS.
     * @param dns2 second DNS.
     */
    public void refreshNetworkInfo(String ip, String netmask, String gateway, String dns1,
            String dns2) {
        Log.d(TAG, "ip, " + ip + " netmask, " + netmask + " gateway, " + gateway + " dns1, " + dns1
                + " dns2, " + dns2);
        if (ip != null) {
            String[] ips = Tools.resolutionIP(ip);
            mEthernetV4Ip1.setText(ips[0]);
            mEthernetV4Ip2.setText(ips[1]);
            mEthernetV4Ip3.setText(ips[2]);
            mEthernetV4Ip4.setText(ips[3]);
        }

        if (netmask != null&&!netmask.equals("")) {
            String[] masks = Tools.resolutionIP(netmask);
            mEthernetV4Subnet1.setText(masks[0]);
            mEthernetV4Subnet2.setText(masks[1]);
            mEthernetV4Subnet3.setText(masks[2]);
            mEthernetV4Subnet4.setText(masks[3]);
        }

        if (gateway != null&&!gateway.equals("")) {
            String[] defaultWays = Tools.resolutionIP(gateway);
            mEthernetV4Gateway1.setText(defaultWays[0]);
            mEthernetV4Gateway2.setText(defaultWays[1]);
            mEthernetV4Gateway3.setText(defaultWays[2]);
            mEthernetV4Gateway4.setText(defaultWays[3]);
        }

        if (dns1 != null&&!dns1.equals("")) {
            String[] firstdnss = Tools.resolutionIP(dns1);
            mEthernetV4FirstDNS1.setText(firstdnss[0]);
            mEthernetV4FirstDNS2.setText(firstdnss[1]);
            mEthernetV4FirstDNS3.setText(firstdnss[2]);
            mEthernetV4FirstDNS4.setText(firstdnss[3]);
        }

        if (dns2 != null&&!dns2.equals("")) {
            String[] secdnss = Tools.resolutionIP(dns2);
            mEthernetV4SecondDNS1.setText(secdnss[0]);
            mEthernetV4SecondDNS2.setText(secdnss[1]);
            mEthernetV4SecondDNS3.setText(secdnss[2]);
            mEthernetV4SecondDNS4.setText(secdnss[3]);
        }
    }

    /**
     * show ethernet information.
     *
     * @param ip ip.
     * @param netmask subnet mask.
     * @param gateway default gateway.
     * @param dns1 first DNS.
     * @param dns2 second DNS.
     */
    public void refreshNetworkInfoV6(String ip, String netmask, String gateway, String dns1,
            String dns2) {
        // refresh ip
        if (ip != null) {
            mEthernetV6Ip.setText(ip);
        }
        // refresh netmask
        if (netmask != null) {
            mEthernetV6Subnet.setText(netmask);
        }
        // FIXME show other infos
    }

    /**
     * ethernet layout visibility.
     *
     * @param visible show/hide.
     */
    public void setEthernetVisible(boolean visible) {
        if (visible) {
            mEthernetSettingsRootLayout.setVisibility(View.VISIBLE);
        } else {
            mEthernetSettingsRootLayout.setVisibility(View.GONE);
        }
    }

    public void setAutoIpOpend(boolean open) {
        mAutoIpChbox.setChecked(open);
        if (open) {
            mAutoIpChbox.setButtonDrawable(R.drawable.open);
        } else {
            mAutoIpChbox.setButtonDrawable(R.drawable.close);
        }
    }

    public void setV4EditTextWritable(boolean isEnable) {
        mEthernetV4Ip4.setEnabled(isEnable);
        mEthernetV4Ip3.setEnabled(isEnable);
        mEthernetV4Ip2.setEnabled(isEnable);
        mEthernetV4Ip1.setEnabled(isEnable);

        mEthernetV4Subnet4.setEnabled(isEnable);
        mEthernetV4Subnet3.setEnabled(isEnable);
        mEthernetV4Subnet2.setEnabled(isEnable);
        mEthernetV4Subnet1.setEnabled(isEnable);

        mEthernetV4Gateway4.setEnabled(isEnable);
        mEthernetV4Gateway3.setEnabled(isEnable);
        mEthernetV4Gateway2.setEnabled(isEnable);
        mEthernetV4Gateway1.setEnabled(isEnable);

        mEthernetV4FirstDNS4.setEnabled(isEnable);
        mEthernetV4FirstDNS3.setEnabled(isEnable);
        mEthernetV4FirstDNS2.setEnabled(isEnable);
        mEthernetV4FirstDNS1.setEnabled(isEnable);

        mEthernetV4SecondDNS4.setEnabled(isEnable);
        mEthernetV4SecondDNS3.setEnabled(isEnable);
        mEthernetV4SecondDNS2.setEnabled(isEnable);
        mEthernetV4SecondDNS1.setEnabled(isEnable);
    }

    public void setV6EditTextWritable(boolean isEnable) {
        mEthernetV6Ip.setEnabled(isEnable);
        mEthernetV6Gateway.setEnabled(isEnable);
        mEthernetV6Subnet.setEnabled(isEnable);
        mEthernetV6Dns1.setEnabled(isEnable);
        mEthernetV6Dns2.setEnabled(isEnable);
    }

    public void clearFocus(int position) {
        switch (position) {
            case Constants.SETTING_ITEM_0:
                mEthernetSwitchChbox.clearFocus();
                break;
            case Constants.SETTING_ITEM_1:
                mAutoIpChbox.clearFocus();
                break;
            case Constants.SETTING_ITEM_2:
                mIpv6Chbox.clearFocus();
                break;
            case Constants.SETTING_ITEM_3:
                if (isIPv6ConfigOpened()) {
                    mEthernetV6Ip.clearFocus();
                } else {
                    mEthernetV4Ip1.clearFocus();
                    mEthernetV4Ip2.clearFocus();
                    mEthernetV4Ip3.clearFocus();
                    mEthernetV4Ip4.clearFocus();
                }
                break;
            case Constants.SETTING_ITEM_4:
                if (isIPv6ConfigOpened()) {
                    mEthernetV6Subnet.clearFocus();
                } else {
                    mEthernetV4Subnet1.clearFocus();
                    mEthernetV4Subnet2.clearFocus();
                    mEthernetV4Subnet3.clearFocus();
                    mEthernetV4Subnet4.clearFocus();
                }
                break;
            case Constants.SETTING_ITEM_5:
                if (isIPv6ConfigOpened()) {
                    mEthernetV6Gateway.clearFocus();
                } else {
                    mEthernetV4Gateway1.clearFocus();
                    mEthernetV4Gateway2.clearFocus();
                    mEthernetV4Gateway3.clearFocus();
                    mEthernetV4Gateway4.clearFocus();
                }
                break;
            case Constants.SETTING_ITEM_6:
                if (isIPv6ConfigOpened()) {
                    mEthernetV6Dns1.clearFocus();
                } else {
                    mEthernetV4FirstDNS1.clearFocus();
                    mEthernetV4FirstDNS2.clearFocus();
                    mEthernetV4FirstDNS3.clearFocus();
                    mEthernetV4FirstDNS4.clearFocus();
                }
                break;
            case Constants.SETTING_ITEM_7:
                if (isIPv6ConfigOpened()) {
                    mEthernetV6Dns2.clearFocus();
                } else {
                    mEthernetV4SecondDNS1.clearFocus();
                    mEthernetV4SecondDNS2.clearFocus();
                    mEthernetV4SecondDNS3.clearFocus();
                    mEthernetV4SecondDNS4.clearFocus();
                }
                break;
            case Constants.SETTING_ITEM_8:
                mSaveConfigBtn.clearFocus();
                break;
            case Constants.SETTING_ITEM_9:
                mCancelConfigBtn.clearFocus();
                break;
            default:
                break;
        }
    }

    public void requestFocus(int position) {
        switch (position) {
            case Constants.SETTING_ITEM_0:
                mEthernetSwitchChbox.requestFocus();
                break;
            case Constants.SETTING_ITEM_1:
                mAutoIpChbox.requestFocus();
                break;
            case Constants.SETTING_ITEM_2:
                mIpv6Chbox.requestFocus();
                break;
            case Constants.SETTING_ITEM_3:
                if (isIPv6ConfigOpened()) {
                    mEthernetV6Ip.requestFocus();
                } else {
                    mEthernetV4Ip1.requestFocus();
                }
                break;
            case Constants.SETTING_ITEM_4:
                if (isIPv6ConfigOpened()) {
                    mEthernetV6Subnet.requestFocus();
                } else {
                    mEthernetV4Subnet1.requestFocus();
                }
                break;
            case Constants.SETTING_ITEM_5:
                if (isIPv6ConfigOpened()) {
                    mEthernetV6Gateway.requestFocus();
                } else {
                    mEthernetV4Gateway1.requestFocus();
                }
                break;
            case Constants.SETTING_ITEM_6:
                if (isIPv6ConfigOpened()) {
                    mEthernetV6Dns1.requestFocus();
                } else {
                    mEthernetV4FirstDNS1.requestFocus();
                }
                break;
            case Constants.SETTING_ITEM_7:
                if (isIPv6ConfigOpened()) {
                    mEthernetV6Dns2.requestFocus();
                } else {
                    mEthernetV4SecondDNS1.requestFocus();
                }
                break;
            case Constants.SETTING_ITEM_8:
                mSaveConfigBtn.requestFocus();
                break;
            case Constants.SETTING_ITEM_9:
                mCancelConfigBtn.requestFocus();
                break;
            default:
                break;
        }
    }

    public boolean isEthernetOpened() {
        return mEthernetSwitchChbox.isChecked();
    }

    public boolean isIPv6ConfigOpened() {
        return mIpv6Chbox.isChecked();
    }
    public boolean isednull(){
    	 if (!Tools.matchIP(getEthernetV4Address())) {
    		 return false;
    	 }
         else if(!Tools.matchIP(getEthernetV4Netmask())){
    		 return false;
    	 }
         else if(!Tools.matchIP(getEthernetV4Gateway())){
    		 return false;
    	 }else if(!Tools.matchIP(getEthernetV4Dns1())){
    		 return false;
    	 }else{
		return true;
    	 }
    }
    public String getEthernetV4Address() {
        StringBuffer address = new StringBuffer();
        address.append(mEthernetV4Ip1.getText().toString().trim());
        address.append(".");
        address.append(mEthernetV4Ip2.getText().toString().trim());
        address.append(".");
        address.append(mEthernetV4Ip3.getText().toString().trim());
        address.append(".");
        address.append(mEthernetV4Ip4.getText().toString().trim());

        return address.toString();
    }

    public String getEthernetV4Netmask() {
        StringBuffer netmask = new StringBuffer();
        netmask.append(mEthernetV4Subnet1.getText().toString().trim());
        netmask.append(".");
        netmask.append(mEthernetV4Subnet2.getText().toString().trim());
        netmask.append(".");
        netmask.append(mEthernetV4Subnet3.getText().toString().trim());
        netmask.append(".");
        netmask.append(mEthernetV4Subnet4.getText().toString().trim());

        return netmask.toString();
    }

    public String getEthernetV4Gateway() {
        StringBuffer gateway = new StringBuffer();
        gateway.append(mEthernetV4Gateway1.getText().toString().trim());
        gateway.append(".");
        gateway.append(mEthernetV4Gateway2.getText().toString().trim());
        gateway.append(".");
        gateway.append(mEthernetV4Gateway3.getText().toString().trim());
        gateway.append(".");
        gateway.append(mEthernetV4Gateway4.getText().toString().trim());

        return gateway.toString();
    }

    public String getEthernetV4Dns1() {
        StringBuffer dns = new StringBuffer();
        dns.append(mEthernetV4FirstDNS1.getText().toString().trim());
        dns.append(".");
        dns.append(mEthernetV4FirstDNS2.getText().toString().trim());
        dns.append(".");
        dns.append(mEthernetV4FirstDNS3.getText().toString().trim());
        dns.append(".");
        dns.append(mEthernetV4FirstDNS4.getText().toString().trim());

        return dns.toString();
    }

    public String getEthernetV4Dns2() {
        StringBuffer dns = new StringBuffer();
        dns.append(mEthernetV4SecondDNS1.getText().toString().trim());
        dns.append(".");
        dns.append(mEthernetV4SecondDNS2.getText().toString().trim());
        dns.append(".");
        dns.append(mEthernetV4SecondDNS3.getText().toString().trim());
        dns.append(".");
        dns.append(mEthernetV4SecondDNS4.getText().toString().trim());

        return dns.toString();
    }

    public String getEthernetV6Address() {
        return mEthernetV6Ip.getText().toString().trim();
    }

    public String getEthernetV6Netmask() {
        return mEthernetV6Subnet.getText().toString().trim();
    }

    public String getEthernetV6Gateway() {
        return mEthernetV6Gateway.getText().toString().trim();
    }

    public String getEthernetV6Dns1() {
        return mEthernetV6Dns1.getText().toString().trim();
    }

    public String getEthernetV6Dns2() {
        return mEthernetV6Dns2.getText().toString().trim();
    }

    public CheckBox getEthernetToggleCheckBox() {
        return mEthernetSwitchChbox;
    }
    public LinearLayout getRlethetsting(){
    	return mEthernetConfigLayout;
    }
    public CheckBox getAutoIpCheckBox() {
        return mAutoIpChbox;
    }

    public CheckBox getIPv6CheckBox() {
        return mIpv6Chbox;
    }

    public Button getSaveButton() {
        return mSaveConfigBtn;
    }

    public Button getCancelButton() {
        return mCancelConfigBtn;
    }

    public boolean isV4FirstFocused() {
        if (mEthernetV4Ip1.isFocused() || mEthernetV4Subnet1.isFocused()
                || mEthernetV4Gateway1.isFocused() || mEthernetV4FirstDNS1.isFocused()
                || mEthernetV4SecondDNS1.isFocused()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isV4LastFocused() {
        if (mEthernetV4Ip4.isFocused() || mEthernetV4Subnet4.isFocused()
                || mEthernetV4Gateway4.isFocused() || mEthernetV4FirstDNS4.isFocused()
                || mEthernetV4SecondDNS4.isFocused()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isV6Focus() {
        if (mEthernetV6Ip.isFocused() || mEthernetV6Subnet.isFocused()
                || mEthernetV6Gateway.isFocused() || mEthernetV6Dns1.isFocused()
                || mEthernetV6Dns2.isFocused()) {
            return true;
        } else {
            return false;
        }
    }

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

}
