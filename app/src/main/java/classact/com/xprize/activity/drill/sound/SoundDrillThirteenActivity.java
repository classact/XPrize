package classact.com.xprize.activity.drill.sound;

import android.content.ClipData;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

import classact.com.xprize.R;

public class SoundDrillThirteenActivity extends AppCompatActivity {
    private ImageView item1;
    private ImageView item2;
    private ImageView item3;
    private ImageView item4;
    private ImageView item5;
    private ImageView item6;
    private ImageView item7;
    private ImageView item8;

    private LinearLayout container1;
    private LinearLayout container2;
    private LinearLayout container3;
    private LinearLayout container4;
    private LinearLayout container5;
    private LinearLayout container6;
    private LinearLayout container7;
    private LinearLayout container8;

    private ImageView receptacle1;
    private ImageView receptacle2;
    private ImageView receptacle3;
    private ImageView receptacle4;
    private ImageView receptacle5;
    private ImageView receptacle6;
    private ImageView receptacle7;
    private ImageView receptacle8;

    public boolean entered1;
    public boolean entered2;
    public boolean entered3;
    public boolean entered4;
    public boolean entered5;
    public boolean entered6;
    public boolean entered7;
    public boolean entered8;
    private int currentItem;
    public JSONArray drills;
    private int currentDrill;
    JSONArray letters;
    private MediaPlayer mp;
    private int correctItems;
    private int totalPositions;
    private int filledPositions;
    private ImageView[] currentOccupants;
    private boolean[] positionIsCorrect;
    private JSONObject allData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_drill_thirteen);
        container1 = (LinearLayout) findViewById(R.id.container1);
        container2 = (LinearLayout) findViewById(R.id.container2);
        container3 = (LinearLayout) findViewById(R.id.container3);
        container4 = (LinearLayout) findViewById(R.id.container4);
        container5 = (LinearLayout) findViewById(R.id.container5);
        container6 = (LinearLayout) findViewById(R.id.container6);
        container7 = (LinearLayout) findViewById(R.id.container7);
        container8 = (LinearLayout) findViewById(R.id.container8);
        receptacle1 = (ImageView)findViewById(R.id.loc1);
        receptacle2 = (ImageView)findViewById(R.id.loc2);
        receptacle3 = (ImageView)findViewById(R.id.loc3);
        receptacle4 = (ImageView)findViewById(R.id.loc4);
        receptacle5 = (ImageView)findViewById(R.id.loc5);
        receptacle6 = (ImageView)findViewById(R.id.loc6);
        receptacle7 = (ImageView)findViewById(R.id.loc7);
        receptacle8 = (ImageView)findViewById(R.id.loc8);

        String drillData = getIntent().getExtras().getString("data");
        initialiseData(drillData);
        currentOccupants = new ImageView[8];
        positionIsCorrect = new boolean[8];
        currentDrill = 1;
        prepareDrill();
    }

    private void sayWord(){
        try {
            JSONObject data = drills.getJSONObject(currentDrill -  1);
            int sound = data.getInt("sound");
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + sound);
            mp.setDataSource(getApplicationContext(), myUri);
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
        }
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
    private ImageView createandPlaceImage(int resource){
        ImageView view = new ImageView(this);
        view.setImageResource(resource);
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
            drills = allData.getJSONArray("words");
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public void resetItem(int item){
        try{
            JSONObject data = drills.getJSONObject(currentDrill -  1);
            letters = data.getJSONArray("words");
            if (item == 1)
                item1.setImageResource(letters.getJSONObject(0).getInt("letter"));
            else if (item == 2)
                item2.setImageResource(letters.getJSONObject(1).getInt("letter"));
            else if (item == 3)
                item3.setImageResource(letters.getJSONObject(2).getInt("letter"));
            else if (item == 4)
                item4.setImageResource(letters.getJSONObject(3).getInt("letter"));
            else if (item == 5)
                item5.setImageResource(letters.getJSONObject(4).getInt("letter"));
            else if (item == 6)
                item6.setImageResource(letters.getJSONObject(5).getInt("letter"));
            else if (item == 7)
                item7.setImageResource(letters.getJSONObject(6).getInt("letter"));
            else if (item == 8)
                item8.setImageResource(letters.getJSONObject(7).getInt("letter"));
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    public void prepareDrill(){
        try{
            JSONObject data = drills.getJSONObject(currentDrill -  1);
            String word = data.getString("word");
            receptacle1.setImageResource(R.drawable.line);
            receptacle2.setImageResource(R.drawable.line);
            receptacle3.setImageResource(R.drawable.line);
            receptacle4.setImageResource(R.drawable.line);
            receptacle5.setImageResource(R.drawable.line);
            receptacle6.setImageResource(R.drawable.line);
            receptacle7.setImageResource(R.drawable.line);
            receptacle8.setImageResource(R.drawable.line);
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
            for(int i = word.length() + 1; i < 10; i++){
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
            letters = data.getJSONArray("letters");
            if (letters.length() > 0) {
                item1 = createandPlaceImage(letters.getJSONObject(0).getInt("letter"));
                initialiseHandlers(1);
            }
            if (letters.length() > 1) {
                item2 = createandPlaceImage(letters.getJSONObject(1).getInt("letter"));
                initialiseHandlers(2);
            }
            if (letters.length() > 2) {
                item3 = createandPlaceImage(letters.getJSONObject(2).getInt("letter"));
                initialiseHandlers(3);
            }
            if (letters.length() > 3) {
                item4 = createandPlaceImage(letters.getJSONObject(3).getInt("letter"));
                initialiseHandlers(4);
            }
            if (letters.length() > 4) {
                item5 = createandPlaceImage(letters.getJSONObject(4).getInt("letter"));
                initialiseHandlers(5);
            }
            if (letters.length() > 5) {
                item6 = createandPlaceImage(letters.getJSONObject(5).getInt("letter"));
                initialiseHandlers(6);
            }
            if (letters.length() > 6) {
                item7 = createandPlaceImage(letters.getJSONObject(6).getInt("letter"));
                initialiseHandlers(7);
            }
            if (letters.length() > 7) {
                item8 = createandPlaceImage(letters.getJSONObject(7).getInt("letter"));
                initialiseHandlers(8);
            }
            resetEntered();
            correctItems = 0;
            totalPositions = word.length();
            correctItems = 0;
            filledPositions = 0;
            for(int i = 0; i < 8; i++) {
                currentOccupants[i] = null;
                positionIsCorrect[i] = false;
            }
            if (currentDrill == 1){
                try {
                    int sound = allData.getInt("drag_the_letters_to_write");
                    mp = MediaPlayer.create(this, sound);
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.reset();
                            sayWord();
                        }
                    });
                }
                catch (Exception ex){
                    ex.printStackTrace();
                    finish();
                }

            }
            else {
                int sound = data.getInt("sound");
                playThisSound(sound);
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
                        setReceptacleToImage(1);
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
                        setReceptacleToImage(2);
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
                        setReceptacleToImage(3);
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
                        setReceptacleToImage(4);
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
                        setReceptacleToImage(5);
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
                        setReceptacleToImage(6);
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
                        setReceptacleToImage(7);
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
                        setReceptacleToImage(8);
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
            JSONArray positions = letters.getJSONObject(currentItem - 1).getJSONArray("positions");
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
            ImageView view = (ImageView) event.getLocalState();
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
            playPositiveSound(R.raw.good_job);
        }
        else if (filledPositions == totalPositions ){
            playNegativeSound(R.raw.uh_oh);
        }
    }
    private void setReceptacleToImage(int receptacle){
        try {

            if (receptacle == 1)
                receptacle1.setImageResource(letters.getJSONObject(currentItem - 1).getInt("letter"));
            else if (receptacle == 2)
                receptacle2.setImageResource(letters.getJSONObject(currentItem - 1).getInt("letter"));
            else if (receptacle == 3)
                receptacle3.setImageResource(letters.getJSONObject(currentItem - 1).getInt("letter"));
            else if (receptacle == 4)
                receptacle4.setImageResource(letters.getJSONObject(currentItem - 1).getInt("letter"));
            else if (receptacle == 5)
                receptacle5.setImageResource(letters.getJSONObject(currentItem - 1).getInt("letter"));
            else if (receptacle == 6)
                receptacle6.setImageResource(letters.getJSONObject(currentItem - 1).getInt("letter"));
            else if (receptacle == 7)
                receptacle7.setImageResource(letters.getJSONObject(currentItem - 1).getInt("letter"));
            else if (receptacle == 8)
                receptacle8.setImageResource(letters.getJSONObject(currentItem - 1).getInt("letter"));
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

    private void playPositiveSound(int soundid){
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
                    } else if (currentDrill == 2) {
                        currentDrill = 3;
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
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

    private void playNegativeSound(int soundid){
        try {
            Uri myUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + soundid);
            mp.setDataSource(getApplicationContext(), myUri);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    prepareDrill();
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
            finish();
        }
    }

//    @Override
//    public void onBackPressed() {
//    }

}
