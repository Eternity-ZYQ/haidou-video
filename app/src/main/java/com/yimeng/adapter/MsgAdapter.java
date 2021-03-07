package com.yimeng.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yimeng.entity.MsgBean;
import com.yimeng.haidou.R;

import java.util.List;



/**
 * Author : huiGer
 * Time   : 2018/8/5 0005 下午 12:57.
 * Desc   : 消息中心
 */
public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {

    private Context mContext;
    private List<MsgBean> mList;

    public MsgAdapter(Context context, List<MsgBean> list){
        this.mContext = context;
        this.mList = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.adapter_msg_item, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MsgBean msgBean = mList.get(position);
        holder.tvTitle.setText(msgBean.getMessageTitle());
//        holder.tvMsg.setText(msgBean.getMessageContent());
        holder.tvTime.setText(msgBean.getCreateTime());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle,tvMsg, tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvMsg = itemView.findViewById(R.id.tv_content);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}
