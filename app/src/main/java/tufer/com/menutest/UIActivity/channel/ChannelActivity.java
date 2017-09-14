package tufer.com.menutest.UIActivity.channel;

import android.app.Activity;
import android.os.Bundle;

import tufer.com.menutest.R;
import tufer.com.menutest.UIActivity.MainActivity;
import tufer.com.menutest.UIActivity.holder.ChannelViewHolder;

/**
 * Created by Administrator on 2017/7/29 0029.
 */

public class ChannelActivity extends Activity {
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
        mChannelViewHolder=new ChannelViewHolder(this);
        mChannelViewHolder.findViews();
        mChannelViewHolder.updateUi();
    }
    @Override
    protected void onStop() {
        MainActivity.myMainActivity.handler.sendEmptyMessage(MainActivity.UPDATE_CHANNEL);
        super.onStop();
    }
}
