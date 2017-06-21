package com.maoqi.listen.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.maoqi.listen.Constant;
import com.maoqi.listen.R;
import com.maoqi.listen.model.DBManager;
import com.maoqi.listen.model.bean.BaseSongBean;
import com.maoqi.listen.model.event.PlayNextEvent;
import com.maoqi.listen.model.event.PlayStateEvent;
import com.maoqi.listen.util.TUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

/**
 * Created by maoqi on 2017/6/15.
 */

public class PlayMusicService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener, AudioManager.OnAudioFocusChangeListener {

    private MediaPlayer player;
    private String url;
    private AudioManager audioManager;
    private List<BaseSongBean> playList;
    private PlayMusicBinder binder = new PlayMusicBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        playList = DBManager.getInstance().getSongList();
        player = new MediaPlayer();
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        player.setOnPreparedListener(this);
        player.setOnInfoListener(this);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int behavior = intent.getExtras().getInt(Constant.BEHAVIOR);

        switch (behavior) {
            case Constant.BEHAVIOR_PLAY:
                url = intent.getExtras().getString("url");
                play();
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
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stop();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void pause() {
        if (player != null && player.isPlaying()) {
            player.pause();
        }
        audioManager.abandonAudioFocus(this);
    }

    public void play() {
        if (player != null) {
            try {
                player.reset();
                player.setDataSource(url);
                player.prepare();
                player.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void continuePlay() {
        if (player != null && !player.isPlaying()) {
            player.start();
        }
    }

    public void stop() {
        if (player != null && player.isPlaying()) {
            player.stop();
            player.release();
        }
        audioManager.abandonAudioFocus(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        EventBus.getDefault().post(new PlayNextEvent());
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

    class PlayMusicBinder extends Binder {

        public List<BaseSongBean> getPlayList() {
            return playList;
        }



    }

}
