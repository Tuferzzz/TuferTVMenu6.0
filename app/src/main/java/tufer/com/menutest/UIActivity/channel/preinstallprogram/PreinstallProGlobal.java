package tufer.com.menutest.UIActivity.channel.preinstallprogram;

public class PreinstallProGlobal {
	  /* showInfo index */
    public final static int IMPORT_PROGRAM_FAIL =0;
    
    public final static int IMPORT_PROGRAM_SUCCESS =1;
    
    public final static int PROGRAM_TABLE_BLANK =2;
    
    public final static int USB_FIND_FAIL =3;
    
    public final static int USB_CHECK_FILE =4;
    
    public final static int COPY_FILE_TO_USB_FAIL =5;
    
    public final static int COPY_FILE_TO_USB_SUCCESS =6;
    
    public final static int USB_NEW_PROGRAM_TABLE_FILE =7;
    
    public final static int COPY_DATE_LOADING =8;
    
    public final static int EXPROT_PROGRAM_NOTE =9;
    
    public final static int COPY_FILE_TO_LOCAL_FAIL =5;
    
    public final static int  COPY_FILE_TO_LOCAL_SUCCESS =6;
    
    /* file path */ 
    public final static String PROGRAM_FILE_NAME ="TvChannel";
    
    public final static String PVR_FOLDER_NAME ="LOST.DIR";
    
    public final static String PROGRAM_FILE_PATH ="/data/data/tufer.com.menutest/list/TvChannel";
    
    /*
     * TvChannel file format:
     *     programNo@programName@videoStandard@AudioStandard@frequencyKHz@frequencyPLL
     * E.g:
     *     method-1: 1@CCTV-1@PAL@BG@85200@0
     *     method-2: 1@CCTV-1@PAL@BG@0@1704
     *     method-3: 1@CCTV-1@PAL@BG@85200
     * note:
     *     Either select the frequency or select PLL, can not be used at the same time 
     */
         
    public final static int  PROGRAM_TABLE_LINE_ASTERISK_TOTAL =5;
    
    public final static String PROGRAM_TABLE_DATA_MARK ="@";
  
}
