package com.yimeng.entity

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-12 19:18.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 金蛋列表
 * </pre>
 */
data class ReelBean(
    val award: Long,         // 活跃值
    val createTime: Long,
    val feederName: String, // 金蛋名称
    val feederNo: String,   // 金蛋编号
    val id: Int,
    val nissan: Long,        // 每天收获
    val period: Int,        // 有效期
    val price: Long,         // 彩蛋，大于0才展示
    val remark: String,     // 金蛋内容
    val saleFee: Long,       // 金蛋值
    val status: String,
    val totalOutput: Long,   // 总产量
    val updateTime: Long,
    val upperLimit: Int     // 最多领取数量
)