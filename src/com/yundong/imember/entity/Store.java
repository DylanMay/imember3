package com.yundong.imember.entity;

import java.io.Serializable;

public class Store implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1101790526788883330L;
	private int store_account_id;
	public int getStore_account_id() {
		return store_account_id;
	}
	public void setStore_account_id(int store_account_id) {
		this.store_account_id = store_account_id;
	}
	private String store_title;
	private double xpoint;
	private double ypoint;
	private String store_addr;
	private String store_tel;
	private String cate_name;
	private String city_name;
	private String store_logo_url;
	private int cate_type_id;
	
	public int getCate_type_id() {
		return cate_type_id;
	}
	public void setCate_type_id(int cate_type_id) {
		this.cate_type_id = cate_type_id;
	}
	public String getStore_title() {
		return store_title;
	}
	public void setStore_title(String store_title) {
		this.store_title = store_title;
	}
	public double getXpoint() {
		return xpoint;
	}
	public void setXpoint(double xpoint) {
		this.xpoint = xpoint;
	}
	public double getYpoint() {
		return ypoint;
	}
	public void setYpoint(double ypoint) {
		this.ypoint = ypoint;
	}
	public String getStore_addr() {
		return store_addr;
	}
	public void setStore_addr(String store_addr) {
		this.store_addr = store_addr;
	}
	public String getStore_tel() {
		return store_tel;
	}
	public void setStore_tel(String store_tel) {
		this.store_tel = store_tel;
	}
	public String getCate_name() {
		return cate_name;
	}
	public void setCate_name(String cate_name) {
		this.cate_name = cate_name;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public String getStore_logo_url() {
		return store_logo_url;
	}
	public void setStore_logo_url(String store_logo_url) {
		this.store_logo_url = store_logo_url;
	}
	
}
