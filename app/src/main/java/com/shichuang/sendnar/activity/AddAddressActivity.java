package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxKeyboardTool;
import com.shichuang.sendnar.entify.Address;
import com.shichuang.sendnar.widget.address.CityInterface;
import com.shichuang.sendnar.widget.address.SelectAddressDialog;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.widget.RxTitleBar;

/**
 * 新增收货地址，修改收货地址
 * Created by Administrator on 2018/4/19.
 */

public class AddAddressActivity extends BaseActivity {
    private RxTitleBar mTitleBar;
    private EditText mEtUsername;
    private EditText mEtPhoneNumber;
    private TextView mTvAddress;
    private EditText mEtDetailsAddress;
    private ImageView mIvDefaultAddressSwitch;

    private String provinceId = "";
    private String cityId = "";
    private String areaId = "";
    // 标识修改地址，新增地址
    private int from;
    private Address address;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_address;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        from = getIntent().getIntExtra("from", 0);
        address = (Address) getIntent().getSerializableExtra("address");
        mTitleBar = (RxTitleBar) findViewById(R.id.title_bar);
        mEtUsername = (EditText) findViewById(R.id.et_username);
        mEtPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
        mTvAddress = (TextView) findViewById(R.id.tv_address);
        mEtDetailsAddress = (EditText) findViewById(R.id.et_details_address);
        mIvDefaultAddressSwitch = (ImageView) findViewById(R.id.iv_default_address_switch);
        //SelectAddressDialog.init();
        mTitleBar.setTitle(from == MyAddressActivity.ADD_ADDRESS ? "新建地址" : "修改地址");
        if (from == MyAddressActivity.EDIT_ADDRESS) {
            if (address != null) {
                mEtUsername.setText(address.getName());
                mEtPhoneNumber.setText(address.getPhone());
                mTvAddress.setText(address.getProvinceName() + address.getCityName() + address.getAreaName());
                mEtDetailsAddress.setText(address.getAddress());
                provinceId = address.getProvinceId();
                cityId = address.getCityId();
                areaId = address.getAreaId();
                mIvDefaultAddressSwitch.setSelected(address.getIsDefault() == 2 ? true : false);
            }
        }
    }

    @Override
    public void initEvent() {
        findViewById(R.id.ll_select_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxKeyboardTool.hideSoftInput(AddAddressActivity.this);
                SelectAddressDialog mDialog = new SelectAddressDialog(mContext, 0.7f, Gravity.BOTTOM);
                mDialog.setOnSelectListener(new SelectAddressDialog.OnSelectListener() {
                    @Override
                    public void onSelected(CityInterface province, CityInterface city, CityInterface area) {
                        provinceId = province.getCityCode();
                        cityId = city.getCityCode();
                        areaId = area.getCityCode();
                        mTvAddress.setText(province.getCityName() + city.getCityName() + area.getCityName());
                    }
                });
                mDialog.show();
            }
        });
        // 设为默认地址
        findViewById(R.id.ll_set_default_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvDefaultAddressSwitch.setSelected(mIvDefaultAddressSwitch.isSelected() ? false : true);
            }
        });
        mTitleBar.setTitleBarClickListener(new RxTitleBar.TitleBarClickListener() {
            @Override
            public void onRightClick() {
                checkInfo();
            }
        });
    }

    @Override
    public void initData() {
    }

    private void checkInfo() {
        String username = mEtUsername.getText().toString().trim();
        String phoneNumber = mEtPhoneNumber.getText().toString().trim();
        String detailsAddress = mEtDetailsAddress.getText().toString().trim();

        int isDefault = mIvDefaultAddressSwitch.isSelected() ? 2 : 1;

        if (TextUtils.isEmpty(username)) {
            showToast("请输入姓名");
        } else if (TextUtils.isEmpty(phoneNumber)) {
            showToast("请输入手机号");
        } else if (TextUtils.isEmpty(provinceId) || TextUtils.isEmpty(provinceId) || TextUtils.isEmpty(provinceId)) {
            showToast("请选择省市区");
        } else if (TextUtils.isEmpty(detailsAddress)) {
            showToast("请输入详细地址");
        } else {
            addOrEditAddress(username, phoneNumber, detailsAddress, isDefault);
        }
    }

    private void addOrEditAddress(String username, String phoneNumber, String detailsAddress, int isDefault) {
        OkGo.<AMBaseDto<Empty>>post(from == MyAddressActivity.ADD_ADDRESS ? Constants.addAddressUrl : Constants.editAddressUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("id", address != null ? address.getId() : -1)
                .params("name", username)
                .params("phone", phoneNumber)
                .params("area_id", areaId)
                .params("province_id", provinceId)
                .params("city_id", cityId)
                .params("address", detailsAddress)
                .params("is_default", isDefault)   //  2默认||1不默认
                .execute(new NewsCallback<AMBaseDto<Empty>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Empty>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<Empty>> response) {
                        showToast(response.body().msg);
                        dismissLoading();
                        if (response.body().code == 0) {
                            RxActivityTool.finish(mContext, RESULT_OK);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Empty>> response) {
                        super.onError(response);
                        showToast(response.getException().getMessage());
                        dismissLoading();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

}
