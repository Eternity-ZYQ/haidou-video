package com.yimeng.interfaces;

import android.view.View;

/**
 * Author : huiGer
 * Time   : 2018/9/17 0017 下午 04:25.
 * Desc   : 地址选择
 */
public interface OnAddressPickerListener {
    /**
     * 选择回调
     * @param v
     * @param province  省
     * @param city      市
     * @param area      区
     */
    void onResult(View v, String province, String city, String area);
}
