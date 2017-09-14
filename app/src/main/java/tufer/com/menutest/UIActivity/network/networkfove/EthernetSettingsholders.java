package tufer.com.menutest.UIActivity.network.networkfove;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import tufer.com.menutest.R;


public class EthernetSettingsholders {
	private NetworkSettingsActivity mNetworkSettingActivity;

	private boolean DeviceFlag; //muti ether device flag is true

	public boolean isDeviceFlag() {
		return DeviceFlag;
	}
	

	private LinearLayout mEthernetConfigLayouts;

	 private CheckBox misethernetChboxs;

	 private RelativeLayout mIpsetingLyout;
	 private CheckBox mIpsetingchboxs;

	 private LinearLayout mipsettingsLlyout;

	 private  RelativeLayout mDHCPlyouts;
	 private CheckBox mDHCPchboxs;

	 private  RelativeLayout mStaticLyouts;
	 private CheckBox mstaticipchboxs;

	 private LinearLayout mipstaticLlouyts;

	 private EditText  mipsttingset;

	 private EditText mziwangeds;

	 private EditText mgeteawys;

	 private EditText mDnsoneeds;

	 private EditText mDnstwoeds;

	 private Button msavebts;

	 private Button mstopts;
	 
	public   EthernetSettingsholders(NetworkSettingsActivity networkSettingActivity) {
		this.mNetworkSettingActivity = networkSettingActivity;

		findViews();
	 setListeners();
	}

//	public EthernetSettingsholders(NetworkSettingsActivity networkSettingActivity,boolean flag){
//		this.mNetworkSettingActivity = networkSettingActivity;
//		this.DeviceFlag = flag;
//		Log.d("yesuo", "Device flag is"+DeviceFlag);
//			findViews();
//		setListeners();
//	}

