package com.shichuang.sendnar.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.tool.RxToastTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.GiftsCategory;
import com.shichuang.sendnar.widget.NavigationClassifyButton;

import java.util.List;

/**
 * 分类页面，左侧Tab
 * Created by xiedd on 2018/04/16.
 */

public class NavClassifyFragment extends Fragment implements View.OnClickListener {
    protected Context mContext;
    protected View mContentView;
    protected LayoutInflater mInflater;

    private LinearLayout mLlNavContainer;
    private int mContainerId;
    private FragmentManager mFragmentManager;
    private NavigationClassifyButton mCurrentNavButton;
    private OnTabSelectedListener mOnTabSelectedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mContentView != null) {
            ViewGroup viewGroup = (ViewGroup) mContentView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(mContentView);
            }
        } else {
            mContentView = inflater.inflate(getLayoutId(), null);
            mInflater = inflater;
            initView(savedInstanceState, mContentView);
            initEvent();
            initData();
        }
        return mContentView;
    }

    public int getLayoutId() {
        return R.layout.fragment_nav_classify;
    }

    public void initView(Bundle savedInstanceState, View view) {
        mLlNavContainer = view.findViewById(R.id.ll_nav_container);
        mLlNavContainer.removeAllViews();
    }

    public void initEvent() {
    }

    public void initData() {
        getCategoryData();
    }

    /**
     * 获取分类数据
     */
    private void getCategoryData() {
        OkGo.<AMBaseDto<GiftsCategory>>get(Constants.getCategoryUrl)
                .tag(mContext)
                .execute(new NewsCallback<AMBaseDto<GiftsCategory>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<GiftsCategory>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<GiftsCategory>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            handleData(response.body().data);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<GiftsCategory>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private void handleData(GiftsCategory giftsCategory) {
        mLlNavContainer.removeAllViews();
        // 价格集合
        List<GiftsCategory.TypeList> priceList = giftsCategory.getPriceList();
        // 类型集合
        List<GiftsCategory.TypeList> typeList = giftsCategory.getTypeList();
        if (priceList != null) {
            for (int i = 0; i < priceList.size(); i++) {
                NavigationClassifyButton mNav = new NavigationClassifyButton(mContext);
                mNav.isType(false);
                mNav.init(priceList.get(i).getId(), priceList.get(i).getName(), priceList.get(i).getSkip() == 1 ? GiftsCategoryType1Fragment.class : GiftsCategoryType2Fragment.class);
                mNav.setOnClickListener(NavClassifyFragment.this);
                mLlNavContainer.addView(mNav);
            }
        }
        if (typeList != null) {
            for (int i = 0; i < typeList.size(); i++) {
                // 不需要显示礼品
                if("礼品".equals(typeList.get(i).getName())){
                    continue;
                }

                NavigationClassifyButton mNav = new NavigationClassifyButton(mContext);
                if("专区".equals(typeList.get(i).getName()) || "礼品".equals(typeList.get(i).getName()) ){
                    mNav.isType(true);
                }else{
                    mNav.isType(false);
                }
                mNav.init(typeList.get(i).getId(), typeList.get(i).getName(), typeList.get(i).getSkip() == 1 ? GiftsCategoryType1Fragment.class : GiftsCategoryType2Fragment.class);
                mNav.setOnClickListener(NavClassifyFragment.this);
                mLlNavContainer.addView(mNav);
            }
        }
        // do select first
        if (mLlNavContainer.getChildCount() > 0 && mLlNavContainer.getChildAt(0) != null) {
            doSelect((NavigationClassifyButton) mLlNavContainer.getChildAt(0));
        }
    }

    @Override
    public void onClick(View view) {
        if (view instanceof NavigationClassifyButton) {
            NavigationClassifyButton nav = (NavigationClassifyButton) view;
            doSelect(nav);
        }
    }

    public void setup(Context context, FragmentManager fragmentManager, int contentId, OnTabSelectedListener listener) {
        //mContext = context;
        mFragmentManager = fragmentManager;
        mContainerId = contentId;
        mOnTabSelectedListener = listener;

        // do clear
        clearOldFragment();
        // do select first
        if (mLlNavContainer.getChildCount() > 0 && mLlNavContainer.getChildAt(0) != null) {
            doSelect((NavigationClassifyButton) mLlNavContainer.getChildAt(0));
        }
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

    private void doSelect(NavigationClassifyButton newNavButton) {
        NavigationClassifyButton oldNavButton = null;
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

    private void doTabChanged(NavigationClassifyButton oldNavButton, NavigationClassifyButton newNavButton) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (oldNavButton != null) {
            if (oldNavButton.getFragment() != null) {
                ft.detach(oldNavButton.getFragment());
            }
        }
        if (newNavButton != null) {
            if (newNavButton.getFragment() == null) {
                Bundle bundle = new Bundle();
                bundle.putInt("typeId", newNavButton.getTypeId());
                bundle.putInt("priceTypeId", newNavButton.getPriceTypeId());
                Fragment fragment = Fragment.instantiate(mContext,
                        newNavButton.getClx().getName(), bundle);
                ft.add(mContainerId, fragment, newNavButton.getTag());
                newNavButton.setFragment(fragment);
            } else {
                ft.attach(newNavButton.getFragment());
            }
        }
        ft.commit();
    }

    private void onSelected(NavigationClassifyButton newNavButton) {
        OnTabSelectedListener listener = mOnTabSelectedListener;
        if (listener != null) {
            listener.onTabSelected(newNavButton);
        }
    }

    private void onReselect(NavigationClassifyButton navigationButton) {
        OnTabSelectedListener listener = mOnTabSelectedListener;
        if (listener != null) {
            listener.onTabReselected(navigationButton);
        }
    }

    public interface OnTabSelectedListener {
        void onTabSelected(NavigationClassifyButton navigationButton);

        void onTabReselected(NavigationClassifyButton navigationButton);
    }

}
