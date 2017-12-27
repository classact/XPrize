package classact.com.xprize.fragment.drill.movie;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.fragment.MenuFragment;
import classact.com.xprize.utils.LiveMediaPlayer;
import classact.com.xprize.utils.SubtitleView;
import dagger.android.support.DaggerFragment;

/**
 * A simple {@link DaggerFragment} subclass.
 * Courtesy: https://github.com/iTech-Developer/TimedTextTest/blob/master/src/com/example/media/timedtexttest/MainActivity.java
 */
public class DrillMovieFragment extends MenuFragment implements
        SurfaceHolder.Callback {

    @Inject ViewModelProvider.Factory viewModelFactory;

    @BindView(R.id.guideline_movie_top) Guideline movieTopGuideline;
    @BindView(R.id.guideline_subtitle_mid) Guideline subtitleGuideline;
    @BindView(R.id.fragment_container) ConstraintLayout container;
    @BindView(R.id.fragment_background) ImageView background;
    @BindView(R.id.movie_surface) SurfaceView movieSurface;
    @BindView(R.id.movie_subtitle) TextView movieSubtitle;

    private LiveMediaPlayer mediaPlayer;
    private DrillMovieViewModel vm;
    private SurfaceHolder surfaceHolder;

    @Inject
    public DrillMovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_drill_movie, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(DrillMovieViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        container.setBackgroundColor(Color.BLACK);

        boolean fullScreen =
                vm.getMovie().name.equalsIgnoreCase("Intro") ||
                vm.getMovie().name.equalsIgnoreCase("Finale");

        ConstraintLayout.LayoutParams movieTopGuidelineLayoutParams = (ConstraintLayout.LayoutParams) movieTopGuideline.getLayoutParams();
        movieTopGuidelineLayoutParams.guideBegin = fullScreen ? 0 : 49;
        movieTopGuideline.setLayoutParams(movieTopGuidelineLayoutParams);

        ConstraintLayout.LayoutParams subtitleGuidelineLayoutParams = (ConstraintLayout.LayoutParams) subtitleGuideline.getLayoutParams();
        subtitleGuidelineLayoutParams.guideBegin = 1597;
        subtitleGuideline.setLayoutParams(subtitleGuidelineLayoutParams);

        ViewGroup.MarginLayoutParams movieSurfaceLayoutParams = (ViewGroup.MarginLayoutParams) movieSurface.getLayoutParams();
        if (fullScreen) {
            movieSurfaceLayoutParams.width = 2560;
            movieSurfaceLayoutParams.height = 1640;
        } else {
            movieSurfaceLayoutParams.width = 2448;
            movieSurfaceLayoutParams.height = 1377;
        }
        movieSurface.setLayoutParams(movieSurfaceLayoutParams);

        ViewGroup.MarginLayoutParams movieSubtitleLayoutParams = (ViewGroup.MarginLayoutParams) movieSubtitle.getLayoutParams();
        movieSubtitleLayoutParams.width = 2300;
        movieSubtitle.setLayoutParams(movieSubtitleLayoutParams);

        movieSubtitle.setTypeface(fetch.font());
        movieSubtitle.setTextColor(Color.WHITE);

        mediaPlayer = vm.getMediaPlayer();

        try {

            String movieRes = fetch.raw(vm.getMovie().videoFile);

            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, Uri.parse(movieRes));
            mediaPlayer.setOnPreparedListener((mp) -> {
                mediaPlayer.start();
            });
            mediaPlayer.setOnCompletionListener((mp) -> {
                mediaPlayer.stop();
                getActivity().finish();
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            });
            mediaPlayer.prepare();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        surfaceHolder = movieSurface.getHolder();
        surfaceHolder.addCallback(this);

        if (!fullScreen && vm.getMovie().subtitleFile != null) {
            SubtitleView subtitle = new SubtitleView(context);
            container.addView(subtitle);
            subtitle.setLayoutParams(movieSubtitle.getLayoutParams());
            subtitle.setTypeface(fetch.font());
            subtitle.setTextColor(Color.WHITE);
            subtitle.setGravity(Gravity.CENTER);
            subtitle.setTextSize(40f);
            subtitle.setPlayer(mediaPlayer);
            subtitle.setSubSource(fetch.rawId(vm.getMovie().subtitleFile), MediaPlayer.MEDIA_MIMETYPE_TEXT_SUBRIP);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer.setDisplay(surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mediaPlayer.setDisplay(null);
    }

    private int findTrackIndex(int mediaTrackType, MediaPlayer.TrackInfo[] trackInfos) {
        int index = -1;
        for (int i = 0; i < trackInfos.length; i++) {
            if (trackInfos[i].getTrackType() == mediaTrackType) {
                return i;
            }
        }
        return index;
    }

    @Override
    public void onPauseEvent() {

    }

    @Override
    public void onResumeEvent() {

    }
}