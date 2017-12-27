package classact.com.xprize.fragment;


import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import butterknife.Unbinder;
import classact.com.xprize.event.ResumeEvent;
import classact.com.xprize.utils.Bus;
import classact.com.xprize.event.PauseEvent;
import classact.com.xprize.event.StopEvent;
import classact.com.xprize.utils.Fetch;
import dagger.android.support.DaggerFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.bumptech.glide.util.Preconditions.checkNotNull;

/**
 * A simple {@link DaggerFragment} subclass with additional goodies
 * for Menu!
 */
public abstract class MenuFragment extends DaggerFragment {

    @Inject protected ViewModelProvider.Factory viewModelFactory;
    @Inject protected Context context;
    @Inject protected Fetch fetch;
    @Inject protected Bus bus;

    protected Unbinder unbinder;
    protected CompositeDisposable compositeDisposable;

    public abstract void onPauseEvent();
    public abstract void onResumeEvent();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        compositeDisposable = new CompositeDisposable();
        subscribeToBus();
    }

    protected void subscribeToBus() {
        compositeDisposable.add(
                bus.asFlowable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                if (o instanceof PauseEvent) {
                                    onPauseEvent();
                                } else if (o instanceof ResumeEvent) {
                                    onPauseEvent();
                                } else if (o instanceof StopEvent) {
                                    onStopEvent();
                                }
                            }
                        }));
    }

    protected void onStopEvent() {
        getActivity().moveTaskToBack(true);
    }

    protected void loadImage(@NonNull String resName, @NonNull ImageView imageView) {
        checkNotNull(resName);
        checkNotNull(imageView);
        Glide.with(this)
                .load(fetch.imagePath(resName))
                .into(imageView);
    }

    /*
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
    */

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