package com.ramo.air.jsonparsing;

import android.databinding.Bindable;
import android.databinding.BaseObservable;

import com.ramo.air.BR;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AirParseBean extends BaseObservable implements Serializable {
    private String resultcode;
    private String reason;
    private Integer error_code;
    private List<AirResultParseBean> result = new ArrayList();

    @Bindable
    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
        notifyPropertyChanged(BR.resultcode);
    }

    @Bindable
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason; notifyPropertyChanged(BR.reason);
    }

    @Bindable
    public Integer getError_code() {
        return error_code;
    }

    public void setError_code(Integer error_code) {
        this.error_code = error_code;notifyPropertyChanged(BR.error_code);
    }

    @Bindable
    public List<AirResultParseBean> getResult() {
        return result;
    }

    public void setResult(List<AirResultParseBean> result) {
        this.result = result;
        notifyPropertyChanged(BR.result);
    }

}
