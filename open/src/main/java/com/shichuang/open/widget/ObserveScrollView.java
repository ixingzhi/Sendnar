package com.shichuang.open.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by xiedd on 2018/1/16.
 */

public class ObserveScrollView extends ScrollView {
    public ObserveScrollView(Context context) {
        super(context);
    }

    public ObserveScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObserveScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollListener != null) {
            scrollListener.onScroll(l, t, oldl, oldt);
        }
    }

    private ScrollListener scrollListener;

    public interface ScrollListener {
        void onScroll(int l, int t, int oldl, int oldt);
    }

    public void setOnScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }
}