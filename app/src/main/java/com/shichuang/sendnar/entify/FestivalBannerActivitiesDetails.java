package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/4/20.
 */

public class FestivalBannerActivitiesDetails {
    private ActionDetail actionDetail;

    public ActionDetail getActionDetail() {
        return actionDetail;
    }

    public void setActionDetail(ActionDetail actionDetail) {
        this.actionDetail = actionDetail;
    }


    public static class ActionDetail {
        private int id;
        private String title;
        @SerializedName("start_time")
        private String startTime;
        @SerializedName("end_time")
        private String endTime;
        private String H5Url;
        @SerializedName("action_detail")
        private String actionDetail;
        private String pic;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public String getH5Url() {
            return H5Url;
        }

        public void setH5Url(String h5Url) {
            H5Url = h5Url;
        }

        public String getActionDetail() {
            return actionDetail;
        }

        public void setActionDetail(String actionDetail) {
            this.actionDetail = actionDetail;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }

}
