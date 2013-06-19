package com.yundong.imember.datamanager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.adapter.ListAdapter_store;
import com.yundong.imember.customWidget.MyDialog;
import com.yundong.imember.customWidget.MyListView;
import com.yundong.imember.entity.Store;
import com.yundong.imember.http.GetServiceDataHandler;
import com.yundong.imember.http.GetServiceDataHandlerListener;
import com.yundong.imember.thread.MyThread_get;

public class StoreList_loadManager_sort {
	private Context _context;
	private MyDialog _myDialog;
	private MyListView _listView_store;
	private ListAdapter_store _listAdapter_store;
	private GetServiceDataHandler handler_storeList;
	private int _cate_id;

	public StoreList_loadManager_sort(Context context, MyListView listView_store, ListAdapter_store listAdapter_store
			) {
		_context = context;
		_listView_store = listView_store;
		_listAdapter_store = listAdapter_store;
		
		handler_storeList = new GetServiceDataHandler("商户列表", 
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
				_listView_store.onRefreshComplete();
				
				return ImemberApplication.DIALOG_1BTN;
			}

			@Override
			public void process_retry_handler() {
				// TODO Auto-generated method stub
			}

			@Override
			public void process_success_handler(String response) {
				if(ImemberApplication.getInstance().stores.size() > 0){
					ImemberApplication.getInstance().stores.clear();
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
						String store_logo_url = object_store.getString("preview");
						System.out.println("url----->" + store_logo_url);
						String store_name = object_store.getString("name");
						double xpoint = object_store.getDouble("xpoint");
						double ypoint = object_store.getDouble("ypoint");
						String store_addr = object_store.getString("address");
						String store_city_name = object_store.getString("city_name");
						String store_tel = object_store.getString("tel");
						String cate_name = object_store.getString("cate_name");
						int store_account_id = object_store.getInt("account_id");
						int cate_type_id = object_store.getInt("cate_type_id");
						
						Store store = new Store();
						store.setStore_account_id(store_account_id);
						store.setStore_logo_url(store_logo_url);
						store.setStore_title(store_name);
						store.setXpoint(xpoint);
						store.setYpoint(ypoint);
						store.setStore_addr(store_addr);
						store.setCity_name(store_city_name);
						store.setStore_tel(store_tel);
						store.setCate_name(cate_name);
						store.setCate_type_id(cate_type_id);
						
						ImemberApplication.getInstance().stores.add(store);
					}
					_listAdapter_store.notifyDataSetChanged();
				}catch (JSONException e) {
					// TODO: handle exception
				}finally{
					if(_myDialog != null){
						if (_myDialog.isShowing()) {
							_myDialog.dismiss();
							_myDialog = null;
						}
					}
					_listView_store.onRefreshComplete();
				}
			}
			
		}, context);
	}
	
	public void load(int cate_id){
		_cate_id = cate_id;
		_listView_store.setSelection(0);
		
		_listView_store.setState(MyListView.REFRESHING);
		_listView_store.changeHeaderViewByState();
		
		String url = ImemberApplication.WEBROOT + ImemberApplication.URL_SHOPLIST + createJson(_cate_id);
		System.out.println("urllllllllllllllllllll------------" + url);
		new MyThread_get(handler_storeList, url, _context).start();
	}
	
	private String createJson(int cate_id){
		JSONObject object = new JSONObject();
		try {
			object.put("city_id", ImemberApplication.getInstance().getCurrentCity().getCity_id());
			object.put("cate_id", cate_id);
			
			return ImemberApplication.encode(object.toString(), "UTF-8");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
}
