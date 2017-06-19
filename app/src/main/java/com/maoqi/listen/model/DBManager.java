package com.maoqi.listen.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.maoqi.listen.Constant;
import com.maoqi.listen.ListenApplication;
import com.maoqi.listen.model.bean.BaseSongBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maoqi on 2017/6/19.
 */

public class DBManager {
    private Context context;
    private static DBManager manager;
    private DBHelper helper;
    private SQLiteDatabase db;

    private DBManager(Context context) {
        checkDBNull();
        this.context = context;
    }

    public static DBManager getInstance(Context context) {
        if (manager == null) {
            manager = new DBManager(context);
        }
        return manager;
    }

    public void insertSong(List<BaseSongBean> list) {
        for (BaseSongBean bean : list) {
            insertSong(bean);
        }
    }


    public void insertSong(BaseSongBean bean) {
        checkDBNull();
        ContentValues values = new ContentValues();
        values.put(Constant.SONG_URL, bean.getSongUrl());
        values.put(Constant.SONG_TITLE, bean.getSongTitle());
        values.put(Constant.SONG_ARTIST, bean.getSongArtist());
        values.put(Constant.SONG_ALBUM, bean.getSongAlbum());
        values.put(Constant.SONG_IMG_URL, bean.getSongImgUrl());
        values.put(Constant.LYRIC_URL, bean.getLyricUrl());
        values.put(Constant.SONG_ID, bean.getSongId());
        values.put(Constant.IS_COLLECT, bean.getCollect());
        db.replace(Constant.TABLE_PLAY_LIST, null, values);
    }

    public List<BaseSongBean> getSongList() {
        checkDBNull();
        List<BaseSongBean> list = new ArrayList();
        Cursor c = db.query(Constant.TABLE_PLAY_LIST, null, null, null, null, null, null);
        BaseSongBean bean = new BaseSongBean();
        if (c != null && c.moveToFirst()) {
            do {
//                String songUrl = c.getString(c.getColumnIndex(Constant.SONG_URL));
//                String songTitle = c.getString(c.getColumnIndex(Constant.SONG_TITLE));
//                String songArtist = c.getString(c.getColumnIndex(Constant.SONG_ARTIST));
//                String songAlbum = c.getString(c.getColumnIndex(Constant.SONG_ALBUM));
//                String songImgUrl = c.getString(c.getColumnIndex(Constant.SONG_IMG_URL));
//                String lyricUrl = c.getString(c.getColumnIndex(Constant.LYRIC_URL));

                bean.setId(c.getInt(c.getColumnIndex("_id")));
                bean.setSongUrl(c.getString(c.getColumnIndex(Constant.SONG_URL)));
                bean.setSongTitle(c.getString(c.getColumnIndex(Constant.SONG_TITLE)));
                bean.setSongArtist(c.getString(c.getColumnIndex(Constant.SONG_ARTIST)));
                bean.setSongAlbum(c.getString(c.getColumnIndex(Constant.SONG_ALBUM)));
                bean.setSongImgUrl(c.getString(c.getColumnIndex(Constant.SONG_IMG_URL)));
                bean.setLyricUrl(c.getString(c.getColumnIndex(Constant.LYRIC_URL)));
                bean.setSongId(c.getString(c.getColumnIndex(Constant.SONG_ID)));
                bean.setCollect(c.getString(c.getColumnIndex(Constant.IS_COLLECT)).equals("1"));
                list.add(bean);
            } while (c.moveToNext());
        }
        return list;
    }

    public void deleteSong(BaseSongBean bean) {
        checkDBNull();
        db.delete(Constant.TABLE_PLAY_LIST, Constant.SONG_ID + " = ?", new String[]{bean.getSongId()});
    }

    public void deleteSong(List<BaseSongBean> list) {
        for (BaseSongBean bean : list) {
            deleteSong(bean);
        }
    }

    public void updateCollect(BaseSongBean bean) {
        checkDBNull();
        ContentValues values = new ContentValues();
        values.put(Constant.IS_COLLECT, bean.getCollect());
        db.update(Constant.TABLE_PLAY_LIST, values, Constant.SONG_ID + " = ?", new String[]{bean.getSongId()});
    }

    public void updateCollect(List<BaseSongBean> list) {
        for (BaseSongBean bean : list) {
            updateCollect(bean);
        }
    }

    public boolean isCollect(BaseSongBean bean) {
        checkDBNull();
        Cursor c = db.query(Constant.TABLE_PLAY_LIST, new String[]{Constant.IS_COLLECT}, Constant.SONG_ID + " = ?", new String[]{bean.getSongId()}, null, null, null);
        if (c != null && c.moveToFirst()) {
            do {
                boolean b =c.getString(c.getColumnIndex(Constant.IS_COLLECT)).equals("1");
                return b;
            }while (c.moveToNext());
        }
        return false;
    }


    private void checkDBNull() {
        if (db == null) {
            if (helper == null) {
                helper = new DBHelper(ListenApplication.appContext);
            }
            db = helper.getWritableDatabase();
        }
    }


}
