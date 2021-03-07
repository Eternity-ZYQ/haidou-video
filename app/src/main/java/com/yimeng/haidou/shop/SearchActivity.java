package com.yimeng.haidou.shop;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yimeng.base.BaseActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.entity.ModelHotSearch;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.GsonUtils;
import com.yimeng.widget.ExpandGridView;
import com.yimeng.widget.FlowLayout;
import com.yimeng.widget.MyToolBar;
import com.google.gson.JsonObject;
import com.huige.library.utils.ToastUtils;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.LinkedList;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 搜索
 */

public class SearchActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.searchET)
    EditText searchET;
    @Bind(R.id.flayout_search_history)
    FlowLayout mFLayoutSearchHistory;
    @Bind(R.id.flayout_hot_recommended)
    FlowLayout mFlayoutHotRecommended;

    @Bind(R.id.gv_search_history)
    ExpandGridView mGVSearchHistory;
    @Bind(R.id.gv_hot_recommended)
    ExpandGridView mGvHotRecommended;
    //热门推荐 热门搜索
    LinkedList<ModelHotSearch> mSearchHistoryList;
    LinkedList<ModelHotSearch> mHotSearchList;
    private MyHandler mHandler = new MyHandler(this);

    @OnClick(R.id.tv_search_submit)
    void SearchClick() {
        String searchContent = String.valueOf(searchET.getText());
        if (!searchContent.isEmpty() && !"".equals(searchContent.trim())) {
            Intent intent = new Intent();
            intent.putExtra("extra", searchContent);
            intent.setClass(this, SearchResultActivity.class);
            startActivity(intent);
        } else {
            ToastUtils.showToast(getString(R.string.tv_input_search_content));
        }
    }

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_search;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initSearchHistoryFlayout();
        initHotRecommendedFlayout();
    }

    private void initSearchHistoryFlayout() {
        //历史纪录text
        //设置textview背景
        HttpParameterUtil.getInstance().requestLogSearch(mHandler);
    }

    private void initHotRecommendedFlayout() {
        //热门推荐text
        //设置textview背景
        HttpParameterUtil.getInstance().requestHotSearch(mHandler);
        mHotSearchList = new LinkedList<>();
    }


    @OnClick(R.id.iv_del_history)
    public void delHistoryData(){

        if(mSearchHistoryList == null || mSearchHistoryList.isEmpty() ) {
            ToastUtils.showToast("还没有搜索历史哦");
            return;
        }
        SimpleDialog.showConfirmDialog(this, null, "确定要删除历史搜索记录吗?", null, new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        delHistory();
                    }
                });
    }

    /**
     * 删除历史
     */
    private void delHistory() {
        new OkHttpCommon().postLoadData(this, ConstantsUrl.URL_DEL_HISTORY, com.yimeng.utils.CommonUtils.createParams(), new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("删除失败，请稍后重试！");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "删除失败，请稍后重试！"));
                    return;
                }
                ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "删除成功！"));
                initSearchHistoryFlayout();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void disposeData(Message msg) {
        Resources resources = getBaseContext().getResources();
        final Intent intent = new Intent();
        switch (msg.what) {

            case ConstantHandler.WHAT_HOT_SEARCH_SUCCESS:
                //热门推荐
                mHotSearchList = (LinkedList<ModelHotSearch>) msg.obj;
                mFlayoutHotRecommended.removeAllViews();
                for (int i = 0; i < mHotSearchList.size(); i++) {
                    Drawable drawable = resources.getDrawable(R.drawable.btn_bg_wihte_stroke_color_aaaaaa);
                    final TextView tv = (TextView) LayoutInflater.from(this).inflate(
                            R.layout.search_label_tv, mFlayoutHotRecommended, false);
                    tv.setTextColor(Color.WHITE);
                    tv.setBackgroundDrawable(drawable);
                    tv.setTextSize(15);
                    tv.setText(mHotSearchList.get(i).getName());
                    mFlayoutHotRecommended.addView(tv);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String extra = (String) tv.getText();
                            intent.putExtra("extra", extra);
                            intent.setClass(SearchActivity.this, SearchResultActivity.class);
                            startActivity(intent);
                        }
                    });
                }
                break;
            case ConstantHandler.WHAT_LOG_SEARCH_SUCCESS:
                //历史纪录
                mSearchHistoryList = (LinkedList<ModelHotSearch>) msg.obj;
                mFLayoutSearchHistory.removeAllViews();
                for (int i = 0; i < mSearchHistoryList.size(); i++) {
                    Drawable drawable = resources.getDrawable(R.drawable.btn_bg_wihte_stroke_color_aaaaaa);
                    final TextView tv = (TextView) LayoutInflater.from(this).inflate(
                            R.layout.search_label_tv, mFLayoutSearchHistory, false);
                    tv.setTextColor(Color.WHITE);
                    tv.setBackgroundDrawable(drawable);
                    tv.setTextSize(15);
                    tv.setText(mSearchHistoryList.get(i).getName());
                    mFLayoutSearchHistory.addView(tv);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String extra = (String) tv.getText();
                            intent.putExtra("extra", extra);
                            intent.setClass(SearchActivity.this, SearchResultActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            default:
                break;
        }
    }

    private static class MyHandler extends Handler {

        private WeakReference<SearchActivity> mImpl;

        public MyHandler(SearchActivity mImpl) {
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
}
