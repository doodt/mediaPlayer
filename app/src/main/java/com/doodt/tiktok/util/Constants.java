package com.doodt.tiktok.util;

import com.doodt.tiktok.entity.MediaModule;
import com.doodt.tiktok.entity.Setting;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    //是否已同步过资源
    public static boolean isInitedSource = false;
    public static List<MediaModule> mediaCache = new ArrayList<>();

    public static Setting cacheSetting = null;

    public static void loadSetting() {
        try {
            cacheSetting = null;
            cacheSetting = LitePal.findFirst(Setting.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
