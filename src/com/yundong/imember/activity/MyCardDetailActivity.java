package com.yundong.imember.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.R;
import com.yundong.imember.datamanager.RewardDetail_loadManager;
import com.yundong.imember.datamanager.StoreDetail_loadManager;
import com.yundong.imember.entity.MyCard;
import com.yundong.imember.entity.Reward;
import com.yundong.imember.entity.Store;
import com.yundong.imember.utils.AsyncLoadImageTask;

public class MyCardDetailActivity extends SuperActivity {
	private Button btn_header_left;
	private Button btn_storeinfo;
	private TextView tv_title;
	private TextView tv_cardNumber;
	private TextView tv_stamp_top_left;
	private TextView tv_stamp_top;
	private TextView tv_reward_top;
	private TextView tv_card_name;
	private TextView tv_c_seacription;
	private TextView tv_service_condition;
	private LinearLayout layout_stamp;
	private LinearLayout layout_reward;
	private ImageView iv_logo;
	
	private TextView tv_stamp_miaoshu;
	private TextView tv_reward_miaoshu;
	
	private LinearLayout layout_yinhua_miaoshu;
	private LinearLayout layout_reward_miaoshu;
	private LinearLayout layout_miaoshu;
	private LinearLayout layout_num;
	private LinearLayout layout_num_yinhua;
	private LinearLayout layout_num_reward;
	private Button btn_info;
	
	private MyCard card;
	private Reward reward;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_mycard_detail);
		super.onCreate(savedInstanceState);
		
		getIntentData();
		initUI();
		setUI();
	}
	
	private void getIntentData(){
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			card = (MyCard)bundle.get("card");
			reward = (Reward)bundle.get("reward");
		}
	}
	
	private void initUI(){
		registFinishSubActivity();
		
		btn_header_left = (Button)findViewById(R.id.btn_header_left);
		btn_storeinfo = (Button)findViewById(R.id.btn_storeinfo);
		tv_title = (TextView)findViewById(R.id.mycard_detail_title);
		tv_cardNumber = (TextView)findViewById(R.id.tv_cardNumber);
		tv_stamp_top_left = (TextView)findViewById(R.id.tv_stamp_top_left);
		tv_stamp_top = (TextView)findViewById(R.id.tv_stamp_top);
		tv_reward_top = (TextView)findViewById(R.id.tv_reward_top);
		tv_card_name = (TextView)findViewById(R.id.tv_cardname);
		tv_c_seacription = (TextView)findViewById(R.id.tv_c_seacription);
		tv_service_condition = (TextView)findViewById(R.id.tv_service_condition);
		layout_stamp = (LinearLayout)findViewById(R.id.layout_stamp);
		layout_reward = (LinearLayout)findViewById(R.id.layout_reward);
		iv_logo = (ImageView)findViewById(R.id.iv_logo);
		
		tv_stamp_miaoshu = (TextView)findViewById(R.id.tv_stamp_miaoshu);
		tv_reward_miaoshu = (TextView)findViewById(R.id.tv_reward_miaoshu);
		layout_yinhua_miaoshu = (LinearLayout)findViewById(R.id.layout_yinhua_miaoshu);
		layout_reward_miaoshu = (LinearLayout)findViewById(R.id.layout_reward_miaoshu);
		layout_miaoshu = (LinearLayout)findViewById(R.id.layout_miaoshu);
		layout_num = (LinearLayout)findViewById(R.id.layout_num);
		layout_num_yinhua = (LinearLayout)findViewById(R.id.layout_num_yinhua);
		layout_num_reward = (LinearLayout)findViewById(R.id.layout_num_reward);
		btn_info = (Button)findViewById(R.id.btn_info);
	}
	
	private void setUI(){
		tv_title.setText("" + card.getCard_name());
		int number = Integer.parseInt(card.getM_number());
		if(number > 0 && number < 10){
			tv_cardNumber.setText("0000 000" + number);
		}
		else if(number >= 10 && number < 100){
			tv_cardNumber.setText("0000 00" + number);
		}
		else if(number >= 100 && number < 1000){
			tv_cardNumber.setText("0000 0" + number);
		}
		else{
			tv_cardNumber.setText("0000 " + number);
		}
		
		
		layout_yinhua_miaoshu.setVisibility(View.VISIBLE);
		layout_reward_miaoshu.setVisibility(View.VISIBLE);
		
		tv_stamp_miaoshu.setText("" + reward.getS_description());
		tv_reward_miaoshu.setText("" + reward.getS_r_description());
		
		
		String stamp_str = "";
		int stamp_now = card.getStamp_now();
		if(stamp_now < 10){
			stamp_str = "0" + stamp_now;
		}
		String reward_str = "";
		int reward_now = card.getReward_now();
		if(reward_now < 10){
			reward_str = "0" + reward_now;
		}
		tv_stamp_top.setText("" + stamp_str + "/" + reward.getS_round());
		
		tv_reward_top.setText("" + reward_str);
		tv_card_name.setText("" + card.getCard_name());
		tv_c_seacription.setText("" + card.getC_seacription());
		tv_service_condition.setText("" + card.getC_servies_terms());
		
		setImage(card.getCard_logo_url());
		
		if(reward.getS_status() == 1){
			layout_stamp.setVisibility(View.VISIBLE);
		}
		else{
			layout_stamp.setVisibility(View.GONE);
		}
		layout_num_yinhua.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("s_status---------->" + reward.getS_status());
				if(reward.getS_status() == 1){				
	//				if(card.getStamp_now() > 0){
						Intent intent = new Intent();
						intent.putExtra("reward", reward);
						intent.setClass(MyCardDetailActivity.this, CardStampDetailActivity.class);
						startActivity(intent);
	//				}
				}
			}
		});
		if(reward.getS_r_status() == 1){	
			layout_reward.setVisibility(View.VISIBLE);
		}
		else{
			layout_reward.setVisibility(View.GONE);
		}
		layout_num_reward.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				if(card.getReward_now() > 0){
//					Intent intent = new Intent();
//					intent.putExtra("reward", reward);
//					intent.setClass(MyCardDetailActivity.this, UseRewardDetailActivity.class);
//					startActivity(intent);
//				}
//				if(reward.getS_r_status() == 1){	
					new RewardDetail_loadManager(MyCardDetailActivity.this).load(reward.getMcard_id(), card.getCard_name());
//				}
			}
		});
		btn_header_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_storeinfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				int account_id = card.getAccount_id();
				new StoreDetail_loadManager(MyCardDetailActivity.this).load(account_id);
				
