package com.yimeng.haidou.shop;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.config.EventTags;
import com.yimeng.haidou.R;
import com.yimeng.haidou.mine.MyCouponActivity;
import com.yimeng.haidou.shop.adapter.PayNowAdapter;
import com.yimeng.dialog.PayCustomKeyboardDialog;
import com.yimeng.dialog.SetPayPwdDialog;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.CouponBean;
import com.yimeng.entity.Member;
import com.yimeng.entity.ModelAddress;
import com.yimeng.entity.ModelShopCarSettle;
import com.yimeng.entity.ModelShopCarSettleItem;
import com.yimeng.entity.ModelWXPay;
import com.yimeng.entity.ModelWallet;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.utils.SpannableStringUtils;
import com.yimeng.utils.StringUtils;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.InputItemLayout;
import com.yimeng.widget.MyToolBar;
import com.google.gson.JsonObject;
import com.huige.library.popupwind.PopupWindowUtils;
import com.huige.library.utils.SharedPreferencesUtils;
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
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * ????????????
 */

public class PayNowActivity extends BaseActivity {

    private final int WHAT_HANDLER_CLICK = 0x01;
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
    RelativeLayout mDefalutRL; //???????????????
    @Bind(R.id.rl_not_data_address)
    RelativeLayout mNotDataAddressRL;//??????????????????
    @Bind(R.id.rl_select_address)
    RelativeLayout mselectAddressRL; //????????????????????????????????????
    @Bind(R.id.tv_select_address)
    TextView mSelectAddressTV; //????????????????????????????????????,???????????????
    //????????????
    @Bind(R.id.checkbox_wechat_pay_normal)
    ImageView mWeChat;
    @Bind(R.id.checkbox_alipay_normal)
    ImageView mAlipay;
    @Bind(R.id.checkbox_yue_normal)
    ImageView mYue;
    @Bind(R.id.tv_consumption_beans)
    TextView beansTV;// ?????????: 100+0??????
    @Bind(R.id.payMoneyTV)
    TextView payMoneyTV;// ??????????????????
    // ????????????
    @Bind(R.id.layout_invoice)
    ConstraintLayout layoutInvoice;
    @Bind(R.id.tv_invoice_info)
    TextView tvInvoiceInfo;
    @Bind(R.id.cb_invoice)
    CheckBox cbInvoice;
    @Bind(R.id.couponRL)
    RelativeLayout couponRL;
    @Bind(R.id.tv_coupon)
    TextView tv_coupon;
    @Bind(R.id.checkbox_coupon_normal)
    ImageView couponIv;
    @Bind(R.id.tv_yue)
    TextView tvYue;
    @Bind(R.id.submitRL)
    Button submitRL;
    @Bind(R.id.tv_postage)
    TextView tv_postage;


    /**
     * ????????????
     */
    private ModelWallet mModelWallet;
    private PayNowAdapter mAdapter;
    private LinkedList<ModelShopCarSettle> mList;
    private ModelAddress mModelAddress;// ?????????????????????
    private LinkedList<ModelAddress> mAddressList;

