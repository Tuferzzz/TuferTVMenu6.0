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

package tufer.com.menutest.UIActivity.general.datetime;

import java.util.Calendar;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import tufer.com.menutest.R;


public class TimeSettingDialog extends Dialog {

    private DateTimeSettings mDateTimeSettings;

    private Handler mUpdateTimeHandler;

    private Button mConfirmButton;

    private Button mCancelButton;

    private EditText mHourEditText;

    private EditText mMinuteEditText;

    private boolean is24Hour = false;

    public TimeSettingDialog(DateTimeSettings dateTimeSettings, Handler handler, boolean is24Hour) {
        super(dateTimeSettings);
        this.mDateTimeSettings = dateTimeSettings;
        this.mUpdateTimeHandler = handler;
        this.is24Hour = is24Hour;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_setting);

        Window w = getWindow();
        DisplayMetrics outMetrics = new DisplayMetrics();
        w.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        Resources resources = mDateTimeSettings.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.set_bg);
        w.setBackgroundDrawable(drawable);
        w.setTitle("                          "
                + mDateTimeSettings.getResources().getString(R.string.time_setting));

        int width = (int) (outMetrics.widthPixels * 0.3);
        int height = (int) (outMetrics.heightPixels * 0.4);
        w.setLayout(width, height);
        w.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams wl = w.getAttributes();
        w.setAttributes(wl);

        findViews();
        registerListeners();
    }

    private void findViews() {
        mHourEditText = (EditText) findViewById(R.id.hour_et);
        mMinuteEditText = (EditText) findViewById(R.id.minute_et);

        final Calendar c = Calendar.getInstance();
        mHourEditText.setText(c.get(Calendar.HOUR_OF_DAY) + "");
        mMinuteEditText.setText(c.get(Calendar.MINUTE) + 1 + "");
        mConfirmButton = (Button) findViewById(R.id.setting_time_ok);
        mCancelButton = (Button) findViewById(R.id.cannel_time_setting);
    }

    private void registerListeners() {
        mConfirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String hour = mHourEditText.getText().toString();
                if ("".equals(hour)) {
                    mHourEditText.setHint(mDateTimeSettings.getString(R.string.correct_hour));
                    mHourEditText.requestFocus();
                    return;
                } else {
                    if (23 < Integer.parseInt(hour)) {
                        hourHint();
                        mHourEditText.requestFocus();

                        return;
                    }
                }

                String minute = mMinuteEditText.getText().toString();
                if ("".equals(minute)) {
                    mMinuteEditText.setHint(mDateTimeSettings.getString(R.string.correct_minute));
                    mMinuteEditText.requestFocus();
                    return;
                } else {
                    if (59 < Integer.parseInt(minute)) {
                        minuteHint();
                        mMinuteEditText.requestFocus();

                        return;
                    }
                }

                Message msg = new Message();
                Bundle mBundle = new Bundle();
                mBundle.putString("hour", hour);
                mBundle.putString("minute", minute);
                msg.setData(mBundle);
                mUpdateTimeHandler.sendMessage(msg);
                dismiss();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mMinuteEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mMinuteEditText.selectAll();
                }
            }
        });

        mMinuteEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                String minuteString = text.toString().trim();
                int length = minuteString.length();
                if (length != 0) {
                    int day = Integer.parseInt(minuteString);
                    if (59 < day || 2 < length) {
                        minuteHint();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mHourEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mHourEditText.selectAll();
                }
            }
        });

        mHourEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                String hourString = text.toString().trim();
                int length = hourString.length();
                if (length != 0) {
                    int hour = Integer.parseInt(hourString);
                    if (23 < hour || 2 < length) {
                        hourHint();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void hourHint() {
        mHourEditText.setText("");
        mHourEditText.setHint(mDateTimeSettings.getString(R.string.correct_hour));
    }

    private void minuteHint() {
        mMinuteEditText.setText("");
        mMinuteEditText.setHint(mDateTimeSettings.getString(R.string.correct_minute));
    }

}
