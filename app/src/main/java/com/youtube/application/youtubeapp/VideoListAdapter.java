package com.youtube.application.youtubeapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class VideoListAdapter extends BaseAdapter {

    public static final String TAG = VideoListAdapter.class.getSimpleName();

    private LayoutInflater mInflater;
    private List<HashMap<String, String>> mVideoList;

    public VideoListAdapter(Context context, List<HashMap<String, String>> list){
        Log.v(TAG, "Context - context = " + context);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mVideoList = list;
    }


    @Override
    public int getCount() {
        Log.v(TAG, "getCount - Count = " + mVideoList.size());
        return mVideoList.size();
    }

    @Override
    public HashMap<String, String> getItem(int i) {
        Log.v(TAG, "getItem - Item = " + mVideoList.get(i).toString());
        return mVideoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        Log.v(TAG, "getItemId - ItemId = " + i);
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            Log.v(TAG, "getView - View = null");
            view = mInflater.inflate(R.layout.listview_icon, viewGroup, false);
            Log.v(TAG, "getView - View = " +  view);
        }
        TextView titleText = (TextView) view.findViewById(R.id.video_title);
        HashMap<String, String> videoInfo = getItem(position);
        String title = videoInfo.get(YouTubeKeys.KEY_VIDEO_TITLE);
//        String thumbnail = videoInfo.get(YouTubeKeys.KEY_VIDEO_THUMBNAILS);

        titleText.setText(title);

        return view;
    }
}
