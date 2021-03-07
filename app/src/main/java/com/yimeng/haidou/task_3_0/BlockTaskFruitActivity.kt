package com.yimeng.haidou.task_3_0

import android.annotation.SuppressLint
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
import com.yimeng.utils.UnitUtil
import com.yimeng.widget.MyToolBar
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_block_task_fruit.*
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
 *  Time   : 2019/8/23 2:47 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 区块下的任务列表
 * </pre>
 */
class BlockTaskFruitActivity : BaseActivity() {

    /**
     * 1. 我的活跃果
     * 2. 抢购活跃果
     */
    private var mType = 1
    private var mBlockNo: String = ""
    private var mBlockName: String = ""
    // 活跃果市场价
    private var mFruitPrice = 0.0

    private val mOkHttpCommon = OkHttpCommon()


    override fun setLayoutResId(): Int = R.layout.activity_block_task_fruit

    override fun init() {
        intent.extras?.let {
            mType = it.getInt("type", 1)
            mBlockNo = it.getString("blockNo", "")
            mBlockName = it.getString("blockName", "")
        }

        toolBar.title = "${mBlockName}活跃果"



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

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                jsonObject?.let {
                    if (it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", getString(R.string.net_error)))
                        return
                    }
                    var dataObj = it.get("data").asJsonObject
                    var jsonArray = dataObj.get("rows").asJsonArray
                    var list = GsonUtils.getGson().fromJson<MutableList<SectionTaskBean>>(jsonArray.toString(), object : TypeToken<List<SectionTaskBean>>() {}.type)
                    initPage(list)
                }
            }
        })

        getFruitPrice()

    }

    /**
     * 获取活跃果市场价
     */
    private fun getFruitPrice() {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_GET_FRUIT_PRICE, CommonUtils.createParams().apply {
            put("blockNo", mBlockNo)
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

                    // 分
                    var price = UnitUtil.getInt(it.get("data").asString)
                    mFruitPrice = price / 100.0
                    tv_seed_price.text = "当前市价:￥$mFruitPrice/颗"
                }
            }
        })
    }

    private fun initPage(tankingList: MutableList<SectionTaskBean>) {

        var titles = mutableListOf<String>()
        var merchantTaskNos = mutableListOf<String>()
        tankingList.forEach {
            if (it.memberTaskNo.isNotEmpty() && it.merchantTaskCode == "rec") {
                titles.add(it.merchantTaskName)
                merchantTaskNos.add(it.merchantTaskNo)
            }
        }

        if (merchantTaskNos.isEmpty()) {
            // 没有合适的排行榜
            tv_empty.visibility = View.VISIBLE
            return
        }

        var pageAdapter = createPageAdapter(titles, merchantTaskNos)
        viewPager.adapter = pageAdapter

        tabLayout.navigator = CommonNavigator(this).apply {
            adapter = object : CommonNavigatorAdapter() {
                override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                    return SimplePagerTitleView(context).apply {
                        text = titles[index]
                        normalColor = ContextCompat.getColor(this@BlockTaskFruitActivity, R.color.c_333333)
                        selectedColor = ContextCompat.getColor(this@BlockTaskFruitActivity, R.color.theme_color)
                        setOnClickListener { viewPager.currentItem = index }
                    }
                }

                override fun getCount(): Int = titles.size

                override fun getIndicator(context: Context?): IPagerIndicator {
                    return LinePagerIndicator(context).apply {
                        mode = LinePagerIndicator.MODE_WRAP_CONTENT
                        setColors(ContextCompat.getColor(this@BlockTaskFruitActivity, R.color.theme_color))
                    }
                }
            }
        }
        ViewPagerHelper.bind(tabLayout, viewPager)
    }

    private fun createPageAdapter(titles: MutableList<String>, merchantTaskNos: MutableList<String>): FragmentPagerAdapter {
        return object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return SaleFruitFragment.getInstance(mBlockNo, merchantTaskNos[position], mType)
            }

            override fun getCount(): Int = titles.size

            override fun getPageTitle(position: Int): CharSequence? = titles[position]
        }
    }
}
