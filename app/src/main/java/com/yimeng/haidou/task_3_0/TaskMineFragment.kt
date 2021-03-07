package com.yimeng.haidou.task_3_0

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.yimeng.base.BaseFragment
import com.yimeng.config.ConstantsUrl
import com.yimeng.config.EventTags
import com.yimeng.haidou.R
import com.yimeng.haidou.circle.ReleaseActivity
import com.yimeng.haidou.task.ExpTaskLogsActivity
import com.yimeng.haidou.task.adapter.TaskAdapter
import com.yimeng.dialog.SimpleDialog
import com.yimeng.entity.*
import com.yimeng.net.CallbackCommon
import com.yimeng.net.NetComment
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.*
import com.google.gson.JsonObject
import com.huige.library.utils.DeviceUtils
import com.huige.library.utils.ToastUtils
import com.huige.library.widget.CircleImageView
import kotlinx.android.synthetic.main.fragment_task_mine.*
import okhttp3.Call
import org.litepal.LitePal
import org.simple.eventbus.EventBus
import org.simple.eventbus.Subscriber
import java.io.IOException
import java.util.*

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/7/31 11:17 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 个人任务
 * </pre>
 */
class TaskMineFragment : BaseFragment() {

    private val mOkHttpCommon = OkHttpCommon()
    private var mList = mutableListOf<TaskItemBean>()
    private val mAdapter: TaskAdapter by lazy { TaskAdapter(mList) }

    // 我的排行
    private var mineActiveRanking: ActiveRankingBean? = null
    /**
     * 分享朋友圈的那个任务
     */
    private var shareTaskNo: String? = null
    private var isShareBitmap = false
    private val mHandler = Handler()


    companion object {
        fun getInstance(): TaskMineFragment = TaskMineFragment()
    }

    override fun setLayoutResId(): Int = R.layout.fragment_task_mine

