package com.yimeng.net.lxmm_net;

import android.os.Handler;
import android.text.TextUtils;

import com.yimeng.config.ConstantHandler;
import com.yimeng.config.Constants;
import com.yimeng.config.ConstantsUrl;
import com.yimeng.utils.CommonUtils;
import com.huige.library.utils.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Desction: 生成okhttp所需的请求参数,并进行请求
 * Author: xp
 * Date: 2017年05月20日 11:00
 */
public class HttpParameterUtil {

    private static final byte[] LOCKER = new byte[0];
    private static HttpParameterUtil mInstance;

    public static HttpParameterUtil getInstance() {

        if (mInstance == null) {
            synchronized (LOCKER) {
                if (mInstance == null) {
                    mInstance = new HttpParameterUtil();
                }
            }
        }

        return mInstance;
    }

    /**
     * 图片上传
     *
     * @param mHandler Handler
     */
    public void requestImageUpload(String pic, Handler mHandler) {
        String reqUrl = ConstantsUrl.UPLOAD_IMG_URL;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("id", "picsjbb00" + System.currentTimeMillis(), true));
        params.putAll(addParams("imgBase64", "data:image/png;base64," + pic, true));
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_IMAGE_UPLOAD_SUCCESS;
        int fail = ConstantHandler.WHAT_IMAGE_UPLOAD_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 添加参数
     *
     * @param key     参数键
     * @param value   参数值
     * @param isEmpty true 都添加， false 若参数为空则不添加
     * @return Map参数
     */
    private Map<String, String> addParams(String key, String value, boolean isEmpty) {
        Map<String, String> params = new HashMap<>();
        if (value != null && (!value.isEmpty() || isEmpty)) {
            params.put(key, value);
        }
        return params;
    }

