package com.yimeng.config;


import com.yimeng.haidou.BuildConfig;

/**
 * 请求URL工具类
 */
public class ConstantsUrl {


    //    public static final String HOST = BuildConfig.Host + (BuildConfig.DEBUG ? "/cuxiao_test/" : "/cuxiao/");
    //public static final String HOST = BuildConfig.Host + "api/";
//    public static final String HOST = "http://120.79.178.137:8080/hd/";
    public static final String HOST = "http://hdapp.caihvideo.com/videoad/";

// public static final String HOST = "http://192.168.2.200:8080/cuxiao_test/";

    /**
     * API 地址
     **/
    public static final String API_HOST = HOST + "api/";
    //  public static final String API_HOST = BuildConfig.Host + "videoad/api/";

    /**
     * 图片服务器前缀
     */
//    public static final String UPLOAD_HEAD_URL = "http://lximg.fengdikj.com/upload/";
//    public static final String UPLOAD_HEAD_URL = "http://zhcity.fengdikj.com/upload/";
    public static final String UPLOAD_HEAD_URL = "http://hdapp.caihvideo.com/upload";

    /**
     * 银行卡logo
     */
    public static final String BANK_LOGO_URL = "http://fd.fg321.cn/upload/";

    /**
     * 上传图片地址
     */
    public static final String UPLOAD_IMG_URL = UPLOAD_HEAD_URL + "imageDataUpload.do";
    public static final String UPLOAD_IMG_FROM_URL = UPLOAD_HEAD_URL + "shopImageUpload.do";

    /**
     * 上传视频
     */
    public static final String UPLOAD_VIDEO_URL = "http://vupload.caihvideo.com/video/upload";

    /**
     * 查看物流信息地址
     */
    public static final String LOGISTICS_URL_HEADER = "http://m.kuaidi100.com/index_all.html?";

    /**
     * 检验银行卡
     */
    public static final String CHECK_BANK_CARD = "https://ccdcapi.alipay.com/validateAndCacheCardInfo.json";

    /**
     * 银行卡图片路径
     */
    public static final String IMG_BANK_PATH = BANK_LOGO_URL + "/fileupload/banklogo/";

    /**
     * 查询当前是什么环境
     */
    public static final String CHECK_TEST = API_HOST + "/main/getIsTestEdition";

    /**
     * 地址数据
     */
    public static final String URL_QUERY_ADDRESS = API_HOST + "area/areaQuery";

    /**
     * 获取支付宝授权签名接口
     */
    public static final String URL_GET_ALIPAY_AUTH = API_HOST + "zhifubao/auth/signparams";

    /**
     * 获取支付宝用户信息
     */
    public static final String URL_GET_ALIPAY_MEMBER_INFO = API_HOST + "zhifubao/auth/userinfo";

    /**
     * 保存用户授权信息
     */
    public static final String URL_SAVE_WECHAR_ALIPAY_AUTH_INFO = API_HOST + "main/addAuthConfig";

    /**
     * 获取收货地址的邮费
     */
    public static final String URL_ADDRESS_POSTAGE = API_HOST + "address/getMemberAddressPostage";

    /**
     * 系统提示
     */
    public static final String URL_SYSTEM_MESSAGE = API_HOST + "main/querySystemPrompt";

    /*--------------------------------- 个人中心 ---------------------------------*/

    /**
     * 获取验证码
     */
    public static final String GET_CODE_URL = API_HOST + "msg/msgSend";

    /**
     * 图片验证码
     */
    public static final String GET_IMG_CODE = API_HOST + "msg/code";

    /**
     * 登录
     */
    public static final String APP_LOGIN_URL = API_HOST + "member/login";
    public static final String APP_LOGIN_PWD_URL = API_HOST + "member/appLogin";

    /**
     * 快捷登录(刷新token)
     */
    public static final String LOGIN_MOBILE_URL = API_HOST + "member/mobilelogin";

    /**
     * 退出登录
     */
    public static final String APP_LOGIN_OUT_URL = API_HOST + "member/loginOut";

    /**
     * 协议
     */
//    public static final String URL_PROTOCOL = API_HOST + "main/getAgreement?menuType=agreement&name=";
    //public static final String URL_PROTOCOL = "http://xjapp.china-dong.com/videoad/api/main/getAgreement?menuType=agreement&name=";

    public static final String URL_PROTOCOL = "http://hdapp.caihvideo.com/videoad/api/main/getAgreement?menuType=agreement&name=";
    //// %E7%94%A8%E6%88%B7%E6%B3%A8%E5%86%8C%E5%8F%8A%E9%9A%90%E7%A7%81%E5%8D%8F%E8%AE%AE

    /**
     * 个人资料
     */
//    public static final String URL_GET_MEMBER_INFO = API_HOST + "memberInfo/getMemberInfo";
    public static final String URL_GET_MEMBER_INFO = API_HOST + "member/getMemberInfo.do";

    /**
     * 激活费用
     */
    public static final String URL_ACTIVATE = API_HOST + "menu/getMenu";

    /**
     * 激活
     */
    public static final String URL_ACTIVATE_TOW = API_HOST + "reward/makeActiveMember";

    /**
     * 激活
     */
    public static final String URL_ACTIVATE_PAY = API_HOST + "reward/zhifubaoParam";

    /**
     * 更新资料
     */
    public static final String URL_UPDATE_MEMBER_INFO = API_HOST + "memberInfo/updateMemberInfo";

    /**
     * 实名认证提交
     */
    public static final String URL_CERTIIFICATION_MEMBER_INF = API_HOST + "memberInfo/certificationMemberInfo";

