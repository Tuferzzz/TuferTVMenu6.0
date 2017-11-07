//<ADA Software>
//******************************************************************************
//
// Created by tianky@20170315.
// 
// The funtion is Update Max Vol Value
//
// file path : /data/local/setmaxvol.sql
//
//
//******************************************************************************
//<ADA Software>

package tufer.com.menutest.Util;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.widget.Toast;
import android.util.Log;

import tufer.com.menutest.R;

/////////////////////////////////////////////
public class MaxVolume {

    private static final String TAG = "MaxVolume";
    /* file path */
    private final static String SQL_FILE_PATH = "/data/local/setmaxvol.sql";
    /* show info */
    private final static String SET_VALUE_FAIL = "set the max volume value to fail!";

    private final static String GET_VALUE_FAIL = "can not read Value from the setmaxvol.sql file";


    public MaxVolume() {

    }

    private File findFileInLocalFolder(String filePath) {
        File target = null;
        File root = new File("/data/local/");
        if (root != null && root.exists()) {
            File[] fileItems = root.listFiles();
            if (fileItems != null) {
                for (int idx = 0; idx < fileItems.length; idx++) {
                    if (fileItems[idx].getPath().equals(filePath)) {
                        Log.i(TAG, "getPath()= " + fileItems[idx].getPath());
                        target = new File(filePath);
                        break;
                    }
                }
            }
        }
        return target;
    }

    private boolean writeValueToSQLFile(String filePath, String writeStr) {
        boolean res = false;
        //File file = findFileInLocalFolder(filePath);
        File file=null;
        try {
            if (filePath.length() != 0) {
//                if (file == null) {
//                    file = new File(filePath);
//                    if (!file.exists()) {
//                        Log.d(TAG, "writeValueToSQLFile-->Create the file:" + filePath);
//                        if (!file.getParentFile().exists()) {
//                            file.getParentFile().mkdirs();
//                        }
//                    }
//                } else {
//                    if (file.exists()) {
//                        file.delete();
//                    }
//                }
//                try {
//                    file.createNewFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    res = false;
//                }
                file = new File(filePath);
                if(file.exists()) {
                    Log.d(TAG,"创建单个文件" + filePath + "失败，目标文件已存在！");
                    file.delete();
                    //return false;
                }
                if (filePath.endsWith(File.separator)) {
                    Log.d(TAG,"创建单个文件" + filePath + "失败，目标文件不能为目录！");
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
                        Log.d(TAG,"创建单个文件" + filePath + "成功！");
                        //return true;
                    } else {
                        Log.d(TAG,"创建单个文件" + filePath + "失败！");
                        //return false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG,"创建单个文件" + filePath + "失败！!!!!!!!" + e.getMessage());
                    //return false;
                }
            }
            if ((file != null) && (writeStr.length() != 0)) {
                BufferedWriter bw = null;
                try {
                    bw = new BufferedWriter(new FileWriter(file));
                    bw.write(writeStr);
                    bw.flush();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    res = false;
                } finally {
                    bw.close();
                    res = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public boolean setLimitMaxVolume(Context context, int value) {
        String str = "";
        boolean res = false;
        if ((value >= 0) && (value <= 100)) {
            str = Integer.toString(value);
            res = writeValueToSQLFile(SQL_FILE_PATH, str);
            if (!res) {
                Toast.makeText(context, SET_VALUE_FAIL, Toast.LENGTH_SHORT).show();
            }
        }
        return res;
    }

    private String readValueFromSQLFile(String filePath) {
        String str = "";
        File file = findFileInLocalFolder(filePath);
        if (file != null && file.exists()) {
            try {
                FileInputStream fin = new FileInputStream(filePath);
                int length = fin.available();
                Log.i(TAG, "readSQLFile()  length= " + length);
                byte[] buffer = new byte[length];
                fin.read(buffer);
                str = EncodingUtils.getString(buffer, "UTF-8");
                fin.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    private boolean strIsNumeric(String str) {
        int len = 0;
        boolean res = false;
        len = str.length();
        if (len < 4 && len > 0) {
            res = true;
            for (int i = 0; i < len; i++) {
                if (!Character.isDigit(str.charAt(i))) {
                    res = false;
                    break;
                }
            }
        }
        return res;
    }

    public int getLimitMaxVolume(Context context) {
        int value = 100;
        String readStr = readValueFromSQLFile(SQL_FILE_PATH);
        if (strIsNumeric(readStr)) {
            value = Integer.parseInt(readStr);
            if (value < 0 || value > 100) {
                value = 100;
            } else if (value >= 0 && value < 5) {
                 /*" min set Max Vol is "*/
                value = 5;
                String mToastSay = context.getString(R.string.show_min_limit_maxvol) + value + " ... ";
                Toast.makeText(context, mToastSay, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, GET_VALUE_FAIL, Toast.LENGTH_SHORT).show();
        }
        return value;
    }

    public int getLimitMaxVolume() {
        int value = 100;
        String readStr = readValueFromSQLFile(SQL_FILE_PATH);
        if (strIsNumeric(readStr)) {
            value = Integer.parseInt(readStr);
            if (value < 0 || value > 100) {
                value = 100;
            } else if (value >= 0 && value < 5) {
                value = 5;
            }
        }
        return value;
    }


}