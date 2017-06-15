package com.maoqi.listen.base;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

/**
 * Created by maoqi on 2017/5/31.
 */

public abstract class BaseToolbarActivity extends BaseActivity{

    protected void initToolbar(Toolbar toolbar){
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeAsUpIndicator();
        actionBar.setDisplayShowTitleEnabled(false);
    }
}
