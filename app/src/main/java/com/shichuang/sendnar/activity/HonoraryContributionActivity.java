package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxBigDecimalTool;
import com.shichuang.open.tool.RxGlideTool;
import com.shichuang.open.tool.RxStatusBarTool;
import com.shichuang.open.tool.RxToastTool;
import com.shichuang.open.widget.CustomGridLayoutManager;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.adapter.BadgeHonorAdapter;
import com.shichuang.sendnar.adapter.MyItemsAdapter;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.SinglePage;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Empty;
import com.shichuang.sendnar.entify.HonoraryContribution;
import com.shichuang.sendnar.event.UpdateOrderEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * 荣誉贡献
 * Created by Administrator on 2018/4/23.
 */

public class HonoraryContributionActivity extends BaseActivity {
    private RelativeLayout mRlTitleBar;
    private ImageView mIvAvatar;
    private TextView mTvHonor;
    private TextView mTvHonorLevel;
    private RecyclerView mRecyclerView;
    private BadgeHonorAdapter mAdapter;

    @Override
    public int getLayoutId() {
        RxStatusBarTool.setStatusBar(this, true);
        return R.layout.activity_honorary_contribution;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mRlTitleBar = (RelativeLayout) findViewById(R.id.rl_title_bar);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRlTitleBar.getLayoutParams();
        params.setMargins(0, RxStatusBarTool.getStatusBarHeight(mContext), 0, 0);

        mIvAvatar = (ImageView) findViewById(R.id.iv_avatar);
        mTvHonor = (TextView) findViewById(R.id.tv_honor);
        mTvHonorLevel = (TextView) findViewById(R.id.tv_honor_level);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        CustomGridLayoutManager mLayoutManager = new CustomGridLayoutManager(mContext, 3);
        mLayoutManager.setScrollEnabled(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new BadgeHonorAdapter();
        mAdapter.setPreLoadNumber(2);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.ll_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxActivityTool.finish(mContext);
            }
        });
        findViewById(R.id.ll_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //RxActivityTool.skipActivity(mContext, HonorGuideActivity.class);
                SinglePage.getInstance().toPage(mContext, "勋章指南", SinglePage.THE_MEDAL_OF_GUIDE, "");
            }
        });
    }

    @Override
    public void initData() {
        getData();
    }

    private void getData() {
        OkGo.<AMBaseDto<HonoraryContribution>>get(Constants.getHonoraryContributionUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .execute(new NewsCallback<AMBaseDto<HonoraryContribution>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<HonoraryContribution>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(Response<AMBaseDto<HonoraryContribution>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            handleData(response.body().data);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<HonoraryContribution>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissLoading();
                    }
                });
    }

    private void handleData(HonoraryContribution data) {
        HonoraryContribution.UserInfo userInfo = data.getCharitable();
        if (userInfo != null) {
            RxGlideTool.loadImageView(mContext, Utils.getSingleImageUrlByImageUrls(userInfo.getHeadPortrait()), mIvAvatar, R.drawable.ic_avatar_default);
            mTvHonor.setText(RxBigDecimalTool.toDecimal(userInfo.getCharitable(), 0));
            mTvHonorLevel.setText(userInfo.getLevelName());
        }
        if (data.getPicList() != null) {
            mAdapter.addData(data.getPicList());
        }

    }
}
