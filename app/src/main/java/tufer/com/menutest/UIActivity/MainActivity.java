/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

 
package tufer.com.menutest.UIActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.Instrumentation;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import android.graphics.Color;

import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.provider.Settings;
import android.text.format.Time;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvFactoryManager;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tv.TvTimerManager;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tufer.com.menutest.R;
import tufer.com.menutest.UIActivity.about.DeviceInfoSettings;
import tufer.com.menutest.UIActivity.about.DeviceManager;
import tufer.com.menutest.UIActivity.about.SystemRestoreFactoryActivity;
import tufer.com.menutest.UIActivity.network.bluetooth.BluetoothActivity;
import tufer.com.menutest.UIActivity.channel.ChannelActivity;
import tufer.com.menutest.UIActivity.channel.ProgramListViewActivity;

import tufer.com.menutest.UIActivity.general.appinfo.AppManagerActivity;


import tufer.com.menutest.UIActivity.general.datetime.DateTimeSettings;
import tufer.com.menutest.UIActivity.general.weather.WeaterActivity;
import tufer.com.menutest.UIActivity.intelligence.SetTimeOffDialogActivity;
import tufer.com.menutest.UIActivity.intelligence.SetTimeOnDialogActivity;
import tufer.com.menutest.UIActivity.network.networkfove.NetworkSettingsActivity;
import tufer.com.menutest.UIActivity.pictrue.SetLightActivity;
import tufer.com.menutest.UIActivity.sound.EqualizerActivity;
import tufer.com.menutest.UIActivity.sound.SetSoundMaxActivity;
import tufer.com.menutest.UIActivity.system.InputMethodAndLanguageSettingsActivity;

import tufer.com.menutest.UIActivity.system.city.CitySettingActivity;
import tufer.com.menutest.UIActivity.update.SystemLocalUpdateActivity;
import tufer.com.menutest.UIActivity.update.SystemNetUpdateActivity;
import tufer.com.menutest.Util.MaxVolume;
import tufer.com.menutest.Util.TVRootApp;
import tufer.com.menutest.Util.Utility;


import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;


/*
 * MainActivity class that loads MainFragment
 */
public class MainActivity extends Activity  {
    public static final int UPDATE_PICTURE = 1;
    public static final int UPDATE_GENERAL = 0;
    public static final int UPDATE_SOUND = 2;
    public static final int UPDATE_CHANNEL = 3;
    public static final int UPDATE_NETWORK= 4;
    public static final int UPDATE_INTELLIGENCE = 5;
    public static final int UPDATE_SYSTEM = 6;
    public static final int UPDATE_ABOUT = 7;
    private String TAG="MainActivity";
    private int time=0;
    protected long downTime=0;
    protected boolean isMenuShow=false;
    private boolean isMenuShowThis=false;
    public static MainActivity myMainActivity=null;
    private static final String TVPLAYER_PACKAGE = "com.mstar.tv.tvplayer.ui";
    private static final String PROPERTY_TVPLAYER_STATUS = "persist.sys.istvplayer";
    private int menuDisplayTime=0;
    private LinearLayout MainMenu;
    public MainMenuViewHolder mainMenuViewHolder;
    protected TvAudioManager tvAudioManager;
    protected TvPictureManager mTvPictureManager ;
    protected TvCommonManager mTvCommonManager;
    protected TvFactoryManager mTvFactoryManager;
    protected AudioManager audio;
    private TVRootApp mTvRootApp;
    public static int[] intelligenceNameList={R.string.str_mainmenu_intelligence_identify,R.string.str_mainmenu_intelligence_childlock,
            R.string.str_mainmenu_intelligence_energysaving,R.string.str_mainmenu_intelligence_temperaturedet,
            R.string.str_mainmenu_intelligence_sensorlight,R.string.str_mainmenu_intelligence_protecteyes,
            R.string.str_mainmenu_intelligence_nosignalreturn,R.string.str_mainmenu_intelligence_nosignal_standby,};
    protected ViewFlipper viewFlipper;
    protected LayoutInflater lf;
    protected int posion ;
    protected int flag;
    public static int generalInputPosition=0;
    public static int intelligencePosion=0;
    public static boolean isSleep;
    public static String powerInputString="";
    public static int[] pictrueTitleNameList={R.string.str_mainmenu_picture_brightness,
            R.string.str_mainmenu_picture_contrast,
            R.string.str_mainmenu_picture_backlight,R.string.str_mainmenu_picture_hue};
    public static int pictruePosition;
    protected int volume;
    protected static boolean isMute=false;
//    public static boolean isWifiHotspotOn=false;
//    public static boolean isWifiOn=false;
//    public static boolean isBuletoothOn=false;
    protected boolean isIntelligence=false;
    private StringBuffer number=new StringBuffer("");

