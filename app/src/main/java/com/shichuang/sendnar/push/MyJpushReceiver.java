package com.shichuang.sendnar.push;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.shichuang.sendnar.activity.MessageActivity;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2018/1/19.
 */

public class MyJpushReceiver extends BroadcastReceiver {
    private static final String TAG = "MyJpushReceiver";
    private NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            // Toast.makeText(context, "JPush用户注册成功",
            // Toast.LENGTH_SHORT).show();
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            // Toast.makeText(context, "接收到推送下来的自定义消息",
            // Toast.LENGTH_SHORT).show();

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            // Toast.makeText(context, "接收到推送下来的通知", Toast.LENGTH_SHORT).show();
            receivingNotification(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            // Toast.makeText(context, "用户点击打开了通知", Toast.LENGTH_SHORT).show();
            openNotification(context, bundle);
        } else {

        }
    }

    private void receivingNotification(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
    }

    private void openNotification(Context context, Bundle bundle) {
//        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage("com.shichuang.mobileworkingticket");
//        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//        context.startActivity(launchIntent);
        // 先进入如首页，再进工作任务
        Intent intent = new Intent(context, MessageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
