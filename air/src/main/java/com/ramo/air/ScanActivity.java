package com.ramo.air;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ramo.air.bean.AirQuality;
import com.ramo.air.bean.NearMonitoring;
import com.ramo.air.jsonparsing.AirParseBean;
import com.ramo.air.jsonparsing.AirResultParseBean;
import com.ramo.air.listener.CaptureSensorsObserver;
import com.ramo.air.listener.HttpCallbackListener;
import com.ramo.air.utils.BitmapUtil;
import com.ramo.air.utils.Constants;
import com.ramo.air.utils.GeoDistanceUtil;
import com.ramo.air.utils.L;
import com.ramo.air.utils.LocationUtil;
import com.ramo.air.utils.MyPreferenceUtils;
import com.ramo.air.utils.NetUtils;
import com.ramo.air.utils.PathManager;
import com.ramo.air.utils.PinYinUtil;
import com.ramo.air.utils.T;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.sina.weibo.SinaWeibo;
import onekeyshare.OnekeyShare;
import onekeyshare.Shake2Share;

@SuppressLint("NewApi")
// 默认的相机为横平，所以Activity设置为横屏，拍出的照片才正确
public class ScanActivity extends Activity implements
        CaptureSensorsObserver.RefocuseListener {

    private FrameLayout framelayoutPreview;
    private CameraPreview preview;
    private com.ramo.air.uicontrols.CameraCropBorderView cropBorderView;
    private Camera camera;
    private PictureCallback pictureCallBack;
    private Camera.AutoFocusCallback focusCallback;
    private CaptureSensorsObserver observer;
    private View focuseView;

    private int currentCameraId = 0;
    private int frontCameraId;
    private boolean _isCapturing;

    private ImageView scan_scanning;
    private ImageView scan_share;

    CaptureOrientationEventListener _orientationEventListener;
    private int _rotation;

    public static final int kPhotoMaxSaveSideLen = 1600;
    public static final String kPhotoPath = "photo_path";

    final static String TAG = "capture";

    private RelativeLayout scan_RL;
    private LinearLayout scan_result_LL;

    private TextView scan_result_aqiNum;
    private TextView scan_result_cityName;
    private TextView scan_result_quality;
    private TextView scan_result_nearMoni;
    private TextView scan_result_date;
    private TextView scan_result_distance;

    private LocationManager locationManager;
    private String provider;

    private Location location;
    // 当前位置经纬度
    private double lng1;
    private double lat1;

    private Gson gson = new Gson();
    private AirResultParseBean air;
    private Handler mHandler = new Handler() {
    };
    private Handler handlerGeo = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(ScanActivity.this, "定位出错，请稍后重试", 0).show();
                    break;
                case 1:
                    try {
                        String cityName = (((JSONObject) msg.obj).getJSONObject("addressComponent")).getString("city");
                        L.e("截取后的address:" + cityName);
                        scan_result_cityName.setText(cityName);

                        // 查询该城市所有监测点
                         queryCityNearMoni(cityName);
                        //queryCityNearMoni("汕头市");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };

    private Handler queryNearMoniHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    air = (AirResultParseBean) MyPreferenceUtils
                            .readObject("scan_result");
                    queryFromPreference(air);
                    break;
                case 0:
                    T.shortShow(ScanActivity.this, "查询失败，请稍后重试");
                default:
                    break;
            }
        }

        ;
    };
    private Runnable takePicture=new Runnable() {
        @Override
        public void run() {
            camera.takePicture(null, null, pictureCallBack);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        observer = new CaptureSensorsObserver(this);
        _orientationEventListener = new CaptureOrientationEventListener(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_layout);
        init();
        beginAnim();
        setListeners();
        setupDevice();
        mHandler.postDelayed(takePicetue, 3000);
    }

    protected void queryFromPreference(AirResultParseBean a) {
        AirQuality city = null;
        Map<Integer, NearMonitoring> lastMoniData = null;
        if (a != null) {
            city = a.getCitynow();
            lastMoniData = a.getLastMoniData();
        }
        if (city != null) {

            scan_result_aqiNum.setText(city.getAQI());
            scan_result_quality.setText(city.getQuality());

            switch (city.getColor()) {
                case 1:
                    scan_result_quality
                            .setBackgroundResource(R.drawable.aqi_official_bg_1);
                    break;
                case 2:
                    scan_result_quality
                            .setBackgroundResource(R.drawable.aqi_official_bg_2);
                    break;
                case 3:
                    scan_result_quality
                            .setBackgroundResource(R.drawable.aqi_official_bg_3);
                    break;
                case 4:
                    scan_result_quality
                            .setBackgroundResource(R.drawable.aqi_official_bg_4);
                    break;
                case 5:
                    scan_result_quality
                            .setBackgroundResource(R.drawable.aqi_official_bg_5);
                    break;
                default:
                    scan_result_quality
                            .setBackgroundResource(R.drawable.aqi_official_bg_6);
                    break;
            }
            L.e("city:" + city.getAQI());
        }
        findNearMonitoring(lastMoniData);

    }

    private void findNearMonitoring(Map<Integer, NearMonitoring> lastMoniData) {
        int i = 1, temp = 1;
        double min = 999999;
        double dis = 0;
        //L.e("scan_lastMoniData.size:" + lastMoniData.size());

        // 和所有的监测站的经纬度进行距离的比较 取最近
        if (lastMoniData != null && lastMoniData.size() > 0) {
            for (Integer id : lastMoniData.keySet()) {
                NearMonitoring moni = lastMoniData.get(id);
                L.e("moni:" + moni.getCity() + ";" + moni.getLon() + ";"
                        + moni.getLat());
                L.e("moni:" + moni.getCity() + moni.getAQI());

                if (moni.getLon() != null && !"".equals(moni.getLon())
                        && moni.getLat() != null && !"".equals(moni.getLat())
                        && !"null".equals(moni.getLat()) &&
                        !"null".equals(moni.getLon())) {

                    dis = GeoDistanceUtil.Distance(lng1, lat1,
                            Double.parseDouble(moni.getLon()),
                            Double.parseDouble(moni.getLat()));
                }
                if (min > dis) {
                    min = dis;
                    temp = i;
                }
                //L.e("min和dis" + min + ":" + dis);
                i++;
            }
            L.e("min位置" + temp);
            NearMonitoring lastMoni = lastMoniData.get(temp);
            if (lastMoni != null) {

                scan_result_nearMoni.setText(lastMoni.getCity());
                L.e("scan_最近检测站:" + lastMoni.getCity());
                scan_result_aqiNum.setText(lastMoni.getAQI());
                scan_result_quality.setText(lastMoni.getQuality());

                if (min < 1000)
                    scan_result_distance.setText(Math.round(min) + "m");
                else
                    scan_result_distance.setText(Math.round(min / 1000) + "km");

            } else
                L.e("lastMoni==null");

        } else {
            scan_result_distance.setText("0km");
            scan_result_nearMoni.setText("无");
        }
    }

    protected void queryCityNearMoni(String address) {

        Map<String, Object> map = new HashMap<>();
        address = PinYinUtil.getFullSpell(address.substring(0, address.length() - 1));// 去掉“市”

        map.put("city", address);
        map.put("key", "61a8b0c0ac3ea2875f8d64785a4cd70e");
        NetUtils.getHttpUrlConnectionJuHe(Constants.JUHE_AQIAPI, map, new HttpCallbackListener() {
            @Override
            public void onSucc(String response) {
                AirParseBean airParse = gson.fromJson(response,
                        AirParseBean.class);
                if (airParse.getResult() != null && airParse.getResult().size() > 0) {
                    MyPreferenceUtils.saveObject("scan_result", airParse.getResult().get(0));
                    queryNearMoniHandler.sendEmptyMessage(1);
                } else
                    queryNearMoniHandler.sendEmptyMessage(0);

            }

            @Override
            public void onError(Exception e) {
                queryNearMoniHandler.sendEmptyMessage(0);
                e.printStackTrace();
            }
        });
        scan_result_LL.setVisibility(View.VISIBLE);
        scan_RL.setVisibility(View.INVISIBLE);
    }

    private void beginAnim() {
        alphaAnimation = new TranslateAnimation(0, 0, 0f, 150f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        scan_scanning.setAnimation(alphaAnimation);
        alphaAnimation.start();
    }

    Runnable takePicetue = new Runnable() {

        @Override
        public void run() {
            bnCaptureClicked();
        }
    };
    private TranslateAnimation alphaAnimation;

    @Override
    protected void onDestroy() {
        if (null != observer) {
            observer.setRefocuseListener(null);
            observer = null;
        }
        _orientationEventListener = null;

        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera(); // release the camera immediately on pause event

        observer.stop();
        _orientationEventListener.disable();
    }

    @Override
    protected void onResume() {
        super.onResume();
        openCamera();
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release(); // release the camera for other applications
            camera = null;
        }

        if (null != preview) {
            framelayoutPreview.removeAllViews();
            preview = null;
        }
    }

    protected void init() {
        framelayoutPreview = (FrameLayout) findViewById(R.id.cameraPreview);
        focuseView = findViewById(R.id.viewFocuse);
        scan_scanning = (ImageView) findViewById(R.id.scan_scanning);
        scan_share = (ImageView) findViewById(R.id.scan_share);

        scan_result_LL = (LinearLayout) findViewById(R.id.scan_result_LL);
     //    scan_result_LL.setVisibility(View.INVISIBLE);
        scan_RL = (RelativeLayout) findViewById(R.id.scan_RL);
      //   scan_RL.setVisibility(View.INVISIBLE);

        scan_result_aqiNum = (TextView) findViewById(R.id.scan_result_aqiNum);
        scan_result_cityName = (TextView) findViewById(R.id.scan_result_cityName);
        scan_result_quality = (TextView) findViewById(R.id.scan_result_quality);
        scan_result_nearMoni = (TextView) findViewById(R.id.scan_result_nearMoni);
        scan_result_date = (TextView) findViewById(R.id.scan_result_date);
        scan_result_distance = (TextView) findViewById(R.id.scan_result_distance);
    }

    private OnekeyShare oks;
    private SimpleDateFormat format;

    protected void setListeners() {
        scan_share.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                L.e("开始分享");
                // 摇一摇截图分享
                Shake2Share s2s = new Shake2Share();
                // 设置回调，摇晃到一定程度就会触发分享

                oks = new OnekeyShare();
                // 设置一个用于截屏分享的View
                View windowView = getWindow().getDecorView();
                oks.setViewToShare(windowView);
                // 设置分享的信息
                oks.setTitle("空气质量分享");
                // 设置信息
                oks.setText("(来自@随手拍大气监控，人人都是监督员，人人都是环保员，欢迎使用随手拍 )");
                oks.setPlatform(SinaWeibo.NAME);
                oks.show(getBaseContext());

                s2s.show(ScanActivity.this, null);
            }
        });

        observer.setRefocuseListener(this);
        pictureCallBack = new PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                _isCapturing = false;
                Bitmap bitmap = null;
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory
                            .decodeByteArray(data, 0, data.length, options);
                    // Debug.debug("width--:" + options.outWidth + "  height--:"
                    // + options.outHeight);
                    options.inJustDecodeBounds = false;
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    // 此处就把图片压缩了
                    options.inSampleSize = Math.max(options.outWidth
                            / kPhotoMaxSaveSideLen, options.outHeight
                            / kPhotoMaxSaveSideLen);
                    bitmap = BitmapUtil.decodeByteArrayUnthrow(data, options);
                    if (null == bitmap) {
                        options.inSampleSize = Math.max(2,
                                options.inSampleSize * 2);
                        bitmap = BitmapUtil.decodeByteArrayUnthrow(data,
                                options);
                    }
                } catch (Throwable e) {
                }
                if (null == bitmap) {
                    Toast.makeText(ScanActivity.this, "内存不足，保存照片失败！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                // long start = System.currentTimeMillis();
                Bitmap addBitmap = BitmapUtil.rotateAndScale(bitmap, _rotation,
                        kPhotoMaxSaveSideLen, true);
                Bitmap finalBitmap = cropPhotoImage(addBitmap);
                File photoFile = PathManager.getCropPhotoPath();
                boolean successful = BitmapUtil.saveBitmap2file(finalBitmap,
                        photoFile, Bitmap.CompressFormat.JPEG, 100);
                while (!successful) {
                    successful = BitmapUtil.saveBitmap2file(finalBitmap,
                            photoFile, Bitmap.CompressFormat.JPEG, 100);
                }
                if (finalBitmap != null && !finalBitmap.isRecycled()) {
                    addBitmap.recycle();
                }
                Intent intent = new Intent();
                intent.putExtra(kPhotoPath, photoFile.getAbsolutePath());
                ScanActivity.this.setResult(RESULT_OK, intent);
                ScanActivity.this.finish();
            }
        };
        focusCallback = new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean successed, Camera camera) {
                focuseView.setVisibility(View.INVISIBLE);
            }
        };
    }

    // 根据拍照的图片来剪裁
    private Bitmap cropPhotoImage(Bitmap bmp) {
        int dw = bmp.getWidth();
        int dh = bmp.getHeight();
        int height;
        int width;
        if (dh > dw) {// 图片竖直方向
            // 切图片时按照竖屏来计算
            height = getWindowManager().getDefaultDisplay().getWidth();
            width = getWindowManager().getDefaultDisplay().getHeight();
        } else {// 图片是水平方向
            // 切图片时按照横屏来计算
            width = getWindowManager().getDefaultDisplay().getWidth();
            height = getWindowManager().getDefaultDisplay().getHeight();
        }
        Rect rect = new Rect();
        int left = (width - cropBorderView.getRect().width()) / 2;
        int top = (height - cropBorderView.getRect().height()) / 2;
        int right = left + cropBorderView.getRect().width();
        int bottom = top + cropBorderView.getRect().height();
        rect.set(left, top, right, bottom);
        float scale = 1.0f;
        // 如果图片的宽或者高大于屏幕，则缩放至屏幕的宽或者高
        if (dw > width && dh <= height) {
            scale = width * 1.0f / dw;
        }
        if (dh > height && dw <= width) {
            scale = height * 1.0f / dh;
        }
        // 如果宽和高都大于屏幕，则让其按按比例适应屏幕大小
        if (dw > width && dh > height) {
            scale = Math.max(width * 1.0f / dw, height * 1.0f / dh);
        }
        // 如果图片的宽度和高度都小于屏幕的宽度和高度，则放大至屏幕大小
        if (dw < width && dh < height) {
            scale = width * 1.0f / dw;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        try {
            Bitmap b2 = Bitmap.createBitmap(bmp, 0, 0, dw, dh, matrix, true);
            if (null != b2 && bmp != b2) {
                bmp.recycle();
                bmp = b2;
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        try {
            Bitmap b3 = Bitmap.createBitmap(bmp, rect.left, rect.top,
                    rect.width(), rect.height());
            if (null != b3 && bmp != b3) {
                bmp.recycle();
                bmp = b3;
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        // 将图片压缩至640*640
        try {
            Bitmap b4 = Bitmap.createScaledBitmap(bmp, 640, 640, false);
            if (null != b4 && bmp != b4) {
                bmp.recycle();
                bmp = b4;
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bmp;
    }

    private void setupDevice() {
        if (android.os.Build.VERSION.SDK_INT > 8) {
            int cameraCount = Camera.getNumberOfCameras();

            if (cameraCount < 1) {
                Toast.makeText(this, "你的设备木有摄像头。。。", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            currentCameraId = 0;
            frontCameraId = findFrontFacingCamera();
        }
    }

    private void openCamera() {
        if (android.os.Build.VERSION.SDK_INT > 8) {
            try {
                camera = Camera.open(currentCameraId);
            } catch (Exception e) {
                Toast.makeText(this, "摄像头打开失败", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            setCameraDisplayOrientation(this, 0, camera);
        } else {
            try {
                camera = Camera.open();
            } catch (Exception e) {
                Toast.makeText(this, "摄像头打开失败", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }

        Camera.Parameters camParmeters = camera.getParameters();
        List<Size> sizes = camParmeters.getSupportedPreviewSizes();
        for (Size size : sizes) {
            Log.v(TAG, "w:" + size.width + ",h:" + size.height);
        }

        preview = new CameraPreview(this, camera);
        cropBorderView = new com.ramo.air.uicontrols.CameraCropBorderView(this);
        FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        framelayoutPreview.addView(preview, params1);
        framelayoutPreview.addView(cropBorderView, params2);
        observer.start();
        _orientationEventListener.enable();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // 横竖屏切换的时候
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void bnCaptureClicked() {
        _isCapturing = true;
        focuseView.setVisibility(View.INVISIBLE);

        try {
            // 设置扫描信息
            initData();
            mHandler.postDelayed(takePicture, 4000);


        } catch (RuntimeException e) {
            e.printStackTrace();
            _isCapturing = false;
        }
    }

    private void initData() {
        // 设置时间
        format = new SimpleDateFormat("yyyy年MM月dd日");
        scan_result_date.setText(format.format(new Date()));

        // 获取当前的经纬度
        initLocation();

    }

    private void initLocation() {
        LocationUtil locationUtil=new LocationUtil(this,handlerGeo);
        locationUtil.initLocation(locationManager,locationListener);
    }


    /**
     * A basic Camera preview class
     */
    public class CameraPreview extends SurfaceView implements
            SurfaceHolder.Callback {
        private SurfaceHolder mHolder;
        private Camera mCamera;

        @SuppressWarnings("deprecation")
        public CameraPreview(Context context, Camera camera) {
            super(context);
            mCamera = camera;

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            // The Surface has been created, now tell the camera where to draw
            // the preview.
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (Exception e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // empty. Take care of releasing the Camera preview in your
            // activity.
            if (camera != null) {
                camera.setPreviewCallback(null);
                camera.stopPreview();// 停止预览
                camera.release(); // 释放摄像头资源
                camera = null;
            }
        }

        private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
            final double ASPECT_TOLERANCE = 0.05;
            double targetRatio = (double) w / h;
            if (sizes == null)
                return null;

            Size optimalSize = null;
            double minDiff = Double.MAX_VALUE;

            int targetHeight = h;

            // Try to find an size match aspect ratio and size
            for (Size size : sizes) {
                double ratio = (double) size.width / size.height;
                if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                    continue;
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }

            // Cannot find the one match the aspect ratio, ignore the
            // requirement
            if (optimalSize == null) {
                minDiff = Double.MAX_VALUE;
                for (Size size : sizes) {
                    if (Math.abs(size.height - targetHeight) < minDiff) {
                        optimalSize = size;
                        minDiff = Math.abs(size.height - targetHeight);
                    }
                }
            }

            return optimalSize;
        }

        private Size getOptimalPictureSize(List<Size> sizes, double targetRatio) {
            final double ASPECT_TOLERANCE = 0.05;

            if (sizes == null)
                return null;

            Size optimalSize = null;
            int optimalSideLen = 0;
            double optimalDiffRatio = Double.MAX_VALUE;

            for (Size size : sizes) {

                int sideLen = Math.max(size.width, size.height);
                // LogEx.i("size.width: " + size.width + ", size.height: " +
                // size.height);
                boolean select = false;
                if (sideLen < kPhotoMaxSaveSideLen) {
                    if (0 == optimalSideLen || sideLen > optimalSideLen) {
                        select = true;
                    }
                } else {
                    if (kPhotoMaxSaveSideLen > optimalSideLen) {
                        select = true;
                    } else {
                        double diffRatio = Math.abs((double) size.width
                                / size.height - targetRatio);
                        if (diffRatio + ASPECT_TOLERANCE < optimalDiffRatio) {
                            select = true;
                        } else if (diffRatio < optimalDiffRatio
                                + ASPECT_TOLERANCE
                                && sideLen < optimalSideLen) {
                            select = true;
                        }
                    }
                }

                if (select) {
                    optimalSize = size;
                    optimalSideLen = sideLen;
                    optimalDiffRatio = Math.abs((double) size.width
                            / size.height - targetRatio);
                }
            }

            return optimalSize;
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w,
                                   int h) {
            // If your preview can change or rotate, take care of those events
            // here.
            // Make sure to stop the preview before resizing or reformatting it.

            L.e("surfaceChanged format:" + format + ", w:" + w + ", h:" + h);
            if (mHolder.getSurface() == null) {
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e) {
                // ignore: tried to stop a non-existent preview
            }

            try {
                Camera.Parameters parameters = mCamera.getParameters();

                List<Size> sizes = parameters.getSupportedPreviewSizes();
                Size optimalSize = getOptimalPreviewSize(sizes, w, h);
                parameters
                        .setPreviewSize(optimalSize.width, optimalSize.height);
                double targetRatio = (double) w / h;
                sizes = parameters.getSupportedPictureSizes();
                optimalSize = getOptimalPictureSize(sizes, targetRatio);
                parameters
                        .setPictureSize(optimalSize.width, optimalSize.height);
                parameters.setRotation(0);
                mCamera.setParameters(parameters);
            } catch (Exception e) {
                L.e(e.toString());
            }

            // set preview size and make any resize, rotate or
            // reformatting changes here

            // start preview with new settings
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
            } catch (Exception e) {
                L.e("Error starting camera preview: " + e.getMessage());
            }
        }
    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                Log.d(TAG, "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    private static void setCameraDisplayOrientation(Activity activity,
                                                    int cameraId, Camera camera) {
        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        // LogEx.i("result: " + result);
        camera.setDisplayOrientation(result);
    }

    @Override
    public void needFocuse() {

        // LogEx.i("_isCapturing: " + _isCapturing);
        if (null == camera || _isCapturing) {
            return;
        }

        // LogEx.i("autoFocus");
        camera.cancelAutoFocus();
        try {
            camera.autoFocus(focusCallback);
        } catch (Exception e) {
            L.e(e.toString());
            return;
        }

        if (View.INVISIBLE == focuseView.getVisibility()) {
            focuseView.setVisibility(View.VISIBLE);
            focuseView.getParent().requestTransparentRegion(preview);
        }
    }

    // 相机旋转监听的类，最后保存图片时用到
    private class CaptureOrientationEventListener extends
            OrientationEventListener {
        public CaptureOrientationEventListener(Context context) {
            super(context);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            if (null == camera)
                return;
            if (orientation == ORIENTATION_UNKNOWN)
                return;

            orientation = (orientation + 45) / 90 * 90;
            if (android.os.Build.VERSION.SDK_INT <= 8) {
                _rotation = (90 + orientation) % 360;
                return;
            }

            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(currentCameraId, info);

            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                _rotation = (info.orientation - orientation + 360) % 360;
            } else { // back-facing camera
                _rotation = (info.orientation + orientation) % 360;
            }
        }
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
          //  reverseGeocoding(location);
        }
    };
}
