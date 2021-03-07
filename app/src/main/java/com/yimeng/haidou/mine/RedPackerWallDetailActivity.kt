package com.yimeng.haidou.mine

import android.view.View
import com.yimeng.base.BaseActivity
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.dialog.GetRedPackerSuccessDialog
import com.yimeng.entity.RedPackerBean
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.DateUtil
import com.yimeng.utils.GsonUtils
import com.yimeng.widget.MyToolBar
import com.google.gson.JsonObject
import com.huige.library.utils.DeviceUtils
import com.huige.library.utils.ToastUtils
import com.lxj.xpopup.XPopup
import kotlinx.android.synthetic.main.activity_red_packer_wall_detail.*
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/31 7:42 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 红包墙红包详情
 * </pre>
 */
class RedPackerWallDetailActivity : BaseActivity() {

    private var mProjectNo = ""
    private var mOkHttpCommon = OkHttpCommon()
    private var mRedPackerBean: RedPackerBean? = null
    // 是否领取成功
    private var isGetSuccess = false

    override fun setLayoutResId(): Int = R.layout.activity_red_packer_wall_detail

    override fun init() {

        intent.extras?.let {
            mProjectNo = it.getString("projectNo", "")
        }


    }

    override fun initListener() {
        toolBar.setOnToolBarClickListener(MyToolBar.OnToolBarClick())

        layout_rp_1.setOnClickListener { getRedPacker() }
        layout_rp_2.setOnClickListener { getRedPacker() }
    }

    private fun getRedPacker() {
        if (isGetSuccess) {
            ToastUtils.showToast("您已经领取该红包啦")
            return
        }
        getReadPacker()
    }


    override fun loadData() {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_RED_PACKER_WALL_DETAIL, CommonUtils.createParams().apply {
            put("modeProjectNo", mProjectNo)
        }, object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                jsonObject?.let {
                    if (it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", getString(R.string.net_error)))
                        return
                    }

                    mRedPackerBean = GsonUtils.getGson().fromJson(it.get("data").asJsonObject.toString(), RedPackerBean::class.java)
                    mRedPackerBean?.apply {
                        toolBar.title = projectTitle

                        CommonUtils.showImage(civ_user_head, headPath)
                        tv_user_name.text = memberName
                        tv_time.text = DateUtil.getAssignDate(createTime, 3)
                        tv_rp_content.text = projectContent
                        CommonUtils.showImage(iv_rp, projectLogo)
                        iv_read_num.text = scanNum.toString()

                        tv_member_get.text = "已有${sharePaidNum-nowHadPaidNum + oldUserNum - nextHadPaidNum}人领到红包"
                    }

                    var windowHeight = DeviceUtils.getWindowHeight(this@RedPackerWallDetailActivity)

                    if(ll_content.measuredHeight > windowHeight) {
                        layout_rp_1.visibility = View.GONE
                        layout_rp_2.visibility = View.VISIBLE
                    }else{
                        layout_rp_1.visibility = View.VISIBLE
                        layout_rp_2.visibility = View.GONE
                    }
                }
            }
        })
    }

    /**
     * 领取红包
     */
    private fun getReadPacker() {
        mOkHttpCommon.postLoadData(this@RedPackerWallDetailActivity, ConstantsUrl.URL_GET_RED_PACKER, CommonUtils.createParams().apply {
            put("projectNo", mProjectNo)
        }, object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                jsonObject?.let {
                    if (it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", getString(R.string.net_error)))
                        return
                    }

//                    ToastUtils.showToast(GsonUtils.parseJson(it, "msg", "领取成功！"))
//                    finish()
                    showDialog()
                }
            }
        })
    }

    private fun showDialog() {
        if (mRedPackerBean == null) return
        isGetSuccess = true
        XPopup.Builder(this)
                .asCustom(GetRedPackerSuccessDialog(this, mRedPackerBean!!.nextSharePrice))
                .show()
    }
}
