/*
 * ************************************************
 * 文件：ShareByOtherActivity.java
 * Author：huiGer
 * 当前修改时间：2019年04月21日 10:53:25
 * 上次修改时间：2019年04月21日 10:53:21
 *
 * Copyright (c) 2019.
 * ************************************************
 */

package com.yimeng.haidou.circle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yimeng.base.BaseActivity;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.MainActivity;
import com.yimeng.haidou.R;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.eventEntity.MainBtnChangeEvent;
import com.yimeng.interfaces.UploadImageCallBack;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.NetComment;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.utils.sensitive.SensitiveFilter;
import com.google.gson.JsonObject;
import com.huige.library.utils.SharedPreferencesUtils;
import com.huige.library.utils.ToastUtils;
import com.huige.library.widget.MyToolBar;

import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.compress.CompressImageUtil;
import org.simple.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/4/21 10:53 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 分享来自系统
 * </pre>
 */
public class ShareByOtherActivity extends BaseActivity {


    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.iv_logo)
    ImageView ivLogo;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.ll_article)
    LinearLayout llArticle;

    private OkHttpCommon mOkHttpCommon;
    private String mContent;
    private String mUrl;
    private Uri mImageUri;

    /**
     * 1. 纯文本
     * 2。纯图片
     * 3。链接
     */
    private int mShareType;


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_share_by_other;
    }

    @Override
    protected void init() {

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (action.equals(Intent.ACTION_SEND) && type != null) {
            Log.d("msg", "ShareByOtherActivity -> init: " + type);
            share(intent);
        }

        mOkHttpCommon = new OkHttpCommon();
    }

    /**
     * 分享
     */
    private void share(Intent intent) {
        mImageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        Bundle extras = intent.getExtras();
        if (mImageUri != null) {
            Log.d("msg", "ShareByOtherActivity -> share: " + mImageUri.getPath());
            Glide.with(this)
                    .load(mImageUri)
                    .into(ivLogo);
        }

        if (extras != null) {

            for (String key : extras.keySet()) {
                Log.d("msg", "ShareByOtherActivity -> share: " + key);
//                Log.d("msg", "ShareByOtherActivity -> share: " + extras.getString(key));
                if (key.contains("url") || key.contains("URL")) {
                    mUrl = extras.getString(key);
                    break;
                }
            }

            mContent = extras.getString(Intent.EXTRA_TEXT);
            if (TextUtils.isEmpty(mUrl) && mContent.contains("http")) {
                String subject = extras.getString(Intent.EXTRA_SUBJECT);
                mUrl = mContent;
                mContent = subject;
            }
            mContent = formatContent(mContent);

        }

        if (mImageUri != null & TextUtils.isEmpty(mUrl)) {
            // 单纯图片分享
            mShareType = 2;


        } else if (!TextUtils.isEmpty(mUrl)) {
            // 网页文章分享
            mShareType = 3;
            tvTitle.setText(mContent);


        } else if (mImageUri == null && !TextUtils.isEmpty(mContent)) {
            // 纯文本分享
            mShareType = 1;
            etContent.setText(mContent);
            llArticle.setVisibility(View.GONE);
        }

    }

    /**
     * @return 内容处理
     */
    private String formatContent(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        if (str.contains("\r\n")) {
            String[] split = str.split("\r\n");
            if (split.length > 1) {
                mUrl = split[1];
            }
            return split[0];
        }
        if (!str.contains("@")) {
            return str;
        }

        return str.substring(0, str.indexOf("@"));
    }


    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick() {
            @Override
            public void onLeftClick() {
                finish();
            }
        });
    }

    @Override
    protected void loadData() {

    }


    @OnClick(R.id.btn_release)
    public void onViewClicked() {
        if (!CommonUtils.checkLogin()) {
            ActivityUtils.getInstance().jumpLoginActivity();
            return;
        }
        try {

            HashMap<String, String> params = CommonUtils.createParams();

            String content = etContent.getText().toString().trim();

            SensitiveFilter filter = SensitiveFilter.DEFAULT;
            String resultStr = filter.filter(content, '*');
            if (!resultStr.equals(content)) {
                SimpleDialog.showSimpleRemarkWithTitleDialog(this, "包含敏感词",
                        resultStr);
                return;
            }
            params.put("content", content);

            if (TextUtils.isEmpty(content)) {
                ToastUtils.showToast("请输入内容！");
                return;
            }

            if (mShareType == 1 || mShareType == 2) {
                // 图片或者纯文本
//                if (TextUtils.isEmpty(content)) {
//                    ToastUtils.showToast("请输入内容！");
//                    return;
//                }

                params.put("isReprinted", "0"); // 是否转载


            } else if (mShareType == 3) {
                // 网页文章
                params.put("isReprinted", "1"); // 是否转载
                params.put("reprintedUrl", mUrl);//转载链接
                params.put("reprintedTitle", mContent);//转载标题
            }
            releaseContent(params);
        } catch (Exception e) {
            ToastUtils.showToast("发布失败，请稍后重试！");
        }


    }

    /**
     * 普通发布
     */
    public void releaseContent(final HashMap<String, String> params) {

        params.put("country", "0"); // 当前国家
        params.put("province", "0");// 省
        params.put("city", "0");    // 市
        params.put("area", "0");    // 区
        params.put("Longitude", (String) SharedPreferencesUtils.get(Constants.APP_LOCATION_LONGITUDE, Constants.APP_DEFAULT_LONGITUDE));
        params.put("Latitude", (String) SharedPreferencesUtils.get(Constants.APP_LOCATION_LATITUDE, Constants.APP_DEFAULT_LATITUDE));
        params.put("isShowAddress", "1"); // 是否显示地址


        if (mImageUri != null) {
            CompressConfig config = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(1200).create();
            String path = mImageUri.getPath();
            if (path.contains("/raw/")) {
                path = path.replace("/raw/", "");
            }
            new CompressImageUtil(ShareByOtherActivity.this, config).compress(path, new CompressImageUtil.CompressListener() {
                @Override
                public void onCompressSuccess(String path) {
                    NetComment.uploadPic(ShareByOtherActivity.this, path, new UploadImageCallBack() {
                        @Override
                        public void uploadSuccess(String url) {
                            params.put("images", url);
                            release(params);
                        }

                        @Override
                        public void uploadFail(String msg) {
                            ToastUtils.showToast("图片上传失败，请稍后重试！");
                        }
                    });
                }

                @Override
                public void onCompressFailed(String s, String s1) {
                    ToastUtils.showToast("图片上传失败，请稍后重试！" + s1);
                }
            });

        } else {
            release(params);
        }


    }

    private void release(HashMap<String, String> params) {

        mOkHttpCommon.postLoadData(this, ConstantsUrl.PARENT_CHILD_RELEASE_URL, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("发布失败，请稍后重试！");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "发布失败，请稍后重试！"));
                    return;
                }

                ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", "发布成功"));

                ActivityUtils.getInstance().jumpActivity(MainActivity.class);
                EventBus.getDefault().post(new MainBtnChangeEvent(MainBtnChangeEvent.FragmentType.circle));
                finish();
            }
        });
    }


}
