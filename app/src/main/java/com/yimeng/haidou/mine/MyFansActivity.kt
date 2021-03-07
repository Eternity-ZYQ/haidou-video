package com.yimeng.haidou.mine

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.widget.TextView
import com.blankj.utilcode.util.ActivityUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.base.BaseActivity
import com.yimeng.entity.MemberFansBean
import com.yimeng.retorfit.RetrofitHelper
import com.yimeng.utils.*
import com.yimeng.widget.MyToolBar
import com.yimeng.haidou.R
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_my_fans.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-12 20:51.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 我的粉丝
 * </pre>
 */
class MyFansActivity : BaseActivity() {

    companion object {
        fun start() {
            val topActivity = ActivityUtils.getTopActivity()
            topActivity.startActivity(Intent(topActivity, MyFansActivity::class.java))
        }
    }

    private val mList = mutableListOf<MemberFansBean>()
    private val mAdapter = object : BaseQuickAdapter<MemberFansBean, BaseViewHolder>(
            R.layout.adapter_fans_item, mList
    ) {
        override fun convert(helper: BaseViewHolder, item: MemberFansBean?) {

            item?.run {
                helper.setText(R.id.tv_user_name, mobileNo)
                        .setText(R.id.tv_user_mobile, memberGrade)
            }

        }
    }

    private var mPage = 1
    private val mLimit = 20

    override fun setLayoutResId(): Int = R.layout.activity_my_fans


    override fun init() {


        recyclerView.run {
            layoutManager = LinearLayoutManager(this@MyFansActivity)
            adapter = mAdapter
        }

        val headView = LayoutInflater.from(this).inflate(R.layout.layout_fans_head, null)

        mAdapter.addHeaderView(headView, 0)


    }

    override fun loadData() {

        getFans()

    }


    override fun initListener() {
        toolBar.setOnToolBarClickListener(object :MyToolBar.OnToolBarClick(){})

        smartRefresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPage++
                getFans()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                if (mList.isNotEmpty()) mList.clear()
                getFans()
            }

        })
    }

    private fun getFans() {
        val start = (mPage - 1) * mLimit
        RetrofitHelper.toDo().getMyFans(CommonUtils.getMember().memberNo,start, mLimit)
                .enqueue(object : Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        smartRefresh.finish(false, mPage, -1)
                    }

                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        response.body()?.run {
                            if(get("status").asInt == 1) {
                                smartRefresh.finish(true, mPage, -1)
                                val jsonArray = get("data").asJsonArray
                                val headObject = jsonArray[0].asJsonObject
                                setHeadInfo(headObject)
                                setUsers(jsonArray[1].asJsonObject.get("rows").asJsonArray)
                            }else{
                                smartRefresh.finish(false, mPage, -1)
                            }
                        } ?: smartRefresh.finish(false, mPage, -1)

                    }
                })
    }

    /**
     * 头部信息
     */
    private fun setHeadInfo(objects: JsonObject) {
        mAdapter.headerLayout?.run {
            findViewById<TextView>(R.id.tv_directCount).text =
                    "直接分享（${GsonUtils.parseJson(objects, "directCount", "0")}）"
            findViewById<TextView>(R.id.tv_directActiveCount).text =
                    "有效用户（${GsonUtils.parseJson(objects, "directActiveCount", "0")}）"
            findViewById<TextView>(R.id.tv_directGrade1).text =
                    "金星（${GsonUtils.parseJson(objects, "directGrade1", "0")}）"
            findViewById<TextView>(R.id.tv_directGrade2).text =
                    "木星（${GsonUtils.parseJson(objects, "directGrade2", "0")}）"
            findViewById<TextView>(R.id.tv_directGrade3).text =
                    "水星（${GsonUtils.parseJson(objects, "directGrade3", "0")}）"
            findViewById<TextView>(R.id.tv_directGrade4).text =
                    "火星（${GsonUtils.parseJson(objects, "directGrade4", "0")}）"
            findViewById<TextView>(R.id.tv_directGrade5).text =
                    "土星（${GsonUtils.parseJson(objects, "directGrade5", "0")}）"
            findViewById<TextView>(R.id.tv_directTotalAmount).text =
                    "总团队业绩（${GsonUtils.parseJson(objects, "directTotalAmount", "0")}）"
            findViewById<TextView>(R.id.tv_directTodayAmount).text =
                    "今日团队任务（${GsonUtils.parseJson(objects, "directTodayAmount", "0")}）"


            findViewById<TextView>(R.id.tv_teamCount).text =
                    "粉丝分享（${GsonUtils.parseJson(objects, "teamCount", "0")}）"
            findViewById<TextView>(R.id.tv_teamActiveCount).text =
                    "有效用户（${GsonUtils.parseJson(objects, "teamActiveCount", "0")}）"
            findViewById<TextView>(R.id.tv_teamGrade1).text =
                    "金星（${GsonUtils.parseJson(objects, "teamGrade1", "0")}）"
            findViewById<TextView>(R.id.tv_teamGrade2).text =
                    "木星（${GsonUtils.parseJson(objects, "teamGrade2", "0")}）"
            findViewById<TextView>(R.id.tv_teamGrade3).text =
                    "水星（${GsonUtils.parseJson(objects, "teamGrade3", "0")}）"
            findViewById<TextView>(R.id.tv_teamGrade4).text =
                    "火星（${GsonUtils.parseJson(objects, "teamGrade4", "0")}）"
            findViewById<TextView>(R.id.tv_teamGrade5).text =
                    "土星（${GsonUtils.parseJson(objects, "teamGrade5", "0")}）"
            findViewById<TextView>(R.id.tv_teamTotalAmount).text =
                    "总团队业绩（${GsonUtils.parseJson(objects, "teamTotalAmount", "0")}）"
            findViewById<TextView>(R.id.tv_teamTodayAmount).text =
                    "今日团队任务（${GsonUtils.parseJson(objects, "teamTodayAmount", "0")}）"

        }
    }

    /**
     * 下级用户
     */
    private fun setUsers(jsonArray: JsonArray){
        if(jsonArray.size() < mLimit) {
            smartRefresh.finishLoadMoreWithNoMoreData()
        }
        jsonArray.forEach {
            val obj = it.asJsonObject
            val mobileNo = GsonUtils.parseJson(obj, "mobileNo", "").formatMonbile()
//            val memberGrade = when(GsonUtils.parseJson(obj, "memberGrade", "0").toInt())
//            {
//                0 ->"金星"
//                1 ->"木星"
//                2 ->"水星"
//                3 ->"火星"
//                4 ->"土星"
//                else -> "土星"
//            }
            val memberGradeName = GsonUtils.parseJson(obj, "memberGradeName", "普通")
            mList.add(MemberFansBean(mobileNo, memberGradeName))
        }
        mAdapter.notifyDataSetChanged()
    }
}
