package com.example.bakingapp;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bakingapp.BakingModel.Step;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

public class ExoPlayerFragment extends Fragment {


    private String video_url;
    private int position;
    private ArrayList<Step> stepArrayList;
    TextView textView;
    private long playbackPosition;
    private boolean playWhenReady = true;
    private int current_position;
    private int total_items = 0;
    PlayerView playerView;
    private boolean stopPlay;
    private long playerStopPosition;

    SimpleExoPlayer player;

    Button next, prev;

    public ExoPlayerFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exo_player, container, false);
        playerView = v.findViewById(R.id.exoplayer_id);
        playerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(),R.drawable.video_not_available));
        textView = v.findViewById(R.id.exo_player_tv);
        next = v.findViewById(R.id.next);
        prev = v.findViewById(R.id.pre);
        if (getArguments() != null) {
            position = getArguments().getInt(getResources().getString(R.string.pos));
            stepArrayList = getArguments().getParcelableArrayList(getResources().getString(R.string.steplist));
        }
        playbackPosition = C.TIME_UNSET;
        if (savedInstanceState != null) {
            playWhenReady = savedInstanceState.getBoolean(getResources().getString(R.string.ready));
            current_position = savedInstanceState.getInt(getResources().getString(R.string.cpos));
            playbackPosition = savedInstanceState.getLong(getResources().getString(R.string.ppos), C.TIME_UNSET);
        }
        total_items = stepArrayList.size();
        video_url = stepArrayList.get(current_position).getVideoURL();
        textView.setText(stepArrayList.get(current_position).getDescription());
        hideUnhideExo();


        if (current_position == 0) {
            prev.setVisibility(View.INVISIBLE);
        }
        if (current_position == total_items - 1) {
            next.setVisibility(View.INVISIBLE);
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoNext();
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPrevious();
            }
        });
        return v;
    }

    private void releasePlayer() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    private void hideUnhideExo() {
        if (TextUtils.isEmpty(video_url)) {
            playerView.setVisibility(View.INVISIBLE);
            if (!TextUtils.isEmpty(stepArrayList.get(current_position).getThumbnailURL())) {
                video_url=stepArrayList.get(current_position).getThumbnailURL();

            }
        } else {
            playerView.setVisibility(View.VISIBLE);
        }
    }
    private void gotoNext() {
        releasePlayer();
        prev.setVisibility(View.VISIBLE);
        current_position++;
        textView.setText(stepArrayList.get(current_position).getDescription());
        video_url = stepArrayList.get(current_position).getVideoURL();

        if(current_position==total_items-1)
        {
            next.setVisibility(View.INVISIBLE);
        }
        hideUnhideExo();
        initializePlayer();
        player.seekTo(0);
    }

    private void gotoPrevious() {
        releasePlayer();
        next.setVisibility(View.VISIBLE);
        current_position--;
        textView.setText(stepArrayList.get(current_position).getDescription());

        if(current_position ==0){
            prev.setVisibility(View.INVISIBLE);
        }
        video_url=stepArrayList.get(current_position).getVideoURL();

        hideUnhideExo();
        initializePlayer();
        player.seekTo(0);

    }

    private void initializePlayer(){

        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),new DefaultTrackSelector(),new DefaultLoadControl());

        playerView.setPlayer(player);
        Uri uri = Uri.parse(video_url);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource,true,false);
        player.setPlayWhenReady(playWhenReady);
        if(playbackPosition!=0&&!stopPlay){
            player.seekTo(playbackPosition);
        }else{
            player.seekTo(playerStopPosition);
        }

    }
    private MediaSource buildMediaSource(Uri uri)
    {
        return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(getResources().getString(R.string.bakingappwidget))).createMediaSource(uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(Util.SDK_INT > 23)
        {
            initializePlayer();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(Util.SDK_INT <= 23 || player == null)
        {
            initializePlayer();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if(Util.SDK_INT <= 23){
            if(player!=null){
                playerStopPosition = player.getCurrentPosition();
                stopPlay = true;
                releasePlayer();
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            if (player != null) {

                playerStopPosition = player.getCurrentPosition();
                stopPlay = true;
                releasePlayer();
            }

        }
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt(getResources().getString(R.string.cpos),current_position);
        outState.putLong(getResources().getString(R.string.ppos),player.getCurrentPosition());
        outState.putBoolean(getResources().getString(R.string.ready),player.getPlayWhenReady());
    }
}
