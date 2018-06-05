package com.shichuang.sendnar.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.widget.X5ProgressBarWebView;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.widget.RxTitleBar;
import com.shichuang.sendnar.widget.ShareDialog;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by Administrator on 2018/5/18.
 */

public class SinglePageActivity extends BaseActivity {
    private RxTitleBar mTitleBar;
    private X5ProgressBarWebView mWebView;
    private String mUrl;
    private String mTitle;
    // 是否显示分享
    private boolean isShowShare;
    // 分享页面使用
    private String id;


    @Override
    public int getLayoutId() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        return R.layout.activity_single_page;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mUrl = getIntent().getStringExtra("url");
        mTitle = getIntent().getStringExtra("title");
        isShowShare = getIntent().getBooleanExtra("isShowShare", false);
        id = getIntent().getStringExtra("id");
        mTitleBar = (RxTitleBar) findViewById(R.id.title_bar);
        mWebView = (X5ProgressBarWebView) findViewById(R.id.web_view);
        mWebView.setWebViewClient(new SinglePageActivity.WebClient());
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
            mTitleBar.setTitle(mTitle);
        }
        if (isShowShare) {
            mTitleBar.setRightIcon(R.drawable.ic_share);
            mTitleBar.setRightEnabled(true);
        } else {
            mTitleBar.setRightEnabled(false);
        }
    }

    @Override
    public void initEvent() {
        mTitleBar.setTitleBarClickListener(new RxTitleBar.TitleBarClickListener() {
            @Override
            public void onRightClick() {
                if(Utils.isLogin(mContext)){
                    share();
                }
            }
        });
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

    /**
     * 分享
     */
    private void share() {
        String url = Constants.MAIN_ENGINE_PIC + "/songnaerWechat/#/singlePage?type=8&from=share&user=" + TokenCache.userId(mContext)+"&id="+id;
        String title = "送哪儿";
        String img = "";
        String description = "送哪儿";
        Log.d("test",url);
        final ShareDialog mDialog = new ShareDialog(SinglePageActivity.this);
        mDialog.setWeb(url, title, img, description);
        mDialog.setListener(new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                showToast("分享成功");
                mDialog.dismiss();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                showToast("分享错误");
                mDialog.dismiss();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                showToast("分享取消");
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    public static void newInstance(Context context, String title, String url, boolean isShowShare,String id) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);
        bundle.putBoolean("isShowShare", isShowShare);
        bundle.putString("id", id);
        RxActivityTool.skipActivity(context, SinglePageActivity.class, bundle);
    }
}
