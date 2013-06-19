package com.yundong.imember.datamanager;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.customWidget.MyDialog;
import com.yundong.imember.entity.User;
import com.yundong.imember.http.GetServiceDataHandler;
import com.yundong.imember.http.GetServiceDataHandlerListener;
import com.yundong.imember.thread.MyThread_get;

public class ReferValidateManager {
	private Context _context;
	private MyDialog myDialog;
	private String mobile;
	private String code;
	private String msg_id;
	
	public ReferValidateManager(Context context) {
		_context = context;
	}
	
	public void setString(String mobile, String code, String msg_id){
		this.mobile = mobile;
		this.code = code;
		this.msg_id = msg_id;
	}
	
	public void excute(int type){
		createProcessDialog();
		String url = ImemberApplication.WEBROOT + ImemberApplication.URL_REFERVALIDATE + createJson(type);
		System.out.println("提交验证码url---->" + url);
		new MyThread_get(handler_refervalidate, url, _context).start();
	}
	
	private String createJson(int type){
		JSONObject object = new JSONObject();
		try {
			object.put("type", type);
			object.put("mobile", mobile);
			object.put("code", code);
			object.put("msg_id", msg_id);
			if(type == 1){
//				object.put("user_id", ImemberApplication.getInstance().getUser().getUser_id());
			}
			if(type == 2){
				object.put("user_id", ImemberApplication.getInstance().getUser().getUser_id());
				object.put("user_pwd", ImemberApplication.getInstance().getUser().getPassword());
			}
			
			return ImemberApplication.encode(object.toString(), "UTF-8");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	private GetServiceDataHandler handler_refervalidate = new GetServiceDataHandler("提交验证码", new GetServiceDataHandlerListener(){

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
//			System.out.println("login_result--->" + response);
			try {
				JSONObject object = new JSONObject(response);
				String recode = object.getString("recode");
				if(recode != null){
					if(!ImemberApplication.RESULT_STATUS.result_login_validate_success.equals(recode)){
						MyDialog myDialog = new MyDialog(_context);
						myDialog.setTitle("提示");
						myDialog.setIcon(android.R.drawable.ic_dialog_info);
						myDialog.setCancelable(false);
						myDialog.setMessage(ImemberApplication.result_status.get(recode));
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
				String data = object.getString("data");
				JSONObject object_data = new JSONObject(data);
				String user_id = object_data.getString("user_id");
				System.out.println("user_id2------->" + user_id);
				String msg_id = object_data.getString("msg_id");
				System.out.println("msg_id1---------->" + msg_id);
				String mobile = object_data.getString("mobile");
				
//				ImemberApplication.getInstance().getLoginSuccessManager().notifyLoginSuccess(mobile);
				Toast.makeText(_context, "验证成功", Toast.LENGTH_SHORT).show();
				
				Intent intent = new Intent();
				intent.putExtra("code", code);
				intent.putExtra("msg_id", msg_id);
				intent.putExtra("mobile", mobile);
				((Activity)_context).setResult(5, intent);
				((Activity)_context).finish();
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
				handler_refervalidate.setIsCancel(true);
			}
			
		});
		myDialog.create();
		myDialog.show();
	}
	
}
