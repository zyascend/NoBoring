package com.zyascend.NoBoring.model;

/**
 * Created by Administrator on 2016/8/16.
 */
public class FreshContent {

    private String status;
    private Post post;
    private String previous_url;
    private String next_url;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setPrevious_url(String previous_url) {
        this.previous_url = previous_url;
    }

    public void setNext_url(String next_url) {
        this.next_url = next_url;
    }

    public String getStatus() {
        return status;
    }

    public Post getPost() {
        return post;
    }

    public String getPrevious_url() {
        return previous_url;
    }

    public String getNext_url() {
        return next_url;
    }

    public class Post {

        private int id;
        private String content;

        public void setId(int id) {
            this.id = id;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getId() {
            return id;
        }

        public String getContent() {
            return content;
        }
    }

}
