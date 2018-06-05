package com.shichuang.sendnar.common;

import android.content.Context;

import com.shichuang.sendnar.SendnarApplication;
import com.shichuang.sendnar.Setting;

import static com.shichuang.sendnar.common.TagAliasOperatorHelper.sequence;

/**
 * Created by Administrator on 2018/1/19.
 */

public class JpushUtils {
    /**
     * 设置极光推送别名
     */
    public static void setJpushAlias(Context context, String alias) {
        if (alias == null || Setting.hasJpushInfo(context))
            return;
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = TagAliasOperatorHelper.ACTION_SET;
        sequence++;
        tagAliasBean.alias = alias;
        tagAliasBean.isAliasAction = true;
        TagAliasOperatorHelper.getInstance().handleAction(SendnarApplication.getInstance(), sequence, tagAliasBean);

        Setting.updateJpushInfo(context, alias);
    }

    /**
     * 删除极光推送别名
     */
    public static void delJpushAlias(Context context) {
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = TagAliasOperatorHelper.ACTION_DELETE;
        sequence++;
        tagAliasBean.isAliasAction = true;
        TagAliasOperatorHelper.getInstance().handleAction(SendnarApplication.getInstance(), sequence, tagAliasBean);

        Setting.updateJpushInfo(context, "");
    }
}
