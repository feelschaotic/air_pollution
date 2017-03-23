package com.ramo.air;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ramo.air.application.MyApplication;
import com.ramo.air.bean.AirQuality;
import com.ramo.air.bean.City;
import com.ramo.air.db.CityProvider;
import com.ramo.air.uicontrols.DragSortGridView;
import com.ramo.air.utils.L;
import com.ramo.air.utils.NetUtils;

import java.util.List;

/**
 * Created by ramo on 2017/3/11.
 */
public class CityManagerActivity extends CityBaseActivity implements
        View.OnClickListener {
    public static final int MAX_CITY_NUM = 9;
    private DragSortGridView mGridView;
    private CityGridAdapter mAdapter;
    private ImageView mBackBtn, mRefreshCityBtn, mDividerLine, mEditCityBtn,
            mConfirmCityBtn;
    private ProgressBar mRefreshProgressBar;
    public static List<City> mTmpCitys;
    public static boolean isRefreshMode;

    private LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager_layout);
        mInflater = LayoutInflater.from(this);
        initDatas();
        initViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateRefreshMode(false);// 暂停时更新刷新模式
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initDatas() {
        mTmpCitys = getTmpCities();
    }

    private void initViews() {
        mGridView = (DragSortGridView) findViewById(R.id.my_city);

        mAdapter = new CityGridAdapter();
        mGridView.setAdapter(mAdapter);
        mGridView.setOnReorderingListener(dragSortListener);

        mBackBtn = (ImageView) findViewById(R.id.back_image);
        mRefreshCityBtn = (ImageView) findViewById(R.id.refresh_city);
        mDividerLine = (ImageView) findViewById(R.id.divider_line);
        mEditCityBtn = (ImageView) findViewById(R.id.edit_city);
        mConfirmCityBtn = (ImageView) findViewById(R.id.confirm_city);
        mRefreshProgressBar = (ProgressBar) findViewById(R.id.refresh_progress);

        mBackBtn.setOnClickListener(this);
        mRefreshCityBtn.setOnClickListener(this);
        mEditCityBtn.setOnClickListener(this);
        mConfirmCityBtn.setOnClickListener(this);
        mRefreshProgressBar.setOnClickListener(this);
        updateBtnStates();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_image:
                finish();
                break;
            case R.id.refresh_city:// 开始刷新
                updateRefreshMode(true);
                break;
            case R.id.refresh_progress:// 取消刷新
                updateRefreshMode(false);
                break;
            case R.id.edit_city:
            case R.id.confirm_city:
                changeEditMode();
                break;
            default:
                break;
        }
    }

    private void updateRefreshMode(boolean isRefresh) {
        if (isRefresh && NetUtils.getNetworkState(this) == NetUtils.NETWORN_NONE) {
            Toast.makeText(this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
            return;
        }
        isRefreshMode = isRefresh;
        mRefreshProgressBar.setVisibility(isRefresh ? View.VISIBLE
                : View.INVISIBLE);
        mRefreshCityBtn
                .setVisibility(isRefresh ? View.INVISIBLE : View.VISIBLE);
        mEditCityBtn.setVisibility(isRefresh ? View.INVISIBLE : View.VISIBLE);
        mEditCityBtn.setEnabled(!isRefresh && (mTmpCitys.size() > 1));
        mGridView.setEnabled(!isRefresh);
        mGridView.setOnReorderingListener(isRefresh ? null : dragSortListener);
        // 开一个异步线程去更新天气或者取消更新


    }

    private DragSortGridView.OnReorderingListener dragSortListener = new DragSortGridView.OnReorderingListener() {

        public void onReordering(int fromPosition, int toPosition) {
            mAdapter.reorder(fromPosition, toPosition);

            // 暂时使用全部删除再插入的办法，效率肯定时有影响了
            mContentResolver.delete(CityProvider.TMPCITY_CONTENT_URI, null,
                    null);
            for (City c : mTmpCitys) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(CityProvider.CityConstants.NAME, c.getName());
                contentValues.put(CityProvider.CityConstants.POST_ID, c.getPostID());
                contentValues.put(CityProvider.CityConstants.REFRESH_TIME,
                        c.getRefreshTime());
                contentValues.put(CityProvider.CityConstants.ISLOCATION,
                        c.getIsLocation() ? 1 : 0);
                mContentResolver.insert(CityProvider.TMPCITY_CONTENT_URI,
                        contentValues);
            }

            // 主键不允许修改，暂时保留。
            // String fromPostID = mAdapter.getItem(fromPosition).getPostID();
            // ContentValues idContentValues = new ContentValues();
            // idContentValues.put(CityConstants.ID, toPosition);
            // int result =
            // mContentResolver.update(CityProvider.TMPCITY_CONTENT_URI,
            // idContentValues, CityConstants.POST_ID + "=?",
            // new String[] { fromPostID });//更新位置
            // L.i("juheCity", "result = " + result);
        }

        public void beginRecordering(AdapterView<?> parent, View view,
                                     int position, long id) {
            if (mAdapter.isEditMode)
                return;
            changeEditMode();
        }

    };


    private void changeEditMode() {
        mAdapter.changeEditMode();
        if (mAdapter.isEditMode) {
            mConfirmCityBtn.setVisibility(View.VISIBLE);
            mRefreshCityBtn.setVisibility(View.INVISIBLE);
            mDividerLine.setVisibility(View.INVISIBLE);
            mEditCityBtn.setVisibility(View.INVISIBLE);
        } else {
            mConfirmCityBtn.setVisibility(View.INVISIBLE);
            if (mRefreshProgressBar.getVisibility() != View.VISIBLE)
                mRefreshCityBtn.setVisibility(View.VISIBLE);
            mDividerLine.setVisibility(View.VISIBLE);
            mEditCityBtn.setVisibility(View.VISIBLE);
        }
        updateBtnStates();
    }

    private void deleteCityFromTable(int position) {
        City city = mAdapter.getItem(position);
        // 从全局变量中删除
        MyApplication.mMainMap.remove(city);
        // 从临时城市表中删除
        mContentResolver
                .delete(CityProvider.TMPCITY_CONTENT_URI, CityProvider.CityConstants.POST_ID
                        + "=?", new String[]{city.getPostID()});
        ContentValues contentValues = new ContentValues();

        // 更新已选择的热门城市表
        contentValues.put(CityProvider.CityConstants.ISSELECTED, 0);
        mContentResolver.update(CityProvider.HOTCITY_CONTENT_URI,
                contentValues, CityProvider.CityConstants.POST_ID + "=?",
                new String[]{city.getPostID()});
        updateUI(false);
        if (mTmpCitys.isEmpty())// 如果全部被删除完了，更新一下编辑状态
            changeEditMode();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                updateUI(true);
                City city = (City) data
                        .getParcelableExtra(CityQueryActivity.CITY_EXTRA_KEY);
                if (city == null)
                    return;
                if (NetUtils.getNetworkState(this) == NetUtils.NETWORN_NONE) {
                    Toast.makeText(this, "网络异常，请稍后重试", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
            }

        }
    }

    private void updateUI(boolean isAdd) {
        mTmpCitys = getTmpCities();
        if (isAdd && mTmpCitys.size() >= MAX_CITY_NUM)
            Toast.makeText(this, "最多只能添加9个城市!", Toast.LENGTH_LONG)
                    .show();
        mAdapter.notifyDataSetChanged();
        updateBtnStates();
    }

    /**
     * 更新ActionBar按钮状态
     */
    private void updateBtnStates() {
        mEditCityBtn.setEnabled(mTmpCitys.size() > 1);
        mRefreshCityBtn.setEnabled(mTmpCitys.size() > 1);
        mRefreshProgressBar.setEnabled(mTmpCitys.size() > 1);
    }


    private City getNewCity(City city) {
        Cursor c = mContentResolver.query(CityProvider.TMPCITY_CONTENT_URI,
                null, CityProvider.CityConstants.POST_ID + "=?",
                new String[]{city.getPostID()}, null);
        if (c.moveToFirst()) {
            String name = c.getString(c.getColumnIndex(CityProvider.CityConstants.NAME));
            String postID = c
                    .getString(c.getColumnIndex(CityProvider.CityConstants.POST_ID));
            long refreshTime = c.getLong(c
                    .getColumnIndex(CityProvider.CityConstants.REFRESH_TIME));
            int isLocation = c.getInt(c
                    .getColumnIndex(CityProvider.CityConstants.ISLOCATION));
            City item = new City(name, postID, refreshTime, isLocation);
            c.close();
            return item;
        }
        return null;
    }


    private class CityGridAdapter extends BaseAdapter {
        public static final int NORMAL_CITY_TYPE = 0;
        public static final int ADD_CITY_TYPE = 1;
        private int refreshingIndex = -1;
        private boolean isEditMode;

        public CityGridAdapter() {
            if (mTmpCitys.size() < MAX_CITY_NUM)
                mTmpCitys.add(null);
        }

        public void setRefreshingIndex(int position) {
            refreshingIndex = position;
            L.i("CityGridAdapter setRefreshingIndex = " + position);
            notifyDataSetChanged();
        }

        public boolean isEditMode() {
            return isEditMode;
        }

        @Override
        public void notifyDataSetChanged() {
            int lastPosition = ((getCount() - 1) < 0) ? 0 : (getCount() - 1);
            if (isEditMode) {
                if (!mTmpCitys.isEmpty() && mTmpCitys.get(lastPosition) == null)// 如果最后一个是空,则编辑模式下移出
                    mTmpCitys.remove(lastPosition);
            } else {
                if (mTmpCitys.isEmpty()
                        || ((mTmpCitys.get(lastPosition) != null) && (getCount() < MAX_CITY_NUM)))// 如果最后一个不为空，并且数量小于9个，则添加一个
                    mTmpCitys.add(null);
            }
            super.notifyDataSetChanged();
        }

        public void changeEditMode() {
            isEditMode = !isEditMode;

            notifyDataSetChanged();
        }

        public void reorder(int from, int to) {
            if (from != to) {
                City oldCity = mTmpCitys.get(from);
                mTmpCitys.remove(from);
                mTmpCitys.add(to, oldCity);

                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return mTmpCitys.size();
        }

        @Override
        public City getItem(int position) {
            return mTmpCitys.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            // return super.getItemViewType(position);
            if (getItem(position) == null)
                return ADD_CITY_TYPE;
            return NORMAL_CITY_TYPE;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            int type = getItemViewType(position);
            if (convertView == null
                    || convertView.getTag(R.mipmap.ic_launcher + type) == null) {
                switch (type) {
                    case NORMAL_CITY_TYPE:
                        convertView = mInflater.inflate(
                                R.layout.activity_city_manger_grid_item_normal, parent,
                                false);
                        break;
                    case ADD_CITY_TYPE:
                        convertView = mInflater.inflate(
                                R.layout.activity_city_manger_grid_item_add, parent, false);
                        break;
                    default:
                        break;
                }
                viewHolder = buildHolder(convertView);
                // 因为类型不同，所以给viewHolder设置一个标识,标识必须是资源id，不然会挂掉
                // 我这里为了区分不同的type，所以加上类型
                convertView.setTag(R.mipmap.ic_launcher + type, viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView
                        .getTag(R.mipmap.ic_launcher + type);
            }
            bindViewData(viewHolder, position);
            return convertView;
        }

        AirQuality weatherInfo = null;

        private void bindViewData(ViewHolder holder, final int position) {
            City city = mTmpCitys.get(position);
            if (MyApplication.mMainMap != null && !MyApplication.mMainMap.isEmpty() && city != null)
                weatherInfo = MyApplication.mMainMap.get(city.getPostID());
            switch (getItemViewType(position)) {
                case NORMAL_CITY_TYPE:
                    if (refreshingIndex == position) {
                        holder.loadingBar.setVisibility(View.VISIBLE);
                        holder.weatherIV.setVisibility(View.GONE);
                        holder.tempTV.setText("加载中...");
                    } else {
                        holder.loadingBar.setVisibility(View.GONE);
                        holder.weatherIV.setVisibility(View.VISIBLE);
                        holder.weatherIV
                                .setImageResource(R.drawable.xy_weather_ic_default);
                        holder.loadingBar.setVisibility(View.GONE);
                        holder.weatherIV.setVisibility(View.VISIBLE);

                        holder.weatherIV
                                .setImageResource(R.drawable.xy_weather_ic_default);
                    }
                    holder.cityTV.setText(city.getName());
                    if (city.getIsLocation()) {
                        holder.cityTV.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.current_loc_active_26x26, 0, 0, 0);
                    } else {
                        holder.cityTV.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                                0, 0);
                    }
                    if (isEditMode && !city.getIsLocation())
                        holder.deleteIV.setVisibility(View.VISIBLE);
                    else
                        holder.deleteIV.setVisibility(View.GONE);
                    holder.deleteIV.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // 从数据库中删除城市
                            deleteCityFromTable(position);
                        }

                    });

                    holder.weatherIV.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {

                            Intent in = new Intent();
                            in.putExtra("cityName", mTmpCitys.get(position).getName()+"市");
                            in.setClass(CityManagerActivity.this, MainActivity.class);
                            startActivity(in);
                          //  finish();
                            finish();
                        }
                    });
                    break;
                case ADD_CITY_TYPE:
                    holder.addView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (isRefreshMode) {
                                Toast.makeText(CityManagerActivity.this,
                                        "请先停止刷新",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            L.i("juheCity", "addView.onClickListener...");
                            CityManagerActivity.this.startActivityForResult(
                                    (new Intent(CityManagerActivity.this,
                                            CityQueryActivity.class)), 0);
                        }
                    });

                    break;

                default:
                    break;
            }
        }

        private ViewHolder buildHolder(View convertView) {
            ViewHolder holder = new ViewHolder();
            holder.cityTV = (TextView) convertView
                    .findViewById(R.id.city_manager_name_tv);
            holder.tempTV = (TextView) convertView
                    .findViewById(R.id.city_manager_temp_tv);
            holder.weatherIV = (ImageView) convertView
                    .findViewById(R.id.city_manager_icon_iv);
            holder.deleteIV = (ImageView) convertView
                    .findViewById(R.id.city_delete_btn);
            holder.loadingBar = (ProgressBar) convertView
                    .findViewById(R.id.city_manager_progressbar);
            holder.addView = convertView;
            return holder;
        }

        private class ViewHolder {
            TextView cityTV;
            TextView tempTV;
            ImageView weatherIV;
            ProgressBar loadingBar;
            ImageView deleteIV;
            View addView;
        }

    }
}
