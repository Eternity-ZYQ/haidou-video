package com.yimeng.haidou.order.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yimeng.haidou.R;
import com.yimeng.entity.ModelShopOrderListItem;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.UnitUtil;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的订单-店铺订单二级adapter
 */

public class OrederGoodsDetailAdapter extends BaseAdapter {

    public LinkedList<ModelShopOrderListItem> mList;
    private Context mContext;
    private Handler mHandler;
    private int mWhat;

    public OrederGoodsDetailAdapter(LinkedList<ModelShopOrderListItem> mList, Context mContext, Handler mHandler, int mWhat) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_order_shop_goods, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ModelShopOrderListItem model = (ModelShopOrderListItem) getItem(i);
        setData(model, holder);

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setData(ModelShopOrderListItem model, ViewHolder holder) {
        CommonUtils.showImage(holder.mSDV, model.getProductImg());
        holder.goodsNameTV.setText(model.getProductName());
        holder.paramTV.setText(model.getProductColorSize());
        holder.moneyTV.setText(UnitUtil.getMoney(model.getOriginalPrice(), false));
        holder.countTV.setText("x"+model.getProductNum());
        holder.clickRL.setTag(model);
    }

    class ViewHolder {

        @Bind(R.id.sdv_image)
        ImageView mSDV;
        @Bind(R.id.goodsName)
        TextView goodsNameTV;
        @Bind(R.id.goodsDesc)
        TextView paramTV;
        @Bind(R.id.tv_money)
        TextView moneyTV;
        @Bind(R.id.tv_count)
        TextView countTV;
        @Bind(R.id.clickRL)
        RelativeLayout clickRL;

        @OnClick({R.id.clickRL,})
        public void myOnClick(View view) {
            Message msg = mHandler.obtainMessage();
            msg.what = mWhat;
            msg.arg1 = view.getId();
            msg.obj = view.getTag();
            mHandler.sendMessage(msg);
        }

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
