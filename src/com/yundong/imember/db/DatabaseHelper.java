package com.yundong.imember.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
	public static final String DATABASE_NAME = "store";
	//���ݿ�汾
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

	//�������ݱ�,�ڵ���getWritableDatabase���ʱ�����
	//���ڳ���ʹ�����ʱ�������ݿ��
	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("������onCreate");
		
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

	//��VERSION�����仯�ͻ�������·���
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("������onUpgrade");
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
			System.out.println("���±����" + e.toString());
		}finally{
//			if(db != null){
//				db.close();
//			}
		}
	}
}
