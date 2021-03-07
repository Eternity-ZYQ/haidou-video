package com.yimeng.haidou.mine.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.yimeng.haidou.R;
import com.yimeng.entity.GoodsModel;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.UnitUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商品列表
 *
 * @author lijb
 */

public class GoodsListAdapter extends BaseSwipeAdapter {

    private Context context;
    public List<GoodsModel> datas;
    private Handler mHandler;
    private int mWhat;
    private int mType;//类型 :0销售中,1仓库中,2审核中,3已驳回
    /**
     * entity 　自有商品
     * cuxiao   促销商品
     */
    private String mProductType;

    public GoodsListAdapter(Context context, List<GoodsModel> datas, String productType, Handler mHandler, int mWhat, int mType) {
        this.context = context;
        this.datas = datas;
        this.mHandler = mHandler;
        this.mWhat = mWhat;
        this.mType = mType;
        this.mProductType = productType;
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.swipe;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View generateView(final int position, ViewGroup parent) {

        @SuppressLint("InflateParams")
        View v = LayoutInflater.from(context).inflate(R.layout.item_goods, null);
        return v;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void fillValues(int position, View v) {

        ViewHolder holder;
        if (v.getTag() == null) {
            holder = new ViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        final GoodsModel item = (GoodsModel) getItem(position);
        item.setPosition(position);

        holder.swipe.close();
        holder.nameTV.setText(item.getProductName());
        holder.describeTV.setText(item.getDescription());
        holder.priceTV.setText(UnitUtil.getMoney(item.getPrice()));
        CommonUtils.showImage(holder.goodsSDV, item.getImagesPathLogo());
        holder.storageTV.setText("库存" + item.getStorage() + " >");

        if(mProductType.equals("cuxiao")) {
            holder.ivProductType.setVisibility(View.VISIBLE);
            if(TextUtils.isEmpty(item.getProductPlatNo())) {
                holder.ivProductType.setImageResource(R.mipmap.jiaobiao_zicu);
            }else{
                holder.ivProductType.setImageResource(R.mipmap.jiaobiao_changcu);
            }
        }else{
            holder.ivProductType.setVisibility(View.GONE);
        }

        holder.delBtn.setVisibility(View.GONE);
        holder.editBtn.setVisibility(View.GONE);
        holder.soldOutBtn.setVisibility(View.GONE);
        holder.putawayBtn.setVisibility(View.GONE);
        switch (mType) {
            case 0:
                holder.editBtn.setVisibility(View.VISIBLE);
                holder.soldOutBtn.setVisibility(View.VISIBLE);
                break;
            case 1:
                holder.delBtn.setVisibility(View.VISIBLE);
                holder.editBtn.setVisibility(View.VISIBLE);
                holder.putawayBtn.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.delBtn.setVisibility(View.VISIBLE);
//                holder.editBtn.setVisibility(View.VISIBLE);
                break;
            case 3:
                holder.delBtn.setVisibility(View.VISIBLE);
                holder.editBtn.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        holder.delBtn.setTag(item);
        holder.editBtn.setTag(item);
        holder.soldOutBtn.setTag(item);
        holder.putawayBtn.setTag(item);
        holder.clickRL.setTag(item);
        holder.item_product_edit_view.setTag(item);
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

    class ViewHolder {
        @Bind(R.id.swipe)
        SwipeLayout swipe;
        @Bind(R.id.goodsSDV)
        ImageView goodsSDV;
        @Bind(R.id.nameTV)
        TextView nameTV;
        @Bind(R.id.describeTV)
        TextView describeTV;
        @Bind(R.id.priceTV)
        TextView priceTV;
        @Bind(R.id.delBtn)
        Button delBtn;
        @Bind(R.id.editBtn)
        Button editBtn;
        @Bind(R.id.soldOutBtn)
        Button soldOutBtn;
        @Bind(R.id.putawayBtn)
        Button putawayBtn;
        @Bind(R.id.clickRL)
        RelativeLayout clickRL;
        @Bind(R.id.storageTV)
        TextView storageTV;
        @Bind(R.id.item_product_edit_view)
        View item_product_edit_view;
        @Bind(R.id.iv_product_type)
        ImageView ivProductType;

        @OnClick({R.id.delBtn, R.id.editBtn, R.id.soldOutBtn, R.id.putawayBtn, R.id.clickRL, R.id.item_product_edit_view})
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
