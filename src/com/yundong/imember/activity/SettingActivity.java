package com.yundong.imember.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.R;
import com.yundong.imember.customWidget.MyDialog;
import com.yundong.imember.datamanager.ChangeMobileManager;
import com.yundong.imember.datamanager.Citys_loadManager_setting;
import com.yundong.imember.entity.User;
import com.yundong.imember.myinterface.SettingChangeSthListener;

public class SettingActivity extends HomeActivity implements SettingChangeSthListener{
	private TextView tv_setting_username;
	private TextView tv_setting_email;
	private TextView tv_setting_mobile;
	
	private LinearLayout layout_email;
	private LinearLayout layout_mobile;
	private LinearLayout layout_changepsd;
	private LinearLayout layout_changecity;
	private TextView tv_changecity;
	private LinearLayout layout_version;
	private TextView tv_version;
	private LinearLayout layout_advice;
	private Button btn_exit_logon;
	
	private ImageView iv_email_arrow;
	private ImageView iv_mobile_arrow;
	private ImageView iv_changepsd_arrow;
	
	private String code;
	private String msg_id;
	private String mobile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_setting);
		super.onCreate(savedInstanceState);
		
		initUI();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setUI();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		tv_changecity.setText("" + ImemberApplication.getInstance().getCurrentCity().getCity_name());
		setUI();
	}
	
	private void initUI(){
		registFinishMainActivity();
		
		tv_setting_username = (TextView)findViewById(R.id.tv_setting_username);
		tv_setting_email = (TextView)findViewById(R.id.tv_setting_email);
		tv_setting_mobile = (TextView)findViewById(R.id.tv_setting_mobile);
		
		layout_email = (LinearLayout)findViewById(R.id.layout_item_email);
		layout_mobile = (LinearLayout)findViewById(R.id.layout_item_mobile);
		layout_changepsd = (LinearLayout)findViewById(R.id.layout_item_changepsd);
		layout_changecity = (LinearLayout)findViewById(R.id.layout_item_changecity);
		tv_changecity = (TextView)findViewById(R.id.tv_setting_changecity);
		layout_version = (LinearLayout)findViewById(R.id.layout_item_version);
		tv_version = (TextView)findViewById(R.id.tv_setting_version);
		layout_advice = (LinearLayout)findViewById(R.id.layout_item_advice);
		btn_exit_logon = (Button)findViewById(R.id.btn_exit_logon);
		
		iv_email_arrow = (ImageView)findViewById(R.id.email_arrow);
		iv_mobile_arrow = (ImageView)findViewById(R.id.mobile_arrow);
		iv_changepsd_arrow = (ImageView)findViewById(R.id.changepsd_arrow);
		
		setUI();
	}
	
	private void setUI(){
		if(ImemberApplication.getInstance().isLogon()){
			User user = ImemberApplication.getInstance().getUser();
			tv_setting_username.setText("" + user.getUser_name());
			String email = user.getEmail().trim();
			if(email != null && !"".equals(email) && !"null".equals(email)){
				tv_setting_email.setText("" + user.getEmail());
			}
			else{
				tv_setting_email.setText("绑定邮箱");
//				tv_setting_email.setText("");
			}
			String mobile = user.getMobile().trim();
			if(mobile != null && !"".equals(mobile) && !"null".equals(mobile)){
				tv_setting_mobile.setText("" + mobile);
			}
			else{
				tv_setting_mobile.setText("绑定手机号码");
//				tv_setting_mobile.setText("");
			}
			
			btn_exit_logon.setVisibility(View.VISIBLE);
			iv_email_arrow.setVisibility(View.VISIBLE);
			iv_mobile_arrow.setVisibility(View.VISIBLE);
			iv_changepsd_arrow.setVisibility(View.VISIBLE);
		}
		else{
			tv_setting_username.setText("");
//			tv_setting_email.setText("绑定邮箱");
//			tv_setting_mobile.setText("绑定手机号码");
			tv_setting_email.setText("");
			tv_setting_mobile.setText("");
			btn_exit_logon.setVisibility(View.GONE);
			iv_email_arrow.setVisibility(View.GONE);
			iv_mobile_arrow.setVisibility(View.GONE);
			iv_changepsd_arrow.setVisibility(View.GONE);
		}
		
		tv_version.setText("" + getResources().getText(R.string.version_interface).toString());
		
		layout_email.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(ImemberApplication.getInstance().isLogon()){
					Intent intent = new Intent();
					intent.setClass(SettingActivity.this, ChangeEmailActivity.class);
					startActivity(intent);
				}
			}
		});
		layout_mobile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(ImemberApplication.getInstance().isLogon()){
					Intent intent = new Intent();
					intent.putExtra("type", 2);
					intent.putExtra("canEdit", true);
					intent.setClass(SettingActivity.this, MsgValidateActivity.class);
					startActivityForResult(intent, 5);
				}
			}
		});
		layout_changepsd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(SettingActivity.this, ChangePsdActivity.class);
//				startActivity(intent);
				
				if(ImemberApplication.getInstance().isLogon()){
					Intent intent = new Intent();
					intent.setClass(SettingActivity.this, ChangePsdActivity.class);
					startActivity(intent);
				}
				else{
					MyDialog myDialog = new MyDialog(SettingActivity.this);
					myDialog.setTitle("提示");
					myDialog.setIcon(android.R.drawable.ic_dialog_info);
					myDialog.setCancelable(false);
					myDialog.setMessage("您还未登录");
					myDialog.setLeftButton("知道了", new MyDialog.MyOnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						}
						
					});
					myDialog.create();
					myDialog.show();
				}
			}
		});
		layout_changecity.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				loadCitys();
			}
		});
		layout_version.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				new updateManager(SettingActivity.this).check();
			}
		});
		layout_advice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SettingActivity.this, AdviceActivity.class);
				startActivity(intent);
			}
		});
		btn_exit_logon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ImemberApplication.getInstance().getSQLite().delete_userinfo();
				ImemberApplication.getInstance().setLogon(false);
				ImemberApplication.getInstance().setAutoLogon();
				ImemberApplication.getInstance().setUserInfo(null);
				setUI();
				
				Toast.makeText(SettingActivity.this, "您已退出登录", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void loadCitys(){
		if(ImemberApplication.getInstance().citys.size() <= 0){
			new Citys_loadManager_setting(this, null).load();
		}
		else{
			Intent intent = new Intent();
			intent.setClass(SettingActivity.this, ChangeCityActivity.class);
			startActivity(intent);
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
				mobile = bundle.getString("mobile");
				
				if(code != null && !"".equals(code)){
					new ChangeMobileManager(this, this).excute(code, msg_id, mobile);
				}
			}
		}
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
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		System.out.println("SettingActivity---------kill");
	}

	@Override
	public void changeSthSuccess() {
		// TODO Auto-generated method stub
		setUI();
	}
}
