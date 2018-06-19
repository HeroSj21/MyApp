package com.youtube.application.youtubeapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class TopActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        if (!networkCheck(this)) {
            Toast.makeText(this, "No Signal", Toast.LENGTH_SHORT).show();
            startWifiSetting();
        }

        Intent intent = new Intent(this, YouTubeSearchActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean networkCheck(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = null;
        if (cm != null) {
            info = cm.getActiveNetworkInfo();
        }
        if (info != null) {
            return info.isConnected();
        } else {
            return false;
        }
    }

    public void startWifiSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_WIFI_SETTINGS);
        startActivity(intent);
    }

}
