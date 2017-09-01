package com.zyascend.NoBoring.fragment;

import android.content.Intent;
import android.util.Log;

import com.zyascend.NoBoring.http.API;
import com.zyascend.NoBoring.activity.BaseDetailActivity;
import com.zyascend.NoBoring.adapter.ZhiHuAdapter;
import com.zyascend.NoBoring.base.BaseRecyclerFragment;
import com.zyascend.NoBoring.dao.ZhiHuResult;
import com.zyascend.NoBoring.utils.rxbus.DateEvent;
import com.zyascend.NoBoring.utils.rxbus.NextEvent;
import com.zyascend.NoBoring.utils.rxbus.RxBus;
import com.zyascend.NoBoring.utils.rx.RxTransformer;
import com.zyascend.NoBoring.http.ZhihuService;
import com.zyascend.NoBoring.utils.rxbus.RxBusSubscriber;
import com.zyascend.NoBoring.utils.rxbus.RxSubscriptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 *
 * Created by Administrator on 2016/11/24.
 */

public class ZhihuFragment extends BaseRecyclerFragment<ZhiHuAdapter> {

    private static final String TAG = "TAG_ZhihuFragment";
    private API.ZhiHuApi api;
    private List<ZhiHuResult.Story> mList = new ArrayList<>();

    private int currentDay;
    private int currentMonth;
    private int currentYear;
    private boolean canLoadBefore = false;
    private boolean isLoadBefore;

    @Override
    protected ZhiHuAdapter initAdapter() {
        return new ZhiHuAdapter(getContext());
    }

    @Override
    public void doOnInitViews() {
        super.doOnInitViews();
        api = ZhihuService.getInstance().getAPI();
        setDate();
        adapter.removeAllFooter();
        subscribeEvent();
    }

    @Override
    protected void doOnItemClick(int position) {
        Intent intent = new Intent(getActivity(), BaseDetailActivity.class);
        intent.putExtra(BaseDetailActivity.INTENT_DETAIL_TYPE,2);
        intent.putExtra(BaseDetailActivity.INTENT_ENTITY,mList.get(position));
        startActivity(intent);
    }

    private void setDate() {
        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        currentDay = calendar.get(Calendar.DATE);
        Log.d(TAG, "setDate: day is "+ currentYear +" "+currentMonth +" "+currentDay);
    }

    @Override
    public void onRefresh() {
        loadLastest();
    }

    private void loadLastest() {
        if (api == null)return;
        isLoadBefore = false;
        subscrible(api.getLastest());
    }

    private List<ZhiHuResult.Story> mapResult(ZhiHuResult zhiHuResult) {
        List<ZhiHuResult.Story> items = new ArrayList<>();
        List<ZhiHuResult.TopStory> tops = zhiHuResult.getTop_stories();
        for (ZhiHuResult.Story story : zhiHuResult.getStories()) {
            if (story == null)return null;
            story.setType(0);
            items.add(isTopStory(story,tops));
        }
        return items;
    }

    private ZhiHuResult.Story isTopStory(ZhiHuResult.Story s, List<ZhiHuResult.TopStory> tops) {
        if (tops == null || tops.isEmpty())return s;
        for (ZhiHuResult.TopStory story : tops) {
            if (story != null){
                if (story.getId() == s.getId()){
                    s.setType(1);
                    List<String> url = new ArrayList<>();
                    url.add(story.getImage());
                    s.setImages(url);
                    Log.d(TAG, "isTopStory: -----> true: "+story.getTitle());
                    return s;
                }
            }
        }
        return s;
    }

    @Override
    public void onLoadMore() {
        //do nothing
    }

    private void loadBefore(){
        if (api == null || !canLoadBefore)return;
        String date = String.valueOf(currentYear) +
                currentMonth +
                currentDay;
        isLoadBefore = true;
        subscrible(api.getHistory(date));
    }

    private void subscrible(Observable<ZhiHuResult> observable) {
        observable.compose(RxTransformer.INSTANCE.<ZhiHuResult>transform(lifeCycleSubject))
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        //正在获取前一天的数据，不能短时间内再次请求
                        canLoadBefore = false;
                    }
                })
                .map(new Func1<ZhiHuResult, List<ZhiHuResult.Story>>() {
                    @Override
                    public List<ZhiHuResult.Story> call(ZhiHuResult zhiHuResult) {
                        List<ZhiHuResult.Story> items = null;
                        try {
                            items = mapResult(zhiHuResult);
                        }catch (Exception e){
                            Observable.error(e);
                        }
                        if (items == null){
                            Observable.error(new Exception("List null"));
                        }
                        return items;
                    }
                })
                .subscribe(new Subscriber<List<ZhiHuResult.Story>>() {
                    @Override
                    public void onCompleted() {
                        showLoadingComplete();
                        syncDate();
                        //可以加载下一个
                        canLoadBefore = true;
                    }

                    @Override
                    public void onError(Throwable e) {
                        showError();
                    }

                    @Override
                    public void onNext(List<ZhiHuResult.Story> stories) {
                        mList.clear();
                        mList.addAll(stories);
                        adapter.clear();
                        adapter.addAll(stories);
                    }
                });
    }

    private void syncDate() {
        if (isLoadBefore){

            if (currentDay == 1){
                if (currentMonth == 3)currentDay = 28;
                currentDay = 30;
                if (currentMonth == 1){
                    currentMonth = 12;
                    currentYear-=1;
                }else {
                    currentMonth-= 1;
                }
            }else {
                currentDay -= 1;
            }
        }else {
            //下拉刷新的时候
            setDate();
        }
        syncToolBar();
        Log.d(TAG, "syncDate: ---->day is "+ currentYear +" "+currentMonth +" "+currentDay);
    }

    private void syncToolBar() {
        if (!getUserVisibleHint()){return;}
        String date = String.valueOf(currentYear) +"年"+
                currentMonth +"月"+
                currentDay+"日";
        RxBus.getDefault().postSticky(new DateEvent(date,true));
    }


    private Subscription mNextSub;

    private void subscribeEvent(){
        if (mNextSub != null && !mNextSub.isUnsubscribed()) {
            //清除以前的订阅
            RxSubscriptions.remove(mNextSub);
        } else {
//            NextEvent event = RxBus.getDefault().getStickyEvent(NextEvent.class);
//            Log.i(TAG, "获取到StickyEvent--->" + event);
            mNextSub = RxBus.getDefault().toObservableSticky(NextEvent.class)
                    // 建议在Sticky时,在操作符内主动try,catch
                    .subscribe(new RxBusSubscriber<NextEvent>() {
                        @Override
                        protected void onEvent(NextEvent nextEvent) {
                            if (nextEvent == null)return;
                            if (nextEvent.next == 0){
                                loadBefore();
                            } else {
                                loadLastest();
                            }
                        }
                    });
        }
        RxSubscriptions.add(mNextSub);
    }

    @Override
    public void doOnInvisible() {
        super.doOnInvisible();
        RxBus.getDefault().postSticky(new DateEvent("趣闻",false));
    }

    @Override
    public void doOnVisible() {
        super.doOnVisible();
        syncDate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxBus.getDefault().removeAllStickyEvents();
        RxSubscriptions.remove(mNextSub);
    }

}
