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

import com.yimeng.entity.ModelSimple;
import com.yimeng.haidou.R;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 售后服务
 */

public class AfterSalesServiceAdapter extends BaseAdapter {

    public LinkedList<ModelSimple> mList;
    private Context mContext;
    private Handler mHandler;
    private int mWhat;

    public AfterSalesServiceAdapter(LinkedList<ModelSimple> mList, Context mContext, Handler mHandler, int mWhat) {
        this.mList = mList;
        this.mContext = mContext;
        this.mHandler = mHandler;
        this.mWhat = mWhat;
    }

    @Override
    public int getCount() {
        return mList.size();
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_gv_after_salces_service, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ModelSimple model = (ModelSimple) getItem(i);
        setData(model, holder, i);
        return view;
    }

    private void setData(ModelSimple model, ViewHolder holder, int i) {

        holder.mSDV.setVisibility(View.VISIBLE);
        holder.mTitle.setText(model.getText());
    }


    class ViewHolder {
        @Bind(R.id.tv_after_sales_service)
        TextView mTitle;
        @Bind(R.id.sdv_image)
        ImageView mSDV;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
