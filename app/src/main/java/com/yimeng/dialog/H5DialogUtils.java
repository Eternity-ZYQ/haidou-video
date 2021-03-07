package com.yimeng.dialog;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yimeng.haidou.R;
import com.yimeng.utils.CommonUtils;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/5/22 4:38 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 网页弹窗
 * </pre>
 */
public class H5DialogUtils {

    private Context  mContext;
    private AlertDialog mDialog;
    private View mDialogLayout;
    private WebView mWebView;
    private ProgressBar mProgressBar;

    public H5DialogUtils(Context context){
        this.mContext = context;
        initView();
    }

    private void initView() {
        mDialogLayout = LayoutInflater.from(mContext).inflate(R.layout.dialog_h5_layout, null);
        mDialogLayout.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mProgressBar = mDialogLayout.findViewById(R.id.progressBar);
        mWebView = mDialogLayout.findViewById(R.id.webView);
        initWebView();
        mDialog = new AlertDialog.Builder(mContext)
                .setView(mDialogLayout)
                .create();
        mDialog.setCanceledOnTouchOutside(false);

    }

    private void initWebView() {

        CommonUtils.setWebSetting(mWebView);

        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                }else{
                    mProgressBar.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    /**
     * 设置标题
     * @param title
     */
    public H5DialogUtils setTitle(String title){
        TextView tvTitle = mDialogLayout.findViewById(R.id.tv_dialog_title);
        tvTitle.setText(title);
        return this;
    }

    /**
     * 设置显示的网页
     * @param url
     * @return
     */
    public H5DialogUtils setUrl(String url){
        mWebView.loadUrl(url);
        return this;
    }

    public boolean isShowing(){
        return mDialog.isShowing();
    }

    public void show(){
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public void dismiss(){
        if(mDialog != null) {
            mDialog.dismiss();
        }
        mDialog = null;
        mContext = null;
    }

}
