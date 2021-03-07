package com.yimeng.haidou.shop.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.entity.ModelGetHotSalesProduct;
import com.yimeng.haidou.R;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.UnitUtil;
import com.huige.library.utils.DeviceUtils;

import java.util.List;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/24 0024 下午 06:41.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 商品grid布局的商品列表
 * </pre>
 */
public class ShopProductGridAdapter extends BaseQuickAdapter<ModelGetHotSalesProduct, BaseViewHolder> {
    public ShopProductGridAdapter(@Nullable List<ModelGetHotSalesProduct> data) {
        super(R.layout.adapter_shop_product_grid_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ModelGetHotSalesProduct item) {

        CommonUtils.showRadiusImage(helper.getView(R.id.iv_product_logo), item.getImagesPath(), DeviceUtils.dp2px(mContext, 10), true, true, false, false);
        helper.setText(R.id.tv_product_name, item.getProductName())
                .setText(R.id.tv_deal_sum, item.getHasSaled() + mContext.getString(R.string.tv_people_pay));
        helper.setText(R.id.tv_product_price, UnitUtil.getMoney(item.getPrice()));
    }
}
