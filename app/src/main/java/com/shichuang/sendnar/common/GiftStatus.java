package com.shichuang.sendnar.common;

/**
 * Created by Administrator on 2018/5/8.
 */

public class GiftStatus {
    /**
     * 待领取
     */
    public static final int WAIT_RECEIVE = 1;
    /**
     * 已领取
     */
    public static final int HAS_RECEIVE = 2;
    /**
     * 超时
     */
    public static final int TIMEOUT = 3;
    /**
     * 已转赠（接收到的礼物才有转赠)
     */
    public static final int HAS_EXAMPLES = 4;
}
