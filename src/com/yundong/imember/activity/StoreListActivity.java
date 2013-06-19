package com.yundong.imember.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.R;
import com.yundong.imember.adapter.ListAdapter_store;
import com.yundong.imember.customWidget.MyListView;
import com.yundong.imember.customWidget.MyListView.OnRefreshListener;
import com.yundong.imember.datamanager.Citys_loadManager_storelist;
import com.yundong.imember.datamanager.LogonManager;
import com.yundong.imember.datamanager.ScanResultManager;
import com.yundong.imember.datamanager.Sorts_loadManager_storelist;
import com.yundong.imember.datamanager.StoreList_loadManager;
import com.yundong.imember.datamanager.updateManager;
import com.yundong.imember.entity.City;
import com.yundong.imember.entity.Store;
import com.yundong.imember.entity.User;
import com.yundong.imember.myinterface.LoginSuccessListener;
import com.yundong.imember.myinterface.LogonSuccessListener;
import com.yundong.imember.utils.AsyncLoadImageTask;
import com.yundong.imember.utils.FileUtils;
import com.yundong.imember.utils.HttpDownLoader;

public class StoreListActivity extends HomeActivity implements LogonSuccessListener,LoginSuccessListener{
	/**
	 * 头部相关
	 */
	private TextView tv_title;
	private ImageView btn_city_select;
	private ImageView btn_right;
	private String[] str_citys;
	/**
	 * 分类相关
	 */
	private RelativeLayout layout_header_storelist;
	private ImageView iv_half_translate;
	private boolean getLayoutHeight;
	private int layout_header_height;
	private int layout_footer_height;
//	private SortDialog sort_dialog;
	private View view_class_dialog;
	private LinearLayout layout_sort;
	private ArrayList<Bundle> bundles;
	private int sort_id;
	private String url_icon;
	private String floderName;
	
	/**
	 * 相关listview
	 */
	private RelativeLayout layout_list_and_sort;
	private MyListView listView_store;
	private ListAdapter_store listAdapter_store;
	private int pageIndex = 1;// 页码
	private int last_pageIndex;
	private int cate_id;
	
	/**
	 * 登录界面相关
	 */
	protected LinearLayout view_logon;
	protected LinearLayout view_islogoning;
	private EditText et_username_logon;
	private EditText et_password_logon;
	private String userName_logon;
	private String passWord_logon;	
	private Button btn_logon;
	private TextView btn_login;
	private TextView tv_forget_psd;
	private LogonManager myLogon;
	
	/**扫码相关
	 * 
	 */
	private ScanResultManager scanProcess;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_home);
		super.onCreate(savedInstanceState);
		
		initData();
		initUI();
		setUI();
		init();
		
		if(ImemberApplication.getInstance().isFirst()){
			ImemberApplication.getInstance().setFirst(false);
			new updateManager(this).check();
		}
		autoLogon();
	}
	
	private void autoLogon(){
		User user = ImemberApplication.getInstance().getUser();
		if(user != null){
			String username = user.getUser_name();
			String psd = user.getPassword();
			if(username != null && !"".equals(username)){
				if(psd != null && !"".equals(psd)){
					myLogon.setString(user.getUser_name(), user.getPassword());
					myLogon.logon();
				}
			}
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setUI();
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private void initData(){
		ImemberApplication.getInstance().getLogonSuccessManager().registeLogonSuccessListener(this);
		ImemberApplication.getInstance().getLoginSuccessManager().registeLoginSuccessListener(this);
	}
	
	private void init(){
		floderName = getResources().getString(R.string.sdcard_floder_name);
		
		view_logon.setVisibility(View.GONE);
		view_islogoning.setVisibility(View.GONE);
		view_store.setVisibility(View.VISIBLE);

		loadStores();
		
//		if(!ImemberApplication.getInstance().isFirst()){
//			view_store.setVisibility(View.VISIBLE);
//			view_logon.setVisibility(View.GONE);
//			
//			loadStores();
//		}
//		else{
//			ImemberApplication.getInstance().getSQLite().insertFirstLoad();
//			
//			if(ImemberApplication.getInstance().isLogon()){
//				view_logon.setVisibility(View.GONE);
//				view_islogoning.setVisibility(View.GONE);
//				view_store.setVisibility(View.VISIBLE);
//
//				loadStores();
//			}
//			else{
//				view_logon.setVisibility(View.VISIBLE);
//				view_store.setVisibility(View.GONE);
//			}
//		}
	}
	
	private void initUI(){
		registFinishMainActivity();
		
		view_store.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(view_class_dialog != null){
					if(view_class_dialog.isShown()){
						view_class_dialog.setVisibility(View.GONE);
					}
				}
			}
		});
