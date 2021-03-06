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

    /** ????????????id */
    private var shareVideoNo: String? = ""

    /** ?????????????????? */
    private var commentsDialog: BasePopupView? = null

    private val mLayoutManager by lazy { ViewPagerLayoutManager(activity!!) }
    private val mList = mutableListOf<VideoBean>()
    private val mAdapter by lazy { VideoPagerAdapter(mList, activity!!) }

//    private var mAdManager: NativeUnifiedAD? = null

    /** ???????????? */
    private val mAdsList = mutableListOf<INativeProvider>()

    private val max_count = 5 * 60
    private var curr = 5 * 60
    private var isStop = false

    // ????????????
    private var isShare = false

    // ???????????????
    private var isFatstPlay = true

    /**
     * ????????????
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
                        Log.d("huiger", "?????????$curr s")
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
//             * ?????????????????????????????????????????????????????????loadData???????????????????????????????????????????????????????????????????????????eCPM??? <br></br>
//             * ??????????????????????????????????????????????????????
//             */
//            /**
//             * ??????????????????????????????????????????????????????????????????????????????
//             *
//             *
//             *
//             * "????????????"????????????????????????????????????SDK??????????????????????????????????????????AutoPlayPolicy??????????????????????????? <br></br>
//             *
//             * ????????????????????????VideoOption.AutoPlayPolicy.NEVER??????????????????????????? <br></br>
//             * ?????????????????????(?????????10???)????????????????????????startVideo?????????????????????????????????????????????????????????
//             */
//            setVideoPlayPolicy(VideoOption.VideoPlayPolicy.AUTO) // ?????????????????????????????????????????????????????????????????????
//
//            /**
//             * ??????????????????????????????????????????????????????????????????????????????SDK???????????????
//             *
//             *
//             *
//             * ????????????????????????????????????????????????SDK???????????????????????????????????????????????? <br></br>
//             *
//             * 1. ???????????????????????????????????????bindMediaView?????????????????????ImageView???????????????????????? <br></br>
//             * 2. ????????????????????????????????????????????????????????????bindMediaView?????????????????????SDK????????? <br></br>
//             * 3. ??????????????????????????????????????????????????????????????????????????????????????????VideoADContainerRender.DEV
//             * 4. ?????????????????????????????????NativeADUnifiedDevRenderContainerActivity?????????
//             */
//            setVideoADContainerRender(VideoOption.VideoADContainerRender.SDK) // ???????????????????????????????????????????????????SDK?????????
//        }

        if (videoIsComplete()) { // ?????????
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
            when (view.id) { // ????????????
                R.id.tv_comment_num -> {
                    showCommentsDialog(videoNo)
                }

                R.id.tv_input -> {
                    showInputDialog(videoNo)
                }

                R.id.tv_like_num -> { // ??????
                    likeVideo(position)
                }

                R.id.tv_share_num -> { //??????
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

                                Log.d("huiger-msg", "TabVideoPagerFragment -> onResponse: ????????????")
                                // ????????????
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
     * ??????????????????
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
                    Log.d("huiger", "?????????????????????????????????$mAdLimit")
                }
            }
        })
    }


    /**
     * ????????????
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
//            Log.d("huiger-msg", "TabVideoPagerFragment -> addAds: ???????????????????????????:$index")
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
                Log.d("huiger", "????????????$index")
                mList.add(index, VideoBean(it))
            }
        }


        mAdapter.notifyDataSetChanged()
    }

    /**
     * ???????????????????????????
     */
    private var playVideoPosition = 0

    /**
     * ????????????
     */
    private fun playPosition(position: Int) {
        Log.d("huiger-msg", "TabVideoPagerFragment -> playPosition: ?????????????????????$position")
//        mAdapter.playPosition(position)
        recyclerView.getChildAt(0)?.findViewById<SamplePagerCoverVideo>(R.id.videoPlayer)?.startPlayLogic()
        if (isStop) {
            isStop = false
            mHandler.sendEmptyMessage(1)
        }
    }

    private fun releaseVideo(position: Int) {
        Log.d("huiger-msg", "TabVideoPagerFragment -> releaseVideo: ??????$position")
        recyclerView.getChildAt(position)?.findViewById<SamplePagerCoverVideo>(R.id.videoPlayer)?.onVideoPause()
    }

//    override fun onADLoaded(list: MutableList<NativeUnifiedADData>) {
//        Log.d("huiger-msg", "TabVideoPagerFragment -> onADLoaded: ???????????????????????????${list.size}")
//        mAdsList.addAll(list)
//        addAds()
//    }

//    override fun onNoAD(err: AdError) {
//        Log.d("huiger-msg", "TabVideoPagerFragment -> onNoAD: code=${err.errorCode}  msg=${err.errorMsg}")
//    }

    /**
     * ????????????????????????
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
     * ????????????
     */
    private fun showInputDialog(videoNo: String) {
        XPopup.Builder(context)
                .autoOpenSoftInput(true)
                .asCustom(EvalInputDialog(activity!!) {
                    commentVideo(videoNo, it)
                })
                .show()
    }

    // ????????????????????????
    private var videoSeek = 0L

    /**
     * ????????????
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
                                showToast("????????????")
                                videoBean.collected = 1
                            }
                            videoSeek = GSYVideoManager.instance().currentPosition
//                            mAdapter.notifyItemChanged(position)
                        } else {
                            showToast("??????????????????????????????")
                        }
                    }
                })
    }

    /**
     * ????????????
     */
    private fun commentVideo(videoNo: String, comment: String) {
        RetrofitHelper.toDo().videoCommect(videoNo, comment)
                .enqueue(object : Callback<ResponseBase> {
                    override fun onFailure(call: Call<ResponseBase>, t: Throwable) {
                        showToast("??????????????????????????????")
                    }

                    override fun onResponse(call: Call<ResponseBase>, response: Response<ResponseBase>) {
                        if (RetrofitHelper.isSuccess(response)) {
                            showToast("???????????????")
                            commentsDialog?.let { (it as EvalVideoDialog).refreshComments() }
                        } else {
                            showToast("??????????????????????????????")
                        }
                    }

                })
    }

    /**
     * ????????????
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
     * ??????????????????
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
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "??????????????????!"))
                } else {
                    ToastUtils.showToast("???????????????")
                }
            }
        })
    }

    /**
     * ????????????
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

    /*--------------------------------- ???????????? ---------------------------------*/

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            isStop = true
            if (mList.size > playVideoPosition && mList[playVideoPosition].itemType == XJConfig.ADAPTER_ITEM_VIDEO) {
                GSYVideoManager.onPause()
            }
        } else {
            isStop = false
            if (!videoIsComplete()) { // ?????????
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
     * ?????????
     * @param stop true ??????
     */
    @Subscriber(tag = "video_stop")
    fun onVodeoStop(stop: Boolean) {
        Log.d("huiger", "???????????????$stop")
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
