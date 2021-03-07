package com.yimeng.haidou.mine

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import com.yimeng.base.BaseActivity
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.dialog.SimpleDialog
import com.yimeng.entity.IncomeAccointInfo
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.ActivityUtils
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.yimeng.utils.UnitUtil
import com.yimeng.widget.MyToolBar
import com.google.gson.JsonObject
import com.huige.library.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_income_account.*
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/7/8 10:11 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 收益账户
 * </pre>
 */
class IncomeAccountActivity : BaseActivity() {
    private val mOkHttpCommon: OkHttpCommon by lazy { OkHttpCommon() }
    /**
     * 余额： 分
     */
    private var moneyAll: Int = 0
    /**
     * 收益账户转用户余额
     */
    private val shopMoney2WalletDialog: AlertDialog by lazy {
        object : AlertDialog.Builder(this) {}.apply {
            val dialogView = LayoutInflater.from(this@IncomeAccountActivity).inflate(R.layout.dialog_shop_money_to_wallet, null)

            val et_input = dialogView.findViewById<EditText>(R.id.et_input)
            dialogView.findViewById<View>(R.id.tv_all).setOnClickListener {
                // 全部转出
                et_input.setText((moneyAll / 100.0).toString())
            }

            dialogView.findViewById<View>(R.id.btn_dialog_cancel).setOnClickListener { shopMoney2WalletDialog.dismiss() }

            dialogView.findViewById<View>(R.id.btn_dialog_submit).setOnClickListener(View.OnClickListener {
                // 开始转账

                val inputMoney = et_input.text.toString().trim { it <= ' ' }
                if (UnitUtil.getDouble(inputMoney) <= 0) {
                    ToastUtils.showToast("请输入金额")
                    return@OnClickListener
                }
                shopMoneyToMineWallet(inputMoney)
            })
            setView(dialogView)
        }.create()
    }

    // 收益账户
    private var mAccointInfo: IncomeAccointInfo? = null

    override fun setLayoutResId(): Int = R.layout.activity_income_account

    override fun init() {
        if (CommonUtils.getMember().isAgent) {
            btn_recharge.visibility = View.GONE
        }


    }

    override fun initListener() {

        toolBar.setOnToolBarClickListener(object : MyToolBar.OnToolBarClick() {
            override fun onRightClick() {
                // 查看记录
                ActivityUtils.getInstance().jumpActivity(IncomeLogsActivity::class.java)
            }
        })

        btn_recharge.setOnClickListener { shopMoney2WalletDialog.show() }
        btn_call.setOnClickListener {
            mAccointInfo?.apply { ActivityUtils.getInstance().diallPhone(this@IncomeAccountActivity, contactService) }
        }

        iv_share.setOnClickListener {
            ActivityUtils.getInstance().jumpShareMakeMoney(this)
        }
    }



    override fun loadData() {
        SimpleDialog.showLoadingHintDialog(this, 1)
        getAccountInfo()
    }

    /**
     * 收益账户数据
     */
    private fun getAccountInfo() {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_INCOME_DETAIL, CommonUtils.createParams(), object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
                SimpleDialog.cancelLoadingHintDialog()
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                SimpleDialog.cancelLoadingHintDialog()
                jsonObject?.apply {
                    if (jsonObject.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", getString(R.string.net_error)))
                        return
                    }

                    var dataObj = jsonObject.get("data").asJsonObject

                    mAccointInfo = GsonUtils.getGson().fromJson(dataObj.toString(), IncomeAccointInfo::class.java)
                    setInfo()
                }
            }
        })
    }

    private fun setInfo() {

        mAccointInfo?.apply {
            // 我的奖金池
            tv_bonusPool.text = UnitUtil.getMoney(bonusPool)
            // 收益金额
            tv_money.text = UnitUtil.getMoney(cycleLoginTimes)
            moneyAll = cycleLoginTimes.toInt()
            // 人脉奖励金
            tv_share_money.text = "￥${getTotalBonusStr()}"
            // 分红收益
            tv_income.text = UnitUtil.getMoney(totalCycleLoginTimes)

            tv_tip.text = "推广专属奖金池${UnitUtil.formatMoney((bonusPool.toInt() + totalBonus).toString(),false)}元！\n邀请好友完成任务，收货即可提现奖金和分红"
        }

    }

    /**
     * 收益账户转用户余额
     *
     * @param money
     */
    private fun shopMoneyToMineWallet(money: String) {
        val dMoney = (UnitUtil.getDouble(money) * 100).toInt()
        val params = CommonUtils.createParams()
        params["amt"] = dMoney.toString()
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_INCONE_TO_MINEMONEY, params, object : CallbackCommon {
            override fun onFailure(call: Call, e: IOException) {
                ToastUtils.showToast("转入失败，请稍后重试")
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, jsonObject: JsonObject) {
                if (jsonObject.get("status").asInt != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "转入失败，请稍后重试"))
                    return
                }

                ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "转入成功！"))
                shopMoney2WalletDialog.dismiss()
                loadData()
            }
        })
    }


}
