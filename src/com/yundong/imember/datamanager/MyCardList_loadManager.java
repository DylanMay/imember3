package com.yundong.imember.datamanager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.activity.LoginActivity;
import com.yundong.imember.activity.MyCardListActivity;
import com.yundong.imember.adapter.ListAdapter_mycard;
import com.yundong.imember.customWidget.MyDialog;
import com.yundong.imember.customWidget.MyListView;
import com.yundong.imember.entity.MyCard;
import com.yundong.imember.entity.User;
import com.yundong.imember.http.GetServiceDataHandler;
import com.yundong.imember.http.GetServiceDataHandlerListener;
import com.yundong.imember.thread.MyThread_get;

public class MyCardList_loadManager {
	private Context _context;
	private MyDialog _myDialog;
	private MyListView _listView_store;
	private ListAdapter_mycard _listAdapter_mycard;
	private GetServiceDataHandler handler_cardList;

	public MyCardList_loadManager(Context context, MyListView listView_store, ListAdapter_mycard listAdapter_mycard) {
		_context = context;
		_listView_store = listView_store;
		_listAdapter_mycard = listAdapter_mycard;
		
		handler_cardList = new GetServiceDataHandler("会员卡列表", 
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
				if(ImemberApplication.getInstance().myCards.size() > 0){
					ImemberApplication.getInstance().myCards.clear();
				}
				try {
					JSONObject object = new JSONObject(response);
					final String recode = object.getString("recode");
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
									if("0012".equals(recode)){
										if(ImemberApplication.getInstance().getSQLite().query_userinfo() != null){
											ImemberApplication.getInstance().getSQLite().delete_userinfo();
										}
										ImemberApplication.getInstance().setLogon(false);
										ImemberApplication.getInstance().setAutoLogonToFalse();
										Intent intent = new Intent();
										intent.setClass(_context, MyCardListActivity.class);
										_context.startActivity(intent);
									}
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
						String card_logo_url = object_store.getString("c_url");
						String card_title = object_store.getString("h_name");
						String card_name = object_store.getString("c_name");
						int mcard_id = object_store.getInt("mcard_id");
//						String c_servies_terms = object_store.getString("c_servies_terms");
						
						MyCard card = new MyCard();
						card.setCard_logo_url(card_logo_url);
						card.setCard_title(card_title);
						card.setCard_name(card_name);
						card.setMcard_id(mcard_id);
//						card.setC_servies_terms(c_servies_terms);
						
						ImemberApplication.getInstance().myCards.add(card);
					}
					_listAdapter_mycard.notifyDataSetChanged();
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
	
	private String createJson(){
		JSONObject object = new JSONObject();
		try {
			User user = ImemberApplication.getInstance().getUser();
			object.put("user_id", user.getUser_id());
			object.put("user_pwd", user.getPassword());
//			object.put("user_pwd", "h21322321");
			System.out.println("user_id" + user.getUser_id() + ", user_pwd" + user.getPassword());
			
			return ImemberApplication.encode(object.toString(), "UTF-8");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public void load(){
		_listView_store.setSelection(0);
		
		_listView_store.setState(MyListView.REFRESHING);
		_listView_store.changeHeaderViewByState();
		
		String url = ImemberApplication.WEBROOT + ImemberApplication.URL_MYCARDLIST + createJson();
		System.out.println("cardList_url--------->" + url);
		new MyThread_get(handler_cardList, url, _context).start();
	}
	
	public void load1(){
		_listView_store.setSelection(0);
		
		_listView_store.setState(MyListView.REFRESHING);
		_listView_store.changeHeaderViewByState();
		
		if(ImemberApplication.getInstance().myCards.size() > 0){
			ImemberApplication.getInstance().myCards.clear();
		}
		
		MyCard card = new MyCard();
//		card.setCard_logo_url(card_logo_url);
		card.setCard_title("沙夫豪森");
		card.setCard_name("IWC至尊会员卡");
		card.setCard_effectivedate("2012-12-11至2013-02-21");
		ImemberApplication.getInstance().myCards.add(card);
		
		MyCard card1 = new MyCard();
//		card.setCard_logo_url(card_logo_url);
		card1.setCard_title("沙夫豪森");
		card1.setCard_name("IWC至尊会员卡");
		card1.setCard_effectivedate("2012-12-11至2013-02-21");
		ImemberApplication.getInstance().myCards.add(card1);
		
		MyCard card2 = new MyCard();
//		card.setCard_logo_url(card_logo_url);
		card2.setCard_title("耐克");
		card2.setCard_name("耐克至尊会员卡");
		card2.setCard_effectivedate("2012-12-11至2013-02-21");
		ImemberApplication.getInstance().myCards.add(card2);
		
		_listAdapter_mycard.notifyDataSetChanged();
		_listView_store.onRefreshComplete();
	}
}
