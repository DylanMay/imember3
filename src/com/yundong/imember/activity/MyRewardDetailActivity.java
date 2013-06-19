package com.yundong.imember.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.R;
import com.yundong.imember.datamanager.RewardDetail_loadManager;
import com.yundong.imember.datamanager.UseRewardDetail_loadManager;
import com.yundong.imember.entity.Reward;
import com.yundong.imember.utils.AsyncLoadImageTask;

public class MyRewardDetailActivity extends SuperActivity {
	private TextView tv_title;
	private Button btn_left;
	private Button btn_right;
	private TextView tv_content;
	private ArrayList<Reward> rewards;
	private LinearLayout layout;
	private ImageView iv_myreward_detail_logo;
	
	private int mcard_id;
	private String card_name;
	
	
	private String[] str_status = {"全部", "未使用", "已经使用", "过期", "失效"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_myreward_detail);
		super.onCreate(savedInstanceState);
		
		getIntentData();
		initUI();
		setUI();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		
		Bundle bundle = intent.getExtras();
		if(bundle != null){
//			if(rewards.size() > 0){
//				rewards.clear();
//			}
			rewards = (ArrayList<Reward>) bundle.get("rewards");
			mcard_id = bundle.getInt("mcard_id");
			card_name = bundle.getString("card_name");
		}
		setUI();
	}
	
	private void getIntentData(){
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			rewards = (ArrayList<Reward>) bundle.get("rewards");
			mcard_id = bundle.getInt("mcard_id");
			card_name = bundle.getString("card_name");
		}
	}
	
	private void initUI(){
		registFinishSubActivity();
		
		btn_left = (Button)findViewById(R.id.btn_header_left);
		btn_right = (Button)findViewById(R.id.btn_header_right);
		tv_title = (TextView)findViewById(R.id.tv_myreward_detail_title);
		layout = (LinearLayout)findViewById(R.id.layout);
		iv_myreward_detail_logo = (ImageView)findViewById(R.id.iv_myreward_detail_logo);
		tv_content = (TextView)findViewById(R.id.tv_myreward_detail_content);
	}
	private void setUI(){
		if(layout.getChildCount() > 0){
			layout.removeAllViews();
		}
		for (int i = 0; i < rewards.size(); i++) {
			final Reward reward = rewards.get(i);
			
			LayoutInflater inflater = (LayoutInflater) this	.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout layout_reward = (LinearLayout)inflater.inflate(R.layout.layout_reward, null);
			
			TextView tv_rewardname = (TextView)layout_reward.findViewById(R.id.tv_rewardname);
			TextView tv_reward_content = (TextView)layout_reward.findViewById(R.id.reward_content);
			TextView tv_date = (TextView)layout_reward.findViewById(R.id.tv_date);
			TextView tv_status = (TextView)layout_reward.findViewById(R.id.tv_status);
			
			tv_title.setText("" + reward.getH_name());
			tv_content.setText("" + reward.getCard_name());
			tv_rewardname.setText("" + reward.getReward_name());
			tv_reward_content.setText(reward.getH_name());
			String date = reward.getReward_effective_date().trim();
			
			
			if("0".equals(date)){
				tv_date.setText("永久有效");
			}
			else if(!"".equals(date) && !"null".equals(date)){
				tv_date.setText("" + date);
			}
			else{
				tv_date.setText("");
			}
			
			
			int status = reward.getStatus();
			switch(status){
			case 0:
				tv_status.setText("未使用");
				break;
			case 1:
				tv_status.setText("已经使用");
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
			layout_reward.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
//					Intent intent = new Intent();
//					intent.putExtra("reward", reward);
//					intent.setClass(MyRewardDetailActivity.this, UseRewardDetailActivity.class);
//					startActivity(intent);
					
					new UseRewardDetail_loadManager(MyRewardDetailActivity.this).load(reward.getU_reward_id());
				}
			});
			layout.addView(layout_reward);
			
			setImage(reward.getLogo_url());
		}
		
		btn_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btn_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				createItemDialog_status();
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
            	new RewardDetail_loadManager(MyRewardDetailActivity.this).load(mcard_id, card_name);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//
			}
		}).create().show();
	}
	
	private void setImage(String imageUrl){
		AsyncLoadImageTask _asyncloader = new AsyncLoadImageTask();
		_asyncloader.loadDrawable(imageUrl,
				new AsyncLoadImageTask.ImageCallback() {
					public void imageLoaded(Drawable imageDrawable,String imageUrl) {
						if(imageDrawable != null){
							iv_myreward_detail_logo.setBackgroundDrawable(imageDrawable);
						}
						else{
							iv_myreward_detail_logo.setBackgroundResource(R.drawable.reward_default);
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
}
