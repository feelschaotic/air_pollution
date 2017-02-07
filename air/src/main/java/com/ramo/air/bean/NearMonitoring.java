package com.ramo.air.bean;

import java.io.Serializable;

public class NearMonitoring implements Serializable{
	private String city;
	private String AQI;
	private String quality;
	private String PM25Hour;// "84μg/m³"
	private String PM25Day;// "84μg/m³"
	private String PM10Hour;// "—μg/m³"
	private String lat;// "31.286389"
	private String lon;// "120.6275"
	private int color;
	private Integer aqi;
	public NearMonitoring(){
		super();
	}
	public NearMonitoring(String city,String AQI,String PM25Hour,String PM25Day,String PM10Hour){
		this.city=city;
		this.AQI=AQI;
		this.PM25Day=PM25Day;
		this.PM25Hour=PM25Hour;
		this.PM10Hour=PM10Hour;
	}
	public int getColor() {
		aqi=Integer.parseInt(AQI);
		if(this.aqi>=0&&this.aqi<=50){
			this.color=1;
		}
		else if(this.aqi>=51&&this.aqi<=100){
			this.color=2;
		}
		else if(this.aqi>=101&&this.aqi<=150){
			this.color=3;
		}
		else if(this.aqi>=151&&this.aqi<=200){
			this.color=4;
		}
		else if(this.aqi>=201&&this.aqi<=300){
			this.color=5;
		}
		else if(this.aqi>=301&&this.aqi<=499){
			this.color=6;
		}
		else if(this.aqi>=500){
			this.color=6;
		}
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Integer getAqi() {
		return aqi;
	}
	public void setAqi(Integer aqi) {
		this.aqi = aqi;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public String getPM25Hour() {
		return PM25Hour;
	}
	public void setPM25Hour(String pM25Hour) {
		PM25Hour = pM25Hour;
	}
	public String getPM25Day() {
		return PM25Day;
	}
	public void setPM25Day(String pM25Day) {
		PM25Day = pM25Day;
	}
	public String getPM10Hour() {
		return PM10Hour;
	}
	public void setPM10Hour(String pM10Hour) {
		PM10Hour = pM10Hour;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getAQI() {
		return AQI;
	}

	public void setAQI(String aQI) {
		AQI = aQI;
	}
}
