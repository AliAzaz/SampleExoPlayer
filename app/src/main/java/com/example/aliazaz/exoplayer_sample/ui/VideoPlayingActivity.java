package com.example.aliazaz.exoplayer_sample.ui;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.aliazaz.exoplayer_sample.R;
import com.example.aliazaz.exoplayer_sample.other.CopyRaw;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.Util;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.io.File;
import java.util.ArrayList;

public class VideoPlayingActivity extends AppCompatActivity {

    private static final String TAG = VideoPlayingActivity.class.getName();
    TextView txtName;
    PlayerView playerView;
    SimpleExoPlayer exoPlayerInstance;
    String Directory;
    DataSource.Factory dataSourceFactory;
    Long playbackPosition = new Long(0);
    int currentWindow = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_playing);

        settingComponents();
        verifyPermission();
    }

    private void settingComponents() {

        playerView = findViewById(R.id.player_view);
        txtName = findViewById(R.id.txtName);

        //Setup Directory
        Directory = Environment.getExternalStorageDirectory() + File.separator + "EXOPLAYER-SAMPLE";
        // Produces DataSource instances through which media data is loaded.
        dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "simpleExoPlayer"));
    }

    private void setupExoPlayer() {
        //Setup Exoplayer
        exoPlayerInstance = ExoPlayerFactory.newSimpleInstance(this);

        // This is the MediaSource representing the media to be played.

        // Getting media from sdcard storage
        MediaSource firstSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(Directory + File.separator + "landscape2"));

        // Getting media from raw resource
        MediaSource secondSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(RawResourceDataSource.buildRawResourceUri(R.raw.landscape));

         /* If we've more then one video then use this concept
         otherwise:

            exoPlayerInstance.prepare(firstSource);

         Plays the first video, then the second video.*/
        ConcatenatingMediaSource concatenatedSource = new ConcatenatingMediaSource(firstSource, secondSource);

        // Prepare the exoPlayerInstance with the source.
        exoPlayerInstance.prepare(concatenatedSource);

    }

    private void settingListener() {

        exoPlayerInstance.addListener(new Player.EventListener() {
            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                // Set video name in textview
                txtName.setText("Playing: " + getVideoName()[exoPlayerInstance.getCurrentWindowIndex()].toUpperCase());
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                exoPlayerInstance.retry();
            }
        });

    }

    private void addVideoInFolder() {
        CopyRaw.run(this, Directory, R.raw.landscape2);
    }

    private String[] getVideoName() {
        return new String[]{"landscape2", "landscape"};
    }

    public void verifyPermission() {

        //Check permission for devices API > 22
        Permissions.check(this/*context*/, Manifest.permission.WRITE_EXTERNAL_STORAGE, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                addVideoInFolder();
                setupExoPlayer();
                settingListener();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
                verifyPermission();
            }
        });

    }

    private void stopPlayer() {
        if (exoPlayerInstance != null) {
            playbackPosition = exoPlayerInstance.getCurrentPosition();
            currentWindow = exoPlayerInstance.getCurrentWindowIndex();
            exoPlayerInstance.setPlayWhenReady(false);
            playerView.onResume();
        }
    }

    private void restartPlayer() {
        playerView.setPlayer(exoPlayerInstance);
        exoPlayerInstance.seekTo(currentWindow, playbackPosition);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (exoPlayerInstance != null) {
            restartPlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            stopPlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            stopPlayer();
        }
    }

}
