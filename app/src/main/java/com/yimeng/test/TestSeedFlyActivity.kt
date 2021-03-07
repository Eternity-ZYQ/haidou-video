package com.yimeng.test

import com.yimeng.base.BaseActivity
import com.yimeng.haidou.R
import com.yimeng.entity.SeedBean
import kotlinx.android.synthetic.main.activity_test_seed_fly.*

class TestSeedFlyActivity : BaseActivity() {

    override fun setLayoutResId(): Int =R.layout.activity_test_seed_fly
    override fun init() {

        seedRainLayout.setSeedBeans(mutableListOf(SeedBean(), SeedBean(), SeedBean(), SeedBean(),SeedBean(), SeedBean(), SeedBean(), SeedBean(), SeedBean()))

    }

    override fun initListener() {
    }

    override fun loadData() {
    }



    override fun onStop() {
        super.onStop()
        seedRainLayout?.stopAnim()
    }
}
