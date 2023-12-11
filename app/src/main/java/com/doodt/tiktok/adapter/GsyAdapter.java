package com.doodt.tiktok.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doodt.tiktok.R;
import com.doodt.tiktok.entity.MediaModule;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.List;

/**
 * 作者：created by Jarchie
 * 时间：2020/12/7 10:28:01
 * 邮箱：jarchie520@gmail.com
 * 说明：仿抖音主界面适配器
 */
public class GsyAdapter extends RecyclerView.Adapter<GsyAdapter.ViewHolder> {
    private final String TAG = "GsyAdapter";
    private List<MediaModule> sources;
    private Context mContext;

    public GsyAdapter(Context context, List<MediaModule> sources) {
        this.mContext = context;
        this.sources = sources;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_gsy_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int pos) {
        if (sources != null && sources.size() > 0) {
            MediaModule module = sources.get(pos);
            Log.i(TAG, "onBindViewHolder: " + module.getMediaUrl());
            holder.mVideoView.setUpLazy(module.getMediaUrl(), true, null, null, module.getTitle());
            //增加title
            holder.mVideoView.getTitleTextView().setVisibility(View.GONE);
            //设置返回键
            holder.mVideoView.getBackButton().setVisibility(View.GONE);
            //设置全屏按键功能
            holder.mVideoView.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mVideoView.startWindowFullscreen(mContext, false, true);
                }
            });
            //是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏
            holder.mVideoView.setAutoFullWithSize(true);
            //音频焦点冲突时是否释放
            holder.mVideoView.setReleaseWhenLossAudio(false);
            //全屏动画
            holder.mVideoView.setShowFullAnimation(true);
            //小屏时不触摸滑动
            holder.mVideoView.setIsTouchWiget(false);
            if (pos == 0) {
                holder.mVideoView.startPlayLogic();
            }
//            holder.mTitle.setText(module.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return Math.max(sources.size(), 0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout mRootView;
        public ImageView mPlay;
        public TextView mTitle;
        public StandardGSYVideoPlayer mVideoView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mRootView = itemView.findViewById(R.id.mRootView);
            mPlay = itemView.findViewById(R.id.mPlay);
            mVideoView = itemView.findViewById(R.id.mVideoView);
            mTitle = itemView.findViewById(R.id.mTitle);
        }
    }
}
