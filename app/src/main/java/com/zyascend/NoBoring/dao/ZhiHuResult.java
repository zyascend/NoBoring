package com.zyascend.NoBoring.dao;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 *
 * Created by Administrator on 2016/11/26.
 */

public class ZhiHuResult {


    /**
     * date : 20161126
     * stories : [{"images":["http://pic3.zhimg.com/58c742a32edd282ea0cc21f985d520aa.jpg"],"type":0,"id":9013673,"ga_prefix":"112622","title":"小事 · 得到的都是侥幸，失去的都是人生"},{"title":"拿了金马奖最佳摄影的《长江图》，在导演眼中是这样的","ga_prefix":"112621","images":["http://pic2.zhimg.com/7bb9a7f89fbce9719da635adc9928bf1.jpg"],"multipic":true,"type":0,"id":9014242},{"images":["http://pic3.zhimg.com/9f77f9cebbe2d5b4092debb07370acfe.jpg"],"type":0,"id":9010750,"ga_prefix":"112621","title":"如何心平气和地继续生活下去？"},{"images":["http://pic3.zhimg.com/da9cd0899db0d8f398cc9686ecaf239e.jpg"],"type":0,"id":9004084,"ga_prefix":"112620","title":"怀孕补充叶酸就好，不要什么花样检查"},{"images":["http://pic4.zhimg.com/a441a9cfb05b2b2d57cf259940462fd3.jpg"],"type":0,"id":9004833,"ga_prefix":"112619","title":"开个脑洞，如果人类的神经系统变快三倍会怎么样？"},{"title":"肥了片方亏了作者和观众\u2014\u2014漫改真人电影版的背后真相","ga_prefix":"112618","images":["http://pic2.zhimg.com/9e4d07757c83f3ca46a3ec9d31c2a689.jpg"],"multipic":true,"type":0,"id":9007092},{"images":["http://pic2.zhimg.com/4213b1500fbf5dcd7f0353b80edbf449.jpg"],"type":0,"id":9013687,"ga_prefix":"112617","title":"知乎好问题 · 男士结婚礼服应该怎么穿？"},{"title":"展品齐全又讲究，这博物馆就是游戏界的卢浮宫啊","ga_prefix":"112616","images":["http://pic1.zhimg.com/4afc1a7bba44953a9dbcbed9a7f7c09c.jpg"],"multipic":true,"type":0,"id":9007099},{"title":"逛这样的菜市场，一定是种享受吧","ga_prefix":"112615","images":["http://pic4.zhimg.com/ef2f2b2c0b03b57a0011b3e523eb196f.jpg"],"multipic":true,"type":0,"id":9000772},{"images":["http://pic3.zhimg.com/9b25548817a967cd4b39a9de0eb030d6.jpg"],"type":0,"id":9004979,"ga_prefix":"112614","title":"美国大选也和看脸有关系？"},{"images":["http://pic1.zhimg.com/35425d6e21ed1f817a1fffa6a8701c98.jpg"],"type":0,"id":9010185,"ga_prefix":"112613","title":"F1 已经够刺激了，还上演惊天逆转这种大戏"},{"images":["http://pic1.zhimg.com/fdcfa7e8259f5083ae3e6be56b5f7150.jpg"],"type":0,"id":8880302,"ga_prefix":"112612","title":"大误 · 有什么适合睡前的小故事？"},{"images":["http://pic2.zhimg.com/93151f85a44e1668c7920eb71bf27115.jpg"],"type":0,"id":8997388,"ga_prefix":"112611","title":"拿土豆做主食，里面的淀粉吸收得了吗？"},{"images":["http://pic3.zhimg.com/9dda0320d4032f4b37541bdde11b430a.jpg"],"type":0,"id":8998509,"ga_prefix":"112610","title":"买房是件大事儿，买二手房更要多注意"},{"images":["http://pic1.zhimg.com/e357e2e11a77f37762311fd99957f634.jpg"],"type":0,"id":8998661,"ga_prefix":"112609","title":"我们富人也很辛苦啊，为什么要交那么多税养穷人？"},{"images":["http://pic2.zhimg.com/40fa7e628868d041cca8eb0806107491.jpg"],"type":0,"id":8995234,"ga_prefix":"112608","title":"吸金机器 Candy Crush，就是这样让你停不下来"},{"images":["http://pic3.zhimg.com/872364a43ba435bbe7eecebe031264ea.jpg"],"type":0,"id":9001169,"ga_prefix":"112607","title":"这个凝视地球的「星孩」是怎么来的？"},{"images":["http://pic1.zhimg.com/b4ff80a248e622ab4a5a7bb0a35217fc.jpg"],"type":0,"id":9005165,"ga_prefix":"112607","title":"Android 手机越用越卡，这事儿得解决啊"},{"images":["http://pic1.zhimg.com/15b2903acf3a62179e6c680c3643c05c.jpg"],"type":0,"id":9010002,"ga_prefix":"112607","title":"电商优惠券吸引了用户还没赔钱，是怎么做到的？"},{"images":["http://pic4.zhimg.com/a73b377e4d1b9140728a610f4423aa8b.jpg"],"type":0,"id":9011169,"ga_prefix":"112607","title":"读读日报 24 小时热门 TOP 5 · 全员加薪 1000 元背后，董明珠有什么动机？"},{"images":["http://pic3.zhimg.com/a712b457112ed7eddb2bfff0fa4ee5e2.jpg"],"type":0,"id":9010975,"ga_prefix":"112606","title":"瞎扯 · 如何正确地吐槽"}]
     * top_stories : [{"image":"http://pic2.zhimg.com/36209c8d5ebe67e18fdcfcf82637fc81.jpg","type":0,"id":9014242,"ga_prefix":"112621","title":"拿了金马奖最佳摄影的《长江图》，在导演眼中是这样的"},{"image":"http://pic1.zhimg.com/431598f29ff2b3c575786d68ac1973e4.jpg","type":0,"id":9013687,"ga_prefix":"112617","title":"知乎好问题 · 男士结婚礼服应该怎么穿？"},{"image":"http://pic3.zhimg.com/e98106a0433dd4073ef5cc1eadf3ed3e.jpg","type":0,"id":8995234,"ga_prefix":"112608","title":"吸金机器 Candy Crush，就是这样让你停不下来"},{"image":"http://pic4.zhimg.com/fc22da4377aa6acf061f57c0b9714ee7.jpg","type":0,"id":9005165,"ga_prefix":"112607","title":"Android 手机越用越卡，这事儿得解决啊"},{"image":"http://pic3.zhimg.com/2d0e2e233291a468066a134735ab5b66.jpg","type":0,"id":9011116,"ga_prefix":"112517","title":"知乎好问题 · 如何能让孩子接受自己的弟弟 / 妹妹？"}]
     */

