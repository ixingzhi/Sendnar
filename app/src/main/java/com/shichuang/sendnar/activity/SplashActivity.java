package com.shichuang.sendnar.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.shichuang.open.tool.RxActivityTool;
import com.shichuang.sendnar.MainActivity;
import com.shichuang.sendnar.common.TokenCache;
import com.shichuang.sendnar.common.UdeskHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/14.
 */

public class SplashActivity extends AppCompatActivity {
    private final int REQUEST_CODE_ASK_RUNTIME_PERMISSIONS = 124;
    private boolean needFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkRuntimePermission();
    }

    private void checkRuntimePermission() {
        // 6.0 以下手机直接启动
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startUp();
            return;
        }

        final List<String> permissionsList = new ArrayList<>();
        addPermissionIfRequired(permissionsList, Manifest.permission.READ_PHONE_STATE);
        boolean basePermission = addPermissionIfRequired(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // 不需要额外权限或者最基本的权限（存储）保证后，直接启动
        if (permissionsList.isEmpty() || basePermission) {
            startUp();
            return;
        }

        AlertDialog dialog = new AlertDialog.Builder(this).setMessage("我们需要一些基本权限来保证送哪儿的正常运行")
                .setPositiveButton("我知道了", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] parr = permissionsList.toArray(new String[permissionsList.size()]);
                        ActivityCompat.requestPermissions(SplashActivity.this, parr,
                                REQUEST_CODE_ASK_RUNTIME_PERMISSIONS);
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (REQUEST_CODE_ASK_RUNTIME_PERMISSIONS == requestCode) {
            Map<String, Integer> perms = new HashMap<>();
            perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            for (int i = 0; i < permissions.length; i++) {
                perms.put(permissions[i], grantResults[i]);
            }

            // 如果获得了基本存储权限，则允许执行
            if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                startUp();
                return;
            }

            String msg = "需要存储权限，否则将无法正常使用送哪儿";
            AlertDialog dialog = new AlertDialog.Builder(this).setMessage(msg).setPositiveButton("去设置", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean addPermissionIfRequired(List<String> permissionsList, String permission) {
        boolean allowed = true;
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            allowed = false;
        }
        return allowed;
    }

    private void startUp() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 设置Udesk客服信息
        if(TokenCache.isUserLogin(SplashActivity.this)){
            UdeskHelper.setUserInfo(SplashActivity.this);
        }
        RxActivityTool.skipActivity(SplashActivity.this, MainActivity.class);

        needFinish = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (needFinish) {
            finish();
        }
    }

}
