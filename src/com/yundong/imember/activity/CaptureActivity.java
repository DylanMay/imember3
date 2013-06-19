package com.yundong.imember.activity;

import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.yundong.imember.ImemberApplication;
import com.yundong.imember.R;
import com.yundong.imember.viewfinder.camera.CameraManager;
import com.yundong.imember.viewfinder.decoding.CaptureActivityHandler;
import com.yundong.imember.viewfinder.decoding.InactivityTimer;
import com.yundong.imember.viewfinder.view.ViewfinderView;

public class CaptureActivity extends Activity implements Callback {
	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private TextView txtResult;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.40f;
	private boolean vibrate;
	
	private LinearLayout count_ly;
	private ImageView title_word_iv;
	
	private String str_scanType = "";
	private Button btn_bottom_close;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);
		
		getIntentData();
		//初始化 CameraManager
		CameraManager.init(getApplication());

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		count_ly = (LinearLayout)findViewById(R.id.scan_count);
		title_word_iv = (ImageView)findViewById(R.id.title_word);
		
	    	  count_ly.setVisibility(View.VISIBLE);
	    	  title_word_iv.setBackgroundResource(R.drawable.scan_title_word_count);
	    	  
		txtResult = (TextView) findViewById(R.id.txtResult);
		btn_bottom_close = (Button)findViewById(R.id.bottom_close);
		btn_bottom_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}
	
	private void getIntentData(){
		Intent intent = getIntent();
		if(intent != null){
			str_scanType = intent.getStringExtra("scanType");
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
		System.out.println("CaptureActivity--------------------->onDestroy");
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}
	
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	//扫描结果
	public void handleDecode(Result obj, Bitmap barcode) {
		inactivityTimer.onActivity();
		viewfinderView.drawResultBitmap(barcode);
		 playBeepSoundAndVibrate();
//		txtResult.setText(obj.getBarcodeFormat().toString() + ":"
//				+ obj.getText());
		 
		 String result = obj.getText();
		 if(result != null && !"".equals(result)){
//			 StaticData.SCAN_RESULT = result;
			 System.out.println("scanResult---->" + result);
		 
//			 Intent intent = new Intent();
//			 intent.putExtra("result", result);
//			 intent.putExtra("scanType", str_scanType);
			 ImemberApplication.getInstance().setScanSuccess(true);
			 ImemberApplication.getInstance().setScanResult(result);
			 ImemberApplication.getInstance().getFinishActivityManager().finishAllSubActivitys();
			 
//			 switch(ImemberApplication.getInstance().getIndex_footer()){
//				case ImemberApplication.FOOTER_ID_STORE:
//					intent.setClass(CaptureActivity.this, StoreListActivity.class);
//					startActivity(intent);
//					break;
//				case ImemberApplication.FOOTER_ID_MYCARD:
//					intent.setClass(CaptureActivity.this, MyCardListActivity.class);
//					startActivity(intent);
//					break;
//				case ImemberApplication.FOOTER_ID_REWARD:
//					intent.setClass(CaptureActivity.this, MyRewardListActivity.class);
//					startActivity(intent);
//					break;
//				case ImemberApplication.FOOTER_ID_SETTING:
//					intent.setClass(CaptureActivity.this, SettingActivity.class);
//					startActivity(intent);
//					break;
//				}
		 }
		 finish();
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
//			if(!StaticData.launch){
//				startActivity(new Intent(this, HomePageActivity.class));
//			}
			finish();
		}
		return false;
	}
}