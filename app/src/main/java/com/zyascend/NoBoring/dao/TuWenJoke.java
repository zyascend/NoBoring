package com.zyascend.NoBoring.dao;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2016/11/26.
 */
@Entity
public class TuWenJoke {

    @Id
    private long id;
    private String title;
    private String thumbUrl;
    private String picUrl;
    private int picHeight;
    private String time;
    private String shareUrl;

    public String getShareUrl() {
        return this.shareUrl;
    }
    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getPicUrl() {
        return this.picUrl;
    }
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getThumbUrl() {
        return this.thumbUrl;
    }
    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
    public int getPicHeight() {
        return this.picHeight;
    }
    public void setPicHeight(int picHeight) {
        this.picHeight = picHeight;
    }
    @Generated(hash = 272925692)
    public TuWenJoke(long id, String title, String thumbUrl, String picUrl,
            int picHeight, String time, String shareUrl) {
        this.id = id;
        this.title = title;
        this.thumbUrl = thumbUrl;
        this.picUrl = picUrl;
        this.picHeight = picHeight;
        this.time = time;
        this.shareUrl = shareUrl;
    }
    @Generated(hash = 1441009629)
    public TuWenJoke() {
    }

}
