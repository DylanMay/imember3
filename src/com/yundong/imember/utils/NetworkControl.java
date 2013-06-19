package com.yundong.imember.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkControl {

	/**
	 * �жϵ�ǰ����״̬�Ƿ����
	 * @param context
	 * @return
	 */
	public static boolean getNetworkState(Context context){
		ConnectivityManager connectivityManager = (ConnectivityManager) 
					context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo(); 

		if(info != null){
			return true;
		}

		return false;
	}

	/**
	 * �ж���������
	 * @param context
	 * @return
	 */
	public static NetType getNetType(Context context){
		ConnectivityManager connectivityManager = (ConnectivityManager) 
					context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivityManager == null) {
			return null; 
		}

		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if(info == null){ 
			return null;
		}

		String type = info.getTypeName();

		if (type.equalsIgnoreCase("WIFI")) {
			NetType netType = new NetType();
			netType.setWIFI(true);
			return netType;
			// WIFI��ʽ
//			return null;
		} else if(type.equalsIgnoreCase("MOBILE") &&
				info.getExtraInfo().equals("cmwap")) {
			// GPRS��ʽ
//			String proxyHost = android.net.Proxy.getDefaultHost();
//	        if (proxyHost != null && !proxyHost.equals("")) {
////	        	// WAP��ʽ
	        	NetType netType = new NetType();
//	            netType.setProxy(proxyHost);
//	        	netType.setPort(android.net.Proxy.getDefaultPort());
	        	netType.setWap(true);
	        	return netType;
//	        }
		}
		return null;
	}

	public static class NetType {
		private String apn = "";
		private String proxy = "";
		private String typeName = "";
		private int port = 0;
		private boolean isWap = false;
		private boolean isWIFI = false;

		public boolean isWIFI() {
			return isWIFI;
		}
		public void setWIFI(boolean isWIFI) {
			this.isWIFI = isWIFI;
		}
		public String getApn() {
			return apn;
		}
		public void setApn(String apn) {
			this.apn = apn;
		}
		public int getPort() {
			return port;
		}
		public void setPort(int port) {
			this.port = port;
		}
		public String getProxy() {
			return proxy;
		}
		public void setProxy(String proxy) {
			this.proxy = proxy;
		}
		public boolean isWap() {
			return isWap;
		}
		public void setWap(boolean isWap) {
			this.isWap = isWap;
		}
		public String getTypeName() {
			return typeName;
		}
		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}
	}

}
