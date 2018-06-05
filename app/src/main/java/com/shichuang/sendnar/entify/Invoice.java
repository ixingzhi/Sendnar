package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/10.
 */

public class Invoice implements Serializable{
    private int id;
    private int type;   // 1普通发票 || 2电子
    private int head;    // 1 个人 || 2 单位
    @SerializedName("company_name")
    private String companyName;  // 公司名 （head=2时 必传 ）
    @SerializedName("user_code")
    private String userCode;  // 纳税识别码 （head=2时 必传 ）
    @SerializedName("goods_type")
    private int goodsType;  // 1 明细 || 2 类别
    private String email;
    @SerializedName("phone_num")
    private String phoneNum;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
