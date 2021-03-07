package com.yimeng.entity;

import android.text.TextUtils;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/11/19 0019 下午 02:33.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class OrderCommentsBean {
    private String orderNo;
    private String productImg;//商品图片
    private String productName;//"商品000006"
    private String productNo;//"201806251552102727525"
    private int start; // 星级
    private String commentStr; // 评论内容
    private String commentPic1, commentPic2, commentPic3; // 评论图片

    public OrderCommentsBean() {
    }

    public OrderCommentsBean(String orderNo, String productImg, String productName, String productNo) {
        this.orderNo = orderNo;
        this.productImg = productImg;
        this.productName = productName;
        this.productNo = productNo;
        start = 5;
    }

    public String getOrderNo() {
        return orderNo == null ? "" : orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getProductImg() {
        return productImg == null ? "" : productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getProductName() {
        return productName == null ? "" : productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNo() {
        return productNo == null ? "" : productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getCommentStr() {
        return commentStr == null ? "" : commentStr;
    }

    public void setCommentStr(String commentStr) {
        this.commentStr = commentStr;
    }

    public String getCommentPic1() {
        return commentPic1 == null ? "" : commentPic1;
    }

    public void setCommentPic1(String commentPic1) {
        this.commentPic1 = commentPic1;
    }

    public String getCommentPic2() {
        return commentPic2 == null ? "" : commentPic2;
    }

    public void setCommentPic2(String commentPic2) {
        this.commentPic2 = commentPic2;
    }

    public String getCommentPic3() {
        return commentPic3 == null ? "" : commentPic3;
    }

    public void setCommentPic3(String commentPic3) {
        this.commentPic3 = commentPic3;
    }

    @Override
    public String toString() {
        return "{" +
                "\"discriptScore\":\"" + start + "\"," +
                "\"content\":\"" + commentStr + "\"," +
                "\"imgPath\":\"" + (TextUtils.isEmpty(commentPic1) ? "" : commentPic1 + ",") + (TextUtils.isEmpty(commentPic2) ? "" : commentPic2 + ",") + (TextUtils.isEmpty(commentPic3) ? "" : commentPic3 + ",") + "\"," +
                "\"repeatNickname\":\"" + productNo + "\"" +
                "}";
    }
}
