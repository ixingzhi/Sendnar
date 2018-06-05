package com.shichuang.sendnar.event;

/**
 * Created by Administrator on 2018/5/7.
 */

public class UpdateOrderEvent {
    public static final int DELETE_ORDER = 0x11;
    public int operationType;

    public UpdateOrderEvent() {
    }

    public UpdateOrderEvent(int type) {
        this.operationType = type;
    }
}
