package com.zyascend.NoBoring.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2016/11/25.
 */
@Entity
public class BudejieVideo {

    @Id
    private long id;
    private String shareUrl;
    private String thumbUrl;
    private String title;
    private String videoUrl;
    private int tag;
    private String np;
    public int getTag() {
        return this.tag;
    }
    public void setTag(int tag) {
        this.tag = tag;
    }
    public String getVideoUrl() {
        return this.videoUrl;
    }
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getThumbUrl() {
        return this.thumbUrl;
    }
    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
    public String getShareUrl() {
        return this.shareUrl;
    }
    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getNp() {
        return this.np;
    }
    public void setNp(String np) {
        this.np = np;
    }
    @Generated(hash = 922751756)
    public BudejieVideo(long id, String shareUrl, String thumbUrl, String title,
            String videoUrl, int tag, String np) {
        this.id = id;
        this.shareUrl = shareUrl;
        this.thumbUrl = thumbUrl;
        this.title = title;
        this.videoUrl = videoUrl;
        this.tag = tag;
        this.np = np;
    }
    @Generated(hash = 1861809813)
    public BudejieVideo() {
    }



}
