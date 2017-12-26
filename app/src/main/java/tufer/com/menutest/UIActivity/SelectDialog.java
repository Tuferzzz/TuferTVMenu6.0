package tufer.com.menutest.UIActivity;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvFactoryManager;
import com.mstar.android.tv.TvPictureManager;

import java.util.ArrayList;

import tufer.com.menutest.R;

import tufer.com.menutest.UIActivity.general.powerinput.GetTvSource;
import tufer.com.menutest.UIActivity.general.powerinput.InputSourceItem;
import tufer.com.menutest.Util.Utility;

/**
 * Created by Administrator on 2017/8/4 0004.
 */

public class SelectDialog extends Activity {
    private static final String TAG = "SelectDialog";
    private static final String[] mSourceListname = {
            "VGA2","VGA3","HDMI-DP","OPS"
       };
    private ListView mPowerInputListView;
    private int mSelect;

    private ArrayList<InputSourceItem> mGalleryItemList;

    private String list[];

    private ArrayAdapter<String> mAdapter;

    private TextView title;
    private int intelligencePosition;
    private int[] titleName;

    private TvPictureManager mTvPictureManager = null;
    private TvAudioManager mTvAudioManager = null;
    private String wind;
    private String[] type={"PowerInput", "PictureMode", "ZoomMode", "ColorTemperature",
            "ImgNoiseReduction", "SoundMode",
            "IntelligenceSwitch", "MenuShowTime","MPEGNoiseReduction"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select);
        wind=getIntent().getStringExtra("Type");
        Window w = getWindow();
        Resources resources = getResources();
        Drawable drawable = resources.getDrawable(R.drawable.dialog_bg);
        w.setBackgroundDrawable(drawable);
        w.setTitle(null);

        Point point = new Point();
        Display display = w.getWindowManager().getDefaultDisplay();
        display.getSize(point);
        int width = (int) (point.x * 0.3);
        int height = (int) (point.y * 0.4);
        w.setLayout(width, height);
        w.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams wl = w.getAttributes();
        w.setAttributes(wl);

