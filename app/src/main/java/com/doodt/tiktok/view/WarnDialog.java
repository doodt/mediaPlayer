package com.doodt.tiktok.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.doodt.tiktok.R;

public class WarnDialog extends CommonDialog {

    private TextView textTile;
    private TextView textContent;
    private Button btnLeft;
    private Button btnRight;
    private onButtonClickListener onButtonClickListener;

    public WarnDialog(Context context) {
        onCreateView(context);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }

    public void setOnButtonClickListener(onButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.dialog_warn;
    }

    @Override
    protected void initView() {
        super.initView();
        textTile = view.findViewById(R.id.text_title);
        textContent = view.findViewById(R.id.text_content);
        btnLeft = view.findViewById(R.id.btn_left);
        btnRight = view.findViewById(R.id.btn_right);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onLeftClick();
                }
                setTitle("");
                setContent("");
                setLeftBtnText("");
                setRightBtnText("");
                dismiss();
            }
        });
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onRightClick();
                }
                setTitle("");
                setContent("");
                setLeftBtnText("");
                setRightBtnText("");
                dismiss();
            }
        });
    }


    public void setTitle(String title) {
        textTile.setText(title);
    }

    public void setContent(String content) {
        textContent.setText(content);
    }

    public void setLeftBtnText(String text) {
        btnLeft.setText(text);
    }

    public void setRightBtnText(String text) {
        btnRight.setText(text);
    }

    public interface onButtonClickListener {
        void onLeftClick();

        void onRightClick();
    }
}
