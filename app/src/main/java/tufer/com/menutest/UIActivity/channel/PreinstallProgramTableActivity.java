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

import java.io.File;
import android.util.Log;
import java.util.Timer;
import java.util.TimerTask;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener; 
import android.content.Context;
//import android.content.BroadcastReceiver;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.net.Uri;

import android.app.Activity;
import tufer.com.menutest.Util.Utility;
import tufer.com.menutest.UIActivity.MstarBaseActivity;
import tufer.com.menutest.UIActivity.holder.ViewHolder;
import tufer.com.menutest.UIActivity.SwitchPageHelper;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import tufer.com.menutest.UIActivity.channel.preinstallprogram.PreinstallProGlobal;
import tufer.com.menutest.UIActivity.channel.preinstallprogram.USBDiskConnector;
import tufer.com.menutest.R;


public class PreinstallProgramTableActivity extends MstarBaseActivity {

    private static final String TAG = "PreinstallProTable";  
    
    private String[] showInfo;
    
    private String mUDiskProgarmPath = " ";
    
    private static USBDiskConnector mUSBDiskConnector = null;
    
    private static PreinstallProgramTableActivity preinstallProTableActivity = null;
       
    protected LinearLayout linearlayout_preinstall_programtable_import;

    protected LinearLayout linearlayout_preinstall_programtable_updatefile;

    protected LinearLayout linearlayout_preinstall_programtable_exprotfile;
    
    protected TextView textview_preinstall_programtable_note;
      
    protected TextView textview_preinstall_programtable_back;
        
    private TvChannelManager mTvChannelManager = null;
    
    private int mCurInputSource = TvCommonManager.INPUT_SOURCE_NONE;
    
    

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          
        mUSBDiskConnector = new USBDiskConnector();        
        preinstallProTableActivity = this;
        mUDiskProgarmPath =mUSBDiskConnector.getUDiskRootDirectory(preinstallProTableActivity);
        if((mUDiskProgarmPath!= null)&&(mUDiskProgarmPath.length() != 0)){
        	 mUDiskProgarmPath = mUDiskProgarmPath+ PreinstallProGlobal.PROGRAM_FILE_NAME;
        }
        Log.d(TAG, " U Disk program file path = " + mUDiskProgarmPath);
        mTvChannelManager = TvChannelManager.getInstance();
        mCurInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
        
