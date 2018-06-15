package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/4/28.
 */

public class OrderDetails {
    @SerializedName("shop_goods_order_detailModellist")
    private List<OrderDetailsGoodsListModel> orderDetailsGoodsList;
    @SerializedName("shop_goods_order")
    private OrderDetailsModel orderDetailsModel;
    @SerializedName("type")
    private String invoiceType;
    @SerializedName("head")
    private String invoiceHead;
    @SerializedName("content")
    private String invoiceContent;
    // 0 普通商品，1送礼未接收，2送礼已接收
    @SerializedName("song")
    private int sendGiftType;

    public List<OrderDetailsGoodsListModel> getOrderDetailsGoodsList() {
        return orderDetailsGoodsList;
    }

    public void setOrderDetailsGoodsList(List<OrderDetailsGoodsListModel> orderDetailsGoodsList) {
        this.orderDetailsGoodsList = orderDetailsGoodsList;
    }

    public OrderDetailsModel getOrderDetailsModel() {
        return orderDetailsModel;
    }

    public void setOrderDetailsModel(OrderDetailsModel orderDetailsModel) {
        this.orderDetailsModel = orderDetailsModel;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceHead() {
        return invoiceHead;
    }

    public void setInvoiceHead(String invoiceHead) {
        this.invoiceHead = invoiceHead;
    }

    public String getInvoiceContent() {
        return invoiceContent;
    }

    public void setInvoiceContent(String invoiceContent) {
        this.invoiceContent = invoiceContent;
    }

    public int getSendGiftType() {
        return sendGiftType;
    }

    public void setSendGiftType(int sendGiftType) {
        this.sendGiftType = sendGiftType;
    }

    public static class OrderDetailsGoodsListModel implements Serializable{
        @SerializedName("sort_name")
        private String sortName;
        @SerializedName("sale_price")
        private String salePrice;
        private String pic;
        @SerializedName("buy_count")
        private int buyCount;
        @SerializedName("order_detail_id")
        private int orderDetailId;
        @SerializedName("refund_status")
        private int refundStatus;
        @SerializedName("need_return")
        private int needReturn;

        public String getSortName() {
            return sortName;
        }

        public void setSortName(String sortName) {
            this.sortName = sortName;
        }

        public String getSalePrice() {
            return salePrice;
        }

        public void setSalePrice(String salePrice) {
            this.salePrice = salePrice;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public int getBuyCount() {
            return buyCount;
        }

        public void setBuyCount(int buyCount) {
            this.buyCount = buyCount;
        }

        public int getOrderDetailId() {
            return orderDetailId;
        }

        public void setOrderDetailId(int orderDetailId) {
            this.orderDetailId = orderDetailId;
        }

        public int getRefundStatus() {
            return refundStatus;
        }

        public void setRefundStatus(int refundStatus) {
            this.refundStatus = refundStatus;
        }

        public int getNeedReturn() {
            return needReturn;
        }

        public void setNeedReturn(int needReturn) {
            this.needReturn = needReturn;
        }
    }

    public static class OrderDetailsModel {
        @SerializedName("order_no")
        private String orderNo;
        @SerializedName("order_status")
        private int orderStatus;
        @SerializedName("invoice_type")
        private int invoiceType;
        @SerializedName("order_amount_total")  // 订单总额
        private String orderAmountTotal;
        @SerializedName("actual_amount")  // 实际金额（或者已付款金额）
        private String actualAmount;
        @SerializedName("create_time")
        private String createTime;
        @SerializedName("pay_time")
        private String payTime;
        @SerializedName("receive_man")
        private String receiveMan;
        @SerializedName("contact_phone")
        private String contactPhone;
        @SerializedName("receive_address")
        private String receiveAddress;
        @SerializedName("consumption_points")
        private String consumptionPoints;
        @SerializedName("send_out_time")
        private String sendOutTime;
        @SerializedName("closing_time")
        private String closingTime;
        @SerializedName("express_no")
        private String expressNo;

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }

        public int getInvoiceType() {
            return invoiceType;
        }

        public void setInvoiceType(int invoiceType) {
            this.invoiceType = invoiceType;
        }

        public String getOrderAmountTotal() {
            return orderAmountTotal;
        }

        public void setOrderAmountTotal(String orderAmountTotal) {
            this.orderAmountTotal = orderAmountTotal;
        }

        public String getActualAmount() {
            return actualAmount;
        }

        public void setActualAmount(String actualAmount) {
            this.actualAmount = actualAmount;
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

        public String getReceiveMan() {
            return receiveMan;
        }

        public void setReceiveMan(String receiveMan) {
            this.receiveMan = receiveMan;
        }

        public String getContactPhone() {
            return contactPhone;
        }

        public void setContactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
        }

        public String getReceiveAddress() {
            return receiveAddress;
        }

        public void setReceiveAddress(String receiveAddress) {
            this.receiveAddress = receiveAddress;
        }

        public String getConsumptionPoints() {
            return consumptionPoints;
        }

        public void setConsumptionPoints(String consumptionPoints) {
            this.consumptionPoints = consumptionPoints;
        }

        public String getSendOutTime() {
            return sendOutTime;
        }

        public void setSendOutTime(String sendOutTime) {
            this.sendOutTime = sendOutTime;
        }

        public String getClosingTime() {
            return closingTime;
        }

        public void setClosingTime(String closingTime) {
            this.closingTime = closingTime;
        }

        public String getExpressNo() {
            return expressNo;
        }

        public void setExpressNo(String expressNo) {
            this.expressNo = expressNo;
        }
    }

}
