package com.yimeng.haidou.mine.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yimeng.haidou.R;
import com.yimeng.entity.BusinessModel;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.DateUtil;
import com.yimeng.utils.UnitUtil;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 交易列表
 *
 * @author lijb
 */

public class BusinessListAdapter extends BaseAdapter {

    public LinkedList<BusinessModel> mList;
    private Context mContext;
    private Handler mHandler;
    private int mWhat;

    public BusinessListAdapter(Context mContext, Handler mHandler, LinkedList<BusinessModel> mList, int mWhat) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_business, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        BusinessModel model = (BusinessModel) getItem(i);
        model.setPosition(i);
        setData(model, holder);

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setData(BusinessModel model, ViewHolder holder) {

        CommonUtils.showImage(holder.imageSDV, model.getMemberHeadPath());
        holder.nameTV.setText(model.getNickname());
        holder.phoneTV.setText("("+model.getReceiverMobile()+")");
        holder.typeTV.setText(model.getOrderName());
        holder.timeTV.setText(DateUtil.getAssignDate(UnitUtil.getLong(model.getCreateTime()), 2));
        if(UnitUtil.getInt(model.getRealAmt()) == 0) {
            holder.cashTV.setText("收益在路上");
        }else{
            holder.cashTV.setText(UnitUtil.getMoney(model.getRealAmt()));
        }

        holder.clickRL.setTag(model);
    }

    class ViewHolder {

        @Bind(R.id.targetIV)
        ImageView imageSDV;
        @Bind(R.id.targetTV)
        TextView nameTV;
        @Bind(R.id.phoneTV)
        TextView phoneTV;
        @Bind(R.id.typeTV)
        TextView typeTV;
        @Bind(R.id.timeTV)
        TextView timeTV;
        @Bind(R.id.cashTV)
        TextView cashTV;
        @Bind(R.id.clickRL)
        RelativeLayout clickRL;

        @OnClick({R.id.clickRL,})
        public void myOnClick(View view) {
            Message msg = mHandler.obtainMessage();
            msg.what = mWhat;
            msg.obj = view.getTag();
            mHandler.sendMessage(msg);
        }

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
