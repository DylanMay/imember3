package com.yundong.imember.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.yundong.imember.R;
import com.yundong.imember.customWidget.MyDialog;
import com.yundong.imember.datamanager.UseRewardManager;
import com.yundong.imember.entity.Reward;
import com.yundong.imember.utils.AsyncLoadImageTask;

public class UseRewardDetailActivity extends SuperActivity {
	private TextView tv_title;
	private Button btn_left;
	private TextView tv_logo;
	private TextView tv_rewardName;
	private TextView tv_reward_description;
	private TextView tv_apply_condition;
	public static TextView tv_date;
	public static TextView tv_status;
	public static Button btn_use_reward;
	private Reward reward;
	private TextView tv_r_code;
	
	private int status;
	
	public static boolean isUsed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_usereward_detail);
		super.onCreate(savedInstanceState);
		
		isUsed = false;
		
		getIntentData();
		initUI();
		setUI();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(status == 0){
			btn_use_reward.setVisibility(View.VISIBLE);
		}
		else{
			btn_use_reward.setVisibility(View.GONE);
		}
	}
	
	private void getIntentData(){
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			reward = (Reward) bundle.get("reward");
		}
	}
	
	private void initUI(){
		registFinishSubActivity();
		
		tv_logo = (TextView)findViewById(R.id.iv_usereward_detail_logo);
		tv_rewardName = (TextView)findViewById(R.id.rewardName);
		tv_reward_description = (TextView)findViewById(R.id.tv_reward_description);
		tv_apply_condition = (TextView)findViewById(R.id.tv_apply_condition);
		tv_date = (TextView)findViewById(R.id.tv_date);
		tv_status = (TextView)findViewById(R.id.tv_status);
		tv_title = (TextView)findViewById(R.id.tv_myreward_detail_title);
		btn_left = (Button)findViewById(R.id.btn_header_left);
		btn_use_reward = (Button)findViewById(R.id.use);
		tv_r_code = (TextView)findViewById(R.id.r_code);
	}
	private void setUI(){
		tv_rewardName.setText("" + reward.getReward_name());
		tv_reward_description.setText("" + reward.getReward_content());
//		System.out.println("--------apply2----->" + reward.getR_apply_condition());
		tv_apply_condition.setText("" + reward.getR_apply_condition());
		tv_title.setText("" + reward.getH_name());
		String date = reward.getReward_effective_date().trim();
		String r_redeemed_time = reward.getR_redeemed_time();
		
		tv_r_code.setText("" + reward.getR_code());
		if("0".equals(date)){
			tv_date.setText("永久有效");
		}
		else if(!"".equals(date) && !"null".equals(date)){
			tv_date.setText("" + date);
		}
		else{
			tv_date.setText("");
		}
		if(r_redeemed_time != null && !"".equals(r_redeemed_time)){
			tv_date.setText("使用日期: \n" + r_redeemed_time);
		}
		
		status = reward.getStatus();
		switch(status){
		case 0:
			tv_status.setText("有效");
			break;
		case 1:
			tv_status.setText("已使用");
			break;
		case 2:
			tv_status.setText("过期");
			break;
		case 3:
			tv_status.setText("失效");
			break;
			default:
				break;
		}
		
		setImage(reward.getLogo_url());
		
		btn_use_reward.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				MyDialog myDialog = new MyDialog(UseRewardDetailActivity.this);
				myDialog.setTitle("提示");
				myDialog.setIcon(android.R.drawable.ic_dialog_info);
				myDialog.setCancelable(false);
				myDialog.setMessage("请在商家面前使用奖赏以做当面确认");
				myDialog.setLeftButton("确认", new MyDialog.MyOnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						new UseRewardManager(UseRewardDetailActivity.this).excute(reward.getU_reward_id());
					}
					
				});
				myDialog.setRightButton("取消", new MyDialog.MyOnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
					
				});
				myDialog.create();
				myDialog.show();
			}
		});
		btn_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				if(isUsed){
					Intent intent = new Intent();
					intent.setClass(UseRewardDetailActivity.this, MyRewardListActivity.class);
					startActivity(intent);
				}
			}
		});
	}
	
	private void setImage(String imageUrl){
		AsyncLoadImageTask _asyncloader = new AsyncLoadImageTask();
		_asyncloader.loadDrawable(imageUrl,
				new AsyncLoadImageTask.ImageCallback() {
					public void imageLoaded(Drawable imageDrawable,String imageUrl) {
						if(imageDrawable != null){
							tv_logo.setBackgroundDrawable(imageDrawable);
						}
						else{
							tv_logo.setBackgroundResource(R.drawable.default_store_logo);
						}
					} 
				});
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
	
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		 if (keyCode == KeyEvent.KEYCODE_BACK) {
			 finish();
			 if(isUsed){
					Intent intent = new Intent();
					intent.setClass(UseRewardDetailActivity.this, MyRewardListActivity.class);
					startActivity(intent);
				}
            return true;
		 }
		 return super.onKeyDown(keyCode, event);
	};
}
