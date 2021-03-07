package com.yimeng.adapter

import android.widget.ImageView
import com.blankj.utilcode.util.TimeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.entity.VideoEvalBean
import com.yimeng.utils.displayUrl
import com.yimeng.haidou.R

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-12 15:16.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 视频评论
 * </pre>
 */
class VideoEvalAdapter(list: MutableList<VideoEvalBean>) : BaseQuickAdapter<VideoEvalBean,
        BaseViewHolder>(R.layout.adapter_eval_item, list) {
    override fun convert(helper: BaseViewHolder, item: VideoEvalBean?) {
        item?.run {

            helper.getView<ImageView>(R.id.civ_user_head).displayUrl(headPath)

            helper.setText(R.id.tv_user_name, nickname)
                    .setText(R.id.tv_eval, content)
            try {
                helper.setText(R.id.tv_time, TimeUtils.millis2String(createTime))
            }catch (e:Exception){
                helper.setText(R.id.tv_time, "")
            }
        }
    }
}