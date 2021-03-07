package com.yimeng.haidou.task_3_0

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import android.widget.TextView
import com.yimeng.base.BaseActivity
import com.yimeng.config.ConstantsUrl
import com.yimeng.config.EventTags
import com.yimeng.haidou.R
import com.yimeng.dialog.SimpleDialog
import com.yimeng.entity.SeedBean
import com.yimeng.net.CallbackCommon
import com.yimeng.net.NetComment
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.ActivityUtils
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.google.gson.JsonObject
import com.huige.library.utils.DeviceUtils
import com.huige.library.utils.StatusBarUtil
import com.huige.library.utils.ToastUtils
import com.huige.library.widget.LimitScrollView
import com.lxj.xpopup.core.BasePopupView
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import com.umeng.socialize.media.UMWeb
import kotlinx.android.synthetic.main.activity_enrichment_tree.*
import okhttp3.Call
import org.litepal.LitePal
import org.simple.eventbus.Subscriber
import java.io.IOException
import java.util.*

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/12 6:49 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 发财树
 * </pre>
 */
class EnrichmentTreeActivity : BaseActivity() {

    private val mOkHttpCommon = OkHttpCommon()
    // 可抢种子列表
    private var mCanGrabSeedList = mutableListOf<SeedBean>()
    // 种子加速时间
    private var mSeedSpeedTime = ""
    // 生长激素
    private var mHormone = 0L
    /**
     * 当前抢到该时间段
     */
    private var mCurrentGrabSeed = 0L
    // 加载中
    private lateinit var loadingDialog: BasePopupView

    override fun setLayoutResId(): Int = R.layout.activity_enrichment_tree

    override fun init() {
        StatusBarUtil.transparencyBar(this)

        // 初始化10颗种子飘
        seedLayout.setSeedBeans(mutableListOf(SeedBean(), SeedBean(), SeedBean(), SeedBean(), SeedBean(), SeedBean(), SeedBean(), SeedBean(), SeedBean(), SeedBean()))

        loadingDialog = SimpleDialog.createDialog(this@EnrichmentTreeActivity, "领取中", false,true,true)

    }

    override fun initListener() {
        iv_back.setOnClickListener { finish() }

        // 去挂卖
        iv_sell.setOnClickListener { ActivityUtils.getInstance().jumpActivity(MySeedActivity::class.java) }
        // 去抢购
        iv_pay.setOnClickListener { ActivityUtils.getInstance().jumpActivity(SaleSeedActivity::class.java) }
        // 去获取
        iv_share.setOnClickListener { shareUM() }

        // 攻略
        iv_help.setOnClickListener { ActivityUtils.getInstance().jumpH5Activity("发财攻略", ConstantsUrl.URL_PROTOCOL + "发财攻略") }

        // 我的发财种子
        layout_seed.setOnClickListener { ActivityUtils.getInstance().jumpActivity(MySeedActivity::class.java) }

        // 生长激素
        layout_hormone.setOnClickListener {
            ToastUtils.showToast("$mSeedSpeedTime")
        }

        // 发财树上的种子点击
        seedLayout.setOnSeedItemClickListener { view, seedBean ->
            //           seedLayout.removeSeed(view)
            canGrab()
        }

        // 发财树
        iv_tree.setOnClickListener {
            it.startAnimation(TranslateAnimation(-10f, 10f, 10f, -10f).apply {
                interpolator = LinearInterpolator()
                duration = 200
                repeatCount = 1
            })
        }
    }


    override fun loadData() {

        getSeedCount()
        getSeedAds()
        getGrabCurrentSeed()
        getSeedSpeedTime()

    }

    override fun onResume() {
        super.onResume()
        limitAds?.startScroll()
    }

    override fun onStop() {
        super.onStop()
        limitAds?.stopScroll()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }

    /**
     * 友盟分享
     */
    private fun shareUM() {
        val member = CommonUtils.getMember()
        val memberNo = member!!.memberNo
        val url = ConstantsUrl.SHARE_URL_HEADER + "memberNo=" + memberNo
        val umWeb = UMWeb(url)
        umWeb.title = "邀请观赏发财树"//标题
        umWeb.setThumb(UMImage(this, R.mipmap.icon_share_seed).apply { compressFormat = Bitmap.CompressFormat.PNG })  //缩略图
        umWeb.description = "告诉你一个种植发财树的秘籍：好友越多，发财树越茂盛，发财种子生长得越快，快来【促销王】观赏发财树，抢免费的发财种子吧！"
        ShareAction(this).withMedia(umWeb)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                .setCallback(object : UMShareListener {
                    override fun onStart(share_media: SHARE_MEDIA) {
                        ToastUtils.showToast("正在启动分享,请稍后!")
                    }

                    override fun onResult(share_media: SHARE_MEDIA) {
                        ToastUtils.showToast("分享成功")
                    }

                    override fun onError(share_media: SHARE_MEDIA, throwable: Throwable) {
                        ToastUtils.showToast("分享失败")
                    }

                    override fun onCancel(share_media: SHARE_MEDIA) {
                        ToastUtils.showToast("您已取消分享")
                    }
                }).open()
    }

    /**
     * 微信分享
     */
    @Subscriber(tag = EventTags.WECHAT_SHARE_RESULT)
    fun onShareWechatResult(result: Boolean) {
        if (result)
            ToastUtils.showToast("分享成功")
    }

