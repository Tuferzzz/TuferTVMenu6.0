//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2015 MStar Semiconductor, Inc. All rights reserved.
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

package tufer.com.menutest.UIActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;

import com.mstar.android.MKeyEvent;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvPvrManager;

import tufer.com.menutest.R;
import tufer.com.menutest.UIActivity.channel.ChannelListActivity;
import tufer.com.menutest.UIActivity.dtv.AudioLanguageActivity;
import tufer.com.menutest.UIActivity.dtv.epg.atsc.AtscEPGActivity;
import tufer.com.menutest.UIActivity.dtv.epg.EPGActivity;
import tufer.com.menutest.UIActivity.dtv.MTSInfoActivity;
import tufer.com.menutest.UIActivity.dtv.SubtitleLanguageActivity;
import tufer.com.menutest.UIActivity.dtv.TeletextActivity;
import tufer.com.menutest.Util.TVRootApp;
import tufer.com.menutest.Util.TvIntent;
import tufer.com.menutest.Util.Constant;
import tufer.com.menutest.Util.Utility;

public class SwitchPageHelper {
    private static final String TAG = "SwitchPageHelper";

    static public String sLastRecordedFileName = null; // This is used for PVR
                                                       // isOneTouchPlay mode.

    public static boolean goToMenuPage(Activity from, int keyCodeToTrans) {
        Log.i(TAG, "--------------> goToMenuPage begin " + System.currentTimeMillis());
        switch (keyCodeToTrans) {
            case KeyEvent.KEYCODE_M:
            case KeyEvent.KEYCODE_MENU:
                Intent intent;
                SharedPreferences settings = from.getSharedPreferences("TvSetting", 0);
                boolean flag = settings.getBoolean("_3Dflag", false);
                if (flag) {
                    // intent = new Intent(from, MainMenu3DActivity.class);
                    // RootActivity.my3DHandler.sendEmptyMessage(RootActivity._3DAction.show);
                    // from.startActivity(intent);
                } else {
                    intent = new Intent(TvIntent.MAINMENU);
                    if (intent.resolveActivity(from.getPackageManager()) != null) {
                        from.startActivity(intent);
                    }
                }
                return true;
            case KeyEvent.KEYCODE_TV_INPUT:
                intent = new Intent(
                        "com.mstar.tvsetting.switchinputsource.intent.action.PictrueChangeActivity");
                from.startActivity(intent);
                return true;
        }
        return false;
    }

