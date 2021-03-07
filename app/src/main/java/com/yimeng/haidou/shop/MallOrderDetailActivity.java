package com.yimeng.haidou.shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.ModelMallOrder;
import com.yimeng.entity.ModelMallOrderItem;
import com.yimeng.entity.ReturnOfGoodsStatus;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.haidou.R;
import com.yimeng.haidou.order.ReturnOfGoodsActivity;
import com.yimeng.haidou.shop.adapter.OrderListGoodsAdapter;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.DateUtil;
import com.yimeng.utils.GsonUtils;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.MyToolBar;
import com.google.gson.JsonObject;
import com.huige.library.utils.ToastUtils;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 商城订单详情
 */

public class MallOrderDetailActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.tv_name)
    TextView mName;
    @Bind(R.id.tv_phone)
    TextView mPhone;
    @Bind(R.id.tv_district)
    TextView mDistrict;
    @Bind(R.id.lv_goods_in_the_shop)
    ListView mListView;
    @Bind(R.id.totalMoneyTV)
    TextView totalMoneyTV;
    @Bind(R.id.freightTV)
    TextView freightTV;
    @Bind(R.id.buyerMessageTV)
    TextView buyerMessageTV;
    @Bind(R.id.shopNameTV)
    TextView shopNameTV;
    @Bind(R.id.payTypeTV)
    TextView payTypeTV;
    @Bind(R.id.orderNoTV)
    TextView orderNoTV;
    @Bind(R.id.confirmTV)
    TextView confirmTV;
    @Bind(R.id.returnMoneyTV)
    TextView returnMoneyTV;
    @Bind(R.id.returnTV)
    TextView returnTV;
    @Bind(R.id.cancelOrderTV)
    TextView cancelOrderTV;
    @Bind(R.id.logisticsTV)
    TextView logisticsTV;
    @Bind(R.id.commentTV)
    TextView commentTV;
    @Bind(R.id.payStatusLL)
    LinearLayout payStatusLL;
    @Bind(R.id.orderTime)
    TextView orderTime;
    @Bind(R.id.tv_return_loading)
    TextView tvReturnLoading;
    @Bind(R.id.layout_reject)
    RelativeLayout layoutReject;
    @Bind(R.id.tv_reject_case)
    TextView tvRejectCase;
    @Bind(R.id.tv_coupon_amt)
    TextView tvCouponAmt;
    @Bind(R.id.rl_coupon)
    RelativeLayout rl_coupon;

    /**
     * 商城订单
     */
    private ModelMallOrder mModelMallOrder;
    /**
     * 订单类型
     * nopay(未支付)
     * pay("已付款/未发货")
     * noReceiving("待收货")
     * nocomment("未评价")
     * complete(已完成)
     * refund(退款中)
     */
    private String mOrderStatus;
    /**
     * 支付类型 -1未选择，1余额，2微信支付，
     */
    private int mPayType;
    /**
     * 运费（分）
     */
    private int mFreightMoney;
    /**
     * 总价格（分）
     */
    private int mTotalMoney;
    private MyHandler mHandler = new MyHandler(this);


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_mall_order_detail;
    }

    @Override
    protected void init() {

        mOrderStatus = getIntent().getStringExtra("mOrderStatus");
        mModelMallOrder = (ModelMallOrder) getIntent().getExtras().get("mModelMallOrder");
        if (mModelMallOrder != null) {
            mName.setText("收货人: " + mModelMallOrder.getReceiver());
            mPhone.setText(mModelMallOrder.getReceiverMobile());
            mDistrict.setText("收货地址: " + mModelMallOrder.getAddress());
            shopNameTV.setText(mModelMallOrder.getShopName());
            orderTime.setText(DateUtil.getAssignDate(mModelMallOrder.getPayTime(), 3));

            // 优惠券
            // 代金券
            if (UnitUtil.getInt(mModelMallOrder.getCouponAmt()) > 0) {
                rl_coupon.setVisibility(View.VISIBLE);
                tvCouponAmt.setText("-" + UnitUtil.getMoney(mModelMallOrder.getCouponAmt()));
            }

            OrderListGoodsAdapter adapter = new OrderListGoodsAdapter(this, mHandler,
                    mModelMallOrder, mModelMallOrder.getModelMallOrderItemList(), ConstantHandler.WHAT_HANDLER_CLICK_1);
            mListView.setAdapter(adapter);

            freightTV.setText(UnitUtil.getDouble(mModelMallOrder.getLogisticsFee()) * 0.01 + "元");
            buyerMessageTV.setText(mModelMallOrder.getLeaveMsg());
            totalMoneyTV.setText(mModelMallOrder.getRealMoney());

            payTypeTV.setText(mModelMallOrder.getPayTypeStr());
            orderNoTV.setText(mModelMallOrder.getOrderNo());

            returnMoneyTV.setVisibility(View.GONE);
            cancelOrderTV.setVisibility(View.GONE);
            logisticsTV.setVisibility(View.GONE);
            confirmTV.setVisibility(View.GONE);
            commentTV.setVisibility(View.GONE);
            payStatusLL.setVisibility(View.VISIBLE);
            returnTV.setVisibility(View.GONE);
            switch (mOrderStatus) {
                case "nopay":
//                    mTitle.setText(R.string.order_pay);
//                    totalMoneyTV.setVisibility(View.GONE);
//                    notPayStatusLL.setVisibility(View.VISIBLE);
//                    payStatusLL.setVisibility(View.GONE);
//                    HttpParameterUtil.getInstance().requestMyWallet(mHandler);
                case "pay":
//                    cancelOrderTV.setVisibility(View.VISIBLE);
                    returnMoneyTV.setVisibility(View.VISIBLE);
                    toolBar.setTitle(getString(R.string.order_status_title, "未发货"));
                    break;
                case "noReceiving":
//                    lookConfirmRL.setVisibility(View.VISIBLE);
                    toolBar.setTitle(getString(R.string.order_status_title, "待收货"));
                    logisticsTV.setVisibility(View.VISIBLE);
                    confirmTV.setVisibility(View.VISIBLE);
                    returnTV.setVisibility(View.VISIBLE);
                    break;
                case "nocomment":
                    toolBar.setTitle(getString(R.string.order_status_title, "待评价"));
//                    lookConfirmRL.setVisibility(View.VISIBLE);
                    logisticsTV.setVisibility(View.VISIBLE);
                    commentTV.setVisibility(View.VISIBLE);
                    returnTV.setText("申请售后");
                    returnTV.setVisibility(View.VISIBLE);
                    break;
                case "complete":
                    toolBar.setTitle(getString(R.string.order_status_title, "已完成"));
//                    lookConfirmRL.setVisibility(View.VISIBLE);
                    logisticsTV.setVisibility(View.VISIBLE);
//                    userCommentLL.setVisibility(View.GONE);
                    returnTV.setText("申请售后");
                    returnTV.setVisibility(View.VISIBLE);

                    break;
                case "refundComplete":
                    toolBar.setTitle(getString(R.string.order_status_title, "已退款"));
//                    lookConfirmRL.setVisibility(View.VISIBLE);
//                    logisticsTV.setVisibility(View.VISIBLE);
                    getRejectCase();
                    break;
                case "refund"://退款中
                    toolBar.setTitle(getString(R.string.order_status_title, "退款中"));
                    tvReturnLoading.setVisibility(View.VISIBLE);
                    getRejectCase();
                    break;
                default:
                    break;
            }

            // 支付相关
            mPayType = -1;
            mFreightMoney = UnitUtil.getInt(mModelMallOrder.getLogisticsFee());
            setTotalMoney();
        }
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {

    }

    /**
     * 退款/退货原因
     */
    private void getRejectCase() {
        layoutReject.setVisibility(View.VISIBLE);
        if (mModelMallOrder == null) return;
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("token", CommonUtils.getToken());
        params.put("orderNo", mModelMallOrder.getOrderNo());
        params.put("refundType", mModelMallOrder.getInvoiceEmail().equals("entryRefund") ? "entryRefund" : "entryGoods");
        new OkHttpCommon().postLoadData(this, ConstantsUrl.URL_QUERY_STATE_OF_RETURN, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() == 1) {
                    tvRejectCase.setText(GsonUtils.parseJson(jsonObject.get("data").getAsJsonObject(), "remark", ""));
                }
            }
        });


    }


    @SuppressLint("SetTextI18n")
    private void setTotalMoney() {
        mTotalMoney = 0;
        for (ModelMallOrderItem item : mModelMallOrder.getModelMallOrderItemList()) {
            mTotalMoney += UnitUtil.getInt(item.getOriginalPrice()) * UnitUtil.getInt(item.getProductNum());
        }
        mTotalMoney += mFreightMoney;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            commentTV.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.logisticsTV, R.id.commentTV, R.id.confirmTV, R.id.cancelOrderTV})
    public void myOnClick(View view) {
        switch (view.getId()) {
            case R.id.confirmTV:
                SimpleDialog.showConfirmDialog(this, null, "确认收货?", null, new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        //确认收货
                        SimpleDialog.showLoadingHintDialog(MallOrderDetailActivity.this, 4);
                        HttpParameterUtil.getInstance().requestConfirmMallOrder(mModelMallOrder.getOrderNo(), mHandler);
                    }
                });
                break;
            case R.id.logisticsTV:
                // 查看物流
                if (mModelMallOrder == null) {
                    return;
                }

                String url = ConstantsUrl.LOGISTICS_URL_HEADER + "type=" +
                        mModelMallOrder.getLogisticsType() + "&postid=" + mModelMallOrder.getLogisticsNo();
                ActivityUtils.getInstance().jumpH5Activity("物流详情", url);
                break;
            case R.id.commentTV:
                if (mModelMallOrder == null) {
                    return;
                }
                Intent intent = new Intent(this, OrderCommentActivity2.class);
                intent.putExtra("mModelMallOrder", mModelMallOrder);
                intent.putExtra("mOrderNo", mModelMallOrder.getOrderNo());
                intent.putExtra("mOrderType", "mall");
                startActivityForResult(intent, 0x01);
                break;
            case R.id.cancelOrderTV:
                if (mModelMallOrder != null) {
                    SimpleDialog.showConfirmDialog(this, null, "确定取消?", null, new OnConfirmListener() {
                        @Override
                        public void onConfirm() {
                            SimpleDialog.showLoadingHintDialog(MallOrderDetailActivity.this, 4);
                            HttpParameterUtil.getInstance().requestCancelMallOrder(mModelMallOrder.getOrderNo(), mHandler);
                        }
                    });
                }
                break;
