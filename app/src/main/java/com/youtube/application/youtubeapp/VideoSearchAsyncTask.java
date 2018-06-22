package com.youtube.application.youtubeapp;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VideoSearchAsyncTask extends AsyncTask<String, String, ArrayList<HashMap<String, String>>> {

    private static final long NUMBER_OF_VIDEOS_RETURNED = 15;
    private final static int NUM_VIDEO_INFO = 3;


    private YouTube mYoutube;
    private YouTube.Search.List mSearch;
    private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private JsonFactory JSON_FACTORY = new JacksonFactory();

    private CallbackSearchAsyncTask mCallback;
    private List<SearchResult> mSearchResultList;
    private String mSearchWord;

    static final String TAG = VideoSearchAsyncTask.class.getSimpleName();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<HashMap<String, String>> doInBackground(String... strings) {

        SearchListResponse response = new SearchListResponse();
        try {
            initializeYouTube();
            mSearch.setQ(mSearchWord);
            response = mSearch.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null) {
            mSearchResultList = response.getItems();
            Log.v(TAG, mSearchResultList.toString());
        }


        return makeList(mSearchResultList);
    }

    @Override
    protected void onPostExecute(ArrayList<HashMap<String, String>> results) {
        mCallback.callbackSearchVideo(results);
    }

    public void setCallback(CallbackSearchAsyncTask callback) {
        mCallback = callback;
    }

    public void setSearchWord(String s) {
        mSearchWord = s;
    }

    private void initializeYouTube() throws IOException {
        Log.v(TAG, "initializeYouTube");
        //YouTubeのインスタンス
        mYoutube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName("youtube-cmdline-search-sample").build();

        //
        mSearch = mYoutube.search().list("id,snippet");
        //YouTubeAPI Keyを設定
        mSearch.setKey(YouTubeKeys.YOUTUBE_API_KEY);
        //
        mSearch.setType("video");
        //
        mSearch.setFields("items(id/videoId,snippet/title,snippet/thumbnails/default/url)");
        //検索件数
        mSearch.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
    }

    public ArrayList<HashMap<String, String>> makeList(List<SearchResult> results) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        for (SearchResult s : results) {
            HashMap<String, String> videoInfo = new HashMap<>();
            //タイトル
            videoInfo.put(YouTubeKeys.KEY_VIDEO_TITLE, s.getSnippet().getTitle());
            //VideoID
            videoInfo.put(YouTubeKeys.KEY_VIDEO_ID, s.getId().getVideoId());
            //サムネイルURL
            videoInfo.put(YouTubeKeys.KEY_VIDEO_THUMBNAILS, s.getSnippet().getThumbnails().getDefault().getUrl());

            list.add(videoInfo);
        }
        return list;
    }

}