    private String date;
    private List<Story> stories;
    private List<TopStory> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    public List<TopStory> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStory> top_stories) {
        this.top_stories = top_stories;
    }

    public static class Story implements Parcelable{
        /**
         * images : ["http://pic3.zhimg.com/58c742a32edd282ea0cc21f985d520aa.jpg"]
         * type : 0
         * id : 9013673
         * ga_prefix : 112622
         * title : 小事 · 得到的都是侥幸，失去的都是人生
         * multipic : true
         */

        private int type;
        private int id;
        private String ga_prefix;
        private String title;
        private boolean multipic;
        private List<String> images;

        protected Story(Parcel in) {
            type = in.readInt();
            id = in.readInt();
            ga_prefix = in.readString();
            title = in.readString();
            multipic = in.readByte() != 0;
            images = in.createStringArrayList();
        }

        public static final Creator<Story> CREATOR = new Creator<Story>() {
            @Override
            public Story createFromParcel(Parcel in) {
                return new Story(in);
            }

            @Override
            public Story[] newArray(int size) {
                return new Story[size];
            }
        };

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isMultipic() {
            return multipic;
        }

        public void setMultipic(boolean multipic) {
            this.multipic = multipic;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(type);
            dest.writeInt(id);
            dest.writeString(ga_prefix);
            dest.writeString(title);
            dest.writeByte((byte) (multipic ? 1 : 0));
            dest.writeStringList(images);
        }
    }

    public static class TopStory {
        /**
         * image : http://pic2.zhimg.com/36209c8d5ebe67e18fdcfcf82637fc81.jpg
         * type : 0
         * id : 9014242
         * ga_prefix : 112621
         * title : 拿了金马奖最佳摄影的《长江图》，在导演眼中是这样的
         */

        private String image;
        private int type;
        private int id;
        private String ga_prefix;
        private String title;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
