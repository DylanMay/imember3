package com.yundong.imember.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yundong.imember.entity.City;
import com.yundong.imember.entity.User;

/**
 * 数据库类
 * 
 * @author Administrator
 * 
 */
public class SQLite {
	private DatabaseHelper databaseHelper;
	public static final String TABLE_NAME_FIRSTLOAD = "firstload";
	public static final String TABLE_NAME_USER = "userInfo";
	public static final String TABLE_USERNAME = "username";
	public static final String TABLE_NAME_LOCATION = "locationInfo";
	public static final String TABLE_NAME_CITY = "city";
	private String sql;

	public SQLite(Context context) {
		// 传入此类对象和数据库名字和版本得到databaseHelper对象
		databaseHelper = DatabaseHelper.getInstance(context);
	}

	public boolean queryFirstLoad() {
		Cursor cursor = null;
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		sql = "select * from " + TABLE_NAME_FIRSTLOAD;
		
		try{
			cursor = db.rawQuery(sql, null);
			if(cursor.getCount() > 0){
				return false;
			}
			return true;
		}catch(SQLException e){
			Log.i("SQlite_erro", "查询是否是第一次启动失败" + e.toString());
			e.printStackTrace();
			return true;
		}finally{
			if(cursor != null){
				cursor.close();
			}
			if(db != null){
				db.close();
			}
		}
	}
	
	public boolean insertFirstLoad() {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		sql = "insert into " + TABLE_NAME_FIRSTLOAD + "(load)values(" + 1 + ")";
		
		try{
				db.execSQL(sql);
				return true;
		}catch(SQLException e){
			Log.i("SQlite_erro", "插入是否是第一次启动失败" + e.toString());
			e.printStackTrace();
			return false;
		}finally{
			if(db != null){
				db.close();
			}
		}
	}
	
	public boolean queryCity() {
		Cursor cursor = null;
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		sql = "select * from " + TABLE_NAME_CITY;
		
		try{
			cursor = db.rawQuery(sql, null);
			if(cursor.getCount() > 0){
				return true;
			}
			return false;
		}catch(SQLException e){
			Log.i("SQlite_erro", "查询城市数据失败" + e.toString());
			e.printStackTrace();
			return false;
		}finally{
			if(cursor != null){
				cursor.close();
			}
			if(db != null){
				db.close();
			}
		}
	}
	
	public City queryCityData() {
		Cursor cursor = null;
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		sql = "select * from " + TABLE_NAME_CITY;
		
		try{
			cursor = db.rawQuery(sql, null);
			City city = null;
			while(cursor.moveToNext()){
				city = new City();
				city.setCity_name(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCity_id(cursor.getInt(cursor.getColumnIndex("city_id")));
			}
			return city;
		}catch(SQLException e){
			Log.i("SQlite_erro", "查询城市数据失败" + e.toString());
			e.printStackTrace();
			return null;
		}finally{
			if(cursor != null){
				cursor.close();
			}
			if(db != null){
				db.close();
			}
		}
	}
	
	public boolean insertCity(City city) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		sql = "insert into " + TABLE_NAME_CITY + "(city_id,city_name)values(" + city.getCity_id()
				+ ",'" + city.getCity_name() + "')";
		
		try{
				db.execSQL(sql);
				return true;
		}catch(SQLException e){
			Log.i("SQlite_erro", "插入城市数据失败" + e.toString());
			e.printStackTrace();
			return false;
		}finally{
			if(db != null){
				db.close();
			}
		}
	}
	
