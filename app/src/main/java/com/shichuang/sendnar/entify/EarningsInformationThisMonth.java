package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

/**
 * 本月收益信息
 * Created by Administrator on 2018/5/21.
 */

public class EarningsInformationThisMonth {
    private String firstDay;
    private String startDay;
    private String today;
    @SerializedName("zongjine")
    private TotalAmount totalAmount;

    public String getFirstDay() {
        return firstDay;
    }

    public void setFirstDay(String firstDay) {
        this.firstDay = firstDay;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public TotalAmount getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(TotalAmount totalAmount) {
        this.totalAmount = totalAmount;
    }

    public static class TotalAmount{
        @SerializedName("SUMshouyi")
        private String totalRevenue;
        @SerializedName("SUMshouyi_yue")
        private String thisMonthRevenue;

        public String getTotalRevenue() {
            return totalRevenue;
        }

        public void setTotalRevenue(String totalRevenue) {
            this.totalRevenue = totalRevenue;
        }

        public String getThisMonthRevenue() {
            return thisMonthRevenue;
        }

        public void setThisMonthRevenue(String thisMonthRevenue) {
            this.thisMonthRevenue = thisMonthRevenue;
        }
    }
}
