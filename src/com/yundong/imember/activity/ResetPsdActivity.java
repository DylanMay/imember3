package com.yundong.imember.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.R;
import com.yundong.imember.datamanager.FindPsdManager_Email;
import com.yundong.imember.datamanager.FindPsdManager_Mobile;

public class ResetPsdActivity extends SuperActivity {
	private EditText et_psd;
	private Button btn_reset;
	private Button btn_left;
	private ImageView iv_key;
	private boolean isOpen=true;
	
	private String str_newpsd;
	private String code;
	private String msg_id;
	private String mobile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_resetpsd);
		super.onCreate(savedInstanceState);
		
		getIntentData();
		initUI();
	}
	
	private void getIntentData(){
		Intent intent = getIntent();
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				mobile = bundle.getString("mobile");
				code = bundle.getString("code");
				msg_id = bundle.getString("msg_id");
			}
		}
	}

	private void initUI() {
		registFinishSubActivity();
		
		btn_left = (Button)findViewById(R.id.btn_header_left);
		et_psd = (EditText)findViewById(R.id.password);
		btn_reset = (Button)findViewById(R.id.btn_resetpsd);
		iv_key = (ImageView)findViewById(R.id.key);
		
		setUI();
	}

	private void setUI() {
		btn_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btn_reset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setStr();
				if(notNull()){
					send();
				}
			}
		});
		iv_key.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isOpen = (isOpen == true) ? false : true;
				if(isOpen){
					iv_key.setBackgroundResource(R.drawable.key_on);
					et_psd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					et_psd.setSelection(et_psd.getText().toString().length());
				}
				else{
					iv_key.setBackgroundResource(R.drawable.key_off);
					et_psd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					et_psd.setSelection(et_psd.getText().toString().length());
				}
			}
		});
	}
	
	private void setStr(){
		str_newpsd = et_psd.getText().toString().trim();
	}
	
	private boolean notNull(){
		if(str_newpsd != null && !"".equals(str_newpsd)){
			if(str_newpsd.length() >= 6){
				
			}
			else{
				Toast.makeText(this, "密码至少6位", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		else{
			Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	private void send(){
		new FindPsdManager_Mobile(this).excute(code, msg_id, str_newpsd, mobile);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 5){
			if(resultCode == 5){
				Bundle bundle = data.getExtras();
				code = bundle.getString("code");
				msg_id = bundle.getString("msg_id");
			}
		}
	}

	@Override
	protected void setFooterClickBg() {
		// TODO Auto-generated method stub
		switch(ImemberApplication.getInstance().getIndex_footer()){
		case ImemberApplication.FOOTER_ID_STORE:
			layout_footer_store.setBackgroundResource(R.drawable.footer_over_bg);
			iv_footer_store.setBackgroundResource(R.drawable.footer_store_click);
			break;
		case ImemberApplication.FOOTER_ID_MYCARD:
			layout_footer_mycard.setBackgroundResource(R.drawable.footer_over_bg);
			iv_footer_mycard.setBackgroundResource(R.drawable.footer_mycard_click);
			break;
		case ImemberApplication.FOOTER_ID_REWARD:
			layout_footer_reward.setBackgroundResource(R.drawable.footer_over_bg);
			iv_footer_reward.setBackgroundResource(R.drawable.footer_myreward_click);
			break;
		}
	}

//	@Override
//	protected void setFooterListener() {
//		// TODO Auto-generated method stub
//		switch(ImemberApplication.getInstance().getIndex_footer()){
//		case ImemberApplication.FOOTER_ID_STORE:
//			layout_footer_mycard.setOnClickListener(new FooterClickListener());
//			layout_footer_scan.setOnClickListener(new FooterClickListener());
//			layout_footer_reward.setOnClickListener(new FooterClickListener());
//			layout_footer_setting.setOnClickListener(new FooterClickListener());
//			break;
//		case ImemberApplication.FOOTER_ID_MYCARD:
//			layout_footer_store.setOnClickListener(new FooterClickListener());
//			layout_footer_scan.setOnClickListener(new FooterClickListener());
//			layout_footer_reward.setOnClickListener(new FooterClickListener());
//			layout_footer_setting.setOnClickListener(new FooterClickListener());
//			break;
//		case ImemberApplication.FOOTER_ID_REWARD:
//			layout_footer_store.setOnClickListener(new FooterClickListener());
//			layout_footer_mycard.setOnClickListener(new FooterClickListener());
//			layout_footer_scan.setOnClickListener(new FooterClickListener());
//			layout_footer_setting.setOnClickListener(new FooterClickListener());
//			break;
//		}
//	}
}
