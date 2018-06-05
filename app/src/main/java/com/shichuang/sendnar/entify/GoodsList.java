package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */

public class GoodsList {

    private List<GoodsListModel> rows;
    private int recordCount;

    public List<GoodsListModel> getRows() {
        return rows;
    }

    public void setRows(List<GoodsListModel> rows) {
        this.rows = rows;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public static class GoodsListModel {
        //@SerializedName("A_id")   (礼品)
        private int id;
        private String pic;
        @SerializedName("promotion_price")
        private String promotionPrice;   // 商品活动价
        @SerializedName("shop_name")
        private String shopName;
        private String labels;  // 商品标签
        @SerializedName("market_price")
        private String marketPrice;
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
    }
}
