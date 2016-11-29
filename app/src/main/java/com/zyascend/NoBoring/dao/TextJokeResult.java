package com.zyascend.NoBoring.dao;

import java.util.List;

/**
 * Created by Administrator on 2016/7/16.
 */
public class TextJokeResult {

    /**
     * info : {"count":1000,"np":1480077620}
     * list : [{"status":4,"comment":"24","top_comments":[{"voicetime":0,"status":0,"cmt_type":"text","precid":0,"content":"肯定有孙子上一楼","like_count":64,"u":{"header":["http://wimg.spriteapp.cn/profile","http://dimg.spriteapp.cn/profile"],"uid":"12444248","is_vip":false,"room_url":"","sex":"m","room_name":"","room_role":"","room_icon":"","name":"逸臣啦嘛"},"preuid":0,"passtime":"2016-11-21 15:05:03","voiceuri":"","id":69176939},{"voicetime":0,"status":0,"cmt_type":"text","precid":0,"content":"成功的破解了魔咒","like_count":45,"u":{"header":["http://wimg.spriteapp.cn/profile/large/2016/07/17/578b9ec5e48c2_mini.jpg","http://dimg.spriteapp.cn/profile/large/2016/07/17/578b9ec5e48c2_mini.jpg"],"uid":"17665618","is_vip":false,"room_url":"","sex":"m","room_name":"","room_role":"","room_icon":"","name":"Vstyles"},"preuid":0,"passtime":"2016-11-21 16:17:26","voiceuri":"","id":69180827}],"tags":[{"id":1,"name":"搞笑"},{"id":62,"name":"内涵"},{"id":64,"name":"糗事"}],"bookmark":"15","text":"刚才不小心看到了某条\u201c不转死全家\u201d的贴子\u2026\u2026犹豫了一下还是坐在电脑椅上转了一圈\u2026\u2026挺方便的\u2026\u2026还缓解了疲劳\u2026\u2026","up":"382","share_url":"http://a.f.budejie.com/share/22183753.html?wx.qq.com","down":40,"forward":5,"u":{"header":["http://wimg.spriteapp.cn/profile/large/2016/10/21/580960b542be8_mini.jpg","http://dimg.spriteapp.cn/profile/large/2016/10/21/580960b542be8_mini.jpg"],"uid":"19545198","is_vip":false,"is_v":false,"room_url":"","room_name":"百思葫芦娃2","room_role":"","room_icon":"http://wimg.spriteapp.cn/ugc/2016/1101/gang_level_2.png","name":"大雨还在下着 [百思葫芦娃2]"},"passtime":"2016-11-26 09:56:01","type":"text","id":"22183753"}]
     */

    private InfoBean info;
    private List<ListBean> list;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class InfoBean {
        /**
         * count : 1000
         * np : 1480077620
         */

        private int count;
        private int np;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getNp() {
            return np;
        }

        public void setNp(int np) {
            this.np = np;
        }
    }

