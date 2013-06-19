package com.yundong.imember.activity;

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
import com.yundong.imember.datamanager.ChangePsdManager;
import com.yundong.imember.utils.InputUtil;

public class ChangePsdActivity extends SuperActivity {
	private Button btn_left;
	private EditText et_currentpsd;
	private EditText et_newpsd;
//	private EditText et_newpsd_confirm;
	private Button btn_send;
	private ImageView iv_key;
	private boolean isOpen=true;
	
	private String str_currentpsd;
	private String str_newpsd;
//	private String str_newpsd_confirm;
	
	private ImageView iv_sign;
	private InputUtil inputUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_changepsd);
		super.onCreate(savedInstanceState);
		
		initUI();
		inputUtil = new InputUtil(iv_sign, layout_footer_scan, layout_footer);
//		inputUtil.setHide(false);
		inputUtil.start();
	}

	private void initUI() {
		registFinishSubActivity();
		
		iv_sign = (ImageView)findViewById(R.id.sign);
		
		btn_left = (Button)findViewById(R.id.btn_header_left);
		et_currentpsd = (EditText)findViewById(R.id.changepsd_currentpsd);
		et_newpsd = (EditText)findViewById(R.id.changepsd_newpsd);
//		et_newpsd_confirm = (EditText)findViewById(R.id.changepsd_newpsd_confirm);
		btn_send = (Button)findViewById(R.id.changepsd_btn_yes);
		iv_key = (ImageView)findViewById(R.id.key);
		
		setUI();
	}
	
	private void setUI(){
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
		iv_key.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isOpen = (isOpen == true) ? false : true;
				if(isOpen){
					iv_key.setBackgroundResource(R.drawable.key_on);
					et_newpsd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					et_newpsd.setSelection(et_newpsd.getText().toString().length());
				}
				else{
					iv_key.setBackgroundResource(R.drawable.key_off);
					et_newpsd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					et_newpsd.setSelection(et_newpsd.getText().toString().length());
				}
			}
		});
	}
	
	private void setStr(){
		str_currentpsd = et_currentpsd.getText().toString().trim();
		str_newpsd = et_newpsd.getText().toString().trim();
//		str_newpsd_confirm = et_newpsd_confirm.getText().toString().trim();
	}
	
	private boolean notNull(){
		if(str_currentpsd != null && !"".equals(str_currentpsd)){
			if(str_currentpsd.length() >= 6){
				
			}
			else{
				Toast.makeText(this, "当前密码至少6位", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		else{
			Toast.makeText(this, "当前密码不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(str_newpsd != null && !"".equals(str_newpsd)){
			if(str_newpsd.length() >= 6){
				
			}
			else{
				Toast.makeText(this, "新密码至少6位", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		else{
			Toast.makeText(this, "新密码不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
//		if(str_newpsd_confirm != null && !"".equals(str_newpsd_confirm)){
//			if(str_newpsd_confirm.equals(str_newpsd)){
//				
//			}
//			else{
//				Toast.makeText(this, "新密码不一致", Toast.LENGTH_SHORT).show();
//				return false;
//			}
//		}
//		else{
//			Toast.makeText(this, "新确认密码不能为空", Toast.LENGTH_SHORT).show();
//			return false;
//		}
		return true;
	}
	
	private void send(){
		new ChangePsdManager(this).excute(str_currentpsd, str_newpsd);
	}

	@Override
	protected void setFooterClickBg() {
		// TODO Auto-generated method stub
		layout_footer_setting.setBackgroundResource(R.drawable.footer_over_bg);
		iv_footer_setting.setBackgroundResource(R.drawable.footer_setting_click);
	}

//	@Override
//	protected void setFooterListener() {
//		// TODO Auto-generated method stub
//		layout_footer_store.setOnClickListener(new FooterClickListener());
//		layout_footer_mycard.setOnClickListener(new FooterClickListener());
//		layout_footer_scan.setOnClickListener(new FooterClickListener());
//		layout_footer_reward.setOnClickListener(new FooterClickListener());
//	}
}
