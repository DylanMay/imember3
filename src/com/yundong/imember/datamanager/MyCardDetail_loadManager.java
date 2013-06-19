package com.yundong.imember.datamanager;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.activity.MyCardDetailActivity;
import com.yundong.imember.customWidget.MyDialog;
import com.yundong.imember.entity.MyCard;
import com.yundong.imember.entity.Reward;
import com.yundong.imember.entity.User;
import com.yundong.imember.http.GetServiceDataHandler;
import com.yundong.imember.http.GetServiceDataHandlerListener;
import com.yundong.imember.thread.MyThread_get;

public class MyCardDetail_loadManager {
	private Context _context;
	private MyDialog _myDialog;
	private GetServiceDataHandler handler_cardList;

	public MyCardDetail_loadManager(Context context) {
		_context = context;
		
		handler_cardList = new GetServiceDataHandler("会员卡详情", 
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
						String card_logo_url = object_store.getString("c_url");
//						String card_title = object_store.getString("s_name");
						String card_name = object_store.getString("c_name");
						
						int stamp_now = object_store.getInt("stamp_now");
						int reward_now = object_store.getInt("reward_now");
						String m_number = object_store.getString("m_number");
						String c_seacription = object_store.getString("c_description");
						
						String h_name = object_store.getString("h_name");
						String reward_name = object_store.getString("s_r_name");
						String reward_description = object_store.getString("s_r_description");
						String r_apply_condition = object_store.getString("s_apply_condition");
//						String c_servies_terms = object_store.getString("c_servies_terms");
						
//						int u_reward_id = object_store.getInt("u_reward_id");
						String reward_id_str = object_store.getString("s_reward_id");
						int reward_id = 0;
						if(reward_id_str != null && !"".equals(reward_id_str)){
							reward_id = Integer.parseInt(reward_id_str);
						}
//						int r_status = object_store.getInt("s_r_status");
						String r_expired_time = object_store.getString("s_effective_months");
						
						int c_status = object_store.getInt("c_status");
						int m_status = object_store.getInt("m_status");
						String s_status = object_store.getString("s_status").trim();
						String s_r_status = object_store.getString("s_r_status").trim();
						
						String s_round_str = object_store.getString("s_round");
						int s_round = 0;
						if(s_round_str != null && !"".equals(s_round_str)){
							s_round = Integer.parseInt(s_round_str);
						}
						String s_name = object_store.getString("s_name");
						String s_description = object_store.getString("s_description");
						int account_id = object_store.getInt("account_id");
						int mcard_id = object_store.getInt("mcard_id");
						String stamp_id = object_store.getString("stamp_id");
						
						final Reward reward = new Reward();
						if(stamp_id != null && !"".equals(stamp_id)){
							reward.setStamp_id(Integer.parseInt(stamp_id));
						}
						else{
							reward.setStamp_id(0);
						}
						
						reward.setH_name(h_name);
						reward.setReward_name(reward_name);
						reward.setReward_description(reward_description);
						reward.setR_apply_condition(r_apply_condition);
						
						reward.setLogo_url(card_logo_url);
						reward.setReward_content(reward_description);
//						reward.setU_reward_id(u_reward_id);
//						reward.setReward_id(reward_id);
						reward.setStatus(0);
						reward.setC_status(c_status);
						reward.setM_status(m_status);
						if(s_status != null && !"".equals(s_status)){
							reward.setS_status(Integer.parseInt(s_status));
						}
						else{
							reward.setS_status(0);
						}
						if(s_r_status != null && !"".equals(s_r_status)){
							reward.setS_r_status(Integer.parseInt(s_r_status));
						}
						else{
							reward.setS_r_status(0);
						}
						reward.setReward_effective_date(r_expired_time+ "个月");
						reward.setCard_name(card_name);
						reward.setS_round(s_round);
						reward.setStamp_now(stamp_now);
						reward.setS_description(s_description);
						reward.setS_name(s_name);
						reward.setMcard_id(mcard_id);
						reward.setS_r_description(reward_description);
						
						MyCard card = new MyCard();
						card.setCard_logo_url(card_logo_url);
//						card.setCard_title(card_title);
						card.setCard_name(card_name);
						card.setStamp_now(stamp_now);
						card.setReward_now(reward_now);
						card.setM_number(m_number);
						card.setC_seacription(c_seacription);
//						card.setC_servies_terms(c_servies_terms);
						card.setAccount_id(account_id);
						
						Intent intent = new Intent();
						intent.putExtra("card", card);
						intent.putExtra("reward", reward);
						intent.setClass(_context, MyCardDetailActivity.class);
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
	
	public void load(int mcard_id){
		
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
		
		String url = ImemberApplication.WEBROOT + ImemberApplication.URL_MYCARDDETAIL + createJson(mcard_id);
		System.out.println("carddetail_url--------->" + url);
		new MyThread_get(handler_cardList, url, _context).start();
	}
}
