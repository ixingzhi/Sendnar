package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */

public class PointList {

    private List<PointListModel> rows;
    private int recordCount;

    public List<PointListModel> getRows() {
        return rows;
    }

    public void setRows(List<PointListModel> rows) {
        this.rows = rows;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public static class PointListModel{
        @SerializedName("point_description")
        private String pointDescription;  // 描述
        @SerializedName("trade_type")
        private int tradeType;   // 交易方式(收入=1|支出=2)
        @SerializedName("trade_describe")
        private String tradeDescribe;   // 交易类型描述(类似于标签)
        @SerializedName("point_trade_time")
        private String pointTradeTime;
        private String point;
        private boolean isOpen;  // 是否点击打开

        public String getPointDescription() {
            return pointDescription;
        }

        public void setPointDescription(String pointDescription) {
            this.pointDescription = pointDescription;
        }

        public int getTradeType() {
            return tradeType;
        }

        public void setTradeType(int tradeType) {
            this.tradeType = tradeType;
        }

        public String getTradeDescribe() {
            return tradeDescribe;
        }

        public void setTradeDescribe(String tradeDescribe) {
            this.tradeDescribe = tradeDescribe;
        }

        public String getPointTradeTime() {
            return pointTradeTime;
        }

        public void setPointTradeTime(String pointTradeTime) {
            this.pointTradeTime = pointTradeTime;
        }

        public String getPoint() {
            return point;
        }

        public void setPoint(String point) {
            this.point = point;
        }

        public boolean isOpen() {
            return isOpen;
        }

        public void setOpen(boolean open) {
            isOpen = open;
        }
    }
}
