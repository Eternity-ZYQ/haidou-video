package com.yimeng.utils;


import com.yimeng.entity.ModelProductDetail;
import com.huige.library.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/11/1 0001 下午 06:06.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class ShopProductUtils {

    private static ShopProductUtils sMShopProductUtils = null;
    private List<ModelProductDetail> mShopProductSelect = new ArrayList<>();
    private ModelProductDetail lastSelSaleProduct;

    public static ShopProductUtils getInstance() {
        if (sMShopProductUtils == null) {
            synchronized (ShopProductUtils.class) {
                if (sMShopProductUtils == null) {
                    sMShopProductUtils = new ShopProductUtils();
                }
            }
        }
        return sMShopProductUtils;
    }

    /**
     * @return 添加成功状态
     */
    public boolean addProduct(ModelProductDetail product) {
        if (mShopProductSelect.size() == 0) {
            mShopProductSelect.add(product);
        } else {
            boolean has = false;
            for (ModelProductDetail productDetail : mShopProductSelect) {

                // 已存在
                if (productDetail.getProductNo().equals(product.getProductNo())) {
                    productDetail.setSelectCount(product.getSelectCount());
                    has = true;
                }else{
                    // 不能添加不同商品服务类别的商品
                    if(productDetail.getFeeMode() != product.getFeeMode()) {
                        ToastUtils.showToast("请留意消费服务类型，到店或送货不能一起下单");
                        return false;
                    }
                }
            }
            if (!has) {
                mShopProductSelect.add(product);
            }

        }
        return true;
    }

    /**
     * @return 删除商品状态
     */
    public boolean removeProduct(ModelProductDetail product) {
        if (!mShopProductSelect.isEmpty()) {
            for (ModelProductDetail productDetail : mShopProductSelect) {
                // 已存在
                if (productDetail.getProductNo().equals(product.getProductNo())) {
                    mShopProductSelect.remove(productDetail);
                    return true;
                }
            }
        }
        return false;
    }

    public List<ModelProductDetail> getShopProductSelectList() {
        return mShopProductSelect;
    }

    /**
     * 总价
     *
     * @return
     */
    public double getPriceSum() {
        double priceSum = 0;
        for (ModelProductDetail product : mShopProductSelect) {
            priceSum += (product.getSelectCount() * UnitUtil.getDouble(product.getPrice()));
        }

        return priceSum/100.0;
    }

    /**
     * @return 商品数量
     */
    public int getProductCount(){
        int count = 0;
        for (ModelProductDetail product : mShopProductSelect) {
            count += product.getSelectCount();
        }
        return count;
    }

    public boolean isEmpty() {
        return mShopProductSelect.isEmpty();
    }

    public void setLastSaleProduct(ModelProductDetail productDetail){
        this.lastSelSaleProduct = productDetail;
    }

    public ModelProductDetail getLastSaleProduct(){
        return lastSelSaleProduct;
    }
}
