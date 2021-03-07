package com.yimeng.haidou.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.yimeng.config.Constants;
import com.yimeng.config.EventTags;
import com.yimeng.haidou.R;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.simple.eventbus.EventBus;

import java.util.Date;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/5/23 2:42 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 支付回调
 * </pre>
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        finish();
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, Constants.WX_APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    private long oldTime = 0;

    @Override
    public void onResp(BaseResp resp) {

        // 2秒内回调只出发一次
        if (new Date().getTime() - oldTime < 2000) {
            return;
        }
        oldTime = new Date().getTime();

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX && resp.errCode == BaseResp.ErrCode.ERR_OK) {
            // 设置购买成功
            sendBroadcast(new Intent().setAction(Constants.ACTION_PAYMENT_SUCCESS));
            postEvent(resp, true);

        } else {
            sendBroadcast(new Intent().setAction(Constants.ACTION_PAYMENT_FAIL));
            postEvent(resp, false);
        }
    }

    private void postEvent(BaseResp resp, boolean result){
        PayResp payResp = (PayResp) resp;
        String extData = payResp.extData;
        if(TextUtils.isEmpty(extData)) {
            EventBus.getDefault().post(result, EventTags.WECHAT_PAY_RESULT);
        }else{
            EventBus.getDefault().post(result, extData);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}