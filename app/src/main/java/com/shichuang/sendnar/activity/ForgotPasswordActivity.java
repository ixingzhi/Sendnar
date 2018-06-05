package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxDataTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.MsgCode;

/**
 * 忘记密码
 * Created by Administrator on 2018/4/22.
 */

public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener {
    private EditText mEtUsername;
    private EditText mEtVerificationCode;
    private TextView mTvGetVerificationCode;
    private EditText mEtNewPassword;
    private EditText mEtConfirmNewPassword;
    private Button mBtnOk;

    private String code = "";
    private CountDownTimer mCountDownTimer;

    @Override
    public int getLayoutId() {
        return R.layout.activity_forgot_password;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mEtUsername = (EditText) findViewById(R.id.et_username);
        mEtVerificationCode = (EditText) findViewById(R.id.et_verification_code);
        mTvGetVerificationCode = (TextView) findViewById(R.id.tv_get_verification_code);
        mEtNewPassword = (EditText) findViewById(R.id.et_new_password);
        mEtConfirmNewPassword = (EditText) findViewById(R.id.et_confirm_new_password);
        mBtnOk = (Button) findViewById(R.id.btn_ok);
    }

    @Override
    public void initEvent() {
        mTvGetVerificationCode.setOnClickListener(this);
        mBtnOk.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_get_verification_code:
                getVerificationCode();
                break;
            case R.id.btn_ok:
                checkInfo();
                break;
        }
    }

    private void getVerificationCode() {
        String username = mEtUsername.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            showToast("请输入手机号");
            return;
        }
        if (!RxDataTool.isMobileNO(username)) {
            showToast("请输入正确的手机号");
            return;
        }

        OkGo.<AMBaseDto<MsgCode>>post(Constants.getSmsCodeUrl)
                .tag(mContext)
                .params("account_number", username)
                .params("operation_type", 2)  // 操作方式（1=注册|2=修改密码|3=解绑|4=绑定账号 | 5=忘记密码）
                .execute(new NewsCallback<AMBaseDto<MsgCode>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<MsgCode>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<MsgCode>> response) {
                        dismissLoading();
                        showToast(response.body().msg);
                        if (response.body().code == 0) {
                            code = response.body().data.getCode();
                            startCountDown();
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<MsgCode>> response) {
                        super.onError(response);
                        dismissLoading();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    /**
     * 倒计时
     */
    private void startCountDown() {
        mTvGetVerificationCode.setEnabled(false);
        if (mCountDownTimer != null) {
            mCountDownTimer.start();
            return;
        }
        mCountDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTvGetVerificationCode.setText("剩余时间 " + millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                mCountDownTimer.cancel();
                mTvGetVerificationCode.setText("重新获取");
                mTvGetVerificationCode.setEnabled(true);
            }
        }.start();
    }

    private void checkInfo() {
        String username = mEtUsername.getText().toString().trim();
        String verificationCode = mEtVerificationCode.getText().toString().trim();
        String newPassword = mEtNewPassword.getText().toString().trim();
        String confirmNewPassword = mEtConfirmNewPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            showToast("请输入手机号");
        } else if (!RxDataTool.isMobileNO(username)) {
            showToast("请输入正确的手机号");
        } else if (TextUtils.isEmpty(verificationCode)) {
            showToast("请输入验证码");
        } else if (!verificationCode.equals(code)) {
            showToast("验证码有误，请重新输入");
        } else if (TextUtils.isEmpty(newPassword)) {
            showToast("请输入新密码");
        } else if (newPassword.length() < 6) {
            showToast("新密码长度需大于6个字符");
        } else if (TextUtils.isEmpty(confirmNewPassword)) {
            showToast("请再次输入新密码");
        } else if (confirmNewPassword.length() < 6) {
            showToast("密码长度需大于6个字符");
        } else if (!newPassword.equals(confirmNewPassword)) {
            showToast("两次密码输入不一致");
        } else {
            forgotPassword(username, newPassword);
        }
    }

    private void forgotPassword(final String username, String password) {
        OkGo.<AMBaseDto<Empty>>post(Constants.forgotPasswordUrl)
                .tag(mContext)
                .params("platform", Constants.PLATFORM)
                .params("phone_num", username)
                .params("new_password", password)
                .params("confirm_password", password)
                .params("code", code)
                .execute(new NewsCallback<AMBaseDto<Empty>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Empty>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<Empty>> response) {
                        dismissLoading();
                        showToast(response.body().msg);
                        if (response.body().code == 0) {
                            Bundle bundle = new Bundle();
                            bundle.putString("username", username);
                            RxActivityTool.finish(mContext, bundle, RESULT_OK);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Empty>> response) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }
}
