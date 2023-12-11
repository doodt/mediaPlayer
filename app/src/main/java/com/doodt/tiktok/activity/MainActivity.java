package com.doodt.tiktok.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.doodt.tiktok.R;
import com.doodt.tiktok.adapter.ViewPagerAdapter;
import com.doodt.tiktok.entity.MediaModule;
import com.doodt.tiktok.holder.RecyclerItemNormalHolder;
import com.doodt.tiktok.util.Constants;
import com.doodt.tiktok.util.HttpHelper;
import com.doodt.tiktok.view.WarnDialog;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager2 viewPager2;
    private List<MediaModule> mediaModuleList = new ArrayList<>();
    private ImageView imageView;

    private int startIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    //初始化View
    private void initView() {
        imageView = findViewById(R.id.config_btn);
        imageView.setOnClickListener(this::popup);
        viewPager2 = findViewById(R.id.view_pager2);
        viewPagerAdapter = new ViewPagerAdapter(this, mediaModuleList);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        viewPager2.setAdapter(viewPagerAdapter);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                //大于0说明有播放
                int playPosition = GSYVideoManager.instance().getPlayPosition();
                if (playPosition >= 0) {
                    GSYVideoManager.releaseAllVideos();
                    //对应的播放列表TAG
                    playPosition(position);
                }
                Log.i(TAG, "onPageSelected: " + position + "," + viewPager2.getAdapter().getItemCount());
                //当前播放的视频位于倒数第二个时,请求新的数据
                if (viewPager2.getAdapter() != null && position >= viewPager2.getAdapter().getItemCount() - 2) {
                    getSource();
                }
            }
        });
        // 提前加载一页
        viewPager2.setOffscreenPageLimit(1);
    }

    public void popup(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view, Gravity.RIGHT);
        popupMenu.inflate(R.menu.menu_main_right);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            switch (itemId) {
                case R.id.menu_setting:
                    openSetting();
                    break;
                case R.id.menu_del:
                    String dir = mediaModuleList.get(viewPager2.getCurrentItem()).getDir();
                    String url = Constants.cacheSetting.getSourceUrl() + "/" + dir;
                    HttpHelper.okHttpDelete(handler, url, 999);
                    LitePal.delete(MediaModule.class, mediaModuleList.get(viewPager2.getCurrentItem()).getId());
                    break;
            }
            return false;
        });
    }

    private void playPosition(int position) {
        viewPager2.postDelayed(() -> {
            RecyclerView.ViewHolder viewHolder = ((RecyclerView) viewPager2.getChildAt(0)).findViewHolderForAdapterPosition(position);
            if (viewHolder != null) {
                RecyclerItemNormalHolder recyclerItemNormalHolder = (RecyclerItemNormalHolder) viewHolder;
                recyclerItemNormalHolder.getPlayer().startPlayLogic();
            }
        }, 100);
    }

    private void openSetting() {
        getActivityResultRegistry().register("openSetting", new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == 200) {
                initData();
            }
        }).launch(new Intent(this, SettingActivity.class));
    }

    private void getSource() {
        new Thread(() -> {
            try {
                int arg1 = 2;
                List<MediaModule> pageData = new ArrayList<>();
                if (Constants.mediaCache.size() == 0) {
                    List<MediaModule> dbSource = LitePal.findAll(MediaModule.class);
                    if (dbSource != null && dbSource.size() > 0) {
                        Collections.shuffle(dbSource);//打乱顺序
                        Constants.mediaCache.addAll(dbSource);
                        arg1 = 0;
                    }
                }
                pageData = Constants.mediaCache.subList(startIndex, Math.min(startIndex + 10, Constants.mediaCache.size() - 1));
                if (pageData.size() > 0) {
                    startIndex += pageData.size();
                } else {
                    if (startIndex != 0) {
                        startIndex = 0;
                        pageData = Constants.mediaCache.subList(startIndex, Math.min(startIndex + 10, Constants.mediaCache.size() - 1));
                    }
                }
                Message.obtain(handler, 1, arg1, arg1, pageData).sendToTarget();
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
                            if (viewPagerAdapter != null) {
                                viewPagerAdapter.notifyDataSetChanged();
                            }
                            if (msg.arg1 == 0) {
                                GSYVideoManager.releaseAllVideos();
                                //自动播放第0个
                                playPosition(0);
                            }
                        }
                        break;
                    case 999:
//                        Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
                        initData();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    });

    public void initData() {
        Constants.loadSetting();
        int count = LitePal.count(MediaModule.class);
        if (Constants.cacheSetting == null || count == 0) {
            //没有资源,打开设置
            openSetting();
        } else {
            mediaModuleList.clear();
            startIndex = 0;
            Constants.mediaCache.clear();
            GSYVideoManager.releaseAllVideos();
            getSource();
        }
    }

    @Override
    public void onBackPressed() {
        WarnDialog warnDialog = new WarnDialog(this);
        warnDialog.show();
        warnDialog.setTitle("退出提醒");
        warnDialog.setContent("请确认是否完全退出应用");
        warnDialog.setLeftBtnText("取消");
        warnDialog.setRightBtnText("确定");
        warnDialog.setOnButtonClickListener(new WarnDialog.onButtonClickListener() {
            @Override
            public void onLeftClick() {
            }

            @Override
            public void onRightClick() {
                GSYVideoManager.releaseAllVideos();
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }
}
