package com.shichuang.sendnar.activity;

import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.view.View;

import com.shichuang.open.base.BaseActivity;
import com.shichuang.sendnar.R;

/**
 * 商品售后审核
 * Created by Administrator on 2018/6/5.
 */

public class GoodsAfterAuditActivity extends BaseActivity{
    @Override
    public int getLayoutId() {
        return R.layout.activity_goods_after_audit;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    public class Test extends InputMethodService{

    }
}
