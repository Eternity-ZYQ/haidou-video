package com.yimeng.haidou.mine.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yimeng.entity.ModelTransactionRecords;
import com.yimeng.haidou.R;
import com.yimeng.utils.DateUtil;
import com.yimeng.utils.UnitUtil;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 交易记录
 *
 * @author xp
 * @describe 交易记录.
 * @date 2017/11/16.
 */

public class TransactionRecordsAdapter extends BaseAdapter {

    public LinkedList<ModelTransactionRecords> mList;
    private Context mContext;
    private Handler mHandler;
    private int mWhat;

    public TransactionRecordsAdapter(Context mContext, Handler mHandler, LinkedList<ModelTransactionRecords> mList, int mWhat) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_lv_transaction_records, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ModelTransactionRecords model = (ModelTransactionRecords) getItem(i);
        setData(model, holder);

        return view;
    }

    @SuppressWarnings("ResourceAsColor")
    private void setData(ModelTransactionRecords model, ViewHolder holder) {
        holder.titleTV.setText(model.getTitle());
        holder.describeTV.setText(DateUtil.getAssignDate(UnitUtil.getLong(model.getDescribe()), 3));
        holder.otherTV.setText(UnitUtil.getMoney(model.getOther(), false));
        int color = model.getOther().contains("-") ? Color.RED : mContext.getResources().getColor(R.color.c_333333);
        holder.otherTV.setTextColor(color);
    }

    class ViewHolder {
        @Bind(R.id.titleTV)
        TextView titleTV;
        @Bind(R.id.describeTV)
        TextView describeTV;
        @Bind(R.id.otherTV)
        TextView otherTV;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
