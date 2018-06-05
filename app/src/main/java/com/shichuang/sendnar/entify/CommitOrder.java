package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/5/2.
 */

public class CommitOrder implements Serializable{

    private int count;
    @SerializedName("actual_amount")
    private String actualAmount;
    @SerializedName("order_no")
    private String orderNo;
    private Goods goods;
    @SerializedName("yesORno")
    private int isPay; // 1跳转支付，2直接支付成功
    // 购物车
    private List<Goods> goodsList;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(String actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public int getIsPay() {
        return isPay;
    }

    public void setIsPay(int isPay) {
        this.isPay = isPay;
    }

    public static class Goods implements Serializable{
        private int id;
        @SerializedName("shop_name")
        private String shopName;
        private String pic;
        @SerializedName("market_price")
        private String marketPrice;
        @SerializedName("sale_price")
        private String salePrice;
        private String XIAOJI;
        @SerializedName("goods_cart_counts")
        private int count;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(String marketPrice) {
            this.marketPrice = marketPrice;
        }

        public String getSalePrice() {
            return salePrice;
        }

        public void setSalePrice(String salePrice) {
            this.salePrice = salePrice;
        }

        public String getXIAOJI() {
            return XIAOJI;
        }

        public void setXIAOJI(String XIAOJI) {
            this.XIAOJI = XIAOJI;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

}
