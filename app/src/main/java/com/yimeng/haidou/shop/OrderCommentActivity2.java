package com.yimeng.haidou.shop;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.base.BaseTakePhotoActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.haidou.R;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.ModelMallOrder;
import com.yimeng.entity.ModelMallOrderItem;
import com.yimeng.entity.ModelShopOrderList;
import com.yimeng.entity.ModelShopOrderListItem;
import com.yimeng.entity.OrderCommentsBean;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.CommonUtils;
import com.yimeng.widget.MyToolBar;
import com.yimeng.widget.RatingStat.RatingStarView;
import com.huige.library.utils.ToastUtils;

import org.devio.takephoto.model.TResult;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/11/19 0019 下午 02:22.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 评价（订单评价）
 * </pre>
 */
public class OrderCommentActivity2 extends BaseTakePhotoActivity {


    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    /**
     * 订单类型  shop 店铺订单，mall 商城订单，repair 维修
     */
    private String mOrderType;
    private MyHandler mHandler = new MyHandler(this);
    private List<OrderCommentsBean> mList;
    private BaseQuickAdapter<OrderCommentsBean, BaseViewHolder> mAdapter;
    private int clickPosition = -1;
    private int clickSDVPosition = -1;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_order_comment2;
    }

    @Override
    protected void init() {
        mOrderType = getIntent().getStringExtra("mOrderType");

        mList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BaseQuickAdapter<OrderCommentsBean, BaseViewHolder>(
                R.layout.adapter_order_comments_item, mList
        ) {
            @Override
            protected void convert(BaseViewHolder helper, final OrderCommentsBean item) {
                CommonUtils.showImage(helper.getView(R.id.sdvLogo), item.getProductImg());
                helper.setText(R.id.tv_product_name, item.getProductName());

                final RatingStarView ratingStarView = helper.getView(R.id.gradeRSV);
                ratingStarView.setRating(item.getStart());
//                ratingStarView.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        int viewWidth = v.getWidth();
//                        int clickWidth = (int) event.getX();
//
//                        float start;
//                        if (clickWidth < viewWidth / 5) {
//                            start = 1.0f;
//                        } else if (clickWidth < viewWidth / 5 * 2) {
//                            start = 2.0f;
//                        } else if (clickWidth < viewWidth / 5 * 3) {
//                            start = 3.0f;
//                        } else if (clickWidth < viewWidth / 5 * 4) {
//                            start = 4.0f;
//                        } else {
//                            start = 5.0f;
//                        }
//                        ratingStarView.setRating(start);
//
//                        return false;
//                    }
//                });

                ratingStarView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("msg", "OrderCommentActivity2 -> onClick: " + ratingStarView.getRating());
                        int start = (int) ratingStarView.getRating();
                        if (start == 0) {
                            start = 1;
                        }
                        ratingStarView.setRating(start);
                        item.setStart(start);
                    }
                });


                final TextView describeET = helper.getView(R.id.describeET);
                describeET.setText(item.getCommentStr());
                describeET.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        item.setCommentStr(s.toString());
                    }
                });

                ImageView sdv1 = helper.getView(R.id.sdv_1);
                String commentPic1 = item.getCommentPic1();
                if (TextUtils.isEmpty(commentPic1)) {
                    sdv1.setImageResource(R.mipmap.btn_addpicture);
                } else {
                    CommonUtils.showImage(sdv1, commentPic1);
                }

                ImageView sdv2 = helper.getView(R.id.sdv_2);
                String commentPic2 = item.getCommentPic2();
                if (TextUtils.isEmpty(commentPic2)) {
                    sdv2.setImageResource(R.mipmap.btn_addpicture);
                } else {
                    CommonUtils.showImage(sdv2, commentPic2);
                }

                ImageView sdv3 = helper.getView(R.id.sdv_3);
                String commentPic3 = item.getCommentPic3();
                if (TextUtils.isEmpty(commentPic3)) {
                    sdv3.setImageResource(R.mipmap.btn_addpicture);
                } else {
                    CommonUtils.showImage(sdv3, commentPic3);
                }

                helper
                        .addOnClickListener(R.id.sdv_1)
                        .addOnClickListener(R.id.sdv_2)
                        .addOnClickListener(R.id.sdv_3)
                ;
            }
        };
        recyclerView.setAdapter(mAdapter);

        parseData();
    }

    /**
     * 格式化数据
     */
    private void parseData() {
        if (TextUtils.isEmpty(mOrderType)) return;
        if (mList == null) return;
        if (!mList.isEmpty()) mList.clear();

        if (mOrderType.equals("mall")) {
            // 商城订单
            ModelMallOrder modelMallOrder = (ModelMallOrder) getIntent().getSerializableExtra("mModelMallOrder");
            for (ModelMallOrderItem modelMallOrderItem : modelMallOrder.getModelMallOrderItemList()) {
                mList.add(new OrderCommentsBean(
                        modelMallOrderItem.getOrderNo(),
                        modelMallOrderItem.getProductImg(),
                        modelMallOrderItem.getProductName(),
                        modelMallOrderItem.getProductNo()
                ));
            }
        }else if(mOrderType.equals("shop")) {
            ModelShopOrderList modelMallOrder = (ModelShopOrderList) getIntent().getSerializableExtra("mModelMallOrder");
            for (ModelShopOrderListItem modelMallOrderItem : modelMallOrder.getModelShopOrderListItemsList()) {
                mList.add(new OrderCommentsBean(
                        modelMallOrderItem.getOrderNo(),
                        modelMallOrderItem.getProductImg(),
                        modelMallOrderItem.getProductName(),
                        modelMallOrderItem.getProductNo()
                ));
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initListener() {

        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                clickPosition = position;
                if (view.getId() == R.id.sdv_1) {
                    clickSDVPosition = 1;
                } else if (view.getId() == R.id.sdv_2) {
                    clickSDVPosition = 2;
                } else if (view.getId() == R.id.sdv_3) {
                    clickSDVPosition = 3;
                }

                showSelPopupWind(view, 1);

            }
        });
    }

    @Override
    protected void loadData() {

    }


    /**
     * 提交
     */
    @OnClick(R.id.submitBTN)
    public void submitComment() {

        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for (OrderCommentsBean commentsBean : mList) {
            sb.append(commentsBean.toString() + ",");
        }
        sb.append("]");

        Log.d("msg", "OrderCommentActivity2 -> submitComment: " + sb.toString());

        SimpleDialog.showLoadingHintDialog(this, 4);
        // shop 店铺订单，mall 商城订单，repair 维修, warrantyRepair 全保维修
        switch (mOrderType) {
            case "shop":
                HttpParameterUtil.getInstance().requestShopComments(mList.get(0).getOrderNo(), sb.toString(), mHandler);
                break;
            case "mall":
                HttpParameterUtil.getInstance().requestMallComments(mList.get(0).getOrderNo(), sb.toString(), mHandler);
                break;
            default:
                break;
        }


    }

    @SuppressWarnings("unchecked")
    private void disposeData(Message msg) {

        switch (msg.what) {
            case ConstantHandler.WHAT_ORDER_COMMENT_SUCCESS:
                SimpleDialog.cancelLoadingHintDialog();
                startActivity(new Intent(this, OrderCommentResultActivity.class));
                setResult(RESULT_OK, new Intent());
                finish();
                break;
            case ConstantHandler.WHAT_ORDER_COMMENT_FAIL:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast((String) msg.obj);
                break;
            case ConstantHandler.WHAT_IMAGE_UPLOAD_SUCCESS: // 图片上传成功
                SimpleDialog.cancelLoadingHintDialog();
                String url = (String) msg.obj;
                if (mList == null || mAdapter == null) return;
                OrderCommentsBean commentsBean = mList.get(clickPosition);
                if (clickSDVPosition == 1) {
                    commentsBean.setCommentPic1(url);
                } else if (clickSDVPosition == 2) {
                    commentsBean.setCommentPic2(url);
                } else if (clickSDVPosition == 3) {
                    commentsBean.setCommentPic3(url);
                }
                mAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_IMAGE_UPLOAD_FAIL:
                SimpleDialog.cancelLoadingHintDialog();
                SimpleDialog.showSimpleRemarkWithTitleDialog(this, "图片上传失败", "请重新上传");
                break;
            default:
                break;
        }
    }

    @Override
    public void takeSuccess(TResult tResult) {
        super.takeSuccess(tResult);
        String path = getTakeSuccessPath(tResult);
        String pic = CommonUtils.imageToBase64Binary(path);
        SimpleDialog.showLoadingHintDialog(OrderCommentActivity2.this, 4);
        HttpParameterUtil.getInstance().requestImageUpload(pic, mHandler);
    }

    private static class MyHandler extends Handler {

        private WeakReference<OrderCommentActivity2> mImpl;

        public MyHandler(OrderCommentActivity2 mImpl) {
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
