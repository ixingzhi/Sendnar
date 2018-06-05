package com.shichuang.open.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.lzy.okgo.OkGo;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxToastTool;
import com.shichuang.open.widget.RxLoadDialog;


/**
 * Created by Administrator on 2017/10/26.
 */

public abstract class BaseActivity extends AppCompatActivity implements IBaseView {
    protected static final String TAG = BaseActivity.class.getSimpleName();
    protected Context mContext;
    protected View mContentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        RxActivityTool.addActivity(this);
        mContentView = LayoutInflater.from(this).inflate(getLayoutId(), null);
        setContentView(mContentView);
        initView(savedInstanceState, mContentView);
        initEvent();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(mContext);
        mContentView = null;
    }

    /**
     * Toast
     * @param msg
     */
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
    public RxLoadDialog mLoadDialog;

    public void showLoading() {
        if (!isFinishing()) {
            dismissLoading();
            mLoadDialog = new RxLoadDialog(mContext);
            mLoadDialog.show();
        }
    }

    public void dismissLoading() {
        try {
            if (!isFinishing()
                    && mLoadDialog != null
                    && mLoadDialog.isShowing()) {
                mLoadDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        RxActivityTool.finish(this);
    }
}
