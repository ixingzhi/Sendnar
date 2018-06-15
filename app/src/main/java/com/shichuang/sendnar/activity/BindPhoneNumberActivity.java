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
import com.shichuang.sendnar.entify.MsgCode;

/**
 * 绑定手机号
 * Created by Administrator on 2018/4/22.
 */

public class BindPhoneNumberActivity extends BaseActivity implements View.OnClickListener {
    private EditText mEtUsername;
    private EditText mEtVerificationCode;
    private TextView mTvGetVerificationCode;
    private EditText mEtPassword;
    private EditText mEtConfirmPassword;
    private Button mBtnBind;

    private String code = "";
    private CountDownTimer mCountDownTimer;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bind_phone_number;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mEtUsername = (EditText) findViewById(R.id.et_username);
        mEtVerificationCode = (EditText) findViewById(R.id.et_verification_code);
        mTvGetVerificationCode = (TextView) findViewById(R.id.tv_get_verification_code);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mEtConfirmPassword = (EditText) findViewById(R.id.et_confirm_password);
        mBtnBind = (Button) findViewById(R.id.btn_bind);
    }

    @Override
    public void initEvent() {
        mTvGetVerificationCode.setOnClickListener(this);
        mBtnBind.setOnClickListener(this);
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
            case R.id.btn_bind:
                checkInfo();
                break;
            default:
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
                .params("operation_type", 4)  // 操作方式（1=注册|2=修改密码|3=解绑|4=绑定账号）
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
        String password = mEtPassword.getText().toString().trim();
        String confirmPassword = mEtConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            showToast("请输入手机号");
        } else if (!RxDataTool.isMobileNO(username)) {
            showToast("请输入正确的手机号");
        } else if (TextUtils.isEmpty(verificationCode)) {
            showToast("请输入验证码");
        } else if (!verificationCode.equals(code)) {
            showToast("验证码有误，请重新输入");
        } else if (TextUtils.isEmpty(password)) {
            showToast("请输入密码");
        } else if (password.length() < 6) {
            showToast("密码不能少于6个字符");
        } else if (TextUtils.isEmpty(confirmPassword)) {
            showToast("请输入确认密码");
        } else if (confirmPassword.length() < 6) {
            showToast("确认密码不能少于6个字符");
        } else if (!password.equals(confirmPassword)) {
            showToast("两次密码输入不一致");
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("phoneNumber", username);
            bundle.putString("password", password);
            bundle.putString("code", verificationCode);
            RxActivityTool.finish(mContext, bundle, RESULT_OK);
        }
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
