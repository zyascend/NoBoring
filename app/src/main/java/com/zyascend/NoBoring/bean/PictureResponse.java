package com.zyascend.NoBoring.bean;

/**
 * 功能：
 * 作者：zyascend on 2017/7/24 10:19
 * 邮箱：zyascend@qq.com
 */

public class PictureResponse {
    private String mime_type;
    private String updatedAt;
    private String key;
    private String name;
    private String objectId;
    private String createdAt;
    private String __type;
    private String url;
    private String provider;
    private MetaDataBean metaData;
    private String bucket;

    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String get__type() {
        return __type;
    }

    public void set__type(String __type) {
        this.__type = __type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public MetaDataBean getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaDataBean metaData) {
        this.metaData = metaData;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public static class MetaDataBean {
        /**
         * size : 43250
         * owner : unknown
         */

        private int size;
        private String owner;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }
    }
}