	public boolean deleteCity(){
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		
		sql = "delete from " + TABLE_NAME_CITY;
		
		try{
				db.execSQL(sql);
				return true;
		}catch(SQLException e){
			Log.i("SQlite_erro", "删除城市数据失败" + e.toString());
			e.printStackTrace();
			return false;
		}finally{
			if(db != null){
				db.close();
			}
		}
	}
	
//	public void updateCity(City city) {
//		SQLiteDatabase db = databaseHelper.getWritableDatabase();
//		sql = "update " + TABLE_NAME_CITY + " set city_id=" + city.getCity_id() + ",city_name="
//			+ store.getTotalYhNum() + " where storeId='" + store.getStoreId() + "'";
//		
//		try{
//				db.execSQL(sql);
//		}catch(SQLException e){
//			Log.i("SQlite_erro", "更新计次数据失败" + e.toString());
//			e.printStackTrace();
//		}finally{
//			if(db != null){
//				db.close();
//			}
//		}
//	}
	
	public User query_userinfo(){
		Cursor cursor = null;
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		sql = "select * from " + TABLE_NAME_USER;
		
		try{
			cursor = db.rawQuery(sql, null);
			if(cursor != null){
				if(cursor.getCount() > 0){
					User user = new User();
						//提取数据
						while(cursor.moveToNext()){
							user.setUser_name(cursor.getString(cursor.getColumnIndex("username")));
							user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
							user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
							user.setUser_id(cursor.getInt(cursor.getColumnIndex("user_id")));
						}
						return user;
				}
			}
			return null;
		}catch(SQLException e){
			Log.i("SQlite_erro", "查询用户数据失败" + e.toString());
			e.printStackTrace();
			return null;
		}finally{
			if(cursor != null){
				cursor.close();
			}
			if(db != null){
				db.close();
			}
		}
	}
	
	public String query_username(){
		Cursor cursor = null;
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		sql = "select * from " + TABLE_USERNAME;
		
		try{
			cursor = db.rawQuery(sql, null);
			if(cursor != null){
				if(cursor.getCount() > 0){
					String username = null;
						//提取数据
						while(cursor.moveToNext()){
							username = cursor.getString(cursor.getColumnIndex("username"));
						}
						return username;
				}
			}
			return null;
		}catch(SQLException e){
			Log.i("SQlite_erro", "查询用户名失败" + e.toString());
			e.printStackTrace();
			return null;
		}finally{
			if(cursor != null){
				cursor.close();
			}
			if(db != null){
				db.close();
			}
		}
	}
	
	public boolean delete_userinfo(){
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		
		sql = "delete from " + TABLE_NAME_USER;
		
		try{
				db.execSQL(sql);
				return true;
		}catch(SQLException e){
			Log.i("SQlite_erro", "删除用户数据失败" + e.toString());
			e.printStackTrace();
			return false;
		}finally{
			if(db != null){
				db.close();
			}
		}
	}
	
	public boolean delete_username(){
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		
		sql = "delete from " + TABLE_USERNAME;
		
		try{
				db.execSQL(sql);
				return true;
		}catch(SQLException e){
			Log.i("SQlite_erro", "删除用户名失败" + e.toString());
			e.printStackTrace();
			return false;
		}finally{
			if(db != null){
				db.close();
			}
		}
	}

	public boolean insert_userinfo(User user) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		sql = "insert into " + TABLE_NAME_USER + "(username,password,email,user_id)values('" + 
				user.getUser_name() + "','" + user.getPassword() + "','" + user.getEmail() +"'," + user.getUser_id() + ")";
		
		try{
				db.execSQL(sql);
				return true;
		}catch(SQLException e){
			System.out.println("插入用户错误---->" + e.toString());
			e.printStackTrace();
			return false;
		}finally{
			if(db != null){
				db.close();
			}
		}
	}
	
	public boolean insert_username(String username) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		sql = "insert into " + TABLE_USERNAME + "(username)values('" + username + "')";
		
		try{
				db.execSQL(sql);
				return true;
		}catch(SQLException e){
			System.out.println("插入用户名错误---->" + e.toString());
			e.printStackTrace();
			return false;
		}finally{
			if(db != null){
				db.close();
			}
		}
	}

	// 删除重复项
	public void deleteMapRepeat() {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		try {
			db.execSQL("delete from addr where _id not in (select max(_id) from addr group by address)");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
}