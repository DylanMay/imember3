package com.yundong.imember.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.amap.mapapi.core.OverlayItem;
import com.amap.mapapi.map.ItemizedOverlay;
import com.amap.mapapi.map.MapView;
import com.yundong.imember.R;
import com.yundong.imember.customWidget.MyDialog;
import com.yundong.imember.entity.Store;
import com.yundong.imember.http.GetServiceDataHandler;
import com.yundong.imember.http.GetServiceDataHandlerListener;


//OverlayItem为地图标记，
//ItemizedOverlay是一个地图标记图层包含了许多地图标记，继承自Overlay
public class MapItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	//通过这个arraylist将所需要的地标OverlayItem标到地图上
    private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
    private ArrayList<Button> buttonList = new ArrayList<Button>();
    private Activity mContext;
    private OverlayItem item;
    private MapView  _MapView;
    private AddButtonHandler addButtonHandler = new AddButtonHandler();
    private PaintFlagsDrawFilter pfd;
    
    private int type;
    
    private boolean isCancel_loadOneStore;
    private int near_id;
    
    public void setType(int type) {
		this.type = type;
	}

	public MapItemizedOverlay(Drawable defaultMarker) {
        super(boundCenterBottom(defaultMarker));
    }
	
	public MapItemizedOverlay() {
        super(null);
    }
	
	public MapItemizedOverlay(Drawable defaultMarker, Activity context, 
			MapView  MapView) {
        this(defaultMarker);
//		super(null);
        this.mContext = context;
        _MapView = MapView;
        
        pfd = new PaintFlagsDrawFilter(0,Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
    }
	
    public MapItemizedOverlay(Activity context) {
        this();
        this.mContext = context;
    }

    //将OverlayItems加到arraylist
    public void addOverlay(OverlayItem overlay) {
        mOverlays.add(overlay);
        setLastFocusedIndex(-1);
        
//        Message msg = addButtonHandler.obtainMessage();
//        msg.obj = overlay;
//        addButtonHandler.sendMessage(msg);
        
        //每添加一个就调用populate，用来读取所有的OverlayItems并准备画到图层上
        //调用完此方法后会调用createItem，重写这个函数获取arraylist中OverlayItem的指示
        //它用来触发每一个OverlayItem的创建
        populate();
    }
    
    private class AddButtonHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			//刷新地图
			addButton((OverlayItem)msg.obj);
		} 
	}
    
    public void addButton(OverlayItem overlay){
    	Button button = new Button(mContext);
    	button.setGravity(Gravity.CENTER);
		button.setText("     " + overlay.getTitle());
		switch(type){
		case Map.USER:
			button.setTextColor(Color.BLACK);
			button.setOnClickListener(new MylocalOnClickListener());
			break;
//		case Map.QUERY:
		case Map.NEAR:
			button.setTextColor(Color.RED);
			button.setOnClickListener(new ButtonOnClickListener());
			break;
		}
//		button.setBackgroundResource(R.drawable.map_qipao);
		MapView.LayoutParams lp = new MapView.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				null, MapView.LayoutParams.BOTTOM_CENTER);
		lp.point = overlay.getPoint();//这行用于popView的定位
		_MapView.addView(button, lp);
		
    	buttonList.add(button);
    	button.setId(buttonList.indexOf(button));
    }
    
    public void clear(){
    	mOverlays.clear();
    	buttonList.clear();
		//防止点击图标出现数组下标越界
		setLastFocusedIndex(-1);
    	populate();
    }
    
    @Override
    protected OverlayItem createItem(int i) {
        return mOverlays.get(i);
    }

    @Override
    public int size() {
        return mOverlays.size();
    }

    //响应用户的触摸
//    @Override
//    protected boolean onTap(int index) {
//    	return false;
//    }
    
    @Override 
    protected boolean onTap(final int index) { 
	    OverlayItem item = mOverlays.get(index); 
	    
	    return true;
    } 
    
    private class ButtonOnClickListener implements OnClickListener{
    	
		public void onClick(View v) {
			try {
				item = mOverlays.get(v.getId());
			} catch (Exception e) {
				item = null;
			}
			if(item == null){
				return ;
			}
			
//			AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
//				//对话框标题设置地图标记所包含的标题
//		      dialog.setTitle(item.getTitle());
//		      //对话框设置内容为地图标记所包含的内容
//		      dialog.setMessage("地址:" + item.getSnippet());
//		      dialog.setNegativeButton(R.string.dialog_close,
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog, int whichButton) 
//							{
//								
//							} 
//		      });
//		      dialog.setPositiveButton(R.string.dialog_queryHouse,
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog, int whichButton) 
//							{
//								Intent intent = new Intent();
//								intent.putExtra("footerId", MetaData.MAP);
//								intent.putExtra("estateName", item.getTitle());
//				    			intent.setClass(mContext, SearchActivity.class);
//				    			//0代表的是请求跳转到添加界面
//				    			mContext.startActivityForResult(intent, 0);
//							}
//						});
//		      //显示对话框
//		      dialog.show();
		}
	} 
    
    private class MylocalOnClickListener implements OnClickListener{
    	
		public void onClick(View v) {
			
//			AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
//				//对话框标题设置地图标记所包含的标题
//		      dialog.setTitle("您所在的位置");
//		      dialog.setNegativeButton(R.string.dialog_close,
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog, int whichButton) 
//							{
//								
//							} 
//		      });
//		      //显示对话框
//		      dialog.show();
		}
	} 

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		
		try{
//			if(type == Map.USER){
//				canvas.setDrawFilter(pfd);
//
//				Paint paint = new Paint();
////	            paint.setColor(Color.BLACK);
//				paint.setColor(Color.parseColor("#38BCEB"));
//	            paint.setDither(true);   
//	            paint.setStyle(Paint.Style.FILL);
//	            paint.setAlpha(9);
//	//            paint.setStrokeCap(Paint.Cap.ROUND);   
//	//            paint.setStrokeJoin(Paint.Join.ROUND);   
////	            paint.setStrokeWidth(3);
//	            Projection projection = mapView.getProjection();   
//	            Point p1 = new Point();
//	            projection.toPixels(new GeoPoint((int) (Map.USER_LAT * 1E6), (int) (Map.USER_LONG * 1E6)), p1);
//	            canvas.drawCircle(p1.x, p1.y, projection.metersToEquatorPixels(StaticData.RADIUS), paint);
//	            
//	            paint.setColor(Color.parseColor("#38BCEB"));
////	            paint.setColor(Color.BLUE);
//	            paint.setDither(true);   
//	            paint.setStyle(Paint.Style.STROKE);
//	            paint.setStrokeWidth(2);
////	            paint.setAlpha(50);
//	            canvas.drawCircle(p1.x, p1.y, projection.metersToEquatorPixels(StaticData.RADIUS), paint);
//			}
            
          //shadow阴影
			super.draw(canvas, mapView, shadow);
		}
		catch(Exception e){
			e.printStackTrace();
			return ;
		}
	}
}