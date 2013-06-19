package com.yundong.imember;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Application;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.yundong.imember.db.SQLite;
import com.yundong.imember.entity.City;
import com.yundong.imember.entity.MyCard;
import com.yundong.imember.entity.Reward;
import com.yundong.imember.entity.Sort;
import com.yundong.imember.entity.Store;
import com.yundong.imember.entity.StoreCard;
import com.yundong.imember.entity.User;
import com.yundong.imember.myinterface.FinishActivityManager;
import com.yundong.imember.myinterface.LoginSuccessManager;
import com.yundong.imember.myinterface.LogonSuccessManager;

public class ImemberApplication extends Application {
	private static  ImemberApplication instance;
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	private int index_footer;
	public static final int FOOTER_ID_STORE = 0;
	public static final int FOOTER_ID_MYCARD = 1;
	public static final int FOOTER_ID_SCAN = 2;
	public static final int FOOTER_ID_REWARD = 3;
	public static final int FOOTER_ID_SETTING = 4;
	
	private static SQLite sqlite;
	private boolean isAutoLogon;
	private boolean isLogon;
	private boolean isFirst=true;
	private boolean isScanSuccess;
	private String scanResult;
	private String userName_current;
	private User user;
	private String username;
	private String login_msg_id;
	private City city_current;
	private City city_gps;
	private City city_new;
	private LogonSuccessManager logonSuccessManager;
	private LoginSuccessManager loginSuccessManager;
	private FinishActivityManager finishActivityManager;
	
	public ArrayList<Store> stores_old;
	public ArrayList<Store> stores;
	public ArrayList<Store> stores_other;
	public ArrayList<MyCard> myCards;
	public ArrayList<StoreCard> storeCards;
	public ArrayList<Reward> rewards;
	public ArrayList<City> citys;
	public ArrayList<Sort> sorts;
	
	private int reward_sort = 0;
	
	private int store_count;
	
	private int cate_id;
	
	public int getCate_id() {
		return cate_id;
	}

	public void setCate_id(int cate_id) {
		this.cate_id = cate_id;
	}

	public int getStore_count() {
		return store_count;
	}

	public void setStore_count(int store_count) {
		this.store_count = store_count;
	}

	public int getReward_sort() {
		return reward_sort;
	}

	public void setReward_sort(int reward_sort) {
		this.reward_sort = reward_sort;
	}
	
	public String getLogin_msg_id() {
		return login_msg_id;
	}

	public void setLogin_msg_id(String login_msg_id) {
		this.login_msg_id = login_msg_id;
	}
	
	public String apk_name;

	/**
	 * �ٶȶ�λ��ر���
	 */
	public LocationClient mLocationClient = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	private long fail_start;
	private long fail_end;
	private Double lat = 0.0;
	private Double lon = 0.0;
	
