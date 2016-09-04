package com.zyascend.NoBoring;

import com.zyascend.NoBoring.model.DongTaiJokeResult;
import com.zyascend.NoBoring.model.FreshContent;
import com.zyascend.NoBoring.model.FreshResult;
import com.zyascend.NoBoring.model.GirlResult;

import com.zyascend.NoBoring.model.TextJokeResult;
import com.zyascend.NoBoring.model.WeiXinResult;


import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 *
 * Created by Administrator on 2016/7/14.
 */
public class API {



    public interface DongTaiJokeApi{
        @GET("341-3")
        Observable<DongTaiJokeResult> getDongTaiJoke(
                @Query("maxResult")String maxResult,
                @Query("page")String page,
                @Query("showapi_appid")String appid,
                @Query("showapi_sign")String sign);
    }


    public interface TextJokeApi{
        @GET("341-1")
        Observable<TextJokeResult> getTextJoke(@Query("maxResult")String maxResult,
                                               @Query("page")String page,
                                               @Query("showapi_appid")String appid,
                                               @Query("time")String time,
                                               @Query("showapi_sign")String sign);
    }


    public interface WeiXinNewsApi{
        @GET("wxnew/")
        Observable<WeiXinResult> getWeixinApi(@Query("key")String key,@Query("num")int num);
    }



    public interface GirlApi{
        @GET("data/福利/{number}/{page}")
        Observable<GirlResult> getGirl(@Path("number")int number, @Path("page")int page);
    }

    public interface FreshApi{
        @GET(" ")
        Observable<FreshResult> getFresh(@Query("oxwlxojflwblxbsapi")String api,
                                         @Query("include")String include,
                                         @Query("custom_fields")String custom,
                                         @Query("page")int page);
    }

    public interface FreshContentApi{
        @GET(" ")
        Observable<FreshContent> getFreshContent(@Query("oxwlxojflwblxbsapi")String api ,
                                                 @Query("id")String id,
                                                 @Query("include")String include);
    }


}
