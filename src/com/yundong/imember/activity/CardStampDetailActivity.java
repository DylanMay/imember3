package com.yundong.imember.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.R;
import com.yundong.imember.entity.Reward;
import com.yundong.imember.utils.AsyncLoadImageTask;

public class CardStampDetailActivity extends SuperActivity {
	private int stamp_now;
	private int stamp_max;
	private Reward reward;
	
	private Button btn_header_left;
	private TextView iv_storedetail_logo;
	private TextView tv_cardstamp_detail_title;
	private TextView tv_cardstamp_detail_stampnum;
	private TextView tv_s_description;
	private TextView tv_s_r_name;
	private TextView tv_s_effective_months;
	private LinearLayout layout_collection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_cardstamp);
		super.onCreate(savedInstanceState);
		
		getIntentData();
		initUI();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		
		Bundle bundle = intent.getExtras();
		if(bundle != null){
//			stamp_now = bundle.getInt("stamp_now");
			reward = (Reward) bundle.get("reward");
		}
		setUI();
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
		
		btn_header_left = (Button)findViewById(R.id.btn_header_left);
		iv_storedetail_logo = (TextView)findViewById(R.id.iv_storedetail_logo);
		tv_cardstamp_detail_title = (TextView)findViewById(R.id.tv_cardstamp_detail_title);
		tv_cardstamp_detail_stampnum = (TextView)findViewById(R.id.tv_cardstamp_detail_stampnum);
		tv_s_description = (TextView)findViewById(R.id.tv_s_description);
		tv_s_r_name = (TextView)findViewById(R.id.tv_s_r_name);
		tv_s_effective_months = (TextView)findViewById(R.id.tv_s_effective_months);
		layout_collection = (LinearLayout)findViewById(R.id.collection);
		
		setUI();
	}
	private void setUI(){
		if(reward != null){
			tv_cardstamp_detail_title.setText("" + reward.getS_name());
			tv_s_description.setText("" + reward.getS_description());
			tv_s_r_name.setText("" + reward.getReward_name());
			String s_effective_months = reward.getReward_effective_date();
			stamp_now = reward.getStamp_now();
			stamp_max = reward.getS_round();
			if("0".equals(s_effective_months)){
				tv_s_effective_months.setText("永久");
			}
			else{
				tv_s_effective_months.setText("获得后" + s_effective_months + "内有效");
			}
			setImage(reward.getLogo_url());
		}
		
//		if(stamp_now > 0 && stamp_now <= 5){
//			stamp_max = 5;
//		}
//		if(stamp_now > 5 && stamp_now <= 10){
//			stamp_max = 10;
//		}
//		if(stamp_now > 10 && stamp_now <= 15){
//			stamp_max = 15;
//		}
//		if(stamp_now > 15 && stamp_now <= 20){
//			stamp_max = 20;
//		}
//		if(stamp_now > 20 && stamp_now <= 25){
//			stamp_max = 25;
//		}
//		
//		if(stamp_now > 25 && stamp_now <= 30){
//			stamp_max = 30;
//		}
//		if(stamp_now > 30 && stamp_now <= 35){
//			stamp_max = 35;
//		}
//		if(stamp_now > 35 && stamp_now <= 40){
//			stamp_max = 40;
//		}
//		if(stamp_now > 40 && stamp_now <= 45){
//			stamp_max = 45;
//		}
//		if(stamp_now > 45 && stamp_now <= 50){
//			stamp_max = 50;
//		}
		btn_header_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv_cardstamp_detail_stampnum.setText("" + stamp_now + "/" + stamp_max);
		createCollections(stamp_now, stamp_max, layout_collection);
	}
	
	private void setImage(String imageUrl){
		AsyncLoadImageTask _asyncloader = new AsyncLoadImageTask();
		_asyncloader.loadDrawable(imageUrl,
				new AsyncLoadImageTask.ImageCallback() {
					public void imageLoaded(Drawable imageDrawable,String imageUrl) {
						if(imageDrawable != null){
							iv_storedetail_logo.setBackgroundDrawable(imageDrawable);
						}
						else{
//							iv_storedetail_logo.setBackgroundResource(R.drawable.default_store_logo);
						}
					}
				});
	}
	
	private void createCollections(int current, int max, LinearLayout layout){
		if(layout.getChildCount() > 0){
			layout.removeAllViews();
		}
		LinearLayout.LayoutParams layoutPrarams1 = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
		layoutPrarams1.topMargin = px;
		LinearLayout.LayoutParams layoutPrarams2 = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		layoutPrarams2.weight = 1;
		
		LinearLayout linearLayout_h = null;
		
		int hide_count = 0;
		if(max % 5 != 0){
			hide_count = 5 - max % 5;
		}
		max = max + hide_count;
		for (int i = 0; i < max; i++) {
			if(i % 5 == 0){
				linearLayout_h = new LinearLayout(this);
				linearLayout_h.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
				linearLayout_h.setLayoutParams(layoutPrarams1);
				linearLayout_h.setOrientation(LinearLayout.HORIZONTAL );
				
				layout.addView(linearLayout_h);
			}
			if(i < current){
				ImageView img_light = new ImageView(this);
				img_light.setLayoutParams(layoutPrarams2);
				img_light.setImageResource(R.drawable.collector_light);
				
				linearLayout_h.addView(img_light);
			}
			else if(i < (max - hide_count)){
				ImageView img_dark = new ImageView(this);
				img_dark.setLayoutParams(layoutPrarams2);
				img_dark.setImageResource(R.drawable.collector_dark);
				
				linearLayout_h.addView(img_dark);
			}
			else{
				ImageView img_hide = new ImageView(this);
				img_hide.setLayoutParams(layoutPrarams2);
				img_hide.setImageResource(R.drawable.collector_hide);
				
				linearLayout_h.addView(img_hide);
			}
			
			linearLayout_h.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					
				}
				
			});
		}
	}

	@Override
	protected void setFooterClickBg() {
		// TODO Auto-generated method stub
		switch(ImemberApplication.getInstance().getIndex_footer()){
		case ImemberApplication.FOOTER_ID_STORE:
			layout_footer_store.setBackgroundResource(R.drawable.footer_over_bg);
			iv_footer_store.setBackgroundResource(R.drawable.footer_store_click);
			break;
		case ImemberApplication.FOOTER_ID_MYCARD:
			layout_footer_mycard.setBackgroundResource(R.drawable.footer_over_bg);
			iv_footer_mycard.setBackgroundResource(R.drawable.footer_mycard_click);
			break;
		case ImemberApplication.FOOTER_ID_REWARD:
			layout_footer_reward.setBackgroundResource(R.drawable.footer_over_bg);
			iv_footer_reward.setBackgroundResource(R.drawable.footer_myreward_click);
			break;
		case ImemberApplication.FOOTER_ID_SETTING:
			layout_footer_setting.setBackgroundResource(R.drawable.footer_over_bg);
			iv_footer_setting.setBackgroundResource(R.drawable.footer_setting_click);
			break;
		}
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
//		case ImemberApplication.FOOTER_ID_SETTING:
//			layout_footer_store.setOnClickListener(new FooterClickListener());
//			layout_footer_mycard.setOnClickListener(new FooterClickListener());
//			layout_footer_scan.setOnClickListener(new FooterClickListener());
//			layout_footer_reward.setOnClickListener(new FooterClickListener());
//			break;
//		}
//	}
}