//            case R.id.returnTV:
//                // 退货
//                if (mModelMallOrder == null) return;
//                intent = new Intent(MallOrderDetailActivity.this, ReturnOfGoodsActivity.class);
//                intent.putExtra("type", 1);
//                intent.putExtra("orderNo", mModelMallOrder.getOrderNo());
//                intent.putExtra("goodsName", mModelMallOrder.getOrderName());
//                intent.putExtra("name", mModelMallOrder.getShop().getMobileNo());
//                intent.putExtra("phone", mModelMallOrder.getShop().getTelephone());
//                intent.putExtra("address", mModelMallOrder.getShop().getAddress());
//                startActivity(intent);
//                break;
//            case R.id.returnMoneyTV:
//                // 退款
//                if (mModelMallOrder == null) return;
//                intent = new Intent(MallOrderDetailActivity.this, ReturnOfGoodsActivity.class);
//                intent.putExtra("type", 2);
//                intent.putExtra("orderNo", mModelMallOrder.getOrderNo());
//                intent.putExtra("goodsName", mModelMallOrder.getOrderName());
//                intent.putExtra("name", mModelMallOrder.getShop().getMobileNo());
//                intent.putExtra("phone", mModelMallOrder.getShop().getTelephone());
//                intent.putExtra("address", mModelMallOrder.getShop().getAddress());
//                startActivity(intent);
//                break;
//            case R.id.tv_return_loading://退款中,查看退款进度
//                if(mModelMallOrder.getCouponNo().equals("entryRefund")) {
//                    // 未发货时退款
//                    ToastUtils.showToast("退款中，请留意消息中心！");
//                }else{
//                    // 退货后的退款
//                    if (mModelMallOrder == null) return;
//                    intent = new Intent(MallOrderDetailActivity.this, ReturnOfGoodsActivity.class);
//                    intent.putExtra("type", 1);
//                    intent.putExtra("orderNo", mModelMallOrder.getOrderNo());
//                    intent.putExtra("goodsName", mModelMallOrder.getOrderName());
//                    intent.putExtra("name", mModelMallOrder.getShop().getMobileNo());
//                    intent.putExtra("phone", mModelMallOrder.getShop().getTelephone());
//                    intent.putExtra("address", mModelMallOrder.getShop().getAddress());
//                    startActivity(intent);
//                }
//                break;
            default:
                break;
        }
    }

    @OnClick({R.id.returnTV, R.id.returnMoneyTV, R.id.tv_return_loading})
    public void onRefundClick(View view) {
        if (mModelMallOrder == null) return;
        // 查询状态
        String couponNo = mModelMallOrder.getInvoiceEmail();
        int type;
        if (mModelMallOrder.getOrderStatus().equals("pay")) {
            type = 2;
        } else if (mModelMallOrder.getOrderStatus().equals("refund")) {
            if (!TextUtils.isEmpty(couponNo) && couponNo.equals("entryGoods")) {
                type = 1;
            } else {
                type = 2;
            }
        } else {
            type = 1;
        }
        HttpParameterUtil.getInstance().queryStateOfReturn(type, mModelMallOrder.getOrderNo(), mHandler);
    }


    @SuppressLint("SetTextI18n")
    @SuppressWarnings("unchecked")
    private void disposeData(Message msg) {
        switch (msg.what) {
            case ConstantHandler.WHAT_WECHAT_PAY_PARAMS_FAIL:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast((String) msg.obj);
                break;
            case ConstantHandler.WHAT_CONFIRM_MALL_ORDER_SUCCESS:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast(getString(R.string.tv_confirm_mall_order_success));
//                confirmTV.setVisibility(View.GONE);
                finish();
                break;
            case ConstantHandler.WHAT_CONFIRM_MALL_ORDER_FAIL:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast((String) msg.obj);
                break;
            case ConstantHandler.WHAT_HANDLER_CLICK_1:
                if (msg.arg1 == R.id.click_rl) {
                    ModelMallOrderItem model = (ModelMallOrderItem) msg.obj;
                    ActivityUtils.getInstance().jumpShopProductActivity(1, model.getShopNo(), model.getProductNo(), 0,"");
                }
                break;
            case ConstantHandler.WHAT_CANCEL_MALL_ORDER_SUCCESS:
                //取消订单
                ToastUtils.showToast(getString(R.string.tv_cancel_mall_order_success));
                setResult(RESULT_OK, new Intent());
                finish();
                break;
            case ConstantHandler.WHAT_CANCEL_MALL_ORDER_FAIL:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast((String) msg.obj);
                break;
            case ConstantHandler.WHAT_QUERY_STATE_OF_RETURN_SUCCESS:
                // 查询状态成功
                ReturnOfGoodsStatus mReturnOfGoodsStatus = (ReturnOfGoodsStatus) msg.obj;
                ReturnOfGoodsStatus.DataBean statusData = mReturnOfGoodsStatus.getData();

                if (mModelMallOrder == null) return;

                String refundStatus = statusData.getRefundState();
                if (refundStatus.equals("refunding")) {
                    ToastUtils.showToast("退款中，请留意消息中心！");
                } else if (refundStatus.equals("refund_success")) {
                    ToastUtils.showToast("退货退款完成");
                } else {
                    String invoiceEmail = mModelMallOrder.getInvoiceEmail();
                    int type;
                    if (mModelMallOrder.getOrderStatus().equals("pay")) {
                        type = 2;//退款
                    } else if (mModelMallOrder.getOrderStatus().equals("refund")) {
                        if (!TextUtils.isEmpty(invoiceEmail) && invoiceEmail.equals("entryGoods")) {
                            type = 1;//退货
                        } else {
                            type = 2;
                        }
                    } else {
                        type = 1;
                    }
                    jumpRefundProduct(mReturnOfGoodsStatus, type);
                }

                break;
            case ConstantHandler.WHAT_QUERY_STATE_OF_RETURN_FAIL:
                ToastUtils.showToast((String) msg.obj);
                break;
            default:
                break;
        }
    }

    private void jumpRefundProduct(ReturnOfGoodsStatus mReturnOfGoodsStatus, int type) {
        Intent intent = new Intent(MallOrderDetailActivity.this, ReturnOfGoodsActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("ReturnOfGoodsStatus", mReturnOfGoodsStatus);
        intent.putExtra("orderNo", mModelMallOrder.getOrderNo());
        intent.putExtra("goodsName", mModelMallOrder.getOrderName());
        intent.putExtra("name", mModelMallOrder.getShop().getMobileNo());
        intent.putExtra("phone", mModelMallOrder.getShop().getTelephone());
        intent.putExtra("address", mModelMallOrder.getShop().getAddress());
        startActivity(intent);
    }

    private static class MyHandler extends Handler {

        private WeakReference<MallOrderDetailActivity> mImpl;

        public MyHandler(MallOrderDetailActivity mImpl) {
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
