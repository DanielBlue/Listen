package com.maoqi.listen.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maoqi.listen.Constant;
import com.maoqi.listen.R;
import com.maoqi.listen.adapter.SearchListAdapter;
import com.maoqi.listen.base.BaseToolbarActivity;
import com.maoqi.listen.bean.CloudSongBean;
import com.maoqi.listen.bean.QQSongBean;
import com.maoqi.listen.bean.XiamiSongBean;
import com.maoqi.listen.fragment.CloudResultFragment;
import com.maoqi.listen.fragment.QQResultFragment;
import com.maoqi.listen.fragment.XiamiResultFragment;
import com.maoqi.listen.model.PlayControllerCallback;
import com.maoqi.listen.service.PlayMusicService;
import com.maoqi.listen.util.KeyBoardUtils;
import com.maoqi.listen.util.TUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class MainActivity extends BaseToolbarActivity implements PlayControllerCallback, View.OnClickListener {

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
    private int playState = Constant.ON_PAUSE;
    private ImageView iv_clear;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        progress = createProgressDialog(R.string.loading);
        initData();
        ivAlbumArt = (ImageView) findViewById(R.id.iv_album_art);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvArtist = (TextView) findViewById(R.id.tv_artist);
        iv_clear = (ImageView) findViewById(R.id.iv_clear);
        ibPlayPause = (ImageButton) findViewById(R.id.ib_play_pause);
        ibList = (ImageButton) findViewById(R.id.ib_list);
        iv_clear.setOnClickListener(this);
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
                if (hasFocus){
                    iv_clear.setVisibility(View.VISIBLE);
                }else {
                    iv_clear.setVisibility(View.GONE);
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


        initPlayState();
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
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, PlayMusicService.class));
    }

    @Override
    public void updateSongInfo(String imgUrl, String songName, String artist) {
        Glide.with(this).load(imgUrl).into(ivAlbumArt);
        tvTitle.setText(songName);
        tvArtist.setText(artist);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_list:
                TUtils.showShort("列表功能还在开发中，莫点了");
                break;
            case R.id.ib_play_pause:
                updatePlayState();
                break;
            case R.id.iv_clear:
                et_search.setText("");
                break;
        }
    }

    public void updatePlayState() {
        Intent intent = new Intent(this,PlayMusicService.class);
        switch (playState){
            case Constant.ON_PLAY:
                ibPlayPause.setImageResource(R.drawable.ic_play_arrow_black_36dp);
                intent.putExtra(Constant.BEHAVIOR,Constant.BEHAVIOR_PAUSE);
                startService(intent);
                playState = Constant.ON_PAUSE;
                break;
            case Constant.ON_PAUSE:
                ibPlayPause.setImageResource(R.drawable.ic_pause_black_36dp);
                intent.putExtra(Constant.BEHAVIOR,Constant.BEHAVIOR_CONTINUE);
                startService(intent);
                playState = Constant.ON_PLAY;
                break;
        }
    }

    private void initPlayState() {
        switch (playState){
            case Constant.ON_PLAY:
                ibPlayPause.setImageResource(R.drawable.ic_pause_black_36dp);
                break;
            case Constant.ON_PAUSE:
                ibPlayPause.setImageResource(R.drawable.ic_play_arrow_black_36dp);
                break;
        }
    }

    public void setPlayState(int playState){
        this.playState = playState;
        initPlayState();
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_search, menu);
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.menu_search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconifiedByDefault(true);
//        return super.onCreateOptionsMenu(menu);
//    }
}
