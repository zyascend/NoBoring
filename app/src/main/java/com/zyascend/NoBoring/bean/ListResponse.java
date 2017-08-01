package com.zyascend.NoBoring.bean;

import java.util.List;

/**
 * 功能：
 * 作者：zyascend on 2017/7/23 21:11
 * 邮箱：zyascend@qq.com
 */

public class ListResponse<T> {

    private List<T> results;

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

}
