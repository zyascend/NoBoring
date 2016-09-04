package com.zyascend.NoBoring.model;

import java.util.List;

/**
 *
 * Created by Administrator on 2016/7/17.
 */
public class WeiXinResult {


    /**
     * code : 200
     * msg : success
     * newslist : [{"ctime":"2016-07-17","title":"贝嫂与小七嘴对嘴亲吻挨轰次子发照挺母亲","description":"南都娱乐周刊","picUrl":"http://zxpic.gtimg.com/infonew/0/wechat_pics_-6787468.jpg/640","url":"http://mp.weixin.qq.com/s?__biz=NzQ3MzYxNDAx&idx=4&mid=2652342666&sn=8d25d34c9a22ebb646feaa495c1a5ea7"},{"ctime":"2016-07-17","title":"TF家族刘志宏退出娱乐圈，TFBOYS三只送祝福，网友狠批公司逼走旧人换新人？","description":"南都娱乐周刊","picUrl":"http://zxpic.gtimg.com/infonew/0/wechat_pics_-6787470.jpg/640","url":"http://mp.weixin.qq.com/s?__biz=NzQ3MzYxNDAx&idx=2&mid=2652342666&sn=cc84595ba3704cc54819f8833221b41c"},{"ctime":"2016-07-17","title":"郑爽VSbaby：贝微微之争，热搜体质的对抗！","description":"南都娱乐周刊","picUrl":"http://zxpic.gtimg.com/infonew/0/wechat_pics_-6787474.jpg/640","url":"http://mp.weixin.qq.com/s?__biz=NzQ3MzYxNDAx&idx=1&mid=2652342666&sn=420413039b86e42fe19ce71b63ebcf1d"},{"ctime":"2016-07-17","title":"《我们战斗吧》l明明冲着王凯去的，却被和他组CP的王嘉尔圈了粉","description":"IF","picUrl":"http://zxpic.gtimg.com/infonew/0/wechat_pics_-6786644.jpg/640","url":"http://mp.weixin.qq.com/s?__biz=MjM5ODY3MzExNw==&idx=1&mid=2651054070&sn=f7dcb8164e75cbd207316016ae7292f3"},{"ctime":"2016-07-17","title":"当iPhone产地不再是中国","description":"创业邦杂志","picUrl":"http://zxpic.gtimg.com/infonew/0/wechat_pics_-6786732.jpg/640","url":"http://mp.weixin.qq.com/s?__biz=MjM5OTAzMjc4MA==&idx=2&mid=2650050632&sn=bfa24d3edd857602f798f40789128fbf"},{"ctime":"2016-07-17","title":"微信三大事：怒封日涨粉百万公号，关键员工被Facebook挖走，全球十大聊天应用微信竟未进前三|早报","description":"创业邦杂志","picUrl":"http://zxpic.gtimg.com/infonew/0/wechat_pics_-6786733.jpg/640","url":"http://mp.weixin.qq.com/s?__biz=MjM5OTAzMjc4MA==&idx=1&mid=2650050632&sn=c34a4464aa3b3fe98d740d4e4e218dc5"},{"ctime":"2016-07-17","title":"每日捧腹段子精选：蝇子这么小，它能喝多少汤呢？","description":"捧腹笑话","picUrl":"http://zxpic.gtimg.com/infonew/0/wechat_pics_-6786803.jpg/640","url":"http://mp.weixin.qq.com/s?__biz=MjM5ODAxNTQ0MA==&idx=2&mid=2653893603&sn=0edc8b92b21525e010ef2d62a55091f2"},{"ctime":"2016-07-17","title":"杨浩涌：第二次创业经验告诉我，CEO要在人和战略方面花50%以上精力","description":"36氪","picUrl":"http://zxpic.gtimg.com/infonew/0/wechat_pics_-4894251.jpg/640","url":"http://mp.weixin.qq.com/s?__biz=MjAzNzMzNTkyMQ==&idx=1&mid=2653750551&sn=73fcab2b4eedfb6f5853febcec4aa45f"},{"ctime":"2016-07-17","title":"Google高端VR头显被搁置，集中发力VR移动平台","description":"36氪","picUrl":"http://t1.qpic.cn/mblogpic/34d9dfb75cfceb04a840/2000","url":"http://mp.weixin.qq.com/s?__biz=MjAzNzMzNTkyMQ==&idx=2&mid=2653750551&sn=ef39f296c90cc7c2c2f1dca5a7a2c583"},{"ctime":"2016-07-17","title":"人类历史上第一死神，他用100项专利毒害千万人，终于把自己作死了","description":"五道口书院","picUrl":"http://zxpic.gtimg.com/infonew/0/wechat_pics_-6786915.jpg/640","url":"http://mp.weixin.qq.com/s?__biz=MzA5MDY1NzUyMQ==&idx=2&mid=2649893612&sn=c8e3f6cf86de4a3b6da121dde3538cc6"}]
     */

    private int code;
    private String msg;
    /**
     * ctime : 2016-07-17
     * title : 贝嫂与小七嘴对嘴亲吻挨轰次子发照挺母亲
     * description : 南都娱乐周刊
     * picUrl : http://zxpic.gtimg.com/infonew/0/wechat_pics_-6787468.jpg/640
     * url : http://mp.weixin.qq.com/s?__biz=NzQ3MzYxNDAx&idx=4&mid=2652342666&sn=8d25d34c9a22ebb646feaa495c1a5ea7
     */

    private List<WeiXin> newslist;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<WeiXin> getNewslist() {
        return newslist;
    }

    public void setNewslist(List<WeiXin> newslist) {
        this.newslist = newslist;
    }

    public static class WeiXin {
        private String ctime;
        private String title;
        private String description;
        private String picUrl;
        private String url;

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
