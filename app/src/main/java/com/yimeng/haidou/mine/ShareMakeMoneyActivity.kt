package com.yimeng.haidou.mine

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.base.BaseActivity
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.App
import com.yimeng.haidou.R
import com.yimeng.dialog.SimpleDialog
import com.yimeng.entity.ContactsInfoBean
import com.yimeng.entity.IncomeAccointInfo
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.ToastUtils
import com.yanzhenjie.permission.Action
import kotlinx.android.synthetic.main.activity_share_make_money.*
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/7/8 11:13 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 分享赚钱
 * </pre>
 */
class ShareMakeMoneyActivity : BaseActivity() {
    private var mAccountInfo: IncomeAccointInfo? = null

    private var mList = mutableListOf<ContactsInfoBean>()
    private val mAdapter: BaseQuickAdapter<ContactsInfoBean, BaseViewHolder> by lazy {
        object : BaseQuickAdapter<ContactsInfoBean, BaseViewHolder>(R.layout.adapter_share_make_money, mList) {
            override fun convert(helper: BaseViewHolder?, item: ContactsInfoBean?) {
                helper?.apply {
                    item?.apply {
                        setText(R.id.tv_member_name, name)
                        setText(R.id.tv_member_mobile, mobileNo)
                        mAccountInfo?.apply {
                            setText(R.id.tv_share, SpannableStringUtils.getBuilder("得")
                                    .append((recReward.toInt().div(100)).toString()).setProportion(1.4f)
                                    .append("元")
                                    .create())
                        }
                        addOnClickListener(R.id.tv_share)
                    }
                }
            }
        }
    }

