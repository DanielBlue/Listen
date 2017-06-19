package com.maoqi.listen.model.bean;

import java.util.List;

/**
 * Created by maoqi on 2017/6/15.
 */

public class CloudSongBean {

    /**
     * album : {"alias":[],"artist":{"albumSize":0,"alias":[],"briefDesc":"","id":0,"img1v1Id":0,"img1v1Url":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","musicSize":0,"name":"","picId":0,"picUrl":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","trans":""},"artists":[{"albumSize":0,"alias":[],"briefDesc":"","id":3691,"img1v1Id":0,"img1v1Url":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","musicSize":0,"name":"刘德华","picId":0,"picUrl":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","trans":""}],"blurPicUrl":"http://p1.music.126.net/PjUxrAcmgsN_iH7sjr8NhA==/5649290743593503.jpg","briefDesc":"","commentThreadId":"R_AL_3_2350632","company":"东亚唱片","companyId":0,"copyrightId":0,"description":"","id":2350632,"name":"Unforgettable Concert 中国巡回演唱会2011","pic":5649290743593503,"picId":5649290743593503,"picUrl":"http://p1.music.126.net/PjUxrAcmgsN_iH7sjr8NhA==/5649290743593503.jpg","publishTime":1328803200007,"size":29,"songs":[],"status":1,"tags":"","type":"专辑"}
     * alias : []
     * artists : [{"albumSize":0,"alias":[],"briefDesc":"","id":3691,"img1v1Id":0,"img1v1Url":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","musicSize":0,"name":"刘德华","picId":0,"picUrl":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","trans":""}]
     * bMusic : {"bitrate":96000,"dfsId":7699879931772758,"extension":"mp3","id":104029582,"playTime":379534,"size":4555381,"sr":44100,"volumeDelta":0.267569}
     * commentThreadId : R_SO_4_34532800
     * copyFrom :
     * copyright : 0
     * copyrightId : 0
     * dayPlays : 0
     * disc : 2
     * duration : 379534
     * fee : 0
     * ftype : 0
     * hMusic : {"bitrate":320000,"dfsId":3346913395615570,"extension":"mp3","id":104029580,"playTime":379534,"size":15184500,"sr":44100,"volumeDelta":0.0758784}
     * hearTime : 0
     * id : 34532800
     * lMusic : {"bitrate":96000,"dfsId":7699879931772758,"extension":"mp3","id":104029582,"playTime":379534,"size":4555381,"sr":44100,"volumeDelta":0.267569}
     * mMusic : {"bitrate":160000,"dfsId":3375500697931298,"extension":"mp3","id":104029581,"playTime":379534,"size":7592272,"sr":44100,"volumeDelta":0.468341}
     * mp3Url : http://m2.music.126.net/-9zyprkqZpv3zjLn_LuRWg==/7699879931772758.mp3
     * mvid : 0
     * name : 冰雨
     * no : 2
     * playedNum : 0
     * popularity : 100.0
     * position : 0
     * rtUrls : []
     * rtype : 0
     * score : 100
     * starred : false
     * starredNum : 0
     * status : 0
     */

    private AlbumBean album;
    private BMusicBean bMusic;
    private String commentThreadId;
    private String copyFrom;
    private int copyright;
    private int copyrightId;
    private int dayPlays;
    private String disc;
    private int duration;
    private int fee;
    private int ftype;
    private HMusicBean hMusic;
    private int hearTime;
    private int id;
    private LMusicBean lMusic;
    private MMusicBean mMusic;
    private String mp3Url;
    private int mvid;
    private String name;
    private int no;
    private int playedNum;
    private double popularity;
    private int position;
    private int rtype;
    private int score;
    private boolean starred;
    private int starredNum;
    private int status;
    private List<?> alias;
    private List<ArtistsBeanX> artists;
    private List<?> rtUrls;

    public AlbumBean getAlbum() {
        return album;
    }

