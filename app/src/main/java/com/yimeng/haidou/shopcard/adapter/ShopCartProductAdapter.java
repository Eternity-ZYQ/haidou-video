package com.yimeng.haidou.shopcard.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.haidou.R;
import com.yimeng.entity.ModelProductDetail;
import com.yimeng.utils.UnitUtil;

import java.util.List;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/25 0025 上午 09:44.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 购物车商品列表
 * </pre>
 */
public class ShopCartProductAdapter extends BaseQuickAdapter<ModelProductDetail, BaseViewHolder> {
    public ShopCartProductAdapter(@Nullable List<ModelProductDetail> data) {
        super(R.layout.adapter_shop_cart_product, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ModelProductDetail item) {
        helper.setText(R.id.tv_product_name, item.getProductName())
                .setText(R.id.tv_product_price, UnitUtil.getMoney(item.getPrice()))
                .setText(R.id.tv_product_count, item.getSelectCount() + "")
                .addOnClickListener(R.id.iv_product_minus)
                .addOnClickListener(R.id.iv_product_add)
        ;
    }
}
