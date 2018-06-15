package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/6/14.
 */

public class MyItemsToSendConfirmOrder {
    private String ids;
    @SerializedName("total_goods_price")
    private String totalGoodsPrice;
    @SerializedName("goods_detaillist")
    private List<GoodsList> goodsList;
    @SerializedName("addresslist")
    private List<AddressList> addressList;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getTotalGoodsPrice() {
        return totalGoodsPrice;
    }

    public void setTotalGoodsPrice(String totalGoodsPrice) {
        this.totalGoodsPrice = totalGoodsPrice;
    }

    public List<GoodsList> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsList> goodsList) {
        this.goodsList = goodsList;
    }

    public List<AddressList> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<AddressList> addressList) {
        this.addressList = addressList;
    }

    public static class GoodsList{
        private int id;
        @SerializedName("order_id")
        private int orderId;
        @SerializedName("shop_goods_id")
        private int shopGoodsId;
        @SerializedName("sort_name")
        private String sortName;
        private String pic;
        @SerializedName("market_price")
        private String marketPrice;
        @SerializedName("sale_price")
        private String salePrice;
        @SerializedName("buy_count")
        private int buyCount;
        @SerializedName("add_time")
        private String addTime;
        @SerializedName("order_detail_id")
        private int orderDetailId;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public int getShopGoodsId() {
            return shopGoodsId;
        }

        public void setShopGoodsId(int shopGoodsId) {
            this.shopGoodsId = shopGoodsId;
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

        public int getBuyCount() {
            return buyCount;
        }

        public void setBuyCount(int buyCount) {
            this.buyCount = buyCount;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public int getOrderDetailId() {
            return orderDetailId;
        }

        public void setOrderDetailId(int orderDetailId) {
            this.orderDetailId = orderDetailId;
        }
    }

    public static class AddressList{
        private int id;
        private String name;
        private String phone;
        private String province;
        @SerializedName("province_id")
        private String provinceId;
        private String city;
        @SerializedName("city_id")
        private String cityId;
        private String area;
        @SerializedName("area_id")
        private String areaId;
        private String address;
        @SerializedName("is_default")
        private int isDefault;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(String provinceId) {
            this.provinceId = provinceId;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(int isDefault) {
            this.isDefault = isDefault;
        }
    }
}
