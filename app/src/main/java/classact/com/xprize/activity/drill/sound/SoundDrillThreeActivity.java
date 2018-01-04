package classact.com.xprize.activity.drill.sound;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.utils.FetchResource;

public class SoundDrillThreeActivity extends DrillActivity {

    @BindView(R.id.item_demo) ImageView demoItem;
    @BindView(R.id.item1) ImageView item1;
    @BindView(R.id.item2) ImageView item2;
    @BindView(R.id.items) LinearLayout itemsLayout;

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

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(SoundDrill03ViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float density = displayMetrics.density;

        // demoItem.setBackgroundColor(Color.argb(100, 0, 0, 255));

        ViewGroup.MarginLayoutParams demoItemLP = (ViewGroup.MarginLayoutParams) demoItem.getLayoutParams();
        demoItemLP.width = (int) density * 326;
        demoItemLP.height = (int) density * 380;
        demoItemLP.topMargin = (int) density * 60;
        demoItemLP.setMarginStart((int) density * 485);
        demoItem.setLayoutParams(demoItemLP);

        // item1.setBackgroundColor(Color.argb(100, 255, 0, 0));
        // item2.setBackgroundColor(Color.argb(100, 0, 255, 0));

        ViewGroup.MarginLayoutParams item1LP = (ViewGroup.MarginLayoutParams) item1.getLayoutParams();
        ViewGroup.MarginLayoutParams item2LP = (ViewGroup.MarginLayoutParams) item2.getLayoutParams();
        item1LP.width = (int) density * 326;
        item2LP.width = (int) density * 326;
        item1LP.height = (int) density * 380;
        item2LP.height = (int) density * 380;
        item1LP.leftMargin = (int) density * 170;
        item2LP.leftMargin = (int) density * 290;
        item1LP.bottomMargin = (int) density * 160;
        item2LP.bottomMargin = (int) density * 160;
        item1.setLayoutParams(item1LP);
        item2.setLayoutParams(item2LP);

        rnd = new Random();
        // setItemsEnabled(false);
        itemsEnabled = false;
        item1.setOnClickListener((v) -> clickedItem(0));
        item2.setOnClickListener((v) -> clickedItem(1));
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
        demoItem.setVisibility(View.VISIBLE);
        itemsLayout.setVisibility(View.INVISIBLE);
        try{
            JSONObject setData = sets.getJSONObject(currentSet);
            String image = setData.getString("image");
            demoItem.setImageResource(FetchResource.imageId(context, image));
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
                item1.setImageResource(FetchResource.imageId(context, item1JSONObject, "image"));
                item2.setImageResource(FetchResource.imageId(context, item2JSONObject, "image"));
            } else if (correctItem == 1) {
                // item1 is item2JSONObject
                item1.setImageResource(FetchResource.imageId(context, item2JSONObject, "image"));
                item2.setImageResource(FetchResource.imageId(context, item1JSONObject, "image"));
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
            demoItem.setVisibility(View.INVISIBLE);
            itemsLayout.setVisibility(View.VISIBLE);
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
                    iv = item1;
                } else if (item == 1) {
                    iv = item2;
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

    private void setItemsEnabled(boolean enable) {
        item1.setEnabled(enable);
        item2.setEnabled(enable);
    }
}