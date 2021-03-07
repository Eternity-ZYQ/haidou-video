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

import com.yimeng.entity.GoodsModel;
import com.yimeng.haidou.R;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.UnitUtil;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 收藏商品列表
 *
 * @author lijb
 */

public class FavoriteGoodsAdapter extends BaseAdapter {

    public LinkedList<GoodsModel> mList;
    private Context mContext;
    private Handler mHandler;
    private int mWhat;

    public FavoriteGoodsAdapter(Context mContext, Handler mHandler, LinkedList<GoodsModel> mList, int mWhat) {
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

        GoodsModel model = (GoodsModel) getItem(i);
        setData(model, holder);

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setData(GoodsModel model, ViewHolder holder) {

        CommonUtils.showImage(holder.goodsSDV, model.getImagesPath());
        holder.titleTV.setText(model.getProductName());
        holder.describeTV.setText(UnitUtil.getMoney(model.getPrice()));
        holder.tv_send_gift.setText(UnitUtil.getJFMoneyStr(mContext, model.getPrice()));

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
        @Bind(R.id.tv_send_gift)
        TextView tv_send_gift;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.clickRL,})
        public void myOnClick(View view) {
            Message msg = mHandler.obtainMessage();
            msg.what = mWhat;
            msg.arg1 = view.getId();
            msg.obj = view.getTag();
            mHandler.sendMessage(msg);
        }
    }
}
