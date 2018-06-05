package com.shichuang.sendnar.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.sendnar.R;


/**
 * Created by Administrator on 2017/9/20.
 */

public class RxTitleBar extends LinearLayout implements View.OnClickListener {
    private LinearLayout mLlLeft;
    private TextView mTvTitle;
    private LinearLayout mLlRight;
    private TextView mTvRight;
    private ImageView mIvRight;
    private TitleBarClickListener titleBarClickListener;

    public RxTitleBar(Context context) {
        this(context, null);
    }

    public RxTitleBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RxTitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RxTitleBar, defStyleAttr, 0);
        String title = array.getString(R.styleable.RxTitleBar_tb_title);
        String rightText = array.getString(R.styleable.RxTitleBar_tb_right_title);
        //int rightIcon = array.getInteger(R.styleable.RxTitleBar_tb_right_icon, 0);
        Drawable rightIcon = array.getDrawable(R.styleable.RxTitleBar_tb_right_icon);


        LayoutInflater.from(getContext()).inflate(R.layout.layout_title_bar, this);
        mLlLeft = (LinearLayout) findViewById(R.id.ll_left);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mLlRight = (LinearLayout) findViewById(R.id.ll_right);
        mTvRight = (TextView) findViewById(R.id.tv_right);
        mIvRight = (ImageView) findViewById(R.id.iv_right);

        mLlLeft.setOnClickListener(this);
        mLlRight.setOnClickListener(this);

        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }
        if (!TextUtils.isEmpty(rightText)) {
            setRightText(rightText);
        }
        if (rightIcon != null) {
            setRightIcon(rightIcon);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_left:
                RxActivityTool.finish(getContext());
                break;
            case R.id.ll_right:
                if (titleBarClickListener != null) {
                    titleBarClickListener.onRightClick();
                }
                break;
            default:
                break;
        }
    }

    public interface TitleBarClickListener {
        void onRightClick();
    }

    public void setTitleBarClickListener(TitleBarClickListener titleBarClickListener) {
        this.titleBarClickListener = titleBarClickListener;
    }

    /**
     * 设置标题
     */
    public void setTitle(String title) {
        mTvTitle.setText(title == null ? "" : title);
    }

    /**
     * 设置右边文字
     */
    public void setRightText(String title) {
        mTvRight.setVisibility(View.VISIBLE);
        mIvRight.setVisibility(View.GONE);
        mTvRight.setText(title == null ? "" : title);
    }

    /**
     * 设置右边图片
     */
    public void setRightIcon(int resId) {
        mTvRight.setVisibility(View.GONE);
        mIvRight.setVisibility(View.VISIBLE);
        mIvRight.setImageResource(resId);
    }

    /**
     * 设置右边图片
     */
    public void setRightIcon(Drawable dra) {
        mTvRight.setVisibility(View.GONE);
        mIvRight.setVisibility(View.VISIBLE);
        mIvRight.setImageDrawable(dra);
    }

    /**
     * 设置右边不可点击
     */
    public void setRightEnabled(boolean bool){
        mLlRight.setEnabled(bool);
    }

    /**
     * 获取控件
     */
    public View getView(int resId) {
        if (resId != 0) {
            return findViewById(resId);
        } else {
            return null;
        }
    }
}
