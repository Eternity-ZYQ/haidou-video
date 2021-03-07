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
import com.yimeng.entity.ModelOrderSettle;
import com.yimeng.utils.DateUtil;
import com.yimeng.utils.UnitUtil;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 未入账
 *
 * @author xp
 * @describe 未入账.
 * @date 2017/11/16.
 */

public class NotAccountToAdapter extends BaseAdapter {

    public LinkedList<ModelOrderSettle> mList;
    private Context mContext;
    private Handler mHandler;
    private int mWhat;

    public NotAccountToAdapter(Context mContext, Handler mHandler, LinkedList<ModelOrderSettle> mList, int mWhat) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_lv_not_accountto, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ModelOrderSettle model = (ModelOrderSettle) getItem(i);
        setData(model, holder);

        return view;
    }

    @SuppressWarnings("ResourceAsColor")
    private void setData(ModelOrderSettle model, ViewHolder holder) {
        holder.titleTV.setText(model.getOrderName());
        holder.dateTV.setText(DateUtil.getAssignDate(UnitUtil.getLong(model.getCreateTime()), 3));
        holder.moneyTV.setText(UnitUtil.getMoney(model.getDueAmt()));
        int color = model.getDueAmt().contains("-") ? mContext.getResources().getColor(R.color.c_money) :
                mContext.getResources().getColor(R.color.c_333333);
        holder.moneyTV.setTextColor(color);
    }

    class ViewHolder {
        @Bind(R.id.titleTV)
        TextView titleTV;
        @Bind(R.id.dateTV)
        TextView dateTV;
        @Bind(R.id.moneyTV)
        TextView moneyTV;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
