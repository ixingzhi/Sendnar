package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.view.View;

import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.sendnar.R;

/**
 * 选择服务类型
 * Created by Administrator on 2018/6/5.
 */

public class ChooseServiceTypeActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_service_type;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {

    }

    @Override
    public void initEvent() {
        findViewById(R.id.rl_exchange_goods).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxActivityTool.skipActivity(mContext, ApplyExchangeGoodsActivity.class);
            }
        });
    }

    @Override
    public void initData() {

    }
}
