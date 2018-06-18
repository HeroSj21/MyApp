package com.youtube.application.youtubeapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.api.services.youtube.model.SearchResult;

import java.util.List;

public class SearchVideoActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private Toolbar mToolbar;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;
    private RequestSearchVideoFragment mRequestFragment;
    private SearchView mSearchView;
//    private Intent mSearchIntent;
    //    private IntentFilter mIntentFilter;
    private SearchVideoAsyncTask mSearchAsyncTask;
    private List<SearchResult> mResultList;
//    public VideoListReceiver mReceiver;

    private CallbackSearchAsyncTask mCallBack = new CallbackSearchAsyncTask() {
        @Override
        public void callbackSearchVideo(String s) {
            Toast.makeText(SearchVideoActivity.this, s, Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_video);

        mRequestFragment = new RequestSearchVideoFragment();

        mFragmentManager = getFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.replace(R.id.search_video, mRequestFragment)
                .commit();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.menu_search);
        setSupportActionBar(mToolbar);

//        mSearchIntent = new Intent(this, SearchVideoActivity.class);
//        mSearchIntent.setAction(YouTubeKeys.ACTION_SEARCH);
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
            Toast.makeText(SearchVideoActivity.this, "Please Type Words!!", Toast.LENGTH_SHORT).show();
        } else {
            //検索ボタンが呼ばれた時の処理
//            Toast.makeText(SearchVideoActivity.this, "Start Search!!", Toast.LENGTH_SHORT).show();

            mSearchAsyncTask = new SearchVideoAsyncTask();
            mSearchAsyncTask.setCallback(mCallBack);
            mSearchAsyncTask.setSearchWord(searchWord);
            mSearchAsyncTask.execute();

//            mSearchIntent.putExtra(YouTubeKeys.SEARCH_WORD, searchWord);
//            startService(mSearchIntent);
//            mReceiver = new VideoListReceiver();
//            mIntentFilter = new IntentFilter();
//            mIntentFilter.addAction("UPDATE_ACTION");
//            registerReceiver(mReceiver, mIntentFilter);
//
//            mReceiver.registerHandler(updateHandler);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
