package com.maoqi.listen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maoqi.listen.ListenApplication;
import com.maoqi.listen.R;
import com.maoqi.listen.bean.CloudSongBean;

import java.util.List;

/**
 * Created by maoqi on 2017/6/15.
 */

public class CloudResultListAdapter extends RecyclerView.Adapter<CloudResultListAdapter.ViewHolder> {
    private List<CloudSongBean> data;

    public CloudResultListAdapter(List<CloudSongBean> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ListenApplication.appContext).inflate(R.layout.item_result_list,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        SpannableString spannableString = new SpannableString("");
//        spannableString.setSpan(new ForegroundColorSpan(ListenApplication.appContext.getResources().getColor(R.color.search_blue)), 0,data.get(position).getAlbum().getArtists().get(0).getName().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE );
        holder.tv_title.setText(data.get(position).getName());
        holder.tv_artist.setText(data.get(position).getAlbum().getArtists().get(0).getName()
                +" - "+data.get(position).getAlbum().getName());
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

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_artist = (TextView) itemView.findViewById(R.id.tv_artist);
            ll_content = (LinearLayout) itemView.findViewById(R.id.ll_content);
            iv_more = (ImageView) itemView.findViewById(R.id.iv_more);
        }
    }
}

