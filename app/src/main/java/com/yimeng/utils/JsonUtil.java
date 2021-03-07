package com.yimeng.utils;

import com.yimeng.entity.ModelShopCarSettle;
import com.yimeng.entity.ModelShopCarSettleItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by xp on 2016/8/16.
 * json
 */
public class JsonUtil {

    public static String getString(JSONObject obj, String name) {

        if (null == obj) {
            return "";
        }

        String value = "";
        try {
            value = obj.isNull(name) ? "" : obj.getString(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return value;
    }

    public static boolean getBoolean(JSONObject obj, String name) {

        if (null == obj) {
            return false;
        }

        boolean value = false;
        try {
            value = !obj.isNull(name) && obj.getBoolean(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return value;
    }

    public static int getInt(JSONObject obj, String name) {

        if (null == obj) {
            return 0;
        }

        int value = 0;
        try {
            value = obj.isNull(name) ? 0 : obj.getInt(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return value;
    }

    public static long getLong(JSONObject obj, String name) {

        if (null == obj) {
            return 0;
        }

        long value = 0;
        try {
            value = obj.isNull(name) ? 0 : obj.getLong(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return value;
    }

    public static double getDouble(JSONObject obj, String name) {

        if (null == obj) {
            return 0;
        }

        double value = 0;
        try {
            value = obj.isNull(name) ? 0 : obj.getDouble(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return value;
    }

    /**
     * 获取购物车编号JSON
     *
     * @param settles 结算信息
     * @return JSON字符串
     */
    public static String getShopCartNosJson(LinkedList<ModelShopCarSettle> settles) {

        String jsonStr;
        try {
            JSONArray root = new JSONArray();//实例一个JSON数组
            for (ModelShopCarSettle settle : settles) {
                String shopCarNos = "";
                for (ModelShopCarSettleItem item : settle.getModelShopCarSettleItemList()) {
                    shopCarNos += item.getShopCarNo() + ",";
                }
                shopCarNos = shopCarNos.contains(",") ? shopCarNos.substring(0, shopCarNos.length() - 1) : shopCarNos;
                JSONObject obj = new JSONObject();//实例一个的JSON对象
                obj.put("shopNo", settle.getShopNo());//店铺编号
                obj.put("shopCarNos", shopCarNos);//购物车编号
                obj.put("leaveMsg", settle.getLeaveMsg());//用户备注
                root.put(obj);
            }
            jsonStr = root.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            jsonStr = "";
        }

        return jsonStr;
    }

}
