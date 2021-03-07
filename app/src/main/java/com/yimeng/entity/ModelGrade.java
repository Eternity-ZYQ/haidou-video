package com.yimeng.entity;

import java.io.Serializable;

/**
 * 评价信息
 *
 * @author xp
 * @describe 评价信息.
 * @date 2018/6/12.
 */

public class ModelGrade implements Serializable {

    private String id;// 用户编号
    private String name;
    private String avatarImg;
    private String grade;
    private String describe;
    private String image1;
    private String image2;
    private String image3;
    private String memberNo;
    private long createTime;

    public ModelGrade(String id, String name, String avatarImg, String grade, String describe, String image1, String image2, String image3, String memberNo) {
        this.id = id;
        this.name = name;
        this.avatarImg = avatarImg;
        this.grade = grade;
        this.describe = describe;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.memberNo = memberNo;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarImg() {
        return avatarImg;
    }

    public void setAvatarImg(String avatarImg) {
        this.avatarImg = avatarImg;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
