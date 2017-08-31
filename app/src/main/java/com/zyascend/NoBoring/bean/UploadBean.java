package com.zyascend.NoBoring.bean;

import java.io.Serializable;

/**
 * 功能：
 * 作者：zyascend on 2017/8/31 23:05
 * 邮箱：zyascend@qq.com
 */

public class UploadBean implements Serializable {

    private static final long serialVersionUID = -8139356468678580612L;

    public String filePath;
    public String fileName;
    public String content;

    public UploadBean(String filePath, String fileName, String content) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.content = content;
    }
}
