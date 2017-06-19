package com.maoqi.listen.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.maoqi.listen.Constant;
import com.maoqi.listen.ListenApplication;
import com.maoqi.listen.R;
import com.maoqi.listen.activity.MainActivity;
import com.maoqi.listen.model.DBManager;
import com.maoqi.listen.model.PlayControllerCallback;
import com.maoqi.listen.model.bean.BaseSongBean;
import com.maoqi.listen.model.bean.CloudSongBean;
import com.maoqi.listen.service.PlayMusicService;
import com.maoqi.listen.util.SongUtils;
import com.maoqi.listen.util.TUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;

/**
 * Created by maoqi on 2017/6/15.
 */

public class CloudResultListAdapter extends RecyclerView.Adapter<CloudResultListAdapter.ViewHolder> {
    private List<CloudSongBean> data;
    private Activity activity;
    private PlayControllerCallback callback;
    private static CloudSongBean cloudSongBean;

    public CloudResultListAdapter(Activity activity, List<CloudSongBean> data, PlayControllerCallback callback) {
        this.data = data;
        this.activity = activity;
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ListenApplication.appContext).inflate(R.layout.item_result_list, parent, false);
        ViewHolder holder = new ViewHolder(view, data, viewType, activity, callback);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        SpannableString spannableString = new SpannableString("");
//        spannableString.setSpan(new ForegroundColorSpan(ListenApplication.appContext.getResources().getColor(R.color.search_blue)), 0,data.get(position).getAlbum().getArtists().get(0).getName().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE );
        holder.tv_title.setText(data.get(position).getName());
        holder.tv_artist.setText(data.get(position).getAlbum().getArtists().get(0).getName()
                + " - " + data.get(position).getAlbum().getName());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        TextView tv_artist;
        LinearLayout ll_content;
        ImageView iv_more;

        public ViewHolder(View itemView, final List<CloudSongBean> data,
                          final int position, final Activity activity, final PlayControllerCallback callback) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_artist = (TextView) itemView.findViewById(R.id.tv_artist);
            ll_content = (LinearLayout) itemView.findViewById(R.id.ll_content);
            iv_more = (ImageView) itemView.findViewById(R.id.iv_more);
            ll_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) activity).setPlayState(Constant.ON_PLAY);

                    cloudSongBean = data.get(position);

                    callback.updateSongInfo(cloudSongBean.getAlbum().getBlurPicUrl(),
                            cloudSongBean.getName(), cloudSongBean.getArtists().get(0).getName());

                    String url = "http://lab.mkblog.cn/music/api.php?" +
                            "callback=jsonp&types=musicInfo&id=" + cloudSongBean.getId();

                    OkHttpUtils.get()
                            .url(url)
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
                                        String songUrl = jsonObject.getString("url");
                                        Intent intent = new Intent(activity, PlayMusicService.class);
                                        intent.putExtra("url", songUrl);
                                        intent.putExtra(Constant.BEHAVIOR, Constant.BEHAVIOR_PLAY);
                                        activity.startService(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
            });

            iv_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View popupView = LayoutInflater.from(activity).inflate(R.layout.popup_more_selection, null);
                    PopupWindow popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setContentView(popupView);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setClippingEnabled(false);
                    popupWindow.setAnimationStyle(R.style.AnimUpDown);

                    TextView tv_song_info = (TextView) popupView.findViewById(R.id.tv_song_info);
                    TextView tv_add_list = (TextView) popupView.findViewById(R.id.tv_add_list);
                    TextView tv_collect = (TextView) popupView.findViewById(R.id.tv_collect);
                    TextView tv_download = (TextView) popupView.findViewById(R.id.tv_download);
                    tv_song_info.setText("歌曲:"+data.get(getAdapterPosition()).getName());
                    tv_add_list.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((MainActivity)activity).addSong2List(SongUtils.cloud2Base(data.get(position)));
                        }
                    });

                    tv_collect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BaseSongBean bean = SongUtils.cloud2Base(data.get(position));
                            if(DBManager.getInstance().isCollect(bean)){
                                bean.setCollect(false);
                                TUtils.showShort(R.string.cancel_successful);
                            }else {
                                bean.setCollect(true);
                                TUtils.showShort(R.string.collect_successful);
                            }
                            DBManager.getInstance().updateCollect(bean);
                        }
                    });

                    tv_download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO: 2017/6/19
                            TUtils.showShort("下载功能开发中");
                        }
                    });

                    popupWindow.showAtLocation(activity.findViewById(R.id.ll_content_parent), Gravity.BOTTOM, 0, 0);
                }
            });
        }
    }

//    void encryptedRequest(Map<String, String> param) {
//        String modulus = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b72" +
//                "5152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbd" +
//                "a92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe48" +
//                "75d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7";
//        String nonce = "0CoJUm6Qyw8W8jud";
//        String pubKey = "010001";
//        Gson gson = new Gson();
//        String json = gson.toJson(param);
//
//
//        String sec_key = createSecretKey(16);
//        String enc_text = null;
//        try {
//            enc_text = AESUtils.encrypt(AESUtils.encrypt(nonce, json),sec_key);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        String enc_sec_key = rsaEncrypt(sec_key, pubKey, modulus);
//        var data = {
//                'params':enc_text,
//                'encSecKey':enc_sec_key
//        }
//    }
//
//    private String rsaEncrypt(String text, String pubKey, String modulus) {
//        text = convert(text);
//        var base = str2bigInt(hexify(text), 16);
//        var exp = str2bigInt(pubKey, 16);
//        var mod = str2bigInt(modulus, 16);
//        var bigNumber = expmod(base, exp, mod);
//        var rs = bigInt2str(bigNumber, 16);
//        return zfill(rs, 256).toLowerCase();
//    }
//
//    public String convert(String s) {
//        StringBuffer sb = new StringBuffer(s);
//        return sb.reverse().toString();
//    }
//
//    private String createSecretKey(int size) {
//        String[] result = {};
//        String choice = "012345679abcdef";
//        String[] str = choice.split("");
//        for (int i = 0; i < size; i++) {
//            int index = (int) Math.floor(Math.random() * str.length);
//            result[i] = str[index];
//        }
//
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < str.length; i++) {
//            sb.append(str[i]);
//        }
//        Log.d("CloudResultListAdapter", sb.toString());
//
//        return sb.toString();
//    }

}

