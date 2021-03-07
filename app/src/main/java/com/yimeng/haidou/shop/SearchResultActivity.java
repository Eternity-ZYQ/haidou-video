package com.yimeng.haidou.shop;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.entity.ModelSearchProduct;
import com.yimeng.entity.ModelSearchResult;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.widget.MyToolBar;
import com.yimeng.haidou.R;
import com.yimeng.haidou.shop.adapter.SearchResultAdapter;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by user on 2018/6/11.
 */

public class SearchResultActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.searchET)
    EditText editText;
    @Bind(R.id.lv_search_result)
    ListView mListView;
    @Bind(R.id.tv_search_result_count)
    TextView countTV;
    @Bind(R.id.tv_search_not_data)
    TextView mNotDataTV;


    private SearchResultAdapter mAdapter;
    private LinkedList<ModelSearchResult> searchResultsList;
    ModelSearchResult searchResults;
    private LinkedList<ModelSearchProduct> mList;

    private int mPage;


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_search_result;
    }

    @Override
    protected void init() {
        mNotDataTV.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
        Intent intent = getIntent();
        String keyWord = intent.getStringExtra("extra");
        editText.setText(keyWord);
        mPage = 1;
        HttpParameterUtil.getInstance().requestSearchProduct(keyWord, mPage, mHandler);
        mList = new LinkedList<>();
        mAdapter = new SearchResultAdapter(mList, this, mHandler, ConstantHandler.WHAT_HANDLER_CLICK);
        mListView.setDivider(new ColorDrawable(Color.parseColor("#999999")));
        mListView.setDividerHeight(1);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {

    }


    @OnClick(R.id.tv_search_cancel)
    void search() {
        finish();
    }

    private SearchResultActivity.MyHandler mHandler = new SearchResultActivity.MyHandler(this);

    private static class MyHandler extends Handler {

        private WeakReference<SearchResultActivity> mImpl;

        public MyHandler(SearchResultActivity mImpl) {
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

        switch (msg.what) {
            case ConstantHandler.WHAT_SEARCH_PRODUCT_SUCCESS:
                searchResults = (ModelSearchResult) msg.obj;
                if (searchResults.getModelSearchProducts().size() == 0) {
                    mNotDataTV.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                    countTV.setText(R.string.tv_search_result_count);
                } else {
                    mNotDataTV.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mAdapter.mList = searchResults.getModelSearchProducts();
                    mAdapter.notifyDataSetChanged();
                    countTV.setText(getString(R.string.tv_search_result_, searchResults.getTotal()));
                }
                break;
            case ConstantHandler.WHAT_HANDLER_CLICK:
                switch (msg.arg1) {
                    case R.id.click_rl:
                        ModelSearchProduct item = (ModelSearchProduct) msg.obj;
                        ActivityUtils.getInstance().jumpShopProductActivity(1, item.getShopNo(), item.getProductNo(), 0,"");
                }
            default:
                break;
        }

    }

}
