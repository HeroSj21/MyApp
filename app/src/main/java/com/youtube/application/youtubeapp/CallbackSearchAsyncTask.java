package com.youtube.application.youtubeapp;

import com.google.api.services.youtube.model.SearchResult;

import java.util.List;

public interface CallbackSearchAsyncTask {

    void callbackSearchVideo(List<SearchResult> results);
//    void callbackSearchVideo(String results);

}
