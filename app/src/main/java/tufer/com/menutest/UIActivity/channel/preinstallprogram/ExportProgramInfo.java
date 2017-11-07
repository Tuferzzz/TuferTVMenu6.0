//<ADA Software>
//******************************************************************************
//
// Created by tianky@20161029.
// 
// TvChannel file format:
//     programNo*programName*frequencyKHz*videoStandard*AudioStandard*frequencyPLL
//
// 
// 
//******************************************************************************
//<ADA Software>

package tufer.com.menutest.UIActivity.channel.preinstallprogram;


import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.Integer;
import java.lang.String;
import android.util.Log;

import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tvapi.atv.vo.EnumGetProgramInfo;


public class ExportProgramInfo {
	
	  private final String TAG = "ExportProgramInfo";
	  
	  private TvChannelManager mTvChannelManager = null; 	  	

    private int mMinCh = 0;

    private int mMaxCh = 0;
    
	  private int mpgActiveCount = 0;
	  
	  private int mpgChannelIndex = 0;
	  
	  private int u16FrequencyKHz = 0;
    
    private String mpgName ="";
    
	  private String mAtvAudioStandard = "";
	  
	  private String mAtvVideoStandard = "";
	  	  
	  private String[] AtvVideoStandardtype = {
    		"PAL",
    		"NTSC",
    		"SECAM",
    		"NTSC44",
    		"PALM",
    		"PALN",
    		"PAL60",
    		"NOTSTANDARD",
    		"AUTO",
    };
	  
	  private String[] AtvAudioStandardtype = {
    		"BG",
    		"BGA2",
    		"BGNICAM",
    		"I",
    		"DK",
    		"DK1A2",
    		"DK2A2",
    		"DK3A2",
    		"DKNICAM",
    		"L",
    		"M",
    		"MBTSC",
    		"MA2",
    		"MEIAJ",
    		"NOTSTANDARD",
    };    
    
