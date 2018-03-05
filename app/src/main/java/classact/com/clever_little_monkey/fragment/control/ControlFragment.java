package classact.com.clever_little_monkey.fragment.control;


import android.animation.ObjectAnimator;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import classact.com.clever_little_monkey.R;
import classact.com.clever_little_monkey.utils.Bus;
import classact.com.clever_little_monkey.event.PauseEvent;
import classact.com.clever_little_monkey.event.ResumeEvent;
import classact.com.clever_little_monkey.event.StopEvent;
import classact.com.clever_little_monkey.fragment.SecondaryFragment;
import classact.com.clever_little_monkey.utils.LiveObjectAnimator;

/**
 * A simple {@link SecondaryFragment} subclass.
 */
public class ControlFragment extends SecondaryFragment {

    @Inject Bus bus;

    @BindView(R.id.bookmark) ImageView bookmark;
    @BindView(R.id.pause_button) ImageButton pauseButton;
    @BindView(R.id.stop_button) ImageButton stopButton;

    private ControlViewModel viewModel;

    @Inject
    public ControlFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_control, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(ControlViewModel.class);

        // Hide pause and stop buttons whilst bookmark background loads
        pauseButton.setVisibility(View.INVISIBLE);
        stopButton.setVisibility(View.INVISIBLE);
        bookmark.setAlpha(0f);

        // Set pause-play button image
        pauseButton.setImageResource(viewModel.isPaused() ?
                R.drawable.play_button :
                R.drawable.pause_button);

        // Load bookmark background
        loadImageWithRequestListener("bookmark", bookmark, new RequestListener() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                pauseButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.VISIBLE);
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(bookmark, "alpha", 1f);
                new LiveObjectAnimator(getLifecycle(), objectAnimator)
                        .setStartDelay(500)
                        .setDuration(500)
                        .setInterpolator(new LinearInterpolator())
                        .start();
                return false;
            }
        });
    }

    @OnClick(R.id.pause_button)
    public void onPauseClicked() {
        if (viewModel.isPaused()) {
            viewModel.resume();
            pauseButton.setImageResource(R.drawable.pause_button);
            if (bus.hasObservers()) {
                bus.send(new ResumeEvent());
            }
        } else {
            viewModel.pause();
            pauseButton.setImageResource(R.drawable.play_button);
            if (bus.hasObservers()) {
                bus.send(new PauseEvent());
            }
        }
    }

    @OnClick(R.id.stop_button)
    public void onStopClicked() {
        if (bus.hasObservers()) {
            bus.send(new StopEvent());
        }
    }
}