    /**
     * 我的钱包
     */
    public static final String URL_MY_WALLET = API_HOST + "account/queryAccountDetail";

    /**
     * 会员资费
     */
    public static final String URL_VIP_PRICE = API_HOST + "member/getVipRewardContentAndVipType";

    /**
     * 创建开通会员订单
     */
    public static final String URL_VIP_OPEN = API_HOST + "reward/makeVipFee";

    /**
     * 购买活跃度
     */
    public static final String URL_PAY_ACTIVE = API_HOST + "reward/makeActiveBigGift";

    /**
     * 余额交易记录
     */
    public static final String URL_TRANSACTION_RECORD = API_HOST + "accountWater/queryTransactionRecord";

    /**
     * 现金财富/提现额度记录
     */
    public static final String URL_CASH_LOG = API_HOST + "scoreLog/queryScoreLog";

    /**
     * 查询银行卡
     */
    public static final String URL_MEMBER_BANKCARD = API_HOST + "memberBankcard/queryMemberBankcard";

    /**
     * 删除银行卡
     */
    public static final String URL_DEL_WITHDRAW_APPLY = API_HOST + "memberBankcard/delMemberBankcard";

    /**
     * 编辑银行卡
     */
    public static final String URL_UPDATE_MEMBER_BANKCARD = API_HOST + "memberBankcard/updateMemberBankcard";

    /**
     * 添加银行卡
     */
    public static final String URL_ADD_MEMBER_BANKCARD = API_HOST + "memberBankcard/addMemberBankcard";

    /**
     * 提现记录(分页查询)
     */
    public static final String URL_WITHDRAW_APPLY_V_2_1_0 = API_HOST + "withdrawApply/queryWithdrawApply_v2_1_0";

    /**
     * 提现记录(分页查询)   赚呗奖金提现记录
     */
    public static final String URL_WITHDRAW_APPLY_AS_BUNOS = API_HOST + "score/queryMyScoreLogWithdraw";


    /**
     * 我的收益统计
     */
    public static final String URL_BONUS = API_HOST + "bonus/accountAmtAndUnsettlement";

    /**
     * 充值记录
     */
    public static final String URL_PAY_RECORDS = API_HOST + "accountWater/queryTransactionRecord";

    // 申请退款
    public static final String URL_RETURN_OF_MONEY = API_HOST + "refund/appOrderRefundMoney";
    // 查询订单状态(退货)
    public static final String URL_QUERY_STATE_OF_RETURN = API_HOST + "refund/appOrderRefundState";
    // 退货申请
    public static final String URL_RETURN_OF_GOODS = API_HOST + "refund/appOrderRefund";
    // 退货回邮
    public static final String URL_RETURN_SEND_BACK_CONFIRM = API_HOST + "refund/updateOrderRefundLogistics";
    // 物流公司列表
    public static final String URL_FLOW_LIST = API_HOST + "order/queryLogisticsCompanyList";

    /**
     * 添加代理商前查询用户 - 手机号
     */
    public static final String URL_QUERY_MEMBER_BY_MOBILE = API_HOST + "member/getMemberBySetProxy";
    /**
     * 添加代理商前查询用户 - 用户编号
     */
    public static final String URL_QUERY_MEMBER_BY_NO = API_HOST + "member/getMemberBySetProxyScan";

    /**
     * 设置代理
     */
    public static final String URL_SET_PROXY = API_HOST + "member/setProxy";

    /**
     * 我的代理列表
     */
    public static final String URL_MY_PROXY_LIST = API_HOST + "member/getMyProxyMember";

    /**
     * 修改代理分润
     */
    public static final String URL_UPDATE_PROXY = API_HOST + "member/updateProxy";

    /**
     * 删除代理
     */
    public static final String URL_DEL_PROXY = API_HOST + "member/delProxy";

    /**
     * 个人提现申请
     */
//    public static final String URL_ADD_WITHDRAW_APPLY = API_HOST + "withdrawApply/addWithdrawApply_v2_0_4";
//    public static final String URL_ADD_WITHDRAW_APPLY = API_HOST + "withdrawApply/addWithdrawApplyWxOrAli";
    public static final String URL_ADD_WITHDRAW_APPLY = API_HOST + "withdrawApply/addWithdrawApplyBalance";

    /**
     * 商家提现
     */
    public static final String URL_SHOP_ADD_WITHDRAW_APPLY = API_HOST + "withdrawApply/addWithdrawApplyBaodanbi";

    /**
     * 提现提示语
     */
    public static final String URL_WITHDRAW_TIPS = API_HOST + "withdrawApply/balanceWithdrawalTips";

    /**
     * 赚呗提现
     */
    public static final String URL_ADD_MAKE_MONEY_WITHDRAW_APPLY = API_HOST + "withdrawApply/addWithdrawApplyScore";

    /**
     * 添加/修改头像地址
     */
    public static final String URL_UPDATE_AVATAR = API_HOST + "memberInfo/updateMemberHeadImg";

    /**
     * 我的伙伴列表
     */
    public static final String URL_PARTNER_LIST = API_HOST + "member/getMemberShareList_v2_1.do";

    /**
     * 我的伙伴邀请的伙伴
     */
    public static final String URL_INVITE_SHARE = API_HOST + "member/getRecSecondMemberList";

    /**
     * 保存通讯录
     */
    public static final String SAVE_CALL_RECORD = API_HOST + "autdit/saveCallRecord.do";

    /**
     * 邀请用户上传通讯录
     */
    public static final String URL_SHARE_UPLOAD_RECORD = API_HOST + "memberBook/uploadMemberBook";

    /**
     * 借款协议
     */
    public static final String APP_PROTOCOL = API_HOST + "/wap/rent/agreement/protocol";

