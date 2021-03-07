package com.yimeng.haidou.mine.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yimeng.haidou.R;
import com.yimeng.entity.ModelShop;
import com.yimeng.utils.CommonUtils;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 收藏商家列表
 *
 * @author lijb
 */

public class FavoriteMerchantAdapter extends BaseAdapter {


    public LinkedList<ModelShop> mList;
    private Context mContext;
    private Handler mHandler;
    private int mWhat;

    public FavoriteMerchantAdapter(Context mContext, Handler mHandler, LinkedList<ModelShop> mList, int mWhat) {
        this.mContext = mContext;
        this.mHandler = mHandler;
        this.mList = mList;
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_favorite_goods, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ModelShop model = (ModelShop) getItem(i);
        setData(model, holder);

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setData(ModelShop model, ViewHolder holder) {

        CommonUtils.showImage(holder.goodsSDV, model.getLogoPath());
        holder.titleTV.setText(model.getShopName());
        holder.describeTV.setText(model.getAddress());
        holder.describeTV.setTextColor(mContext.getResources().getColor(R.color.c_999999));
        holder.describeTV.setTextSize(14);
        holder.clickRL.setTag(model);
    }

    class ViewHolder {
        @Bind(R.id.clickRL)
        ConstraintLayout clickRL;
        @Bind(R.id.goodsSDV)
        ImageView goodsSDV;
        @Bind(R.id.titleTV)
        TextView titleTV;
        @Bind(R.id.describeTV)
        TextView describeTV;

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