package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/4/28.
 */

public class User {
    @SerializedName("head_portrait")
    private String headPortrait;
    // 0 消费者 1发起人 2 合伙人
    @SerializedName("identity_info")
    private int identityInfo;
    private String nickname;
    private String birthday;
    private String phone;
    @SerializedName("user_id")
    private int userId;

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public int getIdentityInfo() {
        return identityInfo;
    }

    public void setIdentityInfo(int identityInfo) {
        this.identityInfo = identityInfo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
