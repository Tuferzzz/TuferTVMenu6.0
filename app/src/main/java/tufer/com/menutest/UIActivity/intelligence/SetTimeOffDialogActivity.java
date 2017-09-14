//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2013 MStar Semiconductor, Inc. All rights reserved.
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

package tufer.com.menutest.UIActivity.intelligence;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.text.format.Time;

import com.mstar.android.tv.TvTimerManager;
import com.mstar.android.tvapi.common.vo.StandardTime;

import tufer.com.menutest.R;
import tufer.com.menutest.UIActivity.MainActivity;
import tufer.com.menutest.UIActivity.component.ComboButton;


public class SetTimeOffDialogActivity extends Activity {
    private String[] hours = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
            "16", "17", "18", "19", "20", "21", "22", "23",
    };

    private String[] minutes = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
            "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
            "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43",
            "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57",
            "58", "59"
    };

    private ComboButton comboBtnPowerOffSwitch;

    private ComboButton comboBtnHour;

    private ComboButton comboBtnMinute;

    private TvTimerManager tvTimerManager = null;

    private boolean enableOffTimer = false;

    private final static int SET_OFF_TIME = 0x11;

    private TimeOffPauseReceiver timeoffpausereceiver;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
           if (msg.what == SET_OFF_TIME) {
                tvTimerManager.setOffTimerEnable(true);
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_off);
        tvTimerManager = TvTimerManager.getInstance();
        findViews();
        loadDataToUI();
        //LittleDownTimer.setHandler(handler);
        timeoffpausereceiver = new TimeOffPauseReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("mstar.tvsetting.ui.pausemainmenu");
        this.registerReceiver(timeoffpausereceiver, filter);
    }

    @Override
    protected void onResume() {
        //LittleDownTimer.resumeMenu();
        super.onResume();
    }

    @Override
    public void onUserInteraction() {
        //LittleDownTimer.resetMenu();
        super.onUserInteraction();
    }

    @Override
    protected void onPause() {
        //LittleDownTimer.pauseMenu();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        tvTimerManager.setOffTimerEnable(enableOffTimer);
        unregisterReceiver(timeoffpausereceiver);
        super.onDestroy();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_PROG_RED) {// red key
            tvTimerManager.setOffTimerEnable(comboBtnPowerOffSwitch.getIdx() == 0 ? false : true);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onStop() {
        tvTimerManager.setOffTimerEnable(comboBtnPowerOffSwitch.getIdx() == 0 ? false : true);
        MainActivity.myMainActivity.handler.sendEmptyMessage(MainActivity.UPDATE_INTELLIGENCE);
        finish();
        super.onStop();
    }

    private void findViews() {
        comboBtnPowerOffSwitch = new ComboButton(this, getResources().getStringArray(
                R.array.str_arr_time_switch), R.id.linearlayout_time_power_off_switch, 1, 2,
                ComboButton.DIRECT_SWITCH) {
            @Override
            public void doUpdate() {
                int idx = comboBtnPowerOffSwitch.getIdx();
                tvTimerManager.setOffTimerEnable(idx == 0 ? false : true);
                if (idx == 0) {
                    setBelowEnable(false);
                    enableOffTimer = false;
                } else {
                    setBelowEnable(true);
                    enableOffTimer = true;
                }
            }
        };
        comboBtnHour = new ComboButton(this, hours, R.id.linearlayout_time_power_off_hour, 1, 2,
                ComboButton.DIRECT_SWITCH) {
            @Override
            public void doUpdate() {
                Time dateTime = tvTimerManager.getCurTimer();
                dateTime.hour = (byte) (comboBtnHour.getIdx());
                tvTimerManager.setOffTimer((StandardTime) dateTime);
                enableOffTimer = true;
                handler.sendEmptyMessageDelayed(SET_OFF_TIME, 500);
            }
        };
        comboBtnMinute = new ComboButton(this, minutes, R.id.linearlayout_time_power_off_minute, 1,
                2, ComboButton.DIRECT_SWITCH) {
            @Override
            public void doUpdate() {
                Time dateTime;
                Time curDateTime;

                curDateTime = tvTimerManager.getCurTimer();
                dateTime = tvTimerManager.getOffTimer();
                dateTime.minute = (byte) (comboBtnMinute.getIdx());
                tvTimerManager.setOffTimer((StandardTime) dateTime);
                if ((dateTime.toMillis(true) - curDateTime.toMillis(true)) > 1000 * 60) {
                    handler.sendEmptyMessageDelayed(SET_OFF_TIME, 500);
                }
                enableOffTimer = true;
            }
        };
        comboBtnPowerOffSwitch.setFocused();
    }

    private void loadDataToUI() {
        if (tvTimerManager.isOffTimerEnable() == false) {
            Time dateTime = tvTimerManager.getCurTimer();
            comboBtnHour.setIdx(dateTime.hour);
            comboBtnMinute.setIdx(dateTime.minute);

            tvTimerManager.setOffTimer((StandardTime) dateTime);
        }

        comboBtnPowerOffSwitch.setIdx(tvTimerManager.isOffTimerEnable() ? 1 : 0);
        comboBtnHour.setIdx(tvTimerManager.getOffTimer().hour);
        int minute = tvTimerManager.getOffTimer().minute;
        comboBtnMinute.setIdx(minute);
        comboBtnMinute.setTextInChild(2, minute + "");
        setBelowEnable(tvTimerManager.isOffTimerEnable());
    }

    private void setBelowEnable(boolean b) {
        comboBtnHour.setEnable(b);
        comboBtnMinute.setEnable(b);
        int textColor = b ? getResources().getColor(R.color.enable_text_color) : getResources()
                .getColor(R.color.disable_text_color);
        ((TextView) comboBtnHour.getLayout().getChildAt(1)).setTextColor(textColor);
        ((TextView) comboBtnHour.getLayout().getChildAt(2)).setTextColor(textColor);
        ((TextView) comboBtnMinute.getLayout().getChildAt(1)).setTextColor(textColor);
        ((TextView) comboBtnMinute.getLayout().getChildAt(2)).setTextColor(textColor);
    }

    private class TimeOffPauseReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("mstar.tvsetting.ui.pausemainmenu")) {
                finish();
                return;
            }
        }
    }
}