//		view_class_dialog = LayoutInflater.from(StoreListActivity.this).inflate(R.layout.sortdialog, null);
		view_class_dialog = (LinearLayout)findViewById(R.id.view_sort);
//		layout_sort = (LinearLayout) view_class_dialog.findViewById(R.id.layout_sort);
		layout_sort = (LinearLayout)findViewById(R.id.layout_sort);
		bundles = new ArrayList<Bundle>();
		
		layout_header_storelist = (RelativeLayout)findViewById(R.id.layout_header_storelist);
		iv_half_translate =(ImageView)findViewById(R.id.iv_half_translate);
//		new Thread(){
//			public void run() {
//				while(!getLayoutHeight){
//					try {
//						if(layout_header_storelist.getHeight() != 0){
//							layout_header_height = layout_header_storelist.getHeight();
//							layout_footer_height = layout_footer.getHeight();
//							System.out.println("height1------>" + layout_header_height);
//							System.out.println("height2------>" + layout_footer_height);
//							getLayoutHeight = true;
//							handler_initSortDialog.sendEmptyMessage(0);
//						}
//						Thread.sleep(10);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			};
//		}.start();
		
		tv_title = (TextView)findViewById(R.id.tv_header_title);
		btn_city_select = (ImageView)findViewById(R.id.btn_city_select);
		btn_right = (ImageView)findViewById(R.id.btn_header_right);
		
		tv_title.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadCitys();
			}
		});
		btn_city_select.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadCitys();
			}
		});
		btn_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				
//				if(sort_dialog != null){
//					if(!sort_dialog.isShowing()){
//						sort_dialog.show();
//						iv_half_translate.setVisibility(View.VISIBLE);
//					}
//				}
				
				if(view_class_dialog != null){
					if(!view_class_dialog.isShown()){
						loadSorts();
						view_class_dialog.setVisibility(View.VISIBLE);
//						iv_half_translate.setVisibility(View.VISIBLE);
					}
					else{
						view_class_dialog.setVisibility(View.GONE);
					}
				}
			}
		});

		initUI_store();
		initUI_logon();
	}
	
//	private Handler handler_initSortDialog = new Handler(){
//		public void handleMessage(android.os.Message msg) {
//			sort_dialog = new SortDialog(StoreListActivity.this);
//			sort_dialog.setContentView(view_class_dialog);
//			sort_dialog.setCanceledOnTouchOutside(true);
//			sort_dialog.setOnCancelListener(new OnCancelListener() {
//				
//				@Override
//				public void onCancel(DialogInterface dialog) {
//					// TODO Auto-generated method stub
//					if(iv_half_translate != null){
//						if(iv_half_translate.isShown()){
//							iv_half_translate.setVisibility(View.INVISIBLE);
//						}
//					}
//				}
//			});
//			Window window=sort_dialog.getWindow();
//			WindowManager.LayoutParams layoutParams =window.getAttributes();
//			window.setGravity(Gravity.TOP | Gravity.RIGHT);
//			layoutParams.y = layout_header_height;
//			layoutParams.width = ImemberApplication.SCREEN_WIDTH/2;
////			WindowManager wm = StoreListActivity.this.getWindowManager(); 
////		     int height = wm.getDefaultDisplay().getHeight(); 
//			layoutParams.height = ImemberApplication.SCREEN_HEIGHT - layout_header_height - layout_header_height;
//			window.setAttributes(layoutParams);
//		};
//	};
	
	private void initUI_store(){
		layout_list_and_sort = (RelativeLayout)findViewById(R.id.layout_list_and_sort);
		layout_list_and_sort.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(view_class_dialog != null){
					if(view_class_dialog.isShown()){
						view_class_dialog.setVisibility(View.GONE);
						return;
					}
				}		
			}
		});
		listView_store = (MyListView) findViewById(R.id.listview_store);
		listAdapter_store = new ListAdapter_store(this, listView_store);
		listView_store.setAdapter(listAdapter_store);
		
		listView_store.setOnItemClickListener(new ItemClickListener_store());
		listView_store.setOnScrollListener(new ListScrollListener());
		
		listView_store.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				loadStores_flush();
				
				
