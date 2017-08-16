package com.zyascend.NoBoring.http;

import com.zyascend.NoBoring.bean.CommentRequset;
import com.zyascend.NoBoring.bean.LoginRequest;
import com.zyascend.NoBoring.bean.RegisterRequsetBody;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * 功能：
 * 作者：zyascend on 2017/7/23 15:38
 * 邮箱：zyascend@qq.com
 */

public class RequestHelper {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static RequestBody getRegisterBody(String username, String password, String email) {
        return RequestBody.create(JSON,new RegisterRequsetBody(username,password,email).toString());
    }


    public static RequestBody getLoginBody(String username, String password) {
        return RequestBody.create(JSON,new LoginRequest(username,password).toString());
    }

    public static RequestBody getIncrementBody(String parameter, String op) {
        String json = "{\""
                + parameter +
                "\":{\"__op\":\""
                + op +
                "\",\"amount\":1}}";
        return RequestBody.create(JSON,json);
    }

    /**
     * 正确的body构建方式
     * @param content
     * @param postId
     * @param posterId
     * @return
     */

    public static RequestBody getPostBody(String content, String postId, String posterId) {
        String json = new CommentRequset(postId,posterId,content).toString();
        return RequestBody.create(JSON,json);
    }
}
