package com.shichuang.open.widget.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Administrator on 2017/12/8.
 */

public class LoadingCircleDrawable extends LoadingDrawable {
    private static final int ANGLE_ADD = 5;
    private static final int MIN_ANGLE_SWEEP = 3;
    private static final int MAX_ANGLE_SWEEP = 255;
    private static int DEFAULT_SIZE = 56;
    private int mMinSize;
    private int mMaxSize;
    private RectF mOval;
    private float mStartAngle;
    private float mSweepAngle;
    private int mAngleIncrement;

    public LoadingCircleDrawable() {
        this.mMinSize = DEFAULT_SIZE;
        this.mMaxSize = DEFAULT_SIZE;
        this.mOval = new RectF();
        this.mStartAngle = 0.0F;
        this.mSweepAngle = 0.0F;
        this.mAngleIncrement = -3;
        this.mForegroundPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public LoadingCircleDrawable(int minSize, int maxSize) {
        this.mMinSize = DEFAULT_SIZE;
        this.mMaxSize = DEFAULT_SIZE;
        this.mOval = new RectF();
        this.mStartAngle = 0.0F;
        this.mSweepAngle = 0.0F;
        this.mAngleIncrement = -3;
        this.mMinSize = minSize;
        this.mMaxSize = maxSize;
    }

    public int getIntrinsicHeight() {
        float maxLine = Math.max(this.mBackgroundPaint.getStrokeWidth(), this.mForegroundPaint.getStrokeWidth());
        int size = (int)(maxLine * 2.0F + 10.0F);
        return Math.min(this.mMaxSize, Math.max(size, this.mMinSize));
    }

    public int getIntrinsicWidth() {
        float maxLine = Math.max(this.mBackgroundPaint.getStrokeWidth(), this.mForegroundPaint.getStrokeWidth());
        int size = (int)(maxLine * 2.0F + 10.0F);
        return Math.min(this.mMaxSize, Math.max(size, this.mMinSize));
    }

    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        if(bounds.left != 0 || bounds.top != 0 || bounds.right != 0 || bounds.bottom != 0) {
            int centerX = bounds.centerX();
            int centerY = bounds.centerY();
            int radius = Math.min(bounds.height(), bounds.width()) >> 1;
            int maxStrokeRadius = ((int)Math.max(this.getForegroundLineSize(), this.getBackgroundLineSize()) >> 1) + 1;
            int areRadius = radius - maxStrokeRadius;
            this.mOval.set((float)(centerX - areRadius), (float)(centerY - areRadius), (float)(centerX + areRadius), (float)(centerY + areRadius));
        }
    }

    protected void onProgressChange(float progress) {
        this.mStartAngle = 0.0F;
        this.mSweepAngle = 360.0F * progress;
    }

    protected void onRefresh() {
        float angle = 5.0F;
        this.mStartAngle += 5.0F;
        if(this.mStartAngle > 360.0F) {
            this.mStartAngle -= 360.0F;
        }

        if(this.mSweepAngle > 255.0F) {
            this.mAngleIncrement = -this.mAngleIncrement;
        } else {
            if(this.mSweepAngle < 3.0F) {
                this.mSweepAngle = 3.0F;
                return;
            }

            if(this.mSweepAngle == 3.0F) {
                this.mAngleIncrement = -this.mAngleIncrement;
                this.getNextForegroundColor();
            }
        }

        this.mSweepAngle += (float)this.mAngleIncrement;
    }

    protected void drawBackground(Canvas canvas, Paint backgroundPaint) {
        canvas.drawArc(this.mOval, 0.0F, 360.0F, false, backgroundPaint);
    }

    protected void drawForeground(Canvas canvas, Paint foregroundPaint) {
        canvas.drawArc(this.mOval, this.mStartAngle, -this.mSweepAngle, false, foregroundPaint);
    }
}