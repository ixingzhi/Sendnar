package com.shichuang.sendnar.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.shichuang.open.base.BaseFragment;
import com.shichuang.open.tool.RxStatusBarTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.entify.ExchangeGift;
import com.shichuang.sendnar.interf.OnTabReselectListener;

/**
 * Created by Administrator on 2018/4/17.
 */

public class ClassifyFragment extends BaseFragment implements OnTabReselectListener {
    private NavClassifyFragment mNavBar;
    private FragmentManager mFragmentManager;

    // 是否转赠商品
    private ExchangeGift exchangeGift;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_classify;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            exchangeGift = (ExchangeGift) bundle.getSerializable("exchangeGift");
        }

        if (exchangeGift == null) {
            View mViewStatusBar = view.findViewById(R.id.view_status_bar);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mViewStatusBar.getLayoutParams();
            params.height = RxStatusBarTool.getStatusBarHeight(mContext);
        } else {
            LinearLayout mLlLeft = view.findViewById(R.id.ll_left);
            mLlLeft.setVisibility(View.VISIBLE);
            mLlLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().finish();
                }
            });
        }

        mFragmentManager = getChildFragmentManager();
        mNavBar = ((NavClassifyFragment) mFragmentManager.findFragmentById(R.id.fag_nav));
        mNavBar.setArguments(bundle);
        mNavBar.setup(mContext, mFragmentManager, R.id.main_container, null);
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void initData() {

    }

    @Override
    public void onTabReselect() {
        if (mNavBar != null) {
            mNavBar.initData();
        }
    }
}
