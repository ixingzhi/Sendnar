package com.shichuang.sendnar.adapter;

import android.util.Log;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.open.tool.RxBigDecimalTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.entify.MyOrder;
import com.shichuang.sendnar.entify.WithdrawalRecord;

import java.util.List;

/**
 * Created by Administrator on 2018/4/22.
 */

public class WithdrawalRecordAdapter extends BaseMultiItemQuickAdapter<WithdrawalRecord, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public WithdrawalRecordAdapter(List<WithdrawalRecord> data) {
        super(data);
        addItemType(WithdrawalRecord.WITHDRAWAL_HEADER, R.layout.item_withdrawal_record_header);
        addItemType(WithdrawalRecord.WITHDRAWAL_BODY, R.layout.item_withdrawal_record_body);
    }

    @Override
    protected void convert(BaseViewHolder helper, WithdrawalRecord item) {
        switch (helper.getItemViewType()) {
            case WithdrawalRecord.WITHDRAWAL_HEADER:
                helper.setText(R.id.tv_time, item.getTime());
                break;
            case WithdrawalRecord.WITHDRAWAL_BODY:
                int position = item.getPosition();
                WithdrawalRecord.MonthWithdrawalData monthWithdrawalData = item.getMonthData().get(position);
                helper.setText(R.id.tv_to_account, monthWithdrawalData.getWithdrawInfo());
                helper.setText(R.id.tv_withdrawal_amount, RxBigDecimalTool.toDecimal(monthWithdrawalData.getWithdrawAmount(), 2));
                helper.setText(R.id.tv_time, monthWithdrawalData.getSucessTime());
                break;
            default:
                break;
        }
    }
}
