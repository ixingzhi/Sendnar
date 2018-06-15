package com.shichuang.sendnar.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shichuang.open.widget.BaseDialog;
import com.shichuang.sendnar.R;

/**
 * Created by Administrator on 2018/5/29.
 */

public class ConfirmDialog extends BaseDialog implements View.OnClickListener {
    private View mView;
    private TextView mTitle;
    private TextView mMessage;
    private Button mBtnNegative;
    private Button mBtnPositive;

    private DialogInterface mNegativeInterface;
    private DialogInterface mPositiveInterface;

    public ConfirmDialog(Context context) {
        super(context, 0.6f, Gravity.CENTER);
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_confirm, null);
        setContentView(mView);
        initView();
        initEvent();
    }

    private void initView() {
        mTitle = mView.findViewById(R.id.tv_title);
        mMessage = mView.findViewById(R.id.tv_message);
        mBtnNegative = mView.findViewById(R.id.btn_negative);
        mBtnPositive = mView.findViewById(R.id.btn_positive);
    }

    private void initEvent() {
        mBtnNegative.setOnClickListener(this);
        mBtnPositive.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_negative:
                dismiss();
                if (mNegativeInterface != null) {
                    mNegativeInterface.OnClickListener();
                }
                break;
            case R.id.btn_positive:
                dismiss();
                if (mPositiveInterface != null) {
                    mPositiveInterface.OnClickListener();
                }
                break;
            default:
                break;
        }
    }

    public void setTitle(String title) {
        mTitle.setText(title == null ? "" : title);
    }

    public void hideTitle() {
        mTitle.setVisibility(View.GONE);
    }

    public void setMessage(String message) {
        mMessage.setText(message == null ? "" : message);
    }

    public void setNegativeButton(String negativeText, DialogInterface listener) {
        mBtnNegative.setText(TextUtils.isEmpty(negativeText) ? "取消" : negativeText);
        mNegativeInterface = listener;
    }

    public void setPositiveButton(String positiveText, DialogInterface listener) {
        mBtnPositive.setText(TextUtils.isEmpty(positiveText) ? "取消" : positiveText);
        mPositiveInterface = listener;
    }

    public interface DialogInterface {
        void OnClickListener();
    }
}
