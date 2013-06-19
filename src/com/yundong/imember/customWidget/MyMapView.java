package com.yundong.imember.customWidget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.amap.mapapi.map.MapView;

public class MyMapView extends MapView {

	public MyMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		
		try{
			return super.drawChild(canvas, child, drawingTime);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void draw(Canvas canvas) {
		try{
			super.draw(canvas);
		}catch(Exception e){
			e.printStackTrace();
			return ;
		}
	}

	
}
