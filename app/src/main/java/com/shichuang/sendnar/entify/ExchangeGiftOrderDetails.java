package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

public class ExchangeGiftOrderDetails {
    @SerializedName("is_exchangeOrder")
    private int isExchangeOrder;
    private ExchangeOrder exchangeOrder;

    public int getIsExchangeOrder() {
        return isExchangeOrder;
    }

    public void setIsExchangeOrder(int isExchangeOrder) {
        this.isExchangeOrder = isExchangeOrder;
    }

    public ExchangeOrder getExchangeOrder() {
        return exchangeOrder;
    }

    public void setExchangeOrder(ExchangeOrder exchangeOrder) {
        this.exchangeOrder = exchangeOrder;
    }

    public static class ExchangeOrder {
        private int id;
        @SerializedName("user_id")
        private int userId;
        @SerializedName("a_goods_name")
        private String originalGoodsName;
        @SerializedName("a_goods_pic")
        private String originalGoodsPic;
        @SerializedName("a_price")
        private String originalGoodsPrice;
        @SerializedName("b_goods_name")
        private String newGoodsName;
        @SerializedName("b_goods_pic")
        private String newGoodsPic;
        @SerializedName("b_price")
        private String newGoodsPrice;
        @SerializedName("record_id")
        private int recordId;
        @SerializedName("change_type")
        private int changeType;
        @SerializedName("price_difference")
        private String priceDifference;
        @SerializedName("order_id")
        private int orderId;
        @SerializedName("old_order_id")
        private int oldOrderId;
        private int status;
        @SerializedName("service_charge")
        private String serviceCharge;
        @SerializedName("is_delete")
        private int isDelete;
        @SerializedName("a_purpose")
        private String originalGoodsPurpose;
        @SerializedName("order_no")
        private String orderNo;
        @SerializedName("create_time")
        private String createTime;
        @SerializedName("pay_time")
        private String payTime;
        @SerializedName("closing_time")
        private String closingTime;
        @SerializedName("order_status")
        private int orderStatus;
        @SerializedName("consumption_points")
        private String consumptionPoints;
        @SerializedName("actual_amount")
        private String actualAmount;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getOriginalGoodsName() {
            return originalGoodsName;
        }

        public void setOriginalGoodsName(String originalGoodsName) {
            this.originalGoodsName = originalGoodsName;
        }

        public String getOriginalGoodsPic() {
            return originalGoodsPic;
        }

        public void setOriginalGoodsPic(String originalGoodsPic) {
            this.originalGoodsPic = originalGoodsPic;
        }

        public String getOriginalGoodsPrice() {
            return originalGoodsPrice;
        }

        public void setOriginalGoodsPrice(String originalGoodsPrice) {
            this.originalGoodsPrice = originalGoodsPrice;
        }

        public String getNewGoodsName() {
            return newGoodsName;
        }

        public void setNewGoodsName(String newGoodsName) {
            this.newGoodsName = newGoodsName;
        }

        public String getNewGoodsPic() {
            return newGoodsPic;
        }

        public void setNewGoodsPic(String newGoodsPic) {
            this.newGoodsPic = newGoodsPic;
        }

        public String getNewGoodsPrice() {
            return newGoodsPrice;
        }

        public void setNewGoodsPrice(String newGoodsPrice) {
            this.newGoodsPrice = newGoodsPrice;
        }

        public int getRecordId() {
            return recordId;
        }

        public void setRecordId(int recordId) {
            this.recordId = recordId;
        }

        public int getChangeType() {
            return changeType;
        }

        public void setChangeType(int changeType) {
            this.changeType = changeType;
        }

        public String getPriceDifference() {
            return priceDifference;
        }

        public void setPriceDifference(String priceDifference) {
            this.priceDifference = priceDifference;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public int getOldOrderId() {
            return oldOrderId;
        }

        public void setOldOrderId(int oldOrderId) {
            this.oldOrderId = oldOrderId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getServiceCharge() {
            return serviceCharge;
        }

        public void setServiceCharge(String serviceCharge) {
            this.serviceCharge = serviceCharge;
        }

        public int getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(int isDelete) {
            this.isDelete = isDelete;
        }

        public String getOriginalGoodsPurpose() {
            return originalGoodsPurpose;
        }

        public void setOriginalGoodsPurpose(String originalGoodsPurpose) {
            this.originalGoodsPurpose = originalGoodsPurpose;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getPayTime() {
            return payTime;
        }

        public void setPayTime(String payTime) {
            this.payTime = payTime;
        }

        public String getClosingTime() {
            return closingTime;
        }

        public void setClosingTime(String closingTime) {
            this.closingTime = closingTime;
        }

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getConsumptionPoints() {
            return consumptionPoints;
        }

        public void setConsumptionPoints(String consumptionPoints) {
            this.consumptionPoints = consumptionPoints;
        }

        public String getActualAmount() {
            return actualAmount;
        }

        public void setActualAmount(String actualAmount) {
            this.actualAmount = actualAmount;
        }
    }

}
