package com.yimeng.haidou.mine

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import com.yimeng.base.BaseActivity
import com.yimeng.config.ConstantsUrl
import com.yimeng.config.EventTags
import com.yimeng.haidou.R
import com.yimeng.dialog.CommonAllPayDialog
import com.yimeng.enums.PayType
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.*
import com.yimeng.widget.MyToolBar
import com.google.gson.JsonObject
import com.huige.library.utils.ToastUtils
import com.lxj.xpopup.XPopup
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import kotlinx.android.synthetic.main.activity_set_red_packer_info.*
import okhttp3.Call
import org.simple.eventbus.Subscriber
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/26 11:33 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 设置红包属性
 * </pre>
 */
class SetRedPackerInfoActivity : BaseActivity() {

    // 红包编号
    private var mProjectNo = ""
    private var mRpTitle = ""
    private var mRpContent = ""
    /**
     * true ：新老用户可领
     * false：仅新用户可领
     */
    private var mRedPackerIsOld = false
    private val mOkHttpCommon = OkHttpCommon()
    private var mTextWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if(mRedPackerIsOld) { // 新老用户
                var newPrice = UnitUtil.getDouble(et_new_price.text.toString())
                var newCount = UnitUtil.getDouble(et_new_count.text.toString())
                var oldPrice = UnitUtil.getDouble(et_old_price.text.toString())
                var oldCount = UnitUtil.getDouble(et_old_count.text.toString())
                tv_money_sum.text = String.format("￥%.2f", newPrice * newCount + oldPrice * oldCount)
            }else {
                var price = UnitUtil.getDouble(et_money.text.toString())
                var count = UnitUtil.getDouble(et_count.text.toString())
                tv_money_sum.text = String.format("￥%.2f", price * count)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        super.onCreate(savedInstanceState)
    }

    override fun setLayoutResId(): Int = R.layout.activity_set_red_packer_info

    override fun init() {

        intent.extras?.let {
            mProjectNo = it.getString("projectNo", "")
            mRpTitle = it.getString("rpTitle", "")
            mRpContent = it.getString("rpContent", "")

        }

    }


