package com.shichuang.sendnar.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseFragment;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxBigDecimalTool;
import com.shichuang.open.tool.RxStatusBarTool;
import com.shichuang.sendnar.MainActivity;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.activity.ConfirmOrderActivity;
import com.shichuang.sendnar.adapter.RecommendGiftsAdapter;
import com.shichuang.sendnar.adapter.ShoppingCartAdapter;
import com.shichuang.sendnar.common.BuyType;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.GoodsList;
import com.shichuang.sendnar.entify.ShoppingCart;
import com.shichuang.sendnar.event.UpdateShoppingCart;
import com.shichuang.sendnar.interf.OnTabReselectListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class ShoppingCartFragment extends BaseFragment implements OnTabReselectListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout mLlEmptyData;
    private LinearLayout mLlHasData;
    private RecyclerView mRecommendRecyclerView;
    private RecommendGiftsAdapter mRecommendAdapter;
    private RecyclerView mRecyclerView;
    private ShoppingCartAdapter mAdapter;
    private ImageView mIvSelectAll;
    private TextView mTvTotalPrice;
    // 马上选购
    private Button mBtnChooseBuys;

    // 礼品推荐
    private int pageSize = 10;
    private int pageIndex = 1;

    private StringBuffer sbCardId;

    public static ShoppingCartFragment newInstance() {
        return new ShoppingCartFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_shopping_cart;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        View mViewStatusBar = view.findViewById(R.id.view_status_bar);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mViewStatusBar.getLayoutParams();
        params.height = RxStatusBarTool.getStatusBarHeight(mContext);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mLlEmptyData = view.findViewById(R.id.ll_empty_data);
        mLlHasData = view.findViewById(R.id.ll_has_data);
        initRecommendRecyclerView();
        initRecyclerView();
        mIvSelectAll = view.findViewById(R.id.iv_select_all);
        mTvTotalPrice = view.findViewById(R.id.tv_total_price);

        EventBus.getDefault().register(this);
    }

    private void initRecommendRecyclerView() {
        mRecommendRecyclerView = mContentView.findViewById(R.id.recycler_view_recommend);
        mRecommendRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecommendAdapter = new RecommendGiftsAdapter();
        mRecommendAdapter.setPreLoadNumber(2);
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_shopping_cart_buy, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
        mBtnChooseBuys = view.findViewById(R.id.btn_choose_buys);
        mRecommendAdapter.addHeaderView(view);
        mRecommendRecyclerView.setAdapter(mRecommendAdapter);
    }

    private void initRecyclerView() {
        mRecyclerView = mContentView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new ShoppingCartAdapter();
        mAdapter.setPreLoadNumber(2);
        mAdapter.addHeaderView(LayoutInflater.from(mContext).inflate(R.layout.layout_shopping_cart_buy_tips, (ViewGroup) getActivity().findViewById(android.R.id.content), false));
        mRecyclerView.setAdapter(mAdapter);
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
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ShoppingCart shoppingCart = mAdapter.getData().get(position);
                if (view.getId() == R.id.tv_subtract) {
                    updateShoppingCartCount(shoppingCart.getGoodsCartId(), shoppingCart.getGoodsCartCounts(), 0, position);
                } else if (view.getId() == R.id.tv_plus) {
                    updateShoppingCartCount(shoppingCart.getGoodsCartId(), shoppingCart.getGoodsCartCounts(), 1, position);
                } else if (view.getId() == R.id.right) {
                    deleteShoppingCartGift(shoppingCart.getGoodsCartId(), position);
                } else if (view.getId() == R.id.iv_select) {
                    shoppingCart.setSelect(!shoppingCart.isSelect());
                    mAdapter.notifyDataSetChanged();
                    handleData();
                }
            }
        });
        mRecommendAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.iv_add_shopping_cart) {
                    addShoppingCart(mRecommendAdapter.getData().get(position).getId());
                }
            }
        });
        // 全选
        mContentView.findViewById(R.id.ll_select_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ShoppingCart> mList = mAdapter.getData();
                boolean isSelect = !mIvSelectAll.isSelected();
                mIvSelectAll.setSelected(isSelect);
                for (ShoppingCart model : mList) {
                    model.setSelect(isSelect);
                }
                mAdapter.notifyDataSetChanged();
                handleData();
            }
        });
        // 微信送礼
        mContentView.findViewById(R.id.btn_wechat_gift_giving).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmOrder(BuyType.SHOPPING_CART_WECHAT_GIFT_GIVING);
            }
        });
        // 送给自己
        mContentView.findViewById(R.id.btn_direct_purchasing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmOrder(BuyType.SHOPPING_CART_BUY_NOW);
            }
        });
    }

    @Override
    public void initData() {
        refresh();
        getRecommendGiftData();
    }

    /**
     * 提交订单页面
     */
    private void confirmOrder(int from) {
        if (sbCardId == null) {
            sbCardId = new StringBuffer();
        }
        sbCardId.setLength(0);
        // 拼接购物车Id
        List<ShoppingCart> mList = mAdapter.getData();
        for (ShoppingCart model : mList) {
            if (model.isSelect()) {
                sbCardId.append(model.getGoodsCartId() + ",");
            }
        }
        if (sbCardId.length() > 0) {
            Bundle bundle = new Bundle();
            bundle.putString("cardIds", sbCardId.deleteCharAt(sbCardId.length() - 1).toString());
            bundle.putInt("buyType", from);
            RxActivityTool.skipActivity(mContext, ConfirmOrderActivity.class, bundle);
        } else {
            showToast("请选择商品");
        }
    }

    private void refresh() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                getShoppingCartData();
            }
        });
    }

    private void getShoppingCartData() {
        OkGo.<AMBaseDto<List<ShoppingCart>>>get(Constants.lookShoppingCartUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .execute(new NewsCallback<AMBaseDto<List<ShoppingCart>>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<List<ShoppingCart>>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<List<ShoppingCart>>> response) {
                        if (response.body().code == 0) {
                            mAdapter.replaceData(response.body().data);
                            handleData();
                        } else {
                            showToast(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<List<ShoppingCart>>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    /**
     * 更新购物车数量
     *
     * @param cartId   id
     * @param count    当前数量
     * @param flag     0 减商品数量，1 加商品
     * @param position adapter位置
     */
    private void updateShoppingCartCount(final int cartId, int count, int flag, final int position) {
        // 减商品数量
        if (flag == 0) {
            count--;
            if (count < 1) {
                showToast("不能再减啦~");
                return;
            }
        } else {
            count++;
        }

        final int finalCount = count;
        OkGo.<AMBaseDto<Empty>>get(Constants.updateShoppingCartGiftCountUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("goods_cart_id", cartId)
                .params("goods_cart_counts", count)
                .execute(new NewsCallback<AMBaseDto<Empty>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Empty>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<Empty>> response) {
                        if (response.body().code == 0) {
                            mAdapter.getData().get(position).setGoodsCartCounts(finalCount);
                            mAdapter.notifyDataSetChanged();
                            handleData();
                        } else {
                            showToast(response.body().msg);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Empty>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissLoading();
                    }
                });
    }

    /**
     * 删除购物车礼品
     */

    private void deleteShoppingCartGift(int cartId, final int position) {
        OkGo.<AMBaseDto<Empty>>get(Constants.deleteShoppingCartGiftUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("goods_cart_id", cartId)
                .execute(new NewsCallback<AMBaseDto<Empty>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Empty>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<Empty>> response) {
                        showToast(response.body().msg);
                        if (response.body().code == 0) {
                            if (position < mAdapter.getData().size()) {
                                mAdapter.getData().remove(position);
                                mAdapter.notifyDataSetChanged();
                                handleData();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Empty>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissLoading();
                    }
                });
    }

    /**
     * 处理数数据，计算价格
     */
    private void handleData() {
        // 总价
        double totalPrice = 0.00;
        // 已选择数量
        int selectedCount = 0;
        List<ShoppingCart> mList = mAdapter.getData();
        if (mList.size() > 0) {
            mLlEmptyData.setVisibility(View.GONE);
            mLlHasData.setVisibility(View.VISIBLE);
        } else {
            mLlEmptyData.setVisibility(View.VISIBLE);
            mLlHasData.setVisibility(View.GONE);
        }
        for (ShoppingCart model : mList) {
            if (model.isSelect()) {
                selectedCount++;
                totalPrice = RxBigDecimalTool.add(totalPrice, Double.valueOf(model.getSalePrice()) * model.getGoodsCartCounts());
            }
        }
        // 是否全部选择
        mIvSelectAll.setSelected(selectedCount == mList.size() ? true : false);
        mTvTotalPrice.setText("¥" + RxBigDecimalTool.toDecimal(totalPrice, 2));
    }

    /**
     * 获得推荐商品
     */
    private void getRecommendGiftData() {
        OkGo.<AMBaseDto<GoodsList>>get(Constants.recommendGoodsUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("pageSize", pageSize)
                .params("pageIndex", pageIndex)
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

    /**
     * 添加到购物车
     *
     * @param id
     */
    private void addShoppingCart(int id) {
        OkGo.<AMBaseDto<Empty>>get(Constants.addShoppingCartUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("goods_id", id)
                .params("goods_cart_counts", 1)
                .params("action_item_id", -1)
                .execute(new NewsCallback<AMBaseDto<Empty>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<Empty>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<Empty>> response) {
                        showToast(response.body().msg);
                        if (response.body().code == 0) {
                            refresh();
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<Empty>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissLoading();
                    }
                });
    }

    @Override
    public void onTabReselect() {
        refresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdateShoppingCart event) {/* Do something */
        if (event != null) {
            refresh();
        }
    }
}
