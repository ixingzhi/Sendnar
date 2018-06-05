package com.shichuang.sendnar.entify;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/4/22.
 */

public class MyOrder implements MultiItemEntity {
    public static final int ORDER_HEADER = 0X11;
    public static final int ORDER_BODY = 0X12;
    public static final int ORDER_FOOT = 0X13;

    private int itemType;
    private int recordCount;
    private int orderPosition;
    private List<OrderInfo> rows;

    public MyOrder(){

    }

    public MyOrder(int itemType) {
        this.itemType = itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getOrderPosition() {
        return orderPosition;
    }

    public void setOrderPosition(int orderPosition) {
        this.orderPosition = orderPosition;
    }

    public List<OrderInfo> getRows() {
        return rows;
    }

    public void setRows(List<OrderInfo> rows) {
        this.rows = rows;
    }

    public static class OrderInfo {
        private int id;
        @SerializedName("order_status")
        private int orderStatus;
        @SerializedName("order_no")
        private String orderNo;
        @SerializedName("shop_goods_order_detailModellist")
        private List<GoodsInfo>  goodsInfoList;
        @SerializedName("order_amount_total")
        private String orderAmountTotal;
        @SerializedName("actual_amount")
        private String actualAmount;
        private int goodsPosition;  // 标识商品的位置

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public List<GoodsInfo> getGoodsInfoList() {
            return goodsInfoList;
        }

        public void setGoodsInfoList(List<GoodsInfo> goodsInfoList) {
            this.goodsInfoList = goodsInfoList;
        }

        public String getOrderAmountTotal() {
            return orderAmountTotal;
        }

        public void setOrderAmountTotal(String orderAmountTotal) {
            this.orderAmountTotal = orderAmountTotal;
        }

        public String getActualAmount() {
            return actualAmount;
        }

        public void setActualAmount(String actualAmount) {
            this.actualAmount = actualAmount;
        }

        public int getGoodsPosition() {
            return goodsPosition;
        }

        public void setGoodsPosition(int goodsPosition) {
            this.goodsPosition = goodsPosition;
        }

        public static class GoodsInfo {
            private int id;
            @SerializedName("shop_goods_id")
            private int shopGoodsId;
            @SerializedName("sort_name")
            private String sortName;
            private String pic;
            @SerializedName("total_price")
            private String totalPrice;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public String getTotalPrice() {
                return totalPrice;
            }

            public void setTotalPrice(String totalPrice) {
                this.totalPrice = totalPrice;
            }
        }
    }
}
