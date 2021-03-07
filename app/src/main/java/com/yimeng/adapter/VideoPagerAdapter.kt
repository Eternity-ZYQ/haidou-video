package com.yimeng.adapter

import android.app.Activity
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.util.MultiTypeDelegate
import com.lrad.adSource.INativeProvider
import com.yimeng.config.XJConfig
import com.yimeng.entity.VideoBean
import com.yimeng.interfaces.SimpleNativeADMediaListener
import com.yimeng.utils.displayUrl
import com.yimeng.widget.SamplePagerCoverVideo
import com.yimeng.haidou.R
import com.qq.e.ads.cfg.VideoOption
import com.qq.e.ads.nativ.MediaView
import com.qq.e.ads.nativ.NativeADEventListener
import com.qq.e.ads.nativ.NativeUnifiedADData
import com.qq.e.comm.constants.AdPatternType
import com.qq.e.comm.util.AdError
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView
import org.simple.eventbus.EventBus

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-05 09:30.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 视频播放器
 * </pre>
 */
class VideoPagerAdapter : BaseQuickAdapter<VideoBean, BaseViewHolder> {

    private var mActivity: Activity? = null

    constructor(list: MutableList<VideoBean>, activity: Activity) : super(list) {

        mActivity = activity

        multiTypeDelegate = object : MultiTypeDelegate<VideoBean>() {
            override fun getItemType(item: VideoBean): Int = item.itemType
        }

        multiTypeDelegate
                .registerItemType(XJConfig.ADAPTER_ITEM_VIDEO, R.layout.adapter_video_pager_item)
                .registerItemType(XJConfig.ADAPTER_ITEM_ADS, R.layout.adapter_video_ad_pager_item)

    }


    override fun convert(helper: BaseViewHolder, item: VideoBean) {

        when (item.itemType) {
            XJConfig.ADAPTER_ITEM_VIDEO -> {
                setVideoInfo(helper, item)
            }

            XJConfig.ADAPTER_ITEM_ADS -> {
                setVideoAds(helper, item)
            }
        }

    }


    /**
     * 设置视频数据
     */
    private fun setVideoInfo(helper: BaseViewHolder, item: VideoBean) {
        item.let {
            val videoPlayer = helper.getView<SamplePagerCoverVideo>(R.id.videoPlayer)

            videoPlayer.run {
                // 隐藏返回键
                backButton.visibility = View.GONE
                // 设置全屏按钮
                fullscreenButton?.setOnClickListener {
                    startWindowFullscreen(mContext, true, true)
                }
                loadCoverImage(it.videoPoster, Color.parseColor("#cccccc"))
            }


            GSYVideoOptionBuilder()
                    .setIsTouchWiget(false)
                    //.setThumbImageView(imageView)
                    .setUrl(it.videoUrl)
                    .setVideoTitle(it.videoDesc)
                    .setCacheWithPlay(true)
                    .setRotateViewAuto(true)
                    .setLockLand(true)
                    .setPlayTag("${helper.adapterPosition}")
                    .setLooping(true)
                    .setThumbPlay(true)
//                    .setMapHeadData(header)
                    .setShowFullAnimation(true)
                    .setNeedLockFull(true)
                    .setPlayPosition(helper.adapterPosition)
                    .setVideoAllCallBack(object : GSYSampleCallBack() {
                        override fun onEnterFullscreen(url: String?, vararg objects: Any?) {
                            super.onEnterFullscreen(url, *objects)
                            GSYVideoManager.instance().isNeedMute = false
                            videoPlayer.currentPlayer.titleTextView.text = objects[0] as String
                        }

                        override fun onClickBlank(url: String?, vararg objects: Any?) {
                            super.onClickBlank(url, *objects)
                            if (videoPlayer.currentState == GSYVideoView.CURRENT_STATE_PAUSE) {
                                // 暂停
                                videoPlayer.onVideoResume()
                            } else if (videoPlayer.currentState == GSYVideoView.CURRENT_STATE_PLAYING) {
                                // 播放中
                                videoPlayer.onVideoPause()
                            }
                        }

                        override fun onClickStop(url: String?, vararg objects: Any?) {
                            super.onClickStop(url, *objects)
                            Log.d("huiger", "暂停")
                            EventBus.getDefault().post(true, "video_stop")
                        }

                        override fun onClickResume(url: String?, vararg objects: Any?) {
                            super.onClickResume(url, *objects)
                            Log.d("huiger", "开始播放")
                            EventBus.getDefault().post(false, "video_stop")
                        }

                    })
                    .build(videoPlayer)


//            helper.getView<ImageView>(R.id.civ_user_head).displayUrl(it.productImage)
            helper
                    .setText(R.id.tv_like_num, "${if (it.likeNum == 0) "点赞" else it.likeNum} ")
                    .setText(R.id.tv_comment_num, "${if (it.commentNum == 0) "评论" else it.commentNum}")
                    .setText(R.id.tv_share_num, "${if (it.shareNum == 0) "分享" else it.shareNum}")


            helper.getView<CheckBox>(R.id.tv_like_num).run {
                val drawable = if (it.collected == 1) {
                    ContextCompat.getDrawable(mContext, R.mipmap.icon_video_like)
                } else {
                    ContextCompat.getDrawable(mContext, R.mipmap.icon_video_unlike)
                }
                drawable!!.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
                setCompoundDrawables(null, drawable, null, null)

                setOnCheckedChangeListener { _, _ ->
                    val drawable = if (it.collected == 0) {
                        ContextCompat.getDrawable(mContext, R.mipmap.icon_video_like)
                    } else {
                        ContextCompat.getDrawable(mContext, R.mipmap.icon_video_unlike)
                    }
                    drawable!!.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
                    setCompoundDrawables(null, drawable, null, null)
                }

            }


            helper.addOnClickListener(R.id.tv_like_num)
                    .addOnClickListener(R.id.tv_comment_num)
                    .addOnClickListener(R.id.tv_share_num)
                    .addOnClickListener(R.id.tv_input)
        }
    }

