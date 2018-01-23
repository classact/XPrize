package classact.com.xprize.activity.drill.sound;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.ResourceSelector;

public class SoundDrillFourActivity extends DrillActivity {

    @BindView(R.id.activity_sound_drill_four) ConstraintLayout rootView;

    @BindView(R.id.toy_01) ImageView item1;
    @BindView(R.id.toy_02) ImageView item2;
    @BindView(R.id.toy_03) ImageView item3;
    @BindView(R.id.toy_04) ImageView item4;
    @BindView(R.id.toy_05) ImageView item5;
    @BindView(R.id.toy_06) ImageView item6;
    @BindView(R.id.toy_box) ImageView toyBox;

    @BindView(R.id.g_h_toy_01) Guideline gh01;
    @BindView(R.id.g_h_toy_02) Guideline gh02;
    @BindView(R.id.g_h_toy_03) Guideline gh03;
    @BindView(R.id.g_h_toy_04) Guideline gh04;
    @BindView(R.id.g_h_toy_05) Guideline gh05;
    @BindView(R.id.g_h_toy_06) Guideline gh06;

    @BindView(R.id.g_v_toy_01) Guideline gv01;
    @BindView(R.id.g_v_toy_02) Guideline gv02;
    @BindView(R.id.g_v_toy_03) Guideline gv03;
    @BindView(R.id.g_v_toy_04) Guideline gv04;
    @BindView(R.id.g_v_toy_05) Guideline gv05;
    @BindView(R.id.g_v_toy_06) Guideline gv06;

    private int currentItem;
    private int current_reward = 0;
    private String drillData;
    private int totalItems = 6;
    private JSONArray images;
    float x;
    float y;
    private boolean entered;
    private String drillSound;
    JSONObject params;
    private boolean itemsEnabled;
    private Runnable mRunnable;

