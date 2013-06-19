package com.yundong.imember.entity;

import java.io.Serializable;

public class MyCard implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3617846296277107898L;
	private String card_title;
	private String card_name;
	private String card_logo_url;
	private int stamp_now;
	private String c_seacription;
	private String c_servies_terms;
	private int account_id;
	private String s_name;
	public String getS_name() {
		return s_name;
	}
	public void setS_name(String s_name) {
		this.s_name = s_name;
	}
	
	public int getAccount_id() {
		return account_id;
	}
	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}
	public String getC_servies_terms() {
		return c_servies_terms;
	}
	public void setC_servies_terms(String c_servies_terms) {
		this.c_servies_terms = c_servies_terms;
	}
	public String getC_seacription() {
		return c_seacription;
	}
	public void setC_seacription(String c_seacription) {
		this.c_seacription = c_seacription;
	}
	public int getStamp_now() {
		return stamp_now;
	}
	public void setStamp_now(int stamp_now) {
		this.stamp_now = stamp_now;
	}
	public int getReward_now() {
		return reward_now;
	}
	public void setReward_now(int reward_now) {
		this.reward_now = reward_now;
	}
	public String getM_number() {
		return m_number;
	}
	public void setM_number(String m_number) {
		this.m_number = m_number;
	}
	private int reward_now;
	private String m_number;
	private int mcard_id;
	public int getMcard_id() {
		return mcard_id;
	}
	public void setMcard_id(int mcard_id) {
		this.mcard_id = mcard_id;
	}
	private String card_effectivedate;
	
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
}
