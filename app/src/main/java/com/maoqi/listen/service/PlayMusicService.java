package com.maoqi.listen.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.maoqi.listen.Constant;

import java.io.IOException;

/**
 * Created by maoqi on 2017/6/15.
 */

public class PlayMusicService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener, AudioManager.OnAudioFocusChangeListener {

    private MediaPlayer player;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        player.setOnPreparedListener(this);
        player.setOnInfoListener(this);

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int behavior = intent.getExtras().getInt(Constant.BEHAVIOR);

        switch (behavior) {
            case Constant.BEHAVIOR_PLAY:
                String url = intent.getExtras().getString("url");
                play(url);
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
        if (player != null) {
            player.release();
        }
        super.onDestroy();
    }

    public void pause() {
        if (player != null && player.isPlaying()) {
            player.pause();
        }
    }

    public void play(String url) {
        if (player != null && !player.isPlaying()) {
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
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
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
}
