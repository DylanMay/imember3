package com.yundong.imember.datamanager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.activity.StoreListActivity;
import com.yundong.imember.customWidget.MyDialog;
import com.yundong.imember.entity.Sort;
import com.yundong.imember.http.GetServiceDataHandler;
import com.yundong.imember.http.GetServiceDataHandlerListener;
import com.yundong.imember.thread.MyThread_get;

public class Sorts_loadManager_storelist {
	private StoreListActivity _context;
	private MyDialog _myDialog;
	private GetServiceDataHandler handler_sortList;

	public Sorts_loadManager_storelist(StoreListActivity context) {
		_context = context;
		handler_sortList = new GetServiceDataHandler("分类列表", 
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
				if(ImemberApplication.getInstance().sorts.size() > 0){
					ImemberApplication.getInstance().sorts.clear();
				}
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
					JSONArray array = new JSONArray(new JSONObject(data).getString("list"));
					
					for (int i = 0; i < array.length(); i++) {
						JSONObject object_store = array.getJSONObject(i);
						String sort_name = object_store.getString("name");
						int sort_id = object_store.getInt("id");
						String url_icon = object_store.getString("icon");
						
						Sort sort = new Sort();
						sort.setSort_id(sort_id);
						sort.setSort_name(sort_name);
						sort.setUrl_icon(url_icon);
						
						ImemberApplication.getInstance().sorts.add(sort);
					}
					_context.createItemDialog_sort();
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
	
	public void load(){
//		_myDialog = new MyDialog(_context);
//		_myDialog.setTitle("正在加载数据...");
//		_myDialog.setIcon(android.R.drawable.ic_dialog_info);
//		_myDialog.setCancelable(false);
//		_myDialog.setLoad();
//		_myDialog.setLeftButton("取消", new MyDialog.MyOnClickListener() {
//
//			@Override
//			public void onClick(View dialog, int which) {
//				handler_sortList.setIsCancel(true);
//			}
//			
//		});
//		_myDialog.create();
//		_myDialog.show();
		
		String url = ImemberApplication.WEBROOT + ImemberApplication.URL_SORTLIST;
		new MyThread_get(handler_sortList, url, _context).start();
	}
	
}