    /**
     * 消息中心
     */
    public static final String APP_MSG = API_HOST + "message/membermsg";

    /**
     * 使用帮助
     */
    public static final String APP_USE_HELP_URL = HOST + "wap/rent/personal/help";

    /**
     * 版本更新
     */
    public static final String CHECK_VERSION = API_HOST + "main/getVersionInfo";

    /**
     * 添加反馈，举报等
     */
    public static final String ADD_FEEDBACK_URL = API_HOST + "main/addFeedback";

    /**
     * 联系客服
     */
    public static final String CONCAT_CUSTOM_URL = API_HOST + "main/menu";

    // 分享地址
//    public static final String SHARE_URL_HEADER = API_HOST + "member/shareRegister?";
    // public static final String SHARE_URL_HEADER = "http://xjapp.china-dong.com/xjvideo/pages/public/register?";

    public static final String SHARE_URL_HEADER = " http://hdreg.caihvideo.com/reg?";


    /**
     * 省市区
     */
    public static final String GET_LOCATION_INFO = HOST + "/static/province.json";

    //添加充值记录
    public static final String URL_RECHARE_ADD = API_HOST + "recharge/add";
    //取消充值
    public static final String URL_CANCEL_PAY = API_HOST + "recharge/cancel";

    /*--------------------------------- 首页 ---------------------------------*/

    /**
     * 首页数据
     */
    public static final String APP_HOST_URL = API_HOST + "product/homeRecCategoryList";

    /**
     * VIP数据
     */
    public static final String VIP_HOST_URL = API_HOST + "product/homeVipCategoryList";

    /**
     * 保存定位信息
     */
    public static final String SAVE_LOCATION = API_HOST + "location/saveMemberLocation";


    /**
     * 获取城市信息接口
     */
    public static final String URL_ADD_CARD_CITY = API_HOST + "bank/queryCity";

    /**
     * 获取银行名称信息接口
     */
    public static final String URL_ADD_CARD_BANK_NAME = API_HOST + "bank/queryBankName";

    /**
     * 获取银行支行名称信息接口
     */
    public static final String URL_ADD_CARD_SUB_BANK_NAME = API_HOST + "bank/querySubBankName";

    /**
     * 首页附近店铺推荐
     */
    public static final String URL_HOME_SHOP_RECOMMEND = API_HOST + "shop/queryHomeNearbyStoreList";

    /*--------------------------------- 商城 ---------------------------------*/
    /**
     * 商品参数
     */
    public static final String URL_PRODUCT_PARAMS = API_HOST + "/product/queryProductDetailBy/";

    /**
     * 热卖分类列表
     */
    public static final String URL_HOT_SALES_CATEGORY_LIST = API_HOST + "product/hotSalesCategoryList";
    /**
     * 商城首页轮播图
     */
    public static final String URL_GET_BANNER_LIST = API_HOST + "main/getBannerList";
    /**
     * 热卖商品
     */
    public static final String URL_GET_HOT_SALES_PRODUCT = API_HOST + "product/getHotSalesProduct";
    /**
     * 商品分类列表
     */
    public static final String URL_PRODUCT_CATEGORIES = API_HOST + "product/productCategories";
    /**
     * 厂促商品分类
     */
    public static final String URL_COMPANY_PRODUCT_CATEGORIES = API_HOST + "product/cuxiaoProductCategories";
    /**
     * 商品列表（根据分类查询商品)
     */
    public static final String URL_PRODUCT_BY_MENU_NO = API_HOST + "product/queryProductByMenuNo";
    /**
     * 搜索结果分页列表(少于20条)
     */
    public static final String URL_LOG_SEARCH = API_HOST + "search/queryLogSearch";
    /**
     * 删除历史搜索记录
     */
    public static final String URL_DEL_HISTORY = API_HOST + "search/clearSearchLog";

    /**
     * 热门搜索
     */
    public static final String URL_HOT_SEARCH = API_HOST + "search/queryHotSearch";
    /**
     * 商品搜索
     */
    public static final String URL_SEARCH_PRODUCT = API_HOST + "product/searchProduct";
    /**
     * 商品评论分页列表
     */
    public static final String URL_PRODUCT_COMMENT = API_HOST + "product/queryProductComment";
    /**
     * 商品详情
     */
    public static final String URL_QUERY_PRODUCT_DETAIL = API_HOST + "product/queryProductDetail";
    /**
     * 添加商品收藏
     */
    public static final String URL_ADD_GOODS_COLLECT = API_HOST + "collect/addProductCollect";
    /**
     * 删除商品收藏
     */
    public static final String URL_DEL_GOODS_COLLECT = API_HOST + "collect/delProductCollect";
    /**
     * 添加购物车-添加商品到购物车
     */
    public static final String URL_ADD_SHOP_CAR = API_HOST + "shopCar/addShopCar";
    /**
     * 删除购物车-从购物车删除商品
     */
    public static final String URL_DELETE_SHOP_CAR = API_HOST + "shopCar/deleteShopCar";
    /**
     * 修改购物车商品数量
     */
    public static final String URL_UPDATE_SHOP_CAR_NUM = API_HOST + "shopCar/updateShopCarNum";
    /**
     * 购物车结算页面商品信息
     */
    public static final String URL_SHOP_CAR_SETTLE = API_HOST + "shopCar/shopCarSettle";
    /**
     * 购物车结算创建订单
     */
    public static final String URL_SHOPCART_CREATE_ORDER = API_HOST + "order/shopCarToPay";
    /**
     * 商品款式规格
     */
    public static final String URL_PRODUCT_SET_BY_PRODUCT_NO = API_HOST + "product/queryProductSetByProductNo";
    /**
     * 购物车列表
     */
    public static final String URL_MY_SHOP_CAR = API_HOST + "shopCar/queryMyShopCarInfo";
    /**
     * 收获地址
     */
    public static final String URL_ADDRESS_ALL = API_HOST + "address/getMemberAddress";
    /**
     * 立即购买
     */
    public static final String URL_PAY_NOW = API_HOST + "order/toPay";
    /**
     * 立即购买（确认支付生成订单）
     */
    public static final String URL_SURE_TOPAY = API_HOST + "order/sureToPay";

