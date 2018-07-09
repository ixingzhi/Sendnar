package com.shichuang.sendnar.common;

import android.content.Context;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.shichuang.sendnar.entify.AMBaseDto;
import com.shichuang.sendnar.entify.MyTeam;
import com.shichuang.sendnar.entify.ShoppingCartCount;
import com.shichuang.sendnar.event.UpdateShoppingCartCount;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by xiedongdong on 2018/6/20.
 */

public class ShoppingCartCountHelper {
    private static ShoppingCartCountHelper mInstance;

    public static ShoppingCartCountHelper getInstance() {
        if (null == mInstance) {
            mInstance = new ShoppingCartCountHelper();
        }
        return mInstance;
    }

    public void getCount(Context context) {
        OkGo.<AMBaseDto<ShoppingCartCount>>get(Constants.getShoppingCartCountUrl)
                .tag(context)
                .params("token", TokenCache.token(context))
                .execute(new NewsCallback<AMBaseDto<ShoppingCartCount>>() {
                    @Override
                    public void onStart(Request<AMBaseDto<ShoppingCartCount>, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(final Response<AMBaseDto<ShoppingCartCount>> response) {
                        if (response.body().code == 0 && response.body().data != null) {
                            int count = response.body().data.getCount();
                            EventBus.getDefault().post(new UpdateShoppingCartCount(count));
                        }
                    }

                    @Override
                    public void onError(Response<AMBaseDto<ShoppingCartCount>> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }


}