	/**
	 * ��̨���нӿ�
	 * @return
	 */
//	public static String WEBROOT = "http://ws.myimember.net/";
	public static String WEBROOT = "http://ws.imember.cc/";
//	public static String WEBROOT = "http://ws.myimember.net/";

	
	public static String URL_SHOPLIST = "index.php?c=shop&act=shopList&req=";
	public static String URL_SORTLIST = "index.php?c=cate&act=cateList";
	public static String URL_MYCARDLIST = "index.php?c=mcard&act=userMcardList&req=";
	public static String URL_MYCARDDETAIL = "index.php?c=mcard&act=userMcardView&req=";
	public static String URL_REWARDLIST = "index.php?c=reward&act=userRewardList&req=";
	public static String URL_REWARDDETAIL = "index.php?c=reward&act=userRewardView&req=";
	public static String URL_USEREWARD = "index.php?c=reward&act=userRewardUse&req=";
	public static String URL_STORECARDLIST = "index.php?c=mcard&act=suppMcardList&req=";
	public static String URL_STOREDETAIL = "index.php?c=supplier&act=suppView&req=";
	public static String URL_CARDAPPLY = "index.php?c=mcard&act=userMcardAdd&req=";
	public static String URL_ADDYINHUA = "index.php?c=stamp&act=addUserStamp&req=";
	public static String URL_LOGON = "index.php?c=user&act=login&req=";
	public static String URL_LOGIN = "index.php?c=user&act=register&req=";
	public static String URL_CITYS = "index.php?c=city&act=cityList";
	public static String URL_FINDPSD_EMAIL = "index.php?c=user&act=userLostPwdByEmail&req=";
	public static String URL_FINDPSD_MOBILE = "index.php?c=user&act=userUpdatePwdByPhone&req=";
	public static String URL_FEEDBACK = "index.php?c=user&act=userFeedBack&req=";
	public static String URL_UPDATE = "index.php?c=system&act=checkPackage&req=";
	public static String URL_CHANGEPSD = "index.php?c=user&act=userUpdatePwd&req=";
	public static String URL_REFERVALIDATE = "index.php?c=system&act=verifySmsCode&req=";
	public static String URL_GETVALIDATE = "index.php?c=system&act=sendSmsCode&req=";
	public static String URL_CHANGEEMAIL = "index.php?c=user&act=userUpdateEmail&req=";
	public static String URL_CHANGEMOBILE= "index.php?c=user&act=userUpdatePhoneBySms&req=";
	
	public static HashMap<String , String> result_status;
	
	public final String PACKAGE_NAME = "com.yundong.imember";
	
	/**
	 * ��¼ע�����
	 * @return
	 */
//	public static final class User{
//		public static boolean firstAutoLogon = true;
//		public static String PASSWORD = null;
//		public static final String LOGON = "�û���¼��ע�����û�";
//		public static String CURRENT_USERNAME = null;
//		public static final String EXIT1 = "��ӭ�������װ���";
//		public static final String EXIT2 = "������˳���¼";
//	}
	public static final class RESULT_STATUS{
		public static String result_basic_success = "0000";
		public static String result_status_success = "0005";
		public static String result_login_success = "1009";
		public static String result_logon_success = "0015";
		public static String result_login_validate_success = "7007";
		
		public static int[] result_status_logon = {-1, -2, -3, -4, 0, 1, 2, 3};
		public static String[] result_str_logon = {"�����û���Ϊ��", "����Ϊ��", "�û������Ȳ��ԣ�3-15�ַ�������ռ���ַ���",
			"���볤�Ȳ��ԣ������ĸ��ַ���", "���벻��ȷ", "�û���¼��֤ͨ��", "�û���������", "��������������"};
	}
	
	/**
	 * ��̨�ӿڷ����������
	 * @return
	 */
	public static final int DIALOG_0BTN = 0;
	public static final int DIALOG_1BTN = 1;
	public static final int DIALOG_2BTN = 2;
	
	public static ImemberApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		instance = this;
		sqlite = new SQLite(this);
		initResultData();
		initBDlocation();
		setAutoLogon();
		getUsername();
//		setFirst();
		setCurrentCity();
		
		stores_old = new ArrayList<Store>();
		stores = new ArrayList<Store>();
		stores_other = new ArrayList<Store>();
		myCards = new ArrayList<MyCard>();
		storeCards = new ArrayList<StoreCard>();
		rewards = new ArrayList<Reward>();
		citys = new ArrayList<City>();
		sorts = new ArrayList<Sort>();
		
		logonSuccessManager = new LogonSuccessManager();
		loginSuccessManager = new LoginSuccessManager();
		finishActivityManager = new FinishActivityManager();
		
		
	}
	
	public LogonSuccessManager getLogonSuccessManager() {
		return logonSuccessManager;
	}
	
	public LoginSuccessManager getLoginSuccessManager(){
		return loginSuccessManager;
	}
	
	public FinishActivityManager getFinishActivityManager() {
		return finishActivityManager;
	}

