package com.yimeng.haidou.goodsClassify;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.base.BaseFragment;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.Constants;
import com.yimeng.haidou.R;
import com.yimeng.haidou.shop.PlatformProductListActivity;
import com.yimeng.haidou.shop.SortListActivity;
import com.yimeng.entity.ModelProductCategories;
import com.yimeng.entity.ModelProductCategoriesContent;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.utils.SpannableStringUtils;
import com.yimeng.widget.MyToolBar;
import com.google.gson.reflect.TypeToken;
import com.huige.library.utils.SharedPreferencesUtils;
import com.huige.library.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

import butterknife.Bind;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/3/23 0023 上午 10:18.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 分类
 * </pre>
 */
public class TabGoodsClassifyFragment extends BaseFragment {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.recyclerView_classify)
    RecyclerView recyclerViewClassify;
    @Bind(R.id.recyclerView_product)
    RecyclerView recyclerViewProduct;
    @Bind(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;

    /**
     * 一级分类
     */
    private BaseQuickAdapter<ModelProductCategories, BaseViewHolder> mClassifyAdapter;
    private LinkedList<ModelProductCategories> mList;

    /**
     * 二级分类
     */
    private BaseQuickAdapter<ModelProductCategoriesContent, BaseViewHolder> mProductAdapter;
    private LinkedList<ModelProductCategoriesContent> mContentList;


    private MyHandler mHandler = new MyHandler(this);
    /**
     * 促销商品
     */
    private boolean mIsSale = false;
    /**
     * 店铺编号
     */
    private String mShopNo;
    /**
     * 商品分类编号
     */
    private String mProductCategoryNo;

    public static TabGoodsClassifyFragment getInstanceGoodsClassify(Bundle bundle) {
        TabGoodsClassifyFragment fragment = new TabGoodsClassifyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int setLayoutResId() {
        return R.layout.fragment_tab_goods_classify2;
    }

    @Override
    protected void init() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            boolean isHideTitle = bundle.getBoolean("isHideTitle", false);
            toolBar.setVisibility(isHideTitle ? View.GONE : View.VISIBLE);
            mIsSale = bundle.getBoolean("isSale", false);
            mShopNo = bundle.getString("shopNo");
            mProductCategoryNo = bundle.getString("productCategoryNo");
            if (mIsSale) {
                toolBar.setLeftIcon(R.mipmap.back_wihte);
            }
        }

        mList = new LinkedList<>();
        mContentList = new LinkedList<>();
        initRecyclerView();

        LinkedList<ModelProductCategories> list = GsonUtils.getGson().fromJson((String) SharedPreferencesUtils.get(Constants.CLASSIFY_DATA_CACHE, ""), new TypeToken<LinkedList<ModelProductCategories>>() {
        }.getType());
        setData(list);
    }

    private void initRecyclerView() {
        // 一级分类
        recyclerViewClassify.setLayoutManager(new LinearLayoutManager(getContext()));
        mClassifyAdapter = new BaseQuickAdapter<ModelProductCategories, BaseViewHolder>(
                R.layout.item_lv_sort_list, mList
        ) {
            @Override
            protected void convert(BaseViewHolder helper, ModelProductCategories item) {

                helper.setText(R.id.tv_title, item.isSelected() ?
                        SpannableStringUtils.getBuilder(item.getName()).setBold().create()
                        : item.getName())
                        .setVisible(R.id.line, item.isSelected())
                        .setTextColor(R.id.tv_title, item.isSelected() ? getResources().getColor(R.color.theme_color) :
                                getResources().getColor(R.color.c_333333))
                        .setBackgroundColor(R.id.rl_click, item.isSelected() ? Color.WHITE :
                                getResources().getColor(R.color.c_f5f5f5))
                ;

            }
        };
        recyclerViewClassify.setAdapter(mClassifyAdapter);

        // 二级分类
        recyclerViewProduct.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mProductAdapter = new BaseQuickAdapter<ModelProductCategoriesContent, BaseViewHolder>(
                R.layout.item_lv_sort_content, mContentList
        ) {
            @Override
            protected void convert(BaseViewHolder helper, ModelProductCategoriesContent item) {
                CommonUtils.showImage(helper.getView(R.id.sdv_image), item.getLogo());
                helper.setText(R.id.tv_sort_content_title, item.getName());

            }
        };
        recyclerViewProduct.setAdapter(mProductAdapter);
    }

    private void setData(LinkedList<ModelProductCategories> list) {
        if (mList == null || mContentList == null || list == null) return;
        if (!mList.isEmpty()) {
            mList.clear();
        }
        lastCheckedPosition = 0;
        mList.addAll(list);
        mList.get(0).setSelected(true);
        mClassifyAdapter.notifyDataSetChanged();

        if (!mContentList.isEmpty()) {
            mContentList.clear();
        }
        mContentList.addAll(mList.get(0).getModelProductCategoriesContentsList());
        mProductAdapter.notifyDataSetChanged();

    }

    /**
     * 最后一次选中的下标
     */
    private int lastCheckedPosition = 0;

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });

        mClassifyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mList.get(lastCheckedPosition).setSelected(false);
                mClassifyAdapter.notifyItemChanged(lastCheckedPosition);

                ModelProductCategories productCategories = mList.get(position);
                productCategories.setSelected(true);
                mClassifyAdapter.notifyItemChanged(position);

                mContentList.clear();
                mContentList.addAll(productCategories.getModelProductCategoriesContentsList());
                mProductAdapter.notifyDataSetChanged();

                lastCheckedPosition = position;
            }
        });

        mProductAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ModelProductCategoriesContent modelProductCategoriesContent = mContentList.get(position);
                String menuNo = modelProductCategoriesContent.getMenuNo();
                String title = modelProductCategoriesContent.getName();
                if (mIsSale) {
                    // 促销商品
                    Bundle bundle = new Bundle();
                    bundle.putString("title", title);
                    bundle.putString("shopNo", mShopNo);
                    bundle.putString("menuNo", menuNo);
                    bundle.putString("productCategoryNo", mProductCategoryNo);
                    ActivityUtils.getInstance().jumpActivity(PlatformProductListActivity.class, bundle);
                } else {
                    //内容点击事件
                    Intent intent = new Intent();
                    intent.putExtra("extra", menuNo);
                    intent.putExtra("title", title);
                    intent.setClass(getActivity(), SortListActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void loadData() {
        HttpParameterUtil.getInstance().requestProductCategories(mHandler);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && smartRefresh != null) {
            smartRefresh.autoRefresh();
        }
    }

    @SuppressWarnings("unchecked")
    private void disposeData(Message msg) {
        switch (msg.what) {
            case ConstantHandler.WHAT_PRODUCT_CATEGORIES_FAIL:
                smartRefresh.finishRefresh();
                ToastUtils.showToast((String) msg.obj);
                break;
            case ConstantHandler.WHAT_PRODUCT_CATEGORIES_SUCCESS:
                //列表网络请求
                smartRefresh.finishRefresh();
                LinkedList<ModelProductCategories> list = (LinkedList<ModelProductCategories>) msg.obj;
                if (null == list || list.size() == 0) {
                    return;
                }
                SharedPreferencesUtils.put(Constants.CLASSIFY_DATA_CACHE, GsonUtils.getGson().toJson(list));
                setData(list);
                break;

        }

    }

    private static class MyHandler extends Handler {

        private WeakReference<TabGoodsClassifyFragment> mImpl;

        public MyHandler(TabGoodsClassifyFragment mImpl) {
            this.mImpl = new WeakReference<>(mImpl);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mImpl.get() != null) {
                mImpl.get().disposeData(msg);
            }
        }
    }

}
