package com.yundong.imember.activity;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.R;

public class DownloadCompleteActivity extends Activity {
	private Button install_ok;
	private Button install_cancel;
	private TextView tv_content;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_complete);
		
		initUI();
	}

	private void initUI(){
		install_ok = (Button)findViewById(R.id.install_ok);
		install_cancel = (Button)findViewById(R.id.install_cancel);
		tv_content = (TextView)findViewById(R.id.content);
		tv_content.setText("是否现在安装？\n如果不马上安装，则可手动安装，下载的文件保存在SD卡根目录，软件名称是" + ImemberApplication.getInstance().apk_name);
		
		install_ok.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent_install();
			}
			
		});
		install_cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
	}
	
	//安装软件
	private void intent_install(){
		Intent mIntent = new Intent(Intent.ACTION_VIEW);
		mIntent.setDataAndType(Uri.fromFile(new File("/sdcard/" + getApkName(this))),
			"application/vnd.android.package-archive");     
		startActivity(mIntent);
		finish();
	}
	//获取软件名
	public static String getApkName(Context context) {
        String verName = ImemberApplication.getInstance().apk_name;
        return verName;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("DownloadCompleteActivity----------------->onDestroy");
	}
}
