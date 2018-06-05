package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */

public class Home {

    private List<Banner> carouselpics;
    private List<Activity> listpics;

    public List<Banner> getCarouselpics() {
        return carouselpics;
    }

    public void setCarouselpics(List<Banner> carouselpics) {
        this.carouselpics = carouselpics;
    }

    public List<Activity> getListpics() {
        return listpics;
    }

    public void setListpics(List<Activity> listpics) {
        this.listpics = listpics;
    }

    public static class Banner {
        private int id;
        private String pic;
        @SerializedName("pic_value_type")
        private int picValueType;  // 2-商品id 3-活动id 4-礼包id 5-单页
        @SerializedName("pic_value_parameter")
        private String picValueParameter;  // 对应值
        @SerializedName("type_id")
        private int typeId;  // 活动类型id 11-扶贫 12-节日

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

        public int getPicValueType() {
            return picValueType;
        }

        public void setPicValueType(int picValueType) {
            this.picValueType = picValueType;
        }

        public String getPicValueParameter() {
            return picValueParameter;
        }

        public void setPicValueParameter(String picValueParameter) {
            this.picValueParameter = picValueParameter;
        }

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }
    }

    public static class Activity {
        private int id;
        private String pic;
        @SerializedName("type_id")
        private int typeId;
        private String title;
        private String label;

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

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }
}
