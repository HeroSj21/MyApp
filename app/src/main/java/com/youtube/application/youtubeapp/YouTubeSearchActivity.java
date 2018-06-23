package com.youtube.application.youtubeapp;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.api.services.youtube.model.SearchResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class YouTubeSearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    static final String TAG = YouTubeSearchActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;
    private RequestSearchVideoFragment mRequestFragment;
    private SearchView mSearchView;
    private VideoSearchAsyncTask mSearchAsyncTask;
    private List<SearchResult> mResultList;
    private VideoListFragment mVideoListFragment;

    private CallbackSearchAsyncTask mCallBackSearchVideo = new CallbackSearchAsyncTask() {
        @Override
        public void callbackSearchVideo(List<HashMap<String, String>> results) {
            mVideoListFragment = new VideoListFragment();
            mVideoListFragment.setList(results);
            //画像取得処理を行う
            getBitmap(results);

        }
    };

    private CallbackImageAsyncTask mCallbackImage = new CallbackImageAsyncTask() {
        @Override
        public void callbackImage(List<Bitmap> bitmap) {
            Log.v(TAG, bitmap.toString());
            mVideoListFragment.setBitmapList(bitmap);
            mFragmentManager = getSupportFragmentManager();
            mTransaction = mFragmentManager.beginTransaction();
            mTransaction.remove(mRequestFragment);
            mTransaction.replace(R.id.search_video, mVideoListFragment)
                    .commit();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_video);

        mRequestFragment = new RequestSearchVideoFragment();

        mFragmentManager = getSupportFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.replace(R.id.search_video, mRequestFragment)
                .commit();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.menu_search);
        setSupportActionBar(mToolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setQueryHint("Search");
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String searchWord) {
        if (searchWord.equals("")) {
            Toast.makeText(YouTubeSearchActivity.this, "Please Type Words!!", Toast.LENGTH_SHORT).show();
        } else {
            //検索ボタンが呼ばれた時の処理
            mSearchAsyncTask = new VideoSearchAsyncTask();
            mSearchAsyncTask.setCallback(mCallBackSearchVideo);
            mSearchAsyncTask.setSearchWord(searchWord);
            mSearchAsyncTask.execute();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void getBitmap(List<HashMap<String, String>> result) {
        List<String> urlList = new ArrayList<>();
        for (HashMap<String, String> map : result) {
            String url = map.get(StringContainer.KEY_VIDEO_THUMBNAILS);
            urlList.add(url);
        }
        Toast.makeText(this, urlList.toString(), Toast.LENGTH_SHORT).show();

        ImageSearchAsyncTask imageSearchAsyncTask = new ImageSearchAsyncTask();
        imageSearchAsyncTask.setCallback(mCallbackImage);
        imageSearchAsyncTask.setUrlList(urlList);
        imageSearchAsyncTask.execute();
    }
}
