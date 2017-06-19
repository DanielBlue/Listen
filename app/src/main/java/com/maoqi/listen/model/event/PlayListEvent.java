package com.maoqi.listen.model.event;

import com.maoqi.listen.model.bean.BaseSongBean;

/**
 * Created by maoqi on 2017/6/19.
 */

public class PlayListEvent {
    int tag;
    BaseSongBean bean;
    int position;

    public PlayListEvent(int tag, BaseSongBean bean,int position) {
        this.tag = tag;
        this.bean = bean;
        this.position = position;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public BaseSongBean getBean() {
        return bean;
    }

    public void setBean(BaseSongBean bean) {
        this.bean = bean;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
