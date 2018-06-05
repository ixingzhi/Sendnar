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
import com.shichuang.sendnar.widget.PromptDialog;

/**
 * 商务合作
 * Created by Administrator on 2018/4/23.
 */

public class BusinessCooperationActivity extends BaseActivity {
    private EditText mEtUsername;
    private EditText mEtPhoneNumber;
    private EditText mEtIdea;


    @Override
    public int getLayoutId() {
        return R.layout.activity_business_cooperation;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mEtUsername = (EditText) findViewById(R.id.et_username);
        mEtPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
        mEtIdea = (EditText) findViewById(R.id.et_idea);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInfo();
            }
        });
        findViewById(R.id.ll_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PromptDialog mDialog = new PromptDialog(mContext, R.layout.layout_contact_us);
                mDialog.show();
            }
        });
    }

    @Override
    public void initData() {
    }


    private void checkInfo() {
        String username = mEtUsername.getText().toString().trim();
        String phoneNumber = mEtPhoneNumber.getText().toString().trim();
        String idea = mEtIdea.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            showToast("请输入您的姓名");
        } else if (TextUtils.isEmpty(phoneNumber)) {
            showToast("请输入您的联系方式");
        } else if (TextUtils.isEmpty(idea)) {
            showToast("请输入您的想法");
        } else {
            commit(username, phoneNumber, idea);
        }
    }

    private void commit(String username, String phoneNumber, String idea) {
        OkGo.<AMBaseDto<Empty>>get(Constants.businessCooperationUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("business_name", username)
                .params("phone_num", phoneNumber)
                .params("content", idea)
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
                            }, 200);
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
