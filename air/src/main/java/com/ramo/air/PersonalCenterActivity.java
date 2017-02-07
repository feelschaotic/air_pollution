package com.ramo.air;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.ramo.air.databinding.PersonalBinding;
import com.ramo.air.utils.DataCleanManager;


public class PersonalCenterActivity extends Activity {
    private PersonalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.personal);
        binding.setPresenter(new Presenter());
        initData();
    }

    private void initData() {
        try {
            binding.cacheSize.setText(
                    DataCleanManager.getCacheSize(this.getCacheDir())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class Presenter {
        public void onCleanCacheLLClick(View v) {
            new AlertDialog.Builder(PersonalCenterActivity.this).setTitle("清空提示")//设置对话框标题

                    .setMessage("确定要清除缓存吗？重新加载将会消耗流量")//设置显示的内容
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                        @Override

                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                            DataCleanManager.cleanInternalCache(PersonalCenterActivity.this);
                            initData();
                        }

                    }).setNegativeButton("再想想", new DialogInterface.OnClickListener() {//添加返回按钮
                @Override

                public void onClick(DialogInterface dialog, int which) {//响应事件
                    finish();
                }

            }).show();//在按键响应事件中显示此对话框
        }

        public void onNewsLLClick(View arg0) {
            startActivity(new Intent(PersonalCenterActivity.this, EPInformationActivity.class));

        }

        public void onBlowLLClick(View arg0) {
            startActivity(new Intent(PersonalCenterActivity.this, BlowActivity.class));
        }

        public void scanLLClick(View arg0) {
            startActivity(new Intent(PersonalCenterActivity.this, ScanBeforeActivity.class));
        }

        public void onPushSetLLClick(View arg0) {
            startActivity(new Intent(PersonalCenterActivity.this, jpushdemo.SettingActivity.class));
        }

        public void onOtherSetLLClick(View arg0) {
            startActivity(new Intent(PersonalCenterActivity.this, OtherSetActivity.class));
        }
    }
}
