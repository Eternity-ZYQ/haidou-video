package com.yimeng.haidou.mine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yimeng.config.ConstantHandler;
import com.yimeng.haidou.R;
import com.yimeng.haidou.mine.adapter.FavoriteMerchantAdapter;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.ModelShop;
import com.yimeng.entity.ModelShopDetail;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.huige.library.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 商家收藏
 */
@SuppressLint("ValidFragment")
public class MerchantFragment extends Fragment {
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.listview)
    ListView listView;

    private final int WHAT_HANDLER_CLICK = 0x01;
    private FavoriteMerchantAdapter mFavoriteMerchantAdapter;

    private int mPage;

    @SuppressLint("ValidFragment")
    public MerchantFragment() {
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
        LinkedList<ModelShop> mOrderList = new LinkedList<>();
        mFavoriteMerchantAdapter = new FavoriteMerchantAdapter(this.getContext(), mHandler, mOrderList, WHAT_HANDLER_CLICK);
        listView.setAdapter(mFavoriteMerchantAdapter);

        mRefreshLayout.autoRefresh();
        mRefreshLayout.setEnableLoadMore(true);
        mRefreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                mPage = 1;
                HttpParameterUtil.getInstance().requestShopCollect(mPage, mHandler);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                HttpParameterUtil.getInstance().requestShopCollect(mPage, mHandler);
            }
        });

    }

    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private WeakReference<MerchantFragment> mImpl;

        public MyHandler(MerchantFragment mImpl) {
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
            case ConstantHandler.WHAT_SHOP_COLLECT_SUCCESS:
                mRefreshLayout.finishRefresh();
                mPage = 2;
                mFavoriteMerchantAdapter.mList = (LinkedList<ModelShop>) msg.obj;
                mFavoriteMerchantAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_SHOP_COLLECT_FAIL:
                mRefreshLayout.finishRefresh();
                break;
            case ConstantHandler.WHAT_SHOP_COLLECT_MORE_SUCCESS:
                mRefreshLayout.finishLoadMore();
                mPage++;
                mFavoriteMerchantAdapter.mList.addAll((Collection<? extends ModelShop>) msg.obj);
                mFavoriteMerchantAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_SHOP_COLLECT_MORE_FAIL:
                mRefreshLayout.finishLoadMore();
                break;
            case WHAT_HANDLER_CLICK:
                ModelShop modelShop = (ModelShop) msg.obj;
//                SimpleDialog.showLoadingHintDialog(getActivity(), 4);
//                HttpParameterUtil.getInstance().requestShopDetail(modelShop.getId(), mHandler);
                if(modelShop.getStatus().equals("common")) {
                    ActivityUtils.getInstance().jumpShopDetailActivity(modelShop.getShopNo(), false);
                }else{
                    ToastUtils.showToast("商家已被冻结!");
                }

                break;
            case ConstantHandler.WHAT_SHOP_DETAIL_SUCCESS:
                SimpleDialog.cancelLoadingHintDialog();
                ModelShopDetail shopDetail = (ModelShopDetail) msg.obj;
                ActivityUtils.getInstance().jumpShopDetailActivity(shopDetail.getShopNo(), false);
                break;
            case ConstantHandler.WHAT_SHOP_DETAIL_FAIL:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast((String) msg.obj);
                break;
            default:
                break;
        }
    }
}
