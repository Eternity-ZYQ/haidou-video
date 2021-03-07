package com.yimeng.entity

import java.io.Serializable

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/5/29 11:07 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 活跃度排行榜
 * </pre>
 */
data class ActiveRankingBean(var mobileNo: String,// 手机号
                             var memberName: String,//名字
                             var memberNo: String,//用户编号
                             var extracts: String,//活跃度
                             var rowno: String,//排行
                             var nickname: String,//店铺名
                             var taskOrderNum: String,//完成订单数
                             var headPath: String,//头像
                             var info: String,// 活跃度,昵称,头像
                             var orderMatchingNum: String,//平台已匹配单数
                             var saleTotalAmt: String,//销售额
                             var shopsProfit: String,//商家预计收益
                             var nkName: String,//昵称
                             var expireTime: String, // 商家缺单数
                             var taskCount: String//富豪缺单数
) : Serializable