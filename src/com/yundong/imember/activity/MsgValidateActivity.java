package com.yundong.imember.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.R;
import com.yundong.imember.datamanager.GetValidateManager;
import com.yundong.imember.datamanager.ReferValidateManager;
import com.yundong.imember.utils.InputUtil;
import com.yundong.imember.utils.MyCount;

public class MsgValidateActivity extends SuperActivity {
	/**
	 * 注册界面相关
	 */
//	private ResizeLayout layout_login;
	private Button btn_getvalidate;
	private Button btn_refervalidate;
	private Button btn_back;
	private EditText et_username_login;
	private EditText et_validate;
	private TextView tv_mobile;
	
	private boolean isRegist=false;
	private boolean canEdit=true;
	private int type;
	private String mobile;
	private String code;
//	private String msg_id;
	
	private ReferValidateManager referValidateManager;
	private GetValidateManager getValidateManager;
	
	private ImageView iv_sign;
	private InputUtil inputUtil;
	private MyCount myCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getIntentData();
		
		if(isRegist){
			setContentView(R.layout.activity_refervalidate);
		}
		else{
			setContentView(R.layout.activity_msgvalidate);
		}
		super.onCreate(savedInstanceState);
		
		if(isRegist){
			initUI_refer();
			getValidate();
			if(myCount != null){
				myCount.start();
				btn_getvalidate.setClickable(false);
			}
		}
		else{
			initUI_msg();
		}
		setUI();
		inputUtil = new InputUtil(iv_sign, layout_footer_scan, layout_footer);
		inputUtil.start();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		inputUtil.stop();
		super.onDestroy();
	}
	
	private void getIntentData(){
		Intent intent = getIntent();
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				type = bundle.getInt("type");
				mobile = bundle.getString("mobile");
				canEdit = bundle.getBoolean("canEdit");
				isRegist = bundle.getBoolean("isRegist");
			}
		}
	}
	
	private void initUI_refer(){
		registFinishSubActivity();
		
		referValidateManager = new ReferValidateManager(this);
		getValidateManager = new GetValidateManager(this);
		
		tv_mobile = (TextView)findViewById(R.id.tv_mobile);
		btn_getvalidate = (Button)findViewById(R.id.login_getvalidate);
		btn_refervalidate = (Button)findViewById(R.id.login_refervalidate);
		et_validate = (EditText)findViewById(R.id.login_validate);
		btn_back = (Button)findViewById(R.id.btn_header_left);
		iv_sign = (ImageView)findViewById(R.id.sign);
		
		myCount = new MyCount(120000, 1000, btn_getvalidate);
		
		if(mobile != null && !"".equals(mobile)){
			tv_mobile.setText(mobile);
		}
		btn_getvalidate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getValidate();
				if(myCount != null){
					myCount.start();
					btn_getvalidate.setClickable(false);
				}
			}
		});
		btn_refervalidate.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				code = et_validate.getText().toString().trim();
				if(notNull_login()){
					referValidateManager.setString(mobile, code, ImemberApplication.getInstance().getLogin_msg_id());
					referValidateManager.excute(type);
				}
			}
			
		});
		btn_back.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				MsgValidateActivity.this.finish();
			}
			
		});
	}
	
	private void initUI_msg(){
		registFinishSubActivity();
		
		referValidateManager = new ReferValidateManager(this);
		getValidateManager = new GetValidateManager(this);		
		iv_sign = (ImageView)findViewById(R.id.sign);
		
		btn_getvalidate = (Button)findViewById(R.id.login_getvalidate);
		btn_refervalidate = (Button)findViewById(R.id.login_refervalidate);
		btn_back = (Button)findViewById(R.id.btn_header_left);
		et_username_login = (EditText)findViewById(R.id.login_username);
		
		myCount = new MyCount(120000, 1000, btn_getvalidate);
		
		if(canEdit){
			et_username_login.setFocusable(true);
		}
		else{
			et_username_login.setFocusable(false);
		}
		et_validate = (EditText)findViewById(R.id.login_validate);

		if(mobile != null && !"".equals(mobile)){
			et_username_login.setText(mobile);
			et_username_login.setSelection(mobile.length());
		}
		btn_getvalidate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setString_getValidate();
				if(notNull_getValidate()){
					getValidate();
					if(myCount != null){
						myCount.start();
						btn_getvalidate.setClickable(false);
					}
				}
			}
		});
		btn_refervalidate.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				setString_login();
				if(notNull_login()){
					referValidateManager.setString(mobile, code, ImemberApplication.getInstance().getLogin_msg_id());
					referValidateManager.excute(type);
				}
			}
			
		});
		btn_back.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				MsgValidateActivity.this.finish();
			}
			
		});
	}
	
	private void setUI(){
		btn_getvalidate.setText("获取验证码");
	}
	
	private void getValidate(){
		getValidateManager.setString(mobile);
		getValidateManager.excute(type);
	}
	
	private void setString_login(){
		mobile = et_username_login.getText().toString().trim();
		code = et_validate.getText().toString().trim();
	}
	
	private void setString_getValidate(){
		mobile = et_username_login.getText().toString().trim();
	}
	private boolean notNull_getValidate(){
		if(mobile != null && !"".equals(mobile)){
			if(ImemberApplication.getInstance().isMobile(mobile)){
				if(mobile.length() == 11){
					
				}
				else{
					Toast.makeText(this, "手机号码必须为11位", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
			else{
				Toast.makeText(this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		else{
			Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	private boolean notNull_login(){
		if(mobile != null && !"".equals(mobile)){
			if(ImemberApplication.getInstance().isMobile(mobile)){
				if(mobile.length() == 11){
					
				}
				else{
					Toast.makeText(this, "手机号码必须为11位", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
			else{
				Toast.makeText(this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		else{
			Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(code != null && !"".equals(code)){
			if(ImemberApplication.getInstance().isNum(code)){
				if(code.length() == 6){
					
				}
				else{
					Toast.makeText(this, "验证码必须为6位", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
			else{
				Toast.makeText(this, "验证码格式不正确", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		else{
			Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
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
		case ImemberApplication.FOOTER_ID_SETTING:
			layout_footer_setting.setBackgroundResource(R.drawable.footer_over_bg);
			iv_footer_setting.setBackgroundResource(R.drawable.footer_setting_click);
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
