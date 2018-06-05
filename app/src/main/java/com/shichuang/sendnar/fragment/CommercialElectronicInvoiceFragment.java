package com.shichuang.sendnar.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shichuang.open.base.BaseFragment;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.entify.Invoice;

/**
 * Created by Administrator on 2018/4/19.
 */

public class CommercialElectronicInvoiceFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout mLlCompanyLayout;
    private ImageView mIvCompany;
    private ImageView mIvPersonage;
    private EditText mEtCompanyName;
    private EditText mEtCompanyCode;
    private EditText mEtReceiveManPhoneNumber;
    private EditText mEtReceiveManEmail;
    private Button mBtnGoodsDetails;
    private Button mBtnGoodsCategory;


    public static CommercialElectronicInvoiceFragment newInstance() {
        CommercialElectronicInvoiceFragment mCommercialElectronicInvoiceFragment = new CommercialElectronicInvoiceFragment();
        return mCommercialElectronicInvoiceFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_commercial_electronicl_invoice;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mLlCompanyLayout = view.findViewById(R.id.ll_company_layout);
        mIvCompany = view.findViewById(R.id.iv_company);
        mIvPersonage = view.findViewById(R.id.iv_personage);
        mEtCompanyName = view.findViewById(R.id.et_company_name);
        mEtCompanyCode = view.findViewById(R.id.et_company_code);
        mEtReceiveManPhoneNumber = view.findViewById(R.id.et_receive_man_phone_number);
        mEtReceiveManEmail = view.findViewById(R.id.et_receive_man_email);
        mBtnGoodsDetails = view.findViewById(R.id.btn_goods_details);
        mBtnGoodsCategory = view.findViewById(R.id.btn_goods_category);

        selectInvoiceLookedUp(0);
        selectInvoiceContent(0);
    }

    @Override
    public void initEvent() {
        mContentView.findViewById(R.id.ll_personage).setOnClickListener(this);
        mContentView.findViewById(R.id.ll_company).setOnClickListener(this);
        mBtnGoodsDetails.setOnClickListener(this);
        mBtnGoodsCategory.setOnClickListener(this);
        mContentView.findViewById(R.id.btn_ok).setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_personage:
                selectInvoiceLookedUp(0);
                break;
            case R.id.ll_company:
                selectInvoiceLookedUp(1);
                break;
            case R.id.btn_goods_details:
                selectInvoiceContent(0);
                break;
            case R.id.btn_goods_category:
                selectInvoiceContent(1);
                break;
            case R.id.btn_ok:
                checkInfo();
                break;
            default:
                break;
        }
    }

    private void selectInvoiceLookedUp(int i) {
        if (i == 0) {  // 个人
            mIvPersonage.setSelected(true);
            mIvCompany.setSelected(false);
            mLlCompanyLayout.setVisibility(View.GONE);
        } else {
            mIvPersonage.setSelected(false);
            mIvCompany.setSelected(true);
            mLlCompanyLayout.setVisibility(View.VISIBLE);
        }
    }


    private void selectInvoiceContent(int i) {
        if (i == 0) {  // 商品明细
            mBtnGoodsDetails.setSelected(true);
            mBtnGoodsCategory.setSelected(false);
        } else {
            mBtnGoodsDetails.setSelected(false);
            mBtnGoodsCategory.setSelected(true);
        }
    }

    private void checkInfo() {
        int type = 2;
        int head = 0;
        String companyName = "";
        String companyCode = "";
        int goodType = 0;
        String receiverEmail = "";
        String receiverPhoneNum = "";

        if (mIvPersonage.isSelected()) {  // 个人
            head = 1;
            String phone = mEtReceiveManPhoneNumber.getText().toString().trim();
            String email = mEtReceiveManEmail.getText().toString().trim();
            if (TextUtils.isEmpty(phone)) {
                showToast("请填写收票人手机号");
                return;
            }
            if (TextUtils.isEmpty(email)) {
                showToast("请填写收票人邮箱");
                return;
            }
            receiverEmail = email;
            receiverPhoneNum = phone;
        } else {  // 单位
            head = 2;
            String name = mEtCompanyName.getText().toString().trim();
            String code = mEtCompanyCode.getText().toString().trim();
            String phone = mEtReceiveManPhoneNumber.getText().toString().trim();
            String email = mEtReceiveManEmail.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                showToast("请填写单位名称");
                return;
            }
            if (TextUtils.isEmpty(code)) {
                showToast("请填写纳税人识别号");
                return;
            }
            if (TextUtils.isEmpty(phone)) {
                showToast("请填写收票人手机号");
                return;
            }
            if (TextUtils.isEmpty(email)) {
                showToast("请填写收票人邮箱");
                return;
            }
            companyName = name;
            companyCode = code;
            receiverEmail = email;
            receiverPhoneNum = phone;
        }
        goodType = mBtnGoodsDetails.isSelected() ? 1 : 2;

        Invoice mInvoice = new Invoice();
        mInvoice.setType(type);
        mInvoice.setHead(head);
        mInvoice.setCompanyName(companyName);
        mInvoice.setUserCode(companyCode);
        mInvoice.setGoodsType(goodType);
        mInvoice.setEmail(receiverEmail);
        mInvoice.setPhoneNum(receiverPhoneNum);

        Bundle bundle = new Bundle();
        bundle.putSerializable("invoice", mInvoice);

        RxActivityTool.finish(mContext, bundle, getActivity().RESULT_OK);
    }

    /**
     * 如果存在发票历史信息，则填充信息
     */
    public void handleData(Invoice invoice) {
        if (invoice.getHead() == 1) {  // 个人
            selectInvoiceLookedUp(0);
            if (invoice.getGoodsType() == 1) {  // 发票详情类型 1 明细 || 2 类别
                selectInvoiceContent(0);
            } else if (invoice.getGoodsType() == 2) {
                selectInvoiceContent(1);
            }
            // 收票人手机
            mEtReceiveManPhoneNumber.setText(invoice.getPhoneNum());
            // 收票人邮箱
            mEtReceiveManEmail.setText(invoice.getEmail());
        } else if (invoice.getHead() == 2) {   // 单位
            selectInvoiceLookedUp(1);
            if (invoice.getGoodsType() == 1) {  // 发票详情类型 1 明细 || 2 类别
                selectInvoiceContent(0);
            } else if (invoice.getGoodsType() == 2) {
                selectInvoiceContent(1);
            }
            // 单位名称
            mEtCompanyName.setText(invoice.getCompanyName());
            // 商品类别
            mEtCompanyCode.setText(invoice.getUserCode());
            // 收票人手机
            mEtReceiveManPhoneNumber.setText(invoice.getPhoneNum());
            // 收票人邮箱
            mEtReceiveManEmail.setText(invoice.getEmail());
        }
    }
}