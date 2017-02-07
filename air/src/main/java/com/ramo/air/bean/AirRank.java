package com.ramo.air.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.ramo.air.BR;

import java.io.Serializable;

public class AirRank extends BaseObservable implements Serializable {
    private String area;
    private String provincename;
    private String aqi;
    private Integer aqinum;
    private String level;
    private String quality;
    private String time_point;

    private int color;


    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AirRank other = (AirRank) obj;
        if (area == null) {
            if (other.area != null) // 要比较的成员
                return false;
        } else if (!area.equals(other.area))
            return false;
        return true;
    }

    @Bindable
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
        notifyPropertyChanged(BR.area);
    }

    @Bindable
    public String getProvincename() {
        return provincename;
    }

    public void setProvincename(String provincename) {
        this.provincename = provincename;
        notifyPropertyChanged(BR.provincename);
    }

    @Bindable
    public Integer getAirnum() {
        return aqinum;
    }

    public void setAirnum(Integer airnum) {
        this.aqinum = airnum;
        notifyPropertyChanged(BR.airnum);
    }

    @Bindable
    public String getLevel() {

        return level;
    }

    public void setLevel(String level) {
        this.level = level;
        notifyPropertyChanged(BR.level);
    }

    @Bindable
    public int getColor() {
        aqinum = Integer.parseInt(this.aqi);
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
        notifyPropertyChanged(BR.color);
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
    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
        notifyPropertyChanged(BR.aqi);
    }

    @Bindable
    public String getTime_point() {
        return time_point;
    }

    public void setTime_point(String time_point) {
        this.time_point = time_point;
        notifyPropertyChanged(BR.time_point);
    }


}
