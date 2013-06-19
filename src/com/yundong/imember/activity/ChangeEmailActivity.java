package com.yundong.imember.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.R;
import com.yundong.imember.datamanager.ChangeEmailManager;

public class ChangeEmailActivity extends SuperActivity {
	private EditText et_email;
	private Button btn_send;
	private Button btn_left;
	
	private String str_email;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_changeemail);
		super.onCreate(savedInstanceState);
		
		initUI();
	}

	private void initUI() {
		registFinishSubActivity();
		
		btn_left = (Button)findViewById(R.id.btn_header_left);
		et_email = (EditText)findViewById(R.id.et_newemail);
		btn_send = (Button)findViewById(R.id.btn_send);
		
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
		str_email = et_email.getText().toString().trim();
	}
	
	private boolean notNull(){
		if(str_email != null && !"".equals(str_email)){
			if(ImemberApplication.getInstance().isEmail(str_email)){
				
			}
			else{
				Toast.makeText(this, "请输入正确的邮箱格式", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		else{
			Toast.makeText(this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	private void send(){
		new ChangeEmailManager(this).excute(str_email);
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
