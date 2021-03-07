package com.yimeng.utils

import android.widget.ImageView
import com.yimeng.base.BaseActivity
import com.yimeng.base.BaseFragment
import com.yimeng.config.XJConfig
import com.yimeng.haidou.R
import com.huige.library.utils.ToastUtils
import com.scwang.smartrefresh.layout.SmartRefreshLayout

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/5/8 6:01 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */

fun BaseActivity.showToast(msg: String) {
    ToastUtils.showToast(msg)
}

fun BaseFragment.showToast(msg: String) {
    ToastUtils.showToast(msg)
}


fun ImageView.displayUrl(url: String?) {
    if (url == null || url.isEmpty() || url == "url") {
        setImageResource(R.mipmap.xj_logo)
    } else {
        CommonUtils.showImage(this, url)
    }
}

fun SmartRefreshLayout.finish(status: Boolean, page: Int, dataSize: Int) {
    if (page == 1) {
        finishRefresh(status)
    } else {
        finishLoadMore(status)
    }
    if (dataSize >= 0 && dataSize < XJConfig.LIMIT) {
        finishLoadMoreWithNoMoreData()
    }
}

fun String.formatMonbile(): String {
    if (length != 11) return this

    return substring(0, 3) + "****" + substring(7, 11)
}
