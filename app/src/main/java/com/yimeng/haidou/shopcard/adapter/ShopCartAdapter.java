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
import android.widget.TextView;

import com.yimeng.entity.ModelShopCart;
import com.yimeng.haidou.R;
import com.yimeng.widget.ExpandListView;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 购物车adapter（店铺列表+商品）
 */
public class ShopCartAdapter extends BaseAdapter {

    public LinkedList<ModelShopCart> mList;
    private Context mContext;
    private Handler mHandler;
    private int mWhat;

    public ShopCartAdapter(LinkedList<ModelShopCart> mList, Context mContext, Handler mHandler, int mWhat) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_lv_shopping_cart_shop, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ModelShopCart model = (ModelShopCart) getItem(i);
        model.setPosition(i);
        setData(model, holder, i);
        return view;
    }

    private void setData(ModelShopCart model, ViewHolder holder, int i) {
        holder.mShopNameTV.setText(model.getShopName());
        for (int p = 0; p < model.getModelShopCartGoodses().size(); p++) {
            model.getModelShopCartGoodses().get(p).setfPosition(i);
        }

        int resId = model.isShopIsSelected() ? R.mipmap.select_circle : R.mipmap.noselect_circle;
        holder.shopCheckboxSDV.setImageResource(resId);

        ShopCartGoodsAdapter mAdapter = new ShopCartGoodsAdapter(model.getModelShopCartGoodses(),
                mContext, mHandler, mWhat, model.getShopNo());
        holder.mGoodsLV.setAdapter(mAdapter);

        holder.clickLL.setTag(model);
        holder.shopSelectFL.setTag(model);
    }


    class ViewHolder {
        @Bind(R.id.tv_shop_name)
        TextView mShopNameTV;
        @Bind(R.id.lv_shopping_cart_goods_in_shop)
        ExpandListView mGoodsLV;
        @Bind(R.id.click_LL)
        LinearLayout clickLL;
        @Bind(R.id.sdv_select_shop)
        ImageView shopCheckboxSDV;
        @Bind(R.id.fl_shop_select)
        FrameLayout shopSelectFL;

        @OnClick({R.id.click_LL, R.id.fl_shop_select})
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
