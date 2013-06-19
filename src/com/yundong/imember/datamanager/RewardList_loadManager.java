package com.yundong.imember.datamanager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.activity.MyCardListActivity;
import com.yundong.imember.activity.MyRewardListActivity;
import com.yundong.imember.adapter.ListAdapter_reward;
import com.yundong.imember.customWidget.MyDialog;
import com.yundong.imember.customWidget.MyListView;
import com.yundong.imember.entity.Reward;
import com.yundong.imember.entity.User;
import com.yundong.imember.http.GetServiceDataHandler;
import com.yundong.imember.http.GetServiceDataHandlerListener;
import com.yundong.imember.thread.MyThread_get;

public class RewardList_loadManager {
	private Context _context;
	private MyDialog _myDialog;
	private MyListView _listView_store;
	private ListAdapter_reward _listAdapter_reward;
	private GetServiceDataHandler handler_rewardList;

	public RewardList_loadManager(Context context, MyListView listView_store, ListAdapter_reward listAdapter_reward) {
		_context = context;
		_listView_store = listView_store;
		_listAdapter_reward = listAdapter_reward;
		
		handler_rewardList = new GetServiceDataHandler("奖赏列表", 
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
				if(ImemberApplication.getInstance().rewards.size() > 0){
					ImemberApplication.getInstance().rewards.clear();
				}
				try {
					JSONObject object = new JSONObject(response);
					final String recode = object.getString("recode");
					if(recode != null){
						if(!ImemberApplication.RESULT_STATUS.result_status_success.equals(recode)){
							if("0012".equals(recode)){
								if(ImemberApplication.getInstance().getSQLite().query_userinfo() != null){
									ImemberApplication.getInstance().getSQLite().delete_userinfo();
								}
								ImemberApplication.getInstance().setLogon(false);
								ImemberApplication.getInstance().setAutoLogonToFalse();
								Intent intent = new Intent();
								intent.setClass(_context, MyRewardListActivity.class);
								_context.startActivity(intent);
							}
//							Toast.makeText(_context, ImemberApplication.result_status.get(recode), Toast.LENGTH_SHORT).show();
							Toast.makeText(_context, "暂无数据", Toast.LENGTH_SHORT).show();
							
							
//							MyDialog myDialog = new MyDialog(_context);
//							myDialog.setTitle("提示");
//							myDialog.setIcon(android.R.drawable.ic_dialog_info);
//							myDialog.setCancelable(false);
//							myDialog.setMessage(ImemberApplication.result_status.get(recode));
//							myDialog.setLeftButton("知道了", new MyDialog.MyOnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog, int which) {
//									// TODO Auto-generated method stub
//									if("0012".equals(recode)){
//										if(ImemberApplication.getInstance().getSQLite().query_userinfo() != null){
//											ImemberApplication.getInstance().getSQLite().delete_userinfo();
//										}
//										ImemberApplication.getInstance().setLogon(false);
//										ImemberApplication.getInstance().setAutoLogonToFalse();
//										Intent intent = new Intent();
//										intent.setClass(_context, MyRewardListActivity.class);
//										_context.startActivity(intent);
//									}
//								}
//								
//							});
//							myDialog.create();
//							myDialog.show();
							
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
						String reward_logo_url = object_store.getString("h_supplier_logo");
						int u_reward_id = object_store.getInt("u_reward_id");
						String reward_id = object_store.getString("reward_id");
//						String reward_content = object_store.getString("r_description");
						String card_name = object_store.getString("c_name");
						int r_status = object_store.getInt("r_status");
						String r_expired_time = object_store.getString("r_expired_time");
						String r_redeemed_time =object_store.getString("r_redeemed_time");
						int mcard_id = object_store.getInt("mcard_id");
						String h_name = object_store.getString("h_name");
						String r_name = object_store.getString("r_name");
						
						Reward reward = new Reward();
						reward.setLogo_url(reward_logo_url);
						reward.setU_reward_id(u_reward_id);
//						reward.setReward_content(reward_content);
						reward.setReward_id(Integer.parseInt(reward_id));
						reward.setCard_name(card_name);
						reward.setStatus(r_status);
						reward.setReward_effective_date(r_expired_time);
						reward.setR_redeemed_time(r_redeemed_time);
						reward.setMcard_id(mcard_id);
						reward.setH_name(h_name);
						reward.setReward_name(r_name);
						
						if(ImemberApplication.getInstance().getReward_sort() == -1){
							ImemberApplication.getInstance().rewards.add(reward);
						}
						else if(ImemberApplication.getInstance().getReward_sort() == r_status){
							ImemberApplication.getInstance().rewards.add(reward);
						}
						if(ImemberApplication.getInstance().rewards.size() <= 0){
							Toast.makeText(_context, "暂无数据", Toast.LENGTH_SHORT).show();
						}
					}
					_listAdapter_reward.notifyDataSetChanged();
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
		
		String url = ImemberApplication.WEBROOT + ImemberApplication.URL_REWARDLIST + createJson();
		System.out.println("rewardList_url--------->" + url);
		new MyThread_get(handler_rewardList, url, _context).start();
	}
	
	public void load1(){
		_listView_store.setSelection(0);
		
		_listView_store.setState(MyListView.REFRESHING);
		_listView_store.changeHeaderViewByState();
		
		if(ImemberApplication.getInstance().rewards.size() > 0){
			ImemberApplication.getInstance().rewards.clear();
		}
		
		Reward reward = new Reward();
//		reward.setLogo_url(reward_logo_url);
		reward.setReward_content("沙夫豪森IWC万国表");
//		reward.setReward_id(Integer.parseInt(reward_id));
		reward.setCard_name("IWC至尊会员卡");
		reward.setAttention("只能在香港IWC总店兑换");
		reward.setReward_effective_date("已过期");
		ImemberApplication.getInstance().rewards.add(reward);
		
		Reward reward1 = new Reward();
//		reward.setLogo_url(reward_logo_url);
		reward1.setReward_content("耐克");
//		reward.setReward_id(Integer.parseInt(reward_id));
		reward1.setCard_name("耐克至尊会员卡");
		reward1.setAttention("只能在耐克总店兑换");
		reward1.setReward_effective_date("已过期");
		ImemberApplication.getInstance().rewards.add(reward1);

		_listAdapter_reward.notifyDataSetChanged();
		_listView_store.onRefreshComplete();
	}
}
