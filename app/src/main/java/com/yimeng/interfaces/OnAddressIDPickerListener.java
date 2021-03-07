package com.yimeng.interfaces;

import android.view.View;

import com.yimeng.entity.ProvinceBean;


/**
 * Author : huiGer
 * Time   : 2018/9/17 0017 下午 04:25.
 * Desc   : 地址选择
 */
public interface OnAddressIDPickerListener {
    /**
     * 选择回调
     * @param v
     * @param province  省
     * @param city      市
     * @param area      区
     */
    void onResult(View v, ProvinceBean province, ProvinceBean.CitysBean city, ProvinceBean.CitysBean.DistrictsBean area);
}
