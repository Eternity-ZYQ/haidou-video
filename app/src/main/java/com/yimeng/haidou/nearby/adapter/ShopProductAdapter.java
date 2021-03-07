package com.yimeng.haidou.nearby.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yimeng.haidou.R;
import com.yimeng.entity.ModelProductDetail;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.UnitUtil;
import com.huige.library.utils.DeviceUtils;

import java.util.List;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/22 0022 上午 10:57.
 *  Email  : zhihuiemail@163.com
 * </pre>
 */
public class ShopProductAdapter extends BaseQuickAdapter<ModelProductDetail, BaseViewHolder> {

    private BaseViewHolder mHelper;
    private boolean isSelected = true;
    /**
     * 是否显示增减按钮
     */
    private boolean isShowCountLayout = true;
    private String shopType;

    public ShopProductAdapter(List<ModelProductDetail> list) {
        super(R.layout.adapter_shop_product_item, list);
    }

    /**
     * 该店铺，店铺类型
     * @param shopType
     */
    public void setShopType(String shopType){
        this.shopType = shopType;
        notifyDataSetChanged();
    }


    /**
     * 设置可否选择
     *
     * @param isSelected
     */
    public void resetSelect(boolean isSelected) {
        this.isSelected = isSelected;
        notifyDataSetChanged();
    }

    /**
     * 设置是否显示增减按钮
     *
     * @param showStatus true  : 显示
     *                   false : 不显示
     */
    public void setShowCountLayoutState(boolean showStatus) {
        isShowCountLayout = showStatus;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, ModelProductDetail item) {
        mHelper = helper;
        helper.setVisible(R.id.layout_count, true)
                .setVisible(R.id.tv_product_num, false)
                .setText(R.id.tv_product_name, item.getProductName())
                .setText(R.id.tv_product_desc, item.getDescription())
                .setText(R.id.tv_product_price, UnitUtil.getMoney(item.getPrice()));

        int num = item.getSelectCount();
        if (num > 0) {
            mHelper.setText(R.id.tv_product_count, String.valueOf(num))
                    .setVisible(R.id.tv_product_count, true)
                    .setVisible(R.id.iv_product_minus, true);
        } else if (num == 0) {
            mHelper.setVisible(R.id.tv_product_count, false)
                    .setVisible(R.id.iv_product_minus, false);
        }

        if (!isShowCountLayout) {
            helper.setVisible(R.id.layout_count, false);

        } else {
            helper.setVisible(R.id.layout_count, true);
        }

        // 不在范围内不能选择数量
        if (isSelected) {
            helper.setVisible(R.id.iv_product_add, true)
                    .addOnClickListener(R.id.iv_product_minus)
                    .addOnClickListener(R.id.iv_product_add);
        } else {
            helper.setVisible(R.id.iv_product_minus, false)
                    .setVisible(R.id.iv_product_add, false);
        }

        // 库存
        int storage = UnitUtil.getInt(item.getStorage());
        String storageStr;
        if (storage == 0 || (storage - item.getSelectCount()) == 0) {
            storageStr = "库存不足";
        } else {
            storageStr = "剩余" + (storage - item.getSelectCount()) + item.getUnits();
        }
        helper.setText(R.id.tv_product_sum, storageStr);

        String imgUrl = item.getImagesPath();

        CommonUtils.showRadiusImage(helper.getView(R.id.iv_product_logo), imgUrl, DeviceUtils.dp2px(mContext, 10),
                true, true, true, true);

        // 实体店
        if(shopType.equals("entity")) {
            // 商品服务类型   0。线上（仅支持送货）1。到店（仅支持到店消费）2。都支持
            TextView tvProductFeeModeType = helper.getView(R.id.tv_product_feeMode_type);
            if(item.getFeeMode() == 0) {
                tvProductFeeModeType.setText("仅支持送货");
            } else if (item.getFeeMode() == 1) {
                tvProductFeeModeType.setText("仅支持到店");
            }else{
                // 都支持
                tvProductFeeModeType.setText("支持送货");
            }
        }


        
    }


}
