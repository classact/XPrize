package classact.com.xprize.activity.drill.sound;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.ResourceSelector;

public class SoundDrillFourActivity extends DrillActivity {

    @BindView(R.id.activity_sound_drill_four) ConstraintLayout rootView;

    @BindView(R.id.item1) ImageView item1;
    @BindView(R.id.item2) ImageView item2;
    @BindView(R.id.item3) ImageView item3;
    @BindView(R.id.item4) ImageView item4;
    @BindView(R.id.item5) ImageView item5;
    @BindView(R.id.item6) ImageView item6;
    @BindView(R.id.toybox) ImageView toyBox;

    private int currentItem;
    private int current_reward = 0;
    private String drillData;
    private int totalItems = 6;
    private JSONArray images;
    private MediaPlayer mp;
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

        loadImage(toyBox, R.drawable.receptacletoybox);

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
            loadImage(item1, images.getJSONObject(0).getInt("image"));
            loadImage(item2, images.getJSONObject(1).getInt("image"));
            loadImage(item3, images.getJSONObject(2).getInt("image"));
            loadImage(item4, images.getJSONObject(3).getInt("image"));
            loadImage(item5, images.getJSONObject(4).getInt("image"));
            loadImage(item6, images.getJSONObject(5).getInt("image"));
            drillSound = params.getString("drillsound");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playDamaNeedsToCleanSound(){
        try {
            String sound = params.getString("dama_needs_to_clean");
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(getApplicationContext(), Uri.parse(soundPath));
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    playDragThePicturesThatStartWithSound();
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playDragThePicturesThatStartWithSound(){
        try {
            String sound = params.getString("drag_the_pictures_that_start");
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(getApplicationContext(), Uri.parse(soundPath));
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    playDrillSound();
                }
            });
            mp.prepare();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void playDrillSound(){
        try{
            String soundPath = FetchResource.sound(getApplicationContext(), drillSound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(getApplicationContext(), Uri.parse(soundPath));
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    playIntoTheBoxSound();
                }
            });
            mp.prepare();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void playIntoTheBoxSound() {
        try{
            String sound = params.getString("into_the_box");
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(getApplicationContext(), Uri.parse(soundPath));
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    // setItemsEnabled(true);
                    itemsEnabled = true;
                }
            });
            mp.prepare();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void playSoundAndRunnableAfterCompletion(String sound) {
        try {
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(getApplicationContext(), Uri.parse(soundPath));
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    if (mRunnable != null) {
                        mRunnable.run();
                    }
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playSoundAndRunnableAfterCompletion(int soundId) {
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundId);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(context, myUri);
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    if (mRunnable != null) {
                        mRunnable.run();
                    }
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playSound(String sound){
        try {
            String soundPath = FetchResource.sound(getApplicationContext(), sound);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(getApplicationContext(), Uri.parse(soundPath));
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void playSound(int soundId){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundId);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
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
                    playSound(sound);
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
                playSound(ResourceSelector.getPositiveAffirmationSound(context));
            }
        } else {
            playSound(ResourceSelector.getNegativeAffirmationSound(context));
        }
    }

    private void setItemsEnabled(boolean enable) {
        item1.setEnabled(enable);
        item2.setEnabled(enable);
        item3.setEnabled(enable);
        item4.setEnabled(enable);
        item5.setEnabled(enable);
        item6.setEnabled(enable);
    }
}