package com.yimeng.haidou.video

import android.annotation.SuppressLint
import android.graphics.drawable.AnimationDrawable
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.TimeUtils
import com.google.gson.JsonObject
import com.huige.library.utils.DeviceUtils
import com.huige.library.utils.SharedPreferencesUtils
import com.huige.library.utils.ToastUtils
import com.lrad.adManager.LanRenManager
import com.lrad.adManager.LoadAdError
import com.lrad.adSource.INativeProvider
import com.lrad.adlistener.ILanRenNativeAdListener
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.qq.e.ads.cfg.VideoOption
import com.qq.e.ads.nativ.NativeADUnifiedListener
import com.qq.e.ads.nativ.NativeUnifiedAD
import com.qq.e.ads.nativ.NativeUnifiedADData
import com.qq.e.comm.util.AdError
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.utils.Debuger
import com.yimeng.adapter.VideoPagerAdapter
import com.yimeng.base.BaseFragment
import com.yimeng.config.Constants
import com.yimeng.config.ConstantsUrl
import com.yimeng.config.EventTags
import com.yimeng.config.XJConfig
import com.yimeng.dialog.EvalInputDialog
import com.yimeng.dialog.EvalVideoDialog
import com.yimeng.dialog.SimpleDialog
import com.yimeng.entity.Member
import com.yimeng.entity.VideoBean
import com.yimeng.haidou.R
import com.yimeng.haidou.mine.UserInfoActivity
import com.yimeng.haidou.task.TaskSendOrderActivity
import com.yimeng.interfaces.OnViewPagerListener
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.retorfit.ResponseBase
import com.yimeng.retorfit.ResponseListData
import com.yimeng.retorfit.RetrofitHelper
import com.yimeng.utils.*
import com.yimeng.widget.DragView
import com.yimeng.widget.SamplePagerCoverVideo
import kotlinx.android.synthetic.main.fragment_tab_video_pager.*
import kotlinx.android.synthetic.main.fragment_tab_video_pager.recyclerView
import kotlinx.android.synthetic.main.fragment_tab_video_pager.smartRefresh
import kotlinx.android.synthetic.main.fragment_tab_video_pager.tv_count
import kotlinx.android.synthetic.main.fragment_tab_video_pager_sideslip.*
import kotlinx.android.synthetic.main.navigationview_header.*
import org.simple.eventbus.EventBus
import org.simple.eventbus.Subscriber
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-05 21:17.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
class TabVideoPagerFragment : BaseFragment() {

    private var mStart = 0
    private val mLimit = 50
    private var AD_COUNT = 10

    /** 分享视屏id */
    private var shareVideoNo: String? = ""

    /** 评论列表弹窗 */
    private var commentsDialog: BasePopupView? = null

    private val mLayoutManager by lazy { ViewPagerLayoutManager(activity!!) }
    private val mList = mutableListOf<VideoBean>()
    private val mAdapter by lazy { VideoPagerAdapter(mList, activity!!) }

//    private var mAdManager: NativeUnifiedAD? = null

    /** 广告集合 */
    private val mAdsList = mutableListOf<INativeProvider>()

    private val max_count = 5 * 60
    private var curr = 5 * 60
    private var isStop = false

    // 是否分享
    private var isShare = false

    // 第一次播放
    private var isFatstPlay = true

    /**
     * 广告间隔
     */
    private var mAdLimit = 5

    private val mHandler: Handler by lazy {
        @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what == 1 && !isStop) {
                    if (curr <= 0) {
                        lookAdComplete()
                    } else {
                        curr--
                        Log.d("huiger", "计时：$curr s")
                        tv_count.text = "$curr s"
                        mHandler.sendEmptyMessageDelayed(1, 1000)
                    }
                }
            }
        }
    }

