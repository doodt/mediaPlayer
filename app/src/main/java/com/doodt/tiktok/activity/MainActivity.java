package com.doodt.tiktok.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.doodt.tiktok.R;
import com.doodt.tiktok.adapter.TiktokAdapter;
import com.doodt.tiktok.entity.MediaModule;
import com.doodt.tiktok.entity.Setting;
import com.doodt.tiktok.util.EchoUtil;
import com.doodt.tiktok.view.CusVideoView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * 作者：created by Jarchie
 * 时间：2020/12/7 10:23:40
 * 邮箱：jarchie520@gmail.com
 * 说明：仿抖音主界面效果实现
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecycler;
    private LinearLayoutManager linearLayoutManager;
    private TiktokAdapter mAdapter;
    private int startIndex;
    private List<MediaModule> mediaModuleList;
    private PagerSnapHelper snapHelper;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    //初始化View
    private void initView() {
        mRecycler = findViewById(R.id.mRecycler);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(linearLayoutManager);

        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecycler);
        mediaModuleList = new ArrayList<>();
        mAdapter = new TiktokAdapter(this, mediaModuleList);
        mRecycler.setAdapter(mAdapter);
        initData();
    }


    //初始化监听
    private void initListener() {
        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE://停止滚动
                        View view = snapHelper.findSnapView(linearLayoutManager);
                        //当前固定后的item,position
                        assert view != null;
                        int position = recyclerView.getChildAdapterPosition(view);
                        if (currentPosition != position) {
                            //如果当前position 和上一次固定后的position相同,说明是同一个item,只不过是滑动了一点点,没有切换到下一个item
                            JzvdStd.releaseAllVideos();//释放所有资源
                            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
                            if (viewHolder != null && viewHolder instanceof TiktokAdapter.ViewHolder) {
                                ((TiktokAdapter.ViewHolder) viewHolder).mVideoView.startVideo();
                            }
                        }
                        currentPosition = position;
                        Log.i(TAG, "onScrollStateChanged: " + currentPosition);
                        if (currentPosition == mediaModuleList.size() - 1) {
                            getSource();
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING://拖动
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING://惯性滑动
                        break;
                }

            }
        });
    }

    //释放
    private void releaseVideo(int index) {
        View itemView = mRecycler.getChildAt(index);
        final CusVideoView mVideoView = itemView.findViewById(R.id.mVideoView);
        final ImageView mPlay = itemView.findViewById(R.id.mPlay);
        mVideoView.stopPlayback();
        mPlay.animate().alpha(0f).start();
    }

    private void getSource() {
        new Thread(() -> {
            try {
                Setting setting = LitePal.findFirst(Setting.class);
                if (setting != null) {
                    List<MediaModule> list = EchoUtil.getTiktokSource(setting.getSourceUrl(), setting.getSourceType(), startIndex, setting.getCacheNum());
                    if (list != null && list.size() > 0) {
                        startIndex += list.size();
                        Message message = new Message();
                        message.what = 1;
                        message.obj = list;
                        handler.sendMessage(message);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "getSource: " + e.getMessage(), e);
            }
        }).start();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 1:
                        List<MediaModule> list = (List<MediaModule>) msg.obj;
                        if (list != null && list.size() > 0) {
                            mediaModuleList.addAll(list);
                            Log.i(TAG, "handleMessage: 获取资源:" + list.size() + "条");
                            mAdapter.notifyDataSetChanged();
                        }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    });

    public void doSetting(View view) {
        startActivityForResult(new Intent(this, SettingActivity.class), 1);
    }

    public void initData() {
        //初始化litepal
        LitePal.initialize(getApplicationContext());
        startIndex = 0;
        getSource();
    }

    /**
     * activity跳转结果回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == 200) {
                    initData();
                }
        }
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }
}
