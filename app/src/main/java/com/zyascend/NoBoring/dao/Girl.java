package com.zyascend.NoBoring.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 *
 * Created by Administrator on 2016/11/26.
 */

@Entity
public class Girl {
    @Id(autoincrement = true)
    private Long id;
    private String createdAt;
    private String url;
    private String desc;
    public String getDesc() {
        return this.desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getCreatedAt() {
        return this.createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 1954760181)
    public Girl(Long id, String createdAt, String url, String desc) {
        this.id = id;
        this.createdAt = createdAt;
        this.url = url;
        this.desc = desc;
    }
    @Generated(hash = 1070094766)
    public Girl() {
    }
}
