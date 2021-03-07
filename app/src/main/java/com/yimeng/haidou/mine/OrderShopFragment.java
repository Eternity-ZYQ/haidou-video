package com.yimeng.haidou.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.haidou.mine.adapter.OrederShopListAdapter;
import com.yimeng.entity.ShopOrderModel;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
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
 * 店铺订单
 *
 * @author lijb
 */

@SuppressLint("ValidFragment")
public class OrderShopFragment extends Fragment {
    private final int WHAT_HANDLER_CLICK = 0x01;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.listview)
    ListView listView;
    private LinkedList<ShopOrderModel> shopOrderModels;
    private OrederShopListAdapter orderAdapter;

    /**
     * new(新订单), noReceiving(待评价)，complete（已完成）
     */
    private String mType;
    /**
     * 页码
     */
    private int mPage;
    private MyHandler mHandler = new MyHandler(this);

    public OrderShopFragment(String mType) {
        this.mType = mType;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ConstantHandler.RESULT_CODE_COMMON_DATA) {
            mRefreshLayout.autoRefresh();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fm_order_shop, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
    }

    @SuppressLint("SetTextI18n")
    private void initData() {

        shopOrderModels = new LinkedList<>();
        orderAdapter = new OrederShopListAdapter(this.getContext(), mHandler, shopOrderModels, mType, WHAT_HANDLER_CLICK);
        listView.setAdapter(orderAdapter);

        mRefreshLayout.autoRefresh();
        mRefreshLayout.setEnableLoadMore(true);
        mRefreshLayout.setEnableAutoLoadMore(true);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                mPage = 1;
                HttpParameterUtil.getInstance().requestMyShopOrderInfo(mPage, mType, mHandler);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                HttpParameterUtil.getInstance().requestMyShopOrderInfo(mPage, mType, mHandler);
            }
        });
    }

    public void onAutoRefresh() {
        mRefreshLayout.autoRefresh();
    }

    @SuppressWarnings("unchecked")
    private void disposeData(Message msg) {
        if (mRefreshLayout == null) return;
        switch (msg.what) {
            case ConstantHandler.WHAT_MY_SHOP_ORDER_SUCCESS:
                // 首页数据
                mRefreshLayout.finishRefresh();
                mPage = 2;
                orderAdapter.mList = (LinkedList<ShopOrderModel>) msg.obj;
                orderAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_MY_SHOP_ORDER_FAIL:
                mRefreshLayout.finishRefresh();
                break;
            case ConstantHandler.WHAT_MY_SHOP_ORDER_MORE_SUCCESS:
                // 首页数据
                mRefreshLayout.finishLoadMore();
                mPage++;
                orderAdapter.mList.addAll((Collection<? extends ShopOrderModel>) msg.obj);
                orderAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_MY_SHOP_ORDER_MORE_FAIL:
                mRefreshLayout.finishLoadMore();
                break;
            case WHAT_HANDLER_CLICK:
                ShopOrderModel shopOrderModel = (ShopOrderModel) msg.obj;
                Bundle bundle = new Bundle();
                bundle.putSerializable("mShopOrderModel", shopOrderModel);
                bundle.putString("type", mType);
                if (msg.arg1 == R.id.sureOrderTV) {
                    if (TextUtils.equals(mType, "new")) {
                        if (!shopOrderModel.getIsPlatno().equals("1")) {
                            // 发货
                            Intent intent = new Intent(this.getContext(), DeliverGoodsActivity.class);
                            intent.putExtras(bundle);
                            startActivityForResult(intent, 0x01);
                        }
                    } else if (TextUtils.equals(mType, "noReceiving")) {
                        // 查看物流
                        if (shopOrderModel == null) {
                            return;
                        }

                        String url = ConstantsUrl.LOGISTICS_URL_HEADER + "type=" +
                                shopOrderModel.getLogisticsType() + "&postid=" + shopOrderModel.getLogisticsNo();
                        ActivityUtils.getInstance().jumpH5Activity("物流详情", url);
                    }
                } else {
                    Class toClass;

                    if (TextUtils.equals(mType, "new")) {
//                    toClass = ShopOrderExtendWarrantyDetailActivity.class;
                        toClass = DeliverGoodsActivity.class;
                    } else if (TextUtils.equals(mType, "noReceiving")) {
                        toClass = DeliverGoodsActivity.class;
                    } else {
                        toClass = ShopOrderDetailActivity.class;
                    }
                    Intent intent = new Intent(this.getContext(), toClass);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 0x01);
                }


                break;
            default:
                break;
        }
    }

    private static class MyHandler extends Handler {

        private WeakReference<OrderShopFragment> mImpl;

        public MyHandler(OrderShopFragment mImpl) {
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
