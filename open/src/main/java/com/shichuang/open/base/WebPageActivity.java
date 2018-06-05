package com.shichuang.open.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.shichuang.open.R;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.widget.X5ProgressBarWebView;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by Administrator on 2018/1/9.
 */

public class WebPageActivity extends BaseActivity {
    private Toolbar mToolbar;
    private X5ProgressBarWebView mWebView;
    private String mUrl;
    private String mTitle;


    @Override
    public int getLayoutId() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        return R.layout.biz_actvitiy_web_page;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mUrl = getIntent().getStringExtra("url");
        mTitle = getIntent().getStringExtra("title");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mWebView = (X5ProgressBarWebView) findViewById(R.id.web_view);
        mWebView.setWebViewClient(new WebClient());
        mWebView.setCallback(new X5ProgressBarWebView.Callback() {

            @Override
            public void setTitle(String title) {
                if (getSupportActionBar() != null && TextUtils.isEmpty(mTitle)) {
                    getSupportActionBar().setTitle(mTitle);
                }
            }
        });
        mWebView.loadUrl(mUrl);
        if (!TextUtils.isEmpty(mTitle)) {
            getSupportActionBar().setTitle(mTitle);
            //mToolbar.setTitle(mTitle);
        }
    }

    @Override
    public void initEvent() {
    }

    @Override
    public void initData() {
    }

    private class WebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
            super.onPageStarted(webView, s, bitmap);
        }

        @Override
        public void onPageFinished(WebView webView, String s) {
            super.onPageFinished(webView, s);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String s) {
            return super.shouldOverrideUrlLoading(webView, s);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            RxActivityTool.finish(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            try {
                ViewGroup parent = (ViewGroup) mWebView.getParent();
                if (parent != null) {
                    parent.removeView(mWebView);
                }
                mWebView.removeAllViews();
                mWebView.destroy();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    public static void newInstance(Context context, String title, String url) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);
        RxActivityTool.skipActivity(context, WebPageActivity.class, bundle);
    }

}
