package com.example.aliazaz.exoplayer_sample;

import android.Manifest;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    TextView txtName;
    PlayerView playerView;
    SimpleExoPlayer player;
    String Directory;
    DataSource.Factory dataSourceFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingComponents();
        verifyPermission();
    }

    private void settingComponents() {
        setContentView(R.layout.activity_main);
        playerView = findViewById(R.id.player_view);
        txtName = findViewById(R.id.txtName);

        //Setup Exoplayer
        player = ExoPlayerFactory.newSimpleInstance(this);

        //Setup Directory
        Directory = Environment.getExternalStorageDirectory() + File.separator + "EXOPLAYER-SAMPLE";

        // Produces DataSource instances through which media data is loaded.
        dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "simpleExoPlayer"));
    }

    private void setupExoPlayer() {
        // Bind the player to the view.
        playerView.setPlayer(player);

        // This is the MediaSource representing the media to be played.
        MediaSource firstSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(Directory + File.separator + "landscape2"));

        MediaSource secondSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(RawResourceDataSource.buildRawResourceUri(R.raw.landscape));

        // Plays the first video, then the second video.
        ConcatenatingMediaSource concatenatedSource = new ConcatenatingMediaSource(firstSource, secondSource);

        // Prepare the player with the source.
        player.prepare(concatenatedSource);

    }

    private void settingListener() {

        player.addListener(new Player.EventListener() {
            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                // Set video name in textview
                txtName.setText("Playing: " + getVideoName()[player.getCurrentWindowIndex()].toUpperCase());
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

}
