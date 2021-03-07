package com.yimeng.haidou.shopcard;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.Constants;
import com.yimeng.config.EventTags;
import com.yimeng.haidou.R;
import com.yimeng.haidou.shop.adapter.PayNowAdapter;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.ModelAddress;
import com.yimeng.entity.ModelShopCarSettle;
import com.yimeng.entity.ModelShopCarSettleItem;
import com.yimeng.entity.ModelWXPay;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.JsonUtil;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.MyToolBar;
import com.huige.library.utils.ToastUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.simple.eventbus.Subscriber;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.LinkedList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 购物车结算中心
 */

public class ShopCartSettleActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.lv_goods_in_the_shop)
    ListView mListView;
    @Bind(R.id.tv_name)
    TextView mName;
    @Bind(R.id.tv_phone)
    TextView mPhone;
    @Bind(R.id.tv_district)
    TextView mDistrict;
    @Bind(R.id.scrollview_pay_now)
    ScrollView mScrollView;

    @Bind(R.id.rl_default_address)
    RelativeLayout mDefalutRL; //有默认地址
    @Bind(R.id.rl_not_data_address)
    RelativeLayout mNotDataAddressRL;//没有设置地址
    @Bind(R.id.rl_select_address)
    RelativeLayout mselectAddressRL; //有地址，没有设置默认地址
    @Bind(R.id.tv_select_address)
    TextView mSelectAddressTV; //有地址，没有设置默认地址,去选择地址

    //支付方式
    @Bind(R.id.checkbox_wechat_pay_normal)
    ImageView mWeChat;
    @Bind(R.id.checkbox_alipay_normal)
    ImageView mAlipay;
    @Bind(R.id.tv_consumption_beans)
    TextView beansTV;// 统计如: 100+0麻豆
    @Bind(R.id.payMoneyTV)
    TextView payMoneyTV;// 需要支付的钱

    private PayNowAdapter mAdapter;
    private final int WHAT_HANDLER_CLICK = 0x01;
    private LinkedList<ModelShopCarSettle> mList;
    private ModelAddress mModelAddress;// 当前选择的地址
    private LinkedList<ModelAddress> mAddressList;

    private String linkman;//联系人
    private String phone;
    private String shopCarNo;//购物车编号

    /**
     * 订单编号
     */
    private String mOrderNo;
    /**
     * 支付类型 -1未选择，1余额，2微信支付，3支付宝支付，
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
    /**
     * 微信支付
     */
    private IWXAPI msgApi;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_shopcart_settle;
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


        beansTV.setVisibility(View.GONE);
        mWeChat.setImageResource(R.mipmap.noselect_circle);
        mAlipay.setImageResource(R.mipmap.noselect_circle);
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {
        mPayType = -1;
        mFreightMoney = 0;
        shopCarNo = getIntent().getStringExtra("mShopCarNo");

        HttpParameterUtil.getInstance().requestAddressAll(mHandler);
        HttpParameterUtil.getInstance().requestShopCarSettle(shopCarNo, mHandler);

        mList = new LinkedList<>();
        mAdapter = new PayNowAdapter(mList, this, mHandler, WHAT_HANDLER_CLICK);
        mListView.setAdapter(mAdapter);

        setAllMoneyInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mPaymentReceiver);
    }

    /**
     * 选择地址回调
     * @param address
     */
    @Subscriber(tag = EventTags.PAY_SELECT_ADDRESS)
    public void selectAddress(ModelAddress address){
        mModelAddress = address;
        assert mModelAddress != null;
        linkman = mModelAddress.getLinkman();
        phone = mModelAddress.getMobileNo();
        String addressStr = mModelAddress.getProvince() + mModelAddress.getCity() + mModelAddress.getArea() + mModelAddress.getAddress();
        mDefalutRL.setVisibility(View.VISIBLE);
        mselectAddressRL.setVisibility(View.GONE);
        mNotDataAddressRL.setVisibility(View.GONE);
        mName.setText(linkman);
        mPhone.setText(phone);
        mDistrict.setText("收货地址: " + addressStr);
    }




    private long oldTime = 0;

    @OnClick({R.id.fl_wechat_pay, R.id.fl_alipay,
            R.id.rl_not_data_address, R.id.rl_select_address, R.id.rl_default_address, R.id.rl_submit})
    public void myOnClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.fl_wechat_pay:
                mWeChat.setImageResource(R.mipmap.select_circle);
                mAlipay.setImageResource(R.mipmap.noselect_circle);
                mPayType = 2;
                setAllMoneyInfo();
                break;
            case R.id.fl_alipay:
                mWeChat.setImageResource(R.mipmap.noselect_circle);
                mAlipay.setImageResource(R.mipmap.select_circle);
                mPayType = 3;
                setAllMoneyInfo();
                break;
            case R.id.rl_not_data_address:
                //没有地址，添加 地址
            case R.id.rl_select_address:
            case R.id.rl_default_address:
