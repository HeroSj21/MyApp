package com.youtube.application.youtubeapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImageSearchAsyncTask extends AsyncTask<Void, Void, List<Bitmap>> {

    private CallbackImageAsyncTask mCallback;
    private List<String> mUrlList = new ArrayList<>();

    @Override
    protected final List<Bitmap> doInBackground(Void... voids) {

        List<Bitmap> bitmapResult = new ArrayList<>();
        for (String urlString : mUrlList) {
            try {
                URL url = new URL(urlString);
                InputStream is = url.openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                bitmapResult.add(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmapResult;
    }

    @Override
    protected void onPostExecute(List<Bitmap> results) {
        mCallback.callbackImage(results);
    }

    void setCallback(CallbackImageAsyncTask callback) {
        mCallback = callback;
    }

    void setUrlList(List<String> list) {
        mUrlList = list;
    }
}
