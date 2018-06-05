package com.shichuang.sendnar.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.base.WebPageActivity;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxBigDecimalTool;
import com.shichuang.open.tool.RxGlideTool;
import com.shichuang.open.tool.RxScreenTool;
import com.shichuang.open.tool.RxStatusBarTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.AuthorizationOpenId;
import com.shichuang.sendnar.entify.MyTeam;
import com.shichuang.sendnar.event.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 我的团队
 * Created by Administrator on 2018/4/23.
 */

public class MyTeamActivity extends BaseActivity {
    private RelativeLayout mRlTitleBar;
    private ImageView mIvAvatar;
    private TextView mTvIdentity;
    // 现在收益
    private TextView mTvNowEarnings;
    // 可提现
    private TextView mTvWithdrawalAmount;
    // 冻结金额
    private TextView mTvFrozenAmount;
    // 累计收益
    private TextView mTvAccumulatedEarnings;
    // 合伙人
    private TextView mTvPartnerAmount;
    // 消费者
    private TextView mTvConsumersAmount;
    // 合伙人会员信息
    private LinearLayout mLlPartnerRankingMemberInfo;
    // 消费者会员信息
    private LinearLayout mLlConsumerRankingMemberInfo;

    private int identity;
    private String withdrawalAmount = "0.00";
    // 累计判断
    private int count;

