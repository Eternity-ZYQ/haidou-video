package com.yimeng.retorfit

import com.yimeng.config.XJConfig
import com.yimeng.entity.*
import com.yimeng.utils.CommonUtils
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2020-12-05 18:07.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 接口
 * </pre>
 */
interface ApiService {


    /** 获取视频 */
    @POST("${XJConfig.URL_TYPE}api/video/query")
    @FormUrlEncoded
    @Headers("wmsyscode:barbecue24h")
    fun getVideo(
            @Field("start") start: Int,
            @Field("limit") limit: Int,
            @Field("token") token: String = CommonUtils.getToken(),
            @Field("sortType") sortType: String = "random"
    ): Call<ResponseListData<VideoBean>>

    /** 获取评论 */
    @FormUrlEncoded
    @POST("${XJConfig.URL_TYPE}api/video/queryComments")
    fun getVideoComments(
            @Field("videoNo") videoNo: String,
            @Field("start") start: Int,
            @Field("limit") limit: Int,
            @Field("token") token: String = CommonUtils.getToken()
    ): Call<ResponseListData<VideoEvalBean>>

    /** 视频点赞 */
    @FormUrlEncoded
    @POST("${XJConfig.URL_TYPE}api/video/collect")
    fun videoCollect(
            @Field("videoNo") videoNo: String,
            @Field("token") token: String = CommonUtils.getToken()
    ): Call<ResponseBase>

    /** 视频评论 */
    @FormUrlEncoded
    @POST("${XJConfig.URL_TYPE}api/video/comment")
    fun videoCommect(
            @Field("videoNo") videoNo: String,
            @Field("comment") comment: String,
            @Field("token") token: String = CommonUtils.getToken()
    ): Call<ResponseBase>

    /** 视频分享 */
    @FormUrlEncoded
    @POST("${XJConfig.URL_TYPE}api/video/share")
    fun videoShare(
            @Field("videoNo") videoNo: String,
            @Field("token") token: String = CommonUtils.getToken()
    ): Call<ResponseBase>

    /*--------------------------------- 用户 ---------------------------------*/

    /** 注册发送验证码 */
    @FormUrlEncoded
    @POST("${XJConfig.URL_TYPE}api/member/msgSend")
    fun getCode(@Field("mobileNo") mobileNo: String): Call<JsonObject>

    /** 用户注册 */
    @FormUrlEncoded
    @POST("${XJConfig.URL_TYPE}api/member/appRegister")
    fun register(@FieldMap params: MutableMap<String, String>): Call<ResponseData<Member>>

    /** 我的粉丝 */
    @FormUrlEncoded
    @POST("${XJConfig.URL_TYPE}api/member/getRecSecondMemberList")
    fun getMyFans(
            @Field("memberNo")memberNo:String,
            @Field("start") start: Int,
            @Field("limit") limit: Int = XJConfig.LIMIT,
            @Field("token") token: String = CommonUtils.getToken()
    ):Call<JsonObject>

    /** 我的活跃用户 */
    @FormUrlEncoded
    @POST("${XJConfig.URL_TYPE}api/member/queryMemberActives")
    fun getMyActive(
            @Field("memberNo")memberNo:String,
            @Field("start") start: Int,
            @Field("limit") limit: Int = XJConfig.LIMIT,
            @Field("token") token: String = CommonUtils.getToken()
    ):Call<JsonObject>

    /** 资金流水 */
    @FormUrlEncoded
    @POST("${XJConfig.URL_TYPE}api/accountWater/queryAccountWater")
    fun getLogs(
            @Field("amtType")amtType:String,
            @Field("start") start: Int,
            @Field("limit") limit: Int = XJConfig.LIMIT,
            @Field("token") token: String = CommonUtils.getToken()
    ) :Call<ResponseListData<LogBean>>

    /** 置换彩蛋 */
    @FormUrlEncoded
    @POST("${XJConfig.URL_TYPE}api/account/exchangeAccountsScore")
    fun changeCaiJin(
            @Field("token") token: String = CommonUtils.getToken(),
            @Field("amt") amt: Long
    ): Call<ResponseBase>


    /*--------------------------------- 金蛋 ---------------------------------*/

    /** 我的金蛋列表 */
    @FormUrlEncoded
    @POST("${XJConfig.URL_TYPE}api/feederMember/queryMyMachineList")
    fun getMyReels(
            @Field("start") start: Int,
            @Field("limit") limit: Int = XJConfig.LIMIT,
            @Field("token") token: String = CommonUtils.getToken()
    ): Call<ResponseListData<ReelBean>>

    /** 金蛋列表 */
    @FormUrlEncoded
    @POST("${XJConfig.URL_TYPE}api/feeder/queryFeederList")
    fun getReels(
            @Field("start") start: Int,
            @Field("limit") limit: Int = XJConfig.LIMIT,
            @Field("token") token: String = CommonUtils.getToken()
    ): Call<ResponseListData<ReelBean>>

    /** 兑换金蛋 */
    @FormUrlEncoded
    @POST("${XJConfig.URL_TYPE}api/feederMember/exchangeKuangji")
    fun changeReels(
            @Field("feederNo") feederNo: String,
            @Field("token") token: String = CommonUtils.getToken()
    ): Call<ResponseBase>



}