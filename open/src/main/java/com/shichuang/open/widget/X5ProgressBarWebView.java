package com.shichuang.open.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by Administrator on 2018/1/9.
 */

public class X5ProgressBarWebView extends X5WebView {
    private WebViewProgressBar mProgressBar;
    private Callback mCallback;

    public X5ProgressBarWebView(Context context) {
        super(context);
        initProgress();
    }

    public X5ProgressBarWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initProgress();
    }

    public X5ProgressBarWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initProgress();
    }

    private void initProgress() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mProgressBar = new WebViewProgressBar(getContext());
        mProgressBar.setLayoutParams(layoutParams);
        mProgressBar.setVisibility(View.GONE);
        mProgressBar.setAlpha(1.0f);

        addView(mProgressBar);
        setWebChromeClient(new ProgressWebChromeClient());
    }

    private class ProgressWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(com.tencent.smtt.sdk.WebView webView, int newProgress) {
            if (newProgress <= 100 && mProgressBar != null) {
                if (View.GONE == mProgressBar.getVisibility()) {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                startProgressAnimation(newProgress);
            }
            super.onProgressChanged(webView, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView webView, String title) {
            super.onReceivedTitle(webView, title);
            if (mCallback != null && !TextUtils.isEmpty(title)) {
                mCallback.setTitle(title);
            }
        }
    }

    private ObjectAnimator mAnimator;
    private int mLastProgress = 0;

    private void startProgressAnimation(final int newProgress) {
        if (mAnimator != null) {
            mAnimator.cancel();
        }

        mAnimator = ObjectAnimator.ofInt(mProgressBar, "progress", mLastProgress, newProgress);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLastProgress = (int) animation.getAnimatedValue();
                mProgressBar.setProgress(mLastProgress);
            }
        });

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (mLastProgress >= 100 && mProgressBar != null) {
                    mProgressBar.setVisibility(GONE);
                    mLastProgress = 0;
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new DecelerateInterpolator());
        mAnimator.start();
    }

    public interface Callback {
        void setTitle(String title);
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

}
