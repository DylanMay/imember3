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
public class ListItemViewCache_store {
	private View _baseView;
	private ImageView iv_store_logo;
	private TextView tv_store_title;
	private TextView tv_cate_name;
	private TextView tv_city_name;
	private TextView tv_store_addr;
	
	public ListItemViewCache_store(View baseView) {
		this._baseView = baseView;
	}
	public ImageView get_iv_store_logo_view() {
		return iv_store_logo == null ? (ImageView) _baseView
				.findViewById(R.id.store_logo) : iv_store_logo;
	}

	public TextView get_tv_store_title_view() {
		return tv_store_title == null ? (TextView) _baseView
				.findViewById(R.id.store_title) : tv_store_title;
	}
	
	public TextView get_tv_cate_name_view() {
		return tv_cate_name == null ? (TextView) _baseView
				.findViewById(R.id.cate_name) : tv_cate_name;
	}

	public TextView get_tv_city_name_view() {
		return tv_city_name == null ? (TextView) _baseView
				.findViewById(R.id.city_name) : tv_city_name;
	}

	public TextView get_tv_store_addr_view() {
		return tv_store_addr == null ? (TextView) _baseView
				.findViewById(R.id.store_addr) : tv_store_addr;
	}

}
