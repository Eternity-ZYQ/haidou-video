package com.yimeng.entity;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * 转换数据
 *
 * @author xp
 * @describe 转换数据.
 * @date 2018/4/26.
 */

public class ModelSwitchData implements Serializable {

    private LinkedList<GoodsModel> mGoodsList;
    private LinkedList<ModelSimple> mSimpleList;
    private LinkedList<ModelProductParams> mProductParamsList;

    public ModelSwitchData() {
    }

    public LinkedList<ModelProductParams> getmProductParamsList() {
        return mProductParamsList;
    }

    public void setmProductParamsList(LinkedList<ModelProductParams> mProductParamsList) {
        this.mProductParamsList = mProductParamsList;
    }

    public ModelSwitchData(LinkedList<GoodsModel> mGoodsList) {
        this.mGoodsList = mGoodsList;
    }

    public LinkedList<ModelSimple> getmSimpleList() {
        return mSimpleList;
    }

    public void setmSimpleList(LinkedList<ModelSimple> mSimpleList) {
        this.mSimpleList = mSimpleList;
    }

    public LinkedList<GoodsModel> getmGoodsList() {
        return mGoodsList;
    }

    public void setmGoodsList(LinkedList<GoodsModel> mGoodsList) {
        this.mGoodsList = mGoodsList;
    }
}
