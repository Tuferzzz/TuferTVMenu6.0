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

package tufer.com.menutest.UIActivity.channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ListView;
import android.graphics.Rect;

import com.mstar.android.MKeyEvent;
import com.mstar.android.tvapi.common.ChannelManager;
import com.mstar.android.tvapi.common.vo.EnumServiceType;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.dtv.vo.DtvEventScan;
import com.mstar.android.tvapi.dtv.listener.OnDtvPlayerEventListener;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvIsdbChannelManager;

import com.mstar.android.tv.TvPvrManager;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.app.Activity;
import android.widget.Toast;

import tufer.com.menutest.R;
import tufer.com.menutest.UIActivity.MstarBaseActivity;
import tufer.com.menutest.UIActivity.TimeOutHelper;
import tufer.com.menutest.UIActivity.pvr.PVRActivity;
import tufer.com.menutest.Util.TvIntent;
import tufer.com.menutest.Util.Utility;


public class AdaChannelListActivity extends MstarBaseActivity {

    private static final String TAG = "AdaChannelListActivity";

    private int mTvSystem = 0;

    private ListView proListView;
    
    private TextView proListTitle;

    //Ada pre 定制功能 start
    private Button exitTv;
    //Ada pre 定制功能 end
            
    private LinearLayout proListLayout;         

    private ArrayList<ProgramListViewItemObject> plvios = new ArrayList<ProgramListViewItemObject>();

    private ProgramListViewItemObject plvioTmp = new ProgramListViewItemObject();

    private ArrayList<ProgramInfo> progInfoList = new ArrayList<ProgramInfo>();

    private ProgramEditAdapter mProgramEditAdapter = null;

    private EditText input = null;

    private boolean moveFlag = false;

    private boolean moveble = false;

    private int moveKeyCount = 0;

    private int position;

    private int pageSize = 10;

    private int currutPage = 1;

    private int m_u32Source = 0;

    private int m_u32Target = 0;

    private int m_nServiceNum = 0;

    private TimeOutHelper timeOutHelper;

    private TvChannelManager mTvChannelManager = null;

    private TvAtscChannelManager mTvAtscChannelManager = null;

    private OnDtvPlayerEventListener mDtvEventListener = null;

    private Map<String, String> mMap ;

    private class DtvEventListener implements OnDtvPlayerEventListener {

        @Override
        public boolean onDtvChannelNameReady(int what) {
            return false;
        }

        @Override
        public boolean onDtvAutoTuningScanInfo(int what, DtvEventScan extra) {
            return false;
        }

        @Override
        public boolean onDtvProgramInfoReady(int what) {
            return false;
        }

        @Override
        public boolean onCiLoadCredentialFail(int what) {
            return false;
        }

        @Override
        public boolean onEpgTimerSimulcast(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onHbbtvStatusMode(int what, boolean arg1) {
            return false;
        }

        @Override
        public boolean onMheg5StatusMode(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onMheg5ReturnKey(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onOadHandler(int what, int arg1, int arg2) {
            return false;
        }

        @Override
        public boolean onOadDownload(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onDtvAutoUpdateScan(int what) {
            return false;
        }

        @Override
        public boolean onTsChange(int what) {
            boolean ret = false;
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                Log.i(TAG, "onTsChange what:" + what);
                AdaChannelListActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        RefreshContent();
                    }
                });
                ret = true;
            }
            return ret;
        }

        @Override
        public boolean onPopupScanDialogLossSignal(int what) {
            return false;
        }

        @Override
        public boolean onPopupScanDialogNewMultiplex(int what) {
            return false;
        }

        @Override
        public boolean onPopupScanDialogFrequencyChange(int what) {
            return false;
        }

        @Override
        public boolean onRctPresence(int what) {
            return false;
        }

        @Override
        public boolean onChangeTtxStatus(int what, boolean arg1) {
            return false;
        }

        @Override
        public boolean onDtvPriComponentMissing(int what) {
            return false;
        }

