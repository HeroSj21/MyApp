package com.youtube.application.youtubeapp;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

public class VideoListFragment extends Fragment implements AdapterView.OnItemClickListener{

    private View mView;
    private VideoListAdapter mAdapter;
    private List<HashMap<String, String>> mVideoList;
    private ListView mListView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.search_result_listview, container, false);
        mListView = (ListView)mView.findViewById(R.id.video_list);
        mAdapter = new VideoListAdapter(getContext(), mVideoList);
        mListView.setOnItemClickListener(this);
        mListView.setAdapter(mAdapter);
        return mView;
    }


    public void setList(List<HashMap<String, String>> list){
        mVideoList = list;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        HashMap<String, String> videoInfo = mAdapter.getItem(i);
        Intent intent = new Intent(getActivity(), YouTubePlayerActivity.class);
        intent.putExtra(YouTubeKeys.KEY_CHOSEN_VIDEO, videoInfo);
        startActivity(intent);
        getActivity().finish();
        Toast.makeText(getActivity(), videoInfo.get(YouTubeKeys.KEY_VIDEO_TITLE), Toast.LENGTH_SHORT).show();
    }
}
