package com.youtube.application.youtubeapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;


//public class YouTubePlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, Toolbar.OnMenuItemClickListener ,SearchView.OnQueryTextListener{
public class YouTubePlayerActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener, Toolbar.OnMenuItemClickListener {


    public static final String YOUTUBE_API_KEY = "AIzaSyA8QLCTtt_IboYB6ebIdchMk_e0aAJd1_o";
    String VIDEO_ID = "i0p1bmr0EmE";
    private YouTubePlayer mYouTubePlayer = null;
    private Toolbar mToolbar;
    private int mPlayState;
    private RequestSearchVideoFragment mRequestFragment;
    private SearchView mSearchView;

    private final int PLAY = 0;
    private final int PAUSE = 1;


    private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String searchWord) {
            Log.v("SearchView", searchWord);
            Toast.makeText(YouTubePlayerActivity.this, "Start Search!!", Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube_player);

        findView();

        //Toolbar設定
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.menu_player);
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(this);

        //Fragment生成
        mRequestFragment = new RequestSearchVideoFragment();

        //FragmentManager生成
        FragmentManager manager = getFragmentManager();
        // FragmentTransaction を開始
        FragmentTransaction transaction = manager.beginTransaction();
        // FragmentContainer のレイアウトに、 を割当てる
        transaction.replace(R.id.container, mRequestFragment)
                .addToBackStack(null)
                .commit();

    }

    private void findView() {
//        YouTubePlayerView mPlayerView = (YouTubePlayerView) findViewById(R.id.youtube);
//        mPlayerView.initialize(YOUTUBE_API_KEY, this);
        YouTubePlayerSupportFragment frag =
                (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube);
        frag.initialize(YOUTUBE_API_KEY, this);
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
            mYouTubePlayer.loadVideo(VIDEO_ID);
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
        }
        return true;
    }
}
