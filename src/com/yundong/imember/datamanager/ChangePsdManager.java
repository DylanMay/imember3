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

public class ChangePsdManager {
	private Context _context;
	private MyDialog myDialog;
	private String str_newpsd;
	
	public ChangePsdManager(Context context) {
		_context = context;
	}
	
	public void excute(String str_currentpsd, String str_newpsd){
		this.str_newpsd = str_newpsd;
		createProcessDialog();
		String url = ImemberApplication.WEBROOT + ImemberApplication.URL_CHANGEPSD + createJson(str_currentpsd, str_newpsd);
		new MyThread_get(handler_changepsd, url, _context).start();
	}
	
	private String createJson(String str_currentpsd, String str_newpsd){
		JSONObject object = new JSONObject();
		try {
			User user = ImemberApplication.getInstance().getUser();
			object.put("user_id", user.getUser_id());
			object.put("old_pwd", str_currentpsd);
			object.put("new_pwd", str_newpsd);
			
			return ImemberApplication.encode(object.toString(), "UTF-8");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	private GetServiceDataHandler handler_changepsd = new GetServiceDataHandler("修改密码", new GetServiceDataHandlerListener(){

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
//				ImemberApplication.getInstance().setLogon(false);
//				ImemberApplication.getInstance().setAutoLogonToFalse();
////				ImemberApplication.getInstance().getLogonSuccessManager().notifyLogonFail(userName);
				
				ImemberApplication.getInstance().getUser().setPassword(str_newpsd);
				save2Db(ImemberApplication.getInstance().getUser());
				
				Toast.makeText(_context, msg, Toast.LENGTH_SHORT).show();
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
				handler_changepsd.setIsCancel(true);
			}
			
		});
		myDialog.create();
		myDialog.show();
	}
}