    /**
     * 订单支付-余额支付
     */
    public static final String URL_ORDER_PAY_BALANCE = API_HOST + "order/yuePay";
    /**
     * 微信支付参数
     */
    public static final String URL_WECHAT_PAY_PARAMS = API_HOST + "order/weixinParam";
    /**
     * 获取订单支付宝参数
     */
    public static final String URL_ORDER_ALIPAY = API_HOST + "order/zhifubaoParam";
    /**
     * 获取VIP订单支付宝参数
     */
    public static final String URL_ORDER_VIP_ALIPAY = API_HOST + "vipprodorder/zhifubaoParam";
    /**
     * 获取充值支付宝参数
     */
    public static final String URL_RECHARE_ALIPAY = API_HOST + "recharge/zhifubaoParam";
    /**
     * 获取充值微信参数
     */
    public static final String URL_RECHARE_WXPARAM = API_HOST + "recharge/weixinParam";
    /**
     * 商城订单分页列表
     */
    public static final String URL_MALL_ORDER = API_HOST + "order/queryMallOrderList";
    /**
     * 我的订单-商城订单-取消订单
     */
    public static final String URL_CANCEL_MALL_ORDER = API_HOST + "order/cancelMallOrder";
    /**
     * 我的订单-商城订单-确认收货
     */
    public static final String URL_CONFIRM_MALL_ORDER = API_HOST + "order/confirmMallOrder";
    /**
     * 修改地址
     */
    public static final String URL_ADDRESS_SAVE = API_HOST + "address/updateMemberAddress";
    /**
     * 删除地址
     */
    public static final String URL_ADDRESS_REMOVE = API_HOST + "address/deleteMemberAddress";
    /**
     * 添加地址
     */
    public static final String URL_ADDRESS_ADD = API_HOST + "address/addMemberAddress";

    /**
     * 商城订单评价
     */
    public static final String URL_CREATE_MALL_COMMENTS = API_HOST + "comment/createMallCommentNew";

    /**
     * 店铺订单评价
     */
    public static final String URL_CREATE_SHOP_COMMENTS = API_HOST + "comment/createShopCommentNew";

    /**
     * 收藏商品列表
     */
    public static final String URL_PRODUCT_COLLECT = API_HOST + "collect/queryProductCollect";

    /**
     * 收藏商家列表
     */
    public static final String URL_SHOP_COLLECT = API_HOST + "collect/queryShopCollect";
    /*--------------------------------- 线下 ---------------------------------*/
    /**
     * 快递物流下单
     */
    public static final String URL_CREATE_EXPRESS_ORDER = API_HOST + "order/addshopDoorOrder";
    /**
     * 线下附近店铺
     */
    public static final String URL_OFFLINE_SHOP = API_HOST + "shop/queryNearbyShopList_v2_1_0";
//    public static final String URL_OFFLINE_SHOP = "http://lxmm.fg321.cn/lxmm_test/api/shop/queryNearbyShopList_v2_1_0";

    /**
     * 线下Banner
     */
    public static final String URL_OFFLINE_BANNER = API_HOST + "main/getBannerList";

