package com.yundong.imember.entity;

import java.io.Serializable;

public class Reward implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4922789119909941325L;
	private int stamp_id;
	public int getStamp_id() {
		return stamp_id;
	}
	public void setStamp_id(int stamp_id) {
		this.stamp_id = stamp_id;
	}
	private int u_reward_id;
	private int reward_id;
	private int mcard_id;
	private String logo_url;
	private String h_name;
	private String reward_content;
	private int stamp_now;
	private int reward_now;
	private String card_name;
	private String reward_name;
	private String reward_description;
	private String r_apply_condition;
	private String attention;
	private int status;
	private int c_status;
	private int m_status;
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
	public int getM_status() {
		return m_status;
	}
	public void setM_status(int m_status) {
		this.m_status = m_status;
	}
	private String reward_effective_date;
	private String r_redeemed_time;
	public String getR_redeemed_time() {
		return r_redeemed_time;
	}
	public void setR_redeemed_time(String r_redeemed_time) {
		this.r_redeemed_time = r_redeemed_time;
	}
	private String s_description;
	private String s_r_description;
	public String getS_r_description() {
		return s_r_description;
	}
	public void setS_r_description(String s_r_description) {
		this.s_r_description = s_r_description;
	}
	private int s_round;
	private String s_name;
	private String r_code;
	
	public String getR_code() {
		return r_code;
	}
	public void setR_code(String r_code) {
		this.r_code = r_code;
	}
	public int getMcard_id() {
		return mcard_id;
	}
	public void setMcard_id(int mcard_id) {
		this.mcard_id = mcard_id;
	}
	public String getS_name() {
		return s_name;
	}
	public void setS_name(String s_name) {
		this.s_name = s_name;
	}
	public int getS_round() {
		return s_round;
	}
	public void setS_round(int s_round) {
		this.s_round = s_round;
	}
	public String getS_description() {
		return s_description;
	}
	public void setS_description(String s_description) {
		this.s_description = s_description;
	}
	public int getU_reward_id() {
		return u_reward_id;
	}
	public void setU_reward_id(int u_reward_id) {
		this.u_reward_id = u_reward_id;
	}
	public int getC_status() {
		return c_status;
	}
	public void setC_status(int c_status) {
		this.c_status = c_status;
	}
	public String getReward_name() {
		return reward_name;
	}
	public void setReward_name(String reward_name) {
		this.reward_name = reward_name;
	}
	public String getReward_description() {
		return reward_description;
	}
	public void setReward_description(String reward_description) {
		this.reward_description = reward_description;
	}
	public String getR_apply_condition() {
		return r_apply_condition;
	}
	public void setR_apply_condition(String r_apply_condition) {
		this.r_apply_condition = r_apply_condition;
	}
	public String getH_name() {
		return h_name;
	}
	public void setH_name(String h_name) {
		this.h_name = h_name;
	}
	public String getAttention() {
		return attention;
	}
	public void setAttention(String attention) {
		this.attention = attention;
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
	public int getReward_id() {
		return reward_id;
	}
	public void setReward_id(int reward_id) {
		this.reward_id = reward_id;
	}
	public String getLogo_url() {
		return logo_url;
	}
	public void setLogo_url(String logo_url) {
		this.logo_url = logo_url;
	}
	public String getReward_content() {
		return reward_content;
	}
	public void setReward_content(String reward_content) {
		this.reward_content = reward_content;
	}
	public String getCard_name() {
		return card_name;
	}
	public void setCard_name(String card_name) {
		this.card_name = card_name;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getReward_effective_date() {
		return reward_effective_date;
	}
	public void setReward_effective_date(String reward_effective_date) {
		this.reward_effective_date = reward_effective_date;
	}
}
