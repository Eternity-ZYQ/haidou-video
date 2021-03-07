package com.yimeng.haidou.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yimeng.entity.ModelPayRecords;
import com.yimeng.haidou.R;
import com.yimeng.utils.DateUtil;
import com.yimeng.utils.UnitUtil;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 充值记录
 *
 * @author xp
 * @describe 充值记录.
 * @date 2017/11/16.
 */

public class PayRecordsAdapter extends BaseAdapter {

    public LinkedList<ModelPayRecords> mList;
    private Context mContext;
    private Handler mHandler;
    private int mWhat;

    public PayRecordsAdapter(Context mContext, Handler mHandler, LinkedList<ModelPayRecords> mList, int mWhat) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_lv_pay_records, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ModelPayRecords model = (ModelPayRecords) getItem(i);
        setData(model, holder);

        return view;
    }

    @SuppressWarnings("ResourceAsColor")
    private void setData(ModelPayRecords model, ViewHolder holder) {
        holder.titleTV.setText(model.getTitle());
        holder.dateTV.setText(DateUtil.getAssignDate(UnitUtil.getLong(model.getDate()), 3));
        holder.moneyTV.setText(model.getMoney());
        holder.totalTV.setText(model.getTotal());
        int color = model.getMoney().contains("-") ? mContext.getResources().getColor(R.color.c_333333) :
                mContext.getResources().getColor(R.color.theme_color);
        holder.moneyTV.setTextColor(color);
    }

    class ViewHolder {
        @Bind(R.id.titleTV)
        TextView titleTV;
        @Bind(R.id.dateTV)
        TextView dateTV;
        @Bind(R.id.moneyTV)
        TextView moneyTV;
        @Bind(R.id.totalTV)
        TextView totalTV;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
