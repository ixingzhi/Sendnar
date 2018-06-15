package com.shichuang.sendnar.common;

import android.content.Context;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.base.WebPageActivity;
import com.shichuang.sendnar.activity.SinglePageActivity;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Page;

/**
 * 单页
 * Created by Administrator on 2018/5/17.
 */

public class SinglePage {
    /**
     * 积分指南
     */
    public static final int INTEGRAL_GUIDE = 3;
    /**
     * 常见问题  Common problems
     */
    public static final int COMMON_PROBLEMS = 4;
    /**
     * 关于我们  About us
     */
    public static final int ABOUT_US = 5;
    /**
     * 勋章指南  The medal of guide
     */
    public static final int THE_MEDAL_OF_GUIDE = 6;
    /**
     * 发票须知  invoice information
     */
    public static final int INVOICE_INFORMATION = 7;
    /**
     * 公司简介  Company profile
     */
    public static final int COMPANY_PROFILE = 8;

    private static SinglePage instance;

    public static SinglePage getInstance() {
        if (null == instance) {
            instance = new SinglePage();
        }
        return instance;
    }

    public void toPage(final Context context, final String title, final int pageType, final String id) {
        OkGo.<AMBaseDto<Page>>get(Constants.getSinglePageUrl)
                .tag(context)
                .params("operation_type", pageType)
                .params("id", id)
                .execute(new NewsCallback<AMBaseDto<Page>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Page>, ? extends Request> request) {
                        super.onStart(request);
                        ((BaseActivity) context).showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<Page>> response) {
                        ((BaseActivity) context).dismissLoading();
                        if (response.body().code == 0 && response.body().data != null) {
                            // 如果是公司简介，显示分享
                            SinglePageActivity.newInstance (context, title, Constants.MAIN_ENGINE_PIC + response.body().data.getH5Url(), pageType == COMPANY_PROFILE, id);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Page>> response) {
                        super.onError(response);
                        ((BaseActivity) context).dismissLoading();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }
}
