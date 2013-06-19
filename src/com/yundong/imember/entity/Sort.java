package com.yundong.imember.entity;

import java.io.Serializable;

public class Sort implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2702208458115051914L;
	private int sort_id;
	private String sort_name;
	private String url_icon;
	
	public String getUrl_icon() {
		return url_icon;
	}
	public void setUrl_icon(String url_icon) {
		this.url_icon = url_icon;
	}
	public int getSort_id() {
		return sort_id;
	}
	public void setSort_id(int sort_id) {
		this.sort_id = sort_id;
	}
	public String getSort_name() {
		return sort_name;
	}
	public void setSort_name(String sort_name) {
		this.sort_name = sort_name;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
