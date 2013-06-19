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
		//���֪ͨʱת������    
	    //  m_Intent = new Intent(LoginActivity.this, MainActivity.class);    
	            
	    //��Ҫ�����õ��֪ͨʱ��ʾ���ݵ���    
	    m_PendingIntent = PendingIntent.getActivity(_context, 0, mIntent, 0); //����D�ƃ��݄t��m_Intent();  
		m_Notification = new Notification();
	}
	
	public void check(){
//		createProcessDialog();
		String url = ImemberApplication.WEBROOT + ImemberApplication.URL_UPDATE + createJson();
//		System.out.println("����������url--------->" + url);
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
	
	private GetServiceDataHandler handler_update = new GetServiceDataHandler("������", new GetServiceDataHandlerListener(){

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
						myDialog.setTitle("��ʾ");
						myDialog.setIcon(android.R.drawable.ic_dialog_info);
						myDialog.setCancelable(false);
						myDialog.setMessage(msg);
						myDialog.setLeftButton("֪����", new MyDialog.MyOnClickListener() {

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
					
					System.out.println("���ص�ַ---->" + p_url);
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
		myDialog.setTitle("���ڼ�������...");
		myDialog.setIcon(android.R.drawable.ic_dialog_info);
		myDialog.setCancelable(false);
		myDialog.setLoad();
		myDialog.setLeftButton("ȡ��", new MyDialog.MyOnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				handler_update.setIsCancel(true);
			}
			
		});
		myDialog.create();
		myDialog.show();
	}
	
	//�����°汾
	private void notNewVersionShow() {
	    StringBuffer sb = new StringBuffer();
	    sb.append("�������°�,�������!");
	    
	    MyDialog dialog = new MyDialog(_context);
	    dialog.setTitle("��ʾ");
//		    dialog.setIcon(R.drawable.my_info_bg);
	    dialog.setMessage(sb.toString());
	    dialog.setCancelable(false);
	    dialog.setLeftButton("ȷ��", new MyDialog.MyOnClickListener(){
        	@Override
			public void onClick(DialogInterface dialog, int which) {
            	
            }  
        });
	    dialog.create();
	    dialog.show();
	}
	
	//�Ӻ������������°汾����
		private void doNewVersionUpdate(final String url, int is_must) {
			StringBuffer sb = new StringBuffer();
			final MyDialog myDialog1 = new MyDialog(_context);
			
			switch(is_must){
			case 0:
			    sb.append("��鵽�°汾���Ƿ���£�");
				myDialog1.setTitle("�������");
				myDialog1.setIcon(android.R.drawable.ic_dialog_info);
				myDialog1.setMessage(sb.toString());
				myDialog1.setCancelable(false);
				myDialog1.setLeftButton("��", new MyDialog.MyOnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						myDialog = new MyDialog(_context);
						myDialog.setTitle("��������");
						myDialog.setIcon(android.R.drawable.ic_dialog_info);
						myDialog.setCancelable(false);
						myDialog.setLoad();
						myDialog.setMessage("0%");
//						myDialog.setRightButton("֪����", new MyDialog.MyOnClickListener() {
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
				myDialog1.setRightButton("��", new MyDialog.MyOnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				});
				myDialog1.create();
				myDialog1.show();
				break;
			case 1:
			    sb.append("ϵͳ��鵽�µİ汾��������º���ܼ���ʹ�á��Ƿ��������ذ�װ���°汾��");
				myDialog1.setTitle("�������");
				myDialog1.setIcon(android.R.drawable.ic_dialog_info);
				myDialog1.setMessage(sb.toString());
				myDialog1.setCancelable(false);
				myDialog1.setLeftButton("��װ����", new MyDialog.MyOnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						myDialog = new MyDialog(_context);
						myDialog.setTitle("��������");
						myDialog.setIcon(android.R.drawable.ic_dialog_info);
						myDialog.setCancelable(false);
						myDialog.setLoad();
						myDialog.setMessage("0%");
//						myDialog.setRightButton("֪����", new MyDialog.MyOnClickListener() {
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
				myDialog1.setRightButton("�˳�����", new MyDialog.MyOnClickListener() {

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
		
		//��ȡ�����
		public static String getApkName(Context context) {  
	        String verName = ImemberApplication.getInstance().apk_name;
	        return verName;
		}
		
		//�������
		private void downFile(final String url) {
//			myDialog.show();
			final File file = new File(Environment.getExternalStorageDirectory(), getApkName(_context));
			if(file.exists()){
				file.delete();
			}
			
			new Thread() {
				public void run() {
					HttpClient client = new DefaultHttpClient();
					// params[0]�������ӵ�url     
					HttpGet get = new HttpGet(url);     
					HttpResponse response;     
					try {     
						response = client.execute(get);     
						HttpEntity entity = response.getEntity();     
						fileSize = entity.getContentLength();
						//ע�����ֵ����int��Χ������
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
	        //�����ǵ��֪ͨʱ��ʾ������    
	        m_Notification.tickerText = "΢��Ա";
	        //֪ͨʱ����Ĭ�ϵ�����    
	        m_Notification.defaults = Notification.DEFAULT_SOUND;    
	        //����֪ͨ��ʾ�Ĳ���
	        m_Notification.setLatestEventInfo(_context, "΢��Ա�������", 
	        		"�Ƿ����ڰ�װ��\n��������ϰ�װ������ֶ���װ�����ص��ļ�������SD����Ŀ¼�����������" +
	        				ImemberApplication.getInstance().apk_name, 
	        		m_PendingIntent);
	        //�������Ϊִ�����֪ͨ
	        m_NotificationManager.notify(0, m_Notification);
		}
}
