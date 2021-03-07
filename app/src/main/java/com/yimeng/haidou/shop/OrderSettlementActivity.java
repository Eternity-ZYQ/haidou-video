package com.yimeng.haidou.shop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.config.EventTags;
import com.yimeng.haidou.R;
import com.yimeng.haidou.goodsClassify.CompanySaleClassifyActivity;
import com.yimeng.haidou.mine.MyCouponActivity;
import com.yimeng.dialog.PayCustomKeyboardDialog;
import com.yimeng.dialog.SetPayPwdDialog;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.CouponBean;
import com.yimeng.entity.Member;
import com.yimeng.entity.ModelAddress;
import com.yimeng.entity.ModelProductDetail;
import com.yimeng.entity.ModelSettlement;
import com.yimeng.entity.ModelWXPay;
import com.yimeng.entity.ModelWallet;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.utils.PayUtil;
import com.yimeng.utils.ShopProductUtils;
import com.yimeng.utils.SpannableStringUtils;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.MyToolBar;
import com.google.gson.JsonObject;
import com.huige.library.utils.DeviceUtils;
import com.huige.library.utils.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * @author xp
 * 订单结算
 */

public class OrderSettlementActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.shopNameTV)
    TextView shopNameTV;
    @Bind(R.id.imageSDV)
    ImageView imageSDV;
    @Bind(R.id.nameTV)
    TextView nameTV;
    @Bind(R.id.goodsMoneyTV)
    TextView goodsMoneyTV;
    @Bind(R.id.checkbox_yue_normal)
    ImageView mYue;
    @Bind(R.id.checkbox_wechat_pay_normal)
    ImageView wechatSDV;
    @Bind(R.id.checkbox_alipay_normal)
    ImageView alipaySDV;
    @Bind(R.id.checkbox_coupon_normal)
    ImageView couponIv;
    @Bind(R.id.moneyTV)
    TextView moneyTV;
    @Bind(R.id.backMoneyTV)
    TextView backMoneyTV;
    @Bind(R.id.add_send_address_rl)
    RelativeLayout add_send_address_rl;
    @Bind(R.id.send_info_rl)
    RelativeLayout send_info_rl;
    @Bind(R.id.send_name_tv)
    TextView send_name_tv;
    @Bind(R.id.send_address_tv)
    TextView send_address_tv;
    @Bind(R.id.send_phone_tv)
    TextView send_phone_tv;
    @Bind(R.id.clickRL)
    RelativeLayout clickRL;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.couponRL)
    RelativeLayout couponRL;
    @Bind(R.id.tv_coupon)
    TextView tv_coupon;
    @Bind(R.id.tv_yue)
    TextView tvYue;
    @Bind(R.id.buyerMessageET)
    EditText buyerMessageET;
    @Bind(R.id.tv_sale_type)
    TextView tv_sale_type;
    @Bind(R.id.submitRL)
    Button submitRL;
    @Bind(R.id.tv_postage)
    TextView tv_postage;


    /**
     * 支付方式
     */
    private String mPayType;
    /**
     * 订单编号
     */
    private String mOrderNo;
    /**
     * 订单结算数据
     */
    private ModelSettlement mModelSettlement;
    /**
     * 微信支付
     */
    private IWXAPI msgApi;
    private boolean hasSubmit = false;
    private OrderSettlementActivity mContext;
    private ModelAddress mModelAddress;
    /**
     * 1.
     * 2. 店铺商品
     * 3. 店铺促销商品
     * 4. 3.0购买任务
     */
    private int mType;
    // 3.0任务区块编号
    private String mBlockNo = "";
    private long oldTime = 0;
    private MyHandler mHandler = new MyHandler(this);
    private BroadcastReceiver mPaymentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