    // 店铺入驻审核未通过店铺信息详情
    public static final String URL_SHOP_AUDIT_APPLY_DETAIL = API_HOST + "ShopAudit/queryShopAuditApplyfail";
    // 店铺信息
//    public static final String URL_SHOP_INFO = API_HOST + "shop/shopHomePage";
    public static final String URL_SHOP_INFO = API_HOST + "shop/detail";
    // 申请开店
    public static final String URL_APPLY_SHOP = API_HOST + "shop/applyShop";
    // 店铺入驻重新提交
    public static final String URL_ANEW_APPLY_SHOP = API_HOST + "shop/applyShopAudit";
    // 店铺资料修改
    public static final String URL_UPDATE_SHOP = API_HOST + "shop/updateShop";
    // 店铺详情
    public static final String URL_SHOP_DETAIL = API_HOST + "shop/shopDetail";
    //我的店铺-店铺订单分页列表
    public static final String URL_MY_SHOP_ORDER_INFO = API_HOST + "orderDetail/orderDetil";
    // 支付宝服务费
    public static final String GET_REWARD_ALIPAY_PARAMS = API_HOST + "reward/zhifubaoParam";
    // 微信打服务费
    public static final String GET_REWARD_WECHAT_PARAMS = API_HOST + "reward/weixinParam";
    // 余额支付
    public static final String GET_REWARD_YUE_PARAMS = API_HOST + "reward/yuePay";
    // 查询服务费
    public static final String URL_QUERY_SERVER_MONEY = API_HOST + "reward/getServiceCharge";
    // 添加服务费
    public static final String URL_MAKE_SERVER_MONEY = API_HOST + "reward/makeServiceCharge";
    // 店铺账户详情
    public static final String URL_SHOP_ACCOUNT_DETAIL = API_HOST + "account/queryShopAccount";
    // 店铺未入账记录
    public static final String URL_ORDER_SETTLE = API_HOST + "orderSettle/queryOrderSettle";
    //店铺商品分类统计列表(我的店铺)
    public static final String URL_SHOP_COMMODITY_STAT = API_HOST + "product/productCategoryList";
    //店铺商品分类删除(我的店铺)
    public static final String URL_SHOP_COMMODITY_CLASSIFY_DELECT = API_HOST + "product/deleteProductCategory";
    //店铺商品分类添加(我的店铺)
    public static final String URL_SHOP_COMMODITY_CLASSIFY_ADD = API_HOST + "product/addProductCategory";
    //店铺商品分类编辑(我的店铺)
    public static final String URL_SHOP_COMMODITY_CLASSIFY_EDIT = API_HOST + "product/updateProductCategory";
    //销售中和仓库中商品分页列表(我的店铺)
    public static final String URL_QUERY_PRODUCT_LIST = API_HOST + "product/queryProductList";
    //审核中和已驳回商品分页列表(我的店铺)
    public static final String URL_QUERY_PRODUCT_AUDIT_LIST = API_HOST + "product/queryProductAuditList";
    //商品删除-审核中，已驳回
    public static final String URL_GOODS_DEL = API_HOST + "productAudit/delProductAuditCategory";
    //商品删除-仓库中
    public static final String URL_GOODS_DEL2 = API_HOST + "product/delProductCategory";
    //商品上下架
    public static final String URL_GOODS_PUTAWAY_SOLDOUT = API_HOST + "product/updateProductIsOnsale";
    // 修改商品价格
    public static final String URL_UPDATE_PRODUCT_PRICE = API_HOST + "product/updateProductPrice";
    // 修改商品库存
    public static final String URL_UPDATE_PRODUCT_STORAGE = API_HOST + "product/updateProductStorage";
    //添加商品
    public static final String URL_GOODS_ADD = API_HOST + "product/addProductAudit";
    //    public static final String URL_GOODS_ADD = API_HOST + "product/addProduct";
    // 驳回商品重新编辑
    public static final String URL_GOODS_REJECT_EDIT = API_HOST + "productAudit/reapplyProductAudit";
    //编辑商品
    public static final String URL_GOODS_EDIT = API_HOST + "product/updateProductAudit";
    //    public static final String URL_GOODS_EDIT = API_HOST + "product/updateProduct";
    //商铺确认延保订单（完善延保订单）
    public static final String URL_CONFIRM_INSURANCE_ORDER = API_HOST + "order/insuranceOrder";
    //我的订单-店铺订单-取消订单
    public static final String URL_CANCEL_SHOP_ORDER = API_HOST + "order/cancelShopOrder";
    // 交易报表
    public static final String URL_ORDER_REPORT = API_HOST + "orderDetail/queryOrderReport";
    //交易统计
    public static final String URL_TRDE_STATISTIC = API_HOST + "orderDetail/queryOrderDetailAndTotal";
    //订单详情
    public static final String URL_ORDER_DETAIL = API_HOST + "orderDetail/queryOrderDetail";

    //店铺商品(附近店铺)
    public static final String URL_NEARBY_SHOP_GOODS = API_HOST + "shop/queryProductList";
    //附近店铺商品分类列表
    public static final String URL_NEARBY_SHOP_CLASSIFY = API_HOST + "shop/productCategoryList";
    //店铺评论
    public static final String URL_SHOP_COMMENT = API_HOST + "shop/shopComment";
    //添加收藏店铺
    public static final String URL_ADD_SHOP_COLLECT = API_HOST + "collect/addShopCollect";
    //删除收藏店铺
    public static final String URL_DEL_SHOP_COLLECT = API_HOST + "collect/delShopCollect";
    //订单结算
    public static final String URL_ORDER_LIST_SETTLEMENT = API_HOST + "order/shopSettleList";
    //创建店铺订单
//    public static final String URL_CREATE_SHOP_ORDER = API_HOST + "order/shopSettleProducts";
    public static final String URL_CREATE_SHOP_ORDER = API_HOST + "order/shopSettleProductsNew";
    // 厂促立即购买
//    public static final String URL_COMPANY_SHOP_ORDER = API_HOST + "order/cuxiaoSettleProducts";
    public static final String URL_COMPANY_SHOP_ORDER = API_HOST + "order/cuxiaoBusSettleProducts";
    // 厂促体验任务购买
    public static final String URL_COMPANY_SHOP_EXP_TASK_ORDER = API_HOST + "order/taskSettleProducts";
    //创建附件订单
    public static final String URL_CREATE_NEARBY_ORDER = API_HOST + "order/shopSettleOrder";
    //店铺订单分页列表（状态分类查询）
    public static final String URL_QUERY_ORDER_LIST = API_HOST + "order/queryShopOrderList";

    /**
     * 订单数量
     */
    public static final String URL_ORDER_COUNT = API_HOST + "order/orderCategoryStatist_V_2.1";

    /**
     * 物流公司
     */
    public static final String URL_LOGISTICS_TYPE = API_HOST + "order/queryLogisticsCompanyList";

    /**
     * 商家发货
     */
    public static final String URL_SHOP_SEND_GOODS = API_HOST + "order/deliverGoodsOrder";

    /**
     * 添加商品到店铺
     */
    public static final String URL_ADD_PRODUCT_2_SALE = API_HOST + "product/addProductFromPlat";

    /**
     * 店铺活动消息
     */
    public static final String URL_SALE_SHOP_TIP = API_HOST + "main/getcuxiaoIntro";

    /**
     * 店铺收益教程
     */
    public static final String URL_SHOP_HELP = API_HOST + "member/storeRules";

