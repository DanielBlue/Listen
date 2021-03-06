package com.maoqi.listen.activity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maoqi.listen.Constant;
import com.maoqi.listen.ListenApplication;
import com.maoqi.listen.R;
import com.maoqi.listen.adapter.PopupPlayListAdapter;
import com.maoqi.listen.adapter.SearchListAdapter;
import com.maoqi.listen.adapter.SpacesItemDecoration;
import com.maoqi.listen.base.BaseToolbarActivity;
import com.maoqi.listen.fragment.CloudResultFragment;
import com.maoqi.listen.fragment.QQResultFragment;
import com.maoqi.listen.fragment.XiamiResultFragment;
import com.maoqi.listen.model.DBManager;
import com.maoqi.listen.model.bean.BaseSongBean;
import com.maoqi.listen.model.bean.CloudSongBean;
import com.maoqi.listen.model.bean.QQSongBean;
import com.maoqi.listen.model.bean.XiamiSongBean;
import com.maoqi.listen.model.event.PlayStateEvent;
import com.maoqi.listen.model.event.UpdateControllerInfoEvent;
import com.maoqi.listen.service.PlayMusicService;
import com.maoqi.listen.util.DensityUtils;
import com.maoqi.listen.util.KeyBoardUtils;
import com.maoqi.listen.util.TUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class MainActivity extends BaseToolbarActivity implements View.OnClickListener {

    Toolbar tbToolbar;
    ImageView ivAlbumArt;
    TextView tvTitle;
    TextView tvArtist;
    ImageButton ibPlayPause;
    ImageButton ibList;
    EditText et_search;
    private int flag;
    private final int CLOUD_MUSIC = 10000;
    private final int XIAMI_MUSIC = 10010;
    private final int QQ_MUSIC = 10086;
    private List<Fragment> fragmentList;
    private CloudResultFragment cloudResultFragment;
    private XiamiResultFragment xiamiResultFragment;
    private QQResultFragment qqResultFragment;
    private TabLayout tb_tab;
    private ViewPager vp_pager;
    private ProgressDialog progress;
    private ImageButton ib_clear;
    private LinearLayout ll_content_parent;
    private View popupView;
    public PopupWindow popupWindow;
    private TextView tv_loop_text;
    private TextView tv_collect;
    private TextView tv_clear;
    private RecyclerView rv_list;
    private LinearLayout ll_loop_style;
    private PopupPlayListAdapter playListAdapter;
    public PlayMusicService.PlayMusicBinder binder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (PlayMusicService.PlayMusicBinder) service;
            updatePlayState(binder.getPlayState());
            if (binder.getPlayList() != null && binder.getPlayList().size() > 0) {
                BaseSongBean bean = binder.getPlayList().get(binder.getCurrentPlayPosition());
                updateSongInfo(bean.getSongImgUrl(), bean.getSongTitle(), bean.getSongArtist());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private ImageButton ib_play_next;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        progress = createProgressDialog(R.string.loading);
        ll_content_parent = (LinearLayout) findViewById(R.id.ll_content_parent);
        ivAlbumArt = (ImageView) findViewById(R.id.iv_album_art);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvArtist = (TextView) findViewById(R.id.tv_artist);
        ib_clear = (ImageButton) findViewById(R.id.ib_clear);
        ibPlayPause = (ImageButton) findViewById(R.id.ib_play_pause);
        ibList = (ImageButton) findViewById(R.id.ib_list);
        ib_clear.setOnClickListener(this);
        ib_play_next = (ImageButton) findViewById(R.id.ib_play_next);
        ib_play_next.setOnClickListener(this);
        ibPlayPause.setOnClickListener(this);
        ibList.setOnClickListener(this);


        tbToolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        initToolbar(tbToolbar);
        tb_tab = (TabLayout) findViewById(R.id.tl_tab);
        vp_pager = (ViewPager) findViewById(R.id.vp_pager);
        vp_pager.setAdapter(new SearchListAdapter(getSupportFragmentManager(), fragmentList));

        vp_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String input = et_search.getText().toString();
                switch (position) {
                    case 0:
                        flag = CLOUD_MUSIC;
                        break;
                    case 1:
                        flag = XIAMI_MUSIC;
                        break;
                    case 2:
                        flag = QQ_MUSIC;
                        break;
                }
                if (!TextUtils.isEmpty(input)) {
                    requestServer(input);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tb_tab.setupWithViewPager(vp_pager);

        et_search = (EditText) findViewById(R.id.et_search);
        et_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ib_clear.setVisibility(View.VISIBLE);
                } else {
                    ib_clear.setVisibility(View.GONE);
                }
            }
        });

        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER) {
                    KeyBoardUtils.closeKeybord(et_search, MainActivity.this);
                    requestServer(et_search.getText().toString());
                }
                return false;
            }
        });

        Intent intent = new Intent(this, PlayMusicService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void requestServer(final String result) {
        if (!progress.isShowing()) {
            progress.show();
        }

        flag = (flag == 0) ? CLOUD_MUSIC : flag;
        String url;

        switch (flag) {
            case CLOUD_MUSIC:
                url = "http://music.163.com/api/search/pc";
                OkHttpUtils
                        .post()
                        .url(url)
                        .addParams("s", result)
                        .addParams("limit", "50")
                        .addParams("type", "1")
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        TUtils.showShort(R.string.net_error + "..." + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Gson gson = new Gson();
                            java.lang.reflect.Type type = new TypeToken<List<CloudSongBean>>() {
                            }.getType();
                            List<CloudSongBean> data =
                                    gson.fromJson(jsonObject.getJSONObject("result").getString("songs"), type);
                            cloudResultFragment.refreshList(data);

                            if (progress.isShowing()) {
                                progress.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                break;
            case XIAMI_MUSIC:
                url = "http://api.xiami.com/web?v=2.0&app_key=1&page=1&limit=50&callback=jsonp&r=search/songs&key=" + result;
                OkHttpUtils.get()
                        .url(url)
                        .addHeader("Referer", "http://m.xiami.com/")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                TUtils.showShort(R.string.net_error + "..." + e.getMessage());
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    String json = response.substring(6, response.length() - 1);
                                    JSONObject jsonObject = new JSONObject(json);
                                    Log.d("MainActivity", "xiami......" + jsonObject.toString());

                                    Gson gson = new Gson();
                                    java.lang.reflect.Type type = new TypeToken<List<XiamiSongBean>>() {
                                    }.getType();
                                    List<XiamiSongBean> data = gson.fromJson(jsonObject.getJSONObject("data").getString("songs"), type);

                                    xiamiResultFragment.refreshList(data);

                                    if (progress.isShowing()) {
                                        progress.dismiss();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                break;
            case QQ_MUSIC:
                url = "http://i.y.qq.com/s.music/fcgi-bin/search_for_qq_cp?g_tk=938407465&uin=0&format=jsonp&inCharset=utf-8&outCharset=utf-8&notice=0&platform=h5&needNewCode=1&zhidaqu=1&catZhida=1&t=0&flag=1&ie=utf-8&sem=1&aggr=0&perpage=20&p=1&remoteplace=txt.mqq.all&_=1459991037831&jsonpCallback=jsonp&n=50&w=" + result;
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
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(json);
                                    Gson gson = new Gson();
                                    java.lang.reflect.Type type = new TypeToken<List<QQSongBean>>() {
                                    }.getType();
                                    List<QQSongBean> data =
                                            gson.fromJson(jsonObject.getJSONObject("data").getJSONObject("song").getString("list"), type);

                                    qqResultFragment.refreshList(data);

                                    if (progress.isShowing()) {
                                        progress.dismiss();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                break;
        }
    }

    @Override
    protected void initData() {
        fragmentList = new ArrayList<>();
        cloudResultFragment = new CloudResultFragment();
        xiamiResultFragment = new XiamiResultFragment();
        qqResultFragment = new QQResultFragment();
        fragmentList.add(cloudResultFragment);
        fragmentList.add(xiamiResultFragment);
        fragmentList.add(qqResultFragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PlayStateEvent event) {
        updatePlayState(event.getPlayState());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateControllerInfo(UpdateControllerInfoEvent event) {
        updateSongInfo(event.getBean().getSongImgUrl(), event.getBean().getSongTitle(), event.getBean().getSongArtist());
    }

    public void updateSongInfo(String imgUrl, String songName, String artist) {
        Glide.with(this).load(imgUrl).into(ivAlbumArt);
        tvTitle.setText(songName);
        tvArtist.setText(artist);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                stopService(new Intent(this,PlayMusicService.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_list:
                popupView = LayoutInflater.from(this).inflate(R.layout.popup_play_list, null);
                popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(this, 400));
                initPopupWindow();
                showPlayList();
                popupWindow.showAtLocation(ll_content_parent, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.ib_play_pause:
                switch (binder.getPlayState()) {
                    case Constant.ON_PLAY:
                        ibPlayPause.setImageResource(R.drawable.ic_play_arrow_black_36dp);
                        startService(PlayMusicService.pauseIntent(this));
                        break;
                    case Constant.ON_PAUSE:
                        ibPlayPause.setImageResource(R.drawable.ic_pause_black_36dp);
                        startService(PlayMusicService.continueIntent(this));
                        break;
                    case Constant.ON_STOP:
                        ibPlayPause.setImageResource(R.drawable.ic_pause_black_36dp);
                        if (binder.getPlayList() != null && binder.getPlayList().size() > 0) {
                            startService(PlayMusicService.playLocalIntent(this, binder.getCurrentPlayPosition()));
                        } else {
                            TUtils.showShort(R.string.empty_list);
                            ibPlayPause.setImageResource(R.drawable.ic_play_arrow_black_36dp);
                        }
                        break;

                }
            case R.id.ib_clear:
                et_search.setText(" ");
                break;
            case R.id.ib_play_next:
                if (binder != null) {
                    binder.playNextSong();
                }
                break;
        }
    }

    private void showPlayList() {
        tv_loop_text = (TextView) popupView.findViewById(R.id.tv_loop_text);
        ll_loop_style = (LinearLayout) popupView.findViewById(R.id.ll_loop_style);
        tv_collect = (TextView) popupView.findViewById(R.id.tv_collect);
        tv_clear = (TextView) popupView.findViewById(R.id.tv_clear);
        rv_list = (RecyclerView) popupView.findViewById(R.id.rv_list);

        rv_list.setLayoutManager(new LinearLayoutManager(this));
        rv_list.addItemDecoration(new SpacesItemDecoration(DensityUtils.dp2px(this, 1), 0));
        playListAdapter = new PopupPlayListAdapter(this, binder.getPlayList());
        rv_list.setAdapter(playListAdapter);
        if (binder != null && binder.getPlayList().size() > 0) {
            rv_list.scrollToPosition(binder.getCurrentPlayPosition());
        }
        updateLoopInfo(binder.getLoopType());

        ll_loop_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (binder.getLoopType()) {
                    case Constant.LIST_LOOP:
                        binder.setLoopType(Constant.SINGLE_LOOP);
                        break;
                    case Constant.SINGLE_LOOP:
                        binder.setLoopType(Constant.RANDOM_PLAY);
                        break;
                    case Constant.RANDOM_PLAY:
                        binder.setLoopType(Constant.LIST_LOOP);
                        break;
                }
                updateLoopInfo(binder.getLoopType());
            }
        });

        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binder.clearPlayList();
                playListAdapter.notifyDataSetChanged();
            }
        });

        tv_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (BaseSongBean bean : binder.getPlayList()) {
                    bean.setCollect(true);
                }
                ListenApplication.fixedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        DBManager.getInstance().insertSong(binder.getPlayList());
                    }
                });
            }
        });

    }

    private void updateLoopInfo(int loopType) {
        switch (loopType) {
            case Constant.LIST_LOOP:
                tv_loop_text.setText(R.string.list_loop);
                break;
            case Constant.SINGLE_LOOP:
                tv_loop_text.setText(R.string.single_loop);
                break;
            case Constant.RANDOM_PLAY:
                tv_loop_text.setText(R.string.random_play);
                break;
        }
    }

    private void initPopupWindow() {
        popupWindow.setContentView(popupView);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setFocusable(true);
        setBgAlpha(0.7f);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setClippingEnabled(false);
        popupWindow.setAnimationStyle(R.style.AnimUpDown);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBgAlpha(1f);
                popupWindow.setFocusable(false);
            }
        });
    }

    void setBgAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

    private void updatePlayState(int playState) {
        switch (playState) {
            case Constant.ON_PLAY:
                ibPlayPause.setImageResource(R.drawable.ic_pause_black_36dp);
                break;
            case Constant.ON_PAUSE:
            case Constant.ON_STOP:
                ibPlayPause.setImageResource(R.drawable.ic_play_arrow_black_36dp);
                break;
        }
    }
}
