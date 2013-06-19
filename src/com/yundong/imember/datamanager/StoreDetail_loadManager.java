package com.yundong.imember.datamanager;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.activity.StoreDetailActivity;
import com.yundong.imember.customWidget.MyDialog;
import com.yundong.imember.entity.Store;
import com.yundong.imember.http.GetServiceDataHandler;
import com.yundong.imember.http.GetServiceDataHandlerListener;
import com.yundong.imember.thread.MyThread_get;

public class StoreDetail_loadManager {
	private Context _context;
	private MyDialog _myDialog;
	private GetServiceDataHandler handler_storeList;

	public StoreDetail_loadManager(Context context) {
		_context = context;
		
		handler_storeList = new GetServiceDataHandler("商户详情", 
				new GetServiceDataHandlerListener(){

			@Override
			public void process_cancel_handler() {
				// TODO Auto-generated method stub
				if(_myDialog != null){
					if (_myDialog.isShowing()) {
						_myDialog.dismiss();
						_myDialog = null;
					}
				}
			}

			@Override
			public int process_fail_handler(String response) {
				// TODO Auto-generated method stub
				if(_myDialog != null){
					if (_myDialog.isShowing()) {
						_myDialog.dismiss();
						_myDialog = null;
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
				try {
					JSONObject object = new JSONObject(response);
					String recode = object.getString("recode");
					if(recode != null){
						if(!ImemberApplication.RESULT_STATUS.result_status_success.equals(recode)){
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
					JSONObject object_store = new JSONObject(data);
					String store_logo_url = object_store.getString("l_preview");
					System.out.println("url----->" + store_logo_url);
					String store_name = object_store.getString("l_name");
					double xpoint = object_store.getDouble("l_xpoint");
					double ypoint = object_store.getDouble("l_ypoint");
					String store_addr = object_store.getString("l_address");
					String store_city_name = object_store.getString("l_city_name");
					String store_tel = object_store.getString("l_tel");
					String cate_name = object_store.getString("l_cate_name");
					int store_account_id = object_store.getInt("account_id");
					int cate_type_id = object_store.getInt("l_cate_type_id");
					
					Store store = new Store();
					store.setStore_account_id(store_account_id);
					store.setStore_logo_url(store_logo_url);
					store.setStore_title(store_name);
					store.setXpoint(xpoint);
					store.setYpoint(ypoint);
					store.setStore_addr("");
					store.setStore_addr(store_addr);
					store.setCity_name(store_city_name);
					store.setStore_tel(store_tel);
					store.setCate_name(cate_name);
					store.setCate_type_id(cate_type_id);

					Intent intent = new Intent();
					intent.putExtra("store", store);
					intent.setClass(_context, StoreDetailActivity.class);
					_context.startActivity(intent);
				}catch (JSONException e) {
					// TODO: handle exception
				}finally{
					if(_myDialog != null){
						if (_myDialog.isShowing()) {
							_myDialog.dismiss();
							_myDialog = null;
						}
					}
				}
			}
			
		}, context);
	}
	
	public void load(int account_id){
		_myDialog = new MyDialog(_context);
		_myDialog.setTitle("正在加载数据...");
		_myDialog.setIcon(android.R.drawable.ic_dialog_info);
		_myDialog.setCancelable(false);
		_myDialog.setLoad();
		_myDialog.setLeftButton("取消", new MyDialog.MyOnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				handler_storeList.setIsCancel(true);
			}
			
		});
		_myDialog.create();
		_myDialog.show();
		
		String url = ImemberApplication.WEBROOT + ImemberApplication.URL_STOREDETAIL + createJson(account_id);
		new MyThread_get(handler_storeList, url, _context).start();
	}
	
	private String createJson(int account_id){
		JSONObject object = new JSONObject();
		try {
			object.put("account_id", account_id);
			
			return ImemberApplication.encode(object.toString(), "UTF-8");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
}
