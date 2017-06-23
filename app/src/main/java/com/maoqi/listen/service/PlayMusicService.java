package com.maoqi.listen.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
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
import com.zhy.http.okhttp.callback.BitmapCallback;
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
    private NotificationCompat.Builder builder;
    private Notification notification;

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
        initNotification();

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

    }

    private void initNotification() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        remoteViews = new RemoteViews(getPackageName(), R.layout.view_notification_song);

        Intent playNextIntent = new Intent(this, PlayMusicService.class);
        playNextIntent.putExtra(Constant.BEHAVIOR, Constant.BEHAVIOR_PLAY_NEXT);
        PendingIntent playNextPendingIntent = PendingIntent.getService(this, 10086, playNextIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ib_play_next, playNextPendingIntent);

        Intent PlayPauseIntent = new Intent(this, PlayMusicService.class);
        PlayPauseIntent.putExtra(Constant.BEHAVIOR, Constant.BEHAVIOR_PLAY_PAUSE);
        PendingIntent playPausePendingIntent = PendingIntent.getService(this, 10000, PlayPauseIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ib_play_pause, playPausePendingIntent);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 10010, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder = new NotificationCompat.Builder(this)
                .setContentIntent(contentIntent)
                .setContent(remoteViews)
                .setAutoCancel(false)
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker(getResources().getText(R.string.listen_is_running));
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        ListenApplication.fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                int behavior = 0;
                try {
                    behavior = intent.getExtras() != null ? intent.getExtras().getInt(Constant.BEHAVIOR) : 0;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                switch (behavior) {
                    case Constant.BEHAVIOR_PLAY_NET:
                        bean = intent.getParcelableExtra(Constant.SONG_BEAN);
                        add2PlayList(bean, true);
                        play(bean);
                        break;
                    case Constant.BEHAVIOR_PLAY_LOCAL:
                        currentPlayPosition = intent.getExtras().getInt(Constant.POSITION);
                        if (playList != null && playList.size() > 0) {
                            play(playList.get(currentPlayPosition));
                        }
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

                    case Constant.BEHAVIOR_PLAY_PAUSE:
                        if (playState == Constant.ON_PLAY) {
                            setNotificationPlayState(Constant.ON_STOP);
                            pause();
                        } else if (playState == Constant.ON_PAUSE) {
                            setNotificationPlayState(Constant.ON_PLAY);
                            continuePlay();
                        } else if (playState == Constant.ON_STOP) {
                            if (playList != null && playList.size() > 0) {
                                setNotificationPlayState(Constant.ON_PLAY);
                                play(playList.get(currentPlayPosition));
                            }
                        }
                        break;
                    case Constant.BEHAVIOR_PLAY_NEXT:
                        playNext();
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
        stop();
        mNotificationManager.cancel(notificationId);
        super.onDestroy();
    }

    public void setPlayState(int playState) {
        this.playState = playState;
    }

    public void play(final BaseSongBean bean) {
        ListenApplication.fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new UpdateControllerInfoEvent(playList.get(currentPlayPosition)));
                setPlayState(Constant.ON_PLAY);
                EventBus.getDefault().post(new PlayStateEvent(playState));
                updateNotificationInfo(playList.get(currentPlayPosition));
            }
        });
        if (player != null) {
            if (bean.getSource() == Constant.SOURCE_CLOUD) {
                OkHttpUtils.getInstance().cancelTag(this);
                String url = "http://lab.mkblog.cn/music/api.php?" +
                        "callback=jsonp&types=musicInfo&id=" + bean.getSongId();

                OkHttpUtils.get()
                        .url(url)
                        .tag(this)
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
                                    start(songUrl);
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
                start(bean.getSongUrl());
            }
        }
    }

    private void updateNotificationInfo(final BaseSongBean baseSongBean) {
        OkHttpUtils.getInstance().cancelTag(Constant.BITMAP);
        OkHttpUtils
                .get()
                .tag(Constant.BITMAP)
                .url(baseSongBean.getSongImgUrl())
                .build()
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        TUtils.showShort(R.string.net_error);
                    }

                    @Override
                    public void onResponse(final Bitmap response, int id) {
                        ListenApplication.fixedThreadPool.execute(new Runnable() {
                            @Override
                            public void run() {
                                remoteViews.setImageViewBitmap(R.id.iv_album_art, response);
                                remoteViews.setTextViewText(R.id.tv_title, baseSongBean.getSongTitle());
                                remoteViews.setTextViewText(R.id.tv_artist, baseSongBean.getSongArtist());
                                notification = builder.build();
                                notification.flags |= Notification.FLAG_NO_CLEAR;
                                startForeground(notificationId, notification);
                            }
                        });
                    }
                });
    }

    public void setNotificationPlayState(int playState) {
        if (playState == Constant.ON_PLAY) {
            remoteViews.setImageViewResource(R.id.ib_play_pause, R.drawable.ic_pause_black_36dp);
        } else {
            remoteViews.setImageViewResource(R.id.ib_play_pause, R.drawable.ic_play_arrow_black_36dp);
        }
        notification = builder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR;
        startForeground(notificationId, notification);
    }

    public synchronized void start(String songUrl) {
        try {
            player.reset();
            player.setDataSource(songUrl);
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
            setPlayState(Constant.ON_STOP);
            EventBus.getDefault().post(new PlayStateEvent(playState));
            setNotificationPlayState(Constant.ON_STOP);
        }
    }

    public void pause() {
        if (player != null && player.isPlaying()) {
            player.pause();
            setPlayState(Constant.ON_PAUSE);
            EventBus.getDefault().post(new PlayStateEvent(playState));
            setNotificationPlayState(playState);
        }
        audioManager.abandonAudioFocus(this);
    }

    public void continuePlay() {
        if (player != null && !player.isPlaying()) {
            player.start();
            setPlayState(Constant.ON_PLAY);
            EventBus.getDefault().post(new PlayStateEvent(playState));
            setNotificationPlayState(playState);
        }
    }

    public void stop() {
        if (player != null && player.isPlaying()) {
            player.stop();
            player.release();
            setPlayState(Constant.ON_STOP);
            EventBus.getDefault().post(new PlayStateEvent(playState));
            setNotificationPlayState(playState);
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
                player = null;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT://你暂时失去了音频焦点
                pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK://你暂时失去了音频焦点，但你可以小声地继续播放音频（低音量）而不是完全扼杀音频。

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
