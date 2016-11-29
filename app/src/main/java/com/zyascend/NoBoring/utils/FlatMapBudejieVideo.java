package com.zyascend.NoBoring.utils;

import android.text.TextUtils;
import android.util.Log;

import com.zyascend.NoBoring.base.BaseFlatMap;
import com.zyascend.NoBoring.dao.BudejieVideo;
import com.zyascend.NoBoring.dao.BudejieVideoResult;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 *
 * Created by Administrator on 2016/11/25.
 */

public class FlatMapBudejieVideo extends BaseFlatMap<BudejieVideoResult,List<BudejieVideo>>{

    private static final String TAG = "TAG_FlatMapBudejieVideo";
    private static int mTag;
    private static FlatMapBudejieVideo INSTANCE;

    private FlatMapBudejieVideo(int tag){
        this.mTag = tag;
    }

    public static FlatMapBudejieVideo getInstance(int tag){
        if (INSTANCE == null || tag != mTag){
            INSTANCE = new FlatMapBudejieVideo(tag);
        }
        return INSTANCE;
    }


    @Override
    protected List<BudejieVideo> getFromCache() {
        return DaoUtils.getInstance().getBudejieVideos(mTag);
    }

    @Override
    protected Observable<List<BudejieVideo>> doForMap(BudejieVideoResult budejieVideoResult) {

        List<BudejieVideo> budejieVideos = new ArrayList<>();
        try {
            for (BudejieVideoResult.ListBean list : budejieVideoResult.getList()) {

                if (TextUtils.equals(list.getType(),"gif")){
                    continue;
                }
                BudejieVideo video = new BudejieVideo(
                        Long.parseLong(list.getId())
                        ,list.getShare_url()
                        ,list.getVideo().getThumbnail().get(0)
                        ,list.getText()
                        ,list.getVideo().getVideo().get(0)
                        ,mTag
                        ,String.valueOf(budejieVideoResult.getInfo().getNp()));
                budejieVideos.add(video);
            }
        }catch (Exception e){
            return Observable.error(e);
        }
        return createObservable(budejieVideos);
    }

    @Override
    protected void saveToCache(List<BudejieVideo> data) {
        DaoUtils.getInstance().saveVideo(data);
    }

}
