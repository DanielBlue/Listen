package com.maoqi.listen.base;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

/**
 * Created by maoqi on 2017/6/2.
 */

public abstract class BaseToolbarFragment extends BaseFragment{

    protected ActionBar initToolbar(Toolbar toolbar){
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        return actionBar;
    }

    protected void initToolbar(Toolbar toolbar,int id) {
        ActionBar actionBar = initToolbar(toolbar);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(id);
    }
}