    /**
     * ????????????
     */
    private String mOrderNo;
    /**
     * ????????????
     */
    private String mProductNo;
    /**
     * ??????????????????
     */
    private String mProductSetNo;
    /**
     * ????????????
     */
    private String mProductNum;
    /**
     * ???????????? -1????????????1?????????2???????????????3??????????????????
     */
    private int mPayType = 1;
    /**
     * ???????????????
     */
    private int mFreightMoney;
    /**
     * ??????????????????
     */
    private int mTotalMoney;
    /**
     * ??????????????????no
     */
    private String mSelectedCouponNo;
    /**
     * ?????????????????????
     */
    private boolean isCheckedCoupon = false;
    /**
     * ????????????
     */
    private IWXAPI msgApi;
    private boolean hasSubmit = false;
    private long oldTime = 0;
    private MyHandler mHandler = new MyHandler(this);
    private BroadcastReceiver mPaymentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(Constants.ACTION_PAYMENT_SUCCESS)) {
//                ToastUtils.showToast("????????????");
//                ActivityUtils.getInstance().jumpSubmitResult(1);
//                finish();
//            } else if (intent.getAction().equals(Constants.ACTION_PAYMENT_FAIL)) {
//                ToastUtils.showToast("????????????");
//                hasSubmit = false;
//            }
            EventBus.getDefault().post(intent.getAction().equals(Constants.ACTION_PAYMENT_SUCCESS),
                    EventTags.WECHAT_PAY_RESULT);
        }
    };
    /**
     * ??????????????????
     */
    private boolean hasInvoice = false;
    /**
     * ????????????
     */
    private boolean isEmail = true;
    /**
     * ??????
     */
    private boolean isPerson = true;
    /**
     * ????????????????????????
     */
    private int postageMoney = 0;

    private PopupWindowUtils mInvoicePopupWindowUtils;
    private RadioButton mRb_invoice_email;
    private RadioButton mRb_invoice_papery;
    private RadioButton mRb_invoice_person;
    private RadioButton mRb_invoice_company;
    private InputItemLayout mInput_email;
    private InputItemLayout mInput_invoice_name;
    private InputItemLayout mInput_invoice_no;
    private InputItemLayout mInput_invoice_address;
    private InputItemLayout mInput_invoice_mobile;
    private InputItemLayout mInput_invoice_bank;
    private InputItemLayout mInput_invoice_bank_no;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_pay_now;
    }

    @Override
    protected void init() {
        // ??????app???????????????
        msgApi = WXAPIFactory.createWXAPI(this, null);
        msgApi.registerApp(Constants.WX_APPID);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_PAYMENT_SUCCESS);
        filter.addAction(Constants.ACTION_PAYMENT_FAIL);
        registerReceiver(mPaymentReceiver, filter);

        mYue.setImageResource(R.mipmap.select_circle);
        mWeChat.setImageResource(R.mipmap.noselect_circle);
        mAlipay.setImageResource(R.mipmap.noselect_circle);

        mPayType = 1;
        mFreightMoney = 0;
        mProductNo = getIntent().getStringExtra("mProductNo");
        mProductSetNo = getIntent().getStringExtra("mProductSetNo");
        mProductNum = getIntent().getStringExtra("mProductNum");

        //vip??????
        if (null != getIntent().getStringExtra("mVip") && "vip".equals(getIntent().getStringExtra("mVip"))) {
            this.findViewById(R.id.rl_yue_pay).setVisibility(View.GONE);
            ImageView select = this.findViewById(R.id.checkbox_alipay_normal);
            select.setImageResource(R.mipmap.select_circle);
            mPayType = 3;
        }

        HttpParameterUtil.getInstance().requestAddressAll(mHandler);
        HttpParameterUtil.getInstance().requestPayNowInfo(mProductNo, mProductSetNo, mProductNum, mHandler);

        mList = new LinkedList<>();
        mAdapter = new PayNowAdapter(mList, this, mHandler, WHAT_HANDLER_CLICK);
        mListView.setAdapter(mAdapter);

        setAllMoneyInfo();

        initInvoicePopup();

    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());

        cbInvoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mInvoicePopupWindowUtils.showAtLocation(PayNowActivity.this, buttonView, Gravity.BOTTOM, 0, 0);
                } else {
                    hasInvoice = false;
                }
            }
        });
    }

    @Override
    protected void loadData() {
        getCouponNum();
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
                    ToastUtils.showToast("????????????");
                    SimpleDialog.cancelLoadingHintDialog();
                    ActivityUtils.getInstance().jumpSubmitResult(1);
                    ActivityUtils.getInstance().jumpMainActivity();
                }
            }, 1000);

        } else {
            ToastUtils.showToast("????????????");
            hasSubmit = false;
        }
    }

    /**
     * ???????????????????????????
     * ????????? = ?????????-????????????-????????????
     */
    private void setAllMoneyInfo() {
        beansTV.setText(UnitUtil.getMoney(mTotalMoney + ""));
        int payMoney = mTotalMoney;
        payMoney = payMoney > 0 ? payMoney : 0;

        if (isCheckedCoupon && payMoney != 0) {
            payMoney -= selCoupon.getAmt().intValue();
        }
        payMoneyTV.setText(
                SpannableStringUtils.getBuilder("????????????")
                        .append(UnitUtil.getMoney(String.valueOf(payMoney)))
                        .setForegroundColor(ContextCompat.getColor(this, R.color.c_money))
                        .create());
        if (isCheckedCoupon && payMoney != 0) {
            payMoneyTV.append("(?????????" + (selCoupon.getAmt() / 100) + "??????");
        }

        if (!mList.isEmpty()) {
            for (ModelShopCarSettle carSettle : mList) {
                if (carSettle.isCanInvoice()) { // ??????????????????????????????????????????
                    layoutInvoice.setVisibility(View.VISIBLE);
                    return;
                }
            }
        }
    }

    /**
     * ?????????????????????
     */
    private void initInvoicePopup() {
        View rootView = LayoutInflater.from(this).inflate(R.layout.popup_layout_invoice, null);
        final RadioGroup rg_invoice_classify = rootView.findViewById(R.id.rg_invoice_classify);
        mRb_invoice_email = rootView.findViewById(R.id.rb_invoice_email);
        mRb_invoice_papery = rootView.findViewById(R.id.rb_invoice_papery);

        final RadioGroup rg_invoice_type = rootView.findViewById(R.id.rg_invoice_type);
        mRb_invoice_person = rootView.findViewById(R.id.rb_invoice_person);
        mRb_invoice_company = rootView.findViewById(R.id.rb_invoice_company);

        mInput_email = rootView.findViewById(R.id.input_email);
        mInput_invoice_name = rootView.findViewById(R.id.input_invoice_name);
        mInput_invoice_no = rootView.findViewById(R.id.input_invoice_no);
        mInput_invoice_address = rootView.findViewById(R.id.input_invoice_address);
        mInput_invoice_mobile = rootView.findViewById(R.id.input_invoice_mobile);
        mInput_invoice_bank = rootView.findViewById(R.id.input_invoice_bank);
        mInput_invoice_bank_no = rootView.findViewById(R.id.input_invoice_bank_no);

        setInvoiceDefaultData();

        rg_invoice_classify.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_invoice_email) {
                    mInput_email.setVisibility(View.VISIBLE);
                } else {
                    if (rg_invoice_type.getCheckedRadioButtonId() == R.id.rb_invoice_company) {
                        mInput_email.setVisibility(View.GONE);
                    } else {
                        mInput_email.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        rg_invoice_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rb_invoice_person) {
                    mInput_email.setVisibility(View.VISIBLE);
                    mInput_invoice_name.setVisibility(View.GONE);
                    mInput_invoice_no.setVisibility(View.GONE);
                    mInput_invoice_address.setVisibility(View.GONE);
                    mInput_invoice_mobile.setVisibility(View.GONE);
                    mInput_invoice_bank.setVisibility(View.GONE);
                    mInput_invoice_bank_no.setVisibility(View.GONE);
                } else {
                    if (rg_invoice_classify.getCheckedRadioButtonId() == R.id.rb_invoice_papery) {
                        mInput_email.setVisibility(View.GONE);
                    } else {
                        mInput_email.setVisibility(View.VISIBLE);
                    }
                    mInput_invoice_name.setVisibility(View.VISIBLE);
                    mInput_invoice_no.setVisibility(View.VISIBLE);
                    mInput_invoice_address.setVisibility(View.VISIBLE);
                    mInput_invoice_mobile.setVisibility(View.VISIBLE);
                    mInput_invoice_bank.setVisibility(View.VISIBLE);
                    mInput_invoice_bank_no.setVisibility(View.VISIBLE);
                }
            }
        });
        rg_invoice_classify.check(R.id.rb_invoice_email);
        rg_invoice_type.check(R.id.rb_invoice_person);
        rootView.findViewById(R.id.btn_submit_invoice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ??????
                if (rg_invoice_classify.getCheckedRadioButtonId() == R.id.rb_invoice_email) {
                    if (CommonUtils.checkInputData(mInput_email, "?????????????????????")) return;
                    if (!StringUtils.isEmail(mInput_email.getInputText())) {
                        ToastUtils.showToast("????????????????????????????????????");
                        return;
                    }
                    isEmail = true;
                } else {
                    // ??????
                    isEmail = false;
                }

                // ??????
                if (rg_invoice_type.getCheckedRadioButtonId() == R.id.rb_invoice_person) {
                    isPerson = true;
                } else {
                    //??????
                    if (CommonUtils.checkInputData(mInput_invoice_name, "?????????????????????")) return;
                    if (CommonUtils.checkInputData(mInput_invoice_no, "???????????????")) return;
                    if (CommonUtils.checkInputData(mInput_invoice_address, "?????????????????????")) return;
                    if (CommonUtils.checkInputData(mInput_invoice_mobile, "?????????????????????")) return;
                    if (CommonUtils.checkInputData(mInput_invoice_bank, "?????????????????????")) return;
                    if (CommonUtils.checkInputData(mInput_invoice_bank_no, "?????????????????????")) return;
                    isPerson = false;
                }
                saveInvoice();
                hasInvoice = true;
                cbInvoice.setChecked(true);
                tvInvoiceInfo.setText((isEmail ? "??????-" : "??????-") + (isPerson ? "??????" : "??????"));
                mInvoicePopupWindowUtils.dismiss();
            }
        });

        mInvoicePopupWindowUtils = new PopupWindowUtils(this, rootView)
                .setStyle(R.style.popupWindowAsBottom)
                .setLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        ;
    }

    /**
     * ????????????????????????
     */
    private void setInvoiceDefaultData() {
        mInput_email.setEditText((String) SharedPreferencesUtils.get("invoice_email", ""));
        mInput_invoice_name.setEditText((String) SharedPreferencesUtils.get("invoice_name", ""));
        mInput_invoice_no.setEditText((String) SharedPreferencesUtils.get("invoice_no", ""));
        mInput_invoice_address.setEditText((String) SharedPreferencesUtils.get("invoice_address", ""));
        mInput_invoice_mobile.setEditText((String) SharedPreferencesUtils.get("invoice_mobile", ""));
        mInput_invoice_bank.setEditText((String) SharedPreferencesUtils.get("invoice_bank", ""));
        mInput_invoice_bank_no.setEditText((String) SharedPreferencesUtils.get("invoice_bank_no", ""));
    }

    /**
     * ???????????????????????????
     */
    private void saveInvoice() {
        if (!TextUtils.isEmpty(mInput_email.getInputText()))
            SharedPreferencesUtils.put("invoice_email", mInput_email.getInputText());
        if (!TextUtils.isEmpty(mInput_invoice_name.getInputText()))
            SharedPreferencesUtils.put("invoice_name", mInput_invoice_name.getInputText());
        if (!TextUtils.isEmpty(mInput_invoice_no.getInputText()))
            SharedPreferencesUtils.put("invoice_no", mInput_invoice_no.getInputText());
        if (!TextUtils.isEmpty(mInput_invoice_address.getInputText()))
            SharedPreferencesUtils.put("invoice_address", mInput_invoice_address.getInputText());
        if (!TextUtils.isEmpty(mInput_invoice_mobile.getInputText()))
            SharedPreferencesUtils.put("invoice_mobile", mInput_invoice_mobile.getInputText());
        if (!TextUtils.isEmpty(mInput_invoice_bank.getInputText()))
            SharedPreferencesUtils.put("invoice_bank", mInput_invoice_bank.getInputText());
        if (!TextUtils.isEmpty(mInput_invoice_bank_no.getInputText()))
            SharedPreferencesUtils.put("invoice_bank_no", mInput_invoice_bank_no.getInputText());
    }

    /**
     * ??????????????????
     *
     * @param address
     */
    @Subscriber(tag = EventTags.PAY_SELECT_ADDRESS)
    public void selectAddress(ModelAddress address) {
        mModelAddress = address;
        setAddressInfo();
    }

    /**
     * ??????????????????
     */
    private void setAddressInfo() {
        if (mModelAddress == null) {
            return;
        }
        String addressStr = mModelAddress.getProvince() + mModelAddress.getCity() + mModelAddress.getArea() + mModelAddress.getAddress();
        mDefalutRL.setVisibility(View.VISIBLE);
        mselectAddressRL.setVisibility(View.GONE);
        mNotDataAddressRL.setVisibility(View.GONE);
        mName.setText(mModelAddress.getLinkman());
        mPhone.setText(mModelAddress.getMobileNo());
        mDistrict.setText("????????????:" + addressStr);

        getPostage(mModelAddress.getProvince());
    }

    /**
     * ????????????????????????
     */
    public void getPostage(String provinceStr) {
        submitRL.setEnabled(false);
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("areaName", provinceStr);
        new OkHttpCommon().postLoadData(this, ConstantsUrl.URL_ADDRESS_POSTAGE, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("????????????????????????");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "????????????????????????"));
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

    @OnClick({R.id.rl_wechat_pay, R.id.rl_alipay, R.id.rl_yue_pay, R.id.rl_not_data_address,
            R.id.rl_select_address, R.id.rl_default_address,
            R.id.layout_invoice, R.id.submitRL, R.id.couponRL, R.id.view_sel_coupon})
    public void myOnClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.view_sel_coupon:
                if (TextUtils.isEmpty(mSelectedCouponNo)) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isSelect", true);
                    ActivityUtils.getInstance().jumpActivity(MyCouponActivity.class, bundle);
                } else {
                    // ???????????????
                    isCheckedCoupon = !isCheckedCoupon;
                    changeCouponStatus();
                }
                break;
            case R.id.couponRL: // ??????????????????
                Bundle bundle = new Bundle();
                bundle.putBoolean("isSelect", true);
                ActivityUtils.getInstance().jumpActivity(MyCouponActivity.class, bundle);
                break;
            case R.id.rl_yue_pay:   // ????????????
                mYue.setImageResource(R.mipmap.select_circle);
                mWeChat.setImageResource(R.mipmap.noselect_circle);
                mAlipay.setImageResource(R.mipmap.noselect_circle);
                mPayType = 1;
                break;
            case R.id.rl_wechat_pay:
                mYue.setImageResource(R.mipmap.noselect_circle);
                mWeChat.setImageResource(R.mipmap.select_circle);
                mAlipay.setImageResource(R.mipmap.noselect_circle);
                mPayType = 2;