        showInfo = this.getResources().getStringArray(R.array.str_preinstall_programtable_showinfo); 
        //if(showInfo.length ==0){
        //	showInfo = new String[]{"Load str_preinstall_programtable_showinfo failed"};
        //}        
        setContentView(R.layout.preinstall_program_table);  
        findView();
        setOnClickLisenters();              
    }
    

    @Override
    protected void onResume() {
        super.onResume();
      
    };

    @Override
    protected void onPause() {
        super.onPause();
    }
    
     @Override
    public void onDestroy() {
       Log.i(TAG, "onDestroy()");
       //unregisterReceiver(mUSBDiskControllerReceiver);
       super.onDestroy();
    }
    
    public Handler mHandler = new Handler() {
		   @Override
		   public void handleMessage(Message msg) {
		    // TODO Auto-generated method stub
			   if(msg.what == PreinstallProGlobal.COPY_FILE_TO_USB_SUCCESS){
				   toastShowInfo(PreinstallProGlobal.COPY_FILE_TO_USB_SUCCESS);
			   }else if(msg.what == PreinstallProGlobal.IMPORT_PROGRAM_SUCCESS){
			   	toastShowInfo(PreinstallProGlobal.IMPORT_PROGRAM_SUCCESS);
			   }
			   super.handleMessage(msg);
		   }
	  };
    

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	 if((keyCode ==KeyEvent.KEYCODE_MENU)||(keyCode ==KeyEvent.KEYCODE_BACK)){
    	   if (SwitchPageHelper.goToMenuPage(this, KeyEvent.KEYCODE_MENU) == true) {
            finish();
            return true;
         } 
       }        
        return super.onKeyDown(keyCode, event);
    }  
    
  
    private void findView() {
    	  linearlayout_preinstall_programtable_import =(LinearLayout)
                findViewById(R.id.linearlayout_preinstall_programtable_import);
        linearlayout_preinstall_programtable_updatefile =(LinearLayout)
                findViewById(R.id.linearlayout_preinstall_programtable_updatefile);
        linearlayout_preinstall_programtable_exprotfile =(LinearLayout)
                findViewById(R.id.linearlayout_preinstall_programtable_exprotfile);
        textview_preinstall_programtable_note =(TextView)
                findViewById(R.id.textview_preinstall_programtable_note); 
        textview_preinstall_programtable_back = (TextView)
                findViewById(R.id.textview_preinstall_programtable_back); 
                
        textview_preinstall_programtable_note.setText(showInfo[PreinstallProGlobal.EXPROT_PROGRAM_NOTE]);
        if(mUSBDiskConnector.checkLostdirFolderOnUSBDisk(preinstallProTableActivity)){
        	 linearlayout_preinstall_programtable_import.setVisibility(View.INVISIBLE);//View.GONE
        }
    }
    
    
    private void setOnClickLisenters() {
       OnClickListener listener = new OnClickListener() {
          @Override
          public void onClick(View view) {
            switch (view.getId()) { 
            	 case R.id.linearlayout_preinstall_programtable_import:
            	      if(!mUSBDiskConnector.checkLostdirFolderOnUSBDisk(PreinstallProgramTableActivity.this)){
            	      	linearlayout_preinstall_programtable_import.setVisibility(View.VISIBLE);
            	        improtAtvProTableFromLocal(PreinstallProgramTableActivity.this); 
            	      }else{
            	       linearlayout_preinstall_programtable_import.setVisibility(View.INVISIBLE);
            	      }             	  
            	      break;
            	 case R.id.linearlayout_preinstall_programtable_updatefile:
            	      improtAtvProTableFromUSBDisk(PreinstallProgramTableActivity.this);              	  
            	      break;
            	 case R.id.linearlayout_preinstall_programtable_exprotfile:
            	      exprotAtvProTableFromLocal(PreinstallProgramTableActivity.this);              	  
            	      break;
            	 case R.id.textview_preinstall_programtable_back:
            	      if(SwitchPageHelper.goToMenuPage(PreinstallProgramTableActivity.this, KeyEvent.KEYCODE_MENU)== true){
                      Log.d(TAG, "Press the menu key to main menu"); 
                      finish();
            	      }             	  
            	      break;
            	 default:
			              break;
		        }
          }
       };
       linearlayout_preinstall_programtable_import.setOnClickListener(listener);
       linearlayout_preinstall_programtable_updatefile.setOnClickListener(listener);
       linearlayout_preinstall_programtable_exprotfile.setOnClickListener(listener);
       textview_preinstall_programtable_back.setOnClickListener(listener);
    }
    
    
    private void improtAtvProTableFromLocal(Context context){
    	 int res = -1;
    	 try {
         	  File targetFile = new File(PreinstallProGlobal.PROGRAM_FILE_PATH);
            if(!targetFile.exists()){
               Log.d( TAG, "There is no program table file locally!");
               res = PreinstallProGlobal.PROGRAM_TABLE_BLANK;               
            }else{
            	 boolean isForamtOk = mUSBDiskConnector.checkProgramTableFormatIsOk(targetFile);
               if(Utility.isSupportATV()&&(mCurInputSource == TvCommonManager.INPUT_SOURCE_ATV)&&isForamtOk){
    	 	          Log.i(TAG, " is Support ATV and cur input source is ATV! ");
    	            if(mTvChannelManager.getProgramCtrl(TvChannelManager.ATV_PROG_CTRL_GET_ACTIVE_PROG_COUNT, 0, 0)!=0){
                    for(int i = 0; i < mTvChannelManager.getProgramCtrl(TvChannelManager.ATV_PROG_CTRL_GET_ACTIVE_PROG_COUNT, 0, 0); i++){
                       mTvChannelManager.setProgramAttribute(TvChannelManager.PROGRAM_ATTRIBUTE_DELETE, i,
                                                             TvChannelManager.SERVICE_TYPE_ATV, 0x00, true); 
                       Log.i(TAG, " Delete the all Atv progrem before improt! "); 
                    }
                  }  
			            if( mTvChannelManager.saveAtvProgram(130)){
			            	res = PreinstallProGlobal.IMPORT_PROGRAM_SUCCESS; 
			            }
			         }else{
			           res = PreinstallProGlobal.IMPORT_PROGRAM_FAIL;
			         }		            	
            }  
       } catch (Exception e) {
         Log.e( TAG, "Error on write File:" + e);
       } 
       if((res >=0)&&(res < showInfo.length)){
			 	 toastShowInfo(context, res);
			 }	   	 	 
    }
    
    
    private void improtAtvProTableFromUSBDisk(Context context){
    	 int res = -1;
    	 res = mUSBDiskConnector.copyFileFromUSBDiskToLocal(context, 
    	                                                    mUDiskProgarmPath,
    	                                                    PreinstallProGlobal.PROGRAM_FILE_PATH);
    	 if(res == PreinstallProGlobal.COPY_FILE_TO_USB_SUCCESS){    	 
    	   if(Utility.isSupportATV()&&(mCurInputSource == TvCommonManager.INPUT_SOURCE_ATV)){
    	 	   Log.i(TAG, " is Support ATV and cur input source is ATV! ");
    	     if(mTvChannelManager.getProgramCtrl(TvChannelManager.ATV_PROG_CTRL_GET_ACTIVE_PROG_COUNT, 0, 0)!=0){
             for(int i = 0; i < mTvChannelManager.getProgramCtrl(TvChannelManager.ATV_PROG_CTRL_GET_ACTIVE_PROG_COUNT, 0, 0); i++){
                mTvChannelManager.setProgramAttribute(TvChannelManager.PROGRAM_ATTRIBUTE_DELETE, i,
                                                     TvChannelManager.SERVICE_TYPE_ATV, 0x00, true); 
                Log.i(TAG, " Delete the all Atv progrem before improt !"); 
             }
           }  
			     if(mTvChannelManager.saveAtvProgram(130)){
			       res = PreinstallProGlobal.IMPORT_PROGRAM_SUCCESS; 
			     }
			   }else{
			      res = PreinstallProGlobal.IMPORT_PROGRAM_FAIL;
			   }
			 }
			 
			 if(res == PreinstallProGlobal.IMPORT_PROGRAM_SUCCESS){
    	    Toast toast = Toast.makeText(context, showInfo[PreinstallProGlobal.COPY_DATE_LOADING], Toast.LENGTH_LONG);
    	    setTimeShowToast(toast, 8*1000);  	 	 
    	 }else if((res >=0)&&(res < showInfo.length)){
			 	 toastShowInfo(context, res);
			 }				 		 
    }
    
    
    private void exprotAtvProTableFromLocal(Context context){
    	 int res = -1;
    	 res = mUSBDiskConnector.copyFileFromLocalToUSBDisk(context, 
    	                                                   PreinstallProGlobal.PROGRAM_FILE_PATH,  
    	                                                   mUDiskProgarmPath);
    	                                                   
    	 if(res == PreinstallProGlobal.COPY_FILE_TO_USB_SUCCESS){
    	    Toast toast = Toast.makeText(context, showInfo[PreinstallProGlobal.COPY_DATE_LOADING], Toast.LENGTH_LONG);
    	    setTimeShowToast(toast, 15*1000);
    	    //mHandler.sendEmptyMessageDelayed(111,9*1000);  	 	 
    	 }else if((res >=0)&&(res < showInfo.length)){
			 	 toastShowInfo(context, res);
			 }    	  
    }
    
    
    private void toastShowInfo(Context context, int resId){
    	 Toast toast = Toast.makeText(context, showInfo[resId], Toast.LENGTH_SHORT);
       toast.show();
    }
    
    
    private void toastShowInfo(int resId){
    	 Toast toast = Toast.makeText(PreinstallProgramTableActivity.this, showInfo[resId], Toast.LENGTH_SHORT);
       toast.show();
    }
    
    
    private void setTimeShowToast(final Toast toast, final int duration) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 3000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
            	  Message message = Message.obtain();
            	  if(duration ==15*1000){
            	  	message.what = PreinstallProGlobal.COPY_FILE_TO_USB_SUCCESS;
            	  }else if(duration ==8*1000){
            	  	message.what = PreinstallProGlobal.IMPORT_PROGRAM_SUCCESS;
            	  }
            	  
            	  mHandler.sendMessage(message);
                toast.cancel();
                timer.cancel();
                cancel();                             
            }
        }, duration);
    }    
    
}
