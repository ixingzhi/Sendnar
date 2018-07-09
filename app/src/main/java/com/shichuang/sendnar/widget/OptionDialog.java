package com.shichuang.sendnar.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shichuang.open.widget.BaseDialog;
import com.shichuang.sendnar.R;

import java.util.List;

/**
 * Created by Administrator on 2018/6/6.
 */

public class OptionDialog extends BaseDialog {
    private View view;
    private TextView mTvTitle;
    private OptionAdapter mAdOption;

    public OptionDialog(Context context) {
        super(context, 0.6f, Gravity.BOTTOM);
        setFullScreenWidth();
        view = LayoutInflater.from(mContext).inflate(R.layout.dialog_option, null);
        setContentView(view);
        initView();
    }

    private void initView() {
        mTvTitle = view.findViewById(R.id.tv_title);
        view.findViewById(R.id.tv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        RecyclerView mRvOption = view.findViewById(R.id.recycler_view);
        mRvOption.setLayoutManager(new LinearLayoutManager(mContext));
        mAdOption = new OptionAdapter();
        mRvOption.setAdapter(mAdOption);
    }

    private class OptionAdapter extends BaseQuickAdapter<Option, BaseViewHolder> {

        public OptionAdapter() {
            super(R.layout.item_option);
        }

        @Override
        protected void convert(final BaseViewHolder helper, final Option item) {
            helper.setText(R.id.tv_content, item.getName());
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (onOptionClickListener != null) {
                        onOptionClickListener.onClick(item, helper.getAdapterPosition());
                    } else {

                    }
                }
            });
        }
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            mTvTitle.setText(title);
        } else {
            mTvTitle.setVisibility(View.GONE);
        }
    }

    public void setData(List<Option> list) {
        mAdOption.addData(list);
    }

    private OnOptionClickListener onOptionClickListener;

    public interface OnOptionClickListener {
        void onClick(Option option, int position);
    }

    public void setOnOptionClickListener(OnOptionClickListener listener) {
        this.onOptionClickListener = listener;
    }

    public static class Option {
        private int id;
        private String name;

        public Option(String name) {
            this(0, name);
        }

        public Option(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
