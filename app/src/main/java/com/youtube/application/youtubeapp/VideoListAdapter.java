package com.youtube.application.youtubeapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoListAdapter extends BaseAdapter {

    public static final String TAG = VideoListAdapter.class.getSimpleName();

    private LayoutInflater mInflater;
    private List<HashMap<String, String>> mVideoList;
    private List<Bitmap> mBitmapList;
    private View mView;
    private Bitmap mBitmap;
    private ImageView mImageView;

    public VideoListAdapter(Context context, List<HashMap<String, String>> list, List<Bitmap> bitmapList) {
        Log.v(TAG, "Context - context = " + context);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mVideoList = list;
        mBitmapList = bitmapList;
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
        mView = view;
        if (mView == null) {
            Log.v(TAG, "getView - View = null");
            mView = mInflater.inflate(R.layout.listview_icon, viewGroup, false);
            Log.v(TAG, "getView - View = " + mView);
        }
        TextView titleText = (TextView) mView.findViewById(R.id.video_title);
        mImageView = (ImageView) mView.findViewById(R.id.video_image);
        mImageView.setImageResource(R.drawable.video_image);
        Map<String, String> videoInfo = getItem(position);
        String title = videoInfo.get(StringContainer.KEY_VIDEO_TITLE);

        titleText.setText(title);
        mImageView.setImageBitmap(mBitmapList.get(position));

        return mView;
    }
}
