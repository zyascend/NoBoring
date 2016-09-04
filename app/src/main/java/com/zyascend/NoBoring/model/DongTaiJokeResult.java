package com.zyascend.NoBoring.model;

import java.util.List;

/**
 * Created by Administrator on 2016/7/15.
 */
public class DongTaiJokeResult {

    /**
     * showapi_res_code : 0
     * showapi_res_error :
     * showapi_res_body : {"allPages":875,"ret_code":0,"contentlist":[{"id":"57874da76e36d9fcc8174d29","title":"差点被坐死","img":"http://www.zbjuran.com/uploads/allimg/160714/2-160G4161046312.gif","type":3,"ct":"2016-07-14 16:30:31.894"},{"id":"57874da76e36d9fcc8174d28","title":"男人 你还在干啥","img":"http://www.zbjuran.com/uploads/allimg/160714/2-160G41612295Y.gif","type":3,"ct":"2016-07-14 16:30:31.894"},{"id":"57874da76e36d9fcc8174d27","title":"的确是非常美","img":"http://www.zbjuran.com/uploads/allimg/160714/2-160G4161312238.gif","type":3,"ct":"2016-07-14 16:30:31.893"},{"id":"57874da76e36d9fcc8174d20","title":"感觉萌萌哒","img":"http://www.zbjuran.com/uploads/allimg/160714/2-160G4161450X5.gif","type":3,"ct":"2016-07-14 16:30:31.820"},{"id":"57874da76e36d9fcc8174d1f","title":"一堆死变态","img":"http://www.zbjuran.com/uploads/allimg/160714/2-160G4160A94A.gif","type":3,"ct":"2016-07-14 16:30:31.729"},{"id":"57874da76e36d9fcc8174d1e","title":"拍摄角度美赞了","img":"http://www.zbjuran.com/uploads/allimg/160714/2-160G41606144U.gif","type":3,"ct":"2016-07-14 16:30:31.729"},{"id":"57874da76e36d9fcc8174d1d","title":"只骗开了一只手失败","img":"http://www.zbjuran.com/uploads/allimg/160714/2-160G4161J6436.gif","type":3,"ct":"2016-07-14 16:30:31.728"},{"id":"57874da76e36d9fcc8174d1b","title":"妹子你口活真好","img":"http://www.zbjuran.com/uploads/allimg/160714/2-160G4162QXS.gif","type":3,"ct":"2016-07-14 16:30:31.728"},{"id":"578746866e36d9fcc8172cee","title":"偷鸡不成蚀把米 偷鸡不成蚀把米小品","img":"http://www.zbjuran.com/uploads/allimg/160714/7-160G4154504250.gif","type":3,"ct":"2016-07-14 16:00:06.222"},{"id":"57873f8f6e36d9fcc8171b2e","title":"淡定,淡定,你这样怎么能成大事 遇大事淡定诗词","img":"http://www.zbjuran.com/uploads/allimg/160714/7-160G415005Y23.gif","type":3,"ct":"2016-07-14 15:30:23.106"}],"currentPage":2,"allNum":8744,"maxResult":10}
     */
    private int showapi_res_code;
    private String showapi_res_error;

    /**
     * allPages : 875
     * ret_code : 0
     * contentlist : [{"id":"57874da76e36d9fcc8174d29","title":"差点被坐死","img":"http://www.zbjuran.com/uploads/allimg/160714/2-160G4161046312.gif","type":3,"ct":"2016-07-14 16:30:31.894"},{"id":"57874da76e36d9fcc8174d28","title":"男人 你还在干啥","img":"http://www.zbjuran.com/uploads/allimg/160714/2-160G41612295Y.gif","type":3,"ct":"2016-07-14 16:30:31.894"},{"id":"57874da76e36d9fcc8174d27","title":"的确是非常美","img":"http://www.zbjuran.com/uploads/allimg/160714/2-160G4161312238.gif","type":3,"ct":"2016-07-14 16:30:31.893"},{"id":"57874da76e36d9fcc8174d20","title":"感觉萌萌哒","img":"http://www.zbjuran.com/uploads/allimg/160714/2-160G4161450X5.gif","type":3,"ct":"2016-07-14 16:30:31.820"},{"id":"57874da76e36d9fcc8174d1f","title":"一堆死变态","img":"http://www.zbjuran.com/uploads/allimg/160714/2-160G4160A94A.gif","type":3,"ct":"2016-07-14 16:30:31.729"},{"id":"57874da76e36d9fcc8174d1e","title":"拍摄角度美赞了","img":"http://www.zbjuran.com/uploads/allimg/160714/2-160G41606144U.gif","type":3,"ct":"2016-07-14 16:30:31.729"},{"id":"57874da76e36d9fcc8174d1d","title":"只骗开了一只手失败","img":"http://www.zbjuran.com/uploads/allimg/160714/2-160G4161J6436.gif","type":3,"ct":"2016-07-14 16:30:31.728"},{"id":"57874da76e36d9fcc8174d1b","title":"妹子你口活真好","img":"http://www.zbjuran.com/uploads/allimg/160714/2-160G4162QXS.gif","type":3,"ct":"2016-07-14 16:30:31.728"},{"id":"578746866e36d9fcc8172cee","title":"偷鸡不成蚀把米 偷鸡不成蚀把米小品","img":"http://www.zbjuran.com/uploads/allimg/160714/7-160G4154504250.gif","type":3,"ct":"2016-07-14 16:00:06.222"},{"id":"57873f8f6e36d9fcc8171b2e","title":"淡定,淡定,你这样怎么能成大事 遇大事淡定诗词","img":"http://www.zbjuran.com/uploads/allimg/160714/7-160G415005Y23.gif","type":3,"ct":"2016-07-14 15:30:23.106"}]
     * currentPage : 2
     * allNum : 8744
     * maxResult : 10
     */

    private ShowapiResBodyBean showapi_res_body;
    public int getShowapi_res_code() {
        return showapi_res_code;
    }

    public void setShowapi_res_code(int showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public ShowapiResBodyBean getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(ShowapiResBodyBean showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }

    public static class ShowapiResBodyBean {
        private int allPages;
        private int ret_code;
        private int currentPage;
        private int allNum;
        private int maxResult;

        /**
         * id : 57874da76e36d9fcc8174d29
         * title : 差点被坐死
         * img : http://www.zbjuran.com/uploads/allimg/160714/2-160G4161046312.gif
         * type : 3
         * ct : 2016-07-14 16:30:31.894
         */

        private List<DongTaiJoke> contentlist;

        public int getAllPages() {
            return allPages;
        }

        public void setAllPages(int allPages) {
            this.allPages = allPages;
        }

        public int getRet_code() {
            return ret_code;
        }

        public void setRet_code(int ret_code) {
            this.ret_code = ret_code;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getAllNum() {
            return allNum;
        }

        public void setAllNum(int allNum) {
            this.allNum = allNum;
        }

        public int getMaxResult() {
            return maxResult;
        }

        public void setMaxResult(int maxResult) {
            this.maxResult = maxResult;
        }

        public List<DongTaiJoke> getContentlist() {
            return contentlist;
        }

        public void setContentlist(List<DongTaiJoke> contentlist) {
            this.contentlist = contentlist;
        }

        public static class DongTaiJoke {
            private String id;
            private String title;
            private String img;
            private int type;
            private String ct;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getCt() {
                return ct;
            }

            public void setCt(String ct) {
                this.ct = ct;
            }
        }
    }
}
