package com.ramo.air.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Report implements Serializable{
	private String report_id;
	private User user;
	private Admin admin;
	private String report_content;
	private String report_location;
	private String report_img;
	private ReportType type;
	private Company company;
	private String result;
	private int state;
	private String stateStr;
	private String accept_time;
	private String deal_time;
	private String report_time;
	private Set<User> attention=new HashSet<User>();
	private String type_id;
	private String user_id;
	private String admin_id;
	private String company_id;
	public int getAttentionSize(){
		return attention.size();
	}
	public String getReport_id() {
		return report_id;
	}
	public void setReport_id(String report_id) {
		this.report_id = report_id;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Admin getAdmin() {
		return admin;
	}
	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
	public String getReport_content() {
		return report_content;
	}
	public void setReport_content(String report_content) {
		this.report_content = report_content;
	}
	public String getReport_location() {
		return report_location;
	}
	public void setReport_location(String report_location) {
		this.report_location = report_location;
	}
	public String getReport_img() {
		return report_img;
	}
	public void setReport_img(String report_img) {
		this.report_img = report_img;
	}
	
	public ReportType getType() {
		return type;
	}
	public void setType(ReportType type) {
		this.type = type;
	}
	
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getAccept_time() {
		return accept_time;
	}
	public String getStateStr() {
		return stateStr;
	}
	public void setStateStr(String stateStr) {
		this.stateStr = stateStr;
	}
	public void setAccept_time(String accept_time) {
		this.accept_time = accept_time;
	}
	public String getDeal_time() {
		return deal_time;
	}
	public void setDeal_time(String deal_time) {
		this.deal_time = deal_time;
	}
	public String getReport_time() {
		return report_time;
	}
	public String getType_id() {
		return type_id;
	}
	public void setType_id(String type_id) {
		this.type_id = type_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getAdmin_id() {
		return admin_id;
	}
	public void setAdmin_id(String admin_id) {
		this.admin_id = admin_id;
	}
	public String getCompany_id() {
		return company_id;
	}
	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}
	public void setReport_time(String report_time) {
		this.report_time = report_time;
	}
	public Set<User> getAttention() {
		return attention;
	}
	public void setAttention(Set<User> attention) {
		this.attention = attention;
	}
}
