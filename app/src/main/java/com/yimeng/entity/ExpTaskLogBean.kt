package com.yimeng.entity

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/5/25 5:25 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 体验任务
 * </pre>
 */
data class ExpTaskLogBean(var taskExpNo: String,//任务项编号
                          var taskType: String,//任务类型
                          var memberNo: String,//会员编号
                          var merchantNo: String,//商家会员编号
                          var orderNo: String,//订单编号
                          var status: String,//状态 common 未结算 complete 已结算
                          var remark: String,//商家订单编号
                          var createTime: Long,//创建时间
                          var updateTime: Long, //更新时间
                          var settlementAmt: String // 结算金额

)
