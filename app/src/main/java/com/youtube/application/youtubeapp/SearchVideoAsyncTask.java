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
import java.util.List;

public class SearchVideoAsyncTask extends AsyncTask<String, String, List<SearchResult>>{

    private static final long NUMBER_OF_VIDEOS_RETURNED = 15;

    private YouTube mYoutube;
    private YouTube.Search.List mSearch;
    private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private JsonFactory JSON_FACTORY = new JacksonFactory();

    private CallbackSearchAsyncTask mCallback;
    private List<SearchResult> mSearchResultList;
    private String mSearchWord;

    static final String TAG = SearchVideoAsyncTask.class.getSimpleName();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<SearchResult> doInBackground(String... strings) {

        SearchListResponse response = new SearchListResponse();
        try {
            initializeYouTube();
            mSearch.setQ(mSearchWord);
            response = mSearch.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response != null){
            mSearchResultList = response.getItems();
            Log.v(TAG, mSearchResultList.toString());
        }

        return mSearchResultList;
    }

    @Override
    protected void onPostExecute(List<SearchResult> results) {
        mCallback.callbackSearchVideo(results);
    }

    public void setCallback(CallbackSearchAsyncTask callback){
        mCallback  = callback;
    }

    public void setSearchWord(String s){
        mSearchWord = s;
    }

    private void initializeYouTube() throws IOException{
        Log.v(TAG, "initializeYouTube");
        //YouTubeのインスタンス
        mYoutube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName("youtube-cmdline-search-sample").build();


        mSearch = mYoutube.search().list("id,snippet");
        //YouTubeAPI Keyを設定
        mSearch.setKey(YouTubeKeys.YOUTUBE_API_KEY);
        mSearch.setType("video");
        mSearch.setFields("items(id/videoId,snippet/title,snippet/thumbnails/default/url)");
        mSearch.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
    }

}
