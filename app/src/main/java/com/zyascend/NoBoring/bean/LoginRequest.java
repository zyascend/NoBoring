package com.zyascend.NoBoring.bean;

import com.alibaba.fastjson.JSON;

/**
 * 功能：
 * 作者：zyascend on 2017/7/31 20:20
 * 邮箱：zyascend@qq.com
 */

public class LoginRequest {

    /**
     * username : hjiang
     * password : f32@ds*@&dsa
     */

    private String username;
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