    public ExportProgramInfo() {
    	  mTvChannelManager = TvChannelManager.getInstance();
    }
     
      
    public void exprotATVProgramInfoToLocal() {
    	  WriteAtvProgramInfoToFile(PreinstallProGlobal.PROGRAM_FILE_PATH);
    	  Log.i(TAG,"Export ATV Program Info :" + PreinstallProGlobal.PROGRAM_FILE_PATH);
    }
      
      
    private void WriteAtvProgramInfoToFile(String strFilePath) {
         String strdata ="";
         mpgChannelIndex =GetATVChannelIndex();
         mpgActiveCount =GetU8AtvProgramActiveCount();
         if(strFilePath.length() != 0){
           try{
			   File file = new File(strFilePath);
			   if(file.exists()) {
				   Log.d(TAG,"创建单个文件" + strFilePath + "失败，目标文件已存在！");
				   //return false;
			   }
			   if (strFilePath.endsWith(File.separator)) {
				   Log.d(TAG,"创建单个文件" + strFilePath + "失败，目标文件不能为目录！");
				   //return false;
			   }
			   //判断目标文件所在的目录是否存在
			   if(!file.getParentFile().exists()) {
				   //如果目标文件所在的目录不存在，则创建父目录
				   Log.d(TAG,"目标文件所在目录不存在，准备创建它！");
				   if(!file.getParentFile().mkdirs()) {
					   Log.d(TAG,"创建目标文件所在目录失败！");
					   //return false;
				   }
			   }
			   //创建目标文件
			   try {
				   if (file.createNewFile()) {
					   Log.d(TAG,"创建单个文件" + strFilePath + "成功！");
					   //return true;
				   } else {
					   Log.d(TAG,"创建单个文件" + strFilePath + "失败！");
					   //return false;
				   }
			   } catch (IOException e) {
				   e.printStackTrace();
				   Log.d(TAG,"创建单个文件" + strFilePath + "失败！" + e.getMessage());
				   //return false;
			   }
//			   if (!file.exists()) {
//                 Log.d( TAG, "Create the file:" + strFilePath);
//                 file.getParentFile().mkdirs();
//                 file.createNewFile();
//               }
               RandomAccessFile raf = new RandomAccessFile(file, "rwd");
               if(file.length()!=0){
                  FileWriter mfile = new FileWriter(file); 
                  BufferedWriter delfile = new BufferedWriter(mfile);      
                  delfile.write("");
                  delfile.close();
               }
               if(mpgActiveCount== 0){
         	       strdata = "No Program !Please auto tuning !" + "\r\n";
         	       //raf.seek(file.length());
                 raf.write(strdata.getBytes());
               }
               else{
                  if((mpgChannelIndex>=GetATVChannelMin())&&(mpgChannelIndex<=GetATVChannelMax())){
         	           for(int i = mpgChannelIndex;i < mpgActiveCount;i++ ){
         	 	           strdata = GetAtvProgramInfoStr(i) + "\r\n";
                       raf.seek(file.length());
                       raf.write(strdata.getBytes());
         	           }
                  }
               }
               raf.close(); 
           } catch (Exception e) {
             Log.e( TAG, "Error on write File:" + e);
           }
         } 
    }
     
           
    private String GetAtvProgramInfoStr(int programNo){
    	   String pgInfoStr = null;  	  
         mAtvVideoStandard = GetAtvVideoStandardStr(programNo);
         mAtvAudioStandard = GetAtvAudioStandardStr(programNo);
         u16FrequencyKHz = GetAtvU16FrequencyKHz(programNo);
         mpgName = GetAtvProgramNameStr(programNo);                    	  
         pgInfoStr =Integer.toString(programNo)+ PreinstallProGlobal.PROGRAM_TABLE_DATA_MARK 
                                     + mpgName + PreinstallProGlobal.PROGRAM_TABLE_DATA_MARK 
                                         + "0" + PreinstallProGlobal.PROGRAM_TABLE_DATA_MARK 
                           + mAtvVideoStandard + PreinstallProGlobal.PROGRAM_TABLE_DATA_MARK 
                           + mAtvAudioStandard + PreinstallProGlobal.PROGRAM_TABLE_DATA_MARK 
                           + Integer.toString(u16FrequencyKHz); 
         return pgInfoStr; 
    }
          
	 
	 private int GetATVChannelMax() {
	      return mTvChannelManager.getProgramCtrl(TvChannelManager.ATV_PROG_CTRL_GET_MAX_CHANNEL, 0, 0);
   }
   
    
   private int GetATVChannelMin() {
	      return mTvChannelManager.getProgramCtrl(TvChannelManager.ATV_PROG_CTRL_GET_MIN_CHANNEL, 0, 0);
   }
   
       
	 private int GetU8AtvProgramActiveCount() {
	 	    int mAtvProgramCount1=mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV);
	 	    int mAtvProgramCount2=mTvChannelManager.getProgramCtrl(TvChannelManager.ATV_PROG_CTRL_GET_ACTIVE_PROG_COUNT, 0, 0);
	 	    if(mAtvProgramCount1>mAtvProgramCount2){
	 	   	  return mAtvProgramCount1;
	 	    }else{
	 	     return mAtvProgramCount2;
	 	   //return mTvChannelManager.getProgramCtrl(TvChannelManager.ATV_PROG_CTRL_GET_ACTIVE_PROG_COUNT, 0, 0);
	 	    }
	 }
   
    
   private int GetATVChannelIndex() {
	     //return mTvChannelManager.getAtvProgramInfo(TvChannelManager.GET_PROGRAMINFO_CHANNEL_INDEX, 0);
	     return mTvChannelManager.getProgramCtrl(TvChannelManager.ATV_PROG_CTRL_GET_FIRST_PROG_NUM, 0, 0);
   }
   
   
   private String GetAtvVideoStandardStr(int programNo) {
   	   int pgVideoStandard=mTvChannelManager.getAtvProgramInfo(TvChannelManager.GET_PROGRAMINFO_VIDEO_STANDARD, programNo);
   	   //Log.d(TAG, "mAtvVideoStandard: " + pgVideoStandard);
   	   for(int i = 0; i < AtvVideoStandardtype.length; i++){
   	   	  if(i==pgVideoStandard){
   	   	    return AtvVideoStandardtype[i];
   	   	  } 
   	   }
   	   return null;
   }
   
    
   private String GetAtvAudioStandardStr(int programNo) {
   	   int pgAudioStandard=mTvChannelManager.getAtvProgramInfo(TvChannelManager.GET_PROGRAMINFO_AUDIO_STANDARD, programNo);
   	   for(int i = 0; i < AtvAudioStandardtype.length; i++){
   	   	  if(i==pgAudioStandard){
   	   	    return AtvAudioStandardtype[i];
   	   	  } 
   	   }
   	   return null;
   }
   
   
   private int GetAtvU16FrequencyKHz(int programNo){
   	   //return mTvChannelManager.getAtvCurrentFrequency();
      return mTvChannelManager.getAtvProgramInfo(TvChannelManager.GET_PROGRAMINFO_PLL_DATA, programNo);
   }
   
   
   private String GetAtvProgramNameStr(int programNo){
	   //return "Tufer";
   	  return mTvChannelManager.getProgramName(programNo, TvChannelManager.SERVICE_TYPE_ATV, 0x00);
   }
}
