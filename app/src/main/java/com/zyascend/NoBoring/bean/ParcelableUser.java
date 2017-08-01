package com.zyascend.NoBoring.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 功能：用于UserActivity与Dialogfragment之间的传值
 * 作者：zyascend on 2017/7/25 16:09
 * 邮箱：zyascend@qq.com
 */

public class ParcelableUser implements Parcelable{

    public String userName;
    public String userId;
    public String avatarUrl;

    public ParcelableUser(String userName, String userId, String avatarUrl) {
        this.userName = userName;
        this.userId = userId;
        this.avatarUrl = avatarUrl;
    }

    protected ParcelableUser(Parcel in) {
        userName = in.readString();
        userId = in.readString();
        avatarUrl = in.readString();
    }

    public static final Creator<ParcelableUser> CREATOR = new Creator<ParcelableUser>() {
        @Override
        public ParcelableUser createFromParcel(Parcel in) {
            return new ParcelableUser(in);
        }

        @Override
        public ParcelableUser[] newArray(int size) {
            return new ParcelableUser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(userId);
        dest.writeString(avatarUrl);
    }
}
