package com.shichuang.sendnar.entify;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/5/8.
 */

public class SendGift {
    @SerializedName("redpacket_id")
    private int redPacketId;

    public int getRedPacketId() {
        return redPacketId;
    }

    public void setRedPacketId(int redPacketId) {
        this.redPacketId = redPacketId;
    }
}
