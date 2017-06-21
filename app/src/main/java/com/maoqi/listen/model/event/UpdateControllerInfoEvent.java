package com.maoqi.listen.model.event;

import com.maoqi.listen.model.bean.BaseSongBean;

/**
 * Created by maoqi on 2017/6/21.
 */

public class UpdateControllerInfoEvent {

    public UpdateControllerInfoEvent(BaseSongBean bean) {
        this.bean = bean;
    }

    BaseSongBean bean;

    public BaseSongBean getBean() {
        return bean;
    }

    public void setBean(BaseSongBean bean) {
        this.bean = bean;
    }
}
