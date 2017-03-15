package com.zyascend.NoBoring.dao;

import java.util.Arrays;

/**
 *
 * Created by Administrator on 2016/11/26.
 */

public class ZhiHuContent {

    public String body;
    public String image_source;
    public String title;
    public String image;
    public String share_url;
    public String ga_prefix;
    public Section section;
    public String[] images;
    public int id;

    public class Section {
        public String thumbnail;
        public int id;
        public String name;
    }


}
