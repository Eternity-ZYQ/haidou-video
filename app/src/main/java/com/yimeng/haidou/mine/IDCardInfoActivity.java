package com.yimeng.haidou.mine;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.aliyun.aliyunface.api.ZIMCallback;
import com.aliyun.aliyunface.api.ZIMFacade;
import com.aliyun.aliyunface.api.ZIMFacadeBuilder;
import com.aliyun.aliyunface.api.ZIMResponse;
import com.yimeng.base.BaseTakePhotoActivity;
import com.yimeng.config.ConstantHandler;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.net.CallbackCommon;
import com.yimeng.net.OkHttpCommon;
import com.yimeng.net.lxmm_net.HttpParameterUtil;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.utils.StringUtils;
import com.yimeng.widget.MyToolBar;
import com.yimeng.haidou.R;
import com.google.gson.JsonObject;
import com.huige.library.utils.SharedPreferencesUtils;
import com.huige.library.utils.ToastUtils;

import org.devio.takephoto.model.TResult;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 身份信息
 *
 * @author xp
 * @describe 身份信息.
 * @date 2018/4/11.
 */

public class IDCardInfoActivity extends BaseTakePhotoActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.idCardNoET)
    EditText idCardNoET;
    @Bind(R.id.nameET)
    EditText nameET;
    @Bind(R.id.idCardFrontSDV)
    ImageView idCardFrontSDV;
    @Bind(R.id.idCardVersoSDV)
    ImageView idCardVersoSDV;
    @Bind(R.id.idPhotoSDV)
    ImageView idPhotoSDV;
    /**
     * 照片类型 1正面照，2反面照，3持证照
     */
    private int mPhotoType;
    /**
     * 身份证正面照
     */
    private String mFrontPhoto;
    /**
     * 身份证反面照
     */
    private String mVersoPhoto;
    /**
     * 持证照
     */
    private String mHandheldPhoto;
    /**
     * 审核失败备注
     */
    private String mRemark;
    private MyHandler mHandler = new MyHandler(this);

    private OkHttpCommon okHttpCommon;

    private String bizCode = "";
    private boolean waitForResult = false;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_idcard_info;
    }

    @Override
    protected void init() {

        mRemark = getIntent().getStringExtra("mRemark");
        if (null != mRemark && !TextUtils.isEmpty(mRemark)) {
            SimpleDialog.showSimpleRemarkWithTitleDialog(this, "实名认证失败原因", mRemark);
        }

        okHttpCommon = new OkHttpCommon();

        ZIMFacade.install(this);
    }

    @Override
    protected void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick());
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.idCardFrontRL, R.id.idCardVersoRL, R.id.idPhotoRL, R.id.submitBTN})
    public void myOnClick(View view) {
        switch (view.getId()) {
            case R.id.submitBTN:
                String idcard = idCardNoET.getText().toString();
                String name = nameET.getText().toString();
                /*if (checkData(mFrontPhoto, mVersoPhoto, mHandheldPhoto, idcard, name)) {
//                    String images = mFrontPhoto + "," + mVersoPhoto + "," + mHandheldPhoto;
                    String images = mFrontPhoto + "," + mVersoPhoto + "," + mVersoPhoto;
                    SimpleDialog.showLoadingHintDialog(this, 4);
                    HttpParameterUtil.getInstance().requestCertificationMemberInfo(name, idcard, images, mHandler);
                }*/

                if (name.isEmpty()) {
                    ToastUtils.showToast(R.string.input_truename);
                    return;
                }
                if (idcard.isEmpty()) {
                    ToastUtils.showToast(R.string.input_idcardno);
                    return;
                }

                getBizCode(name, idcard);

                break;
            case R.id.idCardFrontRL:
                mPhotoType = 1;
                showSelPopupWind(view, 1);
                break;
            case R.id.idCardVersoRL:
                mPhotoType = 2;
                showSelPopupWind(view, 1);
                break;
            case R.id.idPhotoRL:
                mPhotoType = 3;
                showSelPopupWind(view, 1);
                break;
            default:
                break;
        }
    }

    private void getBizCode(String name, String idCard) {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("name", name);
        params.put("idcard", idCard);
        String metaInfo = ZIMFacade.getMetaInfos(this);
        params.put("metaInfo", metaInfo);

        okHttpCommon.postLoadData(this, ConstantsUrl.URL_FACE_GET_BIZCODE, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                toast("认证失败");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", getString(R.string.net_error)));
                    return;
                }

                bizCode = jsonObject.get("data").getAsString();
                Log.d("huiger", "bizcode = " + bizCode);
//                postUrl(bizCode);
                aliyunAuth(bizCode);
            }
        });
    }

    public void toast(String msg) {
        ToastUtils.showToast(msg);
    }

//    private void postUrl(String bizCode) {
//        HashMap<String, String> params = CommonUtils.createParams();
//        params.put("bizCode", bizCode);
//        okHttpCommon.postLoadData(this, ConstantsUrl.URL_FACE_GET_URL, params, new CallbackCommon() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                toast("认证失败");
//            }
//
//            @Override
//            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
//                if (jsonObject.get("status").getAsInt() != 1) {
//                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", getString(R.string.net_error)));
//                    return;
//                }
//
//                String url = jsonObject.get("data").getAsString();
//                Log.d("huiger", "url = " + url);
//                faceAuth(bizCode, url);
//            }
//        });
//    }

