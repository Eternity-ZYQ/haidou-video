package com.yimeng.haidou.order;

import android.view.View;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.haidou.shop.MallOrderActivity;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.widget.MyToolBar;
import com.google.gson.JsonObject;
import com.huige.library.utils.ToastUtils;
import com.huige.library.widget.ItemLayout;

import java.io.IOException;

import butterknife.Bind;
import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019-4-2 17:13:24
 *  Email  : zhihuiemail@163.com
 *  Desc   : 订单管理
 */
public class OrderActivity extends BaseActivity {


    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.item_mall_order)
    ItemLayout itemMallOrder;
    @Bind(R.id.item_shop_order)
    ItemLayout itemShopOrder;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_order;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());

        itemMallOrder.setOnItemClickListener(new ItemLayout.OnSingleItemClickListener() {
            @Override
            public void onItemClick(View v) {
                // 商城订单
                ActivityUtils.getInstance().jumpActivity(MallOrderActivity.class);
            }
        });

        itemShopOrder.setOnItemClickListener(new ItemLayout.OnSingleItemClickListener() {
            @Override
            public void onItemClick(View v) {
                //附近店铺订单
                ActivityUtils.getInstance().jumpActivity(ShopOrderActivity.class);
            }
        });
    }

    @Override
    protected void loadData() {
        new OkHttpCommon().postLoadData(this, ConstantsUrl.URL_ORDER_COUNT, CommonUtils.createParams(), new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("查询订单数量失败!");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if(jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "查询订单数量失败!"));
                    return;
                }

                JsonObject data = jsonObject.get("data").getAsJsonObject();
                // 商城订单
                int mallCount = data.get("mallCount").getAsInt();
                itemMallOrder.setRightText(String.valueOf(mallCount));
                // 店铺订单
                int shopCount = data.get("shopCount").getAsInt();
                itemShopOrder.setRightText(String.valueOf(shopCount));

            }
        });
    }

}
