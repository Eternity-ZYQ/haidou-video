package com.yimeng.utils

import android.util.Log
import com.yimeng.haidou.BuildConfig

/**
 * <pre>
 * Author : huiGer
 * Time   : 2019/8/9 10:48 AM.
 * Email  : zhihuiemail@163.com
 * Desc   :
</pre> *
 */
class LogSimple{
    private var TAG = "LOG-MSG"

    companion object{
        fun instance():LogSimple = LogSimple()
    }

    fun d(tag:String = TAG, msg:String){
        if(BuildConfig.DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun e(tag:String = TAG, msg:String){
        if(BuildConfig.DEBUG) {
            Log.e(tag, msg)
        }
    }

    fun w(tag:String = TAG, msg:String){
        if(BuildConfig.DEBUG) {
            Log.w(tag, msg)
        }
    }



}
