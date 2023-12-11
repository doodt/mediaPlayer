package com.doodt.tiktok.entity;

import com.alibaba.fastjson.JSONObject;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class MediaModule extends LitePalSupport implements Serializable {
    private Integer id;
    //标题
    private String title;
    //所在目录
    private String dir;
    //日期
    private String dateTime;
    //预览图片地址
    private String imageUrl;
    //视频文件地址
    private String mediaUrl;
    //m3u8文件地址
    private String m3u8Url;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getM3u8Url() {
        return m3u8Url;
    }

    public void setM3u8Url(String m3u8Url) {
        this.m3u8Url = m3u8Url;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
