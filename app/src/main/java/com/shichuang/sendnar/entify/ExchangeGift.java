package com.shichuang.sendnar.entify;

import java.io.Serializable;

public class ExchangeGift implements Serializable {
    private int id;
    private int receiveGiftId;
    private int goodIdNew;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReceiveGiftId() {
        return receiveGiftId;
    }

    public void setReceiveGiftId(int receiveGiftId) {
        this.receiveGiftId = receiveGiftId;
    }

    public int getGoodIdNew() {
        return goodIdNew;
    }

    public void setGoodIdNew(int goodIdNew) {
        this.goodIdNew = goodIdNew;
    }
}
