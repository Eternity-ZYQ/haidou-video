package com.yimeng.net.lxmm_net;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.yimeng.config.ConstantHandler;
import com.yimeng.config.Constants;
import com.yimeng.haidou.App;
import com.yimeng.entity.BusinessModel;
import com.yimeng.entity.FlowListModel;
import com.yimeng.entity.GoodsModel;
import com.yimeng.entity.Member;
import com.yimeng.entity.ModelAccountDetail;
import com.yimeng.entity.ModelAddress;
import com.yimeng.entity.ModelBonus;
import com.yimeng.entity.ModelComment;
import com.yimeng.entity.ModelGetBannerList;
import com.yimeng.entity.ModelGetHotSalesProduct;
import com.yimeng.entity.ModelGrade;
import com.yimeng.entity.ModelHotSalesCategoryList;
import com.yimeng.entity.ModelHotSearch;
import com.yimeng.entity.ModelMallOrder;
import com.yimeng.entity.ModelMallOrderItem;
import com.yimeng.entity.ModelMemberBankcard;
import com.yimeng.entity.ModelOrderDetail;
import com.yimeng.entity.ModelOrderGoods;
import com.yimeng.entity.ModelOrderReport;
import com.yimeng.entity.ModelOrderSettle;
import com.yimeng.entity.ModelPayRecords;
import com.yimeng.entity.ModelProductByMenuNo;
import com.yimeng.entity.ModelProductCategories;
import com.yimeng.entity.ModelProductCategoriesContent;
import com.yimeng.entity.ModelProductDetail;
import com.yimeng.entity.ModelProductParams;
import com.yimeng.entity.ModelProductSetByProductNo;
import com.yimeng.entity.ModelRepairOrderCommentDetail;
import com.yimeng.entity.ModelSearchProduct;
import com.yimeng.entity.ModelSearchResult;
import com.yimeng.entity.ModelSettlement;
import com.yimeng.entity.ModelShop;
import com.yimeng.entity.ModelShop1;
import com.yimeng.entity.ModelShopCarSettle;
import com.yimeng.entity.ModelShopCarSettleItem;
import com.yimeng.entity.ModelShopCart;
import com.yimeng.entity.ModelShopCartGoods;
import com.yimeng.entity.ModelShopDetail;
import com.yimeng.entity.ModelShopOrderList;
import com.yimeng.entity.ModelShopOrderListItem;
import com.yimeng.entity.ModelSimple;
import com.yimeng.entity.ModelSize;
import com.yimeng.entity.ModelTransactionRecords;
import com.yimeng.entity.ModelTrdeStatistic;
import com.yimeng.entity.ModelWXPay;
import com.yimeng.entity.ModelWallet;
import com.yimeng.entity.ModelWithdrawDeposit;
import com.yimeng.entity.ParentChildCircleDetail;
import com.yimeng.entity.ParentChildDiscussBean;
import com.yimeng.entity.ReturnOfGoodsApplyModel;
import com.yimeng.entity.ReturnOfGoodsStatus;
import com.yimeng.entity.SendBackConfirmStatus;
import com.yimeng.entity.ShopDetailClassifyBean;
import com.yimeng.entity.ShopOrderModel;
import com.yimeng.utils.CommonUtils;
import com.yimeng.utils.GsonUtils;
import com.yimeng.utils.JsonUtil;
import com.yimeng.utils.UnitUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huige.library.utils.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by xp on 2016/6/14.
 * ????????????Response??????
 */
public class ResponseParserUtil {

    public static ResponseParserUtil requestUtil;
    private final int TYPE_NORMAL = 0x01;// ????????????
    private final int TYPE_SID_INVALID = 0x02;// sid????????????
    long oldTime = 0;

    public static ResponseParserUtil getInstance() {
        if (requestUtil == null) {
            requestUtil = new ResponseParserUtil();
        }

        return requestUtil;
    }

    public Message getDefaultFailMessage(Message msg, int fail) {
        msg.what = fail;
        msg.arg1 = 1112;
        msg.obj = "????????????";
        return msg;
    }

    /**
     * ??????????????????
     */
    void parserErrorData(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            msg = getFailMessage(msg, object, fail);
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ?????????????????? ??????????????????
     */
    void parserCommonGetString(String response, Handler mHandler, int success, int fail) {

        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                msg.what = success;
                msg.obj = JsonUtil.getString(object, "msg");
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }

        mHandler.sendMessage(msg);
    }