//				new AsyncTask<Void, Void, Void>() {
//					protected Void doInBackground(Void... params) {
//						try {
//							Thread.sleep(1000);
//							
//							String url = ImemberApplication.WEBROOT + ImemberApplication.URL_SHOPLIST + createJson();
//							HTTPRequestHelper httpHelper_special = new HTTPRequestHelper(handler, StoreListActivity.this);
//							httpHelper_special.performGet(url);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						// handleList();
//						return null;
//					}
//
//					@Override
//					protected void onPostExecute(Void result) {
//						listAdapter_store.notifyDataSetChanged();
//						listView_store.onRefreshComplete();
//					}
//
//				}.execute();
			}
		});
	}
	
	private class ListScrollListener implements OnScrollListener {
		// 最后显示Item
		private int _lastItem;

		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			listView_store.firstItemIndex = firstVisibleItem;
			_lastItem = firstVisibleItem + visibleItemCount - 1;
		}

		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if(view_class_dialog != null){
				if(view_class_dialog.isShown()){
					view_class_dialog.setVisibility(View.GONE);
					return;
				}
			}			
			if (_lastItem == ImemberApplication.getInstance().stores_old.size()
					&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				if(ImemberApplication.getInstance().stores_old.size() < ImemberApplication.getInstance().getStore_count()){
					pageIndex++;
					System.out.println("diaoyong le a ");
					new StoreList_loadManager(StoreListActivity.this, listView_store, listAdapter_store).load(pageIndex, false);
				}
			}
		}
	}
	
	private void loadSorts(){
		if(ImemberApplication.getInstance().sorts.size() <= 0){
			System.out.println("load-------");
			new Sorts_loadManager_storelist(this).load();
		}
		else{
			System.out.println("noload-------");
			createItemDialog_sort();
		}
	}
	
	public void createItemDialog_sort(){
		if(layout_sort.getChildCount() > 0){
			layout_sort.removeAllViews();
			bundles.clear();
		}
		LinearLayout.LayoutParams layoutPrarams1 = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		layoutPrarams1.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 13, getResources().getDisplayMetrics());
		LinearLayout.LayoutParams layoutPrarams2 = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams layoutPrarams3 = new LinearLayout.LayoutParams(
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()), 
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));
		
