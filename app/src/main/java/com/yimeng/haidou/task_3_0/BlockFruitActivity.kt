package com.yimeng.haidou.task_3_0

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.base.BaseActivity
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.entity.TaskBlockBean
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.ActivityUtils
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.GsonUtils
import com.yimeng.widget.MyToolBar
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.DeviceUtils
import com.huige.library.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_block_fruit.*
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/23 11:39 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 区块果子
 * </pre>
 */
class BlockFruitActivity : BaseActivity() {

    /**
     * 1. 我的活跃果
     * 2. 抢购活跃果
     */
    private var mType = 1
    private var mList = mutableListOf<TaskBlockBean>()
    private val mAdapter: BaseQuickAdapter<TaskBlockBean, BaseViewHolder> by lazy {
        object : BaseQuickAdapter<TaskBlockBean, BaseViewHolder>(
                R.layout.adapter_team_task_section, mList
        ) {
            override fun convert(helper: BaseViewHolder?, item: TaskBlockBean?) {
                item?.apply {
                    helper?.let {
                        CommonUtils.showRadiusImage(it.getView(R.id.iv), item.blockImage,
                                DeviceUtils.dp2px(mContext, 10f), true, true, true, true)
                    }
                }
            }
        }
    }

    private val mOkHttpCommon = OkHttpCommon()

    override fun setLayoutResId(): Int = R.layout.activity_block_fruit

    override fun init() {

        intent.extras?.let {
            mType = it.getInt("type", 1)
        }

        if (mType == 1) {
            toolBar.title = "我的活跃果"
        } else {
            toolBar.title = "任务专区"
        }

        recyclerView.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(this@BlockFruitActivity, 2)
            isNestedScrollingEnabled = false
        }


    }

    override fun initListener() {

        toolBar.setOnToolBarClickListener(MyToolBar.OnToolBarClick())

        smartRefresh.setOnRefreshListener { getData() }

        mAdapter.setOnItemClickListener { _, _, position ->
            var taskBlockBean = mList[position]
            ActivityUtils.getInstance().jumpActivity(BlockTaskFruitActivity2::class.java, Bundle().apply {
                putInt("type", mType)
                putString("blockNo", taskBlockBean.blockNo)
                putString("blockName", taskBlockBean.blockName)
            })
        }

    }

    override fun loadData() {
        smartRefresh.autoRefresh()
    }

    private fun getData() {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_TEAM_TASK_BLOCK, CommonUtils.createParams().apply {
            put("start", "0")
            put("limit", Int.MAX_VALUE.toString())
            put("isOpen", "1")
        }, object : CallbackCommon {
            override fun onFailure(call: Call?, e: IOException?) {
                ToastUtils.showToast(R.string.net_error)
                smartRefresh.finishRefresh()
            }

            override fun onResponse(call: Call?, jsonObject: JsonObject?) {
                smartRefresh.finishRefresh()
                jsonObject?.let {
                    if (it.get("status").asInt != 1) {
                        ToastUtils.showToast(GsonUtils.parseJson(it, "msg", getString(R.string.net_error)))
                        return
                    }
                    var dataObj = it.get("data").asJsonObject
                    var jsonArray = dataObj.get("rows").asJsonArray
                    var list = GsonUtils.getGson().fromJson<List<TaskBlockBean>>(jsonArray.toString(), object : TypeToken<List<TaskBlockBean>>() {}.type)
                    if (mList.isNotEmpty()) mList.clear()

//                    list.forEach { block ->
//                        if (block.isOpen == 1) {
//                            mList.add(block)
//                        }
//                    }
                    mList.addAll(list)
                    mAdapter.notifyDataSetChanged()

                }
            }
        })
    }

}
