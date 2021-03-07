package com.yimeng.haidou.shop.adapter;

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

import com.yimeng.config.ConstantHandler;
import com.yimeng.entity.ModelMallOrder;
import com.yimeng.entity.ModelMallOrderItem;
import com.yimeng.haidou.R;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.UnitUtil;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商城订单分页列表二级adapter
 */

public class OrderListGoodsAdapter extends BaseAdapter {

    public LinkedList<ModelMallOrderItem> mList;
    private Context mContext;
    private Handler mHandler;
    private ModelMallOrder mModelMallOrder;
    private int mWhat;

    public OrderListGoodsAdapter(Context mContext, Handler mHandler, ModelMallOrder mModelMallOrder,
                                 LinkedList<ModelMallOrderItem> mList, int mWhat) {
        this.mContext = mContext;
        this.mHandler = mHandler;
        this.mList = mList;
        this.mWhat = mWhat;
        this.mModelMallOrder = mModelMallOrder;
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_lv_order_list_goods, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ModelMallOrderItem model = (ModelMallOrderItem) getItem(i);
        setData(model, holder);

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setData(ModelMallOrderItem model, ViewHolder holder) {
        CommonUtils.showImage(holder.imgSDV, model.getProductImg());
        holder.titleTV.setText(model.getProductName());
        holder.parameterTV.setText(model.getProductColorSize());

//        String price="";
//        if(model.getPayType().equals("yu_e")){
//            // 余额支付
//            price = UnitUtil.getSwipeMoney((UnitUtil.getDouble(model.getBalance())/ UnitUtil.getInt(model.getProductNum())) +"");
//        }else{
//            price = UnitUtil.getSwipeMoney((UnitUtil.getDouble(model.getRealAmt()) / UnitUtil.getInt(model.getProductNum()))+"");
//        }
//
////        String price = UnitUtil.getSwipeMoney(UnitUtil.getDouble(model.getRealAmt())  / UnitUtil.getInt(model.getProductNum())+ "");
//
//        String md = UnitUtil.getDouble(model.getGetScore()) == 0 ? "" : UnitUtil.getSwipeMoney(UnitUtil.getDouble(model.getGetScore()) / UnitUtil.getInt(model.getProductNum()) +"");
//        md = TextUtils.isEmpty(md) ? "" : ("+"+ md+"妈豆");
//        holder.beansTV.setText(StringUtil.getString(R.string.rmb_unit) + price + md);

        String price = UnitUtil.getMoney(UnitUtil.getDouble(model.getRealAmt()) / UnitUtil.getInt(model.getProductNum()) + "");
        holder.beansTV.setText(price);


        holder.countTV.setText("x" + model.getProductNum());
        if(mWhat == ConstantHandler.WHAT_HANDLER_CLICK) {
            holder.clickRL.setTag(mModelMallOrder);
        }else {
            holder.clickRL.setTag(model);
        }
    }

    class ViewHolder {
        @Bind(R.id.sdv_image)
        ImageView imgSDV;
        @Bind(R.id.tv_title)
        TextView titleTV;
        @Bind(R.id.tv_goods_parameter)
        TextView parameterTV;
        @Bind(R.id.tv_cost_beans)
        TextView beansTV;
        @Bind(R.id.tv_count)
        TextView countTV;
        @Bind(R.id.click_rl)
        RelativeLayout clickRL;

        @OnClick({R.id.click_rl})
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