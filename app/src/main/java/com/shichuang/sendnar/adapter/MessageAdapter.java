package com.shichuang.sendnar.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.entify.MessageList;

/**
 * Created by Administrator on 2018/5/25.
 */

public class MessageAdapter extends BaseQuickAdapter<MessageList.MessageListModel,BaseViewHolder> {
    public MessageAdapter() {
        super(R.layout.item_message);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageList.MessageListModel item) {
//        helper.setText(R.id.tv_time, RxTimeTool.stringFormat(item.getAddTime()));
//        helper.setText(R.id.tv_title, item.getMessageTitle());
//        helper.setText(R.id.tv_message_content, item.getMessageContent());

        if(item.isOpenMessageContent()){
            helper.setImageResource(R.id.iv_open_status,R.drawable.ic_arrows_up_gray);
            ((TextView)helper.getView(R.id.tv_message_content)).setMaxLines(Integer.MAX_VALUE);
        }else{
            helper.setImageResource(R.id.iv_open_status,R.drawable.ic_arrows_down_gray);
            ((TextView)helper.getView(R.id.tv_message_content)).setMaxLines(2);
        }
    }
}
