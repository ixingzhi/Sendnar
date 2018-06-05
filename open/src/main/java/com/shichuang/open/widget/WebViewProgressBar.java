package com.shichuang.open.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.shichuang.open.R;


/**
 * webview 顶部进度条
 * <p>
 */
public class WebViewProgressBar extends View {

	private final static int HEIGHT = 5;

	private int progress = 1;
	private Paint paint;

	public WebViewProgressBar(Context context) {
		this(context, null);
	}

	public WebViewProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public WebViewProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initPaint(context);
	}

	private void initPaint(Context context) {
		paint = new Paint(Paint.DITHER_FLAG);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(HEIGHT);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setColor(context.getResources().getColor(R.color.colorPrimary));
	}

	public void setProgress(int progress) {
		this.progress = progress;
		invalidate();
	}

	public int getProgress() {
		return progress;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawRect(0, 0, getWidth() * progress / 100, HEIGHT, paint);
	}
}
