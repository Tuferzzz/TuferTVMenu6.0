package tufer.com.menutest.UIActivity.channel;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import tufer.com.menutest.R;
import tufer.com.menutest.UIActivity.MainActivity;
import tufer.com.menutest.UIActivity.holder.ChannelViewHolder;


/**
 * Created by Administrator on 2017/7/29 0029.
 */

public class ChannelActivity extends Activity {
    private static final String TAG = "ChannelActivity";
    private ChannelViewHolder mChannelViewHolder;
    public final static int CHANNEL_PAGE = 2;
    public static int selectedstatusforChannel = 0x00000000;
    public final static int PICTURE_PAGE = 0;
    public final static int OPTION_PAGE = 7;
    public final static int LOCK_PAGE = 8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel);
        mChannelViewHolder = new ChannelViewHolder(this);
        mChannelViewHolder.findViews();
        mChannelViewHolder.updateUi();

        //ReadTxtFile(this);
    }

    @Override
    protected void onStop() {
		if(MainActivity.myMainActivity!=null){
			MainActivity.myMainActivity.handler.sendEmptyMessage(MainActivity.UPDATE_CHANNEL);
		}       
        super.onStop();
    }

//    public static void ReadTxtFile(Context context) {
//
//        try {
//            FileInputStream inStream = context.openFileInput("list.txt");//只需传文件名
//            if (inStream != null) {
//                InputStreamReader inputreader = new InputStreamReader(inStream);
//                BufferedReader buffreader = new BufferedReader(inputreader);
//                String line;
//                String[] str;
//                //分行读取
//                while ((line = buffreader.readLine()) != null) {
//                    //content += line + "\n";
//                    str = line.split("@");
//                    mMap.put(str[2], str[1]);
//                    Log.d(TAG, str[0] + ":" + str[1] + ":" + str[2]);
//                    Log.d(TAG, mMap.get(str[2]) + ":" + str[1] );
//                }
//                inStream.close();
//            }
//
////                InputStream instream = new FileInputStream(file);
////                if (instream != null)
////                {
////                    InputStreamReader inputreader = new InputStreamReader(instream);
////                    BufferedReader buffreader = new BufferedReader(inputreader);
////                    String line;
////                    String[] str;
////                    //分行读取
////                    while (( line = buffreader.readLine()) != null) {
////                        //content += line + "\n";
////                        str=line.split("@");
////                        mMap.put(str[1],str[2]);
////                        Log.d(TAG,str[0]+":"+str[1]+":"+str[2]);
////                    }
////                    instream.close();
////                }
//        } catch (java.io.FileNotFoundException e) {
//            Log.d(TAG, "The File doesn't not exist.");
//        } catch (IOException e) {
//            Log.d(TAG, e.getMessage());
//        }
//    }


}
