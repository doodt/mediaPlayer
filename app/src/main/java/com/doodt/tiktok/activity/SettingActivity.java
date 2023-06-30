package com.doodt.tiktok.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.doodt.tiktok.R;
import com.doodt.tiktok.entity.Setting;

import org.litepal.LitePal;

public class SettingActivity extends AppCompatActivity {
    private final String TAG = "SettingActivity";
    private EditText baseUrlText;
    private RadioGroup sourceTypeRadios;
    private EditText sourceNumText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        loadConfig();
    }

    public void initView() {
        baseUrlText = findViewById(R.id.baseUrl);
        sourceTypeRadios = findViewById(R.id.sourceTypeGroup);
        sourceNumText = findViewById(R.id.sourceNum);
    }

    private void loadConfig() {
        try {
            Setting setting = LitePal.findFirst(Setting.class);
            if (setting != null) {
                baseUrlText.setText(setting.getSourceUrl());
                if (setting.getSourceType() == 1) {
                    sourceTypeRadios.check(R.id.sourceType1);
                } else {
                    sourceTypeRadios.check(R.id.sourceType2);
                }
                sourceNumText.setText(String.valueOf(setting.getCacheNum()));
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

    @SuppressLint("ShowToast")
    public void saveConfig(View view) {
        try {
            Setting setting = LitePal.findFirst(Setting.class);
            if (setting == null) {
                setting = new Setting();
            }
            String baseUrl = baseUrlText.getText().toString().trim();
            Integer num = Integer.parseInt(sourceNumText.getText().toString().trim());
            int type = sourceTypeRadios.getCheckedRadioButtonId();
            if (type == R.id.sourceType1) {
                setting.setSourceType(1);
            } else {
                setting.setSourceType(2);
            }
            setting.setSourceUrl(baseUrl);
            setting.setCacheNum(num);
            boolean f = setting.save();
            if (f) {
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "保存数据失败", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "保存数据异常:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "saveConfig: ", e);
        }
    }
}