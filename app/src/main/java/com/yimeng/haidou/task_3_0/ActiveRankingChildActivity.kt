package com.yimeng.haidou.task_3_0

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.view.View
import com.yimeng.base.BaseActivity
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.entity.SectionTaskBean
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.yimeng.widget.MyToolBar
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_active_ranking_child.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/10 11:21 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 子排行榜
 * </pre>
 */
class ActiveRankingChildActivity : BaseActivity() {

    // 专区编号
    private var mBlockNo = ""
    private val mOkHttpCommon = OkHttpCommon()

    override fun setLayoutResId(): Int = R.layout.activity_active_ranking_child

    override fun init() {

        intent.extras?.let {
            mBlockNo = it.getString("blockNo")
            toolBar.title = it.getString("blockName") + "排行榜"
        }

    }

    override fun initListener() {
        toolBar.setOnToolBarClickListener(MyToolBar.OnToolBarClick())


    }

    override fun loadData() {

        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_TEAM_BLOCK_TASK, CommonUtils.createParams().apply {
            put("blockNo", mBlockNo)
            put("start", "0")
            put("limit", "100")
        }, object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                jsonObject?.let {
                    if (it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", getString(R.string.net_error)))
                        return
                    }
                    var jsonArray = it.get("data").asJsonObject.get("rows").asJsonArray
                    var list = GsonUtils.getGson().fromJson<List<SectionTaskBean>>(jsonArray.toString(), object : TypeToken<List<SectionTaskBean>>() {}.type)

                    var tankingList: MutableList<SectionTaskBean> = mutableListOf()
                    list.forEachIndexed { _, sectionTaskBean ->
                        if (sectionTaskBean.merchantTaskCode != "rec") {
                            tankingList.add(sectionTaskBean)
                        }
                    }
                    initPage(tankingList)
                }
            }
        })

    }

    private fun initPage(tankingList: MutableList<SectionTaskBean>) {

        var titles = mutableListOf<String>()
        var merchantTaskNos = mutableListOf<String>()
        tankingList.forEach {
            if(it.memberTaskNo.isNotEmpty()) {
                titles.add(it.merchantTaskName)
                merchantTaskNos.add(it.merchantTaskNo)
            }
        }

        if(merchantTaskNos.isEmpty()) {
            // 没有合适的排行榜
            tv_empty.visibility = View.VISIBLE
            return
        }

        var pageAdapter = createPageAdapter(titles, merchantTaskNos)
        viewPager.adapter = pageAdapter

        tabLayout.navigator = CommonNavigator(this).apply {
            adapter = object : CommonNavigatorAdapter(){
                override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                    return SimplePagerTitleView(context).apply {
                        text = titles[index]
                        normalColor = ContextCompat.getColor(this@ActiveRankingChildActivity, R.color.c_333333)
                        selectedColor = ContextCompat.getColor(this@ActiveRankingChildActivity, R.color.theme_color)


                        setOnClickListener { viewPager.currentItem = index }
                    }
                }

                override fun getCount(): Int = titles.size

                override fun getIndicator(context: Context?): IPagerIndicator {
                    return LinePagerIndicator(context).apply {
                        mode = LinePagerIndicator.MODE_WRAP_CONTENT
                        setColors(ContextCompat.getColor(this@ActiveRankingChildActivity, R.color.theme_color))
                    }
                }
            }

        }
        ViewPagerHelper.bind(tabLayout, viewPager)
    }

    private fun createPageAdapter(titles: MutableList<String>, merchantTaskNos: MutableList<String>): FragmentPagerAdapter {
        return object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return ActiveRankingChildDetailFragment.getInstance(merchantTaskNos[position])
            }

            override fun getCount(): Int = titles.size

            override fun getPageTitle(position: Int): CharSequence? = titles[position]
        }
    }


}
