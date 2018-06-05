package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/24.
 */

public class ApplyWithdrawals implements Serializable{
    @SerializedName("service_charge")
    private String serviceCharge;  // 手续费
    private String rate;  // 税率及手续费率
    private String withdrawAmount;  // 申请提现金额

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(String withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }
}
