package classact.com.xprize.activity.drill.math;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import classact.com.xprize.R;
import classact.com.xprize.common.Code;
import classact.com.xprize.common.Globals;
import classact.com.xprize.utils.FetchResource;
import classact.com.xprize.utils.FisherYates;
import classact.com.xprize.utils.RandomExcluding;
import classact.com.xprize.utils.ResourceSelector;

import android.os.Handler;

public class MathsDrillSevenAndOneActivity extends AppCompatActivity implements View.OnTouchListener, View.OnDragListener {
    private RelativeLayout itemsReceptacle;
    private ImageView filler1;
    private ImageView filler2;
    private ImageView filler3;
    private ImageView item1;
    private ImageView item2;
    private ImageView item3;
    private ImageView itemToFill;
    private ImageView item4;
    private ImageView item5;
    private ImageView item6;
    private MediaPlayer mp;
    private JSONObject allData;
    private android.os.Handler handler;

    private int missingValue;
    private int currentValue;

    private LinkedHashMap<ImageView, Integer> draggableViewValues;
    private SparseArray<ImageView> staticNumberViews;

    private SparseArray<NumberObject> numberObjects;

    private boolean dragEnabled;
    private boolean isDragging;

    private RelativeLayout parentView;
    private final Context THIS = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_drill_seven_and_one);
        parentView = (RelativeLayout) findViewById(R.id.activity_maths_drill_seven);

        handler = new Handler();
        itemsReceptacle = (RelativeLayout)findViewById(R.id.itemsReceptacle);
        initialiseData();
    }

    private void initialiseData(){
        try {
            String drillData = getIntent().getExtras().getString("data");
            allData = new JSONObject(drillData);
            setupNumberObjects();
            initialiseObjects();

            dragEnabled = false;
            isDragging = false;

            String sound = allData.getString("pattern_introduction_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayHelpMonkey();
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void initialiseObjects(){
        try {
            item1 = (ImageView)findViewById(R.id.item_one);
            item2 = (ImageView)findViewById(R.id.item_two);
            item3 = (ImageView)findViewById(R.id.item_three);
            item4 = (ImageView)findViewById(R.id.item_four);
            item5 = (ImageView)findViewById(R.id.item_five);
            item6 = (ImageView)findViewById(R.id.item_six);

            item1.setPadding(40, 40, 40, 40);
            RelativeLayout.LayoutParams item1LP = (RelativeLayout.LayoutParams) item1.getLayoutParams();
            item1LP.width = 230;
            item1LP.height = 230;
            item1LP.leftMargin = 170;
            item1LP.topMargin = 165;
            item1.setLayoutParams(item1LP);

            item2.setPadding(40, 40, 40, 40);
            RelativeLayout.LayoutParams item2LP = (RelativeLayout.LayoutParams) item2.getLayoutParams();
            item2LP.width = 285;
            item2LP.height = 230;
            item2LP.leftMargin = 450;
            item2LP.topMargin = 105;
            item2.setLayoutParams(item2LP);

            item3.setPadding(40, 40, 40, 40);
            RelativeLayout.LayoutParams item3LP = (RelativeLayout.LayoutParams) item3.getLayoutParams();
            item3LP.width = 290;
            item3LP.height = 230;
            item3LP.leftMargin = 760;
            item3LP.topMargin = 165;
            item3.setLayoutParams(item3LP);

            item4.setPadding(40, 40, 40, 40);
            RelativeLayout.LayoutParams item4LP = (RelativeLayout.LayoutParams) item4.getLayoutParams();
            item4LP.width = 290;
            item4LP.height = 240;
            item4LP.leftMargin = 1050;
            item4LP.topMargin = 280;
            item4.setLayoutParams(item4LP);

            item5.setPadding(40, 40, 40, 40);
            RelativeLayout.LayoutParams item5LP = (RelativeLayout.LayoutParams) item5.getLayoutParams();
            item5LP.width = 280;
            item5LP.height = 230;
            item5LP.leftMargin = 1340;
            item5LP.topMargin = 320;
            item5.setLayoutParams(item5LP);

            item6.setPadding(40, 40, 40, 40);
            RelativeLayout.LayoutParams item6LP = (RelativeLayout.LayoutParams) item6.getLayoutParams();
            item6LP.width = 310;
            item6LP.height = 225;
            item6LP.leftMargin = 1630;
            item6LP.topMargin = 220;
            item6.setLayoutParams(item6LP);

            staticNumberViews = new SparseArray<>();
            JSONArray items = allData.getJSONArray("completion_pieces");
            ImageView[] itemViews = {item1, item2, item3, item4, item5, item6};

            missingValue = -1;

            int numOfItems = items.length();
            int numOfItemViews = itemViews.length;

            int indexShift = 0;
            int lengthShift = numOfItemViews - numOfItems;

            if (numOfItems > numOfItemViews) {
                int midPointIndex = numOfItemViews/2;
                if (numOfItemViews % 2 == 0) {
                    midPointIndex -= 1;
                }

                for (int i = 0; i < numOfItems; i++) {
                    JSONObject item = items.getJSONObject(i);
                    if (item.getInt("isRight") == 1) {
                        if (i > midPointIndex) {
                            indexShift = numOfItems - numOfItemViews;
                            lengthShift = 0;
                        }
                        break;
                    }
                }

            }

            for (int i = indexShift; i < numOfItems + lengthShift; i++) {
                JSONObject item = items.getJSONObject(i);

                String itemImage = item.getString("image");
                int itemImageId = FetchResource.imageId(THIS, itemImage);
                final int itemValue = item.getInt("value");

                boolean isMissing = item.getInt("isRight") == 1;

                ImageView itemView = itemViews[i - indexShift];
                // itemView.setBackgroundColor(Color.argb(100, 255, 0, 0));
                staticNumberViews.put(itemValue, itemView);

                if (isMissing) {
                    itemView.setOnDragListener(this);
                    missingValue = itemValue;
                } else {
                    itemView.setImageResource(itemImageId);
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (dragEnabled) {
                                String numberSound = numberObjects.get(itemValue).getSound();
                                playSound(numberSound, null);
                            }
                        }
                    });
                }
            }

            if (missingValue == -1) {
                throw new Exception("No missing value found for pattern");
            }

            List<Integer> possibleNumbers = new ArrayList<>();
            possibleNumbers = RandomExcluding.nextInt(possibleNumbers, 0, missingValue, 21, 2);
            possibleNumbers.add(missingValue);

            draggableViewValues = new LinkedHashMap<>();

            filler1 = (ImageView)findViewById(R.id.numeral_1);
            filler2 = (ImageView)findViewById(R.id.numeral_2);
            filler3 = (ImageView)findViewById(R.id.numeral_3);

            ImageView[] fillerViews = {filler1, filler2, filler3};

            int numOfFillers = possibleNumbers.size();

            int[] s = FisherYates.shuffle(numOfFillers);

            for (int i = 0; i < numOfFillers; i++) {
                int si = s[i];
                int rndVal = possibleNumbers.get(si);
                String rndValImage = numberObjects.get(rndVal).getImage();
                int rndValImageId = FetchResource.imageId(THIS, rndValImage);

                ImageView fillerView = fillerViews[i];
                fillerView.setImageResource(rndValImageId);
                fillerView.setOnTouchListener(this);

                draggableViewValues.put(fillerView, rndVal);
            }
        }
        catch (Exception ex){
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private boolean isCorrectItem(){
        boolean isCorrectItem = false;
        try{
            if (currentValue == missingValue) {
                isCorrectItem = true;
            }
        }
        catch (Exception ex){
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
        return isCorrectItem;
    }

    public void placeItem() {
        try {
            ImageView iv = staticNumberViews.get(missingValue);
            String missingValueImage = numberObjects.get(missingValue).getImage();
            int missingValueImageId = FetchResource.imageId(THIS, missingValueImage);
            iv.setImageResource(missingValueImageId);
        } catch (Exception ex) {
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayHelpMonkey() {
        try {
            filler1.setVisibility(View.VISIBLE);
            filler2.setVisibility(View.VISIBLE);
            filler3.setVisibility(View.VISIBLE);
            String sound = allData.getString("help_monkey_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    sayDrag();
                }
            });
        } catch (Exception ex) {
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void sayDrag(){
        try{
            filler1.setVisibility(View.VISIBLE);
            filler2.setVisibility(View.VISIBLE);
            filler3.setVisibility(View.VISIBLE);
            String sound = allData.getString("drag_sound");
            playSound(sound, new Runnable() {
                @Override
                public void run() {
                    dragEnabled = true;
                }
            });
        }
        catch (Exception ex){
            Toast.makeText(THIS, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void end() {
        try{
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
                    currentValue = draggableViewValues.get(v);
                    String tag = (String) v.getTag();
                    ClipData.Item item = new ClipData.Item(tag);
                    ClipData dragData = new ClipData(tag, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                    View.DragShadowBuilder dragShadow = new View.DragShadowBuilder(v);
                    v.startDragAndDrop(dragData, dragShadow, v, 0);
                    v.setVisibility(View.INVISIBLE);
                    System.out.println("Turn invisible");
                    if (dragEnabled) {
                        String numberSound = numberObjects.get(currentValue).getSound();
                        playSound(numberSound, null);
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    if (!isDragging) {
                        v.setVisibility(View.VISIBLE);
                        System.out.println("Turning back visible (Touch-based)");
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

    @Override
    public boolean onDrag(View v, DragEvent event) {
        final int action = event.getAction();
        try {
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        isDragging = true;
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
                        if (!isCorrectItem()) {
                            if (mp != null) {
                                if (mp.isPlaying()) {
                                    int timeLeft = mp.getDuration() - mp.getCurrentPosition();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            playSound(FetchResource.negativeAffirmation(THIS), null);
                                        }
                                    }, timeLeft);
                                } else {
                                    playSound(FetchResource.negativeAffirmation(THIS), null);
                                }
                            }
                            return false;
                        }
                        return true;
                    } else {
                        return false;
                    }
                case DragEvent.ACTION_DRAG_ENDED:
                    isDragging = false;
                    if (event.getResult()) {
                        dragEnabled = false;
                        placeItem();
                        ImageView view = (ImageView) event.getLocalState();
                        view.setVisibility(View.INVISIBLE);
                        if (mp != null) {
                            if (mp.isPlaying()) {
                                int timeLeft = mp.getDuration() - mp.getCurrentPosition();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        end();
                                    }
                                }, timeLeft);
                            } else {
                                end();
                            }
                        }
                    } else {
                        ImageView view = (ImageView) event.getLocalState();
                        view.setVisibility(View.VISIBLE);
                        System.out.println("Turning back visible");
                        return false;
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
