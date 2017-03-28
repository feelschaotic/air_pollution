package com.ramo.air.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ramo.air.R;
import com.ramo.air.receiver.VoiceAirService;
import com.ramo.air.uicontrols.CheckSwitchButton;
import com.ramo.air.utils.DataCleanManager;
import com.ramo.air.utils.T;


public class OtherSetAdapter extends BaseAdapter {

    private Context context;
    private String[] items;
    private LayoutInflater inflater;

    public OtherSetAdapter(Context context, String[] systemText) {
        this.context = context;
        this.items = systemText;
        this.inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return items.length;
    }

    public Object getItem(int arg0) {
        return items[arg0];
    }

    public long getItemId(int arg0) {
        return arg0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view;
        final ViewHolder holder;
        if (convertView != null) {
            view = convertView;
            holder = (ViewHolder) view.getTag(position);
        } else {

            view = inflater.inflate(R.layout.other_set_item, parent, false);
            holder = new ViewHolder();
            holder.text = (TextView) view.findViewById(R.id.other_set_text);
            holder.btn = (CheckSwitchButton) view.findViewById(R.id.other_set_switchBtn);
            holder.img = (ImageView) view.findViewById(R.id.other_set_right);

            if (position == 1 || position == 6 || position == 7) {
                holder.img.setVisibility(View.GONE);
                holder.btn.setVisibility(View.GONE);
            } else if (position >= 2 && position <= 5) {
                holder.img.setVisibility(View.GONE);
                if (position == 3)
                    holder.btn.setEnabled(false);
                if(position==5)
                    holder.btn.setChecked(true);
            } else {
                holder.btn.setVisibility(View.GONE);
            }
            view.setTag(holder);
        }

        if (holder != null) {
            holder.text.setText(items[position]);
            initEvent(position, holder);
        }


        return view;
    }

    private void initEvent(final int position, final ViewHolder holder) {
        initTextEvent(position, holder);
        initBtnEvent(position, holder);
    }

    private void initBtnEvent(final int position, final ViewHolder holder) {
        holder.btn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (position == 2) {
                    if (holder.btn.isChecked() == false) {
                        Intent intent = new Intent(context, VoiceAirService.class);
                        context.stopService(intent);
                        T.shortShow(context, "已关闭");
                        ((ViewGroup) buttonView.getParent().getParent()).getChildAt(3).findViewById(R.id.other_set_switchBtn).setEnabled(false);
                    } else {
                        //启动语音播报的广播
                        Intent intent = new Intent(context, VoiceAirService.class);
                        context.startService(intent);
                        T.shortShow(context, "已开启");
                        ((ViewGroup) buttonView.getParent().getParent()).getChildAt(3).findViewById(R.id.other_set_switchBtn).setEnabled(true);
                    }
                }
            }
        });
    }

    private void initTextEvent(final int position, ViewHolder holder) {
        holder.text.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                switch (position) {
                    case 0:
                        context.startActivity(new Intent(context, jpushdemo.SettingActivity.class));
                        break;
                    case 1:
                        clearCache();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void clearCache() {
        try {
            String cacheSize = DataCleanManager.getCacheSize(context.getCacheDir());
            new AlertDialog.Builder(context).setTitle("清空提示")//设置对话框标题

                    .setMessage("确定要清除缓存吗？缓存大小：" + cacheSize)//设置显示的内容
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                        @Override

                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                            DataCleanManager.cleanInternalCache(context);
                            T.shortShow(context, "清除完成");
                        }

                    }).setNegativeButton("再想想", new DialogInterface.OnClickListener() {//添加返回按钮
                @Override

                public void onClick(DialogInterface dialog, int which) {//响应事件

                }

            }).show();//在按键响应事件中显示此对话框

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ViewHolder {
        TextView text;
        CheckSwitchButton btn;
        ImageView img;
    }

}
