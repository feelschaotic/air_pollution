package com.ramo.air;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ramo.air.adapter.CityQueryAdapter;
import com.ramo.air.bean.City;
import com.ramo.air.db.CityProvider;
import com.ramo.air.uicontrols.CountDownView;
import com.ramo.air.utils.HotCityUtils;
import com.ramo.air.utils.L;
import com.ramo.air.utils.LocationUtils;
import com.ramo.air.utils.PreferenceUtils;
import com.ramo.air.utils.T;

import java.util.List;

public class CityQueryActivity extends CityBaseActivity implements
        OnClickListener, TextWatcher, OnItemClickListener {
    public static final String CITY_EXTRA_KEY = "city";
    public static final String AUTO_LOCATION_CITY_KEY = "auto_location";
    protected ContentResolver mContentResolver;
    protected Activity mActivity;

    private LayoutInflater mInflater;
    private RelativeLayout mRootView;
    private CountDownView mCountDownView;
    private ImageView mBackBtn;
    private TextView mLocationTV;
    private EditText mQueryCityET;
    private ImageButton mQueryCityExitBtn;
    private ListView mQueryCityListView;
    // private TextView mEmptyCityView;
    private GridView mHotCityGridView;
    private LocationUtils mLocationUtils;
    private List<City> mTmpCitys;
    private List<City> mHotCitys;
    private List<City> mCities;
    private CityQueryAdapter mSearchCityAdapter;
    private Filter mFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_query_layout);
        mContentResolver = getContentResolver();
        initDatas();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initViews() {
        mInflater = LayoutInflater.from(this);
        mRootView = (RelativeLayout) findViewById(R.id.city_add_bg);
        mBackBtn = (ImageView) findViewById(R.id.back_image);
        mLocationTV = (TextView) findViewById(R.id.location_text);
        mQueryCityET = (EditText) findViewById(R.id.queryCityText);
        mQueryCityExitBtn = (ImageButton) findViewById(R.id.queryCityExit);

        mQueryCityListView = (ListView) findViewById(R.id.cityList);
        mQueryCityListView.setOnItemClickListener(this);
        mSearchCityAdapter = new CityQueryAdapter(CityQueryActivity.this,
                mCities);
        mQueryCityListView.setAdapter(mSearchCityAdapter);
        mQueryCityListView.setTextFilterEnabled(true);
        mFilter = mSearchCityAdapter.getFilter();

        // mEmptyCityView = (TextView) findViewById(R.id.noCityText);

        mHotCityGridView = (GridView) findViewById(R.id.hotCityGrid);
        mHotCityGridView.setOnItemClickListener(this);
        mHotCityGridView.setAdapter(new HotCityAdapter());

        //mBackBtn.setOnClickListener(this);
        mLocationTV.setOnClickListener(this);
        mQueryCityExitBtn.setOnClickListener(this);
        mQueryCityET.addTextChangedListener(this);

        String cityName = PreferenceUtils.getPrefString(this,
                AUTO_LOCATION_CITY_KEY, "");
        if (TextUtils.isEmpty(cityName)) {
            //	startLocation(mCityNameStatus);
        } else {
            mLocationTV.setText(cityName);
        }
    }

    private void initDatas() {
        Cursor cityCursor = mContentResolver.query(
                CityProvider.CITY_CONTENT_URI, null, null, null, null);
        mCities = HotCityUtils.getAllCities(cityCursor);

        Cursor hotCityCursor = mContentResolver.query(
                CityProvider.HOTCITY_CONTENT_URI, null, null, null, null);
        mHotCitys = HotCityUtils.getHotCities(hotCityCursor);
        Cursor tmpCityCursor = mContentResolver.query(
                CityProvider.TMPCITY_CONTENT_URI, null, null, null, null);
        mTmpCitys = HotCityUtils.getTmpCities(tmpCityCursor);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        switch (parent.getId()) {
            case R.id.cityList:
                City city = mSearchCityAdapter.getItem(position);
                boolean isExists = false;
                L.i("juheCity", city.getName());
                addToTmpCityTable(city);
                break;
            case R.id.hotCityGrid:
                City hotCity = mHotCitys.get(position);
                if (hotCity.getIsSelected()) {
                    Toast.makeText(this, "已经存在此城市", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                L.i("juheCity", hotCity.getName());
                addToTmpCityTable(hotCity);
                break;
            default:
                break;
        }
    }

    private void addToTmpCityTable(City city) {
        boolean isExists = false;
        for (City c : mTmpCitys)
            if (TextUtils.equals(city.getPostID(), c.getPostID())) {
                isExists = true;
                break;
            }
        if (isExists) {
            Toast.makeText(this, "已经存在此城市", Toast.LENGTH_SHORT)
                    .show();
            return;// 已经存在此城市，直接返回
        }
        // 存储
        ContentValues tmpContentValues = new ContentValues();
        tmpContentValues.put(CityProvider.CityConstants.NAME, city.getName());
        tmpContentValues.put(CityProvider.CityConstants.POST_ID, city.getPostID());
        tmpContentValues.put(CityProvider.CityConstants.REFRESH_TIME, 0L);// 无刷新时间
        tmpContentValues.put(CityProvider.CityConstants.ISLOCATION, 0);// 手动选择的城市存储为0
        mContentResolver.insert(CityProvider.TMPCITY_CONTENT_URI,
                tmpContentValues);

        // 更新热门城市表已选择
        ContentValues hotContentValues = new ContentValues();
        hotContentValues.put(CityProvider.CityConstants.ISSELECTED, 1);
        mContentResolver.update(CityProvider.HOTCITY_CONTENT_URI,
                hotContentValues, CityProvider.CityConstants.POST_ID + "=?",
                new String[]{city.getPostID()});
        Intent i = new Intent();
        i.putExtra(CITY_EXTRA_KEY, city);
        setResult(RESULT_OK, i);
        // setResult(RESULT_OK);
        finish();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_image:
                finish();
                break;
            case R.id.location_text:
                startLocation(mCityNameStatus);
                break;
            case R.id.queryCityExit:
                mQueryCityET.setText("");
                break;
            case R.id.cancel_locate_city_btn:
                T.shortShow(this, "自动定位已取消");
                stopLocation();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocation();
    }


    protected void stopLocation() {
        super.stopLocation();
        dismissCountDownView();
    }


    @Override
    public void onBackPressed() {
        if (mCountDownView != null && mCountDownView.isCountingDown()) {
            T.shortShow(this, "自动定位已取消");
            mCountDownView.cancelCountDown();
        } else {
            super.onBackPressed();
        }
    }

    public boolean enoughToFilter() {
        return mQueryCityET.getText().length() > 0;
    }

    private void doBeforeTextChanged() {
        if (mQueryCityListView.getVisibility() == View.GONE) {
            mQueryCityListView.setVisibility(View.VISIBLE);
        }
    }

    private void doAfterTextChanged() {
        if (enoughToFilter()) {
            L.i("juheCity", "onTextChanged  s = "
                    + mQueryCityET.getText().toString());
            if (mFilter != null) {
                mFilter.filter(mQueryCityET.getText().toString().trim());
            }
        } else {
            if (mQueryCityListView.getVisibility() == View.VISIBLE) {
                mQueryCityListView.setVisibility(View.GONE);
            }
            if (mFilter != null) {
                mFilter.filter(null);
            }
        }

    }

    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        doBeforeTextChanged();
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(s)) {
            mQueryCityExitBtn.setVisibility(View.GONE);
        } else {
            mQueryCityExitBtn.setVisibility(View.VISIBLE);
        }
        doAfterTextChanged();
    }

    public void afterTextChanged(Editable s) {
        // do nothing
    }

    CountDownView.OnCountDownFinishedListener countDownFinishedListener = new CountDownView.OnCountDownFinishedListener() {

        public void onCountDownFinished() {
            T.shortShow(CityQueryActivity.this, "定位失败");
            stopLocation();
        }
    };

    LocationUtils.CityNameStatus mCityNameStatus = new LocationUtils.CityNameStatus() {

        public void update(String name) {
            L.i("juheCity", name);
            dismissCountDownView();

            City city = getLocationCityFromDB(name);
            if (TextUtils.isEmpty(city.getPostID())) {
                T.shortShow(CityQueryActivity.this, "暂时没有该城市的数据！请手动选择");
            } else {
                PreferenceUtils.setPrefString(CityQueryActivity.this,
                        AUTO_LOCATION_CITY_KEY, name);
                L.i("juheCity", "location" + city.toString());
                addOrUpdateLocationCity(city);
                T.shortShow(CityQueryActivity.this, "成功");
                mLocationTV.setText(name);
            }
        }

        public void detecting() {
            showCountDownView();
        }

    };

    private void showCountDownView() {
        mInflater.inflate(R.layout.count_down_to_location, mRootView, true);
        mCountDownView = (CountDownView) mRootView
                .findViewById(R.id.count_down_to_locate);
        Button btn = (Button) mRootView
                .findViewById(R.id.cancel_locate_city_btn);
        btn.setOnClickListener(this);
        mCountDownView.setCountDownFinishedListener(countDownFinishedListener);
        mCountDownView.startCountDown(30);
    }

    private void dismissCountDownView() {
        if (mCountDownView != null && mCountDownView.isCountingDown())
            mCountDownView.cancelCountDown();
    }

    private class HotCityAdapter extends BaseAdapter {

        public int getCount() {
            return mHotCitys.size();
        }

        public Object getItem(int position) {
            return mHotCitys.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            City hotCity = (City) getItem(position);
            ViewHoler viewHoler;
            if (convertView == null) {
                convertView = mInflater.inflate(
                        R.layout.activity_city_query_hotcity_grid_item, parent, false);
                viewHoler = new ViewHoler();
                viewHoler.hotCityTV = (TextView) convertView
                        .findViewById(R.id.grid_city_name);
                viewHoler.selectedIV = (ImageView) convertView
                        .findViewById(R.id.grid_city_selected_iv);
                convertView.setTag(viewHoler);
            } else {
                viewHoler = (ViewHoler) convertView.getTag();
            }
            viewHoler.hotCityTV.setText(hotCity.getName());
            if (hotCity.getIsSelected()) {
                viewHoler.selectedIV.setVisibility(View.VISIBLE);
            } else {
                viewHoler.selectedIV.setVisibility(View.GONE);
            }

            return convertView;
        }

    }

    static class ViewHoler {
        TextView hotCityTV;
        ImageView selectedIV;
    }

}
