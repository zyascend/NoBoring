package com.zyascend.NoBoring.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * Created by Administrator on 2016/7/19.
 */
public class GirlResult {

    public boolean error;
    public @SerializedName("results") List<Girl> girls;

    public static class Girl {
        public String createdAt;
        public String url;
        public String desc;
    }
}