//            if (intent.getAction().equals(Constants.ACTION_PAYMENT_SUCCESS)) {
//                EventBus.getDefault().post(true, EventTags.WECHAT_PAY_RESULT);
//            } else if (intent.getAction().equals(Constants.ACTION_PAYMENT_FAIL)) {
//                EventBus.getDefault().post(false, EventTags.WECHAT_PAY_RESULT);
//            }
            EventBus.getDefault().post(intent.getAction().equals(Constants.ACTION_PAYMENT_SUCCESS),
                    EventTags.WECHAT_PAY_RESULT);
        }
    };
    private List<ModelSettlement> mModelSettlementList;
    /**
     * 是否可以选择代金券
     */
    private boolean mCanSelectedCoupon = true;
    /**
     * 选择的代金券no
     */
    private String mSelectedCouponNo;
    /**
     * 是否选中代金券
     */
    private boolean isCheckedCoupon = false;
    /**
     * shopType,店铺类型
     */
    private String mShopType;
    /**
     * 商品服务类型
     * 0。线上（仅支持送货）
     * 1。到店（仅支持到店消费）
     * 2。都支持
     */
    private int mFeeMode;
    /**
     * 钱包信息
     */
    private ModelWallet mModelWallet;
    private boolean mIsTask = false;
    private String mShopNo;
    /**
     * 运费（单位：分）
     */
    private int postageMoney = 0;
    private CouponBean selCoupon;
    private BasePopupView mPayLoadingDialog;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_order_settlement;
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

        mContext = OrderSettlementActivity.this;

        add_send_address_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpSelAddress();
            }
        });
        send_info_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpSelAddress();
            }
        });

        Bundle bundle = getIntent().getExtras();
        mType = bundle.getInt("type", 1);
        mShopType = bundle.getString("shopType", "");
        mIsTask = bundle.getBoolean("isTask", false);
        mShopNo = bundle.getString("shopNo", "");
        mBlockNo = bundle.getString("blockNo", "");

        // TODO new: 2019/4/19 业务修改，优惠券全场通用
