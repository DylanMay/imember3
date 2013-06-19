package com.yundong.imember.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.R;
import com.yundong.imember.adapter.ListAdapter_reward;
import com.yundong.imember.customWidget.MyListView;
import com.yundong.imember.customWidget.MyListView.OnRefreshListener;
import com.yundong.imember.datamanager.LogonManager;
import com.yundong.imember.datamanager.RewardDetail_loadManager;
import com.yundong.imember.datamanager.RewardList_loadManager;
import com.yundong.imember.datamanager.UseRewardDetail_loadManager;
import com.yundong.imember.entity.City;
import com.yundong.imember.entity.Reward;
import com.yundong.imember.entity.User;
import com.yundong.imember.myinterface.LoginSuccessListener;
import com.yundong.imember.myinterface.LogonSuccessListener;
import com.yundong.imember.utils.InputUtil;

public class MyRewardListActivity extends HomeActivity implements LogonSuccessListener,LoginSuccessListener{
	private MyListView listView_reward;
	private ListAdapter_reward listAdapter_reward;
	
	protected LinearLayout view_logon;
	protected LinearLayout view_islogoning;
	private EditText et_username_logon;
	private EditText et_password_logon;
	private String userName_logon;
	private String passWord_logon;	
	private Button btn_logon;
	private TextView btn_login;
	private TextView tv_forget_psd;
	private LogonManager myLogon;
	private ImageView btn_left;
	
	private String[] str_status = {"全部", "未使用", "已经使用", "过期", "失效"};
	
	private ImageView iv_sign;
	private InputUtil inputUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_myreward_list);
		super.onCreate(savedInstanceState);
		
		initListener();
		initUI();
		initUI_logon();
		inputUtil = new InputUtil(iv_sign, layout_footer_scan, layout_footer);
		initData();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		
