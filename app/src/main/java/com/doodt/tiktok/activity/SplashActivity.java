package com.doodt.tiktok.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.doodt.tiktok.R;
import com.tbruyelle.rxpermissions3.RxPermissions;


/**
 * 启动页
 */
public class SplashActivity extends BaseActivity {
    private String TAG = "SplashActivity";

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    private Handler handler = new Handler();

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
        super.initData();
        checkPermission();
    }

    /**
     * 申请权限
     */
    @SuppressLint("CheckResult")
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.request(Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(aBoolean -> {
                if (aBoolean) {
                    //启动主页
                    handler.postDelayed(() -> {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }, 1500);
                } else {
                    new AlertDialog.Builder(SplashActivity.this).setCancelable(false).setTitle("提示").setMessage("授权失败，即将重启app...").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            //与正常页面跳转一样可传递序列化数据,在Launch页面内获得
                            intent.putExtra("REBOOT", "reboot");
                            startActivity(intent);
                        }
                    }).show();

                }
            });
        } else {
            handler.postDelayed(() -> {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }, 1500);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            checkPermission();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "起始页销毁....");
    }
}

