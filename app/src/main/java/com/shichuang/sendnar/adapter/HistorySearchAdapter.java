package com.shichuang.sendnar.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.sendnar.R;

import java.util.List;

/**
 * Created by Administrator on 2018/6/7.
 */

public class HistorySearchAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public HistorySearchAdapter() {
        super(R.layout.item_history_search);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_content, item);
    }
}
