package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/5/2.
 */

public class ExchangeGiftConfirmOrder {

    private int id;
    private String point;
    @SerializedName("receive_gift_id")
    private int receiveGiftId;
    @SerializedName("consumption_points")
    private String consumptionPoints;
    @SerializedName("actual_amount")
    private String actualAmount;
    @SerializedName("a_purpose")
    private String purpose;
    @SerializedName("goods_A_Row")
    private Goods originalGood;
    @SerializedName("goods_B_Row")
    private Goods newGoods;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public int getReceiveGiftId() {
        return receiveGiftId;
    }

    public void setReceiveGiftId(int receiveGiftId) {
        this.receiveGiftId = receiveGiftId;
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

    public Goods getOriginalGood() {
        return originalGood;
    }

    public void setOriginalGood(Goods originalGood) {
        this.originalGood = originalGood;
    }

    public Goods getNewGoods() {
        return newGoods;
    }

    public void setNewGoods(Goods newGoods) {
        this.newGoods = newGoods;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public static class Goods {
        private int id;
        private String pic;
        @SerializedName("shop_name")
        private String shopName;
        @SerializedName("sale_price")
        private String salePrice;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getSalePrice() {
            return salePrice;
        }

        public void setSalePrice(String salePrice) {
            this.salePrice = salePrice;
        }

    }
}
