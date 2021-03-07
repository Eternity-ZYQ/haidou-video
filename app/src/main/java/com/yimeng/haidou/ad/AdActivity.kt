package com.yimeng.haidou.ad

import android.content.Intent
import android.os.Handler
import android.util.Log
import com.blankj.utilcode.util.ActivityUtils
import com.yimeng.base.BaseActivity
import com.yimeng.config.XJConfig
import com.yimeng.utils.showToast
import com.yimeng.haidou.R
import com.qq.e.ads.rewardvideo.RewardVideoAD
import com.qq.e.ads.rewardvideo.RewardVideoADListener
import com.qq.e.comm.util.AdError

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-12 18:49.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 视频广告
 * </pre>
 */
class AdActivity : BaseActivity() {

    companion object {
        fun startForResult(requstCode: Int) {
            val topActivity = ActivityUtils.getTopActivity()
            topActivity.startActivityForResult(Intent(topActivity, AdActivity::class.java), requstCode)
        }
    }

    private val mHandler  = Handler()

    override fun setLayoutResId(): Int = R.layout.activity_ad

    override fun init() {

        rewardVideoAD.loadAD()
        //广告展示检查1：广告成功加载，此处也可以使用videoCached来实现视频预加载完成后再展示激励视频广告的逻辑
        // 广告展示检查2：是否过期、是否展示过、是否缓存

    }

    override fun loadData() {
    }

    override fun initListener() {

    }

    private val rewardVideoAD: RewardVideoAD by lazy {
        RewardVideoAD(this, XJConfig.GDTAD_VIDEO_AD_ID, object : RewardVideoADListener {
            override fun onADExpose() {
                Log.d("huiger", "onADExpose: ")
            }

            override fun onADClick() {
                Log.d("huiger", "onADClick: ")
            }

            override fun onReward() {
                Log.d("huiger", "onReward: ")
            }

            override fun onVideoCached() {
                Log.d("huiger", "onVideoCached: ")
            }

            override fun onADClose() {
                Log.d("huiger", "onADClose: ")
                finish()
            }

            override fun onADLoad() {
                Log.d("huiger", "onADLoad: ")
                mHandler.postDelayed({
                    rewardVideoAD.showAD()
                }, 500)
            }

            override fun onVideoComplete() {
                Log.d("huiger", "onVideoComplete: ")
            }

            override fun onError(p0: AdError) {
                Log.d("huiger", "onADExpose: ${p0.errorMsg}")
                if(p0.errorCode == 102006) {
                    showToast("没有匹配到相应广告")
                    finish()
                }
            }

            override fun onADShow() {
                Log.d("huiger", "onADShow: ")
            }

        }, true)
    }

}
