package com.yimeng.dialog

import android.content.Context
import com.yimeng.haidou.R
import com.huige.library.utils.ToastUtils
import com.lxj.xpopup.core.CenterPopupView
import com.lxj.xpopup.util.XPopupUtils
import kotlinx.android.synthetic.main.dialog_change_caijing.view.*

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-28 23:41.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
class ChangeCaiJinDialog(context: Context, block: (String) -> Unit) : CenterPopupView(context) {

    private val mResult = block
    override fun getImplLayoutId(): Int = R.layout.dialog_change_caijing

    override fun onCreate() {
        tv_cancel?.setOnClickListener { dismiss() }

        tv_submit.setOnClickListener {
            val input = et_input.text.toString().trim()

            if (input.isEmpty()) {
                ToastUtils.showToast("请输入要置换的彩蛋数量")
                return@setOnClickListener
            }

            if(input.toInt() <= 0) {
                ToastUtils.showToast("请输入正确数量")
                return@setOnClickListener
            }

            dismissWith { mResult(input) }
        }
    }

    override fun getMaxWidth(): Int {
        return (XPopupUtils.getWindowWidth(context) * 0.7).toInt()
    }
}