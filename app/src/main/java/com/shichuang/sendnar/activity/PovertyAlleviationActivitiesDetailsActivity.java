package com.shichuang.sendnar.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxBigDecimalTool;
import com.shichuang.open.tool.RxGlideTool;
import com.shichuang.open.widget.X5WebView;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.Setting;
import com.shichuang.sendnar.common.BuyType;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.common.UserCache;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.ActionList;
import com.shichuang.sendnar.entify.PovertyAlleviationActivitiesDetails;
import com.shichuang.sendnar.entify.User;
import com.shichuang.sendnar.widget.DirectPurchaseDialog;
import com.shichuang.sendnar.widget.GiftsDetailsRemindDialog;
import com.shichuang.sendnar.widget.PAADDirectPurchaseDialog;
import com.shichuang.sendnar.widget.PovertyAlleviationActivitiesRemindDialog;
import com.shichuang.sendnar.widget.ShareDialog;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

/**
 * 扶贫活动详情
 * Created by Administrator on 2018/4/17.
 */

public class PovertyAlleviationActivitiesDetailsActivity extends BaseActivity implements View.OnClickListener {
    private X5WebView mWebView;
    private TextView mTvGiftsPrice;
    private Button mBtnWechatGiftGiving;
    private Button mBtnDirectPurchase;
    private PovertyAlleviationActivitiesDetails data;

    private PAADDirectPurchaseDialog mBuyDialog;

    private int id;
    // 活动是否结束
    private boolean isEnd;

    @Override
    public int getLayoutId() {
        return R.layout.activity_poverty_alleviation_activities_details;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        id = getIntent().getIntExtra("id", 0);
        isEnd = getIntent().getBooleanExtra("isEnd", false);
        mTvGiftsPrice = (TextView) findViewById(R.id.tv_gifts_price);
        mBtnWechatGiftGiving = (Button) findViewById(R.id.btn_wechat_gift_giving);
        mBtnDirectPurchase = (Button) findViewById(R.id.btn_direct_purchase);
        mWebView = (X5WebView) findViewById(R.id.web_view);
        mWebView.setWebViewClient(new WebClient());

        isShowRemind();
        // 活动是否结束
        if (isEnd) {
            mBtnWechatGiftGiving.setVisibility(View.INVISIBLE);
            mBtnDirectPurchase.setText("活动已结束");
            mBtnDirectPurchase.setBackgroundColor(getResources().getColor(R.color.gray));
            mBtnDirectPurchase.setEnabled(false);
        }
    }

    private void isShowRemind() {
        if (Setting.isShowPovertyAlleviationActivitiesRemind(mContext)) {
            PovertyAlleviationActivitiesRemindDialog mDialog = new PovertyAlleviationActivitiesRemindDialog(mContext);
            mDialog.show();
        }
    }

    @Override
    public void initEvent() {
        mBtnWechatGiftGiving.setOnClickListener(this);
        mBtnDirectPurchase.setOnClickListener(this);
        // 分享
        findViewById(R.id.ll_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data != null) {
                    if(Utils.isLogin(mContext)){
                        share();
                    }
                }
            }
        });
    }

    @Override
    public void initData() {
        getData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 微信送礼
            case R.id.btn_wechat_gift_giving:
                if (Utils.isLogin(mContext)) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt("goodsId", data.getActionItem().getGoodsId());
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
            mBuyDialog = new PAADDirectPurchaseDialog(mContext);
            mBuyDialog.setGiftInfo(data);
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


    private void getData() {
        OkGo.<AMBaseDto<PovertyAlleviationActivitiesDetails>>get(Constants.povertyAlleviationActivitiesDetailsUrl)
                .tag(mContext)
                .params("action_id", id)
                .execute(new NewsCallback<AMBaseDto<PovertyAlleviationActivitiesDetails>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<PovertyAlleviationActivitiesDetails>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<PovertyAlleviationActivitiesDetails>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            data = response.body().data;
                            handleData();
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<PovertyAlleviationActivitiesDetails>> response) {
                        super.onError(response);
                        //showToast("数据异常：" + response.getException().getMessage());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private void handleData() {
        PovertyAlleviationActivitiesDetails.ActionDetail actionDetail = data.getActionDetail();
        PovertyAlleviationActivitiesDetails.ActionItem actionItem = data.getActionItem();
        if (actionDetail != null) {
            mWebView.loadUrl(Constants.MAIN_ENGINE_PIC + actionDetail.getH5Url());
        }
        if (actionItem != null) {
            mTvGiftsPrice.setText("¥" + RxBigDecimalTool.toDecimal(actionItem.getPromotionPrice(), 2) + "/" + actionItem.getGoodsUnit());
        }

    }

    /**
     * 分享
     */
    private void share() {
        String type = isEnd ? "old" : "";

        String url = Constants.MAIN_ENGINE_PIC + "/songnaerWechat/#/helpoordetail/" + id + "?type=" + type + "&from=share"+"&user="+ TokenCache.userId(mContext);
        String title = "";
        User user = UserCache.user(mContext);
        if (user == null) {
            title = "向您推荐";
        } else {
            title = UserCache.user(mContext).getNickname() + "向您推荐";
        }
        String img = Utils.getSingleImageUrlByImageUrls(data.getActionDetail().getPic());
        String description = data.getActionDetail().getTitle();
        final ShareDialog mDialog = new ShareDialog(PovertyAlleviationActivitiesDetailsActivity.this);
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
}
