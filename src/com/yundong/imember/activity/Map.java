package com.yundong.imember.activity;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.OverlayItem;
import com.amap.mapapi.map.MapActivity;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.Overlay;
import com.yundong.imember.R;
import com.yundong.imember.customWidget.MyMapView;
import com.yundong.imember.entity.Store;
 
/**
 * 地图类
 * @author Administrator
 *
 */
public class Map extends MapActivity {
	private TextView tv_title;
	private Button back_btn;
	//南昌经纬度
	public static double USER_LONG; 
	public static double USER_LAT;
	
	public static final int NEAR = 1;
	public static final int USER = 2;
	//地图显示
	private MapView mapView;
	//地图控制类
	private MapController mymapController;
	//图层集合
	private List<Overlay> overlays;
	private MapItemizedOverlay nearItemizedoverlay;
	private MapItemizedOverlay userItemizedoverlay;
	private Drawable drawable_user;
	private Drawable drawable_near;
	
    private ZoomControls zoom;
    private Store store;
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        
        getIntentData();
        initUI();
        init_User();
    }
    
    private void getIntentData(){
    	Intent intent = getIntent();
    	Bundle bundle = intent.getExtras();
    	if(bundle != null){
    		store = (Store)bundle.get("store");
    	}
    }
    
    private void initUI(){
    	tv_title = (TextView)findViewById(R.id.tv_header_title);
    	tv_title.setText("" + store.getStore_title());
    	
    	back_btn = (Button)findViewById(R.id.btn_header_left);
    	back_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
    		
    	});
    }
    
	private void init_User(){
        mapView = (MyMapView)findViewById(R.id.map_view);
        
    	zoom=(ZoomControls)findViewById(R.id.map_zoomcontrols);
    	
        //设置地图支持缩放
        mapView.setBuiltInZoomControls(false);
        //设置为激活状态
        mapView.setEnabled(true);
        mymapController = mapView.getController();
        
        zoom.setOnZoomInClickListener(new ZoomControls.OnClickListener(){
	    	public void onClick(View arg0) {
//	    		if(!isLoading && canZoom){
		    		int level = mapView.getZoomLevel();
		    		mymapController.setZoom(level + 1);
//		    		isLoading = true;
//	    		}
	    	}

    	});
    	zoom.setOnZoomOutClickListener(new ZoomControls.OnClickListener(){
	    	public void onClick(View arg0) {
//	    		if(!isLoading && canZoom){
		    		int level = mapView.getZoomLevel();
//		    		if(level > 17){
		    			mymapController.setZoom(level - 1);
//		    			isLoading = true;
//	    			}
//	    		}
	    	}

    	});
        
        //设置放大后的当前层级
        mymapController.setZoom(16);
        //地图模式
        mapView.setSatellite(false);
        //获得图层集合
        overlays = mapView.getOverlays();
        overlays.clear();
        
        drawable_user = Map.this.getResources().getDrawable(R.drawable.map_biaozhu_user);
        userItemizedoverlay = new MapItemizedOverlay(drawable_user, Map.this, mapView);
        userItemizedoverlay.setType(USER);
        drawable_near = Map.this.getResources().getDrawable(R.drawable.map_biaozhu_near);
        nearItemizedoverlay = new MapItemizedOverlay(drawable_near, Map.this, mapView);
//      nearItemizedoverlay = new MapItemizedOverlay(Map.this);
        nearItemizedoverlay.setType(NEAR);
        
        //获取用户当前地理位置
        setUserLocation();
    }
	
	//最后一个参数表示是否是中心点
	private void setPosition(Double px, Double py, String houseTitle,	String houseMessage, boolean center, int type){
        GeoPoint point = new GeoPoint((int)(py * 1E6),
        		(int)(px * 1E6));
        OverlayItem overlayitem = null;
        
        if(center){
//        	mymapController.setCenter(point);
        }
        
        switch(type){
//        case QUERY:
//        	overlayitem = new OverlayItem(point, houseTitle, houseMessage);
//            queryItemizedoverlay.addOverlay(overlayitem);
//        	break;
        case NEAR:
        	overlayitem = new OverlayItem(point, houseTitle, houseMessage);
	        nearItemizedoverlay.addOverlay(overlayitem);
        	break;
        case USER:
        	overlayitem = new OverlayItem(point, houseTitle, houseMessage);
        	userItemizedoverlay.addOverlay(overlayitem);
        	break;
        }
	}
	
	private void setUserLocation(){
		USER_LONG = store.getXpoint();
		USER_LAT = store.getYpoint();
		GeoPoint center = new GeoPoint((int)(USER_LAT * 1E6),
        		(int)(USER_LONG * 1E6));
		mymapController.setCenter(center);
		
		setPosition(USER_LONG, USER_LAT, "所在的位置", "", true, USER);
        overlays.add(userItemizedoverlay);
	}
    
	@Override
    protected boolean isRouteDisplayed() {
        return false;
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			finish();
		}
		return false;
	}
}