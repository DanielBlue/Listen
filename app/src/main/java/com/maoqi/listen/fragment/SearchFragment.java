package com.maoqi.listen.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.maoqi.listen.R;
import com.maoqi.listen.base.BaseToolbarFragment;


public class SearchFragment extends BaseToolbarFragment {


    RecyclerView rvList;
    Toolbar tbToolbar;

    @Override
    protected void initData() {

    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_search, null);
//        initToolbar(tbToolbar);
        tbToolbar.inflateMenu(R.menu.menu_search);
        initSearchView(tbToolbar.getMenu());
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        initSearchView(menu);
    }

    private void initSearchView(Menu menu) {
        SearchManager searchManager =
                (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(activity.getComponentName()));
        searchView.setIconifiedByDefault(true);
    }
}