//	public void setFinishActivityManager(FinishActivityManager finishActivityManager) {
//		this.finishActivityManager = finishActivityManager;
//	}
	
	public SQLite getSQLite(){
		return sqlite;
	}
	
	public int getIndex_footer(){
		return index_footer;
	}
	public void setIndex_footer(int index){
		index_footer = index;
	}
	
	private void initResultData(){
		result_status = new HashMap<String, String>();
		result_status.put("0000", "�����ɹ�");
		result_status.put("0001", "����ʧ��");
		result_status.put("0002", "��������");
		result_status.put("0003", "�Ƿ�����");
		result_status.put("0004", "�������Ѿ���ɾ���򲻴���,��ȷ�ϲ���");
		result_status.put("0005", "����(���м�¼������ɹ�)");
		result_status.put("0006", "��������");
		result_status.put("0007", "δ֪����");
		result_status.put("0008", "�û������ڻ���ɾ��");
		result_status.put("0009", "���ݿ����(�����ַ�����Ϣ)");
		result_status.put("0010", "���ݿ����(������ϢΪ����)");
		result_status.put("0011", "�˺�δ������,��ʱ���ܽ������²���");
		result_status.put("0012", "�û����벻��,��ȷ�����ĵ�½��Ϣ");
		result_status.put("0013", "ϵͳ�쳣");
		result_status.put("0014", "�˺��Ѿ�������,��ʱ���ܽ������²���.��������,���µ���ϵ�ͷ��� <021-58763686>");
		
		result_status.put("1001", "�û�������Ϊ��,��Ϊ3-20���ַ�.");
		result_status.put("1002", "���벻��Ϊ��,����6���ַ�,�ұ���Ϊ�������ַ����");
		result_status.put("1003", "�����ʽ����ȷ");
		result_status.put("1004", "�������Ѿ���ע���,����д��������");
		result_status.put("1005", "�ֻ������ʽ����,�ֻ�����Ϊ11λ.");
		result_status.put("1006", "���û����Ѿ�����,��������д");
		result_status.put("1007", "���ֻ������Ѿ�����,��������д");
		result_status.put("1008", "�û�ע��ʧ��");
		result_status.put("1009", "�û�ע��ɹ�");
		
		result_status.put("1011", "δ���ҵ��������û�����ȷ��Email�� ���µ���ϵ�ͷ��� <021-58763686>");
		result_status.put("1012", "���û��ѱ�ɾ��,������ע��");
		result_status.put("1013", "�������ݲ��ó���500���ַ�");
		result_status.put("1014", "���û�������,��ȷ�ϲ���");
		result_status.put("1015", "�û���½�ɹ�");
		result_status.put("1016", "�û����ֻ�ʧЧ,�����˻�����");
		result_status.put("1017", "��email�Ѿ�����,��������д");

		result_status.put("2001", "��Ա��������");
		result_status.put("2002", "��Ա���Ƿ���״̬,���ܲ鿴");
		result_status.put("2003", "�û�Ա������ӡ������,���ܽ������²���");
		result_status.put("2004", "��Ա��ӡ��������δ����,���ܽ������²���");
		result_status.put("2005", "���û����Ǵ˻�Ա���Ļ�Ա,���ܽ������²���");
		result_status.put("2006", "ÿ�����ӡ����������һ��ӡ��������,������û���ӵ�ӡ�����Ѿ��ﵽ����,���ܼ���Ϊ�����ӡ��.");
		result_status.put("2007", "ÿ�����ӡ����������һ��ӡ��������,������û���ӵ�ӡ�������Ϊnum(����������ӡ������)��,�����²���.");
		result_status.put("3001", "���̼��ѱ�ɾ���򲻴���,��ȷ�ϲ���");
		
		result_status.put("4001", "�û��Ѿ��Ǹû�Ա����Ա,�����ظ�����");
		result_status.put("4002", "���û��Ѿ���xxxx-xx-xx 00:00:00�ύ���û�Ա���Ļ�Ա����,�����ĵȺ��̼����");
		result_status.put("4003", "����ɹ�����Ϊ��Ա");
		result_status.put("4004", "�û�Ա���Ѿ�ֹͣ�����Ա");
		result_status.put("4005", "�ύ����ɹ�,�����ĵȴ��̼����");
		result_status.put("6001", "�ý��Ͳ����ڻ��Ѿ���ɾ��,��ȷ�ϲ���");
		result_status.put("6002", "�ý�������xxxx-xx-xx 00:00:00����");
		result_status.put("6003", "�ý����Ѿ�ʧЧ");
		result_status.put("6004", "�ý�������xxxx-xx-xx 00:00:00ʹ�ù���");
		
		result_status.put("7001", "��������ȷ���ֻ�����");
		result_status.put("7002", "���ϴη�����֤����ŵ�ʱ�䲻��120��,���Ժ�����");
		result_status.put("7003", "���ŷ���ʧ��,��120�������");
		result_status.put("7004", "��֤���Ѿ�ʧЧ,�����²���");
		result_status.put("7005", "�ֻ���������Ϣ����,��ȷ�ϲ���");
		result_status.put("7006", "��֤�����,��ȷ�ϲ���");
		result_status.put("7007", "��֤�ɹ�");
		result_status.put("7008", "��֤ʧ��,��120������·�����֤��");
		result_status.put("7009", "���ֻ���δע��ʹ�ù�,��ȷ�ϲ���");
		
	}
	
	public double getLat(){
		return lat;
	}
	public double getLon(){
		return lon;
	}
	public City getCurrentCity(){
		if(city_new != null){
			return city_new;
		}
		return city_current;
	}
	public void setNewCity(City city){
		this.city_new = city;
	}
