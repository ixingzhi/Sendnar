package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiedongdong on 2018/6/21.
 */

public class OrderReceiveAddress {
    @SerializedName("receive_address")
    private String receiveAddress;
    private String nickname;
    private String phone;

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
