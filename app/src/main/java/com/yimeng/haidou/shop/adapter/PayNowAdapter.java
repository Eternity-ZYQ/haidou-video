package com.yimeng.haidou.shop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.yimeng.entity.ModelShopCarSettle;
import com.yimeng.haidou.R;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.ExpandListView;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 立即购买,购物车结算(店铺，订单)
 */
public class PayNowAdapter extends BaseAdapter {

    public LinkedList<ModelShopCarSettle> mList;
    private Context mContext;
    private Handler mHandler;
    private int mWhat;
    private PayNowItemAdapter mAdapter;

    public PayNowAdapter(LinkedList<ModelShopCarSettle> mList, Context mContext, Handler mHandler, int mWhat) {
        this.mList = mList;
        this.mContext = mContext;
        this.mHandler = mHandler;
        this.mWhat = mWhat;
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_lv_goods_in_the_shop, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ModelShopCarSettle model = (ModelShopCarSettle) getItem(i);
        setData(model, holder);
        return view;
    }

    private void setData(final ModelShopCarSettle model, final PayNowAdapter.ViewHolder holder) {
        holder.shop_nameTV.setText(model.getShopName());
        holder.freightTV.setText("运费"+ (UnitUtil.getInt(model.getLogisticsFee())*0.01) + "元");
        holder.buyerMessageET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                model.setLeaveMsg(holder.buyerMessageET.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mAdapter = new PayNowItemAdapter(model.getModelShopCarSettleItemList(), mContext, mHandler, 19575);
        holder.mListView.setAdapter(mAdapter);
    }

    class ViewHolder {
        @Bind(R.id.tv_shop_name)
        TextView shop_nameTV;
        @Bind(R.id.lv_order_detail)
        ExpandListView mListView;
        @Bind(R.id.freightTV)
        TextView freightTV;
        @Bind(R.id.buyerMessageET)
        EditText buyerMessageET;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
