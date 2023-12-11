package com.doodt.tiktok.entity;

import org.litepal.crud.LitePalSupport;

public class Setting extends LitePalSupport {
    private Long id;
    //资源名称
    private String sourceName;
    //资源地址
    private String sourceUrl;
    //推送类型,1顺序推送,2随机推送
    private Integer sourceType;
    //缓存条目数
    @Deprecated
    private Integer cacheNum;
    //是否优先使用m3u8资源,1是,其他否
    private Integer m3u8First;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public Integer getCacheNum() {
        return cacheNum;
    }

    public void setCacheNum(Integer cacheNum) {
        this.cacheNum = cacheNum;
    }

    public Integer getM3u8First() {
        return m3u8First;
    }

    public void setM3u8First(Integer m3u8First) {
        this.m3u8First = m3u8First;
    }
}
