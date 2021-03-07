package com.yimeng.utils;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;

import com.yimeng.haidou.App;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/6/13 10:30 AM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 语音播报
 * </pre>
 */
public class ChineseToSpeech {
    private TextToSpeech textToSpeech;
    private Handler mHandler = new Handler();

    private ChineseToSpeech() {
        initTTS();
    }

    private static ChineseToSpeech mChineseToSpeech = null;
    public static ChineseToSpeech getInstance(){
        if(mChineseToSpeech == null){
            synchronized(ChineseToSpeech.class){
                if(mChineseToSpeech == null){
                    mChineseToSpeech = new ChineseToSpeech();
                }
            }
        }
        return mChineseToSpeech;
    }

    private void initTTS() {
        this.textToSpeech = new TextToSpeech(App.getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
//                    // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
//                    textToSpeech.setPitch(1.0f);
//                    // 设置语速
//                    textToSpeech.setSpeechRate(0.3f);
//
//                    int result = textToSpeech.setLanguage(Locale.CHINA);
//                    if (result == TextToSpeech.LANG_MISSING_DATA
//                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
//                        ToastUtils.showToast("不支持朗读功能");
//                    }
                    if(textToSpeech == null) {
                        textToSpeech = new TextToSpeech(App.getContext(), this);
                    }

                    textToSpeech.setPitch(1.0f);//方法用来控制音调
                    textToSpeech.setSpeechRate(1.0f);//用来控制语速

                    //判断是否支持下面两种语言
                    int result1 = textToSpeech.setLanguage(Locale.US);
                    int result2 = textToSpeech.setLanguage(Locale.SIMPLIFIED_CHINESE);
                    boolean a = (result1 == TextToSpeech.LANG_MISSING_DATA || result1 == TextToSpeech.LANG_NOT_SUPPORTED);
                    boolean b = (result2 == TextToSpeech.LANG_MISSING_DATA || result2 == TextToSpeech.LANG_NOT_SUPPORTED);

                    Log.i("zhh_tts", "US支持否？--》" + a +
                            "\nzh-CN支持否》--》" + b);
                }
            }
        });
    }


    public void speech(final String text) {
//        if(!ismServiceConnectionUsable(textToSpeech)) {
//            initTTS();
//            return;
//        }
        Log.d("msg", "ChineseToSpeech -> speech: " + text);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        }, 1000);
    }

    public void destroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    /**
     * TTS初始化之后有时会无法播放语音。
     * 从打印日志看failed: not bound to TTS engine
     * 找到源代码打印处
     * if (mServiceConnection == null) {
     *      Log.w(TAG, method + " failed: not bound to TTS engine");
     *   return errorResult;
     *  }
     *  通过反射判断mServiceConnection是否为空
     * @param tts
     * @return true 可用
     */
    public static boolean ismServiceConnectionUsable(TextToSpeech tts) {

        boolean isBindConnection = true;
        if (tts == null){
            return false;
        }
        Field[] fields = tts.getClass().getDeclaredFields();
        for (int j = 0; j < fields.length; j++) {
            fields[j].setAccessible(true);
            if (TextUtils.equals("mServiceConnection",fields[j].getName()) && TextUtils.equals("android.speech.tts.TextToSpeech$Connection",fields[j].getType().getName())) {
                try {
                    if(fields[j].get(tts) == null){
                        isBindConnection = false;
                        Log.e("ChineseToSpeech", "*******反射判断 TTS -> mServiceConnection == null*******");
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return isBindConnection;
    }
}
