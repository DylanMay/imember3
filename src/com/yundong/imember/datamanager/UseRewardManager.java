package com.yundong.imember.datamanager;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.activity.UseRewardDetailActivity;
import com.yundong.imember.customWidget.MyDialog;
import com.yundong.imember.entity.User;
import com.yundong.imember.http.GetServiceDataHandler;
import com.yundong.imember.http.GetServiceDataHandlerListener;
import com.yundong.imember.thread.MyThread_get;

public class UseRewardManager {
	private Context _context;
	private MyDialog myDialog;
	
	public UseRewardManager(Context context) {
		_context = context;
	}
	
	public void excute(int u_reward_id){
		createProcessDialog();
		String url = ImemberApplication.WEBROOT + ImemberApplication.URL_USEREWARD + createJson(u_reward_id);
		new MyThread_get(handler_usereward, url, _context).start();
	}
	
	private String createJson(int u_reward_id){
		User user = ImemberApplication.getInstance().getUser();
		JSONObject object = new JSONObject();
		try {
			object.put("user_id", user.getUser_id());
			object.put("user_pwd", user.getPassword());
			object.put("u_reward_id", u_reward_id);
			
			return ImemberApplication.encode(object.toString(), "UTF-8");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	private GetServiceDataHandler handler_usereward = new GetServiceDataHandler("使用奖励", new GetServiceDataHandlerListener(){

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
				
				String data = object.getString("data");
				JSONObject object1 = new JSONObject(data);
				int use_status = object1.getInt("use_status");
				String r_redeemed_time = object1.getString("r_redeemed_time");
				
				if(use_status == 1){
					UseRewardDetailActivity.btn_use_reward.setVisibility(View.GONE);
					UseRewardDetailActivity.tv_status.setText("已使用");
					UseRewardDetailActivity.tv_date.setText("使用日期: \n" + r_redeemed_time);
					UseRewardDetailActivity.isUsed = true;
				}
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
				handler_usereward.setIsCancel(true);
			}
			
		});
		myDialog.create();
		myDialog.show();
	}
}
