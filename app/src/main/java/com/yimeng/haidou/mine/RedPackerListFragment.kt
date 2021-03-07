package com.yimeng.haidou.mine

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.base.BaseFragment
import com.yimeng.config.Constants
import com.yimeng.config.ConstantsUrl
import com.yimeng.config.EventTags
import com.yimeng.haidou.R
import com.yimeng.dialog.SimpleDialog
import com.yimeng.entity.RedPackerBean
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.*
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.ToastUtils
import com.lxj.xpopup.XPopup
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import kotlinx.android.synthetic.main.activity_all_red_packer.*
import kotlinx.android.synthetic.main.fragment_sale_fruit.*
import okhttp3.Call
import org.simple.eventbus.Subscriber
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/26 6:35 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 红包列表
 * </pre>
 */
class RedPackerListFragment : BaseFragment() {

    private var mStatus = ""
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
                                .setImageResource(R.id.iv_share, if (mStatus == "common") R.mipmap.icon_menu else R.mipmap.ico_share)
                                .setGone(R.id.iv_share, mStatus != "balence")
                                .addOnClickListener(R.id.iv_share)

                        CommonUtils.showImage(it.getView(R.id.iv_rp), projectLogo)

                    }
                }
            }
        }
    }

    companion object {
        fun getInstance(position: Int): RedPackerListFragment = RedPackerListFragment().apply {
            arguments = Bundle().apply { putInt("position", position) }
        }
    }


    override fun setLayoutResId(): Int = R.layout.fragment_red_packer_list

    override fun init() {
        arguments?.let {
            mStatus = when (it.getInt("position")) {
                0 -> "common" // 创建，未塞红包
                1 -> "progress" // 已塞红包
                else -> "balence" // 已领完
            }
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = mAdapter
        }


    }

    override fun initListener() {
        smartRefresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                getData()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                getData()
            }
        })

        mAdapter.setOnItemClickListener { _, _, position ->
            var redPackerBean = mList[position]
            when (mStatus) {
                "common" -> { // 未推广
//                    ActivityUtils.getInstance().jumpActivity(SetRedPackerInfoActivity::class.java, Bundle().apply {
//                        putString("projectNo", redPackerBean.modeProjectNo)
//                        putString("rpTitle", redPackerBean.projectTitle)
//                        putString("rpContent", redPackerBean.projectContent)
//                    })

                    ActivityUtils.getInstance().jumpActivity(RedPackerActivity::class.java, Bundle().apply {
                        putSerializable("RedPackerBean", redPackerBean)
                    })
                }

                "progress" -> { // 已塞红包
                    ActivityUtils.getInstance().jumpActivity(RedPackerResidueActivity::class.java, Bundle().apply {
                        putSerializable("RedPackerBean", redPackerBean)
                    })
                }

                "balence" -> { // 已领完
                    ActivityUtils.getInstance().jumpActivity(RedPackerResidueActivity::class.java, Bundle().apply {
                        putSerializable("RedPackerBean", redPackerBean)
                    })
                }
            }
        }

        mAdapter.setOnItemChildClickListener { _, view, position ->
            var redPackerBean = mList[position]
            when (view.id) {
                R.id.iv_share -> {  // 分享
                    if (mStatus == "common") {
                        showMenu(view, position, redPackerBean)
                    } else {
                        shareRedPacker(redPackerBean)
                    }
                }
            }
        }
    }

    /**
     * 显示菜单
     */
    private fun showMenu(view: View, index: Int, redPackerBean: RedPackerBean) {
        XPopup.Builder(activity)
                .autoDismiss(true)
                .atView(view)
                .asAttachList(arrayOf("编辑", "删除", "分享"), null) { position, _ ->
                    when (position) {
                        0 -> {
                            ActivityUtils.getInstance().jumpActivity(RedPackerActivity::class.java, Bundle().apply {
                                putSerializable("RedPackerBean", redPackerBean)
                            })
                        }
                        1 -> {
                            SimpleDialog.showConfirmDialog(activity, "提示", "确定要删除吗？", null, {
                                delRedPacker(index, redPackerBean.modeProjectNo)
                            })
                        }
                        2 -> {
                            shareRedPacker(redPackerBean)
                        }
                    }
                }
                .show()
    }

    /**
     * 删除红包
     */
    private fun delRedPacker(position: Int, redPackerNo: String) {
        mOkHttpCommon.postLoadData(activity, ConstantsUrl.URL_DEL_RED_PACKER, CommonUtils.createParams().apply {
            put("modeProjectNo", redPackerNo)
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
                    ToastUtils.showToast(GsonUtils.parseJson(it, "msg", "删除成功"))
                    mAdapter.remove(position)
                }
            }
        })
    }

    /**
     * 分享红包
     */
    private fun shareRedPacker(redPackerBean: RedPackerBean) {
        UMShareUtils.getInstance().shareURL(activity!!, ConstantsUrl.URL_SHARE_RED_PACKER + redPackerBean.modeProjectNo,
                shareTitle = redPackerBean.projectTitle, shareDescription = redPackerBean.projectContent,
                shareLogo = R.mipmap.icon_share_red_packer, callback = object : UMShareListener {
            override fun onResult(p0: SHARE_MEDIA?) {
                ToastUtils.showToast("分享成功")
                ActivityUtils.getInstance().jumpActivity(AllRedPackerActivity::class.java, Bundle().apply {
                    putInt("index", 1)
                })
            }

            override fun onCancel(p0: SHARE_MEDIA?) {
                ToastUtils.showToast("您已取消分享")
                ActivityUtils.getInstance().jumpActivity(AllRedPackerActivity::class.java, Bundle().apply {
                    putInt("index", 1)
                })
            }

            override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
                ToastUtils.showToast("分享失败")
            }

            override fun onStart(p0: SHARE_MEDIA?) {
                ToastUtils.showToast("正在启动分享,请稍后!")
            }
        })
    }

    override fun loadData() {

    }

    override fun onResume() {
        super.onResume()
        smartRefresh?.autoRefresh()
    }

    private fun getData() {
        mOkHttpCommon.postLoadData(activity, ConstantsUrl.URL_GET_ALL_RED_PACKER, CommonUtils.createParams().apply {
            put("status", mStatus)
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

                    var arrayJsonStr = it.get("data").asJsonObject.get("rows").asJsonArray.toString()
                    var list = GsonUtils.getGson().fromJson<List<RedPackerBean>>(arrayJsonStr,
                            object : TypeToken<List<RedPackerBean>>() {}.type)
                    if (list.size < Constants.MAX_LIMIT) {
                        smartRefresh.finishLoadMoreWithNoMoreData()
                    }

                    if (mList.isNotEmpty() && mPage == 1) {
                        mList.clear()
                    }
                    mPage++
                    mList.addAll(list)
                    mAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    @Subscriber(tag = EventTags.WECHAT_SHARE_RESULT)
    private fun onShareResult(result: Boolean) {
        (activity as AllRedPackerActivity).viewPager.currentItem = 1
    }
}