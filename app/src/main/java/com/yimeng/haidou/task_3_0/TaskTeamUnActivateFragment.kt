package com.yimeng.haidou.task_3_0

import android.graphics.Color
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.yimeng.base.BaseFragment
import com.yimeng.config.Constants
import com.yimeng.config.ConstantsUrl
import com.yimeng.config.EventTags
import com.yimeng.haidou.R
import com.yimeng.haidou.mine.IDCardInfoActivity
import com.yimeng.haidou.offline.SelPartnerTypeActivity
import com.yimeng.dialog.SimpleDialog
import com.yimeng.net.CallbackCommon
import com.yimeng.net.NetComment
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.*
import com.google.gson.JsonObject
import com.huige.library.utils.DeviceUtils
import com.huige.library.utils.SharedPreferencesUtils
import com.huige.library.utils.ToastUtils
import com.huige.library.widget.LimitScrollView
import com.lxj.xpopup.XPopup
import kotlinx.android.synthetic.main.fragment_task_team_un_activate.*
import okhttp3.Call
import org.simple.eventbus.EventBus
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/7/31 11:17 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 团队任务未激活
 * </pre>
 */
class TaskTeamUnActivateFragment : BaseFragment() {

    private val mOkHttpCommon = OkHttpCommon()
    private var mActivateNeedSeedCount = 0

    companion object {
        fun getInstance(): TaskTeamUnActivateFragment = TaskTeamUnActivateFragment()
    }

    override fun setLayoutResId(): Int = R.layout.fragment_task_team_un_activate

    override fun init() {

        tv.apply {
            movementMethod = LinkMovementMethod.getInstance()
            text = SpannableStringUtils.getBuilder("如何获得")
                    .append("发财种子?").setBold().setClickSpan(object : ClickableSpan() {
                        override fun onClick(p0: View?) {
                            ActivityUtils.getInstance().jumpActivity(SaleSeedActivity::class.java)
                        }
                    })
                    .create()
        }

        mActivateNeedSeedCount = SharedPreferencesUtils.get(Constants.TASK_NEW_IS_ACTIVATE_SEED, 1) as Int


    }

    private fun setActivateSeed() {
        // 消耗种子数量
        tv_seed.text = "激活团队任务需要消耗${mActivateNeedSeedCount}颗发财种子"

        NetComment.getMemberInfo {
            if (it.seedCount == 0) {
                tv_seed.text = "激活团队任务需要消耗${mActivateNeedSeedCount}颗发财种子\n（当前无可用发财种子）"
            }
            smartRefresh.finishRefresh()
        }
    }

    override fun initListener() {

        btn_key.setOnClickListener {

            if (!CommonUtils.checkLogin()) {
                ToastUtils.showToast(R.string.please_login_do)
                ActivityUtils.getInstance().jumpLoginActivity()
            } else if (!CommonUtils.checkAuth()) {
                // 未实名
                ToastUtils.showToast("您还未实名, 请先实名认证吧")
                ActivityUtils.getInstance().jumpActivity(IDCardInfoActivity::class.java)
            } else if (!CommonUtils.checkOpenShop()) {
                SimpleDialog.showConfirmDialog(activity, "提示", "您尚未开店，店主才可以激活团队任务！",
                        "去开店", "知道了", {
                    ActivityUtils.getInstance().jumpActivity(SelPartnerTypeActivity::class.java)
                }, null, false)
            } else {
                NetComment.getMemberInfo {
                    // 消耗种子数量
                    var count = SharedPreferencesUtils.get(Constants.TASK_NEW_IS_ACTIVATE_SEED, 1) as Int

                    if (it.seedCount < count) {
                        // 种子不足
//                        SimpleDialog.showConfirmDialog(activity, "提示", "开启该任务需要$count 颗发财种子，\n可用发财种子不足！",
//                                "去抢购", "知道了", {
//                            ActivityUtils.getInstance().jumpActivity(SaleSeedActivity::class.java)
//                        }, null, false)

                        XPopup.Builder(activity)
                                .maxWidth((DeviceUtils.getWindowWidth(activity) * 0.75).toInt())
                                .autoDismiss(true)
                                .asConfirm("提示", "开启该任务需要$count 颗发财种子，\n可用发财种子不足！",
                                        "去抢购", "知道了", null, {
                                    ActivityUtils.getInstance().jumpActivity(SaleSeedActivity::class.java)
                                }, false)
                                .bindLayout(R.layout.xpopup_custom_layout)
                                .show()

                    } else {
                        // 激活
                        activateTask()
                    }
                }
            }
        }

        smartRefresh.setOnRefreshListener {
            getActivateInfo()
        }
    }

    override fun loadData() {
        getActivateInfo()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(hidden) {
            limitAds?.stopScroll()
        }else{
            limitAds?.startScroll()
        }
    }

    override fun onResume() {
        super.onResume()
        limitAds?.startScroll()
    }

    override fun onStop() {
        super.onStop()
        limitAds?.stopScroll()
    }

    /**
     * 激活信息
     */
    private fun getActivateInfo() {
        OkHttpCommon().postLoadData(activity, ConstantsUrl.URL_TASK_3_0_IS_SHOW, CommonUtils.createParams(), object : CallbackCommon {
            override fun onFailure(call: Call, e: IOException) {
                ToastUtils.showToast(R.string.net_error)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, jsonObject: JsonObject?) {

                jsonObject?.let {
                    if (it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", "激活失败，请稍后重试！"))
                        return
                    }

                    // 就任务是否显示  open:显示旧任务
                    val dataObj = jsonObject.get("data").asJsonObject

                    // 新任务是否激活   1：激活
                    val parentNo = GsonUtils.parseJson(dataObj, "parentNo", "0")
                    SharedPreferencesUtils.put(Constants.TASK_NEW_IS_ACTIVATE, parentNo == "1")
                    if (parentNo == "1") {
                        EventBus.getDefault().post(true, EventTags.ACTIVATE_TEAM_TASK)
                        return
                    }

                    // 3.0任务激活所需种子
                    mActivateNeedSeedCount = UnitUtil.getInt(GsonUtils.parseJson(dataObj, "sort", "0"))
                    setActivateSeed()

                    // 轮播广告
                    setLimitAdsData(GsonUtils.parseJson(dataObj, "logo", ""))
                }

            }
        })
    }

    private fun setLimitAdsData(str:String){
        if(str.isNullOrBlank()) return

        var list = str.split("#")

        limitAds.setAdapter(object : LimitScrollView.LimitScrollViewAdapter{
            override fun getView(position: Int): View {
                return TextView(activity).apply {
                    setTextColor(Color.WHITE)
                    text = list[position]
                    setPadding(0, DeviceUtils.dp2px(activity, 8f), 0, DeviceUtils.dp2px(activity, 8f))
                    gravity = Gravity.CENTER
                    textSize = 14f
                }
            }

            override fun getCount(): Int = list.size
        })

        limitAds.startScroll()
    }


    /**
     * 激活任务
     */
    private fun activateTask() {
        mOkHttpCommon.postLoadData(activity, ConstantsUrl.URL_ACTIVATE_3_0_TASK, CommonUtils.createParams(), object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                jsonObject?.let {
                    if (it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", "激活失败，请稍后重试！"))
                        return
                    }

                    ToastUtils.showToast(GsonUtils.parseJson(it, "msg", "激活成功！"))
                    SharedPreferencesUtils.put(Constants.TASK_NEW_IS_ACTIVATE, true)
                    EventBus.getDefault().post(true, EventTags.ACTIVATE_TEAM_TASK)
                }
            }
        })
    }


}