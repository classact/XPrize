package classact.com.xprize.activity.drill.sound;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import classact.com.xprize.R;
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.ResourceSelector;

public class SoundDrillFourActivity extends DrillActivity {
    private ImageView item1;
    private ImageView item2;
    private ImageView item3;
    private ImageView item4;
    private ImageView item5;
    private ImageView item6;
    private ImageView toyBox;
    private int currentItem;
    private int current_reward = 0;
    private String drillData;
    private Handler handler;
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

    private final Context THIS = this;

    private SoundDrill04ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_four);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(SoundDrill04ViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        toyBox = (ImageView) findViewById(R.id.toybox);

        item1 = (ImageView) findViewById(R.id.item1);
        item2 = (ImageView) findViewById(R.id.item2);
        item3 = (ImageView) findViewById(R.id.item3);
        item4 = (ImageView) findViewById(R.id.item4);
        item5 = (ImageView) findViewById(R.id.item5);
        item6 = (ImageView) findViewById(R.id.item6);

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

        toyBox.setOnDragListener(onItemDraggedIntoToyboxListener);
        handler = new Handler(Looper.getMainLooper());

        drillData = getIntent().getExtras().getString("data");
        initialiseData(drillData);
        playDamaNeedsToCleanSound();
    }

    private void initialiseData (String data){
        try {
            params = new JSONObject(data);
            images = params.getJSONArray("images");
            item1.setImageResource(images.getJSONObject(0).getInt("image"));
            item2.setImageResource(images.getJSONObject(1).getInt("image"));
            item3.setImageResource(images.getJSONObject(2).getInt("image"));
            item4.setImageResource(images.getJSONObject(3).getInt("image"));
            item5.setImageResource(images.getJSONObject(4).getInt("image"));
            item6.setImageResource(images.getJSONObject(5).getInt("image"));
            drillSound = params.getString("drillsound");
        }
        catch (Exception ex){
            System.err.println("------------------------------------------------------");
            System.err.println("SoundDrillFourActivity.initialiseData > Exception: " + ex.getMessage());
            System.err.println("------------------------------------------------------");
            ex.printStackTrace();
            System.err.println("------------------------------------------------------");
            if (mp != null) {
                mp.release();
            }
            finish();
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
            System.err.println("------------------------------------------------------");
            System.err.println("SoundDrillFourActivity.playDamaNeedsToCleanSound > Exception: " + ex.getMessage());
            System.err.println("------------------------------------------------------");
            ex.printStackTrace();
            System.err.println("------------------------------------------------------");
            if (mp != null) {
                mp.release();
            }
            finish();
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
        catch (Exception ex){
            System.err.println("------------------------------------------------------");
            System.err.println("SoundDrillFourActivity.playDragThePicturesThatStartWithSound > Exception: " + ex.getMessage());
            System.err.println("------------------------------------------------------");
            ex.printStackTrace();
            System.err.println("------------------------------------------------------");
            if (mp != null) {
                mp.release();
            }
            finish();
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
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            mp = null;
            Globals.bugBar(this.findViewById(android.R.id.content), "sound", drillSound).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    playIntoTheBoxSound();
                }
            }, 800);
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
        catch (Exception ex){
            System.err.println("------------------------------------------------------");
            System.err.println("SoundDrillFourActivity.playIntoTheBoxSound > Exception: " + ex.getMessage());
            System.err.println("------------------------------------------------------");
            ex.printStackTrace();
            System.err.println("------------------------------------------------------");
            if (mp != null) {
                mp.release();
            }
            finish();
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
            if (mp != null) {
                mp.release();
            }
            mp = null;
            Globals.bugBar(this.findViewById(android.R.id.content), "sound", sound).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mRunnable != null) {
                        mRunnable.run();
                    }
                }
            }, 800);
        }
    }

    private void playSoundAndRunnableAfterCompletion(int soundId) {
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundId);
            if (mp == null) {
                mp = new MediaPlayer();
            }
            mp.reset();
            mp.setDataSource(this, myUri);
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
            System.err.println("------------------------------------------------------");
            System.err.println("SoundDrillFourActivity.playSoundAndRunnableAfterCompletion > Exception: " + ex.getMessage());
            System.err.println("------------------------------------------------------");
            ex.printStackTrace();
            System.err.println("------------------------------------------------------");
            if (mp != null) {
                mp.release();
            }
            finish();
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
            if (mp != null) {
                mp.release();
            }
            mp = null;
            Globals.bugBar(this.findViewById(android.R.id.content), "sound", sound).show();
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
            System.err.println("------------------------------------------------------");
            System.err.println("SoundDrillFourActivity.playSound > Exception: " + ex.getMessage());
            System.err.println("------------------------------------------------------");
            ex.printStackTrace();
            System.err.println("------------------------------------------------------");
            /*if (mp != null) {
                mp.release();
            }
            finish();*/
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
                System.err.println("------------------------------------------------------");
                System.err.println("SoundDrillFourActivity.dragItem > Exception: " + ex.getMessage());
                System.err.println("------------------------------------------------------");
                ex.printStackTrace();
                System.err.println("------------------------------------------------------");
                if (mp != null) {
                    mp.release();
                }
                finish();
            }
        }
        return false;
    }

    private View.OnDragListener onItemDraggedIntoToyboxListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
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
                        Globals.playStarWorks(THIS, toyBox);
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
                System.err.println("------------------------------------------------------");
                System.err.println("SoundDrillFourActivity.onItemDraggedIntoToyboxListener > Exception: " + ex.getMessage());
                System.err.println("------------------------------------------------------");
                ex.printStackTrace();
                System.err.println("------------------------------------------------------");
                if (mp != null) {
                    mp.release();
                }
                finish();
            }
            return false;
        }
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
                        if (mp != null) {
                            mp.release();
                        }
                        finish();
                    }
                };
                playSoundAndRunnableAfterCompletion(ResourceSelector.getPositiveAffirmationSound(this));
            } else {
                playSound(ResourceSelector.getPositiveAffirmationSound(this));
            }
        } else {
            playSound(ResourceSelector.getNegativeAffirmationSound(this));
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int action = event.getAction();

        if (action == KeyEvent.ACTION_UP) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    onBackPressed();
                    return true;
                default:
                    return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mp != null) {
            mp.stop();
            mp.release();
        }
        setResult(Globals.TO_MAIN);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}