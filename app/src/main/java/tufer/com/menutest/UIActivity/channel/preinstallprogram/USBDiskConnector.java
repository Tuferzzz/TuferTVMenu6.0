//<ADA Software>
//******************************************************************************
/*
 *Created by tianky@20161029.
 *
 * TvChannel file format:
 *     programNo@programName@videoStandard@AudioStandard@frequencyKHz@frequencyPLL
 * E.g:
 *    method-1: 1@CCTV-1@PAL@BG@85200@0
 *    method-2: 1@CCTV-1@PAL@BG@0@1704
 *    method-3: 1@CCTV-1@PAL@BG@0
 * note:
 *     Either select the frequency or select PLL, not be used at the same time 
 */
//******************************************************************************
//<ADA Software>

package tufer.com.menutest.UIActivity.channel.preinstallprogram;

import android.media.AudioManager;
import android.media.AudioSystem;
import android.util.Log;
//import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;

import android.content.Context;
import android.hardware.usb.UsbManager;
//import com.mstar.android.storage.MStorageManager;


public class USBDiskConnector {

    private final String TAG = "USBDiskConnector";

    private String mRootDirectory = "/mnt/usb/";


    public USBDiskConnector() {

    }

    private boolean checkUsbIsExist(Context context) {
        boolean ret = false;
        UsbManager usbman = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        if (usbman.getDeviceList().isEmpty() == false) {
            ret = true;
        }
        return ret;
    }

    private String findFileInUSBDisk(String fileName) {
        // TODO Find File On USB function
        String filePath = "";
        File usbRoot = new File("/mnt/usb/");
        File targetFile;
        if (usbRoot != null && usbRoot.exists()) {
            File[] usbItems = usbRoot.listFiles();
            for (int sdx = 0; sdx < usbItems.length; sdx++) {
                if (usbItems[sdx].isDirectory()) {
                    targetFile = new File(usbItems[sdx].getPath() + "/" + fileName);
                    if (targetFile != null && targetFile.exists()) {
                        filePath = usbItems[sdx].getPath() + "/" + fileName;
                        mRootDirectory = usbItems[sdx].getPath() + "/";
                        break;
                    }
                }
            }
        }
        return filePath;
    }

