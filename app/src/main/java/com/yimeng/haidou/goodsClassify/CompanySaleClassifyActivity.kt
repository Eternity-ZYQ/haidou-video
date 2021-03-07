package com.yimeng.haidou.goodsClassify

import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yimeng.base.BaseActivity
import com.yimeng.config.ConstantHandler
import com.yimeng.haidou.R
import com.yimeng.haidou.shop.SortListActivity
import com.yimeng.entity.ModelProductCategories
import com.yimeng.entity.ModelProductCategoriesContent
import com.yimeng.net.lxmm_net.HttpParameterUtil
import com.yimeng.utils.CommonUtils
import com.yimeng.utils.SpannableStringUtils
import com.yimeng.widget.MyToolBar
import com.huige.library.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_company_sale.*
import java.lang.ref.WeakReference
import java.util.*

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/5/20 9:49 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 厂促商品
 * </pre>
 */
class CompanySaleClassifyActivity : BaseActivity() {

    /**
     * 一级分类
     */
    private lateinit var mClassifyAdapter: BaseQuickAdapter<ModelProductCategories, BaseViewHolder>
    private lateinit var mList: LinkedList<ModelProductCategories>

    /**
     * 二级分类
     */
    private lateinit var mProductAdapter: BaseQuickAdapter<ModelProductCategoriesContent, BaseViewHolder>
    private lateinit var mContentList: LinkedList<ModelProductCategoriesContent>


    private val mHandler = MyHandler(this)
    /**
     * 0. 普通购买
     * 1. 任务购买
     * 2. 3.0任务购买
     */
    private var mTaskType = 0
    // 任务编号
    private var mTaskItemNo: String = ""
    //3.0任务区块编号
    private var mBlockNo: String = ""


    /**
     * 最后一次选中的下标
     */
    private var lastCheckedPosition = 0

    override fun setLayoutResId(): Int = R.layout.activity_company_sale

    override fun init() {

        mList = LinkedList()
        mContentList = LinkedList()
        initRecyclerView()

        var bundle = intent.extras
        bundle?.let {
            mTaskType = it.getInt("taskType", 0)
            mTaskItemNo = it.getString("taskItemNo", "")
            mBlockNo = it.getString("blockNo", "")

            var title = it.getString("title", "")
            if (title.isNotEmpty()) {
                toolBar.title = title
            }
        }

    }

    private fun initRecyclerView() {
        // 一级分类
        recyclerView_classify.layoutManager = LinearLayoutManager(this)
        mClassifyAdapter = object : BaseQuickAdapter<ModelProductCategories, BaseViewHolder>(
                R.layout.item_lv_sort_list, mList
        ) {
            override fun convert(helper: BaseViewHolder, item: ModelProductCategories) {

                helper.setText(R.id.tv_title, if (item.isSelected)
                    SpannableStringUtils.getBuilder(item.name).setBold().create()
                else
                    item.name)
                        .setVisible(R.id.line, item.isSelected)
                        .setTextColor(R.id.tv_title, if (item.isSelected)
                            resources.getColor(R.color.theme_color)
                        else
                            resources.getColor(R.color.c_333333))
                        .setBackgroundColor(R.id.rl_click, if (item.isSelected)
                            Color.WHITE
                        else
                            resources.getColor(R.color.c_f5f5f5))
            }
        }
        recyclerView_classify.adapter = mClassifyAdapter

        // 二级分类
        recyclerView_product.layoutManager = GridLayoutManager(this, 3)
        mProductAdapter = object : BaseQuickAdapter<ModelProductCategoriesContent, BaseViewHolder>(
                R.layout.item_lv_sort_content, mContentList
        ) {
            override fun convert(helper: BaseViewHolder, item: ModelProductCategoriesContent) {
                CommonUtils.showImage(helper.getView<View>(R.id.sdv_image) as ImageView, item.logo)
                helper.setText(R.id.tv_sort_content_title, item.name)

            }
        }
        recyclerView_product.adapter = mProductAdapter
    }

    override fun initListener() {
        toolBar.setOnToolBarClickListener(MyToolBar.OnToolBarClick())
        smartRefresh.setOnRefreshListener { loadData() }

        mClassifyAdapter.setOnItemClickListener { adapter, view, position ->
            mList[lastCheckedPosition].isSelected = false
            mClassifyAdapter.notifyItemChanged(lastCheckedPosition)

            val productCategories = mList[position]
            productCategories.isSelected = true
            mClassifyAdapter.notifyItemChanged(position)

            mContentList.clear()
            if (productCategories.isShopSale) {
                getShopSale()
            } else {
                mContentList.addAll(productCategories.modelProductCategoriesContentsList)
                mProductAdapter.notifyDataSetChanged()
            }
            lastCheckedPosition = position
        }

        mProductAdapter.setOnItemClickListener { adapter, view, position ->
            val modelProductCategoriesContent = mContentList[position]
            val menuNo = modelProductCategoriesContent.menuNo
            val title = modelProductCategoriesContent.name

            //内容点击事件
            val intent = Intent()
            intent.putExtra("extra", menuNo)
            intent.putExtra("title", title)
            intent.putExtra("taskType", mTaskType)
            intent.putExtra("taskItemNo", mTaskItemNo)
            intent.putExtra("blockNo", mBlockNo)
            if (modelProductCategoriesContent.isShopSale)
                intent.putExtra("isShopSale", true)
            intent.setClass(this, SortListActivity::class.java)
            startActivity(intent)

        }
    }

