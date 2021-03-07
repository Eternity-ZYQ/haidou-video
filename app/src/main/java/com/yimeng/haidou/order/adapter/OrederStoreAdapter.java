package com.yimeng.haidou.order.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yimeng.haidou.R;
import com.yimeng.entity.ModelShopOrderList;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.ExpandListView;
import com.huige.library.utils.DeviceUtils;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的订单-店铺订单
 */

public class OrederStoreAdapter extends BaseAdapter {

    public LinkedList<ModelShopOrderList> mList;
    private Context mContext;
    private Handler mHandler;
    private int mWhat;
    /**
     * pay 待发货
     * noReceiving 待收货
     * nocomment 待评价
     * complete 已完成
     */
    private String type;

    public OrederStoreAdapter(Context mContext, Handler mHandler, LinkedList<ModelShopOrderList> mList, int mWhat, String type) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_lv_order_store, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ModelShopOrderList model = (ModelShopOrderList) getItem(i);
        setData(model, holder);

        return view;
    }

    private void setData(ModelShopOrderList model, ViewHolder holder) {

        CommonUtils.showRadiusImage(holder.shopSDV, model.getShopLogoPath(), DeviceUtils.dp2px(mContext, 5),
                true, true, true, true);
        holder.shopNameTV.setText(model.getShopName());
//        float total = 0;
//        for (int i = 0; i < model.getModelShopOrderListItemsList().size(); i++) {
//            ModelShopOrderListItem item = model.getModelShopOrderListItemsList().get(i);
//            String originalPrice = item.getOriginalPrice();
//            String num = item.getProductNum();
//            total += UnitUtil.getFloat(originalPrice) * UnitUtil.getInt(num);
//        }
        holder.totalPriceTV.setText(UnitUtil.getMoney(model.getRealMoney()));
//        String refund = TextUtils.isEmpty(model.getScore()) ? "0" : UnitUtil.getMoney(model.getScore(), false);
//        holder.backMoneyTV.setText("(返" +  refund +"元)");

        OrederGoodsListAdapter adapter = new OrederGoodsListAdapter(
                model.getModelShopOrderListItemsList(), mContext, mHandler, mWhat, model);
        holder.goodlist.setAdapter(adapter);

//        if (type.equals("income") && model.getOrderStatus().equals("ensure")) {
//            holder.commentBTN.setVisibility(View.VISIBLE);
//            holder.payBTN.setVisibility(View.GONE);
//        } else if (type.equals("nopay")) {
//            holder.commentBTN.setVisibility(View.GONE);
//            holder.payBTN.setVisibility(View.VISIBLE);
//        } else if (type.equals("complete")) {
//            holder.commentBTN.setVisibility(View.GONE);
//            holder.payBTN.setVisibility(View.GONE);
//        } else if (type.equals("pay") && !TextUtils.isEmpty(model.getSendInfo())) {
//            // 待取件
//            // 物流单pay状态, 可取消订单
//            holder.btnCancel.setVisibility(View.VISIBLE);
//        } else if (type.equals("ensure")) {
//            // 待评价
//            holder.commentBTN.setVisibility(View.VISIBLE);
//        }


        if(type.equals("pay")
//                && !TextUtils.isEmpty(model.getReceiver()) // 没有收货地址
                ){
            // 待发货-催单
            holder.commentBTN.setVisibility(View.VISIBLE);
            holder.commentBTN.setText("催单");
            // 不为空，表示已经催过单
            holder.commentBTN.setEnabled(TextUtils.isEmpty(model.getInvoiceName()));

            if(model.getIsPlatno().equals("1")) {
                // 厂促商品，显示退款按钮
                holder.refundBTN.setVisibility(View.VISIBLE);
            }
        }else if(type.equals("noReceiving")) {
            // 待收货
            holder.payBTN.setVisibility(View.VISIBLE);
            holder.payBTN.setText("查看物流");
            holder.commentBTN.setVisibility(View.VISIBLE);
            holder.commentBTN.setText("确认收货");
        }else if(type.equals("nocomment")) {
            // 待评价
            holder.commentBTN.setText("立即评价");
            holder.commentBTN.setVisibility(View.VISIBLE);
        }else if (model.getOrderStatus().equals("complete")) {
            holder.commentBTN.setVisibility(View.GONE);
        }else{
            holder.payBTN.setVisibility(View.GONE);
            holder.commentBTN.setVisibility(View.GONE);
        }

        holder.clickLL.setTag(model);
        holder.commentBTN.setTag(model);
        holder.refundBTN.setTag(model);
        holder.payBTN.setTag(model);
        holder.btnCancel.setTag(model);
    }

    class ViewHolder {
        @Bind(R.id.shopSDV)
        ImageView shopSDV;
        @Bind(R.id.shopNameTV)
        TextView shopNameTV;
        @Bind(R.id.order_goods_list)
        ExpandListView goodlist;
        @Bind(R.id.totalPriceTV)
        TextView totalPriceTV;
        @Bind(R.id.backMoneyTV)
        TextView backMoneyTV;
        @Bind(R.id.refundBTN)
        Button refundBTN;
        @Bind(R.id.payBTN)
        Button payBTN;
        @Bind(R.id.commentBTN)
        Button commentBTN;
        @Bind(R.id.btn_cancel)
        Button btnCancel;
        @Bind(R.id.clickLL)
        LinearLayout clickLL;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.clickLL, R.id.refundBTN, R.id.payBTN, R.id.commentBTN, R.id.btn_cancel})
        public void myOnClick(View view) {
            Message msg = mHandler.obtainMessage();
            msg.what = mWhat;
            msg.arg1 = view.getId();
            msg.obj = view.getTag();
            mHandler.sendMessage(msg);
        }
    }
}
