package com.shichuang.sendnar.common;

import android.content.Context;
import android.os.Handler;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxToastTool;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.event.UpdateOrderEvent;

import org.greenrobot.eventbus.EventBus;


/**
 * 订单操作类
 * Created by Administrator on 2018/5/7.
 */

public class OrderOperation {
    private static OrderOperation mInstance;

    public static OrderOperation getInstance() {
        if (null == mInstance) {
            mInstance = new OrderOperation();
        }
        return mInstance;
    }

    /**
     * 取消订单
     *
     * @param context
     * @param orderId
     */
    public void cancelOrder(final Context context, int orderId) {
        OkGo.<AMBaseDto<Empty>>get(Constants.cancelOrderUrl)
                .tag(context)
                .params("token", TokenCache.token(context))
                .params("order_id", orderId)
                .execute(new NewsCallback<AMBaseDto<Empty>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Empty>, ? extends Request> request) {
                        super.onStart(request);
                        ((BaseActivity) context).showLoading();
                    }

                    @Override
                    public void onSuccess(Response<AMBaseDto<Empty>> response) {
                        RxToastTool.showShort(response.body().msg);
                        if (response.body().code == 0) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    EventBus.getDefault().post(new UpdateOrderEvent());
                                }
                            }, 200);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Empty>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ((BaseActivity) context).dismissLoading();
                    }
                });
    }

    /**
     * 删除订单
     *
     * @param context
     * @param orderId
     */
    public void deleteOrder(final Context context, int orderId) {
        OkGo.<AMBaseDto<Empty>>get(Constants.deleteOrderUrl)
                .tag(context)
                .params("token", TokenCache.token(context))
                .params("order_id", orderId)
                .execute(new NewsCallback<AMBaseDto<Empty>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Empty>, ? extends Request> request) {
                        super.onStart(request);
                        ((BaseActivity) context).showLoading();
                    }

                    @Override
                    public void onSuccess(Response<AMBaseDto<Empty>> response) {
                        RxToastTool.showShort(response.body().msg);
                        if (response.body().code == 0) {
                            if (response.body().code == 0) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        EventBus.getDefault().post(new UpdateOrderEvent(UpdateOrderEvent.DELETE_ORDER));
                                    }
                                }, 200);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Empty>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ((BaseActivity) context).dismissLoading();
                    }
                });
    }

    /**
     * 确认收货
     *
     * @param context
     * @param orderId
     */
    public void confirmGoodsOrder(final Context context, int orderId) {
        OkGo.<AMBaseDto<Empty>>get(Constants.confirmGoodsUrl)
                .tag(context)
                .params("token", TokenCache.token(context))
                .params("order_id", orderId)
                .execute(new NewsCallback<AMBaseDto<Empty>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Empty>, ? extends Request> request) {
                        super.onStart(request);
                        ((BaseActivity) context).showLoading();
                    }

                    @Override
                    public void onSuccess(Response<AMBaseDto<Empty>> response) {
                        RxToastTool.showShort(response.body().msg);
                        if (response.body().code == 0) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    EventBus.getDefault().post(new UpdateOrderEvent());
                                }
                            }, 200);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Empty>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ((BaseActivity) context).dismissLoading();
                    }
                });
    }
}
