package classact.com.xprize.activity.drill.sound;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.FisherYates;
import classact.com.xprize.utils.ResourceSelector;

public class SoundDrillFiveActivity extends DrillActivity {

    @BindView(R.id.activity_sound_drill_five) ConstraintLayout rootView;

    @BindView(R.id.background) ImageView background;
    @BindView(R.id.target_image) ImageView targetImage;
    @BindView(R.id.image_NW) ImageView image1;
    @BindView(R.id.image_NE) ImageView image2;
    @BindView(R.id.image_SW) ImageView image3;
    @BindView(R.id.image_SE) ImageView image4;
    
    private String drillData;
    private JSONArray sets;
    JSONArray images;
    private int currentSet;
    private int currentItem;
    private int correctItem;
    private String currentItemName;
    private String currentSound;
    private JSONObject params;
    private boolean itemsEnabled;
    private JSONObject[] orderedImages;
    private Random rnd;

    private ImageView[] items;

    private final Context THIS = this;

    private SoundDrill05ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_five);
        ButterKnife.bind(this);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(SoundDrill05ViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();
        itemsEnabled = false;

        image1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        clickedItem(0);
                    }
                }
        );
        image2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        clickedItem(1);
                    }
                }
        );
        image3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        clickedItem(2);
                    }
                }
        );
        image4.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        clickedItem(3);
                    }
                }
        );

        items = new ImageView[4];
        items[0] = image1;
        items[1] = image2;
        items[2] = image3;
        items[3] = image4;

        toggleItemsVisibility(false);

        rnd = new Random();
        drillData = getIntent().getExtras().getString("data");
        currentSet = 0;
        initialiseData();
        showSet();
    }

    private void initialiseData(){
        try {
            params = new JSONObject(drillData);
            sets = params.getJSONArray("sets");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void showSet(){
        toggleItemsVisibility(false);
        try{
            JSONObject setData = sets.getJSONObject(currentSet);

            loadImage(targetImage, setData.getInt("demoimage"));
            targetImage.setVisibility(View.VISIBLE);

            currentItemName = setData.getString("demosound");
            currentSound = setData.getString("sound");
            images = setData.getJSONArray("images");

            ImageView[] items = {image1, image2, image3, image4};
            int numberOfImages = images.length();
            int numberOfItems = items.length;
            orderedImages = new JSONObject[numberOfImages];

            System.out.println("Number of images: " + numberOfImages);
            System.out.println("Number of items: " + numberOfItems);

            // Exception case
            if (numberOfImages > numberOfItems) {
                throw new Exception("SoundDrillFiveActivity.showSet > Exception: " +
                        "number of images do not match number of items");
            }

            // Get shuffled array of indexes
            int[] shuffledItemIndexes = FisherYates.shuffle(numberOfItems);

            // Get right word
            for(int i = 0; i < numberOfImages; i++){
                int randomizedIndex = shuffledItemIndexes[i];
                JSONObject item = images.getJSONObject(i);
                int imageResourceId = item.getInt("image");

                System.out.println("::: Selected image #" + randomizedIndex + ": " + imageResourceId);

                loadImage(items[randomizedIndex], imageResourceId);
                orderedImages[randomizedIndex] = item;

                if (item.getInt("correct") == 1) {
                    correctItem = randomizedIndex;
                    System.out.println(":::: It's correct btw ... ");
                }
            }
            playBahatiHasA();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void playBahatiHasA(){
        try {
            String sound = params.getString("bahati_has_a");
            playSound(sound, this::playCurrentItemSound);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void playCurrentItemSound(){
        try {
            playSound(currentItemName, () ->
                    handler.delayed(this::playSheNeedsSomethingElse, 500));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void playSheNeedsSomethingElse(){
        try {
            toggleItemsVisibility(true);
            String sound = params.getString("she_needs_something_else");
            playSound(sound, this::playCurrentSound);
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void playCurrentSound(){
        try {
            toggleItemsVisibility(true);
            String soundPath = FetchResource.sound(getApplicationContext(), currentSound);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, Uri.parse(soundPath));
            mediaPlayer.setOnPreparedListener((mp) -> {
                mediaPlayer.start();
                    handler.delayed(() -> itemsEnabled = true, mp.getDuration() - 100);
                });
            mediaPlayer.setOnCompletionListener((mp) -> {
                mediaPlayer.stop();
            });
            mediaPlayer.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playPositiveSound(int soundId){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundId);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, myUri);
            mediaPlayer.setOnPreparedListener((mp) -> {
                ImageView iv = items[correctItem];
                int waitDuration = Math.min(mp.getDuration(), 200);
                fadeIncorrect(iv, waitDuration);
                mp.start();
            });
            mediaPlayer.setOnCompletionListener((mp) -> {
                mp.stop();
                handler.delayed(this::repeatCorrect, 500);
            });
            mediaPlayer.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void repeatCorrect() {

        try {
            final String soundMatch = orderedImages[currentItem].getString("sound");

            // Play current
            playSound(currentItemName, () -> {
                handler.delayed(() ->
                        playSound(soundMatch, () ->
                                handler.delayed(this::determineNextDrillItem,
                                    450)),
                    400);
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void determineNextDrillItem() {
        try {
            currentSet++;
            if (currentSet < sets.length()) {
                handler.delayed(this::showSet, 1000);
            } else {
                // End drill
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void clickedItem(int item) {
        if (itemsEnabled) {
            try {
                currentItem = item;

                // Disable items here
                // Otherwise can continuously carry on tapping
                if (currentItem == correctItem) {
                    itemsEnabled = false;

                    ImageView iv = getItem(currentItem);
                    if (iv != null) {
                        Globals.playStarWorks(THIS, iv);
                    }
                }

                String sound = orderedImages[currentItem].getString("sound");
                playSound(sound, this::checkProgress);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void checkProgress(){
        if (currentItem == correctItem){
            playPositiveSound(ResourceSelector.getPositiveAffirmationSound(this));
        }
        else{
            playSound(ResourceSelector.getNegativeAffirmationSound(this), null);
        }
    }

    public ImageView getItem(int index) {
        ImageView iv = null;
        for (int i = 0; i < items.length; i++) {
            if (i == index) {
                iv = items[i];
                break;
            }
        }
        return iv;
    }

    public void fadeIncorrect(ImageView ivCorrect, long fadeDuration) {
        for (int i = 0; i < items.length; i++) {
            ImageView iv = items[i];
            if (iv != ivCorrect) {
                iv.animate()
                        .alpha(0f)
                        .setInterpolator(new LinearInterpolator())
                        .setDuration(fadeDuration)
                        .start();
            }
        }
    }

    private void setItemsEnabled(boolean enable) {
        for (int i = 0; i < items.length; i++) {
            ImageView iv = items[i];
            iv.setEnabled(enable);
        }
    }

    public void toggleItemsVisibility(boolean show){
        if (show){
            for (int i = 0; i < items.length; i++) {
                ImageView iv = items[i];
                iv.setVisibility(View.VISIBLE);
                iv.setAlpha(1f);
            }
        }
        else{
            for (int i = 0; i < items.length; i++) {
                ImageView iv = items[i];
                iv.setVisibility(View.INVISIBLE);
            }
        }
    }
}
