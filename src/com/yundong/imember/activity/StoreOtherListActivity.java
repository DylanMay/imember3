package com.yundong.imember.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.R;
import com.yundong.imember.adapter.ListAdapter_store;
import com.yundong.imember.adapter.ListAdapter_storeother;
import com.yundong.imember.customWidget.MyListView;
import com.yundong.imember.customWidget.MyListView.OnRefreshListener;
import com.yundong.imember.datamanager.LogonManager;
import com.yundong.imember.datamanager.ScanResultManager;
import com.yundong.imember.datamanager.StoreOtherList_loadManager;
import com.yundong.imember.entity.Store;

public class StoreOtherListActivity extends SuperActivity {
	/**
	 * 头部相关
	 */
	private TextView tv_title;
	private Button btn_left;
	
	/**
	 * 相关listview
	 */
	private MyListView listView_store;
	private ListAdapter_storeother listAdapter_store;
	
	/**
	 * 登录界面相关
	 */
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
	
	/**扫码相关
	 * 
	 */
	private ScanResultManager scanProcess;
	private int account_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_storeother);
		super.onCreate(savedInstanceState);
		
		getIntentData();
		initUI();
		setUI();
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private void getIntentData(){
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			account_id = bundle.getInt("account_id");
		}
	}

	private void init(){
		loadStores();
	}
	
	private void initUI(){
		registFinishSubActivity();
		
		tv_title = (TextView)findViewById(R.id.tv_header_title);
		btn_left = (Button)findViewById(R.id.btn_header_left);
		btn_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		initUI_store();
		initUI_logon();
	}
	
	private void initUI_store(){
		listView_store = (MyListView) findViewById(R.id.listview_store);
		listAdapter_store = new ListAdapter_storeother(this, listView_store);
		listView_store.setAdapter(listAdapter_store);
		listView_store.setOnItemClickListener(new ItemClickListener_store());
		
		listView_store.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				loadStores();
			}
		});
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
	}
	
	private void setUI(){
//		tv_title.setText(ImemberApplication.getInstance().getCurrentCity().getCity_name());
		
		
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
				intent.setClass(StoreOtherListActivity.this, LoginActivity.class);
				startActivityForResult(intent, 5);
			}
			
		});
		tv_forget_psd.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(StoreOtherListActivity.this, ForgetPsdActivity.class);
				startActivity(intent);
			}
			
		});
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
	
	private void loadStores(){
//		if(ImemberApplication.getInstance().isChange_city()){
//			if(ImemberApplication.getInstance().stores.size() > 0){
//				ImemberApplication.getInstance().stores.clear();
//			}
//			tv_title.setText(ImemberApplication.getInstance().getCurrentCity().getCity_name());
//			new StoreList_loadManager(this, listView_store, listAdapter_store).load();
//		}
//		else if(ImemberApplication.getInstance().stores.size() <= 0){
//			new StoreList_loadManager(this, listView_store, listAdapter_store).load();
//		}
		
		new StoreOtherList_loadManager(this, listView_store, listAdapter_store).load(account_id);
	}
	
	private class ItemClickListener_store implements OnItemClickListener {
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long id) {
			Intent intent = new Intent();
			Store store = ImemberApplication.getInstance().stores_other.get(position - 1);
			intent.putExtra("store", store);
			intent.setClass(StoreOtherListActivity.this, StoreDetailActivity.class);
			startActivity(intent);
		}
	}
	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (scanProcess == null) {
//			scanProcess = new ScanResultManager(this);
//		}
//		scanProcess.setAll(requestCode, resultCode, data);
//		scanProcess.process_activityResult();
//		super.onActivityResult(requestCode, resultCode, data);
//	}
	
	protected void setFooterScanClick() {
		Intent intent = new Intent();
		intent.setClass(StoreOtherListActivity.this, CaptureActivity.class);
		startActivityForResult(intent, 6);
	}

	@Override
	protected void setFooterClickBg() {
		layout_footer_store.setBackgroundResource(R.drawable.footer_over_bg);
		iv_footer_store.setBackgroundResource(R.drawable.footer_store_click);
	}

//	@Override
//	protected void setFooterListener() {
//		layout_footer_mycard.setOnClickListener(new FooterClickListener());
//		layout_footer_scan.setOnClickListener(new FooterClickListener());
//		layout_footer_reward.setOnClickListener(new FooterClickListener());
//		layout_footer_setting.setOnClickListener(new FooterClickListener());
//	}
}
