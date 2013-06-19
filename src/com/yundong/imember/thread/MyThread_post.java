package com.yundong.imember.thread;

import java.util.HashMap;

import org.apache.http.HttpHost;
import org.apache.http.conn.params.ConnRouteParams;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;

import com.yundong.imember.customWidget.MyDialog;
import com.yundong.imember.http.HTTPRequestHelper;
import com.yundong.imember.utils.NetworkControl;
import com.yundong.imember.utils.NetworkControl.NetType;

public class MyThread_post {
	private Context _context;
	private String _url;
	private Handler _handler;
	private HTTPRequestHelper httpHelper_special;
	private HashMap<String, String> map;
	
	public MyThread_post(Handler handler, String url, Context context, HashMap<String, String> map) {
		// TODO Auto-generated constructor stub
		_handler = handler;
		_url = url;
		_context = context;
		this.map = map;
	}
	
	private void openUserNetwork(final Context context){
//		   if(context.getClass() == CaptureActivity.class){
//			   return;
//		   }
		   if (NetworkControl.getNetworkState(context)){
				NetType netType = NetworkControl.getNetType(context);

				if (netType != null) {
					//GPRSҪ���ô���
					if(netType.isWap()){
						HttpHost proxy = new HttpHost( "10.0.0.172", 80, "http");
						HTTPRequestHelper.client.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
					}
				}
				new Thread(){
					@Override
					public void run() {
						httpHelper_special = new HTTPRequestHelper(_handler, context);
						try {
							httpHelper_special.performPost(_url, null, null, null, map);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}.start();
			}
			//û������
			else{
				createDialog(context);
			}
	   }
	
	public void start(){
		openUserNetwork(_context);
	}
	
	private void createDialog(final Context context){
		final MyDialog myDialog = new MyDialog(context);
		myDialog.setTitle("��ʾ");
		myDialog.setIcon(android.R.drawable.ic_dialog_info);
		myDialog.setMessage("���ֻ���ǰ���粻���ã�������������ԣ�");
		myDialog.setCancelable(false);
		myDialog.setLeftButton("��������", new MyDialog.MyOnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				intoNetSetting(context);
			}
		});
		myDialog.setRightButton("ȡ��", new MyDialog.MyOnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		myDialog.create();
		myDialog.show();
	}
	   
	 //�������ý���
		private void intoNetSetting(Context context){
			Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
			context.startActivity(intent);
		}
}
