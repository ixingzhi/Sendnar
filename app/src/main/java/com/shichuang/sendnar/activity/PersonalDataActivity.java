package com.shichuang.sendnar.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxGlideTool;
import com.shichuang.open.tool.RxKeyboardTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.common.UserCache;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.User;
import com.shichuang.sendnar.event.UpdateLoginStatus;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;

/**
 * 个人资料
 * Created by Administrator on 2018/5/15.
 */

public class PersonalDataActivity extends BaseActivity implements View.OnClickListener {
    private static final int EDIE_AVATAR_SUCCESS = 0x11;
    private ImageView mIvAvatar;
    private TextView mTvNickname;
    private TextView mTvBirthday;
    private TextView mTvPhone;

    private TimePickerView pvTime;
    private String avatarUrl;
    private String cropImagePath;
    private String nickname;
    private String birthday;

    private AlertDialog mEditNicknameDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_personal_data;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mIvAvatar = view.findViewById(R.id.iv_avatar);
        mTvNickname = view.findViewById(R.id.tv_nickname);
        mTvBirthday = view.findViewById(R.id.tv_birthday);
        mTvPhone = view.findViewById(R.id.tv_phone);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.ll_change_avatar).setOnClickListener(this);
        findViewById(R.id.ll_edit_nickname).setOnClickListener(this);
        findViewById(R.id.ll_select_birthday).setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);
    }

    @Override
    public void initData() {
        User user = UserCache.user(mContext);
        if (user != null) {
            avatarUrl = user.getHeadPortrait();
            nickname = user.getNickname();
            if (TextUtils.isEmpty(user.getBirthday())) {
                birthday = "";
                mTvBirthday.setHint("选择生日");
            } else {
                birthday = user.getBirthday();
                mTvBirthday.setText(birthday);
            }

            RxGlideTool.loadImageView(mContext, Utils.getSingleImageUrlByImageUrls(avatarUrl), mIvAvatar, R.drawable.ic_avatar_default);
            mTvNickname.setText(nickname);
            mTvPhone.setText(user.getPhone());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_change_avatar:
                Bundle bundle = new Bundle();
                bundle.putString("cropImagePath", cropImagePath);
                bundle.putString("avatarUrl", avatarUrl);
                RxActivityTool.skipActivityForResult(PersonalDataActivity.this, ChangeAvatarActivity.class, bundle, EDIE_AVATAR_SUCCESS);
                break;
            case R.id.ll_edit_nickname:
                showNicknameDialog();
                break;
            case R.id.ll_select_birthday:
                selectBirthday();
                break;
            case R.id.btn_save:
                checkInfo();
                break;
            default:
                break;
        }
    }

    private void showNicknameDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit_nickname, null);
        final EditText mEtNickname = view.findViewById(R.id.et_nickname);
        mEtNickname.setText(nickname);
        mEtNickname.setSelection(nickname.length());
        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editNickname = mEtNickname.getText().toString().trim();
                if (TextUtils.isEmpty(editNickname)) {
                    showToast("昵称不能为空");
                } else {
                    if (null != mEditNicknameDialog) {
                        nickname = editNickname;
                        mTvNickname.setText(nickname);
                        RxKeyboardTool.hideSoftInput(mContext, mEtNickname);
                        mEditNicknameDialog.dismiss();
                    }
                }
            }
        });
        if (null == mEditNicknameDialog) {
            mEditNicknameDialog = new AlertDialog.Builder(mContext).setView(view).create();
        }
        mEditNicknameDialog.show();
        // 延迟弹出键盘
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RxKeyboardTool.showSoftInput(mContext, mEtNickname);
            }
        }, 300);
    }

    private void selectBirthday() {
        if (null == pvTime) {
            Calendar startDate = Calendar.getInstance();
            startDate.add(Calendar.YEAR, -100);
            Calendar endDate = Calendar.getInstance();
            Calendar selectDate = Calendar.getInstance();
            selectDate.set(1990, 0, 01);
            pvTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    int day = calendar.get(Calendar.DATE);
                    birthday = year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
                    mTvBirthday.setText(birthday);
                }
            }).setRangDate(startDate, endDate).setDate(selectDate).build();
        }
        pvTime.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIE_AVATAR_SUCCESS && resultCode == RESULT_OK) {
            avatarUrl = data.getStringExtra("avatarUrl");
            cropImagePath = data.getStringExtra("cropImagePath");
            mIvAvatar.setImageBitmap(BitmapFactory.decodeFile(cropImagePath));
        }
    }

    private void checkInfo() {
        if (TextUtils.isEmpty(avatarUrl)) {
            showToast("头像上传失败");
        } else if (TextUtils.isEmpty(nickname)) {
            showToast("昵称不能为空");
        } else if (TextUtils.isEmpty(birthday)) {
            showToast("生日不能为空");
        } else {
            updateUserData();
        }
    }


    private void updateUserData() {
        OkGo.<AMBaseDto<Empty>>post(Constants.changeMySelfInfoUrl)
                //.cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)  //缓存模式先使用缓存,然后使用网络数据
                .params("token", TokenCache.token(mContext))
                .params("head_portrait", avatarUrl)
                .params("nickname", nickname)
                .params("birthday", birthday)
                .execute(new NewsCallback<AMBaseDto<Empty>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Empty>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(Response<AMBaseDto<Empty>> response) {
                        dismissLoading();
                        showToast(response.body().msg);
                        if (response.body().code == 0) {
                            EventBus.getDefault().post(new UpdateLoginStatus());
                            RxActivityTool.finish(mContext);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Empty>> response) {
                        super.onError(response);
                        dismissLoading();
                        showToast("网络错误");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }
}