    public void setAlbum(AlbumBean album) {
        this.album = album;
    }

    public BMusicBean getBMusic() {
        return bMusic;
    }

    public void setBMusic(BMusicBean bMusic) {
        this.bMusic = bMusic;
    }

    public String getCommentThreadId() {
        return commentThreadId;
    }

    public void setCommentThreadId(String commentThreadId) {
        this.commentThreadId = commentThreadId;
    }

    public String getCopyFrom() {
        return copyFrom;
    }

    public void setCopyFrom(String copyFrom) {
        this.copyFrom = copyFrom;
    }

    public int getCopyright() {
        return copyright;
    }

    public void setCopyright(int copyright) {
        this.copyright = copyright;
    }

    public int getCopyrightId() {
        return copyrightId;
    }

    public void setCopyrightId(int copyrightId) {
        this.copyrightId = copyrightId;
    }

    public int getDayPlays() {
        return dayPlays;
    }

    public void setDayPlays(int dayPlays) {
        this.dayPlays = dayPlays;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getFtype() {
        return ftype;
    }

    public void setFtype(int ftype) {
        this.ftype = ftype;
    }

    public HMusicBean getHMusic() {
        return hMusic;
    }

    public void setHMusic(HMusicBean hMusic) {
        this.hMusic = hMusic;
    }

    public int getHearTime() {
        return hearTime;
    }

    public void setHearTime(int hearTime) {
        this.hearTime = hearTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LMusicBean getLMusic() {
        return lMusic;
    }

    public void setLMusic(LMusicBean lMusic) {
        this.lMusic = lMusic;
    }

    public MMusicBean getMMusic() {
        return mMusic;
    }

    public void setMMusic(MMusicBean mMusic) {
        this.mMusic = mMusic;
    }

    public String getMp3Url() {
        return mp3Url;
    }

    public void setMp3Url(String mp3Url) {
        this.mp3Url = mp3Url;
    }

    public int getMvid() {
        return mvid;
    }

    public void setMvid(int mvid) {
        this.mvid = mvid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getPlayedNum() {
        return playedNum;
    }

    public void setPlayedNum(int playedNum) {
        this.playedNum = playedNum;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getRtype() {
        return rtype;
    }

    public void setRtype(int rtype) {
        this.rtype = rtype;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public int getStarredNum() {
        return starredNum;
    }

    public void setStarredNum(int starredNum) {
        this.starredNum = starredNum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<?> getAlias() {
        return alias;
    }

    public void setAlias(List<?> alias) {
        this.alias = alias;
    }

    public List<ArtistsBeanX> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistsBeanX> artists) {
        this.artists = artists;
    }

    public List<?> getRtUrls() {
        return rtUrls;
    }

    public void setRtUrls(List<?> rtUrls) {
        this.rtUrls = rtUrls;
    }

    public static class AlbumBean {
        /**
         * alias : []
         * artist : {"albumSize":0,"alias":[],"briefDesc":"","id":0,"img1v1Id":0,"img1v1Url":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","musicSize":0,"name":"","picId":0,"picUrl":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","trans":""}
         * artists : [{"albumSize":0,"alias":[],"briefDesc":"","id":3691,"img1v1Id":0,"img1v1Url":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","musicSize":0,"name":"刘德华","picId":0,"picUrl":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","trans":""}]
         * blurPicUrl : http://p1.music.126.net/PjUxrAcmgsN_iH7sjr8NhA==/5649290743593503.jpg
         * briefDesc :
         * commentThreadId : R_AL_3_2350632
         * company : 东亚唱片
         * companyId : 0
         * copyrightId : 0
         * description :
         * id : 2350632
         * name : Unforgettable Concert 中国巡回演唱会2011
         * pic : 5649290743593503
         * picId : 5649290743593503
         * picUrl : http://p1.music.126.net/PjUxrAcmgsN_iH7sjr8NhA==/5649290743593503.jpg
         * publishTime : 1328803200007
         * size : 29
         * songs : []
         * status : 1
         * tags :
         * type : 专辑
         */

        private ArtistBean artist;
        private String blurPicUrl;
        private String briefDesc;
        private String commentThreadId;
        private String company;
        private int companyId;
        private int copyrightId;
        private String description;
        private int id;
        private String name;
        private long pic;
        private long picId;
        private String picUrl;
        private long publishTime;
        private int size;
        private int status;
        private String tags;
        private String type;
        private List<?> alias;
        private List<ArtistsBean> artists;
        private List<?> songs;

        public ArtistBean getArtist() {
            return artist;
        }

        public void setArtist(ArtistBean artist) {
            this.artist = artist;
        }

        public String getBlurPicUrl() {
            return blurPicUrl;
        }

        public void setBlurPicUrl(String blurPicUrl) {
            this.blurPicUrl = blurPicUrl;
        }

        public String getBriefDesc() {
            return briefDesc;
        }

        public void setBriefDesc(String briefDesc) {
            this.briefDesc = briefDesc;
        }

        public String getCommentThreadId() {
            return commentThreadId;
        }

        public void setCommentThreadId(String commentThreadId) {
            this.commentThreadId = commentThreadId;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public int getCompanyId() {
            return companyId;
        }

        public void setCompanyId(int companyId) {
            this.companyId = companyId;
        }

        public int getCopyrightId() {
            return copyrightId;
        }

        public void setCopyrightId(int copyrightId) {
            this.copyrightId = copyrightId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getPic() {
            return pic;
        }

        public void setPic(long pic) {
            this.pic = pic;
        }

        public long getPicId() {
            return picId;
        }

        public void setPicId(long picId) {
            this.picId = picId;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public long getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(long publishTime) {
            this.publishTime = publishTime;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<?> getAlias() {
            return alias;
        }

        public void setAlias(List<?> alias) {
            this.alias = alias;
        }

        public List<ArtistsBean> getArtists() {
            return artists;
        }

        public void setArtists(List<ArtistsBean> artists) {
            this.artists = artists;
        }

        public List<?> getSongs() {
            return songs;
        }

        public void setSongs(List<?> songs) {
            this.songs = songs;
        }

        public static class ArtistBean {
            /**
             * albumSize : 0
             * alias : []
             * briefDesc :
             * id : 0
             * img1v1Id : 0
             * img1v1Url : http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
             * musicSize : 0
             * name :
             * picId : 0
             * picUrl : http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
             * trans :
             */

            private int albumSize;
            private String briefDesc;
            private int id;
            private int img1v1Id;
            private String img1v1Url;
            private int musicSize;
            private String name;
            private int picId;
            private String picUrl;
            private String trans;
            private List<?> alias;

            public int getAlbumSize() {
                return albumSize;
            }

            public void setAlbumSize(int albumSize) {
                this.albumSize = albumSize;
            }

            public String getBriefDesc() {
                return briefDesc;
            }

            public void setBriefDesc(String briefDesc) {
                this.briefDesc = briefDesc;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getImg1v1Id() {
                return img1v1Id;
            }

            public void setImg1v1Id(int img1v1Id) {
                this.img1v1Id = img1v1Id;
            }

            public String getImg1v1Url() {
                return img1v1Url;
            }

            public void setImg1v1Url(String img1v1Url) {
                this.img1v1Url = img1v1Url;
            }

            public int getMusicSize() {
                return musicSize;
            }

            public void setMusicSize(int musicSize) {
                this.musicSize = musicSize;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getPicId() {
                return picId;
            }

            public void setPicId(int picId) {
                this.picId = picId;
            }

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public String getTrans() {
                return trans;
            }

            public void setTrans(String trans) {
                this.trans = trans;
            }

            public List<?> getAlias() {
                return alias;
            }

            public void setAlias(List<?> alias) {
                this.alias = alias;
            }
        }

        public static class ArtistsBean {
            /**
             * albumSize : 0
             * alias : []
             * briefDesc :
             * id : 3691
             * img1v1Id : 0
             * img1v1Url : http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
             * musicSize : 0
             * name : 刘德华
             * picId : 0
             * picUrl : http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
             * trans :
             */

            private int albumSize;
            private String briefDesc;
            private int id;
            private int img1v1Id;
            private String img1v1Url;
            private int musicSize;
            private String name;
            private int picId;
            private String picUrl;
            private String trans;
            private List<?> alias;

            public int getAlbumSize() {
                return albumSize;
            }

            public void setAlbumSize(int albumSize) {
                this.albumSize = albumSize;
            }

            public String getBriefDesc() {
                return briefDesc;
            }

            public void setBriefDesc(String briefDesc) {
                this.briefDesc = briefDesc;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getImg1v1Id() {
                return img1v1Id;
            }

            public void setImg1v1Id(int img1v1Id) {
                this.img1v1Id = img1v1Id;
            }

            public String getImg1v1Url() {
                return img1v1Url;
            }

            public void setImg1v1Url(String img1v1Url) {
                this.img1v1Url = img1v1Url;
            }

            public int getMusicSize() {
                return musicSize;
            }

            public void setMusicSize(int musicSize) {
                this.musicSize = musicSize;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getPicId() {
                return picId;
            }

            public void setPicId(int picId) {
                this.picId = picId;
            }

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public String getTrans() {
                return trans;
            }

            public void setTrans(String trans) {
                this.trans = trans;
            }

            public List<?> getAlias() {
                return alias;
            }

            public void setAlias(List<?> alias) {
                this.alias = alias;
            }
        }
    }

    public static class BMusicBean {
        /**
         * bitrate : 96000
         * dfsId : 7699879931772758
         * extension : mp3
         * id : 104029582
         * playTime : 379534
         * size : 4555381
         * sr : 44100
         * volumeDelta : 0.267569
         */

        private int bitrate;
        private long dfsId;
        private String extension;
        private int id;
        private int playTime;
        private int size;
        private int sr;
        private double volumeDelta;

        public int getBitrate() {
            return bitrate;
        }

        public void setBitrate(int bitrate) {
            this.bitrate = bitrate;
        }

        public long getDfsId() {
            return dfsId;
        }

        public void setDfsId(long dfsId) {
            this.dfsId = dfsId;
        }

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPlayTime() {
            return playTime;
        }

        public void setPlayTime(int playTime) {
            this.playTime = playTime;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getSr() {
            return sr;
        }

        public void setSr(int sr) {
            this.sr = sr;
        }

        public double getVolumeDelta() {
            return volumeDelta;
        }

        public void setVolumeDelta(double volumeDelta) {
            this.volumeDelta = volumeDelta;
        }
    }

    public static class HMusicBean {
        /**
         * bitrate : 320000
         * dfsId : 3346913395615570
         * extension : mp3
         * id : 104029580
         * playTime : 379534
         * size : 15184500
         * sr : 44100
         * volumeDelta : 0.0758784
         */

        private int bitrate;
        private long dfsId;
        private String extension;
        private int id;
        private int playTime;
        private int size;
        private int sr;
        private double volumeDelta;

        public int getBitrate() {
            return bitrate;
        }

        public void setBitrate(int bitrate) {
            this.bitrate = bitrate;
        }

        public long getDfsId() {
            return dfsId;
        }

        public void setDfsId(long dfsId) {
            this.dfsId = dfsId;
        }

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPlayTime() {
            return playTime;
        }

        public void setPlayTime(int playTime) {
            this.playTime = playTime;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getSr() {
            return sr;
        }

        public void setSr(int sr) {
            this.sr = sr;
        }

        public double getVolumeDelta() {
            return volumeDelta;
        }

        public void setVolumeDelta(double volumeDelta) {
            this.volumeDelta = volumeDelta;
        }
    }

    public static class LMusicBean {
        /**
         * bitrate : 96000
         * dfsId : 7699879931772758
         * extension : mp3
         * id : 104029582
         * playTime : 379534
         * size : 4555381
         * sr : 44100
         * volumeDelta : 0.267569
         */

        private int bitrate;
        private long dfsId;
        private String extension;
        private int id;
        private int playTime;
        private int size;
        private int sr;
        private double volumeDelta;

        public int getBitrate() {
            return bitrate;
        }

        public void setBitrate(int bitrate) {
            this.bitrate = bitrate;
        }

        public long getDfsId() {
            return dfsId;
        }

        public void setDfsId(long dfsId) {
            this.dfsId = dfsId;
        }

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPlayTime() {
            return playTime;
        }

        public void setPlayTime(int playTime) {
            this.playTime = playTime;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getSr() {
            return sr;
        }

        public void setSr(int sr) {
            this.sr = sr;
        }

        public double getVolumeDelta() {
            return volumeDelta;
        }

        public void setVolumeDelta(double volumeDelta) {
            this.volumeDelta = volumeDelta;
        }
    }

    public static class MMusicBean {
        /**
         * bitrate : 160000
         * dfsId : 3375500697931298
         * extension : mp3
         * id : 104029581
         * playTime : 379534
         * size : 7592272
         * sr : 44100
         * volumeDelta : 0.468341
         */

        private int bitrate;
        private long dfsId;
        private String extension;
        private int id;
        private int playTime;
        private int size;
        private int sr;
        private double volumeDelta;

        public int getBitrate() {
            return bitrate;
        }

        public void setBitrate(int bitrate) {
            this.bitrate = bitrate;
        }

        public long getDfsId() {
            return dfsId;
        }

        public void setDfsId(long dfsId) {
            this.dfsId = dfsId;
        }

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPlayTime() {
            return playTime;
        }

        public void setPlayTime(int playTime) {
            this.playTime = playTime;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getSr() {
            return sr;
        }

        public void setSr(int sr) {
            this.sr = sr;
        }

        public double getVolumeDelta() {
            return volumeDelta;
        }

        public void setVolumeDelta(double volumeDelta) {
            this.volumeDelta = volumeDelta;
        }
    }

    public static class ArtistsBeanX {
        /**
         * albumSize : 0
         * alias : []
         * briefDesc :
         * id : 3691
         * img1v1Id : 0
         * img1v1Url : http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
         * musicSize : 0
         * name : 刘德华
         * picId : 0
         * picUrl : http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
         * trans :
         */

        private int albumSize;
        private String briefDesc;
        private int id;
        private int img1v1Id;
        private String img1v1Url;
        private int musicSize;
        private String name;
        private int picId;
        private String picUrl;
        private String trans;
        private List<?> alias;

        public int getAlbumSize() {
            return albumSize;
        }

        public void setAlbumSize(int albumSize) {
            this.albumSize = albumSize;
        }

        public String getBriefDesc() {
            return briefDesc;
        }

        public void setBriefDesc(String briefDesc) {
            this.briefDesc = briefDesc;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getImg1v1Id() {
            return img1v1Id;
        }

        public void setImg1v1Id(int img1v1Id) {
            this.img1v1Id = img1v1Id;
        }

        public String getImg1v1Url() {
            return img1v1Url;
        }

        public void setImg1v1Url(String img1v1Url) {
            this.img1v1Url = img1v1Url;
        }

        public int getMusicSize() {
            return musicSize;
        }

        public void setMusicSize(int musicSize) {
            this.musicSize = musicSize;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPicId() {
            return picId;
        }

        public void setPicId(int picId) {
            this.picId = picId;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getTrans() {
            return trans;
        }

        public void setTrans(String trans) {
            this.trans = trans;
        }

        public List<?> getAlias() {
            return alias;
        }

        public void setAlias(List<?> alias) {
            this.alias = alias;
        }
    }
}