    private val mOkHttpCommon = OkHttpCommon()
    override fun setLayoutResId(): Int = R.layout.activity_share_make_money
    override fun init() {

        //Log.d("showShareIcon", "ShareMakeMoneyActivity init ok")
        ToastUtils.showToast("ShareMakeMoneyActivity init ok")

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ShareMakeMoneyActivity)
            adapter = mAdapter
            isNestedScrollingEnabled = false
        }

    }

    override fun initListener() {
        iv_back.setOnClickListener { finish() }
        ll_share.setOnClickListener {
            // 规则
            ActivityUtils.getInstance().jumpH5Activity("", ConstantsUrl.URL_PROTOCOL + "人脉奖励金规则")
        }
        iv_to_withdraw.setOnClickListener {
            mAccountInfo?.apply {
                if (totalBonus > 0) {
                    ActivityUtils.getInstance().jumpActivity(IncomeAccountActivity::class.java)
                    finish()
                } else {
                    CommonUtils.shareAppUM(this@ShareMakeMoneyActivity, null, "好友助力", "我在促销王做全民团购任务，来帮我加速一把，赚钱少不了你！", "")
                }
            }
        }
        iv_share.setOnClickListener {
            // 立即分享
            CommonUtils.shareAppUM(this, null, "好友助力", "我在促销王做全民团购任务，来帮我加速一把，赚钱少不了你！", "")
        }
        tv_refresh.setOnClickListener {
            // 换一批
            getRecContacts()
        }
        tv_change_contacts.setOnClickListener {
            // 更新通讯录
            changeContacts()
        }

        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            var contactsInfoBean = mList[position]
            // 发送短信
            mAccountInfo?.apply {
                ActivityUtils.getInstance().sendSMSIntent(this@ShareMakeMoneyActivity, contactsInfoBean.mobileNo, recSmsTemplate)
            }
        }

        scrollView.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (scrollY > 100) {
                layout_head.setBackgroundColor(Color.WHITE)
            } else {
                layout_head.setBackgroundColor(Color.TRANSPARENT)
            }
        }

        tv_share_get_money.setOnClickListener {
            // 群发短信
            sendSMSAllMember()
        }
    }

    /**
     * 群发短信
     */
    private fun sendSMSAllMember() {
        mAccountInfo?.apply {
            if(mList.isEmpty()) return
            if (TextUtils.isEmpty(recSmsTemplate)) {
                ToastUtils.showToast("短信内容为空！")
                return
            }
            var mobiles = ""
            mList.forEach {
                mobiles += it.mobileNo + ";"
            }
            mobiles = mobiles.take(mobiles.length-1)
            ActivityUtils.getInstance().sendSMSIntent(this@ShareMakeMoneyActivity, mobiles, recSmsTemplate)

        }
    }

    /**
     * 重传通讯录
     */
    private fun changeContacts() {
        CommonUtils.getPermission(this, Action {
            if (it.size != 2) {
                return@Action
            }
            SimpleDialog.showLoadingHintDialog(this, 1)
            tv_change_contacts.isEnabled = false
            val map = CommonUtils.getAllPhoneContacts(this)
            val callRecord = Gson().toJson(map)
            var params = CommonUtils.createParams()
            params["book"] = callRecord
            val telephonyManager = App.getContext().getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            @SuppressLint("MissingPermission") val bindNo = telephonyManager.deviceId
            params["bindNo"] = bindNo

            mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_SHARE_UPLOAD_RECORD, params, object : CallbackCommon {
                override fun onFailure(call: Call?, e: IOException?) {
                    ToastUtils.showToast(R.string.net_error)
                    SimpleDialog.cancelLoadingHintDialog()
                    tv_change_contacts.isEnabled = true
                }

                override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                    SimpleDialog.cancelLoadingHintDialog()
                    tv_change_contacts.isEnabled = true
                    jsonObject?.apply {
                        if (jsonObject.get("status").asInt != 1) {
                            ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "更新失败"))
                            return
                        }
                        ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "更新成功"))
                    }
                }
            })
        }, Manifest.permission.READ_CONTACTS)
    }

    override fun loadData() {

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

                    mAccountInfo = GsonUtils.getGson().fromJson(dataObj.toString(), IncomeAccointInfo::class.java)
                    setInfo()
                }

                getRecContacts()
            }
        })
    }

    private fun setInfo() {
        mAccountInfo?.apply {
            tv_share_num.text = SpannableStringUtils.getBuilder(recNum).setProportion(1.8f).setForegroundColor(Color.parseColor("#e99905"))
                    .append("人").setProportion(1.2f).setForegroundColor(Color.parseColor("#e99905"))
                    .append("\n已邀人数")
                    .create()
            tv_share_money.text = SpannableStringUtils.getBuilder(getTotalBonusStr()).setProportion(1.8f).setForegroundColor(Color.parseColor("#e99905"))
                    .append("元").setProportion(1.2f).setForegroundColor(Color.parseColor("#e99905"))
                    .append("\n人脉奖励金")
                    .create()

            tv_limit_pool.text = UnitUtil.getMoney(bonusPool)
        }
    }

    /**
     * 换一批   推荐分享用户
     */
    private fun getRecContacts() {
        tv_refresh.isEnabled = false
        SimpleDialog.showLoadingHintDialog(this, 1)
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_GET_RADIOM_MEMBER, CommonUtils.createParams(), object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
                SimpleDialog.cancelLoadingHintDialog()
                tv_refresh.isEnabled = true
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                SimpleDialog.cancelLoadingHintDialog()
                tv_refresh.isEnabled = true
                jsonObject?.apply {
                    if (jsonObject.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", getString(R.string.net_error)))
                        return
                    }
                    if (jsonObject.get("data").isJsonNull) return
                    var array = jsonObject.get("data").asJsonArray
                    var list = GsonUtils.getGson().fromJson<List<ContactsInfoBean>>(array.toString(), object : TypeToken<List<ContactsInfoBean>>() {}.type)
                    mList.clear()
                    mList.addAll(list)
                    mAdapter.notifyDataSetChanged()

                    // 邀请可以获得总数
                    mAccountInfo?.apply {
                        tv_rec_share.text = SpannableStringUtils.getBuilder("推荐邀请")
                                .append("${list.size}").setForegroundColor(Color.parseColor("#e35e35")).setProportion(1.5f)
                                .append("人")
                                .create()
                        tv_share_get_money.text = "邀请获得${list.size * recReward.toInt().div(100)}元"
                    }


                }
            }
        })
    }

}
