package com.shichuang.open.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shichuang.open.R;
import com.shichuang.open.tool.RxDeviceTool;
import com.shichuang.open.widget.drawable.Loading;

/**
 * 适用于加载数据，网络错误，加载失败
 * Created by xiedd on 2017/12/8.
 */

public class RxEmptyLayout extends LinearLayout implements View.OnClickListener {
    public static final int NETWORK_ERROR = 0x01;   // 网络错误
    public static final int NETWORK_LOADING = 0x02;  // 网络加载中
    public static final int EMPTY_DATA = 0x03; // 数据为空

    private Context mContext;
    private LinearLayout llRootView;
    private ImageView ivEmptyView;
    private Loading loading;
    private TextView tvMessage;
    private String emptyDataContent = "";
    private int mStatus;


    public RxEmptyLayout(Context context) {
        this(context, null);
    }

    public RxEmptyLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RxEmptyLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.biz_layout_empty, this, false);
        llRootView = view.findViewById(R.id.ll_root_view);
        ivEmptyView = (ImageView) view.findViewById(R.id.iv_empty_view);
        loading = (Loading) view.findViewById(R.id.loading);
        tvMessage = (TextView) view.findViewById(R.id.tv_message);
        setOnClickListener(this);
        addView(view);
    }

    @Override
    public void onClick(View view) {
        if (onEmptyLayoutClickListener != null) {
            if (mStatus != NETWORK_LOADING) {
                //setVisibility(View.GONE);
                onEmptyLayoutClickListener.onEmptyLayoutClick(mStatus);
            }
        }
    }

    /**
     * 设置显示类型
     */
    public void show(int showType) {
        switch (showType) {
            case NETWORK_ERROR:  // 网络错误
                mStatus = NETWORK_ERROR;
                ivEmptyView.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                loading.stop();
                if (RxDeviceTool.hasInternet()) {
                    tvMessage.setText(R.string.empty_view_network_error);
                    ivEmptyView.setImageResource(R.drawable.biz_ic_empty_data);
                } else {
                    tvMessage.setText(R.string.empty_view_network_error_click_to_refresh);
                    ivEmptyView.setImageResource(R.drawable.biz_ic_page_network);
                }
                setVisibility(View.VISIBLE);
                break;
            case NETWORK_LOADING:  // 加载中...
                mStatus = NETWORK_LOADING;
                ivEmptyView.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                loading.start();
                tvMessage.setText(R.string.empty_view_loading_data);
                setVisibility(View.VISIBLE);
                break;
            case EMPTY_DATA:
                mStatus = EMPTY_DATA;
                ivEmptyView.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                loading.stop();
                ivEmptyView.setImageResource(R.drawable.biz_ic_empty_data);
                if ("".equals(emptyDataContent)) {
                    tvMessage.setText(R.string.empty_view_empty_data);
                } else {
                    tvMessage.setText(emptyDataContent);
                }
                setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * 影藏布局
     */
    public void hide() {
        loading.stop();
        setVisibility(View.GONE);
    }

    /**
     * 设置空数据提示
     */
    public void setEmptyDataContent(String emptyDataContent) {
        this.emptyDataContent = emptyDataContent;
    }

    public OnEmptyLayoutClickListener onEmptyLayoutClickListener;

    public interface OnEmptyLayoutClickListener {
        void onEmptyLayoutClick(int status);
    }

    public void setOnEmptyLayoutClickListener(OnEmptyLayoutClickListener onEmptyLayoutClickListener) {
        this.onEmptyLayoutClickListener = onEmptyLayoutClickListener;
    }
}
