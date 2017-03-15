package com.zyascend.NoBoring.utils;

import android.content.Context;
import android.util.Log;

import com.zyascend.NoBoring.base.BaseApplication;
import com.zyascend.NoBoring.dao.BudejieVideo;
import com.zyascend.NoBoring.dao.BudejieVideoDao;
import com.zyascend.NoBoring.dao.DaoMaster;
import com.zyascend.NoBoring.dao.DaoSession;
import com.zyascend.NoBoring.dao.Girl;
import com.zyascend.NoBoring.dao.GirlResult;
import com.zyascend.NoBoring.dao.Fresh;
import com.zyascend.NoBoring.dao.TextJoke;
import com.zyascend.NoBoring.dao.TextJokeDao;
import com.zyascend.NoBoring.dao.TuWenJoke;
import com.zyascend.NoBoring.dao.WeiXin;
import com.zyascend.NoBoring.dao.WeiXinResult;

import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import java.util.List;

/**
 *
 * Created by Administrator on 2016/11/26.
 */

public class DaoUtils {

    private static final String TAG = "TAG_DataUtils";
    private static final String DEFAULT_URL = " http://ww2.sinaimg.cn/large/610dc034jw1fa8n634v0vj20u00qx0x4.jpg";
    private static DaoUtils dataUtils;
    private static DaoSession daoSession;

    private DaoUtils(){
        Context context = BaseApplication.context;
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "NoBoring.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        daoSession = daoMaster.newSession();
    }

    public static DaoUtils getInstance(){
        if (dataUtils == null){
            synchronized (DaoUtils.class){
                if (dataUtils == null){
                    dataUtils = new DaoUtils();
                }
            }
        }
        return dataUtils;
    }


    public List<BudejieVideo> getBudejieVideos(int tag) {
        BudejieVideoDao dao = daoSession.getBudejieVideoDao();
        return dao.queryBuilder().where(BudejieVideoDao.Properties.Tag.eq(tag)).build().list();
    }

    public List<TextJoke> getTextJokes() {
        TextJokeDao dao = daoSession.getTextJokeDao();
        return dao.loadAll();
    }

    public List<Fresh> getFreshs() {
        return daoSession.getFreshDao().loadAll();
    }

    public List<WeiXin> getWeiXins() {
        return daoSession.getWeiXinDao().loadAll();
    }

    public List<TuWenJoke> getTuWenJokes() {
        return daoSession.getTuWenJokeDao().loadAll();
    }

    public List<Girl> getGirls() {
        return daoSession.getGirlDao().loadAll();
    }


    private boolean isNullOrEmpty(List data) {
        return data == null || data.isEmpty();
    }

    public void saveVideo(List<BudejieVideo> data) {
        if (isNullOrEmpty(data))return;
        daoSession.getBudejieVideoDao().insertOrReplaceInTx(data);
    }

    public void saveFresh(List<Fresh> data) {
        if (isNullOrEmpty(data))return;
        daoSession.getFreshDao().insertOrReplaceInTx(data);
    }

    public void saveGirls(List<Girl> data) {
        if (isNullOrEmpty(data))return;
        Log.d(TAG, "saveGirls: "+data.get(2).getUrl());
        SPUtils.putString(data.get(0).getUrl(),SPUtils.ENTRANCE_PIC);
        daoSession.getGirlDao().insertOrReplaceInTx(data);
    }

    public void saveTextJokes(List<TextJoke> data) {
        if (isNullOrEmpty(data))return;
        daoSession.getTextJokeDao().insertOrReplaceInTx(data);
    }

    public void saveTuWenJokes(List<TuWenJoke> data) {
        if (isNullOrEmpty(data))return;
        daoSession.getTuWenJokeDao().insertOrReplaceInTx(data);
    }

    public void saveWeiXin(List<WeiXin> data) {
        if (isNullOrEmpty(data))return;
        daoSession.getWeiXinDao().insertOrReplaceInTx(data);
    }

    public String getEntranceUrl(){
        String url = DEFAULT_URL;
        try {
//            List<Girl> girls = daoSession.getGirlDao().loadAll();
//            if (girls!= null && !girls.isEmpty()){
//                url = girls.get(0).getUrl();
//                Log.d(TAG, "getEntranceUrl: "+girls.get(0).getDesc());
//            }
            url = SPUtils.getString(SPUtils.ENTRANCE_PIC,DEFAULT_URL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return url;
    }
}
