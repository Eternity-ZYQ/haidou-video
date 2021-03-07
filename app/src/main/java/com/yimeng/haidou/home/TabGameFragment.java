package com.yimeng.haidou.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.huige.library.utils.ToastUtils;
import com.yimeng.base.BaseFragment;
import com.yimeng.config.Constants;
import com.yimeng.utils.CommonUtils;
import com.yimeng.widget.MyToolBar;
import com.yimeng.widget.x5webview.X5WebView;
import com.yimeng.haidou.R;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.Md5Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * created by 33271
 * on 2021/1/18
 */
public class TabGameFragment extends BaseFragment {
    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.webView)
    X5WebView webView;
    private String mTitle;
    private String mUrl;

    @Override
    protected int setLayoutResId() {
        return R.layout.fragment_tab_game_h5;
    }

    @Override
    protected void init() {
        mTitle = "游戏中心";
        toolBar.setRightTextVisible(View.GONE);
        toolBar.setTitle(mTitle);
        toolBar.setTvTitleColorWhite();

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (!getActivity().isDestroyed()) {
                    progressBar.setProgress(newProgress);
                    if (newProgress == 100) {
                        progressBar.setVisibility(View.GONE);
                    } else {
                        if (progressBar.getVisibility() != View.VISIBLE) {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (TextUtils.isEmpty(mTitle)) {
                    toolBar.setTitle(title);
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 如下方案可在非微信内部WebView的H5页面中调出微信支付
                if (url.startsWith("weixin://wap/pay?") || url.startsWith("alipay")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    getActivity().startActivity(intent);
                    return true;
                }
//                if (url.startsWith("https")) {
//                    Map<String, String> extraHeaders = new HashMap<String, String>();
//                    extraHeaders.put("Referer", referer);
//                    webView.loadUrl(url, extraHeaders);
//                    referer = url;
//                    return true;
//                }
//                return super.shouldOverrideUrlLoading(view, url);

                if (!(url.startsWith("http") || url.startsWith("https"))) {
                    return true;
                }

                // 比如我们申请时填写的是经常用来测试网络连通性的 http://www.baidu.com
                HashMap map = new HashMap();
                // 指定申请微信 H5 支付时填写的域名，
                map.put("Referer", "http://www.shandw.com");
                view.loadUrl(url, map);
                return true;

            }
        });

        String openid = CommonUtils.getMember().getMemberNo().toLowerCase();
        String nick = CommonUtils.getMember().getNickname();
        String avatar = "http://hdapp.caihvideo.com" + CommonUtils.getMember().getHeadPath();
        String sex = "0";
        String phone = CommonUtils.getMember().getTelePhone();
        String time = String.valueOf(System.currentTimeMillis() / 1000);


        String openidUrl = returnEncode(openid);
        String nickUrl = returnEncode(nick);
        String avatarUrl = returnEncode(avatar);

        String openidSign = returnEncode(openid);
        String nickSign = nick;
        String avatarSign = avatar;

        String paramsString = "channel=" + Constants.APP_GAME_CHANNEL + "&openid=" + openidSign + "&time=" + time + "&nick=" + nickSign + "&avatar=" + avatarSign + "&sex=" + sex
                + "&phone=" + phone + Constants.APP_GAME_KEY;

        String sign = Md5Utils.getMD5(paramsString).toLowerCase();
        mUrl = "http://www.shandw.com/auth/?channel=" +  Constants.APP_GAME_CHANNEL + "&openid=" + openidUrl + "&time=" + time +
                "&nick=" + nickUrl + "&avatar=" + avatarUrl + "&sex="+sex +
                "&phone="+phone+"&sign=" + sign + "&sdw_simple=1&sdw_ld=1";
        webView.loadUrl(mUrl);


    }


    private String returnEncode(String str) {
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick() {
            @Override
            public void onLeftClick() {
                if (webView.canGoBack()) {
                    webView.goBack();
                }else{
                    ToastUtils.showToast("您已经退出至游戏首页!");
                }
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