    public static class ListBean {
        /**
         * status : 4
         * comment : 24
         * top_comments : [{"voicetime":0,"status":0,"cmt_type":"text","precid":0,"content":"肯定有孙子上一楼","like_count":64,"u":{"header":["http://wimg.spriteapp.cn/profile","http://dimg.spriteapp.cn/profile"],"uid":"12444248","is_vip":false,"room_url":"","sex":"m","room_name":"","room_role":"","room_icon":"","name":"逸臣啦嘛"},"preuid":0,"passtime":"2016-11-21 15:05:03","voiceuri":"","id":69176939},{"voicetime":0,"status":0,"cmt_type":"text","precid":0,"content":"成功的破解了魔咒","like_count":45,"u":{"header":["http://wimg.spriteapp.cn/profile/large/2016/07/17/578b9ec5e48c2_mini.jpg","http://dimg.spriteapp.cn/profile/large/2016/07/17/578b9ec5e48c2_mini.jpg"],"uid":"17665618","is_vip":false,"room_url":"","sex":"m","room_name":"","room_role":"","room_icon":"","name":"Vstyles"},"preuid":0,"passtime":"2016-11-21 16:17:26","voiceuri":"","id":69180827}]
         * tags : [{"id":1,"name":"搞笑"},{"id":62,"name":"内涵"},{"id":64,"name":"糗事"}]
         * bookmark : 15
         * text : 刚才不小心看到了某条“不转死全家”的贴子……犹豫了一下还是坐在电脑椅上转了一圈……挺方便的……还缓解了疲劳……
         * up : 382
         * share_url : http://a.f.budejie.com/share/22183753.html?wx.qq.com
         * down : 40
         * forward : 5
         * u : {"header":["http://wimg.spriteapp.cn/profile/large/2016/10/21/580960b542be8_mini.jpg","http://dimg.spriteapp.cn/profile/large/2016/10/21/580960b542be8_mini.jpg"],"uid":"19545198","is_vip":false,"is_v":false,"room_url":"","room_name":"百思葫芦娃2","room_role":"","room_icon":"http://wimg.spriteapp.cn/ugc/2016/1101/gang_level_2.png","name":"大雨还在下着 [百思葫芦娃2]"}
         * passtime : 2016-11-26 09:56:01
         * type : text
         * id : 22183753
         */

        private int status;
        private String comment;
        private String bookmark;
        private String text;
        private String up;
        private String share_url;
        private int down;
        private int forward;
        private UBean u;
        private String passtime;
        private String type;
        private String id;
        private List<TopCommentsBean> top_comments;
        private List<TagsBean> tags;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getBookmark() {
            return bookmark;
        }

