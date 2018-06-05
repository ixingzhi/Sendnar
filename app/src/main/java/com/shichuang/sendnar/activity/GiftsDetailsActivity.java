package com.shichuang.sendnar.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxBigDecimalTool;
import com.shichuang.open.tool.RxStatusBarTool;
import com.shichuang.open.widget.NoScrollWebView;
import com.shichuang.open.widget.RxEmptyLayout;
import com.shichuang.sendnar.MainActivity;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.Setting;
import com.shichuang.sendnar.common.BuyType;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.common.UserCache;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.GiftsDetails;
import com.shichuang.sendnar.entify.User;
import com.shichuang.sendnar.tool.BannerImageLoader;
import com.shichuang.sendnar.widget.DirectPurchaseDialog;
import com.shichuang.sendnar.widget.GiftsDetailsRemindDialog;
import com.shichuang.sendnar.widget.ShareDialog;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 礼物详情
 * Created by Administrator on 2018/4/18.
 */

public class GiftsDetailsActivity extends BaseActivity implements View.OnClickListener {
    private RxEmptyLayout mEmptyLayout;
    private LinearLayout mLlTitleBar;
    private Banner mBanner;
    private TextView mTvGiftsName;
    private TextView mTvGiftsPrice;
    private TextView mTvGiftsPrice01;
    private LinearLayout mLlAddToShoppingCart;
    private NoScrollWebView mWebView;

    private int id;
    //  1-活动详情，2-礼包详情，3-商品详情
    private int operationType;
    // 是否隐藏购物车，显示商品价格(分类中节日和扶贫需要影藏加入购物车，显示价格)
    private boolean isHideAddShoppingCart;
    private GiftsDetails giftsDetails;
    private DirectPurchaseDialog mBuyDialog;

