package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/4/27.
 */

public class OauthLogin {
    @SerializedName("account_number")
    private String accountNumber;
    @SerializedName("pass_word")
    private String password;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
