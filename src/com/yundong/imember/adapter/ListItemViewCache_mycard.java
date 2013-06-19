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
public class ListItemViewCache_mycard {
	private View _baseView;
	private ImageView iv_card_logo;
	private TextView tv_card_title;
	private TextView tv_card_name;
	private TextView tv_card_effectivedate;
	
	public ListItemViewCache_mycard(View baseView) {
		this._baseView = baseView;
	}
	public ImageView get_iv_card_logo_view() {
		return iv_card_logo == null ? (ImageView) _baseView
				.findViewById(R.id.mycard_logo) : iv_card_logo;
	}

	public TextView get_tv_card_title_view() {
		return tv_card_title == null ? (TextView) _baseView
				.findViewById(R.id.mycard_title) : tv_card_title;
	}
	
	public TextView get_tv_card_name_view() {
		return tv_card_name == null ? (TextView) _baseView
				.findViewById(R.id.mycard_name) : tv_card_name;
	}

	public TextView get_tv_card_effectivedate_view() {
		return tv_card_effectivedate == null ? (TextView) _baseView
				.findViewById(R.id.mycard_effectivedate) : tv_card_effectivedate;
	}

}
