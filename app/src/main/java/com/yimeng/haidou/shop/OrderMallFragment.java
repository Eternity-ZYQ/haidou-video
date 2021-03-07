package com.yimeng.haidou.shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.yimeng.config.ConstantHandler;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.ModelMallOrder;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.haidou.R;
import com.yimeng.haidou.order.ReturnOfGoodsActivity;
import com.yimeng.haidou.shop.adapter.OrderListAdapter;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.huige.library.utils.ToastUtils;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * 商城订单
 */

public class OrderMallFragment extends Fragment {

    private final int WHAT_HANDLER_ORDER_CONFIRM = 0x03;
    @Bind(R.id.listview)
    ListView listView;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.tv_no_data)
    TextView noDataTV;
    LinkedList<ModelMallOrder> mList = new LinkedList<>();
    OrderListAdapter madapter;
    /**
     * 订单类型
     * nopay(未支付)
     * pay("已付款/未发货")
     * noReceiving("待收货")
     * nocomment("未评价")
     * complete(已完成)
     * refundComplete(退货完成)
     * refund(退货/退款申请)
     */
    private String mOrderStatus;
    /**
     * 页码
     */
    private int mPage;
    /**
     * 商城订单
     */
    private ModelMallOrder mModelMallOrder;
    private MyHandler mHandler = new MyHandler(this);

    public OrderMallFragment() {
    }

    @SuppressLint("ValidFragment")
    public OrderMallFragment(String mOrderStatus) {
        this.mOrderStatus = mOrderStatus;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            onAutoRefresh();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fm_mall_order, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
    }

    @Override
    public void onResume() {
        super.onResume();

        mPage = 1;
        getOrderData();
    }

    private void getOrderData() {
        if(CommonUtils.checkLogin()) {
            HttpParameterUtil.getInstance().requestMallOrder(mPage, mOrderStatus, mHandler);
        }else{
            mRefreshLayout.finishRefresh();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        mPage = 0;
        getOrderData();
        madapter = new OrderListAdapter(this.getContext(), mHandler, mList, ConstantHandler.WHAT_HANDLER_CLICK, checkType(mOrderStatus));
        listView.setAdapter(madapter);

        mRefreshLayout.setEnableLoadMore(true);
        mRefreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                mPage = 1;
                getOrderData();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                getOrderData();
            }
        });

    }

    private int checkType(String type) {
        if (null == type) {
            return 1;
        }

        int a = 1;
        switch (type) {
//            case "nopay":
//                a = 0;
//                break;
            case "pay":
                a = 1;
                break;
            case "noReceiving":
                a = 2;
                break;
            case "nocomment":
                a = 3;
                break;
            case "complete":
                a = 4;
                break;
            case "refundComplete":
                a = 5;
                break;
            case "refund":
                a = 6;
                break;
            default:
                break;
        }
        return a;
    }

    public void onAutoRefresh() {
        if (mRefreshLayout != null) {
            mRefreshLayout.autoRefresh();
        }
    }

    @SuppressWarnings("unchecked")
    private void disposeData(Message msg) {
        switch (msg.what) {
            case ConstantHandler.WHAT_CANCEL_MALL_ORDER_SUCCESS:
                //取消订单
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast(getString(R.string.tv_cancel_mall_order_success));
                onAutoRefresh();
                break;
            case ConstantHandler.WHAT_CANCEL_MALL_ORDER_FAIL:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast((String) msg.obj);
                break;
            case ConstantHandler.WHAT_CONFIRM_MALL_ORDER_SUCCESS:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast(getString(R.string.tv_confirm_mall_order_success));
                onAutoRefresh();
                break;
            case ConstantHandler.WHAT_CONFIRM_MALL_ORDER_FAIL:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast((String) msg.obj);
                break;
            case ConstantHandler.WHAT_MALL_ORDER_SUCCESS:
                mRefreshLayout.finishRefresh();
                mPage = 2;
                mList = (LinkedList<ModelMallOrder>) msg.obj;
                break;
            case ConstantHandler.WHAT_MALL_ORDER_FAIL:
                mRefreshLayout.finishRefresh();
                ToastUtils.showToast((String) msg.obj);
                break;
            case ConstantHandler.WHAT_MALL_ORDER_MORE_SUCCESS:
                mRefreshLayout.finishLoadMore();
                mPage++;
                LinkedList<ModelMallOrder> list = (LinkedList<ModelMallOrder>) msg.obj;
                madapter.mList.addAll(list);
                madapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_MALL_ORDER_MORE_FAIL:
                mRefreshLayout.finishLoadMore();
                break;
            case ConstantHandler.WHAT_HANDLER_CLICK:
                mModelMallOrder = (ModelMallOrder) msg.obj;
                switch (msg.arg1) {
                    case R.id.tv_cancel_order:
                        //取消订单
                        SimpleDialog.showConfirmDialog(getActivity(), null, "确定取消?", null, new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                SimpleDialog.showLoadingHintDialog(getActivity(), 4);
                                HttpParameterUtil.getInstance().requestCancelMallOrder(mModelMallOrder.getOrderNo(), mHandler);
                            }
                        });
                        break;
                    case R.id.tv_confirm_goods:
                        //确认收货
                        SimpleDialog.showConfirmDialog(getActivity(), null, "确认收货?", null, new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                SimpleDialog.showLoadingHintDialog(getActivity(), 4);
                                HttpParameterUtil.getInstance().requestConfirmMallOrder(mModelMallOrder.getOrderNo(), mHandler);
                            }
                        });
                        break;
                    case R.id.click_rl:
                    case R.id.tv_check_detail:
                        // 查看详情
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("mModelMallOrder", mModelMallOrder);
                        Intent intent = new Intent(getActivity(), MallOrderDetailActivity.class);
                        intent.putExtras(bundle);
                        intent.putExtra("mOrderStatus", mModelMallOrder.getOrderStatus());
                        startActivityForResult(intent, 0x01);
                        break;
                    case R.id.tv_evaluation:
                        // 评价
                        intent = new Intent(getActivity(), OrderCommentActivity2.class);
                        intent.putExtra("mModelMallOrder", mModelMallOrder);
                        intent.putExtra("mOrderNo", mModelMallOrder.getOrderNo());
                        intent.putExtra("mOrderType", "mall");
                        startActivityForResult(intent, 0x01);
                        break;
                    case R.id.tv_return_loading:
                        // 退款进度
                    case R.id.tv_return_of_goods:
                        // 退货
                        intent = new Intent(getActivity(), ReturnOfGoodsActivity.class);
                        intent.putExtra("orderNo", mModelMallOrder.getOrderNo());
                        intent.putExtra("goodsName", mModelMallOrder.getOrderName());
                        intent.putExtra("name", mModelMallOrder.getShop().getMobileNo());
                        intent.putExtra("phone", mModelMallOrder.getShop().getTelephone());
                        intent.putExtra("address", mModelMallOrder.getShop().getAddress());
                        startActivity(intent);
                        break;
                    case R.id.tv_look_logistics:
                        // 查看物流
                        String url = ConstantsUrl.LOGISTICS_URL_HEADER + "type=" +
                                mModelMallOrder.getLogisticsType() + "&postid=" + mModelMallOrder.getLogisticsNo();
                        ActivityUtils.getInstance().jumpH5Activity("物流详情", url);
                        break;

                    default:
                        break;
                }
            default:
                break;
        }

        if (mList.size() == 0) {
            listView.setVisibility(View.GONE);
            noDataTV.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            noDataTV.setVisibility(View.GONE);
            madapter.mList = mList;
            madapter.notifyDataSetChanged();
        }
    }

    private static class MyHandler extends Handler {

        private WeakReference<OrderMallFragment> mImpl;

        public MyHandler(OrderMallFragment mImpl) {
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
