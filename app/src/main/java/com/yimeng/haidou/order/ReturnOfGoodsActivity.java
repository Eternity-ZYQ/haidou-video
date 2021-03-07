package com.yimeng.haidou.order;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yimeng.base.BaseTakePhotoActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.entity.Member;
import com.yimeng.entity.ReturnOfGoodsApplyModel;
import com.yimeng.entity.ReturnOfGoodsStatus;
import com.yimeng.entity.SendBackConfirmStatus;
import com.yimeng.interfaces.UploadImageCallBack;
import com.yimeng.net.NetComment;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.haidou.R;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.widget.MyToolBar;
import com.huige.library.utils.ToastUtils;

import org.devio.takephoto.model.TResult;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.OnClick;

public class ReturnOfGoodsActivity extends BaseTakePhotoActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    // 退货
    @Bind(R.id.return_of_goods_ll)
    LinearLayout return_of_goods_ll;
    @Bind(R.id.return_of_goods_reason_et)
    EditText return_of_goods_reason_et;
    @Bind(R.id.return_of_goods_name_et)
    EditText return_of_goods_name_et;
    @Bind(R.id.return_of_goods_phone_et)
    EditText return_of_goods_phone_et;
    @Bind(R.id.sdv_1)
    ImageView photo_one_ipv;
    @Bind(R.id.sdv_2)
    ImageView photo_two_ipv;
    @Bind(R.id.sdv_3)
    ImageView photo_there_ipv;
    // 退货审核中
    @Bind(R.id.return_of_goods_verify_ll)
    LinearLayout return_of_goods_verify_ll;
    // 退货驳回
    @Bind(R.id.return_of_goods_reject_ll)
    LinearLayout return_of_goods_reject_ll;
    @Bind(R.id.reject_reason_tv)
    TextView reject_reason_tv;
    // 回邮确认
    @Bind(R.id.return_of_goods_send_back_ll)
    LinearLayout return_of_goods_send_back_ll;
    @Bind(R.id.return_of_goods_goods_name_tv)
    TextView return_of_goods_goods_name_tv;
    @Bind(R.id.return_of_goods_receiver_name_tv)
    TextView return_of_goods_receiver_name_tv;
    @Bind(R.id.return_of_goods_receiver_phone_tv)
    TextView return_of_goods_receiver_phone_tv;
    @Bind(R.id.return_of_goods_receiver_address_tv)
    TextView return_of_goods_receiver_address_tv;
    @Bind(R.id.return_of_goods_flow_company_et)
    EditText return_of_goods_flow_company_et;
    @Bind(R.id.return_of_goods_tracking_number_et)
    EditText return_of_goods_tracking_number_et;
    // 回邮成功，查看物流
    @Bind(R.id.return_of_goods_check_tracking_ll)
    LinearLayout return_of_goods_check_tracking_ll;
    @Bind(R.id.layout_images)
    LinearLayout layout_images;
    @Bind(R.id.tv_return_type)
    TextView tv_return_type;
    @Bind(R.id.tv_return_title)
    TextView tv_return_title;
    @Bind(R.id.return_of_goods_submit_btn)
    Button returnOfGoodsSubmitBtn;

    private Context mContext;
    private MyHandler mHandler;
    private String orderNo;
    private ReturnOfGoodsStatus mReturnOfGoodsStatus;
    private String photo_one_url;
    private String photo_two_url;
    private String photo_there_url;
    private String goodsName;
    private String name;
    private String phone;
    private String address;
    private String pinyin;

    /**
     * 照片类型
     */
    private int mPhotoType;
    /**
     * 1. 退货
     * 2. 退款
     */
    private int mType;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_return_of_goods;
    }

    @Override
    protected void init() {
        mContext = ReturnOfGoodsActivity.this;

        mHandler = new MyHandler(ReturnOfGoodsActivity.this);

        mType = getIntent().getIntExtra("type", 1);
        orderNo = getIntent().getStringExtra("orderNo");
        goodsName = getIntent().getStringExtra("goodsName");
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        address = getIntent().getStringExtra("address");
        mReturnOfGoodsStatus = (ReturnOfGoodsStatus) getIntent().getSerializableExtra("ReturnOfGoodsStatus");
        changeDataView();
        // 查询状态
//        HttpParameterUtil.getInstance().queryStateOfReturn(mType, orderNo, mHandler);
        if (mType == 1) {
            toolBar.setTitle("退货");
        } else {
            toolBar.setTitle("退款");
            toolBar.setRightTextVisible(View.GONE);
        }
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick() {
            @Override
            public void onRightClick() {
                ActivityUtils.getInstance().jumpActivity(ReturnFlowChartActivity.class);
            }
        });
    }

    @Override
    protected void loadData() {

    }

    private void handleMsg(Message msg) {

        switch (msg.what) {
            case ConstantHandler.WHAT_QUERY_STATE_OF_RETURN_SUCCESS:
                mReturnOfGoodsStatus = (ReturnOfGoodsStatus) msg.obj;
                changeDataView();
                break;
            case ConstantHandler.WHAT_QUERY_STATE_OF_RETURN_FAIL:
                ToastUtils.showToast((String) msg.obj);
                break;
            case ConstantHandler.WHAT_RETURN_OF_GOODS_SUCCESS:
                // 退货申请成功
                ReturnOfGoodsApplyModel goodsApplyModel = (ReturnOfGoodsApplyModel) msg.obj;
                ToastUtils.showToast(goodsApplyModel.getMsg());
                // 查询状态后自动刷新页面
                HttpParameterUtil.getInstance().queryStateOfReturn(mType, orderNo, mHandler);
                break;
            case ConstantHandler.WHAT_RETURN_OF_GOODS_FAIL:
                ToastUtils.showToast((String) msg.obj);
                break;
            case ConstantHandler.WHAT_RETURN_SEND_BACK_CONFIRM_SUCCESS:
                // 确认回邮成功
                SendBackConfirmStatus sendBackConfirmStatus = (SendBackConfirmStatus) msg.obj;
                ToastUtils.showToast(sendBackConfirmStatus.getMsg());
                // 查询状态后自动刷新页面
                HttpParameterUtil.getInstance().queryStateOfReturn(mType, orderNo, mHandler);
                break;
            case ConstantHandler.WHAT_RETURN_SEND_BACK_CONFIRM_FAIL:
                ToastUtils.showToast((String) msg.obj);
                break;
            case ConstantHandler.WHAT_RETURN_OF_MONEY_SUCCESS:
                // 退款成功
                ToastUtils.showToast("退款申请提交成功！");
                finish();
                break;
            case ConstantHandler.WHAT_RETURN_OF_MONEY_FAIL:
                // 退款失败
                String str = (String) msg.obj;
                ToastUtils.showToast(TextUtils.isEmpty(str) ? "退款申请提交失败！" : str);
                break;
        }

    }

    private void changeDataView() {
        return_of_goods_ll.setVisibility(View.GONE);
        return_of_goods_verify_ll.setVisibility(View.GONE);
        return_of_goods_send_back_ll.setVisibility(View.GONE);
        return_of_goods_check_tracking_ll.setVisibility(View.GONE);
        return_of_goods_reject_ll.setVisibility(View.GONE);
        switch (mReturnOfGoodsStatus.getData().getRefundState()) {
            case "refund_defult":// 未发起退货
                return_of_goods_ll.setVisibility(View.VISIBLE);
                Member member = CommonUtils.getMember();
                if (member != null) {
                    return_of_goods_name_et.setText(member.getMemberName());
                    return_of_goods_phone_et.setText(member.getMobileNo());
                }

                if (mType == 1) {
                    // 退货
                    returnOfGoodsInit();
                    tv_return_type.setText("退货服务");
                    returnOfGoodsSubmitBtn.setText("申请退货");
                    return_of_goods_reason_et.setHint("填写退货原因，限150字");
                } else {
                    // 退款
                    tv_return_title.setVisibility(View.GONE);
                    layout_images.setVisibility(View.GONE);
                    tv_return_type.setText("退款服务");
                    returnOfGoodsSubmitBtn.setText("申请退款");
                    return_of_goods_reason_et.setHint("填写退款原因，限150字");
                }

                break;
            case "refund":// 申请中
                toolBar.setRightTextVisible(View.GONE);
                return_of_goods_verify_ll.setVisibility(View.VISIBLE);
                break;
            case "refund_complete":// 申请成功,待回邮
                toolBar.setRightTextVisible(View.GONE);
                return_of_goods_send_back_ll.setVisibility(View.VISIBLE);
                return_of_goods_goods_name_tv.setText(goodsName);
                return_of_goods_receiver_name_tv.setText("收货人：" + name);
                return_of_goods_receiver_phone_tv.setText("联系电话：" + phone);
                return_of_goods_receiver_address_tv.setText("收货地址：" + address);
                break;
            case "refund_express":// 已回邮
                toolBar.setRightTextVisible(View.GONE);
                return_of_goods_check_tracking_ll.setVisibility(View.VISIBLE);
                break;
            case "refund_refuse":// 驳回
                toolBar.setRightTextVisible(View.GONE);
                return_of_goods_reject_ll.setVisibility(View.VISIBLE);
                reject_reason_tv.setText(mReturnOfGoodsStatus.getData().getRemark());
                break;
            case "refunding":
                ToastUtils.showToast("退款中，请留意消息中心！");
                finish();
                break;
            case "refund_success":
                ToastUtils.showToast("退货退款完成");
                finish();
                break;
        }
    }

    private void returnOfGoodsInit() {

        photo_one_ipv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoType = 1;
                showSelPopupWind(v, 1);
            }
        });

        photo_two_ipv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoType = 2;
                showSelPopupWind(v, 1);
            }
        });

        photo_there_ipv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoType = 3;
                showSelPopupWind(v, 1);
            }
        });
    }

    @OnClick({R.id.return_of_goods_submit_btn, R.id.return_of_goods_flow_company_rl,
            R.id.return_of_goods_send_back_btn, R.id.return_of_goods_tracking_btn})
    public void myOnclick(View view) {
        switch (view.getId()) {
            case R.id.return_of_goods_submit_btn:
                // 申请退货
                returnSubmit();
                break;
            case R.id.return_of_goods_flow_company_rl:
                // 选择物流公司
                Intent intent1 = new Intent(mContext, ChooseFlowActivity.class);
                startActivityForResult(intent1, 0x01);
                break;
            case R.id.return_of_goods_send_back_btn:
                // 确认回邮
                sendBackConfirm();
                break;
            case R.id.return_of_goods_tracking_btn:
                //   查看物流
                if (mReturnOfGoodsStatus.getData() == null) return;
                String url = ConstantsUrl.LOGISTICS_URL_HEADER + "type=" + mReturnOfGoodsStatus.getData().getLogisticsType() + "&postid=" + mReturnOfGoodsStatus.getData().getLogisticsNo();
//                intent = new Intent(mContext, LogisticsWebActivity.class);
//                intent.putExtra("title", getString(R.string.logistics_detail));
//                intent.putExtra("url", url);
//                startActivity(intent);
                ActivityUtils.getInstance().jumpH5Activity("物流详情", url);
                break;
        }
    }

    /**
     * 申请退货
     */
    private void returnSubmit() {

        if (return_of_goods_reason_et.getText().toString().isEmpty()) {
            ToastUtils.showToast("请填写退换原因");
            return;
        }
        if (return_of_goods_name_et.getText().toString().isEmpty()) {
            ToastUtils.showToast("请填写姓名");
            return;
        }
        if (return_of_goods_phone_et.getText().toString().isEmpty()) {
            ToastUtils.showToast("请填写手机号码");
            return;
        }
        if (mType == 1) {
            // 退货
            if (photo_one_url == null && photo_two_url == null && photo_there_url == null) {
                ToastUtils.showToast("请上传退换图片");
                return;
            }
            StringBuilder imgUrl = new StringBuilder();
            if (photo_one_url != null) {
                imgUrl = imgUrl.append(photo_one_url);
            }

            if (photo_two_url != null) {
                imgUrl = imgUrl.append(",").append(photo_two_url);
            }

            if (photo_there_url != null) {
                imgUrl = imgUrl.append(",").append(photo_there_url);
            }
            HttpParameterUtil.getInstance().returnOfGoodsApply(orderNo,
                    return_of_goods_name_et.getText().toString(),
                    return_of_goods_phone_et.getText().toString(),
                    return_of_goods_reason_et.getText().toString(),
                    imgUrl.toString(),
                    mHandler);
        } else {
            // 退款
            HttpParameterUtil.getInstance().returnOfMoneyApply(
                    orderNo, return_of_goods_name_et.getText().toString(),
                    return_of_goods_phone_et.getText().toString(),
                    return_of_goods_reason_et.getText().toString(), mHandler
            );

        }


    }

    /**
     * 确认回邮
     */
    private void sendBackConfirm() {

        if (return_of_goods_flow_company_et.getText().toString().isEmpty()) {
            ToastUtils.showToast("请选择物流类型");
            return;
        }
        if (return_of_goods_tracking_number_et.getText().toString().trim().isEmpty()) {
            ToastUtils.showToast("请填写物流单号");
            return;
        }

        HttpParameterUtil.getInstance().returnSendBackConfirm(orderNo, pinyin,
                return_of_goods_tracking_number_et.getText().toString().trim(), mHandler);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {

            switch (requestCode) {
                case 0x01:
                    return_of_goods_flow_company_et.setText(data.getStringExtra("name"));
                    pinyin = data.getStringExtra("pinyin");
                    break;
            }
        }
    }

    @Override
    public void takeSuccess(TResult tResult) {
        super.takeSuccess(tResult);
        String pathUrl = getTakeSuccessPath(tResult);
        NetComment.uploadPic(ReturnOfGoodsActivity.this, pathUrl, new UploadImageCallBack() {
            @Override
            public void uploadSuccess(String url) {
                if (mPhotoType == 1) {
                    photo_one_url = url;
                    CommonUtils.showImage(photo_one_ipv, url);
                } else if (mPhotoType == 2) {
                    photo_two_url = url;
                    CommonUtils.showImage(photo_two_ipv, url);
                } else {
                    photo_there_url = url;
                    CommonUtils.showImage(photo_there_ipv, url);
                }
            }

            @Override
            public void uploadFail(String msg) {
                ToastUtils.showToast("上传失败");
            }
        });
    }

    private static class MyHandler extends Handler {

        private WeakReference<ReturnOfGoodsActivity> mImpl;

        public MyHandler(ReturnOfGoodsActivity activity) {
            this.mImpl = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mImpl.get() != null) {
                mImpl.get().handleMsg(msg);
            }
        }
    }

}
