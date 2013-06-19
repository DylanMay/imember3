package com.yundong.imember.datamanager;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.activity.CardStampDetailActivity;
import com.yundong.imember.activity.UseRewardDetailActivity;
import com.yundong.imember.customWidget.MyDialog;
import com.yundong.imember.db.SQLite;
import com.yundong.imember.entity.MyCard;
import com.yundong.imember.entity.Reward;
import com.yundong.imember.entity.User;
import com.yundong.imember.http.GetServiceDataHandler;
import com.yundong.imember.http.GetServiceDataHandlerListener;
import com.yundong.imember.thread.MyThread_get;

public class ScanResultManager{
	private MyDialog _dialog;
	private MyDialog _myDialog;
	private Context _context;
	private SQLite sqlite;
	private GetServiceDataHandler handler_count;
	private GetServiceDataHandler handler_exchange;
	private GetServiceDataHandler handler_cardList;
	private int style;
	
	public ScanResultManager(Context context) {
		// TODO Auto-generated constructor stub
		_context = context;
		sqlite = new SQLite(context);
		
		initGetDataHandler();
	}
	
	private void initGetDataHandler(){
		handler_count = new GetServiceDataHandler("计次扫码收到的数据", new GetServiceDataHandlerListener(){

			@Override
			public void process_cancel_handler() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public int process_fail_handler(String response) {
				// TODO Auto-generated method stub
				if(_dialog != null){
					if (_dialog.isShowing()) {
						_dialog.dismiss();
						_dialog = null;
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
						if(!ImemberApplication.RESULT_STATUS.result_basic_success.equals(recode)){
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
					JSONObject object_result = new JSONObject(data);
					int is_reward = object_result.getInt("is_reward");
					final Intent intent = new Intent();
					switch(is_reward){
					case 0:
						final int mcard_id = object_result.getInt("mcard_id");
						
						MyDialog myDialog = new MyDialog(_context);
						myDialog.setTitle("提示");
						myDialog.setIcon(android.R.drawable.ic_dialog_info);
						myDialog.setCancelable(false);
						myDialog.setMessage("成功添加印花！");
						myDialog.setLeftButton("确定", new MyDialog.MyOnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								loadInfo(mcard_id, 0);
							}
							
						});
						myDialog.create();
						myDialog.show();
						
						break;
					case 1:
//						int stamp_now1 = object_result.getInt("stamp_now");
						final int u_reward_id = object_result.getInt("u_reward_id");
						String h_name = object_result.getString("h_name");
						String reward_name = object_result.getString("r_name");
						String reward_description = object_result.getString("r_description");
						String r_expired_time = object_result.getString("r_expired_time");
						String r_apply_condition = object_result.getString("r_apply_condition");
						String r_code = object_result.getString("r_code");
						String card_logo_url = object_result.getString("c_url");
						
						final Reward reward = new Reward();
						reward.setH_name(h_name);
						reward.setReward_name(reward_name);
						reward.setReward_content(reward_description);
						reward.setR_apply_condition(r_apply_condition);
						reward.setReward_effective_date(r_expired_time);
						reward.setU_reward_id(u_reward_id);
						reward.setR_code(r_code);
						reward.setLogo_url(card_logo_url);
						
//						final int mcard_id1 = object_result.getInt("mcard_id");
						
						MyDialog myDialog1 = new MyDialog(_context);
						myDialog1.setTitle("提示");
						myDialog1.setIcon(android.R.drawable.ic_dialog_info);
						myDialog1.setCancelable(false);
						myDialog1.setMessage("已集齐" + "印花，获得商家奖赏！");
						myDialog1.setLeftButton("确定", new MyDialog.MyOnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								new UseRewardDetail_loadManager(_context).load(u_reward_id);
//								intent.putExtra("reward", reward);
//								intent.setClass(_context, UseRewardDetailActivity.class);
//								_context.startActivity(intent);
								
//								loadInfo(mcard_id1, 1);
							}
							
						});
						myDialog1.create();
						myDialog1.show();
						
						break;
						default:
							break;
					}
				}catch (JSONException e) {
					// TODO: handle exception
				}finally{
					if(_dialog != null){
						if (_dialog.isShowing()) {
							_dialog.dismiss();
							_dialog = null;
						}
					}
				}
			}

		}, _context);
		
		handler_cardList = new GetServiceDataHandler("印花详情", 
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
						String card_logo_url = object_store.getString("h_supplier_logo");
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
						
//						int u_reward_id = object_store.getInt("u_reward_id");
						int reward_id = object_store.getInt("s_reward_id");
						int r_status = object_store.getInt("s_r_status");
						String r_expired_time = object_store.getString("s_effective_months");
						
						int c_status = object_store.getInt("c_status");
						
						int s_round = object_store.getInt("s_round");
						String s_name = object_store.getString("s_name");
						String s_description = object_store.getString("s_description");
						
						final Reward reward = new Reward();
						reward.setH_name(h_name);
						reward.setReward_name(reward_name);
						reward.setReward_description(reward_description);
						reward.setR_apply_condition(r_apply_condition);
						
						reward.setLogo_url(card_logo_url);
						reward.setReward_content(reward_description);
//						reward.setU_reward_id(u_reward_id);
						reward.setReward_id(reward_id);
						reward.setStatus(0);
						reward.setC_status(c_status);
						reward.setReward_effective_date(r_expired_time+ "个月");
						reward.setCard_name(card_name);
						reward.setStamp_now(stamp_now);
						reward.setS_round(s_round);
						reward.setS_description(s_description);
						reward.setS_name(s_name);
						
						MyCard card = new MyCard();
						card.setCard_logo_url(card_logo_url);
//						card.setCard_title(card_title);
						card.setCard_name(card_name);
						card.setStamp_now(stamp_now);
						card.setReward_now(reward_now);
						card.setM_number(m_number);
						card.setC_seacription(c_seacription);
						
						Intent intent = new Intent();
//						intent.putExtra("card", card);
						intent.putExtra("reward", reward);
						switch(style){
						case 0:
							intent.setClass(_context, CardStampDetailActivity.class);
							break;
						case 1:
							intent.setClass(_context, UseRewardDetailActivity.class);
							break;
						}
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
			
		}, _context);
	}
	
	private void loadInfo(int mcard_id, int style){
		this.style = style;
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
	
	public void process_activityResult(String result){
		scanCountThread(result);
		
//		Reward reward = new Reward();
//		Intent intent = new Intent();
//		intent.putExtra("reward", reward);
//		intent.setClass(_context, UseRewardDetailActivity.class);
//		_context.startActivity(intent);
	}
	
	//处理计次结果
	public void scanCountThread(final String data){
			_dialog = new MyDialog(_context);
			_dialog.setTitle("正在加载数据...");
			_dialog.setIcon(android.R.drawable.ic_dialog_info);
			_dialog.setCancelable(false);
			_dialog.setLoad();
			_dialog.setLeftButton("取消", new MyDialog.MyOnClickListener() {
	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
				
			});
			_dialog.create();
			_dialog.show();
		
			String targetUrl = ImemberApplication.WEBROOT + ImemberApplication.URL_ADDYINHUA + createJson(data);
			
			new MyThread_get(handler_count, targetUrl, _context).start();
			
			System.out.println("调用了计次处理url------->" + targetUrl);
	}
	
	private String createJson(String data){
		JSONObject object = new JSONObject();
		try {
			User user = ImemberApplication.getInstance().getUser();
			object.put("user_id", user.getUser_id());
			object.put("user_pwd", user.getPassword());
			object.put("stamp_code", data);
//			System.out.println("user_id:" + user.getUser_id() + ", user_pwd:" + user.getPassword()
//					+ "，stamp_code:" + data);
			
//			return ImemberApplication.encode("{\"stamp_code\"", "UTF-8") + ":" + ImemberApplication.encode("\"" + data + "\"", "UTF-8") + "," +
//					ImemberApplication.encode("\"user_pwd\"", "UTF-8") + ":" + ImemberApplication.encode("\"" + user.getPassword() + "\"", "UTF-8") + "," +
//					ImemberApplication.encode("\"user_id\"", "UTF-8") + ":" + ImemberApplication.encode(user.getUser_id() + "}", "UTF-8");
					
			return ImemberApplication.encode(object.toString(), "UTF-8");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
