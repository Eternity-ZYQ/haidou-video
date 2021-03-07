package com.yimeng.entity

import java.io.Serializable

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/7/10 3:47 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 收益账户数据
 * </pre>
 */
data class IncomeAccointInfo(
        var bonusPool: String,//奖金池
        var cycleLoginTimes: String,//收益金额
        var totalBonus: Int,//累计人脉奖励金
        var totalCycleLoginTimes: String,//累计分红收益
        var contactService: String,//客服电话
        var recNum: String,//推荐人数
        var recReward: String,//每单推荐奖励金
        var recSmsTemplate: String//发送短信内容
) : Serializable {

    fun getTotalBonusStr(): String {
        var div = totalBonus.div(100.0)
        return if (div > 10000) "${div / 10000.0}万"
        else String.format("%.2f", div)
    }
}