    private SoundDrill04ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_four);
        ButterKnife.bind(this);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(SoundDrill04ViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

        loadImage(toyBox, R.drawable.receptacletoybox);

        // ez.highlight(Color.argb(100, 100, 0, 0), item1, item2, item3, item4, item5, item6);

        // setItemsEnabled(false);
        itemsEnabled = false;

        item1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentItem = 0;
                return dragItem(v,event);
            }
        });
        item2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentItem = 1;
                return dragItem(v,event);
            }
        });
        item3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentItem = 2;
                return dragItem(v,event);
            }
        });
        item4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentItem = 3;
                return dragItem(v,event);
            }
        });

        item5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentItem = 4;
                return dragItem(v,event);
            }
        });

        item6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentItem = 5;
                return dragItem(v,event);
            }
        });

        /*  */

        Guideline botRightHoz = ez.guide.create(true, 1f);
        Guideline botRightVert = ez.guide.create(false, 1f);
        rootView.addView(botRightHoz);
        rootView.addView(botRightVert);

        ConstraintLayout.LayoutParams toyBoxLayoutParams = (ConstraintLayout.LayoutParams) toyBox.getLayoutParams();
        toyBoxLayoutParams.bottomToTop = botRightHoz.getId();
        toyBoxLayoutParams.rightToLeft = botRightVert.getId();
        toyBox.setLayoutParams(toyBoxLayoutParams);

        /*  */

        toyBox.setOnDragListener(onItemDraggedIntoToyboxListener);

        drillData = getIntent().getExtras().getString("data");
        initialiseData(drillData);
        playDamaNeedsToCleanSound();
    }

    private void initialiseData (String data){
        try {
            params = new JSONObject(data);
            images = params.getJSONArray("images");

            ImageView[] items = {item1, item2, item3, item4, item5, item6};
            int n = images.length();

            if (n > items.length) {
                throw new Exception("Too many toys!");
            }

            for (int i = 0; i < items.length; i++) {
                if (i < n) {
                    loadImage(items[i], images.getJSONObject(i).getInt("image"));
                } else {
                    rootView.removeView(items[i]);
                }
            }
            drillSound = params.getString("drillsound");

            // Arrange
            if (n < 5) {

                ez.guide.setPercentage(gh01, 0.29f);
                ez.guide.setPercentage(gv01, 0.35f);

                ez.guide.setPercentage(gh02, 0.275f);
                ez.guide.setPercentage(gv02, 0.62f);

                ez.guide.setPercentage(gh03, 0.675f);
                ez.guide.setPercentage(gv03, 0.29f);

                ez.guide.setPercentage(gh04, 0.625f);
                ez.guide.setPercentage(gv04, 0.535f);

            } else if (n < 6) {

                ez.guide.setPercentage(gh01, 0.3f);
                ez.guide.setPercentage(gv01, 0.2375f);

                ez.guide.setPercentage(gh02, 0.21f);
                ez.guide.setPercentage(gv02, 0.5f);

                ez.guide.setPercentage(gh03, 0.3f);
                ez.guide.setPercentage(gv03, 0.75f);

                ez.guide.setPercentage(gh04, 0.675f);
                ez.guide.setPercentage(gv04, 0.29f);

                ez.guide.setPercentage(gh05, 0.625f);
                ez.guide.setPercentage(gv05, 0.535f);

            } else {

                ez.guide.setPercentage(gh01, 0.3f);
                ez.guide.setPercentage(gv01, 0.23f);

                ez.guide.setPercentage(gh02, 0.22f);
                ez.guide.setPercentage(gv02, 0.44f);

                ez.guide.setPercentage(gh03, 0.675f);
                ez.guide.setPercentage(gv03, 0.29f);

                ez.guide.setPercentage(gh04, 0.31f);
                ez.guide.setPercentage(gv04, 0.645f);

                ez.guide.setPercentage(gh05, 0.225f);
                ez.guide.setPercentage(gv05, 0.84f);

                ez.guide.setPercentage(gh06, 0.63f);
                ez.guide.setPercentage(gv06, 0.545f);
            }
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void playDamaNeedsToCleanSound(){
        try {
            String sound = params.getString("dama_needs_to_clean");
            playSound(sound, this::playDragThePicturesThatStartWithSound);
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void playDragThePicturesThatStartWithSound(){
        try {
            String sound = params.getString("drag_the_pictures_that_start");
            playSound(sound, this::playDrillSound);
        }
        catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void playDrillSound(){
        try{
            playSound(drillSound, this::playIntoTheBoxSound);
        }
        catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void playIntoTheBoxSound() {
        try{
            String sound = params.getString("into_the_box");
            playSound(sound, () -> itemsEnabled = true);
        }
        catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void playSoundAndRunnableAfterCompletion(int soundId) {
        try {
            playSound(soundId, () -> {
                if (mRunnable != null) {
                    mRunnable.run();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public boolean dragItem(View view, MotionEvent motionEvent) {
        if (itemsEnabled) {
            try {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                            view);
                    view.startDragAndDrop(data, shadowBuilder, view, 0);
                    entered = false;
                    String sound = images.getJSONObject(currentItem).getString("sound");
                    playSound(sound, null);
                    return true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    private View.OnDragListener onItemDraggedIntoToyboxListener = (v, event) -> {
        try {
            int action = event.getAction();
            if (action == DragEvent.ACTION_DRAG_ENTERED) {
                if (!entered) {
                    entered = true;
                }
            } else if (action == DragEvent.ACTION_DRAG_EXITED) {
                if (entered) {
                    entered = false;
                }
            } else if (event.getAction() == DragEvent.ACTION_DROP && entered) {
                int right = images.getJSONObject(currentItem).getInt("right");
                if (right == 1) {
                    Globals.playStarWorks(this, toyBox);
                    playRewardSound(true);
                } else {
                    playRewardSound(false);
                }
            } else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED && entered) {
                int right = images.getJSONObject(currentItem).getInt("right");
                if (right == 1) {
                    ImageView view = (ImageView) event.getLocalState();
                    view.setVisibility(View.INVISIBLE);
                }
            }
            return true;
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
        return false;
    };

    private void playRewardSound(boolean affirm){
        if (affirm) {
            // Increment reward
            current_reward++;

            // Debug
            System.out.println("SoundDrillFourActivity.playRewardSound > Debug: Current reward is (" + current_reward + ")");

            if (current_reward == 4) {
                // setItemsEnabled(false);
                itemsEnabled = false;

                // Runnable to close activity
                mRunnable = null;
                mRunnable = new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                };
                playSoundAndRunnableAfterCompletion(ResourceSelector.getPositiveAffirmationSound(context));
            } else {
                playSound(ResourceSelector.getPositiveAffirmationSound(context), null);
            }
        } else {
            playSound(ResourceSelector.getNegativeAffirmationSound(context), null);
        }
    }
}