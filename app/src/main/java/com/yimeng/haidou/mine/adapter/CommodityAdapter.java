package com.yimeng.haidou.mine.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.yimeng.haidou.R;
import com.yimeng.entity.ModelSimple;

import java.util.LinkedList;

/**
 * @author lijb
 * 商品管理列表
 */

public class CommodityAdapter extends BaseSwipeAdapter {

    private Context context;
    public LinkedList<ModelSimple> datas;
    private Handler mHandler;
    private int mWhat;

    public CommodityAdapter(Context context, LinkedList<ModelSimple> datas, Handler mHandler, int mWhat) {
        this.context = context;
        this.datas = datas;
        this.mHandler = mHandler;
        this.mWhat = mWhat;
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        @SuppressLint("InflateParams") final View v = LayoutInflater.from(context).inflate(R.layout.item_commodity, null);

        SwipeLayout swipeLayout = v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
            }
        });
        v.setTag(position);

        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {

        final ModelSimple modelSimple = (ModelSimple) getItem(position);
        modelSimple.setPosition(position);

        TextView textTV = convertView.findViewById(R.id.textTV);
        textTV.setText(modelSimple.getText() + "(" + modelSimple.getValue() + ")");

        Button delete = convertView.findViewById(R.id.delBtn);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = mHandler.obtainMessage();
                msg.what = mWhat;
                msg.arg1 = view.getId();
                msg.obj = modelSimple;
                mHandler.sendMessage(msg);
            }
        });
        convertView.findViewById(R.id.editBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = mHandler.obtainMessage();
                msg.what = mWhat;
                msg.arg1 = view.getId();
                msg.obj = modelSimple;
                mHandler.sendMessage(msg);
            }
        });
        convertView.findViewById(R.id.clickRL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = mHandler.obtainMessage();
                msg.what = mWhat;
                msg.arg1 = view.getId();
                msg.obj = modelSimple;
                mHandler.sendMessage(msg);
            }
        });
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
