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
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;


import com.mstar.android.MIntent;
import com.mstar.android.tvapi.common.TimerManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;

import tufer.com.menutest.R;

public class DateTimeSettings extends Activity {

    private static final String TAG = "DateTimeSettings";

    private static final String HOURS_12 = "12";

    private static final String HOURS_24 = "24";

    private DateTimeViewHolder mDateTimeViewHolder;

    private int mCurrentPosition = 0;

    private boolean isAutoDateTime;

    private boolean is24Hour;

    private TimerManager timerMgr = TvManager.getInstance().getTimerManager();

    private String dateFormat;

    private Handler updateDateAndTimeHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            
            Bundle data = msg.getData();
            dateFormat = data.getString("dateFormat");
            Settings.System
                    .putString(getContentResolver(), Settings.System.DATE_FORMAT, dateFormat);
            updateTimeAndDateDisplay();
        }
    };

    private Handler updateDateHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            
            Bundle data = msg.getData();
            String year = data.getString("year");
            String month = data.getString("month");
            String day = data.getString("day");
            onDateSet(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        }
    };

    private Handler updateTimeHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            
            Bundle data = msg.getData();
            String hour = data.getString("hour");
            String minute = data.getString("minute");
            onTimeSet(Integer.parseInt(hour), Integer.parseInt(minute));
        }
    };

    private Handler updateZoneTimeHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1) {                
                updateTimeAndDateDisplay();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.date_time_setting);
        findViews();
        registerListeners();
    }
    

    private void findViews() {
        mDateTimeViewHolder = new DateTimeViewHolder(this);
        isAutoDateTime = getAutoState();
        Log.d(TAG, "isAutoDateTime, " + isAutoDateTime);
        mDateTimeViewHolder.mAutoTimeDateCheckBox.setChecked(isAutoDateTime);

        is24Hour = is24Hour();
        Log.d(TAG, "is24Hour, " + is24Hour);
        mDateTimeViewHolder.mTimeFormatCheckBox.setChecked(is24Hour);

        updateTimeAndDateDisplay();
    }

    private void registerListeners() {
        mDateTimeViewHolder.mAutoTimeDateLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mDateTimeViewHolder.setNoneBackground();
                mCurrentPosition = 0;
                mDateTimeViewHolder.setBackground(mCurrentPosition);
                changeCheckedValue();
            }
        });

        mDateTimeViewHolder.mDateSettingsLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isAutoDateTime) {
                    mDateTimeViewHolder.setNoneBackground();
                    mCurrentPosition = 1;
                    mDateTimeViewHolder.setBackground(mCurrentPosition);
                    showSettingDialog();
                }
            }
        });

        mDateTimeViewHolder.mTimeSettingsLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isAutoDateTime) {
                    mDateTimeViewHolder.setNoneBackground();
                    mCurrentPosition = 2;
                    mDateTimeViewHolder.setBackground(mCurrentPosition);
                    showSettingDialog();
                }
            }
        });

        mDateTimeViewHolder.mTimeZoneSettingsLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isAutoDateTime) {
                    mDateTimeViewHolder.setNoneBackground();
                    mCurrentPosition = 3;
                    mDateTimeViewHolder.setBackground(mCurrentPosition);
                    showSettingDialog();
                }
            }
        });

        mDateTimeViewHolder.mTimeFormatSettingsLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mDateTimeViewHolder.setNoneBackground();
                mCurrentPosition = 4;
                mDateTimeViewHolder.setBackground(mCurrentPosition);
                changeCheckedValue();
            }
        });

        mDateTimeViewHolder.mDateFormatSettingsLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mDateTimeViewHolder.setNoneBackground();
                mCurrentPosition = 5;
                mDateTimeViewHolder.setBackground(mCurrentPosition);
                showSettingDialog();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            return true;
		}
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            dropDown();
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            dropUp();
        }

        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            Log.d(TAG, "onKeyUp");
            showSettingDialog();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {


        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.i("tianky", "<<<<-----------------onResume--------------->>>>>");
        IntentFilter timeFilter = new IntentFilter();
        timeFilter.addAction(Intent.ACTION_TIME_TICK);
        timeFilter.addAction(Intent.ACTION_TIME_CHANGED);
        timeFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        registerReceiver(mTimeChangeReceiver, timeFilter, null, null);
    }
   
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mTimeChangeReceiver);
    }

    private boolean getAutoState() {
        try {
            return Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME) > 0;
        } catch (SettingNotFoundException snfe) {
            Log.d(TAG, "can not find autoTime");
            return false;
        }
    }

    private boolean is24Hour() {
        return DateFormat.is24HourFormat(this);
    }

    private void setTVTime(long millitime) {
        Time time = new Time();
        time.set(millitime);
        time.month += 1;
        try {
            timerMgr.setClkTime(time, true);
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    private void onDateSet(int year, int month, int day) {
        Log.d(TAG,"year:month:day"+year+month+day);
        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        long when = c.getTimeInMillis();
        Log.d(TAG, "getTimeInMillis, " + c.getTimeInMillis());

        if (when / 1000 < Integer.MAX_VALUE) {
            ((AlarmManager) getSystemService(Context.ALARM_SERVICE)).setTime(when);
        }

        updateTimeAndDateDisplay();
    }

    public void onTimeSet(int hourOfDay, int minute) {
        Log.d(TAG,"hourOfDay:minute"+hourOfDay+minute);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long when = c.getTimeInMillis();

        if (when / 1000 < Integer.MAX_VALUE) {
            ((AlarmManager) getSystemService(Context.ALARM_SERVICE)).setTime(when);
        }
        updateTimeAndDateDisplay();
    }

    private void dropDown() {
        switch (mCurrentPosition) {
            case 0:
                if (isAutoDateTime) {
                    mCurrentPosition = 4;
                    mDateTimeViewHolder.mAutoTimeDateLayout
                            .setBackgroundResource(R.drawable.one_px);
                    mDateTimeViewHolder.mTimeFormatSettingsLayout
                            .setBackgroundResource(R.drawable.set_button);
                    mDateTimeViewHolder.mTimeFormatSettingsLayout.requestFocus();
                } else {
                    mCurrentPosition = 1;
                    mDateTimeViewHolder.mAutoTimeDateLayout
                            .setBackgroundResource(R.drawable.one_px);
                    mDateTimeViewHolder.mDateSettingsLayout
                            .setBackgroundResource(R.drawable.set_button);
                    mDateTimeViewHolder.mDateSettingsLayout.requestFocus();
                }
                break;
            case 1:
                mCurrentPosition = 2;
                mDateTimeViewHolder.mDateSettingsLayout.setBackgroundResource(R.drawable.one_px);
                mDateTimeViewHolder.mTimeSettingsLayout
                        .setBackgroundResource(R.drawable.set_button);
                mDateTimeViewHolder.mTimeSettingsLayout.requestFocus();
                break;
            case 2:
                mCurrentPosition = 3;
                mDateTimeViewHolder.mTimeSettingsLayout.setBackgroundResource(R.drawable.one_px);
                mDateTimeViewHolder.mTimeZoneSettingsLayout
                        .setBackgroundResource(R.drawable.set_button);
                mDateTimeViewHolder.mTimeZoneSettingsLayout.requestFocus();
                break;
            case 3:
                mCurrentPosition = 4;
                mDateTimeViewHolder.mTimeZoneSettingsLayout
                        .setBackgroundResource(R.drawable.one_px);
                mDateTimeViewHolder.mTimeFormatSettingsLayout
                        .setBackgroundResource(R.drawable.set_button);
                mDateTimeViewHolder.mTimeFormatSettingsLayout.requestFocus();
                break;
            case 4:
                mCurrentPosition = 5;
                mDateTimeViewHolder.mTimeFormatSettingsLayout
                        .setBackgroundResource(R.drawable.one_px);
                mDateTimeViewHolder.mDateFormatSettingsLayout
                        .setBackgroundResource(R.drawable.set_button);
                mDateTimeViewHolder.mDateFormatSettingsLayout.requestFocus();
                break;
            default:
                break;
        }
    }

    private void dropUp() {
        switch (mCurrentPosition) {
            case 1:
                mCurrentPosition = 0;
                mDateTimeViewHolder.mDateSettingsLayout.setBackgroundResource(R.drawable.one_px);
                mDateTimeViewHolder.mAutoTimeDateLayout
                        .setBackgroundResource(R.drawable.set_button);
                mDateTimeViewHolder.mAutoTimeDateLayout.requestFocus();
                break;
            case 2:
                mCurrentPosition = 1;
                mDateTimeViewHolder.mTimeSettingsLayout.setBackgroundResource(R.drawable.one_px);
                mDateTimeViewHolder.mDateSettingsLayout
                        .setBackgroundResource(R.drawable.set_button);
                mDateTimeViewHolder.mDateSettingsLayout.requestFocus();
                break;
            case 3:
                mCurrentPosition = 2;
                mDateTimeViewHolder.mTimeZoneSettingsLayout
                        .setBackgroundResource(R.drawable.one_px);
                mDateTimeViewHolder.mTimeSettingsLayout
                        .setBackgroundResource(R.drawable.set_button);
                mDateTimeViewHolder.mTimeSettingsLayout.requestFocus();
                break;
            case 4:
                if (isAutoDateTime) {
                    mCurrentPosition = 0;
                    mDateTimeViewHolder.mTimeFormatSettingsLayout
                            .setBackgroundResource(R.drawable.one_px);
                    mDateTimeViewHolder.mAutoTimeDateLayout
                            .setBackgroundResource(R.drawable.set_button);
                    mDateTimeViewHolder.mAutoTimeDateLayout.requestFocus();
                } else {
                    mCurrentPosition = 3;
                    mDateTimeViewHolder.mTimeZoneButton.requestFocus();
                    mDateTimeViewHolder.mTimeFormatSettingsLayout
                            .setBackgroundResource(R.drawable.one_px);
                    mDateTimeViewHolder.mTimeZoneSettingsLayout
                            .setBackgroundResource(R.drawable.set_button);
                    mDateTimeViewHolder.mTimeZoneSettingsLayout.requestFocus();
                }
                break;
            case 5:
                mCurrentPosition = 4;
                mDateTimeViewHolder.mTimeFormatCheckBox.requestFocus();
                mDateTimeViewHolder.mDateFormatSettingsLayout
                        .setBackgroundResource(R.drawable.one_px);
                mDateTimeViewHolder.mTimeFormatSettingsLayout
                        .setBackgroundResource(R.drawable.set_button);
                mDateTimeViewHolder.mTimeFormatSettingsLayout.requestFocus();
                break;
            default:
                break;
        }
    }

    private void updateTimeAndDateDisplay() {
        java.text.DateFormat shortDateFormat = DateFormat.getDateFormat(this);
        Date now = Calendar.getInstance().getTime();
        String stringDateFormat = shortDateFormat.format(now);
        mDateTimeViewHolder.mDateButton.setText(shortDateFormat.format(now));
        mDateTimeViewHolder.mTimeButton.setText(DateFormat.getTimeFormat(this).format(now));
        mDateTimeViewHolder.mTimeZoneButton.setText(getTimeZoneText());
        mDateTimeViewHolder.mTimeFormatButton.setText(stringDateFormat);
    }

    private void saveAutoTimeValue() {
        Log.d(TAG, "saveAutoTimeValue, isAutoDateTime, " + isAutoDateTime);
        Settings.Global.putInt(getContentResolver(), Settings.Global.AUTO_TIME, isAutoDateTime ? 1
                : 0);
        Intent timeChanged = new Intent(MIntent.ACTION_TV_AUTO_TIME_SYNC);
        timeChanged.putExtra(MIntent.EXTRA_KEY_TV_AUTO_TIME, !isAutoDateTime);
        sendBroadcast(timeChanged);
    }

    private void saveTimeFormatValue() {
        Log.d(TAG, "saveTimeFormatValue, is24Hour, " + is24Hour);
        Settings.System.putString(getContentResolver(), Settings.System.TIME_12_24,
                is24Hour ? HOURS_24 : HOURS_12);
        updateTimeAndDateDisplay();

        Intent timeChanged = new Intent(Intent.ACTION_TIME_CHANGED);
        sendBroadcast(timeChanged);
    }

    private String getTimeZoneText() {
        TimeZone tz = java.util.Calendar.getInstance().getTimeZone();

        boolean daylight = tz.inDaylightTime(new Date());
        if (daylight) {
            try {
                timerMgr.setDaylightSavingState(true);
            } catch (TvCommonException e) {
                e.printStackTrace();
            }
        } else {
            try {
                timerMgr.setDaylightSavingState(false);
            } catch (TvCommonException e) {
                e.printStackTrace();
            }
        }
         
        StringBuilder sb = new StringBuilder();
        sb.append(formatOffset(tz.getRawOffset())).append(", ")
                .append(tz.getDisplayName(daylight, TimeZone.LONG));        
        return sb.toString();
    }
   
    private char[] formatOffset(int off) {
        off = off / 1000 / 60;

        char[] buf = new char[9];
        buf[0] = 'G';
        buf[1] = 'M';
        buf[2] = 'T';

        if (off < 0) {
            buf[3] = '-';
            off = -off;
        } else {
            buf[3] = '+';
        }

        int hours = off / 60;
        int minutes = off % 60;

        buf[4] = (char) ('0' + hours / 10);
        buf[5] = (char) ('0' + hours % 10);

        buf[6] = ':';

        buf[7] = (char) ('0' + minutes / 10);
        buf[8] = (char) ('0' + minutes % 10);

        return buf;
    }

    private BroadcastReceiver mTimeChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            updateTimeAndDateDisplay();       
        }
    };

    private void showSettingDialog() {
        switch (mCurrentPosition) {
            case 0:
                changeCheckedValue();
                break;
            case 1:
                if (!isAutoDateTime) {
                    DateSettingDialog dateSettingDialog = new DateSettingDialog(
                            DateTimeSettings.this, updateDateHandler);
                    dateSettingDialog.show();
                }
                break;
            case 2:
                if (!isAutoDateTime) {
                    TimeSettingDialog timeSettingDialog = new TimeSettingDialog(
                            DateTimeSettings.this, updateTimeHandler, is24Hour);
                    timeSettingDialog.show();
                }
                break;
            case 3:
                if (!isAutoDateTime) {
                    ZoneListSelectDialog zoneListSelectDialog = new ZoneListSelectDialog(
                            DateTimeSettings.this, updateZoneTimeHandler);
                    zoneListSelectDialog.show();
                }
                break;
            case 4:
                changeCheckedValue();
                break;
            case 5:
                DateFormatSelectDialog dateFormatSelectDialog = new DateFormatSelectDialog(
                        DateTimeSettings.this, updateDateAndTimeHandler);
                dateFormatSelectDialog.show();
                break;
            default:
                break;
        }
    }

    private void changeCheckedValue() {
        if (mCurrentPosition == 0) {
            isAutoDateTime = !isAutoDateTime;
            mDateTimeViewHolder.mAutoTimeDateCheckBox.setChecked(isAutoDateTime);
            saveAutoTimeValue();
        } else if (mCurrentPosition == 4) {
            is24Hour = !is24Hour;
            mDateTimeViewHolder.mTimeFormatCheckBox.setChecked(is24Hour);
            saveTimeFormatValue();
        }
    }
}
