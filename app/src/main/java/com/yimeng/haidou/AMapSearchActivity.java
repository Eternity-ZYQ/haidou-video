package com.yimeng.haidou;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.gaodeLBS.GeoCoderUtil;
import com.yimeng.widget.MyToolBar;
import com.huige.library.utils.KeyboardUtils;
import com.huige.library.utils.StatusBarUtil;
import com.huige.library.utils.ToastUtils;
import com.yanzhenjie.permission.Action;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018-12-27 18:39:48
 *  Email  : zhihuiemail@163.com
 *  Desc   : 地图搜索位置
 */
public class AMapSearchActivity extends Activity implements LocationSource, AMapLocationListener {


    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.mapView)
    MapView mapView;
    @Bind(R.id.map_search_address_et)
    EditText mapSearchAddressEt;
    OnLocationChangedListener mListener;
    float startX, startY;
    private AMapLocationClient mlocationClient;
    private AMap mAMap;
    // 定位信息
    private String locationProvince, locationCity, locationArea, locationDesc, lon, lat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtils.getInstance().addActivity(this);
        StatusBarUtil.setStatusBarColor(this, Color.TRANSPARENT);
        StatusBarUtil.StatusBarLightMode(this, true);
        setContentView(R.layout.activity_amap_search);
        ButterKnife.bind(this);

        mapView.onCreate(savedInstanceState);
        CommonUtils.getPermission(this, new Action<List<String>>() {
            @Override
            public void onAction(List<String> data) {
                init();
                initListener();
            }
        }, new Action<List<String>>() {
            @Override
            public void onAction(List<String> data) {
                finish();
            }
        }, Manifest.permission.ACCESS_FINE_LOCATION);

    }

    protected void init() {
        mAMap = mapView.getMap();
        if (mAMap != null) {
            // 缩放级别(3~19), 设置最大级别
            mAMap.moveCamera(CameraUpdateFactory.zoomTo(mAMap.getMaxZoomLevel()));

            MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)//定位一次，且将视角移动到地图中心点。
                    .strokeColor(Color.TRANSPARENT) // 边框色
                    .radiusFillColor(Color.TRANSPARENT)// 填充色
                    .interval(1000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
            mAMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
            //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
            mAMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

            // 设置定位监听
            mAMap.setLocationSource(this);
            // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            mAMap.setMyLocationEnabled(true);
            // 显示比例尺
            mAMap.getUiSettings().setScaleControlsEnabled(true);
        }
    }

    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick(){
            @Override
            public void onRightClick() {
                Intent intent = new Intent();
                intent.putExtra("locationProvince",locationProvince);
                intent.putExtra("locationCity",locationCity);
                intent.putExtra("locationArea",locationArea);
                intent.putExtra("locationDesc",locationDesc);
                intent.putExtra("lon",lon);
                intent.putExtra("lat",lat);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        if (mAMap != null) {
//            mAMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
//                @Override
//                public void onCameraChange(CameraPosition cameraPosition) {
//
//                }
//
//                @Override
//                public void onCameraChangeFinish(CameraPosition cameraPosition) {
//                    GeoCoderUtil.getInstance(AMapSearchActivity.this).geoAddress(new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude), new GeoCoderUtil.GeoCoderAddressListener() {
//                        @Override
//                        public void onAddressResult(String result) {
//                            ToastUtils.showToast(result);
//                        }
//                    });
//                }
//            });
            mAMap.setOnMapClickListener(new AMap.OnMapClickListener() {
                @Override
                public void onMapClick(final LatLng latLng) {
                    mAMap.clear();
                    View markerView = LayoutInflater.from(AMapSearchActivity.this).inflate(R.layout.amap_marker, mapView, false);
                    mAMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.fromView(markerView))
                    );
                    GeoCoderUtil.getInstance(AMapSearchActivity.this).geoAddress(latLng, new GeoCoderUtil.GeoCoderAddressListener() {
                        @Override
                        public void onAddressResult(RegeocodeAddress regeocodeAddress, String result) {
                            mapSearchAddressEt.setText(result);
                            locationProvince = regeocodeAddress.getProvince();
                            locationCity = regeocodeAddress.getCity();
                            locationArea = regeocodeAddress.getDistrict();
                            locationDesc = regeocodeAddress.getTownship()
                                    + regeocodeAddress.getStreetNumber().getStreet();
                            if (regeocodeAddress.getAois().size() > 0) {
                                locationDesc += regeocodeAddress.getAois().get(0).getAoiName();
                            }
                            lon = latLng.longitude + "";
                            lat = latLng.latitude + "";
                        }
                    });
                }
            });
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            CommonUtils.hideKeyboard(ev, view);
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 搜索位置
     */
    @OnClick(R.id.map_search_tv)
    public void onViewClicked(View v) {
        String key = mapSearchAddressEt.getText().toString().trim();
        if (TextUtils.isEmpty(key)) {
            return;
        }
        KeyboardUtils.hideKeyBoard(v);

        PoiSearch.Query query = new PoiSearch.Query(key, "", "深圳");
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(0);//设置查询页码
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.searchPOIAsyn();


        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int rCode) {
                Log.d("msg", "AMapSearchActivity -> onPoiSearched: " + poiResult.toString());
                if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (poiResult != null && poiResult.getQuery() != null) {
                        if (poiResult.getPois().size() > 0) {
                            PoiItem poiItem = poiResult.getPois().get(0);
                            LatLonPoint latLonPoint = poiItem.getLatLonPoint();
                            LatLng latLng = new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
                            if (mAMap != null) {
                                mAMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mAMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                            }
                        } else {
                            ToastUtils.showToast("未搜索到相应位置");
                        }
                    } else {
                        ToastUtils.showToast("未搜索到相应位置");
                    }
                } else {
                    ToastUtils.showToast("未搜索到相应位置");
                }

            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {
                Log.d("msg", "AMapSearchActivity -> onPoiItemSearched: " + poiItem.toString());
            }
        });
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            //初始化定位参数
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    /**
     * 定位成功
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if(isLocation)return;
        isLocation = true;
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            mListener.onLocationChanged(aMapLocation);
            mapSearchAddressEt.setText(aMapLocation.getAddress());
            locationProvince = aMapLocation.getProvince();
            locationCity = aMapLocation.getCity();
            locationArea = aMapLocation.getDistrict();
            locationDesc = aMapLocation.getDescription();
            lon = aMapLocation.getLongitude() + "";
            lat = aMapLocation.getLatitude() + "";
        } else {
            ToastUtils.showToast("定位失败!");
        }
    }
    private boolean isLocation;
}