    private File isCopyFile(String copyPath, String pastePath) {
        boolean res = true;
        File pasteFile = null;
        File copyFile = null;
        FileInputStream in = null;
        FileOutputStream out = null;
        FileChannel inC = null;
        FileChannel outC = null;
        if ((copyPath.length() != 0) && (copyPath.length() != 0)) {
            try {
                copyFile = new File(copyPath);
                if (checkProgramTableFormatIsOk(copyFile)) {
                    pasteFile = new File(pastePath);
                    if (!pasteFile.exists()) {
                        Log.d(TAG, "isCopyFile:Create the file:" + pasteFile);
                        pasteFile.getParentFile().mkdirs();
                        res = pasteFile.createNewFile();
                    }
                    if (res) {
                        in = new FileInputStream(copyFile);
                        out = new FileOutputStream(pasteFile);
                        inC = in.getChannel();
                        outC = out.getChannel();
                        inC.transferTo(0, inC.size(), outC);
                        in.close();
                        out.close();
                        inC.close();
                        outC.close();
                    }
                }
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return pasteFile;
    }

    public String getUDiskRootDirectory(Context context) {
        String root = "";
        if (checkUsbIsExist(context)) {
            String mPVRFolderPath = findFileInUSBDisk(PreinstallProGlobal.PVR_FOLDER_NAME);
            if (mPVRFolderPath.length() != 0) {
                root = mPVRFolderPath.substring(0, (mPVRFolderPath.length() - PreinstallProGlobal.PVR_FOLDER_NAME.length()));
                Log.d(TAG, " U Disk Root Directory = " + root);
                if (!root.equals(mRootDirectory)) {
                    root = "";
                }
            }
        }
        return root;
    }

    /*
    * TvChannel file format:
    *     programNo@programName@videoStandard@AudioStandard@frequencyKHz@frequencyPLL
    * E.g:
    *     method-1: 1@CCTV-1@PAL@BG@85200@0
    *     method-2: 1@CCTV-1@PAL@BG@0@1704
    *     method-3: 1@CCTV-1@PAL@BG@0
    * note:
    *     Either select the frequency or select PLL, can not be used at the same time
    */
    public boolean checkProgramTableFormatIsOk(File file) {
        boolean res = false;
        String line = null;
        int lineLength = -1;
        char lineChar[] = null;
        if (file.exists()) {
            try {
                BufferedReader bw = new BufferedReader(new FileReader(file));
                while ((line = bw.readLine()) != null) {
                    lineChar = line.toCharArray(); //.split()
                    //Log.d(TAG, "check Program table of line format is Ok:line.length() = " + line.length());
                    if (line.length() != 0) {
                        int count = 0;
                        String mark = null;
                        for (int i = 0; i < line.length(); i++) {
                            mark = String.valueOf(lineChar[i]);
                            if (mark.equals(PreinstallProGlobal.PROGRAM_TABLE_DATA_MARK)) {
                                count = count + 1;
                            }
                            //Log.d(TAG, "checkProgramTableFormatIsOk: i = "+ i +"  lineChar = " + lineChar[i]);
                        }
                        Log.d(TAG, "check Program table of line format is Ok: Asterisk(*) num = " + count);
                        if ((count > PreinstallProGlobal.PROGRAM_TABLE_LINE_ASTERISK_TOTAL) ||
                                (count < (PreinstallProGlobal.PROGRAM_TABLE_LINE_ASTERISK_TOTAL - 1))) {
                            res = false;
                        } else {
                            res = true;
                        }
                    }
                    if (res == false) {
                        Log.d(TAG, "The program table format is incorrect ");
                        break;
                    }
                }
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }
  
  /*
  * note:
  *     PVR Funtion: Insert U disk will automatically create a new LOST.DIR folder
  *          
  *     Determine whether the U disk is insert or unplug
  *     
  */

    public boolean checkLostdirFolderOnUSBDisk(Context context) {
        boolean res = false;
        if (checkUsbIsExist(context)) {
            String mPVRFolderPath = findFileInUSBDisk(PreinstallProGlobal.PVR_FOLDER_NAME);
            String mListTxtPath = findFileInUSBDisk(PreinstallProGlobal.PROGRAM_FILE_NAME);
            Log.d(TAG, "check Lostdir Folder On USB Disk= " + mPVRFolderPath);
            if ((mPVRFolderPath.length() != 0) || (mListTxtPath.length() != 0)) {
                res = true;
            } else {
                res = false;
            }
        }
        return res;
    }


    public boolean checkAllProgramTableIsExists(Context context) {
        boolean res = false;
        if (checkUsbIsExist(context)) {
            String mPVRFolderPath = findFileInUSBDisk(PreinstallProGlobal.PVR_FOLDER_NAME);
            String mListTxtPath = findFileInUSBDisk(PreinstallProGlobal.PROGRAM_FILE_NAME);
            File mLocalListTxtFile = new File(PreinstallProGlobal.PROGRAM_FILE_PATH);
            Log.d(TAG, "check Lostdir Folder On USB Disk= " + mPVRFolderPath);
            if ((mPVRFolderPath.length() != 0) || (mListTxtPath.length() != 0) || mLocalListTxtFile.exists()) {
                res = true;
            } else {
                res = false;
            }
        }
        return res;
    }


    public int copyFileFromUSBDiskToLocal(Context context, String copyPath, String pastePath) {
        // TODO Auto-generated method stub
        int res = -1;
        if (checkUsbIsExist(context) && (copyPath.length() != 0) && (pastePath.length() != 0)) {
            try {
                if (checkLostdirFolderOnUSBDisk(context)) {
                    File pasteFile = isCopyFile(copyPath, pastePath);
                    Log.d(TAG, "From USBDisk To Local:pasteFile= " + pasteFile);
                    if (pasteFile != null) {
                        if (pasteFile.exists()) {
                            String command = "chmod 777 " + pasteFile.getAbsolutePath();
                            Log.d(TAG, "command= " + command);
                            Runtime runtime = Runtime.getRuntime();
                            Process proc = runtime.exec(command);
                            //proc.destroy();
                            res = PreinstallProGlobal.COPY_FILE_TO_LOCAL_SUCCESS;
                        } else {
                            res = PreinstallProGlobal.COPY_FILE_TO_LOCAL_FAIL;
                        }
                    } else {
                        res = PreinstallProGlobal.PROGRAM_TABLE_BLANK;
                    }
                } else {
                    res = PreinstallProGlobal.USB_FIND_FAIL;
                }
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        } else {
            res = PreinstallProGlobal.USB_FIND_FAIL;
        }
        return res;
    }

    public int copyFileFromLocalToUSBDisk(Context context, String copyPath, String pastePath) {
        int res = -1;
        try {
            File targetFile = new File(copyPath);
            if (!targetFile.exists()) {
                Log.d(TAG, "There is no program table file locally!");
                res = PreinstallProGlobal.PROGRAM_TABLE_BLANK;
            } else {
                if (checkLostdirFolderOnUSBDisk(context)) { // checkUsbIsExist
                    File pasteFile = isCopyFile(copyPath, pastePath);
                    if (pasteFile != null) {
                        if (pasteFile.exists()) {
                            res = PreinstallProGlobal.COPY_FILE_TO_USB_SUCCESS;
                        } else {
                            res = PreinstallProGlobal.COPY_FILE_TO_USB_FAIL;
                        }
                    } else {
                        res = PreinstallProGlobal.PROGRAM_TABLE_BLANK;
                    }
                } else {
                    //String listPath = findFileInUSBDisk(PreinstallProGlobal.PROGRAM_FILE_NAME);
                    String listPath = findFileInUSBDisk(PreinstallProGlobal.PVR_FOLDER_NAME);
                    if (listPath.length() == 0) {
                        res = PreinstallProGlobal.USB_FIND_FAIL;
                    } else {
                        res = PreinstallProGlobal.USB_NEW_PROGRAM_TABLE_FILE;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error on write File:" + e);
        }
        return res;
    }

}
