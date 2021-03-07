package com.yimeng.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.yimeng.config.Constants;
import com.yimeng.haidou.mine.MsgActivity;
import com.yimeng.utils.ChineseToSpeech;
import com.huige.library.utils.ToastUtils;
import com.huige.library.utils.log.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * Author : huiGer
 * Time   : 2018/6/29 0029 下午 07:37.
 * Desc   : 极光推送
 */
public class JPushMessageReceiver extends BroadcastReceiver {

    private static final String TAG = "JIGUANG-Example";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            LogUtils.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                LogUtils.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                LogUtils.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                processCustomMessage(context, bundle);
            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                LogUtils.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                LogUtils.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
                ToastUtils.showToast("您有1条新消息");
                String extraStr = bundle.getString(JPushInterface.EXTRA_EXTRA);
                JSONObject json = new JSONObject(extraStr);

//                int msgRemind = (int) SharedPreferencesUtils.get(Constants.APP_GET_MSG_REMIND, 0);
//                if (msgRemind == 0) { // 用户设置
//                    if(!json.isNull("sound")) {
//                        String sound = json.getString("sound");
//                        if(sound.equals("help_notice")) {
//                            // 播放音频
//                            PushSetUtils.getInstance().playSound(R.raw.sos);
//                        } else if(sound.equals("goHelp")) {
//                            PushSetUtils.getInstance().playSound(R.raw.help);
//                        }
//                    }
//                }

//                测试播报
//                new ChineseToSpeech().speech("促销王，到账，200元");

                if (json.getString("type").equals("shop_pass")) {
                    // 店铺通过，刷新数据
                    context.sendBroadcast(new Intent().setAction(Constants.ACTION_REFRESH_MEMBER_INFO));
                }else if(json.getString("type").equals("income")) {
                    // 促销王到账语音播报
                    String incomeStr = bundle.getString(JPushInterface.EXTRA_ALERT);
                    ChineseToSpeech.getInstance().speech(incomeStr);
                }



            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                LogUtils.d(TAG, "[MyReceiver] 用户点击打开了通知");

                String extraStr = bundle.getString(JPushInterface.EXTRA_EXTRA);
                JSONObject json = new JSONObject(extraStr);

                //打开普通消息列表Activity
                Intent i = new Intent(context, MsgActivity.class);
                i.putExtras(bundle);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                LogUtils.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                LogUtils.d(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                LogUtils.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }
    }


    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    LogUtils.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    LogUtils.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.get(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
//        if (MainActivity.isForeground) {
//            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//            if (!ExampleUtil.isEmpty(extras)) {
//                try {
//                    JSONObject extraJson = new JSONObject(extras);
//                    if (extraJson.length() > 0) {
//                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//                    }
//                } catch (JSONException e) {
//
//                }
//
//            }
//            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
//        }

    }
}
