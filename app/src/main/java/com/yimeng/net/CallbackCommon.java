package com.yimeng.net;

import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.Call;

/**
 * Http请求回调接口
 */
public interface CallbackCommon {

    /**
     * 接口调用失败
     *
     * @param call
     * @param e
     */
    void onFailure(Call call, IOException e);

    /**
     * 接口调用成功
     *
     * @param call
     * @param jsonObject
     * @throws IOException
     */
    void onResponse(Call call, JsonObject jsonObject) throws IOException;
}
