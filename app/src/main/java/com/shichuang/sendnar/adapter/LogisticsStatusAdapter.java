package com.shichuang.sendnar.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.open.tool.RxToastTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.LogisticsInformation;

/**
 * Created by Administrator on 2018/4/19.
 */

public class LogisticsStatusAdapter extends BaseQuickAdapter<LogisticsInformation.LogisticsInformationModel, BaseViewHolder> {
    public LogisticsStatusAdapter() {
        super(R.layout.item_logistics_status);
    }

    @Override
    protected void convert(final BaseViewHolder helper, LogisticsInformation.LogisticsInformationModel item) {
        helper.setText(R.id.tv_logistics_info, item.getContext());
        helper.setText(R.id.tv_time, item.getTime());

        if (helper.getAdapterPosition() - 1 == 0) { // 第一个
            helper.setVisible(R.id.view_up, false);
            helper.setVisible(R.id.view_down, true);
            helper.setBackgroundRes(R.id.view_point, R.drawable.bg_logistics_point_current);
        } else if (helper.getAdapterPosition() == getData().size()) { // 最后一个
            helper.setVisible(R.id.view_up, true);
            helper.setVisible(R.id.view_down, false);
            helper.setBackgroundRes(R.id.view_point, R.drawable.bg_logistics_point_pass);
        } else { // 中间
            helper.setVisible(R.id.view_up, true);
            helper.setVisible(R.id.view_down, true);
            helper.setBackgroundRes(R.id.view_point, R.drawable.bg_logistics_point_pass);
        }
    }
}