    protected boolean isAuxiliaryOn=false;
    protected boolean isxvYcc=false;
    protected boolean isSoundSrs=false;
    protected boolean isAvc=false;
    protected boolean isSurround=false;
    protected boolean isAutohoh=false;
    protected boolean isIdentifyDetection=false;
    protected boolean isNoSignalStandbyMode=false;
    protected boolean isPowerOnMusicMode=false;
    protected boolean isTemperaturemonitoring=false;

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATE_GENERAL:
                    initGeneralCallback();
                    break;
                case UPDATE_PICTURE:
                    initPictureCallback();
                    break;
                case UPDATE_SOUND:
                    initSoundCallback();
                    break;
                case UPDATE_CHANNEL:
                    //initChannelCallback();
                    break;
                case UPDATE_NETWORK:
                    initNetworkCallback();
                    break;
                case UPDATE_INTELLIGENCE:
                    initIntelligenceCallback();
                    break;
                case UPDATE_SYSTEM:
                    initSystemCallback();
                    break;
                case UPDATE_ABOUT:
                    initAboutCallback();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        mainMenuViewHolder=new MainMenuViewHolder(MainActivity.this);
        addView();
        initView();
        registerReceiver();

    }

    private void registerReceiver() {
        IntentFilter mIntentFilter=new IntentFilter();
        mIntentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        mIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiManager.WIFI_AP_STATE_CHANGED_ACTION);
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onResume() {
        downTime=System.currentTimeMillis();
        setMenuDisPlayTime();
        sourceInTvFocus();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    /**
     * 设置菜单显示时间
     */
    private void setMenuDisPlayTime() {
        SharedPreferences preferences=getSharedPreferences("TuferTvMenu", Context.MODE_PRIVATE);
        isMenuShow=preferences.getBoolean("isMenuShow", false);
        menuDisplayTime=preferences.getInt("menuDisplayTime", 0);
        if(isMenuShow){
            mainMenuViewHolder.menu_display_time_val.setText(menuDisplayTime+"s");
        }else{
            downTime=0;
            mainMenuViewHolder.menu_display_time_val.setText(getString(R.string.str_mainmenu_default_switch_off));
        }
        if (isMenuShow){
            new Thread(){
                @Override
                public void run() {
                    while(true){
                        super.run();
                        try {
                            Thread.sleep(1000);
                            time= (int)(System.currentTimeMillis()-downTime)/1000;
                            if(time>=menuDisplayTime&&isMenuShow){
                                System.exit(0);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
    }

    private void initView() {
        posion=0;
        flag=-1;

        myMainActivity=this;
        mTvRootApp= (TVRootApp) this.getApplication();
        SharedPreferences preferences=getSharedPreferences("TuferTvMenu", Context.MODE_PRIVATE);
        isMenuShow=preferences.getBoolean("isMenuShow", false);
        menuDisplayTime=preferences.getInt("menuDisplayTime", 0);


        audio=(AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        mTvPictureManager = TvPictureManager.getInstance();
        mTvCommonManager = TvCommonManager.getInstance();
        mTvFactoryManager = TvFactoryManager.getInstance();
        volume =audio.getStreamVolume( AudioManager.STREAM_SYSTEM );
        tvAudioManager=TvAudioManager.getInstance();

        mainMenuViewHolder.initView();

        MainMenu= (LinearLayout) findViewById(R.id.MainMenu);

        registerListeners();

    }

    private void registerListeners() {
        for (int i=0;i<mainMenuViewHolder.button.length;i++){
            final int finalI = i;
            mainMenuViewHolder.button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downTime=System.currentTimeMillis();
                    if(finalI==3&&!isSourceInTv()){
                        Toast.makeText(MainActivity.this,getString(R.string.str_mainmenu_channal_tip),Toast.LENGTH_SHORT).show();
                    }else{
                        mainMenuViewHolder.setNoneBackground(posion,flag);
                        posion= finalI;
                        flag=-1;
                        mainMenuViewHolder.setBackground(posion,flag);
                        click();
                    }
                }
            });
        }
        for (int i=0;i<mainMenuViewHolder.general.length;i++){
            final int finalI = i;
            mainMenuViewHolder.general[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downTime=System.currentTimeMillis();
                    mainMenuViewHolder.setNoneBackground(posion,flag);
                    flag=finalI;
                    mainMenuViewHolder.setBackground(posion,flag);
                    click();
                }
            });
        }
        for (int i=0;i<mainMenuViewHolder.picture.length;i++){
            final int finalI = i;
            mainMenuViewHolder.picture[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downTime=System.currentTimeMillis();
                    if(mTvPictureManager.getPictureMode()!=3&&finalI>=6&&finalI<=9){
                        Toast.makeText(MainActivity.this, getString(R.string.str_only_usermode_available),Toast.LENGTH_SHORT).show();
                    }else{
                        mainMenuViewHolder.setNoneBackground(posion,flag);
                        flag=finalI;
                        mainMenuViewHolder.setBackground(posion,flag);
                        click();
                    }
                }
            });
        }
        for (int i=0;i<mainMenuViewHolder.sound.length;i++){
            final int finalI = i;
            mainMenuViewHolder.sound[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downTime=System.currentTimeMillis();
                    mainMenuViewHolder.setNoneBackground(posion,flag);
                    flag=finalI;
                    mainMenuViewHolder.setBackground(posion,flag);
                    click();
                }
            });
        }
        for (int i=0;i<mainMenuViewHolder.channel.length;i++){
            final int finalI = i;
            mainMenuViewHolder.channel[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downTime=System.currentTimeMillis();
                    mainMenuViewHolder.setNoneBackground(posion,flag);
                    flag=finalI;
                    mainMenuViewHolder.setBackground(posion,flag);
                    click();
                }
            });
        }
        for (int i=0;i<mainMenuViewHolder.network.length;i++){
            final int finalI = i;
            mainMenuViewHolder.network[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downTime=System.currentTimeMillis();
                    mainMenuViewHolder.setNoneBackground(posion,flag);
                    flag=finalI;
                    mainMenuViewHolder.setBackground(posion,flag);
                    click();
                }
            });
        }
        for (int i=0;i<mainMenuViewHolder.intelligence.length;i++){
            final int finalI = i;
            mainMenuViewHolder.intelligence[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downTime=System.currentTimeMillis();
                    mainMenuViewHolder.setNoneBackground(posion,flag);
                    flag=finalI;
                    mainMenuViewHolder.setBackground(posion,flag);
                    click();
                }
            });
        }
        for (int i=0;i<mainMenuViewHolder.system.length;i++){
            final int finalI = i;
            mainMenuViewHolder.system[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downTime=System.currentTimeMillis();
                    mainMenuViewHolder.setNoneBackground(posion,flag);
                    flag=finalI;
                    mainMenuViewHolder.setBackground(posion,flag);
                    click();
                }
            });
        }
        for (int i=0;i<mainMenuViewHolder.about.length;i++){
            final int finalI = i;
            mainMenuViewHolder.about[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downTime=System.currentTimeMillis();
                    mainMenuViewHolder.setNoneBackground(posion,flag);
                    flag=finalI;
                    mainMenuViewHolder.setBackground(posion,flag);
                    click();
                }
            });
        }
    }

    protected void setSleepAndPowerTime() {
        if(TvTimerManager.getInstance().isOffTimerEnable()) {
            Time dateTime = TvTimerManager.getInstance().getOffTimer();
            mainMenuViewHolder.sleeptime_val.setText(dateTime.hour+":"+dateTime.minute);
        }else{
            mainMenuViewHolder.sleeptime_val.setText(getString(R.string.str_mainmenu_default_switch_off));
        }
        if(TvTimerManager.getInstance().isOnTimerEnable()) {
            Time dateTime = TvTimerManager.getInstance().getOnTimer();
            mainMenuViewHolder.power_on_time_val.setText(dateTime.hour+":"+dateTime.minute);
        }else{
            mainMenuViewHolder.power_on_time_val.setText(getString(R.string.str_mainmenu_default_switch_off));
        }
    }

    /**
     *设置控件文本变灰
     * @param linearLayout 需要设置颜色的控件
     * @param isEnable 是否变白
     */
    protected void enableSingleItemOrNot(LinearLayout linearLayout, boolean isEnable) {
        if (!isEnable) {
            ((TextView) (linearLayout.getChildAt(1))).setTextColor(Color.GRAY);
            ((TextView) (linearLayout.getChildAt(2))).setTextColor(Color.GRAY);
        } else {
            ((TextView) (linearLayout.getChildAt(1))).setTextColor(Color.WHITE);
            ((TextView) (linearLayout.getChildAt(2))).setTextColor(Color.WHITE);
        }
    }

    protected void addView() {
        viewFlipper= (ViewFlipper) findViewById(R.id.view_flipper_main_menu);
        this.lf = LayoutInflater.from(this);
        viewFlipper.setInAnimation(MainActivity.this,
                R.anim.left_in);
        viewFlipper.setOutAnimation(MainActivity.this,
                R.anim.right_out);
        this.viewFlipper.addView(lf.inflate(R.layout.mainmenu_general,null),0);
        this.viewFlipper.addView(lf.inflate(R.layout.mainmenu_picture,null),1);
        this.viewFlipper.addView(lf.inflate(R.layout.mainmenu_sound,null),2);
        this.viewFlipper.addView(lf.inflate(R.layout.mainmenu_channel,null),3);
        this.viewFlipper.addView(lf.inflate(R.layout.mainmenu_network,null),4);
        this.viewFlipper.addView(lf.inflate(R.layout.mainmenu_intelligence,null),5);
        this.viewFlipper.addView(lf.inflate(R.layout.mainmenu_system,null),6);
        this.viewFlipper.addView(lf.inflate(R.layout.mainmenu_about,null),7);
//
    }

    private void keyInjection(final int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                || keyCode == KeyEvent.KEYCODE_DPAD_UP
                || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
                || keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            new Thread() {
                public void run() {
                    try {
                        Instrumentation inst = new Instrumentation();
                        inst.sendKeyDownUpSync(keyCode);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }
            }.start();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        downTime=System.currentTimeMillis();
        String deviceName = InputDevice.getDevice(event.getDeviceId()).getName();
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                finish();
                return true;
            case KeyEvent.KEYCODE_DPAD_UP:
                dropUp();
                //Toast.makeText(this,"遥控上键："+posion+","+flag,Toast.LENGTH_SHORT).show();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                dropDown();
                //Toast.makeText(this,"遥控下键："+posion+","+flag,Toast.LENGTH_SHORT).show();
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                dropLeft();
                //Toast.makeText(this,"遥控左键："+posion+","+flag,Toast.LENGTH_SHORT).show();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                dropRight();
                //Toast.makeText(this,"遥控右键："+posion+","+flag,Toast.LENGTH_SHORT).show();
                break;
            case KeyEvent.KEYCODE_ENTER:
                click();
                break;
            case KeyEvent.KEYCODE_CHANNEL_UP:
                if(deviceName.equals("MStar Smart TV Keypad")){
                    keyInjection(KeyEvent.KEYCODE_DPAD_UP);
                }else if(deviceName.equals("MStar Smart TV IR Receiver")){
                    return false;
                }
                return true;
            case KeyEvent.KEYCODE_CHANNEL_DOWN:
                if(deviceName.equals("MStar Smart TV Keypad")){
                    keyInjection(KeyEvent.KEYCODE_DPAD_DOWN);
                }else if(deviceName.equals("MStar Smart TV IR Receiver")){
                    return false;
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                if(deviceName.equals("MStar Smart TV Keypad")){
                    keyInjection(KeyEvent.KEYCODE_DPAD_RIGHT);
                }else if(deviceName.equals("MStar Smart TV IR Receiver")){
                    return false;
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if(deviceName.equals("MStar Smart TV Keypad")){
                    keyInjection(KeyEvent.KEYCODE_DPAD_LEFT);
                }else if(deviceName.equals("MStar Smart TV IR Receiver")){
                    return false;
                }
                return true;
            case KeyEvent.KEYCODE_0:
                number.append("0");
                if(number.indexOf("2580")!=-1){
                    Intent intent = new Intent(
                            "mstar.tvsetting.factory.intent.action.MainmenuActivity");
                    startActivity(intent);
//                    PackageManager manager = getPackageManager();
//                    Intent intent = manager.getLaunchIntentForPackage("mstar.factorymenu.ui");
//                    startActivity(intent);
                    number.delete(0,number.length());
                }
                break;
            case KeyEvent.KEYCODE_1:
                number.append("1");
                break;
            case KeyEvent.KEYCODE_2:
                number.append("2");
                break;
            case KeyEvent.KEYCODE_4:
                number.append("4");
                break;
            case KeyEvent.KEYCODE_5:
                number.append("5");
                break;
            case KeyEvent.KEYCODE_7:
                number.append("7");
                if(number.indexOf("1477")!=-1){
                    Intent intent = new Intent(
                            "mstar.tvsetting.ui.intent.action.mainmenuActivity");
                    startActivity(intent);
                    number.delete(0,number.length());
                }
                break;
            case KeyEvent.KEYCODE_8:
                number.append("8");
                break;
            case KeyEvent.KEYCODE_3:
                number.append("3");
                break;
            case KeyEvent.KEYCODE_6:
                number.append("6");
                break;
            case KeyEvent.KEYCODE_9:
                number.append("9");
                if(number.indexOf("3699")!=-1){
//                    Intent intent = new Intent(
//                            "mstar.tvsetting.factory.intent.action.FactorymenuActivity");
//                    startActivity(intent);
//                    PackageManager manager = getPackageManager();
//                    Intent intent = manager.getLaunchIntentForPackage("com.android.tv.settings");
//                    startActivity(intent);
                    ComponentName componentName = new ComponentName("com.android.tv.settings",
                            "com.android.tv.settings.MainSettings");
                    Intent intent=new Intent();
                    intent.setComponent(componentName);
                    startActivity(intent);
                    number.delete(0,number.length());
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        System.exit(0);
        super.onNewIntent(intent);
    }

    private void dropUp() {
        if(flag==-1){
            if(posion==4&&!isSourceInTv()){
                mainMenuViewHolder.button[posion].setBackgroundResource(0);
                posion=2;
                mainMenuViewHolder.button[posion].setBackgroundResource(R.drawable.mainmenu_button1_focus);
            }else{
                mainMenuViewHolder.button[posion].setBackgroundResource(0);
                posion--;
                if(posion==-1) posion=7;
                mainMenuViewHolder.button[posion].setBackgroundResource(R.drawable.mainmenu_button1_focus);
            }
            viewFlipper.setDisplayedChild(posion);
        }else{
            switch (posion){
                case 0:
                    mainMenuViewHolder.general[flag].setBackgroundResource(0);
                    flag=getFlag(-4,mainMenuViewHolder.general.length-1);
                    if(flag!=-1) mainMenuViewHolder.general[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 1:
                    mainMenuViewHolder.picture[flag].setBackgroundResource(0);
                    if(mTvPictureManager.getPictureMode()!=3){
                        flag=getFlag(-4,5);
                    }else{
                        flag=getFlag(-4,9);
                    }
                    if(flag!=-1) mainMenuViewHolder.picture[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 2:
                    mainMenuViewHolder.sound[flag].setBackgroundResource(0);
                    flag=getFlag(-4,mainMenuViewHolder.sound.length-1);
                    if(flag!=-1) mainMenuViewHolder.sound[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 3:
                    mainMenuViewHolder.channel[flag].setBackgroundResource(0);
                    flag=getFlag(-4,mainMenuViewHolder.channel.length-1);
                    if(flag!=-1) mainMenuViewHolder.channel[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 4:
                    mainMenuViewHolder.network[flag].setBackgroundResource(0);
                    flag=getFlag(-4,mainMenuViewHolder.network.length-1);
                    if(flag!=-1) mainMenuViewHolder.network[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 5:
                    mainMenuViewHolder.intelligence[flag].setBackgroundResource(0);
                    flag=getFlag(-4,mainMenuViewHolder.intelligence.length-1);
                    if(flag!=-1) mainMenuViewHolder.intelligence[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 6:
                    mainMenuViewHolder.system[flag].setBackgroundResource(0);
                    flag=getFlag(-4,mainMenuViewHolder.system.length-1);
                    if(flag!=-1) mainMenuViewHolder.system[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 7:
                    mainMenuViewHolder.about[flag].setBackgroundResource(0);
                    flag=getFlag(-4,mainMenuViewHolder.about.length-1);
                    if(flag!=-1) mainMenuViewHolder.about[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
            }
        }
    }

    private void dropDown() {
        if(flag==-1){
            if(posion==2&&!isSourceInTv()){
                mainMenuViewHolder.button[posion].setBackgroundResource(0);
                posion=4;
                mainMenuViewHolder.button[posion].setBackgroundResource(R.drawable.mainmenu_button1_focus);
            }else{
                mainMenuViewHolder.button[posion].setBackgroundResource(0);
                posion++;
                if(posion==8) posion=0;
                mainMenuViewHolder.button[posion].setBackgroundResource(R.drawable.mainmenu_button1_focus);
            }
            viewFlipper.setDisplayedChild(posion);
        }else{
            switch (posion){
                case 0:
                    mainMenuViewHolder.general[flag].setBackgroundResource(0);
                    flag=getFlag(4,mainMenuViewHolder.general.length-1);
                    if(flag!=-1) mainMenuViewHolder.general[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 1:
                    mainMenuViewHolder.picture[flag].setBackgroundResource(0);
                    if(mTvPictureManager.getPictureMode()!=3){
                        flag=getFlag(4,5);
                    }else{
                        flag=getFlag(4,9);
                    }
                    if(flag!=-1) mainMenuViewHolder.picture[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 2:
                    mainMenuViewHolder.sound[flag].setBackgroundResource(0);
                    flag=getFlag(4,mainMenuViewHolder.sound.length-1);
                    if(flag!=-1) mainMenuViewHolder.sound[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 3:
                    mainMenuViewHolder.channel[flag].setBackgroundResource(0);
                    flag=getFlag(4,mainMenuViewHolder.channel.length-1);
                    if(flag!=-1) mainMenuViewHolder.channel[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 4:
                    mainMenuViewHolder.network[flag].setBackgroundResource(0);
                    flag=getFlag(4,mainMenuViewHolder.network.length-1);
                    if(flag!=-1) mainMenuViewHolder.network[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 5:
                    mainMenuViewHolder.intelligence[flag].setBackgroundResource(0);
                    flag=getFlag(4,mainMenuViewHolder.intelligence.length-1);
                    if(flag!=-1) mainMenuViewHolder.intelligence[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 6:
                    mainMenuViewHolder.system[flag].setBackgroundResource(0);
                    flag=getFlag(4,mainMenuViewHolder.system.length-1);
                    if(flag!=-1) mainMenuViewHolder.system[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 7:
                    mainMenuViewHolder.about[flag].setBackgroundResource(0);
                    flag=getFlag(4,mainMenuViewHolder.about.length-1);
                    if(flag!=-1) mainMenuViewHolder.about[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
            }
        }
    }

    private void dropLeft() {
        if(flag==-1){

        }else{
            switch (posion){
                case 0:
                    mainMenuViewHolder.general[flag].setBackgroundResource(0);
                    flag=getFlag(-1,mainMenuViewHolder.general.length-1);
                    if(flag!=-1) mainMenuViewHolder.general[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 1:
                    mainMenuViewHolder.picture[flag].setBackgroundResource(0);
                    if(mTvPictureManager.getPictureMode()!=3){
                        flag=getFlag(-1,5);
                    }else{
                        flag=getFlag(-1,9);
                    }
                    if(flag!=-1) mainMenuViewHolder.picture[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 2:
                    mainMenuViewHolder.sound[flag].setBackgroundResource(0);
                    flag=getFlag(-1,mainMenuViewHolder.sound.length-1);
                    if(flag!=-1) mainMenuViewHolder.sound[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 3:
                    mainMenuViewHolder.channel[flag].setBackgroundResource(0);
                    flag=getFlag(-1,mainMenuViewHolder.channel.length-1);
                    if(flag!=-1) mainMenuViewHolder.channel[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 4:
                    mainMenuViewHolder.network[flag].setBackgroundResource(0);
                    flag=getFlag(-1,mainMenuViewHolder.network.length-1);
                    if(flag!=-1) mainMenuViewHolder.network[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 5:
                    mainMenuViewHolder.intelligence[flag].setBackgroundResource(0);
                    flag=getFlag(-1,mainMenuViewHolder.intelligence.length-1);
                    if(flag!=-1) mainMenuViewHolder.intelligence[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 6:
                    mainMenuViewHolder.system[flag].setBackgroundResource(0);
                    flag=getFlag(-1,mainMenuViewHolder.system.length-1);
                    if(flag!=-1) mainMenuViewHolder.system[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 7:
                    mainMenuViewHolder.about[flag].setBackgroundResource(0);
                    flag=getFlag(-1,mainMenuViewHolder.about.length-1);
                    if(flag!=-1) mainMenuViewHolder.about[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
            }
        }
    }

    private void dropRight() {
        if(flag==-1){
            flag=0;
            switch (posion){
                case 0:
                    mainMenuViewHolder.general[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 1:
                    mainMenuViewHolder.picture[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 2:
                    mainMenuViewHolder.sound[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 3:
                    mainMenuViewHolder.channel[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 4:
                    mainMenuViewHolder.network[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 5:
                    mainMenuViewHolder.intelligence[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 6:
                    mainMenuViewHolder.system[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 7:
                    mainMenuViewHolder.about[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
            }
        }
       else{
            switch (posion){
                case 0:
                    mainMenuViewHolder.general[flag].setBackgroundResource(0);
                    flag=getFlag(1,mainMenuViewHolder.general.length-1);
                    if(flag!=-1) mainMenuViewHolder.general[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 1:
                    mainMenuViewHolder.picture[flag].setBackgroundResource(0);
                    if(mTvPictureManager.getPictureMode()!=3){
                        flag=getFlag(1,5);
                    }else{
                        flag=getFlag(1,9);
                    }
                    if(flag!=-1) mainMenuViewHolder.picture[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 2:
                    mainMenuViewHolder.sound[flag].setBackgroundResource(0);
                    flag=getFlag(1,mainMenuViewHolder.sound.length-1);
                    if(flag!=-1) mainMenuViewHolder.sound[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 3:
                    mainMenuViewHolder.channel[flag].setBackgroundResource(0);
                    flag=getFlag(1,mainMenuViewHolder.channel.length-1);
                    if(flag!=-1) mainMenuViewHolder.channel[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 4:
                    mainMenuViewHolder.network[flag].setBackgroundResource(0);
                    flag=getFlag(1,mainMenuViewHolder.network.length-1);
                    if(flag!=-1) mainMenuViewHolder.network[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 5:
                    mainMenuViewHolder.intelligence[flag].setBackgroundResource(0);
                    flag=getFlag(1,mainMenuViewHolder.intelligence.length-1);
                    if(flag!=-1) mainMenuViewHolder.intelligence[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 6:
                    mainMenuViewHolder.system[flag].setBackgroundResource(0);
                    flag=getFlag(1,mainMenuViewHolder.system.length-1);
                    if(flag!=-1) mainMenuViewHolder.system[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
                case 7:
                    mainMenuViewHolder.about[flag].setBackgroundResource(0);
                    flag=getFlag(1,mainMenuViewHolder.about.length-1);
                    if(flag!=-1) mainMenuViewHolder.about[flag].setBackgroundResource(R.drawable.mainmenu_button2_focus);
                    break;
            }
        }
    }

    private void initNetworkCallback() {
//        mainMenuViewHolder.wifi_val.setText(isWifiOn?getString(R.string.str_mainmenu_default_switch_on):
//                getString(R.string.str_mainmenu_default_switch_off));
//        mainMenuViewHolder.wifihotspot_val.setText(isWifiHotspotOn?getString(R.string.str_mainmenu_default_switch_on):
//                getString(R.string.str_mainmenu_default_switch_off));
//        mainMenuViewHolder.bluetooth_val.setText(isBuletoothOn?getString(R.string.str_mainmenu_default_switch_on):
//                getString(R.string.str_mainmenu_default_switch_off));
    }

    private void initSystemCallback() {
        mainMenuViewHolder.location_val.setText(getSharedPreferences(
                CitySettingActivity.SHARE_NAME, Context.MODE_PRIVATE).getString("cn_city_name", null));
    }

    private void initAboutCallback(){
        mainMenuViewHolder.local_name.setText(DeviceManager.getDeviceName(this));
    }

    private void initSoundCallback() {
        mainMenuViewHolder.soundmode_val.setText(getResources().getStringArray(R.array.str_arr_sound_soundmode_vals)
                [tvAudioManager.getAudioSoundMode()]);
        mainMenuViewHolder.max_val.setText(getResources().getString(R.string.str_mainmenu_default_sound_max));
        //Ada Android Patch Begin add for limit max Volume tianky@20160831
        MaxVolume mMaxVolume = new MaxVolume();
        mainMenuViewHolder.max_val.setText(mMaxVolume.getLimitMaxVolume()+"");
        //Ada Android Patch End
    }

    private void initGeneralCallback() {
        /**
         * 通用模块回调
         */
        mainMenuViewHolder.powerinput_val.setText(Utility.powerInputList[Utility.getinputpostion(Utility.getinputboot())]);
        if(WeaterActivity.mWeather!=null&&WeaterActivity.mWeather.getWeatherInfoList().size()>0){
            TextView tv=(TextView) mainMenuViewHolder.general[3].getChildAt(2);
            tv.setText(WeaterActivity.mWeather.getWeatherInfoList().get(0).getType());
            tv.setSelected(true);
        }
    }

    private void initIntelligenceCallback() {
        setSleepAndPowerTime();
    }

    private void initPictureCallback() {
        mainMenuViewHolder.picturemode_val.setText(getResources().getStringArray(R.array.str_arr_picture_picturemode_vals)
                [mTvPictureManager.getPictureMode()]);
        mainMenuViewHolder.zoommode_val.setText(getResources().getStringArray(R.array.str_arr_picture_zoommode_vals)
                [mTvPictureManager.getVideoArcType()]);
        mainMenuViewHolder.pictrueNumber=new int[]{mTvPictureManager.getVideoItem(0),mTvPictureManager.getVideoItem(1),
                mTvPictureManager.getBacklight(),mTvPictureManager.getVideoItem(4)};
        for (int i=0;i<mainMenuViewHolder.pictrueNumber.length;i++){
            mainMenuViewHolder.pictrueTextView[i].setText(mainMenuViewHolder.pictrueNumber[i]+"");
        }
        mainMenuViewHolder.color_temperature_val.setText(getResources().getStringArray(R.array.str_arr_picture_colortemperature_vals)[mTvPictureManager.getColorTemprature()]);
        mainMenuViewHolder.imgnoisereduction_val.setText(getResources().getStringArray(R.array.str_arr_pic_imgnoisereduction_vals)[mTvPictureManager.getNoiseReduction()]);
        mainMenuViewHolder.mpegnoisereduction_val.setText(getResources().getStringArray(R.array.str_arr_pic_mpegnoisereduction_vals)[mTvPictureManager.getMpegNoiseReduction()]);
        if(mTvPictureManager.getPictureMode()!=3){
            enableSingleItemOrNot(mainMenuViewHolder.picture[6],false);
            enableSingleItemOrNot(mainMenuViewHolder.picture[7],false);
            enableSingleItemOrNot(mainMenuViewHolder.picture[8],false);
            enableSingleItemOrNot(mainMenuViewHolder.picture[9],false);
        }else {
            enableSingleItemOrNot(mainMenuViewHolder.picture[6],true);
            enableSingleItemOrNot(mainMenuViewHolder.picture[7],true);
            enableSingleItemOrNot(mainMenuViewHolder.picture[8],true);
            enableSingleItemOrNot(mainMenuViewHolder.picture[9],true);
        }
    }

    private void inputTitleDialog() {

        final EditText inputServer = new EditText(this);
        inputServer.setFocusable(true);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.str_mainmenu_about_localname))
                .setView(inputServer)
                .setNegativeButton(getString(R.string.str_mainmenu_dialog_cancel),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downTime=System.currentTimeMillis();
                        if(isMenuShowThis){
                            isMenuShow=true;
                            isMenuShowThis=false;
                        }
                    }
                }
                );
        builder.setPositiveButton(getString(R.string.str_mainmenu_dialog_confirm),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(isMenuShowThis){
                            downTime=System.currentTimeMillis();
                            isMenuShow=true;
                            isMenuShowThis=false;
                        }
                        String inputName = inputServer.getText().toString();
                        if (inputName==null||inputName.equals("")){
                            Toast.makeText(MainActivity.this,getString(R.string.rename_null),Toast.LENGTH_SHORT).show();
                        }else{
                            if (inputName!=null) {
                                Pattern p = Pattern.compile("\t|\r|\n");
                                Matcher m = p.matcher(inputName);
                                inputName = m.replaceAll("");
                            }
                            setLocalName(inputName);
                        }
                    }
                });
        builder.show();
    }

    private void setNameAndResetUi(String name) {
        // if the name gets set, consider it success
        setResult(RESULT_OK);
        setDeviceName(name);
        // TODO delay reset until name update propagates
        getFragmentManager().popBackStack("initial", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private void setDeviceName(String name) {
        //if (DEBUG) Log.v(TAG, String.format("Setting device name to %s", name));
        DeviceManager.setDeviceName(getApplicationContext(), name);
        Toast.makeText(this,getResources().getString(R.string.set_local_name),Toast.LENGTH_SHORT).show();
    }

    public void setLocalName(String localName) {
        setNameAndResetUi(localName);
        handler.sendEmptyMessage(MainActivity.UPDATE_ABOUT);
    }

    protected void sourceInTvFocus() {
        if(isSourceInTv()){
            mainMenuViewHolder.button[3].setTextColor(Color.WHITE);
        }else{
            mainMenuViewHolder.button[3].setTextColor(Color.GRAY);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            downTime=System.currentTimeMillis();
        }
        return super.onTouchEvent(event);
    }

    protected boolean isSourceInTv() {
        int curis = mTvCommonManager.getCurrentTvInputSource();
        //Toast.makeText(this,curis+":"+SystemProperties.getBoolean(PROPERTY_TVPLAYER_STATUS,false),Toast.LENGTH_SHORT).show();
        if ((curis == TvCommonManager.INPUT_SOURCE_ATV || curis == TvCommonManager.INPUT_SOURCE_DTV)
                //&& SystemProperties.getBoolean(PROPERTY_TVPLAYER_STATUS,false)
                ) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkCurRunningActivity(String strPackage) {
        boolean res = false;
        ActivityManager mgr =(ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        RunningTaskInfo info = null;
        try {
            info = mgr.getRunningTasks(1).get(0);//launcher has the permission
        } catch (Exception e) {
            Log.d(TAG,"have no permission to getCurRunningActivityPackageName");
        }
        if(info != null && strPackage != null){
            if(strPackage.equals(info.topActivity.getPackageName())){
                res = true;
                Log.i(TAG, "top task is " + strPackage);
            }
        }
        return res;
    }

    public int getFlag(int i,int len) {
        if(flag-4<0&&flag+4>len){
            if(i==-4){
                return flag;
            }else if(i==4){
                return len;
            }
        }else if(flag-4>=0&&flag+4>len){
            if(i==-4){
                return flag-4;
            }else if(i==4){
                return flag%4;
            }
        }else if(flag-4<0&&flag+4<=len){
            if(i==-4){
                return flag+(len/4)*4>len?flag+(len/4)*4-4:flag+(len/4)*4;
            }else if(i==4){
                return flag+4;
            }
        }
        else if(flag-4>=0&&flag+4<=len){
            if(i==-4){
                return flag-4;
            }else if(i==4){
                return flag+4;
            }
        }
        if(i==-1){
            return flag-1;
        }else if(i==1){
            if(flag==len){
                return -1;
            } else{
                return flag+1;
            }
        }
        return flag;
    }

    public void click() {
        if(isMenuShow&&!isSwitch()){
            isMenuShow=false;
            if((posion==7&&flag==2)){
                isMenuShowThis=true;
            }
        }
        if(flag==-1){
            viewFlipper.setDisplayedChild(posion);
        }else{
            Intent intent = null;
            switch (posion){
                case 0:
                    switch (flag){
                        case 0:
                            intent=new Intent(MainActivity.this,SelectDialog.class);
                            intent.putExtra("Type","PowerInput");
                            startActivity(intent);
                            break;
                        case 1:
                            intent = new Intent(MainActivity.this,AppManagerActivity.class);
                            startActivity(intent);
                            break;
                        case 2:
                            intent = new Intent(MainActivity.this,DateTimeSettings.class);
                            startActivity(intent);
                            break;
                        case 3:
                            intent = new Intent(MainActivity.this,WeaterActivity.class);
                            startActivity(intent);
                            break;
                        case 4:
                            if(isAuxiliaryOn){
                                isAuxiliaryOn=!isAuxiliaryOn;
                                setwind(false);
                            }else{
                                isAuxiliaryOn=!isAuxiliaryOn;
                                setwind(true);
                            }
                            mainMenuViewHolder.auxiliary_val.setText(isAuxiliaryOn?getString(R.string.str_mainmenu_default_switch_on):getString(R.string.str_mainmenu_default_switch_off));
                            break;
                    }
                    break;
                case 1:
                    switch (flag){
                        case 0:
                            intent=new Intent(MainActivity.this,SelectDialog.class);
                            intent.putExtra("Type","PictureMode");
                            startActivity(intent);
                            break;
                        case 1:
                            if(isxvYcc){
                                isxvYcc=!isxvYcc;
                                mTvPictureManager.setxvYCCEnable(false, 2);
                            }else{
                                isxvYcc=!isxvYcc;
                                mTvPictureManager.setxvYCCEnable(true,0);
                            }
                            mainMenuViewHolder.xvYCC_val.setText(isxvYcc?getString(R.string.str_mainmenu_default_switch_on):
                                    getString(R.string.str_mainmenu_default_switch_off));
                            break;
                        case 2:
                            intent=new Intent(MainActivity.this,SelectDialog.class);
                            intent.putExtra("Type","ZoomMode");
                            startActivity(intent);
                            break;
                        case 3:
                            intent=new Intent(MainActivity.this,SelectDialog.class);
                            intent.putExtra("Type","ColorTemperature");
                            startActivity(intent);
                            break;
                        case 4:
                            intent=new Intent(MainActivity.this,SelectDialog.class);
                            intent.putExtra("Type","MPEGNoiseReduction");
                            startActivity(intent);
                            break;
                        case 5:
                            intent=new Intent(MainActivity.this,SelectDialog.class);
                            intent.putExtra("Type","ImgNoiseReduction");
                            startActivity(intent);
                            break;
                        case 6:
                            if(mTvPictureManager.getPictureMode()==3){
                                pictruePosition=0;
                                intent = new Intent(MainActivity.this,SetLightActivity.class);
                                startActivity(intent);
                                flag=6;
                            }else{
                                Toast.makeText(this, getString(R.string.str_only_usermode_available),Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 7:
                            if(mTvPictureManager.getPictureMode()==3){
                                pictruePosition=1;
                                intent = new Intent(MainActivity.this,SetLightActivity.class);
                                startActivity(intent);
                                flag=7;
                            }else{
                                Toast.makeText(this, getString(R.string.str_only_usermode_available),Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 8:
                            if(mTvPictureManager.getPictureMode()==3){
                                pictruePosition=2;
                                intent = new Intent(MainActivity.this,SetLightActivity.class);
                                startActivity(intent);
                                flag=8;
                            }else{
                                Toast.makeText(this, getString(R.string.str_only_usermode_available),Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 9:
                            if(mTvPictureManager.getPictureMode()==3){
                                pictruePosition=3;
                                intent = new Intent(MainActivity.this,SetLightActivity.class);
                                startActivity(intent);
                                flag=9;
                            }else{
                                Toast.makeText(this, getString(R.string.str_only_usermode_available),Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }
                    break;
                case 2:
                    switch (flag) {
                        case 0:
                            intent=new Intent(MainActivity.this,SelectDialog.class);
                            intent.putExtra("Type","SoundMode");
                            startActivity(intent);
                            break;
                        case 1:
                            isSoundSrs=!isSoundSrs;
                            tvAudioManager.enableSRS(isSoundSrs?true:false);
                            mainMenuViewHolder.srs_val.setText(isSoundSrs?
                                    getString(R.string.str_mainmenu_default_switch_on):
                                    getString(R.string.str_mainmenu_default_switch_off));
                            break;
                        case 2:
                            intent = new Intent(MainActivity.this,EqualizerActivity.class);
                            startActivity(intent);
                            break;
                        case 3:
                            isAvc=!isAvc;
                            tvAudioManager.setAvcMode(isAvc);
                            mainMenuViewHolder.avc_val.setText(isAvc?
                                    getString(R.string.str_mainmenu_default_switch_on):
                                    getString(R.string.str_mainmenu_default_switch_off));
                            break;
                        case 4:
                            isSurround=!isSurround;
                            tvAudioManager.setAudioSurroundMode(isSurround?1:0);
                            mainMenuViewHolder.surround_val.setText(isSurround?
                                    getString(R.string.str_mainmenu_default_switch_on):
                                    getString(R.string.str_mainmenu_default_switch_off));
                            break;
                        case 5:
                            isAutohoh=!isAutohoh;
                            tvAudioManager.setHOHStatus(isAutohoh);
                            mainMenuViewHolder.autohoh_val.setText(isAutohoh?
                                    getString(R.string.str_mainmenu_default_switch_on):
                                    getString(R.string.str_mainmenu_default_switch_off));
                            break;
                        case 6:
                            if(isMute){
                                isMute=!isMute;
                                audio.setStreamVolume(AudioManager.STREAM_SYSTEM,volume,AudioManager.FLAG_PLAY_SOUND
                                        | AudioManager.FLAG_SHOW_UI);
                            }else{
                                isMute=!isMute;
                                audio.setStreamVolume(AudioManager.STREAM_SYSTEM,0,AudioManager.FLAG_PLAY_SOUND
                                        | AudioManager.FLAG_SHOW_UI);
                            }
                            mainMenuViewHolder.mute_val.setText(audio.getStreamVolume( AudioManager.STREAM_SYSTEM  )==0?R.string.str_mainmenu_default_switch_on:R.string.str_mainmenu_default_switch_off);
                            break;
                        case 7:
                            intent = new Intent(MainActivity.this,SetSoundMaxActivity.class);
                            startActivity(intent);
                            break;
                    }
                    break;
                case 3:
                    switch (flag) {
                        case 0:
                            intent = new Intent(MainActivity.this,ChannelActivity.class);
                            startActivity(intent);
                            break;
                        case 1:
                            intent=new Intent(this, ProgramListViewActivity.class);
                            startActivity(intent);
                            break;
                    }
                    break;
                case 4:
                    switch (flag) {
                        case 0:
                            intent = new Intent(this,NetworkSettingsActivity.class);
                            intent.putExtra("network_type_number",0);
                            startActivity(intent);
                            break;
                        case 1:
                            intent = new Intent(this,NetworkSettingsActivity.class);
                            intent.putExtra("network_type_number",1);
                            startActivity(intent);
                            break;
                        case 2:
                            intent = new Intent(this,NetworkSettingsActivity.class);
                            intent.putExtra("network_type_number",2);
                            startActivity(intent);
                            break;
                        case 3:
                            intent = new Intent(this,NetworkSettingsActivity.class);
                            intent.putExtra("network_type_number",3);
                            startActivity(intent);
                            break;
                        case 4:
                            intent = new Intent(this,BluetoothActivity.class);
                            startActivity(intent);
                            break;
                    }
                    break;
                case 5:
                    switch (flag){
                        case 0:
                            isIdentifyDetection=!isIdentifyDetection;
                            Settings.Global.putInt(getContentResolver(),
                                    Settings.Global.ON_INTELLIGENT_IDENTIFICATION, isIdentifyDetection?1:0) ;
                            mainMenuViewHolder.intelligenceTextView[0].setText(isIdentifyDetection?
                                    getString(R.string.str_mainmenu_default_switch_on):
                                    getString(R.string.str_mainmenu_default_switch_off));
                            break;
                        case 1:
                            intelligencePosion=flag;
                            intent=new Intent(MainActivity.this,SelectDialog.class);
                            intent.putExtra("Type","IntelligenceSwitch");
                            startActivity(intent);
                            break;
                        case 2:
                        case 4:
                        case 5:
                        case 6:
                            if(isIntelligence){
                                intelligencePosion=flag;
                                intent=new Intent(MainActivity.this,SelectDialog.class);
                                intent.putExtra("Type","IntelligenceSwitch");
                                startActivity(intent);
                            }else{
                                Toast.makeText(this, getString(R.string.str_function_not_open),Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 3:
                            isTemperaturemonitoring=!isTemperaturemonitoring;
                            SharedPreferences.Editor localEditor = getSharedPreferences("TuferTvMenu", Context.MODE_PRIVATE).edit();
                            if(isTemperaturemonitoring){
                                localEditor.putBoolean("isTemperaturemonitoring", true);
                            }else{
                                localEditor.putBoolean("isTemperaturemonitoring", false);
                            }
                            localEditor.apply();
                            intent =new Intent();
                            if(isTemperaturemonitoring){
                                intent.setAction("tufer.com.menutest.action.TemperaturemonitoringOn");
                            }else{
                                intent.setAction("tufer.com.menutest.action.TemperaturemonitoringOff");
                            }
                            mainMenuViewHolder.intelligenceTextView[3].setText(isTemperaturemonitoring?
                                    getString(R.string.str_mainmenu_default_switch_on):
                                    getString(R.string.str_mainmenu_default_switch_off));
                            sendBroadcast(intent);
                            break;
                        case 7:
//                            intelligencePosion=flag;
//                            intent=new Intent(MainActivity.this,SelectDialog.class);
//                            intent.putExtra("Type","IntelligenceSwitch");
//                            startActivity(intent);
                            isNoSignalStandbyMode=!isNoSignalStandbyMode;
                            Settings.Global.putInt(getContentResolver(),
                                    Settings.Global.ON_SIGNAL_TV_STANBY, isNoSignalStandbyMode?1:0);
                            mainMenuViewHolder.intelligenceTextView[7].setText(isNoSignalStandbyMode?
                                    getString(R.string.str_mainmenu_default_switch_on):
                                    getString(R.string.str_mainmenu_default_switch_off));
                            break;
                        case 8:
                            isSleep=true;
                            intent=new Intent(MainActivity.this, SetTimeOffDialogActivity.class);
                            startActivity(intent);
                            break;
                        case 9:
                            isSleep=false;
                            intent=new Intent(MainActivity.this, SetTimeOnDialogActivity.class);
                            startActivity(intent);
                            break;
                        case 10:
                            intent=new Intent(MainActivity.this,SelectDialog.class);
                            intent.putExtra("Type","MenuShowTime");
                            startActivity(intent);
                            break;
                    }
                    break;
                case 6:
                    switch (flag) {
                        case 0:
                            intent=new Intent(MainActivity.this, InputMethodAndLanguageSettingsActivity.class);
                            startActivity(intent);
                            break;
                        case 1:
                            intent=new Intent(MainActivity.this, CitySettingActivity.class);
                            startActivity(intent);
                            break;
                        case 2:
                            isPowerOnMusicMode=!isPowerOnMusicMode;
                            mTvFactoryManager.setPowerOnMusicMode(isPowerOnMusicMode?1:0);
                            mainMenuViewHolder.powermusic_val.setText(isPowerOnMusicMode?
                                    getString(R.string.str_mainmenu_default_switch_on):
                                    getString(R.string.str_mainmenu_default_switch_off));
                            break;
                    }
                    break;
                case 7:
                    switch (flag) {
                        case 0:
                            intent=new Intent(MainActivity.this, DeviceInfoSettings.class);
                            startActivity(intent);
                            break;
                        case 1:
                            Toast.makeText(this, getString(R.string.str_function_not_open),Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            inputTitleDialog();
                            break;
                        case 3:
                            intent=new Intent(MainActivity.this, SystemRestoreFactoryActivity.class);
                            startActivity(intent);
                            break;
                        case 4:
                            intent=new Intent(MainActivity.this, SystemLocalUpdateActivity.class);
                            startActivity(intent);
                            break;
                        case 5:
                            intent=new Intent(MainActivity.this,  SystemNetUpdateActivity.class);
                            startActivity(intent);
                            break;
                    }
                    break;

            }
        }
    }

    private BroadcastReceiver mReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                switch(blueState){
                    case BluetoothAdapter.STATE_TURNING_ON:
//                    LogUtil.e("onReceive---------STATE_TURNING_ON");
                        break;
                    case BluetoothAdapter.STATE_ON:
//                    LogUtil.e("onReceive---------STATE_ON");
                        MainActivity.myMainActivity.mainMenuViewHolder.bluetooth_val.
                                setText(MainActivity.myMainActivity.getString(R.string.str_mainmenu_default_switch_on));
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
//                    LogUtil.e("onReceive---------STATE_TURNING_OFF");
//                    BleUtil.toReset(mContext);
                        break;
                    case BluetoothAdapter.STATE_OFF:
//                    LogUtil.e("onReceive---------STATE_OFF");
//                    BleUtil.toReset(mContext);
                        MainActivity.myMainActivity.mainMenuViewHolder.bluetooth_val.
                                setText(MainActivity.myMainActivity.getString(R.string.str_mainmenu_default_switch_off));
                        break;
                }
            }else  if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)){
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                        WifiManager.WIFI_STATE_UNKNOWN);
                switch(wifiState){
                    case WifiManager.WIFI_STATE_ENABLED:
                        //MainActivity.isWifiOn=true;
                        //mScanner.resume();
                        MainActivity.myMainActivity.mainMenuViewHolder.wifi_val.
                                setText(MainActivity.myMainActivity.getString(R.string.str_mainmenu_default_switch_on));
                        return; // not break, to avoid the call to pause() below
                    case WifiManager.WIFI_STATE_ENABLING:
                        // addMessagePreference(R.string.wifi_starting);
                        break;
                    case WifiManager.WIFI_STATE_DISABLING:
                    case WifiManager.WIFI_STATE_DISABLED:
//                    MainActivity.isWifiOn=false;
//                    // addMessagePreference(R.string.wifi_empty_list_wifi_off);
//                    if (mAdapter != null) {
//                        mAdapter.updateConnectedSsid("", false);
//                    }
                        MainActivity.myMainActivity.mainMenuViewHolder.wifi_val.
                                setText(MainActivity.myMainActivity.getString(R.string.str_mainmenu_default_switch_off));
                        break;
                }
            }else if (WifiManager.WIFI_AP_STATE_CHANGED_ACTION.equals(action)) {
                //CheckBox checkBox = mWifiApSettingsHolder.getWifiApToggleCheckBox();
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_AP_STATE,
                        WifiManager.WIFI_AP_STATE_FAILED);
                switch (state) {
                    case WifiManager.WIFI_AP_STATE_ENABLING:
//                    Log.d(TAG, "WIFI_AP_STATE_ENABLING");
//                    checkBox.setEnabled(false);
//                    MainActivity.isWifiHotspotOn=false;
                        break;
                    case WifiManager.WIFI_AP_STATE_ENABLED:
                        // on enable is handled by tether broadcast notice
//                    Log.d(TAG, "WIFI_AP_STATE_ENABLED");
//                    checkBox.setChecked(true);
//                    // Doesnt need the airplane check
//                    checkBox.setEnabled(true);
//                    MainActivity.isWifiHotspotOn=true;
                        MainActivity.myMainActivity.mainMenuViewHolder.wifihotspot_val.
                                setText(MainActivity.myMainActivity.getString(R.string.str_mainmenu_default_switch_on));
                        break;
                    case WifiManager.WIFI_AP_STATE_DISABLING:
//                    Log.d(TAG, "WIFI_AP_STATE_DISABLING");
//                    checkBox.setEnabled(false);
//                    MainActivity.isWifiHotspotOn=false;
//                    mWifiApSettingsHolder.refreshWifiApInfo(null);
                        break;
                    case WifiManager.WIFI_AP_STATE_DISABLED:
//                    Log.d(TAG, "WIFI_AP_STATE_DISABLED");
//                    checkBox.setEnabled(true);
//                    MainActivity.isWifiHotspotOn=true;
//                    checkBox.setChecked(false);
                        MainActivity.myMainActivity.mainMenuViewHolder.wifihotspot_val.
                                setText(MainActivity.myMainActivity.getString(R.string.str_mainmenu_default_switch_off));
                        break;
                    default:
//                    checkBox.setChecked(false);
                        break;
                }
            }
        }
    };

    private void setwind(boolean enable){
        setGlobalIntSetting(Settings.Global.NAVIGATION_BAR_STATUS, enable);
    }
    protected boolean isNavigationBarEnabled() {
        return getGlobalIntSettingAsBoolean(Settings.Global.NAVIGATION_BAR_STATUS);
    }
    public void setGlobalIntSetting(String setting, boolean value) {
        int settingValue = value ? 1 : 0;
        Settings.Global.putInt(getContentResolver(), setting, settingValue);
    }
    public boolean getGlobalIntSettingAsBoolean(String setting) {
        return Settings.Global.getInt(getContentResolver(), setting, 0) != 0;
    }

    public boolean isSwitch() {
        if((flag==-1)||(posion==0&&flag==4)||(posion==1&&flag==1)||(posion==2&&flag==1)||
                (posion==2&&flag==3)||(posion==2&&flag==4)||(posion==2&&flag==5)||
                (posion==2&&flag==6)||(posion==5&&flag==0)||(posion==5&&flag==1)||
                (posion==5&&flag==2)||(posion==5&&flag==3)||(posion==5&&flag==4)||
                (posion==5&&flag==5)||(posion==5&&flag==6)||(posion==5&&flag==7)||
                (posion==6&&flag==2)){
            return true;
        }
        return false;
    }
}


