package com.yimeng.interfaces;

import android.view.View;

import com.yimeng.entity.SeedBean;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/13 2:39 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 种子点击
 * </pre>
 */
public interface SeedItemClickListener {
    void seedClick(View view, SeedBean seedBean);
}
