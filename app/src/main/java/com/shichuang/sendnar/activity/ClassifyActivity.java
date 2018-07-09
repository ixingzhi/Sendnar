package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.view.View;

import com.shichuang.open.base.BaseActivity;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.entify.ExchangeGift;
import com.shichuang.sendnar.fragment.ClassifyFragment;

public class ClassifyActivity extends BaseActivity {

    private ExchangeGift exchangeGift;

    @Override
    public int getLayoutId() {
        return R.layout.activity_classify;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        exchangeGift = (ExchangeGift) getIntent().getSerializableExtra("exchangeGift");
        ClassifyFragment mClassifyFragment = new ClassifyFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("exchangeGift", exchangeGift);
        mClassifyFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, mClassifyFragment)
                .show(mClassifyFragment)
                .commit();
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }
}
