package com.yundong.imember.entity;

import java.io.Serializable;

public class StoreCard implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -727765702955477338L;
	private String card_title;
	private String card_name;
	private String card_logo_url;
	private String c_description;
	private String c_seacription;
	private String c_servies_terms;
	private String card_effectivedate;
	private int mcard_id;
	private int c_status;
	private int s_status;
	public int getS_status() {
		return s_status;
	}
	public void setS_status(int s_status) {
		this.s_status = s_status;
	}
	public int getS_r_status() {
		return s_r_status;
	}
	public void setS_r_status(int s_r_status) {
		this.s_r_status = s_r_status;
	}
	private int s_r_status;
	private int stamp_id;
	public int getStamp_id() {
		return stamp_id;
	}
	public void setStamp_id(int stamp_id) {
		this.stamp_id = stamp_id;
	}
	private String s_name;
	private String s_description;
	private String s_r_name;
	private String s_r_description;
	
	public String getS_name() {
		return s_name;
	}
	public void setS_name(String s_name) {
		this.s_name = s_name;
	}
	public String getS_description() {
		return s_description;
	}
	public void setS_description(String s_description) {
		this.s_description = s_description;
	}
	public String getC_seacription() {
		return c_seacription;
	}
	public void setC_seacription(String c_seacription) {
		this.c_seacription = c_seacription;
	}
	public String getS_r_name() {
		return s_r_name;
	}
	public void setS_r_name(String s_r_name) {
		this.s_r_name = s_r_name;
	}
	public String getS_r_description() {
		return s_r_description;
	}
	public void setS_r_description(String s_r_description) {
		this.s_r_description = s_r_description;
	}
	public String getC_description() {
		return c_description;
	}
	public void setC_description(String c_description) {
		this.c_description = c_description;
	}
	public String getC_servies_terms() {
		return c_servies_terms;
	}
	public void setC_servies_terms(String c_servies_terms) {
		this.c_servies_terms = c_servies_terms;
	}
	public int getC_status() {
		return c_status;
	}
	public void setC_status(int c_status) {
		this.c_status = c_status;
	}
	public String getCard_title() {
		return card_title;
	}
	public void setCard_title(String card_title) {
		this.card_title = card_title;
	}
	public String getCard_name() {
		return card_name;
	}
	public void setCard_name(String card_name) {
		this.card_name = card_name;
	}
	public String getCard_logo_url() {
		return card_logo_url;
	}
	public void setCard_logo_url(String card_logo_url) {
		this.card_logo_url = card_logo_url;
	}
	public String getCard_effectivedate() {
		return card_effectivedate;
	}
	public void setCard_effectivedate(String card_effectivedate) {
		this.card_effectivedate = card_effectivedate;
	}
	public int getMcard_id() {
		return mcard_id;
	}
	public void setMcard_id(int mcard_id) {
		this.mcard_id = mcard_id;
	}
}
