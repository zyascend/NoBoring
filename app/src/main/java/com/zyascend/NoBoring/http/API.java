package com.zyascend.NoBoring.http;

import com.zyascend.NoBoring.bean.CommentResponse;
import com.zyascend.NoBoring.bean.CreateResponse;
import com.zyascend.NoBoring.bean.FollowResponse;
import com.zyascend.NoBoring.bean.ListResponse;
import com.zyascend.NoBoring.bean.LoginResponse;
import com.zyascend.NoBoring.bean.PostResponse;
import com.zyascend.NoBoring.bean.RegisterResponse;
import com.zyascend.NoBoring.bean.UpdateResponse;
import com.zyascend.NoBoring.bean.UserResponse;
import com.zyascend.NoBoring.dao.BudejieVideoResult;
import com.zyascend.NoBoring.dao.FreshContent;
import com.zyascend.NoBoring.dao.FreshResult;
import com.zyascend.NoBoring.dao.GirlResult;

import com.zyascend.NoBoring.dao.TextJokeResult;
import com.zyascend.NoBoring.dao.TuWenJokeResult;
import com.zyascend.NoBoring.dao.WeiXinResult;
import com.zyascend.NoBoring.dao.ZhiHuContent;
import com.zyascend.NoBoring.dao.ZhiHuResult;


import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
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
                                                  @Query("type")String type,
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

    public interface LeanCloudAPI{

        @Headers("Content-Type: application/json")
        @POST("users")
        Observable<RegisterResponse> register(@Body RequestBody body);

        @Headers("Content-Type: application/json")
        @POST("login")
        Observable<LoginResponse> login(@Body RequestBody body);


        @Headers("Cache-Control: public, max-age=60")
        @GET("users/me")
        Observable<UserResponse> getLoginedUser(@Header("X-LC-Session")String sessionToken);


        @Headers("Cache-Control: public, max-age=60")
        @GET("users/{userId}")
        Observable<UserResponse> getUserById(@Path("userId") String newsId);

        //@Headers("Content-Type: application/json")
        @Headers("Cache-Control: public, max-age=60")
        @GET("classes/Post")
        Observable<ListResponse<PostResponse>> getAllPost(@QueryMap Map<String,String> map);

        /**
         * where={"postId":"0s9dds9d8s0ads89d"}
         */
        //@Headers("Content-Type: application/json")
        @Headers("Cache-Control: public, max-age=60")
        @GET("classes/Comment")
        Observable<ListResponse<CommentResponse>> getComments(@QueryMap Map<String,String> idJsonMap);
        /**
         * 提交comment
         * {
         "postId": "59754cbf128fe155ce7334d2",
         "poster": {
         "__type": "Pointer",
         "className": "_User",
         "objectId": "59754d1bfe88c2c1d45b156d"
         },
         "content": "这是一条评论003"
         }
         */
        @Headers("Content-Type: application/json")
        @POST("classes/Comment")
        Observable<CreateResponse> postComment(@Body RequestBody body);


        /**
         * where={"posterId":"0s9dds9d8s0ads89d"}
         */
        //@Headers("Content-Type: application/json")
        @Headers("Cache-Control: public, max-age=60")
        @GET("classes/Post")
        Observable<ListResponse<PostResponse>> getUserPost(@QueryMap Map<String,String> map);

        /**
         * 增加赞数或评论数或关注粉丝数
         *
         * @param objectId
         * @param body {"likesCount":{"__op":"Increment","amount":1}}
         *             {"commentsCount":{"__op":"Increment","amount":1}}
         * @return
         */
        @Headers("Content-Type: application/json")
        @PUT("classes/Post/{objectId}")
        Observable<UpdateResponse> increment(@Path("objectId")String objectId, @Body RequestBody body);

        /**
         * 获取所有粉丝
         *
         */
        @Headers("Cache-Control: public, max-age=60")
        @GET("users/{userId}/followers")
        Observable<ListResponse<FollowResponse>> getFollower(@Path("userId")String userId);

        /**
         * 获取所有关注
         *
         */
        @Headers("Cache-Control: public, max-age=60")
        @GET("users/{userId}/followees")
        Observable<ListResponse<FollowResponse>> getFollowee(@Path("userId")String userId);

        /**
         * 关注其他人
         */
        @Headers("Content-Type: application/json")
        @POST("users/{followerId}/friendship/{followeeId}")
        Observable<String> follow(@Path("followerId")String follower,@Path("followeeId")String followee);

        /**
         * 取关其他人
         */
        @Headers("Content-Type: application/json")
        @DELETE("users/{followerId}/friendship/{followeeId}")
        Observable<String> cancelFollow(@Path("followerId")String follower,@Path("followeeId")String followee);


    }

}
