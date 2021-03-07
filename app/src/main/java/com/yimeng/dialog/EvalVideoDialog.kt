package com.yimeng.dialog

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.inputmethod.EditorInfo
import com.yimeng.adapter.VideoEvalAdapter
import com.yimeng.config.XJConfig
import com.yimeng.entity.VideoEvalBean
import com.yimeng.retorfit.ResponseListData
import com.yimeng.retorfit.RetrofitHelper
import com.yimeng.utils.RecyclerViewDivider
import com.yimeng.utils.finish
import com.yimeng.haidou.R
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.util.XPopupUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.dialog_eval_video.view.*
import kotlinx.android.synthetic.main.dialog_video_eval_input.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-12 12:03.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 评论弹窗
 * </pre>
 */
class EvalVideoDialog(context: Context, videoNo: String, evalCallback: (String) -> Unit) : BottomPopupView(context) {

    private val mVideoNo = videoNo
    private val mEvalCallback = evalCallback

    private var mStart = 0
    private var mPage = 1

    private val mList = mutableListOf<VideoEvalBean>()
    private val mAdapter = VideoEvalAdapter(mList)

    override fun getImplLayoutId(): Int = R.layout.dialog_eval_video

    override fun onCreate() {

        rv_eval.run {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            addItemDecoration(RecyclerViewDivider(context, LinearLayoutManager.VERTICAL))
        }

        val lp = layout_content.layoutParams
        lp.height = (XPopupUtils.getWindowHeight(context) * 0.6).toInt()
        layout_content.layoutParams = lp

    }

    override fun onShow() {
        getComments()




        tv_input.setOnClickListener { showInputDialog() }

        smartRefresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPage++
                getComments()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                refreshComments()
            }

        })

    }

    /**
     * 0  10  20  30
     * 1  2   3   4
     */
    private fun getComments() { //
        mStart = (mPage - 1) * XJConfig.LIMIT
        RetrofitHelper.toDo().getVideoComments(mVideoNo, mStart, XJConfig.LIMIT)
                .enqueue(object : Callback<ResponseListData<VideoEvalBean>> {
                    override fun onFailure(call: Call<ResponseListData<VideoEvalBean>>, t: Throwable) {
                        smartRefresh.finish(false, mPage, -1)
                    }

                    override fun onResponse(call: Call<ResponseListData<VideoEvalBean>>, response: Response<ResponseListData<VideoEvalBean>>) {
                        response.body()?.data?.rows?.run {
                            smartRefresh.finish(true, mPage, size)
                            mList.addAll(this)
                            mAdapter.notifyDataSetChanged()
                        } ?: smartRefresh.finish(false, mPage, -1)
                    }
                })
    }

    /**
     * 刷新数据
     */
    fun refreshComments() {
        if (mList.isNotEmpty()) mList.clear()
        mPage = 1
        getComments()

    }

    override fun onDismiss() {
        if (mPage == 1) smartRefresh?.finishRefresh()
        else smartRefresh?.finishLoadMore()
    }

    private fun showInputDialog() {
        XPopup.Builder(context)
                .autoOpenSoftInput(true)
                .asCustom(EvalInputDialog(context) {
                    mEvalCallback(it)
                })
                .show()
    }

//    override fun getMaxHeight(): Int {
//        return (XPopupUtils.getWindowHeight(context) * 0.7).toInt()
//    }

}

/**
 * 底部输入弹窗
 */
class EvalInputDialog(context: Context, evalCallback: (String) -> Unit) : BottomPopupView(context) {

    private val mEvalCallback = evalCallback

    override fun getImplLayoutId(): Int = R.layout.dialog_video_eval_input

    override fun onShow() {
        et_input.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEND) {
                dismissWith { mEvalCallback(getComment()) }
                false
            }
            true
        }
    }

    private fun getComment(): String = et_input?.text?.trim().toString()

}