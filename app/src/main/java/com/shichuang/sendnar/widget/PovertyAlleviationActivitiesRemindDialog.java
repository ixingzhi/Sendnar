package com.shichuang.sendnar.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.shichuang.open.widget.BaseDialog;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.Setting;

/**
 * Created by Administrator on 2018/5/8.
 */

public class PovertyAlleviationActivitiesRemindDialog extends BaseDialog {

    public PovertyAlleviationActivitiesRemindDialog(Context context) {
        super(context, 0.6f, Gravity.NO_GRAVITY);
        setFullScreen();
        setCancelable(false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_poverty_alleviation_activities_remind, null);
        setContentView(view);

        final ImageView mIvNoLongerRemind = view.findViewById(R.id.iv_no_longer_remind);
        mIvNoLongerRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvNoLongerRemind.setSelected(mIvNoLongerRemind.isSelected() ? false : true);
            }
        });

        view.findViewById(R.id.iv_i_know).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Setting.updatePovertyAlleviationActivitiesRemind(mContext, mIvNoLongerRemind.isSelected() ? false : true);
                dismiss();
            }
        });
    }
}
