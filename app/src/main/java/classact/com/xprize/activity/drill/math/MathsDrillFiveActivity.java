package classact.com.xprize.activity.drill.math;

import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.FisherYates;
import classact.com.xprize.utils.Square;
import classact.com.xprize.utils.SquarePacker;

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

    private final Context THIS = this;

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
                        isInReceptacle = true;
                    }
                } else if (action == DragEvent.ACTION_DRAG_EXITED) {
                    if (dragEnabled && !drillComplete) {
                        isInReceptacle = false;
                    }
                } else if (event.getAction() == DragEvent.ACTION_DROP && isInReceptacle) {
                    if (dragEnabled && !drillComplete) {
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
                    try {
                        if (draggedItems > targetItems) {
                            if (dragEnabled && !drillComplete) {
                                playSound(FetchResource.negativeAffirmation(THIS), placementRunnable);
                            }
                        } else {
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

    private void initialise(){
        try {
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            setupObjects();
            numbers = allData.getJSONArray("numerals");
            setupNumbers();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        String sound = allData.getString("help_monkey_pack");
                        playSound(sound, new Runnable() {
                            @Override
                            public void run() {
                                dragItems();
                            }
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }, 500);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void placeOnShelf(View view){
        try {
            ImageView destination = (ImageView) itemsReceptacle.getChildAt(draggedItems - 1);
            destination.setImageResource(itemResId);
            destination.setVisibility(View.VISIBLE);
            playSound(getNumberSound(), placementRunnable);
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

            List<Integer> keys = new ArrayList<>();
            List<Integer> values = new ArrayList<>();
            LinkedHashMap<Integer, Integer> imageMap = new LinkedHashMap<>();

            for(int i = 0; i < items.length();i++){
                JSONObject item = items.getJSONObject(i);
                int key = FetchResource.imageId(this, item, "image");
                int value = item.getInt("number");
                keys.add(key);
                values.add(value);
                targetItems += value;
            }

            int[] s = FisherYates.shuffle(keys.size());
            for (int i = 0; i < keys.size(); i++) {
                int si = s[i];
                int key = keys.get(si);
                int value = values.get(si);
                imageMap.put(key, value);
            }

            // Display metrics
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            float density = displayMetrics.density;

            // Layout
            MarginLayoutParams objectsLayout = (MarginLayoutParams) objectsContainer.getLayoutParams();
            objectsLayout.width = (int) (density * 305);
            objectsLayout.height = (int) (density * 600);
            objectsContainer.setLayoutParams(objectsLayout);

            // Setup squares
            int n = targetItems;
            int w = objectsLayout.width;
            int h = objectsLayout.height;

            SquarePacker squarePacker = new SquarePacker(w, h);
            Square[] squares = squarePacker.get(n);

            int count = 0;
            for (Map.Entry<Integer, Integer> entry : imageMap.entrySet()) {
                final int imageId = entry.getKey();
                int length = entry.getValue();
                for (int i = 0; i < length; i++) {
                    // Get square
                    Square square = squares[count++];
                    // Get drawable
                    Drawable d = getResources().getDrawable(imageId, null);
                    // Create image view
                    ImageView iv = new ImageView(getApplicationContext());
                    iv.setImageDrawable(d);
                    iv.setScaleX(0.8f);
                    iv.setScaleY(0.8f);
                    // iv.setBackgroundColor(Color.argb(150, 0, 0, 255));
                    // Add image view to numbers layout
                    objectsContainer.addView(iv);
                    // Edit image view layout params
                    RelativeLayout.LayoutParams ivParams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );
                    ivParams.leftMargin = 0;
                    ivParams.topMargin = 0;
                    ivParams.width = square.w;
                    ivParams.height = square.w;
                    iv.setLayoutParams(ivParams);
                    // Set coordinates
                    iv.setX((float) square.x);
                    iv.setY((float) square.y);
                    // Set image
                    iv.setVisibility(View.VISIBLE);
                    iv.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            itemResId = imageId;
                            return dragItem(v,event);
                        }
                    });
                }
            }
            // objectsContainer.setBackgroundColor(Color.argb(100, 255, 0, 0));
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private Runnable placementRunnable = new Runnable() {
        @Override
        public void run() {
            if (drillComplete && !endDrill) {
                endDrill = true;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playSound(FetchResource.positiveAffirmation(THIS), placementRunnable);
                    }
                }, 500);
            } else if (endDrill) {
                if (mp != null) {
                    mp.release();
                }
                finish();
            }
        }
    };

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
                // Completion
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

    private void dragItems(){
        try{
            String sound = allData.getString("drag_items_onto_shelf");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    dragEnabled = true;
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
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
            mp.release();
        }
        setResult(Globals.TO_MAIN);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
