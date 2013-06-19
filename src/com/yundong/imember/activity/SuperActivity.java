package com.yundong.imember.activity;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.R;
import com.yundong.imember.datamanager.ScanResultManager;
import com.yundong.imember.myinterface.FinishActivityListener;

public abstract class SuperActivity extends Activity implements
		FinishActivityListener {
	/**
	 * 页面相关控件
	 */
	protected LinearLayout view_store;
	protected LinearLayout view_mycard;
	protected LinearLayout view_scan;
	protected LinearLayout view_reward;
	protected LinearLayout view_setting;

	/**
	 * 页脚相关控件
	 * 
	 * @param savedInstanceState
	 */
	protected LinearLayout layout_footer;
	protected LinearLayout layout_footer_store;
	protected LinearLayout layout_footer_mycard;
	protected LinearLayout layout_footer_scan;
	protected LinearLayout layout_footer_reward;
	protected LinearLayout layout_footer_setting;
	protected ImageView iv_footer_store;
	protected ImageView iv_footer_mycard;
	protected ImageView iv_footer_scan;
	protected ImageView iv_footer_reward;
	protected ImageView iv_footer_setting;
	
	private boolean regist_sub;
	private boolean regist_main;
	
	private ScanResultManager scanProcess;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initSuperUI();
		setSuperUI();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(ImemberApplication.getInstance().isScanSuccess()){
//			ImemberApplication.getInstance().getFinishActivityManager().finishAllSubActivitys();
//			if (scanProcess == null) {
//				scanProcess = new ScanResultManager(this);
//			}
			ScanResultManager scanProcess = new ScanResultManager(this);
			scanProcess.process_activityResult(ImemberApplication.getInstance().getScanResult());
			ImemberApplication.getInstance().setScanSuccess(false);
			return ;
		}
	}
	
	private void initSuperUI() {
		view_store = (LinearLayout) findViewById(R.id.view_store);
		view_mycard = (LinearLayout) findViewById(R.id.view_mycard);
		view_reward = (LinearLayout) findViewById(R.id.view_reward_list);
		view_setting = (LinearLayout) findViewById(R.id.view_setting);

		layout_footer = (LinearLayout)findViewById(R.id.layout_footer);
		layout_footer_store = (LinearLayout) findViewById(R.id.layout_footer_store);
		layout_footer_mycard = (LinearLayout) findViewById(R.id.layout_footer_mycard);
		layout_footer_scan = (LinearLayout) findViewById(R.id.layout_footer_scan);
		layout_footer_reward = (LinearLayout) findViewById(R.id.layout_footer_reward);
		layout_footer_setting = (LinearLayout) findViewById(R.id.layout_footer_setting);
		iv_footer_store = (ImageView) findViewById(R.id.iv_footer_store);
		iv_footer_mycard = (ImageView) findViewById(R.id.iv_footer_mycard);
		iv_footer_reward = (ImageView) findViewById(R.id.iv_footer_myreward);
		iv_footer_setting = (ImageView) findViewById(R.id.iv_footer_setting);
	}

	private void setSuperUI() {
		setFooterClickBg();
		setFooterListener();
	}

	protected abstract void setFooterClickBg();
	private void setFooterListener(){
		layout_footer_store.setOnClickListener(new FooterClickListener());
		layout_footer_mycard.setOnClickListener(new FooterClickListener());
		layout_footer_scan.setOnClickListener(new FooterClickListener());
		layout_footer_reward.setOnClickListener(new FooterClickListener());
		layout_footer_setting.setOnClickListener(new FooterClickListener());
	}
	protected boolean childListener(){
		return false;
	}
	protected void registFinishSubActivity(){
		ImemberApplication.getInstance().getFinishActivityManager().registFinishSubActivity(this);
		regist_sub = true;
	}
	protected void registFinishMainActivity(){
		ImemberApplication.getInstance().getFinishActivityManager().registFinishMainActivity(this);
		regist_main = true;
	}

	protected class FooterClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if(childListener()){
				return;
			}
			Intent footerIntent = new Intent();
			switch (v.getId()) {
			case R.id.layout_footer_store:
				ImemberApplication.getInstance().setIndex_footer(
						ImemberApplication.FOOTER_ID_STORE);
				footerIntent.setClass(SuperActivity.this, StoreListActivity.class);
				ImemberApplication.getInstance().getFinishActivityManager().finishAllSubActivitys();
				SuperActivity.this.startActivity(footerIntent);
				SuperActivity.this.overridePendingTransition(0, 0);
//				SuperActivity.this.finish();
				break;
			case R.id.layout_footer_mycard:
				ImemberApplication.getInstance().setIndex_footer(
						ImemberApplication.FOOTER_ID_MYCARD);
				footerIntent.setClass(SuperActivity.this, MyCardListActivity.class);
				ImemberApplication.getInstance().getFinishActivityManager().finishAllSubActivitys();
				SuperActivity.this.startActivity(footerIntent);
				SuperActivity.this.overridePendingTransition(0, 0);
//				SuperActivity.this.finish();
				break;
			case R.id.layout_footer_scan:
//				ImemberApplication.getInstance().setIndex_footer(
//						ImemberApplication.FOOTER_ID_SCAN);
				footerIntent.setClass(SuperActivity.this, CaptureActivity.class);
				if(ImemberApplication.getInstance().isLogon()){
					SuperActivity.this.startActivity(footerIntent);
					SuperActivity.this.overridePendingTransition(0, 0);
				}
				else{
					Toast.makeText(SuperActivity.this, "您还未登录", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.layout_footer_reward:
				ImemberApplication.getInstance().setIndex_footer(
						ImemberApplication.FOOTER_ID_REWARD);
				footerIntent.setClass(SuperActivity.this,
						MyRewardListActivity.class);
				ImemberApplication.getInstance().getFinishActivityManager().finishAllSubActivitys();
				SuperActivity.this.startActivity(footerIntent);
				SuperActivity.this.overridePendingTransition(0, 0);
//				SuperActivity.this.finish();
				break;
			case R.id.layout_footer_setting:
				ImemberApplication.getInstance().setIndex_footer(
						ImemberApplication.FOOTER_ID_SETTING);
				footerIntent.setClass(SuperActivity.this, SettingActivity.class);
				ImemberApplication.getInstance().getFinishActivityManager().finishAllSubActivitys();
				SuperActivity.this.startActivity(footerIntent);
				SuperActivity.this.overridePendingTransition(0, 0);
//				SuperActivity.this.finish();
				break;
			}

		}

	}

	protected void initData1() {
		layout_footer_store.setBackgroundResource(R.drawable.footer_over_bg);
		layout_footer_mycard.setBackgroundResource(R.drawable.footer_bg);
		layout_footer_reward.setBackgroundResource(R.drawable.footer_bg);
		layout_footer_setting.setBackgroundResource(R.drawable.footer_bg);

		iv_footer_store.setBackgroundResource(R.drawable.footer_store_click);
		iv_footer_mycard.setBackgroundResource(R.drawable.footer_mycard_bg);
		iv_footer_reward.setBackgroundResource(R.drawable.footer_myreward_bg);
		iv_footer_setting.setBackgroundResource(R.drawable.footer_setting_bg);
	}

	protected void setFooterMycardClick() {
		layout_footer_store.setBackgroundResource(R.drawable.footer_bg);
		layout_footer_mycard.setBackgroundResource(R.drawable.footer_over_bg);
		layout_footer_reward.setBackgroundResource(R.drawable.footer_bg);
		layout_footer_setting.setBackgroundResource(R.drawable.footer_bg);

		iv_footer_store.setBackgroundResource(R.drawable.footer_store_bg);
		iv_footer_mycard.setBackgroundResource(R.drawable.footer_mycard_click);
		iv_footer_reward.setBackgroundResource(R.drawable.footer_myreward_bg);
		iv_footer_setting.setBackgroundResource(R.drawable.footer_setting_bg);
	}

	protected void setFooterSettingClick() {
		layout_footer_store.setBackgroundResource(R.drawable.footer_bg);
		layout_footer_mycard.setBackgroundResource(R.drawable.footer_bg);
		layout_footer_reward.setBackgroundResource(R.drawable.footer_bg);
		layout_footer_setting.setBackgroundResource(R.drawable.footer_over_bg);

		iv_footer_store.setBackgroundResource(R.drawable.footer_store_bg);
		iv_footer_mycard.setBackgroundResource(R.drawable.footer_mycard_bg);
		iv_footer_reward.setBackgroundResource(R.drawable.footer_myreward_bg);
		iv_footer_setting
				.setBackgroundResource(R.drawable.footer_setting_click);
	}

	protected void setFooterRewardClick() {
		layout_footer_store.setBackgroundResource(R.drawable.footer_bg);
		layout_footer_mycard.setBackgroundResource(R.drawable.footer_bg);
		layout_footer_reward.setBackgroundResource(R.drawable.footer_over_bg);
		layout_footer_setting.setBackgroundResource(R.drawable.footer_bg);

		iv_footer_store.setBackgroundResource(R.drawable.footer_store_bg);
		iv_footer_mycard.setBackgroundResource(R.drawable.footer_mycard_bg);
		iv_footer_reward
				.setBackgroundResource(R.drawable.footer_myreward_click);
		iv_footer_setting.setBackgroundResource(R.drawable.footer_setting_bg);
	}

	protected void setFooterScanClick() {
		layout_footer_store.setBackgroundResource(R.drawable.footer_bg);
		layout_footer_mycard.setBackgroundResource(R.drawable.footer_bg);
		layout_footer_reward.setBackgroundResource(R.drawable.footer_bg);
		layout_footer_setting.setBackgroundResource(R.drawable.footer_bg);

		iv_footer_store.setBackgroundResource(R.drawable.footer_store_bg);
		iv_footer_mycard.setBackgroundResource(R.drawable.footer_mycard_bg);
		iv_footer_reward.setBackgroundResource(R.drawable.footer_myreward_bg);
		iv_footer_setting.setBackgroundResource(R.drawable.footer_setting_bg);
	}

	@Override
	public void finishActivity() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTasks = am.getRunningTasks(1);
		RunningTaskInfo rti = runningTasks.get(0);
		ComponentName component = rti.topActivity;

		System.out.println(am.getClass().getName());

		if(regist_sub){
			ImemberApplication.getInstance().getFinishActivityManager().removeFinishSubActivity(this);
		}
		if(regist_main){
			ImemberApplication.getInstance().getFinishActivityManager().removeFinishMainActivity(this);
		}
	}
}