        @Override
        public boolean onAudioModeChange(int what, boolean arg1) {
            return false;
        }

        @Override
        public boolean onMheg5EventHandler(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onOadTimeout(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onGingaStatusMode(int what, boolean arg1) {
            return false;
        }

        @Override
        public boolean onSignalLock(int what) {
            return false;
        }

        @Override
        public boolean onSignalUnLock(int what) {
            return false;
        }

        @Override
        public boolean onUiOPRefreshQuery(int what) {
            return false;
        }

        @Override
        public boolean onUiOPServiceList(int what) {
            return false;
        }

        @Override
        public boolean onUiOPExitServiceList(int what) {
            return false;
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == TimeOutHelper.getTimeOutMsg()) {
                finish();
            }
        }
    };
	
	 private boolean isNeedToCheckExitRecord(ProgramInfo pi) {
        if (pi == null) {
            return false;
        }
        /* Always time shift recording will auto stop by tvsystem. */
        final TvPvrManager pvr = TvPvrManager.getInstance();
        if (PVRActivity.FREQUENCY_NOT_RECORDING != pi.frequency
                && pvr.isAlwaysTimeShiftRecording() == false
                && pvr.isRecording()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param keyCode
     * @param selItem
     * @return
     */
    boolean checkChmoveble(int keyCode, int selItem) {
        ProgramInfo cur = null;
        ProgramInfo next = null;
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (selItem >= (progInfoList.size() - 1)) {
                return false;
            }
            cur = progInfoList.get(selItem);
            next = progInfoList.get(selItem + 1);
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (selItem == 0) {
                return false;
            }
            cur = progInfoList.get(selItem);
            next = progInfoList.get(selItem - 1);
        }
        if (cur.serviceType == next.serviceType) {
            return true;
        } else {
            return false;
        }
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().gravity = Gravity.LEFT;
        mTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();
        setContentView(R.layout.ada_program_list_view);
        proListView = (ListView) findViewById(R.id.program_edit_list_view);
        proListTitle = (TextView) findViewById(R.id.program_edit_title);
        proListTitle.setText(R.string.str_channelList_program);
        //Ada pre 定制功能 start
        exitTv= (Button) findViewById(R.id.program_edit_exit);
        exitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(TvIntent.ACTION_EXIT_TV_APK);
                sendBroadcast(intent);
                finish();
            }
        });
        //Ada pre 定制功能 end
        proListLayout = (LinearLayout) findViewById(R.id.ada_program_list);
        mMap=Utility.getAtvMap(this);
        mTvChannelManager = TvChannelManager.getInstance();
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            mTvAtscChannelManager = TvAtscChannelManager.getInstance();
        }
        getProgList();
        mProgramEditAdapter = new ProgramEditAdapter(this, plvios);
        proListView.setAdapter(mProgramEditAdapter);
        proListView.setDividerHeight(0);
        proListView.setSelection(getfocusIndex());
        
        proListView.setOnItemClickListener(new OnItemClickListener() {

			  @Override
			  public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				  // TODO Auto-generated method stub
				  final ProgramInfo ProgInf = progInfoList.get(position);
                Log.d(TAG, "number onItemClick timeOutHelper.reset" + ProgInf.number);
                
                timeOutHelper.reset();
              	//RootActivity.runCount=1; // always delay
              	                
                if(true == isSameWithCurrentProgram(ProgInf)) {
                    Log.d(TAG, "CH List :Select the same channel!!!");
                } else {
                    if (ProgInf.serviceType < TvChannelManager.SERVICE_TYPE_INVALID) {
                        if (isNeedToCheckExitRecord(ProgInf)) {
                            // Toast.makeText
                            AlertDialog.Builder build = new AlertDialog.Builder(
                                    AdaChannelListActivity.this);
                            build.setMessage(R.string.str_pvr_tip2);
                            build.setPositiveButton(R.string.str_stop_record_dialog_stop,
                                    new OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                int which) {
                                            TvPvrManager.getInstance().stopRecord();
                                            //PVRActivity.FREQUENCY_NOT_RECORDING = -1;
                                            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                                                mTvAtscChannelManager.programSel(ProgInf.majorNum, ProgInf.minorNum);
                                            } else {
                                                mTvChannelManager.selectProgram(ProgInf.number, ProgInf.serviceType);
                                            }
                                        }
                                    });
                            build.setNegativeButton(R.string.str_stop_record_dialog_cancel,
                                    new OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                int which) {
                                        }
                                    });
                            build.create().show();
                        } else {
                            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                                mTvAtscChannelManager.programSel(ProgInf.majorNum, ProgInf.minorNum);
                            } else {
                                mTvChannelManager.selectProgram(ProgInf.number, ProgInf.serviceType);
                            }
                        }
                    }
                }
                timeOutHelper.reset();
			    }
        });
    
        proListView.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                timeOutHelper.reset();
                int selItemIndex = (int) proListView.getSelectedItemId();
                /** end modified by jachensy.chen 2012-6-27 */
                if (((keyCode == KeyEvent.KEYCODE_DPAD_UP) && moveFlag && (keyEvent.getAction() == KeyEvent.ACTION_DOWN))
                        || (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && moveFlag && (keyEvent
                                .getAction() == KeyEvent.ACTION_DOWN))) {
                    if (checkChmoveble(keyCode, selItemIndex)) {
                        moveble = true;
                    } else {
                        moveble = false;
                        return true;
                    }
                }
                if (((keyCode == KeyEvent.KEYCODE_DPAD_UP) && moveFlag && (keyEvent.getAction() == KeyEvent.ACTION_UP))
                        || (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && moveble && (keyEvent
                                .getAction() == KeyEvent.ACTION_UP))) {
                    if (moveFlag) {
                        if ((position >= plvios.size()) || (selItemIndex >= plvios.size())) {
                            return false;
                        }
                        swapObject(plvios.get(position), plvios.get(selItemIndex));
                        swapObject(plvios.get(selItemIndex), plvioTmp);
                        position = selItemIndex;
                        mProgramEditAdapter.notifyDataSetChanged();
                        proListView.invalidate();
                        return true;
                    } else {
                        return true;
                    }
                }
                if (moveFlag) {
                    return false;
                }
                if (keyCode == MKeyEvent.KEYCODE_MSTAR_SUBCODE
                        && (keyEvent.getAction() == KeyEvent.ACTION_UP)) {
                    if (selItemIndex >= progInfoList.size()) {
                        return false;
                    }
                    ProgramInfo ProgInf = progInfoList.get(selItemIndex);
                    short bfav = ProgInf.favorite;

                    if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                        if (bfav == 0) {
                            bfav = 1;
                            mTvChannelManager.addProgramToFavorite(TvChannelManager.PROGRAM_FAVORITE_ID_1,
                                ProgInf.majorNum, ProgInf.minorNum, ProgInf.progId);
                        } else {
                            bfav = 0;
                            mTvChannelManager.deleteProgramFromFavorite(TvChannelManager.PROGRAM_FAVORITE_ID_1,
                                ProgInf.majorNum, ProgInf.minorNum, ProgInf.progId);
                        }
                    } else {
                        if (bfav == 0) {
                            bfav = 1;
                            mTvChannelManager.addProgramToFavorite(TvChannelManager.PROGRAM_FAVORITE_ID_1,
                                    ProgInf.number, ProgInf.serviceType, 0x00);
                        } else {
                            bfav = 0;
                            mTvChannelManager.deleteProgramFromFavorite(TvChannelManager.PROGRAM_FAVORITE_ID_1,
                                    ProgInf.number, ProgInf.serviceType, 0x00);
                        }
                    }
                    ProgInf.favorite = bfav;
                    if (selItemIndex >= plvios.size()) {
                        return false;
                    }
                    if (bfav != 0) {
                        (plvios.get(selItemIndex)).setFavoriteImg(true);
                    } else {
                        (plvios.get(selItemIndex)).setFavoriteImg(false);
                    }
                    position = (int) proListView.getSelectedItemId();
                    // swapObject(plvioTmp, plvios.get(position));
                    mProgramEditAdapter.notifyDataSetChanged();
                    proListView.invalidate();
                    return true;
                } else if ((keyCode == KeyEvent.KEYCODE_L && (keyEvent.getAction() == KeyEvent.ACTION_UP))
                        || (keyCode == MKeyEvent.KEYCODE_MSTAR_HOLD && (keyEvent.getAction() == KeyEvent.ACTION_UP))) {
                    if (selItemIndex >= progInfoList.size()) {
                        return false;
                    }
                    if (mTvSystem != TvCommonManager.TV_SYSTEM_ATSC) {
                        return false;
                    }
                    ProgramInfo ProgInf = progInfoList.get(selItemIndex);
                    boolean block = ProgInf.isLock;
                    block = !block;
                    ProgInf.isLock = block;
                    if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                        mTvChannelManager.setProgramAttribute(TvChannelManager.PROGRAM_ATTRIBUTE_LOCK, ProgInf.majorNum,
                            ProgInf.minorNum, ProgInf.progId, block);
                    } else {
                        mTvChannelManager.setProgramAttribute(TvChannelManager.PROGRAM_ATTRIBUTE_LOCK, ProgInf.number,
                            ProgInf.serviceType, 0x00, block);
                    }
                    if (block) {
                        plvios.get(selItemIndex).setLockImg(true);
                    } else {
                        plvios.get(selItemIndex).setLockImg(false);
                    }
                    // swapObject(plvioTmp, plvios.get(position));
                    mProgramEditAdapter.notifyDataSetChanged();
                    proListView.invalidate();
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_PROG_BLUE
                        && (keyEvent.getAction() == KeyEvent.ACTION_UP)) {
                    if (selItemIndex >= progInfoList.size()) {
                        return false;
                    }
                    ProgramInfo ProgInf = progInfoList.get(selItemIndex);
                    boolean bSkip = ProgInf.isSkip;
                    bSkip = !bSkip;
                    ProgInf.isSkip = bSkip;
                    if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                        mTvChannelManager.setProgramAttribute(TvChannelManager.PROGRAM_ATTRIBUTE_SKIP, ProgInf.majorNum,
                            ProgInf.minorNum, ProgInf.progId, bSkip);
                    } else {
                        mTvChannelManager.setProgramAttribute(TvChannelManager.PROGRAM_ATTRIBUTE_SKIP, ProgInf.number,
                            ProgInf.serviceType, 0x00, bSkip);
                    }
                    if (selItemIndex >= plvios.size()) {
                        return false;
                    }
                    if (bSkip) {
                        plvios.get(selItemIndex).setSkipImg(true);
                    } else {
                        plvios.get(selItemIndex).setSkipImg(false);
                    }
                    // swapObject(plvioTmp, plvios.get(position));
                    mProgramEditAdapter.notifyDataSetChanged();
                    proListView.invalidate();
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_ENTER
                        && (keyEvent.getAction() == KeyEvent.ACTION_UP)) {
                    if (selItemIndex >= progInfoList.size()) {
                        return false;
                    }
                    ProgramInfo ProgInf = progInfoList.get(selItemIndex);
                    ProgramInfo curProgInfo = mTvChannelManager.getCurrentProgramInfo();
                    if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                        if ((curProgInfo.majorNum == ProgInf.majorNum)
                            && (curProgInfo.minorNum == ProgInf.minorNum)
                            && (curProgInfo.serviceType == ProgInf.serviceType)) {
                            Log.d(TAG, "ProList:Select the same channel!!!");
                        } else {
                            mTvAtscChannelManager.programSel(ProgInf.majorNum, ProgInf.minorNum);
                        }
                    } else {
                        if ((curProgInfo.number == ProgInf.number)
                            && (curProgInfo.serviceType == ProgInf.serviceType)) {
                            Log.d(TAG, "ProList:Select the same channel!!!");
                        } else {
                            mTvChannelManager.selectProgram(ProgInf.number, ProgInf.serviceType);
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
        
        proListView.setOnScrollListener(new OnScrollListener() {
						
		    @Override  
		    public void onScrollStateChanged(AbsListView view, int scrollState) {  
		        switch(scrollState){  
		          case OnScrollListener.SCROLL_STATE_IDLE:    
					         break;
		          case OnScrollListener.SCROLL_STATE_FLING:
		               break;
		          case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
		        	     timeOutHelper.reset();
              		//RootActivity.runCount=1; // always delay
		            break;  
		        default:
		        	break;
		        }  
		    }  
		    @Override  
		    public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {

		    }  
		    });
		    
        timeOutHelper = new TimeOutHelper(handler, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        timeOutHelper.start();
        timeOutHelper.init();
        mDtvEventListener = new DtvEventListener();
        mTvChannelManager.registerOnDtvPlayerEventListener(mDtvEventListener);
    };

    @Override
    protected void onPause() {
        mTvChannelManager.unregisterOnDtvPlayerEventListener(mDtvEventListener);
        mDtvEventListener = null;
        timeOutHelper.stop();
        super.onPause();
    }
    
    public boolean onTouchEvent(MotionEvent ev) {
    	 if(ev.getAction() == MotionEvent.ACTION_DOWN){
		     int x = (int) ev.getX();
				 int y = (int) ev.getY();
				 Rect rect = new Rect();
				 proListLayout.getGlobalVisibleRect(rect);
				 if(!rect.contains(x, y)) {
					 finish();					
				 }
			 }
		     return true;
	  }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        timeOutHelper.reset();
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (currutPage >= 2) {
                moveKeyCount = 0;
                moveFlag = false;
                currutPage--;
                proListView.setSelection((currutPage - 1) * pageSize);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (currutPage < ((plvios.size() - 1) / pageSize) + 1) {
                moveKeyCount = 0;
                moveFlag = false;
                currutPage++;
                proListView.setSelection((currutPage - 1) * pageSize);
                return true;
            }
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (proListView.getSelectedItemPosition() == 0) {
                proListView.setSelection(plvios.size() - 1);
                return true;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (proListView.getSelectedItemPosition() == plvios.size() - 1) {
                proListView.setSelection(0);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public String splitString(String str, int len) {
        if (str == null) {
            return "";
        }
        int k = 0;
        String new_str = "";
        for (int i = 0; i < str.length(); i++) {
            byte[] b = (str.charAt(i) + "").getBytes();
            k = k + b.length;
            if (k > len) {
                break;
            }
            new_str = new_str + str.charAt(i);
        }
        return new_str;
    }

    private void swapObject(ProgramListViewItemObject obj1, ProgramListViewItemObject obj2) {
        obj1.setTvName(obj2.getTvName());
        obj1.setTvNumber(obj2.getTvNumber());
        obj1.setFavoriteImg(obj2.isFavoriteImg());
        obj1.setSkipImg(obj2.isSkipImg());
        obj1.setSslImg(obj2.isSslImg());
        obj1.setServiceType(obj2.getServiceType());
    }

    private void RefreshContent() {
        plvios.clear();
        progInfoList.clear();
        getProgList();
        mProgramEditAdapter.notifyDataSetChanged();
        proListView.invalidate();
    }

    private void addOneListViewItem(ProgramInfo pgi) {
        boolean flag = false;
        if (pgi != null) {
            ProgramListViewItemObject plvio = new ProgramListViewItemObject();
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                String channum = mTvAtscChannelManager.getDispChannelNum(pgi);
                String name = mTvAtscChannelManager.getDispChannelName(pgi);
                plvio.setTvName(name);
                //Toast.makeText(this,"plvio.setTvName(name)"+name,Toast.LENGTH_SHORT).show();
            } else {
                plvio.setTvName(pgi.serviceName);
                //Toast.makeText(this,"plvio.setTvName(pgi.serviceName)"+pgi.serviceName,Toast.LENGTH_SHORT).show();
            }
            if (pgi.serviceType == TvChannelManager.SERVICE_TYPE_ATV) {
                Log.d(TAG,"pgi.serviceName:"+pgi.serviceName+" pgi.frequency:"+pgi.frequency);
                //Toast.makeText(this,"pgi.frequency:"+pgi.frequency+" map:"+mMap.get("55055"),Toast.LENGTH_SHORT).show();
                if(mMap.containsKey(String.valueOf(pgi.frequency))){
                    plvio.setTvName(mMap.get(String.valueOf(pgi.frequency)));
                    mTvChannelManager.setProgramName(pgi.number, pgi.serviceType, mMap.get(String.valueOf(pgi.frequency)));
                    //Toast.makeText(this,"ChannelActivity.mMap.get(String.valueOf(pgi.frequency))"+mMap.get(String.valueOf(pgi.frequency)),Toast.LENGTH_SHORT).show();
                }
                plvio.setTvNumber(String.valueOf(Utility.getATVDisplayChNum(pgi.number)));
            } else {
                if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                    String channum = mTvAtscChannelManager.getDispChannelNum(pgi);
                    plvio.setTvNumber(channum);
                } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                    String channum = pgi.majorNum + "." + pgi.minorNum;
                    plvio.setTvNumber(channum);
                } else {
                    plvio.setTvNumber(String.valueOf(pgi.number));
                }
            }
            flag = false;
            if (pgi.favorite != 0) {
                flag = true;
            }
            plvio.setFavoriteImg(flag);
            flag = pgi.isSkip;
            plvio.setSkipImg(flag);
            flag = pgi.isLock;
            plvio.setLockImg(flag);
            flag = pgi.isScramble;
            plvio.setSslImg(flag);
            plvio.setServiceType(pgi.serviceType);
            plvios.add(plvio);
        }
    }

    private int getfocusIndex() {
        int focusIndex = 0;
        ProgramInfo cpi = mTvChannelManager.getCurrentProgramInfo();
        for (ProgramInfo pi: progInfoList) {
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                if ((cpi.majorNum == pi.majorNum)
                    && (cpi.minorNum == pi.minorNum)
                    && (cpi.serviceType == pi.serviceType)) {
                    focusIndex = progInfoList.indexOf(pi);
                    break;
                }
            } else {
                if (cpi.number == pi.number) {
                    focusIndex = progInfoList.indexOf(pi);
                    break;
                }
            }
        }
        return focusIndex;
    }
    
    private boolean isSameWithCurrentProgram(ProgramInfo ProgInf) {
        boolean ret = false;
        ProgramInfo curProgInfo = mTvChannelManager.getCurrentProgramInfo();

        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            if ((curProgInfo.majorNum == ProgInf.majorNum)
                && (curProgInfo.minorNum == ProgInf.minorNum)
                && (curProgInfo.serviceType == ProgInf.serviceType)) {
                ret = true;
            }
        } else {
            if ((curProgInfo.number == ProgInf.number)
                && (curProgInfo.serviceType == ProgInf.serviceType)) {
                ret = true;
            }
        }
        return ret;
    }

    private void getProgList() {
        ProgramInfo pgi = null;
        m_nServiceNum = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV_DTV);
        for (int k = 0; k < m_nServiceNum; k++) {
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                pgi = mTvAtscChannelManager.getProgramInfo(k);
            } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                pgi = TvIsdbChannelManager.getInstance().getProgramInfo(k);
            } else {
                pgi = mTvChannelManager.getProgramInfoByIndex(k);
            }
            if (pgi != null) {
                if ((pgi.isDelete == true) || (pgi.isVisible == false)) {
                    continue;
                } else {
                    progInfoList.add(pgi);
                    addOneListViewItem(pgi);
                }
            }
        }
    }
}
