package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/4/25.
 */

public class GiftsCategory {
    @SerializedName("list")
    private List<TypeList> priceList;
    @SerializedName("typelist")
    private List<TypeList> typeList ;

    public List<TypeList> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<TypeList> priceList) {
        this.priceList = priceList;
    }

    public List<TypeList> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<TypeList> typeList) {
        this.typeList = typeList;
    }

    public static class TypeList {
        private int id;
        private String name;
        private int skip;

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

        public int getSkip() {
            return skip;
        }

        public void setSkip(int skip) {
            this.skip = skip;
        }
    }
}
