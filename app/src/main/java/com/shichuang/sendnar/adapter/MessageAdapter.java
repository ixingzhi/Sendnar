package com.shichuang.sendnar.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.open.tool.RxTimeTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.entify.MessageList;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2018/5/25.
 */

public class MessageAdapter extends BaseQuickAdapter<MessageList, BaseViewHolder> {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public MessageAdapter() {
        super(R.layout.item_message);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageList item) {
        helper.setText(R.id.tv_time, RxTimeTool.stringFormat(item.getNewMessage().getSendTime(), format));
        helper.setText(R.id.tv_message_title, item.getNewMessage().getMessageTitle());
        helper.setText(R.id.tv_message_content, item.getNewMessage().getMessageContent());

        if (item.isOpenMessageContent()) {
            helper.setImageResource(R.id.iv_open_status, R.drawable.ic_arrows_up_gray);
            ((TextView) helper.getView(R.id.tv_message_content)).setMaxLines(Integer.MAX_VALUE);
        } else {
            helper.setImageResource(R.id.iv_open_status, R.drawable.ic_arrows_down_gray);
            ((TextView) helper.getView(R.id.tv_message_content)).setMaxLines(2);
        }
    }
}
