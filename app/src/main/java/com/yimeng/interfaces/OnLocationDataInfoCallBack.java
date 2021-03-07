package com.yimeng.interfaces;

import com.yimeng.entity.ProvinceBean;

import java.util.List;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/1/9 0009 下午 12:02.
 *  Email  : zhihuiemail@163.com
 *  Desc   : sjmm地区数据
 * </pre>
 */
public interface OnLocationDataInfoCallBack {

    void onFail(String errorMsg);

    void onSuccess(List<ProvinceBean> list);
}
