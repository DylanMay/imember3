package com.yundong.imember.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.R;
import com.yundong.imember.customWidget.MyDialog;
import com.yundong.imember.datamanager.StoreCardList_loadManager;
import com.yundong.imember.entity.Store;
import com.yundong.imember.utils.AsyncLoadImageTask;

public class StoreDetailActivity extends SuperActivity {
	private ImageView iv_storedetail_logo;
	private TextView tv_storedetail_title;
	private TextView tv_storedetail_catename;
	private TextView tv_storedetail_cityname;
	private TextView tv_storedetail_addr;
	private TextView tv_storedetail_tel;
	
	private LinearLayout layout_store_addr;
	private LinearLayout layout_store_other;
	private LinearLayout layout_store_card;
	private LinearLayout layout_store_tel;
	
	private Button btn_header_left;
	
	private Store store;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_store_detail);
		super.onCreate(savedInstanceState);
		
		getIntentData();
		initUI();
		setUI();
	}
	
	private void getIntentData(){
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			store = (Store) bundle.get("store");
		}
	}
	
	private void initUI(){
		registFinishSubActivity();
		
		iv_storedetail_logo = (ImageView)findViewById(R.id.iv_storedetail_logo);
		tv_storedetail_title = (TextView)findViewById(R.id.tv_storedetail_title);
		tv_storedetail_catename = (TextView)findViewById(R.id.tv_storedetail_catename);
		tv_storedetail_cityname = (TextView)findViewById(R.id.tv_storedetail_cityname);
		tv_storedetail_addr = (TextView)findViewById(R.id.tv_storedetail_addr);
		tv_storedetail_tel = (TextView)findViewById(R.id.tv_storedetail_tel);
		
		layout_store_addr = (LinearLayout)findViewById(R.id.layout_store_addr);
		layout_store_other = (LinearLayout)findViewById(R.id.layout_store_other);
		layout_store_card = (LinearLayout)findViewById(R.id.layout_store_card);
		layout_store_tel = (LinearLayout)findViewById(R.id.layout_store_tel);
		
		btn_header_left = (Button)findViewById(R.id.btn_header_left);
	}
	
	private void setUI(){
		if(store != null){
			tv_storedetail_title.setText("" + store.getStore_title());
			tv_storedetail_catename.setText("" + store.getCate_name());
//			tv_storedetail_cityname.setText("" + ImemberApplication.getInstance().getCurrentCity().getCity_name());
			tv_storedetail_cityname.setText("" + store.getCity_name());
			tv_storedetail_addr.setText("" + store.getStore_addr());
			tv_storedetail_tel.setText("" + store.getStore_tel());
			
			setImage(store.getStore_logo_url());
			
			layout_store_addr.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(StoreDetailActivity.this, Map.class);
					intent.putExtra("store", store);
					startActivity(intent);
				}
				
			});
			layout_store_other.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(StoreDetailActivity.this, StoreOtherListActivity.class);
					intent.putExtra("account_id", store.getStore_account_id());
					startActivity(intent);
				}
				
			});
			layout_store_card.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if(ImemberApplication.getInstance().storeCards.size() > 0){
						ImemberApplication.getInstance().storeCards.clear();
					}
					new StoreCardList_loadManager(StoreDetailActivity.this, store).load(store.getStore_title());
				}
			});
			layout_store_tel.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					MyDialog myDialog = new MyDialog(StoreDetailActivity.this);
					myDialog.setTitle("提示");
					myDialog.setIcon(android.R.drawable.ic_dialog_info);
					myDialog.setCancelable(false);
					myDialog.setMessage("确定拨打电话？");
					myDialog.setLeftButton("确定", new MyDialog.MyOnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + store.getStore_tel()));
							startActivity(intent);
						}
					});
					myDialog.setRightButton("取消", new MyDialog.MyOnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					myDialog.create();
					myDialog.show();
				}
				
			});
		}
		btn_header_left.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				finish();
			}
			
		});
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
							iv_storedetail_logo.setBackgroundResource(R.drawable.default_store_logo);
						}
					} 
				});
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
