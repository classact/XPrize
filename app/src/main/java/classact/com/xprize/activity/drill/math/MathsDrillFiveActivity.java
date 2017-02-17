package classact.com.xprize.activity.drill.math;

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
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Random;

import classact.com.xprize.R;
import classact.com.xprize.utils.ResourceSelector;

public class MathsDrillFiveActivity extends AppCompatActivity {
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
    boolean dragEnabled;
    boolean drillComplete;
    boolean endDrill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_five);
        objectsContainer = (RelativeLayout)findViewById(R.id.itemsContainer);
        numbersContainer = (RelativeLayout)findViewById(R.id.numbers_container);
        numberOne = (ImageView)findViewById(R.id.cakedemo_obect);
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
                if (action == DragEvent.ACTION_DRAG_ENTERED) {
                    if (dragEnabled && !drillComplete) {
                        System.out.println("000A");
                        isInReceptacle = true;
                    }
                } else if (action == DragEvent.ACTION_DRAG_EXITED) {
                    if (dragEnabled && !drillComplete) {
                        System.out.println("000B");
                        isInReceptacle = false;
                    }
                } else if (event.getAction() == DragEvent.ACTION_DROP && isInReceptacle) {
                    System.out.println("000C");
                    if (dragEnabled && !drillComplete) {
                        System.out.println("000CA");
                        try {
                            draggedItems++;
                            if (draggedItems <= targetItems) {
                                placeOnShelf(v);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            if (mp != null) {
                                mp.release();
                            }
                            finish();
                        }
                    }
                } else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED && isInReceptacle) {
                    System.out.println("000D");
                    try {
                        if (draggedItems > targetItems) {
                            System.out.println("000DA");
                            if (dragEnabled && !drillComplete) {
                                System.out.println("000DAA");
                                playSound(ResourceSelector.getNegativeAffirmationSound(getApplicationContext()));
                            }
                        } else {
                            System.out.println("000DB");
                            ImageView view = (ImageView) event.getLocalState();
                            view.setVisibility(View.INVISIBLE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        if (mp != null) {
                            mp.release();
                        }
                        finish();
                    }
                }
                return true;
            }
        });
        handler = new Handler();
        dragEnabled = false;
        drillComplete = false;
        endDrill = false;
        initialise();
    }

    private void initialise(){
        try {
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            setupObjects();
            numbers = allData.getJSONArray("numerals");
            setupNumbers();
            int sound = allData.getInt("help_monkey_pack");
            mp = MediaPlayer.create(this, sound);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    dragItems();
                }
            });
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mp.start();
                }
            }, 500);
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    private void placeOnShelf(View view){
        try {
            ImageView destination = (ImageView) itemsReceptacle.getChildAt(draggedItems - 1);
            destination.setImageResource(itemResId);
            destination.setVisibility(View.VISIBLE);
            playSound(getNumberSound());
            if (draggedItems == targetItems) {
                dragEnabled = false;
                drillComplete = true;
                /*new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    public void run() {
                        playTouch();
                    }
                }, 1000);*/
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
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
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
        return sound;
    }

    public boolean dragItem(View view, MotionEvent motionEvent){
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                    view);
            view.startDragAndDrop(data, shadowBuilder, view, 0);
            if (dragEnabled && !drillComplete) {
                isInReceptacle = false;
            }
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
                targetItems += count;
                final int resId = item.getInt("image");
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
            if (mp != null) {
                mp.release();
            }
            finish();
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
                    if (drillComplete && !endDrill) {
                        endDrill = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                playSound(ResourceSelector.getPositiveAffirmationSound(getApplicationContext()));
                            }
                        }, 500);
                    } else if (endDrill) {
                        mp.release();
                        if (mp != null) {
                            mp.release();
                        }
                        finish();
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
            if (mp != null) {
                mp.release();
            }
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
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    private void playTouch(){
        try{
            numbersContainer.setVisibility(View.VISIBLE);
            objectsContainer.setVisibility(View.INVISIBLE);
            int sound = allData.getInt("can_you_find_and_touch");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
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
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }

    private void dragItems(){
        try{
            int sound = allData.getInt("drag_items_onto_shelf");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
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
                    dragEnabled = true;
                }
            });
            mp.prepare();
        }
        catch (Exception ex){
            ex.printStackTrace();
            if (mp != null) {
                mp.release();
            }
            finish();
        }
    }
}
