package com.yimeng.haidou.video

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.yimeng.adapter.VideoAdapter
import com.yimeng.base.BaseFragment
import com.yimeng.entity.VideoBean
import com.yimeng.retorfit.ResponseListData
import com.yimeng.retorfit.RetrofitHelper
import com.yimeng.utils.ScrollCalculatorHelper
import com.yimeng.haidou.R
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.utils.CommonUtil
import kotlinx.android.synthetic.main.fragment_tab_video.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-05 00:12.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 短视频
 * </pre>
 */
class TabVideoFragment : BaseFragment() {

    private var mFull = false

    private var mStart = 0
    private val mLimit = 50

    private var firstVisibleItem = 0
    private var lastVisibleItem = 0

    private val mList = mutableListOf<VideoBean>()
    private val mAdapter by lazy { VideoAdapter(mList) }


    /** 滑动控制器 */
    private val mScrollCalculatorHelper by lazy {
        //限定范围为屏幕一半的上下偏移180
        val playTop: Int = CommonUtil.getScreenHeight(activity) / 2 - CommonUtil.dip2px(activity, 180f)
        val playBottom: Int = CommonUtil.getScreenHeight(activity) / 2 + CommonUtil.dip2px(activity, 180f)
        ScrollCalculatorHelper(R.id.videoPlayer, playTop, playBottom)
    }

    override fun setLayoutResId(): Int = R.layout.fragment_tab_video

    override fun init() {

        recyclerView.run {
            layoutManager = LinearLayoutManager(activity!!)
            adapter = mAdapter
        }


    }

    override fun loadData() {

        getVideo()
    }

    override fun initListener() {
        smartRefresh.setOnRefreshLoadMoreListener(object :OnRefreshLoadMoreListener{
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mStart += mLimit
                getVideo()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mStart = 0
                if(mList.isNotEmpty()) mList.clear()
                getVideo()
            }
        })


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                }
                mScrollCalculatorHelper.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                //这是滑动自动播放的代码
                if (!mFull) {
                    mScrollCalculatorHelper.onScroll(recyclerView, firstVisibleItem, lastVisibleItem,
                            lastVisibleItem - firstVisibleItem)
                }
            }
        })
    }

    private fun getVideo(){
        RetrofitHelper.toDo()
                .getVideo(mStart, mLimit)
                .enqueue(object : Callback<ResponseListData<VideoBean>>{
                    override fun onFailure(call: Call<ResponseListData<VideoBean>>, t: Throwable) {
                        if(mStart == 0) smartRefresh.finishRefresh(false)
                        else smartRefresh.finishLoadMore(false)
                    }

                    override fun onResponse(call: Call<ResponseListData<VideoBean>>, response: Response<ResponseListData<VideoBean>>) {
                        if(RetrofitHelper.isSuccess(response)) {
                            if(mStart == 0) smartRefresh.finishRefresh()
                            else smartRefresh.finishLoadMore()

                            response.body()?.data?.rows?.run {
                                if(this.size < mLimit) {
                                    smartRefresh.finishLoadMoreWithNoMoreData()
                                }
                                
                                if(mStart == 0 && lastVisibleItem == 0 && this.isNotEmpty()) {
                                    lastVisibleItem = this.size
                                }

                                mList.addAll(this)
                                mAdapter.notifyDataSetChanged()
                            }

                        }else{
                            if(mStart == 0) smartRefresh.finishRefresh(false)
                            else smartRefresh.finishLoadMore(false)
                        }
                    }
                })
    }


    /*--------------------------------- 生命周期 ---------------------------------*/


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        //如果旋转了就全屏
        mFull = newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_USER
    }

    override fun onBackPressed(): Boolean {
        if (GSYVideoManager.backFromWindowFull(activity)) {
            return false
        }
        return super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        GSYVideoManager.onPause()
    }

    override fun onResume() {
        super.onResume()
        GSYVideoManager.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
    }
}