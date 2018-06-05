package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/4/25.
 */

public class GiftsCategoryType2 {

    private GiftsList goodsList;
    private List<PackageList> packageList;
    private List<PicList> picList;

    public GiftsList getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(GiftsList goodsList) {
        this.goodsList = goodsList;
    }

    public List<PackageList> getPackageList() {
        return packageList;
    }

    public void setPackageList(List<PackageList> packageList) {
        this.packageList = packageList;
    }

    public List<PicList> getPicList() {
        return picList;
    }

    public void setPicList(List<PicList> picList) {
        this.picList = picList;
    }

    public static class GiftsList {
        private List<GiftsListModel> rows;
        private int recordCount;

        public List<GiftsListModel> getRows() {
            return rows;
        }

        public void setRows(List<GiftsListModel> rows) {
            this.rows = rows;
        }

        public int getRecordCount() {
            return recordCount;
        }

        public void setRecordCount(int recordCount) {
            this.recordCount = recordCount;
        }

        public static class GiftsListModel {
            @SerializedName("action_item_id")
            private String actionItemId;
            @SerializedName("goods_id")
            private int goodsId;
            private String pic;
            @SerializedName("promotion_price")
            private String promotionPrice;
            @SerializedName("shop_name")
            private String shopName;
            private String labels;

            public String getActionItemId() {
                return actionItemId;
            }

            public void setActionItemId(String actionItemId) {
                this.actionItemId = actionItemId;
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

    public static class PackageList {
        @SerializedName("gift_id")
        private int giftId;
        @SerializedName("action_id")
        private int actionId;
        @SerializedName("PIC")
        private String pic;
        @SerializedName("real_total_pric")
        private String realTotalPrice;
        @SerializedName("gift_pack_name")
        private String giftPackName;

        public int getGiftId() {
            return giftId;
        }

        public void setGiftId(int giftId) {
            this.giftId = giftId;
        }

        public int getActionId() {
            return actionId;
        }

        public void setActionId(int actionId) {
            this.actionId = actionId;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getRealTotalPrice() {
            return realTotalPrice;
        }

        public void setRealTotalPrice(String realTotalPrice) {
            this.realTotalPrice = realTotalPrice;
        }

        public String getGiftPackName() {
            return giftPackName;
        }

        public void setGiftPackName(String giftPackName) {
            this.giftPackName = giftPackName;
        }
    }

    public static class PicList {
        @SerializedName("action_pic_id")
        private int actionPicId;
        @SerializedName("PIC")
        private String pic;

        public int getActionPicId() {
            return actionPicId;
        }

        public void setActionPicId(int actionPicId) {
            this.actionPicId = actionPicId;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
