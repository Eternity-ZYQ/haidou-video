package com.yimeng.utils.baiduLBS;

import android.Manifest;
import android.app.Activity;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.List;

/**
 * Author : huiGer
 * Time   : 2018/7/30 0030 上午 11:59.
 * Desc   : 百度定位
 */
public class LocationBaiDuUtil {

    private static LocationBaiDuUtil mLocationUtil = null;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private LocationBaiDuCallBack callBack;

    private LocationBaiDuUtil() {

    }

    public static LocationBaiDuUtil getInstance() {
        if (mLocationUtil == null) {
            synchronized (LocationBaiDuUtil.class) {
                if (mLocationUtil == null) {
                    mLocationUtil = new LocationBaiDuUtil();
                }
            }
        }
        return mLocationUtil;
    }

    public LocationBaiDuUtil init(Activity activity, LocationBaiDuCallBack callBack) {
        Log.d("msg", "LocationBaiDuUtil -> init: " + "");
        this.callBack = callBack;
        //检查是否开启权限！
        if (!AndPermission.hasPermissions(activity, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            getPermission(activity);
        } else {
            mLocationClient = new LocationClient(activity);
            //声明LocationClient类
            mLocationClient.registerLocationListener(myListener);

            LocationClientOption option = new LocationClientOption();

            //可选，是否需要地址信息，默认为不需要，即参数为false
            //如果开发者需要获得当前点的地址信息，此处必须为true
            option.setIsNeedAddress(true);

            //mLocationClient为第二步初始化过的LocationClient对象
            //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
            //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
            mLocationClient.setLocOption(option);
        }
        return this;
    }

    /**
     * 获取权限
     * @param activity
     */
    private void getPermission(final Activity activity) {
        AndPermission.with(activity)
                .runtime()
                .permission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        init(activity, callBack);
                    }
                })
                .start();
    }

    public void startLocation() {
        if (mLocationClient != null)
            mLocationClient.start();
    }

    public void stopLocation() {
        if (mLocationClient != null)
            mLocationClient.stop();
    }


    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息

            Log.d("msg", "MyLocationListener -> onReceiveLocation: " + city);
            callBack.address(location);
            stopLocation();
        }
    }
}
