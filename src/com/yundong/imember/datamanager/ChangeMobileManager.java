package com.yundong.imember.datamanager;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.customWidget.MyDialog;
import com.yundong.imember.entity.User;
import com.yundong.imember.http.GetServiceDataHandler;
import com.yundong.imember.http.GetServiceDataHandlerListener;
import com.yundong.imember.myinterface.SettingChangeSthListener;
import com.yundong.imember.thread.MyThread_get;

public class ChangeMobileManager {
	private Context _context;
	private MyDialog myDialog;
	private SettingChangeSthListener listener;
	
	private String mobile;
	
	public ChangeMobileManager(Context context, SettingChangeSthListener listener) {
		_context = context;
		this.listener = listener;
	}
	
	public void excute(String code, String msg_id, String mobile){
		this.mobile = mobile;
		createProcessDialog();
		String url = ImemberApplication.WEBROOT + ImemberApplication.URL_CHANGEMOBILE +
				createJson(code, msg_id, mobile);
		System.out.println("---更改Mobile---->" + url);
		new MyThread_get(handler_changemobile, url, _context).start();
	}
	
	private String createJson(String code, String msg_id, String mobile){
		JSONObject object = new JSONObject();
		try {
			object.put("user_id", ImemberApplication.getInstance().getUser().getUser_id());
			System.out.println("user_id1------>" + ImemberApplication.getInstance().getUser().getUser_id());
			object.put("user_pwd", ImemberApplication.getInstance().getUser().getPassword());
			object.put("verify_code", code);
			object.put("msg_id", msg_id);
			object.put("mobile", mobile);
			
			return ImemberApplication.encode(object.toString(), "UTF-8");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	private GetServiceDataHandler handler_changemobile = new GetServiceDataHandler("更改Mobile", new GetServiceDataHandlerListener(){

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
				String msg = object.getString("msg");
				
				if(recode != null){
					if(!ImemberApplication.RESULT_STATUS.result_basic_success.equals(recode)){
						MyDialog myDialog = new MyDialog(_context);
						myDialog.setTitle("提示");
						myDialog.setIcon(android.R.drawable.ic_dialog_info);
						myDialog.setCancelable(false);
						myDialog.setMessage(msg);
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
				ImemberApplication.getInstance().getUser().setMobile(mobile);
				save2Db(ImemberApplication.getInstance().getUser());
				Toast.makeText(_context, "绑定成功", Toast.LENGTH_SHORT).show();
				listener.changeSthSuccess();
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
	
	private void save2Db(User user){
		if(ImemberApplication.getInstance().getSQLite().query_userinfo() != null){
			ImemberApplication.getInstance().getSQLite().delete_userinfo();
		}
		ImemberApplication.getInstance().getSQLite().insert_userinfo(user);
		if(ImemberApplication.getInstance().getSQLite().query_username() != null){
			ImemberApplication.getInstance().getSQLite().delete_username();
		}
		ImemberApplication.getInstance().getSQLite().insert_username(user.getUser_name());
	}
	
	private void createProcessDialog(){
		myDialog = new MyDialog(_context);
		myDialog.setTitle("正在加载数据...");
		myDialog.setIcon(android.R.drawable.ic_dialog_info);
		myDialog.setCancelable(false);
		myDialog.setLoad();
		myDialog.setLeftButton("取消", new MyDialog.MyOnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				handler_changemobile.setIsCancel(true);
			}
			
		});
		myDialog.create();
		myDialog.show();
	}
}