        findViews();
        registerListeners();
    }

    private void registerListeners() {
        mPowerInputListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mSelect=i;
                setData();
				if(MainActivity.myMainActivity!=null){
					MainActivity.myMainActivity.downTime=System.currentTimeMillis();
					SharedPreferences preferences=getSharedPreferences("TuferTvMenu", Context.MODE_PRIVATE);
					MainActivity.myMainActivity.isMenuShow=preferences.getBoolean("isMenuShow", false);
				}  
                finish();

            }
        });
    }



    private void findViews() {
        mPowerInputListView = (ListView) findViewById(R.id.select_listview);
        title= (TextView) findViewById(R.id.select_textview);
        mPowerInputListView.setDivider(null);
        getData();

        mAdapter = new ArrayAdapter<String>(this,
                R.layout.list_item_city_select, list);
        mPowerInputListView.setDividerHeight(0);
        mPowerInputListView.setItemsCanFocus(false);
        mPowerInputListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mPowerInputListView.setAdapter(mAdapter);
        Log.d(TAG, "mSelect, " + mSelect);
        mPowerInputListView.setItemChecked(mSelect, true);
        mPowerInputListView.setSelection(mSelect);
    }
    private int getPictureMode()
    {
        if (this.mTvPictureManager != null) {
            this.mSelect = this.mTvPictureManager.getPictureMode();
        }
        return this.mSelect;
    }
    private void setPictureMode(int paramInt)
    {
        if (this.mTvPictureManager != null) {
            this.mTvPictureManager.setPictureMode(paramInt);
        }
    }

    public void getData() {
        mTvPictureManager = TvPictureManager.getInstance();
        mTvAudioManager = TvAudioManager.getInstance();
        GetTvSource getTvSource = new GetTvSource(this);
        mGalleryItemList = getTvSource.getSource();
        int position = 0;
        for (int i = 0; i < type.length; i++) {
            if (wind.equals(type[i])) {
                position = i;
                break;
            }
        }
        switch (position) {
            case 0:
                mSelect= Utility.getinputpostion(Utility.getinputboot());
                list=Utility.powerInputList;
                title.setText(this.getString(R.string.str_mainmenu_general_powerinput));
                break;
            case 1:
                list = this.getResources().getStringArray(R.array.str_arr_picture_picturemode_vals);
                mSelect = getPictureMode();
                title.setText(this.getString(R.string.str_mainmenu_picture_picturemode));
                break;
            case 2:
                list = this.getResources().getStringArray(R.array.str_arr_picture_zoommode_vals);
                mSelect = getZoomMode();
                title.setText(this.getString(R.string.str_mainmenu_picture_zoommode));
                break;
            case 3:
                list = this.getResources().getStringArray(R.array.str_arr_picture_colortemperature_vals);
                mSelect = getColorTemperature();
                title.setText(this.getString(R.string.str_mainmenu_picture_color_temperature));
                break;
            case 4:
                list = this.getResources().getStringArray(R.array.str_arr_pic_imgnoisereduction_vals);
                mSelect = getNoisReduction();
                title.setText(this.getString(R.string.str_mainmenu_picture_imgnoisereduction));
                break;
            case 5:
                list = this.getResources().getStringArray(R.array.str_arr_sound_soundmode_vals);
                mSelect = getSoundMode();
                title.setText(this.getString(R.string.str_mainmenu_sound_soundmode));
                break;
            case 6:
                titleName = MainActivity.intelligenceNameList;
                intelligencePosition = MainActivity.intelligencePosion;
                list = this.getResources().getStringArray(R.array.str_arr_mainmenu_switch_vals);
                mSelect = getIsOff() ? 0 : 1;
                title.setText(this.getString(titleName[intelligencePosition]));
                break;
            case 7:
                list = this.getResources().getStringArray(R.array.str_arr_intelligence_menu_display_time_vals);
                mSelect = getMenudisplaytimeselect();
                title.setText(this.getString(R.string.str_mainmenu_intelligence_menu_display_time));
                break;
            case 8:
                list = this.getResources().getStringArray(R.array.str_arr_pic_mpegnoisereduction_vals);
                mSelect = getMPEGNoiseReduction();
                title.setText(this.getString(R.string.str_pic_mpegnoisereduction));
                break;
        }


    }
    private void setData() {
        int position=0;
        for(int i=0;i<type.length;i++){
            if(wind.equals(type[i])){
                position=i;
                break;
            }
        }
        switch (position){
            case 0:
                MainActivity.generalInputPosition=mSelect;
                MainActivity.powerInputString=list[mSelect];
                int TvPosition = 0;
                switch (mSelect){
                    case 0:
                        SystemProperties.set("persist.adaboot.channel",String.valueOf(TvCommonManager.INPUT_SOURCE_NONE));
                        break;
                    case 1:
                        SystemProperties.set("persist.adaboot.channel",String.valueOf(TvCommonManager.INPUT_SOURCE_STORAGE));
                        break;
                    default:
                        TvPosition = mSelect -2;
						setTvonmesger(TvPosition);
                        break;
                }
                break;
            case 1:
                setPictureMode(mSelect);
                break;
            case 2:
                setZoomMode(mSelect);
                break;
            case 3:
                setColorTemperature(mSelect);
                break;
            case 4:
                setNoisReduction(mSelect);
                break;
            case 5:
                setSoundMode(mSelect);
                break;
            case 6:
                setIsOff(mSelect==0?true:false);
                break;
            case 7:
                setMenudisplaytimeselect(mSelect);
                break;
            case 8:
                setMPEGNoiseReduction(mSelect);
                break;
        }
    }
    private int getZoomMode()
    {
        if (this.mTvPictureManager != null) {
            this.mSelect = this.mTvPictureManager.getVideoArcType();
        }
        return this.mSelect;
    }

    private void setZoomMode(int paramInt)
    {
        if (this.mTvPictureManager != null) {
            this.mTvPictureManager.setVideoArcType(paramInt);
        }
    }

    private int getColorTemperature()
    {
        if (this.mTvPictureManager != null) {
            this.mSelect = this.mTvPictureManager.getColorTemprature();
        }
        return this.mSelect;
    }

    private void setColorTemperature(int paramInt)
    {
        if (this.mTvPictureManager != null) {
            this.mTvPictureManager.setColorTemprature(paramInt);
        }
    }

    private int getNoisReduction()
    {
        if (this.mTvPictureManager != null) {
            this.mSelect = this.mTvPictureManager.getNoiseReduction();
        }
        return this.mSelect;
    }

    private void setNoisReduction(int paramInt)
    {
        if (this.mTvPictureManager != null) {
            this.mTvPictureManager.setNoiseReduction(paramInt);
        }
    }

    public int getSoundMode()
    {
        if (this.mTvAudioManager != null) {
            this.mSelect = this.mTvAudioManager.getAudioSoundMode();
        }
        return this.mSelect;
    }

    public void setSoundMode(int paramInt)
    {
        if (this.mTvAudioManager != null) {
            this.mTvAudioManager.setAudioSoundMode(paramInt);
        }
    }

    private int getMenudisplaytimeselect() {
        mSelect=getSharedPreferences("TuferTvMenu",Context.MODE_PRIVATE ).getInt("menuDisplayTime",0)/5;
        return mSelect;
    }

    private void setMenudisplaytimeselect(int paramInt)
    {
        SharedPreferences.Editor localEditor = getSharedPreferences("TuferTvMenu", Context.MODE_PRIVATE).edit();
        if(paramInt==0){
            localEditor.putBoolean("isMenuShow", false);
            localEditor.putInt("menuDisplayTime", paramInt*5);
        }else{
            localEditor.putBoolean("isMenuShow", true);
            localEditor.putInt("menuDisplayTime", paramInt*5);
        }
        localEditor.apply();
    }

    private boolean getIsOff() {
        boolean b=false;
        switch (intelligencePosition){
            case 0:
                break;
            case 1:
                b=getChildLockMode();
                break;
            case 2:
                b=getEnergySavingMode();
                break;
            case 3:
                b=getTemperatureDetectionStatus();
                break;
            case 4:
                b=getSensorLightMode();
                break;
            case 5:
                b=getProtectEyesMode();
                break;
            case 6:
                b=getNoSignalReturnMode();
                break;
            case 7:
                break;
        }
        return b;
    }

    private void setIsOff(boolean b) {
        switch (intelligencePosition){
            case 0:
                break;
            case 1:
                setChildLockMode(b);
                break;
            case 2:
                setEnergySavingMode(b);
                break;
            case 3:
                setTemperatureDetectionStatus(b);
                break;
            case 4:
                setSensorLightMode(b);
                break;
            case 5:
                setProtectEyesMode(b);
                break;
            case 6:
                setNoSignalReturnMode(b);
                break;
            case 7:
                break;
        }

    }

    private boolean getChildLockMode()
    {
        return true;
    }

    private void setChildLockMode(boolean b)
    {
        if(!b){
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction("com.szada.systemui.action.LOCK.START");
            this.sendBroadcast(intent);
			if(MainActivity.myMainActivity!=null){
				MainActivity.myMainActivity.finish();
			}
        }

    }

    private boolean getEnergySavingMode()
    {
        return true;

    }

    private void setEnergySavingMode(boolean b)
    {

    }

    private boolean getTemperatureDetectionStatus()
    {
        return !getSharedPreferences("MyTvSetting", 0).getBoolean("temperatureCheckStatus", false);
    }

    private void setTemperatureDetectionStatus(boolean b)
    {
        boolean bool = false;
        SharedPreferences.Editor localEditor =getSharedPreferences("MyTvSetting", 0).edit();
        if (!b) {
            bool = true;
        }
        localEditor.putBoolean("temperatureCheckStatus", bool);
        localEditor.apply();
    }

    private boolean getSensorLightMode()
    {
        return !getSharedPreferences("MyTvSetting", 0).getBoolean("lightSensorStatus", false);
    }

    private void setSensorLightMode(boolean b)
    {

    }

    private boolean getProtectEyesMode()
    {
        return !getSharedPreferences("MyTvSetting", 0).getBoolean("protectEyesStatus", false);
    }

    private void setProtectEyesMode(boolean b)
    {

    }

    private boolean getNoSignalReturnMode()
    {
        return true;

    }

    private void setNoSignalReturnMode(boolean b)
    {

    }

    private void setTvonmesger(int position) {
        InputSourceItem item = mGalleryItemList.get(position);
        int inputSrcIndex = item.getPositon();
      
        switch (inputSrcIndex) {
            case TvCommonManager.INPUT_SOURCE_VGA:
                if(item.getInputSourceName().equals("VGA2")){
                    inputSrcIndex = TvCommonManager.INPUT_SOURCE_NONE + 1;
                }else if(item.getInputSourceName().equals("VGA3")){
                    inputSrcIndex = TvCommonManager.INPUT_SOURCE_NONE + 2;
                }
                break;

            case TvCommonManager.INPUT_SOURCE_HDMI2:
                  if(item.getInputSourceName().equals("HDMI-DP")){
                       inputSrcIndex = TvCommonManager.INPUT_SOURCE_NONE + 3;
                    }

                break;
            case TvCommonManager.INPUT_SOURCE_HDMI3:
                if(item.getInputSourceName().equals("OPS")){
                    inputSrcIndex = TvCommonManager.INPUT_SOURCE_NONE + 4;
                }
                break;
            default:
                break;

        }
		 
        SystemProperties.set("persist.adaboot.channel",String.valueOf(inputSrcIndex));
    }

    public int getMPEGNoiseReduction() {
        return mTvPictureManager.getMpegNoiseReduction();
    }

    public void setMPEGNoiseReduction(int MPEGNoiseReduction) {
        mTvPictureManager.setMpegNoiseReduction(MPEGNoiseReduction);
    }

    @Override
    protected void onStop() {
		if(MainActivity.myMainActivity!=null){
			MainActivity.myMainActivity.downTime=System.currentTimeMillis();
			SharedPreferences preferences=getSharedPreferences("TuferTvMenu", Context.MODE_PRIVATE);
			MainActivity.myMainActivity.isMenuShow=preferences.getBoolean("isMenuShow", false);
			switch (MainActivity.myMainActivity.posion){
				case 0:
					MainActivity.myMainActivity.handler.sendEmptyMessage(MainActivity.UPDATE_GENERAL);
					break;
				case 1:
					MainActivity.myMainActivity.handler.sendEmptyMessage(MainActivity.UPDATE_PICTURE);
					break;
				case 2:
					MainActivity.myMainActivity.handler.sendEmptyMessage(MainActivity.UPDATE_SOUND);
					break;
				case 3:
					//MainActivity.myMainActivity.handler.sendEmptyMessage(MainActivity.UPDATE_CHANNEL);
					break;
				case 4:
					MainActivity.myMainActivity.handler.sendEmptyMessage(MainActivity.UPDATE_NETWORK);
					break;
				case 5:
					MainActivity.myMainActivity.handler.sendEmptyMessage(MainActivity.UPDATE_INTELLIGENCE);
					break;
				case 6:
					MainActivity.myMainActivity.handler.sendEmptyMessage(MainActivity.UPDATE_SYSTEM);
					break;
				case 7:
					MainActivity.myMainActivity.handler.sendEmptyMessage(MainActivity.UPDATE_ABOUT);
					break;
			}
		}
        super.onStop();
    }
}
