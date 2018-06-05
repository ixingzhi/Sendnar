package com.shichuang.sendnar.entify;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/4/28.
 */

public class WithdrawalRecord implements MultiItemEntity {
    public static final int WITHDRAWAL_HEADER = 0X11;
    public static final int WITHDRAWAL_BODY = 0X12;
    private int itemType;
    private String time;
    private List<MonthWithdrawalData> monthData;
    // 标识位置
    private int position;

    public WithdrawalRecord(int itemType){
        this.itemType = itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<MonthWithdrawalData> getMonthData() {
        return monthData;
    }

    public void setMonthData(List<MonthWithdrawalData> monthData) {
        this.monthData = monthData;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public static class MonthWithdrawalData{
        private String date;
        private String month;
        @SerializedName("sucess_time")
        private String sucessTime;
        @SerializedName("TXinfo")
        private String withdrawInfo;
        @SerializedName("withdraw_num")
        private String withdrawAmount;


        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getSucessTime() {
            return sucessTime;
        }

        public void setSucessTime(String sucessTime) {
            this.sucessTime = sucessTime;
        }

        public String getWithdrawInfo() {
            return withdrawInfo;
        }

        public void setWithdrawInfo(String withdrawInfo) {
            this.withdrawInfo = withdrawInfo;
        }

        public String getWithdrawAmount() {
            return withdrawAmount;
        }

        public void setWithdrawAmount(String withdrawAmount) {
            this.withdrawAmount = withdrawAmount;
        }

    }
}