    public static boolean goToEpgPage(Activity from, int keyCodeToTrans) {
        if (TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_EPG)) {
            switch (keyCodeToTrans) {
                case KeyEvent.KEYCODE_GUIDE:
                case MKeyEvent.KEYCODE_EPG:
                    if (specificSourceIsInUse(from, TvCommonManager.INPUT_SOURCE_DTV)) {
                        Intent intent;
                        if (TvCommonManager.getInstance().getCurrentTvSystem() == TvCommonManager.TV_SYSTEM_ATSC) {
                            intent = new Intent(from, AtscEPGActivity.class);
                        } else {
                            intent = new Intent(from, EPGActivity.class);
                        }
                        from.startActivity(intent);
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    public static boolean goToSourceInfo(Activity from, int keyCodeToTrans) {
        switch (keyCodeToTrans) {
            case KeyEvent.KEYCODE_I:
            case KeyEvent.KEYCODE_INFO:
                Intent intent = new Intent(TvIntent.ACTION_SOURCEINFO);
                intent.putExtra("info_key", true);
                from.startActivity(intent);
                return true;
        }
        return false;
    }

    public static boolean goToPvrPage(Activity from, int keyCodeToTrans) {
        TVRootApp app = (TVRootApp) from.getApplication();
        if (!app.isPVREnable()) {
            return false;
        }

        if (specificSourceIsInUse(from, TvCommonManager.INPUT_SOURCE_DTV)) {
            final boolean isAlwaysTimeShiftRecording = TvPvrManager.getInstance().isAlwaysTimeShiftRecording();
            if (isAlwaysTimeShiftRecording &&
                ((keyCodeToTrans == KeyEvent.KEYCODE_DVR) || (keyCodeToTrans == KeyEvent.KEYCODE_MEDIA_PLAY))) {
                return false;
            }
            Intent intent = new Intent(TvIntent.ACTION_PVR_ACTIVITY);
            if (keyCodeToTrans == KeyEvent.KEYCODE_DVR) {
                intent.putExtra(Constant.PVR_CREATE_MODE, Constant.PVR_RECORD_START);
            } else if (keyCodeToTrans == KeyEvent.KEYCODE_MEDIA_PLAY) {
                if (null == sLastRecordedFileName) {
                    return false;
                }
                intent.putExtra(Constant.PVR_CREATE_MODE, Constant.PVR_PLAYBACK_START);
                intent.putExtra(Constant.PVR_FILENAME, sLastRecordedFileName);
            } else if (keyCodeToTrans == KeyEvent.KEYCODE_MEDIA_PAUSE) {
                intent.putExtra(Constant.PVR_CREATE_MODE, Constant.PVR_PLAYBACK_PAUSE);
            } else if (keyCodeToTrans == KeyEvent.KEYCODE_MEDIA_PREVIOUS) {
                intent.putExtra(Constant.PVR_CREATE_MODE, Constant.PVR_PLAYBACK_PREVIOUS);
            } else if (keyCodeToTrans == KeyEvent.KEYCODE_MEDIA_REWIND) {
                intent.putExtra(Constant.PVR_CREATE_MODE, Constant.PVR_PLAYBACK_REWIND);
            } else {
                Log.e(TAG, "==========>>> PVR keyCodeToTrans Not Support !!!!!");
                return false;
            }

            if (intent.resolveActivity(from.getPackageManager()) != null) {
                from.startActivity(intent);
            }
            return true;
        }

        return false;
    }

    public static boolean goToPvrBrowserPage(Activity from, int keyCodeToTrans) {
        TVRootApp app = (TVRootApp) from.getApplication();
        if (false == app.isPVREnable())
            return false;

        if (MKeyEvent.KEYCODE_MSTAR_INDEX != keyCodeToTrans) {
            return false;
        }

        if (false == specificSourceIsInUse(from, TvCommonManager.INPUT_SOURCE_DTV)) {
            return false;
        }

        /* TimeShift not support Index key*/
        if (true == TvPvrManager.getInstance().isTimeShiftRecording()) {
            return false;
        }

        Intent intent = new Intent(TvIntent.ACTION_PVR_BROWSER);
        if (null != intent.resolveActivity(from.getPackageManager())) {
            from.startActivity(intent);
            return true;
        }

        return false;
    }

    public static boolean goToSubtitleLangPage(Activity from, int keyCodeToTrans) {
        switch (keyCodeToTrans) {
            case MKeyEvent.KEYCODE_SUBTITLE:
                if (TvCommonManager.getInstance().getCurrentTvSystem() == TvCommonManager.TV_SYSTEM_ATSC) {
                    return false;
                }
                if (specificSourceIsInUse(from, TvCommonManager.INPUT_SOURCE_DTV)) {
                    Intent intent = new Intent(from, SubtitleLanguageActivity.class);
                    from.startActivity(intent);
                    return true;
                } else {
                    if (TvChannelManager.getInstance().isTeletextSubtitleChannel()) {
                        if (TvChannelManager.getInstance().isTeletextDisplayed()) {
                            TvChannelManager.getInstance()
                                    .sendTeletextCommand(TvChannelManager.TTX_COMMAND_SUBTITLE_NAVIGATION);
                        } else {
                            TvChannelManager.getInstance()
                                    .openTeletext(TvChannelManager.TTX_MODE_SUBTITLE_NAVIGATION);
                        }
                    } else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(from);
                        dialog.setTitle(R.string.str_root_alert_dialog_title)
                                .setMessage(R.string.str_dtv_source_info_no_subtitle)
                                .setPositiveButton(R.string.str_root_alert_dialog_confirm,
                                        new OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int arg1) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                    }
                }
                break;
        }
        return false;
    }

    public static boolean goToTeletextPage(Activity from, int keyCodeToTrans) {
        TVRootApp app = (TVRootApp) from.getApplication();
        if (!app.isTTXEnable()) {
            return false;
        }
        switch (keyCodeToTrans) {
            case MKeyEvent.KEYCODE_MSTAR_CLOCK: {
                if ((TvChannelManager.getInstance().hasTeletextClockSignal())
                        && (specificSourceIsInUse(from, TvCommonManager.INPUT_SOURCE_DTV)
                                || specificSourceIsInUse(from, TvCommonManager.INPUT_SOURCE_ATV)
                                || currentInputSourceIs(from, "CVBS")
                                || currentInputSourceIs(from, "SVIDEO")
                                || currentInputSourceIs(from, "SCART"))) {
                    Intent intent = new Intent(from, TeletextActivity.class);
                    intent.putExtra("TTX_MODE_CLOCK", true);
                    from.startActivity(intent);
                    return true;
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(from);
                    dialog.setTitle(R.string.str_root_alert_dialog_title)
                            .setMessage(R.string.str_dtv_source_info_no_teletext)
                            .setPositiveButton(R.string.str_root_alert_dialog_confirm,
                                    new OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int arg1) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                    Log.i(TAG, "Not Ttx Channel");
                }
            }
                break;
            case MKeyEvent.KEYCODE_TTX:
                boolean bIsTtxChannel = TvChannelManager.getInstance().isTtxChannel();
                // check if hasTeletextSignal
                if (bIsTtxChannel
                        && (specificSourceIsInUse(from, TvCommonManager.INPUT_SOURCE_DTV)
                                || specificSourceIsInUse(from, TvCommonManager.INPUT_SOURCE_ATV)
                                || currentInputSourceIs(from, "CVBS")
                                || currentInputSourceIs(from, "SVIDEO") || currentInputSourceIs(
                                    from, "SCART"))) {
                    Intent intent = new Intent(from, TeletextActivity.class);
                    from.startActivity(intent);
                    return true;
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(from);
                    dialog.setTitle(R.string.str_root_alert_dialog_title)
                            .setMessage(R.string.str_dtv_source_info_no_teletext)
                            .setPositiveButton(R.string.str_root_alert_dialog_confirm,
                                    new OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int arg1) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                    Log.i(TAG, "Not Ttx Channel");
                }
                break;
        }
        return false;
    }

    public static boolean goToAudioLangPage(Activity from, int keyCodeToTrans) {
        switch (keyCodeToTrans) {
            case MKeyEvent.KEYCODE_MTS:
                if ((specificSourceIsInUse(from, TvCommonManager.INPUT_SOURCE_DTV)) && (Utility.isSiganlLocked())) {
                    Intent intent = new Intent(from, AudioLanguageActivity.class);
                    from.startActivity(intent);
                    return true;
                }
                if ((specificSourceIsInUse(from, TvCommonManager.INPUT_SOURCE_ATV)) && (Utility.isSiganlLocked())) {
                    Intent intent = new Intent(from, MTSInfoActivity.class);
                    from.startActivity(intent);
                    return true;
                }
                break;
        }
        return false;
    }

    public static boolean goToProgrameListInfo(Activity from, int keyCodeToTrans) {
        Log.i(TAG, "goToProgrameListInfo start  keycode = " + keyCodeToTrans);
        switch (keyCodeToTrans) {
            // programe list
            case KeyEvent.KEYCODE_ENTER:
            case MKeyEvent.KEYCODE_LIST:
                if (specificSourceIsInUse(from, TvCommonManager.INPUT_SOURCE_DTV)
                        || specificSourceIsInUse(from, TvCommonManager.INPUT_SOURCE_ATV)) {
                    Log.i(TAG, "goToProgrameListInfo : start ChannelListActivity");
                    Intent intent = new Intent(TvIntent.CHANNEL_LIST);
                    intent.putExtra("ListId", Constant.SHOW_PROGRAM_LIST);
                    if (intent.resolveActivity(from.getPackageManager()) != null) {
                        from.startActivity(intent);
                    }
                    return true;
                }
                break;
        }
        return false;
    }

    public static boolean goToFavorateListInfo(Activity from, int keyCodeToTrans) {
        switch (keyCodeToTrans) {
            case MKeyEvent.KEYCODE_MSTAR_SUBCODE:// favorite info
                if (specificSourceIsInUse(from, TvCommonManager.INPUT_SOURCE_DTV)
                        || specificSourceIsInUse(from, TvCommonManager.INPUT_SOURCE_ATV)) {
                    Intent intent = new Intent(TvIntent.CHANNEL_LIST);
                    intent.putExtra("ListId", Constant.SHOW_FAVORITE_LIST);
                    if (intent.resolveActivity(from.getPackageManager()) != null) {
                        from.startActivity(intent);
                    }
                    return true;
                }
                break;
        }
        return false;
    }

    public static boolean specificSourceIsInUse(Activity from, int source) {
        int currInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
        if (currInputSource != source) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean currentInputSourceIs(Activity from, final String source) {
        int currInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
        if (source.equals("CVBS")) {
            if (currInputSource >= TvCommonManager.INPUT_SOURCE_CVBS
                    && currInputSource <= TvCommonManager.INPUT_SOURCE_CVBS_MAX)
                return true;
        } else if (source.equals("SVIDEO")) {
            if (currInputSource >= TvCommonManager.INPUT_SOURCE_SVIDEO
                    && currInputSource <= TvCommonManager.INPUT_SOURCE_SVIDEO_MAX)
                return true;
        } else if (source.equals("SCART")) {
            if (currInputSource >= TvCommonManager.INPUT_SOURCE_SCART
                    && currInputSource <= TvCommonManager.INPUT_SOURCE_SCART_MAX)
                return true;
        }
        return false;
    }
}
