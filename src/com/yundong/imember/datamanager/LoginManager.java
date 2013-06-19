package com.yundong.imember.datamanager;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.R;
import com.yundong.imember.customWidget.MyDialog;
import com.yundong.imember.db.SQLite;
import com.yundong.imember.entity.User;
import com.yundong.imember.http.GetServiceDataHandler;
import com.yundong.imember.http.GetServiceDataHandlerListener;
import com.yundong.imember.thread.MyThread_get;

public class LoginManager {
	private Context _context;
	private MyDialog myDialog;
	private int type;
	private String userName;
	private String email_or_mobile;
	private String passWord;
//	private String confirmPassWord;
	private SQLite sqlite;
	
	private String code;
	private String msg_id;
	
	public LoginManager(Context context) {
		_context = context;
		sqlite = new SQLite(context);
	}
	
	public void setString(int type, String username, String email_or_mobile, String password, String code, String msg_id){
		this.type = type;
		this.userName = username;
		this.email_or_mobile = email_or_mobile;
		this.passWord = password;
//		this.confirmPassWord = confirmPassWord;
		this.code = code;
		this.msg_id = msg_id;
	}
	
	public void login(){
		createProcessDialog();
		String url = ImemberApplication.WEBROOT + ImemberApplication.URL_LOGIN + createJson();
		System.out.println("注册url---->" + url);
		new MyThread_get(handler_login, url, _context).start();
	}
	
	private HashMap<String, String> createMap(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("c", "user");
		map.put("act", "register");
		map.put("req", createJson());
		
//		map.put("type", StoreApplication.encode("0", "UTF-8"));
//		map.put("user_name", StoreApplication.encode(userName, "UTF-8"));
//		map.put("user_pwd", StoreApplication.encode(confirmPassWord, "UTF-8"));
//		map.put("email", StoreApplication.encode(email, "UTF-8"));
		
		return map;
	}
	
	private String createJson(){
		JSONObject object = new JSONObject();
		switch(type){
		case 0:
			try {
				object.put("type", 0);
				object.put("user_name", userName);
				object.put("user_pwd", passWord);
				object.put("email", email_or_mobile);
				object.put("xpoint", ImemberApplication.getInstance().getLat());
				object.put("ypoint", ImemberApplication.getInstance().getLon());
				object.put("city_name", ImemberApplication.getInstance().getCurrentCity().getCity_name());
//				object.put("city_name", 
//						ImemberApplication.encode(ImemberApplication.getInstance().getCurrentCity().getCity_name(), "UTF-8"));
				object.put("ip", ImemberApplication.getInstance().getIpAddress());
				object.put("l_type", 2);
				object.put("l_version", _context.getResources().getString(R.string.version_interface));
				
//				return object.toString();
				return ImemberApplication.encode(object.toString(), "UTF-8");
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 1:
			try {
				object.put("type", 1);
				object.put("user_name", userName);
				object.put("user_pwd", passWord);
//				object.put("email", "");
				object.put("mobile", email_or_mobile);
				object.put("xpoint", ImemberApplication.getInstance().getLat());
				object.put("ypoint", ImemberApplication.getInstance().getLon());
				object.put("city_name", ImemberApplication.getInstance().getCurrentCity().getCity_name());
				object.put("ip", ImemberApplication.getInstance().getIpAddress());
				object.put("l_type", 2);
				object.put("l_version", _context.getResources().getString(R.string.version_interface));
				object.put("verify_code", code);
				object.put("msg_id", msg_id);
				
//				return object.toString();
				return ImemberApplication.encode(object.toString(), "UTF-8");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		return "";
	}
	
	private GetServiceDataHandler handler_login = new GetServiceDataHandler("注册", new GetServiceDataHandlerListener(){

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
			System.out.println("login_result--->" + response);
			try {
				JSONObject object = new JSONObject(response);
				String recode = object.getString("recode");
				if(recode != null){
					if(!ImemberApplication.RESULT_STATUS.result_login_success.equals(recode)){
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
				String user_name = object_data.getString("user_name");
				String email = object_data.getString("email");
				String create_time = object_data.getString("create_time");
				
				User user = new User();
				user.setUser_id(Integer.parseInt(user_id));
				user.setUser_name(user_name);
				user.setEmail(email);
				user.setCreateTime(create_time);
				
				ImemberApplication.getInstance().getLoginSuccessManager().notifyLoginSuccess(user_name, passWord);
				Toast.makeText(_context, "注册成功", Toast.LENGTH_SHORT).show();
				
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
				handler_login.setIsCancel(true);
			}
			
		});
		myDialog.create();
		myDialog.show();
	}
	
}