    /**
     * 店铺订单-确认收货
     */
    public static final String URL_SHOP_ORDER_RECEIPE = API_HOST + "order/confirmMallOrder";

    /**
     * 店铺订单催单
     */
    public static final String URL_REMINDER_ORDER = API_HOST + "order/reminderOrder";

    /**
     * 促销商品申请退款提示语
     */
    public static final String URL_SALE_REFUND_GOODS_TIP = API_HOST + "order/cancelCXOrderDeliverTips";

    /**
     * 促销商品申请退款
     */
    public static final String URL_SALE_REFUND_GOODS = API_HOST + "order/cancelCXOrderDeliver";

    /**
     * 代金券(未使用/已使用)
     */
    public static final String URL_MY_COUPON_LIST = API_HOST + "coupon/query";

    /**
     * 获取获赠人信息
     */
    public static final String URL_COUPON_GET_USER_INFO = API_HOST + "coupon/receiver";

    /**
     * 赠送代金券
     */
    public static final String URL_GIVE_COUPON_TO_MEMBER = API_HOST + "coupon/transfer";

    /**
     * 代金券转余额
     */
    public static final String URL_COUPON_2_MONEY = API_HOST + "coupon/withdrawal";

    /*--------------------------------- 任务 ---------------------------------*/
    /**
     * 获取任务进度
     */
    public static final String URL_GET_MEMBER_TASK = API_HOST + "membertask/current";

    /**
     * 买二配三任务
     */
    public static final String URL_GET_MEMBER_TASK_NEW = API_HOST + "membertask/currentNew";

    /**
     * 任务详情
     */
    public static final String URL_TASK_DETAIL = API_HOST + "membertask/details";
    /**
     * 富豪任务详情
     */
    public static final String URL_TASK_DETAIL_NEW = API_HOST + "membertask/detailsNew";

    /**
     * 开启任务
     */
    public static final String URL_TASK_UPGRADE = API_HOST + "membertask/upgrade";
    /**
     * 富豪任务
     */
    public static final String URL_TASK_UPGRADE_NEW = API_HOST + "membertask/upgradeNew";

    /**
     * 获取体验任务进度
     */
    public static final String URL_GET_EXP_TASK_PROGRESS = API_HOST + "membertask/expProgress";

    /**
     * 体验任务流水
     */
    public static final String URL_EXP_TASK_LOGS = API_HOST + "membertask/queryMemberTaskExp";

    /**
     * 当前活跃度排行
     */
    public static final String URL_NOW_ACTIVE_RANKING = API_HOST + "member/getMemberActiveRanking";

    /**
     * 活跃度排行榜
     */
    public static final String URL_ACTIVE_RANKING_LIST = API_HOST + "member/getMemberActiveRankingList";

    /**
     * 获取当前系统匹配订单数
     */
    public static final String URL_GET_SYSTEM_MATCH_ORDER_LIST = API_HOST + "membertask/getSystemMatchOrderList";

    /**
     * 点击每日下载任务
     */
    public static final String URL_TASK_CLICK_DOWNLOAD_ADS = API_HOST + "membertask/clickAdMemberTask";

    /**
     * 浏览商品增加活跃度
     */
    public static final String URL_BROWSE_PRODUCT = API_HOST + "member/setMemberActiveByBrowseGoods";

    /**
     * 分享成功增加活跃度
     */
    public static final String URL_SHARE_ADD_ACTIVITE = API_HOST + "membertask/clickCPSADMemberTask";

    /**
     * 合伙人派单查询
     */
    public static final String URL_AGENT_QUERY_SEND_ORDER = API_HOST + "member/hangUpOrderListByAgent";

    /**
     * 合伙人派单
     */
    public static final String URL_AGENT_SEND_ORDER = API_HOST + "member/hangUpOrderDistribute";

    /*--------------------------------- 动态 ---------------------------------*/

    // 亲子圈列表
    public static final String CIRCLE_LIST_URL = API_HOST + "shuoshuo/childCircleList";
    // 我的亲子圈列表
    public static final String MY_CIRCLE_LIST_URL = API_HOST + "shuoshuo/myChildCircleList";
    // 亲子圈点赞
    public static final String CIRCLE_START_URL = API_HOST + "shuoshuo/getGiveNum";
    // 亲子圈详情
    public static final String CIRCLE_DETAIL_URL = API_HOST + "shuoshuo/getChildCirclet";
    // 亲子圈评论列表
    public static final String PARENT_CHILD_DISCUSS_LIST_URL = API_HOST + "shuoshuo/queryCommentList";
    // 亲子圈添加评论
    public static final String PARENT_CHILD_ADD_DISCUSS_URL = API_HOST + "shuoshuo/addComment";
    // 发布亲子圈
    public static final String PARENT_CHILD_RELEASE_URL = API_HOST + "shuoshuo/addChildCirclet";
    // 删除自己发布的
    public static final String PARENT_CHILD_DEL_CIRCLE_URL = API_HOST + "shuoshuo/delChildCirclet";
    // 亲子知识/促销王头条
    public static final String ARTICLE_TOP_URL = API_HOST + "article/childKnowledgeList";
    // 头条详情
    public static final String ARTICLE_TOP_DETAIL_URL = API_HOST + "article/getChildKnowledge?articleNo=";

    /*--------------------------------- 代理相关 ---------------------------------*/

    /**
     * 代理申请
     */
//    public static final String URL_APPLY_PROXY = API_HOST + "agent/applyAgent";
    public static final String URL_APPLY_PROXY = API_HOST + "member/agentAlliance?memberNo=";

    /**
     * 扫码获红包
     */
    public static final String URL_SCAN_GETREDPACKET = API_HOST + "member/scanRedPacket?memberNo=";

