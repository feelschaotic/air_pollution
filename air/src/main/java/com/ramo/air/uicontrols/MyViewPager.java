package com.ramo.air.uicontrols;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.ramo.air.utils.L;

/**
 * Created by ramo on 2017/1/24.
 */
public class MyViewPager extends ViewPager {
    private int mLastX = 0;
    private int mLastY = 0;
    public static boolean isLineTouched = false;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return super.onInterceptTouchEvent(ev);
            case MotionEvent.ACTION_UP:
                return super.onInterceptTouchEvent(ev);
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                if (!isLineTouched&&Math.abs(deltaX) > Math.abs(deltaY)) {
                    intercepted = true;
                    return super.onInterceptTouchEvent(ev);

                } else {
                    intercepted = false;
                }
                break;
        }
        mLastX = x;
        mLastY = y;
        L.e("intercepted is " + intercepted);
        isLineTouched = false;

        return intercepted;
    }
}
