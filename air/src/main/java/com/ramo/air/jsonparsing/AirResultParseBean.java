package com.ramo.air.jsonparsing;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.ramo.air.BR;
import com.ramo.air.bean.AirQuality;
import com.ramo.air.bean.NearMonitoring;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class AirResultParseBean extends BaseObservable implements Serializable {
    private AirQuality citynow;
    private Map<Integer, AirQuality> lastTwoWeeks = new HashMap();
    private Map<Integer, NearMonitoring> lastMoniData = new HashMap();

    @Bindable
    public AirQuality getCitynow() {
        return citynow;
    }

    public void setCitynow(AirQuality citynow) {
        this.citynow = citynow;
        notifyPropertyChanged(BR.citynow);
    }

    @Bindable
    public Map<Integer, AirQuality> getLastTwoWeeks() {
        return lastTwoWeeks;
    }

    public void setLastTwoWeeks(Map<Integer, AirQuality> lastTwoWeeks) {
        this.lastTwoWeeks = lastTwoWeeks;
        notifyPropertyChanged(BR.lastTwoWeeks);
    }

    @Bindable
    public Map<Integer, NearMonitoring> getLastMoniData() {
        return lastMoniData;
    }

    public void setLastMoniData(Map<Integer, NearMonitoring> lastMoniData) {
        this.lastMoniData = lastMoniData;
        notifyPropertyChanged(BR.lastMoniData);
    }


}
