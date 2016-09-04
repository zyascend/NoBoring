package com.zyascend.NoBoring.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by Administrator on 2016/8/16.
 */
public class Item implements Parcelable {

    private String tag;
    private int id;
    private String author;
    private String title;
    private String content;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static Creator<Item> getCREATOR() {
        return CREATOR;
    }

    private String url;

    public Item(String tag, int id, String author, String title, String content,String url) {
        this.tag = tag;
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.url = url;
    }

    public Item(){}
    public Item(String tag, int id, String author, String title) {
        this.tag = tag;
        this.id = id;
        this.author = author;
        this.title = title;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tag);
        dest.writeInt(id);
        dest.writeString(author);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(url);
    }


    protected Item(Parcel in) {
        tag = in.readString();
        id = in.readInt();
        author = in.readString();
        title = in.readString();
        content = in.readString();
        url = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public int describeContents() {
        return 0;
    }
}
