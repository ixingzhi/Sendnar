package com.shichuang.sendnar.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.shichuang.sendnar.entify.KeyWord;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/7.
 */

public class SearchCache {
    private static final String PREFS_NAME = "com.shichuang.sendnar.searchcache";
    private static final String USER_KEY = "search_info_v1";

    public static void update(Context ctx, List<KeyWord> searchList) {
        if (searchList != null) {
            String userStr = Convert.toJson(searchList);
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

    public static List<KeyWord> get(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String search = settings.getString(USER_KEY, "");
        if (TextUtils.isEmpty(search)) {
            return new LinkedList<>();
        }
        return Convert.fromJson(search, new TypeToken<LinkedList<KeyWord>>() {
        }.getType());
    }

}
