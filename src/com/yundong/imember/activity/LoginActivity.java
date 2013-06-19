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
import com.yundong.imember.datamanager.LoginManager;
import com.yundong.imember.utils.InputUtil;

public class LoginActivity extends SuperActivity {
	/**
	 * 注册界面相关
	 */
//	private ResizeLayout layout_login;
	private Button btn_regist;
	private Button btn_cancel;
	private EditText et_username_login;
	private EditText et_email_login;
	private EditText et_password_login;
	private ImageView iv_key;
	private boolean isOpen=true;
	private EditText et_password_confirm_login;
	private String userName_login;
	private String passWord_login;
//	private String passWord_sec_login;
	private String email_or_mobile_login;
	private int type;
	
	private LoginManager myLogin;
	
	private ImageView iv_sign;
	private InputUtil inputUtil;
	
	private String code;
	private String msg_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_login);
		super.onCreate(savedInstanceState);
		
		initUI();
		
		inputUtil = new InputUtil(iv_sign, layout_footer_scan, layout_footer);
		inputUtil.start();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		inputUtil.stop();
		super.onDestroy();
	}
	
	private void initUI(){
		registFinishSubActivity();
		
		myLogin = new LoginManager(this);
		
		iv_sign = (ImageView)findViewById(R.id.sign);
		
		btn_regist = (Button)findViewById(R.id.login_btn_yes);
		btn_cancel = (Button)findViewById(R.id.btn_header_right);
		et_username_login = (EditText)findViewById(R.id.login_username);
		et_password_login = (EditText)findViewById(R.id.login_password);
		iv_key = (ImageView)findViewById(R.id.key);
		et_password_confirm_login = (EditText)findViewById(R.id.login_confirmpassword);
		et_email_login = (EditText)findViewById(R.id.login_email);

		btn_regist.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				setString_login();
				if(notNull_login()){
					go2();
				}
			}
			
		});
		btn_cancel.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				LoginActivity.this.finish();
			}
			
		});
		iv_key.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isOpen = (isOpen == true) ? false : true;
				if(isOpen){
					iv_key.setBackgroundResource(R.drawable.key_on);
					et_password_login.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					et_password_login.setSelection(et_password_login.getText().toString().length());
				}
				else{
					iv_key.setBackgroundResource(R.drawable.key_off);
					et_password_login.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					et_password_login.setSelection(et_password_login.getText().toString().length());
				}
			}
		});
	}
	
	private void go2(){
		switch(type){
		case 0:
			myLogin.setString(type, userName_login, email_or_mobile_login, passWord_login, code, msg_id);
			myLogin.login();
			break;
		case 1:
			if(code != null && !"".equals(code)){
//				System.out.println("msg_id2---------->" + msg_id);
				myLogin.setString(type, userName_login, email_or_mobile_login, passWord_login, code, msg_id);
				myLogin.login();
			}
			else{
				Intent intent = new Intent();
				intent.putExtra("mobile", email_or_mobile_login);
				intent.putExtra("canEdit", false);
				intent.putExtra("isRegist", true);
				intent.putExtra("type", 0);
				intent.setClass(LoginActivity.this, MsgValidateActivity.class);
				startActivityForResult(intent, 5);
			}
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
				
				myLogin.setString(type, userName_login, email_or_mobile_login, passWord_login, code, msg_id);
				myLogin.login();
			}
		}
	}
	
	private void setString_login(){
		userName_login = et_username_login.getText().toString().trim();
		passWord_login = et_password_login.getText().toString();
//		passWord_sec_login = et_password_confirm_login.getText().toString();
		email_or_mobile_login = et_email_login.getText().toString().trim();
	}
	
	private boolean notNull_login(){
		if(userName_login != null && !"".equals(userName_login)){
			if(userName_login.length() >= 4){
				if(userName_login.length() <= 15){
					if(!ImemberApplication.getInstance().isNum(userName_login)){
						Bundle bundle = ImemberApplication.getInstance().isSpecial(userName_login);
						boolean has = bundle.getBoolean("has");
						if(!has){
							
						}
						else{
							CharSequence value = bundle.getCharSequence("character");
							Toast.makeText(this, "用户名含有特殊字符 " + value, Toast.LENGTH_SHORT).show();
							return false;
						}
					}
					else{
						Toast.makeText(this, "用户名必须是英文或与数字的组合", Toast.LENGTH_SHORT).show();
						return false;
					}
				}
				else{
					Toast.makeText(this, "用户名长度不能大于15位", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
			else{
				Toast.makeText(this, "用户名长度不能小于4位", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		else{
			Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(email_or_mobile_login != null && !"".equals(email_or_mobile_login)){
			if(ImemberApplication.getInstance().isEmail(email_or_mobile_login)){
				type = 0;
			}
			else{
				if(ImemberApplication.getInstance().isMobile(email_or_mobile_login)){
					if(email_or_mobile_login.length() == 11){
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
		}
		else{
			Toast.makeText(this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(passWord_login != null && !"".equals(passWord_login)){
			if(passWord_login.length() >= 6){
				if(!ImemberApplication.getInstance().isNum(passWord_login)){
					if(!ImemberApplication.getInstance().isCharacter(passWord_login)){
						Bundle bundle = ImemberApplication.getInstance().isSpecial(passWord_login);
						boolean has = bundle.getBoolean("has");
						if(!has){
							
						}
						else{
							CharSequence value = bundle.getCharSequence("character");
							Toast.makeText(this, "密码含有特殊字符 " + value, Toast.LENGTH_SHORT).show();
							return false;
						}
					}
					else{
						Toast.makeText(this, "密码必须是英文或与数字的组合", Toast.LENGTH_SHORT).show();
						return false;
					}
				}
				else{
					Toast.makeText(this, "密码必须是英文或与数字的组合", Toast.LENGTH_SHORT).show();
					return false;
				}
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
//		if(passWord_sec_login != null && !"".equals(passWord_sec_login)){
//			if(passWord_sec_login.equals(passWord_login)){
//				
//			}
//			else{
//				Toast.makeText(this, "密码不一致", Toast.LENGTH_SHORT).show();
//				return false;
//			}
//		}
//		else{
//			Toast.makeText(this, "确认密码不能为空", Toast.LENGTH_SHORT).show();
//			return false;
//		}
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
