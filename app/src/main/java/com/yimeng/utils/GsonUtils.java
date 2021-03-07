package com.yimeng.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Author : huiGer
 * Time   : 2018/8/11 0011 下午 04:36.
 * Desc   :
 */
public class GsonUtils {

    private static Gson gson = new Gson();

    public static Gson getGson() {
        return gson;
    }


    /**
     * 解析字符
     * @param jsonObject  jsonObject
     * @param key         key
     * @param defaultStr  默认字符
     * @return
     */
    public static String parseJson(JsonObject jsonObject, String key, String defaultStr){
        return jsonObject.get(key) == null || jsonObject.get(key).isJsonNull() ? defaultStr : jsonObject.get(key).getAsString();
    }
}
