package classact.com.xprize.activity.drill.math;

import android.content.ClipData;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Random;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.ResourceSelector;

public class MathsDrillSixAndThreeActivity extends AppCompatActivity {
    private JSONObject allData;
    private MediaPlayer mp;
    private Handler handler;
    private ImageView numberOne;
    private ImageView numberTwo;
    private ImageView numberThree;
    private JSONArray numbers;
    private RelativeLayout objectsContainer;
    private int[] positions;
    private int draggedItems = 0;
    private RelativeLayout monkeyMouth;
    private int targetItems = 0;
    private boolean isInReceptacle;

    private final Context THIS = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_six_and_three);
        objectsContainer = (RelativeLayout)findViewById(R.id.itemsContainer);
        numberOne = (ImageView)findViewById(R.id.numeral_1);
        numberOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberClicked(1);
            }
        });
        numberTwo = (ImageView)findViewById(R.id.numeral_2);
        numberTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberClicked(2);
            }
        });
        numberThree = (ImageView)findViewById(R.id.numeral_3);
        numberThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberClicked(3);
            }
        });
        monkeyMouth = (RelativeLayout) findViewById(R.id.monkeyEnclosure);
        monkeyMouth.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                if (action == DragEvent.ACTION_DRAG_ENTERED)
                    isInReceptacle = true;
                else if (action == DragEvent.ACTION_DRAG_EXITED)
                    isInReceptacle = false;
                else if (event.getAction() == DragEvent.ACTION_DROP && isInReceptacle) {
                    try {
                        draggedItems ++;
                        if (draggedItems == targetItems){
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable(){
                                @Override
                                public void run(){
                                    sayTouch();
                                }
                            },500);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        finish();
                    }
                }
                else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED && isInReceptacle) {
                    try {
                        if ( draggedItems > targetItems) {
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
        initialise();
    }

    private void playSound(int soundId){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundId);
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

    private void setupNumbers(){
        try {
            positions = new int[3];
            Arrays.fill(positions, -1);
            Random rand = new Random();
            for (int i = 0; i < 3; i++) {
                int pos = rand.nextInt(3);
                if (positions[pos] == -1) {
                    positions[pos] = i;
                } else {
                    boolean done = false;
                    for (int j = 2; j > -1; j--) {
                        if (positions[j] == -1 && !done) {
                            positions[j] = i;
                            done = true;
                            pos = j;
                        }
                    }
                }
                switch (pos) {
                    case 0:
                        numberOne.setImageResource(numbers.getJSONObject(i).getInt("image"));
                        break;
                    case 1:
                        numberTwo.setImageResource(numbers.getJSONObject(i).getInt("image"));
                        break;
                    case 2:
                        numberThree.setImageResource(numbers.getJSONObject(i).getInt("image"));
                        break;
                }
            }
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
            isInReceptacle = false;
            return true;
        } else {
            return false;
        }
    }

    private void setupObjects(){
        try{
            draggedItems = 0;
            int count = allData.getInt("number_of_objects");
            int resId = allData.getInt("objects_image");
            for(int i = 0; i < count;i++){
                ImageView image = (ImageView)objectsContainer.getChildAt(i);
                image.setImageResource(resId);
                image.setVisibility(View.VISIBLE);
                image.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return dragItem(v,event);
                    }
                });
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public void numberClicked(int position){
        try {
            int correct = numbers.getJSONObject(positions[position - 1]).getInt("right");
            int sound = ResourceSelector.getPositiveAffirmationSound(getApplicationContext());
            mp.reset();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    finish();
                }
            });
            if (correct == 0) {
                sound = ResourceSelector.getNegativeAffirmationSound(getApplicationContext());
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.reset();
                    }
                });
            }
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();

        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void initialise(){
        try {
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            setupObjects();
            numbers = allData.getJSONArray("numerals");
            setupNumbers();
            int sound = allData.getInt("monkey_is_hungry");
            targetItems = allData.getInt("number_of_eaten_objects");
            mp = MediaPlayer.create(this, sound);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayMonkeyEats();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayMonkeyEats(){
        try{
            int sound = allData.getInt("he_eats_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayItemsEaten();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayItemsEaten(){
        try{
            int sound = allData.getInt("objects_eaten");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayDrag();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }


    private void sayDrag(){
        try{
            int sound = allData.getInt("drag_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayobjectsEatenAgain();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayobjectsEatenAgain(){
        try{
            int sound = allData.getInt("objects_eaten");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayToMonkeysMouth();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayToMonkeysMouth(){
        try{
            int sound = allData.getInt("to_the_monkey_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayTouch(){
        try{
            int sound = allData.getInt("touch_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void playSound(String sound, final Runnable action) {
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
                    if (action != null) {
                        action.run();
                    }
                }
            });
            mp.prepare();
        } catch (Exception ex) {
            ex.printStackTrace();
            mp = null;
            Globals.bugBar(this.findViewById(android.R.id.content), "sound", sound).show();
            if (action != null) {
                action.run();
            }
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if (mp != null){
            mp.release();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onBackPressed() {
        if (mp != null) {
            mp.release();
        }
        setResult(Code.NAV_MENU);
        finish();
    }
}
