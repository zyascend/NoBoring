package com.zyascend.NoBoring.dao;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * Created by Administrator on 2016/7/19.
 */
public class GirlResult {

    public boolean error;
    public @SerializedName("results") List<Girl> girls;

}
