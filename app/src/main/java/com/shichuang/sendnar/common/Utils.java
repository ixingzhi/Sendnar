package com.shichuang.sendnar.common;

import android.content.Context;
import android.text.TextUtils;

import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.sendnar.activity.LoginActivity;

/**
 * Created by Administrator on 2018/4/20.
 */

public class Utils {
    /**
     * 截取图片第一张地址
     */
    public static String getSingleImageUrlByImageUrls(String pics) {
        if (!TextUtils.isEmpty(pics) && pics.length() > 0) {
            String[] imageStr = pics.split(",");
            if (imageStr.length > 0) {
                String pic = imageStr[0];
                if (pic.startsWith("http") || pic.startsWith("https")) {
                    return pic;
                } else {
                    return Constants.MAIN_ENGINE_PIC + pic;
                }
            }
        }
        return "";
    }

    /**
     * 判断用户是否登录
     */
    public static boolean isLogin(Context context) {
        boolean bool = TokenCache.isUserLogin(context);
        if (!bool) {
            RxActivityTool.skipActivity(context, LoginActivity.class);
            return false;
        }
        return true;
    }
}
