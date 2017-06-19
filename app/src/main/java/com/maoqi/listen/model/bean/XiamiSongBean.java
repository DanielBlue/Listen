package com.maoqi.listen.model.bean;

import java.util.List;

/**
 * Created by maoqi on 2017/6/15.
 */

public class XiamiSongBean {

    /**
     * album_id : 2928
     * album_logo : http://pic.xiami.net/images/album/img48/648/29281465703800_1.jpg
     * album_name : 美丽的一天
     * artist_id : 648
     * artist_logo : http://pic.xiami.net/images/artistlogo/37/14230160708537_1.jpg
     * artist_name : 刘德华
     * demo : 0
     * is_play : 0
     * listen_file : http://om5.alicdn.com/648/648/2928/375097_1496218955976.mp3?auth_key=b4a8a5b5a2303ed4de4e282c2b4525f6-1498100400-0-null
     * lyric : http://img.xiami.net/lyric/97/375097_1452354991_9421.trc
     * need_pay_flag : 0
     * play_counts : 0
     * purview_roles : [{"operation_list":[{"purpose":1,"upgrade_role":0},{"purpose":2,"upgrade_role":0}],"quality":"e"},{"operation_list":[{"purpose":1,"upgrade_role":0},{"purpose":2,"upgrade_role":0}],"quality":"f"},{"operation_list":[{"purpose":1,"upgrade_role":0},{"purpose":2,"upgrade_role":0}],"quality":"l"},{"operation_list":[{"purpose":1,"upgrade_role":0},{"purpose":2,"upgrade_role":0}],"quality":"h"},{"operation_list":[{"purpose":1,"upgrade_role":3},{"purpose":2,"upgrade_role":3}],"quality":"s"}]
     * singer :
     * song_id : 375097
     * song_name : 练习
     */

    private int album_id;
    private String album_logo;
    private String album_name;
    private int artist_id;
    private String artist_logo;
    private String artist_name;
    private int demo;
    private int is_play;
    private String listen_file;
    private String lyric;
    private int need_pay_flag;
    private int play_counts;
    private String singer;
    private int song_id;
    private String song_name;
    private List<PurviewRolesBean> purview_roles;

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_logo() {
        return album_logo;
    }

    public void setAlbum_logo(String album_logo) {
        this.album_logo = album_logo;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public int getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(int artist_id) {
        this.artist_id = artist_id;
    }

    public String getArtist_logo() {
        return artist_logo;
    }

    public void setArtist_logo(String artist_logo) {
        this.artist_logo = artist_logo;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public int getDemo() {
        return demo;
    }

    public void setDemo(int demo) {
        this.demo = demo;
    }

    public int getIs_play() {
        return is_play;
    }

    public void setIs_play(int is_play) {
        this.is_play = is_play;
    }

    public String getListen_file() {
        return listen_file;
    }

    public void setListen_file(String listen_file) {
        this.listen_file = listen_file;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public int getNeed_pay_flag() {
        return need_pay_flag;
    }

    public void setNeed_pay_flag(int need_pay_flag) {
        this.need_pay_flag = need_pay_flag;
    }

    public int getPlay_counts() {
        return play_counts;
    }

    public void setPlay_counts(int play_counts) {
        this.play_counts = play_counts;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public int getSong_id() {
        return song_id;
    }

    public void setSong_id(int song_id) {
        this.song_id = song_id;
    }

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    public List<PurviewRolesBean> getPurview_roles() {
        return purview_roles;
    }

    public void setPurview_roles(List<PurviewRolesBean> purview_roles) {
        this.purview_roles = purview_roles;
    }

    public static class PurviewRolesBean {
        /**
         * operation_list : [{"purpose":1,"upgrade_role":0},{"purpose":2,"upgrade_role":0}]
         * quality : e
         */

        private String quality;
        private List<OperationListBean> operation_list;

        public String getQuality() {
            return quality;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }

        public List<OperationListBean> getOperation_list() {
            return operation_list;
        }

        public void setOperation_list(List<OperationListBean> operation_list) {
            this.operation_list = operation_list;
        }

        public static class OperationListBean {
            /**
             * purpose : 1
             * upgrade_role : 0
             */

            private int purpose;
            private int upgrade_role;

            public int getPurpose() {
                return purpose;
            }

            public void setPurpose(int purpose) {
                this.purpose = purpose;
            }

            public int getUpgrade_role() {
                return upgrade_role;
            }

            public void setUpgrade_role(int upgrade_role) {
                this.upgrade_role = upgrade_role;
            }
        }
    }
}
