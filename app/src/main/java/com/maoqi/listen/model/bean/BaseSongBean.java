package com.maoqi.listen.model.bean;

/**
 * Created by maoqi on 2017/6/19.
 */

public class BaseSongBean {

    int id;
    String songUrl;
    String songTitle;
    String songArtist;
    String songAlbum;
    String songImgUrl;
    String lyricUrl;
    String songId;
    boolean isCollect;


    public BaseSongBean() {
    }

    public BaseSongBean(String songUrl, String songTitle, String songArtist, String songAlbum, String songImgUrl, String lyricUrl,String songId) {
        this.songUrl = songUrl;
        this.songTitle = songTitle;
        this.songArtist = songArtist;
        this.songAlbum = songAlbum;
        this.songImgUrl = songImgUrl;
        this.lyricUrl = lyricUrl;
        this.songId = songId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
    }

    public String getSongAlbum() {
        return songAlbum;
    }

    public void setSongAlbum(String songAlbum) {
        this.songAlbum = songAlbum;
    }

    public String getSongImgUrl() {
        return songImgUrl;
    }

    public void setSongImgUrl(String songImgUrl) {
        this.songImgUrl = songImgUrl;
    }

    public String getLyricUrl() {
        return lyricUrl;
    }

    public void setLyricUrl(String lyricUrl) {
        this.lyricUrl = lyricUrl;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public boolean getCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }
}
