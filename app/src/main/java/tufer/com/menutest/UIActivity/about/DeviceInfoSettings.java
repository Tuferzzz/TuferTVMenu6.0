package tufer.com.menutest.UIActivity.about;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemProperties;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


import com.mstar.android.tv.TvFactoryManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;

import tufer.com.menutest.R;
import tufer.com.menutest.Util.Tools;


public class DeviceInfoSettings extends Activity {

	private String TAG ="DeviceInfoSettings";

    private static final int LEGAL_INFO = 6;

    private ListView aboutListView;

    private AboutAdapter mAboutAdapter;

    private String freeMemory;

    private String totalMemory;
    
    private String boardModel = "Mstar TV";

    private String customerName = "ADA828";

    private String[] content;

    private Handler handler;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //getTVModelandCustomerName();

        handler = new Handler();

        freeMemory = Tools.getAvailMemory(this);
        totalMemory = Tools.getTotalMemory(this);

        if (freeMemory == null || totalMemory == null) {
            freeMemory = Tools.getAvailMemory(DeviceInfoSettings.this);
            totalMemory = Tools.getTotalMemory(DeviceInfoSettings.this);
        }

        setContentView(R.layout.about);
        content = new String[] {
                getTVModelNumber(), getTVSystemVersion(),getKernelVersion(),
				getPreSotfwareVersion(),getTVMemoryInfo()
        };
        
        findViews();
        registerListeners();
    }

    private void findViews() {
        aboutListView = (ListView) findViewById(R.id.about_list_select);
        aboutListView.setDividerHeight(0);

        mAboutAdapter = new AboutAdapter(this, new String[] {
                getResources().getString(R.string.model_number),
                getResources().getString(R.string.system_version),
                getResources().getString(R.string.kernelVersion),
                getResources().getString(R.string.buildVersion),
                getResources().getString(R.string.memory_infor),                
        }, content);
        aboutListView.setAdapter(mAboutAdapter);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                aboutListView.requestFocusFromTouch();
                aboutListView.setSelection(0);
            }
        }, 100);
    }

    private void registerListeners() {
        aboutListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.requestFocusFromTouch();
                if (position == LEGAL_INFO) {
                    //Tools.intentForward(DeviceInfoSettings.this, LegalInfoActivity.class);
                }
            }
        });
    }
    
    private void getTVModelandCustomerName() {
        String boardType = TvFactoryManager.getInstance().getBoardType();
        String[] array = boardType.split(":");
        if((array.length!=0)&&(array.length==3)){
         	 boardModel = array[0];
         	 customerName = array[1];
        }else{
           //boardModel = "MSD638";
         	 boardModel = "ADA";
        }
    }

    private String getTVModelNumber() {
    	   return "Mstar TV";
         //return boardModel; 
        //return Build.DISPLAY; 
    }

    private String getTVSystemVersion() {
        return Build.VERSION.RELEASE;
    }
    
    private static String getKernelVersion() {
		     String kernelVersion = "";
		     InputStream inputStream = null;
		     try {
			        inputStream = new FileInputStream("/proc/version");
		     } catch (FileNotFoundException e) {
			       e.printStackTrace();
			      return kernelVersion;
		     }
		     BufferedReader bufferedReader = new BufferedReader(
				              new InputStreamReader(inputStream), 8 * 1024);
		     String info = "";
		     String line = "";
		     try {
			        while ((line = bufferedReader.readLine()) != null) {
				             info += line;
			        }
		     } catch (IOException e) {
			       e.printStackTrace();
		     } finally {
			      try {
				          bufferedReader.close();
				          inputStream.close();
		             } catch (IOException e) {
				           e.printStackTrace();
			           }
		     }

		    try {
			       if (info != "") {
				        final String keyword = "version ";
				        int index = info.indexOf(keyword);
				        line = info.substring(index + keyword.length());
				        index = line.indexOf(" ");
				        kernelVersion = line.substring(0, index);
			       }
		    } catch (IndexOutOfBoundsException e) {
			    e.printStackTrace();
		    }

		   return kernelVersion;
	  }
    
    private String getSystemBuildVersion(){
         String mBuildTime = null;
         String mBuildVersion = null;
         int mPanelWidth = 0;
         try{
            mPanelWidth = TvManager.getInstance().getPanelIniInfo("panel:m_wPanelWidth");
         } catch (TvCommonException e) {
          e.printStackTrace();
		     }
         //String mPanelName =TvFactoryManager.getInstance().getPanelType();
     	   mBuildTime = new String(SystemProperties.get("ro.build.date"));  
     	   String[] array = mBuildTime.split(" ");
     	   if(array.length>=7){
     	   	 int spaceNum= 0;
     	  	 for(int i=0; i<(array.length-spaceNum); i++){
     	  		  if("".equals(array[i])){
     	  		  	 for(int j=i;j<array.length;j++){
     	  		  	 	  if(j==(array.length -1)){
     	  		  	 	  	array[j] =""; 
     	  		  	 	  }else{
     	  		  	 	    array[j]=array[j+1];
     	  		  	 	  }
     	  		  	 }
     	  		  	spaceNum =spaceNum+1;
     	  		  }
     	  	 }
     	   }
     	  
     	   if( mBuildTime !=null && array.length!=0 && array.length>=6 && mPanelWidth > 0){
     	     	mBuildVersion = boardModel+" "/*+customerName+" "*/+Integer.toString(mPanelWidth)+" "+
     	     	                 array[5]+array[1]+array[2]+" "+array[3];     	     
     	   }else{
     	   	  mBuildVersion = TvFactoryManager.getInstance().getCompileTime().substring(0, 10)+" "+
     	   	                      TvFactoryManager.getInstance().getCompileTime().substring(11);
     	   } 
     	   return  mBuildVersion; 	 	        	   
    }

	private String getPreSotfwareVersion(){
		String strSWVer= "";
		try {
			if(TvManager.getInstance() != null){
				strSWVer =TvManager.getInstance().getSystemSoftwareVersion();
			}
		} catch (TvCommonException e) {
			e.printStackTrace();
		}
		Log.d(TAG," strSWVer:" + strSWVer);
		return strSWVer;
	}

    private String getTVMemoryInfo() {
        return freeMemory + "/" + totalMemory;
    }

//    private String getTVDiskInfo() {
//        long freeSize = new Storage().getDiskStorage().mFreeStrorage;
//        long totalSize = new Storage().getDiskStorage().mTotalStorage;
//
//        return Tools.sizeToM(freeSize) + "/" + Tools.sizeToM(totalSize);
//    }

}
