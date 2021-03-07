package com.yimeng.haidou.mine

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.base.BaseActivity
import com.yimeng.config.Constants
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.entity.RedPackerBean
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.ActivityUtils
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.DateUtil
import com.yimeng.utils.GsonUtils
import com.yimeng.widget.MyToolBar
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.DeviceUtils
import com.huige.library.utils.ToastUtils
import com.huige.library.widget.LimitScrollView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_red_packer_wall.*
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/30 11:49 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 红包墙
 * </pre>
 */
class RedPackerWallActivity : BaseActivity() {

    /**
     * 0：默认
     * 1：首页
     */
    private var mFromType = 0
    private val mOkHttpCommon = OkHttpCommon()
    private var mPage = 1
    private var mList = mutableListOf<RedPackerBean>()
    private val mAdapter: BaseQuickAdapter<RedPackerBean, BaseViewHolder> by lazy {
        object : BaseQuickAdapter<RedPackerBean, BaseViewHolder>(
                R.layout.adapter_red_packer_item_layout, mList
        ) {
            override fun convert(helper: BaseViewHolder?, item: RedPackerBean?) {
                helper?.let {
                    item?.apply {
                        it.setText(R.id.tv_rp_title, projectTitle)
                                .setText(R.id.tv_rp_content, projectContent)
                                .setText(R.id.tv_rp_time, DateUtil.getAssignDate(createTime, 3))
                                .setGone(R.id.iv_share, false)

                        CommonUtils.showImage(it.getView(R.id.iv_rp), projectLogo)

                    }
                }
            }
        }
    }


    override fun setLayoutResId(): Int = R.layout.activity_red_packer_wall

    override fun init() {

        intent.extras?.let {
            mFromType = it.getInt("fromType", 0)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@RedPackerWallActivity)
            adapter = mAdapter
        }

        if(mFromType == 1) {
            toolBar.setRightContent("我要推广")
        }

    }

    override fun initListener() {
        toolBar.setOnToolBarClickListener(object :MyToolBar.OnToolBarClick(){
            override fun onRightClick() {
                if(mFromType == 1) {
                    ActivityUtils.getInstance().jumpActivity(RedPackerActivity::class.java, Bundle().apply {
                        putInt("fromType", mFromType)
                    })
                }
            }
        })

        smartRefresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                getRedPacker()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                getRedPacker()
            }
        })

        mAdapter.setOnItemClickListener { _, _, position ->
            var redPackerBean = mList[position]
            ActivityUtils.getInstance().jumpActivity(RedPackerWallDetailActivity::class.java, Bundle().apply {
                putString("projectNo", redPackerBean.modeProjectNo)
            })
        }
    }

    override fun loadData() {

    }

    override fun onResume() {
        super.onResume()
        smartRefresh?.autoRefresh()
        limitAds?.startScroll()
    }

    override fun onPause() {
        super.onPause()
        limitAds?.stopScroll()
    }

    override fun onDestroy() {
        super.onDestroy()
        limitAds?.stopScroll()
    }

    /**
     * 红包墙数据
     */
    private fun getRedPacker() {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_GET_RED_PACKER_WALL, CommonUtils.createParams().apply {
            CommonUtils.addPageParams(this, mPage)
        }, object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
                showSmartRefreshGetDataFail(smartRefresh, mPage)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                showSmartRefreshGetDataFail(smartRefresh, mPage)
                jsonObject?.let {
                    if (it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", getString(R.string.net_error)))
                        return
                    }

                    var jsonObj = it.get("data").asJsonObject
                    var jsonArray = jsonObj.get("rows").asJsonArray
                    if (jsonArray.size() != 0) {
                        var list = GsonUtils.getGson().fromJson<List<RedPackerBean>>(jsonArray.toString(), object : TypeToken<List<RedPackerBean>>() {}.type)
                        if (list.size < Constants.MAX_LIMIT) {
                            smartRefresh.finishLoadMoreWithNoMoreData()
                        }
                        if (mPage == 1 && mList.isNotEmpty()) mList.clear()
                        mPage++
                        mList.addAll(list)
                        mAdapter.notifyDataSetChanged()
                    } else {
                        smartRefresh.finishLoadMoreWithNoMoreData()
                        mList.clear()
                        mAdapter.notifyDataSetChanged()
                    }

                    tv_empty.visibility = if(mList.isEmpty()) View.VISIBLE else View.GONE

                    // 轮播公告
                    var array = jsonObj.get("otherData").asJsonArray
                    if (array.size() == 0) return
                    var list = mutableListOf<String>()
                    array.forEach { item -> list.add(item.asString) }
                    setAdsInfo(list)
                }
            }
        })

    }

    /**
     * 滚动数据
     */
    private fun setAdsInfo(strs: MutableList<String>) {
        limitAds.setAdapter(object : LimitScrollView.LimitScrollViewAdapter {
            override fun getView(position: Int): View = TextView(this@RedPackerWallActivity).apply {
                text = strs[position]
                setPadding(0, DeviceUtils.dp2px(this@RedPackerWallActivity, 8f), 0, DeviceUtils.dp2px(this@RedPackerWallActivity, 8f))
                setTextColor(ContextCompat.getColor(this@RedPackerWallActivity, R.color.c_333333))
                textSize = 14f
                gravity = Gravity.CENTER
            }

            override fun getCount(): Int = strs.size
        })
        limitAds.startScroll()
    }

}