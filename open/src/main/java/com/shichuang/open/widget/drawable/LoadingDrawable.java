package com.shichuang.open.widget.drawable;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;

/**
 * Created by Administrator on 2017/12/8.
 */

public abstract class LoadingDrawable extends Drawable implements Animatable, com.shichuang.open.widget.drawable.Animatable {
    private static final int LINE_SIZE = 4;
    protected Paint mForegroundPaint = new Paint(1);
    protected Paint mBackgroundPaint = new Paint(1);
    private boolean mRun;
    private int[] mForegroundColor = new int[]{-872415232, -100251, -8117352};
    private int mForegroundColorIndex = 0;
    protected float mProgress;
    private final Runnable mAnim = new Runnable() {
        public void run() {
            if(LoadingDrawable.this.mRun) {
                LoadingDrawable.this.onRefresh();
                LoadingDrawable.this.invalidateSelf();
            } else {
                LoadingDrawable.this.unscheduleSelf(this);
            }

        }
    };

    public LoadingDrawable() {
        Paint bPaint = this.mBackgroundPaint;
        bPaint.setStyle(Paint.Style.STROKE);
        bPaint.setAntiAlias(true);
        bPaint.setDither(true);
        bPaint.setStrokeWidth(4.0F);
        bPaint.setColor(838860800);
        Paint fPaint = this.mForegroundPaint;
        fPaint.setStyle(Paint.Style.STROKE);
        fPaint.setAntiAlias(true);
        fPaint.setDither(true);
        fPaint.setStrokeWidth(4.0F);
        fPaint.setColor(this.mForegroundColor[0]);
    }

    public int getIntrinsicHeight() {
        float maxLine = Math.max(this.mBackgroundPaint.getStrokeWidth(), this.mForegroundPaint.getStrokeWidth());
        return (int)(maxLine * 2.0F);
    }

    public int getIntrinsicWidth() {
        float maxLine = Math.max(this.mBackgroundPaint.getStrokeWidth(), this.mForegroundPaint.getStrokeWidth());
        return (int)(maxLine * 2.0F);
    }

    public void setBackgroundLineSize(float size) {
        this.mBackgroundPaint.setStrokeWidth(size);
        this.onBoundsChange(this.getBounds());
    }

    public void setForegroundLineSize(float size) {
        this.mForegroundPaint.setStrokeWidth(size);
        this.onBoundsChange(this.getBounds());
    }

    public float getBackgroundLineSize() {
        return this.mBackgroundPaint.getStrokeWidth();
    }

    public float getForegroundLineSize() {
        return this.mForegroundPaint.getStrokeWidth();
    }

    public void setBackgroundColor(int color) {
        this.mBackgroundPaint.setColor(color);
    }

    public int getBackgroundColor() {
        return this.mBackgroundPaint.getColor();
    }

    public void setForegroundColor(int[] colors) {
        if(colors != null) {
            this.mForegroundColor = colors;
            this.mForegroundColorIndex = -1;
            this.getNextForegroundColor();
        }
    }

    public int[] getForegroundColor() {
        return this.mForegroundColor;
    }

    int getNextForegroundColor() {
        int[] colors = this.mForegroundColor;
        Paint fPaint = this.mForegroundPaint;
        if(colors.length > 1) {
            int index = this.mForegroundColorIndex + 1;
            if(index >= colors.length) {
                index = 0;
            }

            fPaint.setColor(colors[index]);
            this.mForegroundColorIndex = index;
        } else {
            fPaint.setColor(colors[0]);
        }

        return fPaint.getColor();
    }

    public float getProgress() {
        return this.mProgress;
    }

    public void setProgress(float progress) {
        if(progress < 0.0F) {
            this.mProgress = 0.0F;
        } else if(this.mProgress > 1.0F) {
            this.mProgress = 1.0F;
        } else {
            this.mProgress = progress;
        }

        this.stop();
        this.onProgressChange(this.mProgress);
        this.invalidateSelf();
    }

    public boolean isRunning() {
        return this.mRun;
    }

    public void start() {
        if(!this.mRun) {
            this.mRun = true;
            this.scheduleSelf(this.mAnim, SystemClock.uptimeMillis() + 16L);
        }

    }

    public void stop() {
        if(this.mRun) {
            this.mRun = false;
            this.unscheduleSelf(this.mAnim);
            this.invalidateSelf();
        }

    }

    public void draw(Canvas canvas) {
        int count = canvas.save();
        Paint bPaint = this.mBackgroundPaint;
        if(bPaint.getColor() != 0 && bPaint.getStrokeWidth() > 0.0F) {
            this.drawBackground(canvas, bPaint);
        }

        Paint fPaint = this.mForegroundPaint;
        if(this.mRun) {
            if(fPaint.getColor() != 0 && fPaint.getStrokeWidth() > 0.0F) {
                this.drawForeground(canvas, fPaint);
            }

            this.scheduleSelf(this.mAnim, SystemClock.uptimeMillis() + 16L);
        } else if(this.mProgress > 0.0F && fPaint.getColor() != 0 && fPaint.getStrokeWidth() > 0.0F) {
            this.drawForeground(canvas, fPaint);
        }

        canvas.restoreToCount(count);
    }

    public void setAlpha(int alpha) {
        this.mForegroundPaint.setAlpha(alpha);
    }

    public void setColorFilter(ColorFilter cf) {
        boolean needRefresh = false;
        Paint bPaint = this.mBackgroundPaint;
        if(bPaint.getColorFilter() != cf) {
            bPaint.setColorFilter(cf);
            needRefresh = true;
        }

        Paint fPaint = this.mForegroundPaint;
        if(fPaint.getColorFilter() != cf) {
            fPaint.setColorFilter(cf);
            needRefresh = true;
        }

        if(needRefresh) {
            this.invalidateSelf();
        }

    }

    @SuppressLint("WrongConstant")
    public int getOpacity() {
        Paint bPaint = this.mBackgroundPaint;
        Paint fPaint = this.mForegroundPaint;
        if(bPaint.getXfermode() == null && fPaint.getXfermode() == null) {
            int alpha = Color.alpha(fPaint.getColor());
            if(alpha == 0) {
                return -2;
            }

            if(alpha == 255) {
                return -1;
            }
        }

        return -3;
    }

    protected abstract void onRefresh();

    protected abstract void drawBackground(Canvas var1, Paint var2);

    protected abstract void drawForeground(Canvas var1, Paint var2);

    protected abstract void onProgressChange(float var1);
}