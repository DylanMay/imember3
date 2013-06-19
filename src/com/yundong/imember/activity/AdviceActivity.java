package com.yundong.imember.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yundong.imember.R;
import com.yundong.imember.customWidget.MyDialog;
import com.yundong.imember.datamanager.FeedBackManager;

public class AdviceActivity extends SuperActivity {
	private Button btn_tel;
	private Button btn_refer;
	private Button btn_left;
	private EditText et_content;
	
	private String str_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_advice);
		super.onCreate(savedInstanceState);
		
		initUI();
		setUI();
	}
	
	private void initUI(){
		registFinishSubActivity();
		
		btn_left = (Button)findViewById(R.id.btn_header_left);
		btn_tel = (Button)findViewById(R.id.btn_advice_tel);
		btn_refer = (Button)findViewById(R.id.btn_advice_refer);
		et_content = (EditText)findViewById(R.id.advice_edit);
	}
	private void setUI(){
		btn_tel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				MyDialog myDialog = new MyDialog(AdviceActivity.this);
				myDialog.setTitle("提示");
				myDialog.setIcon(android.R.drawable.ic_dialog_info);
				myDialog.setCancelable(false);
				myDialog.setMessage("确定拨打电话？");
				myDialog.setLeftButton("确定", new MyDialog.MyOnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:02158763686"));
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
		btn_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btn_refer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setStr();
				if(notNull()){
					refer();
				}
			}
		});
	}
	
	private void setStr(){
		str_content = et_content.getText().toString().trim();
	}
	
	private boolean notNull(){
		if(str_content != null && !"".equals(str_content)){
			return true;
		}
		else{
			Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
		}
		return false;
	}
	private void refer(){
		new FeedBackManager(this).excute(str_content);
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
