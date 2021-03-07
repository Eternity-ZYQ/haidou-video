package com.yimeng.haidou.shop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yimeng.haidou.R;
import com.yimeng.entity.ModelMallOrder;
import com.yimeng.widget.ExpandListView;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商城订单分页列表
 */
public class OrderListAdapter extends BaseAdapter {

    public LinkedList<ModelMallOrder> mList;
    OrderListGoodsAdapter orderListGoodsAdapter;
    private Context mContext;
    private Handler mHandler;
    private int mWhat;
    private int type;

    public OrderListAdapter(Context mContext, Handler mHandler, LinkedList<ModelMallOrder> mList, int mWhat, int type) {
        this.mContext = mContext;
        this.mHandler = mHandler;
        this.mList = mList;
        this.mWhat = mWhat;
        this.type = type;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_lv_order_list, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ModelMallOrder model = (ModelMallOrder) getItem(i);
        setData(model, holder, type);

        return view;
    }

    private void setData(ModelMallOrder model, ViewHolder holder, int type) {

        orderListGoodsAdapter = new OrderListGoodsAdapter(mContext, mHandler, model, model.getModelMallOrderItemList(), mWhat);
        holder.mListView.setAdapter(orderListGoodsAdapter);
        chechType(holder, type);

        holder.tvSumPrice.setText(model.getRealMoney());

        holder.mCheckDetailTV.setTag(model);
        holder.mCancelOrderTV.setTag(model);
        holder.mEvaluationTV.setTag(model);
        holder.mConfirmGoodsTV.setTag(model);
        holder.mReturnOfGoodsTv.setTag(model);
        holder.tvLookLogistics.setTag(model);
        holder.tvReturnLoading.setTag(model);
    }


    private void chechType(ViewHolder holder, int type) {
        holder.tvLookLogistics.setVisibility(View.GONE);
        holder.mCheckDetailTV.setVisibility(View.GONE);
        holder.mCancelOrderTV.setVisibility(View.GONE);
        holder.mEvaluationTV.setVisibility(View.GONE);
        holder.mConfirmGoodsTV.setVisibility(View.GONE);
        holder.mReturnOfGoodsTv.setVisibility(View.GONE);
        switch (type) {
            case 0:
                holder.mCheckDetailTV.setVisibility(View.VISIBLE);
                break;
            case 1:
                holder.mCheckDetailTV.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.tvLookLogistics.setVisibility(View.VISIBLE);
                holder.mConfirmGoodsTV.setVisibility(View.VISIBLE);
                break;
            case 3:
                holder.mCheckDetailTV.setVisibility(View.VISIBLE);
                holder.mEvaluationTV.setVisibility(View.VISIBLE);
                break;
            case 4:
                holder.mCheckDetailTV.setVisibility(View.VISIBLE);
                break;
            case 5:
                holder.mCheckDetailTV.setVisibility(View.VISIBLE);
                break;
            case 6: // 退货/退款申请
//                holder.tvReturnLoading.setVisibility(View.VISIBLE);
                holder.mCheckDetailTV.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

    }


    class ViewHolder {

        @Bind(R.id.tv_look_logistics)
        TextView tvLookLogistics;
        @Bind(R.id.tv_check_detail)
        TextView mCheckDetailTV; //查看详情
        @Bind(R.id.tv_cancel_order)
        TextView mCancelOrderTV;//取消订单
        @Bind(R.id.tv_evaluation)
        TextView mEvaluationTV;//评价
        @Bind(R.id.tv_confirm_goods)
        TextView mConfirmGoodsTV;//确认收货
        @Bind(R.id.tv_return_of_goods)
        TextView mReturnOfGoodsTv;//退货
        @Bind(R.id.lv_mall_order)
        ExpandListView mListView;
        @Bind(R.id.tv_sum_price)
        TextView tvSumPrice;
        @Bind(R.id.tv_price_income)
        TextView tvPriceIncome;
        @Bind(R.id.tv_return_loading)
        TextView tvReturnLoading; // 退款进度

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.tv_check_detail, R.id.tv_cancel_order, R.id.tv_evaluation, R.id.tv_confirm_goods,
                R.id.tv_return_of_goods, R.id.tv_look_logistics})
        public void myOnClick(View view) {
            Message msg = mHandler.obtainMessage();
            msg.what = mWhat;
            msg.arg1 = view.getId();
            msg.obj = view.getTag();
            mHandler.sendMessage(msg);
        }
    }
}