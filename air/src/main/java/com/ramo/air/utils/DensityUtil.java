package com.ramo.air.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class DensityUtil {


    private  final String TAG = DensityUtil.class.getSimpleName();
    
    // ��ǰ��Ļ��densityDpi
    private  float dmDensityDpi = 0.0f;
    private  DisplayMetrics dm;
    private  float scale = 0.0f;
 
    
    public DensityUtil(Context context) {
        // ��ȡ��ǰ��Ļ
        dm = new DisplayMetrics();
 
        //���ص�ǰ��Դ�����DispatchMetrics��Ϣ��
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        // ����DensityDpi
        dmDensityDpi=dm.densityDpi;
       // setDmDensityDpi(dm.densityDpi);
        // �ܶ�����
        scale = getDmDensityDpi() / 160;//���� scale=dm.density;

    }
 
    /**
     * ��Ļ���
     * @return
     */
    public  int getScreenWidth(){
    	return dm.widthPixels;
    }
    
    /**
     * ��Ļ�߶�
     * @return
     */
    public  int getScreenHeight(){
    	return dm.heightPixels;
    }
    
    
    public  float getDmDensityDpi() {
        return dmDensityDpi;
    }
 
//    
//    public  void setDmDensityDpi(float dmDensityDpi) {
//        DensityUtil.dmDensityDpi = dmDensityDpi;
//    }
// 
    
    public  int dip2px(float dipValue) {
 
        return (int) (dipValue * scale + 0.5f);
 
    }
 
    
    public int px2dip(float pxValue) {
        return (int) (pxValue / scale + 0.5f);
    }
 
    public String toString() {
        return " dmDensityDpi:" + dmDensityDpi;
    }
}
