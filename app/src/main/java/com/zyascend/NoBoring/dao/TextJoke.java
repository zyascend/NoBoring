package com.zyascend.NoBoring.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 *
 * Created by Administrator on 2016/11/25.
 */

@Entity
public class TextJoke {

    @Id
    private long id;
    private String url;
    private String content;
    private String np;
    public String getNp() {
        return this.np;
    }
    public void setNp(String np) {
        this.np = np;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    @Generated(hash = 275776016)
    public TextJoke(long id, String url, String content, String np) {
        this.id = id;
        this.url = url;
        this.content = content;
        this.np = np;
    }
    @Generated(hash = 673753194)
    public TextJoke() {
    }
    
}
