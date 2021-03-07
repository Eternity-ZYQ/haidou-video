package com.yimeng.haidou.circle;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.yimeng.base.BaseTakePhotoActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.config.EventTags;
import com.yimeng.haidou.R;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.interfaces.UploadImageCallBack;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.NetComment;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.utils.sensitive.SensitiveFilter;
import com.yimeng.widget.MyToolBar;
import com.google.gson.JsonObject;
import com.huige.library.utils.ToastUtils;
import com.lxj.xpopup.core.BasePopupView;

import org.devio.takephoto.model.TResult;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/10/22 0022 下午 07:16.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 发布商圈
 * </pre>
 */
public class ReleaseActivity extends BaseTakePhotoActivity {


    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.iv_1)
    ImageView iv1;
    @Bind(R.id.iv_2)
    ImageView iv2;
    @Bind(R.id.iv_3)
    ImageView iv3;
    @Bind(R.id.toolBar)
    MyToolBar toolBar;

    private String imageUrl1, imageUrl2, imageUrl3;
    private MyHandler mHandler = new MyHandler(this);
    private int mImageType = 1;

    private boolean isShareTask = false;
    private Bitmap mShareBitmap;
    private BasePopupView mLoadingDialog;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_release;
    }

    @Override
    protected void init() {
        EventBus.getDefault().registerSticky(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            isShareTask = bundle.getBoolean("isShareTask", false);
        }

        if(isShareTask) {
            // 来自任务页面分享
            etContent.setText("全民团购，做促销王任务，嗨赚现金，真的有实时兑现！");
            etContent.setSelection(etContent.getText().length());
        }

    }

    @Subscriber(tag = EventTags.SHARE_BITMAP)
    public void shareBitmapResult(Bitmap bitmap){
        this.mShareBitmap = bitmap;
        iv1.setImageBitmap(mShareBitmap);
        iv1.setEnabled(false);
        iv2.setVisibility(View.VISIBLE);
        loadBitmap();
    }

    private void loadBitmap() {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mShareBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] bytes = baos.toByteArray();
        String baseStr = Base64.encodeToString(bytes, 0);
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("id", "picsjbb00" + System.currentTimeMillis());
        params.put("imgBase64", "data:image/png;base64," + baseStr);
        Log.d("msg", "NetComment -> uploadPic: " + params.toString());
        new OkHttpCommon().postLoadData(this, ConstantsUrl.UPLOAD_IMG_URL, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("图片上传失败");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                String url = GsonUtils.parseJson(jsonObject, "data", "");
                if(jsonObject.get("success").getAsBoolean() && !TextUtils.isEmpty(url)) {
                    imageUrl1 = url;
                }else{
                    ToastUtils.showToast("图片上传失败");
                }
            }
        });

    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());

    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.iv_1,R.id.iv_2,R.id.iv_3})
    public void showSelectImage(View v){
        switch (v.getId()) {
            case R.id.iv_1:
                mImageType = 1;
                break;
            case R.id.iv_2:
                mImageType = 2;
                break;
            case R.id.iv_3:
                mImageType = 3;
                break;
            default:
        }
        showSelPopupWind(v, 1);
    }

    @OnClick({R.id.btn_release})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btn_release:

                String content = etContent.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.showToast("请输入内容!");
                    return;
                }

                SensitiveFilter filter = SensitiveFilter.DEFAULT;
                String resultStr = filter.filter(content, '*');
                if(!resultStr.equals(content)) {
                    SimpleDialog.showSimpleRemarkWithTitleDialog(this, "包含敏感词",
                            resultStr);
                    return;
                }

                String images = "";

                if (!TextUtils.isEmpty(imageUrl1)) {
                    images += imageUrl1;
                    if (!TextUtils.isEmpty(imageUrl2)) {
                        images += "," + imageUrl2;
                        if (!TextUtils.isEmpty(imageUrl3)) {
                            images += "," + imageUrl3;
                        }
                    }
                }

                HttpParameterUtil.getInstance().requestRelease(content, images, mHandler);

                break;
        }
    }

    @Override
    public void takeSuccess(TResult tResult) {
        super.takeSuccess(tResult);
        String url = getTakeSuccessPath(tResult);

        mLoadingDialog = SimpleDialog.createDialog(this, "上传中...", false,true,true).show();

        NetComment.uploadPic(this, url, new UploadImageCallBack() {
            @Override
            public void uploadSuccess(String url) {
                mLoadingDialog.dismiss();
                switch (mImageType) {
                    case 1:
                        imageUrl1 = url;
                        CommonUtils.showImage(iv1, url);
                        if (iv2.getVisibility() != View.VISIBLE) {
                            iv2.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 2:
                        imageUrl2 = url;
                        CommonUtils.showImage(iv2, url);
                        if (iv3.getVisibility() != View.VISIBLE) {
                            iv3.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 3:
                        imageUrl3 = url;
                        CommonUtils.showImage(iv3, url);
                        break;
                    default:

                }
            }

            @Override
            public void uploadFail(String msg) {
                ToastUtils.showToast(msg);
                mLoadingDialog.dismiss();
            }
        });
    }




    private void disposeData(Message msg) {
        ToastUtils.showToast((String) msg.obj);
        if (msg.what == ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_RELEASE_SUCCESS) {
            ToastUtils.showToast("发布成功！");
            finish();
        } else {
            ToastUtils.showToast((String) msg.obj);
        }
    }



    private class MyHandler extends Handler {
        private WeakReference<ReleaseActivity> mImpl;

        public MyHandler(ReleaseActivity impl) {
            mImpl = new WeakReference<>(impl);
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
