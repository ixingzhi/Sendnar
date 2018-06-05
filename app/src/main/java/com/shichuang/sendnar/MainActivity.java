package com.shichuang.sendnar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.shichuang.open.base.BaseActivity;
import com.shichuang.open.tool.RxStatusBarTool;
import com.shichuang.sendnar.event.MessageEvent;
import com.shichuang.sendnar.fragment.NavFragment;
import com.shichuang.sendnar.interf.OnTabReselectListener;
import com.shichuang.sendnar.widget.NavigationButton;
import com.shichuang.sendnar.widget.ShareDialog;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.udesk.activity.UdeskChatActivity;
import cn.udesk.messagemanager.UdeskMessageManager;
import cn.udesk.model.MsgNotice;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by xiedd on 2018/04/16.
 */

public class MainActivity extends BaseActivity implements NavFragment.OnTabSelectedListener {
    private final static int NOTIFY_ID = 0x11111112;
    private NavFragment mNavBar;
    private FragmentManager mFragmentManager;
    private long mExitTime;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        int position = getIntent().getIntExtra("position", 0);
        if (mNavBar != null) {
            mNavBar.setCurrentItem(position);
        }
    }

    @Override
    public int getLayoutId() {
        RxStatusBarTool.setStatusBar(this, true);
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        mFragmentManager = getSupportFragmentManager();
        mNavBar = ((NavFragment) mFragmentManager.findFragmentById(R.id.fag_nav));
        mNavBar.setup(this, mFragmentManager, R.id.main_container, this);
        EventBus.getDefault().register(mContext);
    }

    @Override
    public void initEvent() {
        // Udesk 客服消息监听
        UdeskMessageManager.getInstance().event_OnNewMsgNotice.bind(this, "OnNewMsgNotice");
    }

    @Override
    public void initData() {
        //AppUpdateHelper.getInstance().update(mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            showToast("再按一次离开");
            mExitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onTabSelected(NavigationButton navigationButton) {
        Fragment fragment = navigationButton.getFragment();
//        if (fragment != null) {
//            if (fragment instanceof PersonalCenterFragment) {
//                RxStatusBarTool.setStatusBarLightMode(this,true);
//            } else{
//                RxStatusBarTool.setStatusBarLightMode(this,false);
//            }
//        }
    }

    @Override
    public void onTabReselected(NavigationButton navigationButton) {
        Fragment fragment = navigationButton.getFragment();
        if (fragment != null && fragment instanceof OnTabReselectListener) {
            OnTabReselectListener listener = (OnTabReselectListener) fragment;
            listener.onTabReselect();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event != null && event.message.equals("share")) {
            share();
        }
        try {
            int tab = Integer.parseInt(event.message);
            mNavBar.setCurrentItem(tab);
        } catch (RuntimeException e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(mContext);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);//完成回调
    }

    /**
     * 分享
     */
    private void share() {
        String url = "https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzU4MzQzMjc2Ng==&scene=124#wechat_redirect";
        String title = "送哪儿";
        String img = "";
        String description = "传递你我真情";

        final ShareDialog mDialog = new ShareDialog(MainActivity.this);
        mDialog.setWeb(url, title, img, description);
        mDialog.setListener(new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                showToast("分享成功");
                mDialog.dismiss();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                showToast("分享错误");
                mDialog.dismiss();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                showToast("分享取消");
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    public void OnNewMsgNotice(MsgNotice msgNotice) {
        if (msgNotice != null) {
            NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(mContext);
            Intent intent = new Intent(mContext, UdeskChatActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
            builder.setContentIntent(pendingIntent);
            builder.setSmallIcon(R.mipmap.ic_logo);
            builder.setContentTitle("您有一条新的消息");
            builder.setContentText("客服：" + msgNotice.getContent());
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo));
            builder.setAutoCancel(true);
            builder.setDefaults(Notification.DEFAULT_SOUND);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //builder.setFullScreenIntent(pendingIntent, true);
            }
            mNotificationManager.notify(NOTIFY_ID, builder.build());
        }
    }
}