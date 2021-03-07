package com.yimeng.haidou.mine.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.entity.ModelMemberBankcard;
import com.yimeng.interfaces.onBitmapGetColorListener;
import com.yimeng.haidou.R;
import com.yimeng.utils.CommonUtils;
import com.huige.library.widget.CircleImageView;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 银行卡
 *
 * @author xp
 * @describe 银行卡.
 * @date 2017/11/16.
 */

public class BankCardAdapter extends BaseAdapter {

    public LinkedList<ModelMemberBankcard> mList;
    private Context mContext;
    private Handler mHandler;
    private int mWhat;

    public BankCardAdapter(Context mContext, Handler mHandler, LinkedList<ModelMemberBankcard> mList, int mWhat) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_lv_bankcard, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ModelMemberBankcard model = (ModelMemberBankcard) getItem(i);
        setData(model, holder);

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setData(ModelMemberBankcard model, final ViewHolder holder) {


        holder.describeTV.setText(CommonUtils.getBankCardNo(model.getBankcardNum()));

        String bankName = model.getBankName();
        bankName = bankName.split(",")[2];
        holder.nameTV.setText(bankName);


        String bankLogoUrl = ConstantsUrl.IMG_BANK_PATH + CommonUtils.getBankEn(bankName) + ".png";
        Glide.with(mContext)
                .load(bankLogoUrl)
                .apply(new RequestOptions().error(R.mipmap.logo))
                .into(holder.ivBankLogo);

        Glide.with(mContext)
                .asBitmap()
                .load(bankLogoUrl)
                .apply(new RequestOptions().error(R.mipmap.logo))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        CommonUtils.getBitmapColor(resource, new onBitmapGetColorListener() {
                            @Override
                            public void getColor(int color) {
                                GradientDrawable bg = new GradientDrawable();
                                bg.setCornerRadii(new float[]{20, 20, 20, 20, 0, 0, 0, 0});
                                bg.setColor(color);
                                holder.clickRL.setBackground(bg);
                            }
                        });
                    }
                });

        holder.editBtn.setTag(model);
        holder.delBtn.setTag(model);
        holder.clickRL.setTag(model);
    }

    class ViewHolder {
        @Bind(R.id.nameTV)
        TextView nameTV;
        @Bind(R.id.describeTV)
        TextView describeTV;
        @Bind(R.id.editBtn)
        Button editBtn;
        @Bind(R.id.delBtn)
        Button delBtn;
        @Bind(R.id.iv_bank)
        CircleImageView ivBankLogo;
        @Bind(R.id.clickRL)
        ConstraintLayout clickRL;

        @OnClick({R.id.editBtn, R.id.delBtn, R.id.clickRL})
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