    /**
     * 查询我邀请的店主和代理
     */
    public static final String URL_QUERY_SHARE_PROXY = API_HOST + "agent/getAgentMemberList";

    /**
     * 代理结算记录
     */
    public static final String URL_PROXY_HORSTORY = API_HOST + "agent/queryAgentWithdrawal";


    /*--------------------------------- v2.0 ---------------------------------*/
    /**
     * 用户注册
     */
    public static final String URL_APP_REGISTER = API_HOST + "member/toAppRegister?memberSrc=ANDROID";
    /**
     * 修改密码
     */
    public static final String URL_UPDATE_PASSWORD = API_HOST + "member/updateMemberInfoPWD";

    /**
     * 设置支付密码
     */
    public static final String URL_SET_PAY_PWD = API_HOST + "member/setPayCode";
    /**
     * 修改支付密码
     */
    public static final String URL_UPDATE_PAY_PWD = API_HOST + "member/updatePayCode";

    /**
     * 校验支付密码
     */
    public static final String URL_CHECK_PAY_PWD = API_HOST + "member/checkPayCode";

    /**
     * 忘记密码发送邮箱验证码
     */
    public static final String URL_FORGET_PWD_EMAIL_GET_CODE = API_HOST + "member/sendEmaill";

    /**
     * 忘记密码发送手机验证码
     */
    public static final String URL_FORGET_PWD_MOBILE_GET_CODE = API_HOST + "member/sendSMS";

    /**
     * 忘记密码验证验证码
     */
    public static final String URL_FORGET_PWD_CHECK_CODE = API_HOST + "member/verifyEmailOrMobile";

    /**
     * 忘记密码，重设密码
     */
    public static final String URL_FORGET_PWD_RESET = API_HOST + "member/updateMemberPWD";

    /**
     * 修改邮箱地址
     */
    public static final String URL_UPDATE_EMAIL = API_HOST + "member/updateMemberInfoEmail";

    /**
     * 个人中心  任务数/代金券/余额/店铺余额
     */
//    public static final String URL_ACCOUNT_INFO = API_HOST + "member/memberStatistics";
    public static final String URL_ACCOUNT_INFO = API_HOST + "account/queryAccountDetail";

    /**
     * 个人中心  个人任务
     */
    public static final String URL_MINE_TASK = API_HOST + "commonTask/myTaskList";

    /**
     * 任务完成
     */
    public static final String URL_TASK_COMPLETE = API_HOST + "commonTask/completeShipinTask";

    /**
     * 店铺余额转入用户余额
     */
    public static final String URL_SHOPMONEY_TO_MINEMONEY = API_HOST + "member/personalTransfer";

    /**
     * 收益账户转用户余额
     */
    public static final String URL_INCONE_TO_MINEMONEY = API_HOST + "member/frPersonalTransfer";

    /**
     * 收益账户详情
     */
    public static final String URL_INCOME_DETAIL = API_HOST + "member/memberProfitStatistics";

    /**
     * 分享商圈
     */
    public static final String URL_SHARE_CIRCLE_DETAIL = API_HOST + "shuoshuo/shareCirclet?circlet=";

    /**
     * 随机获取可分享用户
     */
    public static final String URL_GET_RADIOM_MEMBER = API_HOST + "memberBook/recMemberBook";

    /*--------------------------------- 3.0 ---------------------------------*/
    /**
     * 任务是否显示
     */
    public static final String URL_TASK_3_0_IS_SHOW = API_HOST + "mtMemberTask/getMtConfig";

    /**
     * 我的发财种子
     */
    public static final String URL_MINE_SEED = API_HOST + "mtMemberTask/querySeed";

    /**
     * 激活3.0任务
     */
    public static final String URL_ACTIVATE_3_0_TASK = API_HOST + "mtMemberTask/openMemberTask";

    /**
     * 团队任务价格区间
     */
    public static final String URL_TEAM_TASK_BLOCK = API_HOST + "mtMemberTask/queryTaskBlock";

    /**
     * 团队任务激活专区
     */
    public static final String URL_TEAM_ACTIVATE_BLOCK = API_HOST + "mtMemberTask/openMemberBlock";

    /**
     * 升级任务
     */
    public static final String URL_UPGRADE_TASK = API_HOST + "mtMemberTask/upgrade";

    /**
     * 团队任务专区区块任务
     */
    public static final String URL_TEAM_BLOCK_TASK = API_HOST + "mtMemberTask/current";

    /**
     * 团队任务专区激活商家模块任务
     */
    public static final String URL_TEAM_BLOCK_ACTIVATE_TASK = API_HOST + "mtMemberTask/openMerchantTask";

    /**
     * 团队任务专区激活推荐模块任务
     */
    public static final String URL_TEAM_BLOCK_ACTIVATE_REC_TASK = API_HOST + "mtMemberTask/openMerchantTaskByRec";

    /**
     * 团队任务专区查询推荐任务列表
     */
    public static final String URL_TEAM_BLOCK_QUERY_REC_TASK = API_HOST + "mtMemberTask/recTask";

    /**
     * 团队任务专区激活推荐模块子任务
     */
    public static final String URL_TEAM_BLOCK_ACTIVATE_REC_CHILD_TASK = API_HOST + "mtMemberTask/lightenMerchantTask";

    /**
     * 3.0任务创建订单
     */
    public static final String URL_3_0_TASK_CREATE_ORDER = API_HOST + "order/cuxiaoBus3_0_SettleProducts";

    /**
     * 用户抢单
     */
    public static final String URL_3_0_TASK_GRAB_ORDER = API_HOST + "mtMemberTask/distributeOrder";

