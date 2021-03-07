package com.yimeng.net.lxmm_net;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.yimeng.config.ConstantHandler;
import com.yimeng.utils.CommonUtils;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Desction: okhttp基础使用
 * Author: xp
 * Date: 2016年12月20日 11:00
 */
public class OkHttpUtil {

    public static final MediaType MEDIA_TYPE_JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static final byte[] LOCKER = new byte[0];
    private static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static OkHttpUtil mInstance;
    private OkHttpClient mOkHttpClient;


    private OkHttpUtil() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.readTimeout(15, TimeUnit.SECONDS);    //读取超时
        clientBuilder.connectTimeout(15, TimeUnit.SECONDS); //连接超时
        clientBuilder.writeTimeout(15, TimeUnit.SECONDS);   //写入超时
        clientBuilder.sslSocketFactory(createSSLSocketFactory()); // 添加HTTPS支持
        clientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
//        clientBuilder.cookieJar(CookieStore.getInstance(MyApplication.instance.getContext()));// 添加Cookie
        mOkHttpClient = clientBuilder.build();
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ssfFactory;
    }

    public static OkHttpUtil getInstance() {
        if (mInstance == null) {
            synchronized (LOCKER) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtil();
                }
            }
        }
        return mInstance;
    }


    /**
     * 设置请求头
     *
     * @param headersParams 头部参数
     * @return Headers
     */
    private Headers getHeaders(Map<String, String> headersParams) {
        Headers headers;
        Headers.Builder headersbuilder = new Headers.Builder();

        if (headersParams != null) {
            Iterator<String> iterator = headersParams.keySet().iterator();
            String key;
            while (iterator.hasNext()) {
                key = iterator.next();
                headersbuilder.add(key, headersParams.get(key));
                Log.d("get http", "headers===" + key + "====" + headersParams.get(key));
            }
        }
        headers = headersbuilder.build();

        return headers;
    }

    /**
     * post方法获取response
     *
     * @param reqUrl        UR连接
     * @param headersParams 请求头参数
     * @param params        请求参数
     * @param object        标签
     * @param mHandler      Handler
     */
    public void postResponseExecute(final String reqUrl, Map<String, String> headersParams, Map<String, String> params,
                                    final Object object, final Handler mHandler, final int success, final int fail) {

        // 验证网络连接是否正常
        if (!CommonUtils.isNetworkConnected()) {
//            ToastUtils.showToast(R.string.text_network_connected_error);
            Message msg = ResponseParserUtil.getInstance().getExceptionMessage(mHandler.obtainMessage(), fail);
            mHandler.sendMessage(msg);
            return;
        }
        // 通用参数log

        // 拼接测试参数
        String text = reqUrl + "?";
        for (String str : params.keySet()) {
            text += str + "=" + params.get(str) + "&";
        }
        text = text.substring(0, text.length() - 1);

//        headersParams = headersParams.size() == 0 ? getDefaultHeaderParams() : headersParams;
//        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, getJsonRequestBody(params));

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(reqUrl); //添加URL地址
        requestBuilder.headers(getHeaders(headersParams));  //添加请求头
        requestBuilder.post(getRequestBody(params)); //添加参数
        requestBuilder.tag(object); //添加请求标签
        Request request = requestBuilder.build();


        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call arg0, IOException arg1) {
                arg0.cancel();

                Message msg = mHandler.obtainMessage();
                msg = ResponseParserUtil.getInstance().getDefaultFailMessage(msg, fail);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call arg0, Response arg1) throws IOException {

                String response = arg1.body().string();
                ResponseParserUtil.getInstance().parserCheckResult(response, mHandler, success, fail);

                if (arg1.code() == 200) {
                    switch (success) {
                        case ConstantHandler.WHAT_ADDRESS_REMOVE_SUCCESS:
                            // 删除地址
                        case ConstantHandler.WHAT_ORDER_PAY_SUCCESS:
                            //订单支付(余额支付)
                        case ConstantHandler.WHAT_GOODS_ADD_SUCCESS:
                            // 自有商品添加
                        case ConstantHandler.WHAT_GOODS_EDIT_SUCCESS:
                            // 商品编辑
                        case ConstantHandler.WHAT_REJECT_GOODS_EDIT_SUCCESS:
                            // 驳回商品编辑
                        case ConstantHandler.WHAT_UPDATE_PRODUCT_STORAGE_SUCCESS:
                            // 修改商品库存
                        case ConstantHandler.WHAT_UPDATE_PRODUCT_PRICE_SUCCESS:
                            // 修改商品价格
                        case ConstantHandler.WHAT_GOODS_PUTAWAY_SOLDOUT_SUCCESS:
                            //商品上下架
                        case ConstantHandler.WHAT_GOODS_DEL_SUCCESS:
                            // 商品删除
                        case ConstantHandler.WHAT_SHOP_COMMODITY_CLASSIFY_DELECT_SUCCESS:
                            // 商品分类删除
                        case ConstantHandler.WHAT_ADD_SHOP_COLLECT_SUCCESS:
                            // 添加店铺收藏
                        case ConstantHandler.WHAT_DEL_SHOP_COLLECT_SUCCESS:
                            // 删除店铺收藏
                        case ConstantHandler.WHAT_SHOP_COMMODITY_CLASSIFY_EDIT_SUCCESS:
                            // 商品分类编辑
                        case ConstantHandler.WHAT_SHOP_COMMODITY_CLASSIFY_ADD_SUCCESS:
                            // 商品分类添加
                        case ConstantHandler.WHAT_UPDATE_SHOP_SUCCESS:
                            // 编辑店铺资料
                        case ConstantHandler.WHAT_APPLY_SHOP_SUCCESS:
                            // 申请开店
                        case ConstantHandler.WHAT_RETURN_OF_MONEY_SUCCESS:
                            // 退款申请
                        case ConstantHandler.WHAT_SET_PROXY_SUCCESS:
                            // 设置代理
                        case ConstantHandler.WHAT_ADD_MEMBER_BANKCARD_SUCCESS:
                            // 添加银行卡
                        case ConstantHandler.WHAT_UPDATE_MEMBER_BANKCARD_SUCCESS:
                            // 修改银行卡
                        case ConstantHandler.WHAT_DEL_WITHDRAW_APPLY_SUCCESS:
                            // 删除银行卡
                        case ConstantHandler.WHAT_ADD_WITHDRAW_APPLY_SUCCESS:
                            // 提现申请
                        case ConstantHandler.WHAT_CERTIIFICATION_MEMBER_INF_SUCCESS:
                            // 实名认证提交
                        case ConstantHandler.WHAT_ORDER_COMMENT_SUCCESS:
                            // 订单评价
                        case ConstantHandler.WHAT_ADDRESS_ADD_SUCCESS:
                            // 添加地址
                        case ConstantHandler.WHAT_ADDRESS_SAVE_SUCCESS:
                            // 修改地址
                        case ConstantHandler.WHAT_CANCEL_MALL_ORDER_SUCCESS:
                            //我的订单-商城订单-取消订单
                        case ConstantHandler.WHAT_CONFIRM_MALL_ORDER_SUCCESS:
                            //我的订单-商城订单-确认收货
                        case ConstantHandler.WHAT_DELETE_SHOP_CAR_SUCCESS:
                            //删除购物车-从购物车删除商品
                        case ConstantHandler.WHAT_UPDATE_SHOP_CAR_NUM_SUCCESS:
                            //修改购物车商品数量
                        case ConstantHandler.WHAT_ADD_SHOP_CAR_SUCCESS:
                            //添加购物车-添加商品到购物车
                        case ConstantHandler.WHAT_ADD_GOODS_COLLECT_SUCCESS:
                            // 添加商品收藏
                        case ConstantHandler.WHAT_DEL_GOODS_COLLECT_SUCCESS:
                            // 删除商品收藏
                            ResponseParserUtil.getInstance().parserCommon(response, mHandler, success, fail);
                            break;

                        case ConstantHandler.WHAT_REFRESH_TOKEN_SUCCESS:
                            // 刷新token
                            ResponseParserUtil.getInstance().parserRefreshToken(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_HOT_SALES_CATEGORY_LIST_SUCCESS:
                            //热卖分类列表
                            ResponseParserUtil.getInstance().parserHotSalesCategoryList(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_GET_BANNER_LIST_SUCCESS:
                            //商城首页轮播图
                            ResponseParserUtil.getInstance().parserGetBannerList(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_GET_HOT_SALES_PRODUCT_SUCCESS:
                        case ConstantHandler.WHAT_GET_HOT_SALES_PRODUCT_MORE_SUCCESS:
                            //热卖商品
                            ResponseParserUtil.getInstance().parserGetHotSalesProduct(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_PRODUCT_CATEGORIES_SUCCESS:
                            //商品分类列表
                            ResponseParserUtil.getInstance().parserProductCategories(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_PRODUCT_BY_MENU_NO_SUCCESS:
                            //商品列表（根据分类查询商品）
                            ResponseParserUtil.getInstance().parserProductByMenuNo(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_LOG_SEARCH_SUCCESS:
                            //搜索结果分页列表(少于20条)
                            ResponseParserUtil.getInstance().parserLogSearch(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_HOT_SEARCH_SUCCESS:
                            //热门搜索
                            ResponseParserUtil.getInstance().parserHotSearch(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_SEARCH_PRODUCT_SUCCESS:
                        case ConstantHandler.WHAT_SEARCH_PRODUCT_MORE_SUCCESS:
                            //商品搜索
                            ResponseParserUtil.getInstance().parserSearchProduct(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_PRODUCT_COMMENT_SUCCESS:
                        case ConstantHandler.WHAT_PRODUCT_COMMENT_MORE_SUCCESS:
                            //商品评论分页列表
                            ResponseParserUtil.getInstance().parserRepairProductComment(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_QUERY_PRODUCT_DETAIL_SUCCESS:
                            //商品详情
                            ResponseParserUtil.getInstance().parserProductDetail(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_PRODUCT_SET_BY_PRODUCT_NO_SUCCESS:
                            //商品款式规格
                            ResponseParserUtil.getInstance().parserProductSetByProductNo(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_MY_SHOP_CAR_SUCCESS:
                            //购物车列表
                            ResponseParserUtil.getInstance().parserMyShopCar(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_ADDRESS_ALL_SUCCESS:
                            // 地址信息
                            ResponseParserUtil.getInstance().parserAddressInfo(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_PAY_NOW_SUCCESS:
                            //立即购买信息
                        case ConstantHandler.WHAT_SHOP_CAR_SETTLE_SUCCESS:
                            //购物车结算页面商品信息
                            ResponseParserUtil.getInstance().parserShopCarSettle(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_CONFIRM_INSURANCE_ORDER_SUCCESS:
                            // 确认延保订单
                        case ConstantHandler.WHAT_SURE_TOPAY_SUCCESS:
                            //立即购买（确认支付生成订单）
                        case ConstantHandler.WHAT_SHOPCART_CREATE_ORDER_SUCCESS:
                            //购物车结算创建订单
                            ResponseParserUtil.getInstance().parserCreateOrder(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_WECHAT_PAY_PARAMS_SUCCESS:
                            // 微信支付参数
                            ResponseParserUtil.getInstance().parserWechatPayParams(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_ALIPAY_PARAMS_SUCCESS:
                            // 支付宝支付参数
                            ResponseParserUtil.getInstance().parserAlipayParams(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_MALL_ORDER_SUCCESS:
                        case ConstantHandler.WHAT_MALL_ORDER_MORE_SUCCESS:
                            // 我的订单-商城订单分页列表
                            ResponseParserUtil.getInstance().parserMallOrderList(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_IMAGE_UPLOAD_SUCCESS:
                            // 图片上传
                            ResponseParserUtil.getInstance().parserImageUpload(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_PRODUCT_COLLECT_SUCCESS:
                        case ConstantHandler.WHAT_PRODUCT_COLLECT_MORE_SUCCESS:
                            // 商品收藏
                            ResponseParserUtil.getInstance().parserProductCollect(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_MY_WALLET_SUCCESS:
                            // 我的钱包
                            ResponseParserUtil.getInstance().parserMyWallet(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_TRANSACTION_RECORDS_SUCCESS:
                        case ConstantHandler.WHAT_TRANSACTION_RECORDS_MORE_SUCCESS:
                            // 交易记录
                            ResponseParserUtil.getInstance().parserTransactionRecords(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_MEMBER_BANKCARD_SUCCESS:
                            // 查询银行卡
                            ResponseParserUtil.getInstance().parserMemberBankcard(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_WITHDRAW_APPLY_SUCCESS:
                        case ConstantHandler.WHAT_WITHDRAW_APPLY_MORE_SUCCESS:
                            // 提现记录(分页查询)
                            ResponseParserUtil.getInstance().parserWithdrawApply(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_BONUS_SUCCESS:
                            // 我的收益
                            ResponseParserUtil.getInstance().parserBonus(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_PAY_RECORDS_SUCCESS:
                        case ConstantHandler.WHAT_PAY_RECORDS_MORE_SUCCESS:
                            // 收益记录
                            ResponseParserUtil.getInstance().parserPayRecords(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_QUERY_MEMBER_BY_MOBILE_SUCCESS:
                        case ConstantHandler.WHAT_QUERY_MEMBER_BY_NO_SUCCESS:
                            // 查询待添加的执行经理
                            ResponseParserUtil.getInstance().parserUserInfo(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_RECHARE_ADD_SUCCESS:
                            // 添加充值记录
                            ResponseParserUtil.getInstance().parserRechareAdd(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_QUERY_STATE_OF_RETURN_SUCCESS:
                            // 查询订单状态
                            ResponseParserUtil.getInstance().parserStateOfReturn(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_RETURN_OF_GOODS_SUCCESS:
                            // 退货申请
                            ResponseParserUtil.getInstance().parserReturnOfGoodsApply(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_RETURN_SEND_BACK_CONFIRM_SUCCESS:
                            // 回邮确认
                            ResponseParserUtil.getInstance().parserReturnSendBackConfirm(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_QUERY_FLOW_LIST_SUCCESS:
                            // 物流公司
                            ResponseParserUtil.getInstance().parserFlowList(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_SHOP_DETAIL_SUCCESS:
                            // 店铺详情
                        case ConstantHandler.WHAT_SHOP_AUDIT_APPLY_DETAIL_SUCCESS:
                            // 店铺入驻审核未通过店铺信息详情
                            ResponseParserUtil.getInstance().parserShopDetail(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_SHOP_INFO_SUCCESS:
                            // 店铺信息
                            ResponseParserUtil.getInstance().parserShopInfo(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_ACCOUNT_DETAIL_SUCCESS:
                            // 店铺账户详情
                            ResponseParserUtil.getInstance().parserAccountDetail(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_ORDER_SETTLE_SUCCESS:
                        case ConstantHandler.WHAT_ORDER_SETTLE_MORE_SUCCESS:
                            // 店铺未入账记录
                            ResponseParserUtil.getInstance().parserOrderSettle(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_SHOP_COMMODITY_STAT_SUCCESS:
                            // 店铺商品分类统计列表
                            ResponseParserUtil.getInstance().parserShopCommodityStat(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_QUERY_PRODUCT_LIST_SUCCESS:
                        case ConstantHandler.WHAT_QUERY_PRODUCT_LIST_MORE_SUCCESS:
                        case ConstantHandler.WHAT_QUERY_PRODUCT_AUDIT_LIST_SUCCESS:
                        case ConstantHandler.WHAT_QUERY_PRODUCT_AUDIT_LIST_MORE_SUCCESS:
                            // 分类商品列表
                            ResponseParserUtil.getInstance().parserClassifyCommodity(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_ORDER_REPORT_SUCCESS:
                            // 交易报表
                            ResponseParserUtil.getInstance().parserOrderReport(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_TRDE_STATISTIC_SUCCESS:
                        case ConstantHandler.WHAT_TRDE_STATISTIC_MORE_SUCCESS:
                            // 交易统计
                            ResponseParserUtil.getInstance().parserTrdeStatistic(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_ORDER_DETAIL_SUCCESS:
                            // 订单详情
                            ResponseParserUtil.getInstance().parserOrderDetail(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_SHOP_PRODUCT_BY_CLASSIFY_SUCCESS:
                            // 根据类别查找商品
                            ResponseParserUtil.getInstance().parseShopProductByClassify(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_SHOP_PRODUCT_CLASSIFY_SUCCESS:
                            // 店铺商品分类
                            ResponseParserUtil.getInstance().parseShopProductClassify(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_SHOP_COMMENT_SUCCESS:
                        case ConstantHandler.WHAT_SHOP_COMMENT_MORE_SUCCESS:
                            // 店铺评论
                            ResponseParserUtil.getInstance().parserShopComment(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_ORDER_SETTLEMENT_LIST_SUCCESS:
                            // 店铺订单结算
                            ResponseParserUtil.getInstance().parserOrderSettlementList(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_CREATE_NEARBY_ORDER_SUCCESS:
                            // 创建附近订单
                            ResponseParserUtil.getInstance().parserCreateNearbyOrder(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_SHOP_COLLECT_SUCCESS:
                        case ConstantHandler.WHAT_SHOP_COLLECT_MORE_SUCCESS:
                            // 店铺收藏
                            ResponseParserUtil.getInstance().parserShopCollect(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_QUERY_ORDER_LIST_SUCCESS:
                        case ConstantHandler.WHAT_QUERY_ORDER_LIST_MORE_SUCCESS:
                            // 我的订单-店铺订单
                            ResponseParserUtil.getInstance().parserQueryOrderList(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_MY_SHOP_ORDER_SUCCESS:
                        case ConstantHandler.WHAT_MY_SHOP_ORDER_MORE_SUCCESS:
                            // 我的店铺-店铺订单分页列表
                            ResponseParserUtil.getInstance().parserMyShopOrderInfo(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_SUCCESS:
                        case ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_MORE_SUCCESS:
                            // 亲子圈列表
                            ResponseParserUtil.getInstance().parseParentChildCircleList(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_START_SUCCESS:
                            // 亲子圈点赞
                            ResponseParserUtil.getInstance().parserCommonGetString(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_RELEASE_SUCCESS:
                            // 亲子圈发布
                            ResponseParserUtil.getInstance().parserCommonGetString(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_DETAIL_SUCCESS:
                            // 亲子圈详情
                            ResponseParserUtil.getInstance().parseParentChildCircleDetail(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_DISCUSS_SUCCESS:
                            // 亲子圈评论列表
                            ResponseParserUtil.getInstance().parseParentChildCircleDiscussList(response, mHandler, success, fail);
                            break;
                        case ConstantHandler.WHAT_PARENT_CHILD_CIRCLE_ADD_DISCUSS_SUCCESS:
                            // 亲子圈添加评论
                            ResponseParserUtil.getInstance().parserCommonGetString(response, mHandler, success, fail);
                            break;
                        default:
                            Message message = new Message();
                            message.what = success;
                            message.obj = response;
                            mHandler.sendMessage(message);
                            break;
                    }
                } else {
                    // 通讯异常
                    ResponseParserUtil.getInstance().parserErrorData(response, mHandler, success, fail);
                }
            }
        });
    }

    /**
     * post请求参数
     *
     * @param BodyParams 主体参数
     * @return RequestBody
     */
    public RequestBody getRequestBody(Map<String, String> BodyParams) {
        RequestBody body;
        okhttp3.FormBody.Builder formEncodingBuilder = new okhttp3.FormBody.Builder();
        if (BodyParams != null) {
            Iterator<String> iterator = BodyParams.keySet().iterator();
            String key;
            while (iterator.hasNext()) {
                key = iterator.next();
                formEncodingBuilder.add(key, BodyParams.get(key));
            }
        }
        body = formEncodingBuilder.build();
        return body;
    }

    /**
     * post方法上传文件
     *
     * @param reqUrl        UR连接
     * @param headersParams 请求头参数
     * @param params        请求参数
     * @param object        标签
     * @param mHandler      Handler
     */
    public void uploadFileExecute(String reqUrl, Map<String, String> headersParams, Map<String, String> params,
                                  Map<String, String> fileParams, final Object object, final Handler mHandler,
                                  final int success, final int fail) {

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(reqUrl); //添加URL地址
        requestBuilder.headers(getHeaders(headersParams));  //添加请求头
        requestBuilder.post(getFileRequestBody(params, fileParams)); //添加参数
        requestBuilder.tag(object); //添加请求标签

        Request request = requestBuilder.build();


        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call arg0, IOException arg1) {
                Message mess = mHandler.obtainMessage();//
                mess.what = 404;
                mess.obj = "通讯错误-020";
                mHandler.sendMessage(mess);
            }

            @Override
            public void onResponse(Call arg0, Response arg1) throws IOException {
                String response;
                Message mess = mHandler.obtainMessage();
                if (arg1.code() == 200) {
                    response = arg1.body().string();
                    Log.e("get http", "get===" + response);

                } else {
                    mess.what = arg1.code();
                    mess.obj = "通讯异常-" + arg1.code();
                }
                mHandler.sendMessage(mess);
            }
        });
    }

    /**
     * Post上传文件的参数
     *
     * @param BodyParams 主体参数
     * @param fileParams 文件参数
     * @return RequestBody
     */
    private RequestBody getFileRequestBody(Map<String, String> BodyParams, Map<String, String> fileParams) {
        //带文件的Post参数
        RequestBody body;
        MultipartBody.Builder MultipartBodyBuilder = new MultipartBody.Builder();
        MultipartBodyBuilder.setType(MultipartBody.FORM);
        RequestBody fileBody;

        if (BodyParams != null) {
            Iterator<String> iterator = BodyParams.keySet().iterator();
            String key;
            while (iterator.hasNext()) {
                key = iterator.next();
                MultipartBodyBuilder.addFormDataPart(key, BodyParams.get(key));
            }
        }

        if (fileParams != null) {
            Iterator<String> iterator = fileParams.keySet().iterator();
            String key;
            while (iterator.hasNext()) {
                key = iterator.next();
                MultipartBodyBuilder.addFormDataPart(key, fileParams.get(key));
                File file = new File(fileParams.get(key));
                fileBody = RequestBody.create(MEDIA_TYPE_MARKDOWN, file);
                MultipartBodyBuilder.addFormDataPart(key, file.getName(), fileBody);
            }
        }

        body = MultipartBodyBuilder.build();
        return body;
    }

    // HTTPS 相关
    private static class TrustAllCerts implements X509TrustManager {
        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[0];
        }

    }

}
