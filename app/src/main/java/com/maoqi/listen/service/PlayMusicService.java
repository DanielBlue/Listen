package com.maoqi.listen.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.maoqi.listen.Constant;
import com.maoqi.listen.ListenApplication;
import com.maoqi.listen.R;
import com.maoqi.listen.activity.MainActivity;
import com.maoqi.listen.model.DBManager;
import com.maoqi.listen.model.SpHelper;
import com.maoqi.listen.model.bean.BaseSongBean;
import com.maoqi.listen.model.event.PlayStateEvent;
import com.maoqi.listen.model.event.UpdateControllerInfoEvent;
import com.maoqi.listen.util.TUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;

/**
 * Created by maoqi on 2017/6/15.
 */

public class PlayMusicService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener, AudioManager.OnAudioFocusChangeListener {

    private MediaPlayer player;
    private BaseSongBean bean;
    private AudioManager audioManager;
    private List<BaseSongBean> playList;
    private PlayMusicBinder binder = new PlayMusicBinder();
    private int currentPlayPosition;
    private int loopType;
    private int playState = Constant.ON_STOP;
    private int position;
    private static Intent intent;
    private final int notificationId = 10000;
    private NotificationManager mNotificationManager;
    private RemoteViews remoteViews;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        player.setOnPreparedListener(this);
        player.setOnInfoListener(this);

        playList = DBManager.getInstance().getSongList();
        currentPlayPosition = SpHelper.getInt(this, Constant.CURRENT_PLAY_POSITON, 0);
        loopType = SpHelper.getInt(this, Constant.LOOP_TYPE, Constant.LIST_LOOP);

//        initNotification();

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    private void initNotification() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        remoteViews = new RemoteViews(getPackageName(), R.layout.view_notification_song);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 10086, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setContent(remoteViews)
                .setContentIntent(contentIntent)
                .build();

        mNotificationManager.notify(notificationId, notification);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        ListenApplication.fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                int behavior = intent.getExtras().getInt(Constant.BEHAVIOR);

                switch (behavior) {
                    case Constant.BEHAVIOR_PLAY_NET:
                        bean = intent.getParcelableExtra(Constant.SONG_BEAN);
                        add2PlayList(bean, true);
                        play(bean);
                        break;
                    case Constant.BEHAVIOR_PLAY_LOCAL:
                        currentPlayPosition = intent.getExtras().getInt(Constant.POSITION);
                        play(playList.get(currentPlayPosition));
                        break;
                    case Constant.BEHAVIOR_PAUSE:
                        pause();
                        break;
                    case Constant.BEHAVIOR_CONTINUE:
                        continuePlay();
                        break;
                    case Constant.BEHAVIOR_STOP:
                        stop();
                        break;
                    case Constant.BEHAVIOR_DELETE:
                        position = intent.getExtras().getInt(Constant.POSITION);
                        if (position == currentPlayPosition) {
                            playNext();
                        } else if (currentPlayPosition > position) {
                            currentPlayPosition -= 1;
                        }
                        DBManager.getInstance().deleteSong(playList.get(position));
                        playList.remove(position);
                        break;
                    case Constant.BEHAVIOR_ADD:
                        bean = intent.getParcelableExtra(Constant.SONG_BEAN);
                        add2PlayList(bean, false);
                        break;
                }
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    private void add2PlayList(BaseSongBean bean, boolean isPlay) {
        for (int i = 0; i < playList.size(); i++) {
            if (playList.get(i).getSongId().equals(bean.getSongId())) {
                if (isPlay) {
                    currentPlayPosition = i;
                }
                return;
            }
        }
        if (isPlay) {
            currentPlayPosition = playList.size();
        }
        playList.add(bean);
        DBManager.getInstance().insertSong(bean);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        SpHelper.putInt(this, Constant.CURRENT_PLAY_POSITON, currentPlayPosition);
        SpHelper.putInt(this, Constant.LOOP_TYPE, loopType);
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        SpHelper.putInt(this, Constant.CURRENT_PLAY_POSITON, currentPlayPosition);
        SpHelper.putInt(this, Constant.LOOP_TYPE, loopType);
        super.onDestroy();
    }

    public void setPlayState(int playState) {
        this.playState = playState;
    }

