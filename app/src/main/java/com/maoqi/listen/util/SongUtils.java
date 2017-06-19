package com.maoqi.listen.util;

import com.maoqi.listen.Constant;
import com.maoqi.listen.R;
import com.maoqi.listen.model.bean.BaseSongBean;
import com.maoqi.listen.model.bean.CloudSongBean;
import com.maoqi.listen.model.bean.QQSongBean;
import com.maoqi.listen.model.bean.XiamiSongBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by maoqi on 2017/6/19.
 */

public class SongUtils {

    public static BaseSongBean cloud2Base(CloudSongBean bean) {
        BaseSongBean baseSongBean = new BaseSongBean(bean.getMp3Url(), bean.getName(),
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

    public static BaseSongBean qq2Base(final QQSongBean bean) {
        String centerUrl = bean.getAlbummid().substring(bean.getAlbummid().length() - 2, bean.getAlbummid().length() - 1)
                + "/" + bean.getAlbummid().substring(bean.getAlbummid().length() - 1) + "/" + bean.getAlbummid();
        final String imgUrl = "http://imgcache.qq.com/music/photo/mid_album_300/" + centerUrl + ".jpg";
        final BaseSongBean baseSongBean = new BaseSongBean();
        OkHttpUtils.get()
                .url(Constant.QQ_KEY_URL)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        TUtils.showShort(R.string.net_error + "..." + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        String json = response.substring(6, response.length() - 1);
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            String key = jsonObject.getString("key");

                            String songUrl = "http://cc.stream.qqmusic.qq.com/C200"
                                    + bean.getSongmid() + ".m4a?vkey="
                                    + key + "&fromtag=0&guid=780782017";

                            baseSongBean.setSongUrl(songUrl);
                            baseSongBean.setSongTitle(bean.getSongname());
                            baseSongBean.setSongArtist(bean.getSinger().get(0).getName());
                            baseSongBean.setSongAlbum(bean.getAlbumname());
                            baseSongBean.setSongImgUrl(imgUrl);
                            baseSongBean.setLyricUrl("");
                            baseSongBean.setSongId(String.valueOf(bean.getSongid()));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        return baseSongBean;

    }
}
