package com.yundong.imember.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.R;
import com.yundong.imember.datamanager.FindPsdManager_Email;
import com.yundong.imember.datamanager.FindPsdManager_Mobile;

public class ForgetPsdActivity extends SuperActivity {
	private EditText et_email;
	private Button btn_send;
	private Button btn_left;
	
	private String str_email_or_mobile;
	private int type;
	private String code;
	private String msg_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_forgetpsd);
		super.onCreate(savedInstanceState);
		
		initUI();
	}

	private void initUI() {
		registFinishSubActivity();
		
		btn_left = (Button)findViewById(R.id.btn_header_left);
		et_email = (EditText)findViewById(R.id.forgetpsd_et_email);
		btn_send = (Button)findViewById(R.id.forgetpsd_btn_send);
		
		setUI();
	}

	private void setUI() {
		btn_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btn_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setStr();
				if(notNull()){
					send();
				}
			}
		});
	}
	
	private void setStr(){
		str_email_or_mobile = et_email.getText().toString().trim();
	}
	
	private boolean notNull(){
		if(str_email_or_mobile != null && !"".equals(str_email_or_mobile)){
			if(ImemberApplication.getInstance().isEmail(str_email_or_mobile)){
				type = 0;
			}
			else{
				if(ImemberApplication.getInstance().isMobile(str_email_or_mobile)){
					if(str_email_or_mobile.length() == 11){
						type = 1;
					}
					else{
						Toast.makeText(this, "手机号码必须为11位", Toast.LENGTH_SHORT).show();
						return false;
					}
				}
				else{
					Toast.makeText(this, "邮箱或手机号格式不正确", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
//			else{
//				Toast.makeText(this, "请输入正确的邮箱格式", Toast.LENGTH_SHORT).show();
//				return false;
//			}
		}
		else{
			Toast.makeText(this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	private void send(){
		switch(type){
		case 0:
			new FindPsdManager_Email(this).excute(str_email_or_mobile);
			break;
		case 1:
			Intent intent = new Intent();
			intent.putExtra("mobile", str_email_or_mobile);
			intent.putExtra("canEdit", false);
			intent.putExtra("isRegist", true);
			intent.putExtra("type", 1);
			intent.setClass(ForgetPsdActivity.this, MsgValidateActivity.class);
			startActivityForResult(intent, 5);
			break;
		}
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
				
				if(code != null && !"".equals(code)){
					Intent intent = new Intent();
					intent.putExtra("mobile", str_email_or_mobile);
					intent.putExtra("code", code);
					intent.putExtra("msg_id", msg_id);
					intent.setClass(ForgetPsdActivity.this, ResetPsdActivity.class);
					startActivityForResult(intent, 6);
				}
			}
		}
		else if(requestCode == 6){
			if(resultCode == 5){
				finish();
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
