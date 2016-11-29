package com.zyascend.NoBoring.dao;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 *
 * Created by Administrator on 2016/8/16.
 */
@Entity
public class Fresh implements Parcelable{
    
    @Id
    private long id;
    private String tag;
    private String author;
    private String title;
    private String content;
    private String thumbUrl;

    protected Fresh(Parcel in) {
        id = in.readLong();
        tag = in.readString();
        author = in.readString();
        title = in.readString();
        content = in.readString();
        thumbUrl = in.readString();
    }

    public static final Creator<Fresh> CREATOR = new Creator<Fresh>() {
        @Override
        public Fresh createFromParcel(Parcel in) {
            return new Fresh(in);
        }

        @Override
        public Fresh[] newArray(int size) {
            return new Fresh[size];
        }
    };

    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getAuthor() {
        return this.author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getTag() {
        return this.tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
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
    @Generated(hash = 1426317603)
    public Fresh(long id, String tag, String author, String title, String content,
            String thumbUrl) {
        this.id = id;
        this.tag = tag;
        this.author = author;
        this.title = title;
        this.content = content;
        this.thumbUrl = thumbUrl;
    }
    @Generated(hash = 1411708098)
    public Fresh() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(tag);
        dest.writeString(author);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(thumbUrl);
    }
}
