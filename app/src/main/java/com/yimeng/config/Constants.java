package com.yimeng.config;

import android.Manifest;

/**
 * Author : huiGer
 * Time   : 2018/8/30 0030 下午 02:37.
 * Desc   :
 */
public class Constants {

    /**
     * 所需权限
     */
    public static final String[] APP_PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_NETWORK_STATE};

    public static final String WX_APPID = "wx69cc28a101ca5619";
    public static final String WX_SECRET = "7845e56405fcee20d3ca95022da06d59";
    public static final String PAYMENT_TYPE_ALIPAY = "alipay";
    public static final String PAYMENT_TYPE_WECHAT = "wechat";
    public static final String PAYMENT_TYPE_YUE = "yue";

    /**
     * app is test
     */
    public static final String APP_IS_TEST = "app_is_test";

    /**
     * is first
     */
    public static final String APP_IS_FIRST = "is_first";

    /**
     * IMEI
     */
    public static final String MOBILE_ID = "mobile_id";

    /**
     * user_info
     */
    public static final String USER_INFO = "user_info";

    /**
     * user head path
     */
    public static final String USER_HEAD_PATH = "user_head_path";

    /**
     * token
     */
    public static final String USER_TOKEN = "user_token";

    /**
     * login bind phone
     */
    public static final String USER_MOBILE = "user_phone";

    /**
     * user auth status
     */
    public static final String USER_AUTH_STATUS = "user_auth_status";

    /**
     * is_update
     */
    public static final String IS_UPDATE = "is_update";

    /**
     * is_login
     */
    public static final String IS_LOGIN = "is_login";

    /**
     * max limit
     */
    public static final int MAX_LIMIT = 10;

    /**
     * RESULT_CODE
     */
    public static final int RESULT_CODE_COMMON_DATA = 91;

    /**
     * BroadcastReceiver
     */
    public static final String ACTION_PAYMENT_SUCCESS = "action_payment_success";
    public static final String ACTION_PAYMENT_FAIL = "action_payment_fail";
    public static final String ACTION_TO_FRAGMENT = "action_to_fragment";
    public static final String ACTION_TOKEN_FAIL = "action_token_fail";
    public static final String ACTION_REFRESH_MEMBER_INFO = "action_refresh_member_info";

    /**
     * home data
     */
    public static final String HOME_DATA_CACHE = "home_data_cache";

    /**
     * Classify cache
     */
    public static final String CLASSIFY_DATA_CACHE = "classify_cache";

    /**
     * offline cache
     */
    public static final String OFFLINE_DATA_CACHE = "offline_cache";

    /**
     * 经度
     */
    public static final String APP_LOCATION_LONGITUDE = "app_location_longitude";

    /**
     * 纬度
     */
    public static final String APP_LOCATION_LATITUDE = "app_location_latitude";

    /**
     * default Longitude
     */
    public static final String APP_DEFAULT_LONGITUDE = "114.039605";

    /**
     * default Latitude
     */
    public static final String APP_DEFAULT_LATITUDE = "22.66923";

    /**
     * province
     */
    public static final String APP_LOCATION_PROVINCE = "app_location_province";
    /**
     * city
     */
    public static final String APP_LOCATION_CITY = "app_location_city";
    /**
     * area
     */
    public static final String APP_LOCATION_AREA = "app_location_area";
    /**
     * 下载id
     */
    public static final String APP_DOWNLOAD_ID = "app_download_id";

    /**
     * sjmm location info
     */
    public static final String APP_SJMM_LOCATION_INFO = "app_sjmm_location_info";

    /**
     * my shop type
     */
    public static final String MINE_SHOP_TYPE = "mine_shop_type";

    /**
     * 当前账户店铺申请中
     */
    public static final String MINE_SHOP_APPLY_LOADING = "mine_shop_apply_loading";

    /**
     * 任务页面是否显示弹窗
     */
    public static final String TASK_DIALOG_STATUS = "task_dialog_status";

    /**
     * 旧任务是否显示
     */
    public static final String TASK_OLD_IS_SHOW = "task_old_is_show";

    /**
     * 3.0任务是否激活
     */
    public static final String TASK_NEW_IS_ACTIVATE = "task_new_is_activate";

    /**
     * 3.0任务激活所需种子
     */
    public static final String TASK_NEW_IS_ACTIVATE_SEED = "task_new_is_activate_seed";

    /**
     * 最后保存种子数据时间
     */
    public static final String LAST_SAVE_SEED_INFO_TIME = "last_save_seed_info_time";

    /**
     * 推广红包数据缓存
     */
    public static final String CACHE_RED_PACKER_LOGO = "cache_red_packer_logo";
    public static final String CACHE_RED_PACKER_TITLE = "cache_red_packer_title";
    public static final String CACHE_RED_PACKER_CONTENT = "cache_red_packer_content";


    /**
     * 设置金额RESULT
     */
    public static final int REQUEST_SETAMOUNT = 0x1000;


    public static final String INTENT_PARAM_MONEY = "money";

    public static final String INTENT_PARAM_SHOPNO = "shopNo";

    /**
     * 游戏频道号
     */
    public static final String APP_GAME_CHANNEL = "14802";

    /**
     * 游戏KEY
     */
    public static final String APP_GAME_KEY = "c039eb398b39499db45ff32cf22f5152";

}
