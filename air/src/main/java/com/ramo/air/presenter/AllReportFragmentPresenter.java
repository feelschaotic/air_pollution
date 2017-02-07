package com.ramo.air.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ramo.air.MySubmitComplaintsDetailsActivity;
import com.ramo.air.R;
import com.ramo.air.bean.Report;
import com.ramo.air.databinding.FragmentAllReportLayoutBinding;
import com.ramo.air.listener.HttpCallbackListener;
import com.ramo.air.utils.ImageManageUtil;
import com.ramo.air.utils.L;
import com.ramo.air.utils.NetUtils;
import com.ramo.air.utils.ServerUrlUtil;
import com.ramo.air.utils.T;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramo on 2016/11/25.
 */
public class AllReportFragmentPresenter {
    private Context context;
    private RelativeLayout rel;
    private LayoutInflater inflater;

    private ImageView like_img;
    private TextView location;
    private TextView content;
    private TextView time;
    private RelativeLayout re;
    private ImageView img;

    private Intent intent;
    private Bundle bundle;

    private List<Report> reportList;
    private List<Report> attentionList;
    private Gson gson;
    private Report report;
    private Bitmap bitmap;

    private FragmentAllReportLayoutBinding binding;

    public AllReportFragmentPresenter(Context context, FragmentAllReportLayoutBinding binding) {
        this.context = context;
        this.binding = binding;
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    updateReportList();
                    LightMyAttentionReporting();
                    break;
                case 0:
                    T.shortShow(context, "获取举报列表失败，请稍后重试");
                    break;

                default:
                    break;
            }
        }

    };
    private Handler lightHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                 //   LightMyAttentionReporting();
                    break;
                case 0:
                    T.shortShow(context, "获取关注列表失败，请稍后重试");
                    break;

                default:
                    break;
            }
        }

    };
    private Handler attentionHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(context, "已关注", Toast.LENGTH_SHORT)
                            .show();

                    break;
                case 0:
                    T.shortShow(context, "关注失败，请稍后重试");
                    break;

                default:
                    break;
            }
        }

    };
    private Handler cancelAttentionHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(context, "已取消关注",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    T.shortShow(context, "取消关注失败，请稍后重试");
                    break;

                default:
                    break;
            }
        }

    };
    private Handler searchHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    updateReportList();
                    break;
                case 0:
                    T.shortShow(context, "获取举报列表失败，请稍后重试");
                    break;

                default:
                    break;
            }
        }

    };

    public void getAllReportAndMyAttention() {
        NetUtils.getIOFromUrl(ServerUrlUtil.ALL_REPORT,
                new HttpCallbackListener() {
                    Message m = new Message();

                    @Override
                    public void onSucc(String response) {
                        Type listType = new TypeToken<ArrayList<Report>>() {
                        }.getType();
                        reportList = gson.fromJson(response, listType);
                        m.what = 1;
                        handler.sendMessage(m);
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        m.what = 0;
                        handler.sendMessage(m);
                    }
                });

        NetUtils.getIOFromUrl(ServerUrlUtil.MY_ATTENTION_REPORT,
                new HttpCallbackListener() {
                    Message m = new Message();

                    @Override
                    public void onSucc(String response) {
                        Type listType = new TypeToken<ArrayList<Report>>() {
                        }.getType();
                        attentionList = gson.fromJson(response, listType);
                        m.what = 1;
                        lightHandler.sendMessage(m);
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        m.what = 0;
                        lightHandler.sendMessage(m);
                    }
                });
    }

    protected void LightMyAttentionReporting() {
        if (attentionList != null)
            for (int i = 0; i < attentionList.size(); i++) {
                for (int j = 0; j < reportList.size(); j++)
                    if (attentionList.get(i).getReport_id().equals(reportList.get(j).getReport_id())) {
                        L.e("light :" + i);
                        re = (RelativeLayout) binding.submitFlowLayout.getChildAt(j);
                        rel = (RelativeLayout) re.getChildAt(1);
                        ((ImageView) rel.getChildAt(3))
                                .setImageResource(R.drawable.special_xin);
                        ((ImageView) rel.getChildAt(3))
                                .setTag(R.drawable.special_xin);
                    }
            }

    }

    public void searchReport() {
        NetUtils.getIOFromUrl(ServerUrlUtil.SEARCH_REPORT + "?keyword="
                        + binding.queryReportEt.getText().toString(),
                new HttpCallbackListener() {
                    Message m = new Message();

                    @Override
                    public void onSucc(String response) {
                        Type listType = new TypeToken<ArrayList<Report>>() {
                        }.getType();
                        reportList = gson.fromJson(response, listType);
                        m.what = 1;
                        searchHandler.sendMessage(m);
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        m.what = 0;
                        searchHandler.sendMessage(m);
                    }
                });

    }

    public void updateReportList() {
        binding.submitFlowLayout.removeAllViews();
        for (int i = 0; i < reportList.size(); i++) {
            report = reportList.get(i);
            rel = (RelativeLayout) inflater.inflate(R.layout.fragment_report_item,
                    binding.submitFlowLayout, false);
            img = (ImageView) rel.getChildAt(0);
            re = (RelativeLayout) rel.getChildAt(1);
            time = (TextView) re.getChildAt(0);
            content = (TextView) re.getChildAt(1);
            location = (TextView) re.getChildAt(2);
            like_img = (ImageView) re.getChildAt(3);
            like_img.setTag(R.drawable.special_xin_normal);
            initLikeEvent(i);
            initReportEvent(i);

            content.setText(report.getReport_content());
            time.setText(report.getReport_time());
            location.setText(report.getReport_location());
            bitmap = ImageManageUtil.StrToBitmap(report.getReport_img());
            if (bitmap != null)
                img.setImageBitmap(bitmap);
            else
                img.setImageResource(R.drawable.empty_photo);
            binding.submitFlowLayout.addView(rel);
        }
    }

    private void initReportEvent(final int pos) {
        img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                bundle.putSerializable("report", reportList.get(pos));
                intent.putExtras(bundle);
                intent.setClass(context,
                        MySubmitComplaintsDetailsActivity.class);
                context.startActivity(intent);
            }
        });
    }

    private void initLikeEvent(final int pos) {

        like_img.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                re = (RelativeLayout) binding.submitFlowLayout.getChildAt(pos);
                rel = (RelativeLayout) re.getChildAt(1);
                //	L.e((Integer)((ImageView) rel.getChildAt(3)).getTag()+"");
                if ((Integer) ((ImageView) rel.getChildAt(3)).getTag() == R.drawable.special_xin_normal) {

                    ((ImageView) rel.getChildAt(3))
                            .setImageResource(R.drawable.special_xin);
                    ((ImageView) rel.getChildAt(3))
                            .setTag(R.drawable.special_xin);

                    NetUtils.getIOFromUrl(ServerUrlUtil.ATTENTION_REPORT
                                    + "?report_id="
                                    + reportList.get(pos).getReport_id(),
                            new HttpCallbackListener() {

                                @Override
                                public void onSucc(String response) {
                                    attentionHandler.sendEmptyMessage(1);
                                }

                                @Override
                                public void onError(Exception e) {
                                    attentionHandler.sendEmptyMessage(0);
                                    e.printStackTrace();
                                }
                            });

                } else {
                    ((ImageView) rel.getChildAt(3))
                            .setImageResource(R.drawable.special_xin_normal);
                    ((ImageView) rel.getChildAt(3))
                            .setTag(R.drawable.special_xin_normal);

                    NetUtils.getIOFromUrl(ServerUrlUtil.CANCEL_ATTENTION_REPORT
                                    + "?report_id="
                                    + reportList.get(pos).getReport_id(),
                            new HttpCallbackListener() {

                                @Override
                                public void onSucc(String response) {
                                    cancelAttentionHandler.sendEmptyMessage(1);
                                }

                                @Override
                                public void onError(Exception e) {
                                    cancelAttentionHandler.sendEmptyMessage(0);
                                    e.printStackTrace();
                                }
                            });

                }

            }
        });
    }

    public void init() {
        inflater = LayoutInflater.from(context);
        intent = new Intent();
        bundle = new Bundle();
        gson = new Gson();
    }

    public void initEvent() {
        binding.queryReportEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // 进行举报 模糊查询
                searchReport();
            }

        });
    }
}
