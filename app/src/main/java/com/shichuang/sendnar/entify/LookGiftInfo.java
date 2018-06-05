package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/5/8.
 */

public class LookGiftInfo {

    private List<LookGiftInfoModel> rows;
    private int recordCount;

    public List<LookGiftInfoModel> getRows() {
        return rows;
    }

    public void setRows(List<LookGiftInfoModel> rows) {
        this.rows = rows;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public static class LookGiftInfoModel{
        private int id;
        @SerializedName("goods_id")
        private int goodsId;
        private String pic;
        @SerializedName("shop_name")
        private String shopName;
        private String labels;
        @SerializedName("receive_time")
        private String receiveTime;
        private String nickname;
        @SerializedName("sale_price")
        private String salePrice;
        @SerializedName("receive_gift_id")
        private int receiveGiftId;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
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

        public String getLabels() {
            return labels;
        }

        public void setLabels(String labels) {
            this.labels = labels;
        }

        public String getReceiveTime() {
            return receiveTime;
        }

        public void setReceiveTime(String receiveTime) {
            this.receiveTime = receiveTime;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSalePrice() {
            return salePrice;
        }

        public void setSalePrice(String salePrice) {
            this.salePrice = salePrice;
        }

        public int getReceiveGiftId() {
            return receiveGiftId;
        }

        public void setReceiveGiftId(int receiveGiftId) {
            this.receiveGiftId = receiveGiftId;
        }
    }
}
