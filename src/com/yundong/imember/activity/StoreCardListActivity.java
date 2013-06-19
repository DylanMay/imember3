package com.yundong.imember.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.R;
import com.yundong.imember.adapter.ListAdapter_storecard;
import com.yundong.imember.entity.Store;
import com.yundong.imember.entity.StoreCard;

public class StoreCardListActivity extends SuperActivity {
	private Button btn_left;
	
	private ListView listView_storecard;
	private ListAdapter_storecard listAdapter_storecard;
	
	private Store store;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_storecard_list);
		super.onCreate(savedInstanceState);
		
		getIntentData();
		initUI();
		setUI();
	}
	
	private void getIntentData(){
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null){
			store = (Store)bundle.get("store");
		}
	}
	
	private void initUI(){
		registFinishSubActivity();
		
		btn_left = (Button)findViewById(R.id.btn_header_left);
		listView_storecard = (ListView) findViewById(R.id.listview_storecard);
		listAdapter_storecard = new ListAdapter_storecard(this, listView_storecard);
		listView_storecard.setAdapter(listAdapter_storecard);
		listView_storecard.setOnItemClickListener(new ItemClickListener_storecard());
	}
	private void setUI(){
		btn_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	
	private class ItemClickListener_storecard implements OnItemClickListener {
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long id) {
			Intent intent = new Intent();
			StoreCard card = ImemberApplication.getInstance().storeCards.get(position);
			intent.putExtra("card", card);
			intent.putExtra("store", store);
			intent.setClass(StoreCardListActivity.this, StoreCardDetailActivity.class);
			startActivity(intent);
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
