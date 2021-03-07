package com.yimeng.base;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.yimeng.haidou.R;
import com.yimeng.utils.CommonUtils;
import com.huige.library.popupwind.PopupWindowUtils;
import com.yanzhenjie.permission.Action;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoImpl;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.TContextWrap;
import org.devio.takephoto.model.TImage;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.permission.InvokeListener;
import org.devio.takephoto.permission.PermissionManager;
import org.devio.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;
import java.util.List;

/**
 * Author : huiGer
 * Time   : 2018/9/8 0008 上午 10:46.
 * Desc   : 图片选择器
 */
public abstract class BaseTakePhotoFragment extends BaseFragment implements TakePhoto.TakeResultListener, InvokeListener {


    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    /**
     * 显示选择窗
     */
    public void showSelPopupWind(final View view, final int max) {
        CommonUtils.getPermission(getActivity(), new Action<List<String>>() {
            @Override
            public void onAction(List<String> data) {
                showPopup(view, max);
            }
        }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 显示选择窗
     */
    private void showPopup(View view, final int max){
        new PopupWindowUtils(getActivity(), R.layout.layout_picture_selector)
                .setLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setStyle(R.style.popupWindowAsBottom)
                .setOnClickListenerByViewId(R.id.picture_selector_take_photo_btn, new PopupWindowUtils.onPopupWindClickListener() {
                    @Override
                    public void onClick(View view) {
                        openCamera();
                    }
                })
                .setOnClickListenerByViewId(R.id.picture_selector_pick_picture_btn, new PopupWindowUtils.onPopupWindClickListener() {
                    @Override
                    public void onClick(View view) {
                        openPhotoPicker(max);
                    }
                })
                .setOnClickListenerByViewId(R.id.picture_selector_cancel_btn, null)
                .showAtLocation(getActivity(), view, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 打开相机
     */
    public void openCamera() {
        File file = new File(getActivity().getCacheDir().getAbsolutePath(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        getTakePhoto().onPickFromCapture(Uri.fromFile(file));

    }

    /**
     * 打开相册选择
     *
     * @param max 最大张数
     */
    public void openPhotoPicker(int max) {
        if (max < 1) {
            max = 1;
        }
        getTakePhoto().onPickMultiple(max);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(getActivity(), type, invokeParam, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        // 默认压缩
        CompressConfig config = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(1200).create();

        // 鲁班压缩
//            CompressConfig config = CompressConfig.ofLuban(new LubanOptions.Builder().setMaxSize(50 * 1024).setMaxHeight(800).setMaxWidth(800).create());
//            config.enableQualityCompress(false);
        takePhoto.onEnableCompress(config, false);

        return takePhoto;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void takeSuccess(TResult tResult) {

    }

    @Override
    public void takeFail(TResult tResult, String s) {

    }

    @Override
    public void takeCancel() {

    }

    /**
     * 获取选择的图片路径
     *
     * @param tResult
     * @return
     */
    public String getTakeSuccessPath(TResult tResult) {
        TImage resultImage = tResult.getImage();
        String pathUrl = resultImage.getCompressPath();
        if (TextUtils.isEmpty(pathUrl)) {
            pathUrl = resultImage.getOriginalPath();
        }
        return pathUrl;
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

}
