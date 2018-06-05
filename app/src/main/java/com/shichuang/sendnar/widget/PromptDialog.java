package com.shichuang.sendnar.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.shichuang.open.widget.BaseDialog;
import com.shichuang.sendnar.R;

/**
 * Created by Administrator on 2018/5/10.
 */

public class PromptDialog extends BaseDialog {

    public PromptDialog(Context context, int content) {
        super(context, 0.6f, Gravity.CENTER);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_prompt, null);
        View contentView = LayoutInflater.from(mContext).inflate(content, null);
        ((LinearLayout) view.findViewById(R.id.ll_content)).addView(contentView);

        setContentView(view);

        view.findViewById(R.id.tv_i_know).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public PromptDialog(Context context, View content){
        super(context, 0.6f, Gravity.CENTER);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_prompt, null);
        ((LinearLayout) view.findViewById(R.id.ll_content)).addView(content);

        setContentView(view);

        view.findViewById(R.id.tv_i_know).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
