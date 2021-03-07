package com.yimeng.haidou.mine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yimeng.config.ConstantHandler;
import com.yimeng.config.Constants;
import com.yimeng.haidou.R;
import com.yimeng.haidou.mine.adapter.FavoriteGoodsAdapter;
import com.yimeng.entity.GoodsModel;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.huige.library.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 商品收藏
 */
@SuppressLint("ValidFragment")
public class GoodsFragment extends Fragment {
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.listview)
    ListView listView;

    private final int WHAT_HANDLER_CLICK = 0x01;
    private FavoriteGoodsAdapter mFavoriteGoodsAdapter;

    private int mPage;

    @SuppressLint("ValidFragment")
    public GoodsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fm_favorite, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mRefreshLayout.autoRefresh(500);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        LinkedList<GoodsModel> mOrderList = new LinkedList<>();
        mFavoriteGoodsAdapter = new FavoriteGoodsAdapter(this.getContext(), mHandler, mOrderList, WHAT_HANDLER_CLICK);
        listView.setAdapter(mFavoriteGoodsAdapter);

        mRefreshLayout.autoRefresh();
        mRefreshLayout.setEnableLoadMore(true);
        mRefreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                mPage = 1;
                HttpParameterUtil.getInstance().requestProductCollect(mPage, mHandler);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                HttpParameterUtil.getInstance().requestProductCollect(mPage, mHandler);
            }
        });

    }

    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private WeakReference<GoodsFragment> mImpl;

        public MyHandler(GoodsFragment mImpl) {
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

    @SuppressWarnings("unchecked")
    private void disposeData(Message msg) {

        switch (msg.what) {
            case ConstantHandler.WHAT_PRODUCT_COLLECT_SUCCESS:
                mRefreshLayout.finishRefresh();
                mPage = 2;
                mFavoriteGoodsAdapter.mList = (LinkedList<GoodsModel>) msg.obj;
                mFavoriteGoodsAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_PRODUCT_COLLECT_FAIL:
                mRefreshLayout.finishRefresh();
                break;
            case ConstantHandler.WHAT_PRODUCT_COLLECT_MORE_SUCCESS:
                mRefreshLayout.finishLoadMore();
                mPage++;
                LinkedList<GoodsModel> list = (LinkedList<GoodsModel>) msg.obj;
                if (list.size() < Constants.MAX_LIMIT) {
                    mRefreshLayout.finishLoadMoreWithNoMoreData();
                }
                mFavoriteGoodsAdapter.mList.addAll(list);
                mFavoriteGoodsAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_PRODUCT_COLLECT_MORE_FAIL:
                mRefreshLayout.finishLoadMore();
                break;
            case WHAT_HANDLER_CLICK:
                GoodsModel goodsModel = (GoodsModel) msg.obj;
                if(goodsModel.getIsOnsale().equals("0")) {
                    ToastUtils.showToast(getString(R.string.good_not_on_sale));
                    return;
                }
                String productType = goodsModel.getProductType();
                if (TextUtils.equals(productType, "entity") || TextUtils.equals(productType, "cuxiao")) {
//                    Intent intent = new Intent(getActivity(), NearbyGoodsDetailActivity.class);
//                    intent.putExtra("mProductNo", goodsModel.getProductNo());
//                    intent.putExtra("mCollectStatus", true);
//                    startActivityForResult(intent, 0x01);
                    ActivityUtils.getInstance().jumpShopProductActivity(3, goodsModel.getShopNo(), goodsModel.getProductNo(), 0, "");
                } else{
//                    Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
//                    intent.putExtra("extra", goodsModel.getProductNo());
//                    intent.putExtra("mShopNo", goodsModel.getShopNo());
//                    intent.putExtra("mFromActivity", "GoodsFragment");
//                    startActivity(intent);

                    ActivityUtils.getInstance().jumpShopProductActivity(1, goodsModel.getShopNo(), goodsModel.getProductNo(), 0, "");
                }
                break;
            default:
                break;
        }
    }
}
