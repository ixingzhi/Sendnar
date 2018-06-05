package com.shichuang.sendnar;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;

/**
 * Created by xiedd on 2018/1/19.
 */

public class Setting {
    private static final String KEY_JPUSH_ALIAS = "jpushAlias";  // 保存极光推送别名
    private static final String KEY_GIFTS_DETAILS_REMIND = "giftsDetailsRemind";  // 礼品详情提示
    private static final String KEY_MY_ITEMS_REMIND = "myItemsRemind";  // 我的物品提示
    private static final String KEY_POVERTY_ALLEVIATION_ACTIVITIES_REMIND = "povertyAlleviationActivitiesRemind";  // 扶贫商品
    private static final String KEY_GIFTS_RECEIVE_REMIND = "giftsReceiveRemind";  // 接收的礼物


    public static SharedPreferences getSettingPreferences(Context context) {
        return context.getSharedPreferences(Setting.class.getName(), Context.MODE_PRIVATE);
    }

    /**
     * 极光推送
     */
    public static void updateJpushInfo(Context context, String alias) {
        SharedPreferences sp = getSettingPreferences(context);
        SharedPreferences.Editor editor = sp.edit().putString(KEY_JPUSH_ALIAS, alias);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    public static boolean hasJpushInfo(Context context) {
        SharedPreferences sp = getSettingPreferences(context);
        String alias = sp.getString(KEY_JPUSH_ALIAS, "");
        if (alias == null || "".equals(alias)) {
            return false;
        }
        return true;
    }

    /**
     * 商品详情提示
     */
    public static void updateGiftsDetailsRemind(Context context, boolean isRemind) {
        SharedPreferences sp = getSettingPreferences(context);
        SharedPreferences.Editor editor = sp.edit().putBoolean(KEY_GIFTS_DETAILS_REMIND, isRemind);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    public static boolean isShowGiftsDetailsRemind(Context context) {
        SharedPreferences sp = getSettingPreferences(context);
        boolean isRemind = sp.getBoolean(KEY_GIFTS_DETAILS_REMIND, true);
        if (!isRemind) {
            return false;
        }
        return true;
    }

    /**
     * 我的物品提示
     */
    public static void updateMyItemsRemind(Context context, boolean isRemind) {
        SharedPreferences sp = getSettingPreferences(context);
        SharedPreferences.Editor editor = sp.edit().putBoolean(KEY_MY_ITEMS_REMIND, isRemind);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    public static boolean isShowMyItemsRemind(Context context) {
        SharedPreferences sp = getSettingPreferences(context);
        boolean isRemind = sp.getBoolean(KEY_MY_ITEMS_REMIND, true);
        if (!isRemind) {
            return false;
        }
        return true;
    }

    /**
     * 扶贫商品提示
     */
    public static void updatePovertyAlleviationActivitiesRemind(Context context, boolean isRemind) {
        SharedPreferences sp = getSettingPreferences(context);
        SharedPreferences.Editor editor = sp.edit().putBoolean(KEY_POVERTY_ALLEVIATION_ACTIVITIES_REMIND, isRemind);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    public static boolean isShowPovertyAlleviationActivitiesRemind(Context context) {
        SharedPreferences sp = getSettingPreferences(context);
        boolean isRemind = sp.getBoolean(KEY_POVERTY_ALLEVIATION_ACTIVITIES_REMIND, true);
        if (!isRemind) {
            return false;
        }
        return true;
    }

    /**
     * 接收的礼物提示
     */
    public static void updateGiftsReceiveRemind(Context context, boolean isRemind) {
        SharedPreferences sp = getSettingPreferences(context);
        SharedPreferences.Editor editor = sp.edit().putBoolean(KEY_GIFTS_RECEIVE_REMIND, isRemind);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    public static boolean isShowGiftsReceiveRemind(Context context) {
        SharedPreferences sp = getSettingPreferences(context);
        boolean isRemind = sp.getBoolean(KEY_GIFTS_RECEIVE_REMIND, true);
        if (!isRemind) {
            return false;
        }
        return true;
    }



}
