package com.doodt.tiktok.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.doodt.tiktok.databinding.ActivitySettingBinding;
import com.doodt.tiktok.entity.MediaModule;
import com.doodt.tiktok.entity.Setting;
import com.doodt.tiktok.util.Constants;
import com.doodt.tiktok.util.HttpHelper;

import org.litepal.LitePal;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SettingActivity extends AppCompatActivity {
    private ActivitySettingBinding binding;
    private final String TAG = "SettingActivity";
    private ExecutorService exec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        exec = Executors.newSingleThreadExecutor();
        initView();
        loadConfig();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            Toast.makeText(getApplicationContext(), message.what == 1 ? "获取资源成功" : "获取资源失败", Toast.LENGTH_LONG).show();
            return false;
        }
    });

    private void initView() {
        //获取资源
        binding.btnAddSource.setOnClickListener(view -> {
            if (TextUtils.isEmpty(binding.baseUrl.getText().toString())) {
                Toast.makeText(getApplicationContext(), "请输入资源地址", Toast.LENGTH_SHORT).show();
                return;
            }
            final String url = binding.baseUrl.getText().toString();
            exec.execute(() -> {
                try {
                    JSONObject json;
                    if (Constants.isInitedSource) {
                        json = HttpHelper.okHttpGet(url + "/get");
                        if (json.getString("code").equals("SUCCESS")) {
                            List<MediaModule> list = json.getJSONArray("data").toJavaList(MediaModule.class);
                            if (list != null) {
                                LitePal.deleteAll(MediaModule.class);
                                LitePal.saveAll(list);
                                Log.i(TAG, "initView: 获取资源:" + list.size() + "条!");
                                Constants.mediaCache.clear();
                                Message.obtain(handler, 1).sendToTarget();
                                return;
                            }
                        }
                    } else {
                        json = HttpHelper.okHttpPost(url + "/create", "");
                        if (json.getString("code").equals("SUCCESS")) {
                            List<MediaModule> list = json.getJSONArray("data").toJavaList(MediaModule.class);
                            if (list != null) {
                                LitePal.deleteAll(MediaModule.class);
                                LitePal.saveAll(list);
                                Log.i(TAG, "initView: 获取资源:" + list.size() + "条!");
                                Constants.mediaCache.clear();
                                Constants.isInitedSource = true;
                                Message.obtain(handler, 1).sendToTarget();
                                return;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message.obtain(handler, -1).sendToTarget();
            });
        });
        //保存配置
        binding.btnSave.setOnClickListener(this::saveConfig);
        //清空资源
        binding.btnClearSource.setOnClickListener(view -> {
            LitePal.deleteAll(MediaModule.class);
            Constants.isInitedSource = false;
            Toast.makeText(getApplicationContext(), "清空资源成功", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadConfig() {
        try {
            Setting setting = LitePal.findFirst(Setting.class);
            if (setting != null) {
                binding.baseUrl.setText(setting.getSourceUrl());
                binding.cbM3u8.setChecked(setting.getM3u8First() != null && setting.getM3u8First() == 1);
            }
        } catch (Exception e) {
            Log.e(TAG, "loadConfig: ", e);
        }
    }

    public void back(View view) {
        Log.i(TAG, "finishActivity: ");
        Intent intent = new Intent();
        setResult(200, intent);
        finish();
    }

    public void saveConfig(View view) {
        try {
            Setting setting = LitePal.findFirst(Setting.class);
            if (setting == null) {
                setting = new Setting();
            }
            String baseUrl = binding.baseUrl.getText().toString().trim();
            setting.setSourceUrl(baseUrl);
            setting.setM3u8First(binding.cbM3u8.isChecked() ? 1 : -1);
            boolean f = setting.save();
            if (f) {
                Constants.loadSetting();
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "保存数据失败", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "保存数据异常:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "saveConfig: ", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exec != null) {
            exec.shutdownNow();
            exec = null;
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

}