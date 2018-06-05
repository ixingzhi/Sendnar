package com.shichuang.sendnar.adapter;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.open.tool.RxTimeTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.PointList;

/**
 * Created by Administrator on 2018/4/28.
 */

public class IntegralSubsidiaryAdapter extends BaseQuickAdapter<PointList.PointListModel, BaseViewHolder> {

    public IntegralSubsidiaryAdapter() {
        super(R.layout.item_integral_subsidiary);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final PointList.PointListModel item) {
        // 交易方式(收入=1|支出=2)
        if (item.getTradeType() == 1) {
            helper.setText(R.id.tv_point, "+" + item.getPointUp());
        } else {
            helper.setText(R.id.tv_point, "-" + item.getPointUp());
        }
        helper.setText(R.id.tv_time, RxTimeTool.stringFormat(item.getPointTradeTime()));
        helper.setText(R.id.tv_describe, item.getTradeDescribe());
        helper.setGone(R.id.tv_describe, item.isOpen());

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setOpen(item.isOpen() == true ? false : true);
                notifyItemChanged(helper.getAdapterPosition());
            }
        });
    }
}
