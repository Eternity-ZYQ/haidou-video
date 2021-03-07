package com.yimeng.haidou.mine

import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.base.BaseActivity
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.entity.ModelCompanion
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.DateUtil
import com.yimeng.utils.GsonUtils
import com.yimeng.widget.MyToolBar
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.ToastUtils
import com.huige.library.widget.CircleImageView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_invite_share.*
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/6/25 7:00 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 我邀请的伙伴，再邀请的人
 * </pre>
 */
class InviteShareActivity : BaseActivity() {

    private var memberNo: String = ""
    private var mPage = 1
    private var mList: MutableList<ModelCompanion> = mutableListOf()
    private val mAdapter: BaseQuickAdapter<ModelCompanion, BaseViewHolder> by lazy {
        object : BaseQuickAdapter<ModelCompanion, BaseViewHolder>(
                R.layout.adapter_my_invite_item, mList
        ) {
            override fun convert(helper: BaseViewHolder?, item: ModelCompanion?) {
                helper?.apply {
                    item?.apply {
                        CommonUtils.showImage(getView<View>(R.id.iv_user_head) as CircleImageView, item.headPath)

                        setText(R.id.tv_user_name, item.nickname)
                        setText(R.id.tv_user_mobile, item.mobileNo)
                        setText(R.id.tv_time, DateUtil.getAssignDate(item.createTime, 3))
                        setText(R.id.tv_user_grade, if (item.tuijianMemberName == "common") "可销售" else "可消费")
                        setText(R.id.tv_shop_name, if (TextUtils.isEmpty(item.shopNames)) "未开店" else item.shopNames)
                        addOnClickListener(R.id.tv_user_mobile)
                    }
                }
            }
        }
    }

    private val mOkHttpCommon: OkHttpCommon by lazy { OkHttpCommon() }

    override fun setLayoutResId(): Int = R.layout.activity_invite_share

    override fun init() {

        intent.extras?.apply {
            memberNo = getString("memberNo")
            var nickname = getString("nickname")
            toolBar.title = "${nickname}邀请的伙伴"
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@InviteShareActivity)
            adapter = mAdapter
        }

        mAdapter.setEmptyView(R.layout.layout_empty, recyclerView)

    }

    override fun initListener() {
        toolBar.setOnToolBarClickListener(MyToolBar.OnToolBarClick())

        mAdapter.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener {
            adapter, view, position ->
            val modelCompanion = mList[position]
            when(view.id){
                R.id.tv_user_mobile->{
                    // 拨打电话
//                    ActivityUtils.getInstance().diallPhone(this@InviteShareActivity, modelCompanion.mobileNo)
                }
            }

        }

        smartRefresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                loadData()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                loadData()
            }
        })
    }

    override fun loadData() {
        var params = CommonUtils.createParams()
        CommonUtils.addPageParams(params, mPage)
        params["memberNo"] = memberNo
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_INVITE_SHARE, params, object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
                showSmartRefreshGetDataFail(smartRefresh, mPage)
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                showSmartRefreshGetDataFail(smartRefresh, mPage)

                jsonObject?.apply {
                    if (jsonObject.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取数据失败，请稍后重试"))
                        return
                    }

                    if(jsonObject.get("data").isJsonNull) return

                    var asJsonArray = jsonObject.get("data").asJsonObject.get("rows").asJsonArray

                    if(asJsonArray.isJsonNull) return

                    var list = GsonUtils.getGson().fromJson<List<ModelCompanion>>(asJsonArray.toString(), object : TypeToken<List<ModelCompanion>>() {}.type)
                    if (list.isEmpty()) {
                        smartRefresh.finishLoadMoreWithNoMoreData()
                    }

                    if (mPage == 1) {
                        mList.clear()
                    }
                    mPage++
                    mList.addAll(list)
                    mAdapter.notifyDataSetChanged()
                }
            }
        })
    }
}
