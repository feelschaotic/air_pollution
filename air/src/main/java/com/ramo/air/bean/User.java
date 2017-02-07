package com.ramo.air.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class User implements Serializable{
	private String user_id;
	private String user_name;
	private String user_real_name;
	private String user_pass;
	private String user_id_card;
	private Level level;
	private int user_state;
	private Set<Report> attention=new HashSet<Report>();
	private Set<Report> myreport=new HashSet<Report>();
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public String getUser_real_name() {
		return user_real_name;
	}
	public void setUser_real_name(String user_real_name) {
		this.user_real_name = user_real_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_pass() {
		return user_pass;
	}
	public void setUser_pass(String user_pass) {
		this.user_pass = user_pass;
	}
	public String getUser_id_card() {
		return user_id_card;
	}
	public void setUser_id_card(String user_id_card) {
		this.user_id_card = user_id_card;
	}
	
	public Level getLevel() {
		return level;
	}
	public void setLevel(Level level) {
		this.level = level;
	}
	public int getUser_state() {
		return user_state;
	}
	public void setUser_state(int user_state) {
		this.user_state = user_state;
	}
	public Set<Report> getAttention() {
		return attention;
	}
	public void setAttention(Set<Report> attention) {
		this.attention = attention;
	}
	public Set<Report> getMyreport() {
		return myreport;
	}
	public void setMyreport(Set<Report> myreport) {
		this.myreport = myreport;
	}
	
}
