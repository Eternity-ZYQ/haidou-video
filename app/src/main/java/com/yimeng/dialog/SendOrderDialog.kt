package com.yimeng.dialog

import android.content.Context
import android.os.Bundle
import com.yimeng.haidou.R
import com.yimeng.haidou.goodsClassify.CompanySaleClassifyActivity
import com.yimeng.haidou.task.TaskSendOrderActivity
import com.yimeng.utils.ActivityUtils
import com.lxj.xpopup.core.CenterPopupView
import kotlinx.android.synthetic.main.dialog_send_order.view.*

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/7/5 10:13 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 任务排行榜派单弹窗
 * </pre>
 */
class SendOrderDialog(context: Context, type: String, distributeMemberNo: String) : CenterPopupView(context) {
    private var type = ""
    private var distributeMemberNo = ""

    init {
        this.type = type
        this.distributeMemberNo = distributeMemberNo
    }

    override fun getImplLayoutId(): Int = R.layout.dialog_send_order

    override fun onCreate() {
        super.onCreate()

        iv_close.setOnClickListener { dismiss() }


        btn_send_order.setOnClickListener {
            // 去派单
            var bundle = Bundle()
            bundle.putString("type", type)
            bundle.putString("distributeMemberNo", distributeMemberNo)
            ActivityUtils.getInstance().jumpActivity(TaskSendOrderActivity::class.java, bundle)
            dismiss()
        }
        btn_post_order.setOnClickListener {
            // 去配单
            var bundle = Bundle()
            bundle.putInt("taskType", 1)
            bundle.putString("taskItemNo", "agent,${if (type == "fs") 1 else 0},$distributeMemberNo")
            ActivityUtils.getInstance().jumpActivity(CompanySaleClassifyActivity::class.java, bundle)
        }

    }

}