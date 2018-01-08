package classact.com.xprize.activity.drill.sound;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.ResourceSelector;

public class SoundDrillTwoActivity extends DrillActivity {

    @BindView(R.id.activity_sound_drill_two) ConstraintLayout rootView;
    @BindView(R.id.background) ImageView background;

    @BindView(R.id.left_image) ImageView leftImage;
    @BindView(R.id.right_image) ImageView rightImage;

    private int currentPair;
    private int correctItem;
    private String drillData;
    private int totalItems = 4;
    private JSONArray pairs;
    private int play_mode = 1;
    private Runnable mRunnable;
    private String drillSound;
    private JSONObject data;
    private boolean itemsEnabled;

    private SoundDrill02ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_two);
        ButterKnife.bind(this);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(SoundDrill02ViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        itemsEnabled = false;

        leftImage.setOnClickListener((v) -> clickedItem(1));
        rightImage.setOnClickListener((v) -> clickedItem(2));

        // setItemsEnabled(false);
        drillData = getIntent().getExtras().getString("data");
        currentPair = 1;
        initialiseData(drillData);
    }

    private void initialiseData (String data){
        try {
            this.data = new JSONObject(data);
            totalItems = this.data.getInt("paircount");
            pairs = this.data.getJSONArray("pairs");
            drillSound = this.data.getString("drillsound");
            totalItems = this.data.getInt("paircount");
            showPair();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void showPair(){
        try {
            play_mode = 1;
            leftImage.setVisibility(View.INVISIBLE);
            rightImage.setVisibility(View.INVISIBLE);

            int correctImage = pairs.getJSONObject(currentPair - 1).getInt("correctimage");
            int wrongImage = pairs.getJSONObject(currentPair - 1).getInt("wrongimage");
            Random rand = new Random();
            correctItem = rand.nextInt(2);
            if (correctItem < 1) {
                correctItem = 1;
                leftImage.setImageResource(correctImage);
                rightImage.setImageResource(wrongImage);
            } else {
                correctItem = 2;
                leftImage.setImageResource(wrongImage);
                rightImage.setImageResource(correctImage);
            }
            showFirstItem();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void showFirstItem(){
        try {
            leftImage.setVisibility(View.VISIBLE);
            String sound = data.getString("this_is_a");
            playSound(sound, this::playFirstSound);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playFirstSound(){
        String sound = "";
        try {
            if (correctItem == 1)
                sound = pairs.getJSONObject(currentPair - 1).getString("correctsound");
            else
                sound = pairs.getJSONObject(currentPair - 1).getString("wrongsound");
            playSound(sound, this::startSecondItem);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void startSecondItem(){
        try {
            rightImage.setVisibility(View.VISIBLE);
            String sound = data.getString("this_is_a");
            playSound(sound, this::playSecondSound);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playSecondSound(){
        String sound = "";
        try {
            if (correctItem == 2) {
                sound = pairs.getJSONObject(currentPair - 1).getString("correctsound");
            } else {
                sound = pairs.getJSONObject(currentPair - 1).getString("wrongsound");
            }
            playSound(sound, this::playIntro);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void playIntro(){
        try {
            play_mode = 2;
            String sound = data.getString("touch_picture_starts_with");
            playSound(sound, () -> {
                mRunnable = null;
                mRunnable = () -> itemsEnabled = true;
                playSoundAndRunnableAfterCompletion(drillSound);
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playSoundAndRunnableAfterCompletion(String sound) {
        try {
            playSound(sound, mRunnable);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playSoundAndRunnableAfterCompletion(int soundId) {
        try {
            playSound(soundId, mRunnable);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void clickedItem(int item){
        if (itemsEnabled) {
            try {
                if (play_mode == 2) {
                    if (item == correctItem) {
                        // setItemsEnabled(false);

                        ImageView iv = null;
                        if (item == 1) {
                            iv = leftImage;
                        } else if (item == 2) {
                            iv = rightImage;
                        }
                        Globals.playStarWorks(this, iv, 15, 12, 9);

                        itemsEnabled = false;
                        mRunnable = null; // Reset?
                        mRunnable = () -> {
                                if (currentPair < pairs.length()) {
                                    currentPair++;
                                    handler.delayed(this::showPair, 350);
                                } else {
                                    finish();
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                }
                            };
                        playSoundAndRunnableAfterCompletion(ResourceSelector.getPositiveAffirmationSound(this));
                    } else {
                        playSound(ResourceSelector.getNegativeAffirmationSound(this), null);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void setItemsEnabled(boolean enable) {
        leftImage.setEnabled(enable);
        rightImage.setEnabled(enable);
    }
}