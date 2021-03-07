package com.yimeng.entity

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-13 00:01.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 日志
 * </pre>
 */
data class LogBean(
    val accountName: String,
    val accountNo: String,
    val accountSource: String,
    val accountSourceCn: String,
    val accountType: String,        // shouru +  zhichu -
    val accountWaterNo: String,
    val adminName: Any,
    val adminNo: String,
    val amt: Long,
    val amtType: String,
    val asc: String,
    val businessType: String,
    val createTime: Long,
    val currencyType: String,
    val desc: String,
    val id: Int,
    val limit: Int,
    val orderName: Any,
    val orderNo: String,
    val payNo: String,
    val platBalance: Long,
    val prePlatBalance: Long,
    val preRechargeBalance: Any,
    val rechargeBalance: Any,
    val remark: Any,
    val start: Int,
    val state: String,
    val updateTime: Long
)