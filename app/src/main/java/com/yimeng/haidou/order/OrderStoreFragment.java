package com.yimeng.haidou.order;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.haidou.order.adapter.OrederStoreAdapter;
import com.yimeng.haidou.shop.OrderCommentActivity2;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.ModelShopOrderList;
import com.yimeng.entity.ModelWXPay;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.google.gson.JsonObject;
import com.huige.library.utils.ToastUtils;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 我的订单-店铺订单
 */
@SuppressLint("ValidFragment")
public class OrderStoreFragment extends Fragment {
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.listview)
    ListView listView;
    @Bind(R.id.tv_no_data)
    TextView noDataTV;

    LinkedList<ModelShopOrderList> mList;
    private OrederStoreAdapter mOrederStoreAdapter;

    /**
     * 类型 :0待发货, 1待收货, 2待评价, 3已完成
     */
    private int mType;
    private int mPage;

    /**
     * 微信支付
     */
    private IWXAPI msgApi;
    private MyHandler mHandler = new MyHandler(this);
    private BroadcastReceiver mPaymentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.ACTION_PAYMENT_SUCCESS)) {
                ToastUtils.showToast("支付成功");
                ActivityUtils.getInstance().jumpSubmitResult(4);
            } else if (intent.getAction().equals(Constants.ACTION_PAYMENT_FAIL)) {
                ToastUtils.showToast("支付失败");
            }
        }
    };

    public static OrderStoreFragment getInstance(int mType) {
        OrderStoreFragment fragment = new OrderStoreFragment();
        Bundle  bundle = new Bundle();
        bundle.putInt("type", mType);
        fragment.setArguments(bundle);
        return fragment;
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

        mType = getArguments().getInt("type");

        initData();

        // 将该app注册到微信
        msgApi = WXAPIFactory.createWXAPI(getActivity(), null);
        msgApi.registerApp(Constants.WX_APPID);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_PAYMENT_SUCCESS);
        filter.addAction(Constants.ACTION_PAYMENT_FAIL);
        getActivity().registerReceiver(mPaymentReceiver, filter);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mRefreshLayout != null) {
            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mPaymentReceiver);

    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        mPage = 1;
        HttpParameterUtil.getInstance().requestQueryOrderList(whatType(mType), mPage, mHandler);

        mList = new LinkedList<>();
        mOrederStoreAdapter = new OrederStoreAdapter(this.getContext(), mHandler, mList, ConstantHandler.WHAT_HANDLER_CLICK, whatType(mType));
        listView.setAdapter(mOrederStoreAdapter);

        mRefreshLayout.setEnableLoadMore(true);
        mRefreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                mPage = 1;
                HttpParameterUtil.getInstance().requestQueryOrderList(whatType(mType), mPage, mHandler);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.finishRefresh();
                    }
                }, 2000);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                HttpParameterUtil.getInstance().requestQueryOrderList(whatType(mType), mPage, mHandler);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.finishLoadMore();
                    }
                }, 2000);
            }
        });

    }

    /**
     * @param mType 0. 待发货
     *              1. 待收货
     *              2. 待评价
     *              3. 已完成
     * @return
     */
    private String whatType(int mType) {
        String type = "";
        switch (mType) {
            case 0:
                type = "pay";
                break;
            case 1:
                type = "noReceiving";
                break;
            case 2:
                type = "nocomment";
                break;
            case 3:
                type = "complete";
                break;
            default:
                break;
        }
        return type;
    }

    private void disposeData(Message msg) {
        SimpleDialog.cancelLoadingHintDialog();
        switch (msg.what) {
            case ConstantHandler.WHAT_QUERY_ORDER_LIST_SUCCESS:
                mRefreshLayout.finishRefresh();
                mPage = 2;
                mList = (LinkedList<ModelShopOrderList>) msg.obj;
                if (mList.size() == 0) {
                    noDataTV.setVisibility(View.VISIBLE);
                } else {
                    noDataTV.setVisibility(View.GONE);
                }
                mOrederStoreAdapter.mList = mList;
                mOrederStoreAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_QUERY_ORDER_LIST_FAIL:
                mRefreshLayout.finishRefresh();
                break;
            case ConstantHandler.WHAT_QUERY_ORDER_LIST_MORE_FAIL:
                mRefreshLayout.finishLoadMore();
                break;
            case ConstantHandler.WHAT_QUERY_ORDER_LIST_MORE_SUCCESS:
                mPage++;
                List<ModelShopOrderList> list = (LinkedList<ModelShopOrderList>) msg.obj;
                if (list.size() < Constants.MAX_LIMIT)
                    mRefreshLayout.finishLoadMoreWithNoMoreData();
                else mRefreshLayout.finishLoadMore();
                mList.addAll(list);
                mOrederStoreAdapter.mList = mList;
                mOrederStoreAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_HANDLER_CLICK:
                final ModelShopOrderList item = (ModelShopOrderList) msg.obj;
                Intent intent;
                String orderStatus = item.getOrderStatus();
                switch (msg.arg1) {
                    case R.id.refundBTN:    // 退款
                        if(orderStatus.equals("pay")) {
                            refundApplyTip(item.getOrderNo());
                        }
                        break;
                    case R.id.commentBTN:
                        if (orderStatus.equals("pay")) {
                            // 待发货-催单
                            SimpleDialog.showConfirmDialog(getActivity(), null, "确定催单吗?", null, new OnConfirmListener() {
                                        @Override
                                        public void onConfirm() {
                                            reminderOrder(item.getOrderNo());
                                        }
                                    });
                            return;
                        }
                        if (orderStatus.equals("noReceiving")) {
                            // 确认收货
                            SimpleDialog.showConfirmDialog(getActivity(), null, "确认收货吗?", null, new OnConfirmListener() {
                                        @Override
                                        public void onConfirm() {
                                            confirmReceipt(item.getOrderNo());
                                        }
                                    });
                        } else {
                            // 立即评价
                            intent = new Intent();
                            intent.putExtra("mModelMallOrder", item);
                            intent.putExtra("mOrderNo", item.getOrderNo());
                            intent.putExtra("mOrderType", "shop");
//                        intent.setClass(getActivity(), OrderCommentActivity.class);
                            intent.setClass(getActivity(), OrderCommentActivity2.class);
                            startActivity(intent);
                        }
                        break;
                    case R.id.clickLL:
                    case R.id.clickRL:
                        intent = new Intent();
//                        if(TextUtils.isEmpty(item.getSendInfo())) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("modelShopOrderList", item);
                        intent.putExtras(bundle);
                        intent.setClass(getActivity(), OrderStoreDetailActivity.class);
                        startActivity(intent);
//                        }else{
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable("modelShopOrderList", item);
//                            intent.putExtras(bundle);
//                            intent.setClass(getActivity(), ExpressOrderDetailActivity.class);
//                            startActivity(intent);
//                        }

                        break;
                    case R.id.payBTN:
                        if (orderStatus.equals("noReceiving")) {
                            // 查看物流
                            String url = ConstantsUrl.LOGISTICS_URL_HEADER + "type=" +
                                    item.getLogisticsType() + "&postid=" + item.getLogisticsNo();
                            ActivityUtils.getInstance().jumpH5Activity("物流详情", url);
                        } else {
                            //微信支付
                            SimpleDialog.showLoadingHintDialog(getActivity(), 4);
                            HttpParameterUtil.getInstance().requestWechatPayParams(item.getOrderNo(), "0", mHandler);
                        }
                        break;
                    case R.id.btn_cancel:   // 取消订单
                        //  我的订单 - 店铺订单 - 取消订单
                        SimpleDialog.showConfirmDialog(getActivity(), null, "确定要取消订单吗?", null, new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        SimpleDialog.showLoadingHintDialog(getActivity(), 4);
                                        HttpParameterUtil.getInstance().requestCancelShopOrder(item.getOrderNo(), mHandler);
                                    }
                                });
                        break;
                    default:
                        break;
                }
                break;
            case ConstantHandler.WHAT_WECHAT_PAY_PARAMS_SUCCESS:
                // 获取充值信息成功
                SimpleDialog.cancelLoadingHintDialog();
                ModelWXPay modelWXPay = (ModelWXPay) msg.obj;
                PayReq request = new PayReq();
                request.appId = modelWXPay.getAppid();
                request.partnerId = modelWXPay.getPartnerid();
                request.prepayId = modelWXPay.getPrepayid();
                request.packageValue = modelWXPay.getPackage1();
                request.nonceStr = modelWXPay.getNoncestr();
                request.timeStamp = modelWXPay.getTimestamp();
                request.sign = modelWXPay.getSign();
                msgApi.sendReq(request);
                break;
            case ConstantHandler.WHAT_WECHAT_PAY_PARAMS_FAIL:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast((String) msg.obj);
                break;
            case ConstantHandler.WHAT_CANCEL_MALL_ORDER_SUCCESS:
                //取消订单
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast(getString(R.string.tv_cancel_mall_order_success));
                onResume();
                break;
            case ConstantHandler.WHAT_CANCEL_MALL_ORDER_FAIL:
                // 取消订单失败
                ToastUtils.showToast("取消订单失败");
                break;
            default:
                break;
        }
    }

    /**
     * 促销商品申请退款前弹提示窗
     * @param orderNo
     */
    private void refundApplyTip(final String orderNo) {
        new OkHttpCommon().postLoadData(getActivity(), ConstantsUrl.URL_SALE_REFUND_GOODS_TIP, CommonUtils.createParams(), new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast(R.string.net_error);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if(jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "退款失败，请稍后重试！"));
                    return;
                }

                JsonObject dataJson = jsonObject.get("data").getAsJsonObject();
                if(!dataJson.isJsonNull()) {
                    String introduction = dataJson.get("introduction").getAsString();
                    SimpleDialog.showConfirmDialog(getActivity(), "提示", introduction, null, new OnConfirmListener() {
                        @Override
                        public void onConfirm() {
                            refundApply(orderNo);
                        }
                    });
                }

            }
        });

    }

    /**
     * 促销商品申请退款
     * @param orderNo
     */
    private void refundApply(String orderNo) {

        HashMap<String, String> params = CommonUtils.createParams();
        params.put("orderNo", orderNo);
        new OkHttpCommon().postLoadData(getActivity(), ConstantsUrl.URL_SALE_REFUND_GOODS, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast(R.string.net_error);
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if(jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "退款申请失败，请稍后重试！"));
                    return;
                }
                ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "退款申请成功！"));
                mRefreshLayout.autoRefresh(500);
            }
        });

    }

    /**
     * 催单
     */
    private void reminderOrder(String orderNo) {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("orderNo", orderNo);
        new OkHttpCommon().postLoadData(getActivity(), ConstantsUrl.URL_REMINDER_ORDER, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("催单失败!");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "催单失败!"));
                    return;
                }
                ToastUtils.showToast("已通知商家尽快发货!");
                mRefreshLayout.autoRefresh();
            }
        });
    }

    /**
     * 确认收货
     */
    private void confirmReceipt(String orderNo) {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("orderNo", orderNo);
        new OkHttpCommon().postLoadData(getActivity(), ConstantsUrl.URL_SHOP_ORDER_RECEIPE, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("确认收货失败!");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "确认收货失败!"));
                    return;
                }

                ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "确认收货成功!"));
                mRefreshLayout.autoRefresh();
            }
        });
    }

    private static class MyHandler extends Handler {

        private WeakReference<OrderStoreFragment> mImpl;

        public MyHandler(OrderStoreFragment mImpl) {
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
