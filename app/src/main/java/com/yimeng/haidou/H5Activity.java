package com.yimeng.haidou;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.yimeng.base.BaseActivity;
import com.yimeng.haidou.mine.CommodityActivity;
import com.yimeng.haidou.mine.IDCardInfoActivity;
import com.yimeng.haidou.offline.SelPartnerTypeActivity;
import com.yimeng.eventEntity.MainBtnChangeEvent;
import com.yimeng.net.NetComment;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.KeyBoardInputUtils;
import com.yimeng.widget.MyToolBar;

import org.simple.eventbus.EventBus;

import butterknife.Bind;

/**
 * Author : huiGer
 * Time   : 2018/10/25 0025 下午 06:06.
 * Desc   : 网页
 */
public class H5Activity extends BaseActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.webView)
    WebView webView;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    private String mTitle;

    private Handler mHandler = new Handler();
    // 当前连接
    private String mUrl;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_h5;
    }

    @Override
    protected void init() {
        KeyBoardInputUtils.getInstance(this).init();

        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
        mTitle = intent.getStringExtra("title");
        if(mTitle.equals("促销王头条")) {
            toolBar.setRightTextVisible(View.GONE);
            toolBar.setRightIcon(R.mipmap.ico_share);
        }

        toolBar.setTitle(mTitle);

        CommonUtils.setWebSetting(webView);

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(!isDestroyed()) {
                    progressBar.setProgress(newProgress);
                    if(newProgress == 100) {
                        progressBar.setVisibility(View.GONE);
                    } else {
                        if(progressBar.getVisibility() != View.VISIBLE){
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if(TextUtils.isEmpty(mTitle)) {
                    toolBar.setTitle(title);
                }
            }
        });
        webView.setWebViewClient(new WebViewClient(){

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl(mUrl);
        webView.addJavascriptInterface(this, "android");
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick() {
            @Override
            public void onRightClick() {
                super.onRightClick();
                if(mTitle.equals("促销王头条") && !TextUtils.isEmpty(mUrl)) {
                    CommonUtils.shareApp(mUrl);
                }else{
                    webView.reload(); // 刷新
                }
            }
        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                // 下载
                NetComment.downloadBySystem(url, contentDisposition, mimetype);
            }
        });
    }

    @Override
    protected void loadData() {

    }

    /**
     * 促销商品分类页面
     */
    @JavascriptInterface
    public void toClassify(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(H5Activity.this, CommodityActivity.class);
                // 促销商品
                intent.putExtra("productType", "cuxiao");
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * 任务页面
     */
    @JavascriptInterface
    public void toTask(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new MainBtnChangeEvent(MainBtnChangeEvent.FragmentType.task));
                ActivityUtils.getInstance().jumpMainActivity();
            }
        });
    }

    /**
     * 前往开店
     */
    @JavascriptInterface
    public void toOpenShop(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ActivityUtils.getInstance().jumpActivity(SelPartnerTypeActivity.class);
            }
        });
    }


    /**
     * 前往认证
     */
    @JavascriptInterface
    public void toAuth(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ActivityUtils.getInstance().jumpActivity(IDCardInfoActivity.class);
            }
        });
    }

    @JavascriptInterface
    public void close(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(webView != null && webView.canGoBack()) {
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    //销毁Webview
    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }

        mHandler = null;
        super.onDestroy();
    }

}
