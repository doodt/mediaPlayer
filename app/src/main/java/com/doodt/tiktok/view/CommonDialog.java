package com.doodt.tiktok.view;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;


/**
 * 通用的消息弹窗
 */
public abstract class CommonDialog {
    protected AlertDialog dialog;
    protected View view;
    protected Context context;

    protected void onCreateView(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(getLayoutID(), null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        dialog = builder.create();
        initView();
    }

    protected abstract int getLayoutID();

    protected void initView() {

    }

    public void show() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            Window window = dialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER;
            lp.width = dm.widthPixels * 4 / 5;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
    public void setEnableCancel(boolean isCancel){
        dialog.setCancelable(isCancel);
        dialog.setCanceledOnTouchOutside(isCancel);
    }
}
