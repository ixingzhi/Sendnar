package com.shichuang.sendnar.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;

import com.shichuang.open.tool.RxLogTool;
import com.shichuang.open.tool.RxToastTool;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;

/**
 * Created by xiedd on 2017/12/11.
 */

public class NetworkConnectChangedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            switch (networkInfo.getType()) {
                case TYPE_MOBILE:
                    RxLogTool.e("正在使用2G/3G/4G网络");
                    break;
                case TYPE_WIFI:
                    RxLogTool.e("正在使用wifi上网");
                    break;
                default:
                    break;
            }
        } else {
            showToast("当前没有网络连接，请确保你已经打开网络");
        }
    }

    private void showToast(final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RxToastTool.setGravity(Gravity.CENTER, 0, 0);
                RxToastTool.showShort(msg);
            }
        }).start();
    }
}
