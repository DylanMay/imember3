package com.yundong.imember.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.R;
import com.yundong.imember.activity.StoreListActivity;
import com.yundong.imember.entity.MyCard;
import com.yundong.imember.entity.Reward;
import com.yundong.imember.entity.Store;
import com.yundong.imember.utils.AsyncLoadImageTask;

/**
 * listView适配器
 * @author huazhou
 *
 */
public class ListAdapter_reward extends ArrayAdapter<Reward> {
	private ListView _listView;
	private int _resource;
	private LayoutInflater _inflater;
	private AsyncLoadImageTask _asyncloader;
	private Context _context;

	public ListAdapter_reward(Context context, ListView listView) {
		super(context, 0);
		_resource = R.layout.listview_item_reward;
		_inflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		_asyncloader = new AsyncLoadImageTask();
		_listView = listView;
		_context = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ImemberApplication.getInstance().rewards.size();
	}

	@Override
	public Reward getItem(int position) {
		// TODO Auto-generated method stub
		return super.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return super.getItemId(position);
	}

	@Override
	public int getPosition(Reward item) {
		// TODO Auto-generated method stub
		return super.getPosition(item);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(ImemberApplication.getInstance().rewards.size() <= 0){
			return null;
		}
		View view = convertView;
		ListItemViewCache_reward viewCache = null;

		if (view != null) {
			viewCache = (ListItemViewCache_reward) view.getTag();
		} else {
			view = _inflater.inflate(_resource,null);
			viewCache = new ListItemViewCache_reward(view);
			view.setTag(viewCache);
		}
		
		Reward reward = ImemberApplication.getInstance().rewards.get(position);
		
		if (reward != null) {
			ImageView iv_reward_logo = viewCache.get_iv_reward_logo_view();
			String reward_logo_url = reward.getLogo_url();
			if(reward_logo_url != null && !"".equals(reward_logo_url) && !"null".equals(reward_logo_url)){
				iv_reward_logo.setTag(reward_logo_url);
				Drawable drawable = _asyncloader.loadDrawable(reward_logo_url,
						new AsyncLoadImageTask.ImageCallback() {
							public void imageLoaded(Drawable imageDrawable,String imageUrl) {
								ImageView iv_reward_logo = (ImageView)_listView.findViewWithTag(imageUrl);
								if(iv_reward_logo != null){
									if(imageDrawable == null){
//										iv_reward_logo.setBackgroundResource(R.drawable.default_bg);
									} else {
//										iv_reward_logo.setBackgroundDrawable(imageDrawable);
									}
								}
							}
						});
				if(drawable == null){
//					iv_reward_logo.setBackgroundResource(R.drawable.default_bg);
				} else {
//					iv_reward_logo.setBackgroundDrawable(drawable);
				}
			} else {
//				iv_reward_logo.setBackgroundResource(R.drawable.default_bg);
			}
			TextView tv_timeout = viewCache.get_tv_timeout_view();
			int status = reward.getStatus();
			switch(status){
			case 0:
				tv_timeout.setText("未使用");
				break;
			case 1:
				tv_timeout.setText("已经使用");
				break;
			case 2:
				tv_timeout.setText("过期");
				break;
			case 3:
				tv_timeout.setText("失效");
				break;
			}
			
			TextView tv_card_title = viewCache.get_tv_reward_title_view();
			tv_card_title.setText(reward.getReward_name());
			
			TextView tv_card_name = viewCache.get_tv_reward_content_view();
//			tv_card_name.setText(reward.getCard_name());
			tv_card_name.setText(reward.getH_name());
			
			TextView tv = viewCache.get_tv_view();
			TextView tv_card_effectivedate = viewCache.get_tv_reward_effectivedate_view();
			String r_redeemed_time = reward.getR_redeemed_time();
			if(r_redeemed_time != null && !"".equals(r_redeemed_time)){
				tv.setText("使用日期:");
				tv_card_effectivedate.setText(r_redeemed_time);
			}
			else{
				tv.setText("有效期至:");
				if(status == 2){
					tv_card_effectivedate.setTextColor(_context.getResources().getColor(R.color.red));
				}
				else{
					tv_card_effectivedate.setTextColor(_context.getResources().getColor(R.color.light_black2));
				}
				tv_card_effectivedate.setText(reward.getReward_effective_date());
				
				String date = reward.getReward_effective_date();
				if(date != null && "0".equals(date)){
					tv_card_effectivedate.setText("永久");
				}
			}
		}
		return view;
	}
}
