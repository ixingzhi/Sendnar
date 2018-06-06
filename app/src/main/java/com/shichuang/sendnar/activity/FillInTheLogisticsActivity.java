package com.shichuang.sendnar.activity;

import android.os.Bundle;
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
 * 填写物流
 * Created by Administrator on 2018/6/5.
 */

public class FillInTheLogisticsActivity extends BaseActivity {
    private EditText mEtLogisticsNo;
    private EditText mEtContactInformation;

    @Override
    public int getLayoutId() {
        return R.layout.activity_fill_in_the_logistics;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mEtLogisticsNo = view.findViewById(R.id.et_logistics_no);
        mEtContactInformation = view.findViewById(R.id.et_contact_information);
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
        String logisticsNo = mEtLogisticsNo.getText().toString();
        String contactInformation = mEtContactInformation.getText().toString();

        if (TextUtils.isEmpty(logisticsNo)) {
            showToast("请填写物流单号");
        } else if (TextUtils.isEmpty(contactInformation)) {
            showToast("请填写联系方式");
        } else {
            commit(logisticsNo, contactInformation);
        }
    }

    private void commit(String logisticsNo, String contactInformation) {
        OkGo.<AMBaseDto<Empty>>post("")
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("order_detail_id", "")
                .params("express_company", "")
                .params("express_no", "")
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
                            RxActivityTool.finish(mContext, RESULT_OK);
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
