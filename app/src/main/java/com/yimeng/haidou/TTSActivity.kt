package com.yimeng.haidou

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.SeekBar
import com.yimeng.utils.ChineseToSpeech
import com.huige.library.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_tts.*
import java.util.*

class TTSActivity : AppCompatActivity() {

    //创建自带语音对象
    private val textToSpeech: TextToSpeech by lazy {
        TextToSpeech(App.getContext(), TextToSpeech.OnInitListener {
            if(it == TextToSpeech.SUCCESS) {
                textToSpeech.setPitch(1.0f)//方法用来控制音调
                textToSpeech.setSpeechRate(1.0f)//用来控制语速

                //判断是否支持下面两种语言
                var result1 = textToSpeech.setLanguage(Locale.US)
                var result2 = textToSpeech.setLanguage(Locale.SIMPLIFIED_CHINESE)
                var a = (result1 == TextToSpeech.LANG_MISSING_DATA || result1 == TextToSpeech.LANG_NOT_SUPPORTED)
                var b = (result2 == TextToSpeech.LANG_MISSING_DATA || result2 == TextToSpeech.LANG_NOT_SUPPORTED)

                Log.i("zhh_tts", "US支持否？--》" + a +
                        "\nzh-CN支持否》--》" + b)
            }else{
                ToastUtils.showToast("数据丢失或不支持")
            }
        })
    }

    private var pitch = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tts)

//        var chineseToSpeech = ChineseToSpeech()
        btn_submit.setOnClickListener {

            var str = et_amt.text.toString().trim()
            if(str.isNotEmpty()){

//                startAuto(str)
                ChineseToSpeech.getInstance().speech(str)
//                var chineseToSpeech = ChineseToSpeech()

            }


        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                pitch = p1.toFloat() * 0.01f
                Log.d("msg", "p1 -> : $pitch")
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

    }
    private fun startAuto(data :String) {
        // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
//        textToSpeech.setPitch(1.0f)
        textToSpeech.setPitch(pitch)
        // 设置语速
        textToSpeech.setSpeechRate(0.3f)
        textToSpeech.speak(data,//输入中文，若不支持的设备则不会读出来
                TextToSpeech.QUEUE_FLUSH, null)
    }

    override fun onStop() {
        super.onStop()
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}