//                intent = new Intent(this, AddressManageActivity.class);
//                intent.putExtra("activity", "PayNowActivity");
//                startActivityForResult(intent, 0x01);
                ActivityUtils.getInstance().jumpAddressManager(true);
                break;
            case R.id.rl_submit:
                // 确认支付
                if (new Date().getTime() - oldTime < 2000) {
                    return;
                }
                oldTime = new Date().getTime();

                if (mOrderNo == null || TextUtils.isEmpty(mOrderNo)) {
                    submitOrderPay();
                } else {
                    Message msg = mHandler.obtainMessage();
                    msg.what = ConstantHandler.WHAT_SHOPCART_CREATE_ORDER_SUCCESS;
                    msg.obj = mOrderNo;
                    mHandler.sendMessage(msg);
                }
                break;
            default:
                break;
        }
    }

    public void submitOrderPay() {
        if (mPayType == -1) {
            ToastUtils.showToast("请选择支付方式");
            return;
        }
        if (mModelAddress == null) {
            ToastUtils.showToast("请选择地址");
            return;
        }

        String addressNo = mModelAddress.getAddressNo();
        String payType = "";
        switch (mPayType) {
            case 1:
                payType = "yu_e";
                break;
            case 2:
                payType = "weixin";
                break;
            case 3:
                payType = "zhifubao";
                break;
            default:
                break;
        }
        SimpleDialog.showLoadingHintDialog(this, 4);
        HttpParameterUtil.getInstance().requestShopcartCreateOrder(shopCarNo, addressNo, payType,
                JsonUtil.getShopCartNosJson(mAdapter.mList), mHandler);
    }

    @SuppressLint("SetTextI18n")
    private void setTotalMoney() {
        if (mAdapter == null) {
            return;
        }

        mTotalMoney = 0;
        for (ModelShopCarSettle shopCart : mAdapter.mList) {
            for (ModelShopCarSettleItem item : shopCart.getModelShopCarSettleItemList()) {
                mTotalMoney += UnitUtil.getInt(item.getPrice()) * UnitUtil.getInt(item.getShopCarNum());
            }
            mFreightMoney += UnitUtil.getInt(shopCart.getLogisticsFee());
        }
        mTotalMoney += mFreightMoney;
    }

    /**
     * 设置所有的金额信息
     * 需支付 = 总金额-麻豆金额-账号余额
     */
    private void setAllMoneyInfo() {
        beansTV.setText(UnitUtil.getMoney(mTotalMoney + ""));

        int payMoney = mTotalMoney;
        payMoney = payMoney > 0 ? payMoney : 0;
        payMoneyTV.setText("需支付"+ UnitUtil.getMoney(payMoney + ""));
    }

    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private WeakReference<ShopCartSettleActivity> mImpl;

        public MyHandler(ShopCartSettleActivity mImpl) {
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

    @SuppressLint("SetTextI18n")
    @SuppressWarnings("unchecked")
    private void disposeData(Message msg) {
        boolean hasDefault = false;
        switch (msg.what) {
            case ConstantHandler.WHAT_SHOP_CAR_SETTLE_SUCCESS:
                mList = (LinkedList<ModelShopCarSettle>) msg.obj;
                mAdapter.mList = mList;
                mAdapter.notifyDataSetChanged();

                setTotalMoney();
                setAllMoneyInfo();
                break;
            case ConstantHandler.WHAT_ADDRESS_ALL_SUCCESS:
                // 所有地址
                mAddressList = (LinkedList<ModelAddress>) msg.obj;
                if (mAddressList.size() > 0) {
                    //有地址
                    // 查找默认地址
                    for (int i = 0; i < mAddressList.size(); i++) {
                        String isdefault = mAddressList.get(i).getIsdefault();
                        if (isdefault.equals(String.valueOf(1))) {
                            hasDefault = true;
                            mModelAddress = mAddressList.get(i);
                            mDefalutRL.setVisibility(View.VISIBLE);
                            mselectAddressRL.setVisibility(View.GONE);
                            mNotDataAddressRL.setVisibility(View.GONE);
                            mName.setText("收货人: " + mAddressList.get(i).getLinkman());
                            mPhone.setText(mAddressList.get(i).getMobileNo());
                            mDistrict.setText(mAddressList.get(i).getProvince() + mAddressList.get(i).getCity() + mAddressList.get(i).getArea() + mAddressList.get(i).getAddress());
                        }
                    }
                    //有地址，但没有默认地址
                    if (!hasDefault) {
                        mDefalutRL.setVisibility(View.GONE);
                        mselectAddressRL.setVisibility(View.VISIBLE);
                        mNotDataAddressRL.setVisibility(View.GONE);
                    }
                } else if (mAddressList.size() == 0) {
                    //没有地址
                    mDefalutRL.setVisibility(View.GONE);
                    mselectAddressRL.setVisibility(View.GONE);
                    mNotDataAddressRL.setVisibility(View.VISIBLE);
                }
                break;
            case ConstantHandler.WHAT_SHOPCART_CREATE_ORDER_SUCCESS:
                mOrderNo = (String) msg.obj;
                SimpleDialog.cancelLoadingHintDialog();

                // 1余额支付，2微信支付,3支付宝
                if (mPayType == 2) {
                    HttpParameterUtil.getInstance().requestWechatPayParams(mOrderNo, "", mHandler);
                } else {
                    ActivityUtils.getInstance().toAlipayNativePayOrder(this, mOrderNo, "");
                }
                break;
            case ConstantHandler.WHAT_SHOPCART_CREATE_ORDER_FAIL:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast((String) msg.obj);
                break;
            case ConstantHandler.WHAT_WECHAT_PAY_PARAMS_SUCCESS:
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
            default:
                break;
        }
    }

    private BroadcastReceiver mPaymentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.ACTION_PAYMENT_SUCCESS)) {
                ToastUtils.showToast("支付成功");
                ActivityUtils.getInstance().jumpSubmitResult(1);
                finish();
            } else if (intent.getAction().equals(Constants.ACTION_PAYMENT_FAIL)) {
                ToastUtils.showToast("支付失败");
            }
        }
    };

}
