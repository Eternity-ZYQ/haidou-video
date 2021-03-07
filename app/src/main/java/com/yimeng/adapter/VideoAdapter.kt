package com.yimeng.adapter

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.entity.VideoBean
import com.yimeng.utils.displayUrl
import com.yimeng.widget.SampleCoverVideo
import com.yimeng.haidou.R
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-05 09:30.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 视频播放器
 * </pre>
 */
class VideoAdapter(list: MutableList<VideoBean>) :BaseQuickAdapter<VideoBean,
        BaseViewHolder>(R.layout.adapter_video_item, list){

    private val TAG = "video_list"

    override fun convert(helper: BaseViewHolder, item: VideoBean?) {
        item?.let {
            val videoPlayer = helper.getView<SampleCoverVideo>(R.id.videoPlayer)
            videoPlayer.run {
                // 隐藏返回键
                backButton.visibility = View.GONE
                // 设置全屏按钮
                fullscreenButton.setOnClickListener {
                    startWindowFullscreen(mContext, true, true)
                }
                loadCoverImage(it.videoPoster, Color.parseColor("#cccccc"))
            }

            GSYVideoOptionBuilder()
                    .setIsTouchWiget(false)
                    .setThumbImageView(ImageView(mContext))
                    .setUrl(it.videoUrl)
                    .setVideoTitle(it.videoDesc)
                    .setCacheWithPlay(false)
                    .setRotateViewAuto(true)
                    .setLockLand(true)
                    .setPlayTag(TAG)
//                    .setMapHeadData(header)
                    .setShowFullAnimation(true)
                    .setNeedLockFull(true)
                    .setPlayPosition(helper.adapterPosition)
                    .setVideoAllCallBack(object : GSYSampleCallBack(){
                        override fun onEnterFullscreen(url: String?, vararg objects: Any?) {
                            super.onEnterFullscreen(url, *objects)
                            GSYVideoManager.instance().isNeedMute = false
                            videoPlayer.currentPlayer.titleTextView.text = objects[0] as String
                        }
                    })
                    .build(videoPlayer)

            helper.getView<ImageView>(R.id.civ_user_head).displayUrl(it.productImage)
            helper.setText(R.id.tv_user_name, it.videoName)
                    .setText(R.id.tv_like_num, "${it.likeNum}")
        }
    }
}