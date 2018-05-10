package com.example.micha.zajecia7;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class zajecia7 extends AppCompatActivity {

    private MediaRecorder myAudioRecorder;
    private MediaPlayer mediaPlayer;
    private String mFileName;
    private static final int RECORD_REQUEST_CODE = 101;
    public String TAG;
    int numer=0;
    boolean byl=false;
    private Button rec;
    private Button stop;
    private  Button play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zajecia7);

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED || permissionCheck2 !=
                PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied");
            makeRequest();
        }

        myAudioRecorder = new MediaRecorder();

        mFileName = Environment.getDataDirectory().getAbsolutePath();
        //mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(mFileName);

        rec=(Button)findViewById(R.id.rec);
        stop=(Button)findViewById(R.id.stop);
        play=(Button)findViewById(R.id.play);

        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recording();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Stop();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Play();
            }
        });
    }

    public void Recording(){
        if(numer!=0) {
            Stop();
        }

        try {
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        numer=1;
    }

    public void Stop(){
        if(numer==1) {
            myAudioRecorder.stop();
            myAudioRecorder.release();
            myAudioRecorder = null;
        }

        if(numer==2) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        numer=0;
    }

    public void Play(){
        if(numer!=0) {
            Stop();
        }

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(mFileName);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        numer=2;
    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                RECORD_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RECORD_REQUEST_CODE: {
                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED || grantResults[1] !=
                        PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission has been denied by user");
                }
                return;
            }
        }
    }
}