//		inputUtil = new InputUtil(iv_sign, layout_footer_scan, layout_footer);
		initData();
	}
	
	private void initListener(){
		ImemberApplication.getInstance().getLogonSuccessManager().registeLogonSuccessListener(this);
		ImemberApplication.getInstance().getLoginSuccessManager().registeLoginSuccessListener(this);
	}
	
	private void initData() {
		if(ImemberApplication.getInstance().isLogon()){
			view_logon.setVisibility(View.GONE);
			inputUtil.setHide(true);
			view_islogoning.setVisibility(View.GONE);
			view_reward.setVisibility(View.VISIBLE);
			
//			if(ImemberApplication.getInstance().rewards.size() <= 0){
				loadRewards();
//			}
		}
		else{
			if(ImemberApplication.getInstance().isAutoLogon()){
				view_logon.setVisibility(View.GONE);
				inputUtil.setHide(true);
				view_reward.setVisibility(View.GONE);
				view_islogoning.setVisibility(View.VISIBLE);
				
				User user = ImemberApplication.getInstance().getUser();
				myLogon.setString(user.getUser_name(), user.getPassword());
				myLogon.logon();
			}
			else{
				view_logon.setVisibility(View.VISIBLE);
//				inputUtil.setHide(false);
//				inputUtil.start();
				
				et_password_logon.setText("");
				String username = ImemberApplication.getInstance().getUsername();
				if(username != null){
					et_username_logon.setText(username);
				}
//				inputUtil.start();
				view_reward.setVisibility(View.GONE);
			}
		}
	}
	
	private void initUI_logon(){
		/**
		 * 登录相关
		 */
		myLogon = new LogonManager(this);
		view_logon = (LinearLayout)findViewById(R.id.view_logon);
		view_islogoning = (LinearLayout)findViewById(R.id.view_islogoning);
		et_username_logon = (EditText)view_logon.findViewById(R.id.logon_username);
		et_password_logon = (EditText)view_logon.findViewById(R.id.logon_password);
		btn_logon = (Button)view_logon.findViewById(R.id.logon_btn_yes);
		btn_login = (TextView)view_logon.findViewById(R.id.btn_header_right);
		tv_forget_psd = (TextView)view_logon.findViewById(R.id.logon_forget);
		
		setUI_logon();
	}
	
	private void setUI_logon(){
//		User user = ImemberApplication.getInstance().getUser();
//		if(user != null){
//			et_username_logon.setText("" + user.getUser_name());
//			et_password_logon.set
//		}
		
		btn_logon.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				setString_logon();
				if(notNull_logon()){
					myLogon.setString(userName_logon, passWord_logon);
					myLogon.logon();
				}
			}
			
		});
		btn_login.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MyRewardListActivity.this, LoginActivity.class);
				startActivityForResult(intent, 5);
			}
			
		});
		tv_forget_psd.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MyRewardListActivity.this, ForgetPsdActivity.class);
				startActivity(intent);
			}
			
		});
	}

	private void initUI(){
		registFinishMainActivity();
		
		iv_sign = (ImageView)findViewById(R.id.sign);
		btn_left = (ImageView)findViewById(R.id.btn_header_left);
		listView_reward = (MyListView)findViewById(R.id.listview_reward);
		listAdapter_reward = new ListAdapter_reward(this, listView_reward);
		listView_reward.setAdapter(listAdapter_reward);
		listView_reward.setOnItemClickListener(new ItemClickListener_reward());
		
		btn_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				createItemDialog_status();
			}
		});
		listView_reward.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				loadRewards();
			}
		});
	}
	
	public void createItemDialog_status(){
		new AlertDialog.Builder(this)
        .setTitle("请选择排序类别")
        .setItems(str_status, new DialogInterface.OnClickListener() {
        	
            public void onClick(DialogInterface dialog, final int which) {
            	switch(which){
            	case 0:
            		ImemberApplication.getInstance().setReward_sort(-1);
            		break;
            	case 1:
            		ImemberApplication.getInstance().setReward_sort(0);
            		break;
            	case 2:
            		ImemberApplication.getInstance().setReward_sort(1);
            		break;
            	case 3:
            		ImemberApplication.getInstance().setReward_sort(2);
            		break;
            	case 4:
            		ImemberApplication.getInstance().setReward_sort(3);
            		break;
            	}
            	loadRewards();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//
			}
		}).create().show();
	}
	
	private class ItemClickListener_reward implements OnItemClickListener {
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long id) {
			Reward reward = ImemberApplication.getInstance().rewards.get(position - 1);
			
//			new RewardDetail_loadManager(MyRewardListActivity.this).load(reward.getMcard_id());
			
			new UseRewardDetail_loadManager(MyRewardListActivity.this).load(reward.getU_reward_id());
		}
	}
	
	private void loadRewards(){
		new RewardList_loadManager(this, listView_reward, listAdapter_reward).load();
	}
	
	private void setString_logon(){
		userName_logon = et_username_logon.getText().toString().trim();
		passWord_logon = et_password_logon.getText().toString();
	}
	
	private boolean notNull_logon(){
		if(userName_logon != null && !"".equals(userName_logon)){
			
		}
		else{
			Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(passWord_logon != null && !"".equals(passWord_logon)){
			
		}
		else{
			Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	@Override
	protected void setFooterClickBg() {
		// TODO Auto-generated method stub
		layout_footer_reward.setBackgroundResource(R.drawable.footer_over_bg);
		iv_footer_reward.setBackgroundResource(R.drawable.footer_myreward_click);
	}

//	@Override
//	protected void setFooterListener() {
//		// TODO Auto-generated method stub
//		layout_footer_store.setOnClickListener(new FooterClickListener());
//		layout_footer_mycard.setOnClickListener(new FooterClickListener());
//		layout_footer_scan.setOnClickListener(new FooterClickListener());
//		layout_footer_setting.setOnClickListener(new FooterClickListener());
//	}
	
	@Override
	public void logonSuccess() {
		initData();
	}
	@Override
	public void logonFail(String username) {
		// TODO Auto-generated method stub
		initData();
		et_username_logon.setText("" + username);
	}
	
	@Override
	public void loginSuccess(String userName, String psd) {
		// TODO Auto-generated method stub
		et_username_logon.setText("" + userName);
		et_password_logon.setText("");
		myLogon.setString(userName, psd);
		myLogon.logon();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		ImemberApplication.getInstance().getLogonSuccessManager().removeLogonSuccessListener(this);
		ImemberApplication.getInstance().getLoginSuccessManager().removeLoginSuccessListener(this);
		
		System.out.println("MyRewardListActivity---------kill");
	}
}
