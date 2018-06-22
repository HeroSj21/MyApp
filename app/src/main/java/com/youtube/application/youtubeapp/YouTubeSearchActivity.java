package com.youtube.application.youtubeapp;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.api.services.youtube.model.SearchResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class YouTubeSearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Toolbar mToolbar;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;
    private RequestSearchVideoFragment mRequestFragment;
    private SearchView mSearchView;
    private VideoSearchAsyncTask mSearchAsyncTask;
    private List<SearchResult> mResultList;

    private CallbackSearchAsyncTask mCallBack = new CallbackSearchAsyncTask() {
        @Override
        public void callbackSearchVideo(ArrayList<HashMap<String, String>> results) {
            Toast.makeText(YouTubeSearchActivity.this, results.toString(), Toast.LENGTH_SHORT).show();
            VideoListFragment videoListFragment = new VideoListFragment();
            videoListFragment.setList(results);
            mFragmentManager = getSupportFragmentManager();
            mTransaction = mFragmentManager.beginTransaction();
            mTransaction.remove(mRequestFragment);
            mTransaction.replace(R.id.search_video, videoListFragment)
                    .commit();
        }
//        @Override
//        public void callbackSearchVideo(String s) {
//            Toast.makeText(YouTubeSearchActivity.this, s, Toast.LENGTH_SHORT).show();

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
            mSearchAsyncTask.setCallback(mCallBack);
            mSearchAsyncTask.setSearchWord(searchWord);
            mSearchAsyncTask.execute();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
