package classact.com.xprize.activity.mathdrill;

import android.content.ClipData;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Random;

import classact.com.xprize.R;
import classact.com.xprize.utils.ResourceSelector;

public class MathsDrillSixAndFourActivity extends AppCompatActivity {
    private JSONObject allData;
    private MediaPlayer mp;
    private Handler handler;
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
    private int segment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_six_and_four);
        equationNumberOne = (ImageView)findViewById(R.id.equation_one);
        equationNumberTwo = (ImageView)findViewById(R.id.equation_two);
        equationAnswer = (ImageView)findViewById(R.id.equation_answer);
        equationSign = (ImageView) findViewById(R.id.equation_sign);
        equationEqualsSign = (ImageView)findViewById(R.id.equation_equals);
        equationNumberOne.setVisibility(View.INVISIBLE);
        equationNumberTwo.setVisibility(View.INVISIBLE);
        equationAnswer.setVisibility(View.INVISIBLE);
        equationSign.setVisibility(View.INVISIBLE);
        equationEqualsSign.setVisibility(View.INVISIBLE);
        objectsContainer = (RelativeLayout)findViewById(R.id.damasItems);
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
        itemsReceptacle = (RelativeLayout)findViewById(R.id.monkeysReceptable);
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
        initialiseData();
    }

    private void placeOnTable(View view){
        ImageView destination = (ImageView)itemsReceptacle.getChildAt(draggedItems - 1);
        destination.setImageResource(itemResId);
        destination.setVisibility(View.VISIBLE);
        //playSound(getNumberSound());
        if (draggedItems == targetItems){
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable(){public void run(){showEquation();}},1000);
        }
    }

    private int getNumberSound(){
        int sound = 0;
        try{
            switch (draggedItems){
                case 1:
                    sound = allData.getInt("one_sound");
                    break;
                case 2:
                    sound = allData.getInt("two_sound");
                    break;
                case 3:
                    sound = allData.getInt("three_sound");
                    break;
                case 4:
                    sound = allData.getInt("four_sound");
                    break;
                case 5:
                    sound = allData.getInt("five_sound");
                    break;
                case 6:
                    sound = allData.getInt("six_sound");
                    break;
                case 7:
                    sound = allData.getInt("seven_sound");
                    break;
                case 8:
                    sound = allData.getInt("eight_sound");
                    break;
                case 9:
                    sound = allData.getInt("nine_sound");
                    break;
                case 10:
                    sound = allData.getInt("ten_sound");
                    break;
                case 11:
                    sound = allData.getInt("eleven_sound");
                    break;
                case 12:
                    sound = allData.getInt("twelve_sound");
                    break;
                case 13:
                    sound = allData.getInt("thirteen_sound");
                    break;
                case 14:
                    sound = allData.getInt("fourteen_sound");
                    break;
                case 15:
                    sound = allData.getInt("fifteen_sound");
                    break;
                case 16:
                    sound = allData.getInt("sixteen_sound");
                    break;
                case 17:
                    sound = allData.getInt("seveteen_sound");
                    break;
                case 18:
                    sound = allData.getInt("eighteen_sound");
                    break;
                case 19:
                    sound = allData.getInt("nineteen_sound");
                    break;
                case 20:
                    sound = allData.getInt("twenty_sound");
                    break;
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
        return sound;
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
            targetItems = allData.getInt("number_of_given_objects");
            equationNumberOne.setImageResource(allData.getInt("number_of_objects_image"));
            equationNumberTwo.setImageResource(allData.getInt("number_of_given_objects_image"));
            itemResId = allData.getInt("objects_image");
            for(int i = 0; i < count;i++){
                ImageView image = (ImageView)objectsContainer.getChildAt(i);
                image.setImageResource(itemResId);
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
            else{
                equationAnswer.setVisibility(View.VISIBLE);
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

    private void initialiseData(){
        try {
            segment = 1;
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            setupObjects();
            numbers = allData.getJSONArray("numerals");
            setupNumbers();
            equationAnswer.setImageResource(allData.getInt("answer_image"));
            int sound = allData.getInt("dama_has_sound");
            mp = MediaPlayer.create(this, sound);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayNumber();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayNumber(){
        try{
            int sound = allData.getInt("number_of_objects_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    sayHeGives();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }


    private void sayHeGives(){
        try{
            int sound = allData.getInt("he_gives_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    segment = 2;
                    sayNumberToGive();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayNumberToGive(){
        try{
            int sound = allData.getInt("number_of_given_object_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    if (segment == 2)
                        sayToMonkey();
                    else
                        sayToMonkeySpace();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayToMonkey(){
        try{
            int sound = allData.getInt("to_monkey_sound");
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
                    segment = 3;
                    mp.reset();
                    sayNumberToGive();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void sayToMonkeySpace(){
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


    private void showEquation (){
        try{
            equationNumberOne.setVisibility(View.VISIBLE);
            equationNumberTwo.setVisibility(View.VISIBLE);
            equationSign.setVisibility(View.VISIBLE);
            equationEqualsSign.setVisibility(View.VISIBLE);
            int sound = allData.getInt("equation_sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.reset();
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    playTouch();
                }
            });
            mp.start();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }


    private void playTouch(){
        try{
            numbersContainer.setVisibility(View.VISIBLE);
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

    @Override
    public void onPause(){
        super.onPause();
        if (mp != null){
            mp.release();
        }
    }
}
