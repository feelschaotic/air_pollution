package com.ramo.air;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.ramo.air.databinding.SubmitComplaintsLayoutBinding;
import com.ramo.air.presenter.SubmitComplaintsActivityPresenter;
import com.ramo.air.utils.L;
import com.ramo.air.utils.T;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SubmitComplaintsActivity extends Activity {


    private PagerAdapter report_face_adapter;
    private List<View> report_faces;

    private LocationManager locationManager;
    private Gson gson;


    private boolean hasShootPic = false;
    private Bitmap bmp = null;

    private LayoutInflater inflater;

    SubmitComplaintsLayoutBinding binding;
    SubmitComplaintsActivityPresenter activityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.submit_complaints_layout);

        activityPresenter = new SubmitComplaintsActivityPresenter(this, binding);
        binding.setPresenter(new Presenter());
        init();
        activityPresenter.init();
        activityPresenter.initLocation(locationManager, locationListener);
        initEvent();
    }

    public class Presenter {
        public void onReportContextClick(View v) {
            binding.reportViewPager.setVisibility(View.INVISIBLE);
            binding.submitReportFaceButtom.reportButtom.setVisibility(View.INVISIBLE);
        }

        public void onReportLocationClick(View v) {
            binding.reportLocation.setText("定位中，请稍后");
            activityPresenter.initLocation(locationManager, locationListener);
        }

        public void onDeleteLocationClick(View v) {
            binding.reportLocation.setText("点击定位");
        }

        public void onReportFaceBtnClick(View v) {
            binding.reportViewPager.setVisibility(View.VISIBLE);
            binding.submitReportFaceButtom.reportButtom.setVisibility(View.VISIBLE);
        }

        public void onReportSendClick(View v) {
            // 先检查是否有实名认证 如果有 再提交举报
            L.e("开始检查实名");
            activityPresenter.checkHasRealName();
        }

        public void onReportImgClick(View v) {
            Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(i, Activity.DEFAULT_KEYS_DIALER);
        }
    }

    private void initEvent() {

        binding.reportContent.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int editStart;
            private int editEnd;

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                temp = arg0;
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editStart = binding.reportContent.getSelectionStart();
                editEnd = binding.reportContent.getSelectionEnd();
                binding.reportFontLength.setText(temp.length() + "/200");
                binding.reportFontLength.setTextColor(Color.WHITE);

                if (temp.length() > 200) {
                    T.shortShow(SubmitComplaintsActivity.this,
                            "你输入的字数超过限制！");
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    binding.reportContent.setText(s);
                    binding.reportContent.setSelection(tempSelection);
                }
            }
        });
    }


    View tab1, tab2, tab3;

    private void init() {
        inflater = LayoutInflater.from(this);
        tab1 = inflater.inflate(R.layout.submit_report_face_item1, null);
        tab2 = inflater.inflate(R.layout.submit_report_face_item2, null);
        tab3 = inflater.inflate(R.layout.submit_report_face_item3, null);
        report_faces = new ArrayList<View>();
        report_faces.add(tab1);
        report_faces.add(tab2);
        report_faces.add(tab3);

        report_face_adapter = new PagerAdapter() {

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                binding.reportViewPager.removeView(report_faces.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = report_faces.get(position);
                binding.reportViewPager.addView(view);
                return view;
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return report_faces.size();
            }
        };

        binding.reportViewPager.setAdapter(report_face_adapter);

        gson = new Gson();

        Bundle extras = getIntent().getExtras();

        if (extras != null)
            bmp = (Bitmap) extras.get("data");
        if (bmp != null) {
            binding.reportImg.setImageBitmap(bmp);
            hasShootPic = true;
        }
        activityPresenter.setShootFlagAndBitmap(hasShootPic, bmp);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            // 关闭程序时将监听器移除
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(locationListener);

        }
        activityPresenter.destroyHandler();
    }


    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onLocationChanged(Location location) {
           // activityPresenter.reverseGeocoding(location);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                super.onActivityResult(requestCode, resultCode, data);
                if (data != null) {
                    Bundle extras = data.getExtras();
                    bmp = (Bitmap) extras.get("data");

                    binding.reportImg.setImageBitmap(bmp); // 设置照片现实在界面上
                    hasShootPic = true;// 此变量是在提交数据时，验证是否有图片用
                } else {
                    hasShootPic = false;
                }
                activityPresenter.setShootFlagAndBitmap(hasShootPic, bmp);
                break;
            default:
                break;
        }

    }

    private Bitmap bitmap;
    private ImageSpan imageSpan;
    private SpannableString spannableString;

    public void faceClick(View v) {

        Field field;
        int resourceId = 0;
        String num = "1";
        switch (v.getId()) {
            case R.id.moji_text_1_black:
                num = "1";
                break;
            case R.id.moji_text_2_black:
                num = "2";
                break;
            case R.id.moji_text_3_black:
                num = "3";
                break;
            case R.id.moji_text_4_black:
                num = "4";
                break;
            case R.id.moji_text_5_black:
                num = "5";
                break;
            case R.id.moji_text_6_black:
                num = "6";
                break;
            case R.id.moji_text_7_black:
                num = "7";
                break;
            case R.id.moji_text_8_black:
                num = "8";
                break;
            case R.id.moji_text_9_black:
                num = "9";
                break;
            case R.id.moji_text_10_black:
                num = "10";
                break;
            case R.id.moji_text_11_black:
                num = "11";
                break;
            case R.id.moji_text_12_black:
                num = "12";
                break;
            case R.id.moji_text_13_black:
                num = "13";
                break;
            case R.id.moji_text_14_black:
                num = "14";
                break;
            case R.id.moji_text_15_black:
                num = "15";
                break;
            case R.id.moji_text_16_black:
                num = "16";
                break;
            case R.id.moji_text_17_black:
                num = "17";
                break;
            case R.id.moji_text_18_black:
                num = "18";
                break;
            case R.id.moji_text_19_black:
                num = "19";
                break;
            case R.id.moji_text_20_black:
                num = "20";
                break;
            case R.id.moji_text_21_black:
                num = "21";
                break;
            case R.id.moji_text_22_black:
                num = "22";
                break;
            case R.id.moji_text_23_black:
                num = "23";
                break;
            case R.id.moji_text_24_black:
                num = "24";
                break;
            case R.id.moji_text_delete_black:
                num = "0";
                int selectionStart = binding.reportContent.getSelectionStart();// 获取光标的位置
                if (selectionStart > 0) {
                    String body = binding.reportContent.getText().toString();
                    if (!TextUtils.isEmpty(body)) {
                        String tempStr = body.substring(0, selectionStart);
                        int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                        if (i != -1) {
                            CharSequence cs = tempStr
                                    .subSequence(i, selectionStart);
                            binding.reportContent.getEditableText().delete(i,
                                    selectionStart);
                            return;
                        }
                        binding.reportContent.getEditableText().delete(
                                tempStr.length() - 1, selectionStart);
                    }
                }
                break;

            default:
                break;
        }
        if (!("0".equals(num))) {
            try {
                field = R.drawable.class.getDeclaredField("emoji_text_" + num);
                resourceId = Integer.parseInt(field.get(null).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
            imageSpan = new ImageSpan(this, bitmap);
            spannableString = new SpannableString("[face");
            spannableString.setSpan(imageSpan, 0, 5,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            binding.reportContent.append(spannableString);
        }
    }
}
