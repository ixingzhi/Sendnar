package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxStatusBarTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.ExamplesGift;
import com.shichuang.sendnar.entify.SendGift;

/**
 * 送礼，转赠
 * Created by Administrator on 2018/4/23.
 */

public class GreetingsActivity extends BaseActivity {
    private RelativeLayout mRlTitleBar;
    private EditText mEtGreetings;

    private String ids;
    private String description;
    private String imgShareUrl;
    private int type;
    // 是否转赠
    private boolean isExamples;

    @Override
    public int getLayoutId() {
        RxStatusBarTool.setStatusBar(this, true);
        return R.layout.activity_greetings;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        ids = getIntent().getStringExtra("ids");
        description = getIntent().getStringExtra("description");
        imgShareUrl = getIntent().getStringExtra("imgShareUrl");
        type = getIntent().getIntExtra("type", 0);
        isExamples = getIntent().getBooleanExtra("isExamples", false);
        mRlTitleBar = (RelativeLayout) findViewById(R.id.rl_title_bar);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRlTitleBar.getLayoutParams();
        params.setMargins(0, RxStatusBarTool.getStatusBarHeight(mContext), 0, 0);

        mEtGreetings = (EditText) findViewById(R.id.et_greetings);
        mEtGreetings.getBackground().mutate().setAlpha(230);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.ll_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxActivityTool.finish(mContext);
            }
        });
        findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInfo();
            }
        });
    }

    @Override
    public void initData() {

    }

    private void checkInfo() {
        String greetings = mEtGreetings.getText().toString().trim();
//        if (TextUtils.isEmpty(greetings)) {
//            showToast("请输入祝福语");
//        } else {
//            if (isExamples) {
//                examplesGift(greetings);
//            } else {
//                sendGift(greetings);
//            }
//        }
        if (isExamples) {
            examplesGift(greetings);
        } else {
            sendGift(greetings);
        }
    }

    /**
     * 送礼
     *
     * @param greetings
     */
    private void sendGift(final String greetings) {
        OkGo.<AMBaseDto<SendGift>>get(Constants.sendGiftUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("ids", ids)
                .params("blessing", greetings)
                .params("send_type", type)    // 1=微信好友送礼 2=发红包
                .execute(new NewsCallback<AMBaseDto<SendGift>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<SendGift>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<SendGift>> response) {
                        dismissLoading();
                        if (response.body().code == 0) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("redPacketId", response.body().data.getRedPacketId());
                            bundle.putString("greetings", greetings);
                            bundle.putString("description", description);
                            bundle.putString("imgShareUrl", imgShareUrl);
                            bundle.putInt("type", type);
                            RxActivityTool.finish(mContext, bundle, RESULT_OK);
                        } else {
                            showToast(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<SendGift>> response) {
                        super.onError(response);
                        dismissLoading();
                        showToast(response.getException().getMessage());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    /**
     * 转赠
     */
    private void examplesGift(final String greetings) {
        OkGo.<AMBaseDto<ExamplesGift>>get(Constants.examplesGiftUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("id", ids)
                .params("blessing", greetings)
                .execute(new NewsCallback<AMBaseDto<ExamplesGift>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<ExamplesGift>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<ExamplesGift>> response) {
                        dismissLoading();
                        if (response.body().code == 0) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("redPacketId", response.body().data.getId());
                            bundle.putString("greetings", greetings);
                            bundle.putString("description", description);
                            bundle.putString("imgShareUrl", imgShareUrl);
                            bundle.putInt("type", type);
                            RxActivityTool.finish(mContext, bundle, RESULT_OK);
                        } else {
                            showToast(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<ExamplesGift>> response) {
                        super.onError(response);
                        dismissLoading();
                        showToast(response.getException().getMessage());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

}
