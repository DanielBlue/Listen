package com.maoqi.listen.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maoqi.listen.Constant;
import com.maoqi.listen.ListenApplication;
import com.maoqi.listen.R;
import com.maoqi.listen.activity.MainActivity;
import com.maoqi.listen.bean.QQSongBean;
import com.maoqi.listen.model.PlayControllerCallback;
import com.maoqi.listen.service.PlayMusicService;
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

public class QQResultListAdapter extends RecyclerView.Adapter<QQResultListAdapter.ViewHolder> {
    private List<QQSongBean> data;
    private Activity activity;
    public PlayControllerCallback callback;
    private static QQSongBean qqSongBean;

    public QQResultListAdapter(List<QQSongBean> data, Activity activity, PlayControllerCallback callback) {
        this.data = data;
        this.activity = activity;
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ListenApplication.appContext).inflate(R.layout.item_result_list, parent, false);
        ViewHolder holder = new ViewHolder(view, data, viewType, activity,callback);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_title.setText(data.get(position).getSongname());
        holder.tv_artist.setText(data.get(position).getSinger().get(0).getName()
                + " - " + data.get(position).getAlbumname());
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

        public ViewHolder(View itemView, final List<QQSongBean> data, final int position, final Activity activity, final PlayControllerCallback callback) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_artist = (TextView) itemView.findViewById(R.id.tv_artist);
            ll_content = (LinearLayout) itemView.findViewById(R.id.ll_content);
            iv_more = (ImageView) itemView.findViewById(R.id.iv_more);

            ll_content.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ((MainActivity)activity).setPlayState(Constant.ON_PLAY);
                    qqSongBean = data.get(position);
                    String centerUrl = qqSongBean.getAlbummid().substring(qqSongBean.getAlbummid().length()-2,qqSongBean.getAlbummid().length()-1)
                            +"/"+qqSongBean.getAlbummid().substring(qqSongBean.getAlbummid().length()-1)+"/"+qqSongBean.getAlbummid();
                    String imgUrl = "http://imgcache.qq.com/music/photo/mid_album_300/"+centerUrl+".jpg";
                    Log.d("ViewHolder", imgUrl);
                    callback.updateSongInfo(imgUrl,qqSongBean.getSongname(),qqSongBean.getSinger().get(0).getName());

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
                                                + qqSongBean.getSongmid() + ".m4a?vkey="
                                                + key + "&fromtag=0&guid=780782017";

                                        Intent intent = new Intent(activity, PlayMusicService.class);
                                        intent.putExtra("url",songUrl);
                                        intent.putExtra(Constant.BEHAVIOR,Constant.BEHAVIOR_PLAY);
                                        activity.startService(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
            });
        }
    }
}
