package com.yundong.imember.http;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.customWidget.MyDialog;

public class GetServiceDataHandler extends Handler {
	private String systemOut_str;
	private GetServiceDataHandlerListener getDataHandlerListener;
	private Context _context;
	private boolean isCancel;

	public GetServiceDataHandler(String str, GetServiceDataHandlerListener listener, Context context) {
		// TODO Auto-generated constructor stub
		systemOut_str = str;
		getDataHandlerListener = listener;
		_context = context;
	}
	
	public void setIsCancel(boolean isCancel){
		this.isCancel = isCancel;
	}
	public boolean isCancel(){
		return isCancel;
	}
	
	@Override
	public void handleMessage(Message msg) {
		if(isCancel){
			return ;
		}
		
		String response = msg.getData().getString("RESPONSE").trim();
		boolean success = msg.getData().getBoolean("SUCCESS");
		String str = systemOut_str + "----------------->" + response;
		System.out.println(str);
//		CrashHandler.getInstance().printNetInfo(str);
		System.out.println("" + success);

		if(!success){
			int id = getDataHandlerListener.process_fail_handler(response);
			switch(id){
			case ImemberApplication.DIALOG_1BTN:
				createFailDialog_1btn(response);
				break;
			case ImemberApplication.DIALOG_2BTN:
				createFailDialog_2btn(response);
				break;
				default:
					break;
			}
		}
//		else if(response != null && !"".equals(response)){
		else if(response != null && !response.contains("<html>") && !"".equals(response)){
			getDataHandlerListener.process_success_handler(response);
		}
		else{
//			getDataHandlerListener.process_fail_handler(response);
//			createErrorDialog();
			
			int id = getDataHandlerListener.process_fail_handler(response);
			switch(id){
			case ImemberApplication.DIALOG_1BTN:
				createFailDialog_1btn(response);
				break;
			case ImemberApplication.DIALOG_2BTN:
				createFailDialog_2btn(response);
				break;
				default:
					break;
			}
		}
	}
	
	private void createErrorDialog(){
		if(!((Activity)_context).isFinishing()){
			final MyDialog myDialog = new MyDialog(_context);
			myDialog.setTitle("提示");
			myDialog.setIcon(android.R.drawable.ic_dialog_info);
			myDialog.setMessage("异常!");
			myDialog.setCancelable(false);
			myDialog.setLeftButton("确定", new MyDialog.MyOnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			});
			myDialog.create();
			myDialog.show();
		}
	}
	
	private void createFailDialog_1btn(String response){
		if(!((Activity)_context).isFinishing()){
			   final MyDialog myDialog = new MyDialog(_context);
				myDialog.setTitle("提示");
				myDialog.setIcon(android.R.drawable.ic_dialog_info);
				myDialog.setMessage("连接服务器超时!");
				myDialog.setCancelable(false);
				myDialog.setLeftButton("确定", new MyDialog.MyOnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						getDataHandlerListener.process_cancel_handler();
					}
				});
				myDialog.create();
				myDialog.show();
			}
	   }
	
	private void createFailDialog_2btn(String response){
		if(!((Activity)_context).isFinishing()){
		   final MyDialog myDialog = new MyDialog(_context);
			myDialog.setTitle("提示");
			myDialog.setIcon(android.R.drawable.ic_dialog_info);
			myDialog.setMessage("连接服务器超时!");
			myDialog.setCancelable(false);
			myDialog.setLeftButton("重试", new MyDialog.MyOnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					getDataHandlerListener.process_retry_handler();
				}
			});
			myDialog.setRightButton("取消", new MyDialog.MyOnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					getDataHandlerListener.process_cancel_handler();
				}
			});
			myDialog.create();
			myDialog.show();
	   }
	}
}
