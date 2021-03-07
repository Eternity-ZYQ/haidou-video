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
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.yimeng.utils.finish
import com.yimeng.utils.formatMonbile
import com.yimeng.widget.MyToolBar
import com.yimeng.haidou.R
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_my_active.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-19 23:26.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 我的活跃用户
 * </pre>
 */
class MyActiveActivity : BaseActivity() {

    companion object {
        fun start(){
            val topActivity = ActivityUtils.getTopActivity()
            topActivity.startActivity(Intent(topActivity, MyActiveActivity::class.java))
        }
    }

    private val mList = mutableListOf<MemberFansBean>()
    private val mAdapter = object : BaseQuickAdapter<MemberFansBean, BaseViewHolder>(
            R.layout.adapter_fans_item, mList
    ) {
        override fun convert(helper: BaseViewHolder, item: MemberFansBean?) {

            item?.run {
                helper.setText(R.id.tv_user_name, mobileNo)
                        .setText(R.id.tv_user_mobile, "活跃值：$memberGrade")
            }

        }
    }

    private var mPage = 1
    private val mLimit = 20

    override fun setLayoutResId(): Int = R.layout.activity_my_active

    override fun init() {

        recyclerView.run {
            layoutManager = LinearLayoutManager(this@MyActiveActivity)
            adapter = mAdapter
        }

        val headView = LayoutInflater.from(this).inflate(R.layout.layout_active_head, null)

        mAdapter.addHeaderView(headView, 0)
    }

    override fun loadData() {
        getMembers()
    }


    override fun initListener() {

        toolBar.setOnToolBarClickListener(object : MyToolBar.OnToolBarClick(){})

        smartRefresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPage++
                getMembers()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                if (mList.isNotEmpty()) mList.clear()
                getMembers()
            }

        })
    }

    private fun getMembers() {
        val start = (mPage - 1) * mLimit
        RetrofitHelper.toDo().getMyActive(CommonUtils.getMember().memberNo,start, mLimit)
                .enqueue(object : Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        smartRefresh.finish(false, mPage, -1)
                    }

                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        response.body()?.run {
                            if(get("status").asInt == 1) {
                                smartRefresh.finish(true, mPage, -1)

                                try {
                                    val dataObj = get("data").asJsonObject
                                    if(dataObj.get("otherData") != null || !dataObj.get("v").isJsonNull) {
                                        val headObject = dataObj.get("otherData").asJsonObject
                                        setHeadInfo(headObject)
                                    }

                                    if(dataObj.get("rows") != null || !dataObj.get("rows").isJsonNull) {
                                        setUsers(dataObj.get("rows").asJsonArray)
                                    }
                                } catch (e: Exception) {
                                }
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
                    "大合：${GsonUtils.parseJson(objects, "bigScore", "0")}"

            findViewById<TextView>(R.id.tv_teamCount).text =
                    "小合：${GsonUtils.parseJson(objects, "smallScore", "0")}"


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
            val memberGrade =GsonUtils.parseJson(obj, "smallScore", "0")

            mList.add(MemberFansBean(mobileNo, memberGrade))
        }
        mAdapter.notifyDataSetChanged()
    }
}
