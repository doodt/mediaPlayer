package com.doodt.tiktok.util;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.doodt.tiktok.entity.MediaModule;
import com.doodt.tiktok.entity.ReFileInfo;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EchoUtil {
    private static final String TAG = "EchoUtil";
    private static JSONArray fileList = new JSONArray();


    /**
     * 获取所有资源
     *
     * @param url 资源列表地址
     * @return
     */
    public static List<MediaModule> getTiktokSource(Handler handler, String url) {
        try {

        } catch (Exception e) {
            Log.e(TAG, "getMediaData: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取随机资源
     *
     * @param baseUrl
     * @param num     文件个数
     * @return
     */
    private static List<MediaModule> getTiktok365RandomSource(String baseUrl, int num) {
        List<MediaModule> modules = new ArrayList<>();
        try {
            if (TextUtils.isEmpty(baseUrl) || num <= 0) return null;
            JSONObject json = HttpHelper.okHttpGet(baseUrl + "?json=true");
            if (json == null || json.size() == 0) return null;
            //获取月份文件夹
            List<ReFileInfo> dateFiles = json.getJSONArray("files").toJavaList(ReFileInfo.class);
            int dirIndex = createRandoms(dateFiles.size(), 1).get(0);
            JSONObject jsonObject = HttpHelper.okHttpGet(baseUrl + "/" + dateFiles.get(dirIndex).getName() + "?json=true");
            //获取指定月份下的所有文件夹
            List<ReFileInfo> subFileInfo = jsonObject.getJSONArray("files").toJavaList(ReFileInfo.class);
            if (subFileInfo.size() > 0) {
                List<Integer> indexs = createRandoms(subFileInfo.size(), num);
                for (int i = 0; i < indexs.size(); i++) {
                    ReFileInfo f = subFileInfo.get(i);
                    MediaModule mediaModule = new MediaModule();
                    mediaModule.setText(f.getName());
                    mediaModule.setTitle(f.getName());
                    mediaModule.setImageUrl(baseUrl + "/" + dateFiles.get(dirIndex).getName() + "/" + f.getName() + "/" + f.getName() + ".jpg");
                    mediaModule.setMediaUrl(baseUrl + "/" + dateFiles.get(dirIndex).getName() + "/" + f.getName() + "/" + f.getName() + ".mp4");
                    mediaModule.setDateTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA).format(new Date(f.getMtime())));
                    modules.add(mediaModule);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modules;
    }

    /**
     * 从集合中随机取出N个不重复的元素
     *
     * @param listSize 需要被取出数据的集合
     * @param n        取出的元素数量
     * @return
     */
    private static List<Integer> createRandoms(int listSize, int n) {
        Map<Integer, String> map = new HashMap();
        List<Integer> news = new ArrayList();
        if (listSize <= n) {
            for (int i = 0; i < listSize; i++) {
                news.add(i);
            }
        } else {
            while (map.size() < n) {
                int random = (int) (Math.random() * listSize);
                if (!map.containsKey(random)) {
                    map.put(random, "");
                    news.add(random);
                }
            }
        }
        return news;
    }

    public static String getHttpRes(String httpUrl) {
        String res = null;
        try {

        } catch (Exception e) {
            Log.e(TAG, "getHttpRes: " + e.getMessage(), e);
        }
        return res;
    }
}
