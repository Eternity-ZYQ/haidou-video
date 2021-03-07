package com.yimeng.utils.baiduLBS;

import com.baidu.location.BDLocation;

/**
 * Author : huiGer
 * Time   : 2018/6/26 0026 下午 04:39.
 * Desc   : 定位回调
 */
public interface LocationBaiDuCallBack {
    void address(BDLocation location);
}
