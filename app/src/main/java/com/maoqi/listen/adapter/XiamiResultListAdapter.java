package com.maoqi.listen.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
import com.maoqi.listen.bean.XiamiSongBean;
import com.maoqi.listen.model.PlayControllerCallback;
import com.maoqi.listen.service.PlayMusicService;

import java.util.List;

/**
 * Created by maoqi on 2017/6/15.
 */

public class XiamiResultListAdapter extends RecyclerView.Adapter<XiamiResultListAdapter.ViewHolder> {
    private List<XiamiSongBean> data;
    private Activity activity;
    private static PlayControllerCallback callback;

    public XiamiResultListAdapter(List<XiamiSongBean> data,Activity activity,PlayControllerCallback callback) {
        this.data = data;
        this.activity = activity;
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ListenApplication.appContext).inflate(R.layout.item_result_list,parent,false);
        ViewHolder holder = new ViewHolder(view,data,viewType,activity);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_title.setText(data.get(position).getSong_name());
        holder.tv_artist.setText(data.get(position).getArtist_name()
                +" - "+data.get(position).getAlbum_name());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_title;
        TextView tv_artist;
        LinearLayout ll_content;
        ImageView iv_more;

        public ViewHolder(View itemView, final List<XiamiSongBean> data, final int position, final Activity activity) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_artist = (TextView) itemView.findViewById(R.id.tv_artist);
            ll_content = (LinearLayout) itemView.findViewById(R.id.ll_content);
            iv_more = (ImageView) itemView.findViewById(R.id.iv_more);

            ll_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity)activity).setPlayState(Constant.ON_PLAY);
                    XiamiSongBean xiamiSongBean = data.get(position);
                    callback.updateSongInfo(xiamiSongBean.getAlbum_logo(),xiamiSongBean.getSong_name(),xiamiSongBean.getArtist_name());

                    Intent intent = new Intent(activity, PlayMusicService.class);
                    intent.putExtra("url",data.get(position).getListen_file());
                    intent.putExtra(Constant.BEHAVIOR, Constant.BEHAVIOR_PLAY);
                    activity.startService(intent);
                }
            });
        }
    }
}
