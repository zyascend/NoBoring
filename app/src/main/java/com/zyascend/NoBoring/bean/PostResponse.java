package com.zyascend.NoBoring.bean;

/**
 * 功能：
 * 作者：zyascend on 2017/7/24 10:15
 * 邮箱：zyascend@qq.com
 */

public class PostResponse {

    private String updatedAt;
    private String content;
    private String objectId;
    private String posterId;
    private String createdAt;
    private int likesCount;
    private UserResponse poster;
    private PictureResponse picture;
    private int commentsCount;

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getPosterId() {
        return posterId;
    }

    public void setPosterId(String posterId) {
        this.posterId = posterId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public UserResponse getPoster() {
        return poster;
    }

    public void setPoster(UserResponse poster) {
        this.poster = poster;
    }

    public PictureResponse getPicture() {
        return picture;
    }

    public void setPicture(PictureResponse picture) {
        this.picture = picture;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }


}
