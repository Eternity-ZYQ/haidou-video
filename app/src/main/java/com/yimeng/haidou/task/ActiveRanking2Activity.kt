package com.yimeng.haidou.task

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import com.yimeng.base.BaseActivity
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.entity.SendOrderBean
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.ActivityUtils
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.yimeng.widget.MyToolBar
import com.yimeng.widget.NavCommonView
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_active_ranking2.*
import kotlinx.android.synthetic.main.activity_active_ranking2.toolBar
import kotlinx.android.synthetic.main.activity_task_send_order.*
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/5/29 10:57 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 活跃度排行榜
 * </pre>
 */
class ActiveRanking2Activity : BaseActivity() {

    private var title = arrayOf("商家排行榜", "富豪排行榜")

    // 商家排行榜
    private val shopTopFragment: ActiveRankingTopFragment by lazy { ActiveRankingTopFragment.getInstance("") }
    // 富豪排行榜
    private val allMoneyTopFragment: ActiveRankingTopFragment by lazy { ActiveRankingTopFragment.getInstance("fs") }

    private val fragmentPagerAdapter: FragmentPagerAdapter by lazy {
        object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return if (position == 0) shopTopFragment
                else allMoneyTopFragment
            }

            override fun getCount(): Int = title.size

            override fun getPageTitle(position: Int): CharSequence? {
                return title[position]
            }
        }
    }

    override fun setLayoutResId(): Int = R.layout.activity_active_ranking2

    override fun init() {

        viewPager.adapter = fragmentPagerAdapter
        navCommonView.setNavType("商家排行榜", "富豪排行榜", "", "", "")
        navCommonView.setCurrentNavCount(2)

    }

    override fun initListener() {
        toolBar.setOnToolBarClickListener(object :MyToolBar.OnToolBarClick(){
            override fun onRightClick() {
                var bundle = Bundle()
                bundle.putBoolean("isSel", false)
                ActivityUtils.getInstance().jumpActivity(TaskSendOrderActivity::class.java, bundle)
            }
        })

        viewPager.addOnPageChangeListener(
                object : ViewPager.OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) {}
                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                    override fun onPageSelected(position: Int) {
                        navCommonView.setCurrentPosition(position)
                    }
                })

        navCommonView.setOnClickListener(NavCommonView.OnClickListener { position ->
            viewPager.currentItem = position
        })
    }

    override fun loadData() {

        if(CommonUtils.getMember().isAgent) {
            toolBar.setRightContent("可派单数：0")
            agentQueryOrderCount()
        }
    }

    private fun agentQueryOrderCount() {
        OkHttpCommon().postLoadData(this, ConstantsUrl.URL_AGENT_QUERY_SEND_ORDER, CommonUtils.createParams(), object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                smartRefresh.finishRefresh()
                jsonObject?.apply {
                    if (jsonObject.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", getString(R.string.net_error)))
                        return
                    }

                    var dataArr = jsonObject.get("data").asJsonArray
                    if (!dataArr.isJsonNull) {
                        var list = GsonUtils.getGson().fromJson<List<SendOrderBean>>(dataArr.toString(), object : TypeToken<List<SendOrderBean>>() {}.type)
                        toolBar.setRightContent("可派单数：${list.size}")
                    }
                }
            }
        })
    }

}
