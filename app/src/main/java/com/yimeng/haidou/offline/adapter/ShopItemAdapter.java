package com.yimeng.haidou.offline.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.haidou.R;
import com.yimeng.entity.OfflineShopBean;
import com.yimeng.utils.CommonUtils;
import com.huige.library.utils.DeviceUtils;

import java.util.List;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/3/20 0020 下午 05:02.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 店铺item显示
 * </pre>
 */
public class ShopItemAdapter extends BaseQuickAdapter<OfflineShopBean, BaseViewHolder>{
    public ShopItemAdapter(@Nullable List<OfflineShopBean> data) {
        super(R.layout.adapter_offline_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OfflineShopBean item) {
        helper.setText(R.id.tv_start, "店铺评分: " + item.getShopScore())
                .setText(R.id.tv_shop_name, item.getShopName())
                .setText(R.id.tv_shop_address, "地址:" + item.getCity() + item.getArea() + item.getAddress())
                .setText(R.id.tv_shop_distance, CommonUtils.getDistance(item.getDistance() + ""))
        ;
        CommonUtils.showRadiusImage(helper.getView(R.id.iv_logo), item.getLogoPath(),
                DeviceUtils.dp2px(mContext, 5), true, true, true, true);
    }
}