//        mCanSelectedCoupon = bundle.getBoolean("canSelectedCoupon", false);
        if (mType == 2) { // 店铺商品不允许使用优惠券
            mCanSelectedCoupon = false;
        }

        if (mType == 2 || mType == 3 || mType == 4) {
            //
            setNewPay();
            if (mCanSelectedCoupon) {
                // 可以选择代金券
                couponRL.setVisibility(View.VISIBLE);
                getCouponNum();
            } else {
                couponRL.setVisibility(View.GONE);
            }
        } else {
            couponRL.setVisibility(View.GONE);
            mModelSettlement = (ModelSettlement) bundle.get("mModelSettlement");
        }

        // 促销商品显示 <促销商品不支持退换货>
        if (mType == 3 || mType == 4) {
            tv_sale_type.setVisibility(View.VISIBLE);
        }

        setShopInfo();

        mPayType = Constants.PAYMENT_TYPE_YUE;
        setPaymentType();
    }

    /**
     * 前往选择地址
     */
    private void jumpSelAddress() {
//        Intent intent = new Intent(mContext, AddressManageActivity.class);
//        intent.putExtra("activity", "OrderSettlementActivity");
//        startActivityForResult(iantent, 0x02);
        mOrderNo = "";
        ActivityUtils.getInstance().jumpAddressManager(true);
    }

    /**
     * type为2/3时, 设置信息
     */
    private void setNewPay() {
        List<ModelProductDetail> productSelectList;
        if (mType == 2) {
            productSelectList = ShopProductUtils.getInstance().getShopProductSelectList();
        } else {
            productSelectList = Collections.singletonList(ShopProductUtils.getInstance().getLastSaleProduct());
        }

        mModelSettlementList = new ArrayList<>();
        String products = "";
        for (ModelProductDetail detail : productSelectList) {
            products += detail.getProductNo() + ",";
        }

        // 是否需要填写收货地址
        hasAddress(productSelectList.get(0));

        HttpParameterUtil.getInstance().requestOrderSettlementList(products, mHandler);
        clickRL.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        BaseQuickAdapter<ModelProductDetail, BaseViewHolder> adapter = new BaseQuickAdapter<ModelProductDetail, BaseViewHolder>(
                R.layout.adapter_shop_pay_item) {
            @Override
            protected void convert(BaseViewHolder helper, ModelProductDetail item) {
                CommonUtils.showRadiusImage(helper.getView(R.id.sdv_product), item.getImagesPath(),
                        DeviceUtils.dp2px(mContext, 10), true, true, true, true);
                helper.setText(R.id.tv_product_name, item.getProductName())
                        .setText(R.id.tv_product_price, UnitUtil.getMoney(item.getPrice()))
                        .setText(R.id.tv_product_num, "x" + item.getSelectCount())
                        .setChecked(R.id.cb_check, !item.isNotChecked())
                        .addOnClickListener(R.id.cb_check);
                if (mType == 3 || mType == 4) {
                    CheckBox cb = helper.getView(R.id.cb_check);
                    cb.setVisibility(View.GONE);
                }

            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        adapter.setNewData(productSelectList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                // 选中
                ModelProductDetail detail = ShopProductUtils.getInstance().getShopProductSelectList().get(position);
                detail.setChecked(!detail.isNotChecked());
                parseProductPrice();
            }
        });
    }

    /**
     * 查询是否存在可使用的优惠券
     */
    private void getCouponNum() {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("used", "0");
        if (!TextUtils.isEmpty(mBlockNo)) {
            params.put("blockNo", mBlockNo);
        }
        new OkHttpCommon().postLoadData(this, ConstantsUrl.URL_MY_COUPON_LIST, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() == 1) {
                    if (jsonObject.get("data").getAsJsonObject().get("total").getAsInt() == 0) {
                        if (mType == 4) {
                            tv_coupon.setText("代金券(暂无本区可用代金券)");
                        } else {
                            tv_coupon.setText("代金券(没有可用的代金券)");
                        }
                    } else {
                        tv_coupon.setText("代金券(前往选择可用的代金券)");
                    }
                }
            }
        });
    }

    /**
     * 设置店铺默认数据
     */
    private void setShopInfo() {
        if (mModelSettlement != null) {
            shopNameTV.setText(mModelSettlement.getShopName());
            CommonUtils.showImage(imageSDV, mModelSettlement.getImagesPath());
            nameTV.setText(mModelSettlement.getProductName());
            goodsMoneyTV.setText(UnitUtil.getMoney(mModelSettlement.getPrice()));
//            int money = UnitUtil.getInt(mModelSettlement.getPrice()) + UnitUtil.getInt(mModelSettlement.getInsuranceFee());
            int money = UnitUtil.getInt(mModelSettlement.getPrice());
            moneyTV.setText(
                    SpannableStringUtils.getBuilder("实付款：")
                            .append(UnitUtil.getMoney(String.valueOf(money)))
                                            .setForegroundColor(ContextCompat.getColor(this, R.color.c_money))
                            .create());
//            backMoneyTV.setText("（" + UnitUtil.getMoney(String.valueOf(mModelSettlement.getIncome())) + "元）");

//            if (mModelSettlement.getLogisticsType() != null) {
//                // 是否送货上门
//                boolean ifCanSend = mModelSettlement.getLogisticsType().equals("all");
//                add_send_address_rl.setVisibility(ifCanSend ? View.VISIBLE : View.GONE);
//            }
        }
    }

    private void setPaymentType() {
        if (TextUtils.equals(mPayType, Constants.PAYMENT_TYPE_YUE)) {
            mYue.setImageResource(R.mipmap.select_circle);
            alipaySDV.setImageResource(R.mipmap.noselect_circle);
            wechatSDV.setImageResource(R.mipmap.noselect_circle);
        } else if (TextUtils.equals(mPayType, Constants.PAYMENT_TYPE_ALIPAY)) {
            mYue.setImageResource(R.mipmap.noselect_circle);
            alipaySDV.setImageResource(R.mipmap.select_circle);
            wechatSDV.setImageResource(R.mipmap.noselect_circle);
        } else {
            mYue.setImageResource(R.mipmap.noselect_circle);
            alipaySDV.setImageResource(R.mipmap.noselect_circle);
            wechatSDV.setImageResource(R.mipmap.select_circle);
        }

    }

    /**
     * 是否需要填写收货地址
     *
     * @param detail
     */
    public void hasAddress(ModelProductDetail detail) {
        if (!"entity".equals(mShopType)) {// 非实体店，全部线上支付
            HttpParameterUtil.getInstance().requestAddressAll(mHandler);
            return;
        }
        // 商品服务类型   0。线上（仅支持送货）1。到店（仅支持到店消费）2。都支持
        mFeeMode = detail.getFeeMode();

        if (mFeeMode == 0) {
            // 默认地址, 需要填写收货地址
            HttpParameterUtil.getInstance().requestAddressAll(mHandler);
        } else if (mFeeMode == 1) {
            // 不需要地址
        } else {
            // 地址可填可不填
            add_send_address_rl.setVisibility(View.VISIBLE);
        }


    }

    /**
     * 计算价格
     */
    private void parseProductPrice() {
        if (mModelSettlementList != null) {
            int priceSum = 0, income = 0;
            if (mType == 3 || mType == 4) {
                priceSum += (UnitUtil.getInt(ShopProductUtils.getInstance().getLastSaleProduct().getPrice()));
            } else {
                List<ModelProductDetail> productSelectList = ShopProductUtils.getInstance().getShopProductSelectList();
                for (ModelSettlement settlement : mModelSettlementList) {
                    for (ModelProductDetail productDetail : productSelectList) {
                        if (!productDetail.isNotChecked() && settlement.getProductNo().equals(productDetail.getProductNo())) {
                            priceSum += (UnitUtil.getInt(settlement.getPrice()) * productDetail.getSelectCount());
                            income += (UnitUtil.getInt(settlement.getIncome()) * productDetail.getSelectCount());
                        }
                    }
                }
//            backMoneyTV.setText("（返" + UnitUtil.getMoney(String.valueOf(income)) + "）");
            }
            if (isCheckedCoupon && priceSum != 0) {
                priceSum -= selCoupon.getAmt().intValue();
            }
            moneyTV.setText(
                    SpannableStringUtils.getBuilder("实付款：")
                            .append(UnitUtil.getMoney(String.valueOf(priceSum)))
                            .setForegroundColor(ContextCompat.getColor(this, R.color.c_money))
                            .create());
            if (isCheckedCoupon && priceSum != 0) {
                moneyTV.append("(已抵扣" + (selCoupon.getAmt() / 100) + "元）");
            }
        }

    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());

    }

    @Override
    protected void loadData() {
        HttpParameterUtil.getInstance().requestMyWallet(mHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mPaymentReceiver);
    }

    @Subscriber(tag = EventTags.WECHAT_PAY_RESULT)
    public void onPayResult(boolean flag) {
        if (flag) {
            SimpleDialog.showLoadingHintDialog(this, 1);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showToast("支付成功");
                    SimpleDialog.cancelLoadingHintDialog();
                    finishThis();
                }
            }, 1000);
        } else {
            ToastUtils.showToast("支付失败");
            hasSubmit = false;
        }

    }

    /**
     * 处理关闭
     */
    private void finishThis() {
        ActivityUtils.getInstance().jumpSubmitResult(4);
        if(mType == 4){// 3.0买单回到任务页面
            try {
                finish();
                ActivityUtils.getInstance().finishActivity(ShopProductDetailActivity.class);
                ActivityUtils.getInstance().finishActivity(SortListActivity.class);
                ActivityUtils.getInstance().finishActivity(CompanySaleClassifyActivity.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            ActivityUtils.getInstance().jumpMainActivity();
        }
    }

    /**
     * 选择优惠券
     *
     * @param coupon
     */
    @Subscriber(tag = "selectedCoupon")
    public void selectCouponResult(CouponBean coupon) {
        if (coupon == null) return;
        int money = UnitUtil.getInt(mModelSettlement.getPrice());
        if (money < coupon.getAmt().intValue()) {
            ToastUtils.showToast("商品价格不能低于代金券金额!");
            return;
        }
        selCoupon = coupon;
        isCheckedCoupon = true;
        changeCouponStatus();
        mSelectedCouponNo = coupon.getCouponNo();
        tv_coupon.setText("代金券(¥" + (coupon.getAmt() / 100) + ")");
    }

    /**
     * 改变代金券按钮
     */
    private void changeCouponStatus() {
        int money = UnitUtil.getInt(mModelSettlement.getPrice());
        money += postageMoney;

//        String postFeeStr;
//        if(postageMoney==0) {
//            postFeeStr = "（免运费）";
//        }else{
//            postFeeStr = "（免运费" + UnitUtil.getMoney(postageMoney+"") + "）";
//        }

        if (isCheckedCoupon) {
            couponIv.setImageResource(R.mipmap.select_circle);
            money -= selCoupon.getAmt().intValue();
            moneyTV.setText(
                    SpannableStringUtils.getBuilder("实付款：")
                            .append(UnitUtil.getMoney(String.valueOf(money)))
                            .setForegroundColor(ContextCompat.getColor(this, R.color.c_money))
                            .create()
            );
            moneyTV.append("(已抵扣" + (selCoupon.getAmt() / 100) + "元）");
        } else {
            couponIv.setImageResource(R.mipmap.noselect_circle);
            moneyTV.setText(
                    SpannableStringUtils.getBuilder("实付款：")
                            .append(UnitUtil.getMoney(String.valueOf(money)))
                            .setForegroundColor(ContextCompat.getColor(this, R.color.c_money))
                            .create());
        }
    }

    /**
     * 选择地址回调
     *
     * @param address
     */
    @Subscriber(tag = EventTags.PAY_SELECT_ADDRESS)
    public void selectAddress(ModelAddress address) {
        mModelAddress = address;
        if (mModelAddress != null) {
            setAddressInfo();
        }
    }

    /**
     * 设置地址数据
     */
    private void setAddressInfo() {
        if (mModelAddress == null) {
            return;
        }
        submitRL.setEnabled(true);
        send_info_rl.setVisibility(View.VISIBLE);
        add_send_address_rl.setVisibility(View.GONE);
        send_name_tv.setText(mModelAddress.getLinkman());
        send_phone_tv.setText(mModelAddress.getMobileNo());
        send_address_tv.setText("收货地址：" + mModelAddress.getProvince() + mModelAddress.getCity() + mModelAddress.getArea() + mModelAddress.getAddress());

        getPostage(mModelAddress.getProvince());
    }

    /**
     * 获取该地区的邮费
     */
    public void getPostage(String provinceStr) {
        submitRL.setEnabled(false);
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("areaName", provinceStr);
        new OkHttpCommon().postLoadData(this, ConstantsUrl.URL_ADDRESS_POSTAGE, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("获取邮费信息失败");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "获取邮费信息失败"));
                    return;
                }
                submitRL.setEnabled(true);
                String postage = jsonObject.get("data").getAsString();
                postageMoney = UnitUtil.getInt(postage);
                tv_postage.setText(UnitUtil.getMoney(postage));
                changeCouponStatus();
            }
        });

    }

    @OnClick({R.id.submitRL, R.id.wechatRL, R.id.alipayRL, R.id.couponRL, R.id.rl_yue_pay,
            R.id.view_sel_coupon})
    public void myOnClick(View view) {
        switch (view.getId()) {
            case R.id.view_sel_coupon:
                if (TextUtils.isEmpty(mSelectedCouponNo)) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isSelect", true);
                    bundle.putBoolean("isTask", mIsTask);
                    bundle.putString("blockNo", mBlockNo);
                    ActivityUtils.getInstance().jumpActivity(MyCouponActivity.class, bundle);
                } else {
                    // 选中代金券
                    isCheckedCoupon = !isCheckedCoupon;
                    changeCouponStatus();
                }
                break;
            case R.id.couponRL: // 去选择优惠券
                Bundle bundle = new Bundle();
                bundle.putBoolean("isSelect", true);
                bundle.putBoolean("isTask", mIsTask);
                bundle.putString("blockNo", mBlockNo);
                ActivityUtils.getInstance().jumpActivity(MyCouponActivity.class, bundle);
                break;
            case R.id.rl_yue_pay:   // 余额支付
                mPayType = Constants.PAYMENT_TYPE_YUE;
                setPaymentType();
                break;
            case R.id.wechatRL:
                mPayType = Constants.PAYMENT_TYPE_WECHAT;
                setPaymentType();
                break;
            case R.id.alipayRL:
                mPayType = Constants.PAYMENT_TYPE_ALIPAY;
                setPaymentType();
                break;
            case R.id.submitRL:
                String shopNo = CommonUtils.getMember().getTelePhone();
                Log.d("msg", "OrderSettlementActivity -> myOnClick: " + shopNo);
                if (mModelSettlement != null && shopNo.equals(mModelSettlement.getShopName())) {
                    ToastUtils.showToast("不能购买自己店铺的商品!");
                    return;
                }

                if (new Date().getTime() - oldTime < 2000) {
                    return;
                }
                oldTime = new Date().getTime();

                String leaveMsg = buyerMessageET.getText().toString().trim();

                if (mOrderNo == null || TextUtils.isEmpty(mOrderNo)) {
                    if (mType == 2) {
                        StringBuffer ss = new StringBuffer();
                        ss.append("[");
                        List<ModelProductDetail> shopProductSelectList = ShopProductUtils.getInstance().getShopProductSelectList();
                        for (ModelProductDetail productDetail : shopProductSelectList) {
                            if (!productDetail.isNotChecked()) {
                                ss.append("{\"productNo\":\"")
                                        .append(productDetail.getProductNo())
                                        .append("\",\"count\":\"")
                                        .append(productDetail.getSelectCount())
                                        .append("\"},");
                            }
                        }
                        ss.append("]");
                        String addressNo = "";
                        // 实体店不需要收货地址
                        if (!mShopType.equals("entity")) {
                            if (mModelAddress == null) {
                                jumpSelAddress();
                                ToastUtils.showToast("请选择收货地址!");
                                return;
                            }
                            addressNo = mModelAddress.getAddressNo();
                        } else {
                            if (mFeeMode == 0) { // 仅支持送货商品
                                if (mModelAddress == null) {
                                    jumpSelAddress();
                                    ToastUtils.showToast("请选择收货地址!");
                                    return;
                                }
                                addressNo = mModelAddress.getAddressNo();
                            } else if (mFeeMode == 2) {
                                // 可到店也可送货商品
                                if (mModelAddress != null) {
                                    addressNo = mModelAddress.getAddressNo();
                                }
                            }
                        }

                        String couponNo = isCheckedCoupon ? mSelectedCouponNo : "";
                        HttpParameterUtil.getInstance().requestCreateShopOrder(addressNo, ss.toString(), couponNo, leaveMsg, 1, "", "", "", mHandler);
                    } else if (mType == 3 || mType == 4) {
                        // 店铺促销商品
                        StringBuffer ss = new StringBuffer();
                        ModelProductDetail lastSaleProduct = ShopProductUtils.getInstance().getLastSaleProduct();
                        ss.append("[")
                                .append("{\"productNo\":\"")
                                .append(lastSaleProduct.getProductNo())
                                .append("\",\"count\":\"")
                                .append("1")
                                .append("\"}")
                                .append("]");

                        String addressNo = "";
                        // 实体店不需要收货地址
                        if (!mShopType.equals("entity")) {
                            if (mModelAddress == null) {
                                jumpSelAddress();
                                ToastUtils.showToast("请选择收货地址!");
                                return;
                            }
                            addressNo = mModelAddress.getAddressNo();
                        } else {
                            if (mFeeMode == 0) { // 仅支持送货商品
                                if (mModelAddress == null) {
                                    jumpSelAddress();
                                    ToastUtils.showToast("请选择收货地址!");
                                    return;
                                }
                                addressNo = mModelAddress.getAddressNo();
                            } else if (mFeeMode == 2) {
                                // 可到店也可送货商品
                                if (mModelAddress != null) {
                                    addressNo = mModelAddress.getAddressNo();
                                }
                            }
                        }
                        String couponNo = isCheckedCoupon ? mSelectedCouponNo : "";
                        Bundle extras = getIntent().getExtras();
                        String mProductSetNo = "", selParams = "";
                        if (extras != null) {
                            mProductSetNo = extras.getString("mProductSetNo");
                            selParams = extras.getString("selParams");
                        }

                        HttpParameterUtil.getInstance().requestCreateShopOrder(addressNo, ss.toString(), couponNo, leaveMsg, mType == 3 ? 2 : 3, mShopNo, mProductSetNo, selParams, mHandler);
                    } else {
                        if (mModelAddress != null) {
                            HttpParameterUtil.getInstance().requestCreateNearbyOrder(mModelSettlement.getProductNo(), mModelAddress.getAddressNo(), mHandler);
                        } else {
                            HttpParameterUtil.getInstance().requestCreateNearbyOrder(mModelSettlement.getProductNo(), "", mHandler);
                        }
                    }
                    return;
                } else {
                    if (hasSubmit) {
                        return;
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = ConstantHandler.WHAT_CREATE_NEARBY_ORDER_SUCCESS;
                    msg.obj = mOrderNo;
                    mHandler.sendMessage(msg);
                    hasSubmit = true;
                }
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    private void disposeData(Message msg) {
        switch (msg.what) {
            case ConstantHandler.WHAT_MY_WALLET_SUCCESS:
                // 钱包信息
                mModelWallet = (ModelWallet) msg.obj;
                if (mModelWallet != null) {
                    tvYue.setText("余额支付(用户余额：" + UnitUtil.getMoney(mModelWallet.getBalance()) + ")");
                }
                break;
            case ConstantHandler.WHAT_ORDER_PAY_SUCCESS:
                SimpleDialog.cancelLoadingHintDialog();
                if (mPayLoadingDialog != null && mPayLoadingDialog.isShow())
                    mPayLoadingDialog.dismiss();
                finishThis();
                break;
            case ConstantHandler.WHAT_ORDER_PAY_FAIL:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast((String) msg.obj);
                hasSubmit = false;
                break;
            case ConstantHandler.WHAT_CREATE_NEARBY_ORDER_SUCCESS:
                mOrderNo = (String) msg.obj;
                SimpleDialog.cancelLoadingHintDialog();
                //2微信支付
                if (TextUtils.equals(mPayType, Constants.PAYMENT_TYPE_YUE)) {
                    yuePay();
//                    HttpParameterUtil.getInstance().requestOrderToPay(mOrderNo, "0", mHandler);
                } else if (TextUtils.equals(mPayType, Constants.PAYMENT_TYPE_WECHAT)) {
                    HttpParameterUtil.getInstance().requestWechatPayParams(mOrderNo, "0", mHandler);
                } else {
                    PayUtil.toAlipayNativePayOrder(this, mOrderNo, "0");
                }
                break;
            case ConstantHandler.WHAT_CREATE_NEARBY_ORDER_FAIL:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast((String) msg.obj);
                break;
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
            case ConstantHandler.WHAT_ORDER_SETTLEMENT_SUCCESS:
                // 店铺信息
                mModelSettlement = (ModelSettlement) msg.obj;
                setShopInfo();
                break;
            case ConstantHandler.WHAT_ORDER_SETTLEMENT_LIST_SUCCESS:
                // 列表商品信息
                mModelSettlementList.addAll((List<ModelSettlement>) msg.obj);
                mModelSettlement = mModelSettlementList.get(0);
                setShopInfo();
                parseProductPrice();
                break;
            case ConstantHandler.WHAT_ORDER_SETTLEMENT_LIST_FAIL:
                SimpleDialog.showTipDialog(this, (String) msg.obj, new SimpleDialog.OnSimpleDialogClickListener() {
                    @Override
                    public void onClick(View v, AlertDialog dialog) {
                        finish();
                    }
                });
                break;
            case ConstantHandler.WHAT_ADDRESS_ALL_SUCCESS:
                // 所有地址
                LinkedList<ModelAddress> addressList = (LinkedList<ModelAddress>) msg.obj;
                // 没有地址
                add_send_address_rl.setVisibility(View.VISIBLE);
                if (addressList.size() > 0) {
                    //有地址
                    // 查找默认地址
                    for (int i = 0; i < addressList.size(); i++) {
                        String isdefault = addressList.get(i).getIsdefault();
                        if (isdefault.equals(String.valueOf(1))) {
                            mModelAddress = addressList.get(i);
                            setAddressInfo();
                            break;
                        }
                    }
                    //有地址，但没有默认地址
                }
                break;
            default:
                break;
        }
    }



    /**
     * 余额支付
     */
    private void yuePay() {

        Member member = CommonUtils.getMember();
        if (member == null) return;
        if (mModelWallet == null) return;
        if (UnitUtil.getInt(mModelWallet.getBalance()) / 100.0 <= 0) {
            ToastUtils.showToast("余额不足，请选用其它支付方式");
            return;
        }

        if (!member.isSetPayPwd()) {
            // 设置支付密码
            new XPopup.Builder(this)
                    .hasShadowBg(false)
                    .autoOpenSoftInput(true)
                    .asCustom(new SetPayPwdDialog(this))
                    .show();
        } else {

            // 用户余额
            String balance = mModelWallet.getBalance();
            long couponAmt = 0;
            if (isCheckedCoupon) {// 优惠券
                couponAmt = selCoupon.getAmt();
            }
            int productPrice = UnitUtil.getInt(mModelSettlement.getPrice());
            productPrice += postageMoney;

            if ((UnitUtil.getInt(balance) + couponAmt) < productPrice) {
                ToastUtils.showToast("余额不足");
                return;
            }


            hasSubmit = false;
            new XPopup.Builder(this)
                    .autoOpenSoftInput(false)
                    .dismissOnTouchOutside(false)
                    .asCustom(new PayCustomKeyboardDialog(this, new PayCustomKeyboardDialog.CheckPwdSuccess() {
                        @Override
                        public void onResult(boolean result) {
                            showPayLoading();
                            HttpParameterUtil.getInstance().requestOrderToPay(mOrderNo, "0", mHandler);
                        }
                    }))
                    .show();
        }
    }

    /**
     * 支付对话框
     */
    private void showPayLoading() {
//        final CustomLoadingDialog customLoadingDialog = new CustomLoadingDialog(OrderSettlementActivity.this, null,true);
//        mPayLoadingDialog = new XPopup.Builder(OrderSettlementActivity.this)
//                .hasShadowBg(false)
//                .popupAnimation(PopupAnimation.NoAnimation)
//                .setPopupCallback(new SimpleCallback(){
//                    @Override
//                    public void onCreated() {
//                        super.onCreated();
//                        customLoadingDialog.setLoadingMsg("正在支付...").startAnim();
//                    }
//                })
//                .asCustom(customLoadingDialog).show();

        mPayLoadingDialog = SimpleDialog.createDialog(OrderSettlementActivity.this, getString(R.string.pay_loading), false, true, true).show();
    }

    private static class MyHandler extends Handler {

        private WeakReference<OrderSettlementActivity> mImpl;

        public MyHandler(OrderSettlementActivity mImpl) {
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
