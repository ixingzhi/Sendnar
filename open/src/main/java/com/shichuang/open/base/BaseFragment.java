package com.shichuang.open.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shichuang.open.tool.RxToastTool;
import com.shichuang.open.widget.RxLoadDialog;

/**
 * Created by Administrator on 2017/10/26.
 */

public abstract class BaseFragment extends Fragment implements IBaseView {
    protected static final String TAG = BaseFragment.class.getSimpleName();
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    protected Context mContext;
    protected View mContentView;
    protected LayoutInflater mInflater;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
        Log.d(TAG, "onCreate: ");
    }

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

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onDestroyView() {
        if (mContentView != null) {
            ((ViewGroup) mContentView.getParent()).removeView(mContentView);
        }
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    protected void showToast(final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RxToastTool.setGravity(Gravity.CENTER, 0, 0);
                RxToastTool.showShort(msg);
            }
        }).start();
    }

    /**
     * 加载框，适用于接口提交
     */
    protected RxLoadDialog mLoadDialog;

    protected void showLoading() {
        if (!getActivity().isFinishing()) {
            dismissLoading();
            mLoadDialog = new RxLoadDialog(mContext);
            mLoadDialog.show();
        }
    }

    protected void dismissLoading() {
        try {
            if (!getActivity().isFinishing()
                    && mLoadDialog != null
                    && mLoadDialog.isShowing()) {
                mLoadDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
