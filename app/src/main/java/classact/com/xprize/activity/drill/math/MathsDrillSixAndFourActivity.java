package classact.com.xprize.activity.drill.math;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Random;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.ResourceSelector;

public class MathsDrillSixAndFourActivity extends AppCompatActivity implements View.OnTouchListener, View.OnDragListener {
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

    private SparseArray<NumberObject> numberObjects;

    private RelativeLayout monkey;

    private boolean dragEnabled;
    private boolean touchEnabled;

    private RelativeLayout parentView;
    private final Context THIS = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_six_and_four);

        parentView = (RelativeLayout) findViewById(R.id.activity_maths_drill_six_and_four);

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

        monkey = new RelativeLayout(THIS);
        // monkey.setBackgroundColor(Color.argb(100, 255, 0, 0));
        parentView.addView(monkey);
        RelativeLayout.LayoutParams monkeyLP = (RelativeLayout.LayoutParams) monkey.getLayoutParams();
        monkeyLP.width = 360;
        monkeyLP.height = 460;
        monkeyLP.topMargin = 400;
        monkeyLP.leftMargin = 1200;
        monkey.setLayoutParams(monkeyLP);

        monkey.setOnDragListener(this);
        initialiseData();
    }

    private void initialiseData(){
        try {
            segment = 1;
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            setupObjects();
            numbers = allData.getJSONArray("numerals");
            setupNumbers();
            setupNumberObjects();

            String answerImage = allData.getString("answer_image");
            int answerImageId = FetchResource.imageId(THIS, answerImage);
            equationAnswer.setImageResource(answerImageId);

            touchEnabled = false;
            dragEnabled = false;

            String sound = allData.getString("dama_has_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayNumber();
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void setupObjects(){
        try{
            draggedItems = 0;
            int count = allData.getInt("number_of_objects");
            targetItems = allData.getInt("number_of_given_objects");

            String numOfObjectsImage = allData.getString("number_of_objects_image");
            int numOfObjectsImageId = FetchResource.imageId(THIS, numOfObjectsImage);

            String numOfObjectsGivenImage = allData.getString("number_of_given_objects_image");
            int numOfObjectsGivenImageId = FetchResource.imageId(THIS, numOfObjectsGivenImage);

            equationNumberOne.setImageResource(numOfObjectsImageId);
            equationNumberTwo.setImageResource(numOfObjectsGivenImageId);

            String objectsImage = allData.getString("objects_image");
            itemResId = FetchResource.imageId(THIS, objectsImage);

            for(int i = 0; i < count;i++){
                ImageView image = (ImageView)objectsContainer.getChildAt(i);
                image.setImageResource(itemResId);
                image.setVisibility(View.VISIBLE);
                image.setOnTouchListener(this);
            }
        }
        catch(Exception ex){
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void setupNumbers(){
        try {
            System.out.println("NUMBERS SETUP"+ numbers.length());
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
                        JSONObject number = numbers.getJSONObject(i);
                        String numberImage = number.getString("image");
                        int value = number.getInt("value");
                        int numberImageid = FetchResource.imageId(THIS, numberImage);
                        numberOne.setImageResource(numberImageid);
                        numberOne.setTag(String.valueOf(value));
                        break;
                    case 1:
                        number = numbers.getJSONObject(i);
                        numberImage = number.getString("image");
                        value = number.getInt("value");
                        numberImageid = FetchResource.imageId(THIS, numberImage);
                        numberTwo.setImageResource(numberImageid);
                        numberTwo.setTag(String.valueOf(value));
                        break;
                    case 2:
                        number = numbers.getJSONObject(i);
                        numberImage = number.getString("image");
                        value = number.getInt("value");
                        numberImageid = FetchResource.imageId(THIS, numberImage);
                        numberThree.setImageResource(numberImageid);
                        numberThree.setTag(String.valueOf(value));
                        break;
                    default:
                        break;
                }
            }
        }
        catch (Exception ex){
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void numberClicked(int position){
        if (touchEnabled) {
            try {
                JSONObject number = numbers.getJSONObject(positions[position - 1]);
                String numberSound = number.getString("sound");
                int numberValue = number.getInt("value");
                int count = allData.getInt("number_of_objects");
                if (numberValue == (count - targetItems)) {
                    touchEnabled = false;
                    equationAnswer.setVisibility(View.VISIBLE);
                    playSound(numberSound, new Runnable() {
                        @Override
                        public void run() {
                            playSound(FetchResource.positiveAffirmation(THIS), new Runnable() {
                                @Override
                                public void run() {
                                    if (mp != null) {
                                        mp.release();
                                    }
                                    finish();
                                }
                            });
                        }
                    });
                } else {
                    playSound(numberSound, new Runnable() {
                        @Override
                        public void run() {
                            playSound(FetchResource.negativeAffirmation(THIS), null);
                        }
                    });
                }
            } catch (Exception ex) {
                Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        }
    }

    private void sayNumber(){
        try{
            String sound = allData.getString("number_of_objects_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayHeGives();
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }


    private void sayHeGives(){
        try{
            String sound = allData.getString("he_gives_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    segment = 2;
                    sayNumberToGive();
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayNumberToGive(){
        try{
            String sound = allData.getString("number_of_given_object_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    if (segment == 2) {
                        sayToMonkey();
                    } else {
                        sayToMonkeySpace();
                    }
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayToMonkey(){
        try{
            String sound = allData.getString("to_monkey_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayDrag();
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayDrag(){
        try{
            String sound = allData.getString("drag_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    segment = 3;
                    sayNumberToGive();
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayToMonkeySpace(){
        try{
            String sound = allData.getString("to_the_monkey_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    dragEnabled = true;
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void placeOnTable(){
        ImageView destination = (ImageView) itemsReceptacle.getChildAt(draggedItems - 1);
        destination.setImageResource(itemResId);
        destination.setVisibility(View.VISIBLE);
    }

    private void showEquation (){
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
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void playTouch(){
        try{
            numbersContainer.setVisibility(View.VISIBLE);
            String sound = allData.getString("touch_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    touchEnabled = true;
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int action = event.getAction();
        try {
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    String tag = (String) v.getTag();
                    ClipData.Item item = new ClipData.Item(tag);
                    ClipData dragData = new ClipData(tag, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                    View.DragShadowBuilder dragShadow = new View.DragShadowBuilder(v);
                    v.startDragAndDrop(dragData, dragShadow, v, 0);
                    v.setVisibility(View.INVISIBLE);
                    return true;
                default:
                    break;
            }
        } catch (Exception ex) {
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        final int action = event.getAction();
        try {
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        return true;
                    }
                    return false;
                case DragEvent.ACTION_DRAG_ENTERED:
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    return true;
                case DragEvent.ACTION_DROP:
                    if (dragEnabled) {
                        if (draggedItems < targetItems) {
                            draggedItems++;
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                case DragEvent.ACTION_DRAG_ENDED:
                    if (event.getResult()) {

                        if ( draggedItems <= targetItems) {
                            placeOnTable();
                        }

                        ImageView view = (ImageView) event.getLocalState();
                        view.setVisibility(View.INVISIBLE);
                        String numberSound = numberObjects.get(draggedItems).getSound();

                        if (draggedItems >= targetItems) {
                            dragEnabled = false;
                            playSound(numberSound, new Runnable() {
                                @Override
                                public void run() {
                                    playSound(FetchResource.positiveAffirmation(THIS), new Runnable() {
                                        @Override
                                        public void run() {
                                            showEquation();
                                        }
                                    });
                                }
                            });
                        } else {
                            playSound(numberSound, null);
                        }
                    } else {
                        ImageView view = (ImageView) event.getLocalState();
                        view.setVisibility(View.VISIBLE);
                    }
                    return true;
                default:
                    break;
            }
        } catch (Exception ex) {
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
        return false;
    }

    private void setupNumberObjects() {
        try {
            numberObjects = new SparseArray<>();
            JSONArray numberObjectsArray = allData.getJSONArray("count_numbers");
            for (int i = 0; i < numberObjectsArray.length(); i++) {
                JSONObject number = numberObjectsArray.getJSONObject(i);
                String image = number.getString("image");
                String sound = number.getString("sound");
                int value = number.getInt("value");
                numberObjects.put(value, new NumberObject(image, sound, value));
            }
        } catch (Exception ex) {
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private class NumberObject {
        private String image;
        private String sound;
        private int value;

        private NumberObject(String image, String sound, int value) {
            this.image = image;
            this.sound = sound;
            this.value = value;
        }

        public String getImage() {
            return image;
        }

        public String getSound() {
            return sound;
        }

        public int getValue() {
            return value;
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
