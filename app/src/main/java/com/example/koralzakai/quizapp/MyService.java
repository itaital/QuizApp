package com.example.koralzakai.quizapp;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MyService extends Service {
    MediaPlayer myPlayer;

    @Override
    public void onCreate() {
        Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();
        myPlayer = MediaPlayer.create(this, R.raw.song);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        int songNum = intent.getIntExtra("extraSongNum", 1);
        if(songNum == 1)
        {
            myPlayer = MediaPlayer.create(this, R.raw.song);
        }
        else if(songNum == 2)
        {
            myPlayer = MediaPlayer.create(this, R.raw.song2);
        }
        else if(songNum == 3)
        {
            myPlayer = MediaPlayer.create(this, R.raw.song3);
        }
        else if(songNum == 4)
        {
            myPlayer = MediaPlayer.create(this, R.raw.song4);
        }

        myPlayer.setLooping(false); // Set looping
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        myPlayer.start();
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        myPlayer.start();
    }
    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
        myPlayer.stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}