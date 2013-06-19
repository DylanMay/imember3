package com.yundong.imember.datamanager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.activity.StoreCardDetailActivity;
import com.yundong.imember.customWidget.MyDialog;
import com.yundong.imember.entity.Store;
import com.yundong.imember.entity.StoreCard;
import com.yundong.imember.http.GetServiceDataHandler;
import com.yundong.imember.http.GetServiceDataHandlerListener;
import com.yundong.imember.thread.MyThread_get;

public class StoreCardList_loadManager {
	private Context _context;
	private MyDialog _myDialog;
	private Store store;
	private GetServiceDataHandler handler_cardList;
	private String _title;

	public StoreCardList_loadManager(Context context, final Store store) {
		_context = context;
		this.store = store;
		
		handler_cardList = new GetServiceDataHandler("商家会员卡列表", 
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
//							myDialog.setMessage(ImemberApplication.result_status.get(recode));
							myDialog.setMessage("商家还没开通会员卡功能");
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
//					String data1 = new JSONObject(data).getString("data");
//					JSONArray array = new JSONArray(new JSONObject(data1).getString("list"));
					
					JSONArray array = new JSONArray(new JSONObject(data).getString("list"));
					
					int mcard_id=0;
					for (int i = 0; i < array.length(); i++) {
						JSONObject object_store = array.getJSONObject(i);
						String card_logo_url = object_store.getString("c_url");
						String card_title = object_store.getString("s_name");
						String card_name = object_store.getString("c_name");
						String c_description = object_store.getString("c_description");
						String c_servies_terms = object_store.getString("c_servies_terms");
						mcard_id = object_store.getInt("mcard_id");
						int c_status = object_store.getInt("c_status");
						String s_status = object_store.getString("s_status");
						String s_r_status = object_store.getString("s_r_status");
						String stamp_id = object_store.getString("stamp_id");
						
						StoreCard card = new StoreCard();
						if(stamp_id != null){
							if(!"".equals(stamp_id)){
								int id = Integer.parseInt(stamp_id);
								if(id > 0){
									String s_name = object_store.getString("s_name");
									String s_description = object_store.getString("s_description");
									String s_r_name = object_store.getString("s_r_name");
									String s_r_description = object_store.getString("s_r_description");
									
									card.setStamp_id(id);
									card.setS_name(s_name);
									card.setS_description(s_description);
									card.setS_r_name(s_r_name);
									card.setS_r_description(s_r_description);
								}
							}
						}
						card.setCard_logo_url(card_logo_url);
						card.setCard_title(_title);
						card.setCard_name(card_name);
						card.setC_description(c_description);
						card.setC_servies_terms(c_servies_terms);
						card.setMcard_id(mcard_id);
						card.setC_status(c_status);
						if(s_status != null && !"".equals(s_status)){
							card.setS_status(Integer.parseInt(s_status));
						}
						else{
							card.setS_status(0);
						}
						if(s_r_status != null && !"".equals(s_r_status)){
							card.setS_r_status(Integer.parseInt(s_r_status));
						}
						else{
							card.setS_r_status(0);
						}
						
						ImemberApplication.getInstance().storeCards.add(card);
					}
//					Intent intent = new Intent();
//					intent.putExtra("store", StoreCardList_loadManager.this.store);
//					intent.setClass(_context, StoreCardListActivity.class);
//					_context.startActivity(intent);
					
					Intent intent = new Intent();
					StoreCard card = ImemberApplication.getInstance().storeCards.get(0);
					intent.putExtra("card", card);
					intent.putExtra("store", store);
					intent.setClass(_context, StoreCardDetailActivity.class);
					_context.startActivity(intent);
					
//					new MyCardDetail_loadManager(_context).load(mcard_id);
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
	
	private String createJson(){
		JSONObject object = new JSONObject();
		try {
			object.put("account_id", "" + store.getStore_account_id());
			
			return ImemberApplication.encode(object.toString(), "UTF-8");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public void load(String title){
		_myDialog = new MyDialog(_context);
		_myDialog.setTitle("正在加载数据...");
		_myDialog.setIcon(android.R.drawable.ic_dialog_info);
		_myDialog.setCancelable(false);
		_myDialog.setLoad();
		_myDialog.setLeftButton("取消", new MyDialog.MyOnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				handler_cardList.setIsCancel(true);
			}
			
		});
		_myDialog.create();
		_myDialog.show();
		
		_title = title;
		
		String url = ImemberApplication.WEBROOT + ImemberApplication.URL_STORECARDLIST + createJson();
		System.out.println("cardList_url--------->" + url);
		new MyThread_get(handler_cardList, url, _context).start();
	}
	
//	public void load1(){
//		MyCard myCard = new MyCard();
//		myCard.setCard_title("沙夫豪森IWC万国表");
//		myCard.setCard_name("IWC至尊会员卡");
//		myCard.setCard_effectivedate("2012-12-11至2013-02-21");
//		ImemberApplication.getInstance().myCards.add(myCard);
//		
//		myCard = new MyCard();
//		myCard.setCard_title("Nike-耐克");
//		myCard.setCard_name("耐克至尊会员卡");
//		myCard.setCard_effectivedate("2012-12-11至2013-02-21");
//		ImemberApplication.getInstance().myCards.add(myCard);
//		
//		_listAdapter_storecard.notifyDataSetChanged();
//	}
	
}