    /**
     * 刷新token
     *
     * @param mHandler Handler
     */
    public void requestRefreshToken(Handler mHandler) {
        String reqUrl = ConstantsUrl.LOGIN_MOBILE_URL;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("mobileNo", (String) SharedPreferencesUtils.get(Constants.USER_MOBILE, ""), true));
        params.putAll(addParams("bindNo", CommonUtils.getCellphoneKey(), true));
        int success = ConstantHandler.WHAT_REFRESH_TOKEN_SUCCESS;
        int fail = ConstantHandler.WHAT_REFRESH_TOKEN_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 热卖分类列表
     * menuType      必填     值：hotSales
     *
     * @param mHandler Handler
     */
    public void requestHotSalesCategoryList(String menuType, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_HOT_SALES_CATEGORY_LIST;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("token", CommonUtils.getToken());
        params.putAll(addParams("menuType", menuType, true));
        int success = ConstantHandler.WHAT_HOT_SALES_CATEGORY_LIST_SUCCESS;
        int fail = ConstantHandler.WHAT_HOT_SALES_CATEGORY_LIST_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 商城首页轮播图
     * menuType 必填（值=banner ：首页导航图片）
     *
     * @param mHandler Handler
     */
    public void requestGetBannerList(String menuType, Handler mHandler) {

        String reqUrl = ConstantsUrl.URL_GET_BANNER_LIST;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        params.put("token", CommonUtils.getToken());
        params.putAll(addParams("menuType", menuType, true));
        int success = ConstantHandler.WHAT_GET_BANNER_LIST_SUCCESS;
        int fail = ConstantHandler.WHAT_GET_BANNER_LIST_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 热卖商品
     * menuNo  分类编号
     *
     * @param mHandler Handler
     */
    public void requestGetHotSalesProduct(String menuNo, int page, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_GET_HOT_SALES_PRODUCT;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("token", CommonUtils.getToken());
        params.putAll(addParams("menuNo", menuNo, true));
        params.putAll(addPageParams(page, true));
        int success = page == 1 ? ConstantHandler.WHAT_GET_HOT_SALES_PRODUCT_SUCCESS : ConstantHandler.WHAT_GET_HOT_SALES_PRODUCT_MORE_SUCCESS;
        int fail = page == 1 ? ConstantHandler.WHAT_GET_HOT_SALES_PRODUCT_FAIL : ConstantHandler.WHAT_GET_HOT_SALES_PRODUCT_MORE_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 添加分页相关参数
     *
     * @return Map参数
     */
    private Map<String, String> addPageParams(int page, boolean isEmpty) {
        Map<String, String> params = new HashMap<>();
        params.putAll(addParams("limit", Constants.MAX_LIMIT + "", true));
        params.putAll(addParams("start", ((page - 1) * 10) + "", true));
        return params;
    }

    /**
     * 热卖商品
     *
     * @param mHandler Handler
     */
    public void requestProductCategories(Handler mHandler) {

        String reqUrl = ConstantsUrl.URL_PRODUCT_CATEGORIES;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        params.put("token", CommonUtils.getToken());

        int success = ConstantHandler.WHAT_PRODUCT_CATEGORIES_SUCCESS;
        int fail = ConstantHandler.WHAT_PRODUCT_CATEGORIES_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 厂促热卖商品
     *
     * @param mHandler Handler
     */
    public void requestCompanyProductCategories(int taskType, String blockNo, Handler mHandler) {

        String reqUrl = ConstantsUrl.URL_COMPANY_PRODUCT_CATEGORIES;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        params.put("token", CommonUtils.getToken());
        String city = (String) SharedPreferencesUtils.get(Constants.APP_LOCATION_CITY, "");
        if (!TextUtils.isEmpty(city)) {
            params.put("city", city);
        }
        // 3.0任务
        if (taskType == 2 && !TextUtils.isEmpty(blockNo)) {
            params.put("blockNo", blockNo);
        }

        int success = ConstantHandler.WHAT_PRODUCT_CATEGORIES_SUCCESS;
        int fail = ConstantHandler.WHAT_PRODUCT_CATEGORIES_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 商品列表（根据分类查询商品）
     * menuNo：必填  分类编号
     *
     * @param mHandler Handler
     */
    public void requestProductByMenuNo(String menuNo, Handler mHandler) {

        String reqUrl = ConstantsUrl.URL_PRODUCT_BY_MENU_NO;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        params.put("token", CommonUtils.getToken());
        params.putAll(addParams("menuNo", menuNo, true));

        int success = ConstantHandler.WHAT_PRODUCT_BY_MENU_NO_SUCCESS;
        int fail = ConstantHandler.WHAT_PRODUCT_BY_MENU_NO_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }


    /**
     * 搜索结果分页列表(少于20条)
     *
     * @param mHandler Handler
     */
    public void requestLogSearch(Handler mHandler) {

        String reqUrl = ConstantsUrl.URL_LOG_SEARCH;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        params.put("token", CommonUtils.getToken());

        int success = ConstantHandler.WHAT_LOG_SEARCH_SUCCESS;
        int fail = ConstantHandler.WHAT_LOG_SEARCH_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 热门搜索
     *
     * @param mHandler Handler
     */
    public void requestHotSearch(Handler mHandler) {

        String reqUrl = ConstantsUrl.URL_HOT_SEARCH;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        params.put("token", CommonUtils.getToken());

        int success = ConstantHandler.WHAT_HOT_SEARCH_SUCCESS;
        int fail = ConstantHandler.WHAT_HOT_SEARCH_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 搜索结果
     * keyword	必填	搜索关键字
     *
     * @param mHandler Handler
     */
    public void requestSearchProduct(String keyword, int page, Handler mHandler) {

        String reqUrl = ConstantsUrl.URL_SEARCH_PRODUCT;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        params.put("token", CommonUtils.getToken());
        params.putAll(addPageParams(page, true));
        params.putAll(addParams("keyword", keyword, true));

        int success = page > 1 ? ConstantHandler.WHAT_SEARCH_PRODUCT_MORE_SUCCESS : ConstantHandler.WHAT_SEARCH_PRODUCT_SUCCESS;
        int fail = page > 1 ? ConstantHandler.WHAT_SEARCH_PRODUCT_MORE_FAIL : ConstantHandler.WHAT_SEARCH_PRODUCT_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 商品评论分页列表
     * shopNo		店铺编号
     *
     * @param mHandler Handler
     */
    public void requestProductComment(int page, String shopNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_PRODUCT_COMMENT;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("token", CommonUtils.getToken());
        params.putAll(addPageParams(page, true));
        params.putAll(addParams("productNo", shopNo, true));
        int success = page > 1 ? ConstantHandler.WHAT_PRODUCT_COMMENT_MORE_SUCCESS : ConstantHandler.WHAT_PRODUCT_COMMENT_SUCCESS;
        int fail = page > 1 ? ConstantHandler.WHAT_PRODUCT_COMMENT_MORE_FAIL : ConstantHandler.WHAT_PRODUCT_COMMENT_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 商品详情
     * productNo		产品编号
     *
     * @param mHandler Handler
     */
    public void requestQueryProductDetail(String productNo, Handler mHandler) {

        String reqUrl = ConstantsUrl.URL_QUERY_PRODUCT_DETAIL;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        params.put("token", CommonUtils.getToken());
        params.putAll(addParams("productNo", productNo, true));

        int success = ConstantHandler.WHAT_QUERY_PRODUCT_DETAIL_SUCCESS;
        int fail = ConstantHandler.WHAT_QUERY_PRODUCT_DETAIL_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 添加收藏商品
     *
     * @param mHandler Handler
     */
    public void requestAddGoodsCollect(String productNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_ADD_GOODS_COLLECT;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("productNo", productNo, true));
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_ADD_GOODS_COLLECT_SUCCESS;
        int fail = ConstantHandler.WHAT_ADD_GOODS_COLLECT_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 删除收藏商品
     *
     * @param mHandler Handler
     */
    public void requestDelGoodsCollect(String productNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_DEL_GOODS_COLLECT;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("productNo", productNo, true));
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_DEL_GOODS_COLLECT_SUCCESS;
        int fail = ConstantHandler.WHAT_DEL_GOODS_COLLECT_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 添加购物车-添加商品到购物车
     * productNo             商品编号
     * num                  商品数量
     * shopNo               商铺编号
     * param               商品颜色尺寸等
     * productSetNo         商品规格编号
     *
     * @param mHandler Handler
     */
    public void requestAddShopCar(String productNo, String num, String shopNo, String param, String productSetNo, Handler mHandler) {

        String reqUrl = ConstantsUrl.URL_ADD_SHOP_CAR;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        params.put("token", CommonUtils.getToken());
        params.putAll(addParams("productNo", productNo, true));
        params.putAll(addParams("num", num, true));
        params.putAll(addParams("shopNo", shopNo, true));
        params.putAll(addParams("param", param, false));
        params.putAll(addParams("productSetNo", productSetNo, false));

        int success = ConstantHandler.WHAT_ADD_SHOP_CAR_SUCCESS;
        int fail = ConstantHandler.WHAT_ADD_SHOP_CAR_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 商品款式规格
     * productNo		产品编号
     *
     * @param mHandler Handler
     */
    public void requestProductSetByProductNo(String productNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_PRODUCT_SET_BY_PRODUCT_NO;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        params.put("token", CommonUtils.getToken());
        params.putAll(addParams("productNo", productNo, true));

        int success = ConstantHandler.WHAT_PRODUCT_SET_BY_PRODUCT_NO_SUCCESS;
        int fail = ConstantHandler.WHAT_PRODUCT_SET_BY_PRODUCT_NO_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 购物车列表
     *
     * @param mHandler Handler
     */
    public void requestMyShopCar(Handler mHandler) {

        String reqUrl = ConstantsUrl.URL_MY_SHOP_CAR;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        params.put("token", CommonUtils.getToken());

        int success = ConstantHandler.WHAT_MY_SHOP_CAR_SUCCESS;
        int fail = ConstantHandler.WHAT_MY_SHOP_CAR_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 修改购物车商品数量
     * shopCarNo  必填     购物车编号
     * num       必填     商品数量
     *
     * @param mHandler Handler
     */
    public void requestUpdateShopCar(String shopCarNo, String num, Handler mHandler) {

        String reqUrl = ConstantsUrl.URL_UPDATE_SHOP_CAR_NUM;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        params.put("token", CommonUtils.getToken());
        params.putAll(addParams("shopCarNo", shopCarNo, true));
        params.putAll(addParams("num", num, true));

        int success = ConstantHandler.WHAT_UPDATE_SHOP_CAR_NUM_SUCCESS;
        int fail = ConstantHandler.WHAT_UPDATE_SHOP_CAR_NUM_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 删除购物车-从购物车删除商品
     * shopCarNo  必填     购物车编号
     *
     * @param mHandler Handler
     */
    public void requestDeleteShopCar(String shopCarNo, Handler mHandler) {

        String reqUrl = ConstantsUrl.URL_DELETE_SHOP_CAR;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        params.put("token", CommonUtils.getToken());
        params.putAll(addParams("shopCarNo", shopCarNo, true));

        int success = ConstantHandler.WHAT_DELETE_SHOP_CAR_SUCCESS;
        int fail = ConstantHandler.WHAT_DELETE_SHOP_CAR_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 收货地址
     *
     * @param mHandler Handler
     */
    public void requestAddressAll(Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_ADDRESS_ALL;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_ADDRESS_ALL_SUCCESS;
        int fail = ConstantHandler.WHAT_ADDRESS_ALL_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 立即购买信息
     *
     * @param mHandler Handler
     */
    public void requestPayNowInfo(String productNo, String productSetNo, String productNum, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_PAY_NOW;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("token", CommonUtils.getToken());
        params.putAll(addParams("productNo", productNo, true));
        params.putAll(addParams("productSetNo", productSetNo, true));
        params.putAll(addParams("productNum", productNum, true));
        int success = ConstantHandler.WHAT_PAY_NOW_SUCCESS;
        int fail = ConstantHandler.WHAT_PAY_NOW_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 立即购买（确认支付生成订单）
     */
    public void requestPayNowCreateOrder(String productNo, String productNum, String productSetNo,
                                         String addressNo, String leaveMsg, String param,
                                         boolean hasInvoice, boolean isEmail, boolean isPerson,
                                         String emailAddress, String invoiceName, String invoiceNo,
                                         String invoiceCompanyAddress, String invoiceMobile,
                                         String invoiceBank, String invoiceBankNo, String couponNo,
                                         Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_SURE_TOPAY;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("productNo", productNo, true));
        params.putAll(addParams("num", productNum, true));
        // 规格
        params.putAll(addParams("productSetNo", productSetNo, true));
        params.putAll(addParams("addressNo", addressNo, true));
        // 留言
        params.putAll(addParams("leaveMsg", leaveMsg, false));
        params.putAll(addParams("param", param, false));
        if (hasInvoice) { // 开票
            params.put("invoice", "1");
            if (isEmail) {// 电子发票
                params.put("invoiceCategory", "0");
                params.put("invoiceEmail", emailAddress);
            } else {//纸质发票
                params.put("invoiceCategory", "1");
            }

            if (isPerson) {//个人
                params.put("invoiceType", "0");
            } else {//企业
                params.put("invoiceType", "1");
                params.put("invoiceName", invoiceName);
                params.put("invoiceNum", invoiceNo);
                params.put("invoiceAddress", invoiceCompanyAddress);
                params.put("invoicePhone", invoiceMobile);
                params.put("invoiceBank", invoiceBank);
                params.put("invoiceAccount", invoiceBankNo);
            }

        } else {
            params.put("invoice", "0");
        }

        // 代金券
        params.putAll(addParams("couponNo", couponNo, false));
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_SURE_TOPAY_SUCCESS;
        int fail = ConstantHandler.WHAT_SURE_TOPAY_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 支付订单（余额）
     *
     * @param mHandler Handler
     */
    public void requestOrderToPay(String orderNo, String score, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_ORDER_PAY_BALANCE;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("orderNo", orderNo);
        params.put("score", score);
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_ORDER_PAY_SUCCESS;
        int fail = ConstantHandler.WHAT_ORDER_PAY_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 请求微信支付参数
     *
     * @param mHandler Handler
     */
    public void requestWechatPayParams(String orderNo, String score, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_WECHAT_PAY_PARAMS;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("orderNo", orderNo, true));
        params.putAll(addParams("score", score, true));// 麻豆
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_WECHAT_PAY_PARAMS_SUCCESS;
        int fail = ConstantHandler.WHAT_WECHAT_PAY_PARAMS_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 获取微信充值参数
     *
     * @param mHandler Handler
     */
    public void requestWeixinParam(String rechargeNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_RECHARE_WXPARAM;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("rechargeNo", rechargeNo);
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_WECHAT_PAY_PARAMS_SUCCESS;
        int fail = ConstantHandler.WHAT_WECHAT_PAY_PARAMS_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 获取订单支付支付宝参数
     *
     * @param mHandler Handler
     */
    public void requestOrderAlipayParam(String orderNo, String score, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_ORDER_ALIPAY;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("orderNo", orderNo, true));
        params.putAll(addParams("score", score, true));
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_ALIPAY_PARAMS_SUCCESS;
        int fail = ConstantHandler.WHAT_ALIPAY_PARAMS_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 获取VIP订单支付支付宝参数
     *
     * @param mHandler Handler
     */
    public void requestVipOrderAlipayParam(String orderNo, String score, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_ORDER_VIP_ALIPAY;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("orderNo", orderNo, true));
        params.putAll(addParams("score", score, true));
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_ALIPAY_PARAMS_SUCCESS;
        int fail = ConstantHandler.WHAT_ALIPAY_PARAMS_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 获取支付宝充值参数
     *
     * @param mHandler Handler
     */
    public void requestAlipayParam(String rechargeNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_RECHARE_ALIPAY;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("rechargeNo", rechargeNo);
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_ALIPAY_PARAMS_SUCCESS;
        int fail = ConstantHandler.WHAT_ALIPAY_PARAMS_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 商城订单列表
     * 订单类型 nopay(未支付)，pay("已付款/未发货") noReceiving("待收货")
     * ,refund("退款中"),nocomment("未评价"),complete(已完成)
     *
     * @param mHandler Handler
     */
    public void requestMallOrder(int page, String orderStatus, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_MALL_ORDER;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addPageParams(page, true));
        params.putAll(addParams("orderStatus", orderStatus, true));

        params.put("token", CommonUtils.getToken());
        int success = page > 1 ? ConstantHandler.WHAT_MALL_ORDER_MORE_SUCCESS : ConstantHandler.WHAT_MALL_ORDER_SUCCESS;
        int fail = page > 1 ? ConstantHandler.WHAT_MALL_ORDER_MORE_FAIL : ConstantHandler.WHAT_MALL_ORDER_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 我的订单-商城订单-取消订单
     * orderNo	订单编号
     *
     * @param mHandler Handler
     */
    public void requestCancelMallOrder(String orderNo, Handler mHandler) {

        String reqUrl = ConstantsUrl.URL_CANCEL_MALL_ORDER;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        params.put("token", CommonUtils.getToken());
        params.putAll(addParams("orderNo", orderNo, true));

        int success = ConstantHandler.WHAT_CANCEL_MALL_ORDER_SUCCESS;
        int fail = ConstantHandler.WHAT_CANCEL_MALL_ORDER_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 我的订单-商城订单-确认收货
     * orderNo	订单编号
     *
     * @param mHandler Handler
     */
    public void requestConfirmMallOrder(String orderNo, Handler mHandler) {

        String reqUrl = ConstantsUrl.URL_CONFIRM_MALL_ORDER;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        params.put("token", CommonUtils.getToken());
        params.putAll(addParams("orderNo", orderNo, true));

        int success = ConstantHandler.WHAT_CONFIRM_MALL_ORDER_SUCCESS;
        int fail = ConstantHandler.WHAT_CONFIRM_MALL_ORDER_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 修改收货地址
     *
     * @param mHandler Handler
     */
    public void requestAddressSave(String addressNo, String linkman, String mobileNo, String province, String city,
                                   String area, String address, String isdefault, Handler mHandler) {

        String reqUrl = ConstantsUrl.URL_ADDRESS_SAVE;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("addressNo", addressNo);// 地址编号	必须
        params.put("linkman", linkman);// 联系人	必须
        params.put("mobileNo", mobileNo);// 收件人手机	必须
        params.put("province", province);// 省份	必须
        params.put("city", city);// 省份	必须
        params.put("area", area);// 省份	必须
        params.put("address", address);// 详细地址	必须
        params.put("isdefault", isdefault);// 是否设为默认地址 1为默认，0为非默认
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_ADDRESS_SAVE_SUCCESS;
        int fail = ConstantHandler.WHAT_ADDRESS_SAVE_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 添加收货地址
     */
    public void requestAddressAdd(String linkman, String mobileNo, String province, String city,
                                  String area, String address, String isdefault, Handler mHandler) {

        String reqUrl = ConstantsUrl.URL_ADDRESS_ADD;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("linkman", linkman);// 联系人	必须
        params.put("mobileNo", mobileNo);// 收件人手机	必须
        params.put("province", province);// 省份	必须
        params.put("city", city);// 省份	必须
        params.put("area", area);// 省份	必须
        params.put("address", address);// 详细地址	必须
        params.put("isdefault", isdefault);// 是否设为默认地址 1为默认，0为非默认
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_ADDRESS_ADD_SUCCESS;
        int fail = ConstantHandler.WHAT_ADDRESS_ADD_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 删除地址
     *
     * @param mHandler Handler
     */
    public void requestAddressRemove(String addressNo, Handler mHandler) {

        String reqUrl = ConstantsUrl.URL_ADDRESS_REMOVE;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("addressNo", addressNo);
        params.put("token", CommonUtils.getToken());

        int success = ConstantHandler.WHAT_ADDRESS_REMOVE_SUCCESS;
        int fail = ConstantHandler.WHAT_ADDRESS_REMOVE_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 购物车结算页面商品信息
     * shopCarNos  必填     购物车编号
     *
     * @param mHandler Handler
     */
    public void requestShopCarSettle(String shopCarNos, Handler mHandler) {

        String reqUrl = ConstantsUrl.URL_SHOP_CAR_SETTLE;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        params.put("token", CommonUtils.getToken());
        params.putAll(addParams("shopCarNos", shopCarNos, true));

        int success = ConstantHandler.WHAT_SHOP_CAR_SETTLE_SUCCESS;
        int fail = ConstantHandler.WHAT_SHOP_CAR_SETTLE_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 购物车结算创建订单
     *
     * @param shopCarNo  购物车编号
     * @param addressNo  地址编号
     * @param payType    支付类型 yu_e("余额支付"), weixin("微信支付"), zhifubao("支付宝支付"), xianxia("线下支付"), couponExchange("代金券兑换");
     * @param shopCarNos 购物车编号信息
     * @param mHandler   Handler
     */
    public void requestShopcartCreateOrder(String shopCarNo, String addressNo, String payType, String shopCarNos, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_SHOPCART_CREATE_ORDER;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("shopCarNo", shopCarNo, true));
        params.putAll(addParams("addressNo", addressNo, true));
        params.putAll(addParams("payType", payType, true));
        params.putAll(addParams("shopCarNos", shopCarNos, false));
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_SHOPCART_CREATE_ORDER_SUCCESS;
        int fail = ConstantHandler.WHAT_SHOPCART_CREATE_ORDER_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 商城订单评价
     *
     * @param businessNo 订单编号
     * @param comments   评论内容
     * @param mHandler   Handler
     */
    public void requestMallComments(String businessNo, String comments, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_CREATE_MALL_COMMENTS;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("token", CommonUtils.getToken());
        params.putAll(addParams("businessNo", businessNo, true));
        params.putAll(addParams("comments", comments, true));

        int success = ConstantHandler.WHAT_ORDER_COMMENT_SUCCESS;
        int fail = ConstantHandler.WHAT_ORDER_COMMENT_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 店铺订单评价
     *
     * @param businessNo 订单编号
     * @param comments   评论内容
     * @param mHandler   Handler
     */
    public void requestShopComments(String businessNo, String comments, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_CREATE_SHOP_COMMENTS;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("token", CommonUtils.getToken());
        params.putAll(addParams("businessNo", businessNo, true));
        params.putAll(addParams("comments", comments, true));

        int success = ConstantHandler.WHAT_ORDER_COMMENT_SUCCESS;
        int fail = ConstantHandler.WHAT_ORDER_COMMENT_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 收藏商品列表
     *
     * @param mHandler Handler
     */
    public void requestProductCollect(int page, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_PRODUCT_COLLECT;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addPageParams(page, true));
        params.put("token", CommonUtils.getToken());
        int success = page == 1 ? ConstantHandler.WHAT_PRODUCT_COLLECT_SUCCESS : ConstantHandler.WHAT_PRODUCT_COLLECT_MORE_SUCCESS;
        int fail = page == 1 ? ConstantHandler.WHAT_PRODUCT_COLLECT_FAIL : ConstantHandler.WHAT_PRODUCT_COLLECT_MORE_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 收藏商家列表
     *
     * @param mHandler Handler
     */
    public void requestShopCollect(int page, Handler mHandler) {

        String reqUrl = ConstantsUrl.URL_SHOP_COLLECT;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addPageParams(page, true));
        params.put("token", CommonUtils.getToken());
        int success = page == 1 ? ConstantHandler.WHAT_SHOP_COLLECT_SUCCESS : ConstantHandler.WHAT_SHOP_COLLECT_MORE_SUCCESS;
        int fail = page == 1 ? ConstantHandler.WHAT_SHOP_COLLECT_FAIL : ConstantHandler.WHAT_SHOP_COLLECT_MORE_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 实名认证提交
     *
     * @param mHandler Handler
     */
    public void requestCertificationMemberInfo(String memberName, String certNo, String floor, Handler mHandler) {

        String reqUrl = ConstantsUrl.URL_CERTIIFICATION_MEMBER_INF;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("token", CommonUtils.getToken());
        params.put("memberName", memberName);
        params.put("certNo", certNo);
        params.put("floor", floor);
        int success = ConstantHandler.WHAT_CERTIIFICATION_MEMBER_INF_SUCCESS;
        int fail = ConstantHandler.WHAT_CERTIIFICATION_MEMBER_INF_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 我的钱包
     *
     * @param mHandler Handler
     */
    public void requestMyWallet(Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_MY_WALLET;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_MY_WALLET_SUCCESS;
        int fail = ConstantHandler.WHAT_MY_WALLET_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 交易记录
     *
     * @param mHandler Handler
     */
    public void requesttTrasactionRecords(String mAmtType, int page, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_TRANSACTION_RECORD;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addPageParams(page, true));
        params.putAll(addParams("amtType", mAmtType, true));
        params.put("token", CommonUtils.getToken());
        int success = page > 1 ? ConstantHandler.WHAT_TRANSACTION_RECORDS_MORE_SUCCESS : ConstantHandler.WHAT_TRANSACTION_RECORDS_SUCCESS;
        int fail = page > 1 ? ConstantHandler.WHAT_TRANSACTION_RECORDS_MORE_FAIL : ConstantHandler.WHAT_TRANSACTION_RECORDS_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 查询银行卡
     *
     * @param mHandler Handler
     */
    public void requestMemberBankcard(Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_MEMBER_BANKCARD;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_MEMBER_BANKCARD_SUCCESS;
        int fail = ConstantHandler.WHAT_MEMBER_BANKCARD_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 提现申请
     * applyAmt:申请金额
     * bankcardNo:银行卡编号
     *
     * @param mHandler Handler
     */
    public void requestAddWithdrawApply(String applyAmt, String bankcardNo, String withdrawType, Handler mHandler) {
        String reqUrl;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        if (withdrawType.equals("earnings")) {
            // 赚呗提现
            reqUrl = ConstantsUrl.URL_ADD_MAKE_MONEY_WITHDRAW_APPLY;
        } else {
            reqUrl = ConstantsUrl.URL_ADD_WITHDRAW_APPLY;
            params.put("withdrawType", withdrawType);
        }

        params.put("token", CommonUtils.getToken());
        params.put("applyAmt", applyAmt);
        params.put("bankcardNo", bankcardNo);

        int success = ConstantHandler.WHAT_ADD_WITHDRAW_APPLY_SUCCESS;
        int fail = ConstantHandler.WHAT_ADD_WITHDRAW_APPLY_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 提现申请
     * applyAmt:申请金额
     * refundIsWechat:是否为微信提现
     * <p>
     * walletType
     * mine： 我的钱包
     * shop：店铺钱包
     *
     * @param mHandler Handler
     */
    public void requestAddWithdrawApply(String walletType, String applyAmt, boolean refundIsWechat, Handler mHandler) {

        String reqUrl;
        if (walletType.equals("shop")) {
            reqUrl = ConstantsUrl.URL_SHOP_ADD_WITHDRAW_APPLY;
        } else {
            reqUrl = ConstantsUrl.URL_ADD_WITHDRAW_APPLY;
        }
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("withdrawType", refundIsWechat ? "weixin" : "zhifubao");
        params.put("token", CommonUtils.getToken());
        params.put("applyAmt", applyAmt);

        int success = ConstantHandler.WHAT_ADD_WITHDRAW_APPLY_SUCCESS;
        int fail = ConstantHandler.WHAT_ADD_WITHDRAW_APPLY_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 删除银行卡
     * bankcardNo  必填   银行卡编号
     *
     * @param mHandler Handler
     */
    public void requestDelMemberBankcard(String bankcardNo, Handler mHandler) {

        String reqUrl = ConstantsUrl.URL_DEL_WITHDRAW_APPLY;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        params.put("token", CommonUtils.getToken());
        params.putAll(addParams("bankcardNo", bankcardNo, true));
        int success = ConstantHandler.WHAT_DEL_WITHDRAW_APPLY_SUCCESS;
        int fail = ConstantHandler.WHAT_DEL_WITHDRAW_APPLY_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 修改银行卡
     *
     * @param mHandler Handler
     */
    public void requestUpdateMemberBankcard(String bankcardNo, String bankcardNum, String bankName, String bankcardName, String bankMobile, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_UPDATE_MEMBER_BANKCARD;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        params.put("token", CommonUtils.getToken());
        params.putAll(addParams("bankcardNo", bankcardNo, true));

        params.put("bankcardNo", bankcardNo);
        params.put("bankcardNum", bankcardNum);
        params.put("bankName", bankName);
        params.put("bankcardName", bankcardName);
        params.put("bankcardMobile", bankMobile);
        int success = ConstantHandler.WHAT_UPDATE_MEMBER_BANKCARD_SUCCESS;
        int fail = ConstantHandler.WHAT_UPDATE_MEMBER_BANKCARD_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 添加银行卡
     *
     * @param mHandler Handler
     */
    public void requestAddMemberBankcard(String bankcardNum, String bankName, String bankcardName, String bankMobile, Handler mHandler) {

        String reqUrl = ConstantsUrl.URL_ADD_MEMBER_BANKCARD;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("token", CommonUtils.getToken());

        params.put("bankcardNum", bankcardNum);
        params.put("bankName", bankName);
        params.put("bankcardName", bankcardName);
        params.put("bankcardMobile", bankMobile);
        int success = ConstantHandler.WHAT_ADD_MEMBER_BANKCARD_SUCCESS;
        int fail = ConstantHandler.WHAT_ADD_MEMBER_BANKCARD_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 提现记录(分页查询)
     *
     * @param mHandler Handler
     */
    public void requestWithdrawApply(int page, Handler mHandler, String withdrawType) {
        String reqUrl = ConstantsUrl.URL_WITHDRAW_APPLY_V_2_1_0;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addPageParams(page, true));
        params.put("token", CommonUtils.getToken());
        params.put("withdrawType", withdrawType);
        int success = page == 1 ? ConstantHandler.WHAT_WITHDRAW_APPLY_SUCCESS : ConstantHandler.WHAT_WITHDRAW_APPLY_MORE_SUCCESS;
        int fail = page == 1 ? ConstantHandler.WHAT_WITHDRAW_APPLY_FAIL : ConstantHandler.WHAT_WITHDRAW_APPLY_MORE_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 我的收益统计
     *
     * @param mHandler Handler
     */
    public void requestBonus(Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_BONUS;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        params.put("token", CommonUtils.getToken());

        int success = ConstantHandler.WHAT_BONUS_SUCCESS;
        int fail = ConstantHandler.WHAT_BONUS_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 收益记录
     *
     * @param mHandler Handler
     */
    public void requestFenRunRecords(int page, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_PAY_RECORDS;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addPageParams(page, true));
        params.putAll(addParams("amtType", "zhanneiFreeze", true));
        params.put("token", CommonUtils.getToken());
        int success = page > 1 ? ConstantHandler.WHAT_PAY_RECORDS_MORE_SUCCESS : ConstantHandler.WHAT_PAY_RECORDS_SUCCESS;
        int fail = page > 1 ? ConstantHandler.WHAT_PAY_RECORDS_MORE_FAIL : ConstantHandler.WHAT_PAY_RECORDS_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 添加代理商前查询用户 - 手机号码
     *
     * @param mHandler
     * @param mobileNo 手机号
     */
    public void queryMemberByMobile(Handler mHandler, String mobileNo) {
        String reqUrl = ConstantsUrl.URL_QUERY_MEMBER_BY_MOBILE;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("mobileNo", mobileNo);
        int success = ConstantHandler.WHAT_QUERY_MEMBER_BY_MOBILE_SUCCESS;
        int fail = ConstantHandler.WHAT_QUERY_MEMBER_BY_MOBILE_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 添加代理商前查询用户 - 用户编号
     *
     * @param mHandler
     * @param memberNo 用户编号
     */
    public void queryMemberByMemberNo(Handler mHandler, String memberNo) {
        String reqUrl = ConstantsUrl.URL_QUERY_MEMBER_BY_NO;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("token", CommonUtils.getToken());
        params.putAll(addParams("memberNo", memberNo, true));
        int success = ConstantHandler.WHAT_QUERY_MEMBER_BY_NO_SUCCESS;
        int fail = ConstantHandler.WHAT_QUERY_MEMBER_BY_NO_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 设置代理
     *
     * @param mHandler
     * @param memberNo member
     * @param extracts 分润40-50百分比
     */
    public void setProxy(Handler mHandler, String memberNo, String extracts) {
        String url = ConstantsUrl.URL_SET_PROXY;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("token", CommonUtils.getToken());
        params.putAll(addParams("memberNo", memberNo, true));
        params.putAll(addParams("extracts", extracts, true));
        int success = ConstantHandler.WHAT_SET_PROXY_SUCCESS;
        int fail = ConstantHandler.WHAT_SET_PROXY_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(url, headers, params, "", mHandler, success, fail);
    }

    /**
     * 添加充值记录
     *
     * @param mHandler Handler
     */
    public void requestRechargeAdd(String rechargeType, String amt, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_RECHARE_ADD;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("rechargeType", rechargeType);
        params.put("amt", amt);
        params.put("rechargeName", "chongzhi");
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_RECHARE_ADD_SUCCESS;
        int fail = ConstantHandler.WHAT_RECHARE_ADD_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 取消充值
     *
     * @param mHandler Handler
     */
    public void requestCancelPay(String rechargeNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_CANCEL_PAY;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("rechargeNo", rechargeNo);
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_CANCEL_PAY_SUCCESS;
        int fail = ConstantHandler.WHAT_CANCEL_PAY_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 充值记录
     *
     * @param mHandler Handler
     */
    public void requesttPayRecords(int page, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_PAY_RECORDS;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addPageParams(page, true));
        params.putAll(addParams("amtType", "chongzhi", true));
        params.put("token", CommonUtils.getToken());
        int success = page > 1 ? ConstantHandler.WHAT_PAY_RECORDS_MORE_SUCCESS : ConstantHandler.WHAT_PAY_RECORDS_SUCCESS;
        int fail = page > 1 ? ConstantHandler.WHAT_PAY_RECORDS_MORE_FAIL : ConstantHandler.WHAT_PAY_RECORDS_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 查询订单状态（退货）
     *
     * @param type 1. 退货
     *             2. 退款
     */
    public void queryStateOfReturn(int type, String orderNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_QUERY_STATE_OF_RETURN;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("orderNo", orderNo);
        params.put("token", CommonUtils.getToken());
        params.put("refundType", type == 1 ? "entryGoods" : "entryRefund");
        int success = ConstantHandler.WHAT_QUERY_STATE_OF_RETURN_SUCCESS;
        int fail = ConstantHandler.WHAT_QUERY_STATE_OF_RETURN_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 退货申请
     */
    public void returnOfGoodsApply(String orderNo, String username, String mobileNo, String remark, String remarkImg, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_RETURN_OF_GOODS;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("orderNo", orderNo);
        params.put("username", username);
        params.put("mobileNo", mobileNo);
        params.put("remark", remark);
        params.put("remarkImg", remarkImg);
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_RETURN_OF_GOODS_SUCCESS;
        int fail = ConstantHandler.WHAT_RETURN_OF_GOODS_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 退款申请
     */
    public void returnOfMoneyApply(String orderNo, String username, String mobileNo, String remark, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_RETURN_OF_MONEY;
        HashMap<String, String> params = CommonUtils.createParams();
        Map<String, String> headers = new HashMap<>();
        params.put("token", CommonUtils.getToken());
        params.put("orderNo", orderNo);
        params.put("username", username);
        params.put("mobileNo", mobileNo);
        params.put("remark", remark);
        int success = ConstantHandler.WHAT_RETURN_OF_MONEY_SUCCESS;
        int fail = ConstantHandler.WHAT_RETURN_OF_MONEY_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 回邮确认
     */
    public void returnSendBackConfirm(String orderNo, String logisticsType, String logisticsNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_RETURN_SEND_BACK_CONFIRM;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("orderNo", orderNo);
        params.put("logisticsType", logisticsType);
        params.put("logisticsNo", logisticsNo);
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_RETURN_SEND_BACK_CONFIRM_SUCCESS;
        int fail = ConstantHandler.WHAT_RETURN_SEND_BACK_CONFIRM_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 查物流公司
     *
     * @param mHandler
     */
    public void queryFlowList(Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_FLOW_LIST;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_QUERY_FLOW_LIST_SUCCESS;
        int fail = ConstantHandler.WHAT_QUERY_FLOW_LIST_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 店铺入驻审核未通过店铺信息详情
     *
     * @param mHandler Handler
     */
    public void requestShopAuditApplyfail(String shopNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_SHOP_AUDIT_APPLY_DETAIL;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("shopNo", shopNo, true));
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_SHOP_AUDIT_APPLY_DETAIL_SUCCESS;
        int fail = ConstantHandler.WHAT_SHOP_AUDIT_APPLY_DETAIL_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 店铺信息
     *
     * @param mHandler Handler
     */
    public void requestShopInfo(Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_SHOP_INFO;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("token", CommonUtils.getToken());
        params.put("shopNo", CommonUtils.getMember().getTelePhone());
        int success = ConstantHandler.WHAT_SHOP_INFO_SUCCESS;
        int fail = ConstantHandler.WHAT_SHOP_INFO_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 申请开店
     *
     * @param mHandler Handler
     */
    public void requestApplyShop(String identityPath, String imagesPath, String logoPath, String closeTimeStr,
                                 String openTimeStr, String introduce, String address, String area, String city,
                                 String province, String areaId, String latitude, String longitude, String telephone,
                                 String mobileNo, String shopName, String shopType, Handler mHandler, String logisticsType) {
        String reqUrl = ConstantsUrl.URL_APPLY_SHOP;
        int success = ConstantHandler.WHAT_APPLY_SHOP_SUCCESS;
        int fail = ConstantHandler.WHAT_APPLY_SHOP_FAIL;
        requestEditShop(identityPath, imagesPath, logoPath, closeTimeStr, openTimeStr, introduce,
                address, area, city, province, areaId, latitude, longitude, telephone, mobileNo, shopName,
                shopType, "", mHandler, reqUrl, success, fail, logisticsType);
    }

    /**
     * 店铺信息编辑(重新提交，编辑修改)
     *
     * @param mHandler Handler
     */
    public void requestEditShop(String identityPath, String imagesPath, String logoPath, String closeTimeStr,
                                String openTimeStr, String introduce, String address, String area, String city,
                                String province, String areaId, String latitude, String longitude, String telephone,
                                String mobileNo, String shopName, String shopType, String shopNo, Handler mHandler,
                                String reqUrl, int success, int fail, String logisticsType) {

        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("identityPath", identityPath, true));
        params.putAll(addParams("imagesPath", imagesPath, true));
        params.putAll(addParams("logoPath", logoPath, true));
        params.putAll(addParams("closeTimeStr", closeTimeStr, true));
        params.putAll(addParams("openTimeStr", openTimeStr, true));
        params.putAll(addParams("introduce", introduce, true));
        params.putAll(addParams("address", address, true));
        params.putAll(addParams("area", area, true));
        params.putAll(addParams("city", city, true));
        params.putAll(addParams("province", province, true));
        params.putAll(addParams("areaId", areaId, true));
        params.putAll(addParams("latitude", latitude, true));
        params.putAll(addParams("longitude", longitude, true));
        params.putAll(addParams("telephone", telephone, true));
        params.putAll(addParams("mobileNo", mobileNo, true));
        params.putAll(addParams("shopName", shopName, true));
        params.putAll(addParams("shopType", shopType, true));
        params.putAll(addParams("shopNo", shopNo, false));
        params.putAll(addParams("logisticsType", logisticsType, true));
        params.put("token", CommonUtils.getToken());
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 店铺入驻重新提交
     *
     * @param mHandler Handler
     */
    public void requestAnewApplyShop(String identityPath, String imagesPath, String logoPath, String closeTimeStr,
                                     String openTimeStr, String introduce, String address, String area, String city,
                                     String province, String areaId, String latitude, String longitude, String telephone,
                                     String mobileNo, String shopName, String shopType, String shopNo, Handler mHandler, String logisticsType) {
        String reqUrl = ConstantsUrl.URL_ANEW_APPLY_SHOP;
        int success = ConstantHandler.WHAT_APPLY_SHOP_SUCCESS;
        int fail = ConstantHandler.WHAT_APPLY_SHOP_FAIL;
        requestEditShop(identityPath, imagesPath, logoPath, closeTimeStr, openTimeStr, introduce,
                address, area, city, province, areaId, latitude, longitude, telephone, mobileNo, shopName,
                shopType, shopNo, mHandler, reqUrl, success, fail, logisticsType);
    }

    /**
     * 更新店铺信息
     *
     * @param mHandler Handler
     */
    public void requestUpdateShop(String identityPath, String imagesPath, String logoPath, String closeTimeStr,
                                  String openTimeStr, String introduce, String address, String area, String city,
                                  String province, String areaId, String latitude, String longitude, String telephone,
                                  String mobileNo, String shopName, String shopType, String shopNo, Handler mHandler, String logisticsType) {
        String reqUrl = ConstantsUrl.URL_UPDATE_SHOP;
        int success = ConstantHandler.WHAT_UPDATE_SHOP_SUCCESS;
        int fail = ConstantHandler.WHAT_UPDATE_SHOP_FAIL;
        requestEditShop(identityPath, imagesPath, logoPath, closeTimeStr, openTimeStr, introduce,
                address, area, city, province, areaId, latitude, longitude, telephone, mobileNo, shopName,
                shopType, shopNo, mHandler, reqUrl, success, fail, logisticsType);
    }

    /**
     * 店铺详情
     *
     * @param mHandler Handler
     */
    public void requestShopDetail(String shopNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_SHOP_DETAIL;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("shopNo", shopNo, true));
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_SHOP_DETAIL_SUCCESS;
        int fail = ConstantHandler.WHAT_SHOP_DETAIL_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 我的店铺-店铺订单分页列表
     *
     * @param mHandler Handler
     */
    public void requestMyShopOrderInfo(int page, String orderStatus, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_MY_SHOP_ORDER_INFO;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addPageParams(page, true));
        params.putAll(addParams("orderStatus", orderStatus, true));// new(新订单)，complete（已完成）
        params.put("token", CommonUtils.getToken());
        int success = page == 1 ? ConstantHandler.WHAT_MY_SHOP_ORDER_SUCCESS : ConstantHandler.WHAT_MY_SHOP_ORDER_MORE_SUCCESS;
        int fail = page == 1 ? ConstantHandler.WHAT_MY_SHOP_ORDER_FAIL : ConstantHandler.WHAT_MY_SHOP_ORDER_MORE_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 获取微信打赏参数
     *
     * @param mHandler Handler
     */
    public void requestWeixinActivationParam(String rechargeNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.GET_REWARD_WECHAT_PARAMS;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("rewardNo", rechargeNo);
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_WECHAT_PAY_PARAMS_SUCCESS;
        int fail = ConstantHandler.WHAT_WECHAT_PAY_PARAMS_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 获取支付宝打赏参数
     *
     * @param mHandler Handler
     */
    public void requestAlipayActivationParam(String rechargeNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.GET_REWARD_ALIPAY_PARAMS;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("rewardNo", rechargeNo);
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_ALIPAY_PARAMS_SUCCESS;
        int fail = ConstantHandler.WHAT_ALIPAY_PARAMS_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 店铺账户详情
     *
     * @param mHandler Handler
     */
    public void requestQueryShopAccountDetail(Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_SHOP_ACCOUNT_DETAIL;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_ACCOUNT_DETAIL_SUCCESS;
        int fail = ConstantHandler.WHAT_ACCOUNT_DETAIL_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 店铺未入账记录
     * isSettle      必填      0
     *
     * @param mHandler Handler
     */
    public void requestOrderSettle(int page, String isSettle, Handler mHandler) {

        String reqUrl = ConstantsUrl.URL_ORDER_SETTLE;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addPageParams(page, true));
        params.put("token", CommonUtils.getToken());
        params.put("isSettle", isSettle);
        int success = page == 1 ? ConstantHandler.WHAT_ORDER_SETTLE_SUCCESS : ConstantHandler.WHAT_ORDER_SETTLE_MORE_SUCCESS;
        int fail = page == 1 ? ConstantHandler.WHAT_ORDER_SETTLE_FAIL : ConstantHandler.WHAT_ORDER_SETTLE_MORE_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 店铺商品分类统计列表
     *
     * @param productType entity 　自有商品
     *                    cuxiao   促销商品
     * @param mHandler    Handler
     */
    public void requestShopCommodityStat(String shopNo, String productType, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_SHOP_COMMODITY_STAT;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("shopNo", shopNo, true));
        params.put("token", CommonUtils.getToken());
        params.put("productType", productType);
        int success = ConstantHandler.WHAT_SHOP_COMMODITY_STAT_SUCCESS;
        int fail = ConstantHandler.WHAT_SHOP_COMMODITY_STAT_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 店铺商品分类删除
     *
     * @param mHandler Handler
     */
    public void requestShopCommodityClassifyDelect(String shopNo, String productCategoryNo, String productNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_SHOP_COMMODITY_CLASSIFY_DELECT;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("shopNo", shopNo, true));
        params.putAll(addParams("productCategoryNo", productCategoryNo, true));
        params.putAll(addParams("productNo", productNo, false));
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_SHOP_COMMODITY_CLASSIFY_DELECT_SUCCESS;
        int fail = ConstantHandler.WHAT_SHOP_COMMODITY_CLASSIFY_DELECT_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 店铺商品分类添加
     *
     * @param mHandler Handler
     */
    public void requestShopCommodityClassifyAdd(String shopNo, String productCategoryName, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_SHOP_COMMODITY_CLASSIFY_ADD;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("shopNo", shopNo, true));
        params.putAll(addParams("productCategoryName", productCategoryName, true));
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_SHOP_COMMODITY_CLASSIFY_ADD_SUCCESS;
        int fail = ConstantHandler.WHAT_SHOP_COMMODITY_CLASSIFY_ADD_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 店铺商品分类编辑
     *
     * @param mHandler Handler
     */
    public void requestShopCommodityClassifyEdit(String shopNo, String productCategoryNo, String productCategoryName, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_SHOP_COMMODITY_CLASSIFY_EDIT;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("shopNo", shopNo, true));
        params.putAll(addParams("productCategoryNo", productCategoryNo, true));
        params.putAll(addParams("productCategoryName", productCategoryName, true));
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_SHOP_COMMODITY_CLASSIFY_EDIT_SUCCESS;
        int fail = ConstantHandler.WHAT_SHOP_COMMODITY_CLASSIFY_EDIT_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 销售中和仓库中商品分页列表
     *
     * @param mHandler Handler
     */
    public void requestQueryProductList(String shopNo, String productType, String isOnsale, String productCategoryNo,
                                        int page, Handler mHandler, int success, int fail) {
        String reqUrl = ConstantsUrl.URL_QUERY_PRODUCT_LIST;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addPageParams(page, true));
        params.putAll(addParams("shopNo", shopNo, true));//店铺编号
        params.putAll(addParams("isOnsale", isOnsale, true));//销售中（1）或仓库中（0）
        params.putAll(addParams("productCategoryNo", productCategoryNo, true));//分类编号
        params.put("token", CommonUtils.getToken());
        params.put("productType", productType);// 产品类别()
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 审核中和已驳回商品分页列表
     *
     * @param mHandler Handler
     */
    public void requestQueryProductAuditList(String shopNo, String productType, String isOnsale, String productCategoryNo,
                                             int page, Handler mHandler, int success, int fail) {
        String reqUrl = ConstantsUrl.URL_QUERY_PRODUCT_AUDIT_LIST;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("page", String.valueOf(page), true));
        params.putAll(addParams("shopNo", shopNo, true));//店铺编号
        params.put("productType", productType);// 产品类别()
        params.putAll(addParams("isOnsale", isOnsale, true));//审核中（1）或已驳回（0）
        params.putAll(addParams("productCategoryNo", productCategoryNo, true));//分类编号
        params.put("token", CommonUtils.getToken());
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 商品删除-审核中，已驳回
     *
     * @param mHandler Handler
     */
    public void requestGoodsDel(String shopNo, String productCategoryNo, String productNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_GOODS_DEL;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("shopNo", shopNo, true));//店铺编号
        params.putAll(addParams("productCategoryNo", productCategoryNo, true));//分类编号
        params.putAll(addParams("productNo", productNo, true));
        params.putAll(addParams("status", "delete", true));
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_GOODS_DEL_SUCCESS;
        int fail = ConstantHandler.WHAT_GOODS_DEL_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 商品删除-仓库中
     *
     * @param mHandler Handler
     */
    public void requestGoodsDel2(String shopNo, String productCategoryNo, String productNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_GOODS_DEL2;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("shopNo", shopNo, true));//店铺编号
        params.putAll(addParams("productCategoryNo", productCategoryNo, true));//分类编号
        params.putAll(addParams("productNo", productNo, true));
        params.putAll(addParams("status", "delete", true));
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_GOODS_DEL_SUCCESS;
        int fail = ConstantHandler.WHAT_GOODS_DEL_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 商品上架
     *
     * @param mHandler Handler
     */
    public void requestGoodsPutawaySoldout(String shopNo, String isOnsale, String productNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_GOODS_PUTAWAY_SOLDOUT;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("shopNo", shopNo, true));
        params.putAll(addParams("isOnsale", isOnsale, true));//上架（1）或下架（0）
        params.putAll(addParams("productNo", productNo, true));
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_GOODS_PUTAWAY_SOLDOUT_SUCCESS;
        int fail = ConstantHandler.WHAT_GOODS_PUTAWAY_SOLDOUT_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 修改价格
     *
     * @param mHandler
     * @param productNo
     * @param price
     */
    public void updateProductPrice(Handler mHandler, String productNo, String price) {
        String reqUrl = ConstantsUrl.URL_UPDATE_PRODUCT_PRICE;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("token", CommonUtils.getToken());
        params.put("productNo", productNo);
        params.put("price", price);
        int success = ConstantHandler.WHAT_UPDATE_PRODUCT_PRICE_SUCCESS;
        int fail = ConstantHandler.WHAT_UPDATE_PRODUCT_PRICE_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 修改库存
     *
     * @param mHandler
     * @param productNo
     * @param storage
     */
    public void updateProductStorage(Handler mHandler, String productNo, String storage) {
        String reqUrl = ConstantsUrl.URL_UPDATE_PRODUCT_STORAGE;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("token", CommonUtils.getToken());
        params.put("productNo", productNo);
        params.put("storage", storage);
        int success = ConstantHandler.WHAT_UPDATE_PRODUCT_STORAGE_SUCCESS;
        int fail = ConstantHandler.WHAT_UPDATE_PRODUCT_STORAGE_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 添加商品
     *
     * @param mHandler Handler
     */
    public void requestAddCommodity(String shopNo, String productType, String productCategoryNo, String productName,
                                    String description, String units, String imagesPath,
                                    String price, int feeMode, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_GOODS_ADD;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("shopNo", shopNo, false));
        params.putAll(addParams("productType", productType, false));
        params.putAll(addParams("productCategoryNo", productCategoryNo, false));
        params.putAll(addParams("productName", productName, false));
        params.putAll(addParams("description", description, false));
        params.putAll(addParams("units", units, false));
        params.putAll(addParams("imagesPath", imagesPath, false));
        params.putAll(addParams("price", price, false));
        params.putAll(addParams("feeMode", String.valueOf(feeMode), true));
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_GOODS_ADD_SUCCESS;
        int fail = ConstantHandler.WHAT_GOODS_ADD_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 修改驳回商品
     *
     * @param mHandler Handler
     */
    public void requestRejectEditCommodity(String shopNo, String productType, String productCategoryNo, String productName,
                                           String description, String units, String imagesPath,
                                           String price, int feeMode, String productNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_GOODS_REJECT_EDIT;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("shopNo", shopNo, false));
        params.putAll(addParams("productType", productType, false));
        params.putAll(addParams("productCategoryNo", productCategoryNo, false));
        params.putAll(addParams("productName", productName, false));
        params.putAll(addParams("description", description, false));
        params.putAll(addParams("units", units, false));
        params.putAll(addParams("imagesPath", imagesPath, false));
        params.putAll(addParams("price", price, false));
        params.putAll(addParams("feeMode", String.valueOf(feeMode), true));
        params.putAll(addParams("productNo", productNo, false));
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_REJECT_GOODS_EDIT_SUCCESS;
        int fail = ConstantHandler.WHAT_REJECT_GOODS_EDIT_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 修改商品
     *
     * @param mHandler Handler
     */
    public void requestEditCommodity(String shopNo, String productType, String productCategoryNo, String productName,
                                     String description, String units, String imagesPath,
                                     String price, int feeMode, String productNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_GOODS_EDIT;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("shopNo", shopNo, false));
        params.putAll(addParams("productType", productType, false));
        params.putAll(addParams("productCategoryNo", productCategoryNo, false));
        params.putAll(addParams("productName", productName, false));
        params.putAll(addParams("description", description, false));
        params.putAll(addParams("units", units, false));
        params.putAll(addParams("imagesPath", imagesPath, false));
        params.putAll(addParams("price", price, false));
        params.putAll(addParams("feeMode", String.valueOf(feeMode), true));
        params.putAll(addParams("productNo", productNo, false));
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_GOODS_EDIT_SUCCESS;
        int fail = ConstantHandler.WHAT_GOODS_EDIT_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 创建延保订单
     *
     * @param mHandler Handler
     */
    public void requesConfirmExtendedWarrantyOrder(String orderNo, String realPrice, String shopNo, String shopName,
                                                   String address, String receiver, String receiverMobile,
                                                   String receiverPhone, String logisticsType, String logisticsNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_CONFIRM_INSURANCE_ORDER;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("orderNo", orderNo, true));
        params.putAll(addParams("realPrice", realPrice, true));
        params.putAll(addParams("shopNo", shopNo, true));
        params.putAll(addParams("shopName", shopName, true));
        params.putAll(addParams("address", address, false));
        params.putAll(addParams("receiver", receiver, true));
        params.putAll(addParams("receiverMobile", receiverMobile, true));
        params.putAll(addParams("receiverPhone", receiverPhone, true));
        params.putAll(addParams("logisticsType", logisticsType, true));
        params.putAll(addParams("logisticsNo", logisticsNo, true));
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_CONFIRM_INSURANCE_ORDER_SUCCESS;
        int fail = ConstantHandler.WHAT_CONFIRM_INSURANCE_ORDER_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 我的订单-商城订单-取消订单
     * orderNo	订单编号
     *
     * @param mHandler Handler
     */
    public void requestCancelShopOrder(String orderNo, Handler mHandler) {

        String reqUrl = ConstantsUrl.URL_CANCEL_SHOP_ORDER;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        params.put("token", CommonUtils.getToken());
        params.putAll(addParams("orderNo", orderNo, true));

        int success = ConstantHandler.WHAT_CANCEL_MALL_ORDER_SUCCESS;
        int fail = ConstantHandler.WHAT_CANCEL_MALL_ORDER_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 交易报表
     * payTime    必填     年月(2018-05)
     *
     * @param mHandler Handler
     */
    public void requestQueryOrderReport(String token, String payTime, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_ORDER_REPORT;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("shopNo", token);
        params.putAll(addParams("payTime", payTime, true));
        int success = ConstantHandler.WHAT_ORDER_REPORT_SUCCESS;
        int fail = ConstantHandler.WHAT_ORDER_REPORT_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 交易统计（交易列表）
     *
     * @param mHandler Handler
     */
    public void requestTradeStatistic(int page, String shopNo, String yearMonth, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_TRDE_STATISTIC;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addPageParams(page, true));
        params.putAll(addParams("yearMonth", yearMonth, true));
        params.put("shopNo", shopNo);
        int success = page == 1 ? ConstantHandler.WHAT_TRDE_STATISTIC_SUCCESS : ConstantHandler.WHAT_TRDE_STATISTIC_MORE_SUCCESS;
        int fail = page == 1 ? ConstantHandler.WHAT_TRDE_STATISTIC_FAIL : ConstantHandler.WHAT_TRDE_STATISTIC_MORE_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 订单详情
     *
     * @param mHandler Handler
     */
    public void requestOrderDetail(String orderNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_ORDER_DETAIL;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("orderNo", orderNo, true));
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_ORDER_DETAIL_SUCCESS;
        int fail = ConstantHandler.WHAT_ORDER_DETAIL_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 根据类别查找商品
     */
    public void getShopDetailProductByClassifyNo(String shopNo, String productCategoryNo, boolean isSale, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_NEARBY_SHOP_GOODS;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("shopNo", shopNo, true));
        params.putAll(addParams("productType", isSale ? "cuxiao" : "entity", true));
        params.putAll(addParams("productCategoryNo", productCategoryNo, true));
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_SHOP_PRODUCT_BY_CLASSIFY_SUCCESS;
        int fail = ConstantHandler.WHAT_SHOP_PRODUCT_BY_CLASSIFY_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 店铺商品分类
     */
    public void requestShopClassify(String shopNo, String productType, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_NEARBY_SHOP_CLASSIFY;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("shopNo", shopNo, true));
        params.putAll(addParams("productType", productType, false));
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_SHOP_PRODUCT_CLASSIFY_SUCCESS;
        int fail = ConstantHandler.WHAT_SHOP_PRODUCT_CLASSIFY_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 店铺评论列表
     *
     * @param mHandler Handler
     */
    public void requestShopComment(String shopNo, int page, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_SHOP_COMMENT;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addPageParams(page, true));
        params.putAll(addParams("shopNo", shopNo, true));
        params.put("token", CommonUtils.getToken());
        int success = page == 1 ? ConstantHandler.WHAT_SHOP_COMMENT_SUCCESS : ConstantHandler.WHAT_SHOP_COMMENT_MORE_SUCCESS;
        int fail = page == 1 ? ConstantHandler.WHAT_SHOP_COMMENT_FAIL : ConstantHandler.WHAT_SHOP_COMMENT_MORE_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 添加收藏店铺
     *
     * @param mHandler Handler
     */
    public void requestAddShopCollect(String shopNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_ADD_SHOP_COLLECT;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("shopNo", shopNo, true));
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_ADD_SHOP_COLLECT_SUCCESS;
        int fail = ConstantHandler.WHAT_ADD_SHOP_COLLECT_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 删除收藏店铺
     *
     * @param mHandler Handler
     */
    public void requestDelShopCollect(String shopNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_DEL_SHOP_COLLECT;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("shopNo", shopNo, true));
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_DEL_SHOP_COLLECT_SUCCESS;
        int fail = ConstantHandler.WHAT_DEL_SHOP_COLLECT_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 订单结算
     *
     * @param mHandler Handler
     */
    public void requestOrderSettlementList(String productNos, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_ORDER_LIST_SETTLEMENT;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("productNo", productNos, true));
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_ORDER_SETTLEMENT_LIST_SUCCESS;
        int fail = ConstantHandler.WHAT_ORDER_SETTLEMENT_LIST_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 店铺立即购买
     *
     * @param selParams 选中的规格中文描述
     * @param type      1. 普通购买
     *                  2. 任务购买
     *                  3. 3.0任务购买
     */
    public void requestCreateShopOrder(String addressNo, String settleProducts,
                                       String selectedCouponNo, String leaveMsg,
                                       int type, String shopNo, String mProductSetNo, String selParams, Handler mHandler) {


        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        String reqUrl;
        if (type == 2) {
            if (TextUtils.isEmpty(shopNo)) {// 体验任务下单厂促商品
                reqUrl = ConstantsUrl.URL_COMPANY_SHOP_EXP_TASK_ORDER;
            } else { // 购买任务下单厂促商品
                reqUrl = ConstantsUrl.URL_COMPANY_SHOP_ORDER;
                params.putAll(addParams("taskItemNo", shopNo, false));
            }
        } else if (type == 3) {
            reqUrl = ConstantsUrl.URL_3_0_TASK_CREATE_ORDER;
            params.putAll(addParams("memberTaskNo", shopNo, false));
        } else {
            reqUrl = ConstantsUrl.URL_CREATE_SHOP_ORDER;
        }
        // 规格
        params.putAll(addParams("productSetNo", mProductSetNo, false));
        params.putAll(addParams("param", selParams, false));
        params.putAll(addParams("addressNo", addressNo, false));
        params.putAll(addParams("settleProducts", settleProducts, true));
        params.putAll(addParams("couponNo", selectedCouponNo, false));
        // 留言
        params.putAll(addParams("leaveMsg", leaveMsg, false));
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_CREATE_NEARBY_ORDER_SUCCESS;
        int fail = ConstantHandler.WHAT_CREATE_NEARBY_ORDER_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 创建附近订单
     *
     * @param mHandler Handler
     */
    public void requestCreateNearbyOrder(String productNo, String addressNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.URL_CREATE_NEARBY_ORDER;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.putAll(addParams("productNo", productNo, true));
        if (addressNo != null && !addressNo.isEmpty()) {
            params.putAll(addParams("addressNo", addressNo, true));
        }
        params.put("token", CommonUtils.getToken());
        int success = ConstantHandler.WHAT_CREATE_NEARBY_ORDER_SUCCESS;
        int fail = ConstantHandler.WHAT_CREATE_NEARBY_ORDER_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 店铺订单分页列表（状态分类查询）
     * token       必填     登录信息
     * orderStatus  必填   订单类型 nopay(未支付)， income（返麻豆）
     * complete(已完成)
     * hym
     *
     * @param mHandler Handler
     */
    public void requestQueryOrderList(String orderStatus, int page, Handler mHandler) {
//        String reqUrl = ConstantsUrl.URL_QUERY_ORDER_LIST;
        String reqUrl = ConstantsUrl.URL_MALL_ORDER;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();

        params.put("token", CommonUtils.getToken());
        params.putAll(addPageParams(page, true));
        params.putAll(addParams("orderStatus", orderStatus, true));
        params.putAll(addParams("orderCategory", "shop", true));

        int success = page > 1 ? ConstantHandler.WHAT_QUERY_ORDER_LIST_MORE_SUCCESS : ConstantHandler.WHAT_QUERY_ORDER_LIST_SUCCESS;
        int fail = page > 1 ? ConstantHandler.WHAT_QUERY_ORDER_LIST_MORE_FAIL : ConstantHandler.WHAT_QUERY_ORDER_LIST_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }


    /**
     * 亲子圈列表
     */
    public void requestGetParentChildCircleList(int page, Handler mHandler) {
        String reqUrl = ConstantsUrl.CIRCLE_LIST_URL;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("token", CommonUtils.getToken());
        params.putAll(addPageParams(page, true));
        int success = page == 1 ? ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_SUCCESS : ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_MORE_SUCCESS;
        int fail = page == 1 ? ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_FAIL : ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_MORE_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 我的亲子圈列表
     */
    public void requestGetMineParentChildCircleList(int page, Handler mHandler) {
        String reqUrl = ConstantsUrl.MY_CIRCLE_LIST_URL;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("token", CommonUtils.getToken());
        params.putAll(addPageParams(page, true));
        int success = page == 1 ? ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_SUCCESS : ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_MORE_SUCCESS;
        int fail = page == 1 ? ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_FAIL : ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_MORE_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 亲子圈点赞
     */
    public void parentChildCircleStart(String shuoshuoNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.CIRCLE_START_URL;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("token", CommonUtils.getToken());
        params.putAll(addParams("shuoshuoNo", shuoshuoNo, true));

        int success = ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_START_SUCCESS;
        int fail = ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_START_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 发布亲子圈
     */
    public void requestRelease(String content, String images, Handler mHandler) {
        String reqUrl = ConstantsUrl.PARENT_CHILD_RELEASE_URL;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("token", CommonUtils.getToken());
        params.put("content", content);
        params.put("images", images);

        params.put("isReprinted", "0"); // 是否转载
        params.put("country", "0"); // 当前国家
        params.put("province", "0");// 省
        params.put("city", "0");    // 市
        params.put("area", "0");    // 区
        params.put("Longitude", (String) SharedPreferencesUtils.get(Constants.APP_LOCATION_LONGITUDE, Constants.APP_DEFAULT_LONGITUDE));
        params.put("Latitude", (String) SharedPreferencesUtils.get(Constants.APP_LOCATION_LATITUDE, Constants.APP_DEFAULT_LATITUDE));
        params.put("isShowAddress", "1"); // 是否显示地址
        int success = ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_RELEASE_SUCCESS;
        int fail = ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_RELEASE_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 亲子圈详情
     */
    public void getParentChildCircleDetail(String shuoshuoNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.CIRCLE_DETAIL_URL;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("token", CommonUtils.getToken());
        params.put("shuoshuoNo", shuoshuoNo);

        int success = ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_DETAIL_SUCCESS;
        int fail = ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_DETAIL_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 亲子圈评论列表
     */
    public void getParentChildDiscussList(int page, String shuoshuoNo, Handler mHandler) {
        String reqUrl = ConstantsUrl.PARENT_CHILD_DISCUSS_LIST_URL;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("token", CommonUtils.getToken());
        params.put("businessNo", shuoshuoNo);
        params.putAll(addPageParams(page, true));

        int success = ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_DISCUSS_SUCCESS;
        int fail = ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_DISCUSS_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }

    /**
     * 亲子圈添加评论
     */
    public void parentChildAddDiscuss(String shuoshuoNo, String content, Handler mHandler) {
        String reqUrl = ConstantsUrl.PARENT_CHILD_ADD_DISCUSS_URL;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        params.put("token", CommonUtils.getToken());
        params.put("businessNo", shuoshuoNo);
        params.put("content", content);

        int success = ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_ADD_DISCUSS_SUCCESS;
        int fail = ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_ADD_DISCUSS_FAIL;
        OkHttpUtil.getInstance().postResponseExecute(reqUrl, headers, params, "", mHandler, success, fail);
    }


}
