package com.yimeng.haidou.shop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yimeng.entity.ModelShopCarSettleItem;
import com.yimeng.haidou.R;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.UnitUtil;
import com.huige.library.utils.DeviceUtils;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 立即购买二级adapter
 */

public class PayNowItemAdapter extends BaseAdapter {

    public LinkedList<ModelShopCarSettleItem> mList;
    private Context mContext;
    private Handler mHandler;
    private int mWhat;

    public PayNowItemAdapter(LinkedList<ModelShopCarSettleItem> mList, Context mContext, Handler mHandler, int mWhat) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_lv_order_list_goods, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ModelShopCarSettleItem model = (ModelShopCarSettleItem) getItem(i);
        setData(model, holder);
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setData(ModelShopCarSettleItem model, PayNowItemAdapter.ViewHolder holder) {
        CommonUtils.showRadiusImage(holder.imageSDV, model.getImagesPath(), DeviceUtils.dp2px(mContext, 5), true, true, true, true);
        holder.titleTV.setText(model.getProductName());
        holder.goods_parameterTV.setText(model.getRemark());
        holder.costBeansTv.setText(UnitUtil.getMoney(model.getPrice()));
        holder.countTV.setText("x" + model.getShopCarNum());

    }

    class ViewHolder {
        @Bind(R.id.tv_title)
        TextView titleTV;
        @Bind(R.id.tv_goods_parameter)
        TextView goods_parameterTV;
        @Bind(R.id.tv_cost_beans)
        TextView costBeansTv;
        @Bind(R.id.sdv_image)
        ImageView imageSDV;
        @Bind(R.id.tv_count)
        TextView countTV;

//        @OnClick({R.id.clickRL})
//        public void myOnClick(View view) {
//            Message msg = mHandler.obtainMessage();
//            msg.what = mWhat;
//            msg.arg1 = view.getId();
//            msg.obj = view.getTag();
//            mHandler.sendMessage(msg);
//        }

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
