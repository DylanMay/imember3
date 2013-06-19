package com.yundong.imember.datamanager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.yundong.imember.ImemberApplication;
import com.yundong.imember.R;
import com.yundong.imember.activity.DownloadCompleteActivity;
import com.yundong.imember.customWidget.MyDialog;
import com.yundong.imember.db.SQLite;
import com.yundong.imember.http.GetServiceDataHandler;
import com.yundong.imember.http.GetServiceDataHandlerListener;
import com.yundong.imember.thread.MyThread_get;

public class updateManager {
	private Context _context;
	private MyDialog myDialog;
	
	private Long fileSize = 1l;
    private int downLoadFileSize;
    
    private NotificationManager m_NotificationManager; 
    private Notification  m_Notification;
    private PendingIntent  m_PendingIntent;
    private Intent mIntent;
	
	public updateManager(Context context) {
		_context = context;
		initNotification();
	}
	
	private void initIntent(){
		mIntent = new Intent();
		mIntent.setClass(_context, DownloadCompleteActivity.class);
	}
	
	private void initNotification(){
		initIntent();
		
		m_NotificationManager = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
		//点击通知时转移内容    
	    //  m_Intent = new Intent(LoginActivity.this, MainActivity.class);    
	            
	    //主要是设置点击通知时显示内容的类    
	    m_PendingIntent = PendingIntent.getActivity(_context, 0, mIntent, 0); //如果轉移內容則用m_Intent();  
		m_Notification = new Notification();
	}
	
	public void check(){
//		createProcessDialog();
		String url = ImemberApplication.WEBROOT + ImemberApplication.URL_UPDATE + createJson();
//		System.out.println("检测软件更新url--------->" + url);
		new MyThread_get(handler_update, url, _context).start();
		
//		doNewVersionUpdate("", 1);
	}
	
	private String createJson(){
		JSONObject object = new JSONObject();
		try {
			object.put("p_type", 1);
			object.put("p_version", _context.getResources().getText(R.string.version_interface).toString());
			
			return ImemberApplication.encode(object.toString(), "UTF-8");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	private GetServiceDataHandler handler_update = new GetServiceDataHandler("检查更新", new GetServiceDataHandlerListener(){

		@Override
		public void process_cancel_handler() {
			// TODO Auto-generated method stub
			if(myDialog != null){
				if (myDialog.isShowing()) {
					myDialog.dismiss();
					myDialog = null;
				}
			}
		}

		@Override
		public int process_fail_handler(String response) {
			// TODO Auto-generated method stub
			if(myDialog != null){
				if (myDialog.isShowing()) {
					myDialog.dismiss();
					myDialog = null;
				}
			}
			return ImemberApplication.DIALOG_1BTN;
		}

		@Override
		public void process_retry_handler() {
			// TODO Auto-generated method stub
		}

		@Override
		public void process_success_handler(String response) {
			// TODO Auto-generated method stub
			System.out.println("result--->" + response);
			try {
				JSONObject object = new JSONObject(response);
				String recode = object.getString("recode");
				String msg = object.getString("msg");
				
				if(recode != null){
					if(!ImemberApplication.RESULT_STATUS.result_basic_success.equals(recode)){
						MyDialog myDialog = new MyDialog(_context);
						myDialog.setTitle("提示");
						myDialog.setIcon(android.R.drawable.ic_dialog_info);
						myDialog.setCancelable(false);
						myDialog.setMessage(msg);
						myDialog.setLeftButton("知道了", new MyDialog.MyOnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
							}
							
						});
						myDialog.create();
						myDialog.show();
						
						return ;
					}
				}
				else{
					return ;
				}
				String data = object.getString("data");
				JSONObject object1 = new JSONObject(data);
				String p_type = object1.getString("p_type");
				String p_version = object1.getString("p_version");
				String p_url = object1.getString("p_url").trim();
				System.out.println("p_url---->" + p_url);
				int is_must = object1.getInt("is_must");
				
				if(p_url != null && !"".equals(p_url) && !"null".equals(p_url)){
					String str[] = p_url.split("_");
					ImemberApplication.getInstance().apk_name = "imember_" + str[1];
					System.out.println("apkNanme------>" + ImemberApplication.getInstance().apk_name);
//					
//					p_url = ImemberApplication.WEBROOT + p_url.substring(2).replaceAll("\\\\", "");
					
					System.out.println("下载地址---->" + p_url);
					doNewVersionUpdate(p_url, is_must);
				}
				else{
//					notNewVersionShow();
				}
			}catch (JSONException e) {
				// TODO: handle exception
			}finally{
				if(myDialog != null){
					if (myDialog.isShowing()) {
						myDialog.dismiss();
						myDialog = null;
					}
				}
			}
		}
		
	}, _context);
	