    override fun init() {

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = mAdapter
        }


    }

    override fun initListener() {
        smartRefresh.setOnRefreshListener { getDailyTask() }

        mAdapter.setOnItemChildClickListener { _, view, position ->
            val taskItemBean = mList.get(position)
            setChildClick(view, taskItemBean)
        }

        mAdapter.setOnItemClickListener { _, view, position ->
            val taskItemBean = mList.get(position)

            val itemType = taskItemBean.itemType
            if (itemType == TaskAdapter.ITEM_EXP && taskItemBean.complete != 0) {
                ActivityUtils.getInstance().jumpActivity(ExpTaskLogsActivity::class.java)
            } else if (itemType == TaskAdapter.ITEM_DOWNLOAD) {
                downloadAPK(taskItemBean)
            }
        }
    }

    /**
     * 下载
     */
    private fun downloadAPK(taskItemBean: TaskItemBean?) {
        if (taskItemBean == null) return
        if (!CommonUtils.checkLogin()) {
            ToastUtils.showToast(R.string.please_login_str)
            ActivityUtils.getInstance().jumpLoginActivity()
            return
        }

        val member = CommonUtils.getMember()
        val taskNo = taskItemBean.taskNo
        val downloadLogsList = LitePal.where("taskNo = ?", taskNo).find(DownloadLogs::class.java)

        if (downloadLogsList != null && !downloadLogsList.isEmpty()) {
            for (logs in downloadLogsList) {
                if (member!!.memberNo == logs.memberNo) {
                    val calendar = Calendar.getInstance()
                    calendar.time = Date(logs.downloadTime)
                    val calendarNow = Calendar.getInstance()
                    calendarNow.time = Date(System.currentTimeMillis())

                    if (calendar.get(Calendar.DAY_OF_MONTH) == calendarNow.get(Calendar.DAY_OF_MONTH)) {
                        // 今日已下载
                        ToastUtils.showToast("任务已完成，活跃度已增加，请明日再来")
                        return
                    }
                }
            }
        }
        DownloadLogs(member!!.memberNo, taskNo, System.currentTimeMillis()).save()
        clickADS2Internet(taskNo)

        ActivityUtils.getInstance().jumpInternetExplorer(taskItemBean.adUrl)
    }

    /**
     * 点击广告上传点击事件
     */
    private fun clickADS2Internet(taskNo: String) {
        val params = CommonUtils.createParams()
        params["taskNo"] = taskNo
        mOkHttpCommon.postLoadData(activity, ConstantsUrl.URL_TASK_CLICK_DOWNLOAD_ADS, params, object : CallbackCommon {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, jsonObject: JsonObject) {}
        })
    }

    /**
     *
     */
    private fun setChildClick(view: View?, taskItemBean: TaskItemBean) {
        if (taskItemBean.adType == "share") {
            CommonUtils.shareApp()
        } else if (taskItemBean.adType == "moments" || taskItemBean.adType == "cps") {
            // 分享到商圈
            val member = CommonUtils.getMember()
            if (CommonUtils.checkLogin() && member != null) {
                if (member.job == "open") {
                    getShopDetail(member.telePhone, taskItemBean)
                } else {
                    ToastUtils.showToast("您还未开店, 请先开店吧!")
                }
            }
        }
//        else if (taskItemBean.adType == "cps") {
//            // 分享到微信QQ
//            shareUM(taskItemBean)
//        }
    }


    /**
     * 友盟分享
     * @param saleTotalAmt 总收益（分）
     */
    @SuppressLint("SetTextI18n")
    private fun shareUM(taskItemBean: TaskItemBean?, saleTotalAmt:String) {
        if (taskItemBean == null) return
        if (!CommonUtils.checkLogin()) {
            ToastUtils.showToast("请先登录后再操作!")
//            ActivityUtils.getInstance().jumpActivity(LoginActivity::class.java)
            ActivityUtils.getInstance().jumpLoginActivity()
            return
        }
        shareTaskNo = taskItemBean.taskNo
        if (isShareBitmap) {
            val member = CommonUtils.getMember()
            val view = LayoutInflater.from(activity).inflate(R.layout.include_qrcode, null)
            val logoSDV = view.findViewById<ImageView>(R.id.logoSDV)
            val qrcodeSDV = view.findViewById<ImageView>(R.id.qrcodeSDV)
            val tvUserName = view.findViewById<TextView>(R.id.tv_user_name)
            val tv_detail = view.findViewById<TextView>(R.id.tv_detail)
            tv_detail.visibility = View.VISIBLE
            tv_detail.text = "（总收益：${UnitUtil.getMoney(saleTotalAmt)}）"

            CommonUtils.showImage(logoSDV, member!!.headPath)
            tvUserName.text = member.nickname
            var url = ConstantsUrl.SHARE_URL_HEADER
            val memberNo = if (TextUtils.isEmpty(member.memberNo)) "" else "memberNo=" + member.memberNo
            url += "$memberNo&memberType=normal"
            // 生成二维码
            qrcodeSDV.setImageBitmap(QRCodeUtil.createQRCodeBitmap(url, DeviceUtils.dp2px(activity!!, 150f), DeviceUtils.dp2px(activity!!, 150f)))
            // 生成图片
            val shareBitmap = QRCodeUtil.getViewBitmap(activity!!, view)
            // 分享
            mHandler.postDelayed(Runnable { CommonUtils.shareAppUM(activity, shareBitmap, "", "", shareTaskNo) }, 500)
        } else {
            CommonUtils.shareAppUM(activity, null, taskItemBean.title, taskItemBean.remark, shareTaskNo)
        }

    }

    override fun loadData() {


    }

    /**
     * 获取店铺信息
     */
    private fun getShopDetail(shopNo: String, taskItemBean: TaskItemBean) {
        SimpleDialog.showLoadingHintDialog(activity, 1)
        val params = CommonUtils.createParams()
        params["shopNo"] = shopNo
        mOkHttpCommon.postLoadData(activity, ConstantsUrl.URL_SHOP_INFO, params, object : CallbackCommon {
            override fun onFailure(call: Call, e: IOException) {
                SimpleDialog.showLoadingHintDialog(activity, 1)
                ToastUtils.showToast(R.string.text_network_connected_error)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, jsonObject: JsonObject) {
                SimpleDialog.cancelLoadingHintDialog()
                if (jsonObject.get("status").asInt == 1) {
                    val data = jsonObject.get("data").asJsonObject.toString()
                    val modelShop = GsonUtils.getGson().fromJson(data, ModelShop1::class.java)

                    if (UnitUtil.getInt(modelShop.totalIncome) / 100.0 < 2000) {
                        // 总收益小于2000
                        if (taskItemBean.adType == "moments") {
                            ActivityUtils.getInstance().jumpActivity(ReleaseActivity::class.java, Bundle().apply {
                                putBoolean("isShareTask", true)
                            })
                        }else{
                            shareUM(taskItemBean,modelShop.totalIncome)
                        }

                    } else {
                        if(taskItemBean.adType == "moments") {
                            shareShop(modelShop)
                        }else{
                            isShareBitmap = true
                            shareUM(taskItemBean, modelShop.totalIncome)
                        }
                    }
                } else {
                    ToastUtils.showToast("店铺信息获取失败")
                }
            }
        })
    }

    private var mShareShopDialog: AlertDialog? = null
    private var mCivShopLogo: CircleImageView? = null
    private var mTvShopName: TextView? = null
    private var mTvShopAddress: TextView? = null
    private var mTvPredictMoneySum: TextView? = null
    private var mTvPredictMoneyToday: TextView? = null
    private var mTvMoneyToday: TextView? = null
    private var mTvOrderToday: TextView? = null
    /**
     * 分享店铺收益到商圈弹窗
     *
     * @param modelShop
     */
    private fun shareShop(modelShop: ModelShop1) {

        if (mShareShopDialog == null) {
            val dialogLayout = LayoutInflater.from(context).inflate(R.layout.dialog_share_shop, null)
            val shareContent = dialogLayout.findViewById<View>(R.id.frame_content)

            dialogLayout.findViewById<View>(R.id.btn_cancel).setOnClickListener {
                mShareShopDialog?.dismiss()
            }

            dialogLayout.findViewById<View>(R.id.btn_submit).setOnClickListener {
                mShareShopDialog?.dismiss()
                val shareBitmap = QRCodeUtil.getViewBitmap(shareContent)
                val bundle = Bundle()
                bundle.putBoolean("isShareTask", true)
                EventBus.getDefault().postSticky(shareBitmap, EventTags.SHARE_BITMAP)
                ActivityUtils.getInstance().jumpActivity(ReleaseActivity::class.java, bundle)
            }

            mCivShopLogo = dialogLayout.findViewById<CircleImageView>(R.id.civ_shop_logo)
            mTvShopName = dialogLayout.findViewById<TextView>(R.id.tv_shopName)
            mTvShopAddress = dialogLayout.findViewById<TextView>(R.id.tv_shop_address)
            mTvPredictMoneySum = dialogLayout.findViewById<TextView>(R.id.tv_predict_money_sum)
            mTvPredictMoneyToday = dialogLayout.findViewById<TextView>(R.id.tv_predict_money_today)
            mTvMoneyToday = dialogLayout.findViewById<TextView>(R.id.tv_money_today)
            mTvOrderToday = dialogLayout.findViewById<TextView>(R.id.tv_order_today)

            mShareShopDialog = AlertDialog.Builder(activity!!)
                    .setView(dialogLayout)
                    .create()
        }


        CommonUtils.showImage(mCivShopLogo, modelShop.logoPath)
        mTvShopName?.text = modelShop.shopName
        mTvShopAddress?.text = modelShop.address

        // 累计收益
        mTvPredictMoneySum?.text = UnitUtil.getMoney(modelShop.totalIncome)
        // 总订单
        mTvPredictMoneyToday?.text = "总订单\t" + modelShop.totalOrderCount
        // 今日收入
        mTvMoneyToday?.text = UnitUtil.getMoney(modelShop.todayIncome)
        // 今日订单
        mTvOrderToday?.text = "今日订单\t" + modelShop.todayOrderCount

        mShareShopDialog?.let {
            if (!it.isShowing) {
                mShareShopDialog?.show()
            }
        }


    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            smartRefresh.autoRefresh()
        }
    }

    override fun onResume() {
        super.onResume()
        smartRefresh.autoRefresh()
    }


    /**
     * 获取当前排行
     */
    private fun getNowRanking() {

        mOkHttpCommon.postLoadData(activity, ConstantsUrl.URL_NOW_ACTIVE_RANKING, CommonUtils.createParams(), object : CallbackCommon {
            override fun onFailure(call: Call, e: IOException) {
                smartRefresh.finishRefresh()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, jsonObject: JsonObject) {
                smartRefresh.finishRefresh()
                if (jsonObject.get("status").asInt != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取当前活跃度记录失败"))
                    return
                }

                val data = jsonObject.get("data").asJsonObject.toString()
                mineActiveRanking = GsonUtils.getGson().fromJson(data, ActiveRankingBean::class.java)

            }
        })
    }

    /**
     * 每日任务
     */
    private fun getDailyTask() {
        if (!CommonUtils.checkLogin()) return

        mOkHttpCommon.postLoadData(activity, ConstantsUrl.URL_GET_MEMBER_TASK, CommonUtils.createParams(), object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
                smartRefresh.finishRefresh()
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                smartRefresh.finishRefresh()
                jsonObject?.let {
                    if (it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取任务数据失败!"))
                        return
                    }

                    val data = jsonObject.get("data").asJsonObject

                    // 每日任务
                    var dailyObj = data.get("daily").asJsonObject
                    val bean = GsonUtils.getGson().fromJson<TaskBean>(dailyObj.toString(), TaskBean::class.java)
                    if (mList.isNotEmpty()) mList.clear()
                    val children = bean.children
                    for (taskItemBean in children) {
                        if (taskItemBean.adType == "download") {
                            taskItemBean.itemType = TaskAdapter.ITEM_DOWNLOAD
                        } else {
                            taskItemBean.itemType = TaskAdapter.ITEM_DAILY
                        }
                    }
                    mList.addAll(children)
                    mAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    /**
     * 微信分享
     */
    @Subscriber(tag = EventTags.WECHAT_SHARE_RESULT)
    fun onShareWechatResult(result: Boolean) {
        if (TextUtils.isEmpty(shareTaskNo)) {
            return
        }
        EventBus.getDefault().clear()
        NetComment.shareAddActivite(activity, shareTaskNo)
        shareTaskNo = ""
    }

}