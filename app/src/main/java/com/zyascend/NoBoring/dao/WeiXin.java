package com.zyascend.NoBoring.dao;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2016/11/26.
 */
@Entity
public class WeiXin implements Parcelable {

    @Id(autoincrement = true)
    private Long id;
    private String ctime;
    private String title;
    private String description;
    private String picUrl;
    private String url;

    protected WeiXin(Parcel in) {
        ctime = in.readString();
        title = in.readString();
        description = in.readString();
        picUrl = in.readString();
        url = in.readString();
    }

    public static final Creator<WeiXin> CREATOR = new Creator<WeiXin>() {
        @Override
        public WeiXin createFromParcel(Parcel in) {
            return new WeiXin(in);
        }

        @Override
        public WeiXin[] newArray(int size) {
            return new WeiXin[size];
        }
    };

    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getPicUrl() {
        return this.picUrl;
    }
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getCtime() {
        return this.ctime;
    }
    public void setCtime(String ctime) {
        this.ctime = ctime;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 1790508145)
    public WeiXin(Long id, String ctime, String title, String description,
            String picUrl, String url) {
        this.id = id;
        this.ctime = ctime;
        this.title = title;
        this.description = description;
        this.picUrl = picUrl;
        this.url = url;
    }
    @Generated(hash = 824561522)
    public WeiXin() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ctime);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(picUrl);
        dest.writeString(url);
    }
}
