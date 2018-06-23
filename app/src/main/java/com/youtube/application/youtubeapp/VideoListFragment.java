package com.youtube.application.youtubeapp;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

public class VideoListFragment extends Fragment implements AdapterView.OnItemClickListener {

    private final static String TAG = VideoListFragment.class.getSimpleName();

    private View mView;
    private VideoListAdapter mAdapter;
    private List<HashMap<String, String>> mVideoList;
    private List<Bitmap> mBitmapList;
    private ListView mListView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.v(TAG, "onCreateView");
        mView = inflater.inflate(R.layout.search_result_listview, container, false);
        mListView = (ListView) mView.findViewById(R.id.video_list);
        mAdapter = new VideoListAdapter(getContext(), mVideoList, mBitmapList);
        mListView.setOnItemClickListener(this);
        mListView.setAdapter(mAdapter);
        return mView;
    }


    public void setList(List<HashMap<String, String>> list) {
        Log.v(TAG, "setList");
        mVideoList = list;
    }

    public void setBitmapList(List<Bitmap> bitmaps) {
        Log.v(TAG, "setBitmapList");
        mBitmapList = bitmaps;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.v(TAG, "onItemClick Info == " + mAdapter.getItem(i));
        HashMap<String, String> videoInfo = mAdapter.getItem(i);
        Intent intent = new Intent(getActivity(), YouTubePlayerActivity.class);
        intent.putExtra(StringContainer.KEY_CHOSEN_VIDEO, videoInfo);
        startActivity(intent);
        getActivity().finish();
        Toast.makeText(getActivity(), videoInfo.get(StringContainer.KEY_VIDEO_TITLE), Toast.LENGTH_SHORT).show();
    }
}
