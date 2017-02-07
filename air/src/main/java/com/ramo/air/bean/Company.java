package com.ramo.air.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Company implements Serializable{
	private String company_id;
	private String company_name;
	private String company_location;
	private Set<Report> beReport=new HashSet<Report>();
	public String getCompany_id() {
		return company_id;
	}
	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getCompany_location() {
		return company_location;
	}
	public void setCompany_location(String company_location) {
		this.company_location = company_location;
	}
	public Set<Report> getBeReport() {
		return beReport;
	}
	public void setBeReport(Set<Report> beReport) {
		this.beReport = beReport;
	}
}
