package com.zyascend.NoBoring.bean;

import java.io.Serializable;

/**
 * 功能：
 * 作者：zyascend on 2017/8/14 16:19
 * 邮箱：zyascend@qq.com
 */

public class DownLoadBean implements Serializable {

    private static final long serialVersionUID = 7947555201477876363L;

    public String name;
    public String downloadUrl;
    public String picUrl;
    public int id;

    public DownLoadBean(String name, String downloadUrl, String picUrl,int id) {
        this.name = name;
        this.downloadUrl = downloadUrl;
        this.picUrl = picUrl;
        this.id = id;
    }


}
