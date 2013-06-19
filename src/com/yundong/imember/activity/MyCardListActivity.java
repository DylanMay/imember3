package com.yundong.imember.activity;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.R;
import com.yundong.imember.adapter.ListAdapter_mycard;
import com.yundong.imember.customWidget.MyListView;
import com.yundong.imember.customWidget.MyListView.OnRefreshListener;
import com.yundong.imember.datamanager.LogonManager;
import com.yundong.imember.datamanager.MyCardDetail_loadManager;
import com.yundong.imember.datamanager.MyCardList_loadManager;
import com.yundong.imember.entity.MyCard;
import com.yundong.imember.entity.User;
import com.yundong.imember.myinterface.LoginSuccessListener;
import com.yundong.imember.myinterface.LogonSuccessListener;
import com.yundong.imember.utils.InputUtil;

public class MyCardListActivity extends HomeActivity implements LogonSuccessListener,LoginSuccessListener{
	private TextView tv_title;
	private MyListView listView_mycard;
	private ListAdapter_mycard listAdapter_mycard;
	
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
	
	private ImageView iv_sign;
	private InputUtil inputUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_mycard_list);
		super.onCreate(savedInstanceState);
		
		initListener();
		initUI();
		setUI();
		initUI_logon();
		inputUtil = new InputUtil(iv_sign, layout_footer_scan, layout_footer);
		initData();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		
//		inputUtil = new InputUtil(iv_sign, layout_footer_scan, layout_footer);
		setUI();
		initData();
	}
	
	private void initListener(){
		ImemberApplication.getInstance().getLogonSuccessManager().registeLogonSuccessListener(this);
		ImemberApplication.getInstance().getLoginSuccessManager().registeLoginSuccessListener(this);
	}
	
	private void setUI() {
		
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
				intent.setClass(MyCardListActivity.this, LoginActivity.class);
				startActivityForResult(intent, 5);
			}
			
		});
		tv_forget_psd.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MyCardListActivity.this, ForgetPsdActivity.class);
				startActivity(intent);
			}
			
		});
	}
	
	private void initData(){
		if(ImemberApplication.getInstance().isLogon()){
			view_logon.setVisibility(View.GONE);
			inputUtil.setHide(true);
			view_islogoning.setVisibility(View.GONE);
			view_mycard.setVisibility(View.VISIBLE);
			
//			if(ImemberApplication.getInstance().myCards.size() <= 0){
				loadCards();
//			}
		}
		else{
			if(ImemberApplication.getInstance().isAutoLogon()){
				view_logon.setVisibility(View.GONE);
				inputUtil.setHide(true);
				view_mycard.setVisibility(View.GONE);
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
				view_mycard.setVisibility(View.GONE);
			}
		}
	}
	
	private void initUI(){
		registFinishMainActivity();
		
		tv_title = (TextView)findViewById(R.id.tv_header_title);
		
		iv_sign = (ImageView)findViewById(R.id.sign);
		
		listView_mycard = (MyListView) findViewById(R.id.listview_mycard);
		listAdapter_mycard = new ListAdapter_mycard(this, listView_mycard);
		listView_mycard.setAdapter(listAdapter_mycard);
		listView_mycard.setOnItemClickListener(new ItemClickListener_mycard());
		
		listView_mycard.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				loadCards();
			}
		});
	}
	
	private class ItemClickListener_mycard implements OnItemClickListener {
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long id) {
			MyCard card = ImemberApplication.getInstance().myCards.get(position - 1);
//			String c_servies_terms = card.getC_servies_terms();
//			String c_servies_terms = "服务条款";
			new MyCardDetail_loadManager(MyCardListActivity.this).load(card.getMcard_id());
		}
	}
	
	private void loadCards(){
		new MyCardList_loadManager(this, listView_mycard, listAdapter_mycard).load();
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
		layout_footer_mycard.setBackgroundResource(R.drawable.footer_over_bg);
		iv_footer_mycard.setBackgroundResource(R.drawable.footer_mycard_click);
	}

//	@Override
//	protected void setFooterListener() {
//		// TODO Auto-generated method stub
//		layout_footer_store.setOnClickListener(new FooterClickListener());
//		layout_footer_scan.setOnClickListener(new FooterClickListener());
//		layout_footer_reward.setOnClickListener(new FooterClickListener());
//		layout_footer_setting.setOnClickListener(new FooterClickListener());
//	}
	
	@Override
	public void logonSuccess() {
		initData();
//		loadCards();
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
		
		System.out.println("MyCardListActivity---------kill");
	}

	@Override
	public void logonFail(String username) {
		// TODO Auto-generated method stub
		initData();
		et_username_logon.setText("" + username);
	}
}
