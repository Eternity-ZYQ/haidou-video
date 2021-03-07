package com.yimeng.haidou.task

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.base.BaseActivity
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.entity.ActiveRankingBean
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.yimeng.utils.SpannableStringUtils
import com.yimeng.utils.UnitUtil
import com.yimeng.widget.MyToolBar
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_active_ranking.*
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
class ActiveRankingActivity : BaseActivity() {


    private var mList: MutableList<ActiveRankingBean> = mutableListOf()
    private val mAdapter: BaseQuickAdapter<ActiveRankingBean, BaseViewHolder> by lazy {
        object : BaseQuickAdapter<ActiveRankingBean, BaseViewHolder>(R.layout.adapter_active_ranking_item, mList) {
            override fun convert(helper: BaseViewHolder?, item: ActiveRankingBean?) {

                item?.apply {
                    helper?.apply {
                        setText(R.id.tv_active, extracts)
                        setText(R.id.tv_member_name, nickname)
                        CommonUtils.showImage(getView(R.id.civ_user_head) as ImageView, headPath)

                        var tvRanking = getView<TextView>(R.id.tv_ranking)
                        when (adapterPosition) {
                            0 -> {
                                tvRanking.setBackgroundResource(R.mipmap.ico_ranking_1)
                                tvRanking.text = ""
                            }
                            1 -> {
                                tvRanking.setBackgroundResource(R.mipmap.ico_ranking_2)
                                tvRanking.text = ""
                            }
                            2 -> {
                                tvRanking.setBackgroundResource(R.mipmap.ico_ranking_3)
                                tvRanking.text = ""
                            }
                            else -> {
                                tvRanking.setBackgroundColor(Color.TRANSPARENT)
                                tvRanking.text = SpannableStringUtils.getBuilder((adapterPosition + 1).toString())
                                        .setForegroundColor(ContextCompat.getColor(mContext, R.color.c_333333))
                                        .create()
                            }
                        }
                    }
                }

            }
        }
    }

    private val mOkHttpCommon: OkHttpCommon by lazy { OkHttpCommon() }
    // 我的排行
    private var mineActiveRanking: ActiveRankingBean? = null

    override fun setLayoutResId(): Int = R.layout.activity_active_ranking

    override fun init() {

//        mAdapter.setHeaderView(layoutInflater.inflate(R.layout.adapter_active_ranking_item, null))

        var bundle = intent.extras
        bundle?.let {
            mineActiveRanking = it.getSerializable("mineRanking") as ActiveRankingBean?
        }

        var footView = layoutInflater.inflate(R.layout.adapter_active_ranking_foot, null)
        mineActiveRanking?.apply {
            var split = info.split(",")
            footView.findViewById<TextView>(R.id.tv_active).apply {
                text = split[0]
            }

            CommonUtils.showImage(footView.findViewById<ImageView>(R.id.civ_user_head), split[2])

            footView.findViewById<TextView>(R.id.tv_member_name).apply {
                text = split[1]
            }
            footView.findViewById<TextView>(R.id.tv_ranking).apply {

                when (UnitUtil.getInt(rowno)) {
                    0 -> {
                        text = "-"
                    }
                    1 -> {
                        text = ""
                        setBackgroundResource(R.mipmap.ico_ranking_1)
                    }
                    2 -> {
                        text = ""
                        setBackgroundResource(R.mipmap.ico_ranking_2)
                    }
                    3 -> {
                        text = ""
                        setBackgroundResource(R.mipmap.ico_ranking_3)
                    }
                    else -> {
                        text = rowno
                    }
                }
            }
        }
        mAdapter.setFooterView(footView)



        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ActiveRankingActivity)
            adapter = mAdapter
        }
    }

    override fun initListener() {
        toolBar.setOnToolBarClickListener(MyToolBar.OnToolBarClick())

        smartRefresh.setOnRefreshListener {
            loadData()
        }

    }

    override fun loadData() {

        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_ACTIVE_RANKING_LIST, CommonUtils.createParams(), object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                smartRefresh.finishRefresh()
                ToastUtils.showToast("获取排行数据失败！")
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                smartRefresh.finishRefresh()
                if (jsonObject?.get("status")?.asInt != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取排行数据失败！"))
                    return
                }

                var arr = jsonObject.get("data").asJsonObject.get("rows").asJsonArray
                if(arr.size() == 0) return
                var list = GsonUtils.getGson().fromJson<List<ActiveRankingBean>>(arr.toString(),
                        object : TypeToken<List<ActiveRankingBean>>() {}.type)
                if (mList.isNotEmpty()) mList.clear()
                mList.addAll(list)
                mAdapter.notifyDataSetChanged()
            }
        })

    }

}
