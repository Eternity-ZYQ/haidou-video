package com.yimeng.haidou.shop.adapter;

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

import com.yimeng.entity.ModelProductByMenuNo;
import com.yimeng.haidou.R;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.UnitUtil;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 2018/6/13.
 * 分类商品列表
 */

public class SortListAdapter extends BaseAdapter {

    public LinkedList<ModelProductByMenuNo> mList;
    private Context mContext;
    private Handler mHandler;
    private int mWhat;

    public SortListAdapter(LinkedList<ModelProductByMenuNo> mList, Context mContext, Handler mHandler, int mWhat) {
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        int type = getItemViewType(i);
        ModelProductByMenuNo model = (ModelProductByMenuNo) getItem(i);

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_gv_mall, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setData(model, holder);
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setData(ModelProductByMenuNo model, ViewHolder holder) {

//        ViewFactory.bindArcangle(holder.mImage, model.getImagesPath(), 3, 3, 0, 0);
//        CommonUtils.showRadiusImage(holder.mImage, model.getImagesPath(), DeviceUtils.dp2px(mContext, 10), true, true, false, false);
        CommonUtils.showImage(holder.mImage, model.getImagesPath());
        holder.mTitle.setText(model.getProductName());
        holder.mCostBeans.setText(UnitUtil.getMoney(model.getPrice()));
        holder.mPeoplePay.setText(model.getHasSaled() + mContext.getString(R.string.tv_people_pay));
        holder.mProductGift.setText(UnitUtil.getJFMoneyStr(mContext, model.getPrice()));

//        int w = (CommonUtils.getScreenWidth(mContext) - CommonUtils.dp2px(36, mContext)) / 2;
//        int h = (int) (w / 1.37);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(w, h);
//        holder.mImage.setLayoutParams(layoutParams);
        holder.clickLL.setTag(model);
    }

    class ViewHolder {
        @Bind(R.id.tv_product_name)
        TextView mTitle;
        @Bind(R.id.tv_product_price)
        TextView mCostBeans;
        @Bind(R.id.tv_product_pay)
        TextView mPeoplePay;
        @Bind(R.id.tv_product_gift)
        TextView mProductGift;
        @Bind(R.id.iv_product_logo)
        ImageView mImage;
        @Bind(R.id.clickLL)
        ConstraintLayout clickLL;

        @OnClick(R.id.clickLL)
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
