package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/3/22.
 */

public class MessageList {
    private int id;
    private MessageListModel newMessage;
    private boolean isOpenMessageContent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MessageListModel getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(MessageListModel newMessage) {
        this.newMessage = newMessage;
    }

    public boolean isOpenMessageContent() {
        return isOpenMessageContent;
    }

    public void setOpenMessageContent(boolean openMessageContent) {
        isOpenMessageContent = openMessageContent;
    }

    public static class MessageListModel {
        private int id;
        @SerializedName("message_title")
        private String messageTitle;
        @SerializedName("message_content")
        private String messageContent;
        @SerializedName("send_time")
        private String sendTime;


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

        public String getSendTime() {
            return sendTime;
        }

        public void setSendTime(String sendTime) {
            this.sendTime = sendTime;
        }
    }
}
