package com.yimeng.entity

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-12 17:59.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 个人任务
 * </pre>
 */
data class MineTaskBean(
    val commonTaskNo: String,
    val createTime: Long,
    val endTime: Long,
    val id: Int,
    val randomDown: Int,
    val randomUp: Int,
    val remark: String,
    val scoreRandom: Int,
    val startTime: Long,
    val status: String,
    val taskDetail: Any,
    val taskImage: Any,
    val taskName: String,
    val taskScore: Int,
    val taskScoreType: String,
    val taskSubName: Any,
    val taskType: String,
    val typeCategory: String,
    val updateTime: Long
){
    // 视频任务
    var videoTaskComplete = false
    // 广告任务
    var adTaskComplete = false
}