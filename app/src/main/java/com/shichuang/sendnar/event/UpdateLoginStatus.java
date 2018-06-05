package com.shichuang.sendnar.event;

/**
 * Created by Administrator on 2018/4/25.
 */

public class UpdateLoginStatus {
    private boolean isLogin;

    public UpdateLoginStatus(){

    }

    public UpdateLoginStatus(boolean bool) {
        this.isLogin = bool;
    }
}
