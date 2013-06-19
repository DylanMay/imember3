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
	 * 百度定位相关变量
	 */
	public LocationClient mLocationClient = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	private long fail_start;
	private long fail_end;
	private Double lat = 0.0;
	private Double lon = 0.0;
	
	/**
	 * 后台所有接口
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
	 * 登录注册相关
	 * @return
	 */
//	public static final class User{
//		public static boolean firstAutoLogon = true;
//		public static String PASSWORD = null;
//		public static final String LOGON = "用户登录或注册新用户";
//		public static String CURRENT_USERNAME = null;
//		public static final String EXIT1 = "欢迎回来，亲爱的";
//		public static final String EXIT2 = "，点击退出登录";
//	}
	public static final class RESULT_STATUS{
		public static String result_basic_success = "0000";
		public static String result_status_success = "0005";
		public static String result_login_success = "1009";
		public static String result_logon_success = "0015";
		public static String result_login_validate_success = "7007";
		
		public static int[] result_status_logon = {-1, -2, -3, -4, 0, 1, 2, 3};
		public static String[] result_str_logon = {"传入用户名为空", "密码为空", "用户名长度不对（3-15字符，汉字占两字符）",
			"密码长度不对（至少四个字符）", "密码不正确", "用户登录验证通过", "用户名不存在", "其他意外错误情况"};
	}
	
	/**
	 * 后台接口返回数据相关
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
		result_status.put("0000", "操作成功");
		result_status.put("0001", "操作失败");
		result_status.put("0002", "参数错误");
		result_status.put("0003", "非法操作");
		result_status.put("0004", "该数据已经被删除或不存在,请确认操作");
		result_status.put("0005", "正常(即有记录或操作成功)");
		result_status.put("0006", "暂无数据");
		result_status.put("0007", "未知请求");
		result_status.put("0008", "用户不存在或者删除");
		result_status.put("0009", "数据库错误(单条字符串信息)");
		result_status.put("0010", "数据库错误(错误信息为数组)");
		result_status.put("0011", "账号未被激活,暂时不能进行如下操作");
		result_status.put("0012", "用户密码不对,请确认您的登陆信息");
		result_status.put("0013", "系统异常");
		result_status.put("0014", "账号已经被锁定,暂时不能进行如下操作.若有疑问,请致电联系客服： <021-58763686>");
		
		result_status.put("1001", "用户名不能为空,且为3-20个字符.");
		result_status.put("1002", "密码不能为空,至少6个字符,且必须为数字与字符组合");
		result_status.put("1003", "邮箱格式不正确");
		result_status.put("1004", "该邮箱已经被注册过,请填写其他邮箱");
		result_status.put("1005", "手机号码格式错误,手机号码为11位.");
		result_status.put("1006", "该用户名已经存在,请重新填写");
		result_status.put("1007", "该手机号码已经存在,请重新填写");
		result_status.put("1008", "用户注册失败");
		result_status.put("1009", "用户注册成功");
		
		result_status.put("1011", "未能找到该邮箱用户，请确认Email， 或致电联系客服： <021-58763686>");
		result_status.put("1012", "该用户已被删除,请重新注册");
		result_status.put("1013", "反馈内容不得超过500个字符");
		result_status.put("1014", "该用户不存在,请确认操作");
		result_status.put("1015", "用户登陆成功");
		result_status.put("1016", "用户绑定手机失效,导致账户锁定");
		result_status.put("1017", "该email已经存在,请重新填写");

		result_status.put("2001", "会员卡不存在");
		result_status.put("2002", "会员卡非发布状态,不能查看");
		result_status.put("2003", "该会员卡暂无印花功能,不能进行如下操作");
		result_status.put("2004", "会员卡印花功能尚未开启,不能进行如下操作");
		result_status.put("2005", "该用户不是此会员卡的会员,不能进行如下操作");
		result_status.put("2006", "每天添加印花的上限是一个印花满足数,今天该用户添加的印花数已经达到上限,不能继续为其添加印花.");
		result_status.put("2007", "每天添加印花的上限是一个印花满足数,今天该用户添加的印花数最多为num(当天最多添加印花数量)个,请重新操作.");
		result_status.put("3001", "该商家已被删除或不存在,请确认操作");
		
		result_status.put("4001", "用户已经是该会员卡会员,不能重复申请");
		result_status.put("4002", "该用户已经于xxxx-xx-xx 00:00:00提交过该会员卡的会员申请,请耐心等候商家审核");
		result_status.put("4003", "申请成功并成为会员");
		result_status.put("4004", "该会员卡已经停止申请会员");
		result_status.put("4005", "提交申请成功,请耐心等待商家审核");
		result_status.put("6001", "该奖赏不存在或已经被删除,请确认操作");
		result_status.put("6002", "该奖赏已于xxxx-xx-xx 00:00:00过期");
		result_status.put("6003", "该奖赏已经失效");
		result_status.put("6004", "该奖赏已于xxxx-xx-xx 00:00:00使用过了");
		
		result_status.put("7001", "请输入正确的手机号码");
		result_status.put("7002", "距上次发送验证码短信的时间不到120秒,请稍后再试");
		result_status.put("7003", "短信发送失败,请120秒后再试");
		result_status.put("7004", "验证码已经失效,请重新操作");
		result_status.put("7005", "手机号码与信息不符,请确认操作");
		result_status.put("7006", "验证码错误,请确认操作");
		result_status.put("7007", "验证成功");
		result_status.put("7008", "验证失败,请120秒后重新发送验证码");
		result_status.put("7009", "该手机号未注册使用过,请确认操作");
		
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
			city_current.setCity_name("全国");
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
	 * 获取ip
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

	 //获取版本名
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
	 * 字符串转码
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
		option.setOpenGps(false);				//打开gps
		option.setCoorType("bd09ll");		//设置坐标类型
		option.setPriority(LocationClientOption.NetWorkFirst);	//设置网络优先
		option.setProdName("locSDKDemo2");						//设置产品线名称
		option.setScanSpan(1100);	//设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
//		option.setScanSpan(5000);								//定时定位，每隔5秒钟定位一次。
		mLocationClient.setLocOption(option);
	}
	
	/**
	 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
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
//			System.out.println("半径--->" + location.getRadius());
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
	
	//特殊字符
			public Bundle isSpecial(String strEmail){
				Bundle bundle = new Bundle();
				String strPattern = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？^\u4e00-\u9fa5]";     
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
	
	//是否是邮箱格式
		public boolean isEmail(String strEmail){
			String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";     
			Pattern p = Pattern.compile(strPattern);
			Matcher m = p.matcher(strEmail);
			return m.matches();
		}
		
		//姓名
		public boolean isName(String str){
			String strPattern = "^[\u4e00-\u9fa5]{2,}$";
			Pattern p = Pattern.compile(strPattern);
			Matcher m = p.matcher(str);
			return m.matches();
		}
		
		//手机号
		public boolean isMobile(String str){
			String strPattern = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
			Pattern p = Pattern.compile(strPattern);
			Matcher m = p.matcher(str);
			return m.matches();
		}
		
		//电话
		public boolean isTel(String str){
			String strPattern = "^(\\(\\d{3,4}\\)|\\d{3,4}-)?\\d{7,8}$";
			Pattern p = Pattern.compile(strPattern);
			Matcher m = p.matcher(str);
			return m.matches();
		}
		
		//QQ号
		public boolean isQQ(String str){
			String strPattern = "[1-9][0-9]{4,}";
			Pattern p = Pattern.compile(strPattern);
			Matcher m = p.matcher(str);
			return m.matches();
		}
		
		//数字或英文
		public boolean isEngOrNum(String str){
			String strPattern = "/[^a-zA-Z0-9]/g";
			Pattern p = Pattern.compile(strPattern);
			Matcher m = p.matcher(str);
			return m.matches();
		}
		
		//全英文
		public boolean isCharacter(String str){
			String strPattern = "^[A-Za-z]+$";
			Pattern p = Pattern.compile(strPattern);
			Matcher m = p.matcher(str);
			return m.matches();
		}
		
		//数字
				public boolean isNum(String str){
					String strPattern = "^[0-9]*$";
					Pattern p = Pattern.compile(strPattern);
					Matcher m = p.matcher(str);
					return m.matches();
				}
		
		//是否有空格
		public boolean isSpace(String str){
			if(str.indexOf( " ") != -1)
				return true;
			return false;
		}
		
		//Drawable → Bitmap
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
		
		//Bitmap → byte[]
		public static byte[] Bitmap2Bytes(Bitmap bm){
			ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
			bm.compress(Bitmap.CompressFormat.PNG, 100, baos); 
			return baos.toByteArray(); 
		}
}
