package com.yimeng.haidou.mine.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yimeng.entity.ModelWithdrawDeposit;
import com.yimeng.haidou.R;
import com.yimeng.utils.DateUtil;
import com.yimeng.utils.UnitUtil;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 提现记录
 *
 * @author xp
 * @describe 提现记录.
 * @date 2017/11/16.
 */

public class WithdrawDepositRecordsAdapter extends BaseAdapter {

    public LinkedList<ModelWithdrawDeposit> mList;
    private Context mContext;
    private Handler mHandler;
    private int mWhat;

    public WithdrawDepositRecordsAdapter(Context mContext, Handler mHandler, LinkedList<ModelWithdrawDeposit> mList, int mWhat) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_lv_withdraw_deposit_records, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ModelWithdrawDeposit model = (ModelWithdrawDeposit) getItem(i);
        setData(model, holder);

        return view;
    }

    @SuppressLint("SetTextI18n")
    @SuppressWarnings("ResourceAsColor")
    private void setData(final ModelWithdrawDeposit model, ViewHolder holder) {

        String cardNo = model.getCardNo().length() > 4 ? model.getCardNo().substring(
                model.getCardNo().length() - 4) : model.getCardNo();
        String title = "提现" + UnitUtil.getMoney(model.getApplyAmt()) +
                "(" + model.getBank() + cardNo + ")";
        holder.titleTV.setText(title);
        holder.dateTV.setText(DateUtil.getAssignDate(UnitUtil.getLong(model.getCreateTime()), 3));
        holder.moneyTV.setText("-" + UnitUtil.getMoney(model.getApplyAmt()));
        holder.statusTV.setText(model.getWithdrawState());
        holder.moneyTV.setTextColor(Color.parseColor("#FE3C3C"));

        if (model.getIsReject() == 1) {
            holder.rejectIv.setVisibility(View.VISIBLE);
        }else {
            holder.rejectIv.setVisibility(View.GONE);
        }
        holder.rejectIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = mHandler.obtainMessage();
                msg.what = 0x01;
                msg.obj = model.getRemark();
                mHandler.sendMessage(msg);
            }
        });
    }

    class ViewHolder {
        @Bind(R.id.titleTV)
        TextView titleTV;
        @Bind(R.id.dateTV)
        TextView dateTV;
        @Bind(R.id.moneyTV)
        TextView moneyTV;
        @Bind(R.id.statusTV)
        TextView statusTV;
        @Bind(R.id.reject_iv)
        ImageView rejectIv;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
