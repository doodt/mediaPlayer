package com.doodt.tiktok.entity;

import java.io.Serializable;

public class ReFileInfo implements Serializable {
    //文件名
    private String name;
    //路径
    private String path;
    //类型 dir/file
    private String type;
    //大小 KB
    private Long size;
    //文件创建时间
    private Long mtime;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getMtime() {
        return mtime;
    }

    public void setMtime(Long mtime) {
        this.mtime = mtime;
    }
}
