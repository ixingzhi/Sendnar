package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/3/22.
 */

public class MessageList {
    private List<MessageListModel> rows;
    private int recordCount;

    public List<MessageListModel> getRows() {
        return rows;
    }

    public void setRows(List<MessageListModel> rows) {
        this.rows = rows;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public static class MessageListModel{
        private int id;
        @SerializedName("message_title")
        private String messageTitle;
        @SerializedName("message_content")
        private String messageContent;
        @SerializedName("add_time")
        private String addTime;
        @SerializedName("is_read")
        private int isRead;
        private boolean isOpenMessageContent;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMessageTitle() {
            return messageTitle;
        }

        public void setMessageTitle(String messageTitle) {
            this.messageTitle = messageTitle;
        }

        public String getMessageContent() {
            return messageContent;
        }

        public void setMessageContent(String messageContent) {
            this.messageContent = messageContent;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public int getIsRead() {
            return isRead;
        }

        public void setIsRead(int isRead) {
            this.isRead = isRead;
        }

        public boolean isOpenMessageContent() {
            return isOpenMessageContent;
        }

        public void setOpenMessageContent(boolean openMessageContent) {
            isOpenMessageContent = openMessageContent;
        }
    }
}
