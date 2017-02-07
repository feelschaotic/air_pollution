package com.ramo.air.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;

import com.ramo.air.R;
import com.ramo.air.RealNameActivtiy;
import com.ramo.air.bean.ReportDataBean;
import com.ramo.air.databinding.SubmitComplaintsLayoutBinding;
import com.ramo.air.handle.ReportMapHandler;
import com.ramo.air.listener.HttpCallbackListener;
import com.ramo.air.utils.ImageManageUtil;
import com.ramo.air.utils.L;
import com.ramo.air.utils.LocationUtil;
import com.ramo.air.utils.NetUtils;
import com.ramo.air.utils.ServerUrlUtil;
import com.ramo.air.utils.T;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import amap.yuntu.core.PushDataListener;

/**
 * Created by ramo on 2016/11/23.
 */
public class SubmitComplaintsActivityPresenter {
    private Context context;
    private SubmitComplaintsLayoutBinding binding;
    private boolean hasShootPic;
    private Bitmap bmp;
    private ReportMapHandler drawInMapHandler;

    private LocationUtil locationUtil;

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    T.shortShow(context, "提交失败,请稍后再试");
                    break;
                case 1:

                    T.shortShow(context, "举报成功，请等待管理员审核");
                    //在高德云图上标记举报位置
                    drawInMap();
                    ((Activity) context).finish();
                    ((Activity) context).overridePendingTransition(R.anim.keep,
                            R.anim.base_slide_right_out);
                    break;
                default:
                    break;
            }
        }

        ;
    };
    private Handler handlerGeo = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    T.shortShow(context, "定位出错，请稍后重试");
                    break;
                case 1:
                    try {
                        String address;
                        String business;
                        address = ((JSONObject) msg.obj).getString("formatted_address");
                        business = ((JSONObject) msg.obj).getString("business");
                        binding.setLocation(address + business);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };
    private Handler handlerCheckRealName = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    T.shortShow(context, "用户信息查询出错，请稍后重试");
                    break;
                case 1:
                    // 有实名认证
                    funSendReport();
                    break;
                case 2:
                    // 还没实名认证
                    T.shortShow(context, "请先实名认证");
                    context.startActivity(new Intent(context,
                            RealNameActivtiy.class));
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public SubmitComplaintsActivityPresenter(Context context, SubmitComplaintsLayoutBinding binding) {
        this.context = context;
        this.binding = binding;
    }

    public void checkHasRealName() {
        NetUtils.getIOFromUrl(ServerUrlUtil.IS_REALNAME,
                new HttpCallbackListener() {
                    Message msg = new Message();

                    @Override
                    public void onSucc(String response) {
                        // 有实名认证
                        if (response.equals("true"))
                            msg.what = 1;
                        else
                            msg.what = 2;// 还没认证
                        handlerCheckRealName.sendMessage(msg);
                    }

                    @Override
                    public void onError(Exception e) {
                        msg.what = 0;
                        handlerCheckRealName.sendMessage(msg);
                        e.printStackTrace();
                    }
                });

    }

    public void setShootFlagAndBitmap(boolean hasShootPic, Bitmap bmp) {
        this.hasShootPic = hasShootPic;
        this.bmp = bmp;
    }

    private void funSendReport() {
        String imgStr = null;
        if (hasShootPic) {
            imgStr = ImageManageUtil.bitmapToString(bmp);
        } else
            imgStr = "";
        binding.reportImg.setDrawingCacheEnabled(true);

        NetUtils.sendRequestToUrl(ServerUrlUtil.SUBMIT_REPORT, new String[]{
                        "report_content", "report_location", "report_img",
                        "report_type"}, new String[]{
                        binding.reportContent.getText().toString(),
                        binding.reportLocation.getText().toString(), imgStr,
                        binding.reportType.getSelectedItem().toString()},
                new HttpCallbackListener() {
                    Message m = new Message();

                    @Override
                    public void onSucc(String response) {
                        m.what = 1;
                        handler.sendMessage(m);
                        L.d(response + ":report success");
                        binding.reportImg.setDrawingCacheEnabled(false);
                    }

                    @Override
                    public void onError(Exception e) {
                        m.what = 0;
                        handler.sendMessage(m);
                        L.e(e + ":report erroe");
                    }
                });
    }

    protected void drawInMap() {
        ReportDataBean bean = new ReportDataBean();
        bean.set_name(binding.reportLocation.getText().toString());
        bean.set_address("中国" + binding.reportLocation.getText().toString());
        bean.setCompany_name("");
        bean.setReport_time(new Date().toLocaleString());
        bean.setReport_content(binding.reportContent.getText().toString());
        bean.setResult("暂无结果");
        bean.setState("未审核");

        if (locationUtil.getLocation() != null) {
            bean.set_location(locationUtil.getLocation().getLongitude() + "," + locationUtil.getLocation().getLatitude());
        }

        drawInMapHandler.addTask(bean);
    }

    public void init() {
        drawInMapHandler = new ReportMapHandler(new PushDataListener() {

            @Override
            public void onPushFinish(boolean succeed, String errorDes) {
                L.e("上传：" + succeed + "  " + errorDes);
            }
        });
    }

    public void destroyHandler() {
        if (drawInMapHandler != null) {
            drawInMapHandler.destroy();//关闭处理器
        }
    }

    public void reverseGeocoding(Location location) {
        locationUtil.reverseGeocoding(location);
    }

    public void initLocation(LocationManager locationManager, LocationListener locationListener) {
        if (locationUtil == null)
            locationUtil = new LocationUtil(context, handlerGeo);
        locationUtil.initLocation(locationManager, locationListener);
    }
}
