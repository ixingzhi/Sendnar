package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/4/27.
 */

public class ShoppingCart {
    @SerializedName("goods_cart_id")
    private int goodsCartId;
    @SerializedName("goods_cart_counts")
    private int goodsCartCounts;
    @SerializedName("shop_name")
    private String shopName;
    @SerializedName("sale_price")
    private String salePrice;
    @SerializedName("cart_user_info_id")
    private int cartUserInfoId;
    @SerializedName("add_time")
    private String addTime;
    private String pic;
    @SerializedName("goods_id")
    private String goodsId;
    private boolean isSelect;

    public int getGoodsCartId() {
        return goodsCartId;
    }

    public void setGoodsCartId(int goodsCartId) {
        this.goodsCartId = goodsCartId;
    }

    public int getGoodsCartCounts() {
        return goodsCartCounts;
    }

    public void setGoodsCartCounts(int goodsCartCounts) {
        this.goodsCartCounts = goodsCartCounts;
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

    public int getCartUserInfoId() {
        return cartUserInfoId;
    }

    public void setCartUserInfoId(int cartUserInfoId) {
        this.cartUserInfoId = cartUserInfoId;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