    @Override
    public int getLayoutId() {
        RxStatusBarTool.setStatusBar(this, true);
        return R.layout.activity_my_team;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        identity = getIntent().getIntExtra("identity", 0);
        mRlTitleBar = (RelativeLayout) findViewById(R.id.rl_title_bar);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRlTitleBar.getLayoutParams();
        params.setMargins(0, RxStatusBarTool.getStatusBarHeight(mContext), 0, 0);

        mIvAvatar = (ImageView) findViewById(R.id.iv_avatar);
        mTvIdentity = (TextView) findViewById(R.id.tv_identity);
        mTvNowEarnings = (TextView) findViewById(R.id.tv_now_earnings);
        mTvWithdrawalAmount = (TextView) findViewById(R.id.tv_withdrawal_amount);
        mTvFrozenAmount = (TextView) findViewById(R.id.tv_frozen_amount);
        mTvAccumulatedEarnings = (TextView) findViewById(R.id.tv_accumulated_earnings);
        mTvPartnerAmount = (TextView) findViewById(R.id.tv_partner_amount);
        mTvConsumersAmount = (TextView) findViewById(R.id.tv_consumers_amount);
        mLlPartnerRankingMemberInfo = (LinearLayout) findViewById(R.id.ll_partner_ranking_member_info);
        mLlConsumerRankingMemberInfo = (LinearLayout) findViewById(R.id.ll_consumer_ranking_member_info);

        // 影藏显示布局（如果是合伙人，需要影藏合伙人相关的布局）    // 0 消费者 1发起人 2 合伙人
        if (identity == 2) {
            findViewById(R.id.ll_partner_earnings).setVisibility(View.GONE);
            findViewById(R.id.view_partner_earnings_div).setVisibility(View.GONE);
            findViewById(R.id.ll_partner_ranking).setVisibility(View.GONE);
            findViewById(R.id.view_partner_ranking_div).setVisibility(View.GONE);
        }

        EventBus.getDefault().register(this);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.ll_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxActivityTool.finish(mContext);
            }
        });
        findViewById(R.id.ll_withdraw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAuthorizationOpenid();
            }
        });
        // 合伙人    1  是合伙人查所有消费者给我的收益 2  是发起人查所有合伙人给我的收益  3   是发起人查所有消费者给我的收益
        findViewById(R.id.ll_partner_ranking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (identity == 1) {  // 发起人
                    Bundle bundle = new Bundle();
                    bundle.putInt("actionType", 2);
                    RxActivityTool.skipActivity(mContext, CharityContributionActivity.class, bundle);
                }
            }
        });
        // 消费者
        findViewById(R.id.ll_consumer_ranking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (identity == 1) {  // 发起人
                    Bundle bundle = new Bundle();
                    bundle.putInt("actionType", 3);
                    RxActivityTool.skipActivity(mContext, CharityContributionActivity.class, bundle);
                } else if (identity == 2) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("actionType", 1);
                    RxActivityTool.skipActivity(mContext, CharityContributionActivity.class, bundle);
                }
            }
        });
    }

    @Override
    public void initData() {
        getData();
    }

    private void getData() {
        OkGo.<AMBaseDto<MyTeam>>get(Constants.myTeamUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .execute(new NewsCallback<AMBaseDto<MyTeam>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<MyTeam>, ? extends Request> request) {
                        super.onStart(request);
                        // 首次进入该页面时使用（其他页面会发通知更新次页面的）
                        if (count == 0) {
                            count++;
                            showLoading();
                        }
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<MyTeam>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            handleData(response.body().data);
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<MyTeam>> response) {
                        super.onError(response);
                        showToast(response.getException().getMessage());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissLoading();
                    }
                });

    }

    private void handleData(MyTeam data) {
        RxGlideTool.loadImageView(mContext, Utils.getSingleImageUrlByImageUrls(data.getUserPic()), mIvAvatar, R.drawable.ic_avatar_default);
        mTvIdentity.setText(data.getUseridentity());
        withdrawalAmount = data.getPutForward();
        mTvNowEarnings.setText("¥" + RxBigDecimalTool.toDecimal(withdrawalAmount, 2));
        mTvWithdrawalAmount.setText("¥" + RxBigDecimalTool.toDecimal(withdrawalAmount, 2));
        mTvFrozenAmount.setText("¥" + RxBigDecimalTool.toDecimal(data.getIce(), 2));
        if (!TextUtils.isEmpty(data.getPartnerProfit())) {
            mTvPartnerAmount.setText("¥" + RxBigDecimalTool.toDecimal(data.getPartnerProfit(), 2));
        }
        mTvConsumersAmount.setText("¥" + RxBigDecimalTool.toDecimal(data.getConsumerProfit(), 2));
        // 累计收益（合伙人加消费者，合伙人有可能为空）
        double accumulatedEarnings = 0.00;
        if (!TextUtils.isEmpty(data.getPartnerProfit())) {
            accumulatedEarnings = RxBigDecimalTool.add(Double.valueOf(data.getPartnerProfit()), Double.valueOf(data.getConsumerProfit()));
        } else {
            accumulatedEarnings = Double.valueOf(data.getConsumerProfit());
        }
        mTvAccumulatedEarnings.setText("¥" + RxBigDecimalTool.toDecimal(accumulatedEarnings, 2));

        // 合伙人头像
        List<MyTeam.MemberInfo> partnerRankingList = data.getPartnerRankingList();
        mLlPartnerRankingMemberInfo.removeAllViews();
        if (partnerRankingList != null && partnerRankingList.size() > 0) {
            for (int i = 0; i < partnerRankingList.size(); i++) {
                if (i < 3) { // 最多添加3个
                    CircleImageView mIvAvatar = new CircleImageView(mContext);
                    int width = RxScreenTool.dip2px(mContext, 32);
                    mIvAvatar.setLayoutParams(new LinearLayout.LayoutParams(width, width));
                    if (i > 0) { // 大于一个时，设置第二个左边的margin值
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mIvAvatar.getLayoutParams();
                        params.setMargins(12, 0, 0, 0);
                    }
                    RxGlideTool.loadImageView(mContext, Utils.getSingleImageUrlByImageUrls(partnerRankingList.get(i).getHeadPortrait()),
                            mIvAvatar, R.drawable.ic_avatar_default);
                    mLlPartnerRankingMemberInfo.addView(mIvAvatar);
                }
            }
        } else {
            TextView mTvEmptyData = new TextView(mContext);
            mTvEmptyData.setTextColor(getResources().getColor(R.color.textColor2));
            mTvEmptyData.setText("暂无合伙人");
            mLlPartnerRankingMemberInfo.addView(mTvEmptyData);
        }
        //消费者头像
        List<MyTeam.MemberInfo> consumerRankingList = data.getConsumerRankingList();
        mLlConsumerRankingMemberInfo.removeAllViews();
        if (consumerRankingList != null && consumerRankingList.size() > 0) {
            for (int i = 0; i < consumerRankingList.size(); i++) {
                if (i < 3) { // 最多添加3个
                    CircleImageView mIvAvatar = new CircleImageView(mContext);
                    int width = RxScreenTool.dip2px(mContext, 32);
                    mIvAvatar.setLayoutParams(new LinearLayout.LayoutParams(width, width));
                    if (i > 0) { // 大于一个时，设置第二个左边的margin值
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mIvAvatar.getLayoutParams();
                        params.setMargins(12, 0, 0, 0);
                    }
                    RxGlideTool.loadImageView(mContext, Utils.getSingleImageUrlByImageUrls(consumerRankingList.get(i).getHeadPortrait()),
                            mIvAvatar, R.drawable.ic_avatar_default);
                    mLlConsumerRankingMemberInfo.addView(mIvAvatar);
                }
            }
        } else {
            TextView mTvEmptyData = new TextView(mContext);
            mTvEmptyData.setTextColor(getResources().getColor(R.color.textColor2));
            mTvEmptyData.setText("暂无消费者");
            mLlConsumerRankingMemberInfo.addView(mTvEmptyData);
        }
    }

    /**
     * 判断是否绑定过微信，没有绑定微信，跳转H5引导用户，有，直接App提现
     */
    private void checkAuthorizationOpenid() {
        OkGo.<AMBaseDto<AuthorizationOpenId>>post(Constants.checkAuthorizationOpenidUrl)
                .tag(mContext)
                .params("oauth_name", "weixinh5")
                .params("token", TokenCache.token(mContext))
                .execute(new NewsCallback<AMBaseDto<AuthorizationOpenId>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<AuthorizationOpenId>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<AuthorizationOpenId>> response) {
                        dismissLoading();
                        if (response.body().code == 0 && response.body().data != null) {
                            AuthorizationOpenId authorizationOpenId = response.body().data;
                            if (authorizationOpenId.getExist() == 1) { // App提现
                                Bundle bundle = new Bundle();
                                bundle.putString("withdrawalAmount", withdrawalAmount);
                                RxActivityTool.skipActivity(mContext, IncomeAmountActivity.class, bundle);
                            } else {
                                WebPageActivity.newInstance(mContext, "微信授权流程", Constants.MAIN_ENGINE_PIC + authorizationOpenId.getH5Url());
                            }
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<AuthorizationOpenId>> response) {
                        super.onError(response);
                        dismissLoading();
                        showToast(response.getException().getMessage());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event != null) {
            if (event.message.equals("withdrawalSuccess") || event.message.equals("setPartnerSuccess")) {
                getData();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(mContext);
    }
}
