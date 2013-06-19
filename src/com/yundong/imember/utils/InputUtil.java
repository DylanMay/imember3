package com.yundong.imember.utils;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class InputUtil {
	private boolean run=true;
	private static boolean running;
	private boolean canHide;
	private ImageView iv_sign;
	private LinearLayout layout_footer_scan;
	private LinearLayout layout_footer;
	private int old_y;
	private int[] location=new int[2];

	public InputUtil(ImageView iv_sign, LinearLayout layout_footer_scan, LinearLayout layout_footer) {
		// TODO Auto-generated constructor stub
		this.iv_sign = iv_sign;
		this.layout_footer_scan = layout_footer_scan;
		this.layout_footer = layout_footer;
		
	}
	
	public void start(){
		run = false;
//		if(!thread.isAlive()){
//			thread.start();
//		}
	}
	public void stop(){
		run = true;
	}
	
	public void setHide(boolean hide){
		canHide = hide;
	}
	
	Thread thread = new Thread(){
		public void run() {
			while(!running){
				if(!run){
//					System.out.println("-----------------»¹ÔÚ×ß--------------");
					try {
						if(old_y != 0){
//							handler.sendEmptyMessage(0);
						}
						else{
							iv_sign.getLocationOnScreen(location);
							old_y = location[1];
						}
//						System.out.println("------y----->" + old_y);
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
	};
	
//	public void setFooterVisible(){
//		layout_footer_scan.setVisibility(View.VISIBLE);
//		layout_footer.setVisibility(View.VISIBLE);
//	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			iv_sign.getLocationOnScreen(location);
//			System.out.println("location[1]------>" + location[1]);
			if(old_y == location[1]){
				layout_footer_scan.setVisibility(View.VISIBLE);
				layout_footer.setVisibility(View.VISIBLE);
				
//				if(canHide){
//					stop();
//					canHide = false;
//				}
			}
			else{
				layout_footer_scan.setVisibility(View.GONE);
				layout_footer.setVisibility(View.GONE);
			}
			if(canHide){
				stop();
				canHide = false;
				
				layout_footer_scan.setVisibility(View.VISIBLE);
				layout_footer.setVisibility(View.VISIBLE);
			}
		};
	};
}
