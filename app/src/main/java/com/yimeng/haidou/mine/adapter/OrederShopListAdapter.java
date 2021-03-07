package com.yimeng.haidou.mine.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yimeng.haidou.R;
import com.yimeng.entity.ShopOrderModel;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.UnitUtil;
import com.yimeng.widget.ExpandListView;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商家订单列表
 *
 * @author lijb
 */

public class OrederShopListAdapter extends BaseAdapter {

    public LinkedList<ShopOrderModel> mList;
    private Context mContext;
    private Handler mHandler;
    private String mType;// new(新订单)，complete（已完成）
    private int mWhat;

    public OrederShopListAdapter(Context mContext, Handler mHandler, LinkedList<ShopOrderModel> mList, String mType, int mWhat) {
        this.mContext = mContext;
        this.mHandler = mHandler;
        this.mList = mList;
        this.mWhat = mWhat;
        this.mType = mType;
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_order_shop, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ShopOrderModel model = (ShopOrderModel) getItem(i);
        setData(model, holder);

        return view;
    }

    private void setData(ShopOrderModel model, ViewHolder holder) {

        CommonUtils.showImage(holder.order_icon, model.getMember_headPath());
        holder.nameTV.setText(model.getMember_memberName());
        holder.phoneTV.setText(model.getMember_mobileNo());


        OrderGoodsAdapter adapter = new OrderGoodsAdapter(mContext, mHandler, model.getIsPlatno(), model.getGoodsModels(), model, mWhat);
        holder.goodlist.setAdapter(adapter);
        holder.totalPriceTV.setText(UnitUtil.getMoney(model.getDueAmt()));
//        holder.returnMoneyTV.setText("(返" + UnitUtil.getMoney(model.getScore(), false) + "元)");
        // 送货上门
//        holder.can_send_flag_iv.setVisibility(model.getIsDelivery() == 1 ? View.VISIBLE : View.GONE);
        
        if(mType.equals("new")) {
            // 平台商品
            if(model.getIsPlatno().equals("1")) {
                holder.sureOrderTV.setVisibility(View.GONE);
            } else {
                holder.sureOrderTV.setVisibility(View.VISIBLE);
                if(TextUtils.isEmpty(model.getReceiver())) {
                    holder.sureOrderTV.setText("确认订单");
                }else {
                    holder.sureOrderTV.setText("立即发货");
                }
            }
        }else if(mType.equals("noReceiving")) {
            holder.sureOrderTV.setText("查看物流");
        }else{
            holder.sureOrderTV.setVisibility(View.GONE);
        }



        holder.clickLL.setTag(model);
        holder.sureOrderTV.setTag(model);
    }

    class ViewHolder {
        @Bind(R.id.order_icon)
        ImageView order_icon;
        @Bind(R.id.nameTV)
        TextView nameTV;
        @Bind(R.id.phoneTV)
        TextView phoneTV;
        @Bind(R.id.sureOrderTV)
        TextView sureOrderTV;
        @Bind(R.id.order_goods_list)
        ExpandListView goodlist;
        @Bind(R.id.totalPriceTV)
        TextView totalPriceTV;
        @Bind(R.id.returnMoneyTV)
        TextView returnMoneyTV;
        @Bind(R.id.clickLL)
        LinearLayout clickLL;
        @Bind(R.id.can_send_flag_iv)
        ImageView can_send_flag_iv;

        @OnClick({R.id.clickLL, R.id.sureOrderTV})
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
