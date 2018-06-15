package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/6/13.
 */

public class RefundInfo {
    private int id;
    @SerializedName("dispose_no")
    private String disposeNo;
    @SerializedName("is_receive")
    private int isReceive;  // 是否收到货1收到 2未收到
    @SerializedName("need_return")
    private int needReturn;  // 是否需要发货 1 需要退货 2 不需要退货 3 换货
    @SerializedName("sort_name")
    private String sortName;
    private String pic;
    @SerializedName("shop_goods_id")
    private int shopGoodsId;
    @SerializedName("submit_time")
    private String submitTime;
    @SerializedName("agree_refund_time")
    private String agreeRefundTime;  // 卖家同意退款/退货时间
    @SerializedName("refuse_refund_time")
    private String refuseRefundTime;  // 卖家拒绝退款/退货时间
    @SerializedName("returned_time")
    private String returnedTime;  // 买家已退货时间(买家发货时间)
    @SerializedName("buyer_submit_time")
    private String buyerSubmitTime;  // 买家提交时间
    @SerializedName("buyer_cancel_time")
    private String buyerCancelTime;  // 买家取消时间
    @SerializedName("return_reason")
    private String returnReason;  // 退款原因name
    @SerializedName("refund_amount")
    private String refundAmount;  // 申请退款的金额(元))
    @SerializedName("handle_remark")
    private String handleRemark;  // 处理理由
    private int status;   // 待卖家处理=1|卖家拒绝退款/退货/换货=2|待买家退货=3|买家已退货,等待卖家确认收货=4|卖家已退款=5|待财务打款=6|待卖家发货=7|已发货=8|买家取消=9|买家换货确认收货=10
    @SerializedName("express_company")
    private String expressCompany;
    @SerializedName("express_no")
    private String expressNo;
    @SerializedName("return_type")
    private int returnType;  // 账户类型（1=银行卡 2=支付宝 3=微信 4=积分）
    @SerializedName("refund_point")
    private String refundPoint;  // 实际退款的积分(元))
    @SerializedName("refund_money")
    private String refundMoney;  // 实际退款的现金(元))
    @SerializedName("platform_express_company")
    private String platformExpressCompany;  // 平台快递公司
    @SerializedName("platform_express_no")
    private String platformExpressNo;  // 平台快递编号
    @SerializedName("refund_time")
    private String refundTime;  // 退款时间
    private String com;
    @SerializedName("platform_com")
    private String platformCom;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisposeNo() {
        return disposeNo;
    }

    public void setDisposeNo(String disposeNo) {
        this.disposeNo = disposeNo;
    }

    public int getIsReceive() {
        return isReceive;
    }

    public void setIsReceive(int isReceive) {
        this.isReceive = isReceive;
    }

    public int getNeedReturn() {
        return needReturn;
    }

    public void setNeedReturn(int needReturn) {
        this.needReturn = needReturn;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getShopGoodsId() {
        return shopGoodsId;
    }

    public void setShopGoodsId(int shopGoodsId) {
        this.shopGoodsId = shopGoodsId;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getAgreeRefundTime() {
        return agreeRefundTime;
    }

    public void setAgreeRefundTime(String agreeRefundTime) {
        this.agreeRefundTime = agreeRefundTime;
    }

    public String getRefuseRefundTime() {
        return refuseRefundTime;
    }

    public void setRefuseRefundTime(String refuseRefundTime) {
        this.refuseRefundTime = refuseRefundTime;
    }

    public String getReturnedTime() {
        return returnedTime;
    }

    public void setReturnedTime(String returnedTime) {
        this.returnedTime = returnedTime;
    }

    public String getBuyerSubmitTime() {
        return buyerSubmitTime;
    }

    public void setBuyerSubmitTime(String buyerSubmitTime) {
        this.buyerSubmitTime = buyerSubmitTime;
    }

    public String getBuyerCancelTime() {
        return buyerCancelTime;
    }

    public void setBuyerCancelTime(String buyerCancelTime) {
        this.buyerCancelTime = buyerCancelTime;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getHandleRemark() {
        return handleRemark;
    }

    public void setHandleRemark(String handleRemark) {
        this.handleRemark = handleRemark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public int getReturnType() {
        return returnType;
    }

    public void setReturnType(int returnType) {
        this.returnType = returnType;
    }

    public String getRefundPoint() {
        return refundPoint;
    }

    public void setRefundPoint(String refundPoint) {
        this.refundPoint = refundPoint;
    }

    public String getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(String refundMoney) {
        this.refundMoney = refundMoney;
    }

    public String getPlatformExpressCompany() {
        return platformExpressCompany;
    }

    public void setPlatformExpressCompany(String platformExpressCompany) {
        this.platformExpressCompany = platformExpressCompany;
    }

    public String getPlatformExpressNo() {
        return platformExpressNo;
    }

    public void setPlatformExpressNo(String platformExpressNo) {
        this.platformExpressNo = platformExpressNo;
    }

    public String getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getPlatformCom() {
        return platformCom;
    }

    public void setPlatformCom(String platformCom) {
        this.platformCom = platformCom;
    }
}
