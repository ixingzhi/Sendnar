package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
import com.shichuang.sendnar.entify.LogisticsCompany;
import com.shichuang.sendnar.event.UpdateOrderEvent;
import com.shichuang.sendnar.widget.OptionDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 填写物流
 * Created by Administrator on 2018/6/5.
 */

public class FillInTheLogisticsActivity extends BaseActivity {
    private EditText mEtLogisticsNo;
    private EditText mEtContactInformation;
    private TextView mTvExpressMode;

    private List<LogisticsCompany> logisticsCompanyList;
    private String logisticsCompanyName;
    private int orderDetailId;


    @Override
    public int getLayoutId() {
        return R.layout.activity_fill_in_the_logistics;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        orderDetailId = getIntent().getIntExtra("orderDetailId", 0);
        mEtLogisticsNo = view.findViewById(R.id.et_logistics_no);
        mEtContactInformation = view.findViewById(R.id.et_contact_information);
        mTvExpressMode = view.findViewById(R.id.tv_express_mode);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.ll_select_express_mode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logisticsCompanyList != null && logisticsCompanyList.size() > 0) {
                    List<OptionDialog.Option> list = new ArrayList<>();
                    for (LogisticsCompany model : logisticsCompanyList) {
                        list.add(new OptionDialog.Option(model.getId(), model.getName()));
                    }
                    OptionDialog mDialog = new OptionDialog(mContext);
                    mDialog.setTitle("快递方式");
                    mDialog.setData(list);
                    mDialog.setOnOptionClickListener(new OptionDialog.OnOptionClickListener() {
                        @Override
                        public void onClick(OptionDialog.Option option, int position) {
                            logisticsCompanyName = option.getName();
                            mTvExpressMode.setText(option.getName());
                        }
                    });
                    mDialog.show();
                }
            }
        });
        findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInfo();
            }
        });
    }

    @Override
    public void initData() {
        getLogisticsCompanyData();
    }

    private void getLogisticsCompanyData() {
        OkGo.<AMBaseDto<List<LogisticsCompany>>>get(Constants.logisticsCompanyListUrl)
                .tag(mContext)
                .execute(new NewsCallback<AMBaseDto<List<LogisticsCompany>>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<List<LogisticsCompany>>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<List<LogisticsCompany>>> response) {
                        if (response.body().code == 0) {
                            logisticsCompanyList = response.body().data;
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<List<LogisticsCompany>>> response) {
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

    private void checkInfo() {
        String logisticsNo = mEtLogisticsNo.getText().toString();
        String contactInformation = mEtContactInformation.getText().toString();

        if (TextUtils.isEmpty(logisticsCompanyName)) {
            showToast("请选择快递方式");
        } else if (TextUtils.isEmpty(logisticsNo)) {
            showToast("请填写物流单号");
        } else if (TextUtils.isEmpty(contactInformation)) {
            showToast("请填写联系方式");
        } else {
            commit(logisticsNo, contactInformation);
        }
    }

    private void commit(String logisticsNo, String contactInformation) {
        OkGo.<AMBaseDto<Empty>>post(Constants.fillInTheLogisticsUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("order_detail_id", orderDetailId)
                .params("express_company", logisticsCompanyName)
                .params("express_no", logisticsNo)
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
                                    RxActivityTool.finish(mContext, RESULT_OK);
                                    EventBus.getDefault().post(new UpdateOrderEvent());
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
