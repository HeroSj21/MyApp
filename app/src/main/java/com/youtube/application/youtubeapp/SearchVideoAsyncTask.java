package com.youtube.application.youtubeapp;

import android.os.AsyncTask;

import com.google.api.services.youtube.model.SearchResult;

import java.util.List;

//public class SearchVideoAsyncTask extends AsyncTask<String, String, List<SearchResult> {
public class SearchVideoAsyncTask extends AsyncTask<String, String, String> {

    CallbackSearchAsyncTask mCallback;
    String mSearchWord;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

//    @Override
//    protected List<SearchResult> doInBackground(String... strings) {
//        return null;
//    }

    @Override
    protected String doInBackground(String... strings) {
        return "Success!!";
    }

//    @Override
//    protected void onPostExecute(List<SearchResult> results) {
//        mCallback.callbackSearchVideo(results);
//    }

    @Override
    protected void onPostExecute(String s) {
        mCallback.callbackSearchVideo(s);
    }

    public void setCallback(CallbackSearchAsyncTask callback){
        mCallback  = callback;
    }

    public void setSearchWord(String s){
        mSearchWord = s;
    }

}
