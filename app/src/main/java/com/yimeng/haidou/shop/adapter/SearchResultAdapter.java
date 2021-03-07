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

import com.yimeng.entity.ModelSearchProduct;
import com.yimeng.haidou.R;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.UnitUtil;
import com.huige.library.utils.DeviceUtils;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by user on 2018/6/11.
 */

public class SearchResultAdapter extends BaseAdapter {

    public LinkedList<ModelSearchProduct> mList;
    private Context mContext;
    private Handler mHandler;
    private int mWhat;

    public SearchResultAdapter(LinkedList<ModelSearchProduct> mList, Context mContext, Handler mHandler, int mWhat) {
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
        SearchResultAdapter.ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_lv_search_result, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ModelSearchProduct model = (ModelSearchProduct) getItem(i);
        setData(model, holder);

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setData(ModelSearchProduct model, SearchResultAdapter.ViewHolder holder) {
        CommonUtils.showRadiusImage(holder.mImage, model.getImagesPath(), DeviceUtils.dp2px(mContext, 5), true, true, true, true);
        holder.titleTV.setText(model.getProductName());
        holder.shop_nameTV.setText(model.getShopName());
        holder.tvSendGift.setText(UnitUtil.getJFMoneyStr(mContext, model.getPrice()));

        holder.costTv.setText(UnitUtil.getMoney(model.getPrice()));
        holder.mRL.setTag(model);
    }

    class ViewHolder {
        @Bind(R.id.tv_title)
        TextView titleTV;
        @Bind(R.id.tv_shop_name)
        TextView shop_nameTV;
        @Bind(R.id.tv_bean_cost)
        TextView costTv;
        @Bind(R.id.tv_send_gift)
        TextView tvSendGift;
        @Bind(R.id.sdv_image)
        ImageView mImage;
        @Bind(R.id.click_rl)
        ConstraintLayout mRL;

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
