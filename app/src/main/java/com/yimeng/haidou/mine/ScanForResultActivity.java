package com.yimeng.haidou.mine;

import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

import com.yimeng.base.BaseActivity;
import com.yimeng.haidou.R;
import com.yimeng.widget.MyToolBar;
import com.yimeng.widget.zxingnew.qrcode.core.QRCodeView;
import com.yimeng.widget.zxingnew.qrcode.zxing.ZXingView;
import com.huige.library.utils.ToastUtils;

import butterknife.Bind;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/9/23 0023 下午 03:01.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 扫一扫, 本页面不做操作, 将返回的数据返回就好
 * </pre>
 */
public class ScanForResultActivity extends BaseActivity implements QRCodeView.Delegate {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.zxingview)
    ZXingView zxingview;


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_scan_for_result;
    }

    @Override
    protected void init() {
        zxingview.setDelegate(this);
        zxingview.showScanRect();
        zxingview.startSpot();
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
        zxingview.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        zxingview.startCamera();
    }

    @Override
    protected void onStop() {
        zxingview.stopCamera();
        super.onStop();
    }

    /**
     * 扫描成功
     *
     * @param result
     */
    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.d("msg", "ScanForResultActivity -> onScanQRCodeSuccess: " + result);
        vibrate();

        Intent intent = new Intent();
        intent.putExtra("result", result);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 扫描失败
     */
    @Override
    public void onScanQRCodeOpenCameraError() {
        ToastUtils.showToast("扫码失败");
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
}
