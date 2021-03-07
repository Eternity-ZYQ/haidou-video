package com.yimeng.entity

import com.lrad.adSource.INativeProvider
import com.yimeng.config.ConstantsUrl
import com.yimeng.config.XJConfig
import com.qq.e.ads.nativ.NativeUnifiedADData

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-05 09:41.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
class VideoBean {
    constructor() {
        itemType = XJConfig.ADAPTER_ITEM_VIDEO
    }

    constructor(ad: INativeProvider) {
        itemType = XJConfig.ADAPTER_ITEM_ADS
        this.ad = ad
    }

    var itemType: Int = 0
    var ad: INativeProvider? = null

    var collectNum: Int = 0
    var collected: Int = 0  // 1 已点赞  0 未点赞
    var commentNum: Int = 0
    var createTime: Long = 0
    var id: Int = 0
    var likeNum: Int = 0
    var merchantLogo: String = ""
    var merchantName: String = ""
    var merchantNo: String = ""
    var productImage: String = ""   // 用户头像
        get() = ConstantsUrl.UPLOAD_HEAD_URL + field
    var productName: String = ""
    var productNo: String = ""
    var productPrice: String = ""
    var remark: String = ""
    var shareNum: Int = 0
    var sort: Int = 0
    var spareUrl: String = ""   //
    var status: String = ""
    var updateTime: Long = 0
    var videoDesc: String = ""  // 详细描述
    var videoName: String = ""  // 标题
    var videoNo: String = ""
    var videoPoster: String = "" // 封面
    var videoType: String = ""
    var videoUrl: String = ""   // 视频地址
    var seekPosition = 0

}