package com.maoqi.listen.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.maoqi.listen.Constant;

/**
 * Created by maoqi on 2017/6/19.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "song";
    private static final int DB_VERSION = 1;
    String TABLE_PLAY_LIST = "CREATE TABLE IF NOT EXISTS " + Constant.TABLE_PLAY_LIST + "("
            + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Constant.SONG_URL + " TEXT NOT NULL,"
            + Constant.SONG_TITLE + " TEXT NOT NULL,"
            + Constant.SONG_ARTIST + " TEXT NOT NULL,"
            + Constant.SONG_ALBUM + " TEXT NOT NULL,"
            + Constant.SONG_IMG_URL + " TEXT NOT NULL,"
            + Constant.SONG_ID + " TEXT NOT NULL UNIQUE,"
            + Constant.LYRIC_URL + " TEXT,"
            + Constant.IS_COLLECT + "TEXT,"
            + Constant.RESERVED_1 + " TEXT,"
            + Constant.RESERVED_2 + " TEXT,"
            + Constant.RESERVED_3 + " TEXT,"
            + Constant.RESERVED_4 + " TEXT,"
            + Constant.RESERVED_5 + " TEXT,"
            + Constant.RESERVED_6 + " TEXT);";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_PLAY_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
