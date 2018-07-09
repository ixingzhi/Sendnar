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

public class GiftReceiveDialog extends BaseDialog {

    public GiftReceiveDialog(Context context) {
        super(context, 0.6f, Gravity.CENTER);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_gift_receive, null);
        setContentView(view);

        view.findViewById(R.id.tv_i_know).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (mOnClickListener != null) {
                    mOnClickListener.onClick();
                }
            }
        });
    }

    private OnClickListener mOnClickListener;

    public interface OnClickListener {
        void onClick();
    }

    public void setOnClickListener(OnClickListener listener) {
        this.mOnClickListener = listener;
    }
}
