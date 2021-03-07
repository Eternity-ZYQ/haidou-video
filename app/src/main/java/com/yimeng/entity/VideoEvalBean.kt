package com.yimeng.entity

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-12 15:15.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 视频评论
 * </pre>
 */
data class VideoEvalBean(
    val businessNo: String,
    val businessType: String,
    val commentNo: String,
    val content: String,
    val createTime: Long,
    val discriptScore: Any,
    val headPath: String,
    val id: Int,
    val imgPath: Any,
    val logisticsScore: Any,
    val memberNo: String,
    val nickname: String,
    val remark: Any,
    val repeatMemberNo: Any,
    val repeatNickname: Any,
    val repeatNo: Any,
    val serviceScore: Any,
    val shopNo: Any,
    val status: String,
    val updateTime: Long
)