package com.zyascend.NoBoring.dao;

import java.io.Serializable;

/**
 * 功能：
 * 作者：zyascend on 2017/7/22 10:32
 * 邮箱：zyascend@qq.com
 */

public class Photo implements Serializable {

    private int id;
    private String path;  //路径
    private boolean isCamera;

    public Photo(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isCamera() {
        return isCamera;
    }

    public void setIsCamera(boolean isCamera) {
        this.isCamera = isCamera;
    }
}
