package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */

public class ActionList {

    private List<ActionListModel> rows;
    private int recordCount;

    public List<ActionListModel> getRows() {
        return rows;
    }

    public void setRows(List<ActionListModel> rows) {
        this.rows = rows;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public static class ActionListModel{
        private int id;
        @SerializedName("type_id")
        private int typeId;
        private String title;
        @SerializedName("show_pics")
        private String showPics;
        @SerializedName("action_detail")
        private String actionDetail;
        @SerializedName("start_time")
        private String startTime;
        @SerializedName("end_time")
        private String endTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getShowPics() {
            return showPics;
        }

        public void setShowPics(String showPics) {
            this.showPics = showPics;
        }

        public String getActionDetail() {
            return actionDetail;
        }

        public void setActionDetail(String actionDetail) {
            this.actionDetail = actionDetail;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }
}