//		for(int ii = 0; ii < 2; ii++){
		for (int j = 0; j < (ImemberApplication.getInstance().sorts.size() + 1); j++) {
			Bundle bundle = new Bundle();
			String sort_name = "";
			if(j == 0){
				sort_name = "全部分类";
				sort_id = 0;
//				url_icon = sorts.get(0)
			}
			else{
				sort_name = ImemberApplication.getInstance().sorts.get(j - 1).getSort_name();
				sort_id = ImemberApplication.getInstance().sorts.get(j - 1).getSort_id();
				url_icon = ImemberApplication.getInstance().sorts.get(j - 1).getUrl_icon();
			}
			bundle.putString("sort_name", sort_name);
			bundle.putInt("sort_id", sort_id);
			bundles.add(bundle);
			
			LinearLayout linearLayout_h = new LinearLayout(this);
			linearLayout_h.setOrientation(LinearLayout.HORIZONTAL);
			linearLayout_h.setPadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 13, getResources().getDisplayMetrics()), 
					(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 13, getResources().getDisplayMetrics()), 13, 
					(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 13, getResources().getDisplayMetrics()));
			linearLayout_h.setBackgroundResource(R.drawable.sort_item_bg);
			linearLayout_h.setGravity(Gravity.CENTER_VERTICAL);
			
			if(sort_name.equals("全部分类")){
				ImageView iv = new ImageView(this);
				iv.setLayoutParams(layoutPrarams3);
				iv.setBackgroundResource(R.drawable.sort_icon_all);
				linearLayout_h.addView(iv);
			}
			else{
				ImageView iv = new ImageView(this);
				iv.setLayoutParams(layoutPrarams3);
//				iv.setBackgroundResource(R.drawable.sort_icon_service);
				linearLayout_h.addView(iv);
				setIcons("" + sort_id, url_icon, iv);
			}
			
			final TextView tv = new TextView(this);
			
			tv.setLayoutParams(layoutPrarams1);
//			tv.setPadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()), 
//					(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()), 0, 
//					(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
			tv.setTextColor(getResources().getColor(R.color.light_black2));
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		    tv.setText("" + sort_name);
		    
		    linearLayout_h.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int id = 0;
					for (int i = 0; i < bundles.size(); i++) {
						Bundle bundle = bundles.get(i);
						if(tv.getText().toString().trim().equals(bundle.getString("sort_name"))){
							cate_id = bundle.getInt("sort_id");
							ImemberApplication.getInstance().setCate_id(cate_id);
						}
					}
//					new StoreList_loadManager_sort(StoreListActivity.this, listView_store, listAdapter_store).load(cate_id);
					pageIndex = 1;
					new StoreList_loadManager(StoreListActivity.this, listView_store, listAdapter_store).load(pageIndex, true);
					
//					if(sort_dialog != null){
//						if(sort_dialog.isShowing()){
//							sort_dialog.cancel();
//						}
//					}
					
					if(view_class_dialog != null){
						if(view_class_dialog.isShown()){
							view_class_dialog.setVisibility(View.GONE);
						}
					}
				}
			});
		    linearLayout_h.addView(tv);
			
			layout_sort.addView(linearLayout_h);
			
			ImageView img_line = new ImageView(this);
			img_line.setLayoutParams(layoutPrarams2);
			img_line.setImageResource(R.drawable.classdialog_line);
			
			if(j < (ImemberApplication.getInstance().sorts.size())){
				layout_sort.addView(img_line);
			}
		}
