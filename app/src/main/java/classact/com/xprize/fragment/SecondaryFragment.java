package classact.com.xprize.fragment;


import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;

import javax.inject.Inject;

import butterknife.Unbinder;
import classact.com.xprize.utils.Fetch;
import dagger.android.support.DaggerFragment;

/**
 * A simple {@link DaggerFragment} subclass.
 */
public abstract class SecondaryFragment extends DaggerFragment {

    @Inject protected Fetch fetch;
    @Inject protected Context context;
    protected Unbinder unbinder;

    protected void loadImage(String resName, ImageView imageView) {
        Glide.with(this)
                .load(fetch.imagePath(resName))
                .into(imageView);
    }

    protected void loadImageWithRequestListener(String resName, ImageView imageView, RequestListener requestListener) {
        Glide.with(this)
                .load(fetch.imagePath(resName))
                .listener(requestListener)
                .into(imageView);
    }

    /**
     * Called when the view previously created by {@link #onCreateView} has
     * been detached from the fragment.  The next time the fragment needs
     * to be displayed, a new view will be created.  This is called
     * after {@link #onStop()} and before {@link #onDestroy()}.  It is called
     * <em>regardless</em> of whether {@link #onCreateView} returned a
     * non-null view.  Internally it is called after the view's state has
     * been saved but before it has been removed from its parent.
     */
    @Override
    public void onDestroyView() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroyView();
    }
}