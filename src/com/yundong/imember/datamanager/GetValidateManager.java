package com.yundong.imember.datamanager;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.customWidget.MyDialog;
import com.yundong.imember.http.GetServiceDataHandler;
import com.yundong.imember.http.GetServiceDataHandlerListener;
import com.yundong.imember.thread.MyThread_get;

public class GetValidateManager {
	private Context _context;
	private MyDialog myDialog;
	private String mobile;
	
	public GetValidateManager(Context context) {
		_context = context;
	}
	
	public void setString(String mobile){
		this.mobile = mobile;
	}
	
	public void excute(int type){
		createProcessDialog();
		String url = ImemberApplication.WEBROOT + ImemberApplication.URL_GETVALIDATE + createJson(type);
		System.out.println("获取验证码url---->" + url);
		new MyThread_get(handler_getvalidate, url, _context).start();
	}
	
	private String createJson(int type){
		JSONObject object = new JSONObject();
		try {
			object.put("type", type);
			object.put("mobile", mobile);
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
	
	private GetServiceDataHandler handler_getvalidate = new GetServiceDataHandler("获取验证码", new GetServiceDataHandlerListener(){

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
//			System.out.println("getvalidate_result--->" + response);
			try {
				JSONObject object = new JSONObject(response);
				String recode = object.getString("recode");
				if(recode != null){
					if(!ImemberApplication.RESULT_STATUS.result_basic_success.equals(recode)){
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
				String mobile = object_data.getString("mobile");
				String msg_id = object_data.getString("msg_id");
				
				ImemberApplication.getInstance().setLogin_msg_id(msg_id);
				
				MyDialog myDialog = new MyDialog(_context);
				myDialog.setTitle("提示");
				myDialog.setIcon(android.R.drawable.ic_dialog_info);
				myDialog.setCancelable(false);
				myDialog.setMessage("验证码获取成功");
				myDialog.setLeftButton("知道了", new MyDialog.MyOnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
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
				handler_getvalidate.setIsCancel(true);
			}
			
		});
		myDialog.create();
		myDialog.show();
	}
	
}