    @Override
    public int getLayoutId() {
        RxStatusBarTool.setStatusBar(this, true);
        return R.layout.activity_gifts_details;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        id = getIntent().getIntExtra("id", 0);
        operationType = getIntent().getIntExtra("operationType", 0);
        isHideAddShoppingCart = getIntent().getBooleanExtra("isHideAddShoppingCart", false);
        mLlTitleBar = (LinearLayout) findViewById(R.id.ll_title_bar);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mLlTitleBar.getLayoutParams();
        params.setMargins(0, RxStatusBarTool.getStatusBarHeight(mContext), 0, 0);

        initBanner();
        mTvGiftsName = (TextView) findViewById(R.id.tv_gifts_name);
        mTvGiftsPrice = (TextView) findViewById(R.id.tv_gifts_price);
        mTvGiftsPrice01 = (TextView) findViewById(R.id.tv_gifts_price_01);
        mLlAddToShoppingCart = (LinearLayout) findViewById(R.id.ll_add_to_shopping_cart);
        mWebView = (NoScrollWebView) findViewById(R.id.web_view);
        mWebView.setWebViewClient(new WebClient());

        mEmptyLayout = (RxEmptyLayout) findViewById(R.id.empty_layout);
        mEmptyLayout.setOnEmptyLayoutClickListener(new RxEmptyLayout.OnEmptyLayoutClickListener() {
            @Override
            public void onEmptyLayoutClick(int status) {
                getGiftsDetailsData();
            }
        });

        if(isHideAddShoppingCart){
            mLlAddToShoppingCart.setVisibility(View.INVISIBLE);
            mTvGiftsPrice01.setVisibility(View.VISIBLE);
        }else{
            mLlAddToShoppingCart.setVisibility(View.VISIBLE);
            mTvGiftsPrice01.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        isShowRemind();
    }

    private void isShowRemind() {
        if (Setting.isShowGiftsDetailsRemind(mContext)) {
            GiftsDetailsRemindDialog mDialog = new GiftsDetailsRemindDialog(mContext);
            mDialog.show();
        }
    }

    private void initBanner() {
        mBanner = (Banner) findViewById(R.id.banner);
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        mBanner.setImageLoader(new BannerImageLoader());
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.RIGHT);
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

            }
        });
    }

    @Override
    public void initEvent() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_share).setOnClickListener(this);
        findViewById(R.id.iv_go_to_shopping_cart).setOnClickListener(this);
        mLlAddToShoppingCart.setOnClickListener(this);
        findViewById(R.id.btn_wechat_gift_giving).setOnClickListener(this);
        findViewById(R.id.btn_direct_purchase).setOnClickListener(this);
    }

    @Override
    public void initData() {
        getGiftsDetailsData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                RxActivityTool.finish(mContext);
                break;
            case R.id.iv_share:
                if (Utils.isLogin(mContext)) {
                    share();
                }
                break;
            case R.id.iv_go_to_shopping_cart:
                if (Utils.isLogin(mContext)) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", 2);
                    RxActivityTool.skipActivity(mContext, MainActivity.class, bundle);
                }
                break;
            case R.id.ll_add_to_shopping_cart:
                if (Utils.isLogin(mContext)) {
                    addShoppingCart();
                }
                break;
            // 微信送礼
            case R.id.btn_wechat_gift_giving:
                if (Utils.isLogin(mContext)) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt("goodsId", giftsDetails.getId());
                    bundle1.putInt("count", 1);
                    bundle1.putInt("buyType", BuyType.WECHAT_GIFT_GIVING);
                    RxActivityTool.skipActivity(mContext, ConfirmOrderActivity.class, bundle1);
                }
                break;
            // 送给自己
            case R.id.btn_direct_purchase:
                if (Utils.isLogin(mContext)) {
                    showDirectPurchaseDialog();
                }
                break;
            default:
                break;
        }
    }

    private void showDirectPurchaseDialog() {
        if (mBuyDialog == null) {
            mBuyDialog = new DirectPurchaseDialog(mContext);
            mBuyDialog.setGiftInfo(giftsDetails);
        }
        //DirectPurchaseDialog mDialog = new DirectPurchaseDialog(mContext);
        mBuyDialog.show();
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


    private void getGiftsDetailsData() {
        OkGo.<AMBaseDto<GiftsDetails>>get(Constants.giftsDetailsUrl)
                .tag(mContext)
                .params("id", id)
                .params("operation_type", operationType)
                .execute(new NewsCallback<AMBaseDto<GiftsDetails>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<GiftsDetails>, ? extends Request> request) {
                        super.onStart(request);
                        mEmptyLayout.show(RxEmptyLayout.NETWORK_LOADING);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<GiftsDetails>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            giftsDetails = response.body().data;
                            handleData();
                            mEmptyLayout.hide();
                        } else {
                            mEmptyLayout.show(RxEmptyLayout.EMPTY_DATA);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<GiftsDetails>> response) {
                        super.onError(response);
                        mEmptyLayout.show(RxEmptyLayout.NETWORK_ERROR);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private void handleData() {
        if (!TextUtils.isEmpty(giftsDetails.getPic())) {
            String[] bannerStr = giftsDetails.getPic().split(",");
            if (bannerStr.length > 0) {
                List<String> bannerList = new ArrayList<>();
                for (int i = 0; i < bannerStr.length; i++) {
                    bannerList.add(Constants.MAIN_ENGINE_PIC + bannerStr[i]);
                }
                mBanner.update(bannerList);
            }
        }
        mTvGiftsName.setText(giftsDetails.getShopName());
        mTvGiftsPrice.setText("¥" + RxBigDecimalTool.toDecimal(giftsDetails.getSalePrice(), 2));
        mTvGiftsPrice01.setText("¥" + RxBigDecimalTool.toDecimal(giftsDetails.getSalePrice(), 2));
        mWebView.loadUrl(Constants.MAIN_ENGINE_PIC + giftsDetails.getH5Url());
    }

    /**
     * 添加到购物车
     */
    private void addShoppingCart() {
        OkGo.<AMBaseDto<Empty>>get(Constants.addShoppingCartUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("goods_id", giftsDetails.getId())
                .params("goods_cart_counts", 1)
                .params("action_item_id", -1)
                .execute(new NewsCallback<AMBaseDto<Empty>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Empty>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<Empty>> response) {
                        showToast(response.body().msg);
                        if (response.body().code == 0) {
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Empty>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissLoading();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);//完成回调
    }

    /**
     * 分享
     */
    private void share() {
        String url = Constants.MAIN_ENGINE_PIC + "/songnaerWechat/#/item?id=" + id + "&type=" + operationType + "&from=share"+"&user="+ TokenCache.userId(mContext);
        String title = "";
        User user = UserCache.user(mContext);
        if (user == null) {
            title = "向您推荐";
        } else {
            title = UserCache.user(mContext).getNickname() + "向您推荐";
        }
        String img = Utils.getSingleImageUrlByImageUrls(giftsDetails.getPic());
        String description = giftsDetails.getTitle();
        final ShareDialog mDialog = new ShareDialog(GiftsDetailsActivity.this);
        mDialog.setWeb(url, title, img, description);
        mDialog.setListener(new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                mDialog.dismiss();
                showToast("分享成功");
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                mDialog.dismiss();
                showToast("分享出错");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                mDialog.dismiss();
                showToast("分享取消");
            }
        });
        mDialog.show();
    }

    @Override
    protected void onDestroy() {
        if (mBuyDialog != null) {
            mBuyDialog = null;
        }
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
}
