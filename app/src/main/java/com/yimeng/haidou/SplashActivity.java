package com.yimeng.haidou;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lrad.adManager.LanRenManager;
import com.lrad.adManager.LoadAdError;
import com.lrad.adSource.ISplashProvider;
import com.lrad.adlistener.ILanRenSplashAdListener;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private ViewGroup mAdContainer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAdContainer = findViewById(R.id.ad_container);

        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermission();
        } else {
            requestAd();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<>();
        if (!(checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(android.Manifest.permission.READ_PHONE_STATE);
        }

        if (!(checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }

        // 快手SDK所需相关权限，存储权限，此处配置作用于流量分配功能，关于流量分配，详情请咨询商务;如果您的APP不需要快手SDK的流量分配功能，则无需申请SD卡权限
        if (!(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        // 如果需要的权限都已经有了，那么直接调用SDK
        if (lackedPermission.size() == 0) {
            requestAd();
        } else {
            // 否则，建议请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {
            requestAd();
        } else {
            Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 请求广告
     */
    private void requestAd() {
        LanRenManager.getInstance().loadSplash(this, "20000059", new ILanRenSplashAdListener() {
            @Override
            public void onAdError(LoadAdError loadAdError) {
                actionHome(1000);
                Log.d("ifmvo", "onAdError: " + loadAdError.getCode() + ", " + loadAdError.getMessage());
            }

            @Override
            public void onAdClick() { }

            @Override
            public void onAdClose() {
                Log.d("ifmvo", "onAdClose");
                actionHome(0);
            }

            @Override
            public void onAdExpose() {
                Log.d("ifmvo", "onAdExpose");
            }

            @Override
            public void onAdLoad(ISplashProvider iSplashProvider) {
                Log.d("ifmvo", "onAdLoad");
                iSplashProvider.show(SplashActivity.this, mAdContainer);
            }

            @Override
            public void onAdLoadList(List<ISplashProvider> list) {
                Log.d("ifmvo", "onAdLoadList," + list.size());
            }

            @Override
            public void onAdClickSkip() {
                actionHome(0);
                Log.d("ifmvo", "onAdClickSkip");
            }
        });
    }

    private void actionHome(int millLong) {
        mAdContainer.postDelayed(() -> startActivity(new Intent(SplashActivity.this, MainActivity.class)), millLong);
    }

    /**
     * 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return super.onKeyDown(keyCode, event);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

