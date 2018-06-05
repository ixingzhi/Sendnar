package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/4/24.
 */

public class FestivalBannerAndGiftBag {

    private List<PackageList>  packageList;
    private List<PicList> picList;

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

    public static class PackageList {
        private int id;
        @SerializedName("goods_id")
        private int goodsId;
        @SerializedName("action_id")
        private int actionId;
        private String pic;
        @SerializedName("real_total_pric")
        private String realTotalPrice;
        @SerializedName("gift_pack_name")
        private String giftPackName;

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

        public String getRealTotalPrice() {
            return realTotalPrice;
        }

        public void setRealTotalPrice(String realTotalPrice) {
            this.realTotalPrice = realTotalPrice;
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

        public String getRealTotalPric() {
            return realTotalPrice;
        }

        public void setRealTotalPric(String realTotalPrice) {
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
        private int id;
        private String pic;

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
    }

}
