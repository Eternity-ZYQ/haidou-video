package com.yimeng.config;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/11/23 0023 上午 11:49.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class ConstantHandler {

    // 百度语音唤醒
    public static final int BAIDU_REQUEST_CODE = 666;
    // 下架传值用
    public static final int WHAT_HANDLER_SOLDOUT = 95;
    // 上架传值用
    public static final int WHAT_HANDLER_PUTAWAY = 96;
    public static final int WHAT_JUMP_SHOPCART = 90;
    // RESULT_CODE
    public static final int RESULT_CODE_COMMON_DATA = 91;
    public static final int RESULT_CODE_SELECT_ADDRESS = 94;
    // 删除传值用
    public static final int WHAT_HANDLER_DELECT = 97;
    // 点击传值用
    public static final int WHAT_HANDLER_CLICK = 98;
    // 点击传值用1
    public static final int WHAT_HANDLER_CLICK_1 = 99;
    // 网络故障
    public static final int WHAT_EXCEPTION_ERROR = 101;
    // 订单支付
    public static final int WHAT_ORDER_PAY_SUCCESS = 186;
    public static final int WHAT_ORDER_PAY_FAIL = -186;

    // 刷新token
    public static final int WHAT_REFRESH_TOKEN_SUCCESS = 118;
    public static final int WHAT_REFRESH_TOKEN_FAIL = -118;
    //热卖分类列表
    public static final int WHAT_HOT_SALES_CATEGORY_LIST_SUCCESS = 263;
    public static final int WHAT_HOT_SALES_CATEGORY_LIST_FAIL = -263;
    //商城首页轮播图
    public static final int WHAT_GET_BANNER_LIST_SUCCESS = 262;
    public static final int WHAT_GET_BANNER_LIST_FAIL = -262;
    //热卖商品
    public static final int WHAT_GET_HOT_SALES_PRODUCT_SUCCESS = 264;
    public static final int WHAT_GET_HOT_SALES_PRODUCT_FAIL = -264;
    public static final int WHAT_GET_HOT_SALES_PRODUCT_MORE_SUCCESS = 2640;
    public static final int WHAT_GET_HOT_SALES_PRODUCT_MORE_FAIL = -2640;
    //商品分类列表
    public static final int WHAT_PRODUCT_CATEGORIES_SUCCESS = 265;
    public static final int WHAT_PRODUCT_CATEGORIES_FAIL = -265;
    //商品列表（根据分类查询商品）
    public static final int WHAT_PRODUCT_BY_MENU_NO_SUCCESS = 269;
    public static final int WHAT_PRODUCT_BY_MENU_NO_FAIL = -269;
    //搜索结果分页列表(少于20条)
    public static final int WHAT_LOG_SEARCH_SUCCESS = 256;
    public static final int WHAT_LOG_SEARCH_FAIL = -256;
    //热门搜索
    public static final int WHAT_HOT_SEARCH_SUCCESS = 255;
    public static final int WHAT_HOT_SEARCH_FAIL = -255;
    //商品搜索
    public static final int WHAT_SEARCH_PRODUCT_SUCCESS = 268;
    public static final int WHAT_SEARCH_PRODUCT_FAIL = -268;
    public static final int WHAT_SEARCH_PRODUCT_MORE_SUCCESS = 2680;
    public static final int WHAT_SEARCH_PRODUCT_MORE_FAIL = -2680;
    //商品评论分页列表
    public static final int WHAT_PRODUCT_COMMENT_SUCCESS = 251;
    public static final int WHAT_PRODUCT_COMMENT_FAIL = -251;
    public static final int WHAT_PRODUCT_COMMENT_MORE_SUCCESS = 2510;
    public static final int WHAT_PRODUCT_COMMENT_MORE_FAIL = -2510;
    //商品详情
    public static final int WHAT_QUERY_PRODUCT_DETAIL_SUCCESS = 254;
    public static final int WHAT_QUERY_PRODUCT_DETAIL_FAIL = -254;
    // 添加商品收藏
    public static final int WHAT_ADD_GOODS_COLLECT_SUCCESS = 173;
    public static final int WHAT_ADD_GOODS_COLLECT_FAIL = -173;
    // 删除商品收藏
    public static final int WHAT_DEL_GOODS_COLLECT_SUCCESS = 174;
    public static final int WHAT_DEL_GOODS_COLLECT_FAIL = -174;
    // 添加充值记录
    public static final int WHAT_RECHARE_ADD_SUCCESS = 193;
    public static final int WHAT_RECHARE_ADD_FAIL = -193;
    //添加购物车-添加商品到购物车
    public static final int WHAT_ADD_SHOP_CAR_SUCCESS = 258;
    public static final int WHAT_ADD_SHOP_CAR_FAIL = -258;
    //商品款式规格
    public static final int WHAT_PRODUCT_SET_BY_PRODUCT_NO_SUCCESS = 252;
    public static final int WHAT_PRODUCT_SET_BY_PRODUCT_NO_FAIL = -252;
    //购物车列表
    public static final int WHAT_MY_SHOP_CAR_SUCCESS = 257;
    public static final int WHAT_MY_SHOP_CAR_FAIL = -257;
    //修改购物车商品数量
    public static final int WHAT_UPDATE_SHOP_CAR_NUM_SUCCESS = 260;
    public static final int WHAT_UPDATE_SHOP_CAR_NUM_FAIL = -260;
    //删除购物车-从购物车删除商品
    public static final int WHAT_DELETE_SHOP_CAR_SUCCESS = 259;
    public static final int WHAT_DELETE_SHOP_CAR_FAIL = -259;
    //取消支付
    public static final int WHAT_CANCEL_PAY_SUCCESS = 277;
    public static final int WHAT_CANCEL_PAY_FAIL = -277;
    // 收货地址
    public static final int WHAT_ADDRESS_ALL_SUCCESS = 107;
    public static final int WHAT_ADDRESS_ALL_FAIL = -107;
    //立即购买
    public static final int WHAT_PAY_NOW_SUCCESS = 272;
    public static final int WHAT_PAY_NOW_FAIL = -272;
    //购物车结算页面商品信息
    public static final int WHAT_SHOP_CAR_SETTLE_SUCCESS = 261;
    public static final int WHAT_SHOP_CAR_SETTLE_FAIL = -261;
    //立即购买（确认支付生成订单）
    public static final int WHAT_SURE_TOPAY_SUCCESS = 273;
    public static final int WHAT_SURE_TOPAY_FAIL = -273;
    // 购物车结算创建订单
    public static final int WHAT_SHOPCART_CREATE_ORDER_SUCCESS = 185;
    public static final int WHAT_SHOPCART_CREATE_ORDER_FAIL = -185;
    // 订单评价
    public static final int WHAT_ORDER_COMMENT_SUCCESS = 187;
    public static final int WHAT_ORDER_COMMENT_FAIL = -187;
    // 微信支付参数
    public static final int WHAT_WECHAT_PAY_PARAMS_SUCCESS = 176;
    public static final int WHAT_WECHAT_PAY_PARAMS_FAIL = -176;
    //支付宝支付参数
    public static final int WHAT_ALIPAY_PARAMS_SUCCESS = 278;
    public static final int WHAT_ALIPAY_PARAMS_FAIL = -278;
    // 店铺账户详情
    public static final int WHAT_ACCOUNT_DETAIL_SUCCESS = 200;
    public static final int WHAT_ACCOUNT_DETAIL_FAIL = -200;
    // 店铺未入账记录
    public static final int WHAT_ORDER_SETTLE_SUCCESS = 207;
    public static final int WHAT_ORDER_SETTLE_FAIL = -207;
    public static final int WHAT_ORDER_SETTLE_MORE_SUCCESS = 2070;
    public static final int WHAT_ORDER_SETTLE_MORE_FAIL = -2070;
    //商城订单分页列表
    public static final int WHAT_MALL_ORDER_SUCCESS = 270;
    public static final int WHAT_MALL_ORDER_FAIL = -270;
    public static final int WHAT_MALL_ORDER_MORE_SUCCESS = 271;
    public static final int WHAT_MALL_ORDER_MORE_FAIL = -271;
    //我的订单-商城订单-取消订单
    public static final int WHAT_CANCEL_MALL_ORDER_SUCCESS = 275;
    public static final int WHAT_CANCEL_MALL_ORDER_FAIL = -275;
    //我的订单-商城订单-确认收货
    public static final int WHAT_CONFIRM_MALL_ORDER_SUCCESS = 276;
    public static final int WHAT_CONFIRM_MALL_ORDER_FAIL = -276;
    // 修改地址
    public static final int WHAT_ADDRESS_SAVE_SUCCESS = 147;
    public static final int WHAT_ADDRESS_SAVE_FAIL = -147;
    // 删除地址
    public static final int WHAT_ADDRESS_REMOVE_SUCCESS = 112;
    public static final int WHAT_ADDRESS_REMOVE_FAIL = -112;
    // 添加地址
    public static final int WHAT_ADDRESS_ADD_SUCCESS = 145;
    public static final int WHAT_ADDRESS_ADD_FAIL = -145;
    // 图片上传
    public static final int WHAT_IMAGE_UPLOAD_SUCCESS = 148;
    public static final int WHAT_IMAGE_UPLOAD_FAIL = -148;
    // 收藏商品列表
    public static final int WHAT_PRODUCT_COLLECT_SUCCESS = 224;
    public static final int WHAT_PRODUCT_COLLECT_FAIL = -224;
    // 收藏商品列表-加载更多
    public static final int WHAT_PRODUCT_COLLECT_MORE_SUCCESS = 2240;
    public static final int WHAT_PRODUCT_COLLECT_MORE_FAIL = -2240;
    // 收藏商家列表
    public static final int WHAT_SHOP_COLLECT_SUCCESS = 223;
    public static final int WHAT_SHOP_COLLECT_FAIL = -223;
    // 收藏商家列表 -加载更多
    public static final int WHAT_SHOP_COLLECT_MORE_SUCCESS = 2230;
    public static final int WHAT_SHOP_COLLECT_MORE_FAIL = -2230;
    // 实名认证提交
    public static final int WHAT_CERTIIFICATION_MEMBER_INF_SUCCESS = 215;
    public static final int WHAT_CERTIIFICATION_MEMBER_INF_FAIL = -215;
    // 我的钱包
    public static final int WHAT_MY_WALLET_SUCCESS = 179;
    public static final int WHAT_MY_WALLET_FAIL = -179;
    // 交易记录
    public static final int WHAT_TRANSACTION_RECORDS_SUCCESS = 125;
    public static final int WHAT_TRANSACTION_RECORDS_FAIL = -125;
    public static final int WHAT_TRANSACTION_RECORDS_MORE_SUCCESS = 126;
    public static final int WHAT_TRANSACTION_RECORDS_MORE_FAIL = -126;
    // 查询银行卡
    public static final int WHAT_MEMBER_BANKCARD_SUCCESS = 205;
    public static final int WHAT_MEMBER_BANKCARD_FAIL = -205;
    // 提现申请
    public static final int WHAT_ADD_WITHDRAW_APPLY_SUCCESS = 203;
    public static final int WHAT_ADD_WITHDRAW_APPLY_FAIL = -203;
    // 删除银行卡
    public static final int WHAT_DEL_WITHDRAW_APPLY_SUCCESS = 206;
    public static final int WHAT_DEL_WITHDRAW_APPLY_FAIL = -206;
    // 修改银行卡
    public static final int WHAT_UPDATE_MEMBER_BANKCARD_SUCCESS = 199;
    public static final int WHAT_UPDATE_MEMBER_BANKCARD_FAIL = -199;
    // 添加银行卡
    public static final int WHAT_ADD_MEMBER_BANKCARD_SUCCESS = 204;
    public static final int WHAT_ADD_MEMBER_BANKCARD_FAIL = -204;
    // 提现记录(分页查询)
    public static final int WHAT_WITHDRAW_APPLY_SUCCESS = 202;
    public static final int WHAT_WITHDRAW_APPLY_FAIL = -202;
    public static final int WHAT_WITHDRAW_APPLY_MORE_SUCCESS = 2020;
    public static final int WHAT_WITHDRAW_APPLY_MORE_FAIL = -2020;
    // 我的收益统计
    public static final int WHAT_BONUS_SUCCESS = 220;
    public static final int WHAT_BONUS_FAIL = -220;
    // 收益记录
    public static final int WHAT_PAY_RECORDS_SUCCESS = 129;
    public static final int WHAT_PAY_RECORDS_FAIL = -129;
    public static final int WHAT_PAY_RECORDS_MORE_SUCCESS = 130;
    public static final int WHAT_PAY_RECORDS_MORE_FAIL = -130;
    // 添加代理商前查询用户 - 手机号码
    public static final int WHAT_QUERY_MEMBER_BY_MOBILE_SUCCESS = 305;
    public static final int WHAT_QUERY_MEMBER_BY_MOBILE_FAIL = -305;
    // 添加代理商前查询用户 - 用户编号
    public static final int WHAT_QUERY_MEMBER_BY_NO_SUCCESS = 307;
    public static final int WHAT_QUERY_MEMBER_BY_NO_FAIL = -307;
    // 设置代理
    public static final int WHAT_SET_PROXY_SUCCESS = 304;
    public static final int WHAT_SET_PROXY_FAIL = -304;
    // 退货申请
    public static final int WHAT_RETURN_OF_GOODS_SUCCESS = 284;
    public static final int WHAT_RETURN_OF_GOODS_FAIL = -284;
    // 退款申请
    public static final int WHAT_RETURN_OF_MONEY_SUCCESS = 2841;
    public static final int WHAT_RETURN_OF_MONEY_FAIL = -2841;
    // 查询订单状态
    public static final int WHAT_QUERY_STATE_OF_RETURN_SUCCESS = 285;
    public static final int WHAT_QUERY_STATE_OF_RETURN_FAIL = -285;
    // 退货回邮
    public static final int WHAT_RETURN_SEND_BACK_CONFIRM_SUCCESS = 286;
    public static final int WHAT_RETURN_SEND_BACK_CONFIRM_FAIL = -286;
    // 查询物流列表
    public static final int WHAT_QUERY_FLOW_LIST_SUCCESS = 287;
    public static final int WHAT_QUERY_FLOW_LIST_FAIL = -287;
    // 申请开店
    public static final int WHAT_APPLY_SHOP_SUCCESS = 149;
    public static final int WHAT_APPLY_SHOP_FAIL = -149;
    // 店铺信息
    public static final int WHAT_SHOP_INFO_SUCCESS = 150;
    public static final int WHAT_SHOP_INFO_FAIL = -150;
    // 店铺资料修改
    public static final int WHAT_UPDATE_SHOP_SUCCESS = 152;
    public static final int WHAT_UPDATE_SHOP_FAIL = -152;
    // 店铺商品分类编辑
    public static final int WHAT_SHOP_COMMODITY_CLASSIFY_EDIT_SUCCESS = 156;
    public static final int WHAT_SHOP_COMMODITY_CLASSIFY_EDIT_FAIL = -156;
    // 店铺商品分类添加
    public static final int WHAT_SHOP_COMMODITY_CLASSIFY_ADD_SUCCESS = 155;
    public static final int WHAT_SHOP_COMMODITY_CLASSIFY_ADD_FAIL = -155;
    // 店铺详情
    public static final int WHAT_SHOP_DETAIL_SUCCESS = 151;
    public static final int WHAT_SHOP_DETAIL_FAIL = -151;
    // 店铺评论列表
    public static final int WHAT_SHOP_COMMENT_SUCCESS = 169;
    public static final int WHAT_SHOP_COMMENT_FAIL = -169;
    // 店铺评论列表-加载更多
    public static final int WHAT_SHOP_COMMENT_MORE_SUCCESS = 170;
    public static final int WHAT_SHOP_COMMENT_MORE_FAIL = -170;
    // 添加店铺收藏
    public static final int WHAT_ADD_SHOP_COLLECT_SUCCESS = 171;
    public static final int WHAT_ADD_SHOP_COLLECT_FAIL = -171;
    // 删除店铺收藏
    public static final int WHAT_DEL_SHOP_COLLECT_SUCCESS = 172;
    public static final int WHAT_DEL_SHOP_COLLECT_FAIL = -172;
    // 订单结算
    public static final int WHAT_ORDER_SETTLEMENT_SUCCESS = 175;
    public static final int WHAT_ORDER_SETTLEMENT_FAIL = -175;
    public static final int WHAT_ORDER_SETTLEMENT_LIST_SUCCESS = 1751;
    public static final int WHAT_ORDER_SETTLEMENT_LIST_FAIL = -1751;
    // 创建附近订单
    public static final int WHAT_CREATE_NEARBY_ORDER_SUCCESS = 177;
    public static final int WHAT_CREATE_NEARBY_ORDER_FAIL = -177;
    // 店铺商品分类删除
    public static final int WHAT_SHOP_COMMODITY_CLASSIFY_DELECT_SUCCESS = 154;
    public static final int WHAT_SHOP_COMMODITY_CLASSIFY_DELECT_FAIL = -154;
    //店铺订单分页列表（状态分类查询）
    public static final int WHAT_QUERY_ORDER_LIST_SUCCESS = 266;
    public static final int WHAT_QUERY_ORDER_LIST_FAIL = -266;
    //店铺订单分页列表（状态分类查询）--加载更多
    public static final int WHAT_QUERY_ORDER_LIST_MORE_SUCCESS = 267;
    public static final int WHAT_QUERY_ORDER_LIST_MORE_FAIL = -267;
    // 商品上下架
    public static final int WHAT_GOODS_PUTAWAY_SOLDOUT_SUCCESS = 162;
    public static final int WHAT_GOODS_PUTAWAY_SOLDOUT_FAIL = -162;
    // 修改商品库存
    public static final int WHAT_UPDATE_PRODUCT_STORAGE_SUCCESS = 293;
    public static final int WHAT_UPDATE_PRODUCT_STORAGE_FAIL = -293;
    // 商品添加
    public static final int WHAT_GOODS_ADD_SUCCESS = 163;
    public static final int WHAT_GOODS_ADD_FAIL = -163;
    public static final int WHAT_GOODS_HAS_PAY_SERVER_MONEY = 1633;
    // 商品编辑
    public static final int WHAT_GOODS_EDIT_SUCCESS = 164;
    public static final int WHAT_GOODS_EDIT_FAIL = -164;
    // 商品删除
    public static final int WHAT_GOODS_DEL_SUCCESS = 161;
    public static final int WHAT_GOODS_DEL_FAIL = -161;
    // 驳回商品编辑
    public static final int WHAT_REJECT_GOODS_EDIT_SUCCESS = 16499;
    public static final int WHAT_REJECT_GOODS_EDIT_FAIL = -16499;
    // 修改商品价格
    public static final int WHAT_UPDATE_PRODUCT_PRICE_SUCCESS = 294;
    public static final int WHAT_UPDATE_PRODUCT_PRICE_FAIL = -294;
    // 店铺入驻审核未通过店铺信息详情
    public static final int WHAT_SHOP_AUDIT_APPLY_DETAIL_SUCCESS = 1510;
    public static final int WHAT_SHOP_AUDIT_APPLY_DETAIL_FAIL = -1510;

    // 我的店铺-店铺订单分页列表
    public static final int WHAT_MY_SHOP_ORDER_SUCCESS = 182;
    public static final int WHAT_MY_SHOP_ORDER_FAIL = -182;
    // 我的店铺-店铺订单分页列表-加载更多
    public static final int WHAT_MY_SHOP_ORDER_MORE_SUCCESS = 183;
    public static final int WHAT_MY_SHOP_ORDER_MORE_FAIL = -183;
    // 店铺商品分类统计列表
    public static final int WHAT_SHOP_COMMODITY_STAT_SUCCESS = 153;
    public static final int WHAT_SHOP_COMMODITY_STAT_FAIL = -153;
    // 销售中和仓库中商品分页列表
    public static final int WHAT_QUERY_PRODUCT_LIST_SUCCESS = 157;
    public static final int WHAT_QUERY_PRODUCT_LIST_FAIL = -157;
    // 审核中和已驳回商品分页列表
    public static final int WHAT_QUERY_PRODUCT_AUDIT_LIST_SUCCESS = 158;
    public static final int WHAT_QUERY_PRODUCT_AUDIT_LIST_FAIL = -158;
    // 销售中和仓库中商品分页列表-加载更多
    public static final int WHAT_QUERY_PRODUCT_LIST_MORE_SUCCESS = 159;
    public static final int WHAT_QUERY_PRODUCT_LIST_MORE_FAIL = -159;
    // 审核中和已驳回商品分页列表-加载更多
    public static final int WHAT_QUERY_PRODUCT_AUDIT_LIST_MORE_SUCCESS = 160;
    public static final int WHAT_QUERY_PRODUCT_AUDIT_LIST_MORE_FAIL = -160;
    // 商铺确认延保订单（完善延保订单）
    public static final int WHAT_CONFIRM_INSURANCE_ORDER_SUCCESS = 184;
    public static final int WHAT_CONFIRM_INSURANCE_ORDER_FAIL = -184;
    // 交易报表
    public static final int WHAT_ORDER_REPORT_SUCCESS = 201;
    public static final int WHAT_ORDER_REPORT_FAIL = -201;
    // 交易统计
    public static final int WHAT_TRDE_STATISTIC_SUCCESS = 165;
    public static final int WHAT_TRDE_STATISTIC_FAIL = -165;
    // 交易统计-加载更多
    public static final int WHAT_TRDE_STATISTIC_MORE_SUCCESS = 166;
    public static final int WHAT_TRDE_STATISTIC_MORE_FAIL = -166;
    // 订单详情
    public static final int WHAT_ORDER_DETAIL_SUCCESS = 189;
    public static final int WHAT_ORDER_DETAIL_FAIL = -189;
    // 根据类别查找商品
    public static final int WHAT_SHOP_PRODUCT_BY_CLASSIFY_SUCCESS = 327;
    public static final int WHAT_SHOP_PRODUCT_BY_CLASSIFY_FAIL = -327;
    // 店铺分类
    public static final int WHAT_SHOP_PRODUCT_CLASSIFY_SUCCESS = 326;
    public static final int WHAT_SHOP_PRODUCT_CLASSIFY_FAIL = -326;

    // 亲子圈列表
    public static final int WHAT_PARENT_CHILD_CIRCLE_SUCCESS = 319;
    public static final int WHAT_PARENT_CHILD_CIRCLE_FAIL = -319;
    public static final int WHAT_PARENT_CHILD_CIRCLE_MORE_SUCCESS = 3190;
    public static final int WHAT_PARENT_CHILD_CIRCLE_MORE_FAIL = -3190;
    // 亲子圈点赞
    public static final int WHAT_PARENT_CHILD_CIRCLE_START_SUCCESS = 320;
    public static final int WHAT_PARENT_CHILD_CIRCLE_START_FAIL = -320;
    // 亲子圈发布
    public static final int WHAT_PARENT_CHILD_CIRCLE_RELEASE_SUCCESS = 321;
    public static final int WHAT_PARENT_CHILD_CIRCLE_RELEASE_FAIL = -321;
    // 亲子圈详情
    public static final int WHAT_PARENT_CHILD_CIRCLE_DETAIL_SUCCESS = 322;
    public static final int WHAT_PARENT_CHILD_CIRCLE_DETAIL_FAIL = -322;
    // 亲子圈评论列表
    public static final int WHAT_PARENT_CHILD_CIRCLE_DISCUSS_SUCCESS = 323;
    public static final int WHAT_PARENT_CHILD_CIRCLE_DISCUSS_FAIL = -323;
    // 亲子圈添加评论
    public static final int WHAT_PARENT_CHILD_CIRCLE_ADD_DISCUSS_SUCCESS = 324;
    public static final int WHAT_PARENT_CHILD_CIRCLE_ADD_DISCUSS_FAIL = -324;










}
