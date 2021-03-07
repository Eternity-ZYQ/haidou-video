package com.yimeng.haidou.offline;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.yimeng.base.BaseActivity;
import com.yimeng.haidou.R;
import com.yimeng.haidou.mine.ShopCheckInInfomationActivity;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.widget.MyToolBar;
import com.huige.library.widget.ItemLayout;

import butterknife.Bind;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018-12-27 11:40:47
 *  Email  : zhihuiemail@163.com
 *  Desc   : 创建店铺(个人/企业)
 */
public class SelPartnerTypeActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.item_company)
    ItemLayout itemCompany;
    @Bind(R.id.item_person)
    ItemLayout itemPerson;

    /**
     * 是否选择
     */
    private boolean isSel = false;
    /**
     * 企业
     */
    private boolean isCompany = false;

    private ItemLayout.OnSingleItemClickListener mSingleItemClickListener = new ItemLayout.OnSingleItemClickListener() {
        @Override
        public void onItemClick(View view) {

            switch (view.getId()) {
                case R.id.item_company: // 企业
                    if (!isSel) {
                        showLoadingDialog();
                    } else {
                        jumpCreateShop("entity");
                    }
                    break;
                case R.id.item_person:  // 个人
                    if (isCompany) {
                        jumpCreateShop("noshop");
                    } else {
                        jumpCreateShop("sale");
                    }
                    break;
            }
        }
    };

    /**
     * @param type  个人微商 sale，
     *              有店铺实体店 entity，
     *              无店铺 noshop
     */
    private void jumpCreateShop(String type){
        Intent intent = new Intent(this, ShopCheckInInfomationActivity.class);
        intent.putExtra("type", type);
        startActivityForResult(intent, 1);
    }

    private void showLoadingDialog() {
        SimpleDialog.showLoadingHintDialog(this, 1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                itemCompany.setContent("有公司/线下实体店");
                itemPerson.setContent("无公司场地且无实体店");
                toolBar.setTitle("企业账户/个体工商户");
                isSel = true;
                isCompany = true;
                SimpleDialog.cancelLoadingHintDialog();
            }
        }, 500);
    }

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_sel_partner_type;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
        itemCompany.setOnItemClickListener(mSingleItemClickListener);
        itemPerson.setOnItemClickListener(mSingleItemClickListener);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            finish();
        }
    }

}