//    override fun setLayoutResId(): Int = R.layout.fragment_tab_video_pager

    override fun setLayoutResId(): Int = R.layout.fragment_tab_video_pager_sideslip


    override fun init() {
        recyclerView.run {
            layoutManager = mLayoutManager
            adapter = mAdapter
        }
        mAdapter.bindToRecyclerView(recyclerView)

//        mAdManager = NativeUnifiedAD(activity!!, XJConfig.GDTAD_AD_ID, this).apply {
//
//            /**
//             * 如果广告位支持视频广告，强烈建议在调用loadData请求广告前，调用下面两个方法，有助于提高视频广告的eCPM值 <br></br>
//             * 如果广告位仅支持图文广告，则无需调用
//             */
//            /**
//             * 设置本次拉取的视频广告，从用户角度看到的视频播放策略
//             *
//             *
//             *
//             * "用户角度"特指用户看到的情况，并非SDK是否自动播放，与自动播放策略AutoPlayPolicy的取值并非一一对应 <br></br>
//             *
//             * 例如开发者设置了VideoOption.AutoPlayPolicy.NEVER，表示从不自动播放 <br></br>
//             * 但满足某种条件(如晚上10点)时，开发者调用了startVideo播放视频，这在用户看来仍然是自动播放的
//             */
//            setVideoPlayPolicy(VideoOption.VideoPlayPolicy.AUTO) // 本次拉回的视频广告，在用户看来是否为自动播放的
//
//            /**
//             * 设置在视频广告播放前，用户看到显示广告容器的渲染者是SDK还是开发者
//             *
//             *
//             *
//             * 一般来说，用户看到的广告容器都是SDK渲染的，但存在下面这种特殊情况： <br></br>
//             *
//             * 1. 开发者将广告拉回后，未调用bindMediaView，而是用自己的ImageView显示视频的封面图 <br></br>
//             * 2. 用户点击封面图后，打开一个新的页面，调用bindMediaView，此时才会用到SDK的容器 <br></br>
//             * 3. 这种情形下，用户先看到的广告容器就是开发者自己渲染的，其值为VideoADContainerRender.DEV
//             * 4. 如果觉得抽象，可以参考NativeADUnifiedDevRenderContainerActivity的实现
//             */
//            setVideoADContainerRender(VideoOption.VideoADContainerRender.SDK) // 视频播放前，用户看到的广告容器是由SDK渲染的
//        }

        if (videoIsComplete()) { // 已完成
            tv_count.visibility = View.GONE
        } else {
            mHandler.sendEmptyMessageDelayed(1, 1000)
        }

        Debuger.enable()

    }

    private fun videoIsComplete(): Boolean {
        var result = false
        CommonUtils.getMember()?.run {
            result = TimeUtils.isToday(amtTime)
        }

        return result
    }

    override fun loadData() {

        getAdLimit()

        getVideo()

//        showShareIcon()

        val userInfo = SharedPreferencesUtils.get(Constants.USER_INFO, "") as String
        val member: Member? = GsonUtils.getGson().fromJson(userInfo, Member::class.java)

        val tvName = navigation_view.getHeaderView(0).findViewById<TextView>(R.id.tv_name)
        tvName.text = member?.memberName
    }

    override fun initListener() {
        smartRefresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mStart += mLimit
                getVideo()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mStart = 0
                if (mList.isNotEmpty()) mList.clear()
                if (mAdsList.isNotEmpty()) mAdsList.clear()
                getVideo()
            }
        })

        mLayoutManager.setOnViewPagerListener(object : OnViewPagerListener {
            override fun onInitComplete() {
                Log.d("huiger", "onInitComplete")
//                if(isVideoInit) return
//                isVideoInit = true

                if (mAdapter.getItem(0)?.itemType == XJConfig.ADAPTER_ITEM_VIDEO) {
                    playPosition(0)
                }
            }

            override fun onPageRelease(isNext: Boolean, position: Int) {
                Log.d("huiger", "onPageRelease  isNext=$isNext  position=$position")
//                val index = if (isNext) 0 else 1
//                releaseVideo(index)

                if (isFatstPlay) return
                releaseVideo(if (isNext) 0 else 1)
            }

            override fun onPageSelected(position: Int, isBottom: Boolean) {
//                val index = if (isBottom) 1 else 0
//                playPosition(index)
                Log.d("huiger-msg", "TabVideoPagerFragment -> onPageSelected: position=$position  isBottom=$isBottom")

                isFatstPlay = false
                videoSeek = 0

                playVideoPosition = position

                if (mAdapter.getItem(0)?.itemType == XJConfig.ADAPTER_ITEM_VIDEO) {
                    playPosition(0)
                }
            }
        })

        mAdapter.setOnItemChildClickListener { _, view, position ->
            val videoNo = mList[position].videoNo
            when (view.id) { // 评论列表
                R.id.tv_comment_num -> {
                    showCommentsDialog(videoNo)
                }

                R.id.tv_input -> {
                    showInputDialog(videoNo)
                }

                R.id.tv_like_num -> { // 点赞
                    likeVideo(position)
                }

                R.id.tv_share_num -> { //分享
                    shareVideoNo = videoNo
                    isShare = true
                    CommonUtils.shareApp()
                }
            }
        }

        iv_my.setOnClickListener {
            drawer_layout.openDrawer(Gravity.START);
        }

        navigation_view.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.single_1 -> {
                    ActivityUtils.getInstance().jumpActivity(UserInfoActivity::class.java)
                }
            }
            false
        }
    }


    private fun getVideo() {
        RetrofitHelper.toDo()
                .getVideo(mStart, mLimit)
                .enqueue(object : Callback<ResponseListData<VideoBean>> {
                    override fun onFailure(call: Call<ResponseListData<VideoBean>>, t: Throwable) {
                        if (mStart == 0) smartRefresh.finishRefresh(false)
                        else smartRefresh.finishLoadMore(false)
                    }

                    override fun onResponse(call: Call<ResponseListData<VideoBean>>, response: Response<ResponseListData<VideoBean>>) {
                        SimpleDialog.hideLoading()
                        if (RetrofitHelper.isSuccess(response)) {
                            if (mStart == 0) smartRefresh.finishRefresh()
                            else smartRefresh.finishLoadMore()

                            response.body()?.data?.rows?.run {
                                if (this.size < mLimit) {
                                    smartRefresh.finishLoadMoreWithNoMoreData()
                                }
                                this.forEach {
                                    if (!it.videoUrl.startsWith("http")) {
                                        it.videoUrl = ConstantsUrl.UPLOAD_HEAD_URL + it.videoUrl
                                    }
                                    if (it.videoPoster != null && !it.videoPoster.startsWith("http")) {
                                        it.videoPoster = ConstantsUrl.UPLOAD_HEAD_URL + it.videoPoster
                                    }
                                    mList.add(it)
                                }

                                mAdapter.notifyDataSetChanged()
//                                playPosition(0)

                                Log.d("huiger-msg", "TabVideoPagerFragment -> onResponse: 请求广告")
                                // 获取广告
                                AD_COUNT = this.size / mAdLimit
                                if (AD_COUNT < 1) AD_COUNT = 1
//                                mAdManager?.loadData(AD_COUNT)
//                                requestData(AD_COUNT)
                            }

                        } else {
                            if (mStart == 0) smartRefresh.finishRefresh(false)
                            else smartRefresh.finishLoadMore(false)
                        }
                    }
                })
    }

    private fun requestData(adCount: Int) {
        LanRenManager.getInstance().loadNative(activity, "20000060", adCount, object : ILanRenNativeAdListener {
            override fun onAdLoad(p0: INativeProvider?) {

            }

            override fun onAdLoadList(list: MutableList<INativeProvider>?) {
                list?.let {
                    mAdsList.addAll(list)
                    addAds()
                }
            }

            override fun onAdExpose() {
            }

            override fun onAdClick() {

            }

            override fun onAdVideoPlayStart() {

            }

            override fun onAdClose() {

            }

            override fun onAdVideoPlayComplete() {

            }

            override fun onAdError(p0: LoadAdError?) {

            }
        })
    }

    /**
     * 获取广告间隔
     */
    private fun getAdLimit() {
        val params = CommonUtils.createParams()
        params["token"] = CommonUtils.getToken()
        params["menuNo"] = "shipinCount"

        OkHttpCommon().postLoadData(activity, ConstantsUrl.URL_ACTIVATE, params, object : CallbackCommon {
            override fun onFailure(call: okhttp3.Call, e: IOException) {}

            @Throws(IOException::class)
            override fun onResponse(call: okhttp3.Call, jsonObject: JsonObject) {
                if (jsonObject["status"].asInt == 1) {
                    mAdLimit = GsonUtils.parseJson(jsonObject["data"].asJsonObject, "introduction", "5").toInt()
                    Log.d("huiger", "获取到广告间隔数量为：$mAdLimit")
                }
            }
        })
    }


    /**
     * 加入广告
     */
    private fun addAds() {
        val page = mStart / mLimit

//        val random = Random()
//        for (i in AD_COUNT * page until mAdsList.size) {
//            val adData = mAdsList[i]
//            Log.d("huiger-msg", "TabVideoPagerFragment -> addAds: ${adData.iconUrl}")
//            var index = Math.abs(random.nextInt()) % mLimit + mLimit * page
//            if (index > mList.size) {
//                index = mList.size
//            }
//            if (index == 0) {
//                index = 1
//            }
//            mList.add(index, VideoBean(adData))
//            Log.d("huiger-msg", "TabVideoPagerFragment -> addAds: 插入广告成功，下标:$index")
//        }

        if (mList.size <= mAdLimit) {
            mAdsList.forEach { adData ->
                mList.add(mList.size, VideoBean(adData))
            }
        } else {
            var index = 0
//            mList.forEachIndexed { index, _ ->
//                if ((index + 1) % mAdLimit == 0) {
//                    count++
//                    mList.add(VideoBean(mAdsList[count]))
//                }
//            }
            mAdsList.forEachIndexed { i, it ->
                if (i == 0) {
                    index = mAdLimit
                } else {
                    index += mAdLimit + 1
                }
                if (index > mList.size) {
                    index = mList.size
                }
                Log.d("huiger", "插入下标$index")
                mList.add(index, VideoBean(it))
            }
        }


        mAdapter.notifyDataSetChanged()
    }

    /**
     * 当前播放的视频下标
     */
    private var playVideoPosition = 0

    /**
     * 播放下标
     */
    private fun playPosition(position: Int) {
        Log.d("huiger-msg", "TabVideoPagerFragment -> playPosition: 当前播放下标：$position")
//        mAdapter.playPosition(position)
        recyclerView.getChildAt(0)?.findViewById<SamplePagerCoverVideo>(R.id.videoPlayer)?.startPlayLogic()
        if (isStop) {
            isStop = false
            mHandler.sendEmptyMessage(1)
        }
    }

    private fun releaseVideo(position: Int) {
        Log.d("huiger-msg", "TabVideoPagerFragment -> releaseVideo: 暂停$position")
        recyclerView.getChildAt(position)?.findViewById<SamplePagerCoverVideo>(R.id.videoPlayer)?.onVideoPause()
    }

