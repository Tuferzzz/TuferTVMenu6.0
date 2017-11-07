package tufer.com.menutest.UIActivity.sound;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tv.TvPictureManager;

import tufer.com.menutest.R;
import tufer.com.menutest.UIActivity.MainActivity;
import tufer.com.menutest.UIActivity.component.SeekBarButton;
import tufer.com.menutest.Util.MaxVolume;


/**
 * Created by Administrator on 2017/7/7 0007.
 */

public class SetSoundMaxActivity extends Activity {
    private static final String TAG = "SetSoundMaxActivity";
    TextView titleName, name;
    SeekBarButton seekBarButton;
    int number;
    private static final String KEY_MAXVOL = "audio_settings_max_volume";
    private MaxVolume mMaxVolume;
    int mInitMaxVolValue;
    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu_picture_setlight);
        initView();


    }

    private void initView() {

        //position=MainActivity.pictruePosition;
        seekBarButton = new SeekBarButton(this, R.id.set_light, 1, true) {
            public void doUpdate() {
                mInitMaxVolValue=seekBarButton.getProgress();
            }
        };
        //设置初始值
        //Ada Android Patch Begin add for limit max Volume tianky@20160831
        mMaxVolume = new MaxVolume();
        mInitMaxVolValue = mMaxVolume.getLimitMaxVolume();
        //findPreference(KEY_MAXVOL).setSummary(String.valueOf(mInitMaxVolValue));
        //Ada Android Patch End
        seekBarButton.setProgress((short) mInitMaxVolValue);
        titleName = (TextView) findViewById(R.id.title_textview);
        name = (TextView) findViewById(R.id.name);
        titleName.setText(getString(R.string.str_mainmenu_sound_max_set));
        name.setText(getString(R.string.str_mainmenu_sound_max));
        audioManager=(AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                case KeyEvent.KEYCODE_ENTER:

                    finish();
                    break;
            }

        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onStop() {
        mMaxVolume.setLimitMaxVolume(this,mInitMaxVolValue);
        Log.d(TAG,audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM)+"");
        audioManager.setStreamMaxVolume(audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM));
        Log.d(TAG,audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM)+"");
        Log.d(TAG,audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM)+"");

//        if(audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM)>audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM)){
//            audioManager.adjustStreamVolume(audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM),0,AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
//        }
//        Log.d(TAG,audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM)+"");
//        Log.d(TAG,audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM)+"");
        MainActivity.myMainActivity.handler.sendEmptyMessage(MainActivity.UPDATE_SOUND);
        super.onStop();
    }
}