    private Message getFailMessage(Message msg, JSONObject object, int fail) {
        msg.what = fail;
        msg.arg1 = 1111;
        msg.obj = "error";
        try {
            msg.arg1 = object.getInt("status");
            msg.obj = object.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public Message getExceptionMessage(Message msg, int fail) {
        msg.what = fail;
        msg.arg1 = ConstantHandler.WHAT_EXCEPTION_ERROR;
        msg.obj = "??????????????????????????????????????????????????????";
        return msg;
    }

    void parserCheckResult(String response, Handler mHandler, int success, int fail) {
        if (!CommonUtils.checkLogin()) {
            return;
        }
        JSONObject object;
        try {
            object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_SID_INVALID)) {
                if (success == ConstantHandler.WHAT_REFRESH_TOKEN_SUCCESS) {
                    Intent intent = new Intent();
                    intent.setAction(Constants.ACTION_TOKEN_FAIL);
                    App.getContext().sendBroadcast(intent);
//                    CommonUtils.cleanMember();
//                    ActivityUtils.getInstance().jumpActivity(LoginActivity.class);
                } else {
                    HttpParameterUtil.getInstance().requestRefreshToken(mHandler);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * ??????JSON??????????????????
     *
     * @param object JSONObject
     * @param type   TYPE_NORMAL ???????????????TYPE_SID_INVALID sid????????????
     * @return true ?????? false ??????
     */
    private boolean checkJSONStatus(JSONObject object, int type) {

        boolean isInvalid;
        switch (type) {
            case TYPE_NORMAL:
                isInvalid = object != null && !object.isNull("status") && JsonUtil.getInt(object, "status") == 1;
                break;
            case TYPE_SID_INVALID:
                isInvalid = object != null && !object.isNull("status") && JsonUtil.getInt(object, "status") == 2;
                break;
            default:
                isInvalid = false;
                break;
        }
        return isInvalid;
    }

    /**
     * ??????????????????
     */
    void parserCommon(String response, Handler mHandler, int success, int fail) {

        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                msg.what = success;
                msg.obj = true;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }

        mHandler.sendMessage(msg);
    }

    /**
     * ??????token
     */
    void parserRefreshToken(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONObject data = object.getJSONObject("data");
                msg.what = success;
                SharedPreferencesUtils.put(Constants.USER_TOKEN, data.getString("token"));
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ??????????????????
     */
    void parserHotSalesCategoryList(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        LinkedList<ModelHotSalesCategoryList> mList = new LinkedList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONArray array = object.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject data = array.getJSONObject(i);
                    String createTime = JsonUtil.getString(data, "createTime");
                    String createrAdminNo = JsonUtil.getString(data, "createrAdminNo");
                    String id = JsonUtil.getString(data, "id");
                    String introduction = JsonUtil.getString(data, "introduction");
                    String logo = JsonUtil.getString(data, "logo");
                    String menuNo = JsonUtil.getString(data, "menuNo");
                    String menuType = JsonUtil.getString(data, "menuType");
                    String menuTypeName = JsonUtil.getString(data, "menuTypeName");
                    String name = JsonUtil.getString(data, "name");
                    String other = JsonUtil.getString(data, "other");
                    String parentNo = JsonUtil.getString(data, "parentNo");
                    String role = JsonUtil.getString(data, "role");
                    String shopNo = JsonUtil.getString(data, "shopNo");
                    String sort = JsonUtil.getString(data, "sort");
                    String status = JsonUtil.getString(data, "status");
                    String updateTime = JsonUtil.getString(data, "updateTime");

                    ModelHotSalesCategoryList hotSalesCategoryList = new ModelHotSalesCategoryList(createTime, createrAdminNo, id, introduction, logo,
                            menuNo, menuType, menuTypeName, name, other, parentNo, role, shopNo, sort, status, updateTime, false);
                    mList.add(hotSalesCategoryList);
                }
                msg.what = success;
                msg.obj = mList;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ?????????????????????
     */
    void parserGetBannerList(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        LinkedList<ModelGetBannerList> mList = new LinkedList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONArray array = object.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject data = array.getJSONObject(i);
                    String createTime = JsonUtil.getString(data, "createTime");
                    String createrAdminNo = JsonUtil.getString(data, "createrAdminNo");
                    String id = JsonUtil.getString(data, "id");
                    String introduction = JsonUtil.getString(data, "introduction");
                    String imgUrl = JsonUtil.getString(data, "logo");
                    String menuNo = JsonUtil.getString(data, "menuNo");
                    String menuType = JsonUtil.getString(data, "menuType");
                    String menuTypeName = JsonUtil.getString(data, "menuTypeName");
                    String name = JsonUtil.getString(data, "name");
                    String other = JsonUtil.getString(data, "other");
                    String parentNo = JsonUtil.getString(data, "parentNo");
                    String role = JsonUtil.getString(data, "role");
                    String shopNo = JsonUtil.getString(data, "shopNo");
                    String sort = JsonUtil.getString(data, "sort");
                    String status = JsonUtil.getString(data, "status");
                    String updateTime = JsonUtil.getString(data, "updateTime");

                    ModelGetBannerList getBannerList = new ModelGetBannerList(createTime, createrAdminNo, id, introduction, imgUrl, menuNo,
                            menuType, menuTypeName, name, other, parentNo, role, shopNo, sort, status, updateTime);
                    mList.add(getBannerList);
                }
                msg.what = success;
                msg.obj = mList;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ????????????
     */
    void parserGetHotSalesProduct(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        LinkedList<ModelGetHotSalesProduct> mList = new LinkedList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONObject data = object.getJSONObject("data");
                JSONArray array = data.getJSONArray("rows");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.getJSONObject(i);

                    String area = JsonUtil.getString(item, "area");
                    String city = JsonUtil.getString(item, "city");
                    String collect = JsonUtil.getString(item, "collect");
                    String createTime = JsonUtil.getString(item, "createTime");
                    String customService = JsonUtil.getString(item, "customService");
                    String description = JsonUtil.getString(item, "description");
                    String detailImgPath = JsonUtil.getString(item, "detailImgPath");
                    String discountRatio = JsonUtil.getString(item, "discountRatio");
                    String flag = JsonUtil.getString(item, "flag");
                    String freightage = JsonUtil.getString(item, "freightage");
                    String gradeTimes = JsonUtil.getString(item, "gradeTimes");
                    String hasSaled = JsonUtil.getString(item, "hasSaled");
                    String id = JsonUtil.getString(item, "id");
                    String imagesPath = JsonUtil.getString(item, "imagesPath");
                    String isOnsale = JsonUtil.getString(item, "isOnsale");
                    String jsonProductSetBean = JsonUtil.getString(item, "jsonProductSetBean");
                    String logisticsType = JsonUtil.getString(item, "logisticsType");
                    String memberNo = JsonUtil.getString(item, "memberNo");
                    String menuNo = JsonUtil.getString(item, "menuNo");
                    String orderNo = JsonUtil.getString(item, "orderNo");
                    String originalPrice = JsonUtil.getString(item, "originalPrice");
                    String price = JsonUtil.getString(item, "price");

                    String productBanner = JsonUtil.getString(item, "productBanner");
                    String productCategoryNo = JsonUtil.getString(item, "productCategoryNo");
                    String productName = JsonUtil.getString(item, "productName");
                    String productNo = JsonUtil.getString(item, "productNo");
                    String productNum = JsonUtil.getString(item, "productNum");
                    String productParams = JsonUtil.getString(item, "productParams");
                    String productSetList = JsonUtil.getString(item, "productSetList");
                    String productTitle = JsonUtil.getString(item, "productTitle");
                    String productType = JsonUtil.getString(item, "productType");
                    String productsNos = JsonUtil.getString(item, "productsNos");
                    String province = JsonUtil.getString(item, "province");
                    String remark = JsonUtil.getString(item, "remark");
                    String shopCarNum = JsonUtil.getString(item, "shopCarNum");
                    String shopClassify = JsonUtil.getString(item, "shopClassify");
                    String shopName = JsonUtil.getString(item, "shopName");
                    String shopNo = JsonUtil.getString(item, "shopNo");
                    String sort = JsonUtil.getString(item, "sort");
                    String status = JsonUtil.getString(item, "status");
                    String storage = JsonUtil.getString(item, "storage");
                    String telephone = JsonUtil.getString(item, "telephone");
                    String totalGrade = JsonUtil.getString(item, "totalGrade");
                    String totalSaleAmt = JsonUtil.getString(item, "totalSaleAmt");
                    String units = JsonUtil.getString(item, "units");
                    String updateTime = JsonUtil.getString(item, "updateTime");
                    String vipPrice = JsonUtil.getString(item, "vipPrice");
                    String income = JsonUtil.getString(item, "income");

                    ModelGetHotSalesProduct getHotSalesProduct = new ModelGetHotSalesProduct(area, city, collect, createTime, customService, description, detailImgPath, discountRatio, flag, freightage,
                            gradeTimes, hasSaled, id, imagesPath, isOnsale, jsonProductSetBean, logisticsType, memberNo, menuNo, orderNo, originalPrice, price, productBanner, productCategoryNo, productName, productNo,
                            productNum, productParams, productSetList, productTitle, productType, productsNos, province, remark, shopCarNum, shopClassify, shopName, shopNo, sort, status, storage
                            , telephone, totalGrade, totalSaleAmt, units, updateTime, vipPrice);
                    getHotSalesProduct.setIncome(income);
                    mList.add(getHotSalesProduct);
                }

                msg.what = success;
                msg.obj = mList;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ??????????????????
     */
    void parserProductCategories(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        LinkedList<ModelProductCategories> mList = new LinkedList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONArray array = object.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    LinkedList<ModelProductCategoriesContent> mContentList = new LinkedList<>();
                    JSONObject data = array.getJSONObject(i);
                    String id = JsonUtil.getString(data, "id");
                    String menuNo = JsonUtil.getString(data, "menuNo");
                    String name1 = JsonUtil.getString(data, "name");
                    String other = JsonUtil.getString(data, "other");
                    String parentNo = JsonUtil.getString(data, "parentNo");

                    JSONArray array1 = data.getJSONArray("secondMenus");
                    for (int j = 0; j < array1.length(); j++) {
                        JSONObject data1 = array1.getJSONObject(j);
                        String id2 = JsonUtil.getString(data1, "id");//5066672
                        String logo = JsonUtil.getString(data1, "logo");//????????????
                        String menuNo2 = JsonUtil.getString(data1, "menuNo");
                        String name = JsonUtil.getString(data1, "name");//?????????
                        String other2 = JsonUtil.getString(data1, "other");
                        String parentNo2 = JsonUtil.getString(data1, "parentNo");
                        ModelProductCategoriesContent modelProductCategoriesContent = new ModelProductCategoriesContent(id2, logo, menuNo2, name, other2,
                                parentNo2);
                        mContentList.add(modelProductCategoriesContent);
                    }
                    ModelProductCategories modelProductCategories = new ModelProductCategories(id, menuNo, name1, other, parentNo, mContentList);
                    mList.add(modelProductCategories);
                }
                msg.what = success;
                msg.obj = mList;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ??????????????????????????????????????????
     */
    void parserProductByMenuNo(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        LinkedList<ModelProductByMenuNo> mList = new LinkedList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONArray array = object.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject data = array.getJSONObject(i);
                    String productNo = JsonUtil.getString(data, "productNo");
                    String productName = JsonUtil.getString(data, "productName");
                    String imagesPath = JsonUtil.getString(data, "imagesPath");
                    String description = JsonUtil.getString(data, "description");
                    String price = JsonUtil.getString(data, "price");
                    String units = JsonUtil.getString(data, "units");
                    String productCategoryNo = JsonUtil.getString(data, "productCategoryNo");
                    String shopNo = JsonUtil.getString(data, "shopNo");
                    String collect = JsonUtil.getString(data, "collect");
                    String hasSaled = JsonUtil.getString(data, "hasSaled");
                    String income = JsonUtil.getString(data, "income");


                    ModelProductByMenuNo productByMenuNo = new ModelProductByMenuNo(productNo, productName, imagesPath, description, price, units, productCategoryNo,
                            shopNo, collect, hasSaled);
                    productByMenuNo.setIncome(income);
                    mList.add(productByMenuNo);
                }
                msg.what = success;
                msg.obj = mList;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ????????????????????????(??????20???)
     */
    void parserLogSearch(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        LinkedList<ModelHotSearch> mList = new LinkedList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONObject data = object.getJSONObject("data");
                JSONArray array = data.getJSONArray("rows");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.getJSONObject(i);

                    String id = JsonUtil.getString(item, "id");
                    String searchNo = JsonUtil.getString(item, "searchNo");
                    String name = JsonUtil.getString(item, "name");
                    String count = JsonUtil.getString(item, "count");
                    String isShow = JsonUtil.getString(item, "isShow");
                    String showSort = JsonUtil.getString(item, "showSort");
                    String location = JsonUtil.getString(item, "location");
                    String status = JsonUtil.getString(item, "status");
                    String remark = JsonUtil.getString(item, "remark");
                    String createTime = JsonUtil.getString(item, "createTime");
                    String updateTime = JsonUtil.getString(item, "updateTime");

                    ModelHotSearch hotSearch = new ModelHotSearch(id, searchNo, name, count, isShow, showSort, location, status,
                            remark, createTime, updateTime);
                    mList.add(hotSearch);
                }

                msg.what = success;
                msg.obj = mList;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ????????????
     */
    void parserHotSearch(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        LinkedList<ModelHotSearch> mList = new LinkedList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONObject data = object.getJSONObject("data");
                JSONArray array = data.getJSONArray("rows");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.getJSONObject(i);

                    String id = JsonUtil.getString(item, "id");
                    String searchNo = JsonUtil.getString(item, "searchNo");
                    String name = JsonUtil.getString(item, "name");
                    String count = JsonUtil.getString(item, "count");
                    String isShow = JsonUtil.getString(item, "isShow");
                    String showSort = JsonUtil.getString(item, "showSort");
                    String location = JsonUtil.getString(item, "location");
                    String status = JsonUtil.getString(item, "status");
                    String remark = JsonUtil.getString(item, "remark");
                    String createTime = JsonUtil.getString(item, "createTime");
                    String updateTime = JsonUtil.getString(item, "updateTime");

                    ModelHotSearch hotSearch = new ModelHotSearch(id, searchNo, name, count, isShow, showSort, location, status,
                            remark, createTime, updateTime);
                    mList.add(hotSearch);
                }

                msg.what = success;
                msg.obj = mList;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ????????????
     */
    void parserSearchProduct(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        LinkedList<ModelSearchProduct> mList = new LinkedList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONObject data = object.getJSONObject("data");
                String total = JsonUtil.getString(data, "total");
                JSONArray array = data.getJSONArray("rows");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    String productNo = JsonUtil.getString(item, "productNo");
                    String shopNo = JsonUtil.getString(item, "shopNo");
                    String shopName = JsonUtil.getString(item, "shopName");
                    String menuNo = JsonUtil.getString(item, "menuNo");
                    String productCategoryNo = JsonUtil.getString(item, "productCategoryNo");
                    String productName = JsonUtil.getString(item, "productName");
                    String productType = JsonUtil.getString(item, "productType");
                    String price = JsonUtil.getString(item, "price");
                    String units = JsonUtil.getString(item, "units");
                    String storage = JsonUtil.getString(item, "storage");
                    String hasSaled = JsonUtil.getString(item, "hasSaled");
                    String totalGrade = JsonUtil.getString(item, "totalGrade");
                    String gradeTimes = JsonUtil.getString(item, "gradeTimes");
                    String imagesPath = JsonUtil.getString(item, "imagesPath");
                    String detailImgPath = JsonUtil.getString(item, "detailImgPath");

                    ModelSearchProduct getHotSalesProduct = new ModelSearchProduct(productNo, shopNo, shopName, menuNo, productCategoryNo, productName,
                            productType, price, units, storage, hasSaled, totalGrade, gradeTimes, imagesPath, detailImgPath);
                    mList.add(getHotSalesProduct);
                }
                ModelSearchResult searchResult = new ModelSearchResult(total, mList);

                msg.what = success;
                msg.obj = searchResult;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ????????????????????????
     */

    void parserRepairProductComment(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        ModelComment modelComment;
        LinkedList<ModelRepairOrderCommentDetail> mList = new LinkedList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONObject data = object.getJSONObject("data");

                String totalGrade = JsonUtil.getString(data, "totalGrade");

                JSONArray array = data.getJSONArray("rows");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.getJSONObject(i);

                    String id = JsonUtil.getString(item, "id");
                    String commentNo = JsonUtil.getString(item, "commentNo");
                    String businessType = JsonUtil.getString(item, "businessType");
                    String businessNo = JsonUtil.getString(item, "businessNo");
                    String memberNo = JsonUtil.getString(item, "memberNo");
                    String nickname = JsonUtil.getString(item, "nickname");
                    String discriptScore = JsonUtil.getString(item, "discriptScore");
                    String content = JsonUtil.getString(item, "content");
                    String imgPath = JsonUtil.getString(item, "imgPath");
                    String createTime = JsonUtil.getString(item, "createTime");
                    String updateTime = JsonUtil.getString(item, "updateTime");
                    String shopNo = JsonUtil.getString(item, "shopNo");
                    String headPath = JsonUtil.getString(item, "headPath");

                    ModelRepairOrderCommentDetail repairOrderCommentDetail = new ModelRepairOrderCommentDetail(id, commentNo, businessType,
                            businessNo, memberNo, nickname, discriptScore, content, imgPath, createTime, updateTime, shopNo, headPath);
                    mList.add(repairOrderCommentDetail);
                }
                modelComment = new ModelComment(totalGrade, mList);

                msg.what = success;
                msg.obj = modelComment;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ????????????
     */
    void parserProductDetail(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                LinkedList<ModelProductParams> productParamsList = new LinkedList<>();
                JSONObject data = object.getJSONObject("data");
                String id = JsonUtil.getString(data, "id");
                String productNo = JsonUtil.getString(data, "productNo");
                String productsNos = JsonUtil.getString(data, "productsNos");
                String shopNo = JsonUtil.getString(data, "shopNo");
                String shopName = JsonUtil.getString(data, "shopName");
                String menuNo = JsonUtil.getString(data, "menuNo");
                String productCategoryNo = JsonUtil.getString(data, "productCategoryNo");
                String productName = JsonUtil.getString(data, "productName");
                String productType = JsonUtil.getString(data, "productType");
                String price = JsonUtil.getString(data, "price");
                String originalPrice = JsonUtil.getString(data, "originalPrice");
                String vipPrice = JsonUtil.getString(data, "vipPrice");
                String shopCarNum = JsonUtil.getString(data, "shopCarNum");
                String units = JsonUtil.getString(data, "units");
                String storage = JsonUtil.getString(data, "storage");
                String hasSaled = JsonUtil.getString(data, "hasSaled");
                String imagesPath = JsonUtil.getString(data, "imagesPath");
                String description = JsonUtil.getString(data, "description");
                String isOnsale = JsonUtil.getString(data, "isOnsale");
                String remark = JsonUtil.getString(data, "remark");
                String status = JsonUtil.getString(data, "status");
                String createTime = JsonUtil.getString(data, "createTime");
                String updateTime = JsonUtil.getString(data, "updateTime");
                String sort = JsonUtil.getString(data, "sort");
                String detailImgPath = JsonUtil.getString(data, "detailImgPath");
                String productBanner = JsonUtil.getString(data, "productBanner");
                String productTitle = JsonUtil.getString(data, "productTitle");
                String discount = JsonUtil.getString(data, "discount");
                String discountRatio = JsonUtil.getString(data, "discountRatio");
                String shopClassify = JsonUtil.getString(data, "shopClassify");
                String freightage = JsonUtil.getString(data, "freightage");
                String customService = JsonUtil.getString(data, "customService");
                String orderNo = JsonUtil.getString(data, "orderNo");
                String productNum = JsonUtil.getString(data, "productNum");
                String flag = JsonUtil.getString(data, "flag");
                String collect = JsonUtil.getString(data, "collect");
                String productParams = JsonUtil.getString(data, "productParams");
                String totalGrade = JsonUtil.getString(data, "totalGrade");
                String income = JsonUtil.getString(data, "income");
                String productPlatNo = JsonUtil.getString(data, "productPlatNo");
                int feeMode = JsonUtil.getInt(data, "feeMode");

                ModelProductDetail productDetail = new ModelProductDetail(id, productNo, productsNos, shopNo, shopName, menuNo, productCategoryNo,
                        productName, productType, price, originalPrice, vipPrice, shopCarNum, units, storage, hasSaled, imagesPath, description,
                        isOnsale, remark, status, createTime, updateTime, sort, detailImgPath, productBanner, productParamsList, productTitle, discount,
                        discountRatio, shopClassify, freightage, customService, orderNo, productNum, flag, collect);
                productDetail.setTotalGrade(totalGrade);
                productDetail.setProductParams(productParams);
                productDetail.setIncome(income);
                productDetail.setProductPlatNo(productPlatNo);
                productDetail.setFeeMode(feeMode);

                msg.what = success;
                msg.obj = productDetail;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ??????????????????
     */
    void parserProductSetByProductNo(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        LinkedList<ModelProductSetByProductNo> mList = new LinkedList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
//                JSONObject data = object.getJSONObject("data");
                JSONArray array = object.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    LinkedList<ModelSize> sizeList = new LinkedList<>();
                    JSONObject data = array.getJSONObject(i);
                    String productSetNo = JsonUtil.getString(data, "productSetNo");
                    String productNo = JsonUtil.getString(data, "productNo");
                    String color = JsonUtil.getString(data, "color");

                    JSONArray array1 = data.getJSONArray("sizeList");
                    for (int j = 0; j < array1.length(); j++) {
                        JSONObject item = array1.getJSONObject(j);
                        String productSetNo2 = JsonUtil.getString(item, "productSetNo");
                        String imgPath = JsonUtil.getString(item, "imgPath");
                        String size = JsonUtil.getString(item, "size");
                        String stock = JsonUtil.getString(item, "stock");
                        String price = JsonUtil.getString(item, "price");
                        String supplyPrice = JsonUtil.getString(item, "supplyPrice");
                        ModelSize modelSize = new ModelSize(productSetNo2, imgPath, size, stock, price, supplyPrice);
                        sizeList.add(modelSize);
                    }
                    mList.add(new ModelProductSetByProductNo(productSetNo, productNo, color, sizeList));
                }

                msg.what = success;
                msg.obj = mList;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ???????????????
     */
    void parserMyShopCar(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        LinkedList<ModelShopCart> mList = new LinkedList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONArray array = object.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    LinkedList<ModelShopCartGoods> modelShopCartGoodsList = new LinkedList<>();
                    JSONObject data = array.getJSONObject(i);
                    String shopNo = JsonUtil.getString(data, "shopNo");
                    String shopName = JsonUtil.getString(data, "shopName");
                    String telephone = JsonUtil.getString(data, "telephone");
                    String logoPath = JsonUtil.getString(data, "logoPath");
                    String logisticsFee = JsonUtil.getString(data, "logisticsFee");
                    JSONArray array2 = data.getJSONArray("productList");
                    for (int j = 0; j < array2.length(); j++) {
                        JSONObject data2 = array2.getJSONObject(j);
                        String productNo = JsonUtil.getString(data2, "productNo");
                        String menuNo = JsonUtil.getString(data2, "menuNo");
                        String productName = JsonUtil.getString(data2, "productName");
                        String price = JsonUtil.getString(data2, "price");
                        String shopCarNum = JsonUtil.getString(data2, "shopCarNum");
                        String sort = JsonUtil.getString(data2, "sort");
                        String remark = JsonUtil.getString(data2, "remark");
                        String imagesPath = JsonUtil.getString(data2, "imagesPath");
                        String shopCarNo = JsonUtil.getString(data2, "shopCarNo");
                        String isOnsale = JsonUtil.getString(data2, "isOnsale");

                        ModelShopCartGoods modelShopCartGoods = new ModelShopCartGoods(productNo, menuNo, productName, price, shopCarNum,
                                sort, remark, imagesPath, shopCarNo);
                        modelShopCartGoods.setIsOnsale(isOnsale);
                        modelShopCartGoodsList.add(modelShopCartGoods);
                    }

                    ModelShopCart shopCart = new ModelShopCart(shopNo, shopName, telephone, logoPath, modelShopCartGoodsList);
                    shopCart.setLogisticsFee(logisticsFee);
                    mList.add(shopCart);
                }
                msg.what = success;
                msg.obj = mList;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ????????????
     */
    void parserAddressInfo(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        LinkedList<ModelAddress> mList = new LinkedList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONArray array = object.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject data = array.getJSONObject(i);
                    String linkman = JsonUtil.getString(data, "linkman");//?????????
                    String mobileNo = JsonUtil.getString(data, "mobileNo");//?????????
                    String address = JsonUtil.getString(data, "address");//????????????
                    String isdefault = JsonUtil.getString(data, "isdefault");//?????????????????????1????????????0????????????
                    String addressNo = JsonUtil.getString(data, "addressNo");
                    String province = JsonUtil.getString(data, "province");//??????
                    String city = JsonUtil.getString(data, "city");//??????
                    String area = JsonUtil.getString(data, "area");//??????
                    ModelAddress modelAddress = new ModelAddress(linkman,
                            mobileNo, address, isdefault, addressNo, province, city, area);
                    mList.add(modelAddress);
                }

                msg.what = success;
                msg.obj = mList;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ?????????????????????????????????
     */
    void parserShopCarSettle(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        LinkedList<ModelShopCarSettle> mList = new LinkedList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONArray arrayData = object.getJSONArray("data");
                for (int i = 0; i < arrayData.length(); i++) {
                    JSONObject data = arrayData.getJSONObject(i);
                    String shopNo = JsonUtil.getString(data, "shopNo");
                    String shopName = JsonUtil.getString(data, "shopName");
                    String telephone = JsonUtil.getString(data, "telephone");
                    String logoPath = JsonUtil.getString(data, "logoPath");
                    boolean canInvoice = JsonUtil.getString(data, "invoice").equals("1");
                    String logisticsFee = JsonUtil.getString(data, "logisticsFee");

                    LinkedList<ModelShopCarSettleItem> modelShopCarSettleItemList = new LinkedList<>();
                    JSONArray array = data.getJSONArray("productList");
                    for (int j = 0; j < array.length(); j++) {
                        JSONObject item = array.getJSONObject(j);
                        String productNo = JsonUtil.getString(item, "productNo");
                        String menuNo = JsonUtil.getString(item, "menuNo");
                        String productName = JsonUtil.getString(item, "productName");
                        String price = JsonUtil.getString(item, "price");
                        String vipPrice = JsonUtil.getString(item, "vipPrice");
                        String shopCarNum = JsonUtil.getString(item, "shopCarNum");
                        String sort = JsonUtil.getString(item, "sort");
                        String remark = JsonUtil.getString(item, "remark");
                        String imagesPath = JsonUtil.getString(item, "imagesPath");
                        String freightage = JsonUtil.getString(item, "freightage");
                        String shopCarNo = JsonUtil.getString(item, "shopCarNo");

                        ModelShopCarSettleItem modelShopCarSettleItem = new ModelShopCarSettleItem(productNo, menuNo, productName, price, vipPrice, shopCarNum, sort,
                                remark, imagesPath, freightage);
                        modelShopCarSettleItem.setShopCarNo(shopCarNo);
                        modelShopCarSettleItemList.add(modelShopCarSettleItem);
                    }

                    ModelShopCarSettle shopCarSettle = new ModelShopCarSettle(shopNo, shopName, telephone, logoPath, canInvoice, modelShopCarSettleItemList);
                    shopCarSettle.setLogisticsFee(logisticsFee);
                    mList.add(shopCarSettle);
                }

                msg.what = success;
                msg.obj = mList;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ????????????
     */
    void parserCreateOrder(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                String orderNo = JsonUtil.getString(object, "data");
                msg.what = success;
                msg.obj = orderNo;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ??????????????????
     */
    void parserWechatPayParams(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONObject data = object.getJSONObject("data");
                String appid = JsonUtil.getString(data, "appid"); // appid
                String partnerid = JsonUtil.getString(data, "partnerid"); // ?????????
                String prepayid = JsonUtil.getString(data, "prepayid"); // ?????????????????????ID
                String package1 = "Sign=WXPay"; // ????????????
                String noncestr = JsonUtil.getString(data, "noncestr"); // ???????????????
                String timestamp = JsonUtil.getString(data, "timestamp"); // ?????????
                String sign = JsonUtil.getString(data, "sign"); // ??????
                ModelWXPay wxPay = new ModelWXPay(appid, partnerid, prepayid, package1, noncestr, timestamp, sign);
                msg.what = success;
                msg.obj = wxPay;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ?????????????????????
     */
    void parserAlipayParams(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
//                JSONObject dataObj = object.getJSONObject("data");
//                Map<String, String> mMap = new HashMap<>();
//                Iterator<String> iterator = dataObj.keys();
//                String key_sign = "sign"; // ????????????
//                String value_sign = ""; // ????????????
//                while (iterator.hasNext()) {
//                    String key = iterator.next();
//                    String value = dataObj.getString(key);
//                    if (key.equals(key_sign)) {
////                        try {
////                            value = URLEncoder.encode(value, "UTF-8");
////                        } catch (UnsupportedEncodingException e) {
////                            e.printStackTrace();
////                        }
//                        value_sign = value;
//                    } else {
//                        value = URLDecoder.decode(value, "UTF-8");
//                        mMap.put(key, value);
//                    }
//                }
//
//                String urlParameter = CommonUtils.getSortUrlParameter(mMap);
//                urlParameter += "&" + key_sign + "=" + value_sign;

                msg.what = success;
                msg.obj = JsonUtil.getString(object, "data");
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ????????????-????????????????????????
     */
    void parserMallOrderList(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        LinkedList<ModelMallOrder> mList = new LinkedList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONObject data2 = object.getJSONObject("data");
                JSONArray array = data2.getJSONArray("rows");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject data = array.getJSONObject(i);
                    LinkedList<ModelMallOrderItem> mItemList = new LinkedList<>();
                    String orderName = JsonUtil.getString(data, "orderName");
                    String orderNo = JsonUtil.getString(data, "orderNo");
                    String shopName = JsonUtil.getString(data, "shopName");
                    String shopNo = JsonUtil.getString(data, "shopNo");
                    String payType = JsonUtil.getString(data, "payType");
                    String realAmt = JsonUtil.getString(data, "realAmt");
                    String realPrice = JsonUtil.getString(data, "realPrice");
                    String refund = JsonUtil.getString(data, "refund");
                    String couponAmt = JsonUtil.getString(data, "couponAmt");
                    String logisticsFee = JsonUtil.getString(data, "logisticsFee");
                    String logisticsType = JsonUtil.getString(data, "logisticsType");// ????????????
                    String logisticsNo = JsonUtil.getString(data, "logisticsNo");// ????????????
                    String receiver = JsonUtil.getString(data, "receiver");// ?????????
                    String receiverMobile = JsonUtil.getString(data, "receiverMobile");// ??????????????????
                    String address = JsonUtil.getString(data, "address");// ?????????????????????
                    String payTypeStr = JsonUtil.getString(data, "payTypeStr");
                    String getScoreT = JsonUtil.getString(data, "getScore");
                    String dueAmt = JsonUtil.getString(data, "dueAmt");
                    String leaveMsg = JsonUtil.getString(data, "leaveMsg");// ????????????
                    String balance = JsonUtil.getString(data, "balance");
                    long payTime = JsonUtil.getLong(data, "payTime");
                    String orderStatus = JsonUtil.getString(data, "orderStatus");// ????????????
                    String couponNo = JsonUtil.getString(data, "couponNo");// ????????????
                    String invoiceEmail = JsonUtil.getString(data, "invoiceEmail");

                    JSONObject comments = data.isNull("comments") ? null : data.getJSONObject("comments");
                    String commentNo = JsonUtil.getString(comments, "commentNo");// ????????????
                    String businessNo = JsonUtil.getString(comments, "businessNo");// ????????????
                    String businessType = JsonUtil.getString(comments, "businessType");// ????????????
                    String memberNo = JsonUtil.getString(comments, "memberNo");// ???????????????
                    String discriptScore = JsonUtil.getString(comments, "discriptScore");// ??????
                    String content = JsonUtil.getString(comments, "content");// ??????
                    String imgPath = JsonUtil.getString(comments, "imgPath");// ????????????

                    JSONObject shop = data.getJSONObject("shop");

                    JSONArray array1 = data.getJSONArray("orderProductsList");
                    for (int j = 0; j < array1.length(); j++) {
                        JSONObject data1 = array1.getJSONObject(j);
                        String createTime = JsonUtil.getString(data1, "createTime");
                        String getScore = JsonUtil.getString(data1, "getScore");
                        String id = JsonUtil.getString(data1, "id");
                        String logicsFee = JsonUtil.getString(data1, "logicsFee");
                        String menuNo = JsonUtil.getString(data1, "menuNo");
                        String orderNo1 = JsonUtil.getString(data, "orderNo");
                        String orderProductNo = JsonUtil.getString(data1, "orderProductNo");
                        String originalPrice = JsonUtil.getString(data1, "originalPrice");
                        String productColorSize = JsonUtil.getString(data1, "productColorSize");
                        String productImg = JsonUtil.getString(data1, "productImg");
                        String productName = JsonUtil.getString(data1, "productName");
                        String productNo = JsonUtil.getString(data1, "productNo");
                        String productNum = JsonUtil.getString(data1, "productNum");
                        String realAmt1 = JsonUtil.getString(data1, "realAmt");
                        String remark = JsonUtil.getString(data1, "remark");
                        String remindAmt = JsonUtil.getString(data1, "remindAmt");
                        String shopName1 = JsonUtil.getString(data1, "shopName");
                        String shopNo1 = JsonUtil.getString(data1, "shopNo");
                        String unit = JsonUtil.getString(data1, "unit");
                        String updateTime = JsonUtil.getString(data1, "updateTime");
                        ModelMallOrderItem modelMallOrderItem = new ModelMallOrderItem(createTime,
                                getScore, id, logicsFee, menuNo, orderNo1, orderProductNo,
                                originalPrice, productColorSize, productImg, productName,
                                productNo, productNum, realAmt1, remark, remindAmt, shopName1, shopNo1,
                                unit, updateTime);
                        modelMallOrderItem.setPayType(payType);
                        modelMallOrderItem.setBalance(balance);
                        mItemList.add(modelMallOrderItem);
                    }

                    ModelMallOrder modelMallOrder = new ModelMallOrder(orderName, orderNo, shopName,
                            shopNo, payType, realAmt, couponAmt, logisticsFee, logisticsType,
                            logisticsNo, receiver, receiverMobile, address, commentNo, businessNo,
                            businessType, memberNo, discriptScore, content, imgPath, orderStatus, couponNo, mItemList);
                    modelMallOrder.setRealPrice(realPrice);
                    modelMallOrder.setRefund(refund);
                    modelMallOrder.setLeaveMsg(leaveMsg);
                    modelMallOrder.setPayTypeStr(payTypeStr);
                    modelMallOrder.setGetScore(getScoreT);
                    modelMallOrder.setDueAmt(dueAmt);
                    modelMallOrder.setPayTime(payTime);
                    modelMallOrder.setBalance(balance);
                    modelMallOrder.setInvoiceEmail(invoiceEmail);
                    String shopAddressStr = shop.getString("province") + shop.getString("city") + shop.getString("area") + shop.getString("address");
                    modelMallOrder.setShop(new ModelMallOrder.Shop(shop.getString("mobileNo"), shop.getString("telephone"), shopAddressStr));
                    mList.add(modelMallOrder);
                }
                msg.what = success;
                msg.obj = mList;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ????????????
     */

    void parserImageUpload(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            boolean successState = JsonUtil.getBoolean(object, "success");
            String url = JsonUtil.getString(object, "data");
            if (successState && url != null && !url.isEmpty()) {
                msg.what = success;
                msg.obj = url;
            } else {
                msg.what = fail;
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ??????????????????
     */
    void parserProductCollect(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        LinkedList<GoodsModel> mList = new LinkedList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONObject data = object.getJSONObject("data");
                JSONArray rows = data.getJSONArray("rows");
                for (int i = 0; i < rows.length(); i++) {
                    JSONObject item = rows.getJSONObject(i);
                    String productName = JsonUtil.getString(item, "productName");//????????????
                    String imagesPath = JsonUtil.getString(item, "imagesPath");//??????????????????
                    String price = JsonUtil.getString(item, "price");//???????????????
                    String productNo = JsonUtil.getString(item, "productNo");//????????????
                    String productType = JsonUtil.getString(item, "productType");
                    String isOnsale = JsonUtil.getString(item, "isOnsale");
                    GoodsModel goodsModel = new GoodsModel();
                    goodsModel.setProductType(productType);
                    goodsModel.setProductName(productName);
                    goodsModel.setImagesPath(imagesPath);
                    goodsModel.setPrice(price);
                    goodsModel.setProductNo(productNo);
                    goodsModel.setIsOnsale(isOnsale);
                    mList.add(goodsModel);
                }

                msg.what = success;
                msg.obj = mList;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ????????????
     */
    void parserMyWallet(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONObject data = object.getJSONObject("data");
                String balance = JsonUtil.getString(data, "balance");//????????????
                String score = JsonUtil.getString(data, "score");//??????????????????
                String balanceAmtFreeze = JsonUtil.getString(data, "balanceAmtFreeze");//????????????
                String baodanbi = JsonUtil.getString(data, "baodanbi");//???????????????
                String yongjin = JsonUtil.getString(data, "yongjin");//????????????
                String cycleLoginTimes = JsonUtil.getString(data, "cycleLoginTimes");//????????????
                ModelWallet wallet = new ModelWallet(balance, score, balanceAmtFreeze, baodanbi, cycleLoginTimes);
                wallet.setYongjin(yongjin);
                msg.what = success;
                msg.obj = wallet;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ????????????
     */
    void parserTransactionRecords(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        LinkedList<ModelTransactionRecords> mList = new LinkedList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONObject data = object.getJSONObject("data");
                JSONArray rows = data.getJSONArray("rows");
                for (int i = 0; i < rows.length(); i++) {
                    JSONObject item = rows.getJSONObject(i);
                    String accountSource = JsonUtil.getString(item, "accountSource");//??????
                    String amt = JsonUtil.getString(item, "amt");//??????
                    String accountType = JsonUtil.getString(item, "accountType");//zhichu???shouru
                    String createTime = JsonUtil.getString(item, "createTime");//??????
                    amt = (TextUtils.isEmpty(accountType) ? "" : TextUtils.equals(accountType, "zhichu") ? "-" : "+") + amt;
                    ModelTransactionRecords records = new ModelTransactionRecords(
                            i + "", accountSource, createTime, amt);
                    mList.add(records);
                }

                msg.what = success;
                msg.obj = mList;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ???????????????
     */
    void parserMemberBankcard(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        LinkedList<ModelMemberBankcard> mList = new LinkedList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {

                JSONArray data = object.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject item = data.getJSONObject(i);
                    String bankcardNo = JsonUtil.getString(item, "bankcardNo"); // ?????????????????????????????????
                    String bankName = JsonUtil.getString(item, "bankName"); // ?????????
                    String bankcardNum = JsonUtil.getString(item, "bankcardNum"); // ??????
                    String bankcardName = JsonUtil.getString(item, "bankcardName"); // ?????????
                    ModelMemberBankcard memberBankcard = new ModelMemberBankcard(bankcardNo, bankName, bankcardNum, bankcardName);
                    mList.add(memberBankcard);
                }

                msg.what = success;
                msg.obj = mList;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ????????????(????????????)
     */
    void parserWithdrawApply(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        LinkedList<ModelWithdrawDeposit> mList = new LinkedList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONObject dataObj = object.getJSONObject("data");
                JSONArray array = dataObj.getJSONArray("rows");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject data = array.getJSONObject(i);
                    String withdrawApplyNo = JsonUtil.getString(data, "withdrawApplyNo"); // ??????????????????
                    String withdrawState = JsonUtil.getString(data, "withdrawState"); // ????????????
                    String applyAmt = JsonUtil.getString(data, "applyAmt"); // ????????????
                    String createTime = JsonUtil.getString(data, "createTime"); // ????????????
                    String cardNo = JsonUtil.getString(data, "cardNo");// ??????
                    String bank = JsonUtil.getString(data, "bank");// ??????
                    int isReject = JsonUtil.getInt(data, "isReject");
                    String remark = JsonUtil.getString(data, "remark");// ????????????
                    String originalPrice = JsonUtil.getString(data, "originalPrice");// ????????????
                    String vipPrice = JsonUtil.getString(data, "vipPrice");// ?????????
                    String platformAmt = JsonUtil.getString(data, "platformAmt");// ????????????

                    ModelWithdrawDeposit withdrawApply = new ModelWithdrawDeposit(withdrawApplyNo,
                            withdrawState, applyAmt, cardNo, bank, createTime);
                    withdrawApply.setIsReject(isReject);
                    withdrawApply.setRemark(remark);
                    withdrawApply.setOriginalPrice(originalPrice);
                    withdrawApply.setVipPrice(vipPrice);
                    withdrawApply.setPlatformAmt(platformAmt);
                    mList.add(withdrawApply);
                }
                msg.what = success;
                msg.obj = mList;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ??????????????????
     */
    void parserBonus(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {

                JSONObject data = object.getJSONObject("data");
                String bonusAccountAmt = JsonUtil.getString(data, "bonusAccountAmt"); // ??????????????????
                String unSettlement = JsonUtil.getString(data, "unSettlement");
                String orderAmtDay = JsonUtil.getString(data, "orderAmtDay"); // ??????????????????
                String orderAmtBouns = JsonUtil.getString(data, "orderAmtBouns"); // ??????????????????
                String orderAmtMy = JsonUtil.getString(data, "orderAmtMy"); // ??????????????????????????????
                String orderAmtRec = JsonUtil.getString(data, "orderAmtRec"); // ??????????????????????????????
                String orderAmtFirst = JsonUtil.getString(data, "orderAmtFirst"); // ????????????????????????
                String orderAmtSecond = JsonUtil.getString(data, "orderAmtSecond"); // ????????????????????????
                String shopAmt = JsonUtil.getString(data, "shopAmt");// ????????????????????????????????????

                String lastOrderAmtMy = JsonUtil.getString(data, "lastOrderAmtMy"); // ????????????????????????????????????
                String lastOrderAmtRec = JsonUtil.getString(data, "lastOrderAmtRec"); // ????????????????????????????????????
                String lastOrderAmtFirst = JsonUtil.getString(data, "lastOrderAmtFirst"); // ??????????????????????????????
                String lastOrderAmtSecond = JsonUtil.getString(data, "lastOrderAmtSecond"); // ??????????????????????????????
                String lastShopAmt = JsonUtil.getString(data, "lastShopAmt");// ??????????????????????????????????????????

                ModelBonus bonus = new ModelBonus(bonusAccountAmt, orderAmtDay, orderAmtBouns, orderAmtMy, orderAmtRec, orderAmtFirst, orderAmtSecond);
                bonus.setShopAmt(shopAmt);
                bonus.setLastOrderAmtMy(lastOrderAmtMy);
                bonus.setLastOrderAmtRec(lastOrderAmtRec);
                bonus.setLastOrderAmtFirst(lastOrderAmtFirst);
                bonus.setLastOrderAmtSecond(lastOrderAmtSecond);
                bonus.setLastShopAmt(lastShopAmt);
                bonus.setUnSettlement(unSettlement);

                msg.what = success;
                msg.obj = bonus;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ????????????
     */
    void parserPayRecords(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        LinkedList<ModelPayRecords> mList = new LinkedList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONObject data = object.getJSONObject("data");
                JSONArray rows = data.getJSONArray("rows");
                for (int i = 0; i < rows.length(); i++) {
                    JSONObject item = rows.getJSONObject(i);
                    String accountSource = JsonUtil.getString(item, "accountSource");//??????
                    String amt = JsonUtil.getString(item, "amt");//??????
                    String accountType = JsonUtil.getString(item, "accountType");//zhichu???shouru
                    String createTime = JsonUtil.getString(item, "createTime");//??????
                    String remark = JsonUtil.getString(item, "remark");
                    amt = (TextUtils.isEmpty(accountType) ? "" : TextUtils.equals(accountType, "zhichu") ? "-???" : "+???") + (UnitUtil.getMoney(amt, false));
                    ModelPayRecords records = new ModelPayRecords(i + "", accountSource, createTime, amt, "");
                    records.setRemark(remark);
                    mList.add(records);
                }

                ModelPayRecords payRecords = new ModelPayRecords();
                String amtType = "";
                String totalRechargeMoney = "0";
                String totalRechargeCount = "0";
                if (!data.isNull("data")) {
                    JSONObject other = data.getJSONObject("data");
                    amtType = JsonUtil.getString(other, "amtType");// ?????????????????????
                    totalRechargeMoney = JsonUtil.getString(other, "totalRechargeMoney");// ???????????????
                    totalRechargeCount = JsonUtil.getString(other, "totalRechargeCount");// ???????????????
                }
                payRecords.setAmtType(amtType);
                payRecords.setAmtType(totalRechargeMoney);
                payRecords.setAmtType(totalRechargeCount);
                payRecords.setmRecordList(mList);

                msg.what = success;
                msg.obj = payRecords;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ??????????????????
     */
    void parserUserInfo(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONObject data = object.getJSONObject("data");
                msg.what = success;
                msg.obj = GsonUtils.getGson().fromJson(data.toString(), Member.class);
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (JSONException e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ??????????????????
     */
    void parserRechareAdd(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                msg.what = success;
                msg.obj = JsonUtil.getString(object, "data");
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ??????????????????????????????
     */
    void parserStateOfReturn(String response, Handler mHandler, int success, int fail) {

        Message msg = mHandler.obtainMessage();

        Gson gson = new Gson();
        ReturnOfGoodsStatus returnOfGoodsStatus = gson.fromJson(response, ReturnOfGoodsStatus.class);
        if (returnOfGoodsStatus.getStatus() == 1) {
            msg.what = success;
            msg.arg1 = 1;
            msg.obj = returnOfGoodsStatus;
        } else {
            msg.what = fail;
            msg.arg1 = returnOfGoodsStatus.getStatus() == 0 ? 1111 : returnOfGoodsStatus.getStatus();
            msg.obj = returnOfGoodsStatus.getMsg() == null ? "error" : returnOfGoodsStatus.getMsg();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ????????????
     */
    void parserReturnOfGoodsApply(String response, Handler mHandler, int success, int fail) {

        Message msg = mHandler.obtainMessage();

        Gson gson = new Gson();
        ReturnOfGoodsApplyModel returnOfGoodsApplyModel = gson.fromJson(response, ReturnOfGoodsApplyModel.class);
        if (returnOfGoodsApplyModel.getStatus() == 1) {
            msg.what = success;
            msg.arg1 = 1;
            msg.obj = returnOfGoodsApplyModel;
        } else {
            msg.what = fail;
            msg.arg1 = returnOfGoodsApplyModel.getStatus() == 0 ? 1111 : returnOfGoodsApplyModel.getStatus();
            msg.obj = returnOfGoodsApplyModel.getMsg() == null ? "error" : returnOfGoodsApplyModel.getMsg();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ????????????
     */
    void parserReturnSendBackConfirm(String response, Handler mHandler, int success, int fail) {

        Message msg = mHandler.obtainMessage();

        Gson gson = new Gson();
        SendBackConfirmStatus sendBackConfirmStatus = gson.fromJson(response, SendBackConfirmStatus.class);
        if (sendBackConfirmStatus.getStatus() == 1) {
            msg.what = success;
            msg.arg1 = 1;
            msg.obj = sendBackConfirmStatus;
        } else {
            msg.what = fail;
            msg.arg1 = sendBackConfirmStatus.getStatus() == 0 ? 1111 : sendBackConfirmStatus.getStatus();
            msg.obj = sendBackConfirmStatus.getMsg() == null ? "error" : sendBackConfirmStatus.getMsg();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ??????????????????
     */
    void parserFlowList(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();

        Gson gson = new Gson();
        FlowListModel flowListModel = gson.fromJson(response, FlowListModel.class);
        if (flowListModel.getStatus() == 1) {
            msg.what = success;
            msg.arg1 = 1;
            msg.obj = flowListModel;
        } else {
            msg.what = fail;
            msg.arg1 = flowListModel.getStatus() == 0 ? 1111 : flowListModel.getStatus();
            msg.obj = flowListModel.getMsg() == null ? "error" : flowListModel.getMsg();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ????????????
     */
    void parserShopDetail(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONObject data = object.getJSONObject("data");
                msg.what = success;
                msg.obj = getModelShopDetail(data);
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
        }
        mHandler.sendMessage(msg);
    }

    private ModelShopDetail getModelShopDetail(JSONObject data) {
        String shopNo = JsonUtil.getString(data, "shopNo");// ????????????
        String shopType = JsonUtil.getString(data, "shopType");// ????????????
        String shopName = JsonUtil.getString(data, "shopName");// ????????????
        String telephone = JsonUtil.getString(data, "telephone");// ???????????????
        String mobileNo = JsonUtil.getString(data, "mobileNo");// ?????????
        String logoPath = JsonUtil.getString(data, "logoPath");// logo????????????
        String imagesPath = JsonUtil.getString(data, "imagesPath");// ??????????????????
        String longitude = JsonUtil.getString(data, "longitude");// ??????
        String latitude = JsonUtil.getString(data, "latitude");// ??????
        String distance = JsonUtil.getString(data, "distance");// ??????
        String address = JsonUtil.getString(data, "address");// ??????
        String totalScore = JsonUtil.getString(data, "totalScore");// ??????
        String introduce = JsonUtil.getString(data, "introduce");// ????????????
        String province = JsonUtil.getString(data, "province");// ???
        String city = JsonUtil.getString(data, "city");// ??????
        String area = JsonUtil.getString(data, "area");// ??????
        boolean isCollected = JsonUtil.getInt(data, "isCollected") != 0;// ????????????
        String identityPath = JsonUtil.getString(data, "identityPath");// ??????????????????,??????
        String openTimeStr = JsonUtil.getString(data, "openTime");// ??????????????????
        String closeTimeStr = JsonUtil.getString(data, "closeTime");// ??????????????????
        String remark = JsonUtil.getString(data, "remark");// ??????????????????
        String shopScore = JsonUtil.getString(data, "shopScore");
        String logisticsType = JsonUtil.getString(data, "logisticsType");
        String repairBrand = JsonUtil.getString(data, "repairBrand");

        ModelShopDetail detail = new ModelShopDetail(shopNo, shopType, shopName, telephone,
                mobileNo, logoPath, imagesPath, longitude, latitude, distance, address,
                totalScore, introduce, province, city, area, isCollected, identityPath,
                openTimeStr, closeTimeStr, shopScore);
        detail.setRemark(remark);
        detail.setLogisticsType(logisticsType);
        detail.setRepairBrand(repairBrand);
        return detail;
    }

    /**
     * ????????????
     */

    void parserShopInfo(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONObject data = object.getJSONObject("data");
                msg.what = success;
                msg.obj = getModelShop(data);
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    private ModelShop1 getModelShop(JSONObject data) {
        String id = JsonUtil.getString(data, "id");
        String image = JsonUtil.getString(data, "image");
        String title = JsonUtil.getString(data, "title");
        String describe = JsonUtil.getString(data, "describe");
        String distance = JsonUtil.getString(data, "distance");
        String grade = JsonUtil.getString(data, "grade");
        String shopNo = JsonUtil.getString(data, "shopNo");// ????????????
        String shopName = JsonUtil.getString(data, "shopName");// ????????????
        String imagesPath = JsonUtil.getString(data, "imagesPath");// ???????????????????????????
        String address = JsonUtil.getString(data, "address");// ????????????
        String status = JsonUtil.getString(data, "status");// ????????????
        String dayAmtTotal = JsonUtil.getString(data, "todayIncome");// ????????????
        dayAmtTotal = TextUtils.isEmpty(dayAmtTotal) ? "0" : dayAmtTotal;
        String dayOrderCount = JsonUtil.getString(data, "todayOrderCount");// ??????????????????
        dayOrderCount = TextUtils.isEmpty(dayOrderCount) ? "0" : dayOrderCount;
        String balance = JsonUtil.getString(data, "balance");// ????????????
        balance = TextUtils.isEmpty(balance) ? "0" : balance;
        String productCount = JsonUtil.getString(data, "productCategoryCount");// ??????????????????
        String shopType = JsonUtil.getString(data, "shopType");
        String shopScore = JsonUtil.getString(data, "shopScore");
        String logisticsType = JsonUtil.getString(data, "logisticsType");
        String repairBrand = JsonUtil.getString(data, "repairBrand");

        String totalIncome = JsonUtil.getString(data, "totalIncome");//????????????
        String expectIncome = JsonUtil.getString(data, "expectIncome");//????????????

        ModelShop1 modelShop = new ModelShop1(id, image, title, describe, distance, grade, shopNo, shopName, imagesPath,
                address, status, dayAmtTotal, dayOrderCount, balance, productCount, shopScore);
        modelShop.setLogisticsType(logisticsType);
        modelShop.setRepairBrand(repairBrand);
        modelShop.setTotalIncome(totalIncome);
        modelShop.setExpectIncome(expectIncome);

        ModelShopDetail shopDetail = getModelShopDetail(data);
        modelShop.setmModelShopDetail(shopDetail);
        modelShop.setShopType(shopDetail.getShopType());
        modelShop.setProvince(shopDetail.getProvince());
        modelShop.setCity(shopDetail.getCity());
        modelShop.setArea(shopDetail.getArea());

        return modelShop;
    }

    /**
     * ??????????????????
     */
    void parserAccountDetail(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONObject data = object.getJSONObject("data");
                String noSettle = JsonUtil.getString(data, "noSettle"); // ???????????????
                String yongjin = JsonUtil.getString(data, "yongjin"); // ????????????
                ModelAccountDetail accountDetail = new ModelAccountDetail(noSettle, yongjin);
                msg.what = success;
                msg.obj = accountDetail;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ????????????
     */
    void parserOrderSettle(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                LinkedList<ModelOrderSettle> settles = new LinkedList<>();
                JSONObject data = object.getJSONObject("data");
                JSONArray rows = data.getJSONArray("rows");
                for (int i = 0; i < rows.length(); i++) {
                    JSONObject item = rows.getJSONObject(i);
                    String orderNo = JsonUtil.getString(item, "orderNo"); // ????????????
                    String orderName = JsonUtil.getString(item, "orderName"); // ????????????
                    String scoreAmt = JsonUtil.getString(item, "scoreAmt"); // ??????
                    String createTime = JsonUtil.getString(item, "createTime"); // ??????
                    ModelOrderSettle orderSettle = new ModelOrderSettle(orderNo, orderName, scoreAmt, createTime);
                    settles.add(orderSettle);
                }

                msg.what = success;
                msg.obj = settles;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ??????????????????
     */
    void parserShopCommodityStat(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        LinkedList<ModelSimple> mList = new LinkedList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONArray array = object.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject data = array.getJSONObject(i);
                    String productCategoryName = JsonUtil.getString(data, "productCategoryName");//????????????
                    String productCount = JsonUtil.getString(data, "productCount");//???????????????
                    String productCategoryNo = JsonUtil.getString(data, "productCategoryNo");//????????????
                    mList.add(new ModelSimple(productCategoryNo, productCategoryName, productCount));
                }

                msg.what = success;
                msg.obj = mList;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ????????????
     */
    void parserClassifyCommodity(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        LinkedList<GoodsModel> mList = new LinkedList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONArray array = object.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject data = array.getJSONObject(i);
                    mList.add(getGoodsModel(data));
                }

                msg.what = success;
                msg.obj = mList;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    private GoodsModel getGoodsModel(JSONObject data) {
        String productNo = JsonUtil.getString(data, "productNo");// ????????????
        String productName = JsonUtil.getString(data, "productName");// ????????????
        String imagesPath = JsonUtil.getString(data, "imagesPath");// ???????????????????????????,????????????
        String description = JsonUtil.getString(data, "description");// ????????????
        String price = JsonUtil.getString(data, "price");// ????????????
        String units = JsonUtil.getString(data, "units");// ????????????
        String productCategoryNo = JsonUtil.getString(data, "productCategoryNo");// ??????????????????
        String shopNo = JsonUtil.getString(data, "shopNo");// ????????????
        String remark = JsonUtil.getString(data, "remark");// ????????????
        boolean collect = JsonUtil.getBoolean(data, "collect");// true(??????)???false(?????????)
        int hasSaled = JsonUtil.getInt(data, "hasSaled");
        int storage = JsonUtil.getInt(data, "storage");
        int feeMode = JsonUtil.getInt(data, "feeMode");
        GoodsModel goodsModel = new GoodsModel(productNo, productName, imagesPath, description, price,
                units, productCategoryNo, shopNo, collect);
        goodsModel.setHasSaled(hasSaled);
        goodsModel.setStorage(storage);
        goodsModel.setRemark(remark);
        goodsModel.setProductPlatNo(JsonUtil.getString(data, "productPlatNo")); // ??????????????????
        goodsModel.setOriginalPrice(JsonUtil.getString(data, "originalPrice"));
        goodsModel.setFeeMode(feeMode); // ??????????????????
        goodsModel.setVipPrice(JsonUtil.getString(data, "vipPrice"));// ?????????
        return goodsModel;
    }

    /**
     * ????????????
     */
    void parserOrderReport(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        LinkedList<ModelOrderReport> mList = new LinkedList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {

                JSONArray array = object.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject data = array.getJSONObject(i);
                    String dateT = JsonUtil.getString(data, "dateT"); // MM-dd???????????????
                    String dateStr = JsonUtil.getString(data, "dateStr"); // yyyy-MM-dd???????????????
                    String orderCount = JsonUtil.getString(data, "orderCount"); // ????????????
                    String scoreAmtSum = JsonUtil.getString(data, "scoreAmtSum"); // ????????????
                    String refundCount = JsonUtil.getString(data, "refundCount"); // ????????????
                    String refundAmtSum = JsonUtil.getString(data, "refundAmtSum"); // ????????????
                    ModelOrderReport orderReport = new ModelOrderReport(dateT, dateStr, orderCount, scoreAmtSum, refundCount, refundAmtSum);
                    mList.add(orderReport);
                }
                msg.what = success;
                msg.obj = mList;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ????????????
     */
    void parserTrdeStatistic(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONObject data = object.getJSONObject("data");
                String monthOrderCount = JsonUtil.getString(data, "monthOrderCount");//??????????????????
                String monthEarn = JsonUtil.getString(data, "monthEarn");//????????????
                String totalOrderCount = JsonUtil.getString(data, "orderTotals");//???????????????
                String totalEarn = JsonUtil.getString(data, "orderTotalsPrice");//?????????
                LinkedList<BusinessModel> mBusinessList = new LinkedList<>();// ????????????
                if (!data.isNull("list")) {
                    JSONArray array = data.getJSONArray("list");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject item = array.getJSONObject(i);
                        String orderNo = JsonUtil.getString(item, "orderNo"); //????????????"
                        String nickname = JsonUtil.getString(item, "nickname"); //??????"
                        String orderName = JsonUtil.getString(item, "orderName"); //????????????"
                        String memberHeadPath = JsonUtil.getString(item, "memberHeadPath"); //????????????"
                        String realAmt = JsonUtil.getString(item, "realAmt"); //??????????????????"
                        if (item.isNull("realAmt")) {
                            realAmt = JsonUtil.getString(item, "realPrice"); //??????????????????"
                        }
                        String createTime = JsonUtil.getString(item, "createTime"); //????????????"
                        String receiverMobile = JsonUtil.getString(item, "receiverMobile");
                        BusinessModel businessModel = new BusinessModel(orderNo, nickname, orderName, memberHeadPath, realAmt, createTime);
                        businessModel.setReceiverMobile(receiverMobile);
                        mBusinessList.add(businessModel);
                    }
                }

                ModelTrdeStatistic model = new ModelTrdeStatistic(monthOrderCount, monthEarn,
                        totalOrderCount, totalEarn, mBusinessList);

                msg.what = success;
                msg.obj = model;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ????????????
     */
    void parserOrderDetail(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONObject data = object.getJSONObject("data");
                String orderNo = JsonUtil.getString(data, "orderNo");//????????????
                String shopNo = JsonUtil.getString(data, "shopNo");//????????????
                String originalPrice = JsonUtil.getString(data, "originalPrice");//????????????
                String shopLogo = JsonUtil.getString(data, "shopLogo");//????????????
                String shopName = JsonUtil.getString(data, "shopName");//????????????
                String imagesPath = JsonUtil.getString(data, "imagesPath");//????????????
                String productName = JsonUtil.getString(data, "productName");//????????????
                String orderName = JsonUtil.getString(data, "orderName");//????????????
                String realAmt = JsonUtil.getString(data, "realAmt");//??????????????????
                String orderStatus = JsonUtil.getString(data, "orderStatus");//????????????
                String payType = JsonUtil.getString(data, "payType");//????????????
                String payStatus = JsonUtil.getString(data, "payStatus");//????????????
                String createTime = JsonUtil.getString(data, "createTime");//????????????
                String score = JsonUtil.getString(data, "score");//????????????
                String scoreAmt = JsonUtil.getString(data, "scoreAmt");//??????????????????
                String nickname = JsonUtil.getString(data, "nickname");//????????????
                String memberName = JsonUtil.getString(data, "memberName");//????????????
                String leaveMsg = JsonUtil.getString(data, "leaveMsg");//????????????
                String payTypeStr = JsonUtil.getString(data, "payTypeStr");
                String orderStatusStr = JsonUtil.getString(data, "orderStatusStr");
                String dueAmt = JsonUtil.getString(data, "dueAmt");//????????????

                ModelOrderDetail orderDetail = new ModelOrderDetail(orderNo, shopNo, originalPrice,
                        shopLogo, shopName, imagesPath, productName, orderName, realAmt, orderStatus,
                        payType, payStatus, createTime, score, scoreAmt, nickname, memberName, leaveMsg);
                orderDetail.setPayTypeStr(payTypeStr);
                orderDetail.setOrderStatusStr(orderStatusStr);
                orderDetail.setDueAmt(dueAmt);

                msg.what = success;
                msg.obj = orderDetail;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ????????????
     */
    public void parseShopProductByClassify(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                //
                Gson gson = new Gson();
                List<ModelProductDetail> list = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<ModelProductDetail>>() {
                }.getType());
                msg.obj = list;
                msg.what = success;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ??????????????????
     */
    public void parseShopProductClassify(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                //
                Gson gson = new Gson();
                List<ShopDetailClassifyBean> list = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<ShopDetailClassifyBean>>() {
                }.getType());
                msg.obj = list;
                msg.what = success;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ????????????
     */
    void parserShopComment(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        LinkedList<ModelGrade> mList = new LinkedList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONObject data = object.getJSONObject("data");
                msg.arg2 = JsonUtil.getInt(data, "total");
                JSONArray rows = data.getJSONArray("rows");
                for (int i = 0; i < rows.length(); i++) {
                    JSONObject row = rows.getJSONObject(i);
                    String id = JsonUtil.getString(row, "commentNo");
                    String name = JsonUtil.getString(row, "nickname");
                    String avatarImg = JsonUtil.getString(row, "headPath");
                    String grade = JsonUtil.getString(row, "discriptScore");
                    String describe = JsonUtil.getString(row, "content");
                    String imgPath = JsonUtil.getString(row, "imgPath");
                    long createTime = JsonUtil.getLong(row, "createTime");
                    String[] images = imgPath.split(",");
                    String image1 = "";
                    String image2 = "";
                    String image3 = "";
                    for (int j = 0; j < images.length; j++) {
                        switch (j) {
                            case 0:
                                image1 = images[0];
                                break;
                            case 1:
                                image2 = images[1];
                                break;
                            case 2:
                                image3 = images[2];
                                break;
                            default:
                                break;
                        }
                    }
                    String memberNo = JsonUtil.getString(row, "memberNo");
                    ModelGrade modelGrade = new ModelGrade(id, name, avatarImg, grade, describe, image1, image2, image3, memberNo);
                    modelGrade.setCreateTime(createTime);
                    mList.add(modelGrade);
                }

                msg.what = success;
                msg.obj = mList;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ????????????
     */
    void parserOrderSettlementList(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                Gson gson = new Gson();
                List<ModelSettlement> list = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<ModelSettlement>>() {
                }.getType());
                msg.obj = list;
                msg.what = success;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ??????????????????
     */
    void parserCreateNearbyOrder(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                String orderNo = JsonUtil.getString(object, "data");
                msg.what = success;
                msg.obj = orderNo;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ??????????????????
     */
    void parserShopCollect(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONArray rows = object.getJSONObject("data").getJSONArray("rows");

                LinkedList<ModelShop> mList = GsonUtils.getGson().fromJson(rows.toString(),
                        new TypeToken<LinkedList<ModelShop>>() {
                        }.getType());
                msg.what = success;
                msg.obj = mList;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ????????????-????????????????????????????????????????????????
     * hym
     */
    void parserQueryOrderList(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        LinkedList<ModelShopOrderList> mList = new LinkedList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                JSONObject data = object.getJSONObject("data");
                JSONArray array = data.getJSONArray("rows");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    LinkedList<ModelShopOrderListItem> modelShopOrderListItemList = new LinkedList<>();
                    String orderName = JsonUtil.getString(item, "orderName");
                    String orderNo = JsonUtil.getString(item, "orderNo");
                    String shopName = JsonUtil.getString(item, "shopName");
                    String shopNo = JsonUtil.getString(item, "shopNo");
                    String shopLogoPath = JsonUtil.getString(item, "shopLogoPath");
                    String refund = JsonUtil.getString(item, "refund");
                    String orderStatus = JsonUtil.getString(item, "orderStatus");
                    String score = JsonUtil.getString(item, "score");
                    String remark = JsonUtil.getString(item, "remark");
                    String sendInfo = JsonUtil.getString(item, "sendInfo");
                    long createTime = JsonUtil.getLong(item, "createTime");
                    String address = JsonUtil.getString(item, "address");
                    String receiver = JsonUtil.getString(item, "receiver");
                    String receiverMobile = JsonUtil.getString(item, "receiverMobile");
                    String logisticsNo = JsonUtil.getString(item, "logisticsNo");
                    String logisticsName = JsonUtil.getString(item, "logisticsName");
                    String logisticsType = JsonUtil.getString(item, "logisticsType");
                    String couponAmt = JsonUtil.getString(item, "couponAmt");
                    String realAmt = JsonUtil.getString(item, "realAmt");
                    String payType = JsonUtil.getString(item, "payType");
                    String payTypeStr = JsonUtil.getString(item, "payTypeStr");
                    String leaveMsg = JsonUtil.getString(item, "leaveMsg");
                    String logisticsFee = JsonUtil.getString(item, "logisticsFee");
                    String invoiceName = JsonUtil.getString(item, "invoiceName");
                    String isPlatno = JsonUtil.getString(item, "isPlatno");
                    String balance = JsonUtil.getString(item, "balance");

                    JSONArray array1 = item.getJSONArray("orderProductsList");
                    for (int j = 0; j < array1.length(); j++) {
                        JSONObject item2 = array1.getJSONObject(j);
                        String menuNo = JsonUtil.getString(item2, "menuNo");
                        String orderNo1 = JsonUtil.getString(item2, "orderNo");
                        String orderProductNo = JsonUtil.getString(item2, "orderProductNo");
                        String originalPrice = JsonUtil.getString(item2, "originalPrice");
                        String productColorSize = JsonUtil.getString(item2, "productColorSize");
                        String productImg = JsonUtil.getString(item2, "productImg");
                        String productName = JsonUtil.getString(item2, "productName");
                        String productNo = JsonUtil.getString(item2, "productNo");
                        String productNum = JsonUtil.getString(item2, "productNum");
                        String realAmt1 = JsonUtil.getString(item2, "realAmt");
                        String remindAmt = JsonUtil.getString(item2, "remindAmt");

                        ModelShopOrderListItem modelShopOrderListItem = new ModelShopOrderListItem(
                                menuNo, orderNo1, orderProductNo, originalPrice, productColorSize,
                                productImg, productName, productNo, productNum, realAmt1, remindAmt);
                        modelShopOrderListItem.setShopNo(shopNo);
                        modelShopOrderListItemList.add(modelShopOrderListItem);
                    }

                    JSONObject comment = item.isNull("comments") ? null : item.getJSONObject("comments");
                    String id = JsonUtil.getString(comment, "commentNo");// ????????????
                    String name = JsonUtil.getString(comment, "name");
                    String avatarImg = JsonUtil.getString(comment, "avatarImg");
                    String grade = JsonUtil.getString(comment, "discriptScore");
                    String describe = JsonUtil.getString(comment, "content");
                    String imgPath = JsonUtil.getString(comment, "imgPath");
                    String[] images = imgPath.split(",");
                    String image1 = images.length > 0 ? images[0] : "";
                    String image2 = images.length > 1 ? images[1] : "";
                    String image3 = images.length > 2 ? images[2] : "";
                    String memberNo = JsonUtil.getString(comment, "memberNo");
                    ModelGrade modelGrade = new ModelGrade(id, name, avatarImg, grade, describe, image1, image2, image3, memberNo);

                    ModelShopOrderList modelShopOrderList = new ModelShopOrderList(orderName, orderNo,
                            shopName, shopNo, shopLogoPath, refund, orderStatus, modelGrade, modelShopOrderListItemList);
                    modelShopOrderList.setScore(score);
                    modelShopOrderList.setRemark(remark);
                    modelShopOrderList.setSendInfo(sendInfo);
                    modelShopOrderList.setCreateTime(createTime);
                    modelShopOrderList.setAddress(address);
                    modelShopOrderList.setReceiver(receiver);
                    modelShopOrderList.setReceiverMobile(receiverMobile);
                    modelShopOrderList.setLogisticsNo(logisticsNo);
                    modelShopOrderList.setLogisticsType(logisticsType);
                    modelShopOrderList.setLogisticsName(logisticsName);
                    modelShopOrderList.setCouponAmt(couponAmt);
                    modelShopOrderList.setRealAmt(realAmt);
                    modelShopOrderList.setPayType(payType);
                    modelShopOrderList.setPayTypeStr(payTypeStr);
                    modelShopOrderList.setLeaveMsg(leaveMsg);
                    modelShopOrderList.setLogisticsFee(logisticsFee);
                    modelShopOrderList.setInvoiceName(invoiceName);
                    modelShopOrderList.setIsPlatno(isPlatno);
                    modelShopOrderList.setBalance(balance);
                    mList.add(modelShopOrderList);
                }

                msg.what = success;
                msg.obj = mList;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ????????????-????????????????????????
     */
    void parserMyShopOrderInfo(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        LinkedList<ShopOrderModel> mList = new LinkedList<>();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {

                JSONObject dataObj = object.getJSONObject("data");
                msg.arg2 = JsonUtil.getInt(dataObj, "total");
                JSONArray array = dataObj.getJSONArray("rows");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject data = array.getJSONObject(i);
                    String orderNo = JsonUtil.getString(data, "orderNo");//??????
                    String realPrice = JsonUtil.getString(data, "realPrice");//????????????
                    String orderStatus = JsonUtil.getString(data, "orderStatus");//???????????? new(?????????)???complete???????????????
                    String payStatus = JsonUtil.getString(data, "payStatus");// ????????????
                    String score = JsonUtil.getString(data, "score");//????????????
                    String address = JsonUtil.getString(data, "address");
                    String receiver = JsonUtil.getString(data, "receiver");
                    String receiverMobile = JsonUtil.getString(data, "receiverMobile");
                    long sendTime = JsonUtil.getLong(data, "sendTime");
                    int isDelivery = JsonUtil.getInt(data, "isDelivery");
                    String sendInfo = JsonUtil.getString(data, "sendInfo");
                    long createTime = JsonUtil.getLong(data, "createTime");
                    String isPlatno = JsonUtil.getString(data, "isPlatno");
                    String logisticsType = JsonUtil.getString(data, "logisticsType");
                    String logisticsName = JsonUtil.getString(data, "logisticsName");
                    String logisticsNo = JsonUtil.getString(data, "logisticsNo");
                    String payTypeStr = JsonUtil.getString(data, "payTypeStr");
                    String dueAmt = JsonUtil.getString(data, "dueAmt");
                    String leaveMsg = JsonUtil.getString(data, "leaveMsg");
                    String logisticsFee = JsonUtil.getString(data, "logisticsFee");
                    String couponAmt = JsonUtil.getString(data, "couponAmt");

                    JSONObject member = data.getJSONObject("member");
                    String member_memberNo = JsonUtil.getString(member, "memberNo");//???????????? ????????????
                    String member_memberName = JsonUtil.getString(member, "nickname");//???????????? ??????
                    String member_nickname = JsonUtil.getString(member, "nickname");//???????????? ??????
                    String member_mobileNo = JsonUtil.getString(member, "mobileNo");//?????????????????????
                    String member_headPath = JsonUtil.getString(member, "headPath");//?????? //????????????

                    JSONObject comment = data.isNull("comments") ? null : data.getJSONObject("comments");
                    String id = JsonUtil.getString(comment, "commentNo");// ????????????
                    String name = JsonUtil.getString(comment, "name");
                    String avatarImg = JsonUtil.getString(comment, "avatarImg");
                    String grade = JsonUtil.getString(comment, "discriptScore");
                    String describe = JsonUtil.getString(comment, "content");
                    String imgPath = JsonUtil.getString(comment, "imgPath");
                    String[] images = imgPath.split(",");
                    String image1 = images.length > 0 ? images[0] : "";
                    String image2 = images.length > 1 ? images[1] : "";
                    String image3 = images.length > 2 ? images[2] : "";
                    String memberNo = JsonUtil.getString(comment, "memberNo");
                    ModelGrade modelGrade = new ModelGrade(id, name, avatarImg, grade, describe, image1, image2, image3, memberNo);

                    LinkedList<ModelOrderGoods> goodsModels = new LinkedList<>();//????????????
                    JSONArray orderProductsList = data.getJSONArray("orderProductsList");
                    if (orderProductsList != null) {
                        for (int j = 0; j < orderProductsList.length(); j++) {
                            JSONObject product = orderProductsList.getJSONObject(j);
                            String orderProductNo = JsonUtil.getString(product, "orderProductNo");//??????
                            String productName = JsonUtil.getString(product, "productName");//??????
                            String realAmt = JsonUtil.getString(product, "realAmt");//??????
                            String productNum = JsonUtil.getString(product, "productNum");//??????

                            String productImg = JsonUtil.getString(product, "productImg");//??????
                            String[] image = productImg.split(",");
                            productImg = image.length > 0 ? image[0] : "";
                            int type = 1;// 1??????, 2??????
                            ModelOrderGoods modelOrderGoods = new ModelOrderGoods(orderProductNo, productName, realAmt, productImg, type);
                            modelOrderGoods.setProductNum(productNum);
                            goodsModels.add(modelOrderGoods);
                        }
                    }

                    ShopOrderModel shopOrderModel = new ShopOrderModel(orderNo, realPrice, orderStatus,
                            payStatus, score, "", "",
                            "", member_memberNo, member_memberName,
                            member_nickname, member_mobileNo, member_headPath, modelGrade, goodsModels);
                    shopOrderModel.setAddress(address);
                    shopOrderModel.setReceiver(receiver);
                    shopOrderModel.setReceiverMobile(receiverMobile);
                    shopOrderModel.setSendTime(sendTime);
                    shopOrderModel.setIsDelivery(isDelivery);
                    shopOrderModel.setSendInfo(sendInfo);
                    shopOrderModel.setCreateTime(createTime);
                    shopOrderModel.setIsPlatno(isPlatno);
                    shopOrderModel.setLogisticsNo(logisticsNo);
                    shopOrderModel.setLogisticsType(logisticsType);
                    shopOrderModel.setLogisticsName(logisticsName);
                    shopOrderModel.setPayTypeStr(payTypeStr);
                    shopOrderModel.setDueAmt(dueAmt);
                    shopOrderModel.setLeaveMsg(leaveMsg);
                    shopOrderModel.setLogisticsFee(logisticsFee);
                    shopOrderModel.setCouponAmt(couponAmt);

                    mList.add(shopOrderModel);
                }

                msg.what = success;
                msg.obj = mList;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }


    /**
     * ?????????????????????
     */
    public void parseParentChildCircleList(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                Gson gson = new Gson();
                List<ParentChildCircleDetail> list = gson.fromJson(object.getJSONObject("data").getJSONArray("rows").toString(), new TypeToken<List<ParentChildCircleDetail>>() {
                }.getType());

                msg.what = success;
                msg.obj = list;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ???????????????
     */
    public void parseParentChildCircleDetail(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                msg.obj = new Gson().fromJson(object.getJSONObject("data").toString(), ParentChildCircleDetail.class);
                msg.what = success;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }

    /**
     * ?????????????????????
     */
    public void parseParentChildCircleDiscussList(String response, Handler mHandler, int success, int fail) {
        Message msg = mHandler.obtainMessage();
        try {
            JSONObject object = new JSONObject(response);
            if (checkJSONStatus(object, TYPE_NORMAL)) {
                //
                Gson gson = new Gson();
                List<ParentChildDiscussBean> list = gson.fromJson(object.getJSONObject("data").getJSONArray("rows").toString(), new TypeToken<List<ParentChildDiscussBean>>() {
                }.getType());
                msg.obj = list;
                msg.what = success;
            } else {
                msg = getFailMessage(msg, object, fail);
            }
        } catch (Exception e) {
            msg = getExceptionMessage(msg, fail);
            e.printStackTrace();
        }
        mHandler.sendMessage(msg);
    }


}
