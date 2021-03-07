package com.yimeng.interfaces;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-05 22:32.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public interface OnViewPagerListener {
    /**
     * 初始化完成
     */
    void onInitComplete();

    /**
     * 释放的监听
     * @param isNext
     * @param position
     */
    void onPageRelease(boolean isNext, int position);

    /**
     * 选中的监听以及判断是否滑动到底部
     * @param position
     * @param isBottom
     */
    void onPageSelected(int position, boolean isBottom);
}