//				Store store = null;
//				for (int i = 0; i < ImemberApplication.getInstance().stores.size(); i++) {
//					if(account_id == ImemberApplication.getInstance().stores.get(i).getStore_account_id()){
//						store = ImemberApplication.getInstance().stores.get(i);
//					}
//				}
//				Intent intent = new Intent();
//				intent.putExtra("store", store);
//				intent.setClass(MyCardDetailActivity.this, StoreDetailActivity.class);
//				startActivity(intent);
			}
		});
		btn_info.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!layout_miaoshu.isShown()){
					layout_miaoshu.setVisibility(View.VISIBLE);
					layout_num.setVisibility(View.GONE);
				}
				else{
					layout_miaoshu.setVisibility(View.GONE);
					layout_num.setVisibility(View.VISIBLE);
				}
			}
		});
		
		int setStamp_id = reward.getStamp_id();
//		int setStamp_id = 0;
		if(setStamp_id == 0){
			tv_stamp_top_left.setVisibility(View.VISIBLE);
			tv_stamp_top.setVisibility(View.INVISIBLE);
			
			tv_stamp_miaoshu.setText("该商家没有发布印花功能");
			tv_stamp_miaoshu.setTextColor(Color.parseColor("#ef7330"));
			tv_reward_miaoshu.setText("该商家当前没有发布奖赏");
			tv_reward_miaoshu.setTextColor(Color.parseColor("#ef7330"));
		}
	}

	private void setImage(String imageUrl){
		AsyncLoadImageTask _asyncloader = new AsyncLoadImageTask();
		_asyncloader.loadDrawable(imageUrl,
				new AsyncLoadImageTask.ImageCallback() {
					public void imageLoaded(Drawable imageDrawable,String imageUrl) {
						if(imageDrawable != null){
							iv_logo.setBackgroundDrawable(imageDrawable);
						}
						else{
//							iv_logo.setBackgroundResource(R.drawable.default_store_logo);
						}
					} 
				});
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
}
