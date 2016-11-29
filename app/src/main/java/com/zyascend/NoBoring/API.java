package com.zyascend.NoBoring;

import com.zyascend.NoBoring.dao.BudejieVideoResult;
import com.zyascend.NoBoring.dao.FreshContent;
import com.zyascend.NoBoring.dao.FreshResult;
import com.zyascend.NoBoring.dao.GirlResult;

import com.zyascend.NoBoring.dao.TextJokeResult;
import com.zyascend.NoBoring.dao.TuWenJoke;
import com.zyascend.NoBoring.dao.TuWenJokeResult;
import com.zyascend.NoBoring.dao.WeiXinResult;
import com.zyascend.NoBoring.dao.ZhiHuContent;
import com.zyascend.NoBoring.dao.ZhiHuResult;


import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


/**
 *
 * Created by Administrator on 2016/7/14.
 */
public class API {

    public interface YiYuanApi{
        /**
         * https://route.showapi.com/255-1?page=1&showapi_appid=21879&type=10&showapi_sign=B37B72F4933DBF6DC326071C6A4B35CF
         */
        @GET("255-1")
        Observable<TuWenJokeResult> getTuWenJokes(@Query("page")String page,
                                                  @Query("showapi_appid")String appId,
                                                  @Query("type")String type,@Query("showapi_sign")String sign);

    }



    public interface WeiXinNewsApi{
        @GET("wxnew/")
        Observable<WeiXinResult> getWeixinApi(@Query("key")String key,@Query("num")int num);
    }

    public interface GirlApi{
        @GET("data/福利/{number}/{page}")
        Observable<GirlResult> getGirl(@Path("number")int number, @Path("page")int page);
    }


    /**
     * for lastest :
     * http://i.jandan.net/?oxwlxojflwblxbsapi=get_recent_posts&include=url,date,tags,author,
     * title,comment_count,custom_fields&page=1&custom_fields=thumb_c,views&dev=1
     */
    public interface FreshApi{
        @GET(" ")
        Observable<FreshResult> getFresh(@Query("oxwlxojflwblxbsapi")String api,
                                         @Query("include")String include,
                                         @Query("page")int page,
                                         @Query("custom_fields")String custom);
        @GET(" ")
        Observable<FreshContent> getFreshContent(@Query("oxwlxojflwblxbsapi")String api ,
                                                 @Query("id")String id,
                                                 @Query("include")String include);
    }

    public interface BudejieApi{

        @GET("topic/list/jingxuan/10/budejie-android-6.5.5/{page}")
        Observable<TuWenJokeResult> getTuWenJoke(@Path("page")String page);

        @GET("topic/tag-topic/64/hot/budejie-android-6.5.5/{page}")
        Observable<TextJokeResult> getTextJoke(@Path("page")String page);
        //http://s.budejie.com/topic/list/remen/1/budejie-android-6.5.5/0-20.json
        //meinu
        //http://s.budejie.com/topic/tag-topic/117/hot/budejie-android-6.5.5/0-20.json
        @GET("topic/tag-topic/117/hot/budejie-android-6.5.5/{page}")
        Observable<BudejieVideoResult> getGirlVideo(@Path("page")String page);

        @GET("topic/tag-topic/3176/hot/budejie-android-6.5.5/{page}")
        Observable<BudejieVideoResult> getLengZhishiVideo(@Path("page")String page);

        @GET("topic/tag-topic/164/hot/budejie-android-6.5.5/{page}")
        Observable<BudejieVideoResult> getGameVideo(@Path("page")String page);

    }


    public interface ZhiHuApi{

        @Headers("Cache-Control: public, max-age=60")
        @GET("news/latest")
        Observable<ZhiHuResult> getLastest();

        @Headers("Cache-Control: public, max-age=60")
        @GET("news/before/{date}")
        Observable<ZhiHuResult> getHistory(@Path("date") String date);

        @Headers("Cache-Control: public, max-age=1200")
        @GET("news/{id}")
        Observable<ZhiHuContent> getDetails(@Path("id") int id);

    }
}
