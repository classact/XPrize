package classact.com.xprize.activity.drill.sound;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.utils.FetchResource;

public class SoundDrillThreeActivity extends DrillActivity {

    @BindView(R.id.activity_sound_drill_three) ConstraintLayout rootView;
    @BindView(R.id.background) ImageView background;
    @BindView(R.id.target_letter) ImageView targetLetter;
    @BindView(R.id.left_letter) ImageView leftLetter;
    @BindView(R.id.right_letter) ImageView rightLetter;

    private String drillData;
    private JSONArray sets;
    private int currentSet;
    private int correctItem;
    private String currentSound;
    private String currentPhonicSound;
    private JSONObject params;
    private Runnable mRunnable;
    private boolean itemsEnabled;
    private Random rnd;

    private SoundDrill03ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_three);
        ButterKnife.bind(this);

        preloadImage(R.drawable.background_choosetheletter);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(SoundDrill03ViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        rnd = new Random();

        itemsEnabled = false;
        leftLetter.setOnClickListener((v) -> clickedItem(0));
        rightLetter.setOnClickListener((v) -> clickedItem(1));
        drillData = getIntent().getExtras().getString("data");
        currentSet = 0;
        initialiseData();
        showSet();
    }

    private void initialiseData(){
        try {
            params = new JSONObject(drillData);
            sets = params.getJSONArray("sets");
            showSet();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void showSet(){
        background.setImageResource(0);
        targetLetter.setVisibility(View.VISIBLE);
        ez.hide(leftLetter, rightLetter);

        try{
            JSONObject setData = sets.getJSONObject(currentSet);
            String image = setData.getString("image");

            loadImage(targetLetter, FetchResource.imageId(context, image));

            currentSound = setData.getString("sound");
            currentPhonicSound = setData.getString("phonic_sound");
            JSONArray images = setData.getJSONArray("images");

            JSONObject item1JSONObject = null;
            JSONObject item2JSONObject = null;

            for (int i = 0; i < images.length(); i++) {
                // Get item from images JSONArray
                JSONObject item = images.getJSONObject(i);

                // Check if it's the correct one
                if (item.getInt("correct") == 1) {
                    item1JSONObject = item;
                } else {
                    item2JSONObject = item;
                }
            }

            // Get a random value between 0 and 1
            correctItem = rnd.nextInt(2);

            // Determine where item{1|2}JSONObject should be assigned to
            if (correctItem == 0) {

                // item1 is item1JSONObject
                loadImage(leftLetter, FetchResource.imageId(context, item1JSONObject, "image"));
                loadImage(rightLetter, FetchResource.imageId(context, item2JSONObject, "image"));

            } else if (correctItem == 1) {

                // item1 is item2JSONObject
                loadImage(leftLetter, FetchResource.imageId(context, item2JSONObject, "image"));
                loadImage(rightLetter, FetchResource.imageId(context, item1JSONObject, "image"));
            }

            String sound = params.getString("this_is_the_letter");
            playSound(sound, this::playSound);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void playSound(){
        try {
            playSound(currentSound, this::playItMakes);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playItMakes(){
        try {
            String sound = params.getString("it_makes_sound");
            playSound(sound, this::playPhonicSound);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playPhonicSound(){
        try {
            playSound(currentPhonicSound, this::playNextSound);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void playNextSound(){
        try {

            loadImage(background, R.drawable.background_choosetheletter, new RequestListener() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                    ez.hide(targetLetter);
                    ez.show(leftLetter, rightLetter);
                    return false;
                }
            });

            String sound = params.getString("touch");
            playSound(sound, this::playSoundAgain);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void playSoundAgain(){
        try {
            playSound(currentPhonicSound, () -> itemsEnabled = true);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void clickedItem(int item){
        if (itemsEnabled) {
            if (item == correctItem) {
                // setItemsEnabled(false);

                ImageView iv = null;
                if (item == 0) {
                    iv = leftLetter;
                } else if (item == 1) {
                    iv = rightLetter;
                }
                starWorks.play(this, iv); // Globals.playStarWorks(context, iv);

                itemsEnabled = false;
                mRunnable = null;
                mRunnable = () -> {
                        if (currentSet < sets.length() - 1) {
                            currentSet++;
                            // Next drill
                            handler.delayed(this::showSet, 350);
                        } else {
                            finish();
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    };
                playSound(FetchResource.positiveAffirmation(context), mRunnable);
            } else {
                playSound(FetchResource.negativeAffirmation(context), null);
            }
        }
    }
}