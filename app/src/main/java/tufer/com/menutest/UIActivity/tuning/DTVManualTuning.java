//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2014 MStar Semiconductor, Inc. All rights reserved.
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

package tufer.com.menutest.UIActivity.tuning;

import android.app.Instrumentation;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.SharedPreferences;

import tufer.com.menutest.UIActivity.component.ComboButton;
import tufer.com.menutest.UIActivity.component.MultiSelector;
import tufer.com.menutest.UIActivity.dtv.ewbs.EWBSInfoActivity;
import tufer.com.menutest.Util.LittleDownTimer;
import tufer.com.menutest.R;
import tufer.com.menutest.UIActivity.MainActivity;
import tufer.com.menutest.Util.TvIntent;
import tufer.com.menutest.UIActivity.channel.ChannelActivity;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.vo.TvTypeInfo;
import com.mstar.android.tvapi.common.listener.OnMhlEventListener;
import com.mstar.android.tvapi.common.vo.DtvProgramSignalInfo;
import com.mstar.android.tvapi.dtv.atsc.vo.AtscMainListChannelInformation;
import com.mstar.android.tvapi.dtv.vo.DtvEventScan;
import com.mstar.android.tvapi.dtv.dvb.vo.DvbMuxInfo;
import com.mstar.android.tvapi.dtv.vo.RfInfo;
import com.mstar.android.tvapi.dtv.dvb.dvbs.vo.SatelliteInfo;
import com.mstar.android.tvapi.dtv.dvb.dvbs.vo.DvbsScanParam;
import com.mstar.android.tv.TvChannelManager.DvbcScanParam;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvChannelManager.OnDtvScanEventListener;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvDvbChannelManager;
import com.mstar.android.tv.TvCountry;
import tufer.com.menutest.UIActivity.holder.ViewHolder;
import tufer.com.menutest.UIActivity.MstarBaseActivity;
import tufer.com.menutest.Util.Constant;
import tufer.com.menutest.Util.Tools;
import tufer.com.menutest.Util.Utility;

public class DTVManualTuning extends MstarBaseActivity {
    /** Called when the activity is first created. */

    private static final String TAG = "DTVManualTuning";

    private int mMinCh = 0;

    private int mMaxCh = 0;

    private boolean mDirectChangeCh = false;

    private boolean mOnAutoSwitch = false;

    private static final int CHANNEL_INVALID = 0;

    private static final int CHANNEL_MIN_AIR = 2;

    private static final int CHANNEL_MAX_AIR = 69;

    private static final int CHANNEL_MIN_CABLE = 1;

    private static final int CHANNEL_MAX_CABLE = 135;

    private static final int USER_CHANGE_CHANNEL_TIMEOUT = 2000;

    private static final int INVALID_PHYSICAL_CHANNEL_NUMBER = 0xFF;

    private int mTvSystem = 0;

    private ViewHolder viewholder_dtvmanualtuning;

    private int mChannelNumber = 0;

    private int modulationindex = 2;

    private int mSymbolRate;

    private int mFrequency;

    private int inputfreq = 0;

    private int inputsymbol = 0;

    private int mDvbtRouteIndex = TvChannelManager.TV_ROUTE_NONE;

    private int mDtmbRouteIndex = TvChannelManager.TV_ROUTE_NONE;

    private int mDvbcRouteIndex = TvChannelManager.TV_ROUTE_NONE;

    private int mPreviousChannelNumber = -1;

    private int mDvbsRouteIndex = TvChannelManager.TV_ROUTE_NONE;

    private String strfreq = new String();

    private String strsymbol = new String();

    private int CAB_Type = DvbcScanParam.DVBC_CAB_TYPE_QAM_64;

    private int mMaxFrequencyNumber = 999;

    private int mFrequencyDigitsBound = 4;

    private int mSymbolRateDigitsBound = 5;

    private int mDefaultFrequency = 0;

    private int mDefaultSymbolRate = 0;

    private String[] modulationtype = {
            "16 QAM", "32 QAM", "64 QAM", "128 QAM", "256 QAM"
    };

    private boolean bManualScanByUser = false;

    private final static short DTV_SIGNAL_REFRESH_UI = 0x01;

    private final int DELAY_TO_SET_ITEM_SELECTED_LISTENER = 0x02;

    private DtvProgramSignalInfo signalInfo;

    private boolean mIsDtvTuning = false;

    private boolean mRunthread = true;

    private boolean mScanEnd = false;

    private TvChannelManager mTvChannelManager = null;

    private OnDtvScanEventListener mDtvScanEventListener = null;

    private Handler mDtvUiEventHandler = null;

    private Spinner mCountrySelSpinner;

    private String[] mCountryList = null;

    private ArrayAdapter<String> mlistAdapter;

    private Handler mDirectChangeChTimeout = new Handler();

    private MultiSelector mMultiSelector = null;

    private boolean[] mSatelliteList = null;

    private String[] mSatelliteStrList = null;

    private String[] mDvbsLNBType = null;

    private BroadcastReceiver mReceiver = null;

    private int mDtvCount = 0;

    private int mRadioCount = 0;

    private int mDataCount = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (msg.what == LittleDownTimer.TIME_OUT_MSG) {
                DTVManualTuning.this.finish();
                Intent intent = new Intent(DTVManualTuning.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
    };

    private void isdbCheckRf() {
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
            RfInfo rfInfo = null;
            rfInfo = mTvChannelManager.getRfInfo(TvChannelManager.RF_INFO, mChannelNumber);
            if (rfInfo.rfPhyNum == INVALID_PHYSICAL_CHANNEL_NUMBER) {
                mChannelNumber = mPreviousChannelNumber;
                viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_channelnum_val
                        .setText(String.valueOf(mChannelNumber));
            } else {
                mPreviousChannelNumber = mChannelNumber;
                mTvChannelManager.changeDtvManualScanRF(mChannelNumber);
            }
        }
    }

