package com.shichuang.sendnar.widget;

import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.shichuang.open.tool.RxToastTool;
import com.shichuang.open.widget.BaseDialog;
import com.shichuang.sendnar.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * Created by Administrator on 2018/5/8.
 */

public class ShareDialog extends BaseDialog implements View.OnClickListener {
    private Activity mActivity;
    private View view;
    private UMWeb web;
    private UMShareListener shareListener;

    public ShareDialog(Activity context) {
        super(context, 0.6f, Gravity.BOTTOM);
        this.mActivity = context;
        setFullScreenWidth();
        view = LayoutInflater.from(mContext).inflate(R.layout.dialog_share, null);
        setContentView(view);
        initView();
        initEvent();
    }

    private void initView() {
    }

    private void initEvent() {
        view.findViewById(R.id.ll_share_wechat).setOnClickListener(this);
        view.findViewById(R.id.ll_share_circle_friends).setOnClickListener(this);
        view.findViewById(R.id.btn_close).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_share_wechat:
                share(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.ll_share_circle_friends:
                share(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.btn_close:
                dismiss();
                break;
        }
    }

    private void share(SHARE_MEDIA shareMedia) {
        if (web != null) {
            new ShareAction(mActivity)
                    .withMedia(web)
                    .setPlatform(shareMedia)
                    .setCallback(shareListener)
                    .share();
        } else {
            RxToastTool.showShort("分享数据有误");
        }
    }

    public void setWeb(String url, String title, String imgUrl, String description) {
        web = new UMWeb(url);
        web.setTitle(title);//标题
        if (TextUtils.isEmpty(imgUrl)) {   //缩略图
            web.setThumb(new UMImage(mContext, R.mipmap.ic_logo));
        } else {
            web.setThumb(new UMImage(mContext, imgUrl));
        }
        web.setDescription(description);//描述
    }

    public void setListener(UMShareListener listener) {
        this.shareListener = listener;
    }

}