	private void createProcessDialog(){
		myDialog = new MyDialog(_context);
		myDialog.setTitle("正在加载数据...");
		myDialog.setIcon(android.R.drawable.ic_dialog_info);
		myDialog.setCancelable(false);
		myDialog.setLoad();
		myDialog.setLeftButton("取消", new MyDialog.MyOnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				handler_update.setIsCancel(true);
			}
			
		});
		myDialog.create();
		myDialog.show();
	}
	
	//不是新版本
	private void notNewVersionShow() {
	    StringBuffer sb = new StringBuffer();
	    sb.append("已是最新版,无需更新!");
	    
	    MyDialog dialog = new MyDialog(_context);
	    dialog.setTitle("提示");
//		    dialog.setIcon(R.drawable.my_info_bg);
	    dialog.setMessage(sb.toString());
	    dialog.setCancelable(false);
	    dialog.setLeftButton("确定", new MyDialog.MyOnClickListener(){
        	@Override
			public void onClick(DialogInterface dialog, int which) {
            	
            }  
        });
	    dialog.create();
	    dialog.show();
	}
	
	//子函数，若是有新版本，则
		private void doNewVersionUpdate(final String url, int is_must) {
			StringBuffer sb = new StringBuffer();
			final MyDialog myDialog1 = new MyDialog(_context);
			
			switch(is_must){
			case 0:
			    sb.append("检查到新版本，是否更新？");
				myDialog1.setTitle("软件更新");
				myDialog1.setIcon(android.R.drawable.ic_dialog_info);
				myDialog1.setMessage(sb.toString());
				myDialog1.setCancelable(false);
				myDialog1.setLeftButton("是", new MyDialog.MyOnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						myDialog = new MyDialog(_context);
						myDialog.setTitle("正在下载");
						myDialog.setIcon(android.R.drawable.ic_dialog_info);
						myDialog.setCancelable(false);
						myDialog.setLoad();
						myDialog.setMessage("0%");
//						myDialog.setRightButton("知道了", new MyDialog.MyOnClickListener() {
		//
//							@Override
//							public void onClick(View dialog, int which) {
//								// TODO Auto-generated method stub
//							}
//							
//						});
						myDialog.create();
						myDialog.show();
						
		                downFile(url);
					}
				});
				myDialog1.setRightButton("否", new MyDialog.MyOnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				});
				myDialog1.create();
				myDialog1.show();
				break;
			case 1:
			    sb.append("系统检查到新的版本，必须更新后才能继续使用。是否现在下载安装更新版本？");
				myDialog1.setTitle("软件更新");
				myDialog1.setIcon(android.R.drawable.ic_dialog_info);
				myDialog1.setMessage(sb.toString());
				myDialog1.setCancelable(false);
				myDialog1.setLeftButton("安装更新", new MyDialog.MyOnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						myDialog = new MyDialog(_context);
						myDialog.setTitle("正在下载");
						myDialog.setIcon(android.R.drawable.ic_dialog_info);
						myDialog.setCancelable(false);
						myDialog.setLoad();
						myDialog.setMessage("0%");
//						myDialog.setRightButton("知道了", new MyDialog.MyOnClickListener() {
		//
//							@Override
//							public void onClick(View dialog, int which) {
//								// TODO Auto-generated method stub
//							}
//							
//						});
						myDialog.create();
						myDialog.show();
						
		                downFile(url);
					}
				});
				myDialog1.setRightButton("退出程序", new MyDialog.MyOnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						ImemberApplication.getInstance().getFinishActivityManager().finishAllSubActivitys();
		            	 ImemberApplication.getInstance().getFinishActivityManager().finishAllMainActivitys();
		            	 
		            	 SQLite sqlite = ImemberApplication.getInstance().getSQLite();
		            	 if(sqlite.queryCity()){
		            		 sqlite.deleteCity();
		            	 }
		            	 sqlite.insertCity(ImemberApplication.getInstance().getCurrentCity());
		            	 
		            	 System.exit(0);
					}
				});
				myDialog1.create();
				myDialog1.show();
				break;
			}
		}
		
		//获取软件名
		public static String getApkName(Context context) {  
	        String verName = ImemberApplication.getInstance().apk_name;
	        return verName;
		}
		
		//下载软件
		private void downFile(final String url) {
//			myDialog.show();
			final File file = new File(Environment.getExternalStorageDirectory(), getApkName(_context));
			if(file.exists()){
				file.delete();
			}
			
			new Thread() {
				public void run() {
					HttpClient client = new DefaultHttpClient();
					// params[0]代表连接的url     
					HttpGet get = new HttpGet(url);     
					HttpResponse response;     
					try {     
						response = client.execute(get);     
						HttpEntity entity = response.getEntity();     
						fileSize = entity.getContentLength();
						//注意如果值超过int范围即报错
						int fileMaxSize = fileSize.intValue();
						
						InputStream is = entity.getContent();     
						FileOutputStream fileOutputStream = null;     
						if (is != null) {     
//							File file = new File(Environment.getExternalStorageDirectory(), getApkName(_context));
							fileOutputStream = new FileOutputStream(file);
							byte[] buf = new byte[1024];
							int ch = -1;     
							while ((ch = is.read(buf)) != -1) {
								fileOutputStream.write(buf, 0, ch);     
								downLoadFileSize += ch;
//								myDialog.setProgress(downLoadFileSize*100/fileMaxSize + "%");
								Message message = handler_update_message.obtainMessage();
								message.arg1 = fileMaxSize;
								handler_update_message.sendMessage(message);
							}
						}     
						fileOutputStream.flush();     
						if (fileOutputStream != null) {     
							fileOutputStream.close();     
						}     
						if(is != null){
							is.close();
						}
						if(myDialog != null){
							if(myDialog.isShowing()){
								myDialog.cancel();
							}
						}
						downLoadFileSize = 0;
//						handler_downLoadcomplete.sendEmptyMessage(0);
						showNotification();
						
//						Intent intent = new Intent();
//						intent.setAction(StaticData.DOWNLOAD_COMPLETE);
//						_context.sendBroadcast(intent);
						
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block     
						e.printStackTrace();     
					} catch (IOException e) {     
						// TODO Auto-generated catch block     
						e.printStackTrace();     
					}     
				}     
			}.start();
		}
		
		private Handler handler_update_message = new Handler(){
			public void handleMessage(android.os.Message msg) {
				int fileMaxSize = msg.arg1;
				myDialog.setMessage(downLoadFileSize*100/fileMaxSize + "%");
			};
		};
		
		private void showNotification(){
			m_Notification.icon = R.drawable.ic_launcher;
	        //当我们点击通知时显示的内容    
	        m_Notification.tickerText = "微会员";
	        //通知时发出默认的声音    
	        m_Notification.defaults = Notification.DEFAULT_SOUND;    
	        //设置通知显示的参数
	        m_Notification.setLatestEventInfo(_context, "微会员下载完成", 
	        		"是否现在安装？\n如果不马上安装，则可手动安装，下载的文件保存在SD卡根目录，软件名称是" +
	        				ImemberApplication.getInstance().apk_name, 
	        		m_PendingIntent);
	        //可以理解为执行这个通知
	        m_NotificationManager.notify(0, m_Notification);
		}
}
