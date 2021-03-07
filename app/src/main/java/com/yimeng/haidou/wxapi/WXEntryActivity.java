package com.yimeng.haidou.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.yimeng.config.Constants;
import com.yimeng.config.EventTags;
import com.yimeng.haidou.R;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
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
 *  Desc   : 登录分享回调
 * </pre>
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {


	// IWXAPI 是第三方app和微信通信的openapi接口
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

    private long oldTime = 0;

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
        // 2秒内回调只出发一次
        if (new Date().getTime() - oldTime < 2000) {
            return;
        }
        oldTime = new Date().getTime();

        // 1:第三方授权， 2:分享
        if(resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            if(resp.errCode == BaseResp.ErrCode.ERR_OK) {
                EventBus.getDefault().post(((SendAuth.Resp) resp).code,EventTags.WECHAT_AUTH_RESULT);
            }else{
                EventBus.getDefault().post("",EventTags.WECHAT_AUTH_RESULT);
            }
            return;
        }else if(resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
            EventBus.getDefault().post(true, EventTags.WECHAT_SHARE_RESULT);
        }

	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}