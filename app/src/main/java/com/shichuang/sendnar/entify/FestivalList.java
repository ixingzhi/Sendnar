package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */

public class FestivalList {

    private List<FestivalListModel> rows;
    private int recordCount;

    public List<FestivalListModel> getRows() {
        return rows;
    }

    public void setRows(List<FestivalListModel> rows) {
        this.rows = rows;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public static class FestivalListModel {
        @SerializedName("A_id")  //   (礼品)
        private int id;
        @SerializedName("goods_id")
        private int goodsId;
        private String pic;
        @SerializedName("promotion_price")
        private String promotionPrice;   // 商品活动价
        @SerializedName("shop_name")
        private String shopName;
        private String labels;  // 商品标签

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

        public String getPromotionPrice() {
            return promotionPrice;
        }

        public void setPromotionPrice(String promotionPrice) {
            this.promotionPrice = promotionPrice;
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

    }
}
