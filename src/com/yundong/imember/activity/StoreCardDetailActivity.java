package com.yundong.imember.activity;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.R;
import com.yundong.imember.activity.SuperActivity.FooterClickListener;
import com.yundong.imember.datamanager.CardApplyManager;
import com.yundong.imember.datamanager.StoreDetail_loadManager;
import com.yundong.imember.entity.Store;
import com.yundong.imember.entity.StoreCard;
import com.yundong.imember.utils.AsyncLoadImageTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StoreCardDetailActivity extends SuperActivity {
	private Button btn_header_left;
	private Button btn_header_right;
	private Button btn_storeinfo;
	private TextView tv_iseffect;
//	private TextView tv_cardname;
	private TextView tv_title;
	private TextView tv_carddescription;
	private TextView tv_card_servies_terms;
	private ImageView iv_logo;
	private LinearLayout layout_stamp;
	private TextView tv_stamp_name;
	private TextView tv_stamp_miaoshu;
	private TextView tv_reward_name;
	private TextView tv_reward_miaoshu;
	private TextView tv_card_name;
	private TextView tv_c_seacription;
	
	private LinearLayout layout_yinhua_miaoshu;
	private LinearLayout layout_reward_miaoshu;
	
	private StoreCard card;
	private Store store;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_storecard_detail);
		super.onCreate(savedInstanceState);
		
		getIntentData();
		initUI();
		setUI();
	}
	
	private void getIntentData(){
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			card = (StoreCard) bundle.get("card");
			store = (Store)bundle.get("store");
		}
	}
	
	private void initUI(){
		registFinishSubActivity();
		
		btn_header_left = (Button)findViewById(R.id.btn_header_left);
		btn_header_right = (Button)findViewById(R.id.btn_header_right);
		btn_storeinfo = (Button)findViewById(R.id.btn_storeinfo);
		tv_iseffect = (TextView)findViewById(R.id.mycard_detail_iseffect);
		tv_title = (TextView)findViewById(R.id.mycard_detail_title);
		tv_carddescription = (TextView)findViewById(R.id.tv_carddescription);
		tv_card_servies_terms = (TextView)findViewById(R.id.tv_card_servies_terms);
		iv_logo = (ImageView)findViewById(R.id.iv_logo);
		layout_stamp = (LinearLayout)findViewById(R.id.layout_stamp);
		tv_stamp_name = (TextView)findViewById(R.id.tv_stamp_name);
		tv_stamp_miaoshu = (TextView)findViewById(R.id.tv_stamp_miaoshu);
		tv_reward_name = (TextView)findViewById(R.id.tv_reward_name);
		tv_reward_miaoshu = (TextView)findViewById(R.id.tv_reward_miaoshu);
		
		tv_card_name = (TextView)findViewById(R.id.tv_cardname);
		tv_c_seacription = (TextView)findViewById(R.id.tv_c_seacription);
		
		layout_yinhua_miaoshu = (LinearLayout)findViewById(R.id.layout_yinhua_miaoshu);
		layout_reward_miaoshu = (LinearLayout)findViewById(R.id.layout_reward_miaoshu);
	}
	
	private void setUI(){
		tv_title.setText("" + card.getCard_name());
		tv_card_name.setText("" + card.getCard_name());
		tv_c_seacription.setText("" + card.getC_description());
		
		int c_status = card.getC_status();
		if(c_status == 1){
			btn_header_right.setVisibility(View.VISIBLE);
			tv_iseffect.setText("有效中");
		}
		else{
			btn_header_right.setVisibility(View.GONE);
		}
		int s_status = card.getS_status();
		if(s_status == 1){
			layout_yinhua_miaoshu.setVisibility(View.VISIBLE);
		}
		else{
			layout_yinhua_miaoshu.setVisibility(View.GONE);
		}
		int s_r_status = card.getS_r_status();
		if(s_r_status == 1){
			layout_reward_miaoshu.setVisibility(View.VISIBLE);
		}
		else{
			layout_reward_miaoshu.setVisibility(View.GONE);
		}
		
		setImage(card.getCard_logo_url());
		tv_carddescription.setText("" + card.getC_description());
		tv_card_servies_terms.setText("" + card.getC_servies_terms());
		
		btn_header_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_header_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				System.out.println("c_status---------->" + card.getC_status());
				if(card.getC_status() != 2){
					apply();
				}
			}
		});
		btn_storeinfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				int account_id = store.getStore_account_id();
				new StoreDetail_loadManager(StoreCardDetailActivity.this).load(account_id);
				
//				Intent intent = new Intent();
//				intent.putExtra("store", store);
//				intent.setClass(StoreCardDetailActivity.this, StoreDetailActivity.class);
//				startActivity(intent);
			}
		});
		
		if(card.getStamp_id() > 0){
//			layout_stamp.setVisibility(View.VISIBLE);
			layout_stamp.setVisibility(View.GONE);
			tv_stamp_name.setText("" + card.getS_name());
			tv_stamp_miaoshu.setText("" + card.getS_description());
			tv_reward_name.setText("" + card.getS_r_name());
			tv_reward_miaoshu.setText("" + card.getS_r_description());
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
	
	private void apply(){
		if(ImemberApplication.getInstance().isLogon()){
			new CardApplyManager(this, btn_header_right).excute(card.getMcard_id());
		}
		else{
			Toast.makeText(this, "您还未登录", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void setFooterClickBg() {
		// TODO Auto-generated method stub
		layout_footer_store.setBackgroundResource(R.drawable.footer_over_bg);
		iv_footer_store.setBackgroundResource(R.drawable.footer_store_click);
	}

//	@Override
//	protected void setFooterListener() {
//		// TODO Auto-generated method stub
//		layout_footer_mycard.setOnClickListener(new FooterClickListener());
//		layout_footer_scan.setOnClickListener(new FooterClickListener());
//		layout_footer_reward.setOnClickListener(new FooterClickListener());
//		layout_footer_setting.setOnClickListener(new FooterClickListener());
//	}
}