    override fun initListener() {
        toolBar.setOnToolBarClickListener(MyToolBar.OnToolBarClick())
        // 红包预览
        tv_look.setOnClickListener {
            ActivityUtils.getInstance().jumpH5Activity("",
                    "${ConstantsUrl.ULR_PREVIEW_RED_PACKER_2}/$mProjectNo")
        }

        btn_seed.setOnClickListener { seedRedPacker() }

        et_money.addTextChangedListener(mTextWatcher)
        et_count.addTextChangedListener(mTextWatcher)

        et_new_count.addTextChangedListener(mTextWatcher)
        et_new_price.addTextChangedListener(mTextWatcher)

        et_old_count.addTextChangedListener(mTextWatcher)
        et_old_price.addTextChangedListener(mTextWatcher)


        rg_change.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_new -> {
                    mRedPackerIsOld = false
                    showNewRedPacker()
                    et_money.requestFocus()
                }
                R.id.rb_old -> {
                    mRedPackerIsOld = true
                    showOldRedPacker()
                    et_new_count.requestFocus()
                }
            }
        }

        // 推广红包规则
        tv_help.setOnClickListener { ActivityUtils.getInstance().jumpH5Activity("推广红包规则", ConstantsUrl.URL_PROTOCOL + "推广红包规则") }

    }

    /**
     * 新老用户红包
     */
    private fun showOldRedPacker(){
        layout_share_count.visibility = View.GONE
        layout_share_money.visibility = View.GONE

        layout_share_count_old.visibility = View.VISIBLE
        layout_share_money_old.visibility = View.VISIBLE
        var animation = TranslateAnimation(0f, 0f,
                layout_share_count.bottom.toFloat(), 0f).apply {
            duration = 300
            interpolator = LinearInterpolator()
        }
        layout_share_count_old.startAnimation(animation)
        layout_share_money_old.startAnimation(animation)


        var newPrice = UnitUtil.getDouble(et_new_price.text.toString())
        var newCount = UnitUtil.getDouble(et_new_count.text.toString())
        var oldPrice = UnitUtil.getDouble(et_old_price.text.toString())
        var oldCount = UnitUtil.getDouble(et_old_count.text.toString())
        tv_money_sum.text = String.format("￥%.2f", newPrice * newCount + oldPrice * oldCount)
    }

    /**
     * 仅新用户红包
     */
    private fun showNewRedPacker(){
        layout_share_count_old.visibility = View.GONE
        layout_share_money_old.visibility = View.GONE

        layout_share_count.visibility = View.VISIBLE
        layout_share_money.visibility = View.VISIBLE
        var animation = TranslateAnimation(0f, 0f,
                -layout_share_money_old.bottom.toFloat(), 0f).apply {
            duration = 300
            interpolator = LinearInterpolator()
        }
        layout_share_count.startAnimation(animation)
        layout_share_money.startAnimation(animation)


        var price = UnitUtil.getDouble(et_money.text.toString())
        var count = UnitUtil.getDouble(et_count.text.toString())
        tv_money_sum.text = String.format("￥%.2f", price * count)
    }

    /**
     * 发红包
     */
    private fun seedRedPacker() {
        var inputMoney = 0.0
        var count = 0

        if(mRedPackerIsOld) { // 新老用户
            inputMoney = UnitUtil.getDouble(et_new_price.text.toString())
            count = UnitUtil.getInt(et_new_count.text.toString())

            var oldPrice = UnitUtil.getDouble(et_old_price.text.toString())
            var oldCount = UnitUtil.getInt(et_old_count.text.toString())

            if (inputMoney <= 0) {
                ToastUtils.showToast("请输入新用户正确红包金额")
                return
            }

            if (count < 1) {
                ToastUtils.showToast("请输入新用户正确红包个数")
                return
            }

            if (oldPrice <= 0) {
                ToastUtils.showToast("请输入老用户正确红包金额")
                return
            }
            if (oldCount < 1) {
                ToastUtils.showToast("请输入老用户正确红包个数")
                return
            }

            if(inputMoney < 0.1 || oldPrice < 0.1) {
                ToastUtils.showToast("单个红包最低0.1元")
                return
            }
        }else{
            inputMoney = UnitUtil.getDouble(et_money.text.toString())
            count = UnitUtil.getInt(et_count.text.toString())

            if (inputMoney <= 0) {
                ToastUtils.showToast("请输入正确红包金额")
                return
            }

            if (count < 1) {
                ToastUtils.showToast("请输入正确红包个数")
                return
            }

            if(inputMoney < 0.1) {
                ToastUtils.showToast("单个红包最低0.1元")
                return
            }
        }

        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_RED_PACKER_ADD_MONEY, CommonUtils.createParams().apply {
            put("projectNo", mProjectNo)
            put("amt", "${(inputMoney * 100).toInt()}")
            put("num", count.toString())
            if(mRedPackerIsOld) {
                var oldPrice = UnitUtil.getDouble(et_old_price.text.toString())
                var oldCount = UnitUtil.getInt(et_old_count.text.toString())
                put("oldAmt", "${(oldPrice * 100).toInt()}")
                put("oldNum", oldCount.toString())
            }
            put("externalLinks", if(mRedPackerIsOld) "default" else "new")
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

                    var moneySum = if(mRedPackerIsOld) { // 新老用户
                        var newPrice = UnitUtil.getDouble(et_new_price.text.toString())
                        var newCount = UnitUtil.getDouble(et_new_count.text.toString())
                        var oldPrice = UnitUtil.getDouble(et_old_price.text.toString())
                        var oldCount = UnitUtil.getDouble(et_old_count.text.toString())
                        ((newPrice * newCount + oldPrice * oldCount) * 100).toString()
                    }else {
                        var price = UnitUtil.getDouble(et_money.text.toString())
                        var count = UnitUtil.getDouble(et_count.text.toString())
                        (price * count * 100).toString()
                    }

                    val rewardNo = jsonObject.get("data").asJsonObject.get("rewardNo").asString
                    XPopup.Builder(this@SetRedPackerInfoActivity)
                            .asCustom(CommonAllPayDialog(this@SetRedPackerInfoActivity, "塞钱进红包", moneySum, rewardNo,
                                    EventTags.RED_PACKER_ADD_MONEY, PayType.YU_E, PayType.ALIPAY, PayType.WECHAT))
                            .show()
                }
            }
        })
    }


    override fun loadData() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }

    @Subscriber(tag = EventTags.RED_PACKER_ADD_MONEY)
    private fun onPayResult(result: Boolean) {
        Log.d("huiger-msg", "塞钱进红包 -> : onPayResult" + "")
        if (result) {
            ToastUtils.showToast("支付成功！")
            UMShareUtils.getInstance().shareURL(this, ConstantsUrl.URL_SHARE_RED_PACKER + mProjectNo,
                    shareTitle = mRpTitle, shareDescription = mRpContent,
                    shareLogo = R.mipmap.icon_share_red_packer, callback = object : UMShareListener {
                override fun onResult(p0: SHARE_MEDIA?) {
                    ActivityUtils.getInstance().jumpActivity(AllRedPackerActivity::class.java, Bundle().apply {
                        putInt("index", 1)
                    })
                }

                override fun onCancel(p0: SHARE_MEDIA?) {
                    ActivityUtils.getInstance().jumpActivity(AllRedPackerActivity::class.java, Bundle().apply {
                        putInt("index", 1)
                    })
                }

                override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
                }

                override fun onStart(p0: SHARE_MEDIA?) {
                }
            })
        } else {
            ToastUtils.showToast("支付失败！")
        }
    }

    @Subscriber(tag = EventTags.WECHAT_SHARE_RESULT)
    private fun onShareResult(result: Boolean) {
        ActivityUtils.getInstance().jumpActivity(AllRedPackerActivity::class.java, Bundle().apply {
            putInt("index", 1)
        })
    }
}
