package com.shichuang.sendnar.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.lzy.okrx2.adapter.ObservableResponse;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxGlideTool;
import com.shichuang.open.tool.RxStatusBarTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.Convert;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.common.UserCache;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.UploadFile;
import com.shichuang.sendnar.entify.User;
import com.shichuang.sendnar.event.UpdateLoginStatus;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 更换头像
 * Created by Administrator on 2018/4/22.
 */

public class ChangeAvatarActivity extends BaseActivity {
    private ImageView mIvAvatar;

    private String cropImagePath;
    private String avatarUrl;

    @Override
    public int getLayoutId() {
        RxStatusBarTool.setStatusBar(this, true);
        return R.layout.activity_change_avatar;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        cropImagePath = getIntent().getStringExtra("cropImagePath");
        avatarUrl = getIntent().getStringExtra("avatarUrl");
        mIvAvatar = (ImageView) findViewById(R.id.iv_avatar);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.btn_change_avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(ChangeAvatarActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(1)
                        .compress(true)
                        .enableCrop(true)
                        .circleDimmedLayer(true)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            }
        });
        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxActivityTool.finish(mContext);
            }
        });
    }

    @Override
    public void initData() {
        if (!TextUtils.isEmpty(cropImagePath)) {
            mIvAvatar.setImageBitmap(BitmapFactory.decodeFile(cropImagePath));
        } else {
            RxGlideTool.loadImageView(mContext, Utils.getSingleImageUrlByImageUrls(avatarUrl), mIvAvatar, R.drawable.ic_avatar_default);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            if (selectList != null && selectList.size() > 0) {
                uploadFiles(selectList.get(0).getCutPath());
            }
        }
    }

    private void uploadFiles(final String cropImagePath) {
        ArrayList<File> files = new ArrayList<>();
        files.add(new File(cropImagePath));

        OkGo.<String>post(Constants.uploadFile)//
                .tag(this)//
                .addFileParams("file", files)
                .converter(new StringConvert())//
                .adapt(new ObservableResponse<String>())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new Observer<Response<String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        showLoading();
                    }

                    @Override
                    public void onNext(@NonNull Response<String> response) {
                        final UploadFile uploadFile = Convert.fromJson(response.body(), UploadFile.class);
                        if (uploadFile.getStatus() == 1) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("cropImagePath", cropImagePath);
                                    bundle.putString("avatarUrl", uploadFile.getPath());
                                    RxActivityTool.finish(mContext, bundle, RESULT_OK);
                                }
                            }, 300);
                        } else {
                            showToast(uploadFile.getMsg());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        dismissLoading();
                        showToast("网络异常，稍后再试");
                    }

                    @Override
                    public void onComplete() {
                        dismissLoading();
                    }
                });
    }


}
