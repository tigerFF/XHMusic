package cn.xiaohufu.xhmusic.ui;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;


import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.xiaohufu.xhmusic.R;
import cn.xiaohufu.xhmusic.base.BaseActivity;
import cn.xiaohufu.xhmusic.utils.MusicUtils;
import cn.xiaohufu.xhmusic.utils.SharePreferenceUtil;
import cn.xiaohufu.xhmusic.utils.Utils;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class SplashActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    //需要的权限,获得手机状态（包括号码等）以及外部储存的读写权限
    String[] perms = {Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, getString(R.string.reuqest_permission), 1, perms);
        } else {
            jumpIntoMainActivity();
        }
    }

    private void jumpIntoMainActivity() {
        Disposable subscribe = Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(aLong -> {
                    //初始化本地音乐数量  在授予权限后在初始化 避免出现未授权异常
                    final SharePreferenceUtil preferenceUtil = SharePreferenceUtil.getInstance(Utils.getApp());
                    if (preferenceUtil.getLocalMusicCount() == -1) {
                        preferenceUtil.saveLocalMusicCount(MusicUtils.queryMusicSize(Utils.getApp(), MusicUtils.START_FROM_LOCAL));
                    }
                    String authToken = preferenceUtil.getAuthToken("");
                    if (TextUtils.isEmpty(authToken)) {
                        //进入登录界面
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    } else {
                        //进入主界面
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }
                    finish();
                });
    }

    //如果已授权
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (!EasyPermissions.hasPermissions(this, String.valueOf(perms))) {
            EasyPermissions.requestPermissions(this, getString(R.string.reuqest_permission), 1, String.valueOf(perms));
        } else {
            jumpIntoMainActivity();
        }
    }

    //未授权
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}