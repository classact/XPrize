package classact.com.xprize.activity.sounddrill;

import android.content.ClipData;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import classact.com.xprize.R;
import classact.com.xprize.utils.ResourceSelector;

public class SoundDrillFourActivity extends AppCompatActivity {
    private ImageView item1;
    private ImageView item2;
    private ImageView item3;
    private ImageView item4;
    private ImageView item5;
    private ImageView item6;
    private ImageView toyBox;
    private int currentItem;
    private int current_reward = 1;
    private String drillData;
    private int totalItems = 6;
    private JSONArray images;
    private MediaPlayer mp;
    float x;
    float y;
    private boolean entered;
    private int play_mode = 1;
    private Handler handler;
    private int drillSound;
    JSONObject params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_four);

        item1 = (ImageView) findViewById(R.id.item1);
        item2 = (ImageView) findViewById(R.id.item2);
        item3 = (ImageView) findViewById(R.id.item3);
        item4 = (ImageView) findViewById(R.id.item4);
        item5 = (ImageView) findViewById(R.id.item5);
        item6 = (ImageView) findViewById(R.id.item6);


        drillData = getIntent().getExtras().getString("data");
        initialiseData(drillData);
        toyBox = (ImageView) findViewById(R.id.toybox);

        item1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentItem = 1;
                x = v.getX();
                y = v.getZ();
                return dragItem(v,event);
            }
        });
        item2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentItem = 2;
                return dragItem(v,event);
            }
        });
        item3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentItem = 3;
                return dragItem(v,event);
            }
        });
        item4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentItem = 4;
                return dragItem(v,event);
            }
        });

        item5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentItem = 5;
                return dragItem(v,event);
            }
        });

        item6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentItem = 6;
                return dragItem(v,event);
            }
        });


        toyBox.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                if (action == DragEvent.ACTION_DRAG_ENTERED)
                    entered = true;
                else if (action == DragEvent.ACTION_DRAG_EXITED)
                    entered = false;
                else if (event.getAction() == DragEvent.ACTION_DROP && entered) {
                    try {
                        int right = images.getJSONObject(currentItem - 1).getInt("right");
                        if (right == 1) {
                            /*switch (current_reward) {
                                case 1:
                                    reward1.setImageResource(R.drawable.rewardball1colour);
                                    break;
                                case 2:
                                    reward2.setImageResource(R.drawable.rewardball1colour);
                                    break;
                                case 3:
                                    reward3.setImageResource(R.drawable.rewardball1colour);
                                    break;
                                case 4:
                                    reward4.setImageResource(R.drawable.rewardball1colour);
                                    break;
                            }*/
                            //playSound(R.raw.good_job);
                            playObjectSound(images.getJSONObject(currentItem - 1).getInt("sound"));
                        }
                        else{
                            playRewardSound(false);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        finish();
                    }
                }
                else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED  && entered) {
                    try {
                        int right = images.getJSONObject(currentItem - 1).getInt("right");
                        if (right == 1) {
                            ImageView view = (ImageView) event.getLocalState();
                            view.setVisibility(View.INVISIBLE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        finish();
                    }
                }
                return true;
            }
        });
        play_mode = 1;
        handler = new Handler(Looper.getMainLooper());
        playIntroSound();
    }

    private void playObjectSound(int soundid){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundid);
            mp.reset();
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    current_reward++;
                    if (current_reward > 4) {
                        playRewardSound(true);
                        finish();
                    }
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void playRewardSound(boolean affirm){
        if (affirm)
            playSound(ResourceSelector.getPositiveAffirmationSound(this));
        else
            playSound(ResourceSelector.getNegativeAffirmationSound(this));
    }

    private void playSound(int soundid){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundid);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
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
            drillSound = params.getInt("drillsound");
           // getWindow().getDecorView().getRootView().setBackgroundResource(params.getInt("background"));
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void playIntroSound(){
        try {
            play_mode = 1;
            if (mp == null)
                mp = MediaPlayer.create(this, params.getInt("dama_needs_to_clean"));
            else {
                Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + params.getInt("dama_needs_to_clean"));
                mp.setDataSource(this, myUri);
                mp.prepare();
            }
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    playBeginningOfDrillSound();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void resetObject(){
        try{
            item1.setImageResource(images.getJSONObject(0).getInt("image"));
            item2.setImageResource(images.getJSONObject(1).getInt("image"));
            item3.setImageResource(images.getJSONObject(2).getInt("image"));
            item4.setImageResource(images.getJSONObject(3).getInt("image"));
            item5.setImageResource(images.getJSONObject(4).getInt("image"));
            item6.setImageResource(images.getJSONObject(5).getInt("image"));
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public boolean dragItem(View view, MotionEvent motionEvent){
        if (play_mode == 2) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                entered = false;
                return true;
            } else {
                return false;
            }
        }
        else{
            try{
                playSound(images.getJSONObject(currentItem - 1).getInt("sound"));
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
            return false;
        }
    }

    public void playBeginningOfDrillSound(){
        try {
            int sound = params.getInt("drag_the_pictures_that_start");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    playDrillSound();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private Runnable startDrill = new Runnable(){
        @Override
        public void run() {
            playBeginningOfDrillSound();
        }
    };

    public void playDrillSound(){
        try{
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + drillSound);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    try {
                        mp.reset();
                        play_mode = 2;
                        playSound(params.getInt("into_the_box"));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ;
                    }
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private Runnable sayDrillSound = new Runnable(){
        @Override
        public void run() {
            playDrillSound();
        }
    };

    private Runnable endDrillInstructions = new Runnable(){
        @Override
        public void run() {
            play_mode = 2;
            //playSound(R.raw.into_the_box);
        }
    };

    @Override
    public void onPause(){
        super.onPause();
        if (mp != null){
            mp.release();
        }
    }

//    @Override
//    public void onBackPressed() {
//    }
}

