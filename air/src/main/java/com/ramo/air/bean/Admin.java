package com.ramo.air.bean;

import java.io.Serializable;

public class Admin implements Serializable{
	private String admin_id;
	private String admin_name;
	private String admin_pass;
	private boolean admin_state;
	public String getAdmin_id() {
		return admin_id;
	}
	public void setAdmin_id(String admin_id) {
		this.admin_id = admin_id;
	}
	public String getAdmin_name() {
		return admin_name;
	}
	public void setAdmin_name(String admin_name) {
		this.admin_name = admin_name;
	}
	public String getAdmin_pass() {
		return admin_pass;
	}
	public void setAdmin_pass(String admin_pass) {
		this.admin_pass = admin_pass;
	}
	public boolean getAdmin_state() {
		return admin_state;
	}
	public void setAdmin_state(boolean admin_state) {
		this.admin_state = admin_state;
	}
}	
