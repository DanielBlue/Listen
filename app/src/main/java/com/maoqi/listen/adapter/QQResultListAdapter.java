package com.maoqi.listen.adapter;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.maoqi.listen.Constant;
import com.maoqi.listen.ListenApplication;
import com.maoqi.listen.R;
import com.maoqi.listen.activity.MainActivity;
import com.maoqi.listen.model.DBManager;
import com.maoqi.listen.model.bean.BaseSongBean;
import com.maoqi.listen.model.bean.QQSongBean;
import com.maoqi.listen.model.event.PlayListEvent;
import com.maoqi.listen.util.SongUtils;
import com.maoqi.listen.util.TUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
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
    private static QQSongBean qqSongBean;

    public QQResultListAdapter(List<QQSongBean> data, Activity activity) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ListenApplication.appContext).inflate(R.layout.item_result_list, parent, false);
        ViewHolder holder = new ViewHolder(view, data, viewType,activity);
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

        public ViewHolder(View itemView, final List<QQSongBean> data, final int position, final Activity activity) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_artist = (TextView) itemView.findViewById(R.id.tv_artist);
            ll_content = (LinearLayout) itemView.findViewById(R.id.ll_content);
            iv_more = (ImageView) itemView.findViewById(R.id.iv_more);

            Log.d("ViewHolder", "position:" + position);

            ll_content.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    qqSongBean = data.get(position);
                    ((MainActivity) activity).setPlayState(Constant.ON_PLAY);
                    String centerUrl = qqSongBean.getAlbummid().substring(qqSongBean.getAlbummid().length() - 2, qqSongBean.getAlbummid().length() - 1)
                            + "/" + qqSongBean.getAlbummid().substring(qqSongBean.getAlbummid().length() - 1) + "/" + qqSongBean.getAlbummid();
                    final String imgUrl = "http://imgcache.qq.com/music/photo/mid_album_300/" + centerUrl + ".jpg";

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
                                        EventBus.getDefault().post(new PlayListEvent(Constant.ADD, SongUtils.qq2Base(qqSongBean,songUrl,imgUrl), -1));

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
                    qqSongBean = data.get(position);
                    View popupView = LayoutInflater.from(activity).inflate(R.layout.popup_more_selection, null);
                    final PopupWindow popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setContentView(popupView);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
                    setBgAlpha(activity, 0.7f);
                    popupWindow.setFocusable(true);
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setClippingEnabled(false);
                    popupWindow.setAnimationStyle(R.style.AnimUpDown);
                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            setBgAlpha(activity, 1f);
                        }
                    });

                    TextView tv_song_info = (TextView) popupView.findViewById(R.id.tv_song_info);
                    TextView tv_add_list = (TextView) popupView.findViewById(R.id.tv_add_list);
                    TextView tv_collect = (TextView) popupView.findViewById(R.id.tv_collect);
                    TextView tv_download = (TextView) popupView.findViewById(R.id.tv_download);
                    tv_song_info.setText("歌曲:" + qqSongBean.getSongname());
                    tv_add_list.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String centerUrl = qqSongBean.getAlbummid().substring(qqSongBean.getAlbummid().length() - 2, qqSongBean.getAlbummid().length() - 1)
                                    + "/" + qqSongBean.getAlbummid().substring(qqSongBean.getAlbummid().length() - 1) + "/" + qqSongBean.getAlbummid();
                            final String imgUrl = "http://imgcache.qq.com/music/photo/mid_album_300/" + centerUrl + ".jpg";

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
                                                ((MainActivity) activity).addSong2List(SongUtils.qq2Base(data.get(position),songUrl,imgUrl));
                                                popupWindow.dismiss();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });



                        }
                    });

                    tv_collect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            qqSongBean = data.get(position);
                            String centerUrl = qqSongBean.getAlbummid().substring(qqSongBean.getAlbummid().length() - 2, qqSongBean.getAlbummid().length() - 1)
                                    + "/" + qqSongBean.getAlbummid().substring(qqSongBean.getAlbummid().length() - 1) + "/" + qqSongBean.getAlbummid();
                            final String imgUrl = "http://imgcache.qq.com/music/photo/mid_album_300/" + centerUrl + ".jpg";

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
                                                BaseSongBean bean = SongUtils.qq2Base(data.get(position),songUrl,imgUrl);
                                                if (DBManager.getInstance().isCollect(bean)) {
                                                    bean.setCollect(false);
                                                    TUtils.showShort(R.string.cancel_successful);
                                                } else {
                                                    bean.setCollect(true);
                                                    TUtils.showShort(R.string.collect_successful);
                                                }
                                                DBManager.getInstance().updateCollect(bean);
                                                popupWindow.dismiss();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                        }
                    });

                    tv_download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO: 2017/6/19
                            TUtils.showShort("下载功能开发中");
                            popupWindow.dismiss();
                        }
                    });

                    popupWindow.showAtLocation(activity.findViewById(R.id.ll_content_parent), Gravity.BOTTOM, 0, 0);
                }
            });
        }

        void setBgAlpha(Activity activity, float alpha) {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.alpha = alpha;
            activity.getWindow().setAttributes(lp);
        }
    }
}
