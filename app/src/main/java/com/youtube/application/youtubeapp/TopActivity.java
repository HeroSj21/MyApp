package com.youtube.application.youtubeapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TopActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn;
    Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        if(!networkCheck(this)){
            Toast.makeText(this, "No Signal", Toast.LENGTH_SHORT).show();
            startWifiSetting();
        }

        btn = (Button)findViewById(R.id.play);
        btn.setOnClickListener(this);

        btn2 = (Button) findViewById(R.id.search);
        btn2.setOnClickListener(this);
    }

    public boolean networkCheck(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if(info != null){
            return info.isConnected();
        }else{
            return false;
        }
    }

    public void startWifiSetting(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_WIFI_SETTINGS);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.play:
                intent = new Intent(this, YouTubePlayerActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.search:
                intent = new Intent(this, SearchVideoActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
