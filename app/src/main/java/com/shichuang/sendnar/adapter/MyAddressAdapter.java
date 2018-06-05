package com.shichuang.sendnar.adapter;


import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.activity.AddAddressActivity;
import com.shichuang.sendnar.entify.Address;
import com.shichuang.sendnar.entify.Empty;


/**
 * Created by Administrator on 2018/4/17.
 */

public class MyAddressAdapter extends BaseQuickAdapter<Address, BaseViewHolder> {

    public MyAddressAdapter() {
        super(R.layout.item_my_address);
    }

    @Override
    protected void convert(BaseViewHolder helper, Address item) {
        helper.setText(R.id.tv_username, item.getName() + "    " + item.getPhone());
        helper.setText(R.id.tv_details_address, item.getProvinceName() + item.getCityName() + item.getAreaName() + item.getAddress());
        // 默认 2 || 不默认 1
        if (item.getIsDefault() == 2) {
            helper.getView(R.id.iv_is_default_address).setSelected(true);
            helper.setText(R.id.tv_default_address, "默认地址");
            helper.setTextColor(R.id.tv_default_address, mContext.getResources().getColor(R.color.red));
        } else {
            helper.getView(R.id.iv_is_default_address).setSelected(false);
            helper.setText(R.id.tv_default_address, "设为默认地址");
            helper.setTextColor(R.id.tv_default_address, mContext.getResources().getColor(R.color.textColor2));
        }

        helper.addOnClickListener(R.id.ll_set_default_address);
        helper.addOnClickListener(R.id.ll_edit);
        helper.addOnClickListener(R.id.ll_delete);
    }
}
