package com.shichuang.sendnar.common;

import android.content.Context;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.MessageCount;
import com.shichuang.sendnar.entify.ShoppingCartCount;
import com.shichuang.sendnar.event.MessageCountEvent;
import com.shichuang.sendnar.event.UpdateShoppingCartCount;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by xiedongdong on 2018/6/20.
 */

public class MessageCountHelper {
    private static MessageCountHelper mInstance;

    public static MessageCountHelper getInstance() {
        if (null == mInstance) {
            mInstance = new MessageCountHelper();
        }
        return mInstance;
    }

    public void getCount(Context context) {
        OkGo.<AMBaseDto<MessageCount>>get(Constants.messageCountUrl)
                .tag(context)
                .params("token", TokenCache.token(context))
                .execute(new NewsCallback<AMBaseDto<MessageCount>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<MessageCount>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<MessageCount>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            int count = response.body().data.getCount();
                            EventBus.getDefault().post(new MessageCountEvent(count));
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<MessageCount>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }


}