//    override fun onADLoaded(list: MutableList<NativeUnifiedADData>) {
//        Log.d("huiger-msg", "TabVideoPagerFragment -> onADLoaded: 获取广告视频数量：${list.size}")
//        mAdsList.addAll(list)
//        addAds()
//    }

//    override fun onNoAD(err: AdError) {
//        Log.d("huiger-msg", "TabVideoPagerFragment -> onNoAD: code=${err.errorCode}  msg=${err.errorMsg}")
//    }

    /**
     * 显示输入评论弹窗
     */
    private fun showCommentsDialog(videoNo: String) {
        commentsDialog = XPopup.Builder(activity)
                .moveUpToKeyboard(false)
                .asCustom(EvalVideoDialog(activity!!, videoNo) {
                    commentVideo(videoNo, it)
                })
                .show()
    }

    /**
     * 直接评论
     */
    private fun showInputDialog(videoNo: String) {
        XPopup.Builder(context)
                .autoOpenSoftInput(true)
                .asCustom(EvalInputDialog(activity!!) {
                    commentVideo(videoNo, it)
                })
                .show()
    }

    // 当前视频播放进度
    private var videoSeek = 0L

    /**
     * 点赞视频
     */
    private fun likeVideo(position: Int) {
        val videoBean = mList[position]
        RetrofitHelper.toDo().videoCollect(videoBean.videoNo)
                .enqueue(object : Callback<ResponseBase> {
                    override fun onFailure(call: Call<ResponseBase>, t: Throwable) {
                        showToast(RetrofitHelper.getErrMsg(t))
                    }

                    override fun onResponse(call: Call<ResponseBase>, response: Response<ResponseBase>) {
                        if (RetrofitHelper.isSuccess(response)) {
                            if (videoBean.collected == 1) {
                                videoBean.collected = 0
                            } else {
                                showToast("点赞成功")
                                videoBean.collected = 1
                            }
                            videoSeek = GSYVideoManager.instance().currentPosition
//                            mAdapter.notifyItemChanged(position)
                        } else {
                            showToast("点赞失败，请稍后重试")
                        }
                    }
                })
    }

    /**
     * 视频评论
     */
    private fun commentVideo(videoNo: String, comment: String) {
        RetrofitHelper.toDo().videoCommect(videoNo, comment)
                .enqueue(object : Callback<ResponseBase> {
                    override fun onFailure(call: Call<ResponseBase>, t: Throwable) {
                        showToast("评论失败，请稍后重试")
                    }

                    override fun onResponse(call: Call<ResponseBase>, response: Response<ResponseBase>) {
                        if (RetrofitHelper.isSuccess(response)) {
                            showToast("评论成功！")
                            commentsDialog?.let { (it as EvalVideoDialog).refreshComments() }
                        } else {
                            showToast("评论失败，请稍后重试")
                        }
                    }

                })
    }

    /**
     * 分享回调
     */
    private fun shareVideo(videoNo: String?) {
        if (videoNo.isNullOrBlank()) return

        RetrofitHelper.toDo().videoShare(videoNo).enqueue(object : Callback<ResponseBase> {
            override fun onFailure(call: Call<ResponseBase>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponseBase>, response: Response<ResponseBase>) {
                if (RetrofitHelper.isSuccess(response)) {
                    val videoBean = mAdapter.getItem(playVideoPosition)
                    if (videoBean?.itemType == XJConfig.ADAPTER_ITEM_VIDEO) {
                        videoBean.shareNum += 1
                        mAdapter.notifyItemChanged(playVideoPosition)
                    }
                }
            }
        })

    }

    /**
     * 视频任务完成
     */
    private fun lookAdComplete() {
        val params = CommonUtils.createParams()
        params["commonTaskNo"] = "20200326200734383meiri"
        OkHttpCommon().postLoadData(activity, ConstantsUrl.URL_TASK_COMPLETE, params, object : CallbackCommon {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                smartRefresh.finishRefresh()
            }

            @Throws(IOException::class)
            override fun onResponse(call: okhttp3.Call, jsonObject: JsonObject) {
                if (jsonObject["status"].asInt != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取数据失败!"))
                } else {
                    ToastUtils.showToast("任务完成！")
                }
            }
        })
    }

    /**
     * 微信分享
     */
    @Subscriber(tag = EventTags.WECHAT_SHARE_RESULT)
    fun onShareWechatResult(result: Boolean) {
        if (shareVideoNo.isNullOrBlank()) {
            return
        }
        EventBus.getDefault().clear()
        shareVideoNo = ""

        shareVideo(shareVideoNo)
    }

    private fun showShareIcon() {
//        val iv = ImageView(activity)
//        iv.setImageResource(R.mipmap.scan_redpacket) //icon_share_red_packer  icon_red_packer_wall  icon_red_packer_success
//        //iv.setImageResource(R.mipmap.icon_share_make_money);
//        iv.setOnClickListener(View.OnClickListener {
//            if (!CommonUtils.checkLogin()) {
//                //Log.d("showShareIcon", "ActivityUtils.getInstance().jumpLoginActivity() ")
//                ActivityUtils.getInstance().jumpLoginActivity()
//                return@OnClickListener
//            }
//            ActivityUtils.getInstance().jumpShareMakeMoney(activity)
//            //Log.d("showShareIcon", "jumpShareMakeMoney(getActivity() end")
//        })

//        val giv = GifImageView(activity)
//        giv.setImageResource(R.mipmap.money_redpacket)
//        giv.setOnClickListener { view: View? ->
//            if (!CommonUtils.checkLogin()) {
//                ActivityUtils.getInstance().jumpLoginActivity()
//                return@setOnClickListener
//            }
//            ActivityUtils.getInstance().jumpH5Activity(
//                "",
//                ConstantsUrl.URL_SCAN_GETREDPACKET + CommonUtils.getMember().memberNo
//            )
//        }

        val iv = ImageView(activity)
        iv.setBackgroundResource(R.drawable.animation_money)
        val animation = iv.background as AnimationDrawable
        animation.isOneShot = false
        animation.start()
        iv.setOnClickListener { view: View? ->
            if (!CommonUtils.checkLogin()) {
                ActivityUtils.getInstance().jumpLoginActivity()
                return@setOnClickListener
            }
            ActivityUtils.getInstance()
                    .jumpH5Activity("", ConstantsUrl.URL_SCAN_GETREDPACKET + CommonUtils.getMember().memberNo)
        }

        DragView.Builder()
                .setActivity(activity)
//            .setDefaultTop((DeviceUtils.getWindowHeight(activity) * 0.6).toInt())
//            .setDefaultLeft((DeviceUtils.getWindowWidth(activity) * 0.8).toInt())
                .setDefaultTop((DeviceUtils.getWindowHeight(activity) * 0.15).toInt())
                .setDefaultLeft((DeviceUtils.getWindowWidth(activity) * 0.78).toInt())
                .setSize(300)
                .setView(iv)
                .setNeedNearEdge(true)
                .build()

        //Log.d("showShareIcon", "DragView.Builder")
    }

    /*--------------------------------- 生命周期 ---------------------------------*/

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            isStop = true
            if (mList.size > playVideoPosition && mList[playVideoPosition].itemType == XJConfig.ADAPTER_ITEM_VIDEO) {
                GSYVideoManager.onPause()
            }
        } else {
            isStop = false
            if (!videoIsComplete()) { // 未完成
                mHandler.sendEmptyMessage(1)
            }

            if (mList.size > playVideoPosition && mList[playVideoPosition].itemType == XJConfig.ADAPTER_ITEM_VIDEO) {
                GSYVideoManager.onResume()
            }
            mAdsList.forEach {
                it.onResume()
            }
        }

        Log.d("huiger", "onHiddenChanged $hidden")
    }


    /**
     * 计时器
     * @param stop true 暂停
     */
    @Subscriber(tag = "video_stop")
    fun onVodeoStop(stop: Boolean) {
        Log.d("huiger", "收到状态：$stop")
        isStop = stop
        mHandler.sendEmptyMessage(1)
    }

    override fun onBackPressed(): Boolean {
        if (GSYVideoManager.backFromWindowFull(activity)) {
            return false
        }
        return super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        if (mList.size > playVideoPosition && mList[playVideoPosition].itemType == XJConfig.ADAPTER_ITEM_VIDEO) {
            GSYVideoManager.onPause()
        }
        if (curr != max_count) {
            isStop = true
        }

        Log.d("huiger", "onPause  isStop=$isStop")
    }

    override fun onResume() {
        super.onResume()
        if (isShare) {
            if (mList.size > playVideoPosition && mList[playVideoPosition].itemType == XJConfig.ADAPTER_ITEM_VIDEO) {
                GSYVideoManager.onResume()
            }
            mAdsList.forEach {
                it.onResume()
            }
            isStop = false
            mHandler.sendEmptyMessage(1)
            isShare = false
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
        mAdsList.forEach {
            it.destroy()
        }
    }


}