//		}
	}
	
	private void setIcons(final String sort_id, String url_icon, final ImageView logo_iv){
		FileUtils fileUtils=new FileUtils();
		AsyncLoadImageTask _asyncloader = new AsyncLoadImageTask();
		//文件已存在
		if(fileUtils.isFileExist(floderName + "/"+ sort_id + ".png")){
			Bitmap bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/"
					+ floderName + "/" + sort_id + ".png",null);
			Drawable drawable = Drawable.createFromPath(Environment.getExternalStorageDirectory() + "/"
					+ floderName + "/" + sort_id + ".png");
			logo_iv.setBackgroundDrawable(drawable);
		}
		else{
			System.out.println("url_icon------------------------------>" + url_icon);
			if(url_icon != null && !"".equals(url_icon) && !"null".equals(url_icon)){
				Drawable drawable = _asyncloader.loadDrawable(url_icon,
					new AsyncLoadImageTask.ImageCallback() {
						public void imageLoaded(Drawable imageDrawable,String imageUrl) {
							if(imageDrawable != null){
								downLoadBitmap(imageDrawable, sort_id);
								
								logo_iv.setBackgroundDrawable(imageDrawable);
							}
						} 
					});
				if(drawable == null){
					logo_iv.setBackgroundResource(R.drawable.sort_icon_default);
				} else {
					logo_iv.setBackgroundDrawable(drawable);
					downLoadBitmap(drawable, sort_id);
				}
			} else {
				//设置默认图片
				logo_iv.setBackgroundResource(R.drawable.sort_icon_default);
			}
		}
	}
	
	private void downLoadBitmap(Drawable drawable, String sort_id){
		HttpDownLoader httpDownLoader=new HttpDownLoader();
		Bitmap bitmap = ImemberApplication.drawableToBitmap(drawable);
		byte[] b = ImemberApplication.Bitmap2Bytes(bitmap);
		int result=httpDownLoader.downFile(floderName + "/",  sort_id + ".png", b);
	}
	
	private void loadCitys(){
		if(ImemberApplication.getInstance().citys.size() <= 0){
			new Citys_loadManager_storelist(this).load();
		}
		else{
			createItemDialog_city(); 
		}
	}
	
	public void createItemDialog_city(){
		str_citys = new String[ImemberApplication.getInstance().citys.size()];
		for (int j = 0; j < str_citys.length; j++) {
			str_citys[j] = ImemberApplication.getInstance().citys.get(j).getCity_name();
		}
		
		new AlertDialog.Builder(this)
        .setTitle("请选择城市")
        .setItems(str_citys, new DialogInterface.OnClickListener() {
        	
            public void onClick(DialogInterface dialog, final int which) {
            	
            	City city = ImemberApplication.getInstance().citys.get(which);
            	ImemberApplication.getInstance().setNewCity(city);
	            loadStores();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//
			}
		}).create().show();
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			String response = msg.getData().getString("RESPONSE").trim();
			System.out.println("" + response);
		};
	};
	
	private String createJson(){
		JSONObject object = new JSONObject();
		try {
			object.put("city_id", ImemberApplication.getInstance().getCurrentCity().getCity_id());
			
			return ImemberApplication.encode(object.toString(), "UTF-8");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 根据需要将新内容添加在最上位置；
	 * 
	 *@param
	 *@return
	 */
	private void handleList(){
		List<String> list = new ArrayList<String>();
		list.add("刷新后添加的内容");
//		for (int i = 0; i <data.size(); i++) {
//			list.add(data.get(i));
//		}
//		data.clear();
//		data=list;
	}
	
	private void initUI_logon(){
		/**
		 * 登录相关
		 */
		myLogon = new LogonManager(this);
		view_logon = (LinearLayout)findViewById(R.id.view_logon);
		view_islogoning = (LinearLayout)findViewById(R.id.view_islogoning);
		et_username_logon = (EditText)view_logon.findViewById(R.id.logon_username);
		et_password_logon = (EditText)view_logon.findViewById(R.id.logon_password);
		btn_logon = (Button)view_logon.findViewById(R.id.logon_btn_yes);
		btn_login = (TextView)view_logon.findViewById(R.id.btn_header_right);
		tv_forget_psd = (TextView)view_logon.findViewById(R.id.logon_forget);
	}
	
	private void setUI(){
		tv_title.setText(ImemberApplication.getInstance().getCurrentCity().getCity_name());
		
		
		setUI_logon();
	}
	
	private void setUI_logon(){
//		User user = ImemberApplication.getInstance().getUser();
//		if(user != null){
//			et_username_logon.setText("" + user.getUser_name());
//			et_password_logon.set
//		}
		
		btn_logon.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				setString_logon();
				if(notNull_logon()){
					myLogon.setString(userName_logon, passWord_logon);
					myLogon.logon();
				}
			}
			
		});
		btn_login.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(StoreListActivity.this, LoginActivity.class);
				startActivityForResult(intent, 5);
			}
			
		});
		tv_forget_psd.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(StoreListActivity.this, ForgetPsdActivity.class);
				startActivity(intent);
			}
			
		});
	}
	
	private void setString_logon(){
		userName_logon = et_username_logon.getText().toString().trim();
		passWord_logon = et_password_logon.getText().toString();
	}
	
	private boolean notNull_logon(){
		if(userName_logon != null && !"".equals(userName_logon)){
			
		}
		else{
			Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(passWord_logon != null && !"".equals(passWord_logon)){
			
		}
		else{
			Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	private void loadStores(){
		if(ImemberApplication.getInstance().isChange_city()){
//			if(ImemberApplication.getInstance().stores.size() > 0){
//				ImemberApplication.getInstance().stores.clear();
//			}
			ImemberApplication.getInstance().setCurrentCity(ImemberApplication.getInstance().getCurrentCity());
			tv_title.setText(ImemberApplication.getInstance().getCurrentCity().getCity_name());
			pageIndex = 1;
			new StoreList_loadManager(this, listView_store, listAdapter_store).load(pageIndex, true);
		}
		else if(ImemberApplication.getInstance().stores.size() <= 0){
			pageIndex = 1;
			new StoreList_loadManager(this, listView_store, listAdapter_store).load(pageIndex, true);
		}
	}
	
	private void loadStores_flush(){
		pageIndex = 1;
		new StoreList_loadManager(this, listView_store, listAdapter_store).load(pageIndex, true);
	}
	
	private class ItemClickListener_store implements OnItemClickListener {
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long id) {
			if(view_class_dialog != null){
				if(view_class_dialog.isShown()){
					view_class_dialog.setVisibility(View.GONE);
					return;
				}
			}			
			Intent intent = new Intent();
			Store store = ImemberApplication.getInstance().stores_old.get(position - 1);
			intent.putExtra("store", store);
			intent.setClass(StoreListActivity.this, StoreDetailActivity.class);
			startActivity(intent);
		}
	}
	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		System.out.println("调用了啊    ----------------");
//		if (scanProcess == null) {
//			scanProcess = new ScanResultManager(this);
//		}
//		scanProcess.setAll(requestCode, resultCode, data);
//		scanProcess.process_activityResult();
//		super.onActivityResult(requestCode, resultCode, data);
//	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		ImemberApplication.getInstance().getLogonSuccessManager().removeLogonSuccessListener(this);
		ImemberApplication.getInstance().getLoginSuccessManager().removeLoginSuccessListener(this);
		
		System.out.println("StoreListActivity---------kill");
	}
	
	protected void setFooterScanClick() {
		Intent intent = new Intent();
		intent.setClass(StoreListActivity.this, CaptureActivity.class);
		startActivityForResult(intent, 6);
	}

	@Override
	public void logonSuccess() {
		init();
//		setUI_setting();
	}
	@Override
	public void logonFail(String username) {
		// TODO Auto-generated method stub
//		initData();
		et_username_logon.setText("" + username);
	}
	
	@Override
	public void loginSuccess(String userName, String psd) {
		// TODO Auto-generated method stub
		et_username_logon.setText("" + userName);
		et_password_logon.setText("");
	}
	
	@Override
	protected void setFooterClickBg() {
		layout_footer_store.setBackgroundResource(R.drawable.footer_over_bg);
		iv_footer_store.setBackgroundResource(R.drawable.footer_store_click);
	}

//	@Override
//	protected void setFooterListener() {
//		layout_footer_mycard.setOnClickListener(new FooterClickListener());
//		layout_footer_scan.setOnClickListener(new FooterClickListener());
//		layout_footer_reward.setOnClickListener(new FooterClickListener());
//		layout_footer_setting.setOnClickListener(new FooterClickListener());
//	}
	
	@Override
	protected boolean childListener() {
		// TODO Auto-generated method stub
		if(view_class_dialog != null){
			if(view_class_dialog.isShown()){
				view_class_dialog.setVisibility(View.GONE);
				return true;
			}
		}
		return false;
	}
}
