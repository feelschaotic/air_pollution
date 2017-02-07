package com.ramo.air.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.ramo.air.BR;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AirQuality extends BaseObservable implements Serializable {
    private String city;
    private String AQI; /* 空气质量指数 */
    private String quality; /* 空气质量 */
    private String pm25; /* PM2.5指数 */
    private String PM10;/* PM10 */
    private String CO; /* 一氧化碳 */
    private String NO2;/* 二氧化氮 */
    private String O3;/* 臭氧 */
    private String SO2;/* 二氧化硫 */
    private String date;/* 更新时间 */
    List<AirQuality> lastTwoWeeks = new ArrayList<AirQuality>();// 最近几天空气质量
    List<NearMonitoring> lastMoniData = new ArrayList<NearMonitoring>();// 最近的监测点
    private int color;
    private Integer aqinum;

    public AirQuality() {
        super();
    }

    public AirQuality(String city, String AQI, String quality, String pm25, String PM10, String CO, String NO2, String O3, String SO2, String date) {
        this.city = city;
        this.AQI = AQI;
        this.quality = quality;
        this.pm25 = pm25;
        this.PM10 = PM10;
        this.CO = CO;
        this.NO2 = NO2;
        this.O3 = O3;
        this.SO2 = SO2;
        this.date = date;
    }

    public boolean isAQIMoreThanOneHundred() {
        return Integer.valueOf(this.AQI) >= 100;
    }

    public boolean isAQIMoreThanFifty() {
        return Integer.valueOf(this.AQI) >= 50;
    }

    public boolean isAQIMoreThanSixty() {
        return Integer.valueOf(this.AQI) >= 60;
    }

    @Bindable
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        notifyPropertyChanged(BR.city);
    }

    @Bindable
    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
        notifyPropertyChanged(BR.quality);
    }

    @Bindable
    public String getAQI() {
        return AQI;
    }

    public void setAQI(String aQI) {
        AQI = aQI;
        notifyPropertyChanged(BR.aQI);
    }

    @Bindable
    public String getPm25() {
        return pm25;
    }

    public void setPm25(String string) {
        this.pm25 = string;
        notifyPropertyChanged(BR.pm25);
    }

    @Bindable
    public List<AirQuality> getLastTwoWeeks() {
        return lastTwoWeeks;
    }

    public void setLastTwoWeeks(List<AirQuality> lastTwoWeeks) {
        this.lastTwoWeeks = lastTwoWeeks;
        notifyPropertyChanged(BR.lastTwoWeeks);
    }

    @Bindable
    public List<NearMonitoring> getLastMoniData() {
        return lastMoniData;
    }

    public void setLastMoniData(List<NearMonitoring> lastMoniData) {
        this.lastMoniData = lastMoniData;
        notifyPropertyChanged(BR.lastMoniData);
    }

    @Bindable
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        notifyPropertyChanged(BR.date);
    }

    @Bindable
    public String getPM10() {
        return PM10;
    }

    public void setPM10(String pM10) {
        PM10 = pM10;
        notifyPropertyChanged(BR.pM10);
    }

    @Bindable
    public String getCO() {
        return CO;
    }

    public void setCO(String cO) {
        CO = cO;
        notifyPropertyChanged(BR.cO);
    }

    @Bindable
    public String getNO2() {
        return NO2;
    }

    public void setNO2(String nO2) {
        NO2 = nO2;
        notifyPropertyChanged(BR.nO2);
    }

    @Bindable
    public String getO3() {
        return O3;
    }

    public void setO3(String o3) {
        O3 = o3;
        notifyPropertyChanged(BR.o3);
    }

    @Bindable
    public String getSO2() {
        return SO2;
    }

    public void setSO2(String sO2) {
        SO2 = sO2;
        notifyPropertyChanged(BR.sO2);
    }

    @Bindable
    public int getColor() {
        aqinum = Integer.parseInt(this.AQI);
        if (this.aqinum >= 0 && this.aqinum <= 50) {
            this.color = 1;
        } else if (this.aqinum >= 51 && this.aqinum <= 100) {
            this.color = 2;
        } else if (this.aqinum >= 101 && this.aqinum <= 150) {
            this.color = 3;
        } else if (this.aqinum >= 151 && this.aqinum <= 200) {
            this.color = 4;
        } else if (this.aqinum >= 201 && this.aqinum <= 300) {
            this.color = 5;
        } else if (this.aqinum >= 301 && this.aqinum <= 499) {
            this.color = 6;
        } else if (this.aqinum >= 500) {
            this.color = 6;
        }
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

}
