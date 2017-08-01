package com.zyascend.NoBoring.bean;

import com.alibaba.fastjson.JSON;

/**
 * 功能：
 * 作者：zyascend on 2017/7/23 11:10
 * 邮箱：zyascend@qq.com
 */

public class RegisterRequsetBody {

    /**
     * username : hjiang
     * password : f32@ds*@&dsa
     * email : xxxx@qq.com
     */

    private String username;
    private String password;
    private String email;

    public RegisterRequsetBody(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
