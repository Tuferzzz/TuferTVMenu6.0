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

import android.text.InputType;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tufer.com.menutest.R;


/**
 * PPPoE components.
 */
public class PPPoESettingsHolder {

    private NetworkSettingsActivity mActivity;

    private LinearLayout mPPPoESettingsRootLayout;
    // connect type
    private RelativeLayout mConnectTypeLayout;
    private Button mConnectTypeButton;
    // username and passwd
    private EditText mUserEditText;
    private EditText mPasswdEditText;
    // show passwd
    private RelativeLayout mShowPasswdLayout;
    private CheckBox mShowPasswdCheckBox;
    // auto dial
    private RelativeLayout mAutoDialLayout;
    private CheckBox mAutoDialCheckBox;
    // connect status
    private TextView mStatusTextView;
    // hangup and dail
    private Button mHangupButton;
    private Button mDialButton;

    public PPPoESettingsHolder(NetworkSettingsActivity networkSettingActivity) {
        this.mActivity = networkSettingActivity;
        findViews();
    }

    private void findViews() {
        mPPPoESettingsRootLayout = (LinearLayout) mActivity.findViewById(R.id.pppoe_setting_ll);
        mConnectTypeLayout = (RelativeLayout) mActivity.findViewById(R.id.pppoe_auto_ip_rl);
        mConnectTypeButton = (Button) mActivity.findViewById(R.id.pppoe_auto_ip);
        mConnectTypeButton.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mConnectTypeLayout.setBackgroundResource(R.drawable.set_button);
                } else {
                    mConnectTypeLayout.setBackgroundResource(R.drawable.one_px);
                }
            }
        });

        // username and password
        mUserEditText = (EditText) mActivity.findViewById(R.id.username_et);
        mPasswdEditText = (EditText) mActivity.findViewById(R.id.password_et);
        // show passwd
        mShowPasswdLayout = (RelativeLayout) mActivity.findViewById(R.id.show_password_rl);
        mShowPasswdCheckBox = (CheckBox) mActivity.findViewById(R.id.show_password);
        mShowPasswdCheckBox.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mShowPasswdLayout.setBackgroundResource(R.drawable.set_button);
                } else {
                    mShowPasswdLayout.setBackgroundResource(R.drawable.one_px);
                }
            }
        });
        mShowPasswdCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPasswdEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    mPasswdEditText.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        // auto dial
        mAutoDialLayout = (RelativeLayout) mActivity.findViewById(R.id.auto_dialer_rl);
        mAutoDialCheckBox = (CheckBox) mActivity.findViewById(R.id.auto_dialer_cb);
        mAutoDialCheckBox.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mAutoDialLayout.setBackgroundResource(R.drawable.set_button);
                } else {
                    mAutoDialLayout.setBackgroundResource(R.drawable.one_px);
                }
            }
        });
        mAutoDialCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        // connect status type
        mStatusTextView = (TextView) mActivity.findViewById(R.id.dialer_status);
        // handup and dail button
        mHangupButton = (Button) mActivity.findViewById(R.id.dialer_hangup);
//        mHangupButton.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    mHangupButton.setBackgroundResource(R.drawable.edit_focus);
//                } else {
//                    mHangupButton.setBackgroundResource(R.drawable.edit_normal);
//                }
//            }
//        });

        mDialButton = (Button) mActivity.findViewById(R.id.dialer_ok);
//        mDialButton.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    mDialButton.setBackgroundResource(R.drawable.edit_focus);
//                } else {
//                    mDialButton.setBackgroundResource(R.drawable.edit_normal);
//                }
//            }
//        });
    }

    public void setVisible(boolean visible) {
        if (visible) {
            mPPPoESettingsRootLayout.setVisibility(View.VISIBLE);
        } else {
            mPPPoESettingsRootLayout.setVisibility(View.GONE);
        }
    }

    public void clearFocus(int position) {
        switch (position) {
            case Constants.SETTING_ITEM_0:
                mConnectTypeButton.clearFocus();
                break;
            case Constants.SETTING_ITEM_1:
                mUserEditText.clearFocus();
                break;
            case Constants.SETTING_ITEM_2:
                mPasswdEditText.clearFocus();
                break;
            case Constants.SETTING_ITEM_3:
                mShowPasswdCheckBox.clearFocus();
                break;
            case Constants.SETTING_ITEM_4:
                mAutoDialCheckBox.clearFocus();
                break;
            case Constants.SETTING_ITEM_5:
                mHangupButton.clearFocus();
                break;
            case Constants.SETTING_ITEM_6:
                mDialButton.clearFocus();
                break;
            default:
                break;
        }
    }

    public void requestFocus(int position) {
        switch (position) {
            case Constants.SETTING_ITEM_0:
                mConnectTypeButton.requestFocus();
                break;
            case Constants.SETTING_ITEM_1:
                mUserEditText.requestFocus();
                break;
            case Constants.SETTING_ITEM_2:
                mPasswdEditText.requestFocus();
                break;
            case Constants.SETTING_ITEM_3:
                mShowPasswdCheckBox.requestFocus();
                break;
            case Constants.SETTING_ITEM_4:
                mAutoDialCheckBox.requestFocus();
                break;
            case Constants.SETTING_ITEM_5:
                mHangupButton.requestFocus();
                break;
            case Constants.SETTING_ITEM_6:
                mDialButton.requestFocus();
                break;
            default:
                break;
        }
    }

    public CheckBox getAutoDialCheckBox() {
        return mAutoDialCheckBox;
    }

    public Button getHangupButton() {
        return mHangupButton;
    }

    public Button getDialButton() {
        return mDialButton;
    }

    public EditText getUsernameEditText() {
        return mUserEditText;
    }

    public EditText getPasswordEditText() {
        return mPasswdEditText;
    }

    public String getUsername() {
        return mUserEditText.getText().toString().trim();
    }

    public String getPasswd() {
        return mPasswdEditText.getText().toString().trim();
    }

    public void refreshStatus(int id) {
        if (id <= 0) {
            return;
        }

        mStatusTextView.setText(id);
    }

    public void refreshConnectType(int id) {
        if (id <= 0) {
            return;
        }

        mConnectTypeButton.setText(id);
    }

}
