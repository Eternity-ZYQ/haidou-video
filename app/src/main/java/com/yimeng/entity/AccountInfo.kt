package com.yimeng.entity

import android.text.TextUtils


// TODO 礼品数额

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/5/18 11:47 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 个人中心  任务数/代金券/余额/店铺余额
 * </pre>
 */
class AccountInfo {
    var newTaskCount: Long = 0//新任务
    var score: Long = 0//积分
    var couponNum: Long = 0//代金券数量
    var couponAmt: Long = 0//代金券总金额

    //    var baodanbi: String = ""//商家账户
    var balance: Long = 0//用户余额
    var giftNum: Long = 0 // 礼品券数量
    var giftVolume: Long = 0 // 礼品券总金额


    var extra: Long = 0 // 彩蛋
    var baodanbi: Long = 0 // 活跃值
    var yongjin: Long = 0 // 金蛋值

    private var cycleLoginTimes: String = ""//分润账户

    fun getCycleLoginTimes(): String {
        return if (TextUtils.isEmpty(cycleLoginTimes)) "" else cycleLoginTimes
    }
}