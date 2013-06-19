/**
 * 
 */
package com.yundong.imember.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.R;

/**
 * @author Marcin Gil
 *
 */
public class SplashscreenActivity extends Activity {
	public final static String FIRST_RUN_PREFERENCE = "first_run";
	
	private Animation endAnimation;
	
	private Handler endAnimationHandler;
	private Runnable endAnimationRunnable;
	
	private Display display;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		display = getWindowManager().getDefaultDisplay(); 
		ImemberApplication.SCREEN_WIDTH = display.getWidth();
		ImemberApplication.SCREEN_HEIGHT = display.getHeight();
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splashscreen);
		findViewById(R.id.splashlayout);

		endAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		endAnimation.setFillAfter(true);
		
		endAnimationHandler = new Handler();
		endAnimationRunnable = new Runnable() {
			@Override
			public void run() {
				findViewById(R.id.splashlayout).startAnimation(endAnimation);
			}
		};
		
		endAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {	}
			
			@Override
			public void onAnimationRepeat(Animation animation) { }
			
			@Override
			public void onAnimationEnd(Animation animation) {
				boolean showTutorial = PreferenceManager.getDefaultSharedPreferences(SplashscreenActivity.this).getBoolean(FIRST_RUN_PREFERENCE, true);
				if (showTutorial) {
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SplashscreenActivity.this);
					prefs.edit().putBoolean(FIRST_RUN_PREFERENCE, false).commit();
					startActivity(new Intent(SplashscreenActivity.this, LeadActivity.class));
					SplashscreenActivity.this.finish();
				}
				else{
					startActivity(new Intent(SplashscreenActivity.this, StoreListActivity.class));
					SplashscreenActivity.this.finish();
				}
			}
		});
		endAnimationHandler.removeCallbacks(endAnimationRunnable);
		endAnimationHandler.postDelayed(endAnimationRunnable, 2000);
//		showTutorial();
	}
	
	/*
	 * 弹出软件说明对话框
	 */
//	final void showTutorial() {
//		/*
//		 * 从Preference中得到共享的全局配置信息，这里是保存是否弹出对话框。
//		 */
//		boolean showTutorial = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(FIRST_RUN_PREFERENCE, true);
//		if (showTutorial) {
//			final TutorialDialog dlg = new TutorialDialog(this);
//			dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
//				@Override
//				public void onDismiss(DialogInterface dialog) {
//					CheckBox cb = (CheckBox) dlg.findViewById(R.id.toggleFirstRun);
//					if (cb != null && cb.isChecked()) {
//						SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SplashscreenActivity.this);
//						prefs.edit().putBoolean(FIRST_RUN_PREFERENCE, false).commit();
//					}
//					endAnimationHandler.removeCallbacks(endAnimationRunnable);
//					endAnimationHandler.postDelayed(endAnimationRunnable, 2000);
//				}
//			});
//			dlg.show();
//
//		} else {
//			endAnimationHandler.removeCallbacks(endAnimationRunnable);
//			endAnimationHandler.postDelayed(endAnimationRunnable, 1500);
//		}
//	}
}
