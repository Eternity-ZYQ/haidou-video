package com.yimeng.haidou.order;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.ModelShopOrderList;
import com.yimeng.entity.ModelShopOrderListItem;
import com.yimeng.entity.ModelWXPay;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.DateUtil;
import com.yimeng.utils.GsonUtils;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.MyToolBar;
import com.yimeng.widget.RatingStat.RatingStarView;
import com.yimeng.haidou.R;
import com.yimeng.haidou.order.adapter.OrederGoodsDetailAdapter;
import com.yimeng.haidou.shop.OrderCommentActivity2;
import com.google.gson.JsonObject;
import com.huige.library.utils.ToastUtils;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 我的订单-店铺订单-订单详情
 */

public class OrderStoreDetailActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.shopSDV)
    ImageView mSDV;
    @Bind(R.id.shopNameTV)
    TextView shopNameTV;
    @Bind(R.id.totalPriceTV)
    TextView totalPriceTV;
    @Bind(R.id.tv_order_no)
    TextView tvOrderNo;
    @Bind(R.id.tv_order_time)
    TextView tvOrderTime;
    @Bind(R.id.backMoneyTV)
    TextView backMoneyTV;
    @Bind(R.id.commentBTN)
    Button commentBTN;
    @Bind(R.id.payBTN)
    Button payBTN;

    @Bind(R.id.order_goods_list)
    ListView mlistView;

    OrederGoodsDetailAdapter mAdapter;
    @Bind(R.id.gradeRSV)
    RatingStarView gradeRSV;
    @Bind(R.id.gradeLL)
    LinearLayout gradeLL;
    @Bind(R.id.gradeTV)
    TextView gradeTV;
    @Bind(R.id.grade1SDV)
    ImageView grade1SDV;
    @Bind(R.id.grade2SDV)
    ImageView grade2SDV;
    @Bind(R.id.grade3SDV)
    ImageView grade3SDV;
    @Bind(R.id.userCommentLL)
    LinearLayout userCommentLL;
    float totalPrice;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.tv_district)
    TextView tvDistrict;
    @Bind(R.id.rl_logistics_name)
    RelativeLayout rl_logistics_name;
    @Bind(R.id.rl_logistics_no)
    RelativeLayout rl_logistics_no;
    @Bind(R.id.tv_logistics_name)
    TextView tvLogisticsName;
    @Bind(R.id.tv_logistics_no)
    TextView tvLogisticsNo;
    @Bind(R.id.tv_coupon_amt)
    TextView tvCouponAmt;
    @Bind(R.id.rl_coupon)
    RelativeLayout rl_coupon;
    @Bind(R.id.layout_address)
    ConstraintLayout layout_address;
    @Bind(R.id.tv_pay_type)
    TextView tv_pay_type;
    @Bind(R.id.rl_leave_msg)
    RelativeLayout rl_leave_msg;
    @Bind(R.id.tv_leave_msg)
    TextView tv_leave_msg;
    @Bind(R.id.tv_logistics_fee)
    TextView tv_logistics_fee;
    @Bind(R.id.btn_look_logistics)
    Button btn_look_logistics;
    @Bind(R.id.btn_affirm)
    Button btn_affirm;


    private LinkedList<ModelShopOrderListItem> mList;
    /**
     * 店铺订单
     */
    private ModelShopOrderList mModelShopOrder;
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

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_order_store_detail;
    }

    @Override
    protected void init() {
        // 将该app注册到微信
        msgApi = WXAPIFactory.createWXAPI(this, null);
        msgApi.registerApp(Constants.WX_APPID);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_PAYMENT_SUCCESS);
        filter.addAction(Constants.ACTION_PAYMENT_FAIL);
        registerReceiver(mPaymentReceiver, filter);

        mList = new LinkedList<>();
        Bundle bundle = this.getIntent().getExtras();

        mModelShopOrder = (ModelShopOrderList) bundle.get("modelShopOrderList");
        if (mModelShopOrder != null) {
            String orderStatus = mModelShopOrder.getOrderStatus();
            int size = mModelShopOrder.getModelShopOrderListItemsList().size();
            for (int i = 0; i < size; i++) {
                String menuNo = mModelShopOrder.getModelShopOrderListItemsList().get(i).getMenuNo();
                String orderNo = mModelShopOrder.getModelShopOrderListItemsList().get(i).getOrderNo();
                String orderProductNo = mModelShopOrder.getModelShopOrderListItemsList().get(i).getOrderProductNo();
                String originalPrice = mModelShopOrder.getModelShopOrderListItemsList().get(i).getOriginalPrice();
                String productName = mModelShopOrder.getModelShopOrderListItemsList().get(i).getProductName();
                String productNo = mModelShopOrder.getModelShopOrderListItemsList().get(i).getProductNo();
                String productNum = mModelShopOrder.getModelShopOrderListItemsList().get(i).getProductNum();
                String productImg = mModelShopOrder.getModelShopOrderListItemsList().get(i).getProductImg();
                String productColorSize = mModelShopOrder.getModelShopOrderListItemsList().get(i).getProductColorSize();
                String realAmt = mModelShopOrder.getModelShopOrderListItemsList().get(i).getRealAmt();
                String remindAmt = mModelShopOrder.getModelShopOrderListItemsList().get(i).getRemindAmt();
                ModelShopOrderListItem modelShopOrderListItem = new ModelShopOrderListItem(menuNo, orderNo, orderProductNo, originalPrice, productColorSize, productImg, productName,
                        productNo, productNum, realAmt, remindAmt);
                mList.add(modelShopOrderListItem);
//                totalPrice += UnitUtil.getFloat(originalPrice) * Integer.parseInt(productNum);
            }
            String shopName = mModelShopOrder.getShopName();
            String shopLogo = mModelShopOrder.getShopLogoPath();
            String refund = mModelShopOrder.getScore();
            CommonUtils.showImage(mSDV, shopLogo);
            shopNameTV.setText(shopName);
//            backMoneyTV.setText("(返" + UnitUtil.getMoney(refund, false) + "元)");
            totalPriceTV.setText(UnitUtil.getMoney(mModelShopOrder.getRealMoney()));

            if (mModelShopOrder.getOrderStatus().equals("ensure")) {
                commentBTN.setVisibility(View.VISIBLE);
            }

            if (mModelShopOrder.getOrderStatus().equals("nopay")) {
                payBTN.setVisibility(View.VISIBLE);
            }
            // 地址信息
            tvName.setText("收货人: " + mModelShopOrder.getReceiver());
            tvPhone.setText(mModelShopOrder.getReceiverMobile());
            tvDistrict.setText("收货地址: " + mModelShopOrder.getAddress());

            tvOrderNo.setText(mModelShopOrder.getOrderNo());
            tvOrderTime.setText(DateUtil.getAssignDate(mModelShopOrder.getCreateTime(), 3));
            // 支付方式
            tv_pay_type.setText(mModelShopOrder.getPayTypeStr());
            // 物流
            tvLogisticsName.setText(mModelShopOrder.getLogisticsName());
            tvLogisticsNo.setText(mModelShopOrder.getLogisticsNo());
            // 运费
            tv_logistics_fee.setText(UnitUtil.getMoney(mModelShopOrder.getLogisticsFee()));
            // 买家留言
            if (!TextUtils.isEmpty(mModelShopOrder.getLeaveMsg())) {
                rl_leave_msg.setVisibility(View.VISIBLE);
                tv_leave_msg.setText(mModelShopOrder.getLeaveMsg());
            }
            // 代金券
            if (UnitUtil.getInt(mModelShopOrder.getCouponAmt()) > 0) {
                rl_coupon.setVisibility(View.VISIBLE);
                tvCouponAmt.setText("-" + UnitUtil.getMoney(mModelShopOrder.getCouponAmt()));
            }

            if (orderStatus.equals("pay")) {
                // 待发货
                rl_logistics_name.setVisibility(View.GONE);
                rl_logistics_no.setVisibility(View.GONE);

            } else if (orderStatus.equals("complete")) {
                // 已完成, 显示评价
                userCommentLL.setVisibility(View.VISIBLE);
                gradeRSV.setRating(UnitUtil.getFloat(mModelShopOrder.getModelGrade().getGrade()));
                gradeTV.setText(mModelShopOrder.getModelGrade().getDescribe());

                if (TextUtils.isEmpty(mModelShopOrder.getModelGrade().getDescribe()) &&
                        TextUtils.isEmpty(mModelShopOrder.getModelGrade().getImage1()) &&
                        TextUtils.isEmpty(mModelShopOrder.getModelGrade().getId())) {
                    gradeLL.setVisibility(View.GONE);
                }

//                int width = (CommonUtils.getScreenWidth(this) - CommonUtils.dp2px(64, this)) / 3;
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, width);
//                grade1SDV.setLayoutParams(lp);
//                grade2SDV.setLayoutParams(lp);
//                grade3SDV.setLayoutParams(lp);
                grade1SDV.setVisibility(View.INVISIBLE);
                grade2SDV.setVisibility(View.INVISIBLE);
                grade3SDV.setVisibility(View.INVISIBLE);
                if (!TextUtils.isEmpty(mModelShopOrder.getModelGrade().getImage1())) {
                    grade1SDV.setVisibility(View.VISIBLE);
                    CommonUtils.showImage(grade1SDV, mModelShopOrder.getModelGrade().getImage1());
                }
                if (!TextUtils.isEmpty(mModelShopOrder.getModelGrade().getImage2())) {
                    grade2SDV.setVisibility(View.VISIBLE);
                    CommonUtils.showImage(grade2SDV, mModelShopOrder.getModelGrade().getImage2());
                }
                if (!TextUtils.isEmpty(mModelShopOrder.getModelGrade().getImage3())) {
                    grade3SDV.setVisibility(View.VISIBLE);
                    CommonUtils.showImage(grade3SDV, mModelShopOrder.getModelGrade().getImage3());
                }

                btn_look_logistics.setVisibility(View.VISIBLE);//查看物流
            } else if (orderStatus.equals("noReceiving")) {
                // 待收货
                btn_look_logistics.setVisibility(View.VISIBLE);//查看物流
                btn_affirm.setVisibility(View.VISIBLE);//确认收货

            } else if (orderStatus.equals("nocomment")) {
                // 待评价
                btn_look_logistics.setVisibility(View.VISIBLE);//查看物流
            }

            // 上门取件商品
            if (TextUtils.isEmpty(mModelShopOrder.getReceiver())) {
                layout_address.setVisibility(View.GONE);
            }

        }
        mAdapter = new OrederGoodsDetailAdapter(mList, this, mHandler, ConstantHandler.WHAT_HANDLER_CLICK);
        mlistView.setAdapter(mAdapter);

    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mPaymentReceiver);
    }

    @OnClick({R.id.btn_look_logistics, R.id.btn_affirm})
    public void onReceivingClick(View view) {

        if (view.getId() == R.id.btn_look_logistics) {
            // 查看物流
            String url = ConstantsUrl.LOGISTICS_URL_HEADER + "type=" +
                    mModelShopOrder.getLogisticsType() + "&postid=" + mModelShopOrder.getLogisticsNo();
            ActivityUtils.getInstance().jumpH5Activity("物流详情", url);
        } else {
            // 确认收货
            SimpleDialog.showConfirmDialog(this, null, "确认收货吗?", null, new OnConfirmListener() {
                @Override
                public void onConfirm() {
                    confirmReceipt(mModelShopOrder.getOrderNo());
                }
            });
        }
    }


    /**
     * 确认收货
     */
    private void confirmReceipt(String orderNo) {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("orderNo", orderNo);
        new OkHttpCommon().postLoadData(this, ConstantsUrl.URL_SHOP_ORDER_RECEIPE, params, new CallbackCommon() {
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
                finish();
            }
        });
    }

    @OnClick({R.id.commentBTN, R.id.payBTN, R.id.rl_shop})
    void click(View view) {
        switch (view.getId()) {
            case R.id.commentBTN:
                if (mModelShopOrder == null) {
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("mModelMallOrder", mModelShopOrder);
                intent.putExtra("mOrderNo", mModelShopOrder.getOrderNo());
                intent.putExtra("mOrderType", "shop");
                intent.setClass(this, OrderCommentActivity2.class);
                startActivity(intent);
                break;
            case R.id.payBTN:
                if (mModelShopOrder != null) {
                    SimpleDialog.showLoadingHintDialog(this, 4);
                    HttpParameterUtil.getInstance().requestWechatPayParams(mModelShopOrder.getOrderNo(), "0", mHandler);
                }
                break;
            case R.id.rl_shop:  // 店铺详情
//                ActivityUtils.getInstance().jumpShopDetailActivity(mModelShopOrder.getShopNo(), false);
                break;
            default:
                break;
        }
    }

    private void disposeData(Message msg) {

        switch (msg.what) {
            case ConstantHandler.WHAT_WECHAT_PAY_PARAMS_SUCCESS:
                // 获取充值信息成功
                SimpleDialog.cancelLoadingHintDialog();
                final ModelWXPay modelWXPay = (ModelWXPay) msg.obj;
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
            case ConstantHandler.WHAT_HANDLER_CLICK:
                ModelShopOrderListItem model = (ModelShopOrderListItem) msg.obj;
//                ActivityUtils.getInstance().jumpShopProductActivity(3, model.getShopNo(), model.getProductNo(), 0);
                break;
            default:
                break;
        }
    }


    private static class MyHandler extends Handler {

        private WeakReference<OrderStoreDetailActivity> mImpl;

        public MyHandler(OrderStoreDetailActivity mImpl) {
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
