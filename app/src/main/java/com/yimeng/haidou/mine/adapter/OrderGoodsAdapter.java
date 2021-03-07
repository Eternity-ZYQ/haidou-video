package com.yimeng.haidou.mine.adapter;

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
import com.yimeng.entity.ModelOrderGoods;
import com.yimeng.entity.ShopOrderModel;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.UnitUtil;

import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订单店铺商品
 *
 * @author xp
 */

public class OrderGoodsAdapter extends BaseAdapter {

    public LinkedList<ModelOrderGoods> mList;
    private ShopOrderModel mShopOrderModel;
    private Context mContext;
    private Handler mHandler;
    private int mWhat;
    //  0普通商品, 1平台促销, 3自促商品
    private String isPlatno;

    public OrderGoodsAdapter(Context mContext, Handler mHandler, String isPlatno,  LinkedList<ModelOrderGoods> mList, ShopOrderModel mShopOrderModel, int mWhat) {
        this.mContext = mContext;
        this.mHandler = mHandler;
        this.mList = mList;
        this.mShopOrderModel = mShopOrderModel;
        this.mWhat = mWhat;
        this.isPlatno = isPlatno;
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_order_goods, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ModelOrderGoods model = (ModelOrderGoods) getItem(i);
        setData(model, holder);

        return view;
    }

    private void setData(ModelOrderGoods model, ViewHolder holder) {

        if (model.getType() == 1) {
            CommonUtils.showImage(holder.goodsSDV, model.getProductImg());
            holder.nameTV.setText(model.getProductName());
            holder.describeTV.setText(UnitUtil.getMoney(model.getRealAmt(), false));
            if(isPlatno.equals("1")) {
                // 厂促
                holder.ivProductType.setVisibility(View.VISIBLE);
                holder.ivProductType.setImageResource(R.mipmap.jiaobiao_changcu);
            }else if(isPlatno.equals("3")) {
                // 自促
                holder.ivProductType.setVisibility(View.VISIBLE);
                holder.ivProductType.setImageResource(R.mipmap.jiaobiao_zicu);
            }

            holder.clickRL.setTag(model);
            holder.clickRL.setVisibility(View.VISIBLE);
        } else {
//            ViewFactory.bind(holder.goodsSDV, FrescoUtil.getResUrl(R.drawable.warranty));
//            holder.nameTV.setText(R.string.warranty_time);
//            holder.describeTV.setText(UnitUtil.getMoney(mShopOrderModel.getInsuranceOrder_realPrice()));
//            holder.clickRL.setTag(model);
            holder.clickRL.setVisibility(View.GONE);
        }
    }

    class ViewHolder {
        @Bind(R.id.goodsSDV)
        ImageView goodsSDV;
        @Bind(R.id.nameTV)
        TextView nameTV;
        @Bind(R.id.describeTV)
        TextView describeTV;
        @Bind(R.id.clickRL)
        RelativeLayout clickRL;
        @Bind(R.id.iv_product_type)
        ImageView ivProductType;

        @OnClick({R.id.clickRL, R.id.nameTV, R.id.describeTV, R.id.goodsSDV})
        public void myOnClick(View view) {
            Message msg = mHandler.obtainMessage();
            msg.what = mWhat;
            msg.arg1 = view.getId();
            msg.obj = mShopOrderModel;
            mHandler.sendMessage(msg);
        }

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
