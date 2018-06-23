package com.youtube.application.youtubeapp;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class YouTubePlayerActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener, Toolbar.OnMenuItemClickListener {

    public static final String YOUTUBE_API_KEY = "AIzaSyA8QLCTtt_IboYB6ebIdchMk_e0aAJd1_o";
    public static final String TAG = YouTubePlayerActivity.class.getSimpleName();

    private String mVideo_ID;
    private HashMap<String, String> mVideoInfo;
    private YouTubePlayer mYouTubePlayer = null;
    private Toolbar mToolbar;
    private int mPlayState;
    private RequestSearchVideoFragment mRequestFragment;
    private VideoListFragment mVideoListFragment;
    private SearchView mSearchView;
    private VideoSearchAsyncTask mSearchAsyncTask;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    private final int PLAY = 0;
    private final int PAUSE = 1;


    private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String searchWord) {
            Log.v(TAG, "SearchView Word == " + searchWord);
            Toast.makeText(YouTubePlayerActivity.this, "Start Search!!", Toast.LENGTH_SHORT).show();
            mSearchAsyncTask = new VideoSearchAsyncTask();
            mSearchAsyncTask.setCallback(mCallBack);
            mSearchAsyncTask.setSearchWord(searchWord);
            mSearchAsyncTask.execute();
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };

    private CallbackSearchAsyncTask mCallBack = new CallbackSearchAsyncTask() {
        @Override
        public void callbackSearchVideo(List<HashMap<String, String>> results) {
            Log.v(TAG, "callbackSearchVideo == " + results);
            mVideoListFragment = new VideoListFragment();
            mVideoListFragment.setList(results);
            getBitmap(results);
        }
//        @Override
//        public void callbackSearchVideo(String s) {
//            Toast.makeText(YouTubeSearchActivity.this, s, Toast.LENGTH_SHORT).show();

    };

    private CallbackImageAsyncTask mCallbackImage = new CallbackImageAsyncTask() {
        @Override
        public void callbackImage(List<Bitmap> bitmap) {
            Log.v(TAG, "callbackImage : " + bitmap.toString());
            mVideoListFragment.setBitmapList(bitmap);
            mFragmentManager = getSupportFragmentManager();
            mTransaction = mFragmentManager.beginTransaction();
            mTransaction.remove(mRequestFragment);
            mTransaction.replace(R.id.container, mVideoListFragment)
                    .commit();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v(TAG, "onCreate start");

        Intent intent = getIntent();
        mVideoInfo = (HashMap<String, String>) intent.getSerializableExtra(StringContainer.KEY_CHOSEN_VIDEO);
        Log.v(TAG, "onCreate VideoInfo == " + mVideoInfo);

        if (mVideoInfo != null) {
            mVideo_ID = mVideoInfo.get(StringContainer.KEY_VIDEO_ID);
        }
        setContentView(R.layout.activity_you_tube_player);

        findView();

        //Toolbar設定
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.menu_player);
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(this);

        //Fragment生成
        mRequestFragment = new RequestSearchVideoFragment();
        mVideoListFragment = new VideoListFragment();

        setFragment(mRequestFragment);
    }

    private void findView() {
        YouTubePlayerSupportFragment frag =
                (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube);
        frag.initialize(YOUTUBE_API_KEY, this);
    }

    private void setFragment(Fragment fragment) {
        //FragmentManager生成
        mFragmentManager = getSupportFragmentManager();
        // FragmentTransaction を開始
        mTransaction = mFragmentManager.beginTransaction();

        // FragmentContainer のレイアウトに、 を割当てる
        mTransaction.replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void getBitmap(List<HashMap<String, String>> result) {
        List<String> urlList = new ArrayList<>();
        for (HashMap<String, String> map : result) {
            String url = map.get(StringContainer.KEY_VIDEO_THUMBNAILS);
            urlList.add(url);
        }
        ImageSearchAsyncTask imageSearchAsyncTask = new ImageSearchAsyncTask();
        imageSearchAsyncTask.setCallback(mCallbackImage);
        imageSearchAsyncTask.setUrlList(urlList);
        imageSearchAsyncTask.execute();
    }


    //YouTubePlayerの初期化成功時
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        mYouTubePlayer = youTubePlayer;

        if (!wasRestored) {
            //loadVideo(String videoId)
            //指定された動画を読み込んで再生します。

            //loadVideo(String videoId, int timeMillis)
            //指定された動画の指定時間までシークして読み込んで再生します。
            mYouTubePlayer.loadVideo(mVideo_ID);
        }

    }

    //YouTubePlayerの初期化失敗時
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider
                                                provider, YouTubeInitializationResult youTubeInitializationResult) {
        // 初期化に失敗したときの処理

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_player, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setQueryHint("Search");
        mSearchView.setOnQueryTextListener(onQueryTextListener);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        int time30s = 30000;

        switch (id) {
            case R.id.action_play:
                if (mPlayState == PLAY) {
                    item.setIcon(android.R.drawable.ic_media_pause);
                    //pause()
                    //
                    //現在再生中の動画を一時停止します。
                    mYouTubePlayer.pause();
                    mPlayState = PAUSE;
                    Toast.makeText(YouTubePlayerActivity.this, "Pause!!", Toast.LENGTH_SHORT).show();
                } else {
                    item.setIcon(android.R.drawable.ic_media_play);
                    //play()
                    //現在キューに入っている動画/読み込まれている動画の再生を開始します
                    mYouTubePlayer.play();
                    mPlayState = PLAY;
                    Toast.makeText(YouTubePlayerActivity.this, "Play!!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_rewind:
                //seekRelativeMillis(int milliSeconds)
                //指定された秒数だけ、前後にシークを行います。
                mYouTubePlayer.seekRelativeMillis(-time30s);
                Toast.makeText(YouTubePlayerActivity.this, "巻き戻し!!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_forward:
                //seekRelativeMillis(int milliSeconds)
                //指定された秒数だけ、前後にシークを行います。
                mYouTubePlayer.seekRelativeMillis(time30s);
                Toast.makeText(YouTubePlayerActivity.this, "早送り!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_settings:
                Intent intent = new Intent(this, YouTubeSearchActivity.class);
                startActivity(intent);
                finish();
        }
        return true;
    }


    //ハードウェアキーを取得
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.v(TAG, "dispatchKeyEvent : KetEvent == " + event.getKeyCode());
        switch (event.getKeyCode()) {
            //BackKey押下時はYouTubeSearchActivityに戻る
            case KeyEvent.KEYCODE_BACK:
                Intent intent = new Intent(this, YouTubeSearchActivity.class);
                startActivity(intent);
                finish();
                return true;
        }

        return super.dispatchKeyEvent(event);
    }
}
