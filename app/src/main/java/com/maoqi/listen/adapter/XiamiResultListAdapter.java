package com.maoqi.listen.adapter;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
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
import com.maoqi.listen.model.bean.XiamiSongBean;
import com.maoqi.listen.model.event.PlayListEvent;
import com.maoqi.listen.util.SongUtils;
import com.maoqi.listen.util.TUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by maoqi on 2017/6/15.
 */

public class XiamiResultListAdapter extends RecyclerView.Adapter<XiamiResultListAdapter.ViewHolder> {
    private List<XiamiSongBean> data;
    private Activity activity;
    private static XiamiSongBean xiamiSongBean;

    public XiamiResultListAdapter(List<XiamiSongBean> data,Activity activity) {
        this.data = data;
        this.activity = activity;
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
                    xiamiSongBean = data.get(position);
                    ((MainActivity)activity).setPlayState(Constant.ON_PLAY);

                    EventBus.getDefault().post(new PlayListEvent(Constant.ADD,SongUtils.xiami2Base(xiamiSongBean),-1));
                }
            });

            iv_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    xiamiSongBean = data.get(position);
                    View popupView = LayoutInflater.from(activity).inflate(R.layout.popup_more_selection, null);
                    final PopupWindow popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setContentView(popupView);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
                    popupWindow.setOutsideTouchable(true);
                    setBgAlpha(activity,0.7f);
                    popupWindow.setFocusable(true);
                    popupWindow.setClippingEnabled(false);
                    popupWindow.setAnimationStyle(R.style.AnimUpDown);
                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            setBgAlpha(activity,1f);
                        }
                    });

                    TextView tv_song_info = (TextView) popupView.findViewById(R.id.tv_song_info);
                    TextView tv_add_list = (TextView) popupView.findViewById(R.id.tv_add_list);
                    TextView tv_collect = (TextView) popupView.findViewById(R.id.tv_collect);
                    TextView tv_download = (TextView) popupView.findViewById(R.id.tv_download);
                    tv_song_info.setText("歌曲:"+data.get(getAdapterPosition()).getSong_name());
                    tv_add_list.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((MainActivity) activity).addSong2List(SongUtils.xiami2Base(xiamiSongBean));
                            popupWindow.dismiss();
                        }
                    });

                    tv_collect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            xiamiSongBean = data.get(position);
                            BaseSongBean bean = SongUtils.xiami2Base(xiamiSongBean);
                            if(DBManager.getInstance().isCollect(bean)){
                                bean.setCollect(false);
                                TUtils.showShort(R.string.cancel_successful);
                            }else {
                                bean.setCollect(true);
                                TUtils.showShort(R.string.collect_successful);
                            }
                            DBManager.getInstance().updateCollect(bean);
                            popupWindow.dismiss();
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

        void setBgAlpha(Activity activity,float alpha){
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.alpha = alpha;
            activity.getWindow().setAttributes(lp);
        }
    }
}
