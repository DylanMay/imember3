package com.yundong.imember.datamanager;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.customWidget.MyDialog;
import com.yundong.imember.http.GetServiceDataHandler;
import com.yundong.imember.http.GetServiceDataHandlerListener;
import com.yundong.imember.thread.MyThread_get;

public class FindPsdManager_Mobile {
	private Context _context;
	private MyDialog myDialog;
	
	public FindPsdManager_Mobile(Context context) {
		_context = context;
	}
	
	public void excute(String verify_code, String msg_id, String str_newpsd, String mobile){
		createProcessDialog();
		String url = ImemberApplication.WEBROOT + ImemberApplication.URL_FINDPSD_MOBILE +
				createJson(verify_code, msg_id, str_newpsd, mobile);
		System.out.println("---找回密码---->" + url);
		new MyThread_get(handler_findpsd, url, _context).start();
	}
	
	private String createJson(String verify_code, String msg_id, String str_newpsd, String mobile){
		JSONObject object = new JSONObject();
		try {
			object.put("verify_code", verify_code);
			object.put("msg_id", msg_id);
			object.put("new_pwd", str_newpsd);
			object.put("mobile", mobile);
			
			return ImemberApplication.encode(object.toString(), "UTF-8");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	private GetServiceDataHandler handler_findpsd = new GetServiceDataHandler("找回密码", new GetServiceDataHandlerListener(){

		@Override
		public void process_cancel_handler() {
			// TODO Auto-generated method stub
			if(myDialog != null){
				if (myDialog.isShowing()) {
					myDialog.dismiss();
					myDialog = null;
				}
			}
		}

		@Override
		public int process_fail_handler(String response) {
			// TODO Auto-generated method stub
			if(myDialog != null){
				if (myDialog.isShowing()) {
					myDialog.dismiss();
					myDialog = null;
				}
			}
			return ImemberApplication.DIALOG_1BTN;
		}

		@Override
		public void process_retry_handler() {
			// TODO Auto-generated method stub
		}

		@Override
		public void process_success_handler(String response) {
			// TODO Auto-generated method stub
			System.out.println("result--->" + response);
			try {
				JSONObject object = new JSONObject(response);
				String recode = object.getString("recode");
				String result = object.getString("msg");
				
				if(recode != null){
					if(!ImemberApplication.RESULT_STATUS.result_basic_success.equals(recode)){
						MyDialog myDialog = new MyDialog(_context);
						myDialog.setTitle("提示");
						myDialog.setIcon(android.R.drawable.ic_dialog_info);
						myDialog.setCancelable(false);
						myDialog.setMessage(result);
						myDialog.setLeftButton("知道了", new MyDialog.MyOnClickListener() {
		
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
							}
							
						});
						myDialog.create();
						myDialog.show();
						return ;
					}
				}
				else{
					return ;
				}
				MyDialog myDialog = new MyDialog(_context);
				myDialog.setTitle("提示");
				myDialog.setIcon(android.R.drawable.ic_dialog_info);
				myDialog.setCancelable(false);
				myDialog.setMessage(result);
				myDialog.setLeftButton("知道了", new MyDialog.MyOnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						((Activity)_context).setResult(5);
						((Activity)_context).finish();
					}
					
				});
				myDialog.create();
				myDialog.show();
			}catch (JSONException e) {
				// TODO: handle exception
			}finally{
				if(myDialog != null){
					if (myDialog.isShowing()) {
						myDialog.dismiss();
						myDialog = null;
					}
				}
			}
		}
		
	}, _context);
	
	private void createProcessDialog(){
		myDialog = new MyDialog(_context);
		myDialog.setTitle("正在加载数据...");
		myDialog.setIcon(android.R.drawable.ic_dialog_info);
		myDialog.setCancelable(false);
		myDialog.setLoad();
		myDialog.setLeftButton("取消", new MyDialog.MyOnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				handler_findpsd.setIsCancel(true);
			}
			
		});
		myDialog.create();
		myDialog.show();
	}
}
