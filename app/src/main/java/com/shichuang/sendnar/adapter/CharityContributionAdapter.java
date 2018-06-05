package com.shichuang.sendnar.adapter;

import android.util.Log;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout;
import com.shichuang.open.tool.RxBigDecimalTool;
import com.shichuang.open.tool.RxGlideTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.CharityContribution;

/**
 * Created by Administrator on 2018/4/23.
 */

public class CharityContributionAdapter extends BaseQuickAdapter<CharityContribution.CharityContributionModel, BaseViewHolder> {
    private boolean canRightSwipe;

    public CharityContributionAdapter() {
        super(R.layout.item_charity_contribution);
    }

    @Override
    protected void convert(final BaseViewHolder helper, CharityContribution.CharityContributionModel item) {
        helper.setText(R.id.tv_serial_number, String.valueOf(helper.getAdapterPosition() + 1));
        RxGlideTool.loadImageView(mContext, Utils.getSingleImageUrlByImageUrls(item.getHeadPortrait()), (ImageView) helper.getView(R.id.iv_avatar), R.drawable.ic_avatar_default);
        helper.setText(R.id.tv_username, item.getNickname());
        helper.setText(R.id.tv_this_month_amount, "本月：¥" + RxBigDecimalTool.toDecimal(item.getShouyiMonth(), 2));
        helper.setText(R.id.tv_aggregate_amount, "累计：¥" + RxBigDecimalTool.toDecimal(item.getShouyi(), 2));
        // 设置是否可以右侧滑动
        ((EasySwipeMenuLayout) helper.getView(R.id.swipe_menu_layout)).setCanLeftSwipe(canRightSwipe);
        helper.addOnClickListener(R.id.right);
    }

    public void setCanRightSwipe(boolean bool) {
        this.canRightSwipe = bool;
    }
}
