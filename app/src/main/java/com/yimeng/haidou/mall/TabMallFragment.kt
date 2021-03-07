package com.yimeng.haidou.mall

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import com.yimeng.base.BaseFragment
import com.yimeng.config.Constants
import com.yimeng.config.ConstantsUrl
import com.yimeng.entity.HomeDataBean
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.ActivityUtils
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.yimeng.haidou.R
import com.yimeng.haidou.mine.ScanForResultActivity
import com.yimeng.haidou.shop.SearchActivity
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.SharedPreferencesUtils
import com.huige.library.utils.ToastUtils
import com.yanzhenjie.permission.Action
import kotlinx.android.synthetic.main.fragment_tab_mall.*
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
 *  Time   : 2020-12-25 22:17.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 商城
 * </pre>
 */
class TabMallFragment : BaseFragment() {

    /**
     * 扫码
     */
    private val FLAG_SCAN_QRCODE = 0x111

    private val mOkHttpCommon by lazy { OkHttpCommon() }

    private val tabList = mutableListOf<HomeDataBean>()
    private val linkedHashMap = linkedMapOf<Int, MallFragment>()

    override fun setLayoutResId(): Int = R.layout.fragment_tab_mall

    override fun init() {

        viewPager.adapter = object : FragmentPagerAdapter(fragmentManager) {
            override fun getItem(position: Int): Fragment {
                var fragment = linkedHashMap[position]

                if (fragment == null) {
                    fragment = MallFragment.getInstance()
                    linkedHashMap[position] = fragment
                }
                return fragment
            }

            override fun getCount(): Int = tabList.size

            override fun getPageTitle(position: Int): CharSequence = tabList[position].name
        }

        tabLayout.navigator = CommonNavigator(activity).apply {
            adapter = object : CommonNavigatorAdapter(){
                override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                    return SimplePagerTitleView(context).apply {
                        text = tabList[index].name
                        normalColor = Color.WHITE
                        selectedColor = ContextCompat.getColor(activity!!, R.color.theme_color)

                        setOnClickListener { viewPager.currentItem = index }
                    }
                }

                override fun getCount(): Int = tabList.size

                override fun getIndicator(context: Context?): IPagerIndicator {
                    return LinePagerIndicator(context).apply {
                        mode = LinePagerIndicator.MODE_WRAP_CONTENT
                        setColors(ContextCompat.getColor(activity!!, R.color.theme_color))
                    }
                }

            }
            isAdjustMode = true
        }
        ViewPagerHelper.bind(tabLayout, viewPager)



    }

    override fun loadData() {
        val params = CommonUtils.createParams()
        params["menuType"] = "hotSales"
        mOkHttpCommon.postLoadData(activity, ConstantsUrl.APP_HOST_URL, params, object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                smartRefresh.finishRefresh()
                ToastUtils.showToast(R.string.text_network_connected_error)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject) {
                smartRefresh.finishRefresh()
                if (jsonObject["status"].asInt != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取失败!"))
                    return
                }
                val jsonStr = jsonObject["data"].asJsonArray.toString()
                SharedPreferencesUtils.put(Constants.HOME_DATA_CACHE, jsonStr)
                setData(jsonStr)
            }
        })
    }

    override fun initListener() {
        layout_search.setOnClickListener { ActivityUtils.getInstance().jumpActivity(SearchActivity::class.java) }

        // 扫码
        iv_scan.setOnClickListener {
            CommonUtils.getPermission(activity, Action { startActivityForResult(Intent(activity, ScanForResultActivity::class.java), FLAG_SCAN_QRCODE) }, Manifest.permission.CAMERA)
        }
        smartRefresh.setOnRefreshListener { loadData() }
        viewPager.addOnPageChangeListener(object :ViewPager.SimpleOnPageChangeListener(){
            override fun onPageSelected(position: Int) {
                linkedHashMap[position]?.refreshData(tabList[position])
            }
        })
    }

    private fun setData(jsonStr: String) {
        if (jsonStr.isBlank()) return
        if(tabList.isNotEmpty()) tabList.clear()
        val list = GsonUtils.getGson().fromJson<List<HomeDataBean>>(jsonStr, object : TypeToken<List<HomeDataBean?>?>() {}.type)
        list.forEach {
            if (it.introduction == "bottom") {
                tabList.add(it)
            }
        }

        val position = viewPager.currentItem
        viewPager.adapter?.notifyDataSetChanged()
        tabLayout.navigator.notifyDataSetChanged()
        linkedHashMap[position]?.refreshData(tabList[position])
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FLAG_SCAN_QRCODE && resultCode == Activity.RESULT_OK && data != null) {
            val result = data.getStringExtra("result")
            if (result.contains("http")) {
                val paramsMap = CommonUtils.urlToMap(result)
                if (paramsMap.containsKey("shopNo")) {
                    ActivityUtils.getInstance().jumpShopDetailActivity(paramsMap["shopNo"], false)
                } else {
                    ActivityUtils.getInstance().jumpInternetExplorer(result)
                }
            } else {
                ToastUtils.showToast("扫描到:\t\t$result")
            }
        }
    }

}