    /**
     * 获取种子加速时间
     */
    private fun getSeedSpeedTime() {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_SEED_SPEED_TIME, CommonUtils.createParams(), object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                jsonObject?.let {
                    if (it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", getString(R.string.net_error)))
                        return
                    }

                    mSeedSpeedTime = it.get("data").asString
                }
            }
        })
    }

    /**
     * 种子数量
     */
    private fun getSeedCount() {
        NetComment.getMemberInfo {
            tv_seed_num.text = it.seedCount.toString()
            mHormone = it.dayHormone.toLong()
            tv_hormone_num.text = "${it.hormone / 1000.0}"
        }
    }

    /**
     * 当前抢到哪颗种子
     */
    private fun getGrabCurrentSeed() {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_GRAB_CURRENT_SEED, CommonUtils.createParams(), object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                jsonObject?.let {
                    if (it.get("status").asInt == 1) {
                        var seedBean = GsonUtils.getGson().fromJson<SeedBean>(it.get("data").asJsonObject.toString(), SeedBean::class.java)
                        mCurrentGrabSeed = seedBean.downTime
                        getCanGrabSeedData()
                    }
                }
            }
        })
    }


    /**
     * 获取可抢所有种子
     */
    private fun getCanGrabSeedData() {
        // 可抢种子列表
        var list = LitePal.where("downTime >= ?", mCurrentGrabSeed.toString())
                .find(SeedBean::class.java) as List<SeedBean>
        mCanGrabSeedList.addAll(list)
    }

    private var countTemp = 0

    /**
     * 是否可抢
     */
    private fun canGrab() {
        // 时间段维护 03:00~05:00
        var calendar = Calendar.getInstance().apply { System.currentTimeMillis() }
        if(calendar.get(Calendar.HOUR_OF_DAY) in 3..5 ) {
            showToast("很遗憾，种子被抢走了，增加生长激素有更大的几率抢到种子")
            return
        }

        // 没有可抢种子
        if (mCanGrabSeedList.isEmpty()) {
            showToast("很遗憾，种子被抢走了，增加生长激素有更大的几率抢到种子")
            return
        }
        countTemp = 0
        mCanGrabSeedList.forEach {
            Log.d("msg", (System.currentTimeMillis() - mHormone >= it.downTime).toString())
            countTemp++ // 循坏次数
            if (it.used == 0 && System.currentTimeMillis() - mHormone >= it.downTime) {
                // 未使用 并且到可抢时间
                grabSeed(it)
                return
            }
        }

        if(countTemp == mCanGrabSeedList.size) { // 此时没有
            showToast("很遗憾，种子被抢走了，增加生长激素有更大的几率抢到种子")
        }
    }

    /**
     * 消息提示
     */
    private fun showToast(msg:String){
        SimpleDialog.showDialog(this, msg, 3000)
    }

    /**
     * 抢种子
     */
    private fun grabSeed(seedBean: SeedBean) {
        loadingDialog.show()
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_GRAB_SEED, CommonUtils.createParams().apply {
            put("mtSeedNo", seedBean.mtSeedNo)
        }, object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
                loadingDialog.dismiss()
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                loadingDialog.dismiss()
                showLoading(seedBean, jsonObject)
            }
        })
    }

    /**
     * 加载圈
     */
    private fun showLoading(seedBean: SeedBean, jsonObject: JsonObject?) {
        jsonObject?.let {
            var status = it.get("status").asInt
            when (status) {
                0 -> showToast(GsonUtils.parseJson(it, "msg", getString(R.string.net_error)))
                1 -> {
                    // 领取成功
                    showToast(GsonUtils.parseJson(it, "msg", "领取成功"))
                    getSeedCount()
                    mCanGrabSeedList.remove(seedBean)
                }
                3 -> showToast(GsonUtils.parseJson(it, "msg", "种子被领取"))
                4 -> showToast(GsonUtils.parseJson(it, "msg", "种子领取失败"))
                else -> showToast(GsonUtils.parseJson(it, "msg", getString(R.string.net_error)))
            }
        }
    }

    /**
     * 种子领取广告
     */
    private fun getSeedAds() {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_SEED_ADS, CommonUtils.createParams(), object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                jsonObject?.let {
                    if (jsonObject.get("status").asInt == 1) {

                        var jsonArray = jsonObject.get("data").asJsonObject.get("rows").asJsonArray
                        var list = mutableListOf<String>()

                        for (i in 0 until jsonArray.size()) {
                            var obj = jsonArray[i].asJsonObject
                            var nickname = GsonUtils.parseJson(obj, "nickname", "")
                            var used = GsonUtils.parseJson(obj, "used", "")
//                            list.add(if (used == "1") "恭喜${nickname}使用生长激素加速1秒抢到一颗种子" else "恭喜${nickname}抢到一颗发财种子")
                            list.add("恭喜${nickname}抢到一颗发财种子")
                        }

                        setSeedAdsData(list)
                    }
                }
            }
        })
    }

    /**
     * 种子领取广告设置数据
     */
    private fun setSeedAdsData(list: MutableList<String>) {
        if (list.isEmpty()) return

        limitAds.setAdapter(object : LimitScrollView.LimitScrollViewAdapter {
            override fun getView(position: Int): View {
                val tv = TextView(this@EnrichmentTreeActivity)
                tv.setPadding(0, DeviceUtils.dp2px(this@EnrichmentTreeActivity, 8f), 0, DeviceUtils.dp2px(this@EnrichmentTreeActivity, 8f))
                tv.text = list[position]
                tv.gravity = Gravity.CENTER
                tv.setTextColor(Color.WHITE)
                tv.textSize = 10f
                return tv
            }

            override fun getCount(): Int = list.size
        })
        limitAds.startScroll()
    }

}
