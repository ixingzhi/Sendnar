package com.shichuang.sendnar.common;

import android.content.Context;
import android.content.SharedPreferences;


import com.shichuang.sendnar.entify.Token;

public class TokenCache {

    private static final String PREFS_NAME = "com.shichuang.sendnar.tokencache";
    private static final String USER_KEY = "token_info_v1";

    public static void update(Context ctx, Token token) {
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

    public static Token user(Context ctx) {
        Token token = new Token();
        if (isUserLogin(ctx)) {
            SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String userStr = settings.getString(USER_KEY, null);
            token = Convert.fromJson(userStr, Token.class);
        }
        return token;
    }

    public static boolean isUserLogin(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getString(USER_KEY, null) != null;
    }

    public static String token(Context ctx) {
        String s = "";
        if (user(ctx) != null) {
            s = user(ctx).getToken();
        }
        return s;
    }

    public static int userId(Context ctx) {
        int id = 0;
        if (user(ctx) != null) {
            id = user(ctx).getUserId();
        }
        return id;
    }

}
