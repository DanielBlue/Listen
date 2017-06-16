package com.maoqi.listen.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maoqi.listen.R;
import com.maoqi.listen.adapter.CloudResultListAdapter;
import com.maoqi.listen.adapter.SpacesItemDecoration;
import com.maoqi.listen.bean.CloudSongBean;
import com.maoqi.listen.model.PlayControllerCallback;
import com.maoqi.listen.util.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maoqi on 2017/6/15.
 */

public class CloudResultFragment extends Fragment{
    private List<CloudSongBean> data = new ArrayList<>();
    private RecyclerView rv_list;
    private CloudResultListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result_list,null);
        rv_list = (RecyclerView) view.findViewById(R.id.rv_list);
        adapter = new CloudResultListAdapter(getActivity(),data,(PlayControllerCallback) getActivity());
        rv_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_list.addItemDecoration(new SpacesItemDecoration(0,0,0, DensityUtils.dp2px(getActivity(),1)));
        rv_list.setAdapter(adapter);
        return view;
    }

    public void refreshList(List<CloudSongBean> list){
        data.clear();
        data.addAll(list);
        adapter.notifyDataSetChanged();
    }
}
