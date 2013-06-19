package com.yundong.imember.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.R;
import com.yundong.imember.entity.City;

public class ChangeCityActivity extends SuperActivity {
	private LinearLayout layout;
	private Button btn_left;
	private TextView tv_currentcity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_changecity);
		super.onCreate(savedInstanceState);
		
//		getIntentData();
		initUI();
		setUI();
	}
//	private void getIntentData(){
//		Intent intent = getIntent();
//		Bundle bundle = intent.getExtras();
//		if(bundle != null){
//			citys = (ArrayList<City>) bundle.get("citys");
//		}
//	}
	private void initUI(){
		registFinishSubActivity();
		
		layout = (LinearLayout)findViewById(R.id.changecity_layout);
		btn_left = (Button)findViewById(R.id.btn_header_left);
		tv_currentcity = (TextView)findViewById(R.id.tv_currentcity);
		
		createCitysLayout();
	}
	private void setUI(){
		City city = ImemberApplication.getInstance().getGpsCity();
		if(city != null){
			tv_currentcity.setText("" + city.getCity_name());
		}
		
		btn_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	
	private void createCitysLayout(){
		if(ImemberApplication.getInstance().citys != null){
			for (int i = 0; i < ImemberApplication.getInstance().citys.size(); i++) {
				final City city = ImemberApplication.getInstance().citys.get(i);
				final String city_name = city.getCity_name();
				
				LinearLayout.LayoutParams layoutPrarams1 = new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				layoutPrarams1.gravity = Gravity.CENTER_VERTICAL;
	
				TextView tv_cityName = new TextView(this);
				tv_cityName.setLayoutParams(layoutPrarams1);
				tv_cityName.setText(city_name);
				tv_cityName.setPadding(25, 0, 25, 0);
				tv_cityName.setGravity(Gravity.CENTER_VERTICAL);
				tv_cityName.setLines(1);
				tv_cityName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
				tv_cityName.setTextColor(Color.GRAY);
				if(i == 0){
					tv_cityName.setBackgroundResource(R.drawable.box_item_top_bg);
				}
				else if(i == (ImemberApplication.getInstance().citys.size() - 1)){
					tv_cityName.setBackgroundResource(R.drawable.box_item_bottom_bg);
				}
				else{
					tv_cityName.setBackgroundResource(R.drawable.box_item_center_bg);
				}
				tv_cityName.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						ImemberApplication.getInstance().setNewCity(city);
						if(ImemberApplication.getInstance().isChange_city()){
							Toast.makeText(ChangeCityActivity.this, "当前城市:" + city_name, Toast.LENGTH_SHORT).show();
						}
						else{
//							Toast.makeText(ChangeCityActivity.this, "已是当前城市", Toast.LENGTH_SHORT).show();
							Toast.makeText(ChangeCityActivity.this, "当前城市:" + city_name, Toast.LENGTH_SHORT).show();
						}
						finish();
					}
				});
				
				layout.addView(tv_cityName);
				if(i < ImemberApplication.getInstance().citys.size() - 1){
					ImageView line = new ImageView(this);
					line.setLayoutParams(layoutPrarams1);
					line.setBackgroundResource(R.drawable.box_line_bg);
					
					layout.addView(line);
				}
			}
		}
	}
	@Override
	protected void setFooterClickBg() {
		// TODO Auto-generated method stub
		layout_footer_setting.setBackgroundResource(R.drawable.footer_over_bg);
		iv_footer_setting.setBackgroundResource(R.drawable.footer_setting_click);
	}
//	@Override
//	protected void setFooterListener() {
//		// TODO Auto-generated method stub
//		layout_footer_store.setOnClickListener(new FooterClickListener());
//		layout_footer_mycard.setOnClickListener(new FooterClickListener());
//		layout_footer_scan.setOnClickListener(new FooterClickListener());
//		layout_footer_reward.setOnClickListener(new FooterClickListener());
//	}
}
