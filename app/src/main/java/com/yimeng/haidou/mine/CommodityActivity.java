package com.yimeng.haidou.mine;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.daimajia.swipe.util.Attributes;
import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.haidou.R;
import com.yimeng.haidou.mine.adapter.CommodityAdapter;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.ModelSimple;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.CommonUtils;
import com.yimeng.widget.MyToolBar;
import com.huige.library.utils.ToastUtils;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lijb
 *         商品分类
 */

public class CommodityActivity extends BaseActivity {
    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.list_item)
    ListView mListView;

    private CommodityAdapter mAdapter;
    /**
     * 店铺编号
     */
    private String mShopNo;
    /**
     * entity 自由商品
     * cuxiao 促销商品
     */
    private String mProductType;
    /**
     * 当前的商品分类
     */
    private ModelSimple mCurrentItem;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_commodity;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        mProductType = intent.getStringExtra("productType");
        mShopNo = CommonUtils.getMember().getTelePhone();

        LinkedList<ModelSimple> datas = new LinkedList<>();
        mAdapter = new CommodityAdapter(this, datas, mHandler, ConstantHandler.WHAT_HANDLER_CLICK);
        mListView.setAdapter(mAdapter);
        mAdapter.setMode(Attributes.Mode.Single);
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        HttpParameterUtil.getInstance().requestShopCommodityStat(mShopNo, mProductType, mHandler);
    }



    @OnClick({R.id.submitBTN})
    public void myOnClick(View view) {
        switch (view.getId()) {
            case R.id.submitBTN:
                Intent data = new Intent(this, EditClassifyActivity.class);
                data.putExtra("shopNo", mShopNo);
                data.putExtra("type", "add");
                startActivity(data);
                break;
            default:
                break;
        }
    }

    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private WeakReference<CommodityActivity> mImpl;

        public MyHandler(CommodityActivity mImpl) {
            this.mImpl = new WeakReference<>(mImpl);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mImpl.get() != null) {
                mImpl.get().disposeData(msg);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void disposeData(Message msg) {
        if(mListView == null || mAdapter == null) return;
        switch (msg.what) {
            case ConstantHandler.WHAT_HANDLER_CLICK:
                final ModelSimple item = (ModelSimple) msg.obj;
                if (msg.arg1 == R.id.delBtn) {
                    SimpleDialog.showConfirmDialog(this, null, "确认删除?", null, new OnConfirmListener() {
                        @Override
                        public void onConfirm() {
                            mCurrentItem = item;
                            SimpleDialog.showLoadingHintDialog(CommodityActivity.this, 4);
                            HttpParameterUtil.getInstance().requestShopCommodityClassifyDelect(mShopNo, mCurrentItem.getId(), "", mHandler);
                        }
                    });
                } else if (msg.arg1 == R.id.editBtn) {
                    Intent data = new Intent(this, EditClassifyActivity.class);
                    data.putExtra("shopNo", mShopNo);
                    data.putExtra("type", "edit");
                    data.putExtra("productCategoryNo", item.getId());
                    data.putExtra("productCategoryName", item.getText());
                    startActivity(data);
                } else {
                    Intent data = new Intent(CommodityActivity.this, GoodsListActivity.class);
                    data.putExtra("mShopNo", mShopNo);
                    data.putExtra("productType", mProductType);
                    data.putExtra("mClassifyNo", item.getId());
                    data.putExtra("title", item.getText());
                    startActivity(data);
                }
                break;
            case ConstantHandler.WHAT_SHOP_COMMODITY_STAT_SUCCESS:
                mAdapter.datas = (LinkedList<ModelSimple>) msg.obj;
                mListView.setAdapter(mAdapter);
                break;
            case ConstantHandler.WHAT_SHOP_COMMODITY_CLASSIFY_DELECT_SUCCESS:
                SimpleDialog.cancelLoadingHintDialog();
                mAdapter.datas.remove(mCurrentItem.getPosition());
                mAdapter.notifyDataSetChanged();
                break;
            case ConstantHandler.WHAT_SHOP_COMMODITY_CLASSIFY_DELECT_FAIL:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast((String) msg.obj);
                break;
            default:
                break;
        }
    }
}
