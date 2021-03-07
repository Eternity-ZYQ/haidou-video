package com.yimeng.entity;

import java.util.LinkedList;

/**
 * Created by user on 2018/7/3.
 */

public class ModelComment {

    private String totalGrade;
    private LinkedList<ModelRepairOrderCommentDetail> modelRepairOrderCommentDetailList;

    public ModelComment(String totalGrade, LinkedList<ModelRepairOrderCommentDetail> modelRepairOrderCommentDetailList) {
        this.totalGrade = totalGrade;
        this.modelRepairOrderCommentDetailList = modelRepairOrderCommentDetailList;
    }

    public String getTotalGrade() {
        return totalGrade;
    }

    public void setTotalGrade(String totalGrade) {
        this.totalGrade = totalGrade;
    }

    public LinkedList<ModelRepairOrderCommentDetail> getModelRepairOrderCommentDetailList() {
        return modelRepairOrderCommentDetailList;
    }

    public void setModelRepairOrderCommentDetailList(LinkedList<ModelRepairOrderCommentDetail> modelRepairOrderCommentDetailList) {
        this.modelRepairOrderCommentDetailList = modelRepairOrderCommentDetailList;
    }
}
