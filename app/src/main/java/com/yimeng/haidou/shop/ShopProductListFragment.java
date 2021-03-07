package com.yimeng.haidou.shop;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yimeng.base.BaseFragment;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.Constants;
import com.yimeng.entity.ModelGetHotSalesProduct;
import com.yimeng.haidou.R;
import com.yimeng.haidou.shop.adapter.ShopProductGridAdapter;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/24 0024 下午 06:34.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 产品列表
 * </pre>
 */
public class ShopProductListFragment extends BaseFragment {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private ShopProductGridAdapter mAdapter;
    private String mMenuNo;
    private int mPage = 1;
    private List<ModelGetHotSalesProduct> mList;
    private MyHandler mHandler = new MyHandler(this);
    private SmartRefreshLayout smartRefresh;

    public static ShopProductListFragment getNewInstance(String menuNo) {
        ShopProductListFragment fragment = new ShopProductListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("menuNo", menuNo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int setLayoutResId() {
        return R.layout.fragment_shop_product;
    }

    @Override
    protected void init() {

        mMenuNo = getArguments().getString("menuNo");
        mList = new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mAdapter = new ShopProductGridAdapter(mList);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                // 商城进详情
                ModelGetHotSalesProduct product = mList.get(position);
                if(product != null) {
                    ActivityUtils.getInstance().jumpShopProductActivity(1, product.getShopNo(), product.getProductNo(), 0,"");
                }
            }
        });
    }

    @Override
    protected void loadData() {

    }

    public void refreshData(SmartRefreshLayout smartRefreshLayout) {
        this.smartRefresh = smartRefreshLayout;
        mPage = 1;
        HttpParameterUtil.getInstance().requestGetHotSalesProduct(mMenuNo, mPage, mHandler);
    }

    public void loadmoreData(SmartRefreshLayout smartRefreshLayout) {
        this.smartRefresh = smartRefreshLayout;
        HttpParameterUtil.getInstance().requestGetHotSalesProduct(mMenuNo, mPage, mHandler);
    }

    @SuppressLint("RestrictedApi")
    private void disposeData(Message msg) {
        if (mList == null) {
            return;
        }
        switch (msg.what) {
            case ConstantHandler.WHAT_GET_HOT_SALES_PRODUCT_SUCCESS:
                if (smartRefresh != null) {
                    smartRefresh.finishRefresh();
                }
                //热卖商品
                mPage = 2;
                if (!mList.isEmpty()) {
                    mList.clear();
                }
                mList.addAll((LinkedList<ModelGetHotSalesProduct>) msg.obj);
                mAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_GET_HOT_SALES_PRODUCT_MORE_SUCCESS:
                if (smartRefresh != null) {
                    smartRefresh.finishLoadMore();
                }
                //热卖商品
                mPage++;
                LinkedList<ModelGetHotSalesProduct> list = (LinkedList<ModelGetHotSalesProduct>) msg.obj;
                if(list.size() < Constants.MAX_LIMIT) {
                    smartRefresh.finishLoadMoreWithNoMoreData();
                }
                mList.addAll(list);
                mAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_GET_HOT_SALES_PRODUCT_FAIL:
                smartRefresh.finishRefresh(false);
                break;
            case ConstantHandler.WHAT_GET_HOT_SALES_PRODUCT_MORE_FAIL:
                smartRefresh.finishLoadMore(false);
                break;
            default:

        }
    }

    private class MyHandler extends Handler {
        private WeakReference<ShopProductListFragment> mImpl;

        public MyHandler(ShopProductListFragment impl) {
            mImpl = new WeakReference<>(impl);
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
