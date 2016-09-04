package com.zyascend.NoBoring.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 * Created by Administrator on 2016/8/20.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper implements DataConstants {

    private static final String DB_NAME = "NoBoring.db";
    private static final int DB_VERSION = 1 ;

    private static final String CREATE_GIRL_TABLE = "CREATE TABLE" + TABLE_GIRL +
            "("
            + KEY_ID + " TEXT PRIMARY KEY,"
            + KEY_TITLE + " TEXT,"
            + KEY_DATE + " TEXT"
            + ")";

    private static final String CREATE_NEWS_TABLE = "CREATE TABLE" + TABLE_NEWS+
            "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TITLE + " TEXT,"
            + KEY_CONTENT_URL + " TEXT,"
            + KEY_AUTHOR + " TEXT,"
            + KEY_TAG + " INTEGER"+")";

    private static final String CREATE_JOKE_TABLE = "CREATE TABLE" + TABLE_JOKE+
            "("
            + KEY_ID + " TEXT PRIMARY KEY,"
            + KEY_TITLE + " TEXT,"
            + KEY_DATE + " TEXT"
            +")";


    public DatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    private static DatabaseOpenHelper sOpenHelper = null;

    public static DatabaseOpenHelper getInstance(Context context) {
        if (sOpenHelper == null) {
            synchronized (DatabaseOpenHelper.class) {
                if (sOpenHelper == null) {
                    sOpenHelper = new DatabaseOpenHelper(context.getApplicationContext());
                }
            }
        }
        return sOpenHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_GIRL_TABLE);
        db.execSQL(CREATE_NEWS_TABLE);
        db.execSQL(CREATE_JOKE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion >= newVersion){
            return;
        }
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_GIRL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_NEWS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_JOKE_TABLE);
        onCreate(db);
    }
}
