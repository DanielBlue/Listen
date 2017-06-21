package com.maoqi.listen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maoqi.listen.R;
import com.maoqi.listen.activity.MainActivity;
import com.maoqi.listen.model.bean.BaseSongBean;
import com.maoqi.listen.service.PlayMusicService;

import java.util.List;

/**
 * Created by maoqi on 2017/6/19.
 */

public class PopupPlayListAdapter extends RecyclerView.Adapter<PopupPlayListAdapter.ViewHolder> {
    private Context context;
    private List<BaseSongBean> data;

    public PopupPlayListAdapter(Context context, List<BaseSongBean> data) {
        this.context = context;
        this.data = data;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_play_list, parent, false);
        ViewHolder holder = new ViewHolder(view, viewType);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_song_title.setText(data.get(position).getSongTitle());
        holder.tv_song_artist.setText(" - " + data.get(position).getSongArtist());
        if (position == ((MainActivity) context).binder.getCurrentPlayPosition()) {
            holder.tv_song_title.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.tv_song_artist.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            holder.tv_song_title.setTextColor(context.getResources().getColor(R.color.black));
            holder.tv_song_artist.setTextColor(context.getResources().getColor(R.color.textColorGray));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_content;
        private TextView tv_song_title;
        private TextView tv_song_artist;
        private ImageView iv_delete;


        public ViewHolder(View itemView, final int position) {
            super(itemView);
            rl_content = (RelativeLayout) itemView.findViewById(R.id.rl_content);
            tv_song_title = (TextView) itemView.findViewById(R.id.tv_song_title);
            tv_song_artist = (TextView) itemView.findViewById(R.id.tv_song_artist);
            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);

            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).startService(PlayMusicService.deleteIntent(context, position));
                    notifyDataSetChanged();
                }
            });

            rl_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startService(PlayMusicService.playLocalIntent(context, position));
                    ((MainActivity) context).popupWindow.dismiss();
                }
            });
        }
    }
}
