package com.shichuang.sendnar.adapter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxBigDecimalTool;
import com.shichuang.open.tool.RxGlideTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.activity.ChooseServiceTypeActivity;
import com.shichuang.sendnar.activity.FillInTheLogisticsActivity;
import com.shichuang.sendnar.activity.GoodsAfterAuditActivity;
import com.shichuang.sendnar.common.OrderStatus;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.OrderDetails;

/**
 * Created by Administrator on 2018/4/19.
 */

public class OrderDetailsAdapter extends BaseQuickAdapter<OrderDetails.OrderDetailsGoodsListModel, BaseViewHolder> {
    private boolean isOrderDetailsPage = true;
    private int orderStatus;

    public OrderDetailsAdapter() {
        super(R.layout.item_order_info);
    }

    @Override
    protected void convert(BaseViewHolder helper, final OrderDetails.OrderDetailsGoodsListModel item) {
        RxGlideTool.loadImageView(mContext, Utils.getSingleImageUrlByImageUrls(item.getPic()), (ImageView) helper.getView(R.id.iv_picture), R.drawable.ic_gift_default_square);
        helper.setText(R.id.tv_gifts_name, item.getSortName());
        helper.setText(R.id.tv_gifts_price, "¥" + RxBigDecimalTool.toDecimal(item.getSalePrice(), 2));

        if (isOrderDetailsPage) {
            setRefundStatus(helper, item);
        } else {
            helper.setGone(R.id.btn_return_goods, false);
        }
    }

    public void isOrderDetailsPage(boolean bool) {
        this.isOrderDetailsPage = bool;
    }

    public void setOrderStatus(int status) {
        this.orderStatus = status;
    }

    /**
     * 待卖家处理=1|卖家拒绝退款/退货/换货=2|待买家退货=3|买家已退货,等待卖家确认收货=4|卖家已退款=5|待财务打款=6|待卖家发货=7|卖家已发货=8|买家取消=9
     *
     * @param helper
     * @param
     */

    private void setRefundStatus(BaseViewHolder helper, final OrderDetails.OrderDetailsGoodsListModel item) {
        Button btnReturnGoods = helper.getView(R.id.btn_return_goods);
        switch (item.getRefundStatus()) {
            case 0:
                // 待付款和交易关闭不显示
                if (orderStatus == OrderStatus.WAIT_PAYMENT || orderStatus == OrderStatus.TRADING_CLOSED) {
                    btnReturnGoods.setVisibility(View.GONE);
                } else {
                    btnReturnGoods.setText("退换");
                    btnReturnGoods.setVisibility(View.VISIBLE);
                    // 选择服务类型
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("orderInfo", item);
                    skipPage(btnReturnGoods, ChooseServiceTypeActivity.class, bundle);
                }
                break;
            case 1:  // 待卖家处理=1
                btnReturnGoods.setText("待处理");
                btnReturnGoods.setVisibility(View.VISIBLE);
                // 商品售后审核
                Bundle bundle1 = new Bundle();
                bundle1.putInt("orderDetailId", item.getOrderDetailId());
                skipPage(btnReturnGoods, GoodsAfterAuditActivity.class, bundle1);
                break;
            case 2:  // 卖家拒绝退款/退货/换货=2
                btnReturnGoods.setText("已拒绝");
                btnReturnGoods.setVisibility(View.VISIBLE);
                // 商品售后审核
                Bundle bundle2 = new Bundle();
                bundle2.putInt("orderDetailId", item.getOrderDetailId());
                skipPage(btnReturnGoods, GoodsAfterAuditActivity.class, bundle2);
                break;
            case 3:  // 待买家退货=3         是否需要退货(我需要退货=1|不需要退货=2|换货=3)
                btnReturnGoods.setText("填写物流");
                btnReturnGoods.setVisibility(View.VISIBLE);
                Bundle bundle3 = new Bundle();
                bundle3.putInt("orderDetailId", item.getOrderDetailId());
                skipPage(btnReturnGoods, FillInTheLogisticsActivity.class, bundle3);

                break;
            case 4:  // 买家已退货,等待卖家确认收货=4
                btnReturnGoods.setText("买家已发货");
                btnReturnGoods.setVisibility(View.VISIBLE);
                // 商品售后审核
                Bundle bundle4 = new Bundle();
                bundle4.putInt("orderDetailId", item.getOrderDetailId());
                skipPage(btnReturnGoods, GoodsAfterAuditActivity.class, bundle4);
                break;
            case 5:  // 卖家已退款=5
                btnReturnGoods.setText("已退款");
                btnReturnGoods.setVisibility(View.VISIBLE);
                // 商品售后审核
                Bundle bundle5 = new Bundle();
                bundle5.putInt("orderDetailId", item.getOrderDetailId());
                skipPage(btnReturnGoods, GoodsAfterAuditActivity.class, bundle5);
                break;
            case 6:  // 待财务打款=6
                btnReturnGoods.setText("待退款");
                btnReturnGoods.setVisibility(View.VISIBLE);
                // 商品售后审核
                Bundle bundle6 = new Bundle();
                bundle6.putInt("orderDetailId", item.getOrderDetailId());
                skipPage(btnReturnGoods, GoodsAfterAuditActivity.class, bundle6);
                break;
            case 7:  // 待卖家发货=7
                btnReturnGoods.setText("待卖家发货");
                btnReturnGoods.setVisibility(View.VISIBLE);
                // 商品售后审核
                Bundle bundle7 = new Bundle();
                bundle7.putInt("orderDetailId", item.getOrderDetailId());
                skipPage(btnReturnGoods, GoodsAfterAuditActivity.class, bundle7);
                break;
            case 8:  // 卖家已发货=8
                btnReturnGoods.setText("卖家已发货");
                btnReturnGoods.setVisibility(View.VISIBLE);
                // 商品售后审核
                Bundle bundle8 = new Bundle();
                bundle8.putInt("orderDetailId", item.getOrderDetailId());
                skipPage(btnReturnGoods, GoodsAfterAuditActivity.class, bundle8);
                break;
            case 9:  // 买家取消=9
                btnReturnGoods.setText("已取消");
                btnReturnGoods.setVisibility(View.VISIBLE);
                // 商品售后审核
                Bundle bundle9 = new Bundle();
                bundle9.putInt("orderDetailId", item.getOrderDetailId());
                skipPage(btnReturnGoods, GoodsAfterAuditActivity.class, bundle9);
                break;
            case 10:
                btnReturnGoods.setText("已换货");
                btnReturnGoods.setVisibility(View.VISIBLE);
                Bundle bundle10 = new Bundle();
                bundle10.putInt("orderDetailId", item.getOrderDetailId());
                skipPage(btnReturnGoods, GoodsAfterAuditActivity.class, bundle10);
                break;

            default:
                btnReturnGoods.setVisibility(View.GONE);
                break;
        }
    }

    private void skipPage(View view, final Class<?> cls, final Bundle bundle) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxActivityTool.skipActivity(mContext, cls, bundle);
            }
        });
    }


}
