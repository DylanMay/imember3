package com.yundong.imember.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.yundong.imember.R;
import com.yundong.imember.customWidget.ImageAdapter;
import com.yundong.imember.customWidget.ViewFlow;
import com.yundong.imember.customWidget.ViewFlow.ViewSwitchListener;

public class LeadActivity extends Activity{
	private ViewFlow viewFlow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lead);
		
		viewFlow = (ViewFlow) findViewById(R.id.viewflow);
		viewFlow.setContext(this);
		viewFlow.setAdapter(new ImageAdapter(this), 0);
		
		viewFlow.setOnViewSwitchListener(new ViewSwitchListener() {
			
			@Override
			public void onSwitched(View view, int position) {
				// TODO Auto-generated method stub
				switch(position){
				case 0:
					viewFlow.setCanNextPage(false);
					System.out.println("ting---");
					break;
				case 1:
					viewFlow.setCanNextPage(false);
					System.out.println("ting---");
					break;
				case 2:
					viewFlow.setCanNextPage(true);
					System.out.println("ting---");
					break;
				}
			}
		});
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		viewFlow.onConfigurationChanged(newConfig);
	}
}