    /**
     * 3.0模块排行榜
     */
    public static final String URL_3_0_BLOCK_ACTIVE_RANKING = API_HOST + "mtMemberTask/activeRankingList";

    /**
     * 获取种子市场价
     */
    public static final String URL_GET_SEED_PRICE = API_HOST + "mtMemberTask/seedPrice";

    /**
     * 挂卖种子
     */
    public static final String URL_SELL_SEED = API_HOST + "mtMemberTask/upperSeed";

    /**
     * 收回种子
     */
    public static final String URL_RECYCLE_SEED = API_HOST + "mtMemberTask/lowerSeed";

    /**
     * 在售种子
     */
    public static final String URL_SEED_SHOP = API_HOST + "mtMemberTask/queryTradeSeed";

    /**
     * 抢购种子
     */
    public static final String URL_PAY_SEED = API_HOST + "reward/makeBuySeedByMember";

    /**
     * 种子记录
     */
    public static final String URL_SEED_LOGS = API_HOST + "mtMemberTask/queryTradeSeedLog";

    /**
     * 发财树种子
     */
    public static final String URL_GET_ALL_SEED = API_HOST + "mtMemberTask/treeSeed";

    /**
     * 种子加速时间
     */
    public static final String URL_SEED_SPEED_TIME = API_HOST + "mtMemberTask/querySeedSpeedUpTime";

    /**
     * 抢发财种子
     */
    public static final String URL_GRAB_SEED = API_HOST + "mtMemberTask/grabSeed";

    /**
     * 当前抢到哪颗种子
     */
    public static final String URL_GRAB_CURRENT_SEED = API_HOST + "mtMemberTask/newestSeedData";

    /**
     * 种子领取公告
     */
    public static final String URL_SEED_ADS = API_HOST + "mtMemberTask/seedNotice";

    /**
     * 3.0任务订单匹配广告
     */
    public static final String URL_3_0_ORDER_MATCH = API_HOST + "mtMemberTask/getSystemMatchOrderList";

    /**
     * 种子趋势
     */
    public static final String URL_SEED_PRICE_CHART = API_HOST + "mtMemberTask/seedPriceTrendChart";


    /*--------------------------------- 活跃果 ---------------------------------*/
    /**
     * 活跃果市场价
     */
    public static final String URL_GET_FRUIT_PRICE = API_HOST + "mtMemberTask/fruitPrice";

    /**
     * 收回活跃果
     */
    public static final String URL_RECYCLE_FRUIT = API_HOST + "mtMemberTask/lowerFruit";

    /**
     * 在售活跃果
     */
    public static final String URL_FRUIT_SHOP = API_HOST + "mtMemberTask/queryTradeFruit";

    /**
     * 挂卖活跃果
     */
    public static final String URL_SELL_FRUIT = API_HOST + "mtMemberTask/upperFruit";

    /**
     * 抢购活跃果
     */
    public static final String URL_PAY_FRUIT = API_HOST + "reward/makeBuyFruitByMember";

    /**
     * 种子记录
     */
    public static final String URL_FRUIT_LOGS = API_HOST + "mtMemberTask/queryTradeFruitLog";

    /*--------------------------------- 红包 ---------------------------------*/
    /**
     * 获取红包样式
     */
    public static final String URL_GET_RED_PACKER_STYLE = API_HOST + "redPackShare/styleList";

    /**
     * 红包预览 ?logo=xx&title=xx&content=xx
     */
    public static final String URL_PREVIEW_RED_PACKER = API_HOST + "redPackShare/preview1";

    /**
     * 红包预览
     */
    public static final String ULR_PREVIEW_RED_PACKER_2 = API_HOST + "redPackShare/preview";

    /**
     * 分享红包链接
     */
    public static final String URL_SHARE_RED_PACKER = API_HOST + "redPackShare/receive/";

    /**
     * 发红包
     */
    public static final String URL_CREATE_RED_PACKER = API_HOST + "redPackShare/createSharePage";

    /**
     * 塞钱至红包
     */
    public static final String URL_RED_PACKER_ADD_MONEY = API_HOST + "reward/makeRedPackShare";

    /**
     * 红包列表
     */
    public static final String URL_GET_ALL_RED_PACKER = API_HOST + "redPackShare/querySharePageList";

    /**
     * 红包领取情况
     */
    public static final String URL_GET_RED_PACKER_INFO = API_HOST + "redPackShare/queryReceiveLogs";

    /**
     * 红包墙数据
     */
    public static final String URL_GET_RED_PACKER_WALL = API_HOST + "redPackShare/wall";

    /**
     * 删除红包
     */
    public static final String URL_DEL_RED_PACKER = API_HOST + "redPackShare/delSharePage";

    /**
     * 编辑红包
     */
    public static final String URL_EDIT_RED_PACKER = API_HOST + "redPackShare/editSharePage";

    /**
     * 红包墙红包详情
     */
    public static final String URL_RED_PACKER_WALL_DETAIL = API_HOST + "redPackShare/querySharePage";

    /**
     * 领取红包
     */
    public static final String URL_GET_RED_PACKER = API_HOST + "redPackShare/receiveRedPack";


    /**
     * 人脸识别 获取bizcode
     */
    public static final String URL_FACE_GET_BIZCODE = API_HOST + "alicloudauth/faceInit";

//    /**
//     * 人脸识别 获取认证url
//     */
//    public static final String URL_FACE_GET_URL = API_HOST + "alicloudauth/faceVerify2";

    /**
     * 人脸识别 验证实名
     */
    public static final String URL_FACE_CHECK_AUTH = API_HOST + "alicloudauth/faceResult";

}
