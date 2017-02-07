package com.ramo.air.uicontrols;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ramo.air.R;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("NewApi")
public class TopBar extends RelativeLayout {

    private Button leftBtn, rightBtn = null;
    private TextView titleText = null;

    private LayoutParams leftParams, rightParams = null;

    private int leftTextColor;
    private Drawable leftBG;
    private String leftText;

    private int rightTextColor;
    private Drawable rightBG;
    private String rightText;

    private float titleTextSize;
    private int titleTextColor;
    private String title;
    private Drawable BG;

    private TopBarClickListener listener;

    public interface TopBarClickListener {
        public void leftClick();

        public void RightClick();
    }

    public void setOnTopBarClickListener(TopBarClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NewApi")
    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.topBar);

        leftTextColor = type.getColor(R.styleable.topBar_leftTextColor, 0);
        leftBG = type.getDrawable(R.styleable.topBar_leftBG);
        leftText = type.getString(R.styleable.topBar_leftText);

        rightTextColor = type.getColor(R.styleable.topBar_rightTextColor, 0);
        rightBG = type.getDrawable(R.styleable.topBar_rightBG);
        rightText = type.getString(R.styleable.topBar_rightText);

        titleTextColor = type.getColor(R.styleable.topBar_titleTextColors, 0);
        titleTextSize = type.getDimension(R.styleable.topBar_titleSize, 0);
        title = type.getString(R.styleable.topBar_titleStr);

        BG = type.getDrawable(R.styleable.topBar_BG);
        type.recycle();

        leftBtn = new Button(context);
        rightBtn = new Button(context);
        titleText = new TextView(context);

        leftBtn.setText(leftText);
        leftBtn.setTextColor(leftTextColor);
        leftBtn.setBackground(leftBG);

        rightBtn.setText(rightText);
        rightBtn.setTextColor(rightTextColor);
        rightBtn.setBackground(rightBG);

        titleText.setText(title);
        titleText.setTextSize(titleTextSize);
        titleText.setTextColor(titleTextColor);
        titleText.setGravity(Gravity.CENTER);

        setBackground(BG);

        leftParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rightParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, TRUE);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, TRUE);

        addView(leftBtn, leftParams);
        addView(rightBtn, rightParams);

        leftBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (listener != null)
                    listener.leftClick();
            }
        });

        rightBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (listener != null)
                    listener.RightClick();
            }
        });
    }

    public void setLeftBG(int resId) {
        leftBtn.setBackgroundResource(resId);
    }

    public void setRightBG(int resId) {
        rightBtn.setBackgroundResource(resId);
    }
}
