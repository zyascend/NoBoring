package com.zyascend.NoBoring.utils.map;

import android.util.Log;

import com.zyascend.NoBoring.model.FreshResult;
import com.zyascend.NoBoring.model.Item;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 *
 * Created by Administrator on 2016/8/16.
 */
public class MapFresh implements Func1<FreshResult,List<Item>> {

    private static final String TAG = "TAG_MapFresh";
    private static MapFresh INSTANCE = new MapFresh();

    private MapFresh(){}

    public static MapFresh getInstance(){
        Log.d(TAG, "getInstance: ");
        return INSTANCE;
    }

    @Override
    public List<Item> call(FreshResult freshResult) {
        List<Item> items = new ArrayList<>();
        for (FreshResult.PostsBean post : freshResult.getPosts()){
            Item item = new Item(post.getTags().get(0).getTitle()
                    , post.getId()
                    , post.getAuthor().getName()
                    , post.getTitle()
                    , post.getUrl()
                    , post.getCustom_fields().getThumb_c().get(0));
            items.add(item);
        }
        return items;
    }
}
