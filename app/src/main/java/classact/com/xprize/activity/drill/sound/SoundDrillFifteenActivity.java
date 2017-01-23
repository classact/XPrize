package classact.com.xprize.activity.drill.sound;

import android.content.ClipData;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

import classact.com.xprize.R;
import classact.com.xprize.utils.ResourceSelector;

public class SoundDrillFifteenActivity extends AppCompatActivity {
    private ImageButton item1;
    private ImageButton item2;
    private ImageButton item3;
    private ImageButton item4;
    private ImageButton item5;
    private ImageButton item6;
    private ImageButton item7;
    private ImageButton item8;

    private LinearLayout container1;
    private LinearLayout container2;
    private LinearLayout container3;
    private LinearLayout container4;
    private LinearLayout container5;
    private LinearLayout container6;
    private LinearLayout container7;
    private LinearLayout container8;

    private LinearLayout receptacle1;
    private LinearLayout receptacle2;
    private LinearLayout receptacle3;
    private LinearLayout receptacle4;
    private LinearLayout receptacle5;
    private LinearLayout receptacle6;
    private LinearLayout receptacle7;
    private LinearLayout receptacle8;

    public boolean entered1;
    public boolean entered2;
    public boolean entered3;
    public boolean entered4;
    public boolean entered5;
    public boolean entered6;
    public boolean entered7;
    public boolean entered8;
    public boolean entered9;
    private int currentItem;
    public JSONArray drills;
    private int currentDrill;
    JSONArray words;
    private MediaPlayer mp;
    private int correctItems;
    private int totalPositions;
    private int filledPositions;
    private ImageButton[] currentOccupants;
    private boolean[] positionIsCorrect;
    private JSONObject allData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_fifteen);
        container1 = (LinearLayout) findViewById(R.id.container1);
        container2 = (LinearLayout) findViewById(R.id.container2);
        container3 = (LinearLayout) findViewById(R.id.container3);
        container4 = (LinearLayout) findViewById(R.id.container4);
        container5 = (LinearLayout) findViewById(R.id.container5);
        container6 = (LinearLayout) findViewById(R.id.container6);
        container7 = (LinearLayout) findViewById(R.id.container7);
        container8 = (LinearLayout) findViewById(R.id.container8);
        receptacle1 = (LinearLayout)findViewById(R.id.rloc1);
        receptacle2 = (LinearLayout)findViewById(R.id.rloc2);
        receptacle3 = (LinearLayout)findViewById(R.id.rloc3);
        receptacle4 = (LinearLayout)findViewById(R.id.rloc4);
        receptacle5 = (LinearLayout)findViewById(R.id.rloc5);
        receptacle6 = (LinearLayout)findViewById(R.id.rloc6);
        receptacle7 = (LinearLayout)findViewById(R.id.rloc7);
        receptacle8 = (LinearLayout)findViewById(R.id.rloc8);

        String drillData = getIntent().getExtras().getString("data");
        currentOccupants = new ImageButton[8];
        positionIsCorrect = new boolean[8];

        initialiseData(drillData);

    }

    private boolean placeToContainer(View view, int image){
        boolean placed = false;
        switch (image){
            case 1:
                if (container1.getChildCount() == 0){
                    container1.addView(view);
                    placed = true;
                }
                break;
            case 2:
                if (container2.getChildCount() == 0){
                    container2.addView(view);
                    placed = true;
                }
                break;
            case 3:
                if (container3.getChildCount() == 0){
                    container3.addView(view);
                    placed = true;
                }
                break;
            case 4:
                if (container4.getChildCount() == 0){
                    container4.addView(view);
                    placed = true;
                }
                break;
            case 5:
                if (container5.getChildCount() == 0){
                    container5.addView(view);
                    placed = true;
                }
                break;
            case 6:
                if (container6.getChildCount() == 0){
                    container6.addView(view);
                    placed = true;
                }
                break;
            case 7:
                if (container7.getChildCount() == 0){
                    container7.addView(view);
                    placed = true;
                }
                break;
            case 8:
                if (container8.getChildCount() == 0){
                    container8.addView(view);
                    placed = true;
                }
                break;
        }
        return placed;
    }
    private ImageButton createandPlaceImage(int word){
        ImageButton view = new ImageButton(this);
        //view.setText(word);
        //view.setTextSize(25);
        view.setImageResource(word);
        view.setBackgroundColor(0xf7be45);
        Random rand = new Random();
        int position = rand.nextInt(8);
        int i = 0;
        while(!placeToContainer(view,position)){
            i++;
            position = i;
        }
        return view;
    }

    private void initialiseHandlers(int item){
        switch (item) {
            case 1:
                item1.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        currentItem = 1;
                        return dragItem(v, event);
                    }
                });
                break;
            case 2:
                item2.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        currentItem = 2;
                        return dragItem(v, event);
                    }
                });
                break;
            case 3:
                item3.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        currentItem = 3;
                        return dragItem(v, event);
                    }
                });
                break;
            case 4:
                item4.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        currentItem = 4;
                        return dragItem(v, event);
                    }
                });
                break;
            case 5:
                item5.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        currentItem = 5;
                        return dragItem(v, event);
                    }
                });
                break;
            case 6:
                item6.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        currentItem = 6;
                        return dragItem(v, event);
                    }
                });
                break;
            case 7:
                item7.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        currentItem = 7;
                        return dragItem(v, event);
                    }
                });
                break;
            case 8:
                item8.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        currentItem = 8;
                        return dragItem(v, event);
                    }
                });
                break;
        }

    }

    private void initialiseData(String drillData){
        try{
            allData = new JSONObject(drillData);
            drills = allData.getJSONArray("sentences");
            currentDrill = 1;
            prepareDrill();
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public void resetItem(int item){
        try{
            JSONObject data = drills.getJSONObject(currentDrill -  1);
            words = data.getJSONArray("words");
            if (item == 1)
                item1.setImageResource(words.getJSONObject(0).getInt("word"));
            else if (item == 2)
                item2.setImageResource(words.getJSONObject(1).getInt("word"));
            else if (item == 3)
                item3.setImageResource(words.getJSONObject(2).getInt("word"));
            else if (item == 4)
                item4.setImageResource(words.getJSONObject(3).getInt("word"));
            else if (item == 5)
                item5.setImageResource(words.getJSONObject(4).getInt("word"));
            else if (item == 6)
                item6.setImageResource(words.getJSONObject(5).getInt("word"));
            else if (item == 7)
                item7.setImageResource(words.getJSONObject(6).getInt("word"));
            else if (item == 8)
                item8.setImageResource(words.getJSONObject(7).getInt("word"));
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }
    private ImageView createImageView(){
        ImageView view = new ImageView(this);
        view.setImageResource(R.drawable.line);
        return view;
    }

    public void prepareDrill(){
        try{
            JSONObject data = drills.getJSONObject(currentDrill -  1);
            int sentence = data.getInt("sentence");
            receptacle1.removeAllViews();
            receptacle1.addView(createImageView());
            receptacle2.removeAllViews();
            receptacle2.addView(createImageView());
            receptacle3.removeAllViews();
            receptacle3.addView(createImageView());
            receptacle4.removeAllViews();
            receptacle4.addView(createImageView());
            receptacle5.removeAllViews();
            receptacle5.addView(createImageView());
            receptacle6.removeAllViews();
            receptacle6.addView(createImageView());
            receptacle7.removeAllViews();
            receptacle7.addView(createImageView());
            receptacle8.removeAllViews();
            receptacle8.addView(createImageView());
            receptacle1.setVisibility(View.VISIBLE);
            receptacle2.setVisibility(View.VISIBLE);
            receptacle3.setVisibility(View.VISIBLE);
            receptacle4.setVisibility(View.VISIBLE);
            receptacle5.setVisibility(View.VISIBLE);
            receptacle6.setVisibility(View.VISIBLE);
            receptacle7.setVisibility(View.VISIBLE);
            receptacle8.setVisibility(View.VISIBLE);
            setupReceptacleOne();
            setupReceptacleTwo();
            setupReceptacleThree();
            setupReceptacleFour();
            setupReceptacleFive();
            setupReceptacleSix();
            setupReceptacleSeven();
            setupReceptacleEight();
            container1.removeAllViews();
            container2.removeAllViews();
            container3.removeAllViews();
            container4.removeAllViews();
            container5.removeAllViews();
            container6.removeAllViews();
            container7.removeAllViews();
            container8.removeAllViews();
            for(int i = sentence + 1; i < 10; i++){
                if (i == 1) {
                    receptacle1.setVisibility(View.INVISIBLE);
                }
                else if (i == 2) {
                    receptacle2.setVisibility(View.INVISIBLE);
                }
                else if (i == 3) {
                    receptacle3.setVisibility(View.INVISIBLE);
                }
                else if (i == 4) {
                    receptacle4.setVisibility(View.INVISIBLE);
                }
                else if (i == 5) {
                    receptacle5.setVisibility(View.INVISIBLE);
                }
                else if (i == 6) {
                    receptacle6.setVisibility(View.INVISIBLE);
                }
                else if (i == 7) {
                    receptacle7.setVisibility(View.INVISIBLE);
                }
                else if (i == 8) {
                    receptacle8.setVisibility(View.INVISIBLE);
                }
            }
            words = data.getJSONArray("words");
            if (words.length() > 0) {
                item1 = createandPlaceImage(words.getJSONObject(0).getInt("word"));
                initialiseHandlers(1);
            }
            if (words.length() > 1) {
                item2 = createandPlaceImage(words.getJSONObject(1).getInt("word"));
                initialiseHandlers(2);
            }
            if (words.length() > 2) {
                item3 = createandPlaceImage(words.getJSONObject(2).getInt("word"));
                initialiseHandlers(3);
            }
            if (words.length() > 3) {
                item4 = createandPlaceImage(words.getJSONObject(3).getInt("word"));
                initialiseHandlers(4);
            }
            if (words.length() > 4) {
                item5 = createandPlaceImage(words.getJSONObject(4).getInt("word"));
                initialiseHandlers(5);
            }
            if (words.length() > 5) {
                item6 = createandPlaceImage(words.getJSONObject(5).getInt("word"));
                initialiseHandlers(6);
            }
            if (words.length() > 6) {
                item7 = createandPlaceImage(words.getJSONObject(6).getInt("word"));
                initialiseHandlers(7);
            }
            if (words.length() > 7) {
                item8 = createandPlaceImage(words.getJSONObject(7).getInt("word"));
                initialiseHandlers(8);
            }
            resetEntered();
            correctItems = 0;
            totalPositions = sentence;
            correctItems = 0;
            filledPositions = 0;
            for(int i = 0; i < 8; i++) {
                currentOccupants[i] = null;
                positionIsCorrect[i] = false;
            }
            int sound = allData.getInt("drag_word_to_write");
            if (mp == null) {
                mp = MediaPlayer.create(this, sound);
            }
            else{
                Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
                mp.setDataSource(getApplicationContext(), myUri);
                mp.prepare();
            }
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    currentWord = 0;
                    playSentence();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private  int currentWord;
    private void playSentence(){
        try {
            if (currentWord < words.length()){
                //int sound = words.getJSONObject(currentWord).getInt("sound");
                JSONObject data = drills.getJSONObject(currentDrill -  1);
                int sentence = data.getInt("text");
                Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sentence);
                mp.setDataSource(getApplicationContext(), myUri);
                mp.prepare();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.reset();
                        currentWord++;
                        playSentence();
                    }
                });
                mp.start();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void resetEntered(){
        entered1 = false;
        entered2 = false;
        entered3 = false;
        entered4 = false;
        entered5 = false;
        entered6 = false;
        entered7 = false;
        entered8 = false;
    }

    private void setupReceptacleOne(){
        receptacle1.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                try {
                    int action = event.getAction();
                    if (action == DragEvent.ACTION_DRAG_ENTERED)
                        entered1 = true;
                    else if (action == DragEvent.ACTION_DRAG_EXITED)
                        entered1 = false;
                    else if (event.getAction() == DragEvent.ACTION_DROP && entered1) {
                        setReceptacleToButton(1);
                    }
                    else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED  && entered1) {
                        hideItem(1,event);
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                    finish();
                }
                return true;
            }
        });
    }

    private void setupReceptacleTwo(){
        receptacle2.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                try {
                    int action = event.getAction();
                    if (action == DragEvent.ACTION_DRAG_ENTERED)
                        entered2 = true;
                    else if (action == DragEvent.ACTION_DRAG_EXITED)
                        entered2 = false;
                    else if (event.getAction() == DragEvent.ACTION_DROP && entered2) {
                        setReceptacleToButton(2);
                    }
                    else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED  && entered2) {
                        hideItem(2,event);
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                    finish();
                }
                return true;
            }
        });
    }

    private void setupReceptacleThree(){
        receptacle3.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                try {
                    int action = event.getAction();
                    if (action == DragEvent.ACTION_DRAG_ENTERED)
                        entered3 = true;
                    else if (action == DragEvent.ACTION_DRAG_EXITED)
                        entered3 = false;
                    else if (event.getAction() == DragEvent.ACTION_DROP && entered3) {
                        setReceptacleToButton(3);
                    }
                    else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED  && entered3) {
                        hideItem(3,event);
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                    finish();
                }
                return true;
            }
        });
    }

    private void setupReceptacleFour(){
        receptacle4.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                try {
                    int action = event.getAction();
                    if (action == DragEvent.ACTION_DRAG_ENTERED)
                        entered4 = true;
                    else if (action == DragEvent.ACTION_DRAG_EXITED)
                        entered4 = false;
                    else if (event.getAction() == DragEvent.ACTION_DROP && entered4) {
                        setReceptacleToButton(4);
                    }
                    else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED  && entered4) {
                        hideItem(4,event);
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                    finish();
                }
                return true;
            }
        });
    }

    private void setupReceptacleFive(){
        receptacle5.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                try {
                    int action = event.getAction();
                    if (action == DragEvent.ACTION_DRAG_ENTERED)
                        entered5 = true;
                    else if (action == DragEvent.ACTION_DRAG_EXITED)
                        entered5 = false;
                    else if (event.getAction() == DragEvent.ACTION_DROP && entered5) {
                        setReceptacleToButton(5);
                    }
                    else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED  && entered5) {
                        hideItem(5,event);
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                    finish();
                }
                return true;
            }
        });
    }

    private void setupReceptacleSix(){
        receptacle6.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                try {
                    int action = event.getAction();
                    if (action == DragEvent.ACTION_DRAG_ENTERED)
                        entered6 = true;
                    else if (action == DragEvent.ACTION_DRAG_EXITED)
                        entered6 = false;
                    else if (event.getAction() == DragEvent.ACTION_DROP && entered6) {
                        setReceptacleToButton(6);
                    }
                    else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED  && entered6) {
                        hideItem(6,event);
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                    finish();
                }
                return true;
            }
        });
    }

    private void setupReceptacleSeven(){
        receptacle7.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                try {
                    int action = event.getAction();
                    if (action == DragEvent.ACTION_DRAG_ENTERED)
                        entered7 = true;
                    else if (action == DragEvent.ACTION_DRAG_EXITED)
                        entered7 = false;
                    else if (event.getAction() == DragEvent.ACTION_DROP && entered7) {
                        setReceptacleToButton(7);
                    }
                    else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED  && entered7) {
                        hideItem(7,event);
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                    finish();
                }
                return true;
            }
        });
    }

    private void setupReceptacleEight(){
        receptacle8.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                try {
                    int action = event.getAction();
                    if (action == DragEvent.ACTION_DRAG_ENTERED)
                        entered8 = true;
                    else if (action == DragEvent.ACTION_DRAG_EXITED)
                        entered8 = false;
                    else if (event.getAction() == DragEvent.ACTION_DROP && entered8) {
                        setReceptacleToButton(8);
                    }
                    else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED  && entered8) {
                        hideItem(8,event);
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                    finish();
                }
                return true;
            }
        });
    }


    public boolean isCorrectMatch(int receptacle){
        boolean result = false;
        try {
            int position = 0;
            JSONArray positions = words.getJSONObject(currentItem - 1).getJSONArray("positions");
            for(int i = 0; i < positions.length();i++) {
                position = positions.getInt(i);
                if (position == receptacle) {
                    result = true;
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
        return result;
    }

    private void hideItem(int receptacle, DragEvent event){
        try {
            ImageButton view = (ImageButton) event.getLocalState();
            view.setVisibility(View.INVISIBLE);
            if (currentOccupants[receptacle - 1] != null) {
                currentOccupants[receptacle - 1].setVisibility(View.VISIBLE);
            }
            currentOccupants[receptacle-1] = view;

        } catch (Exception ex) {
            ex.printStackTrace();
            finish();
        }
    }

    private void checkCompletion(){
        if (filledPositions == totalPositions  && correctItems == totalPositions) {
            playThisSound(ResourceSelector.getPositiveAffirmationSound(getApplicationContext()));
        }
        else if (filledPositions == totalPositions ){
            playThisSound(ResourceSelector.getNegativeAffirmationSound(getApplicationContext()));
            prepareDrill();
        }
    }

    private ImageButton createButton(int word){
        ImageButton button = new ImageButton (this);
        button.setImageResource(word);
        button.setBackgroundColor(0xf7be45);
        //button.setTextSize(20);
        return button;
    }
    private void setReceptacleToButton(int receptacle){
        try {

            if (receptacle == 1) {
                receptacle1.removeAllViews();
                receptacle1.addView(createButton(words.getJSONObject(currentItem - 1).getInt("word")));
            }
            else if (receptacle == 2) {
                receptacle2.removeAllViews();
                receptacle2.addView(createButton(words.getJSONObject(currentItem - 1).getInt("word")));
            }
            else if (receptacle == 3) {
                receptacle3.removeAllViews();
                receptacle3.addView(createButton(words.getJSONObject(currentItem - 1).getInt("word")));
            }
            else if (receptacle == 4) {
                receptacle4.removeAllViews();
                receptacle4.addView(createButton(words.getJSONObject(currentItem - 1).getInt("word")));
            }
            else if (receptacle == 5) {
                receptacle5.removeAllViews();
                receptacle5.addView(createButton(words.getJSONObject(currentItem - 1).getInt("word")));
            }
            else if (receptacle == 6) {
                receptacle6.removeAllViews();
                receptacle6.addView(createButton(words.getJSONObject(currentItem - 1).getInt("word")));
            }
            else if (receptacle == 7) {
                receptacle7.removeAllViews();
                receptacle7.addView(createButton(words.getJSONObject(currentItem - 1).getInt("word")));
            }
            else if (receptacle == 8) {
                receptacle8.removeAllViews();
                receptacle8.addView(createButton(words.getJSONObject(currentItem - 1).getInt("word")));
            }
            if (positionIsCorrect[receptacle - 1]){
                positionIsCorrect[receptacle - 1] = false;
                correctItems --;
            }
            if (isCorrectMatch(receptacle)) {
                correctItems ++;
                positionIsCorrect[receptacle - 1] = true;
            }
            if (currentOccupants[receptacle - 1] != null) {
                currentOccupants[receptacle - 1].setVisibility(View.VISIBLE);
                filledPositions--;
            }
            filledPositions++;

            checkCompletion();
        }
        catch(Exception ex){
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
            resetEntered();
            return true;
        } else {
            return false;
        }
    }

    private void playThisSound(int soundid){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundid);
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    if (currentDrill == 1) {
                        currentDrill = 2;
                        prepareDrill();
                    } else {
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
