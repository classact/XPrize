package classact.com.xprize.activity.drill.math;

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
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Random;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;

public class MathsDrillFiveAndOneActivity extends AppCompatActivity {
    private JSONObject allData;
    private MediaPlayer mp;
    // private Handler handler;
    private ImageView numberOne;
    private ImageView numberTwo;
    private ImageView numberThree;
    private JSONArray numbers;
    private RelativeLayout objectsContainer;
    private RelativeLayout numbersContainer;
    private int[] positions;
    private int draggedItems = 0;
    private RelativeLayout itemsReceptacle;
    private int targetItems = 0;
    private int itemResId;
    private boolean isInReceptacle;
    private ImageView equationNumberOne;
    private ImageView equationNumberTwo;
    private ImageView equationAnswer;
    private ImageView equationSign;
    private ImageView equationEqualsSign;

    private final Context THIS = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_five_and_one);
        equationNumberOne = (ImageView)findViewById(R.id.equation_one);
        equationNumberTwo = (ImageView)findViewById(R.id.equation_two);
        equationAnswer = (ImageView)findViewById(R.id.equation_answer);
        equationSign = (ImageView)findViewById(R.id.equation_sign);
        equationEqualsSign = (ImageView)findViewById(R.id.equation_equals);
        equationNumberOne.setVisibility(View.INVISIBLE);
        equationNumberTwo.setVisibility(View.INVISIBLE);
        equationAnswer.setVisibility(View.INVISIBLE);
        equationSign.setVisibility(View.INVISIBLE);
        equationEqualsSign.setVisibility(View.INVISIBLE);
        objectsContainer = (RelativeLayout)findViewById(R.id.itemsContainer);
        numbersContainer = (RelativeLayout)findViewById(R.id.numbers_container);
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
        itemsReceptacle = (RelativeLayout)findViewById(R.id.itemsReceptacle);
        itemsReceptacle.setOnDragListener(new View.OnDragListener() {
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
                        if ( draggedItems <= targetItems) {
                            placeOnTable(v);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED && isInReceptacle) {
                    try {
                        if ( draggedItems > targetItems) {
                            playSound(FetchResource.negativeAffirmation(THIS), null);
                        }
                        else{
                            ImageView view = (ImageView) event.getLocalState();
                            view.setVisibility(View.INVISIBLE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                return true;
            }
        });
        // handler = new Handler();
        initialiseData();
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

    private void initialiseData(){
        try {
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            setupObjects();
            numbers = allData.getJSONArray("numerals");
            setupNumbers();
            equationAnswer.setImageResource(FetchResource.imageId(this, allData, "answer_image"));
            String sound = allData.getString("help_dama_with_maths");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    dragItems();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void placeOnTable(View view){
        ImageView destination = (ImageView)itemsReceptacle.getChildAt(draggedItems - 1);
        destination.setImageResource(itemResId);
        destination.setVisibility(View.VISIBLE);
        playSound(getNumberSound(), null);
        if (draggedItems == targetItems){
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable(){public void run(){showEquation();}},1000);
        }
    }

    private String getNumberSound(){
        String sound = "";
        try{
            switch (draggedItems){
                case 1:
                    sound = allData.getString("one_sound");
                    break;
                case 2:
                    sound = allData.getString("two_sound");
                    break;
                case 3:
                    sound = allData.getString("three_sound");
                    break;
                case 4:
                    sound = allData.getString("four_sound");
                    break;
                case 5:
                    sound = allData.getString("five_sound");
                    break;
                case 6:
                    sound = allData.getString("six_sound");
                    break;
                case 7:
                    sound = allData.getString("seven_sound");
                    break;
                case 8:
                    sound = allData.getString("eight_sound");
                    break;
                case 9:
                    sound = allData.getString("nine_sound");
                    break;
                case 10:
                    sound = allData.getString("ten_sound");
                    break;
                case 11:
                    sound = allData.getString("eleven_sound");
                    break;
                case 12:
                    sound = allData.getString("twelve_sound");
                    break;
                case 13:
                    sound = allData.getString("thirteen_sound");
                    break;
                case 14:
                    sound = allData.getString("fourteen_sound");
                    break;
                case 15:
                    sound = allData.getString("fifteen_sound");
                    break;
                case 16:
                    sound = allData.getString("sixteen_sound");
                    break;
                case 17:
                    sound = allData.getString("seveteen_sound");
                    break;
                case 18:
                    sound = allData.getString("eighteen_sound");
                    break;
                case 19:
                    sound = allData.getString("nineteen_sound");
                    break;
                case 20:
                    sound = allData.getString("twenty_sound");
                    break;
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return sound;
    }

    public boolean dragItem(View view, MotionEvent motionEvent){
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                    view);
            view.startDragAndDrop(data, shadowBuilder, view, 0);
            isInReceptacle = false;
            return true;
        } else {
            return false;
        }
    }

    private void setupObjects(){
        try{
            draggedItems = 0;
            JSONArray items = allData.getJSONArray("items");
            int position = 0;
            for(int i = 0; i < items.length();i++){
                JSONObject item = items.getJSONObject(i);
                int count = item.getInt("number");
                if (i == 0)
                    equationNumberOne.setImageResource(FetchResource.imageId(this, item, "numeral"));
                else
                    equationNumberTwo.setImageResource(FetchResource.imageId(this, item, "numeral"));
                targetItems += count;
                final int resId = FetchResource.imageId(this, item, "image");
                for (int j =0; j < count; j++){
                    ImageView image = (ImageView)objectsContainer.getChildAt(position);
                    image.setImageResource(resId);
                    image.setVisibility(View.VISIBLE);
                    image.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            itemResId = resId;
                            return dragItem(v,event);
                        }
                    });
                    position++;
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
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
                        numberOne.setImageResource(FetchResource.imageId(this, numbers, i, "image"));
                        break;
                    case 1:
                        numberTwo.setImageResource(FetchResource.imageId(this, numbers, i, "image"));
                        break;
                    case 2:
                        numberThree.setImageResource(FetchResource.imageId(this, numbers, i, "image"));
                        break;
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void numberClicked(int position){
        try {
            int correct = numbers.getJSONObject(positions[position - 1]).getInt("right");
            if (correct != 0) {
                equationAnswer.setVisibility(View.VISIBLE);
                playSound(FetchResource.positiveAffirmation(this), new Runnable() {
                    @Override
                    public void run() {
                        if (mp != null) {
                            mp.release();
                        }
                        finish();
                    }
                });
            } else {
                playSound(FetchResource.negativeAffirmation(this), null);
            }

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void dragItems(){
        try{
            String sound = allData.getString("drag_items_onto_table");
            playSound(sound, null);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void showEquation(){
        try{
            equationNumberOne.setVisibility(View.VISIBLE);
            equationNumberTwo.setVisibility(View.VISIBLE);
            equationSign.setVisibility(View.VISIBLE);
            equationEqualsSign.setVisibility(View.VISIBLE);
            String sound = allData.getString("equation_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    playTouch();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    private void playTouch(){
        try{
            numbersContainer.setVisibility(View.VISIBLE);
            objectsContainer.setVisibility(View.INVISIBLE);
            String sound = allData.getString("can_you_find_and_touch");
            playSound(sound, null);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if (mp != null){
            mp.release();
        }
        mp = null;
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
