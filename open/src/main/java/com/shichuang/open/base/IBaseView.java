package com.shichuang.open.base;

import android.os.Bundle;
import android.view.View;

/**
 * Created by Administrator on 2017/10/26.
 */

public interface IBaseView{
    /**
     * 绑定布局
     *
     * @return 布局Id
     */
    int getLayoutId();

    /**
     * 初始化view
     */
    void initView(final Bundle savedInstanceState, final View view);

    /**
     * 点击事件
     */
    void initEvent();

    /**
     * 初始化数据
     */
    void initData();
}
