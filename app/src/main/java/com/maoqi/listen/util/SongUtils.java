package com.maoqi.listen.util;

import com.maoqi.listen.model.bean.BaseSongBean;
import com.maoqi.listen.model.bean.CloudSongBean;
import com.maoqi.listen.model.bean.QQSongBean;
import com.maoqi.listen.model.bean.XiamiSongBean;

/**
 * Created by maoqi on 2017/6/19.
 */

public class SongUtils {

    public static BaseSongBean cloud2Base(CloudSongBean bean,String songUrl) {
        BaseSongBean baseSongBean = new BaseSongBean(songUrl, bean.getName(),
                bean.getArtists().get(0).getName(),
                bean.getAlbum().getName(), bean.getAlbum().getBlurPicUrl(),
                "", String.valueOf(bean.getId()));
        return baseSongBean;
    }

    public static BaseSongBean xiami2Base(XiamiSongBean bean) {
        BaseSongBean baseSongBean = new BaseSongBean(bean.getListen_file(), bean.getSong_name(),
                bean.getArtist_name(), bean.getAlbum_name(), bean.getAlbum_logo(),
                "", String.valueOf(bean.getSong_id()));
        return baseSongBean;
    }

    public static BaseSongBean qq2Base(QQSongBean bean,String songUrl,String imgUrl) {
        BaseSongBean baseSongBean = new BaseSongBean();

        baseSongBean.setSongUrl(songUrl);
        baseSongBean.setSongTitle(bean.getSongname());
        baseSongBean.setSongArtist(bean.getSinger().get(0).getName());
        baseSongBean.setSongAlbum(bean.getAlbumname());
        baseSongBean.setSongImgUrl(imgUrl);
        baseSongBean.setLyricUrl("");
        baseSongBean.setSongId(String.valueOf(bean.getSongid()));
        return baseSongBean;

    }
}
