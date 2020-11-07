package cn.xiaohufu.xhmusic.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;

import cn.xiaohufu.xhmusic.R;
import cn.xiaohufu.xhmusic.base.BaseActivity;

public class SplashActivity extends BaseActivity {

    //需要的权限
    String[] perms = {Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
    }
}