	private void findViews() {

		 mEthernetConfigLayouts=(LinearLayout) getViewById(R.id.ethernet_setting_lls);
	
		misethernetChboxs=(CheckBox) getViewById(R.id.ethernet_dev_switch_checkboxs);
		 
		mIpsetingLyout=(RelativeLayout) getViewById(R.id.ethernet_switch_rls);
		mIpsetingchboxs=(CheckBox) getViewById(R.id.ethernet_switch_checkboxs);

		 mipsettingsLlyout=(LinearLayout) getViewById(R.id.ethernet_toggle_layouts);
		 if(mIpsetingchboxs.isChecked()){
			 mipsettingsLlyout.setVisibility(View.VISIBLE);
		 }else{
			 mipsettingsLlyout.setVisibility(View.GONE);
		 }
		 mDHCPlyouts=(RelativeLayout) getViewById(R.id.ethernet_auto_ip_layouts);

		mDHCPchboxs=(CheckBox) getViewById(R.id.ethernet_auto_ips);
	
		mStaticLyouts=(RelativeLayout) getViewById(R.id.ethernet_ipv6_layouts);
		mstaticipchboxs=(CheckBox) getViewById(R.id.ethernet_ipv6s);
	
		mipstaticLlouyts=(LinearLayout) getViewById(R.id.ethernet_ip_lls);


		 mipsttingset=(EditText) getViewById(R.id.ethernet_v6_ips);
		 
		mziwangeds=(EditText) getViewById(R.id.ethernet_v6_subnets);

		 mgeteawys=(EditText) getViewById(R.id.ethernet_v6_gateways);

		 mDnsoneeds=(EditText) getViewById(R.id.ethernet_v6_firsts);

		 mDnstwoeds=(EditText) getViewById(R.id.ethernet_v6_seconds);

		 msavebts=(Button) getViewById(R.id.ethernet_ip_save_btns);

		mstopts=(Button) getViewById(R.id.ethernet_ip_cancel_btns);
	}
	   private void setListeners() {
		   mIpsetingchboxs.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if(arg1){
					mIpsetingLyout.setBackgroundResource(R.drawable.set_button);
				}else{
					mIpsetingLyout.setBackgroundResource(R.drawable.one_px);
				}
			}
		});
		   mIpsetingchboxs.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			if(arg1){
				mipsettingsLlyout.setVisibility(View.VISIBLE);
			}else{
				mipsettingsLlyout.setVisibility(View.GONE);
			}
				
			}
		});
		   mIpsetingchboxs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(mIpsetingchboxs.isChecked()){
					mIpsetingchboxs.setChecked(true);
					mipsettingsLlyout.setVisibility(View.VISIBLE);
				}else{
				
					mIpsetingchboxs.setChecked(false);
					mipsettingsLlyout.setVisibility(View.GONE);
				}
			}
		});
		   mDHCPchboxs.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if(arg1){
					mDHCPlyouts.setBackgroundResource(R.drawable.set_button);
				}else{
					mDHCPlyouts.setBackgroundResource(R.drawable.one_px);
				}
				
			}
		});
		mDHCPchboxs.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(arg1){
					mstaticipchboxs.setChecked(false);
					setV4EditTextWritable(false);
				}else{
					mstaticipchboxs.setChecked(true);
					setV4EditTextWritable(true);
				}
			}
		});
		mDHCPchboxs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			if(mDHCPchboxs.isChecked()){
				mDHCPchboxs.setChecked(true);

			}else{

				mDHCPchboxs.setChecked(false);
						}
				
			}
		});
		mstaticipchboxs.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(arg1){
				mDHCPchboxs.setChecked(false);
				setV4EditTextWritable(true);
				}else{
				mDHCPchboxs.setChecked(true);
				setV4EditTextWritable(false);
				}
			}
		});
		mstaticipchboxs.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if(arg1){
					mStaticLyouts.setBackgroundResource(R.drawable.set_button);
				}else{
					mStaticLyouts.setBackgroundResource(R.drawable.one_px);
				}
				
			}
		});
		mstaticipchboxs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(mstaticipchboxs.isChecked()){
				
				mstaticipchboxs.setChecked(true);
				}else{
					mstaticipchboxs.setChecked(false);	
				}
			}
		});
		mipsttingset.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if (view instanceof EditText) {
                    EditText editText = (EditText) view;
                    if (hasFocus && editText.isEnabled()) {
                        editText.selectAll();
                    }
                }
				
			}
		});
		mziwangeds.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				  if (view instanceof EditText) {
	                    EditText editText = (EditText) view;
	                    if (hasFocus && editText.isEnabled()) {
	                        editText.selectAll();
	                    }
	                }
				
			}
		});
		mgeteawys.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if (view instanceof EditText) {
					EditText editText = (EditText) view;
					if (hasFocus && editText.isEnabled()) {
						editText.selectAll();
					}
				}
				
			}
		});
		mDnsoneeds.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if (view instanceof EditText) {
					EditText editText = (EditText) view;
					if (hasFocus && editText.isEnabled()) {
						editText.selectAll();
					}
				}
				
			}
		});
		mDnstwoeds.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if (view instanceof EditText) {
					EditText editText = (EditText) view;
					if (hasFocus && editText.isEnabled()) {
						editText.selectAll();
					}
				}
				
			}
		});
	   }
	private View getViewById(int id) {
        return mNetworkSettingActivity.findViewById(id);
    }
	public CheckBox getMisethernetChboxs() {
		return misethernetChboxs;
	}

	public void setMisethernetChboxs(CheckBox misethernetChboxs) {
		this.misethernetChboxs = misethernetChboxs;
	}

	public CheckBox getmIpsetingchboxs() {
		return mIpsetingchboxs;
	}

	public void setmIpsetingchboxs(CheckBox mIpsetingchboxs) {
		this.mIpsetingchboxs = mIpsetingchboxs;
	}

	public CheckBox getmDHCPchboxs() {
		return mDHCPchboxs;
	}

	public void setmDHCPchboxs(CheckBox mDHCPchboxs) {
		this.mDHCPchboxs = mDHCPchboxs;
	}

	public CheckBox getMstaticipchboxs() {
		return mstaticipchboxs;
	}

	public void setMstaticipchboxs(CheckBox mstaticipchboxs) {
		this.mstaticipchboxs = mstaticipchboxs;
	}

	public String getMipsttingset() {
		return mipsttingset.getText().toString().trim();
	}

	public String getMziwangeds() {
		return mziwangeds.getText().toString().trim();
	}

	public String getMgeteawys() {
		return mgeteawys.getText().toString().trim();
	}

	public String getmDnsoneeds() {
		return mDnsoneeds.getText().toString().trim();
	}

	public String getmDnstwoeds() {
		return mDnstwoeds.getText().toString().trim();
	}
	public  void Dhsettext(String ip, String netmask, String gateway, String dns1,
            String dns2){
		if(ip!=null){
			mipsttingset.setText(ip);
		}
		if(netmask!=null){
			mziwangeds.setText(netmask);
		}
		if(gateway!=null){
			mgeteawys.setText(gateway);
		}
		if(dns1!=null){
			mDnsoneeds.setText(dns1);
		}
		if(dns2!=null){
			mDnstwoeds.setText(dns2);
		}
		
	}
	public void setV4EditTextWritable(boolean isEnable) {
        mipsttingset.setEnabled(isEnable);
        mziwangeds.setEnabled(isEnable);
        mgeteawys.setEnabled(isEnable);
        mDnsoneeds.setEnabled(isEnable);
        mDnstwoeds.setEnabled(isEnable);
    }
	   public void setEthernetVisible(boolean visible) {
	        if (visible) {
	            mEthernetConfigLayouts.setVisibility(View.VISIBLE);
	        } else {
	            mEthernetConfigLayouts.setVisibility(View.GONE);
	        }
	    }
}
