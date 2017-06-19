package com.maoqi.listen.model.event;

/**
 * Created by maoqi on 2017/6/19.
 */

public class PlayStateEvent {
    private int playState;

    public PlayStateEvent(int playState) {
        this.playState = playState;
    }

    public int getPlayState() {
        return playState;
    }

    public void setPlayState(int playState) {
        this.playState = playState;
    }
}
