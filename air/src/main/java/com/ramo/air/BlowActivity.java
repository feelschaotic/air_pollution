package com.ramo.air;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ramo.air.utils.L;

import onekeyshare.OnekeyShare;
import thread.RecordThread;

public class BlowActivity extends Activity {


    private ScrollView blow_scrollview;
    private ImageView blow_haze_iv;
    private TranslateAnimation alphaAnimation;
    private Button blow_begin_btn;

    private TextView blow_tip_tv;
    private TextView blow_result_tv;
    private TextView blow_result_num;
    private TextView blow_result_percentum;
    private RecordThread thread;

    private LinearLayout blow_result_LL;
    private ImageView blow_bg_iv;

    private boolean firstGet = true;
    private Double num;
    private Double sum = (double) 0;
    private int bg_height;
    private int scroll_height;
    private Handler mhandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    num = Double.parseDouble((String) msg.obj);
                    sum += num;
                    L.e("blowNum:" + num);
                    blow_result_num.setVisibility(View.VISIBLE);
                    blow_result_num.setText(Math.round(sum) + "米");

                    break;

                default:
                    break;
            }
            mhandler.post(mScrollToBottom);
            //是否是第一次得到数据
            if (firstGet) {
                mhandler.postDelayed(stopRunnable, 3000);
                firstGet = false;
            }
        }

        ;

    };
    private Runnable stopRunnable = new Runnable() {
        @Override
        public void run() {
            RecordThread.isRun = false;

            //恢复界面
            recoverView();
            //设置结果界面
            setResultView();
        }
    };
    private Runnable mScrollToBottom = new Runnable() {
        @Override
        public void run() {
            scroll_height -= num / 60;
            L.e("scroll_height:" + scroll_height);
            if (scroll_height > 0) {
                blow_scrollview.scrollTo(0, scroll_height);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blow);
        init();
        //initAnim();
        initEvent();
    }

    protected void setResultView() {

        blow_result_percentum.setText(Math.round((sum / 250000) * 100) + "%");
        blow_result_num.setText(Math.round(sum) + "米");
        blow_result_tv.setVisibility(View.VISIBLE);
        blow_result_LL.setVisibility(View.VISIBLE);
        blow_result_num.setVisibility(View.VISIBLE);

        //分享页面
        // 一键分享
        OnekeyShare share = new OnekeyShare();
        // 设置分享的信息
        share.setTitle("吹雾霾战绩分享");
        // 设置信息
        share.setText("我在吹雾霾小游戏中击败了" + blow_result_percentum.getText().toString() + "的环保卫士，" +
                "快来体验！(来自@随手拍大气监控，人人都是监督员，人人都是环保员，欢迎使用随手拍 )");
        // 显示分享列表
        share.show(BlowActivity.this);
    }

    protected void recoverView() {
        blow_begin_btn.setVisibility(View.VISIBLE);
        blow_tip_tv.setVisibility(View.INVISIBLE);
        blow_begin_btn.setText("再来一次");
        firstGet = true;
    }

    private void initEvent() {
        //禁止srcollview滚动
        blow_scrollview.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
		});
        blow_begin_btn.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View arg0) {
                initAnim();
                sum = (double) 0;

                blow_scrollview.post(new Runnable() {
                    @Override
                    public void run() {
                        blow_scrollview.scrollTo(0, bg_height);
                        scroll_height = bg_height;
                    }
                });

                blow_result_tv.setVisibility(View.INVISIBLE);
                blow_result_LL.setVisibility(View.INVISIBLE);
                blow_result_num.setVisibility(View.INVISIBLE);

                blow_begin_btn.setVisibility(View.INVISIBLE);
                blow_tip_tv.setVisibility(View.VISIBLE);

                thread = new thread.RecordThread(mhandler);
                thread.start();
            }
        });
    }

    private void initAnim() {
        alphaAnimation = new TranslateAnimation(0, 0, 0f, 50f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        blow_haze_iv.setAnimation(alphaAnimation);
        alphaAnimation.start();

    }

    private void init() {
        blow_scrollview = (ScrollView) findViewById(R.id.blow_scrollview);
        blow_bg_iv = (ImageView) findViewById(R.id.blow_bg_iv);
        blow_scrollview.post(new Runnable() {
            @Override
            public void run() {
                bg_height = blow_bg_iv.getMeasuredHeight();
                blow_scrollview.scrollTo(0, bg_height);
                scroll_height = bg_height;
            }
        });

        blow_haze_iv = (ImageView) findViewById(R.id.blow_haze_iv);

        blow_begin_btn = (Button) findViewById(R.id.blow_begin_btn);

        blow_tip_tv = (TextView) findViewById(R.id.blow_tip_tv);

        blow_begin_btn.setVisibility(View.VISIBLE);
        blow_tip_tv.setVisibility(View.INVISIBLE);
        blow_begin_btn.setText("开始吹");
        //提示文字部分
        blow_result_tv = (TextView) findViewById(R.id.blow_result_tv);
        blow_result_num = (TextView) findViewById(R.id.blow_result_num);
        blow_result_LL = (LinearLayout) findViewById(R.id.blow_result_LL);
        blow_result_percentum = (TextView) findViewById(R.id.blow_result_percentum);

        blow_result_tv.setVisibility(View.INVISIBLE);
        blow_result_LL.setVisibility(View.INVISIBLE);
        blow_result_num.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void onPause() {
        super.onPause();
        //thread.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //thread.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //thread.start();
    }

}
