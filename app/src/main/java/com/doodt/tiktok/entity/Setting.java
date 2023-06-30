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
    private Integer cacheNum;

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
}
