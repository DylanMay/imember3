package com.yundong.imember.datamanager;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.activity.MyRewardDetailActivity;
import com.yundong.imember.customWidget.MyDialog;
import com.yundong.imember.entity.Reward;
import com.yundong.imember.entity.User;
import com.yundong.imember.http.GetServiceDataHandler;
import com.yundong.imember.http.GetServiceDataHandlerListener;
import com.yundong.imember.thread.MyThread_get;

public class RewardDetail_loadManager {
	private Context _context;
	private MyDialog _myDialog;
	private GetServiceDataHandler handler_rewardDetail;
	private ArrayList<Reward> rewards;
	private int mcard_id;
	private String card_name;

	public RewardDetail_loadManager(Context context) {
		_context = context;
		
		handler_rewardDetail = new GetServiceDataHandler("奖赏详情", 
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
					JSONObject object_store1 = new JSONObject(data);
					String list = object_store1.getString("list");
					JSONArray array = new JSONArray(list);
					rewards = new ArrayList<Reward>();
					for (int i = 0; i < array.length(); i++) {
						JSONObject object_store = array.getJSONObject(i);
						String reward_logo_url = object_store.getString("c_url");
						int u_reward_id = object_store.getInt("u_reward_id");
						int reward_id = object_store.getInt("reward_id");
//						String reward_content = object_store.getString("r_description");
						int r_status = object_store.getInt("r_status");
						String r_expired_time = object_store.getString("r_expired_time");
						
						String h_name = object_store.getString("h_name");
						String reward_name = object_store.getString("r_name");
//						String card_name = object_store.getString("c_name");
//						String apply_condition = object_store.getString("apply_condition");
						
						Reward reward = new Reward();
						reward.setLogo_url(reward_logo_url);
//						reward.setReward_content(reward_content);
						reward.setU_reward_id(u_reward_id);
						reward.setReward_id(reward_id);
						reward.setStatus(r_status);
						reward.setReward_effective_date(r_expired_time);
						
						reward.setH_name(h_name);
						reward.setReward_name(reward_name);
//						reward.setCard_name(card_name);
//						reward.setR_apply_condition(apply_condition);
						reward.setCard_name(card_name);
						
//						rewards.add(reward);
						if(ImemberApplication.getInstance().getReward_sort() == -1){
							rewards.add(reward);
						}
						else if(ImemberApplication.getInstance().getReward_sort() == r_status){
							rewards.add(reward);
						}
					}					
					Intent intent = new Intent();
					intent.putExtra("rewards", rewards);
					intent.putExtra("mcard_id", mcard_id);
					intent.putExtra("card_name", card_name);
					intent.setClass(_context, MyRewardDetailActivity.class);
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
	
	private String createJson(int mcard_id){
		JSONObject object = new JSONObject();
		try {
			User user = ImemberApplication.getInstance().getUser();
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
	
	public void load(int mcard_id, String card_name){
		_myDialog = new MyDialog(_context);
		_myDialog.setTitle("正在加载数据...");
		_myDialog.setIcon(android.R.drawable.ic_dialog_info);
		_myDialog.setCancelable(false);
		_myDialog.setLoad();
		_myDialog.setLeftButton("取消", new MyDialog.MyOnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				handler_rewardDetail.setIsCancel(true);
			}
			
		});
		_myDialog.create();
		_myDialog.show();
		
		this.mcard_id = mcard_id;
		this.card_name = card_name;
		
		String url = ImemberApplication.WEBROOT + ImemberApplication.URL_REWARDLIST + createJson(mcard_id);
		System.out.println("rewardList_url--------->" + url);
		new MyThread_get(handler_rewardDetail, url, _context).start();
	}
	
}
