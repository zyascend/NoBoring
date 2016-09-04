package com.zyascend.NoBoring.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zyascend.NoBoring.model.GirlResult;
import com.zyascend.NoBoring.model.Item;
import com.zyascend.NoBoring.model.TextJokeResult;

import java.util.ArrayList;

/**
 *
 * Created by Administrator on 2016/8/20.
 */
public class Database implements DataConstants {

    private static final String TAG = "TAG_Database";
    private SQLiteDatabase mDb;

    public Database(Context context) {
        mDb = DatabaseOpenHelper.getInstance(context).getWritableDatabase();
    }

    private static Database sDao = null;

    public static Database getInstance(Context context) {
        if (sDao == null) {
            synchronized (Database.class) {
                if (sDao == null) {
                    sDao = new Database(context);
                }
            }
        }
        return sDao;
    }

    public void addGirls(GirlResult.Girl girl){
        mDb.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(KEY_ID ,girl.url);
            cv.put(KEY_TITLE ,girl.desc);
            cv.put(KEY_DATE, girl.createdAt);
            if (isExistInTable(girl.url,TABLE_GIRL)) {
                mDb.update(TABLE_GIRL, cv, KEY_ID + " = ?", new String[]{girl.url});
            } else {
                mDb.insert(TABLE_GIRL, null, cv);
            }
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
    }

    public void addNews(Item item){

        mDb.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(KEY_ID ,item.getId());
            cv.put(KEY_TITLE ,item.getTitle());
            cv.put(KEY_CONTENT_URL, item.getUrl());
            Log.d(TAG, "addNews: url = "+ item.getUrl());
            cv.put(KEY_AUTHOR,item.getAuthor());
            cv.put(KEY_TAG,item.getTag());
            if (isExistInTable(String.valueOf(item.getId()),TABLE_NEWS)) {
                mDb.update(TABLE_NEWS, cv, KEY_ID + " = ?", new String[]{String.valueOf(item.getId())});
                Log.d(TAG, "addNews: update !!!!!");
            } else {
                mDb.insert(TABLE_NEWS, null, cv);
            }
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }

    }


    public void addJokes(TextJokeResult.ShowapiResBodyBean.TextJoke joke){

        mDb.beginTransaction();

        try {
            ContentValues cv = new ContentValues();
            cv.put(KEY_ID ,joke.getId());
            cv.put(KEY_TITLE ,joke.getText());
            cv.put(KEY_DATE,joke.getCt());
            if (isExistInTable(joke.getId(),TABLE_JOKE)) {
                mDb.update(TABLE_JOKE, cv, KEY_ID + " = ?", new String[]{joke.getId()});
            } else {
                mDb.insert(TABLE_JOKE, null, cv);
            }
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
    }

    public ArrayList<GirlResult.Girl> getAllGirls(){
        ArrayList<GirlResult.Girl> girls = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_GIRL;

        Cursor cursor = mDb.rawQuery(selectQuery,null);
        if (cursor != null){
            if(cursor.moveToFirst()){
                do{

                    GirlResult.Girl girl = new GirlResult.Girl();
                    girl.url = cursor.getString(0);
                    girl.desc = cursor.getColumnName(1);
                    girl.createdAt = cursor.getString(2);
                    girls.add(girl);

                } while (cursor.moveToNext());

            }
            cursor.close();
        }
        return girls;
    }

    public GirlResult.Girl getGirl(String url){
        GirlResult.Girl girl = new GirlResult.Girl();
        Cursor cursor = mDb.query(TABLE_GIRL
                , new String[]{
                KEY_ID,KEY_TITLE, KEY_DATE}
                ,KEY_ID+ "=?"
                ,new String[]{url},null,null,null,null );
        if (cursor != null && cursor.moveToFirst()){

            girl.url = cursor.getString(0);
            girl.createdAt = cursor.getString(2);
            girl.desc = cursor.getString(1);

            cursor.close();
        }else {
            return null;
        }
        return girl;
    }

    public ArrayList<Item> getAllNews(){
        ArrayList<Item> items = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NEWS;

        Cursor cursor = mDb.rawQuery(selectQuery,null);
        if (cursor != null){
            if(cursor.moveToFirst()){
                do{

                    int id = cursor.getInt(0);
                    String title = cursor.getString(1);
                    String url = cursor.getString(2);
                    Log.d(TAG, "getAllNews: url = " + url);
                    String author = cursor.getString(3);
                    String tag = cursor.getString(4);

                    Item item = new Item(tag,id,author,title,"",url);
                    items.add(item);

                } while (cursor.moveToNext());

            }
            cursor.close();
        }
        return items;
    }

    public ArrayList<TextJokeResult.ShowapiResBodyBean.TextJoke> getAllJokes(){
        ArrayList<TextJokeResult.ShowapiResBodyBean.TextJoke> jokes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_JOKE;

        Cursor cursor = mDb.rawQuery(selectQuery,null);
        if (cursor != null){
            if(cursor.moveToFirst()){
                do{

                    String id = cursor.getString(0);
                    String content = cursor.getColumnName(1);
                    String date = cursor.getString(2);

                    TextJokeResult.ShowapiResBodyBean.TextJoke joke =
                            new TextJokeResult.ShowapiResBodyBean.TextJoke(id,content,date);
                    jokes.add(joke);

                } while (cursor.moveToNext());

            }
            cursor.close();
        }
        return jokes;
    }


    public void delete(String TABLE){
        if ("ALL".equals(TABLE)){

            mDb.execSQL("DELETE FROM " + TABLE_GIRL);
            mDb.execSQL("DELETE FROM " + TABLE_NEWS);
            mDb.execSQL("DELETE FROM " + TABLE_JOKE);
            Log.d(TAG, "已删除全部TABLE");

        }else {

            mDb.execSQL("DELETE FROM " + TABLE);
            Log.d(TAG, "已删除： " + TABLE);
        }
    }

    private boolean isExistInTable(String id,String TABLE) {

        boolean isExist = false;
        Cursor cursor = mDb.query(TABLE,null,KEY_ID + " = ?"
                ,new String[]{id},null,null,null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                isExist = true;
            }
            cursor.close();
        }

        return isExist;
    }

}
