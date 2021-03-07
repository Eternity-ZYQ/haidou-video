package com.yimeng.interfaces

import android.util.Log
import com.qq.e.ads.nativ.NativeADMediaListener
import com.qq.e.comm.util.AdError

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-06 21:34.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
open class SimpleNativeADMediaListener : NativeADMediaListener {
    override fun onVideoInit() {
        Log.d("huiger-msg", "SimpleNativeADMediaListener -> onVideoInit: ")
    }

    override fun onVideoStop() {
        Log.d("huiger-msg", "SimpleNativeADMediaListener -> onVideoStop:  ")
    }

    override fun onVideoPause() {
        Log.d("huiger-msg", "SimpleNativeADMediaListener -> onVideoPause: ")
    }

    override fun onVideoStart() {
        Log.d("huiger-msg", "SimpleNativeADMediaListener -> onVideoStart: ")
    }

    override fun onVideoError(err: AdError) {
        Log.d("huiger-msg", "SimpleNativeADMediaListener -> onVideoError: code=${err.errorCode} msg=${err.errorMsg}")
    }

    override fun onVideoCompleted() {
        Log.d("huiger-msg", "SimpleNativeADMediaListener -> onVideoCompleted: ")
    }

    override fun onVideoLoading() {
        Log.d("huiger-msg", "SimpleNativeADMediaListener -> onVideoLoading: ")
    }

    override fun onVideoReady() {
        Log.d("huiger-msg", "SimpleNativeADMediaListener -> onVideoReady: ")
    }

    override fun onVideoLoaded(p0: Int) {
        Log.d("huiger-msg", "SimpleNativeADMediaListener -> onVideoLoaded: ")
    }

    override fun onVideoClicked() {
        Log.d("huiger-msg", "SimpleNativeADMediaListener -> onVideoClicked: ")
    }

    override fun onVideoResume() {
        Log.d("huiger-msg", "SimpleNativeADMediaListener -> onVideoResume: ")
    }
}