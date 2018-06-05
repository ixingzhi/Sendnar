package com.shichuang.open.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.shichuang.open.R;


/**
 * Created by Administrator on 2017/9/28.
 */

public class RxLoadDialog extends Dialog {
    public Context context;

    public RxLoadDialog(Context context) {
        super(context, R.style.LoadingDialog);
        this.context = context;
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
        Window window = this.getWindow();
        window.setWindowAnimations(R.style.DialogWindowStyle);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.biz_dialog_loading);
    }
}
