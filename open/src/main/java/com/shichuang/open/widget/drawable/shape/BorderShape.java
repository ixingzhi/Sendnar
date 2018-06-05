package com.shichuang.open.widget.drawable.shape;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.shapes.Shape;

/**
 * Created by Administrator on 2017/12/8.
 */

public class BorderShape extends Shape {
    private RectF mBorder;
    private DashPathEffect mPathEffect;
    private Path mPath;

    public BorderShape(RectF border) {
        this(border, 0.0F, 0.0F);
    }

    public BorderShape(RectF border, float dashWidth, float dashGap) {
        this.mBorder = null;
        this.mPathEffect = null;
        this.mPath = null;
        if(border.left != 0.0F || border.top != 0.0F || border.right != 0.0F || border.bottom != 0.0F) {
            this.mBorder = border;
            if(dashWidth > 0.0F && dashGap > 0.0F) {
                this.mPathEffect = new DashPathEffect(new float[]{dashWidth, dashGap}, 0.0F);
                this.mPath = new Path();
            }
        }

    }

    public void setBorder(RectF border) {
        if(border.left == 0.0F && border.top == 0.0F && border.right == 0.0F && border.bottom == 0.0F) {
            this.mBorder = null;
        } else if(this.mBorder == null) {
            this.mBorder = new RectF(border);
        } else {
            this.mBorder.set(border);
        }

    }

    public RectF getBorder(RectF border) {
        if(this.mBorder != null && border != null) {
            border.set(this.mBorder);
        }

        return border;
    }

    public void draw(Canvas canvas, Paint paint) {
        if(this.mBorder != null) {
            float width = this.getWidth();
            float height = this.getHeight();
            if(this.mPathEffect == null) {
                if(this.mBorder.left > 0.0F) {
                    canvas.drawRect(0.0F, 0.0F, this.mBorder.left, height, paint);
                }

                if(this.mBorder.top > 0.0F) {
                    canvas.drawRect(0.0F, 0.0F, width, this.mBorder.top, paint);
                }

                if(this.mBorder.right > 0.0F) {
                    canvas.drawRect(width - this.mBorder.right, 0.0F, width, height, paint);
                }

                if(this.mBorder.bottom > 0.0F) {
                    canvas.drawRect(0.0F, height - this.mBorder.bottom, width, height, paint);
                }
            } else {
                if(paint.getPathEffect() != this.mPathEffect) {
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setPathEffect(this.mPathEffect);
                }

                if(this.mBorder.left > 0.0F) {
                    paint.setStrokeWidth(this.mBorder.left);
                    this.initPath(this.mBorder.left / 2.0F, 0.0F, this.mBorder.left / 2.0F, height);
                    canvas.drawPath(this.mPath, paint);
                }

                if(this.mBorder.top > 0.0F) {
                    paint.setStrokeWidth(this.mBorder.top);
                    this.initPath(0.0F, this.mBorder.top / 2.0F, width, this.mBorder.top / 2.0F);
                    canvas.drawPath(this.mPath, paint);
                }

                if(this.mBorder.right > 0.0F) {
                    paint.setStrokeWidth(this.mBorder.right);
                    this.initPath(width - this.mBorder.right / 2.0F, 0.0F, width - this.mBorder.right / 2.0F, height);
                    canvas.drawPath(this.mPath, paint);
                }

                if(this.mBorder.bottom > 0.0F) {
                    paint.setStrokeWidth(this.mBorder.bottom);
                    this.initPath(0.0F, height - this.mBorder.bottom / 2.0F, width, height - this.mBorder.bottom / 2.0F);
                    canvas.drawPath(this.mPath, paint);
                }
            }

        }
    }

    private void initPath(float startX, float startY, float endX, float endY) {
        this.mPath.reset();
        this.mPath.moveTo(startX, startY);
        this.mPath.lineTo(endX, endY);
    }
}

