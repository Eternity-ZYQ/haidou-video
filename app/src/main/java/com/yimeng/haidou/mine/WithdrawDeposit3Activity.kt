package com.yimeng.haidou.mine

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import com.yimeng.base.BaseActivity
import com.yimeng.config.ConstantsUrl
import com.yimeng.config.EventTags
import com.yimeng.haidou.R
import com.yimeng.dialog.RechargeDialogUtils
import com.yimeng.dialog.SimpleDialog
import com.yimeng.entity.ModelWallet
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.*
import com.yimeng.widget.MyToolBar
import com.google.gson.JsonObject
import com.huige.library.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_withdraw_deposit3.*
import okhttp3.Call
import org.simple.eventbus.Subscriber
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/6/25 2:31 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 账户余额
 * </pre>
 */
class WithdrawDeposit3Activity : BaseActivity() {

    private val mOkHttpCommon: OkHttpCommon by lazy { OkHttpCommon() }
    /**
     * 店铺余额转用户余额
     */
    private val shopMoney2WalletDialog: AlertDialog by lazy {
        object : AlertDialog.Builder(this){}.apply {
            val dialogView = LayoutInflater.from(this@WithdrawDeposit3Activity).inflate(R.layout.dialog_shop_money_to_wallet, null)

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
    /**
     * mine：我的钱包
     * shop：店铺钱包
     */
    private var walletType = "mine"
    /**
     * 我的钱包信息
     */
    private var mModelWallet: ModelWallet? = null

    /**
     * 余额： 分
     */
    private var moneyAll: Int = 0


    override fun setLayoutResId(): Int = R.layout.activity_withdraw_deposit3

    override fun init() {

        intent.extras?.apply {
            walletType = getString("walletType", "mine")
        }

        if (walletType == "mine") {
            tv_wallet_title.text = "我的钱包"
            btn_recharge.text = "充值"
            iv_balance.setImageResource(R.mipmap.icon_user_balance)

        } else {
            tv_wallet_title.text = "商家账户"
            btn_recharge.text = "转账"
            iv_balance.setImageResource(R.mipmap.icon_shop_balance)
        }

    }

    override fun initListener() {
        toolBar.setOnToolBarClickListener(object : MyToolBar.OnToolBarClick() {
            override fun onRightClick() {
                val bundle = Bundle()
                bundle.putBoolean("isShop", walletType == "shop")
                ActivityUtils.getInstance().jumpActivity(BalanceLogsActivity::class.java, bundle)
            }
        })

        btn_recharge.setOnClickListener {
            if (walletType == "mine") {
                // 充值
                RechargeDialogUtils(this).showDialog()
            } else if (walletType == "shop") {
                // 店铺余额转账到用户余额
                shopMoney2WalletDialog.show()
            }
        }

        btn_withdraw.setOnClickListener{ jumpWithdraw() }

    }

    /**
     * 跳转到提现页面
     */
    private fun jumpWithdraw() {
        if (!CommonUtils.checkAuth()) {
            // 未实名
            ToastUtils.showToast("您还未实名, 请先实名认证吧")
            ActivityUtils.getInstance().jumpActivity(IDCardInfoActivity::class.java)
            return
        }

        if(mModelWallet==null)return
        var bundle = Bundle()
        if (walletType == "shop") {
            val shopMoneyAll = UnitUtil.getInt(mModelWallet!!.baodanbi)
            //可提现金额(分)
            val balanceAmtFreeze = mModelWallet!!.balanceAmtFreeze//冻结金额
            moneyAll = shopMoneyAll - UnitUtil.getInt(balanceAmtFreeze)
            bundle.putInt("shopMoneyAll", shopMoneyAll)
        } else {
            moneyAll = UnitUtil.getInt(mModelWallet!!.balance)
        }
        bundle.putInt("moneyAll", moneyAll)
        bundle.putString("walletType", walletType)
        ActivityUtils.getInstance().jumpActivity(WithdrawDeposit3ImplActivity::class.java, bundle)
    }



    /**
     * 店铺余额转用户余额
     *
     * @param money
     */
    private fun shopMoneyToMineWallet(money: String) {
        val dMoney = (UnitUtil.getDouble(money) * 100).toInt()
        val params = CommonUtils.createParams()
        params["amt"] = dMoney.toString()
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_SHOPMONEY_TO_MINEMONEY, params, object : CallbackCommon {
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


    override fun loadData() {

        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_MY_WALLET, CommonUtils.createParams(), object : CallbackCommon{
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                jsonObject?.apply {
                    if(jsonObject.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取用户余额失败，请稍后重试"))
                        return
                    }

                    var dataObj = jsonObject.get("data").asJsonObject
                    mModelWallet = GsonUtils.getGson().fromJson(dataObj.toString(), ModelWallet::class.java)
                    setMoneyInfo()
                }
            }
        })

    }

    private fun setMoneyInfo() {
        mModelWallet?.apply {
            if (walletType == "shop") {
                val shopMoneyAll = UnitUtil.getInt(mModelWallet!!.baodanbi)
                tv_wallet_money.text = UnitUtil.getMoney(shopMoneyAll.toString())

                //可提现金额(分)
                val balanceAmtFreeze = mModelWallet!!.balanceAmtFreeze//冻结金额
                moneyAll = shopMoneyAll - UnitUtil.getInt(balanceAmtFreeze)

                tv_wallet_tip.movementMethod = LinkMovementMethod.getInstance()
                tv_wallet_tip.setTextColor(ContextCompat.getColor(this@WithdrawDeposit3Activity, R.color.c_333333))
                tv_wallet_tip.text = SpannableStringUtils.getBuilder("其中")
                        .append(UnitUtil.getMoney(moneyAll.toString())).setForegroundColor(ContextCompat.getColor(this@WithdrawDeposit3Activity, R.color.c_money))
                        .append("可转出至用户余额或提现")
                        .append("查看原因").setForegroundColor(resources.getColor(R.color.c_link_color)).setClickSpan(object : ClickableSpan() {
                            override fun onClick(view: View) {
                                SimpleDialog.showSimpleRemarkWithTitleDialog(this@WithdrawDeposit3Activity, "可转出或可提现金额", "买家收货后卖家收益结算到账，即可提现或转账到自己的用户余额，用于消费抵扣")
                            }
                        })
                        .create()

            } else {
                moneyAll = UnitUtil.getInt(mModelWallet!!.balance)
                tv_wallet_money.text = UnitUtil.getMoney(moneyAll.toString())

                tv_wallet_tip.text = "每日等额赠送活跃度"
            }
        }
    }


    /**
     * 充值回调
     */
    @Subscriber(tag = EventTags.WECHAT_PAY_RESULT)
    fun onPayResult(flag: Boolean) {
        if (flag) {
            ToastUtils.showToast("充值成功")
            loadData()
        } else {
            ToastUtils.showToast("充值失败")
        }
    }

}
