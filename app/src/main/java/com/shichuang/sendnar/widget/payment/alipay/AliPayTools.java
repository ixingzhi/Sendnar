package com.shichuang.sendnar.widget.payment.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.shichuang.sendnar.widget.payment.OnRequestListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class AliPayTools {

    private static final int SDK_PAY_FLAG_V1 = 1;   // 旧版支付
    private static final int SDK_PAY_FLAG_V2 = 2;   // 新版支付

    private static OnRequestListener sOnRequestListener;
    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG_V1:
                    @SuppressWarnings("unchecked")
                    PayResultV1 payResultV1 = new PayResultV1((String) msg.obj);

                    //对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。

                    String resultInfoV1 = payResultV1.getResult();// 同步返回需要验证的信息
                    String resultStatusV1 = payResultV1.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatusV1, "9000")) {
                        sOnRequestListener.onSuccess(resultStatusV1);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        sOnRequestListener.onError(resultStatusV1);
                    }
                    break;
                case SDK_PAY_FLAG_V2:
                    @SuppressWarnings("unchecked")
                    PayResultV2 payResultV2 = new PayResultV2((Map<String, String>) msg.obj);

                    //对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。

                    String resultInfoV2 = payResultV2.getResult();// 同步返回需要验证的信息
                    String resultStatusV2 = payResultV2.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatusV2, "9000")) {
                        sOnRequestListener.onSuccess(resultStatusV2);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        sOnRequestListener.onError(resultStatusV2);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 支付宝新版支付
     *
     * @param activity
     * @param appid
     * @param isRsa2
     * @param alipay_rsa_private
     * @param aliPayModel
     * @param onRxHttp1
     */
    public static void aliPayV2(final Activity activity, String appid, boolean isRsa2, String alipay_rsa_private, String notifyUrl, AliPayModel aliPayModel, OnRequestListener onRxHttp1) {
        sOnRequestListener = onRxHttp1;
        Map<String, String> params = AliPayOrderInfoUtil.buildOrderParamMapV2(appid, isRsa2, aliPayModel.getOut_trade_no(), aliPayModel.getName(), aliPayModel.getMoney(), aliPayModel.getDetail(), notifyUrl);
        String orderParam = AliPayOrderInfoUtil.buildOrderParam(params);

        String privateKey = alipay_rsa_private;

        String sign = AliPayOrderInfoUtil.getSign(params, privateKey, isRsa2);
        final String orderInfo = orderParam + "&" + sign;
        Log.d("test", orderInfo);
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG_V2;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 支付宝旧版支付
     */
    public static void aliPayV1(final Activity activity, String PID, String seller, String alipay_rsa_private, String notifyUrl, AliPayModel aliPayModel, OnRequestListener onRxHttp1) {
        sOnRequestListener = onRxHttp1;
        String orderParam = AliPayOrderInfoUtil.buildOrderParamStringV1(PID, seller, aliPayModel.getOut_trade_no(), aliPayModel.getName(), aliPayModel.getMoney(), aliPayModel.getDetail(), notifyUrl);
        String sign = AliPaySignUtils.sign(orderParam, alipay_rsa_private, false);
        try {
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException var7) {
            var7.printStackTrace();
        }
        final String payInfo = orderParam + "&sign=\"" + sign + "\"&" + "sign_type=\"RSA\"";
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                String result = alipay.pay(payInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG_V1;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

}
