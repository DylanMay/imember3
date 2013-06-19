package com.yundong.imember.datamanager;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.customWidget.MyDialog;
import com.yundong.imember.entity.User;
import com.yundong.imember.http.GetServiceDataHandler;
import com.yundong.imember.http.GetServiceDataHandlerListener;
import com.yundong.imember.thread.MyThread_get;

public class CardApplyManager {
	private Context _context;
	private MyDialog myDialog;
	private Button btn_header_right;
	
	public CardApplyManager(Context context, Button btn_header_right) {
		_context = context;
		this.btn_header_right = btn_header_right;
	}
	
	public void excute(int mcard_id){
		createProcessDialog();
		String url = ImemberApplication.WEBROOT + ImemberApplication.URL_CARDAPPLY + createJson(mcard_id);
		new MyThread_get(handler_cardapply, url, _context).start();
	}
	
	private String createJson(int mcard_id){
		User user = ImemberApplication.getInstance().getUser();
		JSONObject object = new JSONObject();
		try {
			object.put("user_id", user.getUser_id());
			object.put("user_pwd", user.getPassword());
			object.put("mcard_id", mcard_id);
			
			return ImemberApplication.encode(object.toString(), "UTF-8");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	private GetServiceDataHandler handler_cardapply = new GetServiceDataHandler("找回密码", new GetServiceDataHandlerListener(){

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
				String result = object.getString("msg");
				String recode = object.getString("recode");
				if(recode != null){
					if(!"4003".equals(recode)){
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
				btn_header_right.setVisibility(View.GONE);
				
				String data = object.getString("data");
				JSONObject object_store = new JSONObject(data);
				int mcard_id = object_store.getInt("mcard_id");
				new MyCardDetail_loadManager(_context).load(mcard_id);
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
				handler_cardapply.setIsCancel(true);
			}
			
		});
		myDialog.create();
		myDialog.show();
	}
}
