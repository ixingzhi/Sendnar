package com.shichuang.sendnar.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.shichuang.open.base.BaseFragment;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.widget.NavigationButton;

import java.util.List;

/**
 * Created by xiedd on 2018/04/16.
 */

public class NavFragment extends BaseFragment implements View.OnClickListener {
    private NavigationButton mNavHome;
    private NavigationButton mNavClassify;
    private NavigationButton mNavShoppingCart;
    private NavigationButton mNavPersonalCenter;
    private Context mContext;
    private int mContainerId;
    private FragmentManager mFragmentManager;
    private NavigationButton mCurrentNavButton;
    private OnTabSelectedListener mOnTabSelectedListener;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_nav;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mNavHome = view.findViewById(R.id.nav_item_home);
        mNavClassify = view.findViewById(R.id.nav_item_classify);
        mNavShoppingCart = view.findViewById(R.id.nav_item_shopping_cart);
        mNavPersonalCenter = view.findViewById(R.id.nav_item_personal_center);

        mNavHome.init(R.drawable.tab_icon_home,
                R.string.main_tab_name_home,
                HomeFragment.class);
        mNavClassify.init(R.drawable.tab_icon_classify,
                R.string.main_tab_name_classify,
                ClassifyFragment.class);
        mNavShoppingCart.init(R.drawable.tab_icon_shopping_cart,
                R.string.main_tab_name_shopping_cart,
                ShoppingCartFragment.class);
        mNavPersonalCenter.init(R.drawable.tab_icon_personal_center,
                R.string.main_tab_name_personal_center,
                PersonalCenterFragment.class);
    }

    @Override
    public void initEvent() {
        mNavHome.setOnClickListener(this);
        mNavClassify.setOnClickListener(this);
        mNavShoppingCart.setOnClickListener(this);
        mNavPersonalCenter.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View view) {
        if (view instanceof NavigationButton) {
            NavigationButton nav = (NavigationButton) view;
            if (mNavShoppingCart == nav) {
                if(Utils.isLogin(mContext)){
                    doSelect(nav);
                }
            }else{
                doSelect(nav);
            }
        }
    }

    public void setup(Context context, FragmentManager fragmentManager, int contentId, OnTabSelectedListener listener) {
        mContext = context;
        mFragmentManager = fragmentManager;
        mContainerId = contentId;
        mOnTabSelectedListener = listener;

        // do clear
        clearOldFragment();
        // do select first
        doSelect(mNavHome);
    }

    @SuppressWarnings("RestrictedApi")
    private void clearOldFragment() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (transaction == null || fragments == null || fragments.size() == 0)
            return;
        boolean doCommit = false;
        for (Fragment fragment : fragments) {
            if (fragment != this && fragment != null) {
                transaction.remove(fragment);
                doCommit = true;
            }
        }
        if (doCommit)
            transaction.commitNow();
    }

    private void doSelect(NavigationButton newNavButton) {
        NavigationButton oldNavButton = null;
        if (mCurrentNavButton != null) {
            oldNavButton = mCurrentNavButton;
            if (oldNavButton == newNavButton) {  // 第二次点击相同,则执行
                onReselect(oldNavButton);
                return;
            }
            oldNavButton.setSelected(false);
        }
        newNavButton.setSelected(true);
        doTabChanged(oldNavButton, newNavButton);
        onSelected(newNavButton);
        mCurrentNavButton = newNavButton;
    }

    private void doTabChanged(NavigationButton oldNavButton, NavigationButton newNavButton) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (oldNavButton != null) {
            if (oldNavButton.getFragment() != null) {
                ft.detach(oldNavButton.getFragment());
            }
        }
        if (newNavButton != null) {
            if (newNavButton.getFragment() == null) {
                Fragment fragment = Fragment.instantiate(mContext,
                        newNavButton.getClx().getName(), null);
                ft.add(mContainerId, fragment, newNavButton.getTag());
                newNavButton.setFragment(fragment);
            } else {
                ft.attach(newNavButton.getFragment());
            }
        }
        ft.commit();
    }

    private void onSelected(NavigationButton newNavButton) {
        OnTabSelectedListener listener = mOnTabSelectedListener;
        if (listener != null) {
            listener.onTabSelected(newNavButton);
        }
    }

    private void onReselect(NavigationButton navigationButton) {
        OnTabSelectedListener listener = mOnTabSelectedListener;
        if (listener != null) {
            listener.onTabReselected(navigationButton);
        }
    }

    public interface OnTabSelectedListener {
        void onTabSelected(NavigationButton navigationButton);

        void onTabReselected(NavigationButton navigationButton);
    }

    public void setCurrentItem(int i) {
        switch (i) {
            case 0:
                doSelect(mNavHome);
                break;
            case 1:
                doSelect(mNavClassify);
                break;
            case 2:
                doSelect(mNavShoppingCart);
                break;
            case 3:
                doSelect(mNavPersonalCenter);
                break;
            default:
                break;
        }
    }

    public void showUnreadMessageCount(int i) {
        if (mNavShoppingCart != null) {
            mNavShoppingCart.showRedDot(i);
        }
    }
}
