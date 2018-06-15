package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/6/6.
 */

public class RefundReturnGoodsReason {
    @SerializedName("cause_list")
    private List<Reason> reasonList;

    public List<Reason> getReasonList() {
        return reasonList;
    }

    public void setReasonList(List<Reason> reasonList) {
        this.reasonList = reasonList;
    }

    public static class Reason{
        private int id;
        @SerializedName("cause_name")
        private String causeName;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCauseName() {
            return causeName;
        }

        public void setCauseName(String causeName) {
            this.causeName = causeName;
        }
    }
}