    override fun loadData() {
        HttpParameterUtil.getInstance().requestCompanyProductCategories(mTaskType, mBlockNo, mHandler)
    }

    private class MyHandler(mImpl: CompanySaleClassifyActivity) : Handler() {

        private val mImpl: WeakReference<CompanySaleClassifyActivity> = WeakReference(mImpl)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            mImpl.get()?.disposeData(msg)
        }
    }

    private fun disposeData(msg: Message) {
        when (msg.what) {
            ConstantHandler.WHAT_PRODUCT_CATEGORIES_FAIL -> {
                smartRefresh.finishRefresh()
                ToastUtils.showToast(msg.obj as String)
            }
            ConstantHandler.WHAT_PRODUCT_CATEGORIES_SUCCESS -> {
                //列表网络请求
                smartRefresh.finishRefresh()
                val list = msg.obj as LinkedList<ModelProductCategories>
                if (null == list || list.size == 0) {
                    return
                }
//                SharedPreferencesUtils.put(Constants.CLASSIFY_DATA_CACHE, GsonUtils.getGson().toJson(list))
                setData(list)

                // TODO new: 2019/5/30 新任务不需要店铺自促商品
//                if (mShopNo.isNotEmpty())
//                    HttpParameterUtil.getInstance().requestShopDetail(mShopNo, mHandler)
            }
/*

            ConstantHandler.WHAT_SHOP_DETAIL_SUCCESS -> {
                // 店铺详情
                var mModelShopDetail = msg.obj as ModelShopDetail
                // 判断店铺是否在当前定位的区域
                var lat1 = mModelShopDetail.latitude.toDouble()
                var lon1 = mModelShopDetail.longitude.toDouble()
                var lat2 = (SharedPreferencesUtils.get(Constants.APP_LOCATION_LATITUDE, Constants.APP_DEFAULT_LATITUDE) as String).toDouble()
                var lon2 = (SharedPreferencesUtils.get(Constants.APP_LOCATION_LONGITUDE, Constants.APP_DEFAULT_LONGITUDE) as String).toDouble()
                var notTheSameArea = false
                if (mModelShopDetail.city == (SharedPreferencesUtils.get(Constants.APP_LOCATION_CITY, "深圳市") as String)
                        && mModelShopDetail.area == (SharedPreferencesUtils.get(Constants.APP_LOCATION_AREA, "龙华区") as String)
                ) {
                    notTheSameArea = true
                }


                // 如果在同一区域并且距离小于2000m显示店铺自促商品
                if (notTheSameArea && CommonUtils.calcDistance(lat1, lon1, lat2, lon2) <= 2000.00) {
//                    getShopSale()
                    var modelProductCategories = ModelProductCategories()
                    modelProductCategories.name = "自促商品"
                    modelProductCategories.isShopSale = true
                    mList.add(modelProductCategories)
                    mClassifyAdapter.notifyLoadMoreToLoading()
                }
            }

            ConstantHandler.WHAT_SHOP_PRODUCT_CLASSIFY_SUCCESS -> {
                // 商品分类统计
                val list = msg.obj as List<ShopDetailClassifyBean>

                val shopSaleList = mutableListOf<ModelProductCategoriesContent>()
                list.forEach {
                    var modelProductCategoriesContent = ModelProductCategoriesContent()
                    modelProductCategoriesContent.isShopSale = true
                    modelProductCategoriesContent.logo = it.logo
                    modelProductCategoriesContent.name = it.productCategoryName
                    modelProductCategoriesContent.menuNo = it.productCategoryNo
                    shopSaleList.add(modelProductCategoriesContent)
                }
                mContentList.addAll(shopSaleList)
                mProductAdapter.notifyDataSetChanged()
            }
*/

        }
    }

    /**
     * 获取店铺自促商品
     */
    private fun getShopSale() {
//        HttpParameterUtil.getInstance().requestShopClassify(mShopNo, "cuxiao", mHandler)
    }


    private fun setData(list: LinkedList<ModelProductCategories>) {
        if (mList == null || mContentList == null || list == null) return
        if (!mList.isEmpty()) {
            mList.clear()
        }
        lastCheckedPosition = 0
        mList.addAll(list)
        mList[0].isSelected = true
        mClassifyAdapter.notifyDataSetChanged()

        if (!mContentList.isEmpty()) {
            mContentList.clear()
        }
        mContentList.addAll(mList[0].modelProductCategoriesContentsList)
        mProductAdapter.notifyDataSetChanged()

    }

}
