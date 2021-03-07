package com.yimeng.net;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.haidou.R;
import com.yimeng.dialog.SimpleDialog;
import com.yimeng.eventEntity.MainBtnChangeEvent;
import com.yimeng.interfaces.UploadImageCallBack;
import com.yimeng.jpush.TagAliasOperatorHelper;
import com.yimeng.utils.ActivityUtils;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.huige.library.utils.SharedPreferencesUtils;

import org.simple.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * OkHttp工具类
 */
public class OkHttpCommon {

    /**
     * get获取数据
     *
     * @param activity Activity
     * @param url      请求地址
     * @param map      请求参数
     * @param callback 回调接口
     */
    public void getLoadData(final Activity activity, String url, Map<String, String> map, final CallbackCommon callback) {
        //判断是否联网
        if (!NetWorkUtil.isNetworkAvailable(activity)) {
            Toast.makeText(activity, activity.getString(R.string.text_network_connected_error), Toast.LENGTH_SHORT).show();
            return;
        }

        //加载动画
        showLoading(activity);

        //拼数据
        StringBuilder stringBuffer = new StringBuilder();
        if (map != null) {
            try {
                int keyIndex = 0;
                for (String key : map.keySet()) {
                    if (keyIndex > 0) {
                        stringBuffer.append("&");
                    }
                    stringBuffer.append(String.format("%s=%s", key, URLEncoder.encode(map.get(key), "utf-8")));
                    keyIndex++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        url += "?" + stringBuffer.toString();
        Log.i("OkHttpCommon", url);

        //请求
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call mcall = okHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //停止加载动画
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeLoading();
                    }
                });

                //返回失败结果
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {

                //请求返回数据
                final String resultInfo = response.body().string();

                //进入UI线程
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //关闭加载动画
                        closeLoading();

                        //判断请求是否成功
                        if (!response.isSuccessful()) {
                            Toast.makeText(activity, "请求失败！", Toast.LENGTH_SHORT).show();
                        }

                        //返回成功结果
                        try {
                            JsonObject jsonObject = new JsonParser().parse(resultInfo).getAsJsonObject();
                            callback.onResponse(call, jsonObject.getAsJsonObject());
                        } catch (Exception e) {
                            Toast.makeText(activity, activity.getString(R.string.net_error), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }


    /**
     * 显示加载框
     */
    public static void showLoading(Activity activity) {
//        SimpleDialog.showLoadingHintDialog(activity, 1);
        SimpleDialog.showLoading(activity, null);
    }

    /**
     * 关闭加载层
     */
    public static void closeLoading() {
//        SimpleDialog.cancelLoadingHintDialog();
        SimpleDialog.hideLoading();
    }

    /**
     * get获取数据， 不包含任何UI操作
     *
     * @param url      请求地址
     * @param map      请求参数
     * @param callback 回调接口
     */
    public void getLoadDataNoUI(String url, Map<String, String> map, final Callback callback) {
        //拼数据
        StringBuffer stringBuffer = new StringBuffer();
        try {
            int keyIndex = 0;
            for (String key : map.keySet()) {
                if (keyIndex > 0) {
                    stringBuffer.append("&");
                }
                stringBuffer.append(String.format("%s=%s", key, URLEncoder.encode(map.get(key), "utf-8")));
                keyIndex++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        url += "?" + stringBuffer.toString();
        Log.i("OkHttpCommon", url);

        //请求
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder()
                .url(url);
        requestBuilder.method("GET", null);
        Request request = requestBuilder.build();
        Call mcall = okHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            }
        });
    }

    public boolean postLoadData(final Activity activity, final String url, final Map<String, String> map, final CallbackCommon callback) {
        return postLoadData(activity, url, map, callback, true);
    }

    /**
     * http请求
     * <p>
     * token失效时：请求 -> token失效 -》直接登录接口 -》再次请求
     *
     * @param activity
     * @param url
     * @param map
     * @param callback
     */
    public boolean postLoadData(final Activity activity, final String url, final Map<String, String> map, final CallbackCommon callback, boolean isShowLoading) {
        //判断是否联网
        if (!NetWorkUtil.isNetworkAvailable(activity)) {
//            Toast.makeText(activity, activity.getString(R.string.text_network_connected_error), Toast.LENGTH_SHORT).show();
            callback.onFailure(null, new IOException(activity.getString(R.string.text_network_connected_error)));
            return false;
        }

        //加载动画
        if (isShowLoading) {
            showLoading(activity);
        }

        //进入http链接
        postLoadDataNoUI(url, map, new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //关闭动画
                        closeLoading();
                        //返回结果
                        callback.onFailure(call, e);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                try {
                    //请求返回数据
                    final String resultInfo = response.body().string();
                    Log.i("postLoadData:", resultInfo);

                    final JsonObject jsonObject = new JsonParser().parse(resultInfo).getAsJsonObject();

                    //token 失效, session失效
//                    boolean isData = jsonObject.get("data") != null && !jsonObject.get("data").isJsonNull() && !jsonObject.get("data").isJsonObject() && jsonObject.get("data").getAsString().equals("2");
                    boolean isSession = jsonObject.get("status") != null && !jsonObject.get("status").isJsonNull() && jsonObject.get("status").getAsInt() == 2;

                    // msg = 0,1 账号在其他设备登录
                    // status = 2 失效
                    if (isSession) {
                        tokenExpire(activity, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                //再次发送请求
                                postLoadDataNoUI(url, map, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                    }

                                    @Override
                                    public void onResponse(final Call callNoUI, final Response responseNoUI) throws IOException {
                                        final String resultInfoNoUI = responseNoUI.body().string();

                                        final JsonObject jsonObjectNoUI = new JsonParser().parse(resultInfoNoUI).getAsJsonObject();

                                        //进入UI线程
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //关闭加载动画
                                                closeLoading();

                                                //判断请求是否成功
                                                if (!responseNoUI.isSuccessful()) {
                                                    Toast.makeText(activity, "请求失败！", Toast.LENGTH_SHORT).show();
                                                }

                                                //返回成功结果
                                                try {
                                                    callback.onResponse(callNoUI, jsonObjectNoUI);
                                                } catch (Exception e) {
                                                    Toast.makeText(activity, activity.getString(R.string.net_error), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }

                    //进入UI线程
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //关闭加载动画
                            closeLoading();

                            //判断请求是否成功
                            if (!response.isSuccessful()) {
                                Toast.makeText(activity, "请求失败！", Toast.LENGTH_SHORT).show();
                            }

                            //返回成功结果
                            try {
                                callback.onResponse(call, jsonObject);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(activity, activity.getString(R.string.net_error), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    closeLoadingUI(activity);
                }
            }
        });

        return true;
    }

    /**
     * post 获取数据请求
     *
     * @param url      请求地址
     * @param map      请求参数
     * @param callback 回调接口
     */
    public void postLoadDataNoUI(String url, Map<String, String> map, final Callback callback) {
        //构建请求参数
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (String key : map.keySet()) {
            if (!TextUtils.isEmpty(map.get(key))) {
                formBodyBuilder.add(key, map.get(key));
            }
        }
        RequestBody requestBody = formBodyBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Log.d("msg", "OkHttpCommon -> postLoadDataNoUI: " + "请求地址: " + url);
        Log.d("msg", "OkHttpCommon -> postLoadDataNoUI: " + "请求参数: " + map.toString());

//        OkHttpClient okHttpClient = new OkHttpClient();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .proxy(Proxy.NO_PROXY)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            }
        });
    }


    public void uploadVideo(final Activity activity, String url, String fileName, String filePath, String title, final Callback callback) {
        //判断是否联网
        if (!NetWorkUtil.isNetworkAvailable(activity)) {
            Toast.makeText(activity, activity.getString(R.string.text_network_connected_error), Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(5, TimeUnit.MINUTES).build();
        File file = new File(filePath);

        if (!file.exists() || !file.isFile()) {
            Toast.makeText(activity, "上传失败!", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName, RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .build();

        Request request = new Request.Builder()
                .url(url + "?token=" + CommonUtils.getToken() + "&title=" + title)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call, response);
            }
        });
    }

    /**
     * token失效
     *
     * @param callback
     */
    private void tokenExpire(final Activity activity, final Callback callback) {
        String mobileNo = (String) SharedPreferencesUtils.get(Constants.USER_MOBILE, "");
        String bindNo = (String) SharedPreferencesUtils.get(Constants.MOBILE_ID, "");
        if (TextUtils.isEmpty(mobileNo) || TextUtils.isEmpty(bindNo)) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    jumpLoginActivity(activity);
                }
            });
            return;
        }

        Map<String, String> tokenMap = new HashMap<String, String>();
        tokenMap.put("mobileNo", mobileNo);
        tokenMap.put("bindNo", bindNo);
        postLoadDataNoUI(ConstantsUrl.LOGIN_MOBILE_URL, tokenMap, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //关闭动画
                        jumpLoginActivity(activity);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resultInfo = response.body().string();
                JsonObject jsonObject = new JsonParser().parse(resultInfo).getAsJsonObject();
                if (jsonObject.get("status").isJsonNull() || jsonObject.get("status").getAsInt() != 1) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //关闭动画
                            jumpLoginActivity(activity);
                        }
                    });
                    return;
                }

                // 设置推送别名
                TagAliasOperatorHelper.getInstance().bindJPush(jsonObject.get("data").getAsJsonObject().get("memberNo").getAsString());

                //回调
                callback.onResponse(call, response);
            }
        });
    }

    private boolean tokenErrorDialogIsShow = false;

    private synchronized void jumpLoginActivity(final Activity activity) {
        EventBus.getDefault().post(new MainBtnChangeEvent(MainBtnChangeEvent.FragmentType.home));
        if (TextUtils.isEmpty(CommonUtils.getToken())) {
            SharedPreferencesUtils.put(Constants.IS_LOGIN, false);
            ActivityUtils.getInstance().jumpLoginActivity();
            return;
        }
        if (!tokenErrorDialogIsShow) {
            SimpleDialog.showTipDialog(activity, activity.getString(R.string.app_token_error), new SimpleDialog.OnSimpleDialogClickListener() {
                @Override
                public void onClick(View v, AlertDialog dialog) {
                    tokenErrorDialogIsShow = false;
                    closeLoading();
                    CommonUtils.cleanMember();
                    ActivityUtils.getInstance().jumpLoginActivity();
                    SharedPreferencesUtils.put(Constants.IS_LOGIN, false);
                }
            });
            tokenErrorDialogIsShow = true;
        }

    }

    /**
     * UI线程中关闭加载层
     *
     * @param activity
     */
    public void closeLoadingUI(Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeLoading();
            }
        });
    }

    /**
     * 上传文件
     */
    public void uploadFile(final Activity activity, @NonNull List<String> filePathList, final UploadImageCallBack callback) {
        //判断是否联网
        if (!NetWorkUtil.isNetworkAvailable(activity)) {
            Toast.makeText(activity, activity.getString(R.string.text_network_connected_error), Toast.LENGTH_SHORT).show();
            return;
        }

        // 空文件
        if (filePathList.isEmpty()) return;

        //加载动画
        showLoading(activity);

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)//表单类型
                .addFormDataPart("id", System.currentTimeMillis() + "");
//        File file = new File(filePath);
//        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        builder.addFormDataPart("uploadToImportFile", file.getName(), requestBody);

        MediaType mediaType = MediaType.parse("multipart/form-data; charset=utf-8");
        // 循环添加多文件
        for (String path : filePathList) {
            File file = new File(path);
            builder.addFormDataPart("uploadToImportFile", file.getName(), RequestBody.create(mediaType, file));
        }


        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(ConstantsUrl.UPLOAD_IMG_FROM_URL)
                .post(requestBody)
                .build();
        new OkHttpClient.Builder().build().newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //关闭动画
                                closeLoading();
                                callback.uploadFail(e.getMessage());
                            }
                        });

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                closeLoading();
                            }
                        });
                        //请求返回数据
                        final String resultInfo = response.body().string();
                        Log.i("postLoadData:", resultInfo);
                        final JsonObject jsonObject = new JsonParser().parse(resultInfo).getAsJsonObject();
                        if (jsonObject.get("success").getAsBoolean()) {
                            // 上传成功, 返回图片地址
                            callback.uploadSuccess(GsonUtils.parseJson(jsonObject, "data", ""));
                        }
                    }
                });
    }

}
