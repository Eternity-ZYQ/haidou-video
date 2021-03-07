package com.yimeng.haidou.shopcard.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yimeng.entity.ModelShopCartGoods;
import com.yimeng.haidou.R;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.UnitUtil;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 2018/6/12.
 * 二级adapter
 */

public class ShopCartGoodsAdapter extends BaseAdapter {

    public LinkedList<ModelShopCartGoods> mList;
    private Context mContext;
    private Handler mHandler;
    private int mWhat;
    private String mShopNo;

    public ShopCartGoodsAdapter(LinkedList<ModelShopCartGoods> mList, Context mContext,
                                Handler mHandler, int mWhat, String mShopNo) {
        this.mList = mList;
        this.mContext = mContext;
        this.mHandler = mHandler;
        this.mWhat = mWhat;
        this.mShopNo = mShopNo;
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_lv_shopping_cart, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ModelShopCartGoods model = (ModelShopCartGoods) getItem(i);
        model.setPosition(i);
        model.setmShopNo(mShopNo);
        setData(model, holder);
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setData(ModelShopCartGoods model, ViewHolder holder) {
        CommonUtils.showImage(holder.mImage, model.getImagesPath());
        holder.titleTV.setText(model.getProductName());
        holder.countTV.setText(model.getShopCarNum());
        holder.inventoryTV.setText("库存"+ model.getSort());
        holder.goods_paramterTV.setText(model.getRemark());

        holder.costTV.setText(UnitUtil.getMoney(model.getPrice()));

        if(model.getIsOnsale().equals("1")) {
            holder.checkboxSDV.setVisibility(View.VISIBLE);
            holder.tvNoSale.setVisibility(View.GONE);
            holder.goodSelectFL.setClickable(true);

            int resId = model.isProductIsSelected() ? R.mipmap.select_circle : R.mipmap.noselect_circle;
            holder.checkboxSDV.setImageResource(resId);
        }else{
            // 失效商品
            holder.checkboxSDV.setVisibility(View.GONE);
            holder.tvNoSale.setVisibility(View.VISIBLE);
            holder.goodSelectFL.setClickable(false);
        }



        holder.decreaseSDV.setTag(model);
        holder.addSDV.setTag(model);
        holder.deleteLL.setTag(model);
        holder.goodSelectFL.setTag(model);
        holder.click_RL.setTag(model);
    }

    class ViewHolder {
        @Bind(R.id.sdv_shop_cart_image)
        ImageView mImage;
        @Bind(R.id.tv_title)
        TextView titleTV;
        @Bind(R.id.tv_goods_parameter)
        TextView goods_paramterTV;
        @Bind(R.id.tv_shopping_cart_cost)
        TextView costTV;
        @Bind(R.id.tv_shopping_cart_inventory)
        TextView inventoryTV;
        @Bind(R.id.ll_delete_goods)
        LinearLayout deleteLL;
        @Bind(R.id.tv_shopping_cart_goods_count)
        TextView countTV;
        @Bind(R.id.btn_shopping_cart_delete)
        TextView deleteTV;
        @Bind(R.id.btn_icon_minus_count)
        ImageView decreaseSDV;
        @Bind(R.id.btn_icon_add_count)
        ImageView addSDV;
        @Bind(R.id.btn_select_good)
        ImageView checkboxSDV;
        @Bind(R.id.tv_no_sale)
        TextView tvNoSale;
        @Bind(R.id.fl_select)
        FrameLayout goodSelectFL;
        @Bind(R.id.click_RL)
        RelativeLayout click_RL;

        @OnClick({R.id.btn_icon_minus_count, R.id.btn_icon_add_count, R.id.ll_delete_goods,
                R.id.fl_select, R.id.click_RL, R.id.click_LL})
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
