package com.yimeng.haidou.mine.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yimeng.haidou.R;
import com.yimeng.entity.ModelOrderReport;
import com.yimeng.utils.UnitUtil;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 查看报表
 */

public class LookStatementsAdapter extends BaseAdapter {

    public LinkedList<ModelOrderReport> mList;
    private Context mContext;
    private Handler mHandler;
    private int mWhat;

    public LookStatementsAdapter(LinkedList<ModelOrderReport> mList, Context mContext, Handler mHandler, int mWhat) {
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
        LookStatementsAdapter.ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_look_statements, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ModelOrderReport model = (ModelOrderReport) getItem(i);
        setData(model, holder);

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setData(ModelOrderReport model, LookStatementsAdapter.ViewHolder holder) {
        holder.dateTV.setText(model.getDateT());
        holder.orderCountTV.setText(model.getOrderCount());
        holder.incomeMoneyTV.setText(UnitUtil.getMoney(model.getScoreAmtSum()));
//        holder.refundCountTV.setText(model.getRefundCount());
//        holder.refundMoneyTV.setText(StringUtil.getString(R.string.rmb_unit) + UnitUtil.getMoney(model.getRefundAmtSum()));
    }

    class ViewHolder {
        @Bind(R.id.dateTV)
        TextView dateTV;
        @Bind(R.id.orderCountTV)
        TextView orderCountTV;
        @Bind(R.id.incomeMoneyTV)
        TextView incomeMoneyTV;
        @Bind(R.id.refundCountTV)
        TextView refundCountTV;
        @Bind(R.id.refundMoneyTV)
        TextView refundMoneyTV;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
