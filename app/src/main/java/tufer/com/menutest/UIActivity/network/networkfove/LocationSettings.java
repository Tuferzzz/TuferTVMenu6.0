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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import tufer.com.menutest.UIActivity.network.networkfove.ProxySettings.ProxyInfo;
import tufer.com.menutest.R;

//BugFix add begin: Mantis:0392659 Fix error active stauts when cancel in AlertDialog with IR 'Exit' or mouse RightClick;
//BugFix add end: Mantis:0392659



/**
 * Location settings for enabling or disabling Location provider.
 */
public class LocationSettings extends NetworkSettings implements INetworkSettingsListener {

    private static final String TAG = "MSettings.LocationSettings";

    private NetworkSettingsActivity mNetworkSettingsActivity;

    private LocationSettingsHolder mLocationSettingsHolder;
//BugFix add begin: Mantis:0392659 Fix error high-light when cancel in AlertDialog with IR 'Exit' or mouse LeftClick;
    private Dialog mDialog = null;
//BugFix add end: Mantis:0392659

    public LocationSettings(NetworkSettingsActivity networkSettingsActivity) {
        super(networkSettingsActivity);
        this.mNetworkSettingsActivity = networkSettingsActivity;

        mLocationSettingsHolder = new LocationSettingsHolder(networkSettingsActivity);
        registerListener();
    }

    public void setVisible(boolean visible) {
        mLocationSettingsHolder.setVisible(visible);
    }

    @Override
    public boolean onKeyEvent(int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            return true;
        }

        return false;
    }

    @Override
    public void onFocusChange(boolean hasFocus) {
        if (hasFocus) {
            mLocationSettingsHolder.requestFocus(Constants.SETTING_ITEM_0);
        } else {
            mLocationSettingsHolder.clearFocus();
        }
    }

    @Override
    public void onExit() {

    }

    @Override
    public void onProxyChanged(boolean enabled, ProxyInfo proxyInfo) {

    }

    @Override
    public void onWifiHWChanged(boolean isOn) {

    }

    /**
     * Location Service is opened or not.
     * 
     * @return true if Location provider is enabled, otherwise false.
     */
    public boolean isLocationProviderEnabled() {
        return Settings.Secure.isLocationProviderEnabled(
                mNetworkSettingsActivity.getContentResolver(), LocationManager.NETWORK_PROVIDER);
    }

    /**
     * Set Location provider enable or disable.
     * 
     * @param enabled true or false.
     */
    private void setLocationProviderEnabled(boolean enabled) {
        Log.d(TAG, "setLocationProviderEnabled, " + enabled);
        Settings.Secure.setLocationProviderEnabled(mNetworkSettingsActivity.getContentResolver(),
                LocationManager.NETWORK_PROVIDER, enabled);
    }

    

	public class LocationDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.location_service_switch)
               .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
				   mLocationSettingsHolder.getLocationServiceToggleCheckBox().setChecked(true);
				   mLocationSettingsHolder.getLocationServiceToggleCheckBox().invalidate();
				   setLocationProviderEnabled(true);
                   }
               })
               .setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
					  mLocationSettingsHolder.getLocationServiceToggleCheckBox().setChecked(false);
					  mLocationSettingsHolder.getLocationServiceToggleCheckBox().invalidate();
					  setLocationProviderEnabled(false);
                   }
               });
        return builder.create();
    }
}
	
	public void showdialog() {
//BugFix modify begin: Mantis:0392659 Fix error active stauts when cancel in AlertDialog with IR 'Exit' or mouse RightClick;
//        DialogFragment mDialogFragment = new LocationDialogFragment();
//        mDialogFragment.show(mNetworkSettingsActivity.getFragmentManager(),"tag");
        AlertDialog.Builder builder = new AlertDialog.Builder(mNetworkSettingsActivity);
        builder.setMessage(R.string.location_service_switch)
        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mLocationSettingsHolder.getLocationServiceToggleCheckBox().setChecked(true);
                mLocationSettingsHolder.getLocationServiceToggleCheckBox().invalidate();
                setLocationProviderEnabled(true);
                mDialog.dismiss();
            }
        })
        .setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mLocationSettingsHolder.getLocationServiceToggleCheckBox().setChecked(false);
                mLocationSettingsHolder.getLocationServiceToggleCheckBox().invalidate();
                setLocationProviderEnabled(false);
                mDialog.dismiss();
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                Log.d(TAG, "onCancelOfsetOnCancelListener@LocationSettings.java");
                mLocationSettingsHolder.getLocationServiceToggleCheckBox().setChecked(false);
                mLocationSettingsHolder.getLocationServiceToggleCheckBox().invalidate();
                setLocationProviderEnabled(false);
                mLocationSettingsHolder.getLocationServiceToggleCheckBox().requestFocusFromTouch();
                mDialog.dismiss();
            }
        });
        mDialog = builder.create();
        builder.show();
        return;
//BugFix modify end: Mantis:0392659
		}
	
	
	private void registerListener() {
        mLocationSettingsHolder.getLocationServiceToggleCheckBox().setOnCheckedChangeListener(
                new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Log.d(TAG, "onCheckedChanged isChecked, " + isChecked);
						if(isChecked)
						{	
							showdialog();
						}
						else
						{
							setLocationProviderEnabled(false);
						}
                    }
                });
    }

}
