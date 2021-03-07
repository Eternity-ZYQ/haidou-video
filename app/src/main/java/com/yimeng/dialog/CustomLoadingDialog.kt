package com.yimeng.dialog

import android.content.Context
import android.graphics.Color
import android.view.View
import com.yimeng.haidou.R
import com.huige.library.utils.DeviceUtils
import com.lxj.xpopup.core.CenterPopupView
import com.noober.background.drawable.DrawableCreator
import kotlinx.android.synthetic.main.dialog_loading.view.*

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/6 6:56 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 加载窗
 * </pre>
 */
class CustomLoadingDialog(context: Context, title: String?, loadingIsShow:Boolean, hasRect: Boolean) : CenterPopupView(context) {

    // 显示黑背景
    private var mHasRect: Boolean = hasRect
    private var mTitle = title
    // 显示加载圈
    private var mLoadingIsShow = loadingIsShow


    override fun getImplLayoutId(): Int = R.layout.dialog_loading

    override fun onCreate() {
        super.onCreate()

        if (mHasRect) {
            ll_content.background = DrawableCreator.Builder()
                    .setCornersRadius(DeviceUtils.dp2px(context, 5f).toFloat())
                    .setSolidColor(Color.parseColor("#333333"))
                    .build()
            tv_title.setTextColor(Color.WHITE)
        }

        setLoadingMsg(mTitle)

        startAnim()
    }

    /**
     * 加载文字
     */
    fun setLoadingMsg(title: String?): CustomLoadingDialog {
        if (title.isNullOrEmpty()) {
            tv_title?.visibility = View.GONE
        } else {
            tv_title?.apply {
                visibility = View.VISIBLE
                text = title
            }
        }
        return this
    }

    fun startAnim(): CustomLoadingDialog {
        if(mLoadingIsShow) {
            loadingView?.visibility = View.VISIBLE
        }
        return this
    }

}