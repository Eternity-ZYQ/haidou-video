package com.yimeng.utils

import android.app.Activity
import android.graphics.Bitmap
import com.yimeng.haidou.R
import com.huige.library.utils.ToastUtils
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import com.umeng.socialize.media.UMWeb

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/28 11:34 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 友盟分享，
 *          注意：在调用分享的页面请在onActivityResult方法中添加
 *          UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
 * </pre>
 */
class UMShareUtils {

    companion object {
        fun getInstance(): UMShareUtils = UMShareUtils()
    }

    /**
     * 链接分享
     * @param url 链接
     * @param shareTitle 标题
     * @param shareDescription 描述
     * @param callback 可为空，为空时，默认消息提示
     */
    @JvmOverloads
    fun shareURL(activity: Activity, url: String, shareTitle: String = "标题", shareDescription: String = "描述", shareLogo:Int = R.mipmap.logo, callback: UMShareListener? = null) {
        var umWeb = UMWeb(url).apply {
            title = shareTitle
            description = shareDescription
            setThumb(UMImage(activity, shareLogo).apply { compressFormat = Bitmap.CompressFormat.PNG })
        }
        var shareAction = ShareAction(activity)
                .withMedia(umWeb)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
        if (callback == null) {
            shareAction.setCallback(object : UMShareListener {
                override fun onResult(p0: SHARE_MEDIA?) {
                    ToastUtils.showToast("分享成功")
                }

                override fun onCancel(p0: SHARE_MEDIA?) {
                    ToastUtils.showToast("您已取消分享")
                }

                override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
                    ToastUtils.showToast("分享失败")
                }

                override fun onStart(p0: SHARE_MEDIA?) {
                    ToastUtils.showToast("正在启动分享,请稍后!")
                }
            })
        } else {
            shareAction.setCallback(callback)
        }
        shareAction.open()
    }

}