package classact.com.xprize.activity.drill.sound;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Random;

import classact.com.xprize.R;
import classact.com.xprize.utils.ResourceSelector;

public class SoundDrillSixActivity extends AppCompatActivity {
    private ImageView item1;
    private ImageView item2;
    private ImageView item3;
    private ImageView item4;
    private ImageView item5;
    private ImageView item6;
    private ImageView item7;
    private ImageView item8;
    private ImageView receptacleBox1;
    private ImageView receptacleBox2;
    private ImageView demo_Item;
    private LinearLayout demoItemContainer;
    private ImageView demo_Item_one;
    private ImageView demo_Item_two;
    private RelativeLayout recetacles;
    private RelativeLayout items;
    private LinearLayout demo_letters;
    private int currentItem;
    private String drillData;
    public MediaPlayer mp;
    public float x;
    public float y;
    public int image1;
    public int image2;
    public int [] positions;
    public int drillSound;
    public boolean isInReceptacle1;
    public boolean isInReceptacle2;
    private Handler handler;
    private int correctItems = 0;
    private JSONObject data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_six);
        //getWindow().getDecorView().getRootView().setBackgroundResource(R.drawable.);
        item1 = (ImageView)findViewById(R.id.item1);
        item2 = (ImageView)findViewById(R.id.item2);
        item3 = (ImageView)findViewById(R.id.item3);
        item4 = (ImageView)findViewById(R.id.item4);
        item5 = (ImageView)findViewById(R.id.item5);
        item6 = (ImageView)findViewById(R.id.item6);
        item7 = (ImageView)findViewById(R.id.item7);
        item8 = (ImageView)findViewById(R.id.item8);
        demo_Item = (ImageView)findViewById(R.id.lonely_letter);
        demoItemContainer = (LinearLayout) findViewById(R.id.lonely_letter_container);
        items = (RelativeLayout)findViewById(R.id.items);
        demo_Item_one = (ImageView)findViewById(R.id.demo_letter_one);
        demo_Item_two = (ImageView)findViewById(R.id.demo_letter_two);
        demo_letters = (LinearLayout) findViewById(R.id.demo_letters);
        receptacleBox1 = (ImageView)findViewById(R.id.receptacle1_label);
        receptacleBox2 = (ImageView)findViewById(R.id.receptacle2_label);
        recetacles= (RelativeLayout)findViewById(R.id.recetacles);
        drillData = getIntent().getExtras().getString("data");
        handler = new Handler(Looper.getMainLooper());
        initialiseData();

        item1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentItem = 1;
                return dragItem(v,event);
            }
        });
        item2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentItem = 2;
                //if (positions[currentItem - 1]== image1)
                //    playSound(image1Sound);
                //else
                 //   playSound(image2Sound);
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

        item7.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentItem = 7;
                return dragItem(v,event);
            }
        });

        item8.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentItem = 8;
                return dragItem(v,event);
            }
        });

        receptacleBox1.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                if (action == DragEvent.ACTION_DRAG_ENTERED)
                    isInReceptacle1 = true;
                else if (action == DragEvent.ACTION_DRAG_EXITED)
                    isInReceptacle1 = false;
                else if (event.getAction() == DragEvent.ACTION_DROP && isInReceptacle1) {
                    try {
                        if ( positions[currentItem - 1] == image1) {
                            reward();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        finish();
                    }
                }
                else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED && isInReceptacle1) {
                    try {
                        if ( positions[currentItem - 1] != image1) {
                            playSound(ResourceSelector.getNegativeAffirmationSound(getApplicationContext()));
                        }
                        else{
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

        receptacleBox2.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                if (action == DragEvent.ACTION_DRAG_ENTERED)
                    isInReceptacle2 = true;
                else if (action == DragEvent.ACTION_DRAG_EXITED)
                    isInReceptacle2 = false;
                else if (event.getAction() == DragEvent.ACTION_DROP && isInReceptacle2 ) {
                    try {
                        if ( positions[currentItem - 1] == image2) {
                            reward();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        finish();
                    }
                }
                else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED && isInReceptacle2) {
                    try {
                        if ( positions[currentItem - 1] != image2) {
                            playSound(ResourceSelector.getNegativeAffirmationSound(getApplicationContext()));
                        }
                        else{
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
        try {
            mp = MediaPlayer.create(this, data.getInt("we_can_write_the_letter"));
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    playDrillLetter(endIntro);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void playDrillLetter(final Runnable r){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + drillSound);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    handler.postDelayed(r, 500);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private Runnable endIntro = new Runnable(){
        @Override
        public void run() {
            playEndIntroSound();
        }
    };

    public void playEndIntroSound(){
        try {
            int sound = data.getInt("in_two_ways");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    handler.postDelayed(startLowerCase, 1000);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private Runnable startLowerCase = new Runnable(){
        @Override
        public void run() {
            playStartLowerCase();
        }
    };

    private void playStartLowerCase(){
        try {
            demoItemContainer.setVisibility(View.VISIBLE);
            demo_letters.setVisibility(View.GONE);
            demo_Item.setImageResource(data.getInt("small_letter"));
            int sound = data.getInt("this_is_the_lower_case");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    playDrillLetter(startUpperCase);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private Runnable startUpperCase = new Runnable(){
        @Override
        public void run() {
            playUpperCase();
        }
    };

    private void playUpperCase(){
        try {
            demo_Item.setImageResource(data.getInt("big_letter"));
            int sound = data.getInt("this_is_the_upper_case");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(this, myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    playDrillLetter(startDraw);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private Runnable startDraw = new Runnable(){
        @Override
        public void run() {
            playDraw();
        }
    };

    private void playDraw(){
        try {
            demoItemContainer.setVisibility(View.GONE);
            recetacles.setVisibility(View.VISIBLE);
            items.setVisibility(View.VISIBLE);
            int sound = data.getInt("drag_the_letters");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
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

    public void reward() {
        playSound(ResourceSelector.getPositiveAffirmationSound(this));
        correctItems ++;
        if (correctItems == 8){
            finish();
        }
    }

    private void initialiseData(){
        try{
            data = new JSONObject(drillData);
            Random rand = new Random();
            drillSound = data.getInt("letter_sound");
            if (rand.nextInt(2) == 0) {
                image1 = data.getInt("small_letter");
                image2 = data.getInt("big_letter");
            }
            else{
                image2 = data.getInt("small_letter");
                image1 = data.getInt("big_letter");
            }
            ((ImageView)findViewById(R.id.receptacle1_label)).setImageResource(image1);
            ((ImageView)findViewById(R.id.receptacle2_label)).setImageResource(image2);
            demo_Item_one.setImageResource(data.getInt("small_letter"));
            demo_Item_two.setImageResource(data.getInt("big_letter"));
            positions = new int[8];
            Arrays.fill(positions,0);
            int countOne = 0;
            int countTwo = 0;
            for(int i = 0; i < 8; i++){
                boolean assigned = false;
                while (!assigned) {
                    int aOrB = rand.nextInt(100);
                    if (aOrB % 2 == 0 && countOne < 4) {
                        countOne++;
                        positions[i] = image1;
                        assigned = true;
                    }
                    else if ( countTwo < 4) {
                        countTwo++;
                        positions[i] = image2;
                        assigned = true;
                    }
                    else if ((countOne == 4 )&& (countTwo == 4))
                        assigned = true;
                }
            }
            item1.setImageResource(positions[0]);
            item2.setImageResource(positions[1]);
            item3.setImageResource(positions[2]);
            item4.setImageResource(positions[3]);
            item5.setImageResource(positions[4]);
            item6.setImageResource(positions[5]);
            item7.setImageResource(positions[6]);
            item8.setImageResource(positions[7]);

        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public boolean dragItem(View view, MotionEvent motionEvent){
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                    view);
            view.startDrag(data, shadowBuilder, view, 0);
            isInReceptacle1 = false;
            isInReceptacle2 = false;
            //view.setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }

    public void playSound(int soundid){
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
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

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
