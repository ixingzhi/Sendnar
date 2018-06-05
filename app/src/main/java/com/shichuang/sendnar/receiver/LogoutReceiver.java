package com.shichuang.sendnar.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.shichuang.sendnar.activity.LogoutDialogActivity;


/**
 * Created by Administrator on 2017/10/19.
 * 退出登录
 */

public class LogoutReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(LogoutDialogActivity.isFront == false){  // 如果不在前台显示，则打开，防止多次打开
            Intent mIntent = new Intent(context, LogoutDialogActivity.class);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mIntent);
        }
    }
}
