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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.doodt.tiktok.R;
import com.doodt.tiktok.entity.MediaModule;
import com.doodt.tiktok.util.HttpHelper;

import java.util.List;

import cn.jzvd.JzvdStd;

/**
 * 作者：created by Jarchie
 * 时间：2020/12/7 10:28:01
 * 邮箱：jarchie520@gmail.com
 * 说明：仿抖音主界面适配器
 */
public class TiktokAdapter extends RecyclerView.Adapter<TiktokAdapter.ViewHolder> {
    private final String TAG = "TiktokAdapter";
    private List<MediaModule> sources;
    private Context mContext;

    public TiktokAdapter(Context context, List<MediaModule> sources) {
        this.mContext = context;
        this.sources = sources;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_tiktok_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int pos) {
        if (sources != null && sources.size() > 0) {
            MediaModule module = sources.get(pos);
            Log.i(TAG, "onBindViewHolder: " + module.getMediaUrl());
            holder.mVideoView.setUp(HttpHelper.encordHttpUrl(module.getMediaUrl()), module.getTitle(), JzvdStd.STATE_NORMAL);
            if (pos == 0) {
                holder.mVideoView.startVideo();
            }
            Glide.with(mContext)
                    .load(HttpHelper.encordHttpUrl(module.getImageUrl()))
                    .apply(new RequestOptions().error(R.drawable.icon_tiktok))
                    .into(holder.mVideoView.posterImageView);
            holder.mTitle.setText(module.getTitle());
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
        public JzvdStd mVideoView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mRootView = itemView.findViewById(R.id.mRootView);
            mPlay = itemView.findViewById(R.id.mPlay);
            mVideoView = itemView.findViewById(R.id.mVideoView);
            mTitle = itemView.findViewById(R.id.mTitle);
        }
    }
}
