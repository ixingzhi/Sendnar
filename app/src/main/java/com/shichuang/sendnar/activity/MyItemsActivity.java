package com.shichuang.sendnar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxToastTool;
import com.shichuang.open.widget.CustomLinearLayoutManager;
import com.shichuang.open.widget.RxEmptyLayout;
import com.shichuang.sendnar.MainActivity;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.Setting;
import com.shichuang.sendnar.adapter.MyItemsAdapter;
import com.shichuang.sendnar.adapter.OrderInfoAdapter;
import com.shichuang.sendnar.adapter.RecommendGiftsAdapter;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.common.UserCache;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.FestivalList;
import com.shichuang.sendnar.entify.GoodsList;
import com.shichuang.sendnar.entify.MyItems;
import com.shichuang.sendnar.entify.WxMakeOrder;
import com.shichuang.sendnar.widget.GiftsDetailsRemindDialog;
import com.shichuang.sendnar.widget.MyItemsRemindDialog;
import com.shichuang.sendnar.widget.RxTitleBar;
import com.shichuang.sendnar.widget.ShareDialog;
import com.shichuang.sendnar.widget.payment.wechat.pay.WechatPayTools;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * 我的物品
 * Created by Administrator on 2018/4/23.
 */

public class MyItemsActivity extends BaseActivity implements View.OnClickListener {
    private static final int GET_RED_PACKET_ID = 0x11;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout mLlEmptyData;
    private LinearLayout mLlHasData;
    private RecyclerView mRecyclerView;
    private MyItemsAdapter mAdapter;
    private RecyclerView mRecommendRecyclerView;
    private RecommendGiftsAdapter mRecommendAdapter;
    private ImageView mIvCheckAll;
    // 马上选购
    private Button mBtnChooseBuys;

