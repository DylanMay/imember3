package com.yundong.imember.datamanager;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.customWidget.MyDialog;
import com.yundong.imember.db.SQLite;
import com.yundong.imember.entity.User;
import com.yundong.imember.http.GetServiceDataHandler;
import com.yundong.imember.http.GetServiceDataHandlerListener;
import com.yundong.imember.thread.MyThread_get;

public class LogonManager {
	private Context _context;
	private MyDialog myDialog;
	private String userName;
	private String passWord;
//	private SQLite sqlite;
	
	public LogonManager(Context context) {
		_context = context;
//		sqlite = new SQLite(context);
	}
	
	public void setString(String username, String password){
		this.userName = username;
		this.passWord = password;
	}
	
	public void logon(){
//		new updateManager(_context).check();
		
		createProcessDialog();
		String url = ImemberApplication.WEBROOT + ImemberApplication.URL_LOGON + createJson();
		new MyThread_get(handler_logon, url, _context).start();
		
		System.out.println("登录url------>" + url);
	}
	
	private String createJson(){
		JSONObject object = new JSONObject();
		try {
			object.put("user_name", userName);
			object.put("user_pwd", passWord);
			object.put("l_ip", ImemberApplication.getInstance().getIpAddress());
			object.put("l_xpoint", ImemberApplication.getInstance().getLat());
			object.put("l_ypoint", ImemberApplication.getInstance().getLon());
			object.put("city_name", ImemberApplication.getInstance().getCurrentCity().getCity_name());
			object.put("l_version", ImemberApplication.getInstance().getVerName());
			
			return ImemberApplication.encode(object.toString(), "UTF-8");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	private GetServiceDataHandler handler_logon = new GetServiceDataHandler("登录", new GetServiceDataHandlerListener(){

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
					if(!ImemberApplication.RESULT_STATUS.result_logon_success.equals(recode)){
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
						
						ImemberApplication.getInstance().setLogon(false);
						ImemberApplication.getInstance().setAutoLogonToFalse();
						ImemberApplication.getInstance().getLogonSuccessManager().notifyLogonFail(userName);
						
						return ;
					}
				}
				else{
					return ;
				}
				String data = object.getString("data");
				JSONObject object1 = new JSONObject(data);
				String user_name = object1.getString("user_name");
				String email = object1.getString("email");
				int user_id = object1.getInt("user_id");
				String mobile = object1.getString("mobile");
				
				User user = new User();
				user.setUser_id(user_id);
				user.setUser_name(user_name);
				user.setPassword(passWord);
				user.setEmail(email);
				user.setMobile(mobile);
				
				ImemberApplication.getInstance().setLogon(true);
				ImemberApplication.getInstance().setUserInfo(user);
				ImemberApplication.getInstance().getLogonSuccessManager().notifyLogonSuccess();
				save2Db(user);
//				Toast.makeText(_context, "登录成功", Toast.LENGTH_SHORT).show();
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
				handler_logon.setIsCancel(true);
			}
			
		});
		myDialog.create();
		myDialog.show();
	}
}
