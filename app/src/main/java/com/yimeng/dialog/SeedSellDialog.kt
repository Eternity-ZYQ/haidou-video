package com.yimeng.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.entity.SellFruitBean
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.yimeng.utils.UnitUtil
import com.google.gson.JsonObject
import com.huige.library.utils.ToastUtils
import com.lxj.xpopup.core.CenterPopupView
import kotlinx.android.synthetic.main.dialog_sell_seed.view.*
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/12 11:36 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 挂卖
 *  @param maxCount 最大数量
 *  @param sellType 1: 挂卖种子
 *                  2: 挂卖活跃果
 * </pre>
 */

class SeedSellDialog @JvmOverloads constructor(context: Context, maxCount: Int, sellType: Int, fruitBean: SellFruitBean? = null) : CenterPopupView(context) {

    private var mOkHttpCommon: OkHttpCommon = OkHttpCommon()
    private var mMaxCount = maxCount
    private var mSellType = sellType
    private var mFruitBean = fruitBean


    override fun getImplLayoutId(): Int = R.layout.dialog_sell_seed

    private var mPrice = 0.0

    override fun onCreate() {
        super.onCreate()

        if (mSellType == 1) {
            tv_title.text = "种子挂卖"
            getSeedPrice()
        } else {
            tv_title.text = "活跃果挂卖"
            getFruitPrice()
        }

        et_count.hint = "请输入数量（最多${mMaxCount}颗）"

        et_count.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (p0 != null) {
                    if (p0.isNotEmpty()) {
                        var count = p0.toString().toInt()
                        when {
                            count in 1..mMaxCount -> {
                                tv_seed_price_sum.text = "￥" + String.format("%.2f", mPrice * count)
                            }
                            count > mMaxCount -> {
                                tv_seed_price_sum.text = "￥" + String.format("%.2f", mPrice * mMaxCount)
                                et_count.setText(mMaxCount.toString())
                                et_count.setSelection(et_count.text.length)
                            }
                            else -> tv_seed_price_sum.text = ""
                        }
                    } else {
                        tv_seed_price_sum.text = ""
                    }
                } else {
                    tv_seed_price_sum.text = ""
                }
            }
        })



        btn_cancel.setOnClickListener { dismiss() }
        btn_submit.setOnClickListener {
            // 挂卖
            if (mSellType == 1) {
                sellSeed()
            } else {
                sellFruit()
            }
        }
    }

    /**
     * 挂卖
     */
    private fun sellSeed() {
        var countStr = et_count?.text.toString()
        if (countStr.isNullOrBlank() || countStr.toInt() <= 0) {
            ToastUtils.showToast("请输入要挂卖的种子数量")
        } else {
            mOkHttpCommon.postLoadData(context as Activity, ConstantsUrl.URL_SELL_SEED, CommonUtils.createParams().apply {
                put("num", countStr)
            }, object : CallbackCommon {
                override fun onFailure(call: Call?, e: IOException?) {
                    ToastUtils.showToast(R.string.net_error)
                }

                override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                    jsonObject?.let {
                        if (it.get("status").asInt != 1) {
                            ToastUtils.showToast(GsonUtils.parseJson(it, "msg", context.getString(R.string.net_error)))
                            return
                        }

                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", "挂卖成功"))
                        dismiss()
                    }
                }
            })
        }
    }

    /**
     * 活跃果挂卖
     */
    private fun sellFruit() {
        var countStr = et_count?.text.toString()
        if (countStr.isNullOrBlank() || countStr.toInt() <= 0) {
            ToastUtils.showToast("请输入要挂卖的活跃果数量")
        } else {
            mFruitBean?.let {
                mOkHttpCommon.postLoadData(context as Activity, ConstantsUrl.URL_SELL_FRUIT, CommonUtils.createParams().apply {
                    put("num", countStr)
                    put("blockNo", it.blockNo)
                    put("merchantTaskNo", it.merchantTaskNo)
                    put("memberTaskNo", it.memberTaskNo)
                    put("fruitLevel", it.rlevel.toString())

                }, object : CallbackCommon {
                    override fun onFailure(call: Call?, e: IOException?) {
                        ToastUtils.showToast(R.string.net_error)
                    }

                    override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                        jsonObject?.let {jsonObject ->
                            if (jsonObject.get("status").asInt != 1) {
                                ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", context.getString(R.string.net_error)))
                                return
                            }

                            ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "挂卖成功"))
                            dismiss()
                        }
                    }
                })
            }
        }
    }

    /**
     * 获取种子市场价
     */
    private fun getSeedPrice() {
        mOkHttpCommon.postLoadData(context as Activity, ConstantsUrl.URL_GET_SEED_PRICE, CommonUtils.createParams(), object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                jsonObject?.let {
                    if (it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", context.getString(R.string.net_error)))
                        return
                    }

                    // 分
                    var price = UnitUtil.getInt(it.get("data").asString)
                    mPrice = price / 100.0
                    tv_seed_price.text = "当前市价￥$mPrice/颗"
                }
            }
        })
    }

    /**
     * 活跃果市价
     */
    private fun getFruitPrice() {
        mOkHttpCommon.postLoadData(context as Activity, ConstantsUrl.URL_GET_FRUIT_PRICE, CommonUtils.createParams().apply {
            put("blockNo", mFruitBean?.blockNo)
        }, object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                jsonObject?.let {
                    if (it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", context.getString(R.string.net_error)))
                        return
                    }

                    // 分
                    var price = UnitUtil.getInt(it.get("data").asString)
                    mPrice = price / 100.0
                    tv_seed_price.text = "当前市价:￥$mPrice/颗"
                }
            }
        })
    }

}