    private int pageSize = 100;
    private int pageIndex = 1;
    // 推荐更多分页
    private int rPageSize = 10;
    private int rPageIndex = 1;
    // 多个红包Id
    private StringBuffer sbSelectIds;
    // 多个商品名称
    private StringBuffer sbGoodsTitle;
    // 多个商品名称（只选第一个图片的链接）
    private String imgShareUrl = "";
    // 凑红包Id
    private int gatherTogetherRedPacketId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_items;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mLlEmptyData = view.findViewById(R.id.ll_empty_data);
        mLlHasData = view.findViewById(R.id.ll_has_data);
        initRecommendRecyclerView();
        initRecyclerView();
        mIvCheckAll = (ImageView) findViewById(R.id.iv_check_all);
        isShowRemind();
    }

    private void isShowRemind() {
        if (Setting.isShowMyItemsRemind(mContext)) {
            MyItemsRemindDialog mDialog = new MyItemsRemindDialog(mContext);
            mDialog.show();
        }
    }

    private void initRecyclerView() {
        gatherTogetherRedPacketId = getIntent().getIntExtra("gatherTogetherRedPacketId", 0);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyItemsAdapter();
        mAdapter.setPreLoadNumber(2);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initRecommendRecyclerView() {
        mRecommendRecyclerView = mContentView.findViewById(R.id.recycler_view_recommend);
        mRecommendRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecommendAdapter = new RecommendGiftsAdapter();
        mRecommendAdapter.setPreLoadNumber(2);
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_my_items_buy, (ViewGroup) findViewById(android.R.id.content), false);
        mBtnChooseBuys = view.findViewById(R.id.btn_choose_buys);
        mRecommendAdapter.addHeaderView(view);
        mRecommendRecyclerView.setAdapter(mRecommendAdapter);
    }

    @Override
    public void initEvent() {
        mBtnChooseBuys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("position", 1);
                RxActivityTool.skipActivity(mContext, MainActivity.class, bundle);
            }
        });
        findViewById(R.id.ll_check_all).setOnClickListener(this);
        findViewById(R.id.btn_red_envelope).setOnClickListener(this);
        findViewById(R.id.btn_share_wechat_friends).setOnClickListener(this);
        ((RxTitleBar) findViewById(R.id.title_bar)).setTitleBarClickListener(new RxTitleBar.TitleBarClickListener() {
            @Override
            public void onRightClick() {
                Bundle bundle = new Bundle();
                bundle.putInt("position", 1);
                RxActivityTool.skipActivity(mContext, MainActivity.class, bundle);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MyItems.MyItemsModel items = mAdapter.getItem(position);
                items.setSelect(items.isSelect() ? false : true);
                mAdapter.notifyDataSetChanged();
                handleData();
            }
        });
    }

    @Override
    public void initData() {
        refresh();
        getRecommendGiftData();
    }

    private void refresh() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                getData();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 全选
            case R.id.ll_check_all:
                if (mAdapter.getData().size() > 0) {
                    boolean isCheckAll = !mIvCheckAll.isSelected();
                    List<MyItems.MyItemsModel> mList = mAdapter.getData();
                    for (MyItems.MyItemsModel model : mList) {
                        model.setSelect(isCheckAll);
                    }
                    mAdapter.notifyDataSetChanged();
                    handleData();
                } else {
                    showToast("请添加商品");
                }
                break;
            // 1=微信好友送礼 2=发红包
            case R.id.btn_red_envelope:
                send(2);
                break;
            case R.id.btn_share_wechat_friends:
                send(1);
                break;
            default:
                break;
        }
    }

    /**
     * 送礼
     */
    private void send(int type) {
        if (null == sbSelectIds || null == sbGoodsTitle) {
            sbSelectIds = new StringBuffer();
            sbGoodsTitle = new StringBuffer();
        } else {
            sbSelectIds.setLength(0);
            sbGoodsTitle.setLength(0);
        }
        List<MyItems.MyItemsModel> mList = mAdapter.getData();
        for (MyItems.MyItemsModel model : mList) {
            if (model.isSelect()) {
                sbSelectIds.append(model.getId() + ",");
                sbGoodsTitle.append(model.getShopName() + ",");
                if ("".equals(imgShareUrl)) {
                    imgShareUrl = Utils.getSingleImageUrlByImageUrls(model.getPic());
                }
            }
        }
        if (sbSelectIds.length() > 0) {
            // 判断是否存在凑红包
            Bundle bundle = new Bundle();
//            if (gatherTogetherRedPacketId == 0) {
//                // 去除最后一个逗号
//                bundle.putString("ids", sbSelectIds.deleteCharAt(sbSelectIds.length() - 1).toString());
//            } else {
//                // 不需要去除最后一个逗号，直接累加凑红包id
//                bundle.putString("ids", sbSelectIds.append(String.valueOf(gatherTogetherRedPacketId)).toString());
//            }
            // 去除最后一个逗号
            bundle.putString("ids", sbSelectIds.deleteCharAt(sbSelectIds.length() - 1).toString());
            bundle.putString("description", sbGoodsTitle.toString());
            bundle.putString("imgShareUrl", imgShareUrl);
            bundle.putInt("type", type);
            RxActivityTool.skipActivityForResult(MyItemsActivity.this, GreetingsActivity.class, bundle, GET_RED_PACKET_ID);
        } else {
            showToast("请选择礼物");
        }
    }

    private void getData() {
        OkGo.<AMBaseDto<MyItems>>get(Constants.getGiftListUrl)
                //.cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)  //缓存模式先使用缓存,然后使用网络数据
                .tag(context)
                .params("token", TokenCache.token(mContext))
                .params("pageSize", pageSize)
                .params("pageIndex", pageIndex)
                .execute(new NewsCallback<AMBaseDto<MyItems>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<MyItems>, ? extends Request> request) {
                        super.onStart(request);
                        //mEmptyLayout.show(RxEmptyLayout.NETWORK_LOADING);
                    }

                    @Override
                    public void onSuccess(Response<AMBaseDto<MyItems>> response) {
                        if (response.body().code == 0) {
                            MyItems table = response.body().data;
                            setData(table.getRows());
                            // 判断是否有更多数据
                            if (table.getRecordCount() > 0) {
                                //mEmptyLayout.hide();
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
                                //mEmptyLayout.show(RxEmptyLayout.EMPTY_DATA);
                            }
                        } else {
                            showToast(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<MyItems>> response) {
                        super.onError(response);
                        //mEmptyLayout.show(RxEmptyLayout.NETWORK_ERROR);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void setData(List<MyItems.MyItemsModel> data) {
        // 存在凑红包Id，设置为选中
        if (gatherTogetherRedPacketId != 0) {
            for (MyItems.MyItemsModel model : data) {
                if (model.getId() == gatherTogetherRedPacketId) {
                    model.setSelect(true);
                }
            }
        }
        if (mSwipeRefreshLayout.isRefreshing()) {
            mAdapter.setNewData(data);
        } else {
            mAdapter.addData(data);
        }
        if (data.size() > 0) {
            mLlEmptyData.setVisibility(View.GONE);
            mLlHasData.setVisibility(View.VISIBLE);
        } else {
            mLlEmptyData.setVisibility(View.VISIBLE);
            mLlHasData.setVisibility(View.GONE);
        }
        handleData();
    }

    private void handleData() {
        int selectCount = 0;
        List<MyItems.MyItemsModel> mList = mAdapter.getData();
        for (MyItems.MyItemsModel model : mList) {
            if (model.isSelect()) {
                selectCount++;
            }
        }
        mIvCheckAll.setSelected(selectCount == mList.size() ? true : false);
    }

    /**
     * 获得推荐商品
     */
    private void getRecommendGiftData() {
        OkGo.<AMBaseDto<GoodsList>>get(Constants.recommendGoodsUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("pageSize", rPageSize)
                .params("pageIndex", rPageIndex)
                .execute(new NewsCallback<AMBaseDto<GoodsList>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<GoodsList>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<GoodsList>> response) {
                        if (response.body().code == 0) {
                            GoodsList table = response.body().data;
                            setRecommendData(table);
                            // 判断是否有更多数据
                            if (table.getRecordCount() > 0) {
                                //mEmptyLayout.hide();
                                if (mAdapter.getData().size() < table.getRecordCount()) {
                                    pageIndex++;
                                    mAdapter.loadMoreComplete();
                                    mAdapter.setEnableLoadMore(true);
                                } else {
                                    if (table.getRecordCount() < pageSize) {
                                        mAdapter.loadMoreEnd(true);
                                    } else {
                                        mAdapter.loadMoreEnd(false);
                                    }
                                }
                            } else {
                                //mEmptyLayout.show(RxEmptyLayout.EMPTY_DATA);
                            }
                        } else {
                            showToast(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<GoodsList>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    /**
     * 绑定推荐数据
     *
     * @param data
     */
    private void setRecommendData(GoodsList data) {
        mRecommendAdapter.addData(data.getRows());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_RED_PACKET_ID && resultCode == RESULT_OK) {
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
        String url = Constants.MAIN_ENGINE_PIC + "/songnaerWechat/#/redenvelopes?id=" + redPacketId + "&type=" + type + "&user=" + TokenCache.userId(mContext);
        String title = UserCache.user(mContext).getNickname() + "祝您" + greetings;

        final ShareDialog mDialog = new ShareDialog(MyItemsActivity.this);
        mDialog.setWeb(url, title, imgShareUrl, description);
        mDialog.setListener(new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                showToast("发送成功");
                mDialog.dismiss();
                refresh();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                showToast("发送错误");
                mDialog.dismiss();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                showToast("发送取消");
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

}
