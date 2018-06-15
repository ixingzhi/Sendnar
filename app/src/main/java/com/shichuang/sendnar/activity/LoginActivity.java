package com.shichuang.sendnar.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxDataTool;
import com.shichuang.open.tool.RxStatusBarTool;
import com.shichuang.open.tool.RxToastTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.JpushUtils;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.common.UdeskHelper;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.CheckOpenId;
import com.shichuang.sendnar.entify.OauthLogin;
import com.shichuang.sendnar.entify.Token;
import com.shichuang.sendnar.event.UpdateLoginStatus;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 * 登录
 * Created by Administrator on 2018/4/20.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final int REGISTER_SUCCESS = 0x11;
    private static final int BIND_PHONE_NUMBER = 0x12;
    private ImageView mIvBack;
    private EditText mEtUsername;
    private EditText mEtPassword;

    // 第三方信息
    private String uid = "";
    private String nickname = "";
    private String avatar = "";
    private String phoneNumber = "";
    private String password = "";
    private String code = "";

    @Override
    public int getLayoutId() {
        RxStatusBarTool.setStatusBar(this, true);
        return R.layout.activity_login;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mIvBack.getLayoutParams();
        params.setMargins(0, RxStatusBarTool.getStatusBarHeight(mContext), 0, 0);

        mEtUsername = (EditText) findViewById(R.id.et_username);
        mEtPassword = (EditText) findViewById(R.id.et_password);
    }

    @Override
    public void initEvent() {
        //findViewById(R.id.btn_register).setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        findViewById(R.id.tv_forgot_password).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.ll_wechat_login).setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_register:
//                RxActivityTool.skipActivityForResult(LoginActivity.this, RegisterActivity.class, REGISTER_SUCCESS);
//                break;
            case R.id.iv_back:
                RxActivityTool.finish(mContext);
                break;
            case R.id.tv_forgot_password:
                RxActivityTool.skipActivityForResult(LoginActivity.this, ForgotPasswordActivity.class, REGISTER_SUCCESS);
                break;
            case R.id.btn_login:
                checkInfo();
                break;
            case R.id.ll_wechat_login:
                wechatLogin();
                break;
            default:
                break;
        }
    }

    private void checkInfo() {
        String username = mEtUsername.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            showToast("请输入手机号");
        } else if (!RxDataTool.isMobileNO(username)) {
            showToast("请输入正确的手机号");
        } else if (TextUtils.isEmpty(password)) {
            showToast("请输入密码");
        } else if (password.length() < 6) {
            showToast("密码长度需大于6个字符");
        } else {
            login(username, password);
        }
    }

    private void login(String username, String password) {
        OkGo.<AMBaseDto<Token>>post(Constants.userLoginUrl)
                .tag(mContext)
                .params("platform", Constants.PLATFORM)
                .params("account_number", username)
                .params("pass_word", password)
                .execute(new NewsCallback<AMBaseDto<Token>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Token>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<Token>> response) {
                        dismissLoading();
                        showToast(response.body().msg);
                        if (response.body().code == 0) {
                            Token token = response.body().data;
                            TokenCache.update(mContext, token);
                            // 更新用户登录状态
                            EventBus.getDefault().post(new UpdateLoginStatus());
                            // 设置Udesk客服信息
                            UdeskHelper.setUserInfo(mContext);
                            // 设置极光推送
                            JpushUtils.setJpushAlias(mContext, token.getUserId() + "");
                            RxActivityTool.finish(mContext);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Token>> response) {
                        super.onError(response);
                        dismissLoading();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTER_SUCCESS && resultCode == RESULT_OK) {
            String username = data.getStringExtra("username");
            mEtUsername.setText(username);
        } else if (requestCode == BIND_PHONE_NUMBER && resultCode == RESULT_OK) {
            phoneNumber = data.getStringExtra("phoneNumber");
            password = data.getStringExtra("password");
            code = data.getStringExtra("code");
            oauthLogin();
        }
    }


    private void wechatLogin() {
        if (UMShareAPI.get(mContext).isInstall((Activity) mContext, SHARE_MEDIA.WEIXIN)) {
            UMShareAPI.get(mContext).getPlatformInfo((Activity) mContext, SHARE_MEDIA.WEIXIN, authListener);
        } else {
            showToast("请安装微信客户端");
        }
    }

    private UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
            showLoading();
        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            dismissLoading();
            if (data != null) {
                uid = data.get("uid");
                nickname = data.get("name");
                //String gender = data.get("gender");
                avatar = data.get("iconurl");
                checkOpenId();
            }
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            dismissLoading();
            RxToastTool.showLong("onError：" + t.getMessage());
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            dismissLoading();
            RxToastTool.showLong("onCancel");
        }
    };

    /**
     * 检查是否绑定过第三方信息
     */
    private void checkOpenId() {
        OkGo.<AMBaseDto<CheckOpenId>>post(Constants.checkOpenIdUrl)
                .tag(mContext)
                .params("oauth_name", "weixin")
                .params("oauth_unionid", uid)
                .execute(new NewsCallback<AMBaseDto<CheckOpenId>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<CheckOpenId>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<CheckOpenId>> response) {
                        dismissLoading();
                        if (response.body().code == 0) {
                            // 1存在 0不存在
                            if (response.body().data.getExist() == 1) {
                                oauthLogin();
                            } else {
                                RxActivityTool.skipActivityForResult(LoginActivity.this, BindPhoneNumberActivity.class, BIND_PHONE_NUMBER);
                            }
                        } else {
                            showToast(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<CheckOpenId>> response) {
                        super.onError(response);
                        dismissLoading();
                        showToast("网络连接异常");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    /**
     * 第三方登录或者绑定
     */
    private void oauthLogin() {
        OkGo.<AMBaseDto<OauthLogin>>post(Constants.oauthLoginUrl)
                .tag(mContext)
                .params("oauth_name", "weixin")
                .params("oauth_openid", uid)
                .params("oauth_unionid", uid)
                .params("nickname", nickname)
                .params("phoneNum", phoneNumber)
                .params("pass_word", password)
                .params("confirm_pass_word", password)
                .params("head_portrait", avatar)
                .params("code", code)
                .execute(new NewsCallback<AMBaseDto<OauthLogin>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<OauthLogin>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<OauthLogin>> response) {
                        dismissLoading();
                        if (response.body().code == 0) {
                            OauthLogin oauthLogin = response.body().data;
                            login(oauthLogin.getAccountNumber(), oauthLogin.getPassword());
                        } else {
                            showToast(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<OauthLogin>> response) {
                        super.onError(response);
                        dismissLoading();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

}
