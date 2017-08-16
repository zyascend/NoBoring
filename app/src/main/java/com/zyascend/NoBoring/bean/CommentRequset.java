package com.zyascend.NoBoring.bean;

import com.alibaba.fastjson.JSON;

/**
 * 功能：
 * 作者：zyascend on 2017/8/16 16:34
 * 邮箱：zyascend@qq.com
 */

public class CommentRequset {

    /**
     * postId : 59754cbf128fe155ce7334d2
     * poster : {"__type":"Pointer","className":"_User","objectId":"59754d1bfe88c2c1d45b156d"}
     * content : 这是一条评论003
     */

    private String postId;
    private PosterBean poster;
    private String content;

    public CommentRequset(String postId, String posterId, String content) {
        this.postId = postId;
        this.poster = new PosterBean(posterId);
        this.content = content;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public PosterBean getPoster() {
        return poster;
    }

    public void setPoster(PosterBean poster) {
        this.poster = poster;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static class PosterBean {
        /**
         * __type : Pointer
         * className : _User
         * objectId : 59754d1bfe88c2c1d45b156d
         */

        private String __type = "Pointer";
        private String className = "_User";
        private String objectId;

        public PosterBean(String objectId) {
            this.objectId = objectId;
        }

        public String get__type() {
            return __type;
        }

        public void set__type(String __type) {
            this.__type = __type;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
