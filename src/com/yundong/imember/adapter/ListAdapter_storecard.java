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
import com.yundong.imember.entity.Store;
import com.yundong.imember.entity.StoreCard;
import com.yundong.imember.utils.AsyncLoadImageTask;

/**
 * listView  ≈‰∆˜
 * @author huazhou
 *
 */
public class ListAdapter_storecard extends ArrayAdapter<Store> {
	private ListView _listView;
	private int _resource;
	private LayoutInflater _inflater;
	private AsyncLoadImageTask _asyncloader;
	private Context _context;

	public ListAdapter_storecard(Context context, ListView listView) {
		super(context, 0);
		_resource = R.layout.listview_item_storecard;
		_inflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		_asyncloader = new AsyncLoadImageTask();
		_listView = listView;
		_context = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ImemberApplication.getInstance().storeCards.size();
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
		if(ImemberApplication.getInstance().storeCards.size() <= 0){
			return null;
		}
		View view = convertView;
		ListItemViewCache_storecard viewCache = null;

		if (view != null) {
			viewCache = (ListItemViewCache_storecard) view.getTag();
		} else {
			view = _inflater.inflate(_resource,null);
			viewCache = new ListItemViewCache_storecard(view);
			view.setTag(viewCache);
		}
		
		StoreCard card = ImemberApplication.getInstance().storeCards.get(position);
		
		if (card != null) {
			ImageView iv_card_logo = viewCache.get_iv_card_logo_view();
			String store_logo_url = card.getCard_logo_url();
			if(store_logo_url != null && !"".equals(store_logo_url) && !"null".equals(store_logo_url)){
				iv_card_logo.setTag(store_logo_url);
				Drawable drawable = _asyncloader.loadDrawable(store_logo_url,
						new AsyncLoadImageTask.ImageCallback() {
							public void imageLoaded(Drawable imageDrawable,String imageUrl) {
								ImageView iv_card_logo = (ImageView)_listView.findViewWithTag(imageUrl);
								if(iv_card_logo != null){
									if(imageDrawable == null){
										iv_card_logo.setBackgroundResource(R.drawable.default_store_logo);
									} else {
										iv_card_logo.setBackgroundDrawable(imageDrawable);
									}
								}
							}
						});
				if(drawable == null){
					iv_card_logo.setBackgroundResource(R.drawable.default_store_logo);
				} else {
					iv_card_logo.setBackgroundDrawable(drawable);
				}
			} else {
				iv_card_logo.setBackgroundResource(R.drawable.default_store_logo);
			}
			TextView tv_card_title = viewCache.get_tv_card_title_view();
			tv_card_title.setText(card.getCard_title());
			
			TextView tv_card_name = viewCache.get_tv_card_name_view();
			tv_card_name.setText(card.getCard_name());
			
			TextView tv_card_effectivedate = viewCache.get_tv_card_effectivedate_view();
			tv_card_effectivedate.setText(card.getCard_effectivedate());
		}
		return view;
	}
}
