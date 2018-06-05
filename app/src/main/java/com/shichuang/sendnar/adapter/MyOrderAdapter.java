package com.shichuang.sendnar.adapter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxBigDecimalTool;
import com.shichuang.open.tool.RxGlideTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.activity.LogisticsStatusActivity;
import com.shichuang.sendnar.activity.OrderDetailsActivity;
import com.shichuang.sendnar.activity.OrderSettlementActivity;
import com.shichuang.sendnar.common.OrderOperation;
import com.shichuang.sendnar.common.OrderStatus;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.ConfirmOrder;
import com.shichuang.sendnar.entify.MyOrder;
import com.shichuang.sendnar.widget.ConfirmDialog;

import java.util.List;

/**
 * Created by Administrator on 2018/4/22.
 */

public class MyOrderAdapter extends BaseMultiItemQuickAdapter<MyOrder, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MyOrderAdapter(List<MyOrder> data) {
        super(data);
        addItemType(MyOrder.ORDER_HEADER, R.layout.item_my_order_header);
        addItemType(MyOrder.ORDER_BODY, R.layout.item_my_order_body);
        addItemType(MyOrder.ORDER_FOOT, R.layout.item_my_order_foot);
    }

    @Override
    protected void convert(BaseViewHolder helper, final MyOrder item) {
        switch (helper.getItemViewType()) {
            case MyOrder.ORDER_HEADER:
                int orderHeaderPosition = item.getOrderPosition();
                MyOrder.OrderInfo orderHeaderInfo = item.getRows().get(orderHeaderPosition);
                helper.setText(R.id.tv_order_no, "订单编号：" + orderHeaderInfo.getOrderNo());
                helper.setText(R.id.tv_order_status, setOrderStatus(orderHeaderInfo.getOrderStatus()));
                break;
            case MyOrder.ORDER_BODY:
                final int orderBodyPosition = item.getOrderPosition();
                int goodsBodyPosition = item.getRows().get(orderBodyPosition).getGoodsPosition();
                MyOrder.OrderInfo.GoodsInfo goodsInfo = item.getRows().get(orderBodyPosition).getGoodsInfoList().get(goodsBodyPosition);

                RxGlideTool.loadImageView(mContext, Utils.getSingleImageUrlByImageUrls(goodsInfo.getPic()), (ImageView) helper.getView(R.id.iv_picture), R.drawable.ic_gift_default_square);
                helper.setText(R.id.tv_gifts_name, goodsInfo.getSortName());
                helper.setText(R.id.tv_gifts_price, "¥" + RxBigDecimalTool.toDecimal(goodsInfo.getTotalPrice(), 2));

                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", item.getRows().get(orderBodyPosition).getId());
                        RxActivityTool.skipActivity(mContext, OrderDetailsActivity.class, bundle);
                    }
                });
                break;
            case MyOrder.ORDER_FOOT:
                MyOrder.OrderInfo orderFootInfo = item.getRows().get(item.getOrderPosition());
                setOrderOperation(helper, orderFootInfo);
                break;
            default:
                break;
        }
    }

    private String setOrderStatus(int state) {
        String status = "";
        switch (state) {
            case OrderStatus.WAIT_PAYMENT:
                status = "待付款";
                break;
            case OrderStatus.WAIT_WAIT_DELIVERY:
                status = "待发货";
                break;
            case OrderStatus.DELIVERED:
                status = "待收货";
                break;
            case OrderStatus.COMPLETED:
                status = "已完成";
                break;
            case OrderStatus.TRADING_CLOSED:
                status = "交易关闭";
                break;
            default:
                break;
        }
        return status;
    }


    private void setOrderOperation(BaseViewHolder helper, final MyOrder.OrderInfo orderFootInfo) {
        Button mBtnCancelOrder = helper.getView(R.id.btn_cancel_order);
        Button mBtnCheckLogistics = helper.getView(R.id.btn_check_logistics);
        Button mBtnDeleteOrder = helper.getView(R.id.btn_delete_order);
        Button mBtnConfirmGoods = helper.getView(R.id.btn_confirm_goods);
        Button mBtnImmediatePayment = helper.getView(R.id.btn_immediate_payment);
        // 影藏显示按钮
        switch (orderFootInfo.getOrderStatus()) {
            // "待付款"
            case 1:
                mBtnCancelOrder.setVisibility(View.VISIBLE);
                mBtnCheckLogistics.setVisibility(View.GONE);
                mBtnDeleteOrder.setVisibility(View.GONE);
                mBtnConfirmGoods.setVisibility(View.GONE);
                mBtnImmediatePayment.setVisibility(View.VISIBLE);
                break;
            // "待发货"
            case 3:
                mBtnCancelOrder.setVisibility(View.GONE);
                mBtnCheckLogistics.setVisibility(View.GONE);
                mBtnDeleteOrder.setVisibility(View.GONE);
                mBtnConfirmGoods.setVisibility(View.GONE);
                mBtnImmediatePayment.setVisibility(View.GONE);
                break;
            // "待收货"
            case 4:
                mBtnCancelOrder.setVisibility(View.GONE);
                mBtnCheckLogistics.setVisibility(View.VISIBLE);
                mBtnDeleteOrder.setVisibility(View.GONE);
                mBtnConfirmGoods.setVisibility(View.VISIBLE);
                mBtnImmediatePayment.setVisibility(View.GONE);
                break;
            // "已完成"
            case 6:
                mBtnCancelOrder.setVisibility(View.GONE);
                mBtnCheckLogistics.setVisibility(View.VISIBLE);
                mBtnDeleteOrder.setVisibility(View.VISIBLE);
                mBtnConfirmGoods.setVisibility(View.GONE);
                mBtnImmediatePayment.setVisibility(View.GONE);
                break;
            // "交易关闭"
            case 11:
                mBtnCancelOrder.setVisibility(View.GONE);
                mBtnCheckLogistics.setVisibility(View.GONE);
                mBtnDeleteOrder.setVisibility(View.VISIBLE);
                mBtnConfirmGoods.setVisibility(View.GONE);
                mBtnImmediatePayment.setVisibility(View.GONE);
                break;
            default:
                mBtnCancelOrder.setVisibility(View.GONE);
                mBtnCheckLogistics.setVisibility(View.GONE);
                mBtnDeleteOrder.setVisibility(View.GONE);
                mBtnConfirmGoods.setVisibility(View.GONE);
                mBtnImmediatePayment.setVisibility(View.GONE);
                break;
        }

        // 取消订单
        mBtnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog mDialog = new ConfirmDialog(mContext);
                mDialog.setMessage("确认取消订单？");
                mDialog.setNegativeButton("取消",null);
                mDialog.setPositiveButton("确定", new ConfirmDialog.DialogInterface() {
                    @Override
                    public void OnClickListener() {
                        OrderOperation.getInstance().cancelOrder(mContext, orderFootInfo.getId());
                    }
                });
                mDialog.show();
            }
        });
        // 查看物流
        mBtnCheckLogistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("orderNo", orderFootInfo.getOrderNo());
                RxActivityTool.skipActivity(mContext, LogisticsStatusActivity.class, bundle);
            }
        });
        // 删除订单
        mBtnDeleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog mDialog = new ConfirmDialog(mContext);
                mDialog.setMessage("确认删除订单？");
                mDialog.setNegativeButton("取消",null);
                mDialog.setPositiveButton("确定", new ConfirmDialog.DialogInterface() {
                    @Override
                    public void OnClickListener() {
                        OrderOperation.getInstance().deleteOrder(mContext, orderFootInfo.getId());
                    }
                });
                mDialog.show();
            }
        });
        // 确认收货
        mBtnConfirmGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog mDialog = new ConfirmDialog(mContext);
                mDialog.setMessage("确认已收到货物？");
                mDialog.setNegativeButton("取消",null);
                mDialog.setPositiveButton("确定", new ConfirmDialog.DialogInterface() {
                    @Override
                    public void OnClickListener() {
                        OrderOperation.getInstance().confirmGoodsOrder(mContext, orderFootInfo.getId());
                    }
                });
                mDialog.show();
            }
        });
        // 立即支付
        mBtnImmediatePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("orderNo", orderFootInfo.getOrderNo());
                RxActivityTool.skipActivity(mContext, OrderSettlementActivity.class, bundle);
            }
        });
    }
}
