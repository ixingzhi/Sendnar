package com.shichuang.sendnar.activity;

import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.base.WebPageActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxAppTool;
import com.shichuang.open.tool.RxFileTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.JpushUtils;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.OrderOperation;
import com.shichuang.sendnar.common.SinglePage;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.common.UdeskHelper;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Page;
import com.shichuang.sendnar.event.UpdateLoginStatus;
import com.shichuang.sendnar.widget.ConfirmDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * 设置
 * Created by xiedd on 2018/4/22.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvCacheSize;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        tvCacheSize = (TextView) findViewById(R.id.tv_cache_size);
        ((TextView) findViewById(R.id.tv_version_number)).setText("v" + getVersionNumber());
    }

    @Override
    public void initEvent() {
        skipPage(R.id.ll_personal_data, PersonalDataActivity.class);
        findViewById(R.id.ll_common_problems).setOnClickListener(this);
        skipPage(R.id.ll_feedback, FeedbackActivity.class);
        findViewById(R.id.ll_wipe_cache).setOnClickListener(this);
        findViewById(R.id.ll_about_us).setOnClickListener(this);
        findViewById(R.id.btn_logout).setOnClickListener(this);
    }

    @Override
    public void initData() {
        calculateCacheSize();
    }

    private void skipPage(int resId, final Class<?> cls) {
        findViewById(resId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxActivityTool.skipActivity(mContext, cls);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_common_problems:
                //getCommonProblemsData();
                SinglePage.getInstance().toPage(mContext, "常见问题", SinglePage.COMMON_PROBLEMS,"");
                break;
            case R.id.ll_wipe_cache:
                showWipeCacheDialog();
                //RxActivityTool.skipActivity(mContext, TestActivity.class);
                break;
            case R.id.btn_logout:
                logout();
                break;
            case R.id.ll_about_us:
                //getAboutUsData();
                SinglePage.getInstance().toPage(mContext, "关于我们", SinglePage.ABOUT_US,"");
                break;
            default:
                break;
        }
    }

    private String getVersionNumber() {
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return info.versionName;
    }

    private void showWipeCacheDialog() {
        ConfirmDialog mDialog = new ConfirmDialog(mContext);
        mDialog.setMessage("确定清空缓存吗？");
        mDialog.setNegativeButton("取消",null);
        mDialog.setPositiveButton("确定", new ConfirmDialog.DialogInterface() {
            @Override
            public void OnClickListener() {
                cleanWipeCache();
                tvCacheSize.setText("0KB");
            }
        });
        mDialog.show();
    }

    /**
     * 计算缓存的大小
     */
    private void calculateCacheSize() {
        long fileSize = 0;
        String cacheSize = "0KB";
        File filesDir = mContext.getFilesDir();
        File cacheDir = mContext.getCacheDir();

        fileSize += RxFileTool.getDirSize(filesDir);
        fileSize += RxFileTool.getDirSize(cacheDir);
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (RxAppTool.isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            File externalCacheDir = RxFileTool
                    .getExternalCacheDir(mContext);
            fileSize += RxFileTool.getDirSize(externalCacheDir);
        }
        if (fileSize > 0)
            cacheSize = RxFileTool.formatFileSize(fileSize);
        tvCacheSize.setText(cacheSize);
    }

    /**
     * 清空缓存
     */
    private void cleanWipeCache() {
        RxFileTool.cleanInternalCache(this);
    }

    private void logout() {
        ConfirmDialog mDialog = new ConfirmDialog(mContext);
        mDialog.setMessage("确定退出登录？");
        mDialog.setNegativeButton("取消",null);
        mDialog.setPositiveButton("确定", new ConfirmDialog.DialogInterface() {
            @Override
            public void OnClickListener() {
                TokenCache.clear(mContext);
                // 更新用户登录状态
                EventBus.getDefault().post(new UpdateLoginStatus());
                UdeskHelper.logout();
                JpushUtils.delJpushAlias(mContext);
                RxActivityTool.finish(mContext);
            }
        });
        mDialog.show();
    }

    /**
     * 常见问题
     */
    private void getCommonProblemsData() {
        OkGo.<AMBaseDto<Page>>get(Constants.getCommonProblemsUrl)
                .tag(mContext)
                .execute(new NewsCallback<AMBaseDto<Page>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Page>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<Page>> response) {
                        dismissLoading();
                        if (response.body().code == 0) {
                            WebPageActivity.newInstance(mContext, "常见问题", Constants.MAIN_ENGINE_PIC + response.body().data.getH5Url());
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Page>> response) {
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
     * 关于我们
     */
    private void getAboutUsData() {
        OkGo.<AMBaseDto<Page>>get(Constants.getAboutUsUrl)
                .tag(mContext)
                .execute(new NewsCallback<AMBaseDto<Page>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Page>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<Page>> response) {
                        dismissLoading();
                        if (response.body().code == 0) {
                            WebPageActivity.newInstance(mContext, "关于我们", Constants.MAIN_ENGINE_PIC + response.body().data.getH5Url());
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Page>> response) {
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
