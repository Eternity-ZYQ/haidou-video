package com.yimeng.haidou.mine

import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.base.BaseActivity
import com.yimeng.config.ConstantsUrl
import com.yimeng.haidou.R
import com.yimeng.entity.GetRedPackerBean
import com.yimeng.entity.RedPackerBean
import com.yimeng.net.CallbackCommon
import com.yimeng.net.OkHttpCommon
import com.yimeng.utils.*
import com.yimeng.widget.MyToolBar
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.huige.library.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_red_packer_residue.*
import okhttp3.Call
import java.io.IOException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/8/28 4:52 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 红包剩余
 * </pre>
 */
class RedPackerResidueActivity : BaseActivity() {

    // 红包
    private var mRedPackerBean: RedPackerBean? = null
    private val mOkHttpCommon = OkHttpCommon()
    private var mList = mutableListOf<GetRedPackerBean>()
    private val mAdapter: BaseQuickAdapter<GetRedPackerBean, BaseViewHolder> by lazy {
        object : BaseQuickAdapter<GetRedPackerBean, BaseViewHolder>(
                R.layout.adapter_red_packer_residue_item, mList
        ) {

            override fun convert(helper: BaseViewHolder?, item: GetRedPackerBean?) {
                helper?.let {
                    item?.apply {

                        CommonUtils.showImage(it.getView(R.id.iv_user_head), headPath)
                        it.setText(R.id.tv_user_name, memberName)
                                .setText(R.id.tv_user_mobile, mobileNo)
                                .setText(R.id.tv_time, DateUtil.getAssignDate(createTime, 3))
//                                .setText(R.id.tv_user_proxy, if (status == "common") "可销售" else "可消费")
                                .setText(R.id.tv_user_proxy, String.format("￥%.2f",receiveAmt/100.0))
                                .setTextColor(R.id.tv_user_proxy, ContextCompat.getColor(mContext, R.color.c_money))
                                .setText(R.id.tv_shop_name, if (shopName.isNullOrBlank()) "未开店" else shopName)

                    }
                }

            }
        }
    }

    override fun setLayoutResId(): Int = R.layout.activity_red_packer_residue

    override fun init() {
        intent.extras?.let {
            mRedPackerBean = it.getSerializable("RedPackerBean") as RedPackerBean
        }

        mRedPackerBean?.apply {
            toolBar.title = projectTitle
            tv_rp_content.text = projectContent
            tv_rp_price.text = UnitUtil.getMoney(shareTotalPrice)
            tv_time.text = DateUtil.getAssignDate(updateTime, 3)
            tv_rp_residue.text = SpannableStringUtils.getBuilder("新用户红包已领取")
                    .append("${sharePaidNum - nowHadPaidNum}/${sharePaidNum}个，")
                    .append("共${String.format("%.2f", newSum - UnitUtil.getInt(shareGetPrice) / 100.0)}")
                    .append("/$newSum")
                    .append("元")
                    .create()

            if (externalLinks == "default") { // 新老用户
                tv_rp_old_residue.apply {
                    text = SpannableStringUtils.getBuilder("老用户红包已领取")
                            .append("${oldUserNum - nextHadPaidNum}/${oldUserNum}个，")
                            .append("共${String.format("%.2f", oldSum - UnitUtil.getInt(oldGetPrice) / 100.0)}")
                            .append("/$oldSum")
                            .append("元")
                            .create()
                    visibility = View.VISIBLE
                }

                tv_rp_info.text = "新用户可领${sharePaidNum}个，${UnitUtil.getMoney(sharePrice, false)}元/个\n老用户可领${oldUserNum}个，${UnitUtil.getMoney(nextSharePrice, false)}元/个"
            }else{
                tv_rp_info.text = "新用户可领${sharePaidNum}个，${UnitUtil.getMoney(sharePrice, false)}元/个"
            }


        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@RedPackerResidueActivity)
            adapter = mAdapter
        }

    }

    override fun initListener() {
        toolBar.setOnToolBarClickListener(MyToolBar.OnToolBarClick())
    }

    override fun loadData() {
        mOkHttpCommon.postLoadData(this, ConstantsUrl.URL_GET_RED_PACKER_INFO, CommonUtils.createParams().apply {
            put("projectNo", mRedPackerBean?.modeProjectNo)
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
                    var arrayObjStr = it.get("data").asJsonObject.get("rows").asJsonArray.toString()
                    var list = GsonUtils.getGson().fromJson<List<GetRedPackerBean>>(arrayObjStr, object : TypeToken<List<GetRedPackerBean>>() {}.type)
                    mList.addAll(list)
                    mAdapter.notifyDataSetChanged()
                }
            }
        })
    }


}
