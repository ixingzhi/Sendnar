package com.shichuang.sendnar.common;

/**
 * Created by Administrator on 2018/5/7.
 */

public class OrderStatus {
    /**
     * 全部
     */
    public static final int ALL = 0;
    /**
     * 待付款
     */
    public static final int WAIT_PAYMENT = 1;
    /**
     * 待发货
     */
    public static final int WAIT_WAIT_DELIVERY = 3;
    /**
     * 已发货
     */
    public static final int DELIVERED = 4;
    /**
     * 已完成
     */
    public static final int COMPLETED = 6;
    /**
     * 交易关闭
     */
    public static final int TRADING_CLOSED = 11;
}