    private Runnable mDirectChangeChTimeoutRunnable = new Runnable() {
        @Override
        public void run() {
            mDirectChangeCh = false;
            isdbCheckRf();
        }
    };

    private class DtvScanEventListener implements OnDtvScanEventListener {
        @Override
        public boolean onDtvScanEvent(int what, int arg1, int arg2, Object info) {
            if (what == TvChannelManager.TVPLAYER_DTV_AUTO_TUNING_SCAN_INFO) {
                Message msg = mDtvUiEventHandler.obtainMessage(what, info);
                mDtvUiEventHandler.sendMessage(msg);
                return true;
            }
            return false;
        }
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                if (!mRunthread)
                    break;
                signalInfo = mTvChannelManager.getCurrentSignalInformation();

                dtvSignalHandler.sendEmptyMessageDelayed(DTV_SIGNAL_REFRESH_UI, 500);
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // dtvSignalHandler.postDelayed(runnable, 600);
        }
    };

    public void showGalaxySeletionMenu() {
        mMultiSelector = new MultiSelector(this, AlertDialog.THEME_HOLO_DARK);

        DialogInterface.OnCancelListener onCancelListener = new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mSatelliteList = mMultiSelector.getResults();
                // set text view to show first selected satellite
                for (int i = 0; i < mSatelliteList.length; i++) {
                    if (true == mSatelliteList[i]) {
                        viewholder_dtvmanualtuning.mTextViewDtvmanualtuningGalaxy
                                .setText(mSatelliteStrList[i]);
                        TvDvbChannelManager.getInstance().setCurrentSatelliteNumber(i);
                        break;
                    }
                    // no satellite be checked, set text to number 0
                    if (i == (mSatelliteList.length - 1)) {
                        viewholder_dtvmanualtuning.mTextViewDtvmanualtuningGalaxy
                                .setText(mSatelliteStrList[0]);
                        TvDvbChannelManager.getInstance().setCurrentSatelliteNumber(0);
                    }
                }
            }
        };

        // FIXME: use string resource
        mMultiSelector.setSingleChoiceItems(mSatelliteStrList, 0);
        mMultiSelector.setOnCancelListener(onCancelListener);
        mMultiSelector.setTitle("GALAXY selection");
        mMultiSelector.show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dtvmanualtuning);
        mTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();
        viewholder_dtvmanualtuning = new ViewHolder(DTVManualTuning.this);
        viewholder_dtvmanualtuning.findViewForDtvManualTuning();
        InitialProgressValueForSignalQuality();
        InitialProgressValueForSignalStrengh();

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(TvIntent.ACTION_CIPLUS_TUNER_UNAVAIABLE)) {
                    Log.i(TAG, "Receive ACTION_CIPLUS_TUNER_UNAVAIABLE...");
                    finish();
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(TvIntent.ACTION_CIPLUS_TUNER_UNAVAIABLE);
        registerReceiver(mReceiver, filter);


        mTvChannelManager = TvChannelManager.getInstance();
        if (TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_DTV) {
            TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
        }
        isSearchByUser = false;
        mDvbtRouteIndex = mTvChannelManager
                .getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DVBT);
        if (mDvbtRouteIndex < 0) {
            mDvbtRouteIndex = mTvChannelManager
                    .getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DVBT2);
        }
        mDtmbRouteIndex = mTvChannelManager
                .getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DTMB);
        mDvbcRouteIndex = mTvChannelManager
                .getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DVBC);
        mDvbsRouteIndex = mTvChannelManager
                .getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DVBS);
        if (mDvbsRouteIndex < 0) {
            mDvbsRouteIndex = mTvChannelManager
                    .getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DVBS2);
        }

        getCurrentFreqAndSymRate();
        mTvChannelManager.stopDtvScan();
        updatedtvManualtuningComponents();
        bManualScanByUser = false;
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            if (TvAtscChannelManager.getInstance().getDtvAntennaType() == TvChannelManager.DTV_ANTENNA_TYPE_AIR) {
                // FIXME: get value from SN
                mMinCh = CHANNEL_MIN_AIR;
                // FIXME: get value from SN
                mMaxCh = CHANNEL_MAX_AIR;
            } else {
                // FIXME: get value from SN
                mMinCh = CHANNEL_MIN_CABLE;
                // FIXME: get value from SN
                mMaxCh = CHANNEL_MAX_CABLE;
            }

            mTvChannelManager.setDtvManualScanByRF(mChannelNumber);

            viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_channelnum
                    .setOnFocusChangeListener(new OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                mDirectChangeCh = false;
                                mDirectChangeChTimeout
                                        .removeCallbacks(mDirectChangeChTimeoutRunnable);
                            } else {
                                Utility.ttsSepakLinearLayout(viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_channelnum);
                            }
                        }
                    });
            viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_starttuning
                    .setOnFocusChangeListener(new OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) {
                                Utility.ttsSepakLinearLayout(viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_starttuning);
                            }
                        }
                    });
        } else if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
            viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_channelnum
                    .setOnFocusChangeListener(new OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                if (true == mDirectChangeCh) {
                                    mDirectChangeCh = false;
                                    mDirectChangeChTimeout.removeCallbacks(mDirectChangeChTimeoutRunnable);
                                    isdbCheckRf();
                                }
                            }
                        }
                    });
        } else {
            int currentIndex = mTvChannelManager.getCurrentDtvRouteIndex();
            if (mDvbsRouteIndex == currentIndex) {
                mMaxFrequencyNumber = 99999;
                mFrequencyDigitsBound = 6;
                mSymbolRateDigitsBound = 6;

                viewholder_dtvmanualtuning.mTextViewDtvmanualtuningGalaxy = (TextView) findViewById(R.id.linearlayout_cha_dtvmanualtuning_galaxy);
                viewholder_dtvmanualtuning.mComboBtnDtvmanualtuningPolarization = new ComboButton(
                        this, getResources().getStringArray(
                                R.array.str_arr_dtvmanualtuning_polarization_vals),
                        R.id.linearlayout_cha_dtvmanualtuning_polarization, 1, 2, ComboButton.DIRECT_SWITCH);
                viewholder_dtvmanualtuning.mComboBtnDtvmanualtuningNetwork = new ComboButton(
                        this,
                        getResources().getStringArray(R.array.str_arr_dtvmanualtuning_network_vals),
                        R.id.linearlayout_cha_dtvmanualtuning_network, 1, 2, ComboButton.DIRECT_SWITCH);
                viewholder_dtvmanualtuning.mComboBtnDtvmanualtuningScanMode = new ComboButton(this,
                        getResources().getStringArray(
                                R.array.str_arr_dtvmanualtuning_scan_mode_vals),
                        R.id.linearlayout_cha_dtvmanualtuning_scan_mode, 1, 2, ComboButton.DIRECT_SWITCH);
                viewholder_dtvmanualtuning.mComboBtnDtvmanualtuningServiceType = new ComboButton(
                        this, getResources().getStringArray(
                                R.array.str_arr_dtvmanualtuning_service_type_vals),
                        R.id.linearlayout_cha_dtvmanualtuning_service_type, 1, 2, ComboButton.DIRECT_SWITCH);
                mDvbsLNBType = getResources().getStringArray(
                        R.array.str_arr_dtvmanualtuning_lnbtype_vals);

                OnClickListener comoBtnOnClickListener = new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentid = getCurrentFocus().getId();
                        LinearLayout container = (LinearLayout) v;
                        if (container.isSelected()) {
                            container.getChildAt(0).setVisibility(View.VISIBLE);
                            container.getChildAt(3).setVisibility(View.VISIBLE);
                        } else {
                            container.getChildAt(0).setVisibility(View.GONE);
                            container.getChildAt(3).setVisibility(View.GONE);
                        }

                        switch (currentid) {
                            case R.id.linearlayout_cha_dtvmanualtuning_galaxy:
                                showGalaxySeletionMenu();
                                break;
                            default:
                                break;
                        }
                    }
                };

                viewholder_dtvmanualtuning.mComboBtnDtvmanualtuningPolarization
                        .setOnClickListener(comoBtnOnClickListener);
                viewholder_dtvmanualtuning.mComboBtnDtvmanualtuningNetwork
                        .setOnClickListener(comoBtnOnClickListener);
                viewholder_dtvmanualtuning.mComboBtnDtvmanualtuningScanMode
                        .setOnClickListener(comoBtnOnClickListener);
                viewholder_dtvmanualtuning.mComboBtnDtvmanualtuningServiceType
                        .setOnClickListener(comoBtnOnClickListener);

                OnFocusChangeListener comboBtnFocusListener = new OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        LinearLayout container = (LinearLayout) v;
                        container.getChildAt(0).setVisibility(View.GONE);
                        container.getChildAt(3).setVisibility(View.GONE);
                    }
                };
                viewholder_dtvmanualtuning.mComboBtnDtvmanualtuningPolarization
                        .setOnFocusChangeListener(comboBtnFocusListener);
                viewholder_dtvmanualtuning.mComboBtnDtvmanualtuningNetwork
                        .setOnFocusChangeListener(comboBtnFocusListener);
                viewholder_dtvmanualtuning.mComboBtnDtvmanualtuningScanMode
                        .setOnFocusChangeListener(comboBtnFocusListener);
                viewholder_dtvmanualtuning.mComboBtnDtvmanualtuningServiceType
                        .setOnFocusChangeListener(comboBtnFocusListener);

                OnClickListener textViewOnClickListener = new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentid = getCurrentFocus().getId();
                        switch (currentid) {
                            case R.id.linearlayout_cha_dtvmanualtuning_galaxy:
                                showGalaxySeletionMenu();
                                break;
                            default:
                                break;
                        }
                    }
                };
                viewholder_dtvmanualtuning.mTextViewDtvmanualtuningGalaxy
                        .setOnClickListener(textViewOnClickListener);

                // init satellite list used for selecting satellite
                int satCount = TvDvbChannelManager.getInstance().getCurrentSatelliteCount();
                mSatelliteStrList = new String[satCount];
                mSatelliteList = new boolean[satCount];
                for (int i = 0; i < satCount; i++) {
                    // update satellite list
                    SatelliteInfo satInfo = TvDvbChannelManager.getInstance().getSatelliteInfo(i);
                    mSatelliteStrList[i] = i + " " + satInfo.satName + " "
                            + mDvbsLNBType[satInfo.lnbType];
                    // update default enable list to false
                    mSatelliteList[i] = false;
                }
                // show current satellite naming as default one
                int satNumber = TvDvbChannelManager.getInstance().getCurrentSatelliteNumber();
                mSatelliteList[satNumber] = true;
                viewholder_dtvmanualtuning.mTextViewDtvmanualtuningGalaxy
                        .setText(mSatelliteStrList[satNumber]);
            }
        }

        mCountrySelSpinner = (Spinner) findViewById(R.id.DtvManualTuneSpinner);
        if (mTvSystem != TvCommonManager.TV_SYSTEM_ISDB)
            mCountrySelSpinner.setVisibility(View.GONE);
        mCountryList = getResources().getStringArray(R.array.str_arr_autotuning_country);
        mlistAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                mCountryList);
        mCountrySelSpinner.setAdapter(mlistAdapter);
        mCountrySelSpinner.setSelection(mTvChannelManager.getSystemCountryId());
        mSpinnerHandler.sendEmptyMessageDelayed(DELAY_TO_SET_ITEM_SELECTED_LISTENER, 100);

        mRunthread = true;
        new Thread(runnable).start();
        TvManager.getInstance().getMhlManager().setOnMhlEventListener(new OnMhlEventListener() {

            @Override
            public boolean onKeyInfo(int arg0, int arg1, int arg2) {
                Log.d(TAG, "onKeyInfo");
                return false;
            }

            @Override
            public boolean onAutoSwitch(int arg0, int arg1, int arg2) {
                Log.d(TAG, "onAutoSwitch");
                mOnAutoSwitch = true;
                finish();
                return false;
            }
        });

        mDtvUiEventHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                updateDtvTuningScanInfo((DtvEventScan) msg.obj);
            }
        };
    }

    private Handler dtvSignalHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case DTV_SIGNAL_REFRESH_UI:
                    if (null == signalInfo) {
                        return;
                    }
                    if (signalInfo.quality <= 0) {
                        setProgressValueForSignalQuality(0);
                    } else {
                        setProgressValueForSignalQuality(signalInfo.quality / 10);
                    }
                    if (signalInfo.strength <= 0) {
                        setProgressValueForSignalStrengh(0);
                    } else {
                        setProgressValueForSignalStrengh(signalInfo.strength / 10);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private Handler mSpinnerHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DELAY_TO_SET_ITEM_SELECTED_LISTENER:
                    mCountrySelSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                            mTvChannelManager.setSystemCountryId(position);
                            if (true == Tools.isEwbsSupport()) {
                                SharedPreferences settings = getSharedPreferences(Constant.PREFERENCES_TV_SETTING,
                                        Context.MODE_PRIVATE);
                                if (false == settings.getBoolean(Constant.PREFERENCES_IS_EWBS_AREA_CODE_SELECTED, false)) {
                                    Intent intent = new Intent();
                                    intent.putExtra("forManualTuning", true);
                                    intent.setClass(DTVManualTuning.this, EWBSInfoActivity.class);
                                    startActivity(intent);
                                    return;
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };

    public boolean MapKeyPadToIR(int keyCode, KeyEvent event) {
        String deviceName = InputDevice.getDevice(event.getDeviceId()).getName();
        if (!deviceName.equals("MStar Smart TV Keypad"))
            return false;
        switch (keyCode) {
            case KeyEvent.KEYCODE_CHANNEL_UP:
                keyInjection(KeyEvent.KEYCODE_DPAD_UP);
                return true;
            case KeyEvent.KEYCODE_CHANNEL_DOWN:
                keyInjection(KeyEvent.KEYCODE_DPAD_DOWN);
                return true;
            default:
                return false;
        }

    }

    private void keyInjection(final int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP
                || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            new Thread() {
                public void run() {
                    try {
                        Instrumentation inst = new Instrumentation();
                        inst.sendKeyDownUpSync(keyCode);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }
            }.start();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC && keyCode != KeyEvent.KEYCODE_BACK) {
            if (mScanEnd == true) {
                return true;
            }
        }
        // If MapKeyPadToIR returns true,the previous keycode has been changed
        // to responding
        // android d_pad keys,just return.
        if (MapKeyPadToIR(keyCode, event))
            return true;
        Intent intent = new Intent();
        int currentid = getCurrentFocus().getId();
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                switch (currentid) {
                    case R.id.linearlayout_cha_dtvmanualtuning_channelnum:
                        onChannelManualTune(TvChannelManager.NEXT_RF);
                        break;
                    case R.id.linearlayout_cha_dtvmanualtuning_modulation:
                        if (modulationindex == DvbcScanParam.DVBC_CAB_TYPE_QAM_256)
                            modulationindex = 0;
                        else
                            modulationindex++;
                        CAB_Type = modulationindex;
                        viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_modulation_val
                                .setText(modulationtype[modulationindex]);
                        break;
                    case R.id.linearlayout_cha_dtvmanualtuning_symbol:
                        break;
                    case R.id.linearlayout_cha_dtvmanualtuning_frequency: {
                        mFrequency = (mFrequency + 1) % mMaxFrequencyNumber;
                        updatedtvManualtuningComponents();
                    }
                        break;
                    default:
                        break;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                switch (currentid) {
                    case R.id.linearlayout_cha_dtvmanualtuning_channelnum:
                        onChannelManualTune(TvChannelManager.PREVIOUS_RF);
                        break;
                    case R.id.linearlayout_cha_dtvmanualtuning_modulation:
                        if (modulationindex == DvbcScanParam.DVBC_CAB_TYPE_QAM_16)
                            modulationindex = DvbcScanParam.DVBC_CAB_TYPE_QAM_256;
                        else
                            modulationindex--;
                        CAB_Type = modulationindex;
                        viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_modulation_val
                                .setText(modulationtype[modulationindex]);
                        break;
                    case R.id.linearlayout_cha_dtvmanualtuning_symbol:
                        break;
                    case R.id.linearlayout_cha_dtvmanualtuning_frequency:
                        mFrequency = (mFrequency + mMaxFrequencyNumber - 1) % mMaxFrequencyNumber;
                        updatedtvManualtuningComponents();
                        break;
                    default:
                        break;
                }
                break;
            case KeyEvent.KEYCODE_0:
            case KeyEvent.KEYCODE_1:
            case KeyEvent.KEYCODE_2:
            case KeyEvent.KEYCODE_3:
            case KeyEvent.KEYCODE_4:
            case KeyEvent.KEYCODE_5:
            case KeyEvent.KEYCODE_6:
            case KeyEvent.KEYCODE_7:
            case KeyEvent.KEYCODE_8:
            case KeyEvent.KEYCODE_9:
                switch (currentid) {
                    case R.id.linearlayout_cha_dtvmanualtuning_channelnum:
                        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                            int currNumLen = 0;
                            if (mDirectChangeCh == false) {
                                if (KeyEvent.KEYCODE_0 == keyCode) {
                                    // just ignore input when first input is 0.
                                    break;
                                }
                                mDirectChangeCh = true;
                                mChannelNumber = keyCode - KeyEvent.KEYCODE_0;
                            } else if (getIntLen(mChannelNumber) >= getIntLen(mMaxCh)) {
                                if (KeyEvent.KEYCODE_0 == keyCode) {
                                    // just ignore input when first input is 0.
                                    break;
                                }
                                mChannelNumber = keyCode - KeyEvent.KEYCODE_0;
                            } else {
                                mChannelNumber = mChannelNumber * 10 + (keyCode - KeyEvent.KEYCODE_0);
                                if (mChannelNumber > mMaxCh) {
                                    mChannelNumber = mMaxCh;
                                }
                                if (mChannelNumber < mMinCh) {
                                    mChannelNumber = mMinCh;
                                }
                            }

                            mTvChannelManager.changeDtvManualScanRF(mChannelNumber);
                            viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_channelnum_val
                                    .setText(Integer.toString(mChannelNumber));
                        } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                            if (mDirectChangeCh == false) {
                                // it's the first input number and it equals to
                                // 0, do nothing
                                if (KeyEvent.KEYCODE_0 == keyCode) {
                                    break;
                                }
                                mDirectChangeCh = true;
                                mChannelNumber = keyCode - KeyEvent.KEYCODE_0;
                            } else if (mChannelNumber >= 10) {
                                mChannelNumber = keyCode - KeyEvent.KEYCODE_0;
                            } else {
                                mChannelNumber = mChannelNumber * 10 + (keyCode - KeyEvent.KEYCODE_0);
                            }
                            viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_channelnum_val
                                    .setText(Integer.toString(mChannelNumber));
                            initScanResult();
                            mTvChannelManager.stopDtvScan();
                            LittleDownTimer.resetMenu();
                            LittleDownTimer.resumeMenu();
                        }
                        mDirectChangeChTimeout.removeCallbacks(mDirectChangeChTimeoutRunnable);
                        mDirectChangeChTimeout.postDelayed(mDirectChangeChTimeoutRunnable,
                                USER_CHANGE_CHANNEL_TIMEOUT);
                        break;
                    case R.id.linearlayout_cha_dtvmanualtuning_frequency:
                        inputfreq = keyCode - KeyEvent.KEYCODE_0;
                        inputFrequencyNumber(inputfreq);
                        break;
                    case R.id.linearlayout_cha_dtvmanualtuning_symbol:
                        inputsymbol = keyCode - KeyEvent.KEYCODE_0;
                        inputSymbolNumber(inputsymbol);
                        break;
                    default:
                        break;
                }
                break;
            case KeyEvent.KEYCODE_ENTER:
                switch (currentid) {
                    case R.id.linearlayout_cha_dtvmanualtuning_starttuning: {
                        if (Tools.isBox()) {
                            Log.d(TAG, "dtv manual tuning for box");
                            // wait for the tuning done
                            if (mIsDtvTuning) {
                                Toast.makeText(this, R.string.wait_for_tuning_hint,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                isSearchByUser = true;
                                startdtvmanutuning();
                            }
                            return super.onKeyDown(keyCode, event);
                        }
                        Log.d(TAG, "dtv manual tuning");
                        isSearchByUser = true;
                        startdtvmanutuning();
                    }
                        break;
                    default:
                        break;
                }
                break;
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_MENU:
                intent.setAction(TvIntent.MAINMENU);
                intent.putExtra("currentPage", ChannelActivity.CHANNEL_PAGE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                finish();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private int getIntLen(int num) {
        int numLen = 1;

        while (num > 9) {
            numLen++;
            num /= 10;
        }
        return numLen;
    }

    @Override
    public void onUserInteraction() {
            LittleDownTimer.resetMenu();
        super.onUserInteraction();
    }

    // Whether search is triggered by user or auto start when enter this screen.
    private boolean isSearchByUser = false;

    private void startdtvmanutuning() {
        LittleDownTimer.pauseMenu();
        LittleDownTimer.resetMenu();
        mIsDtvTuning = true;
        String Sfreq = viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_frequency_val.getText()
                .toString();
        String Ssymbol = viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_symbol_val.getText()
                .toString();
        mSymbolRate = (short) Integer.parseInt(Ssymbol);
        mFrequency = Integer.parseInt(Sfreq);
        Log.d(TAG, "symb=" + mSymbolRate + " freq=" + mFrequency);
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            mTvChannelManager.startDtvManualScan();
        } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
            mTvChannelManager.setDtvManualScanByRF(mChannelNumber);
            mTvChannelManager.startDtvManualScan();
        } else {
            if (mTvChannelManager.getCurrentDtvRouteIndex() == mDvbcRouteIndex) {
                mTvChannelManager.setDvbcScanParam((short) mSymbolRate, CAB_Type, 0, 0,
                        (short) 0x0000);
                mTvChannelManager.setDtvManualScanByFreq(mFrequency * 1000);
                mTvChannelManager.startDtvManualScan();
            } else if (mTvChannelManager.getCurrentDtvRouteIndex() == mDvbtRouteIndex) {
                mTvChannelManager.setDtvManualScanByRF(mChannelNumber);
                mTvChannelManager.startDtvManualScan();
            } else if (mTvChannelManager.getCurrentDtvRouteIndex() == mDtmbRouteIndex) {
                mTvChannelManager.setDtvManualScanByRF(mChannelNumber);
                mTvChannelManager.startDtvManualScan();
            } else if (mTvChannelManager.getCurrentDtvRouteIndex() == mDvbsRouteIndex) {
                DvbsScanParam param = new DvbsScanParam();
                param.setSymbolRate(mSymbolRate);
                param.setNetworkSearch(viewholder_dtvmanualtuning.mComboBtnDtvmanualtuningNetwork
                        .getIdx());
                param.setScanMode(viewholder_dtvmanualtuning.mComboBtnDtvmanualtuningScanMode
                        .getIdx());
                param.setServiceType(viewholder_dtvmanualtuning.mComboBtnDtvmanualtuningServiceType
                        .getIdx());
                if (0 == viewholder_dtvmanualtuning.mComboBtnDtvmanualtuningPolarization.getIdx()) {
                    param.setPolarization(false);
                } else {
                    param.setPolarization(true);
                }
                TvDvbChannelManager.getInstance().setDvbsScanParam(param);
                mTvChannelManager.setDtvManualScanByFreq(mFrequency);
                mTvChannelManager.startDtvManualScan();
            }
        }
        bManualScanByUser = true;
        mPreviousChannelNumber = mChannelNumber;
    }

    private void getCurrentFreqAndSymRate() {
        /*
         * if current dtv program count is not zero, use current frequency and
         * symbol rate otherwise using default frequency and default symbol
         * rate.
         */
        int m_nServiceNum = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
        if (m_nServiceNum > 0) {
            DvbMuxInfo dmi = mTvChannelManager.getCurrentMuxInfo();
            if (dmi != null) {
                // dvbs frequency needn't to divide , bacause it's already MHz.
                if (mTvChannelManager.getCurrentDtvRouteIndex() == mDvbsRouteIndex) {
                    mFrequency = dmi.frequency;
                } else {
                    mFrequency = (dmi.frequency / 1000);
                }
                mSymbolRate = dmi.symbRate;
            } else {
                // use default value if get muxinfo error
                mFrequency = mDefaultFrequency;
                mSymbolRate = mDefaultSymbolRate;
                Log.e(TAG, "getCurrentMuxInfo error");
            }

        } else {
            mFrequency = mDefaultFrequency;
            mSymbolRate = mDefaultSymbolRate;
        }

    }

    private void updatedtvManualtuningComponents() {
        // set item show/hide
        viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_symbol_val.setText(Integer
                .toString(mSymbolRate));
        viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_frequency_val.setText(Integer
                .toString(mFrequency));
        if ((mTvChannelManager.getCurrentDtvRouteIndex() == mDvbtRouteIndex)
                || (mTvChannelManager.getCurrentDtvRouteIndex() == mDtmbRouteIndex)
                || (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC)
                || (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB)) {
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                final int country = mTvChannelManager.getSystemCountryId();
                if (!((country == TvCountry.JAPAN) || (country == TvCountry.PHILIPPINES)
                        || (country == TvCountry.THAILAND) || (country == TvCountry.MALDIVES)
                        || (country == TvCountry.URUGUAY) || (country == TvCountry.PERU)
                        || (country == TvCountry.ARGENTINA) || (country == TvCountry.CHILE)
                        || (country == TvCountry.VENEZUELA) || (country == TvCountry.ECUADOR)
                        || (country == TvCountry.COSTARICA) || (country == TvCountry.PARAGUAY)
                        || (country == TvCountry.BOLIVIA) || (country == TvCountry.BELIZE)
                        || (country == TvCountry.NICARAGUA) || (country == TvCountry.GUATEMALA) || (country == TvCountry.BRAZIL))) {
                    Log.i(TAG, "Not ISDB country : " + country);
                    Log.i(TAG, "Set to default ISDB country E_BRAZIL");
                    mTvChannelManager.setSystemCountryId(TvCountry.BRAZIL);
                }
            }
            RfInfo rfInfo = null;
            String name = "";

            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                AtscMainListChannelInformation chInfo = TvAtscChannelManager.getInstance().getCurrentChannelInformation();

                mChannelNumber = chInfo.rfCh;
                Log.d(TAG, "Current RF channel = " + chInfo.rfCh);
                if (mChannelNumber == CHANNEL_INVALID) {
                    if (TvAtscChannelManager.getInstance().getDtvAntennaType() == TvChannelManager.DTV_ANTENNA_TYPE_AIR) {
                        // FIXME: get value from SN
                        mChannelNumber = CHANNEL_MIN_AIR;
                    } else {
                        // FIXME: get value from SN
                        mChannelNumber = CHANNEL_MIN_CABLE;
                    }
                }

                viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_channelnum_val.setText(String.valueOf(mChannelNumber));
                mPreviousChannelNumber = mChannelNumber;
            } else {
                rfInfo = mTvChannelManager.getRfInfo(TvChannelManager.FIRST_TO_SHOW_RF, 0);
                if (rfInfo != null) {
                    mChannelNumber = rfInfo.rfPhyNum;
                    // Only DTMB and DVBT shows RF Name instead rfPhyNum (e.g 24-1 / K30)
                    if ((mTvChannelManager.getCurrentDtvRouteIndex() != mDvbtRouteIndex) &&
                            (mTvChannelManager.getCurrentDtvRouteIndex() != mDtmbRouteIndex)) {
                        name = String.valueOf(mChannelNumber);
                    } else {
                        name = rfInfo.rfName;
                    }

                    viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_channelnum_val.setText(name);
                    mPreviousChannelNumber = mChannelNumber;
                }
            }
            LinearLayout ln = (LinearLayout) findViewById(R.id.linearlayout_cha_dtvmanualtuning_frequency);
            ln.setVisibility(View.GONE);
            ln = (LinearLayout) findViewById(R.id.linearlayout_cha_dtvmanualtuning_modulation);
            ln.setVisibility(View.GONE);
            ln = (LinearLayout) findViewById(R.id.linearlayout_cha_dtvmanualtuning_symbol);
            ln.setVisibility(View.GONE);
        } else if (mTvChannelManager.getCurrentDtvRouteIndex() == mDvbsRouteIndex) {
            LinearLayout ln = (LinearLayout) findViewById(R.id.linearlayout_cha_dtvmanualtuning_channelnum);
            ln.setVisibility(View.GONE);
            ln = (LinearLayout) findViewById(R.id.linearlayout_cha_dtvmanualtuning_modulation);
            ln.setVisibility(View.GONE);
            ln = (LinearLayout) findViewById(R.id.linearlayout_cha_dtvmanualtuning_polarization);
            ln.setVisibility(View.VISIBLE);
            ln = (LinearLayout) findViewById(R.id.linearlayout_cha_dtvmanualtuning_network);
            ln.setVisibility(View.VISIBLE);
            ln = (LinearLayout) findViewById(R.id.linearlayout_cha_dtvmanualtuning_scan_mode);
            ln.setVisibility(View.VISIBLE);
            ln = (LinearLayout) findViewById(R.id.linearlayout_cha_dtvmanualtuning_service_type);
            ln.setVisibility(View.VISIBLE);
            TextView tv = (TextView) findViewById(R.id.linearlayout_cha_dtvmanualtuning_galaxy);
            tv.setVisibility(View.VISIBLE);
        } else {
            LinearLayout ln = (LinearLayout) findViewById(R.id.linearlayout_cha_dtvmanualtuning_channelnum);
            ln.setVisibility(View.GONE);
        }
    }

    private void setProgressValueForSignalQuality(int val) {
        if (val <= 10 && val > 0) {
            for (int i = 0; i <= val - 1; i++) {
                ImageView searchImage = (ImageView) (viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_signalquality_val
                        .getChildAt(i));
                searchImage.setImageDrawable(getResources().getDrawable(
                        R.drawable.picture_serchprogressbar_solid));
            }
            for (int i = val; i <= 9; i++) {
                ImageView searchImage = (ImageView) (viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_signalquality_val
                        .getChildAt(i));
                searchImage.setImageDrawable(getResources().getDrawable(
                        R.drawable.picture_serchprogressbar_empty));
            }
        } else if (val > 10) {
            for (int i = 0; i <= 9; i++) {
                ImageView searchImage = (ImageView) (viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_signalquality_val
                        .getChildAt(i));
                searchImage.setImageDrawable(getResources().getDrawable(
                        R.drawable.picture_serchprogressbar_solid));
            }
        } else if (val == 0) {
            for (int i = 0; i <= 9; i++) {
                ImageView searchImage = (ImageView) (viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_signalquality_val
                        .getChildAt(i));
                searchImage.setImageDrawable(getResources().getDrawable(
                        R.drawable.picture_serchprogressbar_empty));
            }
        }
    }

    private void InitialProgressValueForSignalQuality() {
        for (int i = 0; i <= 9; i++) {
            ImageView searchImage = (ImageView) (viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_signalquality_val
                    .getChildAt(i));
            searchImage.setImageDrawable(getResources().getDrawable(
                    R.drawable.picture_serchprogressbar_empty));
        }
    }

    private void setProgressValueForSignalStrengh(int val) {

        if (val <= 10 && val > 0) {
            for (int i = 0; i <= val - 1; i++) {
                ImageView searchImage = (ImageView) (viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_signalstrength_val
                        .getChildAt(i));
                searchImage.setImageDrawable(getResources().getDrawable(
                        R.drawable.picture_serchprogressbar_solid));
            }
            for (int i = val; i <= 9; i++) {
                ImageView searchImage = (ImageView) (viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_signalstrength_val
                        .getChildAt(i));
                searchImage.setImageDrawable(getResources().getDrawable(
                        R.drawable.picture_serchprogressbar_empty));
            }
        } else if (val > 10) {
            for (int i = 0; i <= 9; i++) {
                ImageView searchImage = (ImageView) (viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_signalstrength_val
                        .getChildAt(i));
                searchImage.setImageDrawable(getResources().getDrawable(
                        R.drawable.picture_serchprogressbar_solid));
            }
        } else if (val == 0) {
            for (int i = 0; i <= 9; i++) {
                ImageView searchImage = (ImageView) (viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_signalstrength_val
                        .getChildAt(i));
                searchImage.setImageDrawable(getResources().getDrawable(
                        R.drawable.picture_serchprogressbar_empty));
            }
        }
    }

    private void InitialProgressValueForSignalStrengh() {
        for (int i = 0; i <= 9; i++) {
            ImageView searchImage = (ImageView) (viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_signalstrength_val
                    .getChildAt(i));
            searchImage.setImageDrawable(getResources().getDrawable(
                    R.drawable.picture_serchprogressbar_empty));
        }
    }

    private void inputFrequencyNumber(int inputno) {
        int freq = 0;
        strfreq = strfreq + Integer.toString(inputno);
        freq = Integer.parseInt(strfreq);
        if (freq > mMaxFrequencyNumber) {
            strfreq = Integer.toString(inputno);
            mFrequency = inputno;
        } else if (strfreq.length() >= mFrequencyDigitsBound) {
            strfreq = Integer.toString(inputno);
            mFrequency = inputno;
        } else {
            mFrequency = freq;
        }
        viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_frequency_val.setText(strfreq);
        return;
    }

    private void inputSymbolNumber(int inputno) {
        short symbol = 0;
        strsymbol = strsymbol + Integer.toString(inputno);
        symbol = (short) Integer.parseInt(strsymbol);
        if (strsymbol.length() >= mSymbolRateDigitsBound) {
            strsymbol = Integer.toString(inputno);
            mSymbolRate = (short) inputno;
        } else {
            mSymbolRate = symbol;
        }
        viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_symbol_val.setText(strsymbol);
        return;
    }

    @Override
    protected void onResume() {
        super.onResume();

        LittleDownTimer.setHandler(mHandler);
        LittleDownTimer.resetMenu();
        LittleDownTimer.resumeMenu();

        mDtvScanEventListener = new DtvScanEventListener();
        TvChannelManager.getInstance().registerOnDtvScanEventListener(mDtvScanEventListener);
    }

    @Override
    protected void onPause() {
        LittleDownTimer.pauseMenu();
        LittleDownTimer.setHandler(null);
        mTvChannelManager.stopDtvScan();
        // When the DTV searching is interrupted
        if (bManualScanByUser) {
            if (!mOnAutoSwitch) {
                if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                    if (mScanEnd == false) {
                        mTvChannelManager.changeToFirstService(
                                TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                                TvChannelManager.FIRST_SERVICE_DEFAULT);
                    }
                } else {
                    mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                            TvChannelManager.FIRST_SERVICE_DEFAULT);
                }
            }
        } else {
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                mTvChannelManager.changeToFirstService(
                        TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                        TvChannelManager.FIRST_SERVICE_DEFAULT);
            }
        }
        if (null != mDtvScanEventListener) {
            TvChannelManager.getInstance().unregisterOnDtvScanEventListener(mDtvScanEventListener);
            mDtvScanEventListener = null;
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mRunthread = false;
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private void updateDtvCount(DtvEventScan extra) {
        String str;

        str = "" + extra.dtvSrvCount;
        viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_tuningresult_dtv_val.setText(str);

        str = "" + extra.radioSrvCount;
        viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_tuningresult_radio_val.setText(str);

        str = "" + extra.dataSrvCount;
        viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_tuningresult_data_val.setText(str);

        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            if ((mDtvCount != extra.dtvSrvCount)
                ||(mRadioCount != extra.radioSrvCount)
                ||(mDataCount != extra.dataSrvCount)) {
                Utility.ttsSepakLinearLayout(viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_tuningresult);
            }
        }
        mDtvCount = extra.dtvSrvCount;
        mRadioCount = extra.radioSrvCount;
        mDataCount = extra.dataSrvCount;
    }

    private void updateDtvTuningScanInfo(DtvEventScan extra) {
        LittleDownTimer.resetMenu();

        if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
            if ((extra.scanStatus == TvChannelManager.DTV_SCAN_STATUS_END)
                || (extra.scanStatus == TvChannelManager.DTV_SCAN_STATUS_GET_PROGRAMS)) {
                    updateDtvCount(extra);
                }
        } else if (isSearchByUser) {
            updateDtvCount(extra);
        }

        setProgressValueForSignalQuality((extra.signalQuality / 10));
        setProgressValueForSignalStrengh((extra.signalStrength / 10));

        if (extra.scanStatus == TvChannelManager.DTV_SCAN_STATUS_END) {
            LittleDownTimer.resumeMenu();
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                if ((extra.dtvSrvCount + extra.radioSrvCount + extra.dataSrvCount) > 0) {
                    TvAtscChannelManager.getInstance().changeDtvToManualFirstService(mChannelNumber);
                    mScanEnd = true;
                } else {
                    mTvChannelManager.setDtvManualScanByRF(mChannelNumber);
                    Toast.makeText(DTVManualTuning.this, R.string.str_no_sinal, Toast.LENGTH_SHORT)
                            .show();
                }
                bManualScanByUser = false;
                isSearchByUser = false;
            } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                if ((extra.dtvSrvCount + extra.radioSrvCount + extra.dataSrvCount) > 0) {
                    mTvChannelManager.changeToFirstService(
                            TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                            TvChannelManager.FIRST_SERVICE_DEFAULT);
                } else {
                    mTvChannelManager.setDtvManualScanByRF(mChannelNumber);
                }
                bManualScanByUser = false;
                isSearchByUser = false;
            } else {
                if ((extra.dtvSrvCount + extra.radioSrvCount + extra.dataSrvCount) > 0) {
                    mTvChannelManager.changeToFirstService(
                            TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                            TvChannelManager.FIRST_SERVICE_DEFAULT);
                    bManualScanByUser = false;
                }
                isSearchByUser = false;
            }
        }
    }

    private void initScanResult() {
        viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_tuningresult_dtv_val.setText(" 0");
        viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_tuningresult_radio_val.setText(" 0");
        viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_tuningresult_data_val.setText(" 0");
        InitialProgressValueForSignalQuality();
        InitialProgressValueForSignalStrengh();
    }

    /**
     * onChannelManualTune
     *
     * @param rfDirection, a.k.a. channel up/down
     * @see com.mstar.android.tv.TvChannelManager#PREVIOUS_RF
     * @see com.mstar.android.tv.TvChannelManager#NEXT_RF
     */
    private void onChannelManualTune(int rfDirection) {
        String channelName = null;
        if ((mTvChannelManager.getCurrentDtvRouteIndex() == mDvbtRouteIndex)
                || (mTvChannelManager.getCurrentDtvRouteIndex() == mDtmbRouteIndex)
                || (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC)
                || (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB)) {
            RfInfo rfInfo = null;
            rfInfo = mTvChannelManager.getRfInfo(rfDirection, mChannelNumber);
            mChannelNumber = rfInfo.rfPhyNum;
            channelName = rfInfo.rfName;
            if (true == Utility.isISDB()) {
                initScanResult();
                mTvChannelManager.stopDtvScan();
                mTvChannelManager.changeDtvManualScanRF(mChannelNumber);
                LittleDownTimer.resetMenu();
                LittleDownTimer.resumeMenu();
            } else {
                // Only DTMB and DVBT shows RF Name instead rfPhyNum (e.g 24-1 / K30)
                if ((mTvChannelManager.getCurrentDtvRouteIndex() != mDvbtRouteIndex) &&
                        (mTvChannelManager.getCurrentDtvRouteIndex() != mDtmbRouteIndex)) {
                    channelName = Integer.toString(mChannelNumber);
                }
                bManualScanByUser = true;
                mTvChannelManager.setDtvManualScanByRF(mChannelNumber);
                mTvChannelManager.changeDtvManualScanRF(mChannelNumber);
            }
        }
        viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_channelnum_val
                .setText(channelName);
        Utility.ttsSepakLinearLayout(viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_channelnum);
    }
}
