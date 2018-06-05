package com.shichuang.sendnar.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.widget.RxEmptyLayout;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.adapter.MyAddressAdapter;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.OrderOperation;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Address;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.widget.ConfirmDialog;

import java.io.Serializable;
import java.util.List;

/**
 * 我的地址
 * Created by Administrator on 2018/4/19.
 */

public class MyAddressActivity extends BaseActivity {
    private static final int UPDATE_ADDRESS = 0x11;
    // 修改地址
    public static final int EDIT_ADDRESS = 0x12;
    // 新增地址
    public static final int ADD_ADDRESS = 0x13;
    // 地址管理进入
    public static final int ADDRESS_MANAGER = 0x14;
    // 确认订单进入
    public static final int CONFIRM_ORDER = 0x15;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout mLlHasAddress;
    private LinearLayout mLlEmptyAddress;
    private RecyclerView mRecyclerView;
    private MyAddressAdapter mAdapter;
    private RxEmptyLayout mEmptyLayout;

    // 来自哪个页面
    private int from;


    @Override
    public int getLayoutId() {
        return R.layout.activity_my_address;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        from = getIntent().getIntExtra("from", 0);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mLlHasAddress = view.findViewById(R.id.ll_has_address);
        mLlEmptyAddress = view.findViewById(R.id.ll_empty_address);
        initRecyclerView();
        mEmptyLayout = (RxEmptyLayout) findViewById(R.id.empty_layout);
    }

    private void initRecyclerView() {
        mRecyclerView = mContentView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new MyAddressAdapter();
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
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
                if (from == CONFIRM_ORDER) {
                    ConfirmDialog mDialog = new ConfirmDialog(mContext);
                    mDialog.setMessage("是否发货到该地址？");
                    mDialog.setNegativeButton("取消",null);
                    mDialog.setPositiveButton("确定", new ConfirmDialog.DialogInterface() {
                        @Override
                        public void OnClickListener() {
                            Address address = mAdapter.getData().get(position);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("address", address);
                            RxActivityTool.finish(mContext, bundle, RESULT_OK);
                        }
                    });
                    mDialog.show();
                }
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                if (view.getId() == R.id.ll_edit) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("from", EDIT_ADDRESS);
                    bundle.putSerializable("address", (Serializable) adapter.getData().get(position));
                    RxActivityTool.skipActivityForResult(MyAddressActivity.this, AddAddressActivity.class, bundle, UPDATE_ADDRESS);
                } else if (view.getId() == R.id.ll_delete) {
                    if (mAdapter.getData().get(position).getIsDefault() == 2) {
                        showToast("默认地址不可删除");
                    } else {
                        ConfirmDialog mDialog = new ConfirmDialog(mContext);
                        mDialog.setMessage("是否删除此地址？");
                        mDialog.setNegativeButton("取消",null);
                        mDialog.setPositiveButton("确定", new ConfirmDialog.DialogInterface() {
                            @Override
                            public void OnClickListener() {
                                deleteAddress(mAdapter.getData().get(position).getId(), position);
                            }
                        });
                        mDialog.show();
                    }
                } else if (view.getId() == R.id.ll_set_default_address) {
                    if (mAdapter.getData().get(position).getIsDefault() == 2) {
                        showToast("已是默认地址");
                    } else {
                        ConfirmDialog mDialog = new ConfirmDialog(mContext);
                        mDialog.setMessage("设置为默认地址？");
                        mDialog.setNegativeButton("取消",null);
                        mDialog.setPositiveButton("确定", new ConfirmDialog.DialogInterface() {
                            @Override
                            public void OnClickListener() {
                                setDefaultAddress(mAdapter.getData().get(position).getId());
                            }
                        });
                        mDialog.show();
                    }
                }
            }
        });
        findViewById(R.id.btn_add_new_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("from", ADD_ADDRESS);
                RxActivityTool.skipActivityForResult(MyAddressActivity.this, AddAddressActivity.class, bundle, UPDATE_ADDRESS);
            }
        });
        findViewById(R.id.btn_add_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("from", ADD_ADDRESS);
                RxActivityTool.skipActivityForResult(MyAddressActivity.this, AddAddressActivity.class, bundle, UPDATE_ADDRESS);
            }
        });
        mEmptyLayout.setOnEmptyLayoutClickListener(new RxEmptyLayout.OnEmptyLayoutClickListener() {
            @Override
            public void onEmptyLayoutClick(int status) {
                refresh();
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
                getAddressData();
            }
        });
    }

    private void getAddressData() {
        OkGo.<AMBaseDto<List<Address>>>get(Constants.getAddressUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .execute(new NewsCallback<AMBaseDto<List<Address>>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<List<Address>>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<List<Address>>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            mEmptyLayout.hide();
                            if (response.body().data.size() > 0) {
                                mAdapter.replaceData(response.body().data);
                                mLlEmptyAddress.setVisibility(View.GONE);
                                mLlHasAddress.setVisibility(View.VISIBLE);
                            } else {
                                mLlEmptyAddress.setVisibility(View.VISIBLE);
                                mLlHasAddress.setVisibility(View.GONE);
                            }
                        } else {
                            mEmptyLayout.show(RxEmptyLayout.EMPTY_DATA);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<List<Address>>> response) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_ADDRESS && resultCode == RESULT_OK) {
            refresh();
        }
    }

    /**
     * 删除地址
     */
    private void deleteAddress(int id, final int position) {
        OkGo.<AMBaseDto<Empty>>get(Constants.deleteAddressUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("id", id)
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
                            mAdapter.remove(position);
                            if (mAdapter.getData().size() == 0) {
                                mLlEmptyAddress.setVisibility(View.VISIBLE);
                                mLlHasAddress.setVisibility(View.GONE);
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
     * 设置默认地址
     */
    private void setDefaultAddress(int id) {
        OkGo.<AMBaseDto<Empty>>get(Constants.setDefaultAddressUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .params("id", id)
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
}
