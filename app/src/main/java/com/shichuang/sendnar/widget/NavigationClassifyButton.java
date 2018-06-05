package com.shichuang.sendnar.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shichuang.sendnar.R;


/**
 * Created by xiedd on 2017/12/04.
 */
public class NavigationClassifyButton extends FrameLayout {
    private Fragment mFragment = null;
    private Class<?> mClx;
    private View mIconView;
    private TextView mTitleView;
    private RelativeLayout mLlBackground;
    private String mTag;
    // 是否（专区，礼品）区分
    private boolean isType;
    private int typeId;
    private int priceTypeId;

    public NavigationClassifyButton(Context context) {
        super(context);
        init();
    }

    public NavigationClassifyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NavigationClassifyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NavigationClassifyButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.layout_nav_classify_item, this, true);

        mIconView = (View) findViewById(R.id.nav_iv_icon);
        mTitleView = (TextView) findViewById(R.id.nav_tv_title);
        mLlBackground = (RelativeLayout) findViewById(R.id.rl_background);

    }

    public void setSelected(boolean selected) {
        super.setSelected(selected);
        mIconView.setVisibility(selected ? View.VISIBLE : View.INVISIBLE);
        mTitleView.setSelected(selected);
        mLlBackground.setSelected(selected);
    }

    /**
     * 是否节日类型
     */
    public void isType(boolean bool) {
        this.isType = bool;
    }

    public void init(@StringRes int strId, Class<?> clx) {
        mTitleView.setText(strId);
        mClx = clx;
        mTag = mClx.getName();
    }

    public void init(int typeId, String str, Class<?> clx) {
        if (isType) {
            this.typeId = typeId;
        } else {
            this.priceTypeId = typeId;
        }
        mTitleView.setText(str);
        mClx = clx;
        mTag = mClx.getName();
    }

    public Class<?> getClx() {
        return mClx;
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public void setFragment(Fragment fragment) {
        this.mFragment = fragment;
    }

    public String getTag() {
        return mTag;
    }

    public int getTypeId() {
        return typeId;
    }

    public int getPriceTypeId() {
        return priceTypeId;
    }
}
