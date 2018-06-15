package com.shichuang.sendnar.common;

import android.content.Context;

import com.shichuang.sendnar.entify.User;

import java.util.HashMap;
import java.util.Map;

import cn.udesk.UdeskConst;
import cn.udesk.UdeskSDKManager;

/**
 * 客服管理
 * Created by Administrator on 2018/5/16.
 */

public class UdeskHelper {
    private static final String DOMAIN = "sendnar.udesk.cn";
    private static final String APP_KEY = "afdbd6ea71be4788f8cc17dee8c50eee";
    private static final String APP_ID = "e311f7440076fc65";

    public static void initApiKey(Context context) {
        UdeskSDKManager.getInstance().initApiKey(context, DOMAIN, APP_KEY, APP_ID);
    }

    public static void setUserInfo(Context context){
        // 注意sdktoken是客户的唯一标识，用来识别身份，sdk_token: 你们传入的字符请使用 只包含字母，数字的字符集。
        Map<String, String> info = new HashMap<String, String>();
        String sdkToken = TokenCache.token(context);
        User user = UserCache.user(context);
        info.put(UdeskConst.UdeskUserInfo.USER_SDK_TOKEN, sdkToken);
        info.put(UdeskConst.UdeskUserInfo.NICK_NAME, user.getNickname());
        info.put(UdeskConst.UdeskUserInfo.CELLPHONE, user.getPhone());
        UdeskSDKManager.getInstance().setUserInfo(context, sdkToken, info);
        // 设置用户头像
        UdeskSDKManager.getInstance().setCustomerUrl(Utils.getSingleImageUrlByImageUrls(user.getHeadPortrait()));
    }

    public static void entryChat(Context context) {
        // 进入聊天页面
        UdeskSDKManager.getInstance().entryChat(context);
    }

    public static void logout(){
        UdeskSDKManager.getInstance().logoutUdesk();
    }
}
