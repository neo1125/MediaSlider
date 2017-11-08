package com.neo1125.mediaslider;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import static com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_FILL;

class SlideVideoView extends SlideView {

    private SimpleExoPlayer player;
    private SimpleExoPlayerView videoPlayerView;
    private boolean isPlayend = false;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            player.setPlayWhenReady(true);
        }
    };

    public SlideVideoView(Context context) {
        this(context, null);
    }

    public SlideVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        DrmSessionManager<FrameworkMediaCrypto> drmSessionManager = null;
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(getContext(),
                drmSessionManager, DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector();

        player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector);
        player.clearVideoSurface();
        player.setVolume(0);
        player.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if(playWhenReady && playbackState == ExoPlayer.STATE_READY) {
                    if (listner != null && !isStart) {
                        isStart = true;
                        listner.onReady();
                    }
                } else if (playWhenReady && playbackState == ExoPlayer.STATE_ENDED) {

                    isPlayend = true;
                    player.stop();
                    player.release();
                    player = null;

                    if (listner != null)
                        listner.nextPage();
                }
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                if (listner != null)
                    listner.onError(new Error(error.getMessage()));
            }

            @Override
            public void onPositionDiscontinuity() {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }
        });

        videoPlayerView = new SimpleExoPlayerView(getContext());
        videoPlayerView.setPlayer(player);
        videoPlayerView.setBackgroundColor(Color.BLACK);
        videoPlayerView.setControllerHideOnTouch(false);
        videoPlayerView.setResizeMode(RESIZE_MODE_FILL);
        videoPlayerView.setUseController(false);
        videoPlayerView.hideController();

        addView(videoPlayerView, new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void setUri(String uriString) {
        Uri uri = Uri.parse(uriString);
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), "exoplayer2example"),
                bandwidthMeter);
        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(uri, dataSourceFactory,
                extractorsFactory, null, null);
        player.prepare(mediaSource, false, true);
    }

    @Override
    public void start() {
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 300);
    }

    @Override
    public Media.Type getType() {
        return Media.Type.video;
    }

    @Override
    public boolean isPlayEnd() {
        return isPlayend;
    }

    @Override
    public void pause() {
        if (player != null)
            player.setPlayWhenReady(false);
    }

    @Override
    public void resume() {
        if (player != null)
            player.setPlayWhenReady(true);
    }
}