//                setAllMoneyInfo();
                break;
            case R.id.rl_alipay:
                mYue.setImageResource(R.mipmap.noselect_circle);
                mWeChat.setImageResource(R.mipmap.noselect_circle);
                mAlipay.setImageResource(R.mipmap.select_circle);
                mPayType = 3;
//                setAllMoneyInfo();
                break;
            case R.id.rl_not_data_address:
                //????????????????????? ??????
                jumpSelAddress();
                break;
            case R.id.rl_select_address:
                jumpSelAddress();
                break;
            case R.id.rl_default_address:
                jumpSelAddress();
                break;
            case R.id.submitRL:
                // ????????????
                if (new Date().getTime() - oldTime < 2000) {
                    return;
                }
                oldTime = new Date().getTime();

                if (mOrderNo == null || TextUtils.isEmpty(mOrderNo)) {
                    submitOrderPay();
                } else {
                    if (hasSubmit) {
                        return;
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = ConstantHandler.WHAT_SURE_TOPAY_SUCCESS;
                    msg.obj = mOrderNo;
                    mHandler.sendMessage(msg);
                    if (mPayType == 1) { // ?????????????????????
                        return;
                    }
                    hasSubmit = true;
                }
                break;
            case R.id.layout_invoice:// ????????????
                mInvoicePopupWindowUtils.showAtLocation(this, view, Gravity.BOTTOM, 0, 0);
                break;
            default:
                break;
        }
    }

    /**
     * ??????????????????
     */
    private void jumpSelAddress() {
//        Intent intent = new Intent(this, AddressManageActivity.class);
//        intent.putExtra("activity", "PayNowActivity");
//        startActivityForResult(intent, 0x01);
        mOrderNo = "";
        ActivityUtils.getInstance().jumpAddressManager(true);
    }

    /**
     * ???????????????????????????????????????
     */
    private void getCouponNum() {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("used", "0");
        new OkHttpCommon().postLoadData(this, ConstantsUrl.URL_MY_COUPON_LIST, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() == 1) {
                    if (jsonObject.get("data").getAsJsonObject().get("total").getAsInt() == 0) {
                        tv_coupon.setText("?????????(????????????????????????)");
                    } else {
                        tv_coupon.setText("?????????(??????????????????????????????)");
                    }
                }
            }
        });
    }

    private CouponBean selCoupon;

    /**
     * ???????????????
     *
     * @param coupon
     */
    @Subscriber(tag = "selectedCoupon")
    public void selectCouponResult(CouponBean coupon) {
        if (coupon == null) return;
        int money = mTotalMoney;
        if (money < coupon.getAmt().intValue()) {
            ToastUtils.showToast("???????????????????????????????????????!");
            return;
        }
        selCoupon = coupon;
        isCheckedCoupon = true;
        changeCouponStatus();
        mSelectedCouponNo = coupon.getCouponNo();

        tv_coupon.setText("?????????(??" + (coupon.getAmt() / 100) + ")");
    }

    /**
     * ?????????????????????
     */
    private void changeCouponStatus() {
        int money = mTotalMoney;
        money += postageMoney;

//        String postFeeStr;
//        if(postageMoney==0) {
//            postFeeStr = "???????????????";
//        }else{
//            postFeeStr = "????????????" + UnitUtil.getMoney(postageMoney+"") + "???";
//        }

        if (isCheckedCoupon) {
            couponIv.setImageResource(R.mipmap.select_circle);
            money -= selCoupon.getAmt().intValue();
            payMoneyTV.setText(
                    SpannableStringUtils.getBuilder("????????????")
                            .append(UnitUtil.getMoney(String.valueOf(money)))
                            .setForegroundColor(ContextCompat.getColor(this, R.color.c_money))
                            .create()
            );
            payMoneyTV.append("(?????????" + (selCoupon.getAmt() / 100) + "??????");
        } else {
            couponIv.setImageResource(R.mipmap.noselect_circle);
            payMoneyTV.setText(
                    SpannableStringUtils.getBuilder("????????????")
                            .append(UnitUtil.getMoney(String.valueOf(money)))
                            .setForegroundColor(ContextCompat.getColor(this, R.color.c_money))
                            .create());
        }
    }

    public void submitOrderPay() {
        if (mPayType == -1) {
            ToastUtils.showToast("?????????????????????");
            return;
        }
        if (mModelAddress == null) {
            jumpSelAddress();
            ToastUtils.showToast("???????????????");
            return;
        }

        String addressNo = mModelAddress.getAddressNo();
        SimpleDialog.showLoadingHintDialog(this, 4);

        String leaveMsg = "";
        for (ModelShopCarSettle shopCarSettle : mAdapter.mList) {
            leaveMsg = shopCarSettle.getLeaveMsg();
        }
        String param = "";
        for (ModelShopCarSettle shopCarSettle : mAdapter.mList) {
            for (ModelShopCarSettleItem item : shopCarSettle.getModelShopCarSettleItemList()) {
                param += item.getRemark() + ",";
            }
        }

        String emailAddress = mInput_email.getInputText();
        String invoiceName = mInput_invoice_name.getInputText();
        String invoiceNo = mInput_invoice_no.getInputText();
        String invoiceCompanyAddress = mInput_invoice_address.getInputText();
        String invoiceMobile = mInput_invoice_mobile.getInputText();
        String invoiceBank = mInput_invoice_bank.getInputText();
        String invoiceBankNo = mInput_invoice_bank_no.getInputText();
        // ?????????????????????
        String couponNo = isCheckedCoupon ? mSelectedCouponNo : "";

        HttpParameterUtil.getInstance().requestPayNowCreateOrder(mProductNo,
                mProductNum, mProductSetNo, addressNo, leaveMsg, param,
                hasInvoice, isEmail, isPerson, emailAddress, invoiceName,
                invoiceNo, invoiceCompanyAddress, invoiceMobile, invoiceBank,
                invoiceBankNo, couponNo,
                mHandler);
    }

    @SuppressLint("SetTextI18n")
    @SuppressWarnings("unchecked")
    private void disposeData(Message msg) {
        boolean hasDefault = false;
        switch (msg.what) {
            case ConstantHandler.WHAT_MY_WALLET_SUCCESS:
                // ????????????
                mModelWallet = (ModelWallet) msg.obj;
                if (mModelWallet != null) {
                    tvYue.setText("????????????(???????????????" + UnitUtil.getMoney(mModelWallet.getBalance()) + ")");
                }
                break;
            case ConstantHandler.WHAT_PAY_NOW_SUCCESS:
                mList = (LinkedList<ModelShopCarSettle>) msg.obj;
                mAdapter.mList = mList;
                mAdapter.notifyDataSetChanged();
                setTotalMoney();
                setAllMoneyInfo();
                break;
            case ConstantHandler.WHAT_ADDRESS_ALL_SUCCESS:
                // ????????????
                mAddressList = (LinkedList<ModelAddress>) msg.obj;
                if (mAddressList.size() > 0) {
                    //?????????
                    // ??????????????????
                    for (int i = 0; i < mAddressList.size(); i++) {
                        String isdefault = mAddressList.get(i).getIsdefault();
                        if (isdefault.equals(String.valueOf(1))) {
                            hasDefault = true;
                            mModelAddress = mAddressList.get(i);
                            setAddressInfo();
                        }
                    }
                    //?????????????????????????????????
                    if (!hasDefault) {
                        mDefalutRL.setVisibility(View.GONE);
                        mselectAddressRL.setVisibility(View.VISIBLE);
                        mNotDataAddressRL.setVisibility(View.GONE);
                    }
                } else if (mAddressList.size() == 0) {
                    //????????????
                    mDefalutRL.setVisibility(View.GONE);
                    mselectAddressRL.setVisibility(View.GONE);
                    mNotDataAddressRL.setVisibility(View.VISIBLE);
                }
                break;
            case ConstantHandler.WHAT_SURE_TOPAY_SUCCESS:
                mOrderNo = (String) msg.obj;
                SimpleDialog.cancelLoadingHintDialog();

                // 1???????????????2????????????,3?????????
                if (mPayType == 1) {
//                    HttpParameterUtil.getInstance().requestOrderToPay(mOrderNo, "0", mHandler);
                    yuePay();
                } else if (mPayType == 2) {
                    HttpParameterUtil.getInstance().requestWechatPayParams(mOrderNo, "0", mHandler);
                } else {
                    if (null != getIntent().getStringExtra("mVip") && "vip".equals(getIntent().getStringExtra("mVip"))) {
                        //vip??????
                        ActivityUtils.getInstance().toAlipayNativePayVipOrder(this, mOrderNo, "0");
                    } else {
                        ActivityUtils.getInstance().toAlipayNativePayOrder(this, mOrderNo, "0");
                    }
                }
                break;
            case ConstantHandler.WHAT_SURE_TOPAY_FAIL:
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
            case ConstantHandler.WHAT_ORDER_PAY_FAIL:
            case ConstantHandler.WHAT_WECHAT_PAY_PARAMS_FAIL:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast((String) msg.obj);
                if (mPayLoadingDialog != null && mPayLoadingDialog.isShow()) mPayLoadingDialog.dismiss();

                break;
            case ConstantHandler.WHAT_ORDER_PAY_SUCCESS:
                SimpleDialog.cancelLoadingHintDialog();
                if (mPayLoadingDialog != null && mPayLoadingDialog.isShow()) mPayLoadingDialog.dismiss();
                ActivityUtils.getInstance().jumpSubmitResult(1);
                ActivityUtils.getInstance().jumpMainActivity();
                break;

            default:
                break;
        }
    }

    /**
     * ????????????
     */
    private void yuePay() {
        Member member = CommonUtils.getMember();
        if (member == null) return;
        if (mModelWallet == null) return;
        if (UnitUtil.getInt(mModelWallet.getBalance()) / 100.0 <= 0) {
            ToastUtils.showToast("??????????????????????????????????????????");
            return;
        }
        if (!member.isSetPayPwd()) {
            // ??????????????????
            new XPopup.Builder(this)
                    .hasShadowBg(false)
                    .autoOpenSoftInput(true)
                    .asCustom(new SetPayPwdDialog(this))
                    .show();
        } else {
            // ????????????
            String balance = mModelWallet.getBalance();
            long couponAmt = 0;
            if (isCheckedCoupon) {// ?????????
                couponAmt = selCoupon.getAmt();
            }
            if ((UnitUtil.getInt(balance) + couponAmt) < mTotalMoney) {
                ToastUtils.showToast("????????????");
                if (mPayLoadingDialog != null && mPayLoadingDialog.isShow()) mPayLoadingDialog.dismiss();
                return;
            }


            hasSubmit = false;
            new XPopup.Builder(this)
                    .autoOpenSoftInput(false)
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

    private BasePopupView mPayLoadingDialog;

    /**
     * ???????????????
     */
    private void showPayLoading() {
        mPayLoadingDialog = SimpleDialog.createDialog(this, getString(R.string.pay_loading), false, true, true).show();
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

    private static class MyHandler extends Handler {

        private WeakReference<PayNowActivity> mImpl;

        public MyHandler(PayNowActivity mImpl) {
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
