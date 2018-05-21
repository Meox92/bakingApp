package com.example.maola.bakingapp.UI;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maola.bakingapp.Adapter.NavigationStepAdapter;
import com.example.maola.bakingapp.Constants;
import com.example.maola.bakingapp.Model.Step;
import com.example.maola.bakingapp.R;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailFragment extends Fragment implements NavigationStepAdapter.ListItemClickListener {
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private RecyclerView mRecyclerView;
    private long playbackPosition = 0;
    private Boolean playWhenReady = true;
    private Step step;
    private ArrayList<Step> stepList;
    private static String PLAYBACK_POSITION = "PLAYBACK_POSITION";
    private onNavigationStepClickListener onNavigationStepClickListener;
    private boolean mTwoPanel = false;



    public StepDetailFragment() {
        // Required empty public constructor
    }

    public interface onNavigationStepClickListener{
        void onNavigationStepClicked(int index);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            onNavigationStepClickListener = (onNavigationStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement onNavigationStepClickListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);



        if(savedInstanceState != null){
            playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION);
        }

        playerView = (PlayerView) rootView.findViewById(R.id.detail_video_player);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.detail_recycler_view);
        TextView textView = (TextView) rootView.findViewById(R.id.detail_description_tv);


        Bundle bundle = this.getArguments();

        if(bundle != null){
//            step = bundle.getParcelable(Step.mString);
            stepList = bundle.getParcelableArrayList(Constants.STEP);
            int stepIndex = bundle.getInt(Constants.STEP_INDEX);
            step = stepList.get(stepIndex);
            mTwoPanel = bundle.getBoolean("MTWOPANEL");

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            layoutManager.scrollToPositionWithOffset(stepIndex, 20);

            mRecyclerView.setLayoutManager(layoutManager);
            NavigationStepAdapter navigationStepAdapter = new NavigationStepAdapter(stepList, this, stepIndex);
            mRecyclerView.setAdapter(navigationStepAdapter);

            textView.setText(stepList.get(stepIndex).getDescription());
            initializePlayer();

        }

        // Detect orientation
        int orientation = getResources().getConfiguration().orientation;
        int screenSize = getResources().getConfiguration().screenLayout;
        switch(orientation)
        {
            case  Configuration.ORIENTATION_LANDSCAPE:
                if(!mTwoPanel){
                    hideSystemUi();
                    mRecyclerView.setVisibility(View.GONE);
                }
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                break;
            default:
                break;
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        releasePlayer();
        outState.putLong(PLAYBACK_POSITION, playbackPosition);
    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);

        if(step.getVideoURL() == null || step.getVideoURL().equals("")){
            playerView.setVisibility(View.GONE);
            return;
        }

        Uri uri = Uri.parse(step.getVideoURL());
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource);
        player.seekTo(playbackPosition);
        player.setPlayWhenReady(playWhenReady);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            playWhenReady = player.getPlayWhenReady();
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        onNavigationStepClickListener.onNavigationStepClicked(clickedItemIndex);
    }
}
