package com.shichuang.sendnar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxTabLayoutTool;
import com.shichuang.open.widget.RxEmptyLayout;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.adapter.GiftGivingAdapter;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.GiftStatus;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.common.UserCache;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.FestivalList;
import com.shichuang.sendnar.entify.LookGiftInfo;
import com.shichuang.sendnar.widget.RxTitleBar;
import com.shichuang.sendnar.widget.ShareDialog;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

/**
 * 送出的礼物
 * Created by Administrator on 2018/4/23.
 */

public class GiftGivingActivity extends BaseActivity {
    // 收礼
    private static final int RECEIVE_GIFT = 0x11;
    // 转赠
    private static final int EXAMPLES_GIFT = 0x12;
    private RxEmptyLayout mEmptyLayout;
    private TabLayout mTabLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private GiftGivingAdapter mAdapter;

    private int giftStatus = GiftStatus.WAIT_RECEIVE;
    private int pageSize = 10;
    private int pageIndex = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_gift_giving;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        ((RxTitleBar) findViewById(R.id.title_bar)).setTitle("送出的礼物");
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.addTab(mTabLayout.newTab().setText("待接收"));
        mTabLayout.addTab(mTabLayout.newTab().setText("已超时"));
        mTabLayout.addTab(mTabLayout.newTab().setText("已接收"));
        RxTabLayoutTool.setIndicator(mContext,mTabLayout,24,24);

        mEmptyLayout = (RxEmptyLayout) findViewById(R.id.empty_layout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GiftGivingAdapter();
        mAdapter.setPreLoadNumber(2);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        giftStatus = GiftStatus.WAIT_RECEIVE;
                        break;
                    case 1:
                        giftStatus = GiftStatus.TIMEOUT;
                        break;
                    case 2:
                        giftStatus = GiftStatus.HAS_RECEIVE;
                        break;
                }
                mAdapter.setGiftStatus(giftStatus);
                refresh();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        mEmptyLayout.setOnEmptyLayoutClickListener(new RxEmptyLayout.OnEmptyLayoutClickListener() {
            @Override
            public void onEmptyLayoutClick(int status) {
                refresh();
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, mRecyclerView);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                LookGiftInfo.LookGiftInfoModel item = mAdapter.getItem(position);
                switch (view.getId()) {
                    case R.id.btn_gather_together_red_packet:
                        Bundle bundle = new Bundle();
                        bundle.putInt("gatherTogetherRedPacketId", item.getReceiveGiftId());
                        RxActivityTool.skipActivity(mContext, MyItemsActivity.class, bundle);
                        break;
                    case R.id.btn_examples_of:
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("ids", item.getId()+"");
                        bundle1.putString("description", item.getShopName());
                        bundle1.putString("imgShareUrl", Utils.getSingleImageUrlByImageUrls(item.getPic()));
                        bundle1.putBoolean("isExamples", true);
                        bundle1.putInt("type", 1);
                        RxActivityTool.skipActivityForResult(GiftGivingActivity.this, GreetingsActivity.class, bundle1, EXAMPLES_GIFT);
                        break;
                    case R.id.btn_receive:
                        Bundle bundle2 = new Bundle();
                        bundle2.putInt("goodsId", item.getGoodsId());
                        bundle2.putInt("giftOrderId", item.getId());
                        RxActivityTool.skipActivityForResult(GiftGivingActivity.this, GiftReceivedConfirmOrderActivity.class, bundle2, RECEIVE_GIFT);
                        break;
                }
            }
        });
    }

    @Override
    public void initData() {
        mAdapter.setGiftStatus(giftStatus);
        refresh();
    }

    private void refresh() {
        pageIndex = 1;
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                getActionData();
            }
        });
    }

    private void loadMore() {
        getActionData();
    }

    /**
     * 活动商品
     */
    private void getActionData() {
        OkGo.<AMBaseDto<LookGiftInfo>>get(Constants.lookGiftInfoUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("operation_type", 1)   // 1 发送的礼物 2 接收的礼物
                .params("state", giftStatus)
                .params("pageSize", pageSize)
                .params("pageIndex", pageIndex)
                .execute(new NewsCallback<AMBaseDto<LookGiftInfo>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<LookGiftInfo>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<LookGiftInfo>> response) {
                        if (response.body().code == 0) {
                            LookGiftInfo table = response.body().data;
                            setData(table.getRows());
                            // 判断是否有更多数据
                            if (table.getRecordCount() > 0) {
                                mEmptyLayout.hide();
                                if (mAdapter.getData().size() < table.getRecordCount()) {
                                    pageIndex++;
                                    mAdapter.loadMoreComplete();
                                    mAdapter.setEnableLoadMore(true);
                                } else {
                                    if (table.getRecordCount() < pageSize) {
                                        mAdapter.loadMoreEnd(true);
                                        //showToast("没有更多数据");
                                    } else {
                                        mAdapter.loadMoreEnd(false);
                                        //mAdapter.setEnableLoadMore(false);
                                    }
                                }
                            } else {
                                mEmptyLayout.show(RxEmptyLayout.EMPTY_DATA);
                            }
                        } else {
                            showToast(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<LookGiftInfo>> response) {
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

    private void setData(List<LookGiftInfo.LookGiftInfoModel> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mAdapter.setNewData(data);
        } else {
            mAdapter.addData(data);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECEIVE_GIFT && resultCode == RESULT_OK) {
            refresh();
        } else if (requestCode == EXAMPLES_GIFT && resultCode == RESULT_OK) {
            int redPacketId = data.getIntExtra("redPacketId", 0);
            int type = data.getIntExtra("type", 0);
            String greetings = data.getStringExtra("greetings");
            String description = data.getStringExtra("description");
            String imgShareUrl = data.getStringExtra("imgShareUrl");
            share(redPacketId, greetings, description, imgShareUrl, type);
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);//完成回调
    }

    /**
     * 分享
     */
    private void share(int redPacketId, String greetings, String description, String imgShareUrl, int type) {
        String url = Constants.MAIN_ENGINE_PIC + "/songnaerWechat/#/redenvelopes?id=" + redPacketId + "&type=" + type+ "&user=" + TokenCache.userId(mContext);
        String title = UserCache.user(mContext).getNickname() + "祝您" + greetings;

        final ShareDialog mDialog = new ShareDialog(GiftGivingActivity.this);
        mDialog.setWeb(url, title, imgShareUrl, description);
        mDialog.setListener(new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                showToast("转赠成功");
                mDialog.dismiss();
                refresh();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                showToast("转赠错误");
                mDialog.dismiss();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                showToast("转赠取消");
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }
}
