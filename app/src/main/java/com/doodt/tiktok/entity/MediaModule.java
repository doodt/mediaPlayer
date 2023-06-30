package com.doodt.tiktok.entity;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class MediaModule implements Serializable {
    //标题
    private String title;
    //日期
    private String dateTime;
    //预览图片地址
    private String imageUrl;
    //视频文件地址
    private String mediaUrl;
    //备注
    private String text;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
