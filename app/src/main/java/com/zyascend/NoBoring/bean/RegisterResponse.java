package com.zyascend.NoBoring.bean;

/**
 * 功能：注册返回体
 * 作者：zyascend on 2017/7/23 11:08
 * 邮箱：zyascend@qq.com
 */

public class RegisterResponse {

    /**
     * sessionToken : qmdj8pdidnmyzp0c7yqil91oc
     * createdAt : 2015-07-14T02:31:50.100Z
     * objectId : 55a47496e4b05001a7732c5f
     */

    private String sessionToken;
    private String createdAt;
    private String objectId;

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
