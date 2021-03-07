package com.yimeng.haidou.shop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yimeng.entity.ModelAddress;
import com.yimeng.haidou.R;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 地址管理
 *
 * @author xp
 * @describe 地址管理.
 * @date 2017/11/16.
 */

public class AddressAdapter extends BaseAdapter {

    public LinkedList<ModelAddress> mList;
    private Context mContext;
    private Handler mHandler;
    private int mWhat;

    public AddressAdapter(Context mContext, Handler mHandler, LinkedList<ModelAddress> mList, int mWhat) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_lv_address_manage, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ModelAddress model = (ModelAddress) getItem(i);
        setData(model, holder);

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setData(ModelAddress model, ViewHolder holder) {
        holder.tv_name.setText(model.getLinkman());
        holder.tv_phone.setText(model.getMobileNo());
        String addr = model.getProvince() + " " + model.getCity() + " " + model.getArea();
        holder.tv_district.setText(addr + model.getAddress());
        int resId = model.getIsdefault().equals("1") ? R.mipmap.select_circle : R.mipmap.noselect_circle;
        holder.checkSDV.setImageResource(resId);

        holder.editClickTV.setTag(model);
        holder.delectClickTV.setTag(model);
        holder.checkLL.setTag(model);
        holder.ll_click.setTag(model);
    }

    class ViewHolder {
        @Bind(R.id.tv_name)
        TextView tv_name;
        @Bind(R.id.tv_phone)
        TextView tv_phone;
        @Bind(R.id.tv_district)
        TextView tv_district;
        @Bind(R.id.editClickTV)
        TextView editClickTV;
        @Bind(R.id.delectClickTV)
        TextView delectClickTV;
        @Bind(R.id.ll_click)
        LinearLayout ll_click;
        @Bind(R.id.checkLL)
        LinearLayout checkLL;
        @Bind(R.id.checkSDV)
        ImageView checkSDV;

        @OnClick({R.id.editClickTV, R.id.delectClickTV, R.id.ll_click, R.id.checkLL})
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
