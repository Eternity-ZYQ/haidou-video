package com.yimeng.haidou.mine

import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import com.yimeng.base.BaseActivity
import com.yimeng.haidou.R

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-13 18:40.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 我的商品收藏
 * </pre>
 */
class MyGoodFavoriteActivity : BaseActivity() {
    companion object {
        fun start(){
            val topActivity = ActivityUtils.getTopActivity()
            topActivity.startActivity(Intent(topActivity, MyGoodFavoriteActivity::class.java))
        }
    }

    override fun init() {
    }

    override fun loadData() {
    }

    override fun initListener() {
    }

    override fun setLayoutResId(): Int = R.layout.activity_my_good_favorite
}
