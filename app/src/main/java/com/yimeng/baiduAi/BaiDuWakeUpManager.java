package com.yimeng.baiduAi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.baidu.aip.asrwakeup3.core.recog.IStatus;
import com.baidu.aip.asrwakeup3.core.recog.MyRecognizer;
import com.baidu.aip.asrwakeup3.core.recog.listener.ChainRecogListener;
import com.baidu.aip.asrwakeup3.core.recog.listener.MessageStatusRecogListener;
import com.baidu.aip.asrwakeup3.core.wakeup.MyWakeup;
import com.baidu.aip.asrwakeup3.core.wakeup.SimpleWakeupListener;
import com.baidu.aip.asrwakeup3.core.wakeup.WakeUpResult;
import com.baidu.speech.asr.SpeechConstant;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DigitalDialogInput;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.haidou.mine.CustomerServiceActivity;
import com.yimeng.haidou.mine.FeedbackActivity;
import com.yimeng.haidou.mine.IDCardInfoActivity;
import com.yimeng.haidou.mine.MineShopDetailActivity;
import com.yimeng.haidou.mine.MoreSettingActivity;
import com.yimeng.haidou.mine.MsgActivity;
import com.yimeng.haidou.mine.MyCouponActivity;
import com.yimeng.haidou.mine.MyFavoriteActivity;
import com.yimeng.haidou.mine.MyInviteActivity;
import com.yimeng.haidou.mine.UserInfoActivity;
import com.yimeng.haidou.offline.SelPartnerTypeActivity;
import com.yimeng.haidou.order.OrderActivity;
import com.yimeng.entity.Member;
import com.yimeng.eventEntity.MainBtnChangeEvent;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.huige.library.utils.log.LogUtils;

import org.simple.eventbus.EventBus;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/4/9 4:42 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 百度语音唤醒，识别功能
 * </pre>
 */
public class BaiDuWakeUpManager {

    private static MyRecognizer myRecognizer;
    private static MyWakeup myWakeup;


    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == IStatus.STATUS_FINISHED && msg.arg2 == 1) {
                String resultStr = (String) msg.obj;
                if(resultStr.contains("我的店铺")) {
                    openMineShop();
                }else if(resultStr.contains("我的订单")) {
                    openMineOrder();
                }else if(resultStr.contains("任务")) {
                    EventBus.getDefault().post(new MainBtnChangeEvent(MainBtnChangeEvent.FragmentType.task));
                    ActivityUtils.getInstance().jumpMainActivity();
                }else if(resultStr.contains("分类")) {
                    EventBus.getDefault().post(new MainBtnChangeEvent(MainBtnChangeEvent.FragmentType.goods_classify));
                    ActivityUtils.getInstance().jumpMainActivity();
                }else if(resultStr.contains("我的代金券")) {
                    ActivityUtils.getInstance().jumpActivity(MyCouponActivity.class);
                }else if(resultStr.contains("我的收藏")) {
                    ActivityUtils.getInstance().jumpActivity(MyFavoriteActivity.class);
                }else if(resultStr.contains("我的伙伴")) {
                    ActivityUtils.getInstance().jumpActivity(MyInviteActivity.class);
                }else if(resultStr.contains("我的消息")) {
                    ActivityUtils.getInstance().jumpActivity(MsgActivity.class);
                }else if(resultStr.contains("我的地址")) {
                    ActivityUtils.getInstance().jumpAddressManager(false);
                }else if(resultStr.contains("帮助")) {
                    ActivityUtils.getInstance().jumpH5Activity("使用帮助", ConstantsUrl.APP_USE_HELP_URL);
                }else if(resultStr.contains("联系我们")
                        || resultStr.contains("客服")) {
                    ActivityUtils.getInstance().jumpActivity(CustomerServiceActivity.class);
                }else if(resultStr.contains("意见反馈")
                        || resultStr.contains("投诉")
                        || resultStr.contains("建议")) {
                    ActivityUtils.getInstance().jumpActivity(FeedbackActivity.class);
                }else if(resultStr.contains("设置") ) {
                    ActivityUtils.getInstance().jumpActivity(MoreSettingActivity.class);
                }else if(resultStr.contains("我的资料")) {
                    ActivityUtils.getInstance().jumpActivity(UserInfoActivity.class);
                }else if(resultStr.contains("我的二维码")) {
                    ActivityUtils.getInstance().jumpMyQrCode(1);
                }else if(resultStr.contains("商家二维码")) {
                    ActivityUtils.getInstance().jumpMyQrCode(2);
                }




                LogUtils.d("baiDuAi", resultStr);
            }
        }
    };



    public synchronized static void getInstance(final Activity context){

        if(myWakeup != null) {
            return;
        }

        final ChainRecogListener chainRecogListener = new ChainRecogListener();
        chainRecogListener.addListener(new MessageStatusRecogListener(mHandler));
        myRecognizer = new MyRecognizer(context, chainRecogListener);


        myWakeup = new MyWakeup(context, new SimpleWakeupListener() {
            @Override
            public void onSuccess(String word, WakeUpResult result) {
                super.onSuccess(word, result);

                Map<String, Object> params = new LinkedHashMap<String, Object>();
                params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
                params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
                params.put(SpeechConstant.PID, 1536);
                // 音效
                params.put(SpeechConstant.SOUND_START, R.raw.bdspeech_recognition_start);
                params.put(SpeechConstant.SOUND_END, R.raw.bdspeech_speech_end);
                params.put(SpeechConstant.SOUND_SUCCESS, R.raw.bdspeech_recognition_success);
                params.put(SpeechConstant.SOUND_ERROR, R.raw.bdspeech_recognition_error);
                params.put(SpeechConstant.SOUND_CANCEL, R.raw.bdspeech_recognition_cancel);

                DigitalDialogInput input = new DigitalDialogInput(myRecognizer, chainRecogListener, params);
                BaiduASRDigitalDialog.setInput(input); // 传递input信息，在BaiduASRDialog中读取,

                Intent intent = new Intent(context, BaiduASRDigitalDialog.class);
//                context.startActivityForResult(intent, ConstantHandler.BAIDU_REQUEST_CODE);
                context.startActivity(intent);
            }

        });

        // 唤醒
        Map<String, Object> params = new TreeMap<String, Object>();
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        params.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp.bin");
        myWakeup.start(params);


    }


    /**
     * 打开我的店铺
     */
    private static void openMineShop(){
        Member member = CommonUtils.getMember();
        if (member == null) {
            ActivityUtils.getInstance().jumpLoginActivity();
            return;
        }
        if(member.getJob().equals("open")) {
            // 已开店
            ActivityUtils.getInstance().jumpActivity(MineShopDetailActivity.class);
        }else{//未开店
            if (!CommonUtils.checkAuth()) { // 先实名认证
                ActivityUtils.getInstance().jumpActivity(IDCardInfoActivity.class);
                return;
            }
            ActivityUtils.getInstance().jumpActivity(SelPartnerTypeActivity.class);
        }
    }

    /**
     * 打开我的订单
     */
    private static void openMineOrder() {
        if(!CommonUtils.checkLogin()) {
            ActivityUtils.getInstance().jumpLoginActivity();
            return;
        }
        ActivityUtils.getInstance().jumpActivity(OrderActivity.class);
    }

}
