package com.doodt.tiktok.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.doodt.tiktok.R;
import com.doodt.tiktok.entity.MediaModule;
import com.doodt.tiktok.util.Constants;
import com.doodt.tiktok.util.HttpHelper;
import com.doodt.tiktok.video.SampleCoverVideo;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by guoshuyu on 2017/1/9.
 */

public class RecyclerItemNormalHolder extends RecyclerItemBaseHolder {

    public final static String TAG = "RecyclerView2List";

    protected Context context;

    SampleCoverVideo gsyVideoPlayer;

    ImageView imageView;

    GSYVideoOptionBuilder gsyVideoOptionBuilder;

    public RecyclerItemNormalHolder(Context context, View v) {
        super(v);
        this.context = context;
        gsyVideoPlayer = v.findViewById(R.id.video_item_player);
        imageView = new ImageView(context);
        gsyVideoOptionBuilder = new GSYVideoOptionBuilder();
    }

    public void onBind(final int position, MediaModule videoModel) {
        //预览图片
        Glide.with(context).load(HttpHelper.encordHttpUrl(videoModel.getImageUrl())).apply(new RequestOptions().error(R.drawable.icon_tiktok)).into(imageView);

        Map<String, String> header = new HashMap<>();
        header.put("ee", "33");
        //防止错位，离开释放
        //gsyVideoPlayer.initUIState();
        if (Constants.cacheSetting.getM3u8First() != null && Constants.cacheSetting.getM3u8First() == 1 && !TextUtils.isEmpty(videoModel.getM3u8Url())) {
            gsyVideoOptionBuilder.setUrl(videoModel.getM3u8Url());
        } else {
            gsyVideoOptionBuilder.setUrl(videoModel.getMediaUrl());
        }
        gsyVideoOptionBuilder.setIsTouchWiget(false).setThumbImageView(imageView).setVideoTitle(videoModel.getTitle()).setCacheWithPlay(false).setRotateViewAuto(true).setLockLand(true).setPlayTag(TAG).setMapHeadData(header).setShowFullAnimation(true).setNeedLockFull(true).setPlayPosition(position).setLooping(true).setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                if (!gsyVideoPlayer.isIfCurrentIsFullscreen()) {
                    //静音
//                    GSYVideoManager.instance().setNeedMute(true);
                }

            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                //全屏不静音
//                GSYVideoManager.instance().setNeedMute(true);
            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {
                super.onEnterFullscreen(url, objects);
                GSYVideoManager.instance().setNeedMute(false);
                gsyVideoPlayer.getCurrentPlayer().getTitleTextView().setText((String) objects[0]);
            }
        }).build(gsyVideoPlayer);


        //增加title
//        gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);
        GSYVideoManager.instance().setNeedMute(false);//取消静音
        //设置返回键
        gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
        //设置全屏按键功能
        gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolveFullBtn(gsyVideoPlayer);
            }
        });

        gsyVideoPlayer.loadCoverImageBy(R.mipmap.xxx2, R.mipmap.xxx2);
    }

    /**
     * 全屏幕按键处理
     */
    private void resolveFullBtn(final StandardGSYVideoPlayer standardGSYVideoPlayer) {
        standardGSYVideoPlayer.startWindowFullscreen(context, true, true);
    }

    public SampleCoverVideo getPlayer() {
        return gsyVideoPlayer;
    }
}