//	public City getNewCity(){
//		return city_new;
//	}
	public void setCurrentCity(City city){
		city_current = city;
	}
	public boolean isChange_city(){
		if(city_new != null){
			if(city_current.getCity_id() != city_new.getCity_id()){
//				city_current = city_new;
				return true;
			}
		}
		return false;
	}
	private void setCurrentCity(){
		City city = sqlite.queryCityData();
		if(city != null){
			city_current = city;
		}
		else{
			city_current = new City();
			city_current.setCity_id(1);
//			city_current.setCity_id(17);
			city_current.setCity_name("ȫ��");
		}
	}
	private void setGpsCity(City city_gps){
		this.city_gps = city_gps;
	}
	public City getGpsCity(){
		return city_gps;
	}
	
	public boolean isFirst(){
		return isFirst;
	}
	
	public void setFirst(boolean b){
//		isFirst = sqlite.queryFirstLoad();
		isFirst = b;
	}
	
	public boolean isAutoLogon(){
		return isAutoLogon;
	}
	public void setAutoLogonToFalse(){
		isAutoLogon = false;
	}
	
	public void setAutoLogon(){
		user = sqlite.query_userinfo();
		if(user != null){
			isAutoLogon = true;
		}
		else{
			isAutoLogon = false;
		}
	}
	
	public String getUsername(){
		username = sqlite.query_username();
		return username;
	}
	
	public boolean isLogon(){
		return isLogon;
	}
	public void setLogon(boolean isLogon){
		this.isLogon = isLogon;
	}
	
	public boolean isScanSuccess(){
		return isScanSuccess;
	}
	public void setScanSuccess(boolean isScanSuccess){
		this.isScanSuccess = isScanSuccess;
	}
	public String getScanResult(){
		return scanResult;
	}
	public void setScanResult(String scanResult){
		this.scanResult = scanResult;
	}
	
	public User getUser(){
		return user;
	}
	public void setUserInfo(User user){
		this.user = user;
	}
	
	/**
	 * ��ȡip
	 */
	public String getIpAddress() {
        Enumeration<NetworkInterface> netInterfaces = null;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                System.out.println("DisplayName:" + ni.getDisplayName());
                System.out.println("Name:" + ni.getName());
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    InetAddress ip = ips.nextElement();
                    if (!ip.isLoopbackAddress()) {
                    	System.out.println("ip-------------------->" + ip.getHostAddress());
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

	 //��ȡ�汾��
    public String getVerName() {  
        String verName = null;  
        try {  
            verName = getPackageManager().getPackageInfo("com.yundong.imember", 0).versionName;  
        } catch (NameNotFoundException e) {
        	 e.printStackTrace();
        }  
        return verName;     
    }  
	
	/**
	 * �ַ���ת��
	 * @param value
	 * @param charset
	 * @return string
	 */
	public static String encode(String value, String charset) {
		String temp = null;
		try {
			temp = URLEncoder.encode(value, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return temp;
	}
	
	private void initBDlocation(){
		mLocationClient = new LocationClient(this);
		mLocationClient.registerLocationListener( myListener );
		setLocationOption();
		mLocationClient.start();
	}
	
	public LocationClient getLocationClient(){
		return mLocationClient;
	}
	
	public void startBDlocation(){
		fail_start = System.currentTimeMillis();
		mLocationClient.start();
	}
	
	private void setLocationOption(){
		LocationClientOption option = new LocationClientOption();		
		option.setOpenGps(false);				//��gps
		option.setCoorType("bd09ll");		//������������
		option.setPriority(LocationClientOption.NetWorkFirst);	//������������
		option.setProdName("locSDKDemo2");						//���ò�Ʒ������
		option.setScanSpan(1100);	//���ö�λģʽ��С��1����һ�ζ�λ;���ڵ���1����ʱ��λ
//		option.setScanSpan(5000);								//��ʱ��λ��ÿ��5���Ӷ�λһ�Ρ�
		mLocationClient.setLocOption(option);
	}
	
	/**
	 * ��������������λ�õ�ʱ�򣬸�ʽ�����ַ������������Ļ��
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null){
				fail_end = System.currentTimeMillis();
				long temp = (fail_end - fail_start)/1000;
				if(temp >= 30){
					mLocationClient.stop();
					mLocationClient.start();
				}
				return ;
			}
//			StringBuffer sb = new StringBuffer(256);
//			sb.append("time : ");
//			sb.append(location.getTime());
//			sb.append("\nerror code : ");
//			sb.append(location.getLocType());
//			sb.append("\nlatitude : ");
//			sb.append(location.getLatitude());
			
			lat = location.getLatitude();
			if(lat != 0.0 && lat != 4.9E-324){
//				StaticData.lat = lat;
			}
//			sb.append("\nlontitude : ");
//			sb.append(location.getLongitude());
			lon = location.getLongitude();
			if(lon != 0.0 && lon != 4.9E-324){
				City city = new City();
				city.setCity_name(location.getCity());
				setGpsCity(city);
				System.out.println("lat---->" + lat +",lon---->" + lon);
				mLocationClient.stop();
			}
//			sb.append("\nradius : ");
//			sb.append(location.getRadius());
//			System.out.println("�뾶--->" + location.getRadius());
			float radius = location.getRadius();
			if(radius != 0){
//				StaticData.RADIUS = location.getRadius();
			}
//			if (location.getLocType() == BDLocation.TypeGpsLocation){
//				sb.append("\nspeed : ");
//				sb.append(location.getSpeed());
//				sb.append("\nsatellite : ");
//				sb.append(location.getSatelliteNumber());
//			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
//				sb.append("\naddr : ");
//				sb.append(location.getAddrStr());
//			}
//			processSuccess(sb.toString());
			
			
//			if (location == null)
//				return ;
//			StringBuffer sb = new StringBuffer(256);
//			sb.append("time : ");
//			sb.append(location.getTime());
//			sb.append("\nerror code : ");
//			sb.append(location.getLocType());
//			sb.append("\nlatitude : ");
//			sb.append(location.getLatitude());
//			sb.append("\nlontitude : ");
//			sb.append(location.getLongitude());
//			sb.append("\nradius : ");
//			sb.append(location.getRadius());
//			if (location.getLocType() == BDLocation.TypeGpsLocation){
//				sb.append("\nspeed : ");
//				sb.append(location.getSpeed());
//				sb.append("\nsatellite : ");
//				sb.append(location.getSatelliteNumber());
//			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
//				sb.append("\naddr : ");
//				sb.append(location.getAddrStr());
//			}
//			sb.append("\nsdk version : ");
//			sb.append(mLocationClient.getVersion());
		}

		public void onReceivePoi(BDLocation location){
        	//return ;
        }
	}
	
	public boolean isLetter_Num(String strEmail){
		String strPattern = "[^a-zA-Z0-9]";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}
	
	//�����ַ�
			public Bundle isSpecial(String strEmail){
				Bundle bundle = new Bundle();
				String strPattern = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~��@#��%����&*��������+|{}������������������������^\u4e00-\u9fa5]";     
				Pattern p = Pattern.compile(strPattern);
				Matcher m = p.matcher(strEmail);
				
				boolean has = m.find();
				bundle.putBoolean("has", has);
				if(has){
					CharSequence  value = m.group(0);
					bundle.putCharSequence("character", value);
				}
				return bundle;
			}
			
			public Bundle isSpecial_psd(String strEmail){
				Bundle bundle = new Bundle();
				String strPattern = "[%&]";
				Pattern p = Pattern.compile(strPattern);
				Matcher m = p.matcher(strEmail);
				
				boolean has = m.find();
				bundle.putBoolean("has", has);
				if(has){
					CharSequence  value = m.group(0);
					bundle.putCharSequence("character", value);
				}
				return bundle;
			}
	
	//�Ƿ��������ʽ
		public boolean isEmail(String strEmail){
			String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";     
			Pattern p = Pattern.compile(strPattern);
			Matcher m = p.matcher(strEmail);
			return m.matches();
		}
		
		//����
		public boolean isName(String str){
			String strPattern = "^[\u4e00-\u9fa5]{2,}$";
			Pattern p = Pattern.compile(strPattern);
			Matcher m = p.matcher(str);
			return m.matches();
		}
		
		//�ֻ���
		public boolean isMobile(String str){
			String strPattern = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
			Pattern p = Pattern.compile(strPattern);
			Matcher m = p.matcher(str);
			return m.matches();
		}
		
		//�绰
		public boolean isTel(String str){
			String strPattern = "^(\\(\\d{3,4}\\)|\\d{3,4}-)?\\d{7,8}$";
			Pattern p = Pattern.compile(strPattern);
			Matcher m = p.matcher(str);
			return m.matches();
		}
		
		//QQ��
		public boolean isQQ(String str){
			String strPattern = "[1-9][0-9]{4,}";
			Pattern p = Pattern.compile(strPattern);
			Matcher m = p.matcher(str);
			return m.matches();
		}
		
		//���ֻ�Ӣ��
		public boolean isEngOrNum(String str){
			String strPattern = "/[^a-zA-Z0-9]/g";
			Pattern p = Pattern.compile(strPattern);
			Matcher m = p.matcher(str);
			return m.matches();
		}
		
		//ȫӢ��
		public boolean isCharacter(String str){
			String strPattern = "^[A-Za-z]+$";
			Pattern p = Pattern.compile(strPattern);
			Matcher m = p.matcher(str);
			return m.matches();
		}
		
		//����
				public boolean isNum(String str){
					String strPattern = "^[0-9]*$";
					Pattern p = Pattern.compile(strPattern);
					Matcher m = p.matcher(str);
					return m.matches();
				}
		
		//�Ƿ��пո�
		public boolean isSpace(String str){
			if(str.indexOf( " ") != -1)
				return true;
			return false;
		}
		
		//Drawable �� Bitmap
		public static Bitmap drawableToBitmap(Drawable drawable) { 
			Bitmap bitmap = Bitmap 
			.createBitmap( 
			drawable.getIntrinsicWidth(),
			drawable.getIntrinsicHeight(), 
			drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 
			: Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap); 
			//canvas.setBitmap(bitmap); 
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			drawable.draw(canvas); 
			return bitmap; 
		}
		
		//Bitmap �� byte[]
		public static byte[] Bitmap2Bytes(Bitmap bm){
			ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
			bm.compress(Bitmap.CompressFormat.PNG, 100, baos); 
			return baos.toByteArray(); 
		}
}