        public void setBookmark(String bookmark) {
            this.bookmark = bookmark;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getUp() {
            return up;
        }

        public void setUp(String up) {
            this.up = up;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public int getDown() {
            return down;
        }

        public void setDown(int down) {
            this.down = down;
        }

        public int getForward() {
            return forward;
        }

        public void setForward(int forward) {
            this.forward = forward;
        }

        public UBean getU() {
            return u;
        }

        public void setU(UBean u) {
            this.u = u;
        }

        public String getPasstime() {
            return passtime;
        }

        public void setPasstime(String passtime) {
            this.passtime = passtime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<TopCommentsBean> getTop_comments() {
            return top_comments;
        }

        public void setTop_comments(List<TopCommentsBean> top_comments) {
            this.top_comments = top_comments;
        }

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public static class UBean {
            /**
             * header : ["http://wimg.spriteapp.cn/profile/large/2016/10/21/580960b542be8_mini.jpg","http://dimg.spriteapp.cn/profile/large/2016/10/21/580960b542be8_mini.jpg"]
             * uid : 19545198
             * is_vip : false
             * is_v : false
             * room_url :
             * room_name : 百思葫芦娃2
             * room_role :
             * room_icon : http://wimg.spriteapp.cn/ugc/2016/1101/gang_level_2.png
             * name : 大雨还在下着 [百思葫芦娃2]
             */

            private String uid;
            private boolean is_vip;
            private boolean is_v;
            private String room_url;
            private String room_name;
            private String room_role;
            private String room_icon;
            private String name;
            private List<String> header;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public boolean isIs_vip() {
                return is_vip;
            }

            public void setIs_vip(boolean is_vip) {
                this.is_vip = is_vip;
            }

            public boolean isIs_v() {
                return is_v;
            }

            public void setIs_v(boolean is_v) {
                this.is_v = is_v;
            }

            public String getRoom_url() {
                return room_url;
            }

            public void setRoom_url(String room_url) {
                this.room_url = room_url;
            }

            public String getRoom_name() {
                return room_name;
            }

            public void setRoom_name(String room_name) {
                this.room_name = room_name;
            }

            public String getRoom_role() {
                return room_role;
            }

            public void setRoom_role(String room_role) {
                this.room_role = room_role;
            }

            public String getRoom_icon() {
                return room_icon;
            }

            public void setRoom_icon(String room_icon) {
                this.room_icon = room_icon;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<String> getHeader() {
                return header;
            }

            public void setHeader(List<String> header) {
                this.header = header;
            }
        }

        public static class TopCommentsBean {
            /**
             * voicetime : 0
             * status : 0
             * cmt_type : text
             * precid : 0
             * content : 肯定有孙子上一楼
             * like_count : 64
             * u : {"header":["http://wimg.spriteapp.cn/profile","http://dimg.spriteapp.cn/profile"],"uid":"12444248","is_vip":false,"room_url":"","sex":"m","room_name":"","room_role":"","room_icon":"","name":"逸臣啦嘛"}
             * preuid : 0
             * passtime : 2016-11-21 15:05:03
             * voiceuri :
             * id : 69176939
             */

            private int voicetime;
            private int status;
            private String cmt_type;
            private int precid;
            private String content;
            private int like_count;
            private UBeanX u;
            private int preuid;
            private String passtime;
            private String voiceuri;
            private int id;

            public int getVoicetime() {
                return voicetime;
            }

            public void setVoicetime(int voicetime) {
                this.voicetime = voicetime;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getCmt_type() {
                return cmt_type;
            }

            public void setCmt_type(String cmt_type) {
                this.cmt_type = cmt_type;
            }

            public int getPrecid() {
                return precid;
            }

            public void setPrecid(int precid) {
                this.precid = precid;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getLike_count() {
                return like_count;
            }

            public void setLike_count(int like_count) {
                this.like_count = like_count;
            }

            public UBeanX getU() {
                return u;
            }

            public void setU(UBeanX u) {
                this.u = u;
            }

            public int getPreuid() {
                return preuid;
            }

            public void setPreuid(int preuid) {
                this.preuid = preuid;
            }

            public String getPasstime() {
                return passtime;
            }

            public void setPasstime(String passtime) {
                this.passtime = passtime;
            }

            public String getVoiceuri() {
                return voiceuri;
            }

            public void setVoiceuri(String voiceuri) {
                this.voiceuri = voiceuri;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public static class UBeanX {
                /**
                 * header : ["http://wimg.spriteapp.cn/profile","http://dimg.spriteapp.cn/profile"]
                 * uid : 12444248
                 * is_vip : false
                 * room_url :
                 * sex : m
                 * room_name :
                 * room_role :
                 * room_icon :
                 * name : 逸臣啦嘛
                 */

                private String uid;
                private boolean is_vip;
                private String room_url;
                private String sex;
                private String room_name;
                private String room_role;
                private String room_icon;
                private String name;
                private List<String> header;

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

                public boolean isIs_vip() {
                    return is_vip;
                }

                public void setIs_vip(boolean is_vip) {
                    this.is_vip = is_vip;
                }

                public String getRoom_url() {
                    return room_url;
                }

                public void setRoom_url(String room_url) {
                    this.room_url = room_url;
                }

                public String getSex() {
                    return sex;
                }

                public void setSex(String sex) {
                    this.sex = sex;
                }

                public String getRoom_name() {
                    return room_name;
                }

                public void setRoom_name(String room_name) {
                    this.room_name = room_name;
                }

                public String getRoom_role() {
                    return room_role;
                }

                public void setRoom_role(String room_role) {
                    this.room_role = room_role;
                }

                public String getRoom_icon() {
                    return room_icon;
                }

                public void setRoom_icon(String room_icon) {
                    this.room_icon = room_icon;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public List<String> getHeader() {
                    return header;
                }

                public void setHeader(List<String> header) {
                    this.header = header;
                }
            }
        }

        public static class TagsBean {
            /**
             * id : 1
             * name : 搞笑
             */

            private int id;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}