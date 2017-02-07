package com.ramo.air.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ramo.air.R;
import com.ramo.air.databinding.FragmentAllReportLayoutBinding;
import com.ramo.air.presenter.AllReportFragmentPresenter;


public class AllReportFragment extends Fragment {

    private EditText query_report_et;

    private FragmentAllReportLayoutBinding binding;
    private AllReportFragmentPresenter fragmentPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_report_layout, container, false);
        init();
        initData();
        initEvent();
        return binding.getRoot();
    }

    private void init() {
        fragmentPresenter=new AllReportFragmentPresenter(getContext(),binding);
        fragmentPresenter.init();
    }

    private void initData() {
        fragmentPresenter.getAllReportAndMyAttention();
    }
    private void initEvent() {
        fragmentPresenter.initEvent();
        binding.layout4ScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_MOVE:

                        if (binding.layout4ScrollView.getChildAt(0).getMeasuredHeight() <= v.getHeight() + v.getScrollY()) {
                            // L.d("scroll view", "bottom");
                            //滑到底部 开始加载下一页
                        }
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
    }



}
