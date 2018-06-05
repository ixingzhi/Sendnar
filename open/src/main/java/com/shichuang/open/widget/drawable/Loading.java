package com.shichuang.open.widget.drawable;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.shichuang.open.R;

/**
 * Created by Administrator on 2017/12/8.
 */

public class Loading extends View {
    public static int STYLE_CIRCLE = 1;
    public static int STYLE_LINE = 2;
    private LoadingDrawable mLoadingDrawable;
    private boolean mAutoRun;
    private boolean mNeedRun;

    public Loading(Context context) {
        super(context);
        this.init((AttributeSet)null, 0, 0);
    }

    public Loading(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs, 0, 0);
    }

    public Loading(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public Loading(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init(attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        Context context = this.getContext();
        Resources resource = this.getResources();
        if(attrs == null) {
            this.setProgressStyle();
        } else {
            float density = resource.getDisplayMetrics().density;
            int baseSize = (int)(density * 2.0F);
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Loading, defStyleAttr, defStyleRes);
            int bgLineSize = a.getDimensionPixelOffset(R.styleable.Loading_gBackgroundLineSize, baseSize);
            int fgLineSize = a.getDimensionPixelOffset(R.styleable.Loading_gForegroundLineSize, baseSize);
            int bgColor = 0;
            ColorStateList colorStateList = a.getColorStateList(R.styleable.Loading_gBackgroundColor);
            if(colorStateList != null) {
                bgColor = colorStateList.getDefaultColor();
            }

            int fgColorId = a.getResourceId(R.styleable.Loading_gForegroundColor, R.array.g_default_loading_fg);
            boolean autoRun = a.getBoolean(R.styleable.Loading_gAutoRun, true);
            float progress = a.getFloat(R.styleable.Loading_gProgressFloat, 0.0F);
            a.recycle();
            this.setProgressStyle();
            this.setAutoRun(autoRun);
            this.setProgress(progress);
            this.setBackgroundLineSize(bgLineSize);
            this.setForegroundLineSize(fgLineSize);
            this.setBackgroundColor(bgColor);
            if(!this.isInEditMode()) {
                String type = resource.getResourceTypeName(fgColorId);

                try {
                    byte var18 = -1;
                    switch(type.hashCode()) {
                        case 93090393:
                            if(type.equals("array")) {
                                var18 = 1;
                            }
                            break;
                        case 94842723:
                            if(type.equals("color")) {
                                var18 = 0;
                            }
                    }

                    switch(var18) {
                        case 0:
                            this.setForegroundColor(resource.getColor(fgColorId));
                            break;
                        case 1:
                            this.setForegroundColor(resource.getIntArray(fgColorId));
                            break;
                        default:
                            this.setForegroundColor(resource.getIntArray(R.array.g_default_loading_fg));
                    }
                } catch (Exception var19) {
                    this.setForegroundColor(resource.getIntArray(R.array.g_default_loading_fg));
                }
            }

        }
    }

    public void start() {
        this.mLoadingDrawable.start();
        this.mNeedRun = false;
    }

    public void stop() {
        this.mLoadingDrawable.stop();
        this.mNeedRun = false;
    }

    public boolean isRunning() {
        return this.mLoadingDrawable.isRunning();
    }

    public void setBackgroundLineSize(int size) {
        this.mLoadingDrawable.setBackgroundLineSize((float)size);
        this.invalidate();
        this.requestLayout();
    }

    public void setForegroundLineSize(int size) {
        this.mLoadingDrawable.setForegroundLineSize((float)size);
        this.invalidate();
        this.requestLayout();
    }

    public float getBackgroundLineSize() {
        return this.mLoadingDrawable.getBackgroundLineSize();
    }

    public float getForegroundLineSize() {
        return this.mLoadingDrawable.getForegroundLineSize();
    }

    public void setBackgroundColor(int color) {
        this.mLoadingDrawable.setBackgroundColor(color);
        this.invalidate();
    }

    public void setBackgroundColorRes(int colorRes) {
        ColorStateList colorStateList = this.getResources().getColorStateList(colorRes);
        if(colorStateList == null) {
            this.setBackgroundColor(0);
        } else {
            this.setBackgroundColor(colorStateList.getDefaultColor());
        }

    }

    public int getBackgroundColor() {
        return this.mLoadingDrawable.getBackgroundColor();
    }

    public void setForegroundColor(int color) {
        this.setForegroundColor(new int[]{color});
    }

    public void setForegroundColor(int[] colors) {
        this.mLoadingDrawable.setForegroundColor(colors);
        this.invalidate();
    }

    public int[] getForegroundColor() {
        return this.mLoadingDrawable.getForegroundColor();
    }

    public float getProgress() {
        return this.mLoadingDrawable.getProgress();
    }

    public void setProgress(float progress) {
        this.mLoadingDrawable.setProgress(progress);
        this.invalidate();
    }

    public void setAutoRun(boolean autoRun) {
        this.mAutoRun = autoRun;
    }

    public boolean isAutoRun() {
        return this.mAutoRun;
    }

    public void setProgressStyle() {
        LoadingDrawable drawable = null;
        Resources resources = this.getResources();
        drawable = new LoadingCircleDrawable(resources.getDimensionPixelOffset(R.dimen.g_loading_minSize), resources.getDimensionPixelOffset(R.dimen.g_loading_maxSize));
        drawable.setCallback(this);
        this.mLoadingDrawable = drawable;
        this.invalidate();
        this.requestLayout();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int iHeight = this.mLoadingDrawable.getIntrinsicHeight() + this.getPaddingTop() + this.getPaddingBottom();
        int iWidth = this.mLoadingDrawable.getIntrinsicWidth() + this.getPaddingLeft() + this.getPaddingRight();
        int measuredWidth;
        if(widthMode == 1073741824) {
            measuredWidth = widthSize;
        } else if(widthMode == -2147483648) {
            measuredWidth = Math.min(widthSize, iWidth);
        } else {
            measuredWidth = iWidth;
        }

        int measuredHeight;
        if(heightMode == 1073741824) {
            measuredHeight = heightSize;
        } else if(heightMode == -2147483648) {
            measuredHeight = Math.min(heightSize, iHeight);
        } else {
            measuredHeight = iHeight;
        }

        this.setMeasuredDimension(measuredWidth, measuredHeight);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int paddingLeft = this.getPaddingLeft();
        int paddingTop = this.getPaddingTop();
        int paddingRight = this.getPaddingRight();
        int paddingBottom = this.getPaddingBottom();
        this.mLoadingDrawable.setBounds(paddingLeft, paddingTop, w - paddingRight, h - paddingBottom);
    }

    protected boolean verifyDrawable(Drawable who) {
        return who == this.mLoadingDrawable || super.verifyDrawable(who);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mLoadingDrawable.draw(canvas);
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        this.changeRunStateByVisibility(visibility);
    }

    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        this.changeRunStateByVisibility(visibility);
    }

    private void changeRunStateByVisibility(int visibility) {
        if(this.mLoadingDrawable != null) {
            if(visibility == 0) {
                if(this.mNeedRun) {
                    this.start();
                }
            } else if(this.mLoadingDrawable.isRunning()) {
                this.mNeedRun = true;
                this.mLoadingDrawable.stop();
            }

        }
    }

    @SuppressLint("WrongConstant")
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(this.mAutoRun && this.mLoadingDrawable.getProgress() == 0.0F) {
            if(this.getVisibility() == 0) {
                this.mLoadingDrawable.start();
            } else {
                this.mNeedRun = true;
            }
        }

    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mLoadingDrawable.stop();
    }
}
