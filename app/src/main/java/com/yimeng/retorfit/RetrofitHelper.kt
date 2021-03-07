package com.yimeng.retorfit

import android.content.Context
import android.util.Log
import com.blankj.utilcode.util.ReflectUtils
import com.yimeng.haidou.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.huige.library.utils.ToastUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLHandshakeException

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-05 18:03.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
object RetrofitHelper {

    private var sRetrofit: Retrofit? = null
    private var sOkHttpClient: OkHttpClient? = null
    private var mGson: Gson? = null

    fun init(context: Context) {
        if (sRetrofit == null) {
            sRetrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.Host)// 必须以 “/” 结束
                    .client(client(context))
                    .addConverterFactory(GsonConverterFactory.create(buildGson()))
                    .build()
        }
    }

    fun buildGson(): Gson {
        if (mGson == null) {
            mGson = GsonBuilder()
//                    .registerTypeAdapter(Int::class.java, IntegerDefaultAdapter())
//                    .registerTypeAdapter(Double::class.java, DoubleDefault0Adapter())
//                    .registerTypeAdapter(Long::class.java, LongDefault0Adapter())
                    .create()
        }
        return mGson!!
    }

    fun client(context: Context): OkHttpClient {
        if (sOkHttpClient == null) {
            val builder = OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
//                    .sslSocketFactory(createSSLSocketFactory())

            if (BuildConfig.DEBUG) {
                val httpLoggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                    private val mMessage = StringBuilder()
                    override fun log(message: String) {
                        // 请求或者响应开始
                        try {
                            if (message.startsWith("-->") && !message.startsWith("--> END")) {
                                mMessage.setLength(0)
                            }
//                        // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
                            mMessage.append(message + "\n")
                            // 响应结束，打印整条日志
                            if (message.startsWith("<-- END HTTP")) {
                                Log.d("huiGer", mMessage.toString())
                            }
                        } catch (e: Exception) {
                        }

                    }
                })
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                builder.addNetworkInterceptor(httpLoggingInterceptor)
            }
            sOkHttpClient = builder
                    //          .addInterceptor(LogInterceptor())
                    //          .cookieJar(CookiesManager(context))
                    .build()
        }
        return sOkHttpClient!!
    }


    fun <T> service(service: Class<T>): T {
        return sRetrofit!!.create(service) as T
    }

    fun toDo(): ApiService {
        return service(ApiService::class.java)
    }


    /**
     * 是否请求成功
     * @param errAsToast 失败是否弹消息提示
     */
    @Synchronized
    fun isSuccess(response: Response<*>, errAsToast: Boolean = true): Boolean {
        val body = response.body()
        if (body == null) {
            if (errAsToast) ToastUtils.showToast("返回数据为空")
            return false
        }

        try {
            val reflect = ReflectUtils.reflect(body)
            val status = reflect.field("status").get<Int>()
            if(status == 1) {
                return true
            }else{
                val msg = reflect.field("msg").get<String>()
                if (errAsToast) ToastUtils.showToast(msg)
                return false
            }

        }catch (e:Exception){
            return false
        }
    }

    /**
     * 错误信息
     */
    fun getErrMsg(t: Throwable): String {
        t.printStackTrace()
        return when (t) {
            is HttpException -> "网络异常"
            is JsonParseException -> "解析错误"
            is ConnectException -> "网络竟然崩溃了"
            is TimeoutException -> "连接超时"
            is SSLHandshakeException -> "证书验证失败"
            else -> "网络异常"
        }
    }

}