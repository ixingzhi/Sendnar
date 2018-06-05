package com.shichuang.sendnar.common;

import android.content.Context;
import android.content.SharedPreferences;
import com.shichuang.sendnar.entify.User;

public class UserCache {

    private static final String PREFS_NAME = "com.shichuang.sendnar.usercache";
    private static final String USER_KEY = "user_info_v1";

    public static void update(Context ctx, User token) {
        if (token != null) {
            String userStr = Convert.toJson(token);
            SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(USER_KEY, userStr);
            editor.commit();
        }
    }

    public static void clear(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(USER_KEY);
        editor.commit();
    }

    public static User user(Context ctx) {
        User user = new User();
        if (isUserLogin(ctx)) {
            SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String userStr = settings.getString(USER_KEY, null);
            user = Convert.fromJson(userStr, User.class);
        }
        return user;
    }

    public static boolean isUserLogin(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getString(USER_KEY, null) != null;
    }

}