    public void play(final BaseSongBean bean) {
        if (player != null) {
            try {
                player.reset();
                if (bean.getSource() == Constant.SOURCE_CLOUD) {
                    String url = "http://lab.mkblog.cn/music/api.php?" +
                            "callback=jsonp&types=musicInfo&id=" + bean.getSongId();

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
                                        final String songUrl = jsonObject.getString("url");
                                        try {
                                            player.setDataSource(songUrl);
                                            player.prepare();
                                            player.start();
                                            setPlayState(Constant.ON_PLAY);
                                            EventBus.getDefault().post(new PlayStateEvent(playState));
                                            EventBus.getDefault().post(new UpdateControllerInfoEvent(playList.get(currentPlayPosition)));
//                                            remoteViews.setImageViewUri(R.id.iv_icon, Uri.parse(bean.getSongImgUrl()));
//                                            remoteViews.setTextViewText(R.id.tv_title,bean.getSongTitle());
//                                            remoteViews.setTextViewText(R.id.tv_artist,bean.getSongArtist());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        ListenApplication.fixedThreadPool.execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                bean.setSongUrl(songUrl);
                                                DBManager.getInstance().updateSongUrl(bean.getSongId(), bean.getSongUrl());
                                            }
                                        });
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                } else {
                    player.setDataSource(bean.getSongUrl());
                    player.prepare();
                    player.start();
                    setPlayState(Constant.ON_PLAY);
                    EventBus.getDefault().post(new PlayStateEvent(playState));
                    EventBus.getDefault().post(new UpdateControllerInfoEvent(playList.get(currentPlayPosition)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void pause() {
        if (player != null && player.isPlaying()) {
            player.pause();
            setPlayState(Constant.ON_PAUSE);
        }
        audioManager.abandonAudioFocus(this);
    }

    public void continuePlay() {
        if (player != null && !player.isPlaying()) {
            player.start();
            setPlayState(Constant.ON_PLAY);
        }
    }

    public void stop() {
        if (player != null && player.isPlaying()) {
            player.stop();
            player.release();
            setPlayState(Constant.ON_STOP);
        }
        audioManager.abandonAudioFocus(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        playNext();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        TUtils.showShort(R.string.source_error);
        EventBus.getDefault().post(new PlayStateEvent(Constant.ON_STOP));
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    private void playNext() {
        switch (loopType) {
            case Constant.LIST_LOOP:
                if (currentPlayPosition < playList.size() - 1) {
                    currentPlayPosition += 1;
                } else if (currentPlayPosition == playList.size() - 1) {
                    currentPlayPosition = 0;
                }
                break;
            case Constant.SINGLE_LOOP:
                break;
            case Constant.RANDOM_PLAY:
                double b = Math.random();
                currentPlayPosition = (int) (b * playList.size());
                break;
        }
        play(playList.get(currentPlayPosition));
        EventBus.getDefault().post(new UpdateControllerInfoEvent(playList.get(currentPlayPosition)));
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN://你已经得到了音频焦点。
                if (player != null && !player.isPlaying())
                    player.start();
                player.setVolume(1.0f, 1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS://你已经失去了音频焦点很长时间了。你必须停止所有的音频播放
                stop();
                player.release();
                player = null;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT://你暂时失去了音频焦点
                pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK://你暂时失去了音频焦点，但你可以小声地继续播放音频（低音量）而不是完全扼杀音频。
                if (player != null && player.isPlaying())
                    player.setVolume(0.1f, 0.1f);
                break;
        }
    }

    public class PlayMusicBinder extends Binder {

        public List<BaseSongBean> getPlayList() {
            return playList;
        }

        public int getCurrentPlayPosition() {
            return currentPlayPosition;
        }

        public int getLoopType() {
            return loopType;
        }

        public int getPlayState() {
            return playState;
        }

        public void setLoopType(int type) {
            loopType = type;
        }

        public void clearPlayList() {
            ListenApplication.fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    DBManager.getInstance().deleteSong(playList);
                }
            });
            playList.clear();
            currentPlayPosition = 0;
        }

        public void playNextSong() {
            playNext();
        }
    }

    public static Intent playNetIntent(Context context, BaseSongBean bean) {
        intent = new Intent(context, PlayMusicService.class);
        intent.putExtra(Constant.BEHAVIOR, Constant.BEHAVIOR_PLAY_NET);
        intent.putExtra(Constant.SONG_BEAN, bean);
        return intent;
    }

    public static Intent playLocalIntent(Context context, int position) {
        intent = new Intent(context, PlayMusicService.class);
        intent.putExtra(Constant.BEHAVIOR, Constant.BEHAVIOR_PLAY_LOCAL);
        intent.putExtra(Constant.POSITION, position);
        return intent;
    }

    public static Intent continueIntent(Context context) {
        intent = new Intent(context, PlayMusicService.class);
        intent.putExtra(Constant.BEHAVIOR, Constant.BEHAVIOR_CONTINUE);
        return intent;
    }

    public static Intent pauseIntent(Context context) {
        intent = new Intent(context, PlayMusicService.class);
        intent.putExtra(Constant.BEHAVIOR, Constant.BEHAVIOR_PAUSE);
        return intent;
    }

    public static Intent stopIntent(Context context) {
        intent = new Intent(context, PlayMusicService.class);
        intent.putExtra(Constant.BEHAVIOR, Constant.BEHAVIOR_STOP);
        return intent;
    }

    public static Intent addIntent(Context context, BaseSongBean bean) {
        intent = new Intent(context, PlayMusicService.class);
        intent.putExtra(Constant.BEHAVIOR, Constant.BEHAVIOR_ADD);
        intent.putExtra(Constant.SONG_BEAN, bean);
        return intent;
    }

    public static Intent deleteIntent(Context context, int position) {
        intent = new Intent(context, PlayMusicService.class);
        intent.putExtra(Constant.BEHAVIOR, Constant.BEHAVIOR_DELETE);
        intent.putExtra(Constant.POSITION, position);
        return intent;
    }

}
