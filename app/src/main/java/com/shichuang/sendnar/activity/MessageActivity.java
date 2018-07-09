package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.widget.RxEmptyLayout;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.adapter.MessageAdapter;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.MessageCountHelper;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.MessageCount;
import com.shichuang.sendnar.entify.MessageList;
import com.shichuang.sendnar.event.MessageCountEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Administrator on 2018/5/24.
 */

public class MessageActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MessageAdapter mAdapter;
    private RxEmptyLayout mEmptyLayout;

    @Override
    public int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new MessageAdapter();
        // 当列表滑动到倒数第N个Item的时候(默认是1)回调onLoadMoreRequested方法
        mAdapter.setPreLoadNumber(2);
        mRecyclerView.setAdapter(mAdapter);

        mEmptyLayout = view.findViewById(R.id.empty_layout);
        mEmptyLayout.setOnEmptyLayoutClickListener(new RxEmptyLayout.OnEmptyLayoutClickListener() {
            @Override
            public void onEmptyLayoutClick(int status) {
                if (status != RxEmptyLayout.NETWORK_LOADING) {
                    refresh();
                }
            }
        });
    }

    @Override
    public void initEvent() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                rotateAnim(mAdapter.getViewByPosition(mRecyclerView, position, R.id.iv_open_status), 180f);
                boolean openStatus = !mAdapter.getItem(position).isOpenMessageContent();
                mAdapter.getItem(position).setOpenMessageContent(openStatus);
                mAdapter.notifyItemChanged(position);

                // 阅读消息
                readMessage(mAdapter.getItem(position).getId());
            }
        });
    }

    @Override
    public void initData() {
        refresh();
    }

    private void refresh() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                getMessageListData();
            }
        });
    }

    private void getMessageListData() {
        OkGo.<AMBaseDto<List<MessageList>>>get(Constants.messageCenterUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .execute(new NewsCallback<AMBaseDto<List<MessageList>>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<List<MessageList>>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<List<MessageList>>> response) {
                        if (response.body().code == 0) {
                            List<MessageList> list = response.body().data;
                            if (list != null && list.size() > 0) {
                                setData(list);
                            } else {
                                mEmptyLayout.show(RxEmptyLayout.EMPTY_DATA);
                            }
                        } else {
                            showToast(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<List<MessageList>>> response) {
                        super.onError(response);
                        mEmptyLayout.show(RxEmptyLayout.NETWORK_ERROR);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void setData(List<MessageList> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mAdapter.setNewData(data);
        } else {
            mAdapter.addData(data);
        }
    }

    private void rotateAnim(View view, float rotate) {
        Animation anim = new RotateAnimation(0f, rotate, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true); // 设置保持动画最后的状态
        anim.setDuration(400); // 设置动画时间
        //anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
        view.startAnimation(anim);
    }

    private void readMessage(int id) {
        OkGo.<AMBaseDto<Empty>>get(Constants.readMessageUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("message_id", id)
                .execute(new NewsCallback<AMBaseDto<Empty>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Empty>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<Empty>> response) {
                        if (response.body().code == 0) {
                            MessageCountHelper.getInstance().getCount(mContext);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Empty>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }
}