    /**
     * 视屏广告
     */
    private fun setVideoAds(helper: BaseViewHolder, item: VideoBean) {
        item.ad?.run {
//            Log.d("huiger-msg", "VideoPagerAdapter -> setVideoAds: iconUrl=$iconUrl")
//            Log.d("huiger-msg", "VideoPagerAdapter -> setVideoAds: imgUrl=$imgUrl")

            helper.getView<ImageView>(R.id.img_logo).displayUrl(if (this.nativeLogo.isNullOrBlank()) this.nativeImage else this.nativeLogo)

            helper.setText(R.id.text_title, this.nativeTitel)
                    .setText(R.id.text_desc, this.nativeDesc)

//            mActivity?.let {
//                onCreateView(it, )
//            }

//                    .setText(R.id.btn_download, updateAdAction(this))
//            if (adPatternType == AdPatternType.NATIVE_VIDEO) { //视频广告
//                helper.setVisible(R.id.img_poster, false)
//                        .setVisible(R.id.gdt_media_view, true)
//                        .setBackgroundColor(R.id.ad_info_container, Color.parseColor("#00000000"))
//                        .setGone(R.id.ad_info_container, false)
//
//            } else {
//                helper.setVisible(R.id.img_poster, true)
//                        .setVisible(R.id.gdt_media_view, false)
//                        .setBackgroundColor(R.id.ad_info_container, Color.parseColor("#999999"))
//                        .setGone(R.id.ad_info_container, true)
//            }

//            val customClickableViews = mutableListOf<View>(
//            )
//            val clickableViews = mutableListOf<View>(
//                    helper.getView(R.id.btn_download)
//            )


            //作为customClickableViews传入，点击不进入详情页，直接下载或进入落地页，图文、视频广告均生效，
//            bindAdToView(mContext, helper.getView(R.id.native_ad_container), null,
//                    clickableViews, customClickableViews)

//            setNativeAdEventListener(object : NativeADEventListener {
//                override fun onADStatusChanged() {
//                    helper.setText(R.id.btn_download, updateAdAction(this@run))
//                    Log.d("huiger-msg", "VideoPagerAdapter -> onADStatusChanged: 广告状态变化")
//                }
//
//                override fun onADError(err: AdError) {
//                    Log.d("huiger-msg", "VideoPagerAdapter -> onADError: code=${err.errorCode}  msg=${err.errorMsg}")
//                }
//
//                override fun onADClicked() {
//                    Log.d("huiger-msg", "VideoPagerAdapter -> onADClicked: 广告被点击")
//                }
//
//                override fun onADExposed() {
//                    Log.d("huiger-msg", "VideoPagerAdapter -> onADExposed: 广告曝光")
//                }
//            })

//            val mediaView = helper.getView<MediaView>(R.id.gdt_media_view)
//            bindMediaView(mediaView, getVideoOption(), object : SimpleNativeADMediaListener() {
//                override fun onVideoStart() {
//                    helper.setGone(R.id.ad_info_container, true)
//                }
//            })

        }

    }

//    fun updateAdAction(ad: INativeProvider): String {
//        if (!ad.) {
//            return "浏览"
//        }
//        return when (ad.appStatus) {
//            0 -> "下载"
//            1 -> "启动"
//            2 -> "更新"
//            4 -> "${ad.progress}%"
//            8 -> "安装"
//            16 -> "下载失败，重新下载"
//            else -> "浏览"
//        }
//    }

//    fun getVideoOption(): VideoOption {
//        return VideoOption.Builder()
//                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.ALWAYS)
//                .setAutoPlayMuted(false)
//                .setDetailPageMuted(false)
//                .setNeedCoverImage(true)
//                .setNeedProgressBar(true)
//                .setEnableDetailPage(true) // 跳转详情
//                .setEnableUserControl(true)
//                .build()
//    }

//    /**
//     * 播放当前position下的适配
//     */
//    fun playPosition(position: Int) {
//        mData[position].run {
//            if (itemType == XJConfig.ADAPTER_ITEM_VIDEO) {
//                // 视频
//                (getViewByPosition(position, R.id.videoPlayer) as SamplePagerCoverVideo).startPlayLogic()
//            } else {
//                // 广告
//
//            }
//        }
//    }
}