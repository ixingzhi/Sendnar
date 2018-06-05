package com.shichuang.sendnar.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.open.base.BaseFragment;
import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.open.tool.RxGlideTool;
import com.shichuang.open.tool.RxStatusBarTool;
import com.shichuang.sendnar.R;
import com.shichuang.sendnar.activity.BusinessCooperationActivity;
import com.shichuang.sendnar.activity.ChangeAvatarActivity;
import com.shichuang.sendnar.activity.GiftGivingActivity;
import com.shichuang.sendnar.activity.GiftReceivedActivity;
import com.shichuang.sendnar.activity.HonoraryContributionActivity;
import com.shichuang.sendnar.activity.MyAddressActivity;
import com.shichuang.sendnar.activity.MyIntegralActivity;
import com.shichuang.sendnar.activity.MyItemsActivity;
import com.shichuang.sendnar.activity.MyOrderActivity;
import com.shichuang.sendnar.activity.MyTeamActivity;
import com.shichuang.sendnar.activity.PersonalDataActivity;
import com.shichuang.sendnar.activity.SettingActivity;
import com.shichuang.sendnar.common.Constants;
import com.shichuang.sendnar.common.NewsCallback;
import com.shichuang.sendnar.common.OrderStatus;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.common.UdeskHelper;
import com.shichuang.sendnar.common.UserCache;
import com.shichuang.sendnar.common.Utils;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.Home;
import com.shichuang.sendnar.entify.Token;
import com.shichuang.sendnar.entify.User;
import com.shichuang.sendnar.event.MessageEvent;
import com.shichuang.sendnar.event.UpdateLoginStatus;
import com.shichuang.sendnar.interf.OnTabReselectListener;
import com.shichuang.sendnar.widget.ShareDialog;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class PersonalCenterFragment extends BaseFragment implements OnTabReselectListener {
    private final int REQUEST_CODE_ASK_RUNTIME_PERMISSIONS = 124;
    private ScrollView mScrollView;
    private RelativeLayout mRlTitleBar;
    private ImageView mIvAvatar;
    private TextView mTvNickname;
    private TextView mTvRoleName;

    private int identity;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_personal_center;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mScrollView = view.findViewById(R.id.scroll_view);
        mRlTitleBar = view.findViewById(R.id.rl_title_bar);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRlTitleBar.getLayoutParams();
        params.setMargins(0, RxStatusBarTool.getStatusBarHeight(mContext), 0, 0);

        mIvAvatar = view.findViewById(R.id.iv_avatar);
        mTvNickname = view.findViewById(R.id.tv_nickname);
        mTvRoleName = view.findViewById(R.id.tv_role_name);

        EventBus.getDefault().register(this);
    }

    @Override
    public void initEvent() {
        // 设置
        skipPage(R.id.iv_setting, SettingActivity.class, null);
        // 登录
        skipPage(R.id.iv_avatar, PersonalDataActivity.class, null);
        // 我的订单
        Bundle myOrderBundle = new Bundle();
        myOrderBundle.putInt("orderStatus", OrderStatus.ALL);
        skipPage(R.id.ll_my_order, MyOrderActivity.class, myOrderBundle);
        // 待付款
        Bundle waitPaymentBundle = new Bundle();
        waitPaymentBundle.putInt("orderStatus", OrderStatus.WAIT_PAYMENT);
        skipPage(R.id.ll_wait_payment, MyOrderActivity.class, waitPaymentBundle);
        // 待发货
        Bundle waitDeliveryBundle = new Bundle();
        waitDeliveryBundle.putInt("orderStatus", OrderStatus.WAIT_WAIT_DELIVERY);
        skipPage(R.id.ll_wait_delivery, MyOrderActivity.class, waitDeliveryBundle);
        // 已发货
        Bundle deliveredBundle = new Bundle();
        deliveredBundle.putInt("orderStatus", OrderStatus.DELIVERED);
        skipPage(R.id.ll_delivered, MyOrderActivity.class, deliveredBundle);
        // 我的物品
        skipPage(R.id.ll_my_items, MyItemsActivity.class, null);
        // 送出的礼物
        skipPage(R.id.ll_gift_giving, GiftGivingActivity.class, null);
        // 收到的礼物
        skipPage(R.id.ll_gift_received, GiftReceivedActivity.class, null);
        // 推荐好友
        mContentView.findViewById(R.id.ll_recommend_friends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent("share"));
            }
        });
        // 我的收获地址
        skipPage(R.id.ll_my_address, MyAddressActivity.class, null);
        // 客服
        mContentView.findViewById(R.id.ll_customer_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call();
                if (Utils.isLogin(mContext)) {
                    UdeskHelper.entryChat(mContext);
                }
            }
        });
        // 慈善贡献度
        skipPage(R.id.ll_honorary_contribution, HonoraryContributionActivity.class, null);
        // 我的积分
        skipPage(R.id.ll_my_integral, MyIntegralActivity.class, null);
        // 我的团队
        mContentView.findViewById(R.id.ll_my_team).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isLogin(mContext)) {
                    Bundle teamBundle = new Bundle();
                    teamBundle.putInt("identity", identity);
                    RxActivityTool.skipActivity(mContext, MyTeamActivity.class, teamBundle);
                }
            }
        });
        // 商务合作
        skipPage(R.id.ll_business_cooperation, BusinessCooperationActivity.class, null);
    }

    @Override
    public void initData() {
        updateUserInfo();
    }

    private void skipPage(int resId, final Class<?> cls, final Bundle bundle) {
        mContentView.findViewById(resId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isLogin(mContext)) {
                    if (bundle != null) {
                        RxActivityTool.skipActivity(mContext, cls, bundle);
                    } else {
                        RxActivityTool.skipActivity(mContext, cls);
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdateLoginStatus event) {/* Do something */
        if (event != null) {
            updateUserInfo();
        }
    }

    private void updateUserInfo() {
        if (TokenCache.isUserLogin(mContext)) {
            getUserInfoData();
        } else {
            mIvAvatar.setImageResource(R.drawable.ic_avatar_default);
            mTvNickname.setText("用户名");
            mTvRoleName.setText("");
        }
    }

    private void getUserInfoData() {
        OkGo.<AMBaseDto<User>>get(Constants.selfInfoUrl)
                .tag(mContext)
                .params("token", TokenCache.token(mContext))
                .execute(new NewsCallback<AMBaseDto<User>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<User>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<User>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            User user = response.body().data;
                            UserCache.update(mContext, user);
                            handleData(user);
                        } else {
                            User user = UserCache.user(mContext);
                            if (user != null) {
                                handleData(user);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<User>> response) {
                        super.onError(response);
                        User user = UserCache.user(mContext);
                        if (user != null) {
                            handleData(user);
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private void handleData(User user) {
        UserCache.update(mContext, user);
        RxGlideTool.loadImageView(mContext, Utils.getSingleImageUrlByImageUrls(user.getHeadPortrait()), mIvAvatar, R.drawable.ic_avatar_default);
        mTvNickname.setText(user.getNickname());
        // 0 消费者 1发起人 2 合伙人
        identity = user.getIdentityInfo();
        switch (user.getIdentityInfo()) {
            case 0:
                mTvRoleName.setText("");
                isShowTeam(false);
                //isShowTeam(true);
                break;
            case 1:
                mTvRoleName.setText("【发起人】");
                isShowTeam(true);
                break;
            case 2:
                mTvRoleName.setText("【合伙人】");
                isShowTeam(true);
                break;
            default:
                mTvRoleName.setText("");
                isShowTeam(false);
                //isShowTeam(true);
                break;
        }
    }

    /**
     * 合伙人和发起人可显示 团队
     *
     * @param bool
     */
    private void isShowTeam(boolean bool) {
        mContentView.findViewById(R.id.ll_my_team).setVisibility(bool ? View.VISIBLE : View.GONE);
        mContentView.findViewById(R.id.ll_empty_layout).setVisibility(bool ? View.GONE : View.VISIBLE);
    }

    private void call() {
        final String phoneNum = "0510-1234567";
        if (phoneNum != null && !phoneNum.equals("")) {
            new AlertDialog.Builder(mContext)
                    .setMessage("拨打 " + phoneNum + " ？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},
                                        REQUEST_CODE_ASK_RUNTIME_PERMISSIONS);
                            } else {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum.toString().trim()));
                                startActivity(intent);
                            }
                        }
                    }).create().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_CODE_ASK_RUNTIME_PERMISSIONS == requestCode) {
            // 如果获得了基本拨打电话权限，则允许执行
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                call();
                return;
            }

            String msg = "需要拨打电话权限，否则将无法正常使用送哪儿";
            AlertDialog dialog = new AlertDialog.Builder(mContext).setMessage(msg).setPositiveButton("去设置", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    @Override
    public void onTabReselect() {
        mScrollView.scrollTo(0, 0);
        updateUserInfo();
    }
}
