package com.yimeng.haidou.mine;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.CommonUtils;
import com.yimeng.widget.MyToolBar;
import com.google.gson.JsonObject;
import com.huige.library.widget.ItemLayout;

import java.io.IOException;

import butterknife.Bind;
import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019-3-21 14:49:04
 *  Email  : zhihuiemail@163.com
 *  Desc   : 店铺商品管理
 */
public class MineShopProductManagerActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.item_mine_goods)
    ItemLayout itemMineGoods;
    @Bind(R.id.item_sale_goods)
    ItemLayout itemSaleGoods;
    @Bind(R.id.tv_tip_desc)
    TextView tv_tip_desc;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_mine_shop_product_manager;
    }

    @Override
    protected void init() {
        if(CommonUtils.getMember().getMemberGrade() < 1) {
            itemSaleGoods.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());

        itemMineGoods.setOnItemClickListener(mOnSingleItemClickListener);
        itemSaleGoods.setOnItemClickListener(mOnSingleItemClickListener);
    }

    private ItemLayout.OnSingleItemClickListener mOnSingleItemClickListener = new ItemLayout.OnSingleItemClickListener() {
        @Override
        public void onItemClick(View v) {

            Intent commodityIntent = new Intent(MineShopProductManagerActivity.this, CommodityActivity.class);
            if(v.getId() == R.id.item_mine_goods) {
                // 自有商品
                commodityIntent.putExtra("productType", "entity");
            }else{
                // 促销商品
                commodityIntent.putExtra("productType", "cuxiao");
            }
            startActivity(commodityIntent);
        }
    };


    @Override
    protected void loadData() {

        getTipMsg();
    }

    /**
     * 开启促销活动标题
     */
    private void getTipMsg() {
        new OkHttpCommon().postLoadData(this, ConstantsUrl.URL_SALE_SHOP_TIP, CommonUtils.createParams(), new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if(jsonObject.get("status").getAsInt() == 1) {
                    JsonObject data = jsonObject.get("data").getAsJsonObject();
//                    tv_tip_title.setText(data.get("name").getAsString());
                    tv_tip_desc.setText(data.get("introduction").getAsString());
                }
            }
        });
    }

}
