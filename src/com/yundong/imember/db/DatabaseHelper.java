package com.yundong.imember.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
	public static final String DATABASE_NAME = "store";
	//数据库版本
	public static final int DATABASE_VERSION = 4;
	public static DatabaseHelper instance = null;
	private String sql;
	
	public static DatabaseHelper getInstance(Context context){
		if(instance == null){
			instance = new DatabaseHelper(context);
		}
		return instance;
	}
	
	private DatabaseHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	//创建数据表,在调用getWritableDatabase或的时候调用
	//用于初次使用软件时生成数据库表
	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("调用了onCreate");
		
		sql = "create table if not exists " + SQLite.TABLE_NAME_FIRSTLOAD
				+ "(_id integer primary key autoincrement,"
				+ "load integer(4))";
				db.execSQL(sql);
		
		sql = "create table if not exists " + SQLite.TABLE_NAME_USER
				+ "(_id integer primary key autoincrement,"
				+ "username VARCHAR(60),"
				+ "password VARCHAR(60),"
				+ "email VARCHAR(60),"
				+ "user_id integer(16))";
		db.execSQL(sql);
		
		sql = "create table if not exists " + SQLite.TABLE_USERNAME
				+ "(_id integer primary key autoincrement,"
				+ "username VARCHAR(60))";
		db.execSQL(sql);
		
		sql = "create table if not exists " + SQLite.TABLE_NAME_LOCATION
		+ "(_id integer primary key autoincrement,"
		+ "lat VARCHAR(50),"
		+ "lon VARCHAR(50))";
		db.execSQL(sql);
		
		sql = "create table if not exists " + SQLite.TABLE_NAME_CITY
				+ "(_id integer primary key autoincrement,"
				+ "city_id integer(50),"
				+ "city_name VARCHAR(50))";
				db.execSQL(sql);
	}

	//当VERSION发生变化就会调用以下方法
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("调用了onUpgrade");
		try{
			sql = "drop table if exists " + SQLite.TABLE_NAME_USER;
			db.execSQL(sql);
			sql = "drop table if exists " + SQLite.TABLE_USERNAME;
			db.execSQL(sql);
			sql = "drop table if exists " + SQLite.TABLE_NAME_LOCATION;
			db.execSQL(sql);
			
			onCreate(db);
		}
		catch(SQLiteException e){
			System.out.println("更新表出错" + e.toString());
		}finally{
//			if(db != null){
//				db.close();
//			}
		}
	}
}