//    private void faceAuth(String certifyId, String url) {
//        JSONObject requestInfo = new JSONObject();
//        requestInfo.put("bizCode", initCode);
//        requestInfo.put("url", url);
//        requestInfo.put("certifyId", certifyId);
//
//        ServiceFactory.build()
//                .startService(this, requestInfo, new ICallback() {
//                    @Override
//                    public void onResponse(Map<String, String> response) {
//                        String responseCode = response.get("resultStatus");
//                        if ("9001".equals(responseCode) || "9000".equals(responseCode)) {
//                            // 9001需要等待回调/回前台查询认证结果
//                            waitForResult = true;
//                        }
//                        // 回调处理
//                        Log.d("huiger-msg", "IDCardInfoActivity -> onResponse: " + JSON.toJSONString(response));
//                    }
//                });
//    }

    /**
     * 阿里云人脸
     * @param certifyId
     */
    private void aliyunAuth(String certifyId){
        ZIMFacade zimFacade = ZIMFacadeBuilder.create(this);
        zimFacade.verify(certifyId, true, new ZIMCallback() {
            @Override
            public boolean response(ZIMResponse response) {
                if (null != response && 1000 == response.code) {
                    // 认证成功。
                    waitForResult = true;
                } else {
                    // 认证失败。
                    toast("认证失败");
                }
                return true;
            }
        });
    }


    /**
     * 处理回前台触发结果查询
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (waitForResult) {
            // 查询认证结果
            waitForResult = false;
            checkAuth();
        }
    }

    private void checkAuth() {
        HashMap<String, String> params = CommonUtils.createParams();
        params.put("bizCode", bizCode);
        okHttpCommon.postLoadData(this, ConstantsUrl.URL_FACE_CHECK_AUTH, params, new CallbackCommon() {
            @Override
            public void onFailure(Call call, IOException e) {
                toast("认证失败");
            }

            @Override
            public void onResponse(Call call, JsonObject jsonObject) throws IOException {
                if (jsonObject.get("status").getAsInt() != 1) {
                    ToastUtils.showToast(GsonUtils.parseJson(jsonObject, "msg", getString(R.string.net_error)));
                    return;
                }
                String result = jsonObject.get("data").getAsString();
                if("T".equals(result)) {
                    toast("认证成功");
                    setResult(RESULT_OK, new Intent());
                    finish();
                }else{
                    toast("认证失败");
                }

             }
        });
    }

    private boolean checkData(String sth_img, String con_img, String certificate_img,
                              String idcard, String name) {

        if (TextUtils.isEmpty(name)) {
            ToastUtils.showToast(getString(R.string.input_truename));
            return false;
        }

        if (TextUtils.isEmpty(idcard)) {
            ToastUtils.showToast(getString(R.string.input_idcardno));
            return false;
        }

        if (!StringUtils.isIDCard(idcard)) {
            ToastUtils.showToast("身份证号码错误");
            return false;
        }

        if (TextUtils.isEmpty(sth_img)) {
            ToastUtils.showToast(getString(R.string.photo_idcard_front_text));
            return false;
        }
        if (TextUtils.isEmpty(con_img)) {
            ToastUtils.showToast(getString(R.string.photo_idcard_verso_text));
            return false;
        }
//        if (TextUtils.isEmpty(certificate_img)) {
//            ToastUtils.showToast(getString(R.string.photo_credentials_text));
//            return false;
//        }

        return true;
    }

    @SuppressWarnings("unchecked")
    private void disposeData(Message msg) {

        switch (msg.what) {
            case ConstantHandler.WHAT_IMAGE_UPLOAD_SUCCESS:
                SimpleDialog.cancelLoadingHintDialog();
                String url = (String) msg.obj;
                if (mPhotoType == 1) {
                    mFrontPhoto = url;
                    idCardFrontSDV.setVisibility(View.VISIBLE);
                    CommonUtils.showImage(idCardFrontSDV, url);
                } else if (mPhotoType == 2) {
                    mVersoPhoto = url;
                    idCardVersoSDV.setVisibility(View.VISIBLE);
                    CommonUtils.showImage(idCardVersoSDV, url);
                } else {
                    mHandheldPhoto = url;
                    idPhotoSDV.setVisibility(View.VISIBLE);
                    CommonUtils.showImage(idPhotoSDV, url);
                }
                break;
            case ConstantHandler.WHAT_IMAGE_UPLOAD_FAIL:
                ToastUtils.showToast("上传失败, 请重新上传!");
                SimpleDialog.cancelLoadingHintDialog();
                break;
            case ConstantHandler.WHAT_CERTIIFICATION_MEMBER_INF_SUCCESS:
                SimpleDialog.cancelLoadingHintDialog();
                ToastUtils.showToast("提交成功");
                SharedPreferencesUtils.put(Constants.USER_AUTH_STATUS, "1");
                setResult(RESULT_OK);
                finish();
                break;
            case ConstantHandler.WHAT_CERTIIFICATION_MEMBER_INF_FAIL:
                SimpleDialog.cancelLoadingHintDialog();
                SimpleDialog.showTipDialog(this, (String) msg.obj, null);
                break;
            default:
                break;
        }
    }

    @Override
    public void takeSuccess(TResult tResult) {
        super.takeSuccess(tResult);
        String pathUrl = getTakeSuccessPath(tResult);
        String pic = CommonUtils.imageToBase64Binary(pathUrl);
        SimpleDialog.showLoadingHintDialog(IDCardInfoActivity.this, 4);
        HttpParameterUtil.getInstance().requestImageUpload(pic, mHandler);
    }

    private static class MyHandler extends Handler {

        private WeakReference<IDCardInfoActivity> mImpl;

        public MyHandler(IDCardInfoActivity mImpl) {
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
