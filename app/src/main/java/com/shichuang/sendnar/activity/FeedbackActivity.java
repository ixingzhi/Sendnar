package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Empty;

/**
 * 意见反馈
 * Created by Administrator on 2018/4/22.
 */

public class FeedbackActivity extends BaseActivity {
    private EditText mEtOpinionAdvice;
    private EditText mEtContact;

    @Override
    public int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mEtOpinionAdvice = (EditText) findViewById(R.id.et_opinion_advice);
        mEtContact = (EditText) findViewById(R.id.et_contact);
    }

    @Override
    public void initEvent() {
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
        String opinionAdvice = mEtOpinionAdvice.getText().toString().trim();
        String contact = mEtContact.getText().toString().trim();
        if (TextUtils.isEmpty(opinionAdvice)) {
            showToast("请输入您的意见或建议");
        } else if (TextUtils.isEmpty(contact)) {
            showToast("请输入您的联系方式");
        } else {
            commit(opinionAdvice, contact);
        }
    }

    private void commit(String opinionAdvice, String contact) {
        OkGo.<AMBaseDto<Empty>>get(Constants.feedbackUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("opinion", opinionAdvice)
                .params("phone", contact)
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
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    RxActivityTool.finish(mContext);
                                }
                            },200);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Empty>> response) {
                        super.onError(response);
                        showToast(response.getException().getMessage());

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissLoading();
                    }
                });
    }

}
