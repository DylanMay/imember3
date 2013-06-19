package com.yundong.imember.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.R;
import com.yundong.imember.activity.StoreListActivity;
import com.yundong.imember.entity.Store;
import com.yundong.imember.utils.AsyncLoadImageTask;

/**
 * listView  ≈‰∆˜
 * @author huazhou
 *
 */
public class ListAdapter_store extends ArrayAdapter<Store> {
	private ListView _listView;
	private int _resource;
	private LayoutInflater _inflater;
	private AsyncLoadImageTask _asyncloader;
	private Context _context;

	public ListAdapter_store(Context context, ListView listView) {
		super(context, 0);
		_resource = R.layout.listview_item_store;
		_inflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		_asyncloader = new AsyncLoadImageTask();
		_listView = listView;
		_context = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ImemberApplication.getInstance().stores_old.size();
	}

	@Override
	public Store getItem(int position) {
		// TODO Auto-generated method stub
		return super.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return super.getItemId(position);
	}

	@Override
	public int getPosition(Store item) {
		// TODO Auto-generated method stub
		return super.getPosition(item);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(ImemberApplication.getInstance().stores_old.size() <= 0){
			return null;
		}
		View view = convertView;
		ListItemViewCache_store viewCache = null;

		if (view != null) {
			viewCache = (ListItemViewCache_store) view.getTag();
		} else {
			view = _inflater.inflate(_resource,null);
			viewCache = new ListItemViewCache_store(view);
			view.setTag(viewCache);
		}
		
		Store store = ImemberApplication.getInstance().stores_old.get(position);
		
		if (store != null) {
			ImageView iv_store_logo = viewCache.get_iv_store_logo_view();
			String store_logo_url = store.getStore_logo_url();
			if(store_logo_url != null && !"".equals(store_logo_url) && !"null".equals(store_logo_url)){
				iv_store_logo.setTag(store_logo_url);
				Drawable drawable = _asyncloader.loadDrawable(store_logo_url,
						new AsyncLoadImageTask.ImageCallback() {
							public void imageLoaded(Drawable imageDrawable,String imageUrl) {
								ImageView iv_store_logo = (ImageView)_listView.findViewWithTag(imageUrl);
								if(iv_store_logo != null){
									if(imageDrawable == null){
										iv_store_logo.setBackgroundResource(R.drawable.default_store_logo);
									} else {
										iv_store_logo.setBackgroundDrawable(imageDrawable);
									}
								}
							}
						});
				if(drawable == null){
					iv_store_logo.setBackgroundResource(R.drawable.default_store_logo);
				} else {
					iv_store_logo.setBackgroundDrawable(drawable);
				}
			} else {
				iv_store_logo.setBackgroundResource(R.drawable.default_store_logo);
			}
			TextView tv_cate_name = viewCache.get_tv_cate_name_view();
			tv_cate_name.setText(store.getCate_name());
			
			TextView tv_city_name = viewCache.get_tv_city_name_view();
			tv_city_name.setText(store.getCity_name());
			
			TextView tv_store_addr = viewCache.get_tv_store_addr_view();
			tv_store_addr.setText(store.getStore_addr());
			
			TextView tv_store_title = viewCache.get_tv_store_title_view();
			tv_store_title.setText(store.getStore_title());
		}
		return view;
	}
}
