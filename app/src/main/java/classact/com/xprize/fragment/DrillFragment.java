package classact.com.xprize.fragment;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Unbinder;
import classact.com.xprize.event.PauseEvent;
import classact.com.xprize.event.ResumeEvent;
import classact.com.xprize.event.StopEvent;
import classact.com.xprize.state.DrillState;
import classact.com.xprize.utils.Bus;
import classact.com.xprize.utils.EZ;
import classact.com.xprize.utils.Fetch;
import classact.com.xprize.utils.LiveHandler;
import classact.com.xprize.utils.LiveMediaPlayer;
import classact.com.xprize.utils.StarWorks;
import dagger.android.support.DaggerFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.bumptech.glide.util.Preconditions.checkNotNull;

/**
 * A simple {@link DaggerFragment} subclass with additional goodies
 * for Drills!
 */

public abstract class DrillFragment extends DaggerFragment {

    @Inject protected ViewModelProvider.Factory viewModelFactory;
    @Inject protected Context context;
    @Inject protected Bus bus;
    @Inject protected Fetch fetch;
    @Inject protected StarWorks starWorks;
    @Inject protected EZ ez;

    protected Unbinder unbinder;
    protected CompositeDisposable compositeDisposable;
    protected Handler tester;
    protected LiveHandler handler;
    protected LiveMediaPlayer mediaPlayer;
    protected List<DrillState> states;

    public abstract void onPauseEvent();
    public abstract void onResumeEvent();
    public abstract void onStopEvent();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        compositeDisposable = new CompositeDisposable();
        states = new ArrayList<>();
        subscribeToBus();
    }

    protected void playSound(String sound, Runnable action) {
        playSound(sound, action, 0);
    }

    protected void playSound(String sound, Runnable action, long delayMillis) {
        try {
            String soundPath = fetch.raw(sound);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, Uri.parse(soundPath));
            mediaPlayer.setOnPreparedListener((mp -> {
                mediaPlayer.start();
            }));
            mediaPlayer.setOnCompletionListener((mp -> {
                mediaPlayer.stop();
                if (action != null) {
                    handler.delayed(action, delayMillis);
                }
            }));
            mediaPlayer.prepare();
        } catch (Exception ex) {
            Toast.makeText(context, "Error for ‘" + sound + "’", Toast.LENGTH_LONG).show();
            Log.e("Drill Fragment", "playSound(sound, action)");
            ex.printStackTrace();
        }
    }

    protected void playSound(String sound, Runnable preparedAction, long preparedDelayMillis, Runnable completeAction, long completeDelayMillis) {
        try {
            String soundPath = fetch.raw(sound);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, Uri.parse(soundPath));
            mediaPlayer.setOnPreparedListener((mp -> {
                mediaPlayer.start();
                if (preparedAction != null) {
                    handler.delayed(preparedAction, preparedDelayMillis);
                }
            }));
            mediaPlayer.setOnCompletionListener((mp -> {
                mediaPlayer.stop();
                if (completeAction != null) {
                    handler.delayed(completeAction, completeDelayMillis);
                }
            }));
            mediaPlayer.prepare();
        } catch (Exception ex) {
            Toast.makeText(context, "Error for ‘" + sound + "’", Toast.LENGTH_LONG).show();
            Log.e("Drill Fragment", "playSound(sound, action)");
            ex.printStackTrace();
        }
    }

    protected void loadImage(ImageView iv, int placeholderResId, int newResId) {
        RequestOptions requestOptions = new RequestOptions().placeholder(placeholderResId);
        Glide.with(this).setDefaultRequestOptions(requestOptions).load(newResId).into(iv);
    }

    protected void setTouchListener(ImageView iv, int color, PorterDuff.Mode porterDuffMode) {
        iv.setOnTouchListener((v, event) -> {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    iv.setColorFilter(color, porterDuffMode);
                    return true;
                case MotionEvent.ACTION_UP:
                    iv.setColorFilter(null);
                    iv.performClick();
                    return true;
                default:
                    return false;
            }
        });
    }

    protected void setTouchListener(ImageView iv, int upResId, int downResId) {
        iv.setOnTouchListener((v, event) -> {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    loadImage(iv, upResId, downResId);
                    return true;
                case MotionEvent.ACTION_UP:
                    loadImage(iv, downResId, upResId);
                    iv.performClick();
                    return true;
                default:
                    return false;
            }
        });
    }

    protected void subscribeToBus() {
        compositeDisposable.add(
                bus.asFlowable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((o) -> {
                            if (o instanceof PauseEvent) {
                                onPauseEvent();
                            } else if (o instanceof ResumeEvent) {
                                onResumeEvent();
                            } else if (o instanceof StopEvent) {
                                onStopEvent();
                            }
                        }));
    }

//    protected void onStopEvent() {
//        getActivity().moveTaskToBack(true);
//    }

    @Override
    public void onResume() {
        super.onResume();
//
//        View view = getView();
//        if (view != null) {
//            view.setFocusableInTouchMode(true);
//            view.requestFocus();
//            view.setOnKeyListener((v, i, keyEvent) -> {
//                if (i == KeyEvent.KEYCODE_BACK) {
//                    switch (keyEvent.getAction()) {
//                        case KeyEvent.ACTION_DOWN:
//                            break;
//                        case KeyEvent.ACTION_UP:
//                            onBackPressed();
//                            return true;
//                        default:
//                            break;
//                    }
//                }
//                return false;
//            });
//        }
    }

    protected void loadImage(@NonNull String resName, @NonNull ImageView imageView) {
        checkNotNull(resName);
        checkNotNull(imageView);
        Glide.with(this)
                .load(fetch.imagePath(resName))
                .into(imageView);
    }

    protected void loadImageWithRequestListener(
            @NonNull String resName, @NonNull ImageView imageView,
            @NonNull RequestListener requestListener) {
        checkNotNull(resName);
        checkNotNull(imageView);
        checkNotNull(requestListener);
        Glide.with(this)
                .load(fetch.imagePath(resName))
                .listener(requestListener)
                .into(imageView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (tester != null) {
            tester.removeCallbacksAndMessages(null);
            tester = null;
        }
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (compositeDisposable != null) {
            compositeDisposable.clear();
            compositeDisposable = null;
        }
        super.onDestroyView();
    }
}