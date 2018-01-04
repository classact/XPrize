package classact.com.xprize.activity.drill.math;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import classact.com.xprize.activity.DrillActivity;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;

public class MathsDrillSixAndThreeActivity extends DrillActivity implements View.OnTouchListener, View.OnDragListener {
    private JSONObject allData;
    private ImageView numberOne;
    private ImageView numberTwo;
    private ImageView numberThree;
    private JSONArray numbers;
    private RelativeLayout objectsContainer;
    private int[] positions;
    private int draggedItems = 0;
    private RelativeLayout monkeyMouth;
    private int targetItems = 0;

    private SparseArray<NumberObject> numberObjects;

    private boolean dragEnabled;
    private boolean touchEnabled;

    private MathDrill06DViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_six_and_three);

        // View Model
        vm = ViewModelProviders.of(this, viewModelFactory)
                .get(MathDrill06DViewModel.class)
                .register(getLifecycle())
                .prepare(context);

        handler = vm.getHandler();
        mediaPlayer = vm.getMediaPlayer();

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
        monkeyMouth.setOnDragListener(this);
        initialise();
    }

    private void initialise(){
        try {
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            setupObjects();
            numbers = allData.getJSONArray("drill_specific_numbers");
            setupNumbers();
            setupNumberObjects();
            targetItems = allData.getInt("number_of_eaten_objects");

            touchEnabled = false;
            dragEnabled = false;

            String sound = allData.getString("monkey_is_hungry");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayMonkeyEats();
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void setupObjects(){
        try{
            dragEnabled = false; // disable drag
            draggedItems = 0;
            int count = allData.getInt("number_of_objects");
            String res = allData.getString("objects_image");
            int resId = FetchResource.imageId(context, res);
            for(int i = 0; i < count;i++){
                ImageView image = (ImageView) objectsContainer.getChildAt(i);
                loadImage(image, resId);
                image.setVisibility(View.VISIBLE);
                image.setOnTouchListener(this);
            }
        }
        catch(Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
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
                        JSONObject number = numbers.getJSONObject(i);
                        String imageName = number.getString("image");
                        int value = number.getInt("value");
                        int imageId = FetchResource.imageId(context, imageName);
                        loadImage(numberOne, imageId);
                        numberOne.setTag(String.valueOf(value));
                        numberOne.setAlpha(0.2f);
                        break;
                    case 1:
                        number = numbers.getJSONObject(i);
                        imageName = number.getString("image");
                        value = number.getInt("value");
                        imageId = FetchResource.imageId(context, imageName);
                        loadImage(numberTwo, imageId);
                        numberTwo.setTag(String.valueOf(value));
                        numberTwo.setAlpha(0.2f);
                        break;
                    case 2:
                        number = numbers.getJSONObject(i);
                        imageName = number.getString("image");
                        value = number.getInt("value");
                        imageId = FetchResource.imageId(context, imageName);
                        loadImage(numberThree, imageId);
                        numberThree.setTag(String.valueOf(value));
                        numberThree.setAlpha(0.2f);
                        break;
                    default:
                        break;
                }
            }
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
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
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void numberClicked(int position){
        if (touchEnabled) {
            try {
                JSONObject number = numbers.getJSONObject(positions[position - 1]);
                int numberValue = number.getInt("value");
                String numberSound = numberObjects.get(numberValue).getSound();

                if (numberValue == targetItems) {
                    touchEnabled = false;
                    playSound(numberSound, new Runnable() {
                        @Override
                        public void run() {
                            end();
                        }
                    });
                } else {
                    playSound(numberSound, new Runnable() {
                        @Override
                        public void run() {
                            playSound(FetchResource.negativeAffirmation(context), null);
                        }
                    });
                }
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        }
    }

    private void sayMonkeyEats(){
        try{
            String sound = allData.getString("he_eats_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayItemsEaten();
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayItemsEaten(){
        try{
            String sound = allData.getString("objects_eaten");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayDrag();
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }


    private void sayDrag(){
        try{
            String sound = allData.getString("drag_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayobjectsEatenAgain();
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayobjectsEatenAgain(){
        try{
            String sound = allData.getString("objects_eaten");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayToMonkeysMouth();
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayToMonkeysMouth(){
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
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void nextSequence() {
        try {
            playSound(FetchResource.positiveAffirmation(context), new Runnable() {
                @Override
                public void run() {
                    sayTouch();
                }
            });
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayTouch(){
        try{
            String sound = allData.getString("touch_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    numberOne.setAlpha(1.0f);
                    numberTwo.setAlpha(1.0f);
                    numberThree.setAlpha(1.0f);
                    touchEnabled = true;
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void end() {
        playSound(FetchResource.positiveAffirmation(context), new Runnable() {
            @Override
            public void run() {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
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
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
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
                        ImageView view = (ImageView) event.getLocalState();
                        view.setVisibility(View.INVISIBLE);
                        String numberSound = numberObjects.get(draggedItems).getSound();

                        if (draggedItems >= targetItems) {
                            dragEnabled = false;
                            playSound(numberSound, new Runnable() {
                                @Override
                                public void run() {
                                    nextSequence();
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
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
        return false;
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
}
