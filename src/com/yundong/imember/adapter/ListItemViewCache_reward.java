package com.yundong.imember.adapter;


import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yundong.imember.R;

/**
 * listView中单个项目中的各个控件
 * @author huazhou
 *
 */
public class ListItemViewCache_reward {
	private View _baseView;
	private ImageView iv_reward_logo;
	private TextView tv_reward_title;
	private TextView tv_reward_content;
	private TextView tv_reward_effectivedate;
	private TextView tv_timeout;
	private TextView tv;
	
	public ListItemViewCache_reward(View baseView) {
		this._baseView = baseView;
	}
	public ImageView get_iv_reward_logo_view() {
		return iv_reward_logo == null ? (ImageView) _baseView
				.findViewById(R.id.reward_logo) : iv_reward_logo;
	}

	public TextView get_tv_reward_title_view() {
		return tv_reward_title == null ? (TextView) _baseView
				.findViewById(R.id.reward_title) : tv_reward_title;
	}
	
	public TextView get_tv_reward_content_view() {
		return tv_reward_content == null ? (TextView) _baseView
				.findViewById(R.id.reward_content) : tv_reward_content;
	}

	public TextView get_tv_reward_effectivedate_view() {
		return tv_reward_effectivedate == null ? (TextView) _baseView
				.findViewById(R.id.reward_effectivedate) : tv_reward_effectivedate;
	}
	
	public TextView get_tv_timeout_view(){
		return tv_timeout == null ? (TextView) _baseView
				.findViewById(R.id.tv_timeout) : tv_timeout;
	}

	public TextView get_tv_view(){
		return tv == null ? (TextView) _baseView
				.findViewById(R.id.tv) : tv;
	}
}
