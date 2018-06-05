package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.shichuang.open.base.BaseActivity;
import com.shichuang.sendnar.R;

/**
 * Created by Administrator on 2018/5/22.
 */

public class TestActivity extends BaseActivity {
    //    private RecyclerView mRecyclerView;
//    private TestAdapter mAdapter;
    private LinearLayout mLlMainLayout;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
//        mRecyclerView = view.findViewById(R.id.recycler_view);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        mAdapter = new TestAdapter();
//        mRecyclerView.setAdapter(mAdapter);

        /**
         *      root  ==  null,不是不添加到任何父容器里，第三个参数失效，子View设置的参数发生变化
         *
         *
         *      root  !=  null,attachToRoot == true,自动将改布局添加到父容器，子View设置的参数不变
         *      root  !=  null,attachToRoot == false,没有添加到父容器，子View设置的参数不变
         */


        mLlMainLayout = view.findViewById(R.id.ll_main_layout);
        View layoutView = LayoutInflater.from(mContext).inflate(R.layout.layout_test_01, mLlMainLayout,false);
        mLlMainLayout.addView(layoutView);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {
        //mAdapter.addData("xx");
    }
}
