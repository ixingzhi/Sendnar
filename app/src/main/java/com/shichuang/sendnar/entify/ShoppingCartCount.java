package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiedongdong on 2018/6/21.
 */

public class ShoppingCartCount {

    @SerializedName("cart_